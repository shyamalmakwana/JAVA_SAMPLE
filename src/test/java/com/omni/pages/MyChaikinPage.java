package com.omni.pages;

import org.openqa.selenium.By;

import com.omni.utilities.ReadPropertyFile;

public class MyChaikinPage {
	ReadPropertyFile readPropertyFile = new ReadPropertyFile();

	public int payloadCountListViewPage() throws Exception {
		return Integer.parseInt(readPropertyFile.get("payloadWatchlistCount")); // payload count of watchlist Api on List View Page
	}

	public int payloadCountHealthCheckPage() throws Exception {
		return Integer.parseInt(readPropertyFile.get("payloadCountHealthCheckPage")); // payload count of Api on health check page
	}

	public int paginationCount() throws Exception {
		return Integer.parseInt(readPropertyFile.get("paginationCount"));
	}
	
	public int listCount() throws Exception {
		return Integer.parseInt(readPropertyFile.get("listCount"));
	}

	public String chaikinListName = "Chaikin Hotlist";

	public By headingMyChaikin = By.xpath("//div[@class='body-main']//h1[text()='My Chaikin']");
	public By headingPublicationPortfolios = By.xpath("//h3[text()='Publication Portfolios']");
	public By headingChaikinHotlists = By.xpath("//h3[text()='Chaikin Hotlists']");
	public By headingRecentlySaved = By.xpath("//h3[text()='Recently Saved']");
	public By btnViewAllWatchlists = By.xpath("//a[text()='View All Watchlists']");
	public By btnViewAllPortfolios = By.xpath("//a[text()='View Portfolios as Lists']");
	public By btnViewAllHotlists = By.xpath("//a[text()='View All Lists']");
	public By btnViewAllSaved = By.xpath("//a[text()='View All Saved']");
	public By headingListView = By.xpath("//h1[text()='List View']");

	public By myChaikinPage = By.xpath("//a[text()='My Chaikin']");
	public By btnViewAllList = By.xpath("//button[text()='View All Lists']");

	// x-path to select List Type on 'List View' Page
	public By rdoSelectListType(String listType) {
		return By.xpath("//div[contains(@class,'border-panel')][2]//input//following-sibling::label[text()='" + listType + "']");
	}

	public By ddlFilterByOptionCount = By.xpath("//div[@class='body-side py-2']/div[contains(@class,'border-panel')]/div[@class='filter-panel']//select//option");

	public By ddlFilterByOptionValue(int index) {
		return By.xpath("//div[@class='body-side py-2']/div[contains(@class,'border-panel')]/div[@class='filter-panel']//select//option[" + index + "]");
	}

	public By marketIndicesCount = By.xpath("//button[@class='list-group-item list-group-item-action']");
	public By marketStatusOpenClosed = By.xpath("//div[contains(@class,'market-status')]//span");
	public By myWatchlistCount = By.xpath("//div[contains(@class,'watchlist-panel')]//tbody//tr[@class='list-table-row h-100']");
	public By publicationPortfoliosCount = By.xpath("//div[contains(@class,'portfolio-list-panel')]//tbody//tr[@class='list-table-row h-100']");
	public By chaikinHotlistsCount = By.xpath("//div[contains(@class,'hotlist-panel')]//tbody//tr[@class='list-table-row h-100']");
	public By recentlySavedCount = By.xpath("//div[contains(@class,'saved-articles-panel')]//div[contains(@class,'col')]");
	public By paginationNextPage = By.xpath("//span[contains(@class,'next')]");
	public By matchingArticles = By.xpath("//h2//span[1]");// x-path of matching articles count on Issues&More Page
	public By msgNoWatchlistFound = By.xpath("//*[text()='No lists found' or contains(text(),'No Watchlists Found')]");
	public By msgNoPublicationPortfolios = By.xpath("//h3[text()='You have no publication portfolios.']");
	public By msgNoSavedArticles = By.xpath("//h3[text()='No saved articles']");
	public By rowsCount = By.xpath("//div[contains(@class,'main-panel')]//tbody//tr"); // This is the x-path of rows visible after click on View All button

