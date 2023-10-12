package com.omni.pages;

import org.openqa.selenium.By;

public class PgrPage {

	// Navbar->PGR Page Button
	public By btnQuickRating = By.xpath("//input[@placeholder='Get a quick rating']");
	public By headingAboutOurRating = By.xpath("//h2[text()='About Our Rating']");
	public By btnViewAllList = By.xpath("//a[text()='View All Lists']");
	public By rdoChaikinHotlist = By.xpath("//div[contains(@class,'border-panel')][2]//input[@checked]//following-sibling::label[text()='Chaikin Hotlist']");
	public By lastPrice = By.xpath("//span[contains(@class,'header--price')][1]");
	public By change = By.xpath("//span[contains(@class,'header--price')][2]");
	public By percentage = By.xpath("//span[contains(@class,'header--price')][3]");

	public By actualRecentTicker = By.xpath("//table[@class='recent-table table']//tbody//tr[1]//td[2]//p[1]");
	public By checklist = By.xpath("//h4[text()='Checklist for ']");

	public By pgr = By.xpath("//span[text()='Power Gauge Rating']/following-sibling::span[1]");
	public By relStrength = By.xpath("//span[text()='Rel. Strength vs SPY']/following-sibling::span[1]");
	public By overbrought = By.xpath("//span[text()='Overbought/ Oversold']/following-sibling::span[1]");
	public By ltStrength = By.xpath("//span[text()='Long Term Trend']/following-sibling::span[1]");
	public By moneyFlow = By.xpath("//span[text()='Money Flow Persist.']/following-sibling::span[1]");
	public By strengthCount = By.xpath("//div[2]//div[2]//div[@class='tally-count']");
	public By timingCount = By.xpath("//div[4]//div[2]//div[@class='tally-count']");
	public By groupStrength = By.xpath("//span[text()='Group Strength']/following-sibling::span[1]");
	public By industryStrength = By.xpath("//span[text()='Industry Strength']/following-sibling::span[1]");

	public By bearish = By.xpath("//div[@class='flex-cell powerbar-wrapper align-items-center mt-1']//span[1]//span[@class='powerbar-count']");
	public By neutral = By.xpath("//div[@class='flex-cell powerbar-wrapper align-items-center mt-1']//span[2]//span[@class='powerbar-count']");
	public By bullish = By.xpath("//div[@class='flex-cell powerbar-wrapper align-items-center mt-1']//span[3]//span[@class='powerbar-count']");
	public By etfGroup = By.xpath("//h4[text()='ETF Group:']/following-sibling::h2");

	// visit etf website | heading - Earnings
	public By etfWebsite = By.xpath("//h4[text()='Earnings'] |//a[contains(text(),'Visit ETF Website')]");

	public By seeMore = By.xpath("//button[text()='See More']");
	public By seeAll = By.xpath("//button[contains(text(),'See All ')]");
	public By btnClose = By.xpath("//button[@aria-label='Close']|//button[@class='btn-close']");
	public By symbolColumn = By.xpath("//button[text()='Symbol']");

	public By rating = By.xpath("//tr[@class='ticker-row'][1]//img");
	public By symbol_name = By.xpath("//tr[@class='ticker-row'][1]//p[contains(@class,'symbol')]");
	public By price = By.xpath("//tr[@class='ticker-row'][1]//p[contains(@class,'data')]");
	public By allocation = By.xpath("//tr[@class='ticker-row'][1]//td[4]");
	public By company = By.xpath("//tr[@class='ticker-row'][1]//p[contains(@class,'company')]");

	// View Full Rating Report| heading - ETF Group:
	public By headingViewFullRatingReport = By.xpath("//a[text()='View Full Rating Report'] | //h4[text()='ETF Group:']");
	public By countArticles = By.xpath("//div[@class='issues-list-item- py-2']");

	public By pgrValueHeading = By.xpath("//div[contains(@class,'panel-object')]//h1");
	public By hotList_count = By.xpath("//tr[@class='list-table-row h-100']");

	public By noResult = By.xpath("//div[@class='no-results']//h3");
	public By containingHotListCount = By.xpath("//div[contains(@class,'tile list')]");
	public By stockSimilarListCount = By.xpath("//div[@class='tile stock-tile']");
	public By stockSimilarListTitle = By.xpath("//section[contains(@class,'tile-list')]//h2");
	public By headingQuickStats = By.xpath("//h2[text()='Quick Stats']");
	public By headerTicker = By.xpath("//h2[@class='widget__header--ticker']");

