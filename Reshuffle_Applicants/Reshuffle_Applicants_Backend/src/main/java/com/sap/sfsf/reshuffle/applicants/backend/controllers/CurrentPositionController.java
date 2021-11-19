package com.sap.sfsf.reshuffle.applicants.backend.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.sfsf.reshuffle.applicants.backend.model.ExCandidate;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.CandidateFilter;
import com.sap.sfsf.reshuffle.applicants.backend.services.CurrentPositionService;
import com.sap.sfsf.reshuffle.applicants.backend.util.EmptyConfigException;
import com.sap.sfsf.reshuffle.applicants.backend.util.validator.CurrentPositionReqParamsValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/currentpositions")
public class CurrentPositionController {
	Logger logger = LoggerFactory.getLogger(CurrentPositionController.class);

	@Autowired
	CurrentPositionService currentPositionService;

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String getSfsfCandidates(@RequestParam Map<String, String> params) throws ODataException, EmptyConfigException {
		long start = System.currentTimeMillis();

		CurrentPositionReqParamsValidator pValidator = new CurrentPositionReqParamsValidator(params);

		if (pValidator.isBadRequest() == true) {
			Optional<String> result = pValidator.getProblemList().stream().reduce((accum, value) -> {
				return accum + ", " + value;
			});
			logger.error("Filter Validation Error(s) : " + result.orElse(""));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		} else {
			CandidateFilter candidateFilter = new CandidateFilter(params);
			List<ExCandidate> list = null;

			list = currentPositionService.findAllFromSfsf(candidateFilter);
			Gson gson = new GsonBuilder().serializeNulls().create();

			long end = System.currentTimeMillis();
			logger.info("Exec Time: GET /candidates, " + (end - start) + "ms");

			return gson.toJson(list);
		}
	}	

}
