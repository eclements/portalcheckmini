package portalcheck;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import core.JSWaiter;
import core.SeleniumHelper;
import core.Stopwatch;
import java.util.Date;

public class Trello_Flow01 {

	final static Logger logger = Logger.getLogger(Trello_Flow01.class);
	
	//The portalHelper contains your portal URL and login details:
	private Portal portalHelper = new Portal();
	
	//The seleniumHelper contains wrapper methods to do Selenium commands for you in an easy way:
	private SeleniumHelper seleniumHelper = new SeleniumHelper();
	
	//The title of the card this test will create.
	private String currentCardTitle = "";

	//We keep a main method so that we can run the tests in via our IDE (Eclipse) without testNG:
	public static void main(String[] args) {
		logger.debug("main() - START");

		try {
			// Start the test case:
			Trello_Flow01 flow = new Trello_Flow01();
			flow.runAllTests();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		logger.debug("main() - END");
	}

	/* Internal method to envoke all tests in correct order */
	public void runAllTests() {
		logger.debug("runAllTests() - START");

		try {

			Stopwatch sw = new Stopwatch();
			
			doPrepare();
			
			doStep01();		//open website
			doStep02();		//login
			doStep03();		//Open a board
			doStep04();		//Create a card
			doStep05();		//Edit card
			doStep06();		//Move card
			doStep07();		//Archive card
			//doStep08();	//placeholder for your step
			//doStep09();	//placeholder for your step
			//doStep10();	//placeholder for your step
			
			//Overall time taken:
			sw.logTimeTaken();	
			
			doLogout();			
			

		} catch (Exception ex) {
			logger.debug("Problem during step orchestration: " + ex.getMessage());
		}
		
		logger.debug("runAllTests() - END");

	}

	/* Prepare any dependancies needed for the flow to run, for example browser needs to be open */
	@Test(description = "Step 0 - Prepare")
	public void doPrepare() {
		logger.debug("doPrepare() - START");
		
		Stopwatch sw = new Stopwatch();
		SoftAssert softAssert = new SoftAssert();

		try {

			seleniumHelper.startBrowserInWebDriver();

		} catch (Exception ex) {
			logger.debug("Could not open browser: " + ex.getMessage());
			ex.printStackTrace();
		}

		logger.debug("doPrepare() - END");
		sw.logTimeTaken();	
		
		softAssert.assertAll();
	}

	/* Step 01 is typically opening your web URL */
	@Test(description = "Step 01", dependsOnMethods = { "doPrepare" })
	public void doStep01() {
		logger.debug("doStep01 - START");
		
		SoftAssert softAssert = new SoftAssert();
		Stopwatch sw = new Stopwatch();

		try {

			// Open the portal URL in the webdriver
			seleniumHelper.openURLInWebDriver( portalHelper.getPortalURL());
			
			seleniumHelper.waitForText("Log In");

		} catch (Exception ex) {
			logger.error("Problem with Step01: " + ex.getMessage());
		}

		sw.logTimeTaken();		
		softAssert.assertAll();

		logger.debug("doStep01 - END");
	}

	/* Step 02 is typically login */
	@Test(description = "Step 02", dependsOnMethods = { "doStep01" })
	public void doStep02() throws Exception {
		logger.debug("doStep02() - START");
		
		String stepName = "Step 02";
		
		Stopwatch sw = new Stopwatch();
		
		SoftAssert softAssert = new SoftAssert();
		String xpath_string = "";
		
		boolean isStepCompleted = false;
		
		try {	
			boolean isLoginReady = false;
			
			//Close the cookies button bar:
			boolean isExistsCookiesBar = false;
			String cookiesButtonText = "Accept Cookies";
			isExistsCookiesBar = seleniumHelper.checkIfExists(cookiesButtonText, "text", true);
			if (isExistsCookiesBar) {
				logger.debug("Found and clicking cookies button...");	
				seleniumHelper.click("button", "text", cookiesButtonText,true);
			}
			
			seleniumHelper.waitForText( "Log In");
			//seleniumHelper.clickALinkByText("Log In", true);
			seleniumHelper.click("a", "text", "Log In", true);
			
			seleniumHelper.waitForText( "Log in to Trello");
			
			logger.debug("Write username...");
			seleniumHelper.inputTextById( "user", portalHelper.getPortalUsername(), true);

			logger.debug("Write password...");
			seleniumHelper.inputTextById("password",portalHelper.getPortalPassword(), true);

			logger.debug("Click Login...");
			seleniumHelper.click("input", "id", "login", true);
			
			logger.debug("Done clicking login button...");
			
			boolean isFound = seleniumHelper.waitUntilTextVisible("Personal Boards");
			
			logger.debug("Done waiting for Personal Boards...: " + isFound);
					
			try {
				isLoginReady = seleniumHelper.waitVisible("h3", "text", "Personal Boards", true);		
			}  catch (Exception ex) {
				logger.debug("Exception checking for Personal Boards: " + ex.getMessage());
				isLoginReady = false;
			}
			logger.debug("isLoginReady: " + isLoginReady);			
			
			softAssert.assertTrue(isLoginReady, "Board screen not displaying.");

			isStepCompleted = true; 
			
		} catch (Exception ex) {
			logger.error("Problem with Step02: " + ex.getMessage());
			isStepCompleted = false;
		}

		softAssert.assertTrue(isStepCompleted, "Problem completing " + stepName);

		sw.logTimeTaken();			
		softAssert.assertAll();
		
		logger.debug("doStep02() - END");
	}

	/* Step 03 is typically to navigation to the starting point of your flow */
	/* Open Board */
	@Test(description = "Step 03", dependsOnMethods = { "doStep02" })
	public void doStep03() {
		logger.debug("doStep03() - START");
		Stopwatch sw = new Stopwatch();
		SoftAssert softAssert = new SoftAssert();

		boolean isPageResultReady = false;
		try {

			logger.debug("Click on portalcheckmini Board...");
			String xpath_string = "/html/body/div[1]/div[2]/div[1]/div[2]/main/div[3]/div/div[2]/div/div/div/div/div[2]/div/div/div/div[2]/ul/li[1]/a/div";
			//Todo: Better Xpath string:"div[contains(@class, 'board-title-details') and contains(.//div, 'portalcheckmini')]";
			
			seleniumHelper.clickOnElementByXpath(xpath_string);
			
			logger.debug("Clicked on Board.");
		
			try {
				isPageResultReady = seleniumHelper.waitVisible("*", "text", "Todo", false);		
			}  catch (Exception ex) {
				logger.debug("Exception checking for screen result: " + ex.getMessage());
				isPageResultReady = false;
			}
			logger.debug("isScreenResultReady: " + isPageResultReady);
			softAssert.assertTrue(isPageResultReady, "Screen result not as expected. ");


		} catch (Exception ex) {
			logger.error("Problem with Step03: " + ex.getMessage());
		}

		sw.logTimeTaken();			
		softAssert.assertAll();
		
		logger.debug("doStep03() - END");
	}

	/* Add card */
	@Test(description = "Step 04", dependsOnMethods = { "doStep03" })
	public void doStep04() {
		logger.debug("doStep04() - START");
		
		Stopwatch sw = new Stopwatch();
		SoftAssert softAssert = new SoftAssert();
		String xpath_string = "";

		boolean isPageResultReady = false;
		try {
					
			logger.debug("Click add another card...");
			seleniumHelper.click("span", "text", "Add another card", false);
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			currentCardTitle = "New card at " + dateFormat.format(date); 
			
			logger.debug("Enter the card title...");
			seleniumHelper.inputTextByCssSelector(".list-card-composer-textarea", currentCardTitle);
			seleniumHelper.sendKeyByCssSelector(".list-card-composer-textarea", Keys.ENTER);
			
			//xpath example:
			//xpath_string = "/html/body/div[1]/div[2]/div[1]/div[2]/main/div[3]/div/div[1]/div[3]/div[1]/div[1]/div/div[2]/div/div[1]/div/textarea";
			//seleniumHelper.inputTextByXpath(xpath_string, currentCardTitle);
			//seleniumHelper.sendKeyByXpath(xpath_string, Keys.ENTER);
			
			isPageResultReady = seleniumHelper.checkIfPageResultVisible(softAssert, currentCardTitle);
		

		} catch (Exception ex) {
			logger.error("Problem with Step04: " + ex.getMessage());
		}

		sw.logTimeTaken();					
		softAssert.assertAll();
		
		logger.debug("doStep04() - END");
	}

	/* Edit Card */
	@Test(description = "Step 05", dependsOnMethods = { "doStep04" })
	public void doStep05() {
		logger.debug("doStep05() - START");
		
		Stopwatch sw = new Stopwatch();
		SoftAssert softAssert = new SoftAssert();
		String xpath_string = "";

		boolean isPageResultReady = false;
		try {
			
			seleniumHelper.waitForText(currentCardTitle, seleniumHelper.defaultWaitForText);
			
			logger.debug("Click on the new card...");
			seleniumHelper.click("span", "text", currentCardTitle, true);
			
			isPageResultReady = seleniumHelper.checkIfPageResultVisible(softAssert, "Description");
			
			logger.debug("Enter a description for the card...");
			seleniumHelper.inputTextByCssSelector(".field", "This is a monitoring card");
			//ALternative with xpath example:
			//xpath_string = "/html/body/div[1]/div[2]/div[3]/div/div/div/div[4]/div[2]/div/div/div[2]/div/div/div[3]/textarea";
			//seleniumHelper.inputTextByXpath(xpath_string, "This is a monitoring card");
			
			logger.debug("Save the description...");
			seleniumHelper.clickOnElementByCssSelector(".mod-submit-edit");
			//Todo: Find way to click by value: seleniumHelper.click("input", "value", "Save", false);
			
			isPageResultReady = seleniumHelper.checkIfPageResultVisible(softAssert, "This is a monitoring card");

			logger.debug("Close the edit window...");
			seleniumHelper.clickOnElementByCssSelector(".icon-md");
			

		} catch (Exception ex) {
			logger.error("Problem with Step05: " + ex.getMessage());
		}

		sw.logTimeTaken();					
		softAssert.assertAll();
		
		logger.debug("doStep05() - END");
	}

	/* Move card */
	@Test(description = "Step 06", dependsOnMethods = { "doStep05" })
	public void doStep06() {
		logger.debug("goToCustomer() - START");
		
		Stopwatch sw = new Stopwatch();
		String xpathString = "";
		SoftAssert softAssert = new SoftAssert();

		try {
			
			logger.debug("Right click on the card...");
			seleniumHelper.rightClick("span", "text", currentCardTitle, true);
			
			logger.debug("Click move...");
			seleniumHelper.clickOnElementByCssSelector("a.quick-card-editor-buttons-item:nth-child(3)");
			
			//logger.debug("Click board select...");
			//Not needed due to how option elements work on trello page
			
			logger.debug("Click on Done board...");
			seleniumHelper.click("option", "text", "Done", true);
			
			logger.debug("Click on Move button...");
			//seleniumHelper.click("input", "value", "Move", true);
			seleniumHelper.clickOnElementByCssSelector("input.primary:nth-child(4)");

		} catch (Exception ex) {
			logger.error("Problem with Step06: " + ex.getMessage());
		}

		logger.debug("goToCustomer() - END");
		sw.logTimeTaken();			
	
		softAssert.assertAll();
	}

	/* Archive card */
	@Test(description = "Step 07", dependsOnMethods = { "doStep06" })
	public void doStep07() {
		logger.debug("doStep07() - START");
		
		Stopwatch sw = new Stopwatch();
		SoftAssert softAssert = new SoftAssert();

		try {

			seleniumHelper.waitForText(currentCardTitle, seleniumHelper.defaultWaitForText);
			
			logger.debug("Right click on the card...");
			seleniumHelper.rightClick("span", "text", currentCardTitle, false);
			
			logger.debug("Click on archive...");
			seleniumHelper.clickSpanByText("Archive", true);
			
			//Close the background popup if visible
			boolean isExistsPopup = false;
			String popupButtonText = "No thanks. Dismiss this.";
			isExistsPopup = seleniumHelper.checkIfExists(popupButtonText, "text", true);
			if (isExistsPopup) {
				logger.debug("Found and clicking background popup hide button...");	
				seleniumHelper.click("span", "text", popupButtonText, true);
			}			
			

		} catch (Exception ex) {
			logger.error("Problem with Step07: " + ex.getMessage());
		}

		sw.logTimeTaken();			
		softAssert.assertAll();
		
		logger.debug("doStep07() - END");
	}

	/*
	@Test(description = "Step 08", dependsOnMethods = { "doStep07" })
	public void doStep08() {
		logger.debug("doStep08() - START");
		
		Stopwatch sw = new Stopwatch();
		SoftAssert softAssert = new SoftAssert();

		try {

			//Place your steps here
			
			//Make sure to add a softAssert of your expected result

		} catch (Exception ex) {
			logger.error("Problem with Step08: " + ex.getMessage());
		}

		sw.logTimeTaken();					
		softAssert.assertAll();
		
		logger.debug("doStep08() - END");
	}
	
	@Test(description = "Step 09", dependsOnMethods = { "doStep08" })
	public void doStep09() {
		logger.debug("doStep09() - START");
		
		Stopwatch sw = new Stopwatch();
		SoftAssert softAssert = new SoftAssert();

		try {

			//Place your steps here
			
			//Make sure to add a softAssert of your expected result

		} catch (Exception ex) {
			logger.error("Problem with Step09: " + ex.getMessage());
		}

		sw.logTimeTaken();					
		softAssert.assertAll();
		
		logger.debug("doStep09() - END");
	}	
	
	@Test(description = "Step 10", dependsOnMethods = { "doStep09" })
	public void doStep10() {
		logger.debug("doStep10() - START");
		
		Stopwatch sw = new Stopwatch();
		SoftAssert softAssert = new SoftAssert();

		try {

			//Place your steps here
			
			//Make sure to add a softAssert of your expected result

		} catch (Exception ex) {
			logger.error("Problem with Step10: " + ex.getMessage());
		}

		sw.logTimeTaken();					
		softAssert.assertAll();
		
		logger.debug("doStep10() - END");
	}	
	*/

	/* Log out and close browser */
	@AfterTest 
	public void doLogout() {
		logger.debug("doLogout() - START");
		Stopwatch sw = new Stopwatch();
		SoftAssert softAssert = new SoftAssert();

		try {

			logger.debug("Click on logout...");
			//seleniumHelper.clickSpanByText("SK", true);
			seleniumHelper.click("span", "text", "SK", true);
			
			//seleniumHelper.clickSpanByText("Log Out", true);
			seleniumHelper.click("span", "text", "Log Out", true);


		} catch (Exception ex) {
			logger.debug("Problem with step 10 (Logout): " + ex.getMessage());
		}
		
		// Close the browser and end the webdriver session:
		logger.debug("Quit the WebDriver and close browser...");
		seleniumHelper.getWebDriver().quit();		

		sw.logTimeTaken();			
		softAssert.assertAll();
		
		logger.debug("doLogout() - END");
	}


}