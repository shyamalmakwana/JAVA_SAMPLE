package com.omni.pages;

import org.openqa.selenium.By;

public class HomePage {

	// Navbar->My Account
	public By btnMyAccount = By.xpath("//button[text()='My Account']");
	// xpath of drop-down
	public By ddlCurrentPinnedHomepage = By.xpath("//select[contains(@aria-label,'Pinned homepage')]");

	// xpath of selected page visible
	public By currentPinnedHomepage = By.xpath("//select[contains(@aria-label,'Pinned homepage')]//option[@selected]");
	public By myPublications = By.xpath("//select[contains(@aria-label,'Pinned homepage')]//option[@value='/publications']");

	public By lnkActivePage = By.xpath("//a[@aria-current='page']");

	// Navbar->My Account
	public By btnLogout = By.xpath("//span[text()='Logout']");
	
	//"Click Here" button on session logout screen.
	public By btnClickHere = By.xpath("//a[text()='Click here']");
}