package com.omni.helpers;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.testng.Assert;

import com.omni.base.ApiBaseUrl;
import com.omni.base.BasePage;
import com.omni.pages.PgrPage;
import com.omni.reports.ExtentLogger;
import io.restassured.path.json.JsonPath;

public class PgrHelper {
	PgrPage pgrPage = new PgrPage();
	BasePage basePage = new BasePage();
	ApiBaseUrl apiUrl = new ApiBaseUrl();
	String value = null;
	String key = null;

	public void validatingQuickRatingSearchBox(){
		basePage.verifyElement(pgrPage.btnQuickRating, "Quick Rating Button");
	}

	public void validateFeaturedChaikinHotlistData() throws Exception {
		getTextHotlist(basePage.makeGetApiCall(apiUrl.featuredChaikinHotlistApiUrl()));
	}

	public void verifyChaikinHotlistViewAllListPage() throws Exception {
		basePage.verifyElement(pgrPage.btnViewAllList, "View All Lists");
		basePage.click(pgrPage.btnViewAllList, "View All Lists");
		basePage.verifyElement(pgrPage.rdoChaikinHotlist, "Chaikin Hotlist");
	}

	public void validateTextVisibleOnPgrPage() throws Exception {
		basePage.scroll(pgrPage.headingAboutOurRating);

		JsonPath whatIsPowerGaugeApiRes = basePage.makeGetApiCall(apiUrl.whatIsPowergaugeApiUrl());
		Assert.assertEquals("success", whatIsPowerGaugeApiRes.getString("status"));
		Assert.assertEquals("What is the Chaikin Power Gauge?", whatIsPowerGaugeApiRes.getString("data.title"));

	}

	public String[] pickRandomSymbol(Hashtable<String, String> data){
		String[] ticker_convert = basePage.convertStocks_ETFS(data);
		return ticker_convert;
	}

	public void clickOnPGRSearchBoxEnterSymbol(String symbol){
		basePage.verifyElement(pgrPage.btnQuickRating, "Quick Rating Button");
		basePage.type(pgrPage.btnQuickRating, symbol, "Search");
	}

	public void navigateToPgrSecondPage() throws Exception {
		basePage.click(pgrPage.headerTicker, "header_ticker");
		basePage.verifyElement(pgrPage.headerTicker, "header_ticker");
		Thread.sleep(3000);
	}

	public void navigateToPgrFirstPage(){
		basePage.navigateToBackPage();
		basePage.verifyElement(pgrPage.btnQuickRating, "Quick Rating Button");
	}

	public void checkSymbolFoundOrNot(String symbol) throws Exception {

		// matchCount
		HashMap<String, String> data = new HashMap<>();
		data.put("text", symbol);
		JsonPath searchApiRes = basePage.makePostApiCall(apiUrl.postSearchApiUrl(), null, data);
		int matchCount = searchApiRes.getInt("data.suggestions.matchCount");
		String id = searchApiRes.getString("data.suggestions.data[0].id");// return id from data[] index 0.
		// Search Stock/Symbol and click on it if found.
		if (matchCount != 0 && id.equalsIgnoreCase(symbol)) {
			// Stocks & ETFs found
			// captureScreenshot();
			basePage.click(pgrPage.symbol(symbol), "Stock");
			basePage.verifyElement(pgrPage.headingSymbol(symbol), "Symbol");
		} else {
			// No stocks or ETFs found
			// captureScreenshot();
		}
	}

	public void validateSearchedSymbolVisibleInRecentlyViewedSection(String symbol){
		String actualRecentTicketString = basePage.getText(pgrPage.actualRecentTicker);
		Assert.assertEquals(symbol, actualRecentTicketString);
	}

	public void validateStockETFPage(String symbol) throws Exception {
		HashMap<String, String> data = new HashMap<>();
		data.put("text", symbol);
		JsonPath searchApiRes = basePage.makePostApiCall(apiUrl.postSearchApiUrl(), null, data);
		int pgrRating = searchApiRes.getInt("data.suggestions.data[0].pgrRating");
		// Boolean is_etf = searchApiRes.getBoolean("data.suggestions.data[0].isEtf");
		// PGR Api response
		// JsonPath etfData = basePage.getETFSpecificData(symbol);
		if (pgrRating <= 0) {
			ExtentLogger.info("Power Gauge Rating for " + symbol + " = [" + pgrRating + "]");
		}
	}

