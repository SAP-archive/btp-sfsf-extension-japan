package com.sap.sfsf.reshuffle.applicants.backend.model.competencies;

import com.sap.cloud.sdk.result.ElementName;

import lombok.Data;

@Data
public class PositionCompetency {
	@ElementName("positionNav")
	private PositionNav.PositionNavContainer positionNav;

	@ElementName("positionCompetencyMappings")
	private PositionCompetencyMappings.PositionCompetencyMappingsContainer positionCompetencyMappings;
}
