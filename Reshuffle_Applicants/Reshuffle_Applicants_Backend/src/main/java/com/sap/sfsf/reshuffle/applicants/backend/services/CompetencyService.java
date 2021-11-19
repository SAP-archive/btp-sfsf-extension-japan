package com.sap.sfsf.reshuffle.applicants.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.qos.logback.core.filter.Filter;

import com.sap.cloud.sdk.odatav2.connectivity.FilterExpression;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQuery;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryBuilder;
import com.sap.cloud.sdk.odatav2.connectivity.ODataType;
import com.sap.sfsf.reshuffle.applicants.backend.model.Config;
import com.sap.sfsf.reshuffle.applicants.backend.model.competencies.Competencies;
import com.sap.sfsf.reshuffle.applicants.backend.model.competencies.CompetencyResults;
import com.sap.sfsf.reshuffle.applicants.backend.model.competencies.CompetencySections;
import com.sap.sfsf.reshuffle.applicants.backend.model.competencies.FormSubject;
import com.sap.sfsf.reshuffle.applicants.backend.model.competencies.OfficialRating;
import com.sap.sfsf.reshuffle.applicants.backend.model.competencies.PmReviewContentDetail;
import com.sap.sfsf.reshuffle.applicants.backend.model.competencies.PositionCompetency;
import com.sap.sfsf.reshuffle.applicants.backend.model.competencies.PositionCompetencyMappings;
import com.sap.sfsf.reshuffle.applicants.backend.model.competencies.UserCompetency;
import com.sap.sfsf.reshuffle.applicants.backend.model.competencies.PmReviewContentDetail.PmReviewContentDetailContainer;
import com.sap.sfsf.reshuffle.applicants.backend.model.competencies.PositionCompetencyMappings.PositionCompetencyMappingsContainer;
import com.sap.sfsf.reshuffle.applicants.backend.util.EmptyConfigException;

@Service
public class CompetencyService {
    private Logger Logger = LoggerFactory.getLogger(CompetencyService.class);

    @Autowired
    ConfigService configService;

    public List<UserCompetency> getUserCompetencyList(String positionCode)
            throws IllegalArgumentException, ODataException, EmptyConfigException {
        List<PositionCompetency> positionCompList = getPositionCompetencyList(positionCode);
        List<String> positionCompetencyNames = extractCompetencyNames(positionCompList);

        Config config = configService.getConfig();
        int rateFormKey1 = Integer.parseInt(config.getRateFormKey1());
        int rateFormKey2 = Integer.parseInt(config.getRateFormKey2());
        int rateFormKey3 = Integer.parseInt(config.getRateFormKey3());
        int[] rateFormArr = {rateFormKey1, rateFormKey2, rateFormKey3};
        int competencyThreshold = config.getCompetencyThreshold();

        List<CompetencyResults> compResList = getCompetencyResultList(rateFormArr);
        List<UserCompetency> userCompetencyList = computeUserCompetencies(compResList, positionCompetencyNames, competencyThreshold);

        return userCompetencyList;
   } 

   private List<PositionCompetency> getPositionCompetencyList(String positionCode) throws IllegalArgumentException, ODataException {
        String[] selects = {
            "positionNav/externalName_defaultValue",
            "positionCompetencyMappings/competencyNav/name_defaultValue"
        };
        String[] expands = {
            "positionNav",
            "positionCompetencyMappings/competencyNav"
        };
        FilterExpression filter = new FilterExpression("positionNav/code", "eq", ODataType.of(positionCode));

        List<PositionCompetency> list = null;
        ODataQuery query = ODataQueryBuilder
            .withEntity("odata/v2", "PositionEntity")
            .select(selects)
            .expand(expands)
            .filter(filter)
            .build();
        Logger.info("PositionCompetency Query: " + query.toString());

        list = query.execute("SFSF")
                    .asList(PositionCompetency.class);
        return list;
   }

