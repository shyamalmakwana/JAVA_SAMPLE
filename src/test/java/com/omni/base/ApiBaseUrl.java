package com.omni.base;

import com.omni.utilities.ReadPropertyFile;

public class ApiBaseUrl {
	ReadPropertyFile readPropertyFile = new ReadPropertyFile();

	public String whatIsPowergaugeApiUrl() throws Exception {
		return readPropertyFile.get("baseURI") + "/article/what-is-the-power-guage";
	}

	public String featuredChaikinHotlistApiUrl() throws Exception {
		return readPropertyFile.get("baseURI") + "/chaikinlist/hotlists";
	}

	public String factor20ApiUrl(String symbol) throws Exception {
		return readPropertyFile.get("baseURI") + "/twentyfactors/" + symbol;
	}

	public String hotlistContainingApiUrl(String ticker) throws Exception {
		return readPropertyFile.get("baseURI") + "/chaikinlist/recommendation/" + ticker;
	}

	public String getETFSpecificDataApiUrl(String symbol) throws Exception {
		return readPropertyFile.get("baseCptRestSecureURI") + "/portfolioWise/getETFSpecificData?ticker=" + symbol;
	}

	public String getChecklistStockApiUrl(String symbol) throws Exception {
		return readPropertyFile.get("baseCptRestSecureURI") + "/portfolio/getChecklistStocks?symbol=" + symbol;
	}

	public String pgrPageApiUrl(String symbol) throws Exception {
		return readPropertyFile.get("baseCptRestSecureURI") + "/portfolio/getSymbolData?symbol=" + symbol + "&components=pgr,metaInfo,EPSData,fundamentalData,technical";
	}

	public String getExpandedAssociatedListDataApiUrl(String symbol) throws Exception {
		return readPropertyFile.get("baseCptRestSecureURI") + "/portfolioWise/getExpandedAssociatedListData?symbol=" + symbol;
	}

	public String getETFConstituentSpecificDataApiUrl(String symbol) throws Exception {
		return readPropertyFile.get("baseCptRestSecureURI") + "/portfolioWise/getETFConstituentSpecificData?ticker=" + symbol;
	}

	public String newsApiUrl(String ticker, String sessionId) throws Exception {
		return readPropertyFile.get("baseCptRestSecureURI") + "/xigniteNews/getHeadlines?symbol=";
	}

	public String getResultListsApiUrl(String ticker) throws Exception {
		return readPropertyFile.get("baseCptRestSecureURI") + "/discoveryEngine/getResultLists?stock=" + ticker;
	}

	public String getjwtAuthorizationApiUrl(String sessionToken) throws Exception {
		return readPropertyFile.get("baseCptRestSecureURI") + "/authenticate/getJWTAuthorization?jwtToken=" + sessionToken;
	}

	public String getPowerFeedMarketViewApiUrl() throws Exception {
		return readPropertyFile.get("baseCptRestSecureURI") + "/powerGauge/getPowerFeedMarketview?tickerCSV=SPY,DIA,QQQ,IWV,IWM,TLT,IEF,GLD";
	}

	public String getWatchlistApiUrl() throws Exception {
		return readPropertyFile.get("baseURI") + "/chaikinlist/watchlists";
	}

	public String getViewAllWatchlistApiUrl(String count, String page) throws Exception {
		return readPropertyFile.get("baseURI") + "/chaikinlist/mylists/watchlist?count=" + count + "&page=" + page + "&sortField=strength&sortOrder=desc";
	}
	
	public String getViewAllHotlistApiUrl(String count, String page, String category) throws Exception {
		return readPropertyFile.get("baseURI") + "/chaikinlist/mylists/hotlists?count=" + count + "&page=" + page + "&sortField=strength&sortOrder=desc&categories=" + category;
	}
	
	public String getViewAllIndustryApiUrl(String count, String page) throws Exception {
		return readPropertyFile.get("baseURI") + "/chaikinlist/mylists/industry?count=" + count + "&page=" + page + "&sortField=strength&sortOrder=desc";
	}
	
	public String getViewAllIndexApiUrl(String count, String page) throws Exception {
		return readPropertyFile.get("baseURI") + "/chaikinlist/mylists/index?count=" + count + "&page=" + page + "&sortField=strength&sortOrder=desc";
	}

	public String getViewAllPublicationApiUrl(String count, String page) throws Exception {
		return readPropertyFile.get("baseURI") + "/chaikinlist/mylists/publication?count=" + count + "&page=" + page + "&sortField=strength&sortOrder=desc";
	}

	public String getViewAllEtfApiUrl(String count, String page, String etfListId) throws Exception {
		return readPropertyFile.get("baseURI") + "/chaikinlist/mylists/etf?count=" + count + "&page=" + page + "&sortField=strength&sortOrder=desc&etfListId=" + etfListId;
	}

	public String getPublicationsApiUrl() throws Exception {
		return readPropertyFile.get("baseURI") + "/chaikinlist/mylists/publication";
	}

	public String getChaikinHotlistApiUrl() throws Exception {
		return readPropertyFile.get("baseURI") + "/chaikinlist/hotlists";
	}

	public String getBookmarksRecentlySavedApiUrl() throws Exception {
		return readPropertyFile.get("baseURI") + "/article/bookmarks?sortField=modifiedAt&sortDirection=asc";
	}

	public String postAuthenticateApiUrl() throws Exception {
		return readPropertyFile.get("baseURI") + "/user/authenticate";
	}

	public String postArticlesApiUrl() throws Exception {
		return readPropertyFile.get("baseURI") + "/articles";
	}

	public String postSearchApiUrl() throws Exception {
		return readPropertyFile.get("baseURI") + "/search";
	}

	public String postSuggestionsApiUrl() throws Exception {
		return readPropertyFile.get("baseURI") + "/suggestions";
	}

	public String postIndustryExposureApiUrl() throws Exception {
		return readPropertyFile.get("baseURI") + "/chaikinlist/industryexposure";
	}

	public String getAllAlertsApiUrl(int listId) throws Exception {
		return readPropertyFile.get("baseCptRestSecureURI") + "/alerts/getAllAlerts?period=3&list_id=" + String.valueOf(listId);
	}
}
