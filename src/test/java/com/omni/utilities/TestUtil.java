package com.omni.utilities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.DataProvider;

import com.omni.driver.DriverManager;

public class TestUtil {
	DriverManager driverManager = new DriverManager();
	FrameworkConstants frameworkConstants = new FrameworkConstants();

	public String screenshotPath;
	public String screenshotName;

	public void captureScreenshot() throws IOException {

		File scrFile = ((TakesScreenshot) driverManager.getDriver()).getScreenshotAs(OutputType.FILE);

		Date d = new Date();
		screenshotName = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";

		FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + "\\target\\surefire-reports\\html\\" + screenshotName));
		FileUtils.copyFile(scrFile, new File(".\\reports\\" + screenshotName));

	}

	@DataProvider(name = "dp")
	public Object[][] getData(Method m) {
		ExcelReader excel = new ExcelReader(frameworkConstants.getTestDataFilePath());
		Class<?> declaringClass = m.getDeclaringClass();
		String className = declaringClass.getName();
		String sheetName = className.substring(className.lastIndexOf(".") + 1);
		String methodName = m.getName();
		int rows = excel.getRowCount(sheetName);
		int cols = excel.getColumnCount(sheetName);
		int temp = 0;
		for (int count = 1; count <= rows; count++) {
			if (methodName.equalsIgnoreCase(excel.getCellData(sheetName, 1, count))) {
				temp = temp + 1;
			}
		}
		rows = temp + 1;
		Object[][] data = new Object[rows - 1][1];

		Hashtable<String, String> table = null;
		int rowNew = 2;
		for (int rowNum = 2; rowNum <= excel.getRowCount(sheetName); rowNum++) { // 2
			if (methodName.equalsIgnoreCase(excel.getCellData(sheetName, 1, rowNum))) {
				table = new Hashtable<String, String>();
				for (int colNum = 1; colNum < cols; colNum++) {
					table.put(excel.getCellData(sheetName, colNum, 1), excel.getCellData(sheetName, colNum, rowNum));
					data[rowNew - 2][0] = table;
				}rowNew++;
			}
		}
		return data;
	}

	public boolean isTestRunnable(String testName) {
		ExcelReader excel = new ExcelReader(frameworkConstants.getTestDataFilePath());

		String sheetName = "test_suite";
		int rows = excel.getRowCount(sheetName);

		for (int rNum = 2; rNum <= rows; rNum++) {

			String testCase = excel.getCellData(sheetName, "TCID", rNum);

			if (testCase.equalsIgnoreCase(testName)) {

				String runmode = excel.getCellData(sheetName, "Runmode", rNum);

				if (runmode.equalsIgnoreCase("Y"))
					return true;
				else
					return false;
			}

		}
		return false;
	}

}