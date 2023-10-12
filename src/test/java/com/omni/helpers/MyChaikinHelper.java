package com.omni.helpers;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import org.testng.Assert;
import com.omni.base.ApiBaseUrl;
import com.omni.base.BasePage;
import com.omni.pages.MyChaikinPage;
import com.omni.reports.ExtentLogger;
import io.restassured.path.json.JsonPath;

public class MyChaikinHelper {
	Factor20Helper factor20Helper = new Factor20Helper();
	PgrHelper pGRHelper = new PgrHelper();
	MyChaikinPage chaikinPage = new MyChaikinPage();
	BasePage basePage = new BasePage();
	ApiBaseUrl apiUrl = new ApiBaseUrl();
	// int index = 1;
	int listCount;
	int apiIndex;

	// This is to validate U.S. markets are Open or Closed.
	public void validateMyChaikinPage() {
		basePage.verifyElement(chaikinPage.headingMyChaikin, "'My Chaikin' Heading");
	}

	// This is to validate U.S. markets are Open or Closed.
	public void validateUSMarketsOpenClose() throws Exception {
		JsonPath powerFeedMarketviewApiResponse = powerFeedMarketViewApiCall();
		basePage.verifyElement(chaikinPage.marketIndicesCount, "Market Indices");
		String marketStatus = basePage.getText(chaikinPage.marketStatusOpenClosed);
		if (powerFeedMarketviewApiResponse.getBoolean("isMarketOpen"))
			Assert.assertEquals("Open", marketStatus);
		else
			Assert.assertEquals("Closed", marketStatus);
	}

	// This is to validate the data for Market Indices List on My Chaikin page.
	public void validateMarketIndicesList() throws Exception {
		JsonPath powerFeedMarketviewApiResponse = powerFeedMarketViewApiCall();
		int count = basePage.getSize(chaikinPage.marketIndicesCount);
		for (int index = 1; index <= count; index++) {
			Assert.assertEquals(basePage.getText(chaikinPage.ticker_name(index)), powerFeedMarketviewApiResponse.getString("data[" + (index - 1) + "].name") + "(" + powerFeedMarketviewApiResponse.getString("data[" + (index - 1) + "].Symbol") + ")");
			Assert.assertEquals(basePage.getText(chaikinPage.price(index)), "$" + String.format("%.2f", powerFeedMarketviewApiResponse.getDouble("data[" + (index - 1) + "].Last")));
			Assert.assertEquals(basePage.getText(chaikinPage.change(index)), String.format("%.2f", powerFeedMarketviewApiResponse.getDouble("data[" + (index - 1) + "].\"Percentage \"")) + "%");
		}
	}

	// This is to validate the data for MyChaikin List on My Chaikin page.
	public void validateMyChaikinList() throws Exception {
		if (!basePage.verifyElement(chaikinPage.msgNoWatchlistFound, "No Watchlist Found")) {
			JsonPath watchlistsApiResponse = watchlistsApiCall();
			int count = basePage.getSize(chaikinPage.myWatchlistCount);
			for (int index = 1; index <= count; index++) {
				for (apiIndex = 0; apiIndex <= count - 1; apiIndex++) {
					if (basePage.getText(chaikinPage.listName(index)).equalsIgnoreCase(watchlistsApiResponse.getString("data.data[" + apiIndex + "].listName")))
						break;
				}
				Assert.assertEquals(basePage.getText(chaikinPage.listName(index)), watchlistsApiResponse.getString("data.data[" + apiIndex + "].listName"));
				Assert.assertEquals(basePage.getText(chaikinPage.changePct(index)), String.format("%.2f", watchlistsApiResponse.getDouble("data.data[" + apiIndex + "].months1ChangePct")) + "%");
				Assert.assertEquals(basePage.getText(chaikinPage.holdingsStocks(index)), Integer.toString(watchlistsApiResponse.getInt("data.data[" + apiIndex + "].stockCount")));
				Assert.assertEquals(basePage.getText(chaikinPage.holdingsEtfs(index)), Integer.toString(watchlistsApiResponse.getInt("data.data[" + apiIndex + "].etfCount")));
				Assert.assertEquals(basePage.getText(chaikinPage.powerbarBullish(index, "chaikin page")), Integer.toString(watchlistsApiResponse.getInt("data.data[" + apiIndex + "].greenCount")));
				Assert.assertEquals(basePage.getText(chaikinPage.powerbarNeutral(index, "chaikin page")), Integer.toString(watchlistsApiResponse.getInt("data.data[" + apiIndex + "].yellowCount")));
				Assert.assertEquals(basePage.getText(chaikinPage.powerbarBearish(index, "chaikin page")), Integer.toString(watchlistsApiResponse.getInt("data.data[" + apiIndex + "].redCount")));
			}
		} else
			Assert.assertTrue(basePage.verifyElement(chaikinPage.msgNoWatchlistFound, "No Watchlists Found"));
	}

	// This will click on 'View All Watchlist' button on 'My watchlist' section.
	public void clickViewAllWatchlist() throws Exception {
		basePage.click(chaikinPage.btnViewAllWatchlists, "View All Watchlist");
	}