	public void validatePriceChangePercentage(String symbol) throws Exception {
		JsonPath pgrApiRes = basePage.makeGetApiCall(apiUrl.pgrPageApiUrl(symbol));
		// Validate Price, change, percentage
		String lastPrice = basePage.getText(pgrPage.lastPrice);
		String change = basePage.getText(pgrPage.change);
		String percentage = basePage.getText(pgrPage.percentage);
		Assert.assertEquals(lastPrice, "$" + String.format("%.2f", pgrApiRes.getDouble("metaInfo[0].Last")));
		Assert.assertEquals(change, String.format("%.2f", pgrApiRes.getDouble("metaInfo[0].Change")));
		Assert.assertEquals(percentage.replace("+", ""), String.format("%.2f", pgrApiRes.getDouble("metaInfo[0].\"Percentage \"")) + "%");
	}

	public void validateMainPgrRating(String symbol) throws Exception {
		JsonPath pgrApiRes = basePage.makeGetApiCall(apiUrl.pgrPageApiUrl(symbol));
		String pgrValue = basePage.getText(pgrPage.pgrValueHeading); // This will extract the main rating of stock-ETF
		basePage.getPgrValue(pgrApiRes, pgrValue);
	}

	// For Etfs Chaikin Power Bar & etf groups
	public void validateEtfChaikinPowerBarAndGroup(JsonPath etfData){
		String bearish = basePage.getText(pgrPage.bearish);
		String neutral = basePage.getText(pgrPage.neutral);
		String bullish = basePage.getText(pgrPage.bullish);
		String etfGroup = basePage.getText(pgrPage.etfGroup);

		Assert.assertEquals(bearish, etfData.getString("powerbar_count.bears"));
		Assert.assertEquals(neutral, etfData.getString("powerbar_count.neutral"));
		Assert.assertEquals(bullish, etfData.getString("powerbar_count.bulls"));
		Assert.assertEquals(etfGroup, etfData.getString("etf_group"));
	}

	// For Chart flow
	public void validateChartData(String symbol, String chartTimePeriod) throws Exception {
		if (!chartTimePeriod.equalsIgnoreCase("")) {
			basePage.click(pgrPage.clickChartTimePeriod(chartTimePeriod), chartTimePeriod);
			Boolean result = basePage.validateChartData(symbol);
			Assert.assertTrue(result);
		}
	}

	public void validateChecklistStrengthTimings(String symbol) throws Exception {
		HashMap<String, String> data = new HashMap<>();
		data.put("text", symbol);
		JsonPath searchApiRes = basePage.makePostApiCall(apiUrl.postSearchApiUrl(), null, data);
		Boolean is_etf = searchApiRes.getBoolean("data.suggestions.data[0].isEtf");

		basePage.scroll(pgrPage.checklist);
		JsonPath getChecklistStock = basePage.makeGetApiCall(apiUrl.getChecklistStockApiUrl(symbol));
		// Ui data extraction.
		String pgr = basePage.getText(pgrPage.pgr);
		String relStrength = basePage.getText(pgrPage.relStrength);
		String overbrought = basePage.getText(pgrPage.overbrought);
		String ltStrength = basePage.getText(pgrPage.ltStrength);
		String moneyFlow = basePage.getText(pgrPage.moneyFlow);
		String strengthCount = basePage.getText(pgrPage.strengthCount);
		String timingCount = basePage.getText(pgrPage.timingCount);

		Assert.assertEquals(pgr, getChecklistStock.getString("pgr"));
		Assert.assertEquals(relStrength, getChecklistStock.getString("relativeStrength"));
		Assert.assertEquals(overbrought, getChecklistStock.getString("overboughtOversold"));
		Assert.assertEquals(ltStrength, getChecklistStock.getString("ltTrend"));
		Assert.assertEquals(moneyFlow, getChecklistStock.getString("moneyFlow"));
		Assert.assertEquals(String.valueOf(strengthCount.charAt(0)), String.valueOf(getChecklistStock.getInt("strengthCount")));
		Assert.assertEquals(String.valueOf(timingCount.charAt(0)), String.valueOf(getChecklistStock.getInt("timingCount")));

		if (is_etf.equals(true)) {
			String groupStrength = basePage.getText(pgrPage.groupStrength);
			Assert.assertEquals(groupStrength, getChecklistStock.getString("industry"));
		} else {// for stock - Industry Strength
			String industryStrength = basePage.getText(pgrPage.industryStrength);
			Assert.assertEquals(industryStrength, getChecklistStock.getString("industry"));
		}
		// captureScreenshot();
	}

