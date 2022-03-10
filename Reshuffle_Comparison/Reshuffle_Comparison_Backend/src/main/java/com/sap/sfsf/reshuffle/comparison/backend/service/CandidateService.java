package com.sap.sfsf.reshuffle.comparison.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sap.sfsf.reshuffle.comparison.backend.model.Candidate;
import com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.*;
import com.sap.sfsf.reshuffle.comparison.backend.repository.CandidateRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {
    Logger logger = LoggerFactory.getLogger(CandidateService.class);

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

    public List<String> findDistinctCaseIds() {
        Optional<List<String>> optCaseIdList = Optional.ofNullable(candidateRepository.findDistinctCaseIds());
        return optCaseIdList.orElse(null);
    }

    public List<CandidateDivision> findDistinctCandidateDivisions() {
        Optional<List<CandidateDivision>> optCandidateDivisionList = Optional.ofNullable(candidateRepository.findDistinctCandidateDivisions());
        return optCandidateDivisionList.orElse(null);
    }

    public List<CandidateDepartment> findDistinctCandidateDepartments() {
        Optional<List<CandidateDepartment>> optCandidateDepartmentList = Optional.ofNullable(candidateRepository.findDistinctCandidateDepartments());
        return optCandidateDepartmentList.orElse(null);
    }

    public List<CandidatePosition> findDistinctCandidatePositions() {
        Optional<List<CandidatePosition>> optCandidatePositionList = Optional.ofNullable(candidateRepository.findDistinctCandidatePositions());
        return optCandidatePositionList.orElse(null);
    }

    public List<Division> findDistinctDivisions() {
        Optional<List<Division>> optDivisionList = Optional.ofNullable(candidateRepository.findDistinctDivisions());
        return optDivisionList.orElse(null);
    }

    public List<Department> findDistinctDepartments() {
        Optional<List<Department>> optDepartmentList = Optional.ofNullable(candidateRepository.findDistinctDepartments());
        return optDepartmentList.orElse(null);
    }

    public List<Position> findDistinctPositions() {
        Optional<List<Position>> optPositionList = Optional.ofNullable(candidateRepository.findDistinctPositions());
        return optPositionList.orElse(null);
    }

    public List<Candidate> findByCaseid(String caseId) {
        Optional<List<Candidate>> optCandidateList = Optional.ofNullable(candidateRepository.findByCaseid(caseId));
        return optCandidateList.orElse(null);
    }

    public List<Candidate> findByCandidateid(String candidateId) {
        Optional<List<Candidate>> optCandidateList = Optional.ofNullable(candidateRepository.findByCandidateid(candidateId));
        return optCandidateList.orElse(null);
    }

    public void deleteCandidateByCaseId(List<Candidate> candidateList) {
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