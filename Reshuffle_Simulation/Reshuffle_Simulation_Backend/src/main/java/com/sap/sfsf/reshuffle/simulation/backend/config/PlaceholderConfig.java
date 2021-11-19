package com.sap.sfsf.reshuffle.simulation.backend.config;

import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:placeholder.properties")
public class PlaceholderConfig {
	private Logger LOG = LoggerFactory.getLogger(PlaceholderConfig.class);

	//candidateID
	@Value("${candidateID.placeholder.name}")
	private String candidateIDPlaceholder;
	@Value("${candidateID.placeholder.example}")
	private String candidateIDExample;

	//candidateName
	@Value("${candidateName.placeholder.name}")
	private String candidateNamePlaceholder;
	@Value("${candidateName.placeholder.example}")
	private String candidateNameExample;
	
	//division
	@Value("${division.placeholder.name}")
	private String divisionPlaceholder;
	@Value("${division.placeholder.example}")
	private String divisionExample;
	
	//department
	@Value("${department.placeholder.name}")
	private String departmentPlaceholder;
	@Value("${department.placeholder.example}")
	private String departmentExample;
	
	//position
	@Value("${position.placeholder.name}")
	private String positionPlaceholder;
	@Value("${position.placeholder.example}")
	private String positionExample;
	
	//jobGrade
	@Value("${jobGrade.placeholder.name}")
	private String jobGradePlaceholder;
	@Value("${jobGrade.placeholder.example}")
	private String jobGradeExample;
	
	//nextDivision
	@Value("${nextDivision.placeholder.name}")
	private String nextDivisionPlaceholder;
	@Value("${nextDivision.placeholder.example}")
	private String nextDivisionExample;
	
	//nextDepartment
	@Value("${nextDepartment.placeholder.name}")
	private String nextDepartmentPlaceholder;
	@Value("${nextDepartment.placeholder.example}")
	private String nextDepartmentExample;
	
	//nextPosition
	@Value("${nextPosition.placeholder.name}")
	private String nextPositionPlaceholder;
	@Value("${nextPosition.placeholder.example}")
	private String nextPositionExample;
	
	//nextJobGrade
	@Value("${nextJobGrade.placeholder.name}")
	private String nextJobGradePlaceholder;
	@Value("${nextJobGrade.placeholder.example}")
	private String nextJobGradeExample;
	
	@Bean
	public String[] fetchPlaceholderNames() {
		String str[] = {
				candidateIDPlaceholder,
				candidateNamePlaceholder,
				divisionPlaceholder,
				departmentPlaceholder,
				positionPlaceholder,
				jobGradePlaceholder,
				nextDivisionPlaceholder,
				nextDepartmentPlaceholder,
				nextPositionPlaceholder,
				nextJobGradePlaceholder
				};
		
		return str;
	}
	
	@Bean
	public String readPlaceholderExamples() {
		JsonObject json = new JsonObject();
		json.addProperty(candidateIDPlaceholder,    candidateIDExample);
		json.addProperty(candidateNamePlaceholder,  candidateNameExample);
		json.addProperty(divisionPlaceholder,       divisionExample);
		json.addProperty(departmentPlaceholder,     departmentExample);
		json.addProperty(positionPlaceholder,       positionExample);
		json.addProperty(jobGradePlaceholder,       jobGradeExample);
		json.addProperty(nextDivisionPlaceholder,   nextDivisionExample);
		json.addProperty(nextDepartmentPlaceholder, nextDepartmentExample);
		json.addProperty(nextPositionPlaceholder,   nextPositionExample);
		json.addProperty(nextJobGradePlaceholder,   nextJobGradeExample);
		
		return json.toString();
	}
}