	public void validateEtfHoldings(String symbol) throws Exception {
		HashMap<String, String> data = new HashMap<>();
		data.put("text", symbol);
		JsonPath searchApiRes = basePage.makePostApiCall(apiUrl.postSearchApiUrl(), null, data);
		int pgrRating = searchApiRes.getInt("data.suggestions.data[0].pgrRating");
		Boolean is_etf = searchApiRes.getBoolean("data.suggestions.data[0].isEtf");

		// validating ETFs holding
		JsonPath etfTopHoldingListResponse = null;
		JsonPath stockTopHoldingListResponse = null;
		basePage.scroll(pgrPage.etfWebsite);
		if (pgrRating > 0 && is_etf.equals(true)) {
			etfTopHoldingListResponse = basePage.makeGetApiCall(apiUrl.getETFConstituentSpecificDataApiUrl(symbol));
			basePage.click(pgrPage.seeAll, "See All");
			// captureScreenshot(); // Check symbol in modal-popup as well.
			basePage.click(pgrPage.btnClose, "Close button");
		} else { // for stock - ETFs holding
			stockTopHoldingListResponse = basePage.makeGetApiCall(apiUrl.getExpandedAssociatedListDataApiUrl(symbol));
		}
		basePage.click(pgrPage.symbolColumn, "Symbol Column"); // For sorting according to alphabetical order.
		for (int i = 1; i <= 10; i++) {
			String rating = basePage.getAttribute(pgrPage.rating, "alt");
			String symbol_name = basePage.getText(pgrPage.symbol_name);
			String price = basePage.getText(pgrPage.price);
			String allocation = basePage.getText(pgrPage.allocation);
			String company = basePage.getText(pgrPage.company);

			rating = basePage.convertRating(rating);

			if (pgrRating > 0 && is_etf.equals(true)) {
				for (int j = 0; j <= 100; j++) {
					if (etfTopHoldingListResponse.getString("[" + (j) + "].ticker").equals(symbol_name)) {
						Assert.assertEquals(symbol_name, etfTopHoldingListResponse.getString("[" + j + "].ticker"));
						Assert.assertEquals(price, "$" + String.format("%.2f", etfTopHoldingListResponse.getDouble("[" + j + "].last")));
						Assert.assertEquals(allocation.replace("+", ""), String.format("%.2f", etfTopHoldingListResponse.getDouble("[" + j + "].percentChange")) + "%");
						Assert.assertEquals(company, etfTopHoldingListResponse.getString("[" + j + "].company"));
						if (etfTopHoldingListResponse.getInt("[" + (j) + "].corrected_pgr") == 3) {
							if (etfTopHoldingListResponse.getInt("[" + (j) + "].raw_pgr") == 4 | etfTopHoldingListResponse.getInt("[" + (j) + "].raw_pgr") == 5)
								Assert.assertEquals(rating, "Neutral +");
							else if (etfTopHoldingListResponse.getInt("[" + (j) + "].raw_pgr") == 1 | etfTopHoldingListResponse.getInt("[" + (j) + "].raw_pgr") == 2)
								Assert.assertEquals(rating, "Neutral -");
							else
								Assert.assertEquals(rating, "3");
						} else
							Assert.assertEquals(rating, String.valueOf(etfTopHoldingListResponse.getInt("[" + (j) + "].corrected_pgr")));
						break;
					}
				}
			} else { // for stock - ETFs holding
				for (int j = 0; j <= 100; j++) {
					if (stockTopHoldingListResponse.getString("data[" + (j) + "].ticker").equals(symbol_name)) {
						Assert.assertEquals(symbol_name, stockTopHoldingListResponse.getString("data[" + (j) + "].ticker"));
						Assert.assertEquals(price, "$" + String.format("%.2f", stockTopHoldingListResponse.getDouble("data[" + (j) + "].last")));
						Assert.assertEquals(allocation.replace("+", ""), String.format("%.2f", Double.parseDouble(stockTopHoldingListResponse.getString("data[" + (j) + "].weight"))) + "%");
						Assert.assertEquals(company, stockTopHoldingListResponse.getString("data[" + (j) + "].name"));
						if (stockTopHoldingListResponse.getInt("data[" + (j) + "].rating") == 3) {
							if (stockTopHoldingListResponse.getInt("data[" + (j) + "].rawPgrRating") == 4 | stockTopHoldingListResponse.getInt("data[" + (j) + "].rawPgrRating") == 5)
								Assert.assertEquals(rating, "Neutral +");
							else if (stockTopHoldingListResponse.getInt("data[" + (j) + "].rawPgrRating") == 1 | stockTopHoldingListResponse.getInt("data[" + (j) + "].rawPgrRating") == 2)
								Assert.assertEquals(rating, "Neutral -");
							else
								Assert.assertEquals(rating, "3");
						} else
							Assert.assertEquals(rating, String.valueOf(stockTopHoldingListResponse.getInt("data[" + (j) + "].rating")));
						break;
					}
				}
			}
		}
	}

