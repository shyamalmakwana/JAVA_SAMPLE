package com.omni.testcases;

import java.util.Hashtable;

import org.testng.annotations.Test;
import com.omni.base.BaseTest;
import com.omni.helpers.Factor20Helper;
import com.omni.utilities.TestUtil;

public class Factor20Test extends BaseTest {
	Factor20Helper factor20Helper = new Factor20Helper();

	@Test(priority = 1, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testGlobalSearch(Hashtable<String, String> data) throws Exception {
		String[] symbolString = factor20Helper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		factor20Helper.clickOnGlobalSearch();
		factor20Helper.validateGlobalSearchTextFieldAndTypeSymbol(symbol);
		factor20Helper.validateSearcedSymbolVisibleOrNot(symbol);
		factor20Helper.navigateBackOn20factorPage();
	}

	//
	@Test(priority = 2, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testViewFullRatingReportButton(Hashtable<String, String> data) throws Exception {
		String[] symbolString = factor20Helper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		factor20Helper.clickOnSearchedSymbol(symbol);
		factor20Helper.validateStockEtfPage(symbol);
	}

	@Test(priority = 3, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void test20FactorPageAvailableForStock(Hashtable<String, String> data) throws Exception {
		String[] symbolString = factor20Helper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		factor20Helper.clickOnSearchedSymbol(symbol);
		factor20Helper.validateStockEtfPage(symbol);
		factor20Helper.clickFullRatingBtnAndVerify20FactorPage();
	}

	@Test(priority = 4, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testStockSlidebarRating(Hashtable<String, String> data) throws Exception {
		String[] symbolString = factor20Helper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		factor20Helper.clickOnSearchedSymbol(symbol);
		factor20Helper.validateStockEtfPage(symbol);
		factor20Helper.clickFullRatingBtnAndVerify20FactorPage();
		factor20Helper.validateMainPgrRating(symbol);
		factor20Helper.validateLeftSideStockEtfSectionData(symbol);
	}

	@Test(priority = 5, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testChartFunctionality(Hashtable<String, String> data) throws Exception {
		String[] symbolString = factor20Helper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		factor20Helper.clickOnSearchedSymbol(symbol);
		factor20Helper.validateStockEtfPage(symbol);
		factor20Helper.clickFullRatingBtnAndVerify20FactorPage();
		factor20Helper.validateChartData(symbol,data.get("chart"));
	}

	@Test(priority = 6, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testCategoryRating(Hashtable<String, String> data) throws Exception {
		String[] symbolString = factor20Helper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		factor20Helper.clickOnSearchedSymbol(symbol);
		factor20Helper.validateStockEtfPage(symbol);
		factor20Helper.clickFullRatingBtnAndVerify20FactorPage();
		factor20Helper.validateCategoryPgrRating(symbol);
	}

	@Test(priority = 7, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testCategorySlidebarsRating(Hashtable<String, String> data) throws Exception {
		String[] symbolString = factor20Helper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		factor20Helper.clickOnSearchedSymbol(symbol);
		factor20Helper.validateStockEtfPage(symbol);
		factor20Helper.clickFullRatingBtnAndVerify20FactorPage();
		factor20Helper.validateSlidebarFinancials(symbol);
		factor20Helper.validateSlidebarEarnings(symbol);
		factor20Helper.validateSlidebarTechnicals(symbol);
		factor20Helper.validateSlidebarExperts(symbol);
	}

	@Test(priority = 8, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testFinancialsSectionData(Hashtable<String, String> data) throws Exception {
		String[] symbolString = factor20Helper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		factor20Helper.clickOnSearchedSymbol(symbol);
		factor20Helper.validateStockEtfPage(symbol);
		factor20Helper.clickFullRatingBtnAndVerify20FactorPage();
		factor20Helper.validateFinancials_AssetAndLiability(symbol);
		factor20Helper.validateFinancials_Valuation(symbol);
		factor20Helper.validateFinancials_Dividends(symbol);
		factor20Helper.validateFinancials_Return(symbol);
	}

	@Test(priority = 9, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testEarningsSectionData(Hashtable<String, String> data) throws Exception {
		String[] symbolString = factor20Helper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		factor20Helper.clickOnSearchedSymbol(symbol);
		factor20Helper.validateStockEtfPage(symbol);
		factor20Helper.clickFullRatingBtnAndVerify20FactorPage();
		factor20Helper.validateEarnings_Data(symbol);
	}

	@Test(priority = 10, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testTechnicalsSectionData(Hashtable<String, String> data) throws Exception {
		String[] symbolString = factor20Helper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		factor20Helper.clickOnSearchedSymbol(symbol);
		factor20Helper.validateStockEtfPage(symbol);
		factor20Helper.clickFullRatingBtnAndVerify20FactorPage();
		factor20Helper.validateTechnicals_PriceActivity(symbol);
		factor20Helper.validateTechnicals_PriceChange(symbol);
		factor20Helper.validateTechnicals_VolumeActivity(symbol);
		factor20Helper.validateTechnicals_Volatility(symbol);
	}

	@Test(priority = 11, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testExpertsSectionData(Hashtable<String, String> data) throws Exception {
		String[] symbolString = factor20Helper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		factor20Helper.clickOnSearchedSymbol(symbol);
		factor20Helper.validateStockEtfPage(symbol);
		factor20Helper.clickFullRatingBtnAndVerify20FactorPage();
		factor20Helper.validateExperts_EarningsEstimates(symbol);
		factor20Helper.validateTechnicals_ShortInterest(symbol);
		factor20Helper.validateTechnicals_WallStreetConsensus(symbol);
	}

	@Test(priority = 12, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testDataTableToggleButton(Hashtable<String, String> data) throws Exception {
		String[] symbolString = factor20Helper.pickRandomSymbol(data);
		String symbol = symbolString[0];
		factor20Helper.clickOnSearchedSymbol(symbol);
		factor20Helper.validateStockEtfPage(symbol);
		factor20Helper.clickFullRatingBtnAndVerify20FactorPage();
		factor20Helper.validateGraphView();
	}

}
