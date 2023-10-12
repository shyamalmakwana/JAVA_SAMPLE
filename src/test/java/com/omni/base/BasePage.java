package com.omni.base;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.omni.driver.DriverManager;
import com.omni.reports.ExtentLogger;
import com.omni.reports.ExtentReport;
import com.omni.utilities.ExcelReader;
import com.omni.utilities.FrameworkConstants;
import com.omni.utilities.ReadPropertyFile;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;

public class BasePage {
	DriverManager driverManager = new DriverManager();
	ReadPropertyFile readPropertyFile = new ReadPropertyFile();
	FrameworkConstants frameworkConstants = new FrameworkConstants();
	ExtentReport extentReport = new ExtentReport();
	ExcelReader excelReader = new ExcelReader(frameworkConstants.getTestDataFilePath());
	ApiBaseUrl apiUrl = new ApiBaseUrl();

	// Login will run before any testcase, So assigning xpath in basePage.
	By txtEmail = By.xpath("//input[@id='email']");
	By txtPassword = By.xpath("//input[@id='password']");
	By btnLogin = By.xpath("//button[text()='Log In']");
	By btnLogout = By.xpath("//span[text()='Logout']");
	By clickHere = By.xpath("//a[text()='Click here']");
	By myChaikinPage = By.xpath("//a[text()='My Chaikin']");
	By pgrPage = By.xpath("//a[text()='Power Gauge Rating']");
	By txtUSMarketStatus = By.xpath("//h3[text()='U.S. Markets Are:']");
	By chartCanvas = By.cssSelector("#chartdiv canvas");
	By uiDateSelector = By.cssSelector("#chartdiv > div > div.am5-tooltip-container > div:nth-child(2)");
	By uiPriceSelector = By.cssSelector("#chartdiv > div > div.am5-tooltip-container > div:nth-child(1)");

	// To click on any element.
	public void click(By element, String elementName) throws Exception {
		String statusOk = "True";
		try {
			new WebDriverWait(driverManager.getDriver(), Duration.ofSeconds(Integer.parseInt(readPropertyFile.get("explicit_wait")))).until(ExpectedConditions.visibilityOfElementLocated(element)).click();
		} catch (Exception e) {
			statusOk = "False";
			e.printStackTrace();
		}
		ExtentLogger.info("Clicking on [" + elementName + "] - [" + statusOk + "].");
	}

	// To type any value inside text field.
	public void type(By element, String value, String elementName){
		String statusOk = "True";
		try {
			new WebDriverWait(driverManager.getDriver(), Duration.ofSeconds(Integer.parseInt(readPropertyFile.get("explicit_wait")))).until(ExpectedConditions.visibilityOfElementLocated(element)).sendKeys(value);
		} catch (Exception e) {
			statusOk = "False";
			e.printStackTrace();
		}
		ExtentLogger.info("Enter value [" + value + "] in [" + elementName + "] field - [" + statusOk + "].");
	}

	// To verify whether the element displayed or not.
	public boolean verifyElement(By element, String elementName){
		String statusOk = "True";
		boolean elementPresent = false;
		try {
			elementPresent = new WebDriverWait(driverManager.getDriver(), Duration.ofSeconds(Integer.parseInt(readPropertyFile.get("explicit_wait")))).until(ExpectedConditions.visibilityOfElementLocated(element)).isDisplayed();
		} catch (Exception e) {
			statusOk = "False";
			e.printStackTrace();
		}
		ExtentLogger.info("Element [" + elementName + "] visible - [" + statusOk + "].");
		return elementPresent;
	}

