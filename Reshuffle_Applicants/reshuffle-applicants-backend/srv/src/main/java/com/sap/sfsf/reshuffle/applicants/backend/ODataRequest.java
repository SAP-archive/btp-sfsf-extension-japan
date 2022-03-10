package com.sap.sfsf.reshuffle.applicants.backend;

import java.util.List;

import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpDestination;
import com.sap.cloud.sdk.datamodel.odata.helper.ExpressionFluentHelper;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.sfsf.reshuffle.applicants.backend.config.EnvConfig;
import com.sap.sfsf.vdm.namespaces.custwillingness.Cust_transfer_willingness;
import com.sap.sfsf.vdm.namespaces.empjob.EmpJob;
import com.sap.sfsf.vdm.namespaces.fobusinessunit.FOBusinessUnit;
import com.sap.sfsf.vdm.namespaces.focompany.FOCompany;
import com.sap.sfsf.vdm.namespaces.fodepartment.FODepartment;
import com.sap.sfsf.vdm.namespaces.fodivision.FODivision;
import com.sap.sfsf.vdm.namespaces.formheader.FormHeader;
import com.sap.sfsf.vdm.namespaces.photo.Photo;
import com.sap.sfsf.vdm.namespaces.position.Position;
import com.sap.sfsf.vdm.namespaces.positionentity.PositionEntity;
import com.sap.sfsf.vdm.services.DefaultCustWillingnessService;
import com.sap.sfsf.vdm.services.DefaultEmpJobService;
import com.sap.sfsf.vdm.services.DefaultFOBusinessUnitService;
import com.sap.sfsf.vdm.services.DefaultFOCompanyService;
import com.sap.sfsf.vdm.services.DefaultFODepartmentService;
import com.sap.sfsf.vdm.services.DefaultFODivisionService;
import com.sap.sfsf.vdm.services.DefaultFormHeaderService;
import com.sap.sfsf.vdm.services.DefaultPhotoService;
import com.sap.sfsf.vdm.services.DefaultPositionEntityService;
import com.sap.sfsf.vdm.services.DefaultPositionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ODataRequest {
    private Logger logger = LoggerFactory.getLogger(ODataRequest.class);

    @Autowired
    private EnvConfig envConfig;

    public List<Position> requestPosition(ExpressionFluentHelper<Position> filter) throws ODataException {
        String destinationName = envConfig.getDestinationName();
        final HttpDestination destination = DestinationAccessor.getDestination(destinationName).asHttp();

        logger.info("Destination Name = " + destinationName);
        logger.info("Fetching Position entities...");

        return new DefaultPositionService()
                .withServicePath(envConfig.getServicePath())
                .getAllPosition()
                .select(
                        Position.DEPARTMENT,
                        Position.DIVISION,
                        Position.CODE,
                        () -> "departmentNav/name_ja_JP",
                        () -> "divisionNav/name_ja_JP",
                        Position.EXTERNAL_NAME_JA_JP,
                        Position.DEPARTMENT)
                .filter(filter)
                .executeRequest(destination);
    }

    public List<EmpJob> requestEmpJob(ExpressionFluentHelper<EmpJob> filter) throws ODataException {
        String destinationName = envConfig.getDestinationName();
        final HttpDestination destination = DestinationAccessor.getDestination(destinationName).asHttp();

        logger.info("Destination Name = " + destinationName);
        logger.info("Fetching EmpJob entities...");

        return new DefaultEmpJobService()
                .withServicePath(envConfig.getServicePath())
                .getAllEmpJob()
                .select(
                        EmpJob.USER_ID,
                        () -> "userNav/firstName",
                        () -> "userNav/lastName",
                        EmpJob.DIVISION,
                        () -> "divisionNav/name_ja_JP",
                        EmpJob.DEPARTMENT,
                        () -> "departmentNav/name_ja_JP",
                        EmpJob.POSITION,
                        () -> "positionNav/externalName_ja_JP",
                        EmpJob.PAY_GRADE,
                        () -> "payGradeNav/name",
                        EmpJob.START_DATE,
                        EmpJob.END_DATE,
                        EmpJob.MANAGER_ID)
                .filter(filter)
                .executeRequest(destination);
    }

    public List<Cust_transfer_willingness> requestWillingness(
            ExpressionFluentHelper<Cust_transfer_willingness> filter) throws ODataException {

        String destinationName = envConfig.getDestinationName();
        final HttpDestination destination = DestinationAccessor.getDestination(destinationName).asHttp();

        logger.info("Destination Name = " + destinationName);
        logger.info("Fetching Willingness entities...");

        return new DefaultCustWillingnessService()
                .withServicePath(envConfig.getServicePath())
                .getAllCust_transfer_willingness()
                .select(
                        Cust_transfer_willingness.USER,
                        Cust_transfer_willingness.CUST_WILLINGNESS)
                .filter(filter)
                .executeRequest(destination);
    }

    public List<FormHeader> requestFormHeader(ExpressionFluentHelper<FormHeader> filter) throws ODataException {
        String destinationName = envConfig.getDestinationName();
        final HttpDestination destination = DestinationAccessor.getDestination(destinationName).asHttp();

        logger.info("Destination Name = " + destinationName);
        logger.info("Fetching rating entities...");

        return new DefaultFormHeaderService()
                .withServicePath(envConfig.getServicePath())
                .getAllFormHeader()
                .select(
                        FormHeader.FORM_SUBJECT_ID,
                        FormHeader.RATING)
                .filter(filter)
                .executeRequest(destination);
    }

    public List<FormHeader> requestExpandFormHeader(ExpressionFluentHelper<FormHeader> filter) {
        String destinationName = envConfig.getDestinationName();
        final HttpDestination destination = DestinationAccessor.getDestination(destinationName).asHttp();

        logger.info("Destination Name = " + destinationName);
        logger.debug("Fetching FormHeader entities...");

        return new DefaultFormHeaderService()
                .withServicePath(envConfig.getServicePath())
                .getAllFormHeader()
                .select(
                        () -> "formSubject/userId",
                        () -> "formSubject/lastName",
                        () -> "formSubject/firstName",
                        () -> "formLastContent/status",
                        () -> "formLastContent/pmReviewContentDetail/competencySections/competencies/name",
                        () -> "formLastContent/pmReviewContentDetail/competencySections/competencies/officialRating/rating")
                .filter(filter)
                .top(24)
                .executeRequest(destination);
    }

    public List<PositionEntity> requestPositionEntity(ExpressionFluentHelper<PositionEntity> filter)
            throws ODataException {

        String destinationName = envConfig.getDestinationName();
        final HttpDestination destination = DestinationAccessor.getDestination(destinationName).asHttp();

        logger.info("Destination Name = " + destinationName);
        logger.info("Fetching PositionEntity entities...");

        return new DefaultPositionEntityService()
                .withServicePath(envConfig.getServicePath())
                .getAllPositionEntity()
                .select(
                        () -> "positionNav/externalName_defaultValue",
                        () -> "positionCompetencyMappings/competencyNav/name_defaultValue")
                .filter(filter)
                .executeRequest(destination);
    }

    public List<FOCompany> requestCompany(ExpressionFluentHelper<FOCompany> filter) {
        String destinationName = envConfig.getDestinationName();
        final HttpDestination destination = DestinationAccessor.getDestination(destinationName).asHttp();

        logger.info("Destination Name = " + destinationName);
        logger.info("Fetching Company entities...");

        return new DefaultFOCompanyService()
                .withServicePath(envConfig.getServicePath())
                .getAllFOCompany()
                .select(
                        FOCompany.EXTERNAL_CODE,
                        FOCompany.NAME_JA_JP)
                .filter(filter)
                .executeRequest(destination);
    }

    public List<FOBusinessUnit> requestBusinessUnit(ExpressionFluentHelper<FOBusinessUnit> filter) {
        String destinationName = envConfig.getDestinationName();
        final HttpDestination destination = DestinationAccessor.getDestination(destinationName).asHttp();

        logger.info("Destination Name = " + destinationName);
        logger.info("Fetching BusinessUnit entities...");

        return new DefaultFOBusinessUnitService()
                .withServicePath(envConfig.getServicePath())
                .getAllFOBusinessUnit()
                .select(
                        FOBusinessUnit.EXTERNAL_CODE,
                        FOBusinessUnit.NAME_JA_JP)
                .filter(filter)
                .executeRequest(destination);
    }

    public List<FODivision> requestDivision(ExpressionFluentHelper<FODivision> filter) {
        String destinationName = envConfig.getDestinationName();
        final HttpDestination destination = DestinationAccessor.getDestination(destinationName).asHttp();

        logger.info("Destination Name = " + destinationName);
        logger.info("Fetching Division entities...");

        return new DefaultFODivisionService()
                .withServicePath(envConfig.getServicePath())
                .getAllFODivision()
                .select(
                        FODivision.EXTERNAL_CODE,
                        FODivision.NAME_JA_JP,
                        () -> "cust_toBusinessUnit/externalCode")
                .filter(filter)
                .executeRequest(destination);
    }

    public List<FODepartment> requestDepartment(ExpressionFluentHelper<FODepartment> filter) {
        String destinationName = envConfig.getDestinationName();
        final HttpDestination destination = DestinationAccessor.getDestination(destinationName).asHttp();

        logger.info("Destination Name = " + destinationName);
        logger.info("Fetching Department entities...");

        return new DefaultFODepartmentService()
                .withServicePath(envConfig.getServicePath())
                .getAllFODepartment()
                .select(
                        FODepartment.EXTERNAL_CODE,
                        FODepartment.NAME_JA_JP,
                        () -> "cust_toDivision/externalCode")
                .filter(filter)
                .executeRequest(destination);
    }

    public List<Photo> requestPhoto(ExpressionFluentHelper<Photo> filter) throws ODataException {
        String destinationName = envConfig.getDestinationName();
        final HttpDestination destination = DestinationAccessor.getDestination(destinationName).asHttp();

        logger.info("Destination Name = " + destinationName);
        logger.info("Fetching Photo entities...");

        return new DefaultPhotoService()
                .withServicePath(envConfig.getServicePath())
                .getAllPhoto()
                .select(
                        Photo.USER_ID,
                        Photo.PHOTO,
                        Photo.MIME_TYPE)
                .filter(filter)
                .executeRequest(destination);
    }
}
