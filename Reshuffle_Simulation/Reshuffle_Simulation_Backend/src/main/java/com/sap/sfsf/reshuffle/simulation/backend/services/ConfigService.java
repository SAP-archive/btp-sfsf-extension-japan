package com.sap.sfsf.reshuffle.simulation.backend.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.sfsf.reshuffle.simulation.backend.model.Config;
import com.sap.sfsf.reshuffle.simulation.backend.repository.ConfigRepository;
import com.sap.sfsf.reshuffle.simulation.backend.util.DateTimeUtil;

@Service
public class ConfigService {
	@Autowired
	private ConfigRepository configRepo;

	public String getStartDTStr() {
		Config config = configRepo.findOne();
		Date startDateTime = config.getStartDateTime();
		String startDT = DateTimeUtil.formatJobDateTokyo(startDateTime);
		return startDT;
	}

	public Date getStartDate() {
		Config config = configRepo.findOne();
		if (config != null) {
			return config.getStartDateTime();
		} else {
			return null;
		}
	}

}
