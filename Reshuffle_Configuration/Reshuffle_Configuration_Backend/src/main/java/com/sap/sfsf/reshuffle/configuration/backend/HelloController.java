package com.sap.sfsf.reshuffle.configuration.backend;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.sfsf.reshuffle.configuration.backend.model.Config;
import com.sap.sfsf.reshuffle.configuration.backend.model.RateForm;
import com.sap.sfsf.reshuffle.configuration.backend.services.ConfigService;
import com.sap.sfsf.reshuffle.configuration.backend.util.ConfigReqBodyValidator;
import com.sap.sfsf.reshuffle.configuration.backend.util.Utils;

@Controller
@EnableAutoConfiguration
public class HelloController {
	Logger logger = LoggerFactory.getLogger(HelloController.class);

	@Autowired
	ConfigService configService;
	
	@GetMapping(value = "/config", produces = "application/json")
	@ResponseBody
	String getConfig() throws Exception {
		Config config = configService.getConfig();
		JSONObject json = config.getFixedDateConfig();

		return json.toString();
	}

	@PutMapping(value = "/config", produces = "application/json")
	@ResponseBody
	void postConfig(@RequestBody String reqStr) throws Exception {
		JSONObject jsonObj = new JSONObject(reqStr);
		
		ConfigReqBodyValidator cValidator = new ConfigReqBodyValidator(jsonObj);

		if(cValidator.isBadRequest() == true) {
			Optional<String> result = cValidator.getProblemList().stream().reduce(
					(accum, value) -> {
						return accum + ", "  + value;
					});
			logger.error("Config Validation Error(s) : " + result.orElse(""));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		} else {
			Config config = new Config();
			Utils utils = new Utils();
			
			String strDate = utils.getOptinalStringField(jsonObj, "startDateTime");
			config.setUnFixedStartDateTime(strDate);
			
			int span = utils.getOptionalIntField(jsonObj, "span");
			config.setSpan(span);
			int competencyThreshold = utils.getOptionalIntField(jsonObj, "competencyThreshold");
			config.setCompetencyThreshold(competencyThreshold);
			String rateFormKey1 = utils.getOptinalStringField(jsonObj, "rateFormKey1");
			String rateFormKey2 = utils.getOptinalStringField(jsonObj, "rateFormKey2");
			String rateFormKey3 = utils.getOptinalStringField(jsonObj, "rateFormKey3");
			String presidentName = utils.getOptinalStringField(jsonObj, "presidentName");
			String mailTemplate = utils.getOptinalStringField(jsonObj, "mailTemplate");
			config.setRateFormKey1(rateFormKey1);
			config.setRateFormKey2(rateFormKey2);
			config.setRateFormKey3(rateFormKey3);
			config.setPresidentName(presidentName);
			config.setMailTemplate(mailTemplate);
			
			configService.update(config);
			
			return;
		}
	}
	
	@GetMapping(value = "/rateform", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String rating() {
		List<RateForm> list = configService.getRateFormList();
		
		Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(list);
	}
	
	@GetMapping(value = "/jpconfig", produces = "application/json")
	@ResponseBody
	String getJpConfig() throws Exception {
		Config config = configService.getConfig();
		JSONObject json = config.getFixedDateConfig("jp");
		
		return json.toString();
	}

}
