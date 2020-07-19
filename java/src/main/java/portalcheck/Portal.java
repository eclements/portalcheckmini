package portalcheck;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Portal {
	private String portalUsername = "";
	private String portalPassword = "";
	private String portalURL = ""; 
	
	final static Logger logger = Logger.getLogger(Trello_Flow01.class);

	public Portal () {
		logger.debug("PortalHelper() - START");

		System.out.println("Reading portal login details from properties file...");

		readPropertiesFromFile();
		
		setDefaults();
		
		logger.debug("PortalHelper() - END");
	}
	
	public String getPortalUsername() {
		return portalUsername;
	}
	
	public String getPortalPassword() {
		return portalPassword;
	}
	
	public String getPortalURL() {
		return portalURL;
	}	
	
	private void readPropertiesFromFile() {
		logger.debug("readPropertiesFromFile() - START");

		try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {

			Properties prop = new Properties();

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			portalURL = prop.getProperty("portalURL");
			portalUsername = prop.getProperty("portalUsername");
			portalPassword = prop.getProperty("portalPassword");

		} catch (IOException ex) {
			System.out
			.println("ERRROR - Problem parsing properties file. Please fix field formats: " + ex.getMessage());
			System.exit(0);
		}
		logger.debug("readPropertiesFromFile() - END");
	}	

	private void setDefaults() {
		logger.debug("setDefaults() - START");
		
		if (portalUsername.isEmpty()) portalUsername = "AlertMonitor";
		logger.debug("portalUsername: " + portalUsername);
		
		logger.debug("setDefaults() - END");
	}
	
}
