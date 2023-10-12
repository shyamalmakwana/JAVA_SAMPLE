package com.omni.utilities;

import java.util.Date;

public class FrameworkConstants {

	Date d = new Date();
	String fileName = "Extent_" + d.toString().replace(":", "_").replace(" ", "_") + ".html";

	private final String RESOURCEPATH = System.getProperty("user.dir") + "/src/test/resources";
	private final String CONFIGFILEPATH = RESOURCEPATH + "/properties/config.properties";
	private final String EXTENTREPORTFILEPATH = System.getProperty("user.dir") + "\\reports\\" + fileName;
	private final String TESTDATAFILEPATH = System.getProperty("user.dir") + "\\testdata.xlsx";
	private final String APIRESPONSEFILEPATH = RESOURCEPATH + "/properties/apiResponse.txt";

	public String getConfigFilePath() {
		return CONFIGFILEPATH;
	}

	public String getExtentReportFilePath() {
		return EXTENTREPORTFILEPATH;
	}

	public String getTestDataFilePath() {
		return TESTDATAFILEPATH;
	}

	public String getApiResponseFilePath() {
		return APIRESPONSEFILEPATH;
	}

}
