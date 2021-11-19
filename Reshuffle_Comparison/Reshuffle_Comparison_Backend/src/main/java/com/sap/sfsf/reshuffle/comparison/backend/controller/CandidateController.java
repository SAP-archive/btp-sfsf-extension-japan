package com.sap.sfsf.reshuffle.comparison.backend.controller;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.sfsf.reshuffle.comparison.backend.model.Candidate;
import com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.CurrentDepartment;
import com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.CurrentDivision;
import com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.CurrentPosition;
import com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.NextDepartment;
import com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.NextDivision;
import com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.NextPosition;
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
        List<Candidate> candidates = candidateService.findAll();
        Gson gson = new GsonBuilder().serializeNulls().create();
        
        return gson.toJson(candidates);
    }

    @GetMapping(value = "/{candidateid:.{1,1024}}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCandidatesByCandidateId(@PathVariable String candidateid) {
        List<Candidate> candidates = candidateService.findByCandidateid(candidateid);
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(candidates);
    }

    @GetMapping(value = "/caseids", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCandidateCaseids() {
        List<String> caseIds = candidateService.findDistinctCaseIds();
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(caseIds);
    }

    @GetMapping(value = "/currentdivisions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCandidateCurrentDivisions() {
        List<CurrentDivision> currentDivisions = candidateService.findDistinctCurrentDivisions();
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(currentDivisions);
    }

    @GetMapping(value = "/currentdepartments", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCandidateCurrentDepartments() {
        List<CurrentDepartment> currentDepartments = candidateService.findDistinctCurrentDepartments();
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(currentDepartments);
    }

    @GetMapping(value = "/currentpositions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCandidateCurrentPositions() {
        List<CurrentPosition> currentPositions = candidateService.findDistinctCurrentPositions();
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(currentPositions);
    }

    @GetMapping(value = "/nextdivisions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCandidateNextDivisions() {
        List<NextDivision> nextDivisions = candidateService.findDistinctNextDivisions();
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(nextDivisions);
    }

    @GetMapping(value = "/nextdepartments", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCandidateNextDepartments() {
        List<NextDepartment> nextDepartments = candidateService.findDistinctNextDepartments();
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(nextDepartments);
    }

    @GetMapping(value = "/nextpositions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getCandidateNextPositions() {
        List<NextPosition> nextPositions = candidateService.findDistinctNextPositions();
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(nextPositions);
    }
}