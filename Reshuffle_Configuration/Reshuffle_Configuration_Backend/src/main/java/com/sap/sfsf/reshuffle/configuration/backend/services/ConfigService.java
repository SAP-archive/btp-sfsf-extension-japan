package com.sap.sfsf.reshuffle.configuration.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sap.cloud.sdk.odatav2.connectivity.FilterExpression;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQuery;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryBuilder;
import com.sap.cloud.sdk.odatav2.connectivity.ODataType;
import com.sap.sfsf.reshuffle.configuration.backend.exception.InvalidDBRowCountException;
import com.sap.sfsf.reshuffle.configuration.backend.model.Config;
import com.sap.sfsf.reshuffle.configuration.backend.model.RateForm;
import com.sap.sfsf.reshuffle.configuration.backend.repository.ConfigRepository;

@Service
@Transactional
public class ConfigService {
	Logger logger = LoggerFactory.getLogger(ConfigService.class);
	
	@Autowired
	private ConfigRepository configRepository;
	
	public Config getConfig() throws Exception {
		ArrayList<Config> confList = (ArrayList<Config>) configRepository.findAll();
		
		Config conf = null;
		if(confList.size() != 0 || confList.isEmpty() == false ) {
			conf = confList.get(0);
		} else {
			throw new InvalidDBRowCountException("Number of Config table row is not one.");
		}
		return conf;
	}
	
	public void update(Config config) {
		configRepository.deleteAll();
		configRepository.save(config);
	}
	
	public List<RateForm> getRateFormList() {
		FilterExpression filter = new FilterExpression("formTemplateType", "eq", ODataType.of("Review"));
		String[] selects = {"formTemplateId", "formTemplateName"};
		
		List<RateForm> list = null;
		ODataQuery query = ODataQueryBuilder
				.withEntity("odata/v2", "FormTemplate")
				.select(selects)
				.filter(filter)
				.build();
		
		logger.info("RateForm Query: " + query.toString());
		try {
			list = query.execute("SFSF_2nd")
					.asList(RateForm.class);
		} catch (ODataException e) {
			e.printStackTrace();
		}
		
		return list;
	}

}
