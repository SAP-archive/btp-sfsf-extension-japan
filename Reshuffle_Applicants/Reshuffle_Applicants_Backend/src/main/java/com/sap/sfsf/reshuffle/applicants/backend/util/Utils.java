package com.sap.sfsf.reshuffle.applicants.backend.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;
import com.sap.sfsf.reshuffle.applicants.backend.model.Candidate;
import com.sap.sfsf.reshuffle.applicants.backend.services.CandidateService;
import com.sap.sfsf.reshuffle.applicants.backend.util.validator.CandidateReqBodyValidator;
import com.sap.cloud.sdk.cloudplatform.security.Authorization;
import com.sap.cloud.sdk.cloudplatform.security.principal.PrincipalAccessor;

public class Utils {
    Logger logger = LoggerFactory.getLogger(Utils.class);
    private static int EXCEPTIONAL_INT = -1;
    private static boolean EXCEPTIONAL_BOOL = false;

    public String getMandtStringField(JSONObject obj, String key) {
        String retVal = null;
        try {
            retVal = obj.getString(key);
        } catch (JSONException e) {
            throw e;
        }
        return retVal;
    }

    public String getOptinalStringField(JSONObject obj, String key) {
        String retVal = null;
        try {
            System.out.println("JSON RET: " + key);
            retVal = obj.getString(key);

        } catch (JSONException ignoired) {
            ignoired.printStackTrace();
        }
        return retVal;
    }

    public int getMandtIntField(JSONObject obj, String key) {
        int retVal = EXCEPTIONAL_INT;
        try {
            retVal = obj.getInt(key);
        } catch (JSONException e) {
            throw e;
        }
        return retVal;
    }

    public int getOptionalIntField(JSONObject obj, String key) {
        int retVal = EXCEPTIONAL_INT;
        try {
            retVal = obj.getInt(key);
        } catch (JSONException ignored) {
            ignored.printStackTrace();
        }
        return retVal;
    }

    public boolean getMandtBoolField(JSONObject obj, String key) {
        boolean retVal = EXCEPTIONAL_BOOL;
        try {
            retVal = obj.getBoolean(key);
        } catch (JSONException e) {
            throw e;
        }
        return retVal;
    }

    public boolean getOptionalBoolField(JSONObject obj, String key) {
        boolean retVal = EXCEPTIONAL_BOOL;
        try {
            retVal = obj.getBoolean(key);
        } catch (JSONException ignored) {
            ignored.printStackTrace();
        }
        return retVal;
    }

    public void validateCandidateList(List<Candidate> candidateList, List<String> caseIds) {
        boolean isBadRequest = false;
        boolean isCaseIdDuplicated = false;
        ArrayList<String> errorsList = new ArrayList<String>();
        for (int i = 0; i < candidateList.size(); i++) {
            CandidateReqBodyValidator cValidator = new CandidateReqBodyValidator(candidateList.get(i), caseIds);
            isBadRequest = cValidator.isBadRequest();
            isCaseIdDuplicated = cValidator.isCaseIdDuplicated();
            if (isBadRequest == true) {
                errorsList.addAll(cValidator.getProblemList());
                Optional<String> result = cValidator.getProblemList().stream().reduce((accum, value) -> {
                    return accum + ", " + value;
                });
                logger.error("Candidate Validation Error(s) : " + i + "th candidate: " + result.orElse(""));
            }
        }
        if (isBadRequest == true) {
            if (isCaseIdDuplicated == true) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }
    }

    public List<Candidate> reqToCandidateListWithoutValidation(String req) {
        List<Candidate> candidateList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(req);
        for (int i = 0; i < jsonArray.length(); i++) {
            Gson gson = new Gson();
            JSONObject json = jsonArray.getJSONObject(i);
            Candidate candidate = gson.fromJson(json.toString(), Candidate.class);
            candidateList.add(candidate);
        }
        return candidateList;
    }

    public Boolean confrimCandidateListAsNewCase(List<Candidate> candidateList) {
        boolean isNew = true;
        for (int i = 0; i < candidateList.size(); i++) {
            if (candidateList.get(i).getCreatedAt() != null || candidateList.get(i).getModifiedAt() != null) {
                isNew = false;
                break;
            }
        }
        return isNew;
    }

    public void validateCandidateListForUpdating(List<Candidate> candidateList, List<String> caseIds) {
        String loginUser = PrincipalAccessor.getCurrentPrincipal().getPrincipalId();
        boolean isBadRequest = false;
        boolean isCaseIdDuplicated = false;
        boolean isDifferentUser = false;
        for (int i = 0; i < candidateList.size(); i++) {
            if (!candidateList.get(i).getCreatedBy().equals(loginUser)) {
                isDifferentUser = true;
                isBadRequest = true;
                break;
            }
        }
        if (isBadRequest == true) {
            if (isDifferentUser == true) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            } else if (isCaseIdDuplicated == true) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }
    }

    public void setCreated(List<Candidate> candidateList) {
        String loginUser = PrincipalAccessor.getCurrentPrincipal().getPrincipalId();
        for(int i = 0; i < candidateList.size(); i++) {
            candidateList.get(i).setCreatedAt(DateTimeUtil.toDate(LocalDateTime.now()));
            candidateList.get(i).setCreatedBy(loginUser);
        }
    }

    public void setModified(List<Candidate> candidateList) {
        String loginUser = PrincipalAccessor.getCurrentPrincipal().getPrincipalId();
        for(int i = 0; i < candidateList.size(); i++) {
            candidateList.get(i).setModifiedAt(DateTimeUtil.toDate(LocalDateTime.now()));
            candidateList.get(i).setModifiedBy(loginUser);
        }
    }

}
