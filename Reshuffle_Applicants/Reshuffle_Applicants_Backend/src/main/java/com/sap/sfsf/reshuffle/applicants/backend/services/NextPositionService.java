package com.sap.sfsf.reshuffle.applicants.backend.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.sap.cloud.sdk.odatav2.connectivity.FilterExpression;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQuery;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryBuilder;
import com.sap.sfsf.reshuffle.applicants.backend.config.EnvConfig;
import com.sap.sfsf.reshuffle.applicants.backend.model.Config;
import com.sap.sfsf.reshuffle.applicants.backend.model.ExEmpJob;
import com.sap.sfsf.reshuffle.applicants.backend.model.NextPosition;
import com.sap.sfsf.reshuffle.applicants.backend.model.Photo;
import com.sap.sfsf.reshuffle.applicants.backend.model.Rating;
import com.sap.sfsf.reshuffle.applicants.backend.model.Willingness;
import com.sap.sfsf.reshuffle.applicants.backend.model.details.DepartmentDetails;
import com.sap.sfsf.reshuffle.applicants.backend.model.details.DivisionDetails;
import com.sap.sfsf.reshuffle.applicants.backend.model.details.PayGradeDetails;
import com.sap.sfsf.reshuffle.applicants.backend.model.details.PositionDetails;
import com.sap.sfsf.reshuffle.applicants.backend.model.details.UserDetails;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.CurrentPositionFilter;
import com.sap.sfsf.reshuffle.applicants.backend.util.CustomFilterExpression;
import com.sap.sfsf.reshuffle.applicants.backend.util.DateTimeUtil;
import com.sap.sfsf.reshuffle.applicants.backend.util.EmptyConfigException;

@Service
public class NextPositionService extends CurrentPositionService {
		
	@Autowired
    ConfigService configService;
    
    @Autowired
	EnvConfig envConfig;
	
	//This method consists of 5 steps below...
	//  1. Get an extended-EmpJob list
	//  2. Get a Photo map
	//  3. Combine the Photo map to the extended-Empjob list
	//  4. Convert Extended-EmpJob list to a Candidate list
	//  5. Return the Candidate list
	public List<NextPosition> findAllfromSfsfNP(CurrentPositionFilter candidateFilter) throws ODataException, EmptyConfigException {
		List<ExEmpJob> exList = null;
		List<NextPosition> retList = new ArrayList<NextPosition>();

		exList = getExEmpJobList(candidateFilter, null);
		
		if(exList != null && exList.size() != 0) {
			String userIds = exList.stream()
					.map(exEmpJob -> exEmpJob.getUserId())
					.collect(Collectors.joining("\',\'"));

			Map<String, Photo> photoMap = getPhotoMap(userIds);

			exList.forEach(exEmpJob ->
			retList.add(convertToNextPosition(exEmpJob, null, photoMap, null, null))
					);
		}

		return retList;
	}

	@Override
	protected List<ExEmpJob> getExEmpJobList(CurrentPositionFilter candidateFilter, LocalDateTime startDate) throws ODataException, EmptyConfigException {
		String[] selects = {"userId", 
				"userNav/firstName", "userNav/lastName", 
				"startDate", "event",
				"department", "departmentNav/description_ja_JP", 
				"division", "divisionNav/description_ja_JP", 
				"managerId",
				"position", "positionNav/externalName_ja_JP", 
				"payGrade", "payGradeNav/name"};

		String[] expands = {"userNav", "departmentNav", "divisionNav", "positionNav"};

		LocalDateTime endDate = DateTimeUtil.getEndDate();
		Config config = configService.getConfig();
		Date configuredStartDate = config.getStartDateTime();
		int configuredSpan = config.getSpan();
		LocalDateTime nextStartDate = DateTimeUtil.toLocalDateTime(configuredStartDate);
		LocalDateTime nextEndDate = DateTimeUtil.getEndDate(nextStartDate, configuredSpan);

		//filter= 
		//  (
		//    (startDate >= nextStartDate && endDate <= nextEndDate)
		//    or
		//    (startDate >= nextStartDate && endDate == 9999/12/31)
		//  )
		//  and
		//  ( argumentsFilters )
		FilterExpression filters = null;
		CustomFilterExpression filterEndDate = new CustomFilterExpression("endDate", "eq", DateTimeUtil.getDateTimeFilter(endDate));
		CustomFilterExpression filterNextStartDate = new CustomFilterExpression("startDate", "ge", DateTimeUtil.getDateTimeFilter(nextStartDate));
		CustomFilterExpression filterNextEndDate = new CustomFilterExpression("endDate", "le", DateTimeUtil.getDateTimeFilter(nextEndDate));
		FilterExpression nextFilters1 = filterNextStartDate.and(filterNextEndDate);
		FilterExpression nextFilters2 = filterNextStartDate.and(filterEndDate);
		filters = nextFilters1.or(nextFilters2);	
		filters = candidateFilter != null? candidateFilter.addArgsFilter(filters): filters;	

		ODataQuery query = null;
		query = ODataQueryBuilder
				.withEntity("odata/v2", "EmpJob")
				.expand(expands)
				.select(selects)
				.filter(filters)
				.build();

		logger.info("Query:" + query.toString());

		return query.execute("SFSF_2nd")
				.asList(ExEmpJob.class);
	}

