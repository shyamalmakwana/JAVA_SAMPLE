package com.omni.driver;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.omni.utilities.ReadPropertyFile;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Driver {
	DriverManager driverManager = new DriverManager();
	ReadPropertyFile readPropertyFile = new ReadPropertyFile();
	String testClass = getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1);

	// This method will initialize the specific browser, passed in the config.properties file and open the browser.
	public ChromeOptions browserOptions(String testClassName){
		ChromeOptions browserOptions = new ChromeOptions();
		browserOptions.setPlatformName("Windows 10");
		browserOptions.setBrowserVersion("90.0");
		HashMap<String, Object> ltOptions = new HashMap<String, Object>();
		ltOptions.put("video", true);
		ltOptions.put("timezone", "Kolkata");
		ltOptions.put("build", "Automation");
		ltOptions.put("project", "Chaikin-Analytics-OMNI-QA");
		ltOptions.put("name",testClassName);
		ltOptions.put("w3c", true);
		ltOptions.put("plugin", "java-testNG");
		browserOptions.setCapability("LT:Options", ltOptions);
		return browserOptions;
	}

	public void initDriver(String testClass) throws Exception {
		if (Objects.isNull(driverManager.getDriver())) {
			switch (readPropertyFile.get("browser")) {

				case "remote":
					Method Method = null;
					RemoteWebDriver driver=	new RemoteWebDriver(new URL("https://" + readPropertyFile.get("lambdaTestUsername") + ":" + readPropertyFile.get("lambdaTestAccessKey") + readPropertyFile.get("lambdaTestHubUrl")), browserOptions(testClass));
					driverManager.setDriver(driver);
					break;

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
	//This method will provide LambdaTest status on dashboard passed/failed/skipped
	public void executeScript(String lambdaTestStatus) throws Exception {
		((JavascriptExecutor) driverManager.getDriver()).executeScript(lambdaTestStatus);
}
}