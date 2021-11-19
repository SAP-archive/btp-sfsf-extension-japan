package com.sap.sfsf.reshuffle.comparison.backend.controller;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.cloud.sdk.datamodel.odata.client.exception.ODataException;
import com.sap.sfsf.reshuffle.comparison.backend.service.JobHistoryService;
import com.sap.sfsf.vdm.namespaces.ecemployeeprofile.Background_InsideWorkExperience;

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
@RequestMapping("/jobhistories")
public class JobHistoryController {
    Logger logger = LoggerFactory.getLogger(JobHistoryController.class);

    @Autowired
    JobHistoryService jobHistService;

    @GetMapping(value = "/{candidateid:.{1,1024}}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getJobHistoriesByCandidateId(@PathVariable String candidateid) throws ODataException {
        List<Background_InsideWorkExperience> workHistoryList = 
            jobHistService.getWorkHistoryByCandidateId(candidateid);

        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(workHistoryList);
    }
}