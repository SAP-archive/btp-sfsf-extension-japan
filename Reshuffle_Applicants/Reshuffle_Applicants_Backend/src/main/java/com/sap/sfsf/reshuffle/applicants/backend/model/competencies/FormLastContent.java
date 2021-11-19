package com.sap.sfsf.reshuffle.applicants.backend.model.competencies;

import com.sap.cloud.sdk.result.ElementName;

import lombok.Data;

@Data
public class FormLastContent {
    @ElementName("status")
    private String status;

    @ElementName("pmReviewContentDetail")
    private PmReviewContentDetail.PmReviewContentDetailContainer pmReviewContentDetail;
    
}