	// After navigating on 'All list view', this will this will validate data for 'Watchlist' section.
	public void validateViewAllWatchlist() throws Exception {
		int count = chaikinPage.payloadCountListViewPage();
		int paginationCount = chaikinPage.paginationCount();
		Assert.assertTrue(basePage.verifyElement(chaikinPage.rdoSelectListType("Watchlist"), "Watchlist radio button Selected"));
		if (!basePage.verifyElement(chaikinPage.msgNoWatchlistFound, "No Watchlist Found")) {
			JsonPath viewAllWatchlistsApiResponse = viewAllWatchlistsApiCall(Integer.toString(count), "1");
			int totalListCount = viewAllWatchlistsApiResponse.getInt("data.count");
			// evaluate pagination count.
			if (paginationCount > (totalListCount / count)) {
				paginationCount = (totalListCount / count) + 1;
				if (totalListCount % count == 0)
					paginationCount = paginationCount - 1;
			}
			for (int page = 1; page <= paginationCount; page++) {
				viewAllWatchlistsApiResponse = viewAllWatchlistsApiCall(Integer.toString(count), Integer.toString(page));
				int rowsCount = basePage.getSize(chaikinPage.rowsCount);
				for (int index = 1; index <= rowsCount; index++) {
					for (apiIndex = 0; apiIndex <= rowsCount - 1; apiIndex++) {
						if (basePage.getText(chaikinPage.listName(index)).equalsIgnoreCase(viewAllWatchlistsApiResponse.getString("data.data[" + apiIndex + "].listName")))
							break;
					}
					Assert.assertEquals(basePage.getText(chaikinPage.listName(index)), viewAllWatchlistsApiResponse.getString("data.data[" + apiIndex + "].listName"));
					Assert.assertEquals(basePage.getText(chaikinPage.holdings(index)), Integer.toString(viewAllWatchlistsApiResponse.getInt("data.data[" + apiIndex + "].totalCount")));
					Assert.assertEquals(basePage.getText(chaikinPage.powerbarBullish(index, "list-page")), Integer.toString(viewAllWatchlistsApiResponse.getInt("data.data[" + apiIndex + "].greenCount")));
					Assert.assertEquals(basePage.getText(chaikinPage.powerbarNeutral(index, "list-page")), Integer.toString(viewAllWatchlistsApiResponse.getInt("data.data[" + apiIndex + "].yellowCount")));
					Assert.assertEquals(basePage.getText(chaikinPage.powerbarBearish(index, "list-page")), Integer.toString(viewAllWatchlistsApiResponse.getInt("data.data[" + apiIndex + "].redCount")));
					Assert.assertEquals(basePage.getText(chaikinPage.performance1Y(index)), String.format("%.2f", viewAllWatchlistsApiResponse.getDouble("data.data[" + apiIndex + "].years1ChangePct")) + "%");
				}
				if (paginationCount > page) {
					basePage.scroll(chaikinPage.paginationNextPage);
					basePage.click(chaikinPage.paginationNextPage, "Pagination Next page");
					basePage.scroll(chaikinPage.headingListView);
				}
			}
		} else
			Assert.assertTrue(basePage.verifyElement(chaikinPage.msgNoWatchlistFound, "No Watchlists Found"));
	}

	// This is to validate the data for Publication Portfolios on My Chaikin page.
	public void validatePublicationPortfolios() throws Exception {
		basePage.scroll(chaikinPage.headingPublicationPortfolios);
		if (!basePage.verifyElement(chaikinPage.msgNoPublicationPortfolios, "No publication portfolios")) {
			JsonPath publicationsApiResponse = publicationsApiCall();
			int count = basePage.getSize(chaikinPage.publicationPortfoliosCount);
			for (int index = 1; index <= count; index++) {
				for (apiIndex = 0; apiIndex <= count - 1; apiIndex++) {
					if (basePage.getText(chaikinPage.publication(index)).equalsIgnoreCase(publicationsApiResponse.getString("data.data[" + apiIndex + "].listName")))
						break;
				}
				Assert.assertEquals(basePage.getText(chaikinPage.publication(index)), publicationsApiResponse.getString("data.data[" + (apiIndex) + "].listName"));
				Assert.assertEquals(basePage.getText(chaikinPage.publicationName(index)), publicationsApiResponse.getString("data.data[" + apiIndex + "].publicationName"));
				Assert.assertEquals(basePage.getText(chaikinPage.publicationHoldingsStocks(index)), Integer.toString(publicationsApiResponse.getInt("data.data[" + apiIndex + "].stockCount")));
				Assert.assertEquals(basePage.getText(chaikinPage.publicationHoldingsEtf(index)), Integer.toString(publicationsApiResponse.getInt("data.data[" + apiIndex + "].etfCount")));
				Assert.assertEquals(basePage.getText(chaikinPage.publicationBullishPct(index)), String.format("%.2f", publicationsApiResponse.getDouble("data.data[" + apiIndex + "].bullPercentage")) + "%");
				Assert.assertEquals(basePage.getText(chaikinPage.publicationBullishCount(index)), Integer.toString(publicationsApiResponse.getInt("data.data[" + apiIndex + "].greenCount")));
				Assert.assertEquals(basePage.getText(chaikinPage.publicationNeutralCount(index)), Integer.toString(publicationsApiResponse.getInt("data.data[" + apiIndex + "].yellowCount")));
				Assert.assertEquals(basePage.getText(chaikinPage.publicationBearishCount(index)), Integer.toString(publicationsApiResponse.getInt("data.data[" + apiIndex + "].redCount")));
			}
		} else
			Assert.assertTrue(basePage.verifyElement(chaikinPage.msgNoPublicationPortfolios, "No publication portfolios"));
	}

	public void clickViewPortfoliosAsList() throws Exception {
		basePage.verifyElement(chaikinPage.btnViewAllPortfolios, "View Portfolios as Lists");
		basePage.scroll(chaikinPage.headingMyChaikin);
		basePage.click(chaikinPage.btnViewAllPortfolios, "View Portfolios as Lists");
	}

