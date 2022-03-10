package com.sap.sfsf.reshuffle.applicants.backend.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.cds.Result;
import com.sap.cds.ql.Select;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.cds.CdsCreateEventContext;
import com.sap.cds.services.cds.CdsReadEventContext;
import com.sap.cds.services.cds.CdsUpdateEventContext;
import com.sap.cds.services.cds.CqnService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.sfsf.reshuffle.applicants.backend.util.CandidateUtil;
import com.sap.sfsf.reshuffle.applicants.backend.util.exception.EmptyConfigException;
import com.sap.sfsf.reshuffle.applicants.backend.util.query.CandidateQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cds.gen.CdsModel_;
import cds.gen.reshuffleservice.Candidates;
import cds.gen.reshuffleservice.Candidates_;

@Component
@ServiceName("ReshuffleService")
public class CandidateService extends AbstractCandidateService implements EventHandler {
    private Logger logger = LoggerFactory.getLogger(CandidateService.class);

    @Autowired
    private PersistenceService db;

    @On(event = CqnService.EVENT_CREATE, entity = Candidates_.CDS_NAME)
    public void onCreate(CdsCreateEventContext context, Candidates candidate)
            throws ODataException, ServiceException, EmptyConfigException {

        CandidateUtil util = new CandidateUtil();
        String argsPositionId = candidate.getPositionID();

        createCandidateEntity(candidate);
        util.validateCandidate(candidate, argsPositionId);
    }

    @On(entity = Candidates_.CDS_NAME)
    public void onRead(CdsReadEventContext context) throws ODataException, ServiceException {
        CandidateQuery query = new CandidateQuery();
        query.validateParameters(context.getParameterInfo().getQueryParams());

        String caseId = query.getCaseId();
        final List<Map<String, Object>> result = new ArrayList<>();

        // For PUT requests, do not process the response.
        if (caseId != null) {
            List<Candidates> candidateList = getCandidates(caseId);

            for (final Candidates c : candidateList)
                result.add(c);
        }

        context.setResult(result);
    }

    @On(event = CqnService.EVENT_UPDATE, entity = Candidates_.CDS_NAME)
    public void onUpdate(CdsUpdateEventContext context, Candidates candidate)
            throws ODataException, ServiceException, EmptyConfigException {

        CandidateUtil util = new CandidateUtil();
        String argsPositionId = candidate.getPositionID();

        createCandidateEntity(candidate);
        util.validateCandidate(candidate, argsPositionId);
    }

    private List<Candidates> getCandidates(String caseId) {
        Result result = db
                .run(Select.from(CdsModel_.CANDIDATES).columns(c -> c._all()).where(c -> c.caseID().eq(caseId)));

        return result.listOf(Candidates.class);
    }
}
