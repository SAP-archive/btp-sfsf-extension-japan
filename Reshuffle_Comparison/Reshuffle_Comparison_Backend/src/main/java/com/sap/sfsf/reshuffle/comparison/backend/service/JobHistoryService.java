package com.sap.sfsf.reshuffle.comparison.backend.service;

import java.util.List;

import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpDestination;
import com.sap.cloud.sdk.datamodel.odata.client.exception.ODataException;
import com.sap.sfsf.reshuffle.comparison.backend.config.EnvConfig;
import com.sap.sfsf.vdm.namespaces.ecemployeeprofile.Background_InsideWorkExperience;
import com.sap.sfsf.vdm.services.DefaultECEmployeeProfileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobHistoryService  {
    Logger logger = LoggerFactory.getLogger(JobHistoryService.class);

    @Autowired
    private EnvConfig envConfig;

    public List<Background_InsideWorkExperience> getWorkHistoryByCandidateId(String candidateId) throws ODataException {
        String destinationName = envConfig.getDestinationName();
        final HttpDestination httpDestination =
            DestinationAccessor.getDestination(destinationName).asHttp();
    
        String servicePath = envConfig.getServicePath();
    
        DefaultECEmployeeProfileService employeeService = new DefaultECEmployeeProfileService();
        List<Background_InsideWorkExperience> workHistoryList = employeeService
            .withServicePath(servicePath)
            .getAllBackground_InsideWorkExperience()
            .filter(Background_InsideWorkExperience.USER_ID.eq(candidateId))
            .executeRequest(httpDestination);

        return workHistoryList;
    }

}