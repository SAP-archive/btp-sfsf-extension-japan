package com.sap.sfsf.reshuffle.simulation.backend.model;

import java.io.Serializable;

public class CandidateId implements Serializable{
    private static final long serialVersionUID = -1164157497632309515L;

    private String caseID;
    private String candidateID;

    /**
     * @return the candidateId
     */
    public String getCandidateID() {
        return candidateID;
    }

    /**
     * @return the caseId
     */
    public String getCaseID() {
        return caseID;
    }
}