package com.sap.sfsf.reshuffle.applicants.backend.model.competencies;

import com.sap.cloud.sdk.result.ElementName;
import lombok.Data;

@Data
public class FormSubject {
    @ElementName("userId")
    private String userId;

    @ElementName("lastName")
    private String lastName;

    @ElementName("firstName")
    private String firstName;
}
