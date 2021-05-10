package com.sap.sfsf.reshuffle.applicants.backend.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.sap.cloud.sdk.odatav2.connectivity.FilterExpression;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQuery;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryBuilder;
import com.sap.sfsf.reshuffle.applicants.backend.model.Candidate;
import com.sap.sfsf.reshuffle.applicants.backend.model.Config;
import com.sap.sfsf.reshuffle.applicants.backend.model.ExCandidate;
import com.sap.sfsf.reshuffle.applicants.backend.model.ExEmpJob;
import com.sap.sfsf.reshuffle.applicants.backend.model.Photo;
import com.sap.sfsf.reshuffle.applicants.backend.model.Rating;
import com.sap.sfsf.reshuffle.applicants.backend.model.Willingness;
import com.sap.sfsf.reshuffle.applicants.backend.model.details.DepartmentDetails;
import com.sap.sfsf.reshuffle.applicants.backend.model.details.DivisionDetails;
import com.sap.sfsf.reshuffle.applicants.backend.model.details.PayGradeDetails;
import com.sap.sfsf.reshuffle.applicants.backend.model.details.PositionDetails;
import com.sap.sfsf.reshuffle.applicants.backend.model.details.UserDetails;
import com.sap.sfsf.reshuffle.applicants.backend.model.filters.CurrentPositionFilter;
import com.sap.sfsf.reshuffle.applicants.backend.repository.CandidateRepository;
import com.sap.sfsf.reshuffle.applicants.backend.util.CustomFilterExpression;
import com.sap.sfsf.reshuffle.applicants.backend.util.DateTimeUtil;
import com.sap.sfsf.reshuffle.applicants.backend.util.EmptyConfigException;
import com.sap.sfsf.reshuffle.applicants.backend.util.ListToMapUtil;

@Service
public class CurrentPositionService {
	Logger logger = LoggerFactory.getLogger(CurrentPositionService.class);

	@Autowired
	ConfigService configService;

	private final String DESTINATION = "SFSF_2nd";
	private final int EXCEPTIONAL_INT = -1;
	private final String TERMINATIONCODE = "3680";
	private final String TIMEZONE = "UTC";

	@Autowired
	private CandidateRepository candidateRepository;

	public List<Candidate> findAll() {
		List<Candidate> list = candidateRepository.findAll();
		return list;
	}

	//This method consists of 5 steps below...
	//  1. Get an extended-EmpJob list
	//  2. Get a Photo map
	//  3. Combine the Photo map to the extended-Empjob list
	//  4. Convert Extended-EmpJob list to a Candidate list
	//  5. Return the Candidate list
	public List<ExCandidate> findAllFromSfsf(CurrentPositionFilter candidateFilter) throws ODataException, EmptyConfigException {
		List<ExEmpJob> exList = null;
		List<ExCandidate> retList = new ArrayList<ExCandidate>();
		LocalDateTime today = DateTimeUtil.getToday();

		exList = getExEmpJobList(candidateFilter, today);

		if(exList != null && exList.size() != 0) {
			String userIds = exList.stream()
					.map(exEmpJob -> exEmpJob.getUserId())
					.collect(Collectors.joining("\',\'"));

			Config config = configService.getConfig();

			Map<String, Willingness> willMap = getWillingnessMap(userIds);
			Map<String, Rating> rate1Map = getRatingMap(userIds, config.getRateFormKey1());
			Map<String, Rating> rate2Map = getRatingMap(userIds, config.getRateFormKey2());
			Map<String, Rating> rate3Map = getRatingMap(userIds, config.getRateFormKey3());
			Map<String, Photo> photoMap = getPhotoMap(userIds);

			exList.forEach(exEmpJob ->
			retList.add(convertToCandidate(exEmpJob, today, photoMap, willMap, rate1Map, rate2Map, rate3Map, candidateFilter))
					);
			retList.removeAll(Collections.singleton(null));
		}

		return retList;
	}

	protected ExCandidate convertToCandidate(ExEmpJob exEmpJob, LocalDateTime today, 
			Map<String, Photo> photoMap, Map<String, Willingness> willMap, Map<String, Rating> rate1Map, Map<String, Rating> rate2Map, Map<String, Rating> rate3Map,
			CurrentPositionFilter candidateFilter) {

		String department = exEmpJob.getDepartment();
		String division = exEmpJob.getDivision();
		String position = exEmpJob.getPosition();
		String managerId = exEmpJob.getManagerId();
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

		Date startDate = exEmpJob.getStartDate();
		int jobTenure = getTenurePosition(startDate, today);

		String name = getName(lastName, firstName);

		String isRetire = event.equals(TERMINATIONCODE)? "yes": "no";

		Photo photoIst = photoMap != null? photoMap.get(userId) : null;
		String rawBase64 = photoIst != null? photoIst.getPhoto(): null;
		String mimeType = photoIst != null? photoIst.getMimeType(): null;
		String contentType = mimeType != null? mimeType: MediaType.APPLICATION_OCTET_STREAM_VALUE;
		String escapeSeqRemoved = rawBase64 != null? rawBase64.replaceAll("\\r\\n", ""): null;
		String photo = escapeSeqRemoved != null?"data:" + contentType + ";base64," + escapeSeqRemoved: null;

		Willingness willIst = willMap != null? willMap.get(userId): null;
		String willingness = "-";
		if(willIst != null) {
			willingness = willIst.isWillingness() == true ? "yes": "no";
		}

		Rating rate1Ist = rate1Map != null? rate1Map.get(userId): null;
		String rating1 = rate1Ist != null? rate1Ist.getRating(): null;	

		Rating rate2Ist = rate2Map != null? rate2Map.get(userId): null;
		String rating2 = rate2Ist != null? rate2Ist.getRating(): null;

		Rating rate3Ist = rate3Map != null? rate3Map.get(userId): null;
		String rating3 = rate3Ist != null? rate3Ist.getRating(): null;

		ExCandidate exCandidate = new ExCandidate(
				userId, name, isRetire,
				department, division, managerId, position, payGrade,
				departmentName, divisionName, positionName, payGradeName,
				jobTenure, willingness, rating1, rating2, rating3, photo);

		//avoid returning exCandidate if a willingness does not correspond with the filter willingness
		if(candidateFilter.isWillingness() == null) {
			return exCandidate;
		} else {
			if(isWillingness(willingness) == null) {
				return null;
			} else {
				if(candidateFilter.isWillingness().booleanValue() == isWillingness(willingness).booleanValue()) {
					return exCandidate;				
				} else {
					return null;
				}				
			}
		}
	}

