package com.sap.sfsf.reshuffle.simulation.backend.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.GsonBuilder;
import com.sap.sfsf.reshuffle.simulation.backend.model.Candidate;
import com.sap.sfsf.reshuffle.simulation.backend.services.CandidateService;
import com.sap.sfsf.reshuffle.simulation.backend.services.FilterService;
import com.sap.sfsf.reshuffle.simulation.backend.util.DateTimeUtil;
import com.sap.sfsf.reshuffle.simulation.backend.util.PasswordUtil;

import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;

@Controller
public class ExportController {
	@Autowired
	FilterService filterService;

	@Autowired
	CandidateService candidateService;

	@RequestMapping(value = "/export", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	void export(HttpServletResponse response) {
		String password = PasswordUtil.generateCommonLangPassword();
		Date now = new Date();
		String fileName = "download_" + DateTimeUtil.formatDateTokyoZip(now) + ".zip";
		response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
		response.setHeader("password", password);

		List<Candidate> candidates = candidateService.findAll();
		String candidatesJson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").serializeNulls().create()
				.toJson(candidates);

		File file = new File("export_"+ DateTimeUtil.formatDateTokyoZip(now) + ".csv");
		try {
			JSONArray array = new JSONArray(candidatesJson);

			String csv = CDL.toString(array);
			FileUtils.writeStringToFile(file, csv, Charset.forName("UTF-8"));

			ZipParameters params = new ZipParameters();
			params.setEncryptFiles(true);
			params.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
			params.setFileNameInZip(file.getName());
			params.setEntrySize(file.length());

			ZipOutputStream zos = new ZipOutputStream(response.getOutputStream(), password.toCharArray(),
					Charset.forName("UTF-8"));
			zos.putNextEntry(params);

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
