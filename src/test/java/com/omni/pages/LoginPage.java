package com.omni.pages;

import org.openqa.selenium.By;

public class LoginPage {

	// x-path for login page
	public By imgLogo = By.xpath("//img[@alt='Chaikin Analytics']");
	public By txtEmail = By.xpath("//input[@id='email']");
	public By txtPassword = By.xpath("//input[@id='password']");
	public By btnLogin = By.xpath("//button[text()='Log In']");

	// x-path for forgot password screen
	public By lnkForgotPassword = By.xpath("//button[text()='Forgot Password?']");
	public By btnForgotPasswordClose = By.xpath("//div[@class='modal-content']//button[@class='btn-close']");
	public By txtForgotPasswordEmail = By.xpath("//div[@class='modal-content']//input[@id='email']");
	public By btnSendPassword = By.xpath("//button[text()='Send Password Reset Email']");

	public String alertMsg = "Password reset email sent successfully";
}