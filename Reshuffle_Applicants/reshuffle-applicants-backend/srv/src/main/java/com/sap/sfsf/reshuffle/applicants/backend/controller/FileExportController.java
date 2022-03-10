package com.sap.sfsf.reshuffle.applicants.backend.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.GsonBuilder;
import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cloud.sdk.datamodel.odata.helper.ExpressionFluentHelper;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.sfsf.reshuffle.applicants.backend.ODataRequest;
import com.sap.sfsf.reshuffle.applicants.backend.config.ConfigService;
import com.sap.sfsf.reshuffle.applicants.backend.model.FileExportEntityModel;
import com.sap.sfsf.reshuffle.applicants.backend.model.FileExportModel;
import com.sap.sfsf.reshuffle.applicants.backend.util.CandidateUtil;
import com.sap.sfsf.reshuffle.applicants.backend.util.DateTimeUtil;
import com.sap.sfsf.reshuffle.applicants.backend.util.PasswordUtil;
import com.sap.sfsf.reshuffle.applicants.backend.util.Util;
import com.sap.sfsf.reshuffle.applicants.backend.util.exception.EmptyConfigException;
import com.sap.sfsf.vdm.namespaces.custwillingness.Cust_transfer_willingness;
import com.sap.sfsf.vdm.namespaces.empjob.EmpJob;
import com.sap.sfsf.vdm.namespaces.formheader.FormHeader;
import com.sap.sfsf.vdm.namespaces.position.Position;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cds.gen.reshuffleservice.Candidates;
import cds.gen.reshuffleservice.Configs;
import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;

@RestController
@RequestMapping(path = "/api/v4/ReshuffleService/exportFile")
public class FileExportController {
    Logger logger = LoggerFactory.getLogger(FileExportController.class);
    private static final String DEFAULTFILEPREFIX = "export";
    private static final String LOCALTIMEZONEID = "JST";
    private static final String FILEENCODING = "UTF-8";
    private static int STRUL = 50;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ODataRequest request;

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/zip")
    @ResponseStatus(HttpStatus.CREATED)
    public void export(@RequestBody FileExportModel req, HttpServletResponse res)
            throws ODataException, EmptyConfigException {
        String plainPassword = PasswordUtil.generateCommonLangPassword();

        // determine filename
        OffsetDateTime now = OffsetDateTime.now(ZoneId.of(LOCALTIMEZONEID, ZoneId.SHORT_IDS));
        String filenameDate = DateTimeUtil.toStr(now, "yyyyMMddHHmmss");
        String filenamePrefix = req.getFilename();

        if (filenamePrefix == null || filenamePrefix.isEmpty())
            filenamePrefix = DEFAULTFILEPREFIX;
        else if (filenamePrefix.length() > STRUL)
            throw new ServiceException(ErrorStatuses.BAD_REQUEST,
                    "The file name should be no more than " + STRUL + " characters.");

        String filename = filenamePrefix + filenameDate;

        // set Response Header
        String contentDiscription = "attachment;filename=" + filename + ".zip";
        res.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDiscription);
        res.setHeader("password", plainPassword);
        res.setContentType("application/zip");

        // create CandidateList
        List<FileExportEntityModel> entities = req.getEntities();
        Configs config = configService.getConfig();

        List<String> candidateIdList = new ArrayList<>();
        List<String> incumbentIdList = new ArrayList<>();
        List<String> positionIdList = new ArrayList<>();

        for (final FileExportEntityModel entity : entities) {
            candidateIdList.add(entity.getCandidateID());
            incumbentIdList.add(entity.getIncumbentEmpID());
            positionIdList.add(entity.getPositionID());
        }

        Map<String, EmpJob> candidateEmpJobList = fetchEmpJob(candidateIdList);
        Map<String, EmpJob> incumbentEmpJobList = fetchEmpJob(incumbentIdList);
        Map<String, Position> positionList = fetchPosition(positionIdList);
        Map<String, BigDecimal> rating1List = fetchRating(candidateIdList, config.getRateFormKey1());
        Map<String, BigDecimal> rating2List = fetchRating(candidateIdList, config.getRateFormKey2());
        Map<String, BigDecimal> rating3List = fetchRating(candidateIdList, config.getRateFormKey3());
        Map<String, Boolean> willingnessList = fetchWillingness(candidateIdList);

        List<Candidates> candidateList = new ArrayList<>();

        for (final FileExportEntityModel entity : entities) {
            final Candidates candidate = Candidates.create();
            candidate.setCandidateID(entity.getCandidateID());
            candidate.setIncumbentEmpID(entity.getIncumbentEmpID());
            candidate.setPositionID(entity.getPositionID());

            createCandidateEntity(candidate, candidateEmpJobList, incumbentEmpJobList, positionList, rating1List,
                    rating2List, rating3List, willingnessList);
            candidateList.add(candidate);
        }

        String candidatesJson = new GsonBuilder()
                .setDateFormat("yyyy/MM/dd HH:mm:ss")
                .serializeNulls()
                .create()
                .toJson(candidateList);

        // create File
        String csvname = filename + ".csv";
        File file = new File(csvname);

