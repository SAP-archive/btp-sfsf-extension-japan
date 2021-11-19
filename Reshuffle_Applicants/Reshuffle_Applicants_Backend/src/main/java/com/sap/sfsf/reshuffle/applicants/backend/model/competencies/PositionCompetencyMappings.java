package com.sap.sfsf.reshuffle.applicants.backend.model.competencies;

import java.util.List;

import com.sap.cloud.sdk.result.ElementName;
import lombok.Data;

@Data
public class PositionCompetencyMappings {
    @ElementName("competencyNav")
    private CompetencyNav competencyNav = null;

    @Data
    public static class PositionCompetencyMappingsContainer {
        @ElementName("results")
        private List<PositionCompetencyMappings> positionCompetencyMappingsList = null;
    };
}
