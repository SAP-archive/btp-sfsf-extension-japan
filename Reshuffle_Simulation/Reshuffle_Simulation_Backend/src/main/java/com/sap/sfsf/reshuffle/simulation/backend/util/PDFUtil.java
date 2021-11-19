package com.sap.sfsf.reshuffle.simulation.backend.util;

import com.google.gson.Gson;

public class PDFUtil {

	public String getToken(String tokenStr) {
		TokenWrapper[] parsed = new Gson().fromJson(tokenStr, TokenWrapper[].class);
		if (parsed.length > 0) {
			return parsed[0].value;
		}
		return "";
	}
	
	public String getPDFContent(String pdfStr) {
		PDFWrapper parsed = new Gson().fromJson(pdfStr, PDFWrapper.class);
		return parsed.fileContent;
	}

}


class TokenWrapper {
	String type;
	String value;
}

class PDFWrapper {
	public String fileName;
	public String fileContent;
}