	public By clickChartTimePeriod(String timePeriod) {
		return By.xpath("//label[text()='" + timePeriod + "']");
	}

	public By articleTitle(int index) {
		return By.xpath("//h2[text()='Articles/Reports Mentioning']/ancestor::div//div[@class='list-group-item'][" + index + "]//h1");
	}

	public By author(int index) {
		return By.xpath("//h2[text()='Articles/Reports Mentioning']/ancestor::div//div[@class='list-group-item'][" + index + "]//h4//a[1]");
	}

	public By product(int index) {
		return By.xpath("//h2[text()='Articles/Reports Mentioning']/ancestor::div//div[@class='list-group-item'][" + index + "]//h4//a[2]");
	}

	public By rating(int index) {
		return By.xpath("//div[@class='tile stock-tile'][" + index + "]//img");
	}

	public By symbol(int index) {
		return By.xpath("//div[@class='tile stock-tile'][" + index + "]//h3");
	}

	public By company(int index) {
		return By.xpath("//div[@class='tile stock-tile'][" + index + "]//div[2]");
	}

	public By price(int index) {
		return By.xpath("//div[@class='tile stock-tile'][" + index + "]//span[1]");
	}

	public By percentageChange(int index) {
		return By.xpath("//div[@class='tile stock-tile'][" + index + "]//span[2]");
	}

	public By industry(int index) {
		return By.xpath("//div[@class='tile stock-tile'][" + index + "]//div[contains(@class,'industry')]");
	}

	public By getTextHotlistCategoryName(int index) {
		return By.xpath("//tr[@class='list-table-row h-100'][" + index + "]//td[1]//div");
	}

	public By getTextHotlistListName(String categoryName) {
		return By.xpath("//*[text()='" + categoryName + "']/ancestor::tr//td[1]//div");
	}

	public By getTextHotlistHoldings(String categoryName) {
		return By.xpath("//*[text()='" + categoryName + "']/ancestor::tr//td[2]//b");
	}

	public By getTextHotlistBullishPercentage(String categoryName) {
		return By.xpath("//*[text()='" + categoryName + "']/ancestor::tr//td[3]//span//span");
	}

	public By getTextHotlistBullishCount(String categoryName) {
		return By.xpath("//*[text()='" + categoryName + "']/ancestor::tr//td[4]//span[contains(@class,'bullish')]//span");
	}

	public By getTextHotlistNeutralCount(String categoryName) {
		return By.xpath("//*[text()='" + categoryName + "']/ancestor::tr//td[4]//span[contains(@class,'neutral')]//span");
	}

	public By getTextHotlistBearishCount(String categoryName) {
		return By.xpath("//*[text()='" + categoryName + "']/ancestor::tr//td[4]//span[contains(@class,'bearish')]//span");
	}

	public By getTextQuickStatsCount(String categoryName) {
		return By.xpath("//th[text()='" + categoryName + "']/ancestor::table//tbody/tr");
	}

	public By getTextQuickStatsKey(String categoryName, int index) {
		return By.xpath("//th[text()='" + categoryName + "']/ancestor::table//tbody/tr[" + index + "]/td[1]");
	}

	public By getTextQuickStatsValue(String categoryName, int index) {
		return By.xpath("//th[text()='" + categoryName + "']/ancestor::table//tbody/tr[" + index + "]/td[2]");
	}

	public By listType(int index) {
		return By.xpath("//div[contains(@class,'tile list')][" + index + "]//a");
	}

	public By listName(int index) {
		return By.xpath("//div[contains(@class,'tile list')][" + index + "]//h3");
	}

	public By greenCount(int index) {
		return By.xpath("//div[contains(@class,'tile list')][" + index + "]//span[3]//span");
	}

	public By yellowCount(int index) {
		return By.xpath("//div[contains(@class,'tile list')][" + index + "]//span[2]//span");
	}

	public By redCount(int index) {
		return By.xpath("//div[contains(@class,'tile list')][" + index + "]//span[1]//span[@class='powerbar-count']");
	}

	public By symbol(String symbol) {
		return By.xpath("//span[text()='" + symbol + "']");
	}

	public By headingSymbol(String symbol) {
		return By.xpath("//h2[text()='" + symbol + "']");
	}

}