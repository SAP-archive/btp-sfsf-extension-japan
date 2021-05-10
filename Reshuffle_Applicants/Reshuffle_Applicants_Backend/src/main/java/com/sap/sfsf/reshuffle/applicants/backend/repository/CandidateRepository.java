package com.sap.sfsf.reshuffle.applicants.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.sap.sfsf.reshuffle.applicants.backend.model.Candidate;

@Service
public interface CandidateRepository extends CrudRepository<Candidate, String> {
	List<Candidate> findAll();
}