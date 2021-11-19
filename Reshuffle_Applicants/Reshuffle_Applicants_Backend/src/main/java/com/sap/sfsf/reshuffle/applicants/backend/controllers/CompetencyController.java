package com.sap.sfsf.reshuffle.applicants.backend.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.sfsf.reshuffle.applicants.backend.model.competencies.UserCompetency;
import com.sap.sfsf.reshuffle.applicants.backend.services.CompetencyService;
import com.sap.sfsf.reshuffle.applicants.backend.util.EmptyConfigException;
import com.sap.sfsf.reshuffle.applicants.backend.util.validator.CompetencyReqParamsValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/userpositionprecisions")
public class CompetencyController {
	Logger logger = LoggerFactory.getLogger(CompetencyController.class);

	@Autowired
	CompetencyService competencyService;

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	String GetCompetency(@RequestParam Map<String, String> params) throws IllegalArgumentException, ODataException, EmptyConfigException {
		long start = System.currentTimeMillis();

		CompetencyReqParamsValidator pValidator = new CompetencyReqParamsValidator(params);

		if (pValidator.isBadRequest() == true) {
			Optional<String> result = pValidator.getProblemList().stream().reduce((accum, value) -> {
				return accum + ", " + value;
			});
			logger.error("Filter Validation Error(s) : " + result.orElse(""));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			String positionCode = params.get("currentPosition");
			List<UserCompetency> list = competencyService.getUserCompetencyList(positionCode);
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();

			long end = System.currentTimeMillis();
			logger.info("Exec Time: GET /competency, " + (end - start) + "ms");

			return gson.toJson(list);
		}
	}
}