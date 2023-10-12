package com.omni.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.Assert;

import com.omni.base.ApiBaseUrl;
import com.omni.base.BasePage;
import com.omni.pages.Factor20Page;
import com.omni.reports.ExtentLogger;
import io.restassured.path.json.JsonPath;

public class Factor20Helper {
	ApiBaseUrl apiUrl = new ApiBaseUrl();
	BasePage basePage = new BasePage();
	Factor20Page factor20Page = new Factor20Page();
	HashMap<String, String> map = new HashMap<>();
	String value = null;
	String key = null;
	JsonPath factor20ApiRes = null;
	Map<String, String> apiValue;
	Map<String, String> uiValue;

	public void navigateBackOn20factorPage(){
		basePage.navigateToBackPage();
		basePage.verifyElement(factor20Page.Global_search, "Global Search");
	}

	public String[] pickRandomSymbol(Hashtable<String, String> data) {
		String[] ticker_convert = basePage.convertStocks_ETFS(data);
		return ticker_convert;
	}

	public void clickOnGlobalSearch() throws Exception {
		basePage.verifyElement(factor20Page.Global_search, "Global Search");
		basePage.click(factor20Page.Global_search, "Global Search");
	}

	public void validateGlobalSearchTextFieldAndTypeSymbol(String symbol){
		basePage.verifyElement(factor20Page.Global_search_input, "Search");
		basePage.type(factor20Page.Global_search_input, symbol, "Search");
	}

