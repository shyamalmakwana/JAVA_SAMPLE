package com.omni.pages;

import org.openqa.selenium.By;

public class Factor20Page {

	// Selectors for 20 factor page ...
	public By Global_search = By.xpath("//button[contains(@class,'search')]");
	public By Global_search_input = By.xpath("//input[contains(@placeholder,'Search')]");

	// Selectors for 20 factor page chart...
	public By btnViewFullRating = By.xpath("//a[text()='View Full Rating Report']");

	public By stockRatingReportPage = By.xpath("//h1[text()='Stock Rating Report']");
	public By errorPage = By.xpath("//div[@class='error-page']");
	public By btnGoToHome = By.xpath("//button[text()='Go to home']");
	public By btnDataTableToggle = By.xpath("//input[@id='earnings-tables-switch']");
	public By headingGraphView = By.xpath("//h3[text()='5 Year Revenue & Earnings Growth']");
	public By headingReturn = By.xpath("//h4[text()='Return']");

	public By headingFinancials = By.xpath("//h2[text()='Financials:']");
	public By headingEarnings = By.xpath("//h2[text()='Earnings:']");
	public By headingExperts = By.xpath("//h2[text()='Experts:']");
	public By pgrValueHeading = By.xpath("//div[contains(@class,'panel-object')]//h1");

	public By clickChartTimePeriod(String timePeriod) {
		return By.xpath("//label[text()='" + timePeriod + "']");
	}

	public By ticker(String symbol) {
		return By.xpath("//span[text()='" + symbol + "']");
	}

	public By txtCategoryRating(String category) {
		return By.xpath("//h2[text()='" + category + "']//span");
	}

	public By trTextSlidebar(String category) {
		return By.xpath("//h2[text()='" + category + "']/ancestor::div[@class='twenty-factors-panel container']//div[contains(@class,'w-100 me-1')]");
	}

	public By tdTextSlidebarKey(String category, int index) {
		return By.xpath("//h2[text()='" + category + "']/ancestor::div[@class='twenty-factors-panel container']//div[contains(@class,'w-100 me-1')][" + index + "]//span");
	}

	public By tdTextSlidebarAttribute(String category, int index) {
		return By.xpath("//h2[text()='" + category + "']/ancestor::div[@class='twenty-factors-panel container']//div[contains(@class,'w-100 me-1')][" + index + "]//div//div[1]");
	}

	public By trEarnings(String category) {
		return By.xpath("//div[@class='print-hide']//td[text()='" + category + "']//ancestor::table//thead//tr[2]//th");
	}

	public By tdEarningsKey(String category, int index) {
		return By.xpath("//div[@class='print-hide']//td[text()='" + category + "']/ancestor::table//tr[2]//th[" + (index + 1) + "]");
	}

	public By tdEarningsValue(String category, int index) {
		return By.xpath("//div[@class='print-hide']//td[text()='" + category + "']/following-sibling::*[" + index + "]");
	}

	public By tdYear5EarningsKey(String category, int index) {
		return By.xpath("//div[@class='print-hide']//td[text()='" + category + "']/ancestor::table//thead//tr[2]//th[" + (index + 2) + "]");
	}

	public By trFinancialsTechnicalsExperts(String category) {
		return By.xpath("//h4[text()='" + category + "']/ancestor::table//tbody/tr");
	}

	public By tdFinancialsTechnicalsExpertsKey(String category, int index) {
		return By.xpath("//h4[text()='" + category + "']/ancestor::table//tbody/tr[" + index + "]/td[1]");
	}

	public By tdFinancialsTechnicalsExpertsValue(String category, int index) {
		return By.xpath("//h4[text()='" + category + "']/ancestor::table//tbody/tr[" + index + "]/td[2]");
	}

	public By trExperts(String category) {
		return By.xpath("//td[text()='" + category + "']/ancestor::table//tbody//tr[1]//td");
	}

	public By tdExpertsKey(String category, int index) {
		return By.xpath("//td[text()='" + category + "']/ancestor::table//tbody//tr[1]//td[" + (index + 1) + "]");
	}

	public By tdExpertsValue(String category, int index) {
		return By.xpath("//td[text()='" + category + "']/ancestor::tr//td[" + (index + 1) + "]");
	}

	public By tdExpertsShortInterest(String category) {
		return By.xpath("//td[text()='" + category + "']");
	}

	public By tdWallStreet(String category) {
		return By.xpath("//td[text()='" + category + "']/ancestor::tr//td[2]");
	}

}
