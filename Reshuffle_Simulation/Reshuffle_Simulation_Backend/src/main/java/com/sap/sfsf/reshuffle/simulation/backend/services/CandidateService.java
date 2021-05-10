package com.sap.sfsf.reshuffle.simulation.backend.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sap.sfsf.reshuffle.simulation.backend.model.Candidate;
import com.sap.sfsf.reshuffle.simulation.backend.repository.CandidateRepository;

@Service
public class CandidateService {
	
	private Logger LOG = LoggerFactory.getLogger(CandidateService.class);
	private Gson gson = new Gson();
	
	@Autowired
	CandidateRepository candidateRepo;

	public List<Candidate> findAll() {
		return candidateRepo.findAll();
	}

	public void deleteAll() {
		candidateRepo.deleteAll();
	}

	public void saveAll(List<Candidate> list) {
		candidateRepo.saveAll(list);
	}

	/**
	 * チェックステータス確認
	 * 
	 * @return
	 */
	public String checkStatus(List<Candidate> list) {
		long ngCnt = ngCnt(list);
		long okCnt = okCnt(list);
		long warnCnt = warnCnt(list);

		if (okCnt == list.size()) {
			return "OK";
		}
		if (ngCnt == 0 && warnCnt > 0) {
			return "WARN";
		}
		if (ngCnt > 0) {
			return "NG";
		}
		return "NG";
	}

	public long ngCnt(List<Candidate> list) {
		return list.stream().filter(c -> c.getCheckStatus().equals("NG")).count();
	}
	
	public long warnCnt(List<Candidate> list) {
		return list.stream().filter(c -> c.getCheckStatus().equals("WARN")).count();
	}
	
	public long okCnt(List<Candidate> list) {
		return list.stream().filter(c -> c.getCheckStatus().equals("OK")).count();
	}
	
	public Candidate findCheckedOne() {
		return candidateRepo.findCheckedOne();
	}

	@Transactional
	public List<Candidate> updateMailFlg(List<Candidate> list, String flg) {
		
		List<String> idList = new ArrayList<String>();
		for (Candidate c : list) {
			idList.add(c.getCandidateID());
		}

		List<Candidate> listForUpdate = candidateRepo.findByCandidateidIn(idList);
		Date now = new Date();
		for (Candidate c : listForUpdate) {
			c.setMailSentAt(now);
			c.setMailSentFlg(flg);
		}

		candidateRepo.saveAll(listForUpdate);
		return listForUpdate;
	}
	
	@Transactional
	public List<Candidate> updateUpsertFlg(List<Candidate> list, String flg) {
		List<String> idList = new ArrayList<String>();
		for (Candidate c : list) {
			idList.add(c.getCandidateID());
		}
		LOG.debug(gson.toJson(idList));
		List<Candidate> listForUpdate = candidateRepo.findByCandidateidIn(idList);
		Date now = new Date();
		for (Candidate c : listForUpdate) {
			c.setUpsertAt(now);
			c.setUpsertFlg(flg);
		}

		candidateRepo.saveAll(listForUpdate);
		return listForUpdate;
	}

}
