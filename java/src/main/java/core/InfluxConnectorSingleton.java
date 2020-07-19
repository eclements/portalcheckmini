package core;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

class InfluxConnectorSingleton {

	// static variable single_instance of type Singleton 
	private static InfluxConnectorSingleton theInstance = null; 

	private InfluxDB theInfluxDB; 
	private String influxDbHostURL;
	private String influxDbUsername;
	private String influxDbPassword;
	private String influxDbName;	

	private boolean isIgnoreInfluxDBWrite = false;
	private boolean isSendPointsToInfluxDB = true;	

	private InfluxConnectorSingleton () {

		System.out.println("Reading InfluxDB connection details from properties file...");

		try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {

			Properties prop = new Properties();

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			influxDbHostURL = prop.getProperty("influxDbHostURL");
			influxDbUsername = prop.getProperty("influxDbUsername");
			influxDbPassword = prop.getProperty("influxDbPassword");
			influxDbName = prop.getProperty("influxDbName");
			isSendPointsToInfluxDB = Boolean.parseBoolean(prop.getProperty("isSendPointsToInfluxDB"));

			System.out.println("influxDbHostURL: " + influxDbHostURL);
			System.out.println("influxDbUsername: " + influxDbUsername);
			System.out.println("influxDbName: " + influxDbName);
			System.out.println("isSendPointsToInfluxDB: " + isSendPointsToInfluxDB);			


			input.close();

		} catch (Exception ex) {
			System.out
			.println("ERRROR - Problem parsing InfluxDB connection properties file. Please fix field formats: " + ex.getMessage());
			this.isIgnoreInfluxDBWrite = true;
		}

		if (isSendPointsToInfluxDB) {
			System.out.println("Setting up InfluxDB connection...");
			try {
				theInfluxDB = InfluxDBFactory.connect(influxDbHostURL, influxDbUsername, influxDbPassword);
				theInfluxDB.setDatabase(influxDbName);
			} catch (Exception ex) {
				System.out.println("ERRROR - Problem Setting up InfluxDB connector: " + ex.getMessage());
				System.out.println("WARNING: Ignoring writes to influxdb.");
				this.isIgnoreInfluxDBWrite = true;
			}
		} else {
			System.out.println("InfluxDB writing is turned off in config file...");
		}
	}

	public static InfluxConnectorSingleton getInstance() 
	{ 
		if (theInstance == null) 
			theInstance = new InfluxConnectorSingleton(); 

		return theInstance; 
	}

	public void postInfluxDataPoint(final Point point) {
		try {
			//If we are connected and our config file says we should write to InfluxDB:
			if (!this.isIgnoreInfluxDBWrite && isSendPointsToInfluxDB) { 
				theInfluxDB.write(point);
			}
		} catch (Exception ex) {
			System.out.println("WARNING: Problem writing data point to influxDB: " + ex.getMessage());
		}
	}

}