	protected NextPosition convertToNextPosition(ExEmpJob exEmpJob, LocalDateTime today, 
			Map<String, Photo> photoMap, Map<String, Willingness> willMap, Map<String, Rating> ratingMap) {

		String department = exEmpJob.getDepartment();
		String division = exEmpJob.getDivision();
		String managerId = exEmpJob.getManagerId();
		String position = exEmpJob.getPosition();
		String payGrade = exEmpJob.getPayGrade();
		String event = exEmpJob.getEvent();

		Optional<UserDetails> optUserDetails = Optional.ofNullable(exEmpJob.getUserDetails());
		Optional<DepartmentDetails> optDepartmentDetails = Optional.ofNullable(exEmpJob.getDepartmentDetails());
		Optional<DivisionDetails> optDivisionDetails = Optional.ofNullable(exEmpJob.getDivisionDetails());
		Optional<PositionDetails> optPositionDetails = Optional.ofNullable(exEmpJob.getPositionDetails());
		Optional<PayGradeDetails> optPayGradeDetails = Optional.ofNullable(exEmpJob.getPayGradeDetails());

		UserDetails userDetails = optUserDetails.orElse(null);
		DepartmentDetails departmentDetails = optDepartmentDetails.orElse(null);
		DivisionDetails divisionDetails = optDivisionDetails.orElse(null);
		PositionDetails positionDetails = optPositionDetails.orElse(null);
		PayGradeDetails payGradeDetails = optPayGradeDetails.orElse(null);

		String userId = exEmpJob.getUserId();
		String lastName = userDetails != null? userDetails.getLastName(): null;
		String firstName = userDetails != null? userDetails.getFirstName(): null;
		String departmentName = departmentDetails != null? departmentDetails.getDescription_ja_JP(): null;
		String divisionName = divisionDetails != null? divisionDetails.getDescription_ja_JP(): null;
		String positionName = positionDetails != null? positionDetails.getExternalName_ja_JP(): null;
		String payGradeName = payGradeDetails != null? payGradeDetails.getName(): null;

		String name = getName(lastName, firstName);
		
		String terminationCode = envConfig.getTerminationCode();
		String isRetire = event.equals(terminationCode)? "yes": "no";
		
		Photo photoIst = photoMap != null? photoMap.get(userId) : null;
		String rawBase64 = photoIst != null? photoIst.getPhoto(): null;
		String mimeType = photoIst != null? photoIst.getMimeType(): null;
		String contentType = mimeType != null? mimeType: MediaType.APPLICATION_OCTET_STREAM_VALUE;
		String escapeSeqRemoved = rawBase64.replaceAll("\\r\\n", "");
		String photo = "data:" + contentType + ";base64," + escapeSeqRemoved;

		return new NextPosition(
				userId, name, isRetire,
				division, department, managerId, position, payGrade,
				divisionName, departmentName, positionName, payGradeName,
				photo);
	}
}
