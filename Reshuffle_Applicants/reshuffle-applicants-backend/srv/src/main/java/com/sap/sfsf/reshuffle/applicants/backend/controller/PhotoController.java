package com.sap.sfsf.reshuffle.applicants.backend.controller;

import java.util.List;

import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.sfsf.reshuffle.applicants.backend.ODataRequest;
import com.sap.sfsf.reshuffle.applicants.backend.util.exception.EmptyConfigException;
import com.sap.sfsf.vdm.namespaces.photo.Photo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v4/ReshuffleService/Photos")
public class PhotoController {
    Logger logger = LoggerFactory.getLogger(FileExportController.class);

    @Autowired
    private ODataRequest request;

    @ResponseBody
    @RequestMapping(value = "/{empID}", method = { RequestMethod.GET })
    public HttpEntity<byte[]> export(@PathVariable String empID) throws ODataException, EmptyConfigException {

        List<Photo> photoList = request.requestPhoto(Photo.USER_ID.eq(empID));

        Photo photo = photoList.get(0);

        byte[] photoBinary = photo.getPhoto();
        
        HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);		
		headers.setContentLength(photoBinary.length);
		return new HttpEntity<byte[]>(photoBinary, headers);
    }
}
