package com.omni.utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.omni.driver.Driver;
import com.omni.reports.ExtentLogger;
import com.omni.reports.ExtentReport;

public class CustomListeners implements ITestListener, ISuiteListener {
	Driver driver = new Driver();
	TestUtil testUtil = new TestUtil();
	ExtentReport extentReport = new ExtentReport();
	Date d = new Date();
	String folderName = System.getProperty("user.dir") + "\\report_" + d.toString().replace(":", "_").replace(" ", "_");
	String fileName = "Extent_" + d.toString().replace(":", "_").replace(" ", "_") + ".html";

	public void onStart(ISuite suite) {
		try {
			extentReport.initReports();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onFinish(ISuite suite) {
		try {
			extentReport.flushReports();
			// update name of "reports" folder to "report_<timestamp>".
			File reports = new File(System.getProperty("user.dir") + "\\reports\\");
			File reportsUpdatedPath = new File(folderName);
			reports.renameTo(reportsUpdatedPath);
			Desktop.getDesktop().browse(new File(folderName + "\\" + fileName).toURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onTestStart(ITestResult result) {

		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onTestSuccess(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		String logText = "<b>" + "TEST CASE:- " + methodName.toUpperCase() + " PASSED" + "</b>";
		Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
		// ExtentLogger.pass(logText);
		ExtentLogger.log(Status.PASS, m);

		try {
			TestUtil testUtil = new TestUtil();
			testUtil.captureScreenshot();
			ExtentLogger.pass("<b>" + "<font color=" + "green>" + "Screenshot: " + "</font>" + "</b>", MediaEntityBuilder.createScreenCaptureFromPath(testUtil.screenshotName).build());
			driver.executeScript("lambda-status=passed");
		} catch (Exception e) {

		}
	}

	public void onTestFailure(ITestResult result) {
		String excepionMessage = result.getThrowable().getMessage() + "<br>" + Arrays.toString(result.getThrowable().getStackTrace());
		ExtentLogger.fail("<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Exception Occured: Click to see" + "</font>" + "</b >" + "</summary>" + excepionMessage.replaceAll(",", "<br>") + "</details>" + " \n");
		try {

			testUtil.captureScreenshot();
			ExtentLogger.fail("<b>" + "<font color=" + "red>" + "Screenshot of failure" + "</font>" + "</b>", MediaEntityBuilder.createScreenCaptureFromPath(testUtil.screenshotName).build());
			driver.executeScript("lambda-status=failed");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String failureLogg = "TEST CASE FAILED";
		Markup m = MarkupHelper.createLabel(failureLogg, ExtentColor.RED);
		ExtentLogger.log(Status.FAIL, m);
	}

	public void onTestSkipped(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		String logText = "<b>" + "Test Case:- " + methodName + " is Skipped" + "</b>";
		Markup m = MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
		ExtentLogger.skip(m);
		try {
			driver.executeScript("lambda-status=skipped");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}