package com.sap.sfsf.reshuffle.applicants.backend.model.competencies;

import java.util.List;

import com.sap.cloud.sdk.result.ElementName;
import lombok.Data;

@Data
public class PmReviewContentDetail {
    @ElementName("competencySections")
    private CompetencySections.CompetencySectionsContainer competencySections;
    
    @Data
    public static class PmReviewContentDetailContainer {
        @ElementName("results")
        private List<PmReviewContentDetail> pmReviewContentDetailList = null;
    }
}