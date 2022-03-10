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

	public Candidate findByCaseidAndCandidateidIn(String caseId, String candidateId) {
		return candidateRepo.findByCaseidAndCandidateidIn(caseId, candidateId);
	}
	
	public List<String> findDistinctCaseid() {
		return candidateRepo.findDistinctCaseid();
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
		long applCnt = applCnt(list);
        long appdCnt = appdCnt(list);
        long denyCnt = denyCnt(list);

        if (okCnt == list.size()) {
            if (denyCnt > 0) {
                return "DENY";
            } else if (applCnt > 0) {
                return "APPL";
            } else if (appdCnt > 0) {
                return "APPD";
            } else {
                return "OK";
            }

        } else {
            if (ngCnt > 0 && warnCnt > 0) {
                return "WARN";
            } else if (ngCnt > 0) {
                return "NG";
            } else {
                return "";
            }
        }
	}

	public long ngCnt(List<Candidate> list) {
		return list.stream().filter(c -> "NG".equals(c.getSimulationCheckStatus())).count();
	}
	
	public long warnCnt(List<Candidate> list) {
		return list.stream().filter(c -> "WARN".equals(c.getSimulationCheckStatus())).count();
	}
	
	public long okCnt(List<Candidate> list) {
		return list.stream().filter(c -> "OK".equals(c.getSimulationCheckStatus())).count();
	}
	
	public long applCnt(List<Candidate> list) {
		return list.stream().filter(c -> "APPL".equals(c.getWfStatus())).count();
	}
	
	public long appdCnt(List<Candidate> list) {
		return list.stream().filter(c -> "APPD".equals(c.getWfStatus())).count();
	}
	
	public long denyCnt(List<Candidate> list) {
		return list.stream().filter(c -> "DENY".equals(c.getWfStatus())).count();
	}

    public Candidate findCheckedOne() {
		return candidateRepo.findCheckedOne();
	}
	
	public Candidate findCheckedOnebyCaseID(String caseID) {
		return candidateRepo.findCheckedOnebyCaseID(caseID);
	}


	@Transactional
	public List<Candidate> updateMailFlg(List<Candidate> list, String caseID, String flg) {
		
		List<String> idList = new ArrayList<String>();
		for (Candidate c : list) {
			idList.add(c.getCandidateID());
		}

		List<Candidate> listForUpdate = candidateRepo.findByCaseidAndCandidateidsIn(caseID, idList);
		Date now = new Date();
		for (Candidate c : listForUpdate) {
			c.setMailSentAt(now);
			c.setMailSentFlg(flg);
		}

		candidateRepo.saveAll(listForUpdate);
		return listForUpdate;
	}
	
	@Transactional
	public List<Candidate> updateUpsertFlg(List<Candidate> list, String casdID, String flg) {
		List<String> idList = new ArrayList<String>();
		for (Candidate c : list) {
			idList.add(c.getCandidateID());
		}
		LOG.debug(gson.toJson(idList));
		List<Candidate> listForUpdate = candidateRepo.findByCaseidAndCandidateidsIn(casdID, idList);
		Date now = new Date();
		for (Candidate c : listForUpdate) {
			c.setSfUpsertAt(now);
			c.setSfUpsertFlg(flg);
		}

		candidateRepo.saveAll(listForUpdate);
		return listForUpdate;
	}
	
	public List <Candidate> findByCaseid(String caseID) {
		List <Candidate> candidates = candidateRepo.findByCaseid(caseID);
		return candidates;
	}
	
	@Transactional
	public void approval (String caseID) {
        caseID = caseID.replace("\"","");
        List<Candidate> candidates = candidateRepo.findByCaseid(caseID);
		for (Candidate c : candidates) {
			c.setWfStatus("APPD");
		}
		candidateRepo.saveAll(candidates);
	} 

    @Transactional
	public void denial (String caseID) {
        caseID = caseID.replace("\"","");
        List<Candidate> candidates = candidateRepo.findByCaseid(caseID);
		for (Candidate c : candidates) {
			c.setWfStatus("DENY");
		}
		candidateRepo.saveAll(candidates);
	} 

}