	// xpaths of health check report page
	public By pieChartPanel = By.xpath("//div[@id='health-check-page']//div[@class='pie-chart-panel']");
	public By btnListType = By.xpath("//div[@class='row']//span[3]//button");
	public By btnListCategory = By.xpath("//div[@class='row']//span[5]//button");
	public By btnDropDownList = By.xpath("//div[@class='row']//button[@id='dropdown-list-button']");
	public By lstDropDown = By.xpath("//div[@class='dropdown-menu show dropdown-menu-end']");
	public By btnLoadMore = By.xpath("//div[@class='dropdown-menu show dropdown-menu-end']//a[text()='Load More...']");
	public By holdingsCount = By.xpath("//h4[text()='Holdings']/ancestor::div[contains(@class,'items')]//span//span");
	public By stocksCount = By.xpath("//h4[text()='Stocks']/ancestor::div[contains(@class,'items')]//span//span");
	public By etfsCount = By.xpath("//h4[text()='ETFs']/ancestor::div[contains(@class,'items')]//span//span");
	public By bullishCount = By.xpath("//span[contains(@class,'bullish')]//span");
	public By neutralCount = By.xpath("//span[contains(@class,'neutral')]//span");
	public By bearishCount = By.xpath("//span[contains(@class,'bearish')]//span");
	public By unratedCount = By.xpath("//div[contains(@class,'panel-object')]//b");
	public String txtLoadMore = "Load More...";
	public By spinLoader = By.xpath("//div[contains(@class,'spinner-border')]");

	public By ddlMenuOption(int index) {
		return By.xpath("//div[contains(@class,'dropdown-menu')]//a[" + index + "]");
	}

	// xpaths of My chaikin Page
	public By listName(int index) {
		return By.xpath("//tr[" + index + "]//div[contains(@class,'list-name')]//span");
	}

	public By changePct(int index) {
		return By.xpath("//tr[" + index + "]//span[@class='price boldest']");
	}

	public By holdingsStocks(int index) {
		return By.xpath("//div[contains(@class,'main-panel')][1]//tr[contains(@class,'list-table')][" + index + "]//td[3]//span[1]//b");
	}

	public By holdingsEtfs(int index) {
		return By.xpath("//div[contains(@class,'main-panel')][1]//tr[contains(@class,'list-table')][" + index + "]//td[3]//span[3]//b");
	}

	public By powerbarBullish(int index, String page) {
		if (page.equalsIgnoreCase("list-page"))
			return By.xpath("//tr[" + index + "]//td[contains(@class,'powerbar')]//span[3]//span");
		else // For Chaikn First Page
			return By.xpath("//div[contains(@class,'main-panel')][1]//tr[contains(@class,'list-table')][" + index + "]//td[4]//span[3]//span");
	}

	public By powerbarNeutral(int index, String page) {
		if (page.equalsIgnoreCase("list-page"))
			return By.xpath("//tr[" + index + "]//td[contains(@class,'powerbar')]//span[2]//span");
		else // For Chaikn First Page
			return By.xpath("//div[contains(@class,'main-panel')][1]//tr[contains(@class,'list-table')][" + index + "]//td[4]//span[2]//span");
	}

	public By powerbarBearish(int index, String page) {
		if (page.equalsIgnoreCase("list-page"))
			return By.xpath("//tr[" + index + "]//td[contains(@class,'powerbar')]//span[1]//span");
		else // For Chaikn First Page
			return By.xpath("//div[contains(@class,'main-panel')][1]//tr[contains(@class,'list-table')][" + index + "]//td[4]//span[1]//span");
	}

	public By publication(int index) {
		return By.xpath("//div[contains(@class,'portfolio-list-panel')]//tbody//tr[" + index + "]//td[1]//div");
	}

	public By publicationName(int index) {
		return By.xpath("//div[contains(@class,'portfolio-list-panel')]//tbody//tr[" + index + "]//td[2]//div");
	}

