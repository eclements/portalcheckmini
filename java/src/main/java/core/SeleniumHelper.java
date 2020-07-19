package core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import portalcheck.Trello_Flow01;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;


public class SeleniumHelper {
	
	final static Logger logger = Logger.getLogger(Trello_Flow01.class);
	
	private WebDriverHelper webDriverHelper = new WebDriverHelper();
	
	//SeleniumHelper specific variables:
	public int defaultXpathWaitS = 60;
	public int threadSleepTimeDropdowns = 2000;
	public int webDriverWaitSeconds = 60;
	public int defaultWaitForText = 60;		
	
	public WebDriver getWebDriver() {
		return webDriverHelper.getDriver();
	}
	
	public SeleniumHelper() {
		logger.debug("SeleniumHelper() - START");
		
		readSeleniumPropertiesFromFile();
				
		logger.debug("SeleniumHelper() - END");
	}
	
	public void startBrowserInWebDriver() {
		webDriverHelper.startBrowser();
	}
	
	private void readSeleniumPropertiesFromFile() {
		logger.debug("readSeleniumPropertiesFromFile() - START");
		System.out.println("readSeleniumPropertiesFromFile() - START");

		try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {

			Properties prop = new Properties();

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			defaultXpathWaitS = Integer.parseInt(prop.getProperty("defaultXpathWaitS"));
			threadSleepTimeDropdowns = Integer.parseInt(prop.getProperty("threadSleepTimeDropdowns"));
			webDriverWaitSeconds = Integer.parseInt(prop.getProperty("webDriverWaitSeconds"));
			defaultWaitForText = Integer.parseInt(prop.getProperty("defaultWaitForText"));

		} catch (IOException ex) {
			System.out.println("ERRROR - Problem parsing properties file. Please fix field formats: " + ex.getMessage());
			System.exit(0);
		}
		logger.debug("readSeleniumPropertiesFromFile() - END");
	}		

	public void inputTextByTitle(String buttonID, String textInput, boolean isExactMatchNeeded) {

		String xpath_string = "";

		if (isExactMatchNeeded) {
			xpath_string = "//input[@title='" + buttonID + "']";
		} else {
			xpath_string = "//input[contains(@title,'" + buttonID + "')]";
		}

		By locator = By.xpath(xpath_string);

		WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		WebElement inputBox = this.getWebDriver().findElement(locator);
		inputBox.click();
		inputBox.sendKeys(textInput);
	}
	
	public void openURLInWebDriver(String url) {
		webDriverHelper.openURL(url);
	}
	
	public WebElement inputTextById(String idString, String textInput,
			boolean isExactMatchNeeded) {

		String xpath_string = "";

		if (isExactMatchNeeded) {
			xpath_string = "//input[@id='" + idString + "']";
		} else {
			xpath_string = "//input[contains(@id,'" + idString + "')]";
		}

		By locator = By.xpath(xpath_string);

		WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		WebDriverWait wait2 = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		WebElement inputBox = wait2.until(ExpectedConditions.elementToBeClickable(locator));

		inputBox.sendKeys(textInput);

		return inputBox;
	}

	public WebElement inputTextByXpath(String xpath_string, String textInput) {

		By locator = By.xpath(xpath_string);

		WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		WebElement inputBox = this.getWebDriver().findElement(locator);
		inputBox.click();
		inputBox.sendKeys(textInput);

		return inputBox;
	}

	public void clickOnElementByXpath(String element_xpath) {

		By locator = By.xpath(element_xpath);

		WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		WebDriverWait wait2 = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		WebElement element = wait2.until(ExpectedConditions.elementToBeClickable(locator));

		element.click();
	}
	
	public boolean checkForElementByXpath(String element_xpath) {

		By locator = By.xpath(element_xpath);

		try {
			WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (Exception ex) {
			logger.debug("Did not find element that was expected: " + element_xpath);
		}
		
		boolean isElementFound = this.getWebDriver().findElement(By.xpath(element_xpath)).isDisplayed();
		
		return isElementFound;
	}	

	public void clickALinkByText(String elementText, boolean isExactMatchNeeded) {

		String xpath_string = "";

		if (isExactMatchNeeded) {
			xpath_string = "//a[.='" + elementText + "']";
		} else {
			xpath_string = "//a[contains(.,'" + elementText + "')]";
		}

		By locator = By.xpath(xpath_string);

		JSWaiter.waitUntilJSReady();
		JSWaiter.waitForJQueryLoad();

		WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);

		wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		WebDriverWait wait2 = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		WebElement element = wait2.until(ExpectedConditions.elementToBeClickable(locator));
		element.click();

	}

