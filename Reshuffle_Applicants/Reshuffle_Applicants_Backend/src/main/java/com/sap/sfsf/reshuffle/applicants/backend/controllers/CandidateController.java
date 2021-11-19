package com.sap.sfsf.reshuffle.applicants.backend.controllers;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.sfsf.reshuffle.applicants.backend.model.Candidate;
import com.sap.sfsf.reshuffle.applicants.backend.services.CandidateService;
import com.sap.sfsf.reshuffle.applicants.backend.util.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/candidates")
public class CandidateController {
    Logger logger = LoggerFactory.getLogger(CandidateController.class);

    @Autowired
    CandidateService candidateService;

    @GetMapping("/{caseid:.{1,1024}}")
    @ResponseBody
    String getCandidatesByCaseId(@PathVariable String caseid) {
        List<Candidate> candidates = candidateService.findByCaseid(caseid);
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(candidates);
    }

    @DeleteMapping("/{caseid:.{1,1024}}")
    void deleteCandidateByCaseId(@PathVariable String caseid) {
        candidateService.deleteCandidateByCaseId(caseid);
        return;
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    void postCandidates(@RequestBody String req) {
        long start = System.currentTimeMillis();

        List<String> caseIds = candidateService.findDistinctCaseId();
        Utils utils = new Utils();

        List<Candidate> candidateList = utils.reqToCandidateListWithoutValidation(req);
        if (utils.confrimCandidateListAsNewCase(candidateList)) {
            utils.validateCandidateList(candidateList, caseIds);
            utils.setCreated(candidateList);
            utils.setModified(candidateList);
            candidateService.saveAll(candidateList);
        } else {
            utils.validateCandidateListForUpdating(candidateList, caseIds);
            utils.setModified(candidateList);
            candidateService.deleteCandidatesByCaseId(candidateList);
            candidateService.saveAll(candidateList);
        }

        long end = System.currentTimeMillis();
        logger.info("Exec Time: POST /candidates, " + (end - start) + "ms");

        return;
    }
}
