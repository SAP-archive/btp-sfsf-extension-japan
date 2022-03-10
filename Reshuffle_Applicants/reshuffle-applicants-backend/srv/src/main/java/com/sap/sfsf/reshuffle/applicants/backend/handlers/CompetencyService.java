package com.sap.sfsf.reshuffle.applicants.backend.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.cds.Struct;
import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.cds.CdsReadEventContext;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cloud.sdk.datamodel.odata.helper.ExpressionFluentHelper;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.sfsf.reshuffle.applicants.backend.ODataRequest;
import com.sap.sfsf.reshuffle.applicants.backend.config.ConfigService;
import com.sap.sfsf.reshuffle.applicants.backend.util.exception.EmptyConfigException;
import com.sap.sfsf.reshuffle.applicants.backend.util.query.CompetencyQuery;
import com.sap.sfsf.vdm.namespaces.formheader.FormHeader;
import com.sap.sfsf.vdm.namespaces.positionentity.PositionEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cds.gen.reshuffleservice.Competencies;
import cds.gen.reshuffleservice.Competencies_;
import cds.gen.reshuffleservice.Configs;

@Component
@ServiceName("ReshuffleService")
public class CompetencyService implements EventHandler {
    private Logger logger = LoggerFactory.getLogger(FilterService.class);

    @Autowired
    private ConfigService configService;

    @Autowired
    private ODataRequest request;

    @On(entity = Competencies_.CDS_NAME)
    public void onRead(CdsReadEventContext context) throws ODataException, EmptyConfigException, ServiceException {
        // 1. create filters
        CompetencyQuery query = new CompetencyQuery();
        query.validateParameters(context.getParameterInfo().getQueryParams());
        ExpressionFluentHelper<PositionEntity> positionEntityFilter = query.createPositionEntityFilter();

        // 2. fetch Competencies
        List<PositionEntity> positionEntityList = request.requestPositionEntity(positionEntityFilter);

        List<String> competencyNameList = extractCompetencyNames(positionEntityList);

        // 3. fetch ratings
        Configs config = configService.getConfig();

        ExpressionFluentHelper<FormHeader> formHeaderFilter = FormHeader.FORM_TEMPLATE_ID
                .eq(Long.parseLong(config.getRateFormKey1()))
                .or(FormHeader.FORM_TEMPLATE_ID.eq(Long.parseLong(config.getRateFormKey2()))
                        .or(FormHeader.FORM_TEMPLATE_ID.eq(Long.parseLong(config.getRateFormKey3()))))
                .and(FormHeader.FORM_DATA_STATUS.eq(3L));

        List<FormHeader> formHeaderList = request.requestExpandFormHeader(formHeaderFilter);

        int competencyThreshold = config.getCompetencyThreshold();

        // 4. create return entity
        List<Competencies> competencyList = createCompetencyList(competencyNameList, formHeaderList,
                competencyThreshold);

        final List<Map<String, Object>> result = new ArrayList<>();

        for (final Competencies c : competencyList)
            result.add(c);

        context.setResult(result);
    }

    private List<String> extractCompetencyNames(List<PositionEntity> positionEntityList) throws ServiceException {
        ArrayList<String> competencyNames = new ArrayList<String>();

        for (final PositionEntity positionEntity : positionEntityList) {
            try {
                List<Map<String, Map<String, String>>> positionCompetencyMappingEntityList = positionEntity
                        .getCustomField("positionCompetencyMappings");

                for (final Map<String, Map<String, String>> c : positionCompetencyMappingEntityList) {
                    Map<String, String> competencyEntity = c.getOrDefault("competencyNav", null);
                    competencyNames.add(competencyEntity.getOrDefault("name_defaultValue", ""));
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new ServiceException(ErrorStatuses.BAD_REQUEST, "The parameter was invalid.");
            }
        }

        return competencyNames;
    }

    private List<Competencies> createCompetencyList(List<String> competencyNameList, List<FormHeader> formHeaderList,
            int competencyThreshold) {

        List<Competencies> retList = new ArrayList<>();

        for (final FormHeader formHeader : formHeaderList) {
            Map<String, String> formSubjects = new HashMap<>();
            Competencies competency = Struct.create(Competencies.class);

            try {
                formSubjects = formHeader.getCustomField("formSubject");
                String userId = formSubjects.getOrDefault("userId", "");
                String userName = formSubjects.getOrDefault("lastName", "") + " "
                        + formSubjects.getOrDefault("firstName", "");

                competency.setCurrentEmpID(userId);
                competency.setCurrentEmpName(userName);
            } catch (Exception e) {
                logger.warn(e.getMessage());
                continue;
            }

            int totalCompCount = 0;
            double beyondThresholdCompCount = 0;
            int competencyPercentage = 0;

            List<Map<String, List<Map<String, List<Map<String, Object>>>>>> pmReviewContentDetail = null;

            try {
                Map<String, List<Map<String, List<Map<String, List<Map<String, Object>>>>>>> formLastContent = formHeader
                        .getCustomField("formLastContent");
                pmReviewContentDetail = formLastContent.getOrDefault("pmReviewContentDetail", new ArrayList<>());
            } catch (Exception e) {
                logger.warn(e.getMessage());
                continue;
            }

            for (final Map<String, List<Map<String, List<Map<String, Object>>>>> p : pmReviewContentDetail) {
                List<Map<String, List<Map<String, Object>>>> competencySections = p.getOrDefault("competencySections",
                        new ArrayList<>());

                for (final Map<String, List<Map<String, Object>>> s : competencySections) {
                    List<Map<String, Object>> competencies = s.getOrDefault("competencies", new ArrayList<>());

                    for (final Map<String, Object> competencyMap : competencies) {
                        String userCompName = (String) competencyMap.getOrDefault("name", "");

                        for (final String name : competencyNameList) {
                            if (!name.equals(userCompName))
                                continue;

                            Map<String, String> officialRating = (Map<String, String>) competencyMap
                                    .getOrDefault("officialRating", new HashMap<>());
                            String ratingStr = officialRating.getOrDefault("rating", null);

                            if (ratingStr == null)
                                continue;
                            double rating = Double.parseDouble(ratingStr);

                            totalCompCount++;
                            if (rating >= competencyThreshold)
                                beyondThresholdCompCount++;
                            if (beyondThresholdCompCount != 0)
                                competencyPercentage = (int) ((beyondThresholdCompCount / totalCompCount) * 100);
                        }
                    }
                }
            }

            competency.setCompetencyPercentage(competencyPercentage);
            retList.add(competency);
        }

        return retList;
    }
}