	public By publicationHoldingsStocks(int index) {
		return By.xpath("//div[contains(@class,'portfolio-list-panel')]//tbody//tr[" + index + "]//td[3]//span[1]//b");
	}

	public By publicationHoldingsEtf(int index) {
		return By.xpath("//div[contains(@class,'portfolio-list-panel')]//tbody//tr[" + index + "]//td[3]//span[3]//b");
	}

	public By publicationBullishPct(int index) {
		return By.xpath("//div[contains(@class,'portfolio-list-panel')]//tbody//tr[" + index + "]//td[4]//span//span");
	}

	public By publicationBullishCount(int index) {
		return By.xpath("//div[contains(@class,'portfolio-list-panel')]//tbody//tr[" + index + "]//td[5]//span[3]//span");
	}

	public By publicationNeutralCount(int index) {
		return By.xpath("//div[contains(@class,'portfolio-list-panel')]//tbody//tr[" + index + "]//td[5]//span[2]//span");
	}

	public By publicationBearishCount(int index) {
		return By.xpath("//div[contains(@class,'portfolio-list-panel')]//tbody//tr[" + index + "]//td[5]//span[1]//span");
	}

	public By chaikinHotlistName(int index) {
		return By.xpath("//div[contains(@class,'hotlist-panel')]//tbody//tr[" + index + "]//td[1]//div");
	}

	public By chaikinHotlistHoldingsStocks(int index) {
		return By.xpath("//div[contains(@class,'hotlist-panel')]//tbody//tr[" + index + "]//td[2]//span[1]//b");
	}

	public By chaikinHotlistHoldingsEtf(int index) {
		return By.xpath("//div[contains(@class,'hotlist-panel')]//tbody//tr[" + index + "]//td[2]//span[3]//b");
	}

	public By chaikinHotlistBullishPct(int index) {
		return By.xpath("//div[contains(@class,'hotlist-panel')]//tbody//tr[" + index + "]//td[3]//span//span");
	}

	public By chaikinHotlistBullishCount(int index) {
		return By.xpath("//div[contains(@class,'hotlist-panel')]//tbody//tr[" + index + "]//td[4]//span[3]//span");
	}

	public By chaikinHotlistNeutralCount(int index) {
		return By.xpath("//div[contains(@class,'hotlist-panel')]//tbody//tr[" + index + "]//td[4]//span[2]//span");
	}

	public By chaikinHotlistBearishCount(int index) {
		return By.xpath("//div[contains(@class,'hotlist-panel')]//tbody//tr[" + index + "]//td[4]//span[1]//span");
	}

	public By recentlySavedTitle(int index) {
		return By.xpath("//div[contains(@class,'saved-articles-panel')]//div[contains(@class,'col')][" + index + "]//h1//a");
	}

	public By recentlySavedAnalystName(int index) {
		return By.xpath("//div[contains(@class,'saved-articles-panel')]//div[contains(@class,'col')][" + index + "]//h4[2]//a");
	}

	public By ticker_name(int index) {
		return By.xpath("//button[" + index + "]//span[@class='ticker-name']");
	}

	public By price(int index) {
		return By.xpath("//button[" + index + "]//span[contains(text(),'$')]");
	}

	public By change(int index) {
		return By.xpath("//button[" + index + "]//span[@class='change']");
	}

	// xpath for My Chainkin>>My Watchlist>>View All Watchlist page
	public By holdings(int index) {
		return By.xpath("//tr[" + index + "]//td[contains(@class,'holdings')]//div");
	}

	public By performance1Y(int index) {
		return By.xpath("//tr[" + index + "]//td[contains(@class,'performance')][1]//span");
	}

	// xpath Price Movement
	public By priceMovementRowsCount = By.xpath("//h2[text()='Price Movement:']/ancestor::div[@class='list-results-panel']//tbody//tr");

	public By priceMovementSymbol(int index) {
		return By.xpath("//h2[text()='Price Movement:']/ancestor::div[@class='list-results-panel']//tbody//tr[" + index + "]//td[2]//div");
	}