   private List<CompetencyResults> getCompetencyResultList(int[] rateFormArr) throws IllegalArgumentException, ODataException {
        String[] selects = {
            "formSubject/userId",
            "formSubject/lastName",
            "formSubject/firstName",
            "formLastContent/status",
            "formLastContent/pmReviewContentDetail/competencySections/competencies/name",
            "formLastContent/pmReviewContentDetail/competencySections/competencies/officialRating/rating"
        };
        String[] expands = {
            "formLastContent/pmReviewContentDetail/competencySections/competencies",
            "formLastContent/pmReviewContentDetail/competencySections/competencies/officialRating",
            "formSubject"
        };

        FilterExpression rateFilters = new FilterExpression("formTemplateId", "eq", ODataType.of(rateFormArr[0]));
        for(int i=1; i < rateFormArr.length; i++) {
            FilterExpression rateFilterLoop = new FilterExpression("formTemplateId", "eq", ODataType.of(rateFormArr[i]));
            rateFilters = rateFilters.or(rateFilterLoop);
        }
        FilterExpression statusFilter = new FilterExpression("formDataStatus", "eq", ODataType.of(3));

        List<CompetencyResults> list = null;
        ODataQuery query = ODataQueryBuilder
            .withEntity("odata/v2", "FormHeader")
            .select(selects)
            .expand(expands)
            .filter(rateFilters.and(statusFilter))
            .top(24)
            .build();
        Logger.info("FormHeader Query: " + query.toString());

        list = query.execute("SFSF")
            .asList(CompetencyResults.class);

        return list;
   }

   private List<String> extractCompetencyNames(List<PositionCompetency> positionCompList) {
        ArrayList<String> competencyNames = new ArrayList<String>();
        positionCompList.forEach(positionComp -> {
            PositionCompetencyMappingsContainer positionCompetencyMappingsContainer = positionComp
                    .getPositionCompetencyMappings();
            List<PositionCompetencyMappings> positionCompMaps = positionCompetencyMappingsContainer
                    .getPositionCompetencyMappingsList();
            positionCompMaps.forEach(positionCompMap -> {
                String competencyName = positionCompMap.getCompetencyNav().getName_defaultValue();
                competencyNames.add(competencyName);
            });
        });

        return competencyNames;
   }
   private List<UserCompetency> computeUserCompetencies(List<CompetencyResults> compResList, List<String> positionCompetencyNames, int competencyThreshold) {
       List<UserCompetency> userCompetencyList = new ArrayList<UserCompetency>();
       compResList.forEach(compRes -> {
           Optional<FormSubject> optFormSubject = Optional.ofNullable(compRes.getFormSubject());
           FormSubject formSubject = optFormSubject.orElse(null);
           if(formSubject != null) {
                String userId = formSubject.getUserId();
                String userName = formSubject.getLastName() + " " + formSubject.getFirstName();
                UserCompetency userCompetency = new UserCompetency();
                userCompetency.setCurrentEmpId(userId);
                userCompetency.setCurrentEmpName(userName);

                PmReviewContentDetailContainer pmReviewContentDetailContainer = compRes.getFormLastContent().getPmReviewContentDetail();
                List<PmReviewContentDetail> pmReviewContentDetailList = pmReviewContentDetailContainer
                        .getPmReviewContentDetailList();
                pmReviewContentDetailList.forEach(pmReviewContentDetail -> {
                    List<CompetencySections> compSectionsList = pmReviewContentDetail.getCompetencySections()
                            .getCompetencySectionsList();
                    compSectionsList.forEach(compSection -> {
                        List<Competencies> compList = compSection.getCompetencies().getCompetenciesList();
                        compList.forEach(comp -> {
                            String userCompName = comp.getName();
                            positionCompetencyNames.forEach(positionCompName -> {
                                if (positionCompName.equals(userCompName)) {
                                    Optional<OfficialRating> optOfficialRating = Optional.ofNullable(comp.getOfficialRating());
                                    OfficialRating officialRating = optOfficialRating.orElse(null);
                                    Optional<String> optRatingStr = officialRating != null
                                            ? Optional.ofNullable(officialRating.getRating())
                                            : null;
                                    String ratingStr = optRatingStr != null? optRatingStr.orElse(null): null;
                                    if (ratingStr != null) {
                                        double rating = Double.parseDouble(ratingStr);
                                        boolean isBeyondThreshold = rating >= competencyThreshold? true : false;
                                        userCompetency.addCompCount(isBeyondThreshold);
                                    }
                                }
                            });
                        });
                    });
                });
                userCompetencyList.add(userCompetency);
           }
       });
       return userCompetencyList;
   }
}
