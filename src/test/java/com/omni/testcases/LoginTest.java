package com.omni.testcases;

import java.util.Hashtable;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.omni.base.BaseTest;
import com.omni.helpers.LoginHelper;
import com.omni.utilities.TestUtil;

public class LoginTest extends BaseTest {
	LoginHelper loginHelper = new LoginHelper();

	// Test for forgot password screen close button.
	@Test(priority = 1)
	public void testCloseButtonOnForgetPassowrdScreen() throws Exception {
		loginHelper.verifyLoginPage();
		loginHelper.clickingOnForgetPasswordButton();
		loginHelper.verifyCloseButtonOnForgetPassowrdScreen();
		loginHelper.verifyLoginPage();
	}

	// Test for forgot password functionality.
	@Test(priority = 2, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testForgotPassword(Hashtable<String, String> data) throws Exception {
			if (data.get("flow").equalsIgnoreCase("forgot_password")) {
				loginHelper.enteringDetailsInForgetPasswordEmailField(data.get("email_id"));
				loginHelper.clickingOnSendPasswordButton();
				if (!data.get("email_id").equalsIgnoreCase("")) {
					loginHelper.handleAlert();
				}
				loginHelper.verifyLoginPage();
			}
		}
	

	// Test for login functionality in OMNI application.
	@Test(priority = 3, dataProviderClass = TestUtil.class, dataProvider = "dp")
	public void testLoginLogout(Hashtable<String, String> data) throws Exception {
		if (data.get("flow").equalsIgnoreCase("login_logout")) {
			loginHelper.enterDetailsOnLoginScreen(data);
			loginHelper.clickingOnLoginButton();
			Boolean status=loginHelper.validatePinnedHomePage();
			loginHelper.logoutAndValidateLoginPage();
			Assert.assertTrue(status);
		}
	}
}
