package com.sap.sfsf.reshuffle.simulation.backend.controller;

import java.util.Base64;
import java.util.List;

import com.sap.sfsf.reshuffle.simulation.backend.model.Candidate;
import com.sap.sfsf.reshuffle.simulation.backend.services.PDFService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PDFController {

    @Autowired
    PDFService pdfService;

    @RequestMapping(value = "/pdf", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ByteArrayResource> previewPDF(@RequestBody List<Candidate> list) {
        String content = pdfService.getPDFContent(list);
        byte[] contentBytes = Base64.getDecoder().decode(content);
        ByteArrayResource resource = new ByteArrayResource(contentBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=異動命令.pdf");
        //@formatter:off
		return ResponseEntity
				.ok()
				.headers(headers)
				.contentLength(contentBytes.length)
				.contentType(MediaType.APPLICATION_PDF)
				.body(resource);
		//@formatter:on
    }

}
