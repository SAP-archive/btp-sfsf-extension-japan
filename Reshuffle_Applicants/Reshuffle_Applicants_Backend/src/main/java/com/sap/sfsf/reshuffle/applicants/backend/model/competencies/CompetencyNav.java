package com.sap.sfsf.reshuffle.applicants.backend.model.competencies;

import com.sap.cloud.sdk.result.ElementName;
import lombok.Data;

@Data
public class CompetencyNav {
    @ElementName("name_defaultValue")
    private String name_defaultValue;
}
