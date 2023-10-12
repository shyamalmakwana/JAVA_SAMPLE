package com.omni.reports;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.model.Media;

public class ExtentLogger {

	public static void pass(String message) {

		ExtentManager.getExtentTest().pass(message);
	}

	public static void fail(String message) {
		ExtentManager.getExtentTest().fail(message);

	}

	public static void skip(Markup m) {
		ExtentManager.getExtentTest().skip(m);
	}

	public static void info(String message) {
		ExtentManager.getExtentTest().info(message);
	}

	public static void fail(String string, Media build) {
		ExtentManager.getExtentTest().fail(string, build);
	}

	public static void log(Status log, Markup m) {
		ExtentManager.getExtentTest().log(log, m);
	}

	public static void pass(String string, Media build) {
		ExtentManager.getExtentTest().pass(string, build);
	}

}
