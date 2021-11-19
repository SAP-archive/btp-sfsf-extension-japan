package com.sap.sfsf.reshuffle.applicants.backend.model.competencies;

import java.util.List;

import com.sap.cloud.sdk.result.ElementName;
import lombok.Data;

@Data
public class Competencies {
    @ElementName("name")
    private String name;

    @ElementName("officialRating")
    private OfficialRating officialRating = null;

    @Data
    public static class CompetenciesContainer {
        @ElementName("results")
        private List<Competencies> competenciesList = null;
    }
    
}