	public void validateViewAllPortfolios() throws Exception {
		int count = chaikinPage.payloadCountListViewPage();
		int paginationCount = chaikinPage.paginationCount();
		Assert.assertTrue(basePage.verifyElement(chaikinPage.rdoSelectListType("Publication"), "Publications radio button Selected"));
		if (!basePage.verifyElement(chaikinPage.msgNoWatchlistFound, "No Watchlist Found")) {
			JsonPath viewAllPortfoliosApiResponse = viewAllPublicationApiCall(Integer.toString(count), "1");
			int totalListCount = viewAllPortfoliosApiResponse.getInt("data.count");
			// evaluate pagination count.
			if (paginationCount > (totalListCount / count)) {
				paginationCount = (totalListCount / count) + 1;
				if (totalListCount % count == 0)
					paginationCount = paginationCount - 1;
			}
			for (int page = 1; page <= paginationCount; page++) {
				viewAllPortfoliosApiResponse = viewAllPublicationApiCall(Integer.toString(count), Integer.toString(page));
				int rowsCount = basePage.getSize(chaikinPage.rowsCount);
				for (int index = 1; index <= rowsCount; index++) {
					for (apiIndex = 0; apiIndex <= rowsCount - 1; apiIndex++) {
						if (basePage.getText(chaikinPage.listName(index)).equalsIgnoreCase(viewAllPortfoliosApiResponse.getString("data.data[" + apiIndex + "].listName")))
							break;
					}
					Assert.assertEquals(basePage.getText(chaikinPage.listName(index)), viewAllPortfoliosApiResponse.getString("data.data[" + apiIndex + "].listName"));
					Assert.assertEquals(basePage.getText(chaikinPage.holdings(index)), Integer.toString(viewAllPortfoliosApiResponse.getInt("data.data[" + apiIndex + "].totalCount")));
					Assert.assertEquals(basePage.getText(chaikinPage.powerbarBullish(index, "list-page")), Integer.toString(viewAllPortfoliosApiResponse.getInt("data.data[" + apiIndex + "].greenCount")));
					Assert.assertEquals(basePage.getText(chaikinPage.powerbarNeutral(index, "list-page")), Integer.toString(viewAllPortfoliosApiResponse.getInt("data.data[" + apiIndex + "].yellowCount")));
					Assert.assertEquals(basePage.getText(chaikinPage.powerbarBearish(index, "list-page")), Integer.toString(viewAllPortfoliosApiResponse.getInt("data.data[" + apiIndex + "].redCount")));
					Assert.assertEquals(basePage.getText(chaikinPage.performance1Y(index)), String.format("%.2f", viewAllPortfoliosApiResponse.getDouble("data.data[" + apiIndex + "].years1ChangePct")) + "%");
				}
				if (paginationCount > page) {
					basePage.scroll(chaikinPage.paginationNextPage);
					basePage.click(chaikinPage.paginationNextPage, "Pagination Next page");
					basePage.scroll(chaikinPage.headingListView);
				}
			}
		} else
			Assert.assertTrue(basePage.verifyElement(chaikinPage.msgNoWatchlistFound, "No Watchlists Found"));
	}

	// This is to validate the data for Chaikin Hotlist on My Chaikin page.
	public void validateChaikinHotlist() throws Exception {
		basePage.scroll(chaikinPage.headingChaikinHotlists);
		JsonPath chaikinHotlistApiResponse = chaikinHotlistApiCall();
		int count = basePage.getSize(chaikinPage.chaikinHotlistsCount);
		int apiIndex;
		for (int index = 1; index <= count; index++) {
			for (apiIndex = 0; apiIndex <= count - 1; apiIndex++) {
				if (basePage.getText(chaikinPage.chaikinHotlistName(index)).equalsIgnoreCase(chaikinHotlistApiResponse.getString("data.data[" + apiIndex + "].listName")))
					break;
			}
			Assert.assertEquals(basePage.getText(chaikinPage.chaikinHotlistName(index)), chaikinHotlistApiResponse.getString("data.data[" + (apiIndex) + "].listName"));
			Assert.assertEquals(basePage.getText(chaikinPage.chaikinHotlistHoldingsStocks(index)), Integer.toString(chaikinHotlistApiResponse.getInt("data.data[" + apiIndex + "].stockCount")));
			Assert.assertEquals(basePage.getText(chaikinPage.chaikinHotlistHoldingsEtf(index)), Integer.toString(chaikinHotlistApiResponse.getInt("data.data[" + apiIndex + "].etfCount")));
			Assert.assertEquals(basePage.getText(chaikinPage.chaikinHotlistBullishPct(index)), String.format("%.2f", chaikinHotlistApiResponse.getDouble("data.data[" + apiIndex + "].bullPercentage")) + "%");
			Assert.assertEquals(basePage.getText(chaikinPage.chaikinHotlistBullishCount(index)), Integer.toString(chaikinHotlistApiResponse.getInt("data.data[" + apiIndex + "].greenCount")));
			Assert.assertEquals(basePage.getText(chaikinPage.chaikinHotlistNeutralCount(index)), Integer.toString(chaikinHotlistApiResponse.getInt("data.data[" + apiIndex + "].yellowCount")));
			Assert.assertEquals(basePage.getText(chaikinPage.chaikinHotlistBearishCount(index)), Integer.toString(chaikinHotlistApiResponse.getInt("data.data[" + apiIndex + "].redCount")));
		}
	}

	public void clickViewAllHotlist() throws Exception {
		basePage.scroll(chaikinPage.btnViewAllPortfolios);
		basePage.click(chaikinPage.btnViewAllHotlists, "View All Hotlists");
	}