	public void clickSpanByText(String elementText, boolean isExactMatchNeeded) {

		String xpath_string = "";

		if (isExactMatchNeeded) {
			xpath_string = "//span[.='" + elementText + "']";
		} else {
			xpath_string = "//span[contains(.,'" + elementText + "')]";
		}

		By locator = By.xpath(xpath_string);

		JSWaiter.waitUntilJSReady();
		JSWaiter.waitForJQueryLoad();

		WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		WebDriverWait wait2 = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		WebElement element = wait2.until(ExpectedConditions.elementToBeClickable(locator));
		element.click();

	}

	public void clickLiByText(String elementText, boolean isExactMatchNeeded) {

		String xpath_string = "";

		if (isExactMatchNeeded) {
			xpath_string = "//li[.='" + elementText + "']";
		} else {
			xpath_string = "//li[contains(.,'" + elementText + "')]";
		}

		By locator = By.xpath(xpath_string);

		JSWaiter.waitUntilJSReady();
		JSWaiter.waitForJQueryLoad();

		WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		WebDriverWait wait2 = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		WebElement element = wait2.until(ExpectedConditions.elementToBeClickable(locator));
		element.click();

	}

	public WebElement scrollToElementByXpath(String xpath_string) {

		By locator = By.xpath(xpath_string);

		JSWaiter.waitUntilJSReady();
		JSWaiter.waitForJQueryLoad();

		WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		((JavascriptExecutor) this.getWebDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
		
		JSWaiter.waitUntilJSReady();
		JSWaiter.waitForJQueryLoad();

		return element;
	}

	public boolean waitForText(String elementText) {

		String xpath_string = "//*[contains(.,'" + elementText + "')]";

		//logger.debug("Waiting for: " + elementText);

		return waitForXpath(xpath_string, this.defaultWaitForText);
	}

	public boolean waitForText(String elementText, int timeOutS) {

		String xpath_string = "//*[contains(.,'" + elementText + "')]";

		//logger.debug("Waiting for: " + elementText);

		return waitForXpath(xpath_string, timeOutS);
	}

	public boolean waitForXpath(String xpath_string, int timeOutS) {

		boolean isTextFound = false;

		try {

			logger.debug("Waiting for: " + xpath_string);

			By locator = By.xpath(xpath_string);

			//JSWaiter.waitUntilJSReady();
			//JSWaiter.waitForJQueryLoad();

			//logger.debug("Waiting time: " + timeOutS);

			WebDriverWait wait = new WebDriverWait(this.getWebDriver(), timeOutS); 
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));

			isTextFound = true;

		} catch (org.openqa.selenium.TimeoutException ex) {
			isTextFound = false;
			logger.debug("Timeout waiting for: " + xpath_string);
		}

