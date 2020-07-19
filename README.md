# PortalCheckMini
##### A Synthetic Java Robot for end-to-end monitoring of Web Portals in a Grafana Dashboard with InfluxDB, TestNG and Selenium.
Latest stable version: 0.0.17
## What?
A simple Selenium Java programme that executes a set of steps against a web portal. Stores all step results (testNG) in a time series database (InfluxDB). Comes with an "out of box" Grafana Dashboard template.
## Why?
Reduce the time it takes to create your own Selenium Java robot to monitor your web portal. Use PortalCheckMini as a template to quickly create up to 10 steps or more clicking through your web portal, including login and logout.

While debugging an enterprise web platform I wanted to have a dashboard that could show me the time taken for each step in a long product application process. Grafana seemed a good choice for this however Grafana couldn't automatically step through our website and do the complex decision points in the flow. I built this mini Selenium robot as a base we could use to build more automated flows in Selenium quickly and show the results in Grafana.
## How?
To use this project to create your own Synthetic test, simply follow the "Getting Started" steps, and then once up and running:
1. Change the portal login details to your site
2. Edit each of the methods "doStep01", "doStep02", "doStep03" to perform the main actions on your site.
3. The Grafana dashboard will automatically read the results from these steps and show in your dashboard.
4. Note that your class name is important, if you change "Trello_Flow01" to your own name, the dashboard will have to be updated to point to that new Metric key name. TestNG sends the class name and method name as KEYS to InfluxDB/Grafana.
## Dependancies
### Maven Dependancies
The programme relies on the following dependancies that will automatically be downloaded and compiled with Maven: (see pom.xml for details)
* Selenium - for web page interactions
* Maven testNG Plugin - definition of test classes and methods. To generate the PASS/FAIL asserts.
* InfluxDB Java Client - time series database for PASS/FAIL data point metrics.
* Log4J - for logging
* [JSWaiter](https://www.swtestacademy.com/selenium-wait-javascript-angular-ajax/) - by By Onur Baskirt. For Javascript and Jquery waits in Selenium (included as a class)
### Manual Dependancies
Please ensure you have the following dependancies installed/downloaded on your machine:
* [Selenium Firefox (Gecko) WebDriver](https://github.com/mozilla/geckodriver/releases) - Download the appropriate WebDriver for your environment.
* [Firefox Binary](https://www.mozilla.org/en-US/firefox/new/) - You will need a firefox binary to run PortalCheckMini. Set the path to your binary in PortalCheckMini's config.properties file.
## Geting Started
To use this project, you will need to setup 4x different areas:
1. **Server Side** - InfluxDB and Grafana on your server
2. **Trello (Example Flow)** - Create a Trello board, so that the example Robot Flow can interact with the board.
3. **Client Side (Java App)** - This is the java robot that will run the flow.
4. **Grafana Setup** - Importing of the PortalCheckMini Grafana dashboard template
5. **Scheduling** - Use Jenkins or Crontab to schedule the robot to run every 10mins.
### Server Side
1. Install InfluxDB - See Andre Miller's [install guide](http://www.andremiller.net/content/grafana-and-influxdb-quickstart-on-ubuntu)
2. Install Grafana  
3. Create the InfluxDB database for PortalCheckMini:
```
CREATE DATABASE portalcheckmini_monitoring_results
```
4. Create the user needed for the java app to connect to InfluxDB:
```
CREATE USER java_client WITH PASSWORD 'yourowngoodpassword'
GRANT ALL ON portalcheckmini_monitoring_results TO java_client
```
### Trello (for the Example)
If you plan to run the Java Robot with the example flow. You will need to have a Trello test account and the following board create. I suggest you create a new Trello account for this purpose.
1. Create a board called "portalcheckmini"
2. Create the following lists: "Todo", "Doing", "Done" - Ensure the names are spelt exactly right.
3. Mark this board as a favourite to ensure it shows at the top of your home screen otherwise the Robot will not find it.
### Client Side (Java App)
Note: Can also be run on server in headless mode. Use "isHeadless" property in config.properties file.
1. **Download Firefox** if not already installed
2. **Download Selenium Firefox WebDriver** if not already downloaded. [Download the geckodriver](https://github.com/mozilla/geckodriver/releases)
3. **Clone** or download the PortalCheckMini repo to get the source code
```
git clone https://github.com/eclements/portalcheckmini.git
```
4. Use the example config file to **create your own config file**:
```
cd ./portalcheckmini/java/src/main/resources/
cp config.properties.example config.properties
```
5. **Edit the config.properties file** to set your own values. 
```
vi config.properties
```
6. Set your **browser binary and driver paths**:
```
#For macos use:
browserDriverPath=/Users/YOURUSERNAME/selenium/geckodriver
browserBinPath=/Applications/Firefox.app/Contents/MacOS/firefox
browserLogfilePath=/dev/null

#For windows use:
#browserDriverPath=C:\\YOURDOWNLOADPATH\\geckodriver_v24_win64.exe
#browserBinPath=C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe
#browserLogfilePath=C:\\portalcheckmini_browser.log 

#For Unix use:
#browserDriverPath = "/usr/bin/geckodriver_26_linux64";
#browserBinPath = "/usr/bin/firefox";
#browserLogfilePath = "/dev/null";
```
7. Decide if you will run with browser GUI or without (**headless**):
```
isHeadless=false
```
8. Define your Trello **login details** (to use the out of box Trello steps example)
```
portalURL=https://www.trello.com
portalUsername=youremail@gmail.com
portalPassword=YOUR_TRELLO_PASSWORD
```
9. Set the **InfluxDB Host connection details**:
```
influxDbHostURL=http://hostname.domain.com:8086
influxDbUsername=java_client
influxDbPassword=JAVACLIENT_PASSWORD 
influxDbName=portalcheckmini_monitoring_results
```
10. Compile the code
```
cd ./portalcheckmini/java
mvn compile
```
You should see "BUILD SUCCESS"
10. Run the programme:
```
mvn test
```
11. Watch the robot run. You should see the following **output on your console**. You should also see a **Firefox window opening** and going to the Trello website.
```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running TestSuite
2020-07-19 19:49:51 DEBUG Trello_Flow01 - PortalHelper() - START
...
TESTS START: 
...
(a total of 7 steps will run, you should see no FAIL or ERRORS)
...
TESTS COMPLETED. 
Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 35.065 sec - in TestSuite
```
12. Schedule your Java app to run every 10 minutes. You can use Jenkins but for simplicity I use crontab:
```
0,10,20,30,40,50 4-19 * * 0-5   ubuntu_root     /usr/bin/flock -n /tmp/portalcheckmini.lockfile /usr/local/bin/runportalcheckmini.sh >> /var/log/portalcheckmini/test.log 2>&1
```
Example bash script for cron job. Create this script at /usr/local/bin/runportalcheckmini.sh:
```
#!/bin/bash
cd /var/log/portalcheckmini
mvn -f /usr/local/portalcheckmini/java/pom.xml test
```

### Grafana
1. Login to your Grafana instance.
2. Create a new datasource called "portalcheckmini_monitoring_results". Select "InfluxDB" type. Configure it with "Basic AUthentication" to point to your InfluxDB instance created in "Server Side" above. 
3. Create a new Grafana dashboard by "importing" the PortalCheckMini template.
4. Set the dashboard datasource to "portalcheckmini_monitoring_results", the datasource you just created.
5. You should see your Robot Dashboard in Grafana.


## Support
Contact me via LinkedIn for support: [Eric Clements](https://www.linkedin.com/in/eric-clements-15591613)

## References
The following articles and examples were used to build PortalCheckMini. It is recommended to familiarise yourself with these articles to use PortalCheckMini:
* This work was inspired by the BLOG post [Make your Selenium Test Results Live using Grafana & InfluxDB](https://blog.testproject.io/2020/05/12/make-your-selenium-test-results-live-using-grafana-and-influxdb/) by Giridhar Rajkumar
* A good place to start with InfluxDB is [Getting started with InfluxBD](https://docs.influxdata.com/influxdb/v1.8/introduction/get-started/) on the Influx community site.
* See [TestNG for Eclipse](https://testng.org/doc/download.html) - If you plan to use Eclipse for your Java IDE.
* [EGIT](https://projects.eclipse.org/projects/technology.egit) - GIT Integration for Eclipse 

## Contributors 

Become one of the first contributes to this project. Send a pull request with your contribution. 