	public void validateSearcedSymbolVisibleOrNot(String symbol) throws Exception {
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
			basePage.verifyElement(factor20Page.ticker(symbol), "Stock");
		} else {
			// No stocks or ETFs found
			// captureScreenshot();
		}
	}

	// This will click on the search symbol visible in the drop-down
	public void clickOnSearchedSymbol(String symbol) throws Exception {
		basePage.click(factor20Page.Global_search, "Global Search");
		validateGlobalSearchTextFieldAndTypeSymbol(symbol);
		validateSearcedSymbolVisibleOrNot(symbol);
		basePage.click(factor20Page.ticker(symbol), "Stock");
	}

	// Validate the stock/ETF page, by validating [View_Full_Rating] button.
	public void validateStockEtfPage(String symbol) throws Exception {
		HashMap<String, String> data = new HashMap<>();
		data.put("text", symbol);
		JsonPath searchApiRes = basePage.makePostApiCall(apiUrl.postSearchApiUrl(), null, data);
		int pgrRating = searchApiRes.getInt("data.suggestions.data[0].pgrRating");
		Boolean is_etf = searchApiRes.getBoolean("data.suggestions.data[0].isEtf");

		if (pgrRating <= 0) {
			ExtentLogger.info("Power Gauge Rating for " + symbol + " = [" + pgrRating + "]");
		}
		if (pgrRating > 0 && is_etf.equals(true)) {
			ExtentLogger.info("20 Factor Page not available for ETFs");
		}
		if (pgrRating > 0 && is_etf.equals(false)) {
			ExtentLogger.info("20 Factor Page available for [" + symbol + "]");
			basePage.verifyElement(factor20Page.btnViewFullRating, "View Full Rating Report button");
		}
	}

	// This will click on the [View Full Rating Report] button on company page & Verify 20-factor Page.
	public void clickFullRatingBtnAndVerify20FactorPage() throws Exception {
		basePage.click(factor20Page.btnViewFullRating, "View Full Rating Report button");
		Boolean stockRatingPage = basePage.verifyElement(factor20Page.stockRatingReportPage, "Stock Rating Report Page");
		if (stockRatingPage.equals(false)) {
			basePage.verifyElement(factor20Page.errorPage, "error-page");
			ExtentLogger.info("Error Page Visible. [Oops! Something went wrong. Please try again later.]");
			// captureScreenshot();
			basePage.click(factor20Page.btnGoToHome, "Go to home button");
			basePage.verifyElement(factor20Page.Global_search, "Global Search");
		}
	}

	public JsonPath get20FactorApiRes(String symbol) throws Exception {
		factor20ApiRes = basePage.makeGetApiCall(apiUrl.factor20ApiUrl(symbol));
		return factor20ApiRes;
	}

	// This function will return the Ui values of the category main rating visible on 20factor page.
	public String getUiRating(String categoryName){
		basePage.scroll(factor20Page.txtCategoryRating(categoryName));
		String uiRating = basePage.getText(factor20Page.txtCategoryRating(categoryName));
		return uiRating;
	}

	// This function will return the Ui values of the category main rating visible on 20factor page.
	public void validateCategoryPgrRating(String symbol) throws Exception {
		factor20ApiRes = get20FactorApiRes(symbol);
		Assert.assertEquals(getUiRating("Financials:"), factor20ApiRes.getString("data.financialsData.financialContextSummary.status"));
		Assert.assertEquals(getUiRating("Earnings:"), factor20ApiRes.getString("data.earningsData.earningsContextSummary.status"));
		Assert.assertEquals(getUiRating("Technicals:"), factor20ApiRes.getString("data.technicalsData.technicalContextSummary.status"));
		Assert.assertEquals(getUiRating("Experts:"), factor20ApiRes.getString("data.expertData.expertOpnionsContextSummary.status"));
	}

	public void validateSlidebarFinancials(String symbol) throws Exception {
		factor20ApiRes = get20FactorApiRes(symbol);
		Map<String, Integer> apiValue = factor20ApiRes.getMap("data.financialsData.financialsRating");
		uiValue = getTextSlidebar("Financials:");
		Assert.assertEquals(uiValue.get("Free Cash Flow"), Integer.toString(apiValue.get("freeCashFlow")));
		Assert.assertEquals(uiValue.get("LT Debt to Equity"), Integer.toString(apiValue.get("ltDebtToEquity")));
		Assert.assertEquals(uiValue.get("Return on Equity"), Integer.toString(apiValue.get("returnOnEquity")));
		Assert.assertEquals(uiValue.get("Price to Sales"), Integer.toString(apiValue.get("priceToSales")));
		Assert.assertEquals(uiValue.get("Price to Book"), Integer.toString(apiValue.get("priceToBook")));
	}

	public void validateSlidebarEarnings(String symbol) throws Exception {
		factor20ApiRes = get20FactorApiRes(symbol);
		Map<String, Integer> apiValue = factor20ApiRes.getMap("data.earningsData.earningsRating");
		Map<String, String> uiValue = getTextSlidebar("Earnings:");
		Assert.assertEquals(uiValue.get("Earnings Growth"), Integer.toString(apiValue.get("earningsGrowth")));
		Assert.assertEquals(uiValue.get("Earnings Trend"), Integer.toString(apiValue.get("earningsTrend")));
		Assert.assertEquals(uiValue.get("Earnings Surprises"), Integer.toString(apiValue.get("earningsSurprise")));
		Assert.assertEquals(uiValue.get("Projected P/E"), Integer.toString(apiValue.get("projectedRatio")));
		Assert.assertEquals(uiValue.get("Earnings Consistency"), Integer.toString(apiValue.get("earningsConsistency")));
	}

	public void validateSlidebarTechnicals(String symbol) throws Exception {
		factor20ApiRes = get20FactorApiRes(symbol);
		Map<String, Integer> apiValue = factor20ApiRes.getMap("data.technicalsData.technicalsRating");
		uiValue = getTextSlidebar("Technicals:");
		Assert.assertEquals(uiValue.get("Chaikin Money Flow"), Integer.toString(apiValue.get("chaikinMoneyFlow")));
		Assert.assertEquals(uiValue.get("Price Trend ROC"), Integer.toString(apiValue.get("priceTrendROC")));
		Assert.assertEquals(uiValue.get("Volume Trend"), Integer.toString(apiValue.get("volumeTrend")));
		Assert.assertEquals(uiValue.get("Price Strength"), Integer.toString(apiValue.get("priceStrength")));
		Assert.assertEquals(uiValue.get("Relative Strength vs Market"), Integer.toString(apiValue.get("relStrengthVSMarket")));
	}

	public void validateSlidebarExperts(String symbol) throws Exception {
		factor20ApiRes = get20FactorApiRes(symbol);
		Map<String, Integer> apiValue = factor20ApiRes.getMap("data.expertData.expertsRating");
		uiValue = getTextSlidebar("Experts:");
		Assert.assertEquals(uiValue.get("Industry Relative Strength"), Integer.toString(apiValue.get("industryRelStrength")));
		Assert.assertEquals(uiValue.get("Insider Activity"), Integer.toString(apiValue.get("insiderActivity")));
		Assert.assertEquals(uiValue.get("Short Interest"), Integer.toString(apiValue.get("shortInterest")));
		Assert.assertEquals(uiValue.get("Estimate Trend"), Integer.toString(apiValue.get("estTrend")));
		Assert.assertEquals(uiValue.get("Analyst Rating Trend"), Integer.toString(apiValue.get("analystRatingTrend")));
	}

	// Verify Asset&Liability data
	public void validateFinancials_AssetAndLiability(String symbol) throws Exception {
		basePage.scroll(factor20Page.headingFinancials);
		apiValue = formatApiResponse("data.financialsData.assetAndLiability", symbol);
		uiValue = getTextUiFinancialsTechnicalExpertsData("Assets & Liabilities");
		Assert.assertEquals(uiValue.get("Current Ratio"), apiValue.get("current_ratio"));
		Assert.assertEquals(uiValue.get("LT Debt/Equity"), apiValue.get("debtEquity"));
		Assert.assertEquals(uiValue.get("Market Cap"), basePage.millionBillionConversion(apiValue.get("marketCap")));
		Assert.assertEquals(uiValue.get("Revenue"), basePage.millionBillionConversion(apiValue.get("revenue")));
	}

	// Verify Valuation data
	public void validateFinancials_Valuation(String symbol) throws Exception {
		apiValue = formatApiResponse("data.financialsData.valuation", symbol);
		uiValue = getTextUiFinancialsTechnicalExpertsData("Valuation");
		Assert.assertEquals(uiValue.get("PEG"), apiValue.get("peg"));
		Assert.assertEquals(uiValue.get("Price/Earnings"), apiValue.get("priceEarning"));
		Assert.assertEquals(uiValue.get("Price to Book"), apiValue.get("priceToBook"));
		Assert.assertEquals(uiValue.get("Price to Sales (TTM)"), apiValue.get("priceToSale"));
	}

	// Verify Dividends data
	public void validateFinancials_Dividends(String symbol) throws Exception {
		apiValue = formatApiResponse("data.financialsData.dividends", symbol);
		uiValue = getTextUiFinancialsTechnicalExpertsData("Dividends");
		Assert.assertEquals(uiValue.get("Annual Dividend per Share"), "$" + apiValue.get("dividendPerShare"));
		Assert.assertEquals(uiValue.get("Payout"), apiValue.get("payoutRatio").equals("NA") ? apiValue.get("payoutRatio") : String.format("%.2f", Double.parseDouble(apiValue.get("payoutRatio"))) + "%");
		Assert.assertEquals(uiValue.get("Yield"), apiValue.get("yield").equals("NA") ? apiValue.get("yield") : String.format("%.2f", Double.parseDouble(apiValue.get("yield"))) + "%");
		Assert.assertEquals(uiValue.get("5Y Growth Rate"), apiValue.get("growthRate5Year").equals("NA") ? apiValue.get("growthRate5Year") : String.format("%.2f", Double.parseDouble(apiValue.get("growthRate5Year"))) + "%");
	}

	// Verify Return data
	public void validateFinancials_Return(String symbol) throws Exception {
		apiValue = formatApiResponse("data.financialsData.returns", symbol);
		uiValue = getTextUiFinancialsTechnicalExpertsData("Return");
		Assert.assertEquals(uiValue.get("1Y Return on Investment"), apiValue.get("returnOnInvestment").equals("NA") ? apiValue.get("returnOnInvestment") : String.format("%.2f", Double.parseDouble(apiValue.get("returnOnInvestment"))) + "%");
		Assert.assertEquals(uiValue.get("1Y Return on Equity"), apiValue.get("returnOnEquity").equals("NA") ? apiValue.get("returnOnEquity") : String.format("%.2f", Double.parseDouble(apiValue.get("returnOnEquity"))) + "%");
		Assert.assertEquals(uiValue.get("1 Month Return"), apiValue.get("returnOneMonth").equals("NA") ? apiValue.get("returnOneMonth") : String.format("%.2f", Double.parseDouble(apiValue.get("returnOneMonth"))) + "%");
		Assert.assertEquals(uiValue.get("3 Month Return"), apiValue.get("returnThreeMonth").equals("NA") ? apiValue.get("returnThreeMonth") : String.format("%.2f", Double.parseDouble(apiValue.get("returnThreeMonth"))) + "%");
	}

	// Verify Earnings data
	public void validateEarnings_Data(String symbol) throws Exception {
		basePage.scroll(factor20Page.headingEarnings);
		factor20ApiRes = get20FactorApiRes(symbol);

		// Earnings: 5-Year EPS>>EPS
		Assert.assertTrue(year5EarningsCalculation(symbol, "EPS", "annualEPS", "eps"));

		// Earnings: 5-Year EPS>>EPS % Growth
		Assert.assertTrue(year5EarningsCalculation(symbol, "EPS % Growth", "ePSPercentageGrowth", "growth"));

		// Earnings: 5-Year Revenue>>Revenue (M)
		Assert.assertTrue(year5EarningsCalculation(symbol, "Revenue (M)", "annualRevenue", "revenue"));

		// Earnings: 5-Year Revenue>>Rev % Growth
		Assert.assertTrue(year5EarningsCalculation(symbol, "Rev % Growth", "revenuePercentageGrowth", "growth"));

		// Earnings: EPS Surprise>>Current Qtr
		Assert.assertTrue(epsSurpriseEarningsCalculation(symbol, "Current Qtr", 0));

		// Earnings: EPS Surprise>>1 Qtrs Ago
		Assert.assertTrue(epsSurpriseEarningsCalculation(symbol, "1 Qtrs Ago", 1));

		// Earnings: EPS Surprise>>2 Qtrs Ago
		Assert.assertTrue(epsSurpriseEarningsCalculation(symbol, "2 Qtrs Ago", 2));

		// Earnings: EPS Surprise>>3 Qtrs Ago
		Assert.assertTrue(epsSurpriseEarningsCalculation(symbol, "3 Qtrs Ago", 3));

		// Earnings: EPS Estimates>>Quarterly EPS
		apiValue = factor20ApiRes.getMap("data.earningsData.ePSEstimates[0]");
		uiValue = getTextUiEarnings("Quarterly EPS");
		Assert.assertEquals(uiValue.get("Actual EPS Prev"), apiValue.get("actualEPSPrev"));
		Assert.assertEquals(uiValue.get("EST EPS Current"), apiValue.get("estEPSCurrent"));
		Assert.assertEquals(uiValue.get("Change"), apiValue.get("change"));

		// Earnings: EPS Estimates>>Yearly EPS
		apiValue = factor20ApiRes.getMap("data.earningsData.ePSEstimates[1]");
		uiValue = getTextUiEarnings("Yearly EPS");
		Assert.assertEquals(uiValue.get("Actual EPS Prev"), apiValue.get("actualEPSPrev"));
		Assert.assertEquals(uiValue.get("EST EPS Current"), apiValue.get("estEPSCurrent"));
		Assert.assertEquals(uiValue.get("Change"), apiValue.get("change"));

		// Earnings: EPS Actual Growth>>3-5 year EPS
		apiValue = factor20ApiRes.getMap("data.earningsData.ePSEstimates[2]");
		uiValue = getTextUiEarnings("3-5 year EPS");
		Assert.assertEquals(uiValue.get("Actual EPS Growth"), apiValue.get("actualEPSPrev"));
		Assert.assertEquals(uiValue.get("EST EPS Current"), apiValue.get("estEPSCurrent"));
		Assert.assertEquals(uiValue.get("Change"), apiValue.get("change"));
	}

	// Verify Technical data
	public void validateTechnicals_PriceActivity(String symbol) throws Exception {
		basePage.scroll(factor20Page.headingEarnings);
		factor20ApiRes = get20FactorApiRes(symbol);
		// Technical: Price Activity
		apiValue = factor20ApiRes.getMap("data.technicalsData.priceActivity");
		ExtentLogger.info("Technicals: priceActivity<br>" + apiValue.toString());
		uiValue = getTextUiFinancialsTechnicalExpertsData("Price Activity");
		Assert.assertEquals(uiValue.get("52 Week High"), String.format("%.2f", Double.parseDouble(apiValue.get("high52Week"))));
		Assert.assertEquals(uiValue.get("52 Week Low"), String.format("%.2f", Double.parseDouble(apiValue.get("low52Week"))));
	}

	// Technical: Price % Change
	public void validateTechnicals_PriceChange(String symbol) throws Exception {
		factor20ApiRes = get20FactorApiRes(symbol);
		apiValue = factor20ApiRes.getMap("data.technicalsData.priceChangePtc");
		ExtentLogger.info("Technicals: priceChangePtc<br>" + apiValue.toString());
		uiValue = getTextUiFinancialsTechnicalExpertsData("Price % Change");
		Assert.assertEquals(uiValue.get("% Chg 4wk Rel S&P"), String.format("%.2f", Double.parseDouble(apiValue.get("chg4weekRelativeToSPY"))) + "%");
		Assert.assertEquals(uiValue.get("% Chg 24wk Rel S&P"), String.format("%.2f", Double.parseDouble(apiValue.get("chg24weekRelativeToSPY"))) + "%");
	}

	// Technical: Volume Activity
	public void validateTechnicals_VolumeActivity(String symbol) throws Exception {
		factor20ApiRes = get20FactorApiRes(symbol);
		apiValue = factor20ApiRes.getMap("data.technicalsData.volumeActivity");
		ExtentLogger.info("Technicals: volumeActivity<br>" + apiValue.toString());
		uiValue = getTextUiFinancialsTechnicalExpertsData("Volume Activity");
		Assert.assertEquals(uiValue.get("Avg. Vol 20 days").replace(",", ""), basePage.truncateNumber(apiValue.get("avgVolume20Days")));
		Assert.assertEquals(uiValue.get("Avg. Vol 90 days").replace(",", ""), basePage.truncateNumber(apiValue.get("avgVolume90Days")));
	}

	// Technical: Volatility Rel. to Mkt
	public void validateTechnicals_Volatility(String symbol) throws Exception {
		factor20ApiRes = get20FactorApiRes(symbol);
		apiValue = factor20ApiRes.getMap("data.technicalsData.volatilityRelativeToMarket");
		ExtentLogger.info("Technicals: volatilityRelativeToMarket<br>" + apiValue.toString());
		uiValue = getTextUiFinancialsTechnicalExpertsData("Volatility Rel. to Mkt");
		// Convert value for Volatile
		String expectedUiVolatility;
		double beta = Double.parseDouble(apiValue.get("beta"));
		if (beta < 1) {
			expectedUiVolatility = "Less Volatile";
		} else if (beta > 1) {
			expectedUiVolatility = "More Volatile";
		} else
			expectedUiVolatility = "Equally Volatile";
		Assert.assertEquals(uiValue.get("Beta"), String.format("%.2f", Double.parseDouble(apiValue.get("beta"))));
		Assert.assertEquals(uiValue.get("Volatility"), expectedUiVolatility);
	}

	// Experts: Earnings Estimates Revisions
	public void validateExperts_EarningsEstimates(String symbol) throws Exception {
		basePage.scroll(factor20Page.headingExperts);
		factor20ApiRes = get20FactorApiRes(symbol);
		apiValue = factor20ApiRes.get("data.expertData.earningEstimateRevision");

		// Experts: Earnings Estimates Revisions>>Current Quarter
		uiValue = expertsEarningsEstimatesRevisions(symbol, "Current Quarter");
		Assert.assertEquals(uiValue.get("Current"), apiValue.get("currentQuarterCurrent"));
		Assert.assertEquals(uiValue.get("30 Days Ago"), apiValue.get("currentQuarter30Dago"));
		Assert.assertEquals(uiValue.get("%Change"), apiValue.get("currentQuarterPercentChange"));

		// Experts: Earnings Estimates Revisions>>Next Quarter
		uiValue = expertsEarningsEstimatesRevisions(symbol, "Next Quarter");
		Assert.assertEquals(uiValue.get("Current"), apiValue.get("nextQuarterCurrent"));
		Assert.assertEquals(uiValue.get("30 Days Ago"), apiValue.get("nextQuarter30Dago"));
		Assert.assertEquals(uiValue.get("%Change"), apiValue.get("nextQuarterPercentChange"));
	}

	// Experts: Short Interest
	public void validateTechnicals_ShortInterest(String symbol) throws Exception {
		factor20ApiRes = get20FactorApiRes(symbol);
		int shortIntrestApivalue = factor20ApiRes.getInt("data.expertData.expertsRating.shortInterest");

		// class attribute will return 'class="statLabel green"'. Split it and extract color name.
		String[] uiShortIntrestLowColor = basePage.getAttribute(factor20Page.tdExpertsShortInterest("Low"), "class").split(" ");
		String[] uiShortIntrestMediumColor = basePage.getAttribute(factor20Page.tdExpertsShortInterest("Medium"), "class").split(" ");
		String[] uiShortIntrestHighColor = basePage.getAttribute(factor20Page.tdExpertsShortInterest("High"), "class").split(" ");

		if (shortIntrestApivalue < 3) {
			Assert.assertEquals(uiShortIntrestHighColor[1], "red");
			Assert.assertEquals(uiShortIntrestMediumColor[1], "grey");
			Assert.assertEquals(uiShortIntrestLowColor[1], "grey");
		} else if (shortIntrestApivalue == 3) {
			Assert.assertEquals(uiShortIntrestHighColor[1], "grey");
			Assert.assertEquals(uiShortIntrestMediumColor[1], "yellow");
			Assert.assertEquals(uiShortIntrestLowColor[1], "grey");
		} else {
			Assert.assertEquals(uiShortIntrestHighColor[1], "grey");
			Assert.assertEquals(uiShortIntrestMediumColor[1], "grey");
			Assert.assertEquals(uiShortIntrestLowColor[1], "green");
		}
	}

	// Experts: WallStreetConsensus
	public void validateTechnicals_WallStreetConsensus(String symbol) throws Exception {
		factor20ApiRes = get20FactorApiRes(symbol);
		apiValue = factor20ApiRes.get("data.expertData.recommendations");
		uiValue = getTextUiFinancialsTechnicalExpertsData("Wall Street Consensus");
		Assert.assertEquals(uiValue.get("This Week"), apiValue.get("meanThisWeek"));
		Assert.assertEquals(uiValue.get("Last Week"), apiValue.get("meanLastWeek"));
		Assert.assertEquals(uiValue.get("5 Weeks Ago"), apiValue.get("fiveweekago"));

		String[] uiThisWeekValueColor = basePage.getAttribute(factor20Page.tdWallStreet("This Week"), "class").split(" ");
		String[] uiLastWeekValueColor = basePage.getAttribute(factor20Page.tdWallStreet("Last Week"), "class").split(" ");
		String[] ui5WeekAgoValueColor = basePage.getAttribute(factor20Page.tdWallStreet("5 Weeks Ago"), "class").split(" ");

		Assert.assertEquals(uiThisWeekValueColor[1], returnColourWallStreetConsensus(apiValue.get("meanThisWeek")));
		Assert.assertEquals(uiLastWeekValueColor[1], returnColourWallStreetConsensus(apiValue.get("meanLastWeek")));
		Assert.assertEquals(ui5WeekAgoValueColor[1], returnColourWallStreetConsensus(apiValue.get("fiveweekago")));

	}

	// Validate Graph view
	public void validateGraphView() throws Exception {
		basePage.scroll(factor20Page.headingReturn);
		basePage.click(factor20Page.btnDataTableToggle, "Data Table Toggle");
		basePage.verifyElement(factor20Page.headingGraphView, "Graph Heading");
		basePage.scroll(factor20Page.headingGraphView);
	}

	public String returnColourWallStreetConsensus(String recommendation) {
		return switch (recommendation) {
			case "Strong Buy" -> "green";
			case "Buy" -> "green";
			case "Hold" -> "yellow";
			case "Sell" -> "red";
			case "Strong Sell" -> "red";
			default -> "grey";
		};
	}

	public void validateMainPgrRating(String symbol) throws Exception {
		JsonPath pgrApiRes = basePage.makeGetApiCall(apiUrl.pgrPageApiUrl(symbol));
		String pgrValue = basePage.getText(factor20Page.pgrValueHeading); // This will extract the main rating of stock-ETF
		basePage.getPgrValue(pgrApiRes, pgrValue);
	}

	public void validateLeftSideStockEtfSectionData(String symbol) throws Exception {
		basePage.validateLeftSideStockEtfSectionData(symbol);
	}

	// For Chart flow
	public void validateChartData(String symbol, String chartTimePeriod) throws Exception {
		if (!chartTimePeriod.equalsIgnoreCase("")) {
			basePage.click(factor20Page.clickChartTimePeriod(chartTimePeriod), chartTimePeriod);
			Boolean result = basePage.validateChartData(symbol);
			Assert.assertTrue(result);
		}
	}

	// This function will return the Ui values financial, Technical & experts(wall street) visible on 20factor page.
	public Map<String, String> getTextUiFinancialsTechnicalExpertsData(String categoryName){
		StringBuilder info = new StringBuilder();
		int count = basePage.getSize(factor20Page.trFinancialsTechnicalsExperts(categoryName));
		for (int index = 1; index <= count; index++) {
			key = basePage.getText(factor20Page.tdFinancialsTechnicalsExpertsKey(categoryName, index));
			value = basePage.getText(factor20Page.tdFinancialsTechnicalsExpertsValue(categoryName, index));
			map.put(key, value);
			info.append(key).append(" = ").append(value).append("<br>");
		}
		ExtentLogger.info("UI values of section [" + categoryName + "]<br>" + info);
		return map;
	}

	// This function will return the Ui values of the earnings sections visible on 20factor page.
	public Map<String, String> getTextUiEarnings(String categoryName){
		int count = basePage.getSize(factor20Page.trEarnings(categoryName));
		for (int index = 1; index < count; index++) {
			key = basePage.getText(factor20Page.tdEarningsKey(categoryName, index));
			value = basePage.getText(factor20Page.tdEarningsValue(categoryName, index));
			map.put(key, value);
		}
		return map;
	}

	// This function will return the Ui values of power rating bars visible on 20factor page.
	public Map<String, String> getTextSlidebar(String categoryName){
		basePage.scroll(factor20Page.trTextSlidebar(categoryName));
		StringBuilder info = new StringBuilder();
		int count = basePage.getSize(factor20Page.trTextSlidebar(categoryName));
		for (int index = 1; index <= count; index++) {
			key = basePage.getText(factor20Page.tdTextSlidebarKey(categoryName, index));
			value = basePage.getAttribute(factor20Page.tdTextSlidebarAttribute(categoryName, index), "class");
			String uiRating = value;
			if (!uiRating.equalsIgnoreCase("slider-disc ")) {
				String lastWord = value.substring(value.lastIndexOf(" ") + 1);
				String[] parts = lastWord.split("-");
				uiRating = parts[1];
			}
			value = basePage.convertRating(uiRating);
			map.put(key, value);
			info.append(key).append(" = ").append(value).append("<br>");
		}
		ExtentLogger.info("Slidebar rating of [" + categoryName + "]<br>" + info);
		return map;
	}

	// This function is used for convert & format the APi response if getting "NA".
	Map<String, String> formatApiResponse(String section, String symbol) throws Exception {
		factor20ApiRes = get20FactorApiRes(symbol);
		Map<String, String> apiResMap = factor20ApiRes.getMap(section);
		Set<String> keySet = apiResMap.keySet();
		List<String> listKeys = new ArrayList<>(keySet);
		StringBuilder apiResult = new StringBuilder();
		for (int h = 0; h < apiResMap.size(); h++) {
			key = listKeys.get(h);
			String format = apiResMap.get(key);
			if (!(apiResMap.get(key)).equalsIgnoreCase("NA"))
				format = String.format("%.2f", Double.valueOf(apiResMap.get(key)));
			apiResult.append(key).append(" = ").append(apiResMap.get(key)).append(" ~ ").append(format).append("<br>");
			map.put(key, format);
		}
		ExtentLogger.info("API Result of [" + section + "]:<br>" + apiResult);
		return map;
	}

	// This function is used for the calculation of EPS Surprise Earnings. Finding difference and Difference%.
	public Boolean epsSurpriseEarningsCalculation(String symbol, String category, int index) throws Exception {
		factor20ApiRes = get20FactorApiRes(symbol);
		Map<String, String> apiResMap = factor20ApiRes.getMap("data.earningsData.earningAnnouncement[" + index + "]");
		Map<String, String> uiValue = getTextUiEarnings(category);
		double[] arr = new double[5];
		arr[0] = Double.parseDouble(String.format("%.2f", apiResMap.get("estimatedEps")));// Estimate
		arr[1] = Double.parseDouble(apiResMap.get("actualEps"));// Actual
		arr[2] = Double.parseDouble(String.format("%.2f", apiResMap.get("difference")));// Difference
		arr[3] = Double.parseDouble(String.format("%.2f", apiResMap.get("differencePercentage")));// Difference%

		Assert.assertEquals(uiValue.get("Estimate"), "$" + String.format("%.2f", arr[0]));
		Assert.assertEquals(uiValue.get("Actual"), "$" + String.format("%.2f", arr[1]));
		Assert.assertEquals(uiValue.get("Difference"), "$" + String.format("%.2f", arr[2]));
		Assert.assertEquals(uiValue.get("% Diff"), String.format("%.2f", arr[3]) + "%");

		return uiValue.get("Estimate").equalsIgnoreCase("$" + String.format("%.2f", arr[0])) && (uiValue.get("Actual").equalsIgnoreCase("$" + String.format("%.2f", arr[1]))) && (uiValue.get("Difference").equalsIgnoreCase("$" + String.format("%.2f", arr[2]))) && (uiValue.get("% Diff").equalsIgnoreCase(
				String.format("%.2f", arr[3]) + "%"));
	}

	// This function is used to calculate the Ui values of 5year earnings section.
	public Boolean year5EarningsCalculation(String symbol, String categoryName, String earningsDataCategory, String earningsDataKey) throws Exception {
		factor20ApiRes = get20FactorApiRes(symbol);
		Map<String, String> uiValue = getTextUiEarnings(categoryName);
		String[] arr = new String[5];
		Map<String, String> apiResMap;
		boolean status = true;
		for (int index = 0; index < 5; index++) {
			apiResMap = factor20ApiRes.getMap("data.earningsData." + earningsDataCategory + "[" + index + "]");
			arr[index] = apiResMap.get(earningsDataKey);
			if (!arr[index].equalsIgnoreCase("N/A")) {
				key = basePage.getText(factor20Page.tdYear5EarningsKey(categoryName, index));
				Assert.assertEquals(uiValue.get(key), arr[index]);
				if (!uiValue.get(key).contains(arr[index])) {
					status = false;
					break;
				}
			}
		}
		return status;
	}

	// This function is used to calculate the Ui values of Earnings Estimates Revisions section.
	public HashMap<String, String> expertsEarningsEstimatesRevisions(String symbol, String categoryName) throws Exception {
		factor20ApiRes = get20FactorApiRes(symbol);
		int count = basePage.getSize(factor20Page.trExperts(categoryName));
		for (int index = 1; index < count; index++) {
			key = basePage.getText(factor20Page.tdExpertsKey(categoryName, index));
			value = basePage.getText(factor20Page.tdExpertsValue(categoryName, index));
			map.put(key, value);
		}
		return map;
	}
}