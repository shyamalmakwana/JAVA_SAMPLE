package com.omni.driver;

import java.util.Objects;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.omni.utilities.ReadPropertyFile;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Driver {
	DriverManager driverManager = new DriverManager();
	ReadPropertyFile readPropertyFile = new ReadPropertyFile();

	// This method will initialize the specific browser, passed in the config.properties file and open the browser.
	@SuppressWarnings("deprecation")
	public void initDriver() throws Exception {
		if (Objects.isNull(driverManager.getDriver())) {
			switch (readPropertyFile.get("browser")) {

				case "chrome":
					ChromeOptions options = new ChromeOptions();
					options.setHeadless(Boolean.parseBoolean(readPropertyFile.get("headless")));
					WebDriverManager.chromedriver().setup();
					driverManager.setDriver(new ChromeDriver(options));
					break;

				case "firefox":
					WebDriverManager.firefoxdriver().setup();
					driverManager.setDriver(new FirefoxDriver());
					break;

				case "edge":
					WebDriverManager.edgedriver().setup();
					driverManager.setDriver(new EdgeDriver());
					break;

				case "safari":
					WebDriverManager.safaridriver().setup();
					driverManager.setDriver(new SafariDriver());
					break;

				case "ie":
					WebDriverManager.iedriver().setup();
					driverManager.setDriver(new InternetExplorerDriver());
					break;

				default:
					System.out.println("browser : " + readPropertyFile.get("browser") + " is invalid, Launching Chrome as browser of choice..");
					WebDriverManager.chromedriver().setup();
					driverManager.setDriver(new ChromeDriver());
			}
			driverManager.getDriver().manage().window().setSize(new Dimension(1440, 900));
			driverManager.getDriver().manage().window().maximize();
			driverManager.getDriver().get(readPropertyFile.get("url"));
		}
	}

	// This method will refresh the browser tab.
	public void refreshDriver() {
		if (Objects.nonNull(driverManager.getDriver())) {
			driverManager.getDriver().navigate().refresh();
		}
	}

	// This method will close the browser after completion of the test case.
	public void quitDriver() {
		if (Objects.nonNull(driverManager.getDriver())) {
			driverManager.getDriver().quit();
			driverManager.unload();
		}
	}
}