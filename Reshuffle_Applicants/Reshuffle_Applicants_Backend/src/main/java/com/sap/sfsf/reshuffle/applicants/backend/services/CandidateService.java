package com.sap.sfsf.reshuffle.applicants.backend.services;

import java.util.List;

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

    public void saveAll(List<Candidate> candidateList) {
        candidateRepository.saveAll(candidateList);
    }
}