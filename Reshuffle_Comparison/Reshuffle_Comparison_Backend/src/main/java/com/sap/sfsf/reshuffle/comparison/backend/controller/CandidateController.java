package com.sap.sfsf.reshuffle.comparison.backend.controller;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.sfsf.reshuffle.comparison.backend.model.Candidate;
import com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.*;
import com.sap.sfsf.reshuffle.comparison.backend.service.CandidateService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/candidates")
public class CandidateController {
    Logger logger = LoggerFactory.getLogger(CandidateController.class);

    @Autowired
    CandidateService candidateService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCandidates() {
        logger.info("/candidates is called.");
        List<Candidate> candidates = candidateService.findAll();
        Gson gson = new GsonBuilder().serializeNulls().create();
        
        return gson.toJson(candidates);
    }

    @GetMapping(value = "/{candidateid:.{1,1024}}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCandidatesByCandidateId(@PathVariable String candidateid) {
        logger.info("/{candidateId} is called.");
        List<Candidate> candidates = candidateService.findByCandidateid(candidateid);
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(candidates);
    }

    @GetMapping(value = "/caseids", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCandidateCaseids() {
        logger.info("/caseids is called.");
        List<String> caseIds = candidateService.findDistinctCaseIds();
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(caseIds);
    }

    @GetMapping(value = "/candidatedivisions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCandidateDivisions() {
        logger.info("/candidatedivisions is called.");
        List<CandidateDivision> candidateDivisions = candidateService.findDistinctCandidateDivisions();
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(candidateDivisions);
    }

    @GetMapping(value = "/candidatedepartments", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCandidateDepartments() {
        logger.info("/candidatedepartments is called.");
        List<CandidateDepartment> candidateDepartments = candidateService.findDistinctCandidateDepartments();
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(candidateDepartments);
    }

    @GetMapping(value = "/candidatepositions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCandidatePositions() {
        logger.info("/candidatepositions is called.");
        List<CandidatePosition> candidatePositions = candidateService.findDistinctCandidatePositions();
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(candidatePositions);
    }

    @GetMapping(value = "/departments", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getDepartments() {
        logger.info("/departments is called.");
        List<Department> departments = candidateService.findDistinctDepartments();
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(departments);
    }

    @GetMapping(value = "/divisions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getDivisions() {
        logger.info("/divisions is called.");
        List<Division> divisions = candidateService.findDistinctDivisions();
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(divisions);
    }

    @GetMapping(value = "/positions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getPositions() {
        logger.info("/positions is called.");
        List<Position> positions = candidateService.findDistinctPositions();
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(positions);
    }
}