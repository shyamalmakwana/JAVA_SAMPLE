package com.omni.base;

import java.lang.reflect.Method;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.omni.driver.Driver;

public class BaseTest {
	Driver driver = new Driver();
	BasePage basePage = new BasePage();
	String testClass = getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1);

	@BeforeTest
	// This method will open the browser and enter the url.
	public void initBrowser() throws Exception {
		driver.initDriver();
	}
	
	@BeforeClass
	// This method will login into the application, except for Login testclass.
	public void login() throws Exception {
		if (!testClass.equalsIgnoreCase("LoginTest")) {
			basePage.login();
			basePage.setSessionKeyAndJsessionId();
		}
	}

	@BeforeMethod
	// This method will setup the testcase for extent report.
	public void clickOnPageBeforeStartingTestCase(Method m) throws Exception {
		String testName=m.getName();
		basePage.clickOnModule(testClass,testName);
	}

	@AfterMethod
	public void afterTestMethod() throws Exception {
		driver.refreshDriver();
	}
	
	@AfterClass
	// This method will open the browser and enter the url.
	public void logoutAfterClass() throws Exception {
		if (!testClass.equalsIgnoreCase("LoginTest")) {
			basePage.logout();
		}
	}

	@AfterTest
	// This method will close the browser after completing testcase.
	public void quitDriverAfterFinishTestClass() throws Exception {
		driver.quitDriver();
	}

}