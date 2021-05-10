package com.sap.sfsf.reshuffle.applicants.backend.util;

public class EmptyConfigException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final String msg = "Config is empty";
	
	public EmptyConfigException() {
		super(msg);
	}
}
