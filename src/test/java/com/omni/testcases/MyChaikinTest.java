package com.omni.testcases;

import java.util.Hashtable;

import org.testng.annotations.Test;

import com.omni.base.BaseTest;
import com.omni.helpers.MyChaikinHelper;
import com.omni.utilities.TestUtil;

public class MyChaikinTest extends BaseTest {
	MyChaikinHelper myChaikinHelper = new MyChaikinHelper();

	// This is to test the My Chaikin page and validate page heading
	@Test(priority = 1)
	public void testMyChaikinPagebyValidatingHeading(){
		myChaikinHelper.validateMyChaikinPage();
	}

	// This is to test the market indices left side on My chaikin page
	@Test(priority = 2)
	public void testMarketIndices() throws Exception {
		myChaikinHelper.validateUSMarketsOpenClose();
		myChaikinHelper.validateMarketIndicesList();
	}

	// This is to test the recently viewed ticker searched.
	@Test(priority = 3, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testRecentlyViewed(Hashtable<String, String> data) throws Exception {
		String[] symbolString = myChaikinHelper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		myChaikinHelper.validateRecentlyViewedSymbol(symbol);
	}

	// To test the My watchlist section
	@Test(priority = 4)
	public void testMyWatchlist() throws Exception {
		myChaikinHelper.validateMyChaikinList();
	}

	// To test the My watchlist section after click on View all watchlist.
	@Test(priority = 5)
	public void testViewAllWatchlistList() throws Exception {
		myChaikinHelper.clickViewAllWatchlist();
		myChaikinHelper.validateViewAllWatchlist();
	}

	// This is to test the Publication portfolios section.
	@Test(priority = 6)
	public void testPublicationPortfolios() throws Exception {
		myChaikinHelper.validatePublicationPortfolios();
	}

	// This is to test publication portfolios after click on View All Portfolios.
	@Test(priority = 7)
	public void testViewPortfoliosAsLists() throws Exception {
		myChaikinHelper.clickViewPortfoliosAsList();
		myChaikinHelper.validateViewAllPortfolios();
	}

//This is to test Chaikin Hotlist section and validate data
	@Test(priority = 8)
	public void testChaikinHotlist() throws Exception {
		myChaikinHelper.validateChaikinHotlist();
	}

	// This is to to test chain hoylist data after click on View All list.
	@Test(priority = 9)
	public void testViewAllChaikiHotlist() throws Exception {
		myChaikinHelper.clickViewAllHotlist();
		myChaikinHelper.validateViewAllHotlists("Chaikin Ideas");
	}

	// This is to test recently saved section
	@Test(priority = 10)
	public void testRecentlySaved() throws Exception {
		myChaikinHelper.validateRecentlySaved();
	}

	// This is to test all bookmarked saved articles on Issue&More page
	@Test(priority = 11)
	public void testViewAllSaved() throws Exception {
		myChaikinHelper.clickViewAllSaved();
		myChaikinHelper.validateViewAllSavedArticles();
	}

	// This is to test Chaikin Hotlist - All lists (inside Filter by Chaikin Hotlist) stock/Etf/holdings count on health-check report page.
	@Test(priority = 12)
	public void testFilterByChaikinHotlist() throws Exception {
		myChaikinHelper.validateMyChaikin_ViewAllListPage();
		myChaikinHelper.validateDataOfListsFilterByDropDown("Chaikin Hotlist");
	}

	// This is to test US Equity ETF - All lists (inside Filter By ETF List) stock/Etf/holdings count on health-check report page.
	@Test(priority = 13)
	public void testFilterByUSEquityEtf() throws Exception {
		myChaikinHelper.validateMyChaikin_ViewAllListPage();
		myChaikinHelper.validateDataOfListsFilterByDropDown("US Equity ETF");
	}

	/*
	 * This is to test Watchlist stock/Etf/holdings count on health-check report page. The test will start from My Chaikin>>View All list Page>>Click on first list name After navigating to health check page, it will count the list from dropdown. Select list one by one and validate data
	 * stocks/etf/holding/slidebar rating
	 */
	@Test(priority = 14, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void test_MyWatchlist_HealthCheck_StockEtfHoldingCount(Hashtable<String, String> data) throws Exception {
		myChaikinHelper.validateMyChaikin_ViewAllListPage();
		String etfListId = myChaikinHelper.selectListType(data.get("flow"));
		myChaikinHelper.clickOnFirstNameInList();
		myChaikinHelper.validate_MyWatchlist_StockEtfCount_HealthCheckPage(etfListId);
	}

	// This is to test price Movement section on health-check report page.
	@Test(priority = 15, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void test_PriceMovement_and_ValidateData_On_HealthCheckReportPage(Hashtable<String, String> data) throws Exception {
		myChaikinHelper.validateMyChaikin_ViewAllListPage();
		String etfListId =myChaikinHelper.selectListType(data.get("flow"));
		myChaikinHelper.clickOnFirstNameInList();
		myChaikinHelper.validate_MyWatchlist_PriceMovement_HealthCheckPage(etfListId);
	}

	// This is to test Industry exposure section and validate data from API on health-check report page.
	@Test(priority = 16, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void test_IndustryExposure_and_ValidateData_On_HealthCheckReportPage(Hashtable<String, String> data) throws Exception {
		myChaikinHelper.validateMyChaikin_ViewAllListPage();
		String etfListId =	myChaikinHelper.selectListType(data.get("flow"));
		myChaikinHelper.clickOnFirstNameInList();
		myChaikinHelper.validate_IndustryExposure_HealthCheckPage(etfListId);
	}

	// This is to test Alerts UI section and validate data from API on health-check report page.
	@Test(priority = 17, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void test_Alerts_and_ValidateData_On_HealthCheckReportPage(Hashtable<String, String> data) throws Exception {
		myChaikinHelper.validateMyChaikin_ViewAllListPage();
		String etfListId =myChaikinHelper.selectListType(data.get("flow"));
		myChaikinHelper.clickOnFirstNameInList();
		myChaikinHelper.validate_Alerts_HealthCheckPage(etfListId);
	}
}