	// To extract the text of a webElement.
	public String getText(By element){
		String text = null;
		try {
			text = new WebDriverWait(driverManager.getDriver(), Duration.ofSeconds(Integer.parseInt(readPropertyFile.get("explicit_wait")))).until(ExpectedConditions.visibilityOfElementLocated(element)).getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}

	// To Wait until element not visible on page.
	public void WaitUntilElementNotVisible(By element){
		try {
			new WebDriverWait(driverManager.getDriver(), Duration.ofSeconds(Integer.parseInt(readPropertyFile.get("explicit_wait")))).until(ExpectedConditions.invisibilityOfElementLocated(element));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// This function is used for just login.
	public void login() throws Exception {
		extentReport.createTest("@TestCase : login", readPropertyFile.get("author"), readPropertyFile.get("testingType"), readPropertyFile.get("browser"));
		type(txtEmail, readPropertyFile.get("email"), "Email Address");
		type(txtPassword, readPropertyFile.get("password"), "Passowrd");
		click(btnLogin, "Login Button");
	}

	// This function is used for logout.
	public void logout() throws Exception {
		extentReport.createTest("@TestCase : logout", readPropertyFile.get("author"), readPropertyFile.get("testingType"), readPropertyFile.get("browser"));
		click(btnLogout, "Logout Button");
		click(clickHere, "Click Here");
	}

	// This function is used to handle alert at login screen.
	public void acceptAlert(String message) throws Exception {
		Thread.sleep(3000); // alert visible on screen after 2-3 seconds.
		Alert alert = driverManager.getDriver().switchTo().alert();
		Assert.assertEquals(alert.getText(), message);
		ExtentLogger.info("Alert data: " + alert.getText());
		alert.accept();
	}

	public void clickOnModule(String testClass, String testName) throws Exception {
		extentReport.createTest(testClass + "@TestCase : " + testName, readPropertyFile.get("author"), readPropertyFile.get("testingType"), readPropertyFile.get("browser"));
		switch (testClass) {
			case "MyChaikinTest" -> {
				click(myChaikinPage, "My Chaikin Page");
				verifyElement(txtUSMarketStatus, "U.S. Markets");
			}
			case "PgrTest" -> click(pgrPage, "PGR Page");
			default -> driverManager.getDriver().navigate().refresh();
		}
	}

	// To get the count of number of respective webElements present.
	public int getSize(By element){
		int count = 0;
		try {
			count = new WebDriverWait(driverManager.getDriver(), Duration.ofSeconds(Integer.parseInt(readPropertyFile.get("explicit_wait")))).until(ExpectedConditions.presenceOfAllElementsLocatedBy(element)).size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	// To get the value of the given attribute of the element.
	public String getAttribute(By element, String attribute_name){
		String attribute = null;
		try {
			attribute = new WebDriverWait(driverManager.getDriver(), Duration.ofSeconds(Integer.parseInt(readPropertyFile.get("explicit_wait")))).until(ExpectedConditions.visibilityOfElementLocated(element)).getAttribute(attribute_name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attribute;
	}

	// To scroll over to the element into the view.
	public void scroll(By element){
		int count = 0;
		try {
			count = new WebDriverWait(driverManager.getDriver(), Duration.ofSeconds(Integer.parseInt(readPropertyFile.get("explicit_wait")))).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(element)).size();
			if (count > 0) {
				((JavascriptExecutor) driverManager.getDriver()).executeScript("arguments[0].scrollIntoView();", driverManager.getDriver().findElement(element));
				Thread.sleep(2000);
			}
		} catch (Exception ignored) {
		}
	}

	// This function is used to convert stock/etf into String[] whether passed in customized or in random manner.
	public String[] convertStocks_ETFS(Hashtable<String, String> data) {
		int number = Integer.parseInt(data.get("count"));
		String[] ticker_convert = new String[number];

		String[] ticker_all = ((data.get("ticker").toUpperCase()).split(","));
		if (data.get("flow").equalsIgnoreCase("random")) {
			Random random = new Random();
			for (int i = 1; i <= number; i++) {
				String randomElement = ticker_all[random.nextInt(ticker_all.length)];
				ticker_convert[i - 1] = randomElement;
			}
			return ticker_convert;
		} else
			return ticker_all;
	}

	public String convertRating(String uiRating) {
		switch (uiRating) {
			case "", "NO RATING" -> uiRating = "-1";
			case "slider-disc ", "None" -> uiRating = "0";
			case "veryBearish", "Very Bearish" -> uiRating = "1";
			case "bearish", "Bearish" -> uiRating = "2";
			case "neutral", "Neutral" -> uiRating = "3";
			case "bullish", "Bullish" -> uiRating = "4";
			case "veryBullish", "Very Bullish" -> uiRating = "5";
		}
		return uiRating;
	}

	// After every login sessionKey & JsessionId will store in apiResponse.txt file.
	public void setSessionKeyAndJsessionId() throws Exception {
		try (OutputStream output = new FileOutputStream(frameworkConstants.getApiResponseFilePath())) {

			Properties prop = new Properties();

			JsonPath authApiRes = authApiCall(readPropertyFile.get("email"), readPropertyFile.get("password"));
			String sessionKey = authApiRes.getString("data.sessionInfo.sessionKey");
			String sessionToken = authApiRes.getString("data.sessionInfo.sessionToken");
			JsonPath getjwtAuthorizationRes = getjwtAuthorizationApiResponse(apiUrl.getjwtAuthorizationApiUrl(sessionToken), sessionToken);
			String jsessionId = getjwtAuthorizationRes.getString("sessionId");

			// set the properties value
			prop.setProperty("sessionKey", sessionKey);
			prop.setProperty("jsessionId", jsessionId);

			// save properties to project root folder
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	// This function will return sessionKey or JsessionId stored in apiResponse.txt file.
	public String getIds(String key){
		String value = null;
		try {
			Map<String, String> map = new HashMap<>();
			FileReader reader = new FileReader(frameworkConstants.getApiResponseFilePath());
			for (int lineNum = 1; lineNum <= 2; lineNum++) {// apiResponse.txt file always contains 2 lines - sessionKey & JsessionId.
				String[] temp = Files.readAllLines(Paths.get(frameworkConstants.getApiResponseFilePath())).get(lineNum).split("="); // Split the line text, and storing value in temp variable.
				map.put(temp[0], temp[1]);
			}
			value = map.get(key);
			reader.close();
		} catch (IOException io) {
			io.printStackTrace();
		}
		return value;
	}

	// This function is used to test the chart functionality. Validate the dates and the prices on each coordinate.
	public Boolean validateChartData(String ticker) throws Exception {
		verifyElement(chartCanvas, "Chart Canvas");
		WebElement rows = driverManager.getDriver().findElement(chartCanvas);
		int ChartWidth = rows.getSize().getWidth();
		Actions builder = new Actions(driverManager.getDriver());
		int j = 0;
		String uiDate = "no data";
		String uiPrice = "no data";
		String temp = "no data";// This is a temperory variable used to match the date from one coordinate to
								// another
		String[] newdate = new String[20000];

		for (int i = -((ChartWidth / 2)); i <= (ChartWidth / 2); i = i + 1) {
			builder.moveToElement(rows, i, 1).build().perform();
			int myCount = driverManager.getDriver().findElements(uiDateSelector).size();
			if (myCount == 1)
				uiDate = driverManager.getDriver().findElement(uiDateSelector).getText();
			uiPrice = driverManager.getDriver().findElement(uiPriceSelector).getText();
			if (!uiDate.equalsIgnoreCase(temp) && (!uiPrice.equalsIgnoreCase("Price:"))) {
				temp = uiDate;
				newdate[j] = uiDate;
				ExtentLogger.info("UiDate = " + newdate[j] + " Price = " + uiPrice);
				j++;
			}
		}

		String url = readPropertyFile.get("api1DInterval");

		if (getExcelData("chart").equalsIgnoreCase("5Y")) {
			url = readPropertyFile.get("api1WInterval");
		}

		url = url.replace("<ticker>", ticker);
		driverManager.getDriver().switchTo().newWindow(WindowType.TAB);
		ArrayList<String> tabs = new ArrayList<>(driverManager.getDriver().getWindowHandles());
		driverManager.getDriver().switchTo().window(tabs.get(1)); // switches to new tab
		driverManager.getDriver().get(url);
		String a = driverManager.getDriver().findElement(By.tagName("pre")).getText();
		driverManager.getDriver().close();
		driverManager.getDriver().switchTo().window(tabs.get(0)); // switch back to main screen

		/*
		 * ChromeOptions options = new ChromeOptions(); options.addArguments("--headless"); WebDriver driver1 = new ChromeDriver(options); driver1.get(url);
		 */
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(a);
		JSONArray jsonArray = (JSONArray) jsonObject.get("dates");

		// Iterating the contents of the array

		@SuppressWarnings("unchecked")
		Iterator<String> iterator = jsonArray.iterator();
		String[] formatdate = new String[20000];
		int e = 0;
		while (iterator.hasNext()) {
			String Resultmasterid = iterator.next();
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			Date varDate1 = dateFormat1.parse(Resultmasterid);
			dateFormat1 = new SimpleDateFormat("MMM dd, yyyy");
			String final_admitDT = dateFormat1.format(varDate1);
			formatdate[e] = final_admitDT;
			e++;
		}
		String[] arr = new String[5];
		int output = 0;
		arr[output] = ("Total dates in chart = " + (j));
		output++;
		arr[output] = ("Actual First Date = " + newdate[0]);
		output++;
		arr[output] = ("Actual Latest Date = " + newdate[(j - 1)]);
		output++;
		arr[output] = ("Expected First Date = " + formatdate[j]);
		output++;
		arr[output] = ("Expected Latest Date = " + formatdate[1]);
		ExtentLogger.info(arr[0] + "<br>" + arr[1] + "<br>" + arr[2] + "<br>" + arr[3] + "<br>" + arr[4]);
		return newdate[0].equalsIgnoreCase(formatdate[j]) && newdate[(j - 1)].equalsIgnoreCase(formatdate[1]);
	}

	// To convert number into million, billion
	public String truncateNumber(String value) {
		float floatNumber = Float.parseFloat(value.replace(",", ""));
		long million = 1000000L;
		long billion = 1000000000L;
		long number = Math.round(floatNumber);
		if ((number >= million) && (number < billion)) {
			float fraction = calculateFraction(number, million);
			return fraction + "m";
		} else if (number >= billion) {
			float fraction = calculateFraction(number, billion);
			return fraction + "b";
		}
		return Long.toString(number);
	}

	public float calculateFraction(long number, long divisor) {
		long truncate = (number * 100L + (divisor / 2L)) / divisor;
		float fraction = (float) truncate * 0.01F;
		fraction = Math.round(fraction * 100) / 100F;
		return fraction;
	}

	public JsonPath authApiCall(String username, String password) throws Exception {
		HashMap<String, String> data = new HashMap<>();
		data.put("email", username);
		data.put("password", password);
		baseURI = readPropertyFile.get("baseURI");
		JsonPath jsonPath = given().header("X-Api-Key", readPropertyFile.get("X-Api-Key")).header("X-App-Id", readPropertyFile.get("X-App-Id")).body(data).contentType("application/json").when().post("/user/authenticate").jsonPath();
		return jsonPath;
	}

	// This function will return the APi response of the articles.
	public JsonPath articlesApiCall(String ticker) throws Exception {
		HashMap<String, String[]> data = new HashMap<>();
		data.put("symbols", new String[] { ticker });
		baseURI = readPropertyFile.get("baseURI");
		JsonPath jsonPath = given().header("X-Api-Key", readPropertyFile.get("X-Api-Key")).header("X-App-Id", readPropertyFile.get("X-App-Id")).header("X-Session-Id", getIds("sessionKey")).body(data).contentType("application/json").when().post("/articles").jsonPath();
		return jsonPath;
	}

	public JsonPath getjwtAuthorizationApiResponse(String url, String sessionToken){
		return given().header("jwtToken", sessionToken).when().get(url).jsonPath();
	}

	public String dateFormat(String date) {
		// This is for inception_date - Quick Stats - ETF
		if (date.contains(" ")) {
			String[] format_date = date.split(" ", 2);
			format_date = format_date[0].split("-", 3);
			return (format_date[1] + "/" + format_date[2] + "/" + format_date[0]);
		}
		// This is for Ex Dividend Date - Quick Stats - Stocks
		DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive()
				// use "dd" for day of month and "yyyy" for year
				.appendPattern("yyyyMMdd").toFormatter(Locale.ENGLISH);
		LocalDate localDate = LocalDate.parse(date, inputFormatter);

		// use "dd" for day of month and "yyyy" for year
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
		String formattedString = localDate.format(outputFormatter);
		return formattedString;
	}

	public String millionBillionConversion(String value) {
		double data = Double.parseDouble(value);
		int intPart = (int) data;
		if (intPart < 1000)
			value = "$" + String.format("%.2f", data) + "m";
		else {
			data = data / 1000;
			value = "$" + String.format("%.2f", data) + "b";
		}
		return value;
	}

	public String getExcelData(String columnName) {
		return excelReader.getCellData("symbol", columnName, 2);
	}

	public void navigateToBackPage() {
		driverManager.getDriver().navigate().back();
	}

	public JsonPath makeGetApiCall(String url) throws Exception {
		RequestSpecification requestSpecification = given();
		requestSpecification.header("Uuid", readPropertyFile.get("email")).header("Jsessionid", getIds("jsessionId")).header("X-Api-Key", readPropertyFile.get("X-Api-Key")).header("X-App-Id", readPropertyFile.get("X-App-Id")).header("X-Session-Id", getIds("sessionKey"));
		Response response = requestSpecification.when().get(url);
		// response.then().log().body();
		return response.jsonPath();
	}

	public JsonPath makePostApiCall(String url, Map<String, String> headers, Map<String, String> data) throws Exception {
		RequestSpecification requestSpecification = given();
		requestSpecification.header("Uuid", readPropertyFile.get("email")).header("Jsessionid", getIds("jsessionId")).header("X-Api-Key", readPropertyFile.get("X-Api-Key")).header("X-App-Id", readPropertyFile.get("X-App-Id")).header("X-Session-Id", getIds("sessionKey"));
		if (headers != null) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				requestSpecification.header(entry.getKey(), entry.getValue());
			}
		}
		if (data != null) {
			requestSpecification.body(data).contentType("application/json");
		}
		return requestSpecification.when().post(url).jsonPath();
	}

	// This function will return the APi response of the suggestion for Price Movement section on Health check report page.
	public JsonPath suggestionApiCall(String url, List<String> list) throws Exception {
		HashMap<String, Object> data = new HashMap<>();
		data.put("symbols", list);
		data.put("sortDirection", "asc");
		data.put("sortField", "symbol");
		JsonPath jsonPath = given().header("X-Api-Key", readPropertyFile.get("X-Api-Key")).header("X-App-Id", readPropertyFile.get("X-App-Id")).header("X-Session-Id", getIds("sessionKey")).body(data).contentType("application/json").when().post(url).jsonPath();
		return jsonPath;
	}

	// This function will return the APi response of the industryExposure section on Health check report page.
	public JsonPath industryExposureApiCall(String url, List<String> list) throws Exception {
		HashMap<String, Object> data = new HashMap<>();
		data.put("symbols", list);
		JsonPath jsonPath = given().header("X-Api-Key", readPropertyFile.get("X-Api-Key")).header("X-App-Id", readPropertyFile.get("X-App-Id")).header("X-Session-Id", getIds("sessionKey")).body(data).contentType("application/json").when().post(url).jsonPath();
		return jsonPath;
	}

	public void getPgrValue(JsonPath pgrApiRes, String pgrValue){
		String value;
		switch (pgrValue) {
			case "Neutral +", "Neutral -" -> {
				String raw_PGR = String.valueOf(pgrApiRes.getString("metaInfo[0].raw_PGR").substring(pgrApiRes.getString("metaInfo[0].raw_PGR").lastIndexOf(":") + 1).charAt(0));
				if (raw_PGR.equals("4") | raw_PGR.equals("5"))
					Assert.assertEquals(pgrValue, "Neutral +");
				else if (raw_PGR.equals("1") | raw_PGR.equals("2"))
					Assert.assertEquals(pgrValue, "Neutral -");
				value = raw_PGR;
			}
			default -> value = convertRating(pgrValue);
		}
		if (String.valueOf(pgrApiRes.getString("pgr[5]").substring(pgrApiRes.getString("pgr[5]").lastIndexOf(":") + 1).charAt(0)).equals("3"))
			Assert.assertEquals(value, String.valueOf(pgrApiRes.getString("metaInfo[0].raw_PGR").substring(pgrApiRes.getString("metaInfo[0].raw_PGR").lastIndexOf(":") + 1).charAt(0)));
		else
			Assert.assertEquals(value, pgrApiRes.getString("pgr[5]").substring(pgrApiRes.getString("pgr[5]").lastIndexOf(":") + 1).replace("]", ""));
	}

	public void validateLeftSideStockEtfSectionData(String symbol) throws Exception {
		HashMap<String, String> data = new HashMap<>();
		data.put("text", symbol);
		JsonPath searchApiRes = makePostApiCall(apiUrl.postSearchApiUrl(), null, data);
		int pgrRating = searchApiRes.getInt("data.suggestions.data[0].pgrRating");
		Boolean is_etf = searchApiRes.getBoolean("data.suggestions.data[0].isEtf");
		JsonPath pgrApiRes = makeGetApiCall(apiUrl.pgrPageApiUrl(symbol));

		// Validate Price, change, percentage
		// validatePriceChangePercentage(symbol);

		// for etf
		if (pgrRating > 0 && is_etf.equals(true)) {
			ExtentLogger.info("PGR Page for ETFs");
			// validateEtfChaikinPowerBarAndGroup(etfData);
		}
		// for stocks
		if (pgrRating > 0 && is_etf.equals(false)) {

			// For power rating bars validation: Financials
			validateFinancialsData(pgrApiRes);

			// For power rating bars validation: Earnings
			validateEarningsData(pgrApiRes);

			// For power rating bars validation: Technicals
			validateTechnicalsData(pgrApiRes);

			// For power rating bars validation: Experts
			validateExpertsData(pgrApiRes);
		}
	}

	// For power rating bars validation: Financials
	public void validateFinancialsData(JsonPath pgrApiRes) throws Exception {
		Map<String, String> uiSlidebar = getTextSlidebar("Financials");
		Assert.assertEquals(uiSlidebar.get("Financials"), String.valueOf(pgrApiRes.getString("pgr[1].Financials[0]").substring(pgrApiRes.getString("pgr[1].Financials[0]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("LT Debt to Equity"), String.valueOf(pgrApiRes.getString("pgr[1].Financials[1]").substring(pgrApiRes.getString("pgr[1].Financials[1]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Price to Book"), String.valueOf(pgrApiRes.getString("pgr[1].Financials[2]").substring(pgrApiRes.getString("pgr[1].Financials[2]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Return on Equity"), String.valueOf(pgrApiRes.getString("pgr[1].Financials[3]").substring(pgrApiRes.getString("pgr[1].Financials[3]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Price to Sales"), String.valueOf(pgrApiRes.getString("pgr[1].Financials[4]").substring(pgrApiRes.getString("pgr[1].Financials[4]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Free Cash Flow"), String.valueOf(pgrApiRes.getString("pgr[1].Financials[5]").substring(pgrApiRes.getString("pgr[1].Financials[5]").lastIndexOf(":") + 1).charAt(0)));
		// captureScreenshot();
	}

	// For power rating bars validation: Earnings
	public void validateEarningsData(JsonPath pgrApiRes) throws Exception {
		Map<String, String> uiSlidebar = getTextSlidebar("Earnings");
		Assert.assertEquals(uiSlidebar.get("Earnings"), String.valueOf(pgrApiRes.getString("pgr[2].Earnings[0]").substring(pgrApiRes.getString("pgr[2].Earnings[0]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Earnings Growth"), String.valueOf(pgrApiRes.getString("pgr[2].Earnings[1]").substring(pgrApiRes.getString("pgr[2].Earnings[1]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Earnings Surprise"), String.valueOf(pgrApiRes.getString("pgr[2].Earnings[2]").substring(pgrApiRes.getString("pgr[2].Earnings[2]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Earnings Trend"), String.valueOf(pgrApiRes.getString("pgr[2].Earnings[3]").substring(pgrApiRes.getString("pgr[2].Earnings[3]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Projected P/E"), String.valueOf(pgrApiRes.getString("pgr[2].Earnings[4]").substring(pgrApiRes.getString("pgr[2].Earnings[4]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Earnings Consistency"), String.valueOf(pgrApiRes.getString("pgr[2].Earnings[5]").substring(pgrApiRes.getString("pgr[2].Earnings[5]").lastIndexOf(":") + 1).charAt(0)));
		// captureScreenshot();
	}

	// For power rating bars validation: Technicals
	public void validateTechnicalsData(JsonPath pgrApiRes) throws Exception {
		Map<String, String> uiSlidebar = getTextSlidebar("Technicals");
		Assert.assertEquals(uiSlidebar.get("Technicals"), String.valueOf(pgrApiRes.getString("pgr[3].Technicals[0]").substring(pgrApiRes.getString("pgr[3].Technicals[0]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Rel Strength vs Mrkt"), String.valueOf(pgrApiRes.getString("pgr[3].Technicals[1]").substring(pgrApiRes.getString("pgr[3].Technicals[1]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Chaikin Money Flow"), String.valueOf(pgrApiRes.getString("pgr[3].Technicals[2]").substring(pgrApiRes.getString("pgr[3].Technicals[2]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Price Strength"), String.valueOf(pgrApiRes.getString("pgr[3].Technicals[3]").substring(pgrApiRes.getString("pgr[3].Technicals[3]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Price Trend ROC"), String.valueOf(pgrApiRes.getString("pgr[3].Technicals[4]").substring(pgrApiRes.getString("pgr[3].Technicals[4]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Volume Trend"), String.valueOf(pgrApiRes.getString("pgr[3].Technicals[5]").substring(pgrApiRes.getString("pgr[3].Technicals[5]").lastIndexOf(":") + 1).charAt(0)));
		// captureScreenshot();
	}

	// For power rating bars validation: Experts
	public void validateExpertsData(JsonPath pgrApiRes) throws Exception {
		Map<String, String> uiSlidebar = getTextSlidebar("Experts");
		Assert.assertEquals(uiSlidebar.get("Experts"), String.valueOf(pgrApiRes.getString("pgr[4].Experts[0]").substring(pgrApiRes.getString("pgr[4].Experts[0]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Estimate Trend"), String.valueOf(pgrApiRes.getString("pgr[4].Experts[1]").substring(pgrApiRes.getString("pgr[4].Experts[1]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Short Interest"), String.valueOf(pgrApiRes.getString("pgr[4].Experts[2]").substring(pgrApiRes.getString("pgr[4].Experts[2]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Insider Activity"), String.valueOf(pgrApiRes.getString("pgr[4].Experts[3]").substring(pgrApiRes.getString("pgr[4].Experts[3]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Analyst Rating Trend"), String.valueOf(pgrApiRes.getString("pgr[4].Experts[4]").substring(pgrApiRes.getString("pgr[4].Experts[4]").lastIndexOf(":") + 1).charAt(0)));
		Assert.assertEquals(uiSlidebar.get("Industry Rel Strength"), String.valueOf(pgrApiRes.getString("pgr[4].Experts[5]").substring(pgrApiRes.getString("pgr[4].Experts[5]").lastIndexOf(":") + 1).charAt(0)));
		// captureScreenshot();
	}

	// This function will return the Ui values of power rating bars visible on page.
	public Map<String, String> getTextSlidebar(String categoryName) throws Exception {
		Map<String, String> map = new HashMap<>();
		String key = null;
		String value = null;
		StringBuilder info = new StringBuilder();
		click(getTextSlidebarCategory(categoryName), categoryName);
		for (int i = 0; i <= 5; i++) {
			if (i >= 1) {
				key = getText(getTextSlidebarKey(categoryName, i));
				value = getAttribute(getTextSlidebarValue(categoryName, i), "class");
			} else {// This part is for category headers rating. Like, Financials, earnings, Experts, technical
				key = getText(getCategoryHeaderKey(categoryName));
				value = getAttribute(getCategoryHeaderValue(categoryName), "class");
			}
			String uiRating = value;
			if (!uiRating.equalsIgnoreCase("slider-disc ")) {
				String lastWord = value.substring(value.lastIndexOf(" ") + 1);
				String[] parts = lastWord.split("-");
				uiRating = parts[1];
			}
			value = convertRating(uiRating);
			map.put(key, value);
			info.append(key).append(" = ").append(value).append("<br>");
		}
		ExtentLogger.info("Slidebar rating of [" + categoryName + "]<br>" + info);
		return map;
	}

	public By getTextSlidebarCategory(String categoryName) {
		return By.xpath("//button//span[text()='" + categoryName + "']");
	}

	public By getTextSlidebarKey(String categoryName, int index) {
		return By.xpath("//span[text()='" + categoryName + "']/ancestor::div[@class='accordion-item']//div//div[contains(@class,'w-100 me-1')][" + index + "]//span");
	}

	public By getTextSlidebarValue(String categoryName, int index) {
		return By.xpath("//span[text()='" + categoryName + "']/ancestor::div[@class='accordion-item']//div//div[contains(@class,'w-100 me-1')][" + index + "]//div//div[1]");
	}

	public By getCategoryHeaderKey(String categoryName) {
		return By.xpath("//button//span[text()='" + categoryName + "']");
	}

	public By getCategoryHeaderValue(String categoryName) {
		return By.xpath("//button//span[text()='" + categoryName + "']/ancestor::h2[@class='accordion-header']//div//div//div[1]");
	}
}
