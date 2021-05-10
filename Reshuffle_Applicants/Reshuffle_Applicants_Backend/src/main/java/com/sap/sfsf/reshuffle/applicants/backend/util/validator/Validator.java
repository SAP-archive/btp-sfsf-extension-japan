package com.sap.sfsf.reshuffle.applicants.backend.util.validator;

import java.util.ArrayList;

public abstract class Validator {
	
	private boolean isBadRequest = false;
	private ArrayList<String> errorList = new ArrayList<String>();
	
	public boolean isBadRequest() {
		return this.isBadRequest;
	}

	public ArrayList<String> getProblemList() {
		return this.errorList;
	}
	
	protected void addErrorList(String err) {
		isBadRequest = true;
		errorList.add(err);
	}
}
