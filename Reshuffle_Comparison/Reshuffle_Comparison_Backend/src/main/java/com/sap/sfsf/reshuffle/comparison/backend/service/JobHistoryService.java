package com.sap.sfsf.reshuffle.comparison.backend.service;

import java.util.List;

import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpDestination;
import com.sap.cloud.sdk.datamodel.odata.client.exception.ODataException;
import com.sap.sfsf.vdm.namespaces.ecemployeeprofile.Background_InsideWorkExperience;
import com.sap.sfsf.vdm.services.DefaultECEmployeeProfileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobHistoryService  {
    Logger logger = LoggerFactory.getLogger(JobHistoryService.class);

    public List<Background_InsideWorkExperience> getWorkHistoryByCandidateId(String candidateId) throws ODataException {
        String destinationName = System.getenv("DEST_NAME");
        final HttpDestination httpDestination =
            DestinationAccessor.getDestination(destinationName).asHttp();
    
        String servicePath = System.getenv("ODATA_SERVICE_PATH");
        System.out.println("=============================");
        System.out.println("service path: " + servicePath);
    
        DefaultECEmployeeProfileService employeeService = new DefaultECEmployeeProfileService();
        List<Background_InsideWorkExperience> workHistoryList = employeeService
            .withServicePath(servicePath)
            .getAllBackground_InsideWorkExperience()
            .filter(Background_InsideWorkExperience.USER_ID.eq(candidateId))
            .executeRequest(httpDestination);

        return workHistoryList;
    }

}