	public void validateArticlesOnPgrPage(String symbol) throws Exception {
		// For Articles flow
		basePage.scroll(pgrPage.headingViewFullRatingReport);
		basePage.click(pgrPage.seeMore, "See More Articles/Reports");
		basePage.click(pgrPage.btnClose, "Close button");
		// captureScreenshot();
		int articleCount = basePage.getSize(pgrPage.countArticles);
		if (articleCount == 0)
			ExtentLogger.info("No current articles");
		else {
			for (int i = 1; i <= articleCount; i++) {
				String articleTitle = basePage.getText(pgrPage.articleTitle(i));
				String author = basePage.getText(pgrPage.author(i));
				String product = basePage.getText(pgrPage.product(i));

				// Validate UI articles & source from API response
				Map<String, String[]> data = new HashMap<>();
				data.put("symbols", new String[] { symbol });
				// JsonPath articlesApiCall = basePage.makePostApiCall(apiUrl.postArticlesApiUrl(), null, data);
				JsonPath articlesApiCall = basePage.articlesApiCall(symbol);
				Assert.assertEquals(articleTitle, articlesApiCall.getString("data.data[" + (i - 1) + "].title"));
				Assert.assertEquals(author, articlesApiCall.getString("data.data[" + (i - 1) + "].analysts[0].name"));
				Assert.assertEquals(product, articlesApiCall.getString("data.data[" + (i - 1) + "].products[0].name"));
			}
		}
	}

	public void validateQuickStatsSection(String symbol) throws Exception {
		JsonPath pgrApiRes = basePage.makeGetApiCall(apiUrl.pgrPageApiUrl(symbol));
		JsonPath etfData = basePage.makeGetApiCall(apiUrl.getETFSpecificDataApiUrl(symbol));
		HashMap<String, String> data = new HashMap<>();
		data.put("text", symbol);
		JsonPath searchApiRes = basePage.makePostApiCall(apiUrl.postSearchApiUrl(), null, data);
		Boolean is_etf = searchApiRes.getBoolean("data.suggestions.data[0].isEtf");
		basePage.scroll(pgrPage.headingQuickStats);
		if (is_etf.equals(false))
			validateStockQuickStats(symbol, pgrApiRes);
		if (is_etf.equals(true))
			validateEtfQuickStats(etfData);
	}

