package com.sap.sfsf.reshuffle.simulation.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.sfsf.reshuffle.simulation.backend.model.Candidate;
import com.sap.sfsf.reshuffle.simulation.backend.model.History;
import com.sap.sfsf.reshuffle.simulation.backend.repository.HistoryRepository;

@Service
public class HistoryService {
	
	@Autowired
	HistoryRepository historyRepo;
	
	@Autowired
	CandidateService candidateService;


	public History saveHistory(List<Candidate> list) {
		long ngCnt = list.stream().filter(c -> c.getSimulationCheckStatus().equals("NG")).count();
		long okCnt = list.stream().filter(c -> c.getSimulationCheckStatus().equals("OK")).count();
		long warnCnt = list.stream().filter(c -> c.getSimulationCheckStatus().equals("WARN")).count();

		History history = new History();
		String status = candidateService.checkStatus(list);
		history.setStatus(status);
		history.setTotalCnt((long)list.size());
		history.setCheckedAt(list.get(0).getSimulationCheckDatetime());
		history.setNgCnt(ngCnt);
		history.setOkCnt(okCnt);
		history.setWarnCnt(warnCnt);
		
		return historyRepo.save(history);
	}


	public History findLatestOne() {
		return historyRepo.findLatesOne();
	}
}