        try {
            JSONArray array = new JSONArray(candidatesJson);
            String csv = CDL.toString(array);
            FileUtils.writeStringToFile(file, csv, Charset.forName(FILEENCODING));

            ZipParameters resParams = new ZipParameters();
            resParams.setEncryptFiles(true);
            resParams.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
            resParams.setFileNameInZip(file.getName());
            resParams.setEntrySize(file.length());

            ZipOutputStream zos = new ZipOutputStream(
                    res.getOutputStream(), plainPassword.toCharArray(), Charset.forName(FILEENCODING));
            zos.putNextEntry(resParams);

            InputStream inputStream = new FileInputStream(file);
            StreamUtils.copy(inputStream, zos);

            zos.flush();
            zos.closeEntry();
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, EmpJob> fetchEmpJob(List<String> userIdList) throws ODataException {
        Map<String, EmpJob> empJobMap = new HashMap<>();
        Util util = new Util();

        Map<Integer, ExpressionFluentHelper<EmpJob>> filters = util.createFilterMap(EmpJob.USER_ID, userIdList);

        for (final ExpressionFluentHelper<EmpJob> f : filters.values()) {
            List<EmpJob> tmpEmpJobList = request.requestEmpJob(f);

            for (final EmpJob e : tmpEmpJobList)
                empJobMap.put(e.getUserId(), e);
        }

        return empJobMap;
    }

    private Map<String, Position> fetchPosition(List<String> positionIdList) throws ODataException {
        Map<String, Position> positionMap = new HashMap<>();
        Util util = new Util();

        Map<Integer, ExpressionFluentHelper<Position>> filters = util.createFilterMap(Position.CODE, positionIdList);

        for (final ExpressionFluentHelper<Position> f : filters.values()) {
            List<Position> tmpPositionList = request.requestPosition(f);

            for (final Position p : tmpPositionList)
                positionMap.put(p.getCode(), p);
        }

        return positionMap;
    }

    private Map<String, BigDecimal> fetchRating(List<String> userIdList, String formTemplateId)
            throws ODataException {

        Map<String, BigDecimal> ratingMap = new HashMap<>();
        Util util = new Util();

        Map<Integer, ExpressionFluentHelper<FormHeader>> filters = util
                .createFilterMap(FormHeader.FORM_SUBJECT_ID, userIdList);

        for (final ExpressionFluentHelper<FormHeader> f : filters.values()) {
            ExpressionFluentHelper<FormHeader> filter = f
                    .and(FormHeader.FORM_TEMPLATE_ID.eq(Long.parseLong(formTemplateId)));

            List<FormHeader> tmpFormHeaderList = request.requestFormHeader(filter);

            for (final FormHeader h : tmpFormHeaderList)
                ratingMap.put(h.getFormSubjectId(), h.getRating());
        }

        return ratingMap;
    }

    private Map<String, Boolean> fetchWillingness(List<String> userIdList) throws ODataException {
        Map<String, Boolean> willingnessMap = new HashMap<>();
        Util util = new Util();

        Map<Integer, ExpressionFluentHelper<Cust_transfer_willingness>> filters = util
                .createFilterMap(Cust_transfer_willingness.USER, userIdList);

        for (final ExpressionFluentHelper<Cust_transfer_willingness> f : filters.values()) {
            List<Cust_transfer_willingness> tmpWillingnessList = request.requestWillingness(f);

            for (final Cust_transfer_willingness c : tmpWillingnessList)
                willingnessMap.put(c.getUser(), c.getCust_willingness());
        }

        return willingnessMap;
    }

    private void createCandidateEntity(Candidates candidate, Map<String, EmpJob> candidateEmpJobList,
            Map<String, EmpJob> incumbentEmpJobList, Map<String, Position> positionList,
            Map<String, BigDecimal> rating1List, Map<String, BigDecimal> rating2List, Map<String, BigDecimal> rating3List,
            Map<String, Boolean> willingnessList)
            throws ODataException, EmptyConfigException {

        CandidateUtil util = new CandidateUtil();

        String caseId = candidate.getCaseID();
        String candidateId = candidate.getCandidateID();
        String incumbentEmpId = candidate.getIncumbentEmpID();
        String positionId = candidate.getPositionID();

        EmpJob candidateEmpJob = candidateEmpJobList.get(candidateId);
        EmpJob incumbentEmpJob = incumbentEmpJobList.get(incumbentEmpId);
        Position position = positionList.get(positionId);

        if (candidateEmpJob == null && incumbentEmpJob == null && position == null)
            throw new ServiceException(ErrorStatuses.BAD_REQUEST, "There were no contents.");

        BigDecimal rating1 = rating1List.get(candidateId);
        BigDecimal rating2 = rating2List.get(candidateId);
        BigDecimal rating3 = rating3List.get(candidateId);
        String willingness = null;

        if (willingnessList.get(candidateId) != null) {
            if (willingnessList.get(candidateId))
                willingness = "yes";
            else
                willingness = "no";
        }

        util.createCandidateEntity(candidate, caseId, candidateEmpJob, incumbentEmpJob, position, rating1, rating2,
                rating3, willingness);
    }
}