	public void validateStockQuickStats(String symbol, JsonPath pgrApiRes) throws Exception {
		// For Quick Stats flow - Fundamentals
		Map<String, String> uiQuickStats = getTextQuickStats("Fundamentals");
		Map<String, String> pgrApiMap = pgrApiRes.getJsonObject("fundamentalData");

		Assert.assertEquals(uiQuickStats.get("Market Cap"), basePage.millionBillionConversion(pgrApiMap.get("Mkt Capitalization")));
		// Assert.assertEquals(uiQuickStats.get("Revenue"), pgrApiRes.getString("fundamentalData.Revenue").equals("NA") ? pgrApiRes.getString("fundamentalData.Revenue") : basePage.millionBillionConversion(pgrApiRes.getString("fundamentalData.Revenue")));
		Assert.assertEquals(uiQuickStats.get("Proj 1 year P/E"), pgrApiMap.get("P/E").equals("N/A") ? pgrApiMap.get("P/E") : String.format("%.2f", Double.parseDouble(pgrApiMap.get("P/E"))));
		Assert.assertEquals(uiQuickStats.get("Yield"), pgrApiRes.getString("fundamentalData.Yield") + "%");

		// For Quick Stats flow - Technicals
		uiQuickStats = getTextQuickStats("Technicals");
		Assert.assertEquals(uiQuickStats.get("21-Day Exp Moving Avg*"), String.format("%.2f", pgrApiRes.getDouble("TechnicalData.ema21")));
		Assert.assertEquals(uiQuickStats.get("Long Term Trend Line*"), String.format("%.2f", pgrApiRes.getDouble("TechnicalData.ltTrend")));
		// Assert.assertEquals(uiQuickStats.get("Chaikin Money Flow*"), String.format("%.2f", pgrApiRes.getDouble("TechnicalData.chaikinMoneyFLow")));
		// Assert.assertEquals(uiQuickStats.get("Technical Trend"), pgrApiRes.getString("TechnicalData.Yield"));

		// For Quick Stats flow - Performance
		uiQuickStats = getTextQuickStats("Performance");
		Assert.assertEquals(uiQuickStats.get("52 Week High/Low"), String.format("%.2f", Double.parseDouble(pgrApiMap.get("52 Wk Hi"))) + "/" + String.format("%.2f", Double.parseDouble(pgrApiMap.get("52 Wk Lo"))));
		Assert.assertEquals(uiQuickStats.get("52 Week Performance").replace("+", ""), String.format("%.2f", Double.parseDouble(pgrApiMap.get("52 Wk Performance (%)"))) + "%");
		Assert.assertEquals(uiQuickStats.get("Rel Strength to SPY").replace("+", ""), String.format("%.2f", Double.parseDouble(pgrApiMap.get("Relative to S&P (%)"))) + "%");
		Assert.assertEquals(uiQuickStats.get("Beta"), pgrApiRes.getString("fundamentalData.beta").equals("NA") ? pgrApiRes.getString("fundamentalData.beta") : String.format("%.2f", Double.parseDouble(pgrApiRes.getString("fundamentalData.beta"))));

		// For Quick Stats flow - Earnings
		uiQuickStats = getTextQuickStats("Earnings");
		Assert.assertEquals(uiQuickStats.get("EPS 2022"), pgrApiMap.get("EPS_Previous (prev FY)").equals("N/A") ? pgrApiMap.get("EPS_Previous (prev FY)") : "$" + String.format("%.2f", Double.parseDouble(pgrApiMap.get("EPS_Previous (prev FY)"))));
		Assert.assertEquals(uiQuickStats.get("EPS 2023"), "$" + String.format("%.2f", Double.parseDouble(pgrApiMap.get("EPS_current (curr FY)"))));
		Assert.assertEquals(uiQuickStats.get("Annual 5Y EPS Growth").replace("+", ""), String.format("%.2f", Double.parseDouble(pgrApiMap.get("5 Yr EPS Growth (%)"))) + "%");
		Assert.assertEquals(uiQuickStats.get("Analyst Revisions"), String.format("%.2f", Double.parseDouble(pgrApiMap.get("Analyst Revisions (%)"))) + "%");

		// For Quick Stats flow - Ratios
		uiQuickStats = getTextQuickStats("Ratios");
		Assert.assertEquals(uiQuickStats.get("LT Debt-Equity"), pgrApiMap.get("Debt/Equity").equals("NA") ? pgrApiMap.get("Debt/Equity") : String.format("%.2f", Double.parseDouble(pgrApiMap.get("Debt/Equity"))));
		Assert.assertEquals(uiQuickStats.get("Price-Book"), pgrApiMap.get("Price/Book").equals("NA") ? pgrApiMap.get("Price/Book") : String.format("%.2f", Double.parseDouble(pgrApiMap.get("Price/Book"))));
		Assert.assertEquals(uiQuickStats.get("1Y ROE").replace("+", ""), pgrApiRes.getString("fundamentalData.ROE").equals("NA") ? pgrApiRes.getString("fundamentalData.ROE") : String.format("%.2f", Double.parseDouble(pgrApiRes.getString("fundamentalData.ROE"))) + "%");
		Assert.assertEquals(uiQuickStats.get("Price-Sales"), pgrApiMap.get("Price/Sales").equals("NA") ? pgrApiMap.get("Price/Sales") : String.format("%.2f", Double.parseDouble(pgrApiMap.get("Price/Sales"))));

		// For Quick Stats flow - Dividends
		uiQuickStats = getTextQuickStats("Dividends");
		Assert.assertEquals(uiQuickStats.get("Annual Dividend per Share"), pgrApiRes.getString("fundamentalData.dividend_per_share").equals("NA") ? pgrApiRes.getString("fundamentalData.dividend_per_share")
				: "$" + String.format("%.2f", Double.parseDouble(pgrApiRes.getString("fundamentalData.dividend_per_share"))));
		Assert.assertEquals(uiQuickStats.get("Payout"), pgrApiRes.getString("fundamentalData.payout").equals("NA") ? pgrApiRes.getString("fundamentalData.payout") : String.format("%.2f", Double.parseDouble(pgrApiRes.getString("fundamentalData.payout"))) + "%");
		Assert.assertEquals(uiQuickStats.get("Ex Dividend Date"), pgrApiRes.getString("fundamentalData.next_div_date").equals("N/A") ? pgrApiRes.getString("fundamentalData.next_div_date") : basePage.dateFormat(pgrApiRes.getString("fundamentalData.next_div_date")));
		Assert.assertEquals(uiQuickStats.get("Annual 5Y Growth Rate").replace("+", ""), pgrApiRes.getString("fundamentalData.growth_rate").equals("N/A") ? pgrApiRes.getString("fundamentalData.growth_rate")
				: String.format("%.2f", Double.parseDouble(pgrApiRes.getString("fundamentalData.growth_rate"))) + "%");
	}