	public By priceMovementCompanyName(int index) {
		return By.xpath("//h2[text()='Price Movement:']/ancestor::div[@class='list-results-panel']//tbody//tr[" + index + "]//td[3]//div");
	}

	public By priceMovementPrevLast(int index) {
		return By.xpath("//h2[text()='Price Movement:']/ancestor::div[@class='list-results-panel']//tbody//tr[" + index + "]//td[4]//div");
	}

	public By priceMovementChange(int index) {
		return By.xpath("//h2[text()='Price Movement:']/ancestor::div[@class='list-results-panel']//tbody//tr[" + index + "]//td[5]//div");
	}

	public By priceMovementMktCap(int index) {
		return By.xpath("//h2[text()='Price Movement:']/ancestor::div[@class='list-results-panel']//tbody//tr[" + index + "]//td[6]//div");
	}

	public By priceMovement(int index) {
		return By.xpath("//h2[text()='Price Movement:']/ancestor::div[@class='list-results-panel']//tbody//tr[" + index + "]//td[7]//span");
	}

	public By pctChangePriceMovement = By.xpath("//h2[text()='Price Movement:']//span//span");// This is for Price Movement:<pctChnage> over the last.
	public By ddTimePeriodPriceMovement = By.xpath("//h2[text()='Price Movement:']/ancestor::div[@class='list-results-panel']//select[@aria-label='comparison-benchmark']//option");
	public By btnSymbol = By.xpath("//button[text()='Symbol']");

	public By ddlTimePriceMovement(int index) {
		return By.xpath("//h2[text()='Price Movement:']/ancestor::div[@class='list-results-panel']//select[@aria-label='comparison-benchmark']//option[" + index + "]");
	}

	// x-path Industry Exposure
	public By industryExposureRowsCount = By.xpath("//h2[text()='Industry Exposure']/ancestor::div[@class='industry-exposure-panel']//tbody//tr");

	public By industryName(int index) {
		return By.xpath("//h2[text()='Industry Exposure']/ancestor::div[@class='industry-exposure-panel']//tbody//tr[" + index + "]//td[2]//div");
	}

	public By industryStrength(int index) {
		return By.xpath("//h2[text()='Industry Exposure']/ancestor::div[@class='industry-exposure-panel']//tbody//tr[" + index + "]//td[3]//div");
	}

	public By industryExposure(int index) {
		return By.xpath("//h2[text()='Industry Exposure']/ancestor::div[@class='industry-exposure-panel']//tbody//tr[" + index + "]//td[4]//span[contains(@class,'exposure')]");
	}

	public By industrySymbols(int index) {
		return By.xpath("//h2[text()='Industry Exposure']/ancestor::div[@class='industry-exposure-panel']//tbody//tr[" + index + "]//td[5]//span[@class='ticker']");
	}

	// x-path Alerts
	public By alertsCount = By.xpath("//div[contains(@class,'active')]//div[@role='button']");
	public By noAlerts = By.xpath("//*[text()='No alerts found.']");
	public By btnBearish = By.xpath("//button[contains(text(),'Bearish')]");
	public By ratingChange = By.xpath("//span[text()='Rating Change']");

	public By alertSymbol(int index) {
		return By.xpath("//div[contains(@class,'active')]//div[@role='button'][" + index + "]//div[2]//span[1]");
	}

	public By alertEstimateRevisionSurpriseText(int index) {
		return By.xpath("//div[contains(@class,'active')]//div[@role='button'][" + index + "]//div[3]//span");
	}

	public By alertEstimateRevisionSurpriseOld(int index) {
		return By.xpath("//div[contains(@class,'active')]//div[@role='button'][" + index + "]//span//span[1]//span");
	}

	public By alertEstimateRevisionSurpriseNew(int index) {
		return By.xpath("//div[contains(@class,'active')]//div[@role='button'][" + index + "]//span//span[2]//span");
	}

	public By alertEarningsSurprisePctChange(int index) {
		return By.xpath("//div[contains(@class,'active')]//div[@role='button'][" + index + "]//span//span[3]");
	}
}
