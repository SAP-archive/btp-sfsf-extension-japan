package com.sap.sfsf.reshuffle.simulation.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sap.sfsf.reshuffle.simulation.backend.model.Candidate;

@Repository
public interface CandidateRepository extends CrudRepository<Candidate, String> {
	List<Candidate> findAll();

	@Query("select count(*) from Candidate c where c.checkStatus = 'NG'")
	long countNG();
	
	@Query("select count(*) from Candidate c where c.checkStatus = 'WARN'")
	long countWARN();

	@Query(nativeQuery = true, value = "select top 1 * from Candidate c where c.CHECKDATETIME is not null")
	Candidate findCheckedOne();

	@Query("select c from Candidate c where c.candidateID in :list")
	List<Candidate> findByCandidateidIn(@Param(value="list")List<String> idList);
}