	public void validateEtfQuickStats(JsonPath etfData) throws Exception {
		// For Quick Stats flow - Profile
		Map<String, String> uiQuickStats = getTextQuickStats("Profile");
		Assert.assertEquals(uiQuickStats.get("ETF Group"), etfData.getString("etf_group"));
		Assert.assertEquals(uiQuickStats.get("Issuer"), etfData.getString("issuer"));
		Assert.assertEquals(uiQuickStats.get("Holds (Asset Class)"), etfData.getString("asset_class"));
		Assert.assertEquals(uiQuickStats.get("Management"), etfData.getString("management"));
		Assert.assertEquals(uiQuickStats.get("Long/Short"), etfData.getString("long_short"));
		Assert.assertEquals(uiQuickStats.get("Region"), etfData.getString("region"));
		Assert.assertEquals(uiQuickStats.get("Special Focus"), etfData.getString("special_focus").replace("NULL", "-"));

		// For Quick Stats flow - Summary
		uiQuickStats = getTextQuickStats("Summary");
		Assert.assertEquals(uiQuickStats.get("Inception Date"), basePage.dateFormat(etfData.getString("inception_date")));
		Assert.assertEquals(uiQuickStats.get("Assets (AUM)"), etfData.getString("aum").equals("NULL") ? etfData.getString("aum") : basePage.millionBillionConversion(etfData.getString("aum")));
		Assert.assertEquals(uiQuickStats.get("# Underlying US Equities"), String.format(etfData.getString("stock_count")));
		Assert.assertEquals(uiQuickStats.get("Avg Daily Volume").replace(",", ""), basePage.truncateNumber(etfData.getString("avg_daily_vol")));
		Assert.assertEquals(uiQuickStats.get("Expense Ratio"), String.format("%.2f", Double.parseDouble(etfData.getString("expense_ratio"))));
		Assert.assertEquals(uiQuickStats.get("Dividend Yield"), etfData.getString("divident_yield").equals("NULL") ? etfData.getString("divident_yield") : String.format("%.2f", Double.parseDouble(etfData.getString("divident_yield"))));
		Assert.assertEquals(uiQuickStats.get("Beta"), String.format("%.2f", Double.parseDouble(etfData.getString("beta"))));
	}