	public void validateViewAllHotlists(String category) throws Exception {
		int count = chaikinPage.payloadCountListViewPage();
		int paginationCount = chaikinPage.paginationCount();
		JsonPath viewAllHotlistsApiResponse = viewAllHotlistApiCall(Integer.toString(count), "1", category);
		int totalListCount = viewAllHotlistsApiResponse.getInt("data.count");

		// evaluate pagination count.
		if (paginationCount > (totalListCount / count)) {
			paginationCount = (totalListCount / count) + 1;
			if (totalListCount % count == 0)
				paginationCount = paginationCount - 1;
		}
		for (int page = 1; page <= paginationCount; page++) {
			viewAllHotlistsApiResponse = viewAllHotlistApiCall(Integer.toString(count), Integer.toString(page), category);
			int rowsCount = basePage.getSize(chaikinPage.rowsCount);
			for (int index = 1; index <= rowsCount; index++) {
				for (apiIndex = 0; apiIndex <= rowsCount - 1; apiIndex++) {
					if (basePage.getText(chaikinPage.listName(index)).equalsIgnoreCase(viewAllHotlistsApiResponse.getString("data.data[" + apiIndex + "].listName")))
						break;
				}
				Assert.assertEquals(basePage.getText(chaikinPage.listName(index)), viewAllHotlistsApiResponse.getString("data.data[" + apiIndex + "].listName"));
				Assert.assertEquals(basePage.getText(chaikinPage.holdings(index)), Integer.toString(viewAllHotlistsApiResponse.getInt("data.data[" + apiIndex + "].totalCount")));
				Assert.assertEquals(basePage.getText(chaikinPage.powerbarBullish(index, "list-page")), Integer.toString(viewAllHotlistsApiResponse.getInt("data.data[" + apiIndex + "].greenCount")));
				Assert.assertEquals(basePage.getText(chaikinPage.powerbarNeutral(index, "list-page")), Integer.toString(viewAllHotlistsApiResponse.getInt("data.data[" + apiIndex + "].yellowCount")));
				Assert.assertEquals(basePage.getText(chaikinPage.powerbarBearish(index, "list-page")), Integer.toString(viewAllHotlistsApiResponse.getInt("data.data[" + apiIndex + "].redCount")));
				Assert.assertEquals(basePage.getText(chaikinPage.performance1Y(index)), String.format("%.2f", viewAllHotlistsApiResponse.getDouble("data.data[" + apiIndex + "].years1ChangePct")) + "%");
			}
			if (paginationCount > page) {
				basePage.scroll(chaikinPage.paginationNextPage);
				basePage.click(chaikinPage.paginationNextPage, "Pagination Next page");
				basePage.scroll(chaikinPage.headingListView);
			}
		}
	}

	public void validateViewAllEtfs(String etfListId) throws Exception {
		int count = chaikinPage.payloadCountListViewPage();
		int paginationCount = chaikinPage.paginationCount();
		JsonPath viewAllEtfApiResponse = viewAllEtfApiCall(Integer.toString(count), "1", etfListId);
		int totalListCount = viewAllEtfApiResponse.getInt("data.count");
		// evaluate pagination count.
		if (paginationCount > (totalListCount / count)) {
			paginationCount = (totalListCount / count) + 1;
			if (totalListCount % count == 0)
				paginationCount = paginationCount - 1;
		}
		for (int page = 1; page <= paginationCount; page++) {
			viewAllEtfApiResponse = viewAllEtfApiCall(Integer.toString(count), Integer.toString(page), etfListId);
			int rowsCount = basePage.getSize(chaikinPage.rowsCount);
			for (int index = 1; index <= rowsCount; index++) {
				for (apiIndex = 0; apiIndex <= rowsCount - 1; apiIndex++) {
					if (basePage.getText(chaikinPage.listName(index)).equalsIgnoreCase(viewAllEtfApiResponse.getString("data.data[" + apiIndex + "].listName")))
						break;
				}
				Assert.assertEquals(basePage.getText(chaikinPage.listName(index)), viewAllEtfApiResponse.getString("data.data[" + apiIndex + "].listName"));
				Assert.assertEquals(basePage.getText(chaikinPage.holdings(index)), Integer.toString(viewAllEtfApiResponse.getInt("data.data[" + apiIndex + "].totalCount")));
				Assert.assertEquals(basePage.getText(chaikinPage.powerbarBullish(index, "list-page")), Integer.toString(viewAllEtfApiResponse.getInt("data.data[" + apiIndex + "].greenCount")));
				Assert.assertEquals(basePage.getText(chaikinPage.powerbarNeutral(index, "list-page")), Integer.toString(viewAllEtfApiResponse.getInt("data.data[" + apiIndex + "].yellowCount")));
				Assert.assertEquals(basePage.getText(chaikinPage.powerbarBearish(index, "list-page")), Integer.toString(viewAllEtfApiResponse.getInt("data.data[" + apiIndex + "].redCount")));
				if (viewAllEtfApiResponse.get("data.data[" + apiIndex + "].years1ChangePct") != null) {
					Assert.assertEquals(basePage.getText(chaikinPage.performance1Y(index)), String.format("%.2f", viewAllEtfApiResponse.getDouble("data.data[" + apiIndex + "].years1ChangePct")) + "%");
				}
			}
			if (paginationCount > page) {
				basePage.scroll(chaikinPage.paginationNextPage);
				basePage.click(chaikinPage.paginationNextPage, "Pagination Next page");
				basePage.scroll(chaikinPage.headingListView);
			}
		}
	}

	// This will validate the searched symbol should be visible in Recently viewed section.
	public void validateRecentlyViewedSymbol(String symbol) throws Exception {
		factor20Helper.clickOnSearchedSymbol(symbol);
		basePage.click(chaikinPage.myChaikinPage, "My Chaikin");
		pGRHelper.validateSearchedSymbolVisibleInRecentlyViewedSection(symbol);
	}

	public void validateRecentlySaved() throws Exception {
		basePage.scroll(chaikinPage.headingRecentlySaved);
		if (!basePage.verifyElement(chaikinPage.msgNoSavedArticles, "No saved articles")) {
			JsonPath bookmarkRecentlySavedApiResponse = bookmarkRecentlySavedApiCall();
			int count = basePage.getSize(chaikinPage.recentlySavedCount);
			int apiIndex;
			for (int index = 1; index <= count; index++) {
				for (apiIndex = 0; apiIndex <= count - 1; apiIndex++) {
					if (basePage.getText(chaikinPage.recentlySavedTitle(index)).equalsIgnoreCase(bookmarkRecentlySavedApiResponse.getString("data.data[" + apiIndex + "].title")))
						break;
				}
				Assert.assertEquals(basePage.getText(chaikinPage.recentlySavedTitle(index)), bookmarkRecentlySavedApiResponse.getString("data.data[" + (apiIndex) + "].title"));
				Assert.assertEquals(basePage.getText(chaikinPage.recentlySavedAnalystName(index)), bookmarkRecentlySavedApiResponse.getString("data.data[" + (apiIndex) + "].analysts[0].name"));
			}
		} else
			Assert.assertTrue(basePage.verifyElement(chaikinPage.msgNoSavedArticles, "No saved articles"));
	}

