package com.omni.driver;

import org.openqa.selenium.WebDriver;

public class DriverManager {
	private static ThreadLocal<WebDriver> dr = new ThreadLocal<>();

	//This is to start the WebDriver
	public WebDriver getDriver() {
		return dr.get();
	}
	
	//This is to set the WebDriver
	public void setDriver(WebDriver driverref) {
		dr.set(driverref);
	}

	//This is to unload the WebDriver while closing browser.
	public void unload() {
		dr.remove();
	}

}