	protected int getTenurePosition(Date startDate, LocalDateTime today) {
		if(startDate != null) {
			LocalDate localStartDate = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.of(TIMEZONE)).toLocalDate();
			return Period.between(localStartDate, today.toLocalDate()).getYears();
		} else {
			return EXCEPTIONAL_INT;
		}
	}

	protected String getName(String lastName, String firstName) {
		String name = null;
		if(lastName != null && firstName != null) {
			name = lastName + " " + firstName;
		} else if (firstName != null && lastName == null) {
			name = firstName;
		} else if (firstName == null && lastName != null) {
			name = lastName;
		}
		return name;
	}

	protected List<ExEmpJob> getExEmpJobList(CurrentPositionFilter candidateFilter, LocalDateTime today) throws ODataException, EmptyConfigException {
		String[] selects = {"userId", "userNav/firstName", "userNav/lastName",
				"startDate", "event",
				"department", "departmentNav/description_ja_JP", 
				"division", "divisionNav/description_ja_JP", "managerId",
				"position", "positionNav/externalName_ja_JP", "payGrade", "payGradeNav/name"};

		String[] expands = {"userNav", "departmentNav", "divisionNav", "positionNav"};

		FilterExpression filters = null;
		CustomFilterExpression filterEndDate = new CustomFilterExpression("endDate", "gt", DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filterStartDate = new CustomFilterExpression("startDate", "le", DateTimeUtil.getDateTimeFilter(today));
		filters = filterEndDate.and(filterStartDate);
		filters = candidateFilter != null? candidateFilter.addArgsFilter(filters): filters;			

		ODataQuery query = null;
		query = ODataQueryBuilder
				.withEntity("odata/v2", "EmpJob")
				.expand(expands)
				.select(selects)
				.filter(filters)
				.build();

		logger.info("Candidate Query:" + query.toString());

		return query.execute(DESTINATION)
				.asList(ExEmpJob.class);
	}

	protected Map<String, Willingness> getWillingnessMap(String userIds) throws ODataException {
		String[] selects = {"User", "cust_willingness"};
		String filter = "User" + " in \'" + userIds + "\'";
		ODataQuery query = ODataQueryBuilder
				.withEntity("odata/v2", "cust_transfer_willingness")
				.select(selects)
				.param("$filter", filter)
				.build();

		logger.info("Will Query:" + query.toString());
		List<Willingness> list = query.execute(DESTINATION)
				.asList(Willingness.class);

		return ListToMapUtil.getMap("userId", list);
	}

	protected Map<String, Rating> getRatingMap(String userIds, String formTemplateId) throws ODataException {
		if(formTemplateId != null) {
			String[] selects = {"formSubjectId","rating"};
			String filter = "formTemplateId eq \'" + formTemplateId + "\' "
					+ "and isRated eq true "
					+ "and formSubjectId in \'" + userIds + "\'";
			ODataQuery query = ODataQueryBuilder
					.withEntity("odata/v2", "FormHeader")
					.select(selects)
					.param("$filter", filter)
					.build();

			logger.info("Rating Query:" + query.toString());
			List<Rating> list = query.execute(DESTINATION)
					.asList(Rating.class);

			return ListToMapUtil.getMap("userId", list);
		} else {
			return null;
		}
	}

	protected Map<String, Photo> getPhotoMap(String userIds) throws ODataException {
		//userIdFilter will be "photoType eq '26' and userId in '123','234','256'"
		String filter = "photoType eq \'26\' and userId in \'" + userIds + "\'";
		String[] selects = {"userId", "photo", "mimeType"};
		ODataQuery query = ODataQueryBuilder
				.withEntity("odata/v2", "Photo")
				.select(selects)
				.param("$filter", filter)
				.build();

		logger.info("Photo Query:" + query.toString());
		List<Photo> list = query.execute(DESTINATION)
				.asList(Photo.class);

		return ListToMapUtil.getMap("userId", list);
	}

	protected Boolean isWillingness(String willingness) {
		Boolean isWillingness = null;
		if(willingness != null) {
			if(willingness.equals("yes")) {
				isWillingness = true;
			} else if(willingness.equals("no")) {
				isWillingness = false;
			} else if(willingness.equals("-")) {
				isWillingness = null;
			}
		}		
		return isWillingness;
	}

	public void save(Candidate candidate) {
		candidateRepository.save(candidate);
	}

	public void saveAll(List<Candidate> candidateList) {
		candidateRepository.deleteAll();
		candidateRepository.saveAll(candidateList);
	}
}