	public void clickViewAllSaved() throws Exception {
		basePage.scroll(chaikinPage.btnViewAllSaved);
		basePage.click(chaikinPage.btnViewAllSaved, "View All Saved");
	}

	public void validateViewAllSavedArticles() throws Exception {
		JsonPath articleApiResponse = postArticleApiCall();
		int totalListCount = articleApiResponse.getInt("data.matchCount");
		Assert.assertEquals(basePage.getText(chaikinPage.matchingArticles), Integer.toString(totalListCount));
	}

	public void validateMyChaikin_ViewAllListPage() throws Exception {
		basePage.click(chaikinPage.btnViewAllList, "View All List");
		Assert.assertTrue(basePage.verifyElement(chaikinPage.rdoSelectListType("Watchlist"), "Watchlist"));
	}

	// This method will click on first name (if any) in list and navigate to health-check report page
	public void clickOnFirstNameInList() throws Exception {
		basePage.verifyElement(chaikinPage.paginationNextPage, "Pagination");
		int rowsCount = basePage.getSize(chaikinPage.rowsCount);
		if (rowsCount > 0) {
			basePage.click(chaikinPage.listName(1), "First Name in List");
			Assert.assertTrue(basePage.verifyElement(chaikinPage.pieChartPanel, "Health Check report page"));
		} else
			Assert.assertTrue(basePage.verifyElement(chaikinPage.msgNoWatchlistFound, "No Watchlists Found"));
	}

	// Click on radio button and select List Type on 'List View' Page
	public String selectListType(String listType) throws Exception {
		String etfListId = null;
		basePage.click(chaikinPage.rdoSelectListType(listType), listType);
		if (listType.equalsIgnoreCase("Chaikin Hotlist") | listType.equalsIgnoreCase("US Equity ETF")) {
			int count = basePage.getSize(chaikinPage.ddlFilterByOptionCount);// List drop-down options count.
			int random = (int) (Math.random() * count + 1);
			etfListId = basePage.getAttribute(chaikinPage.ddlFilterByOptionValue(random), "value");
			basePage.click(chaikinPage.ddlFilterByOptionValue(random), "Select option from list");
			ExtentLogger.info("[" + listType + " - " + basePage.getText(chaikinPage.ddlFilterByOptionValue(random)) + "] selected.");
		}
		return etfListId;
	}

	public void validateDataOfListsFilterByDropDown(String listType) throws Exception {
		basePage.click(chaikinPage.rdoSelectListType(listType), listType);
		int count = basePage.getSize(chaikinPage.ddlFilterByOptionCount);
		String etfListId = null;
		for (int index = 1; index <= count; index++) {
			String category = basePage.getText(chaikinPage.ddlFilterByOptionValue(index));
			basePage.click(chaikinPage.ddlFilterByOptionValue(index), "Select option from list");
			if (!category.equalsIgnoreCase("Featured Bulls & Bears")) { // "This list not handled yet. '&' not passing in restAssured API. Need to work on this."
				if (listType.equalsIgnoreCase(chaikinPage.chaikinListName))
					validateViewAllHotlists(category);
				else // for "US Equity ETF" List
				{
					etfListId = basePage.getAttribute(chaikinPage.ddlFilterByOptionValue(index), "value");
					validateViewAllEtfs(etfListId);
				}
			} else {
				ExtentLogger.info("[" + category + "] not handled yet. '&' symbol not passing in restAssured Api in Automation. Test Manually.");
			}
		}
	}

	// This method will click on one by one list in drop-down and validate stock/etf/holding count and pgr slidebar left side on Health Check Report page.
	public void validate_MyWatchlist_StockEtfCount_HealthCheckPage(String etfListId) throws Exception {
		int index = 1;
		int count = chaikinPage.payloadCountHealthCheckPage();
		JsonPath viewAllWatchlistsApiResponse = getListTypeApiResponse(Integer.toString(count), Integer.toString(1), etfListId);
		listCount = getListCount(viewAllWatchlistsApiResponse);

		do {
			int page = selectListFromDropdownAndEvaluatePageCount(index, count);
			viewAllWatchlistsApiResponse = getListTypeApiResponse(Integer.toString(count), Integer.toString(page), etfListId);
			int apiIndex = getApiIndex(count, viewAllWatchlistsApiResponse);

			Assert.assertEquals(basePage.getText(chaikinPage.holdingsCount), Integer.toString(viewAllWatchlistsApiResponse.getInt("data.data[" + (apiIndex) + "].totalCount")));
			Assert.assertEquals(basePage.getText(chaikinPage.stocksCount), viewAllWatchlistsApiResponse.getString("data.data[" + (apiIndex) + "].stockCount"));
			Assert.assertEquals(basePage.getText(chaikinPage.etfsCount), viewAllWatchlistsApiResponse.getString("data.data[" + (apiIndex) + "].etfCount"));
			Assert.assertEquals(basePage.getText(chaikinPage.bullishCount), viewAllWatchlistsApiResponse.getString("data.data[" + (apiIndex) + "].greenCount"));
			Assert.assertEquals(basePage.getText(chaikinPage.neutralCount), viewAllWatchlistsApiResponse.getString("data.data[" + (apiIndex) + "].yellowCount"));
			Assert.assertEquals(basePage.getText(chaikinPage.bearishCount), viewAllWatchlistsApiResponse.getString("data.data[" + (apiIndex) + "].redCount"));
			Assert.assertEquals(basePage.getText(chaikinPage.unratedCount), viewAllWatchlistsApiResponse.getString("data.data[" + (apiIndex) + "].invalidCnt") + " unrated");
			ExtentLogger.info("List = <b> [" + basePage.getText(chaikinPage.btnDropDownList) + "]</b> validated successfully.");
			index++;
		} while (index <= listCount);
	}

