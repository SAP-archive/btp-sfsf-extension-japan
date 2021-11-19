package com.sap.sfsf.reshuffle.applicants.backend.model.competencies;

import com.sap.cloud.sdk.result.ElementName;
import lombok.Data;

@Data
public class FormHeader {
    @ElementName("formSubject")
    private FormSubject formSubject = null;
}
