package com.sap.sfsf.reshuffle.applicants.backend.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.GsonBuilder;
import com.sap.sfsf.reshuffle.applicants.backend.model.Candidate;
import com.sap.sfsf.reshuffle.applicants.backend.util.DateTimeUtil;
import com.sap.sfsf.reshuffle.applicants.backend.util.PasswordUtil;
import com.sap.sfsf.reshuffle.applicants.backend.util.Utils;
import com.sap.sfsf.reshuffle.applicants.backend.util.validator.ExportReqParamsValidator;

import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;

@Controller
@RequestMapping("/export")
public class ExportController {
	Logger logger = LoggerFactory.getLogger(ExportController.class);
	private static String DEFAULTFILEPREFIX = "export";
	private static String REQKEYFILEPREFIX = "filename";
	private static String LOCALTIMEZONEID = "JST";
	private static String FILEENCODING = "UTF-8";

	@PostMapping(value ="")
	void export(@RequestParam Map<String, String> params, @RequestBody String req, HttpServletResponse res) {
		
		ExportReqParamsValidator eValidator = new ExportReqParamsValidator(params);
		
		if(eValidator.isBadRequest() == true) {
			Optional<String> result = eValidator.getProblemList().stream().reduce(
					(accum, value) -> {
						return accum + ", "  + value;
					});
			logger.error("Filter Validation Error(s) : " + result.orElse(""));
			
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);			
		} else {
			String password = PasswordUtil.generateCommonLangPassword();		
			
			OffsetDateTime now = OffsetDateTime.now(ZoneId.of(LOCALTIMEZONEID, ZoneId.SHORT_IDS));
			String filenameDate = DateTimeUtil.toStr(now, "yyyyMMddHHmmss");
			String filenamePrefix = params.get(REQKEYFILEPREFIX);
			filenamePrefix = filenamePrefix == null? DEFAULTFILEPREFIX: filenamePrefix;
			String filename = filenamePrefix + filenameDate;
			
			String contentDiscription = "attachment;filename=" + filename + ".zip";
			res.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
			res.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDiscription);
			res.setHeader("password", password);

			Utils utils = new Utils();
			List<Candidate> candidateList = utils.reqToCandidateListWithoutValidation(req);
			String candidatesJson = new GsonBuilder()
					.setDateFormat("yyyy/MM/dd HH:mm:ss")
					.serializeNulls()
					.create()
					.toJson(candidateList);

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
						res.getOutputStream(), password.toCharArray(), Charset.forName(FILEENCODING));
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
	}
}