	// This method will click on one by one list in drop-down and validate price moments with different time-periods on Health Check Report page.
	public void validate_MyWatchlist_PriceMovement_HealthCheckPage(String etfListId) throws Exception {
		int index = 1;
		int count = chaikinPage.payloadCountHealthCheckPage();
		JsonPath viewAllWatchlistsApiResponse = getListTypeApiResponse(Integer.toString(count), Integer.toString(1), etfListId);
		listCount = getListCount(viewAllWatchlistsApiResponse);

		do {
			int page = selectListFromDropdownAndEvaluatePageCount(index, count);
			viewAllWatchlistsApiResponse = getListTypeApiResponse(Integer.toString(count), Integer.toString(page), etfListId);
			int apiIndex = getApiIndex(count, viewAllWatchlistsApiResponse);
			int watchlistApiIndex = apiIndex;
			// click on 'Symbol' column for sorting.
			basePage.click(chaikinPage.btnSymbol, "Symbol");

			// Response from suggestions Api
			List<String> symbols = viewAllWatchlistsApiResponse.getList("data.data[" + apiIndex + "].symbols");
			JsonPath suggestionsApiResponse = postSuggestionApiCall(symbols);

			// Loop for time period, will select different time period
			int timePeriodOptions = basePage.getSize(chaikinPage.ddTimePeriodPriceMovement);
			int priceMovementRowCount = basePage.getSize(chaikinPage.priceMovementRowsCount);
			if (!basePage.getText(chaikinPage.holdingsCount).equalsIgnoreCase("0")) {
				do {
					basePage.click(chaikinPage.ddlTimePriceMovement(timePeriodOptions), basePage.getText(chaikinPage.ddlTimePriceMovement(timePeriodOptions)));
					String timePeriodValue = basePage.getAttribute(chaikinPage.ddlTimePriceMovement(timePeriodOptions), "value");
					for (int rowCount = 1; rowCount <= priceMovementRowCount; rowCount++) {
						for (apiIndex = 0; apiIndex < priceMovementRowCount; apiIndex++) {
							if (basePage.getText(chaikinPage.priceMovementSymbol(rowCount)).equalsIgnoreCase(suggestionsApiResponse.getString("data.data[" + apiIndex + "].symbol")))
								break;
						}
						Assert.assertEquals(basePage.getText(chaikinPage.priceMovementSymbol(rowCount)), suggestionsApiResponse.getString("data.data[" + apiIndex + "].symbol"));
						Assert.assertEquals(basePage.getText(chaikinPage.priceMovementCompanyName(rowCount)), suggestionsApiResponse.getString("data.data[" + apiIndex + "].name"));
						Assert.assertEquals(basePage.getText(chaikinPage.priceMovementPrevLast(rowCount)), "$" + String.format("%.2f", suggestionsApiResponse.getDouble("data.data[" + apiIndex + "].latestClosePrice")));
						Assert.assertEquals(basePage.getText(chaikinPage.priceMovementChange(rowCount)).replace("+", ""), String.format("%.2f", suggestionsApiResponse.getDouble("data.data[" + apiIndex + "].latestChangePct")) + "%");
						Assert.assertEquals(basePage.getText(chaikinPage.priceMovementMktCap(rowCount)), basePage.millionBillionConversion(String.valueOf(suggestionsApiResponse.getDouble("data.data[" + apiIndex + "].marketCap"))));
						Assert.assertEquals(basePage.getText(chaikinPage.priceMovement(rowCount)).replace("+", ""), (suggestionsApiResponse.get("data.data[" + apiIndex + "]." + timePeriodValue) == null) ? "0.00%"
								: String.format("%.2f", suggestionsApiResponse.getDouble("data.data[" + apiIndex + "]." + timePeriodValue)) + "%");
						Assert.assertEquals(basePage.getText(chaikinPage.pctChangePriceMovement).replace("+", ""), String.format("%.2f", viewAllWatchlistsApiResponse.getDouble("data.data[" + watchlistApiIndex + "]." + timePeriodValue)) + "%");
					}
					ExtentLogger.info("List <b> [" + basePage.getText(chaikinPage.btnDropDownList) + "]</b> validated successfully for price movement [" + basePage.getText(chaikinPage.ddlTimePriceMovement(timePeriodOptions)) + "].");
					timePeriodOptions--;
				} while (timePeriodOptions != 0);
			} else {
				ExtentLogger.info("List <b> [" + basePage.getText(chaikinPage.btnDropDownList) + "]</b> = No symbols found ");
			}
			index++;
		} while (index <= listCount);
	}

	// This method will click on one by one list in drop-down and validate Industry Exposure data on Health Check Report page.
	public void validate_IndustryExposure_HealthCheckPage(String etfListId) throws Exception {
		int index = 1;
		int count = chaikinPage.payloadCountHealthCheckPage();
		JsonPath viewAllWatchlistsApiResponse = getListTypeApiResponse(Integer.toString(count), Integer.toString(1), etfListId);
		listCount = getListCount(viewAllWatchlistsApiResponse);
		do {
			int page = selectListFromDropdownAndEvaluatePageCount(index, count);
			viewAllWatchlistsApiResponse = getListTypeApiResponse(Integer.toString(count), Integer.toString(page), etfListId);
			int apiIndex = getApiIndex(count, viewAllWatchlistsApiResponse);

			// Response from industry Exposure Api
			List<String> listId = viewAllWatchlistsApiResponse.getList("data.data[" + apiIndex + "].symbols");
			JsonPath industryExposureApiResponse = postIndustryExposureApiCall(listId);
			int industryExposureRowsCount = basePage.getSize(chaikinPage.industryExposureRowsCount);
			if (industryExposureRowsCount != 0) {
				for (int rowCount = 1; rowCount <= industryExposureRowsCount; rowCount++) {
					for (apiIndex = 0; apiIndex < industryExposureRowsCount; apiIndex++) {
						String listName = industryExposureApiResponse.getString("data[" + apiIndex + "].listName");
						if (basePage.getText(chaikinPage.industryName(rowCount)).equalsIgnoreCase(listName))
							break;
					}
					Assert.assertEquals(basePage.getText(chaikinPage.industryName(rowCount)), industryExposureApiResponse.getString("data[" + apiIndex + "].listName"));
					Assert.assertEquals(basePage.getText(chaikinPage.industryStrength(rowCount)), industryExposureApiResponse.getString("data[" + apiIndex + "].strength"));
					Assert.assertEquals(basePage.getText(chaikinPage.industryExposure(rowCount)), String.valueOf(industryExposureApiResponse.getInt("data[" + apiIndex + "].exposureCount")));
					int uiSymbolsListCount = basePage.getSize(chaikinPage.industrySymbols(rowCount));
					List<Object> apiSymbolsList = industryExposureApiResponse.getList("data[" + apiIndex + "].matchedSymbols");
					Assert.assertEquals(uiSymbolsListCount, apiSymbolsList.size());
				}
				ExtentLogger.info("List <b> [" + basePage.getText(chaikinPage.btnDropDownList) + "]</b> validated successfully for Industry Exposure");
			} else {
				ExtentLogger.info("List <b> [" + basePage.getText(chaikinPage.btnDropDownList) + "]</b> = No industry exposure found ");
			}
			index++;
		} while (index <= listCount);
	}

