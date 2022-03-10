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
    @Query("select distinct new com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.CandidateDivision(c.candidateDivisionID, c.candidateDivisionName) from Candidate c")
    List<CandidateDivision> findDistinctCandidateDivisions();

    @Nullable
    @Query("select distinct new com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.CandidateDepartment(c.candidateDepartmentID, c.candidateDepartmentName) from Candidate c")
    List<CandidateDepartment> findDistinctCandidateDepartments();

    @Nullable
    @Query("select distinct new com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.CandidatePosition(c.candidatePositionID, c.candidatePositionName) from Candidate c")
    List<CandidatePosition> findDistinctCandidatePositions();

    @Nullable
    @Query("select distinct new com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.Division(c.divisionID, c.divisionName) from Candidate c")
    List<Division> findDistinctDivisions();

    @Nullable
    @Query("select distinct new com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.Department(c.departmentID, c.departmentName) from Candidate c")
    List<Department> findDistinctDepartments();

    @Nullable
    @Query("select distinct new com.sap.sfsf.reshuffle.comparison.backend.model.candidateDto.Position(c.positionID, c.positionName) from Candidate c")
    List<Position> findDistinctPositions();

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