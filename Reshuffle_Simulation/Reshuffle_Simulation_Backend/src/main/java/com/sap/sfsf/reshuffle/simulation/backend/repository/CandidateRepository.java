package com.sap.sfsf.reshuffle.simulation.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sap.sfsf.reshuffle.simulation.backend.model.Candidate;
import com.sap.sfsf.reshuffle.simulation.backend.model.CandidateId;

@Repository
public interface CandidateRepository extends CrudRepository<Candidate, CandidateId> {
	List<Candidate> findAll();

	@Query("select count(*) from Candidate c where c.simulationCheckStatus = 'NG'")
	long countNG();
	
	@Query("select count(*) from Candidate c where c.simulationCheckStatus = 'WARN'")
	long countWARN();
	
	@Query(nativeQuery = true, value = "select top 1 * from Candidate c where c.simulationCheckDatetime is not null")
	Candidate findCheckedOne();
	
	@Query(nativeQuery = true, value = "select top 1 * from Candidate c where c.caseID = :caseID and c.simulationCheckDatetime is not null")
	Candidate findCheckedOnebyCaseID(@Param(value="caseID")String caseID);

	@Query("select c from Candidate c where c.candidateID in :list")
	List<Candidate> findByCandidateidIn(@Param(value="list")List<String> idList);

	@Query("select c from Candidate c where c.caseID in :caseID and c.candidateID in :candidateID")
	Candidate findByCaseidAndCandidateidIn(@Param(value="caseID")String caseID, @Param(value="candidateID")String candidateID);

	@Query("select c from Candidate c where c.caseID = :caseID and c.candidateID in :list")
	List<Candidate> findByCaseidAndCandidateidsIn(@Param(value="caseID")String caseID, @Param(value="list")List<String> idList);

	@Query("select c from Candidate c where c.caseID in :caseID")
	List<Candidate> findByCaseid(@Param(value="caseID")String caseID);

	@Query("select distinct caseID from Candidate")
	List<String> findDistinctCaseid();
    
}