	// This method will click on one by one list in drop-down and validate Industry Exposure data on Health Check Report page.
	public void validate_Alerts_HealthCheckPage(String etfListId) throws Exception {
		int index = 1;
		String alertType = "Bullish";
		int count = chaikinPage.payloadCountHealthCheckPage();
		JsonPath viewAllWatchlistsApiResponse = getListTypeApiResponse(Integer.toString(count), Integer.toString(1), etfListId);
		listCount = getListCount(viewAllWatchlistsApiResponse);
		do {
			int page = selectListFromDropdownAndEvaluatePageCount(index, count);
			viewAllWatchlistsApiResponse = getListTypeApiResponse(Integer.toString(count), Integer.toString(page), etfListId);
			int apiIndex = getApiIndex(count, viewAllWatchlistsApiResponse);
			// Response from alerts Api
			int listId = viewAllWatchlistsApiResponse.getInt("data.data[" + apiIndex + "].listId");
			if (basePage.getSize(chaikinPage.noAlerts) != 1) {
				for (int k = 1; k <= 2; k++) {
					JsonPath alertsApiResponse = getAllAlertsApiCall(listId);
					int alertsCount = basePage.getSize(chaikinPage.alertsCount);
					if (alertsCount != 0) {
						for (int rowCount = 1; rowCount <= alertsCount; rowCount++) {
							String key = "PGR";
							// For Estimate Revision
							if (basePage.getText(chaikinPage.alertEstimateRevisionSurpriseText(rowCount)).equals("Estimate Revision")) {
								key = "EarningEstimate"; // For alerts having estimate revision
							}
							// For Earning Surprise
							if (basePage.getText(chaikinPage.alertEstimateRevisionSurpriseText(rowCount)).equals("Earnings Surprise")) {
								key = "EarningSurprise"; // For alerts having earing surprise
							}
							if (!key.equalsIgnoreCase("PGR") && alertType.equalsIgnoreCase("Bullish")) {
								// For Bearish alerts - shifting the api index.
								apiIndex = (rowCount - 1);
								int value;
								do {
									value = alertsApiResponse.getInt(key + "[" + apiIndex + "].Value");
									if (value <= 2) {
										apiIndex++;
									}
								} while (value <= 2);
							}
							if (!key.equalsIgnoreCase("PGR") && alertType.equalsIgnoreCase("Bearish")) {
								// For Bearish alerts - shifting the api index.
								apiIndex = (rowCount - 1);
								int value;
								do {
									value = alertsApiResponse.getInt(key + "[" + apiIndex + "].Value");
									if (value >= 4) {
										apiIndex++;
									}
								} while (value >= 4);
							}
							if (key.equalsIgnoreCase("PGR")) {
								int ratingChangeCount = basePage.getSize(chaikinPage.ratingChange);
								for (apiIndex = 0; apiIndex <= ratingChangeCount; apiIndex++) {
									if (basePage.getText(chaikinPage.alertSymbol(rowCount)).equalsIgnoreCase(alertsApiResponse.getString(key + "[" + apiIndex + "].Symbol")))
										break;
								}
							}
							Assert.assertEquals(basePage.getText(chaikinPage.alertSymbol(rowCount)), alertsApiResponse.getString(key + "[" + apiIndex + "].Symbol"));
							// Validate 'old' & 'new' values for Estimate Revision
							if (key.equalsIgnoreCase("EarningEstimate")) {
								Assert.assertEquals(basePage.getText(chaikinPage.alertEstimateRevisionSurpriseOld(rowCount)), "$" + alertsApiResponse.getString(key + "[" + apiIndex + "].MeanESTPreviousDay"));
								Assert.assertEquals(basePage.getText(chaikinPage.alertEstimateRevisionSurpriseNew(rowCount)), "$" + alertsApiResponse.getString(key + "[" + apiIndex + "].MeanESTCurrentDay"));
							}
							// Validate 'old' & 'new' values for Earning surprise
							if (key.equalsIgnoreCase("EarningSurprise")) {
								Assert.assertEquals(basePage.getText(chaikinPage.alertEstimateRevisionSurpriseOld(rowCount)), "$" + alertsApiResponse.getString(key + "[" + apiIndex + "].ConsensusEstimate"));
								Assert.assertEquals(basePage.getText(chaikinPage.alertEstimateRevisionSurpriseNew(rowCount)), "$" + alertsApiResponse.getString(key + "[" + apiIndex + "].ActualEPS"));
								Assert.assertEquals(basePage.getText(chaikinPage.alertEarningsSurprisePctChange(rowCount)), alertsApiResponse.getString(key + "[" + apiIndex + "].PercentageSurprise") + "%");
							}
						}
						ExtentLogger.info("List <b> [" + basePage.getText(chaikinPage.btnDropDownList) + "]</b> validated successfully for Alerts");
					}
					basePage.click(chaikinPage.btnBearish, "Bearish");
					alertType="Bearish";
				}
			} else {
				ExtentLogger.info("List <b> [" + basePage.getText(chaikinPage.btnDropDownList) + "]</b> = No alerts found ");
			}
			index++;
		} while (index <= listCount);
	}

	// This will return the API response of the selected list on health check page.
	public JsonPath getListTypeApiResponse(String count, String page, String etfListId) throws Exception {
		String listType = basePage.getText(chaikinPage.btnListType);
		String listCategory = null;
		JsonPath apiResponse = null;

		switch (listType) {
			case "Watchlist" -> apiResponse = viewAllWatchlistsApiCall(count, page);
			case "Chaikin Hotlist" -> {
				listCategory = basePage.getText(chaikinPage.btnListCategory);
				apiResponse = viewAllHotlistApiCall(count, page, listCategory);
			}
			case "Industry" -> apiResponse = viewAllIndustryApiCall(count, page);
			case "Index" -> apiResponse = viewAllIndexApiCall(count, page);
			case "Publication" -> apiResponse = viewAllPublicationApiCall(count, page);
			case "US Equity ETF" -> apiResponse = viewAllEtfApiCall(count, page, etfListId);
		}
		return apiResponse;
	}

	// This is to get the apiIndex of the matched api response.
	public int getApiIndex(int count, JsonPath apiResponse){
		int apiIndex;
		for (apiIndex = 0; apiIndex < count; apiIndex++) {
			if (basePage.getText(chaikinPage.btnDropDownList).equalsIgnoreCase(apiResponse.getString("data.data[" + apiIndex + "].listName")))
				break;
		}
		return apiIndex;
	}

	// This method will select list from drop-down, click on load more button and evaluate page count for API parameter.
	public int selectListFromDropdownAndEvaluatePageCount(int index, int count) throws Exception {
		basePage.click(chaikinPage.btnDropDownList, "Drop-down");
		// Click on drop-down if list not visible.
		if (!basePage.verifyElement(chaikinPage.lstDropDown, "Drop-down List")) {
			basePage.click(chaikinPage.btnDropDownList, "Drop-down button");
			basePage.verifyElement(chaikinPage.lstDropDown, "Drop-down List");
		}

		// Click on load more button.
		if (index > count) {
			basePage.click(chaikinPage.btnLoadMore, chaikinPage.txtLoadMore);
			basePage.WaitUntilElementNotVisible(chaikinPage.spinLoader);
		}

		// Selecting list from the drop-down.
		basePage.click(chaikinPage.ddlMenuOption(index), basePage.getText(chaikinPage.ddlMenuOption(index)));

		// Evaluating Page count based on count.
		int page = (index / count) + 1;
		if (index % count == 0) {
			page = page - 1;
		}
		return page;
	}

	// This method will return the listCount from health-Check page drop-down. If the count passed from constant file > count from API, then value of count API is used.
	public int getListCount(JsonPath apiResponse) throws Exception {
		int listCount = chaikinPage.listCount();
		if (chaikinPage.listCount() > apiResponse.getInt("data.count")) {
			listCount = apiResponse.getInt("data.count");
		}
		return listCount;
	}

	public JsonPath powerFeedMarketViewApiCall() throws Exception {
		return basePage.makeGetApiCall(apiUrl.getPowerFeedMarketViewApiUrl());
	}

	public JsonPath watchlistsApiCall() throws Exception {
		return basePage.makeGetApiCall(apiUrl.getWatchlistApiUrl());
	}

	public JsonPath viewAllWatchlistsApiCall(String count, String page) throws Exception {
		return basePage.makeGetApiCall(apiUrl.getViewAllWatchlistApiUrl(count, page));
	}

	public JsonPath viewAllHotlistApiCall(String count, String page, String category) throws Exception {
		return basePage.makeGetApiCall(apiUrl.getViewAllHotlistApiUrl(count, page, category));
	}

	public JsonPath viewAllIndustryApiCall(String count, String page) throws Exception {
		return basePage.makeGetApiCall(apiUrl.getViewAllIndustryApiUrl(count, page));
	}

	public JsonPath viewAllIndexApiCall(String count, String page) throws Exception {
		return basePage.makeGetApiCall(apiUrl.getViewAllIndexApiUrl(count, page));
	}

	public JsonPath viewAllPublicationApiCall(String count, String page) throws Exception {
		return basePage.makeGetApiCall(apiUrl.getViewAllPublicationApiUrl(count, page));
	}

	public JsonPath viewAllEtfApiCall(String count, String page, String etfListId) throws Exception {
		return basePage.makeGetApiCall(apiUrl.getViewAllEtfApiUrl(count, page, etfListId));
	}

	public JsonPath publicationsApiCall() throws Exception {
		return basePage.makeGetApiCall(apiUrl.getPublicationsApiUrl());
	}

	public JsonPath chaikinHotlistApiCall() throws Exception {
		return basePage.makeGetApiCall(apiUrl.getChaikinHotlistApiUrl());
	}

	public JsonPath bookmarkRecentlySavedApiCall() throws Exception {
		return basePage.makeGetApiCall(apiUrl.getBookmarksRecentlySavedApiUrl());
	}

	public JsonPath postArticleApiCall() throws Exception {
		HashMap<String, String> data = new HashMap<>();
		data.put("bookmarkedOnly", "true");
		return basePage.makePostApiCall(apiUrl.postArticlesApiUrl(), null, data);
	}

	public String[] pickRandomSymbol(Hashtable<String, String> data){
		return basePage.convertStocks_ETFS(data);
	}

	public JsonPath postSuggestionApiCall(List<String> list) throws Exception {
		return basePage.suggestionApiCall(apiUrl.postSuggestionsApiUrl(), list);
	}

	public JsonPath postIndustryExposureApiCall(List<String> list) throws Exception {
		return basePage.industryExposureApiCall(apiUrl.postIndustryExposureApiUrl(), list);
	}

	public JsonPath getAllAlertsApiCall(int listId) throws Exception {
		return basePage.makeGetApiCall(apiUrl.getAllAlertsApiUrl(listId));
	}

}
