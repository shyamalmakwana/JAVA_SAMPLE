package com.omni.helpers;

import java.util.Hashtable;

import org.junit.Assert;

import com.omni.base.BasePage;
import com.omni.pages.HomePage;
import com.omni.pages.LoginPage;

public class LoginHelper {
	LoginPage loginPage = new LoginPage();
	HomePage homePage = new HomePage();
	BasePage basePage = new BasePage();

	public void clickingOnForgetPasswordButton() throws Exception {
		basePage.verifyElement(loginPage.lnkForgotPassword, "Forgot Password link");
		basePage.click(loginPage.lnkForgotPassword, "Forgot Passsword link");
	}

	public void verifyCloseButtonOnForgetPassowrdScreen() throws Exception {
		basePage.verifyElement(loginPage.btnForgotPasswordClose, "Forgot Password Modal Close button");
		basePage.click(loginPage.btnForgotPasswordClose, "Forgot Password Modal Close button");
	}

	public void clickingOnSendPasswordButton() throws Exception {
		basePage.click(loginPage.btnSendPassword, "Send Password button");
	}

	public void enterDetailsOnLoginScreen(Hashtable<String, String> data){
		basePage.type(loginPage.txtEmail, data.get("email_id"), "Email Address");
		basePage.type(loginPage.txtPassword, data.get("password"), "Passowrd");
	}

	public void enteringDetailsInForgetPasswordEmailField(String email) throws Exception {// need to pass by testcase null value and valid value
		basePage.click(loginPage.lnkForgotPassword, "Forgot Password link");
		basePage.type(loginPage.txtForgotPasswordEmail, email, "Email Address");
	}

	public void handleAlert() throws Exception {
		// click on 'Ok' button in alert.
		basePage.acceptAlert(loginPage.alertMsg);
	}

	public void clickingOnLoginButton() throws Exception {
		// click on 'Login' button.
		basePage.click(loginPage.btnLogin, "Login Button");
	}

	public void verifyLoginPage(){
		Assert.assertTrue(basePage.verifyElement(loginPage.imgLogo, "Chaikin Analytics Logo"));
		Assert.assertTrue(basePage.verifyElement(loginPage.btnLogin, "Login button"));
	}

	// This function is used to verify the pinned page and current page.
	public boolean validatePinnedHomePage() throws Exception {
		basePage.verifyElement(homePage.btnMyAccount, "My Account Button");
		basePage.click(homePage.btnMyAccount, "My Account");
		basePage.verifyElement(homePage.ddlCurrentPinnedHomepage, "Current Pinned Homepage Dropdown");
		basePage.click(homePage.ddlCurrentPinnedHomepage, "ddropdown");
		int pinnedPageCount = basePage.getSize(homePage.currentPinnedHomepage);//If no pinned page set for that user.
		String currentHomePage;
		if (pinnedPageCount != 0)
			currentHomePage = basePage.getText(homePage.currentPinnedHomepage);
		else
			currentHomePage = basePage.getText(homePage.myPublications);
		// click(HomePage.btnMyAccount, "My Account");
		basePage.verifyElement(homePage.lnkActivePage, "Active Page");
		String activePage = basePage.getText(homePage.lnkActivePage);
		basePage.click(homePage.btnMyAccount, "My Account Button");
		return currentHomePage.equalsIgnoreCase(activePage);
	}

	// This function is used for logout from OMNI application.
	public void logoutAndValidateLoginPage() throws Exception {
		basePage.verifyElement(homePage.btnLogout, "Logout Button");
		basePage.click(homePage.btnLogout, "Logout Button");
		// basePage.verifyElement(homePage.btnClickHere, "Click Here button");
		// basePage.click(homePage.btnClickHere, "Click Here button");
		basePage.verifyElement(loginPage.btnLogin, "Login Button");
	}
}