		return isTextFound;
	}

	public boolean waitUntilTextVisible(String elementText) {

		String xpath_string = "//*[contains(.,'" + elementText + "')]";

		return waitUntilXpathVisible(xpath_string);
	}

	public boolean waitUntilXpathVisible(String xpath_string) {

		boolean isTextFound = false;

		try {

			By locator = By.xpath(xpath_string);

			JSWaiter.waitUntilJSReady();
			//JSWaiter.waitForJQueryLoad();

			WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

			isTextFound = true;

		} catch (org.openqa.selenium.TimeoutException ex) {
			isTextFound = false;
			logger.debug("waitUntilXpathVisible: TIMEOUT");
		}

		return isTextFound;
	}	
	
	public void clickButton(String elementText, String elementType, boolean isExactMatchNeeded) {

		String xpath_string = "";
		
		switch(elementType) {
		case "buttonText":
			if (isExactMatchNeeded) {
				xpath_string = "//button[.='" + elementText + "']";
			} else {
				xpath_string = "//button[contains(.,'" + elementText + "')]";
			}
			break;
		case "id":
			if (isExactMatchNeeded) {
				xpath_string = "//input[@id='" + elementText + "']";
			} else {
				xpath_string = "//input[contains(@id,'" + elementText + "')]";
			}
			break;
		case "name":
			if (isExactMatchNeeded) {
				xpath_string = "//input[@name='" + elementText + "']";
			} else {
				xpath_string = "//input[contains(@name,'" + elementText + "')]";
			}	
			break;
		case "anyElement":
			if (isExactMatchNeeded) {
				xpath_string = "//*[.='" + elementText + "']";
			} else {
				xpath_string = "//*[contains(.,'" + elementText + "')]";
			}
			break;
		default: 
			break;
		}

		By locator = By.xpath(xpath_string);

		JSWaiter.waitUntilJSReady();
		JSWaiter.waitForJQueryLoad();

		WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);

		wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		WebDriverWait wait2 = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		WebElement element = wait2.until(ExpectedConditions.elementToBeClickable(locator));
		element.click();

	}	
	
	public boolean checkIfExists(String elementText, String elementType, Boolean isExactMatchNeeded) {
		String xpath_string;
		
		switch(elementType) {
		case "h3":
			if (isExactMatchNeeded) {
				xpath_string = "//h3[.='" + elementText + "']";
			} else {
				xpath_string = "//h3[contains(.,'" + elementText + "')]";
			}
			break;		
		case "text":
			if (isExactMatchNeeded) {
				xpath_string = "//*[.='" + elementText + "']";
			} else {
				xpath_string = "//*[contains(.,'" + elementText + "')]";
			}
			break;
		case "id":
			if (isExactMatchNeeded) {
				xpath_string = "//*[@id='" + elementText + "']";
			} else {
				xpath_string = "//*[contains(@id,'" + elementText + "')]";
			}
			break;
		case "name":
			if (isExactMatchNeeded) {
				xpath_string = "//*[@name='" + elementText + "']";
			} else {
				xpath_string = "//*[contains(@name,'" + elementText + "')]";
			}	
			break;
		default: 
			if (isExactMatchNeeded) {
				xpath_string = "//*[.='" + elementText + "']";
			} else {
				xpath_string = "//*[contains(.,'" + elementText + "')]";
			}			
			break;
		}		
		
		boolean isExists = false;
		try {
			isExists = this.getWebDriver().findElement(By.xpath(xpath_string)).isDisplayed();
		} catch (Exception ex) {
			logger.debug("Could not find element by xpath:" + xpath_string);
		}
		return isExists;
	}
	
	public void click(String elementType, String variableType, String searchText, boolean isExactMatchNeeded) {

		String xpath_string = "";
		
		switch(variableType) {
		case "id":
			if (isExactMatchNeeded) {
				xpath_string = "//"+elementType+"[@id='" + searchText + "']";
			} else {
				xpath_string = "//"+elementType+"[contains(@id,'" + searchText + "')]";
			}
			break;
		case "name":
			if (isExactMatchNeeded) {
				xpath_string = "//"+elementType+"[@name='" + searchText + "']";
			} else {
				xpath_string = "//"+elementType+"[contains(@name,'" + searchText + "')]";
			}	
			break;
		case "text":
			if (isExactMatchNeeded) {
				xpath_string = "//"+elementType+"[.='" + searchText + "']";
			} else {
				xpath_string = "//"+elementType+"[contains(.,'" + searchText + "')]";
			}
			break;
		default: 
			break;
		}

		By locator = By.xpath(xpath_string);

		JSWaiter.waitUntilJSReady();
		JSWaiter.waitForJQueryLoad();

		WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);

		wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		WebDriverWait wait2 = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		WebElement element = wait2.until(ExpectedConditions.elementToBeClickable(locator));
		element.click();

	}		
	
	public boolean waitVisible(String elementType, String variableType, String searchText, boolean isExactMatchNeeded) {

		String xpath_string = "";
		boolean isFound = false;

		try {

			switch(variableType) {
			case "id":
				if (isExactMatchNeeded) {
					xpath_string = "//"+elementType+"[@id='" + searchText + "']";
				} else {
					xpath_string = "//"+elementType+"[contains(@id,'" + searchText + "')]";
				}
				break;
			case "name":
				if (isExactMatchNeeded) {
					xpath_string = "//"+elementType+"[@name='" + searchText + "']";
				} else {
					xpath_string = "//"+elementType+"[contains(@name,'" + searchText + "')]";
				}	
				break;
			case "text":
				if (isExactMatchNeeded) {
					xpath_string = "//"+elementType+"[.='" + searchText + "']";
				} else {
					xpath_string = "//"+elementType+"[contains(.,'" + searchText + "')]";
				}
				break;
			default: 
				break;
			}

			By locator = By.xpath(xpath_string);

			JSWaiter.waitUntilJSReady();
			JSWaiter.waitForJQueryLoad();

			WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);

			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

			isFound = true;

		} catch (Exception ex) {
			isFound = false;
			logger.debug("Timeout waiting for: " + xpath_string);
		}	
		
		return isFound;

	}	
	
	public void input(String elementType, String variableType, String searchText, String inputText, boolean isExactMatchNeeded) {

		String xpath_string = "";
		
		switch(variableType) {
		case "id":
			if (isExactMatchNeeded) {
				xpath_string = "//"+elementType+"[@id='" + searchText + "']";
			} else {
				xpath_string = "//"+elementType+"[contains(@id,'" + searchText + "')]";
			}
			break;
		case "name":
			if (isExactMatchNeeded) {
				xpath_string = "//"+elementType+"[@name='" + searchText + "']";
			} else {
				xpath_string = "//"+elementType+"[contains(@name,'" + searchText + "')]";
			}	
			break;
		case "text":
			if (isExactMatchNeeded) {
				xpath_string = "//"+elementType+"[.='" + searchText + "']";
			} else {
				xpath_string = "//"+elementType+"[contains(.,'" + searchText + "')]";
			}
			break;
		default: 
			break;
		}

		By locator = By.xpath(xpath_string);

		JSWaiter.waitUntilJSReady();
		JSWaiter.waitForJQueryLoad();

		WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		WebDriverWait wait2 = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		WebElement element = wait2.until(ExpectedConditions.elementToBeClickable(locator));
		element.click();
		element.sendKeys(inputText);		

	}
	
	public WebElement sendKeyByXpath(String xpath_string, CharSequence key) {

		By locator = By.xpath(xpath_string);

		WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		WebElement element = this.getWebDriver().findElement(locator);
		element.sendKeys(key);

		return element;
	}
	
	public WebElement sendKeyByCssSelector(String cssSelector, CharSequence key) {

		By locator = By.cssSelector(cssSelector);

		WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		WebElement element = this.getWebDriver().findElement(locator);
		element.sendKeys(key);

		return element;
	}	
	
	public boolean checkIfPageResultVisible(SoftAssert softAssert, String textToWaitFor) {
		boolean isPageResultReady = false;
		try {
			isPageResultReady = this.waitVisible("*", "text", textToWaitFor, false);		
		}  catch (Exception ex) {
			logger.debug("Exception checking for screen result: " + ex.getMessage());
			isPageResultReady = false;
		}
		logger.debug("isPageResultReady: " + isPageResultReady);
		softAssert.assertTrue(isPageResultReady, "Screen result not as expected. ");
		
		return isPageResultReady;
	}

