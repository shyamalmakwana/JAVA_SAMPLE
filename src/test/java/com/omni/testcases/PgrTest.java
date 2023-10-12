package com.omni.testcases;

import java.util.Hashtable;

import org.testng.annotations.Test;

import com.omni.base.BaseTest;
import com.omni.helpers.PgrHelper;
import com.omni.utilities.TestUtil;

public class PgrTest extends BaseTest {
	PgrHelper pGRHelper = new PgrHelper();

	/*
	 * Test for PGR page in OMNI application. this will start after the login. Enter stock/etf in the 'get quick rating' box, click on it, Validate the data, check for all the power rating bars. Check PGR page. logout
	 */
	@Test(priority = 1)
	public void testPgrPagebyValidatingQuickRatingSearchBox() throws Exception {
		pGRHelper.validatingQuickRatingSearchBox();
	}

	@Test(priority = 2)
	public void testFeaturedChaikinHotlist() throws Exception {
		pGRHelper.validateFeaturedChaikinHotlistData();
		pGRHelper.verifyChaikinHotlistViewAllListPage();
	}

	// Wordpress ApI getting 200 or not - To test page content.
	@Test(priority = 3)
	public void testWordpressApiTextVisibleOnUiOrNot() throws Exception {
		pGRHelper.validateTextVisibleOnPgrPage();
	}

	@Test(priority = 4, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testRecentlyViewedTickers(Hashtable<String, String> data) throws Exception {
		String[] symbolString = pGRHelper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		pGRHelper.clickOnPGRSearchBoxEnterSymbol(symbol);
		pGRHelper.checkSymbolFoundOrNot(symbol);
		pGRHelper.validateSearchedSymbolVisibleInRecentlyViewedSection(symbol);
	}

	@Test(priority = 5, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testSymbolDataAndSlidebarRating(Hashtable<String, String> data) throws Exception {
		String[] symbolString = pGRHelper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		pGRHelper.clickOnPGRSearchBoxEnterSymbol(symbol);
		pGRHelper.checkSymbolFoundOrNot(symbol);
		pGRHelper.validateStockETFPage(symbol);
		for (int pgrPage = 1; pgrPage <= 2; pgrPage++) { // to test slidebars rating for same symbol on second page
			pGRHelper.validatePriceChangePercentage(symbol);
			pGRHelper.validateMainPgrRating(symbol);
			pGRHelper.validateLeftSideStockEtfSectionData(symbol);
			if (pgrPage == 1)
				pGRHelper.navigateToPgrSecondPage();
			else
				pGRHelper.navigateToPgrFirstPage();
		}
	}

	@Test(priority = 6, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testChartFunctionality(Hashtable<String, String> data) throws Exception {
		String[] symbolString = pGRHelper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		pGRHelper.clickOnPGRSearchBoxEnterSymbol(symbol);
		pGRHelper.checkSymbolFoundOrNot(symbol);
		pGRHelper.navigateToPgrSecondPage();
		pGRHelper.validatePriceChangePercentage(symbol);
		pGRHelper.validateChartData(symbol,data.get("chart"));
		pGRHelper.navigateToPgrFirstPage();
	}

	@Test(priority = 7, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testChecklistSection(Hashtable<String, String> data) throws Exception {
		String[] symbolString = pGRHelper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		pGRHelper.clickOnPGRSearchBoxEnterSymbol(symbol);
		pGRHelper.checkSymbolFoundOrNot(symbol);
		pGRHelper.navigateToPgrSecondPage();
		pGRHelper.validateChecklistStrengthTimings(symbol);
		pGRHelper.navigateToPgrFirstPage();
	}

	@Test(priority = 8, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testHoldingSection(Hashtable<String, String> data) throws Exception {
		String[] symbolString = pGRHelper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		pGRHelper.clickOnPGRSearchBoxEnterSymbol(symbol);
		pGRHelper.checkSymbolFoundOrNot(symbol);
		pGRHelper.navigateToPgrSecondPage();
		pGRHelper.validateEtfHoldings(symbol);
		pGRHelper.navigateToPgrFirstPage();
	}

	@Test(priority = 9, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testArticlesSection(Hashtable<String, String> data) throws Exception {
		String[] symbolString = pGRHelper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		pGRHelper.clickOnPGRSearchBoxEnterSymbol(symbol);
		pGRHelper.checkSymbolFoundOrNot(symbol);
		pGRHelper.navigateToPgrSecondPage();
		pGRHelper.validateArticlesOnPgrPage(symbol);
		pGRHelper.navigateToPgrFirstPage();
	}

	@Test(priority = 10, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testQuickStatsSection(Hashtable<String, String> data) throws Exception {
		String[] symbolString = pGRHelper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		pGRHelper.clickOnPGRSearchBoxEnterSymbol(symbol);
		pGRHelper.checkSymbolFoundOrNot(symbol);
		pGRHelper.navigateToPgrSecondPage();
		pGRHelper.validateQuickStatsSection(symbol);
		pGRHelper.navigateToPgrFirstPage();
	}

	@Test(priority = 11, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testStocksLike(Hashtable<String, String> data) throws Exception {
		String[] symbolString = pGRHelper.pickRandomSymbol(data);
		for (int index = 0; index < symbolString.length; index++) {
			String symbol = symbolString[index];
			pGRHelper.clickOnPGRSearchBoxEnterSymbol(symbol);
			pGRHelper.checkSymbolFoundOrNot(symbol);
			pGRHelper.navigateToPgrSecondPage();
			pGRHelper.validateStockEtfSimilarList(symbol);
			pGRHelper.navigateToPgrFirstPage();
		}
	}

}
