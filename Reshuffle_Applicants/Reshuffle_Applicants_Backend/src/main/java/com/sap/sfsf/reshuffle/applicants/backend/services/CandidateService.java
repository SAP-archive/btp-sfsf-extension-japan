package com.sap.sfsf.reshuffle.applicants.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sap.sfsf.reshuffle.applicants.backend.model.Candidate;
import com.sap.sfsf.reshuffle.applicants.backend.repository.CandidateRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {
    Logger logger = LoggerFactory.getLogger(CandidateService.class);

    @Autowired
    ConfigService configService;

    @Autowired
    private CandidateRepository candidateRepository;

    public List<Candidate> findAll() {
        List<Candidate> list = candidateRepository.findAll();
        return list;
    }

    public void save(Candidate candidate) {
        candidateRepository.save(candidate);
    }

    public void saveAll(List<Candidate> candidateList) {
        candidateRepository.saveAll(candidateList);
    }

    public List<String> findDistinctCaseId() {
        Optional<List<String>> optCaseIdList = Optional.ofNullable(candidateRepository.findDistinctCaseId());
        return optCaseIdList.orElse(null);
    }

    public List<Candidate> findByCaseid(String caseId) {
        Optional<List<Candidate>> optCandidateList = Optional.ofNullable(candidateRepository.findByCaseid(caseId));
        return optCandidateList.orElse(null);
    }

    public void deleteCandidateByCaseId(String caseId) {
        candidateRepository.deleteBycaseId(caseId);
    }

    public void deleteCandidatesByCaseId(List<Candidate> candidateList) {
        List<String> caseIdsList = new ArrayList<String>();
        for (int i = 0; i < candidateList.size(); i++) {
            caseIdsList.add(candidateList.get(i).getCaseID());
        }
        List<String> list = caseIdsList.stream().distinct().collect(Collectors.toList());
        for (int i = 0; i < list.size(); i++) {
            candidateRepository.deleteBycaseId(list.get(i));
        }
    }
}