package core;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
//import org.testng.asserts.SoftAssert;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.Dimension;

import portalcheck.Trello_Flow01;

public class WebDriverHelper {
	
	final static Logger logger = Logger.getLogger(Trello_Flow01.class);
	
	// Setup global variables:
	private String osName = "";
	private String driverName = "";
	private String browserDriverPath = "";
	private String browserBinPath = "";
	private String browserLogfilePath = "";
	private boolean isHeadless = false;
	
	private WebDriver driver;
	
	public WebDriver getDriver() {
		return driver;
	}
	
	public WebDriverHelper() {
		logger.debug("SeleniumHelper() - START");
		
		try {
		
			// Read and setup settings:
			this.readWebDriverPropertiesFromFile();
	
			// Use defaults since no command line:
			this.setDefaults();
		
		} catch (Exception ex) {
			logger.error("Problem setting up SeleniumHelper: " + ex.getMessage());
		}
		
		logger.debug("SeleniumHelper() - END");
	}
	

	private void readWebDriverPropertiesFromFile() {
		logger.debug("readSeleniumPropertiesFromFile() - START");
		System.out.println("readSeleniumPropertiesFromFile() - START");

		try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {

			Properties prop = new Properties();

			// load a properties file
			prop.load(input);

			// get the property values:			
			driverName =		 	prop.getProperty("driverName");
			browserDriverPath = 	prop.getProperty("browserDriverPath");
			browserBinPath = 		prop.getProperty("browserBinPath");
			browserLogfilePath = 	prop.getProperty("browserLogfilePath");
			isHeadless = 	Boolean.parseBoolean(prop.getProperty("isHeadless"));

		} catch (IOException ex) {
			System.out.println("ERRROR - Problem parsing properties file. Please fix field formats: " + ex.getMessage());
			System.exit(0);
		}
		logger.debug("readSeleniumPropertiesFromFile() - END");
	}	
	
	private void setDefaults() {
		logger.debug("setDefaults() - START");

		// Check if the OS is windows or macos:
		String OS = System.getProperty("os.name").toLowerCase();
		boolean isWindows = OS.indexOf("win") >= 0;
		boolean isMac = OS.indexOf("mac") >= 0;
		boolean isUnix = (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
		if (isWindows) {
			logger.debug("Detected Windows OS.");
			osName = "windows";
		} else if (isUnix) {
			logger.debug("Detected Unix OS.");
			osName = "unix";
			browserDriverPath = "/usr/bin/geckodriver";
			browserBinPath = "/usr/bin/firefox";
			browserLogfilePath = "/dev/null";	
		} else if (isMac) {
			logger.debug("Detected Mac OS.");
			osName = "macos";
		} else {
			logger.debug("Could not detect OS.");
		}
	
		logger.debug("osName: " + osName);
		logger.debug("driverName: " + driverName);
		logger.debug("browserDriverPath: " + browserDriverPath);
		logger.debug("browserBinPath: " + browserBinPath);

		logger.debug("setDefaults() - END");
	}	
	
	private WebDriver startChrome() {
		WebDriver newWebDriver;
		System.setProperty("webdriver.chrome.driver", browserDriverPath);

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--no-sandbox"); // Bypass OS security model

		if (isHeadless)
			options.addArguments("--headless"); // Bypass OS security model

		options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems

		options.setExperimentalOption("useAutomationExtension", false);
		options.addArguments("start-maximized"); // open Browser in maximized mode
		options.addArguments("disable-infobars"); // disabling infobars
		options.addArguments("--disable-extensions"); // disabling extensions
		options.addArguments("--disable-gpu"); // applicable to windows os only
		options.setExperimentalOption("useAutomationExtension", false);
		options.setBinary(browserDriverPath);

		newWebDriver = new ChromeDriver(options);

		return newWebDriver;
	}

	private WebDriver startFirefox() {
		WebDriver newWebDriver;

		System.setProperty("webdriver.gecko.driver", browserDriverPath);
		System.setProperty("webdriver.firefox.bin", browserBinPath);

		System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, browserLogfilePath);

		FirefoxOptions options = new FirefoxOptions();

		if (isHeadless)
			options.setHeadless(true);

		newWebDriver = new FirefoxDriver(options);

		// Resize current window to the set dimension
		Dimension d = new Dimension(1400, 850);
		newWebDriver.manage().window().setSize(d);

		return newWebDriver;
	}


	public WebDriver startBrowser() {
		logger.debug("startBrowserInWebDriver() - START");

		try {

			// Setup the Selenium WebDriver that will connect to the browser (for now
			// default to Firefox):
			logger.debug("Creating web driver...");
			if (driverName.contentEquals("chrome")) {

				driver = startChrome();

			} else { // if (driverName.contentEquals("firefox")

				driver = startFirefox();

			}

			// Connect our javascript code to the WebDriver:
			JSWaiter.setDriver(driver);

		} catch (Exception ex) {
			logger.debug("Could not open browser: " + ex.getMessage());
			ex.printStackTrace();
		}
		
		logger.debug("startBrowserInWebDriver() - END");
		return driver;
	}		
	
	public void openURL(String url_String) {

		// Open the VPOS URL:
		logger.debug("Trying to open URL: " + url_String);
		driver.get(url_String);
	}		

}