	public void validateStockEtfSimilarList(String symbol) throws Exception {
		// For Stock-ETF similar lists
		basePage.scroll(pgrPage.stockSimilarListTitle);
		JsonPath resultListResponse = basePage.makeGetApiCall(apiUrl.getResultListsApiUrl(symbol));
		String list_title = basePage.getText(pgrPage.stockSimilarListTitle);
		Assert.assertEquals(list_title, resultListResponse.getString("results[0].list_name"));
		int list_count = basePage.getSize(pgrPage.stockSimilarListCount);
		validateStockLikeSection(list_count, resultListResponse);

		// For hotlist Containing Section
		JsonPath containingHotlistResponse = basePage.makeGetApiCall(apiUrl.hotlistContainingApiUrl(symbol));
		list_count = basePage.getSize(pgrPage.containingHotListCount);
		if (list_count == 0)
			ExtentLogger.info(basePage.getText(pgrPage.noResult));
		else {
			for (int i = 1; i <= list_count; i++) {
				String listType = basePage.getText(pgrPage.listType(i));
				String listName = basePage.getText(pgrPage.listName(i));
				String greenCount = basePage.getText(pgrPage.greenCount(i));
				String yellowCount = basePage.getText(pgrPage.yellowCount(i));
				String redCount = basePage.getText(pgrPage.redCount(i));

				for (int k = 0; k <= 9; k++) {
					if (containingHotlistResponse.getString("data.data[" + k + "].listName").equals(listName)) {
						Assert.assertEquals(listType, containingHotlistResponse.getString("data.data[" + k + "].listType"));
						Assert.assertEquals(listName, containingHotlistResponse.getString("data.data[" + k + "].listName"));
						Assert.assertEquals(greenCount, String.valueOf(containingHotlistResponse.getInt("data.data[" + k + "].greenCount")));
						Assert.assertEquals(yellowCount, String.valueOf(containingHotlistResponse.getInt("data.data[" + k + "].yellowCount")));
						Assert.assertEquals(redCount, String.valueOf(containingHotlistResponse.getInt("data.data[" + k + "].redCount")));
						break;
					}
				}
			}
		}
	}

	public Map<String, String> getTextQuickStats(String categoryName) {
		StringBuilder info = new StringBuilder();
		Map<String, String> map = new HashMap<>();
		int count = basePage.getSize(pgrPage.getTextQuickStatsCount(categoryName));

		for (int i = 1; i <= count; i++) {
			key = basePage.getText(pgrPage.getTextQuickStatsKey(categoryName, i));
			value = basePage.getText(pgrPage.getTextQuickStatsValue(categoryName, i));
			map.put(key, value);
			info.append(key).append(" = ").append(value).append("<br>");
		}
		ExtentLogger.info("UI values of section [" + categoryName + "]<br>" + info);
		return map;
	}

