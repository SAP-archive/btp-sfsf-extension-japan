package com.sap.sfsf.reshuffle.applicants.backend.model.competencies;

import java.util.List;
import com.sap.cloud.sdk.result.ElementName;
import lombok.Data;

@Data
public class PositionNav {
    @ElementName("code")    
    private String code;

    @ElementName("externalName_defaultValue")
    private String externalName_defaultValue;

    public static class PositionNavContainer {
        @ElementName("results")
        private List<PositionNav> positionNavList = null;
    }
}
