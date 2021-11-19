package com.sap.sfsf.reshuffle.comparison.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sap.sfsf.reshuffle.comparison.backend.model.Candidate;
import com.sap.sfsf.reshuffle.comparison.backend.model.CandidateId;
import com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.*;

@Service
public interface CandidateRepository extends CrudRepository<Candidate, CandidateId> {
	List<Candidate> findAll();

	@Nullable
	@Query("select distinct caseID from Candidate")
	List<String> findDistinctCaseIds();

    @Nullable
    @Query("select distinct new com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.CurrentDivision(c.currentDivision, c.currentDivisionName) from Candidate c")
    List<CurrentDivision> findDistinctCurrentDivisions();

    @Nullable
    @Query("select distinct new com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.CurrentDepartment(c.currentDepartment, c.currentDepartmentName) from Candidate c")
    List<CurrentDepartment> findDistinctCurrentDepartments();

    @Nullable
    @Query("select distinct new com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.CurrentPosition(c.currentPosition, c.currentPositionName) from Candidate c")
    List<CurrentPosition> findDistinctCurrentPositions();

    @Nullable
    @Query("select distinct new com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.NextDivision(c.nextDivision, c.nextDivisionName) from Candidate c")
    List<NextDivision> findDistinctNextDivisions();

    @Nullable
    @Query("select distinct new com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.NextDepartment(c.nextDepartment, c.nextDepartmentName) from Candidate c")
    List<NextDepartment> findDistinctNextDepartments();

    @Nullable
    @Query("select distinct new com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.NextPosition(c.nextPosition, c.nextPositionName) from Candidate c")
    List<NextPosition> findDistinctNextPositions();

    @Nullable
	@Query("select c from Candidate c where c.caseID in :caseID")
    List<Candidate> findByCaseid(@Param(value="caseID")String caseID);

    @Nullable
    @Query("select c from Candidate c where c.candidateID in :candidateID")
    List<Candidate> findByCandidateid(@Param(value="candidateID")String candidateID);
    
    @Transactional
    @Modifying
	@Query("delete from Candidate c where c.caseID in :caseID")
	void deleteBycaseId(@Param(value="caseID")String caseID);
}