//	Todo: Make all methods more generic with enumerator for cssSelector vs xpath
//	By locator;
//	switch(selectApproach) {
//	case "xpath":
//		locator = By.xpath(xpath_string);
//		break;
//	case "cssSelector":
//		locator = By.cssSelector(xpath_string);
//		break;
//	}	
	public void clickOnElementByCssSelector(String element_cssSelector) {

		By locator = By.cssSelector(element_cssSelector);

		WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		WebDriverWait wait2 = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		WebElement element = wait2.until(ExpectedConditions.elementToBeClickable(locator));
		
		element.click();
	}
	
	public void rightClickOnElementByCssSelector(String element_cssSelector) {

		By locator = By.cssSelector(element_cssSelector);

		WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		WebDriverWait wait2 = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		WebElement element = wait2.until(ExpectedConditions.elementToBeClickable(locator));
	
		Actions actions = new Actions(this.getWebDriver());
		actions.contextClick(element).perform();
	}	
	
	
	public void rightClick(String elementType, String variableType, String searchText, boolean isExactMatchNeeded) {

		String xpath_string = "";
		
		switch(variableType) {
		case "id":
			if (isExactMatchNeeded) {
				xpath_string = "//"+elementType+"[@id='" + searchText + "']";
			} else {
				xpath_string = "//"+elementType+"[contains(@id,'" + searchText + "')]";
			}
			break;
		case "name":
			if (isExactMatchNeeded) {
				xpath_string = "//"+elementType+"[@name='" + searchText + "']";
			} else {
				xpath_string = "//"+elementType+"[contains(@name,'" + searchText + "')]";
			}	
			break;
		case "text":
			if (isExactMatchNeeded) {
				xpath_string = "//"+elementType+"[.='" + searchText + "']";
			} else {
				xpath_string = "//"+elementType+"[contains(.,'" + searchText + "')]";
			}
			break;
		default: 
			break;
		}

		By locator = By.xpath(xpath_string);

		JSWaiter.waitUntilJSReady();
		JSWaiter.waitForJQueryLoad();

		WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);

		wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		WebDriverWait wait2 = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		WebElement element = wait2.until(ExpectedConditions.elementToBeClickable(locator));
		
		Actions actions = new Actions(this.getWebDriver());
		actions.contextClick(element).perform();

	}
	
	public WebElement inputTextByCssSelector(String cssSelector, String textInput) {

		By locator = By.cssSelector(cssSelector);

		WebDriverWait wait = new WebDriverWait(this.getWebDriver(), this.webDriverWaitSeconds);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));

		WebElement inputBox = this.getWebDriver().findElement(locator);
		inputBox.click();
		inputBox.sendKeys(textInput);

		return inputBox;
	}	
	
	
}
