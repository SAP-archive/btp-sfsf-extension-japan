package com.sap.sfsf.reshuffle.applicants.backend.model.filters;

import java.util.List;

import lombok.Data;

@Data
public class AllFilters {
	List<CompanyFilter> companyList;
	List<BusinessUnitFilter> businessUnitList;
	List<DivisionFilter> divisionList;
	List<DepartmentFilter> departmentList;
	List<PositionFilter> positionList;
	List<RatingFilter> ratingList;
}
