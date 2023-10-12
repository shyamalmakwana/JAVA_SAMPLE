package com.omni.reports;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import com.omni.utilities.FrameworkConstants;

public class ExtentReport {

	FrameworkConstants frameworkConstants = new FrameworkConstants();
	public static ExtentReports extent = new ExtentReports();

	public void initReports() throws IOException {
		if (Objects.nonNull(extent)) {
			ExtentSparkReporter spark = new ExtentSparkReporter(frameworkConstants.getExtentReportFilePath()).viewConfigurer().viewOrder().as(new ViewName[] { ViewName.DASHBOARD, ViewName.TEST, ViewName.AUTHOR, ViewName.CATEGORY, ViewName.DEVICE, ViewName.LOG, ViewName.EXCEPTION }).apply();
			ExtentSparkReporter failedspark = new ExtentSparkReporter("failed-tests-index.html").filter().statusFilter().as(new Status[] { Status.FAIL, Status.SKIP }).apply();
			failedspark.config().setDocumentTitle("Failed Tests");
			final File CONF = new File("extentconfig.json");
			spark.loadJSONConfig(CONF);
			extent.attachReporter(spark, failedspark);
			extent.setSystemInfo("Automation Tester", "smittal");
			extent.setSystemInfo("Organization", "Paxcel");
		}
	}

	public void flushReports() throws IOException {
		if (Objects.nonNull(extent)) {
			extent.flush();
		}
	}

	public void createTest(String testcasename, String author, String category, String device) {
		ExtentManager.setExtentTest(extent.createTest(testcasename).assignAuthor(author).assignCategory(category).assignDevice(device));
	}
}
