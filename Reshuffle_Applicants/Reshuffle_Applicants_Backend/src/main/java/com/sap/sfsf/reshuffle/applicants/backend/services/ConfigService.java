package com.sap.sfsf.reshuffle.applicants.backend.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.sfsf.reshuffle.applicants.backend.model.Config;
import com.sap.sfsf.reshuffle.applicants.backend.repository.ConfigRepository;
import com.sap.sfsf.reshuffle.applicants.backend.util.EmptyConfigException;

@Service
public class ConfigService {
	
	@Autowired
	private ConfigRepository configRepository;

	public Config getConfig() throws EmptyConfigException {
		ArrayList<Config> confList = (ArrayList<Config>) configRepository.findAll();
		
		Config conf = null;
		if(confList.size() != 0 || confList.isEmpty() == false ) {
			conf = confList.get(0);
		} else {
			throw new EmptyConfigException();
		}
		return conf;
	}
}
