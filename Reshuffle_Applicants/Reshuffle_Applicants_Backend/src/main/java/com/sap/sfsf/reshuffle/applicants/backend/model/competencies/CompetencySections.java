package com.sap.sfsf.reshuffle.applicants.backend.model.competencies;

import java.util.List;

import com.sap.cloud.sdk.result.ElementName;
import lombok.Data;

@Data
public class CompetencySections {
    @ElementName("competencies")
    private Competencies.CompetenciesContainer competencies;

    @Data
    public static class CompetencySectionsContainer {
        @ElementName("results")
        private List<CompetencySections> competencySectionsList = null;
    }
    
}
