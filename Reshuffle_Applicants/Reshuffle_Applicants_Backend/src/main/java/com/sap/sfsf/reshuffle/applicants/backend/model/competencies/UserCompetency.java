package com.sap.sfsf.reshuffle.applicants.backend.model.competencies;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class UserCompetency {
    @Expose
    private String currentEmpId;
    @Expose
    private String currentEmpName;
    @Expose
    private int competencyPercentage = 0;
    private int totalCompCount = 0;
    private int beyondThresholdCompCount = 0;
    
    public void addCompCount(boolean bool) {
        totalCompCount ++;
        if(bool) {
            beyondThresholdCompCount ++;
        }
        if(beyondThresholdCompCount != 0) {
            competencyPercentage = beyondThresholdCompCount / totalCompCount * 100;
        }
    };
}

