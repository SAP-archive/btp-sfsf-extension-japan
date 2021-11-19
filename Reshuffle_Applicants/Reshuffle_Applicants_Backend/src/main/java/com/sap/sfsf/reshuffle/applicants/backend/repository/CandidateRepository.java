package com.sap.sfsf.reshuffle.applicants.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sap.sfsf.reshuffle.applicants.backend.model.Candidate;
import com.sap.sfsf.reshuffle.applicants.backend.model.CandidateId;

@Service
public interface CandidateRepository extends CrudRepository<Candidate, CandidateId> {
	List<Candidate> findAll();

	@Nullable
	@Query("select distinct caseID from Candidate")
	List<String> findDistinctCaseId();

	@Nullable
	@Query("select c from Candidate c where c.caseID in :caseID")
    List<Candidate> findByCaseid(@Param(value="caseID")String caseID);
    
    @Transactional
    @Modifying
	@Query("delete from Candidate c where c.caseID in :caseID")
	void deleteBycaseId(@Param(value="caseID")String caseID);
}