	public void getTextHotlist(JsonPath hotlistRes){
		int hotList_count = basePage.getSize(pgrPage.hotList_count);
		for (int i = 1; i <= hotList_count; i++) {
			String info = "";
			String categoryName = basePage.getText(pgrPage.getTextHotlistCategoryName(i));

			String list_name = basePage.getText(pgrPage.getTextHotlistListName(categoryName));
			info = info + "list_name" + " = " + list_name + "<br>";

			String holdings = basePage.getText(pgrPage.getTextHotlistHoldings(categoryName));
			info = info + "Holdings" + " = " + holdings + "<br>";

			String bullishPercentage = basePage.getText(pgrPage.getTextHotlistBullishPercentage(categoryName));
			info = info + "bullishPercentage" + " = " + bullishPercentage + "<br>";

			String bullish_count = basePage.getText(pgrPage.getTextHotlistBullishCount(categoryName));
			info = info + "bullish_count" + " = " + bullish_count + "<br>";

			String neutral_count = basePage.getText(pgrPage.getTextHotlistNeutralCount(categoryName));
			info = info + "neutral_count" + " = " + neutral_count + "<br>";

			String bearish_count = basePage.getText(pgrPage.getTextHotlistBearishCount(categoryName));
			info = info + "bearish_count" + " = " + bearish_count + "<br>";

			ExtentLogger.info("UI values of section [" + categoryName + "]<br>" + info);

			for (int apiIndex = 0; apiIndex <= hotList_count; apiIndex++) {
				if (hotlistRes.getString("data.data[" + apiIndex + "].listName").equals(list_name)) {
					Assert.assertEquals(list_name, hotlistRes.getString("data.data[" + apiIndex + "].listName"));
					Assert.assertEquals(holdings, hotlistRes.getString("data.data[" + apiIndex + "].stockCount"));
					Assert.assertEquals(bullishPercentage, String.format("%.2f", ((double) hotlistRes.getInt("data.data[" + apiIndex + "].bullPercentage"))) + "%");
					Assert.assertEquals(bullish_count, Integer.toString(hotlistRes.getInt("data.data[" + apiIndex + "].greenCount")));
					Assert.assertEquals(neutral_count, Integer.toString(hotlistRes.getInt("data.data[" + apiIndex + "].yellowCount")));
					Assert.assertEquals(bearish_count, Integer.toString(hotlistRes.getInt("data.data[" + apiIndex + "].redCount")));
					break;
				}
			}
		}
	}

	public void validateLeftSideStockEtfSectionData(String symbol) throws Exception {
		basePage.validateLeftSideStockEtfSectionData(symbol);
	}

	public void validateStockLikeSection(int list_count, JsonPath resultListResponse) {
		for (int i = 1; i <= list_count; i++) {
			String rating = basePage.getAttribute(pgrPage.rating(i), "alt");
			String symbol = basePage.getText(pgrPage.symbol(i));
			String company = basePage.getText(pgrPage.company(i));
			String price = basePage.getText(pgrPage.price(i));
			String percentageChange = basePage.getText(pgrPage.percentageChange(i));
			String industry = basePage.getText(pgrPage.industry(i));
			switch (rating) {
				case "Neutral +", "Neutral -" -> {
					if (resultListResponse.getInt("results[0].stocks[" + (i - 1) + "].PGR") == 3)
						if (resultListResponse.getInt("results[0].stocks[" + (i - 1) + "].raw_PGR") == 4 || resultListResponse.getInt("results[0].stocks[" + (i - 1) + "].raw_PGR") == 5)
							Assert.assertEquals(rating, "Neutral +");
						else if (resultListResponse.getInt("results[0].stocks[" + (i - 1) + "].raw_PGR") == 1 || resultListResponse.getInt("results[0].stocks[" + (i - 1) + "].raw_PGR") == 2)
							Assert.assertEquals(rating, "Neutral -");
					rating = String.valueOf(resultListResponse.getInt("results[0].stocks[" + (i - 1) + "].raw_PGR"));
				}
				default -> rating = basePage.convertRating(rating);
			}
			Assert.assertEquals(rating, String.valueOf(resultListResponse.getInt("results[0].stocks[" + (i - 1) + "].raw_PGR")));
			Assert.assertEquals(symbol, resultListResponse.getString("results[0].stocks[" + (i - 1) + "].symbol"));
			Assert.assertEquals(company, resultListResponse.getString("results[0].stocks[" + (i - 1) + "].name"));
			Assert.assertEquals(price, "$" + String.format("%.2f", resultListResponse.getDouble("results[0].stocks[" + (i - 1) + "].Last")));
			Assert.assertEquals(percentageChange.replace("+", ""), String.format("%.2f", resultListResponse.getDouble("results[0].stocks[" + (i - 1) + "].Percentage")) + "%");
			Assert.assertEquals(industry, resultListResponse.getString("results[0].stocks[" + (i - 1) + "].most_significant_shared_factor"));
		}
	}

}