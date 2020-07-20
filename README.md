# PortalCheckMini
### A Synthetic Java Robot for end-to-end monitoring of Web Portals in a Grafana Dashboard with InfluxDB, TestNG and Selenium.
Latest stable version: 0.0.17
## What is it?
PortalCheckMini is a very simple Java program that aims to be the foundation for mini Java robots that execute a flows of steps against a web portal. The Java program will run 10x steps in a flow (or more), and store all the step results in a time series database. The PASS/FAIL results (and time taken to execute) is then shown in a Grafana dashboard. The project is written as an abstract structure within which new flows can quickly be coded for any web portal.

This project is specifically written so that any person with basic Java skills can create a new end to end monitoring robot in under 2 hours for their web portal, together with a sexy Grafana dashboard as in this example.


## What is included?
* **The Java project** that comes with **10x** (doStep01, doStep02, etc) methods pre-coded for you to start with. Just add a few seleniumHelper.click(), or seleniumHelper.input() calls to each step to create your own flow.
* **Maven build config** already setup in pom.xml simply do a mvn compile to get all dependancies downloaded and compiled.
* **A SeleniumHelper Class** with multiple .click(), .input() methods that hide the complexity of Selenium interations, xPath strings and wait methods in a blackbox.  
* **TestNG configured** with a test suite, test set and test methods. These methods are in turn already configured as input metrics in the template Grafana dashboard.
* **A Grafana template** that is already hooked up with grafana queries to read the doStep01, doStep02, etc method PASS/FAIL results from your flow. No headaches on setting up Grafana queries or how to link Java/Grafana. That is all taken care of.

## Example: End-To-End Monitoring of Trello.com 
To showcase the robot the project uses "Trello.com" as an example case study. All the robot code in this Master branch (specifically the Trello_flow01.java file, and config.properties example) is setup to perform 7x steps on the Trello website. The robot will login to Trello, select a board, create a card, edit the card, move the card and archive the card. This is the 7x steps that make up the Syntehic test "Trello_Flow01" as shown in the example Grafana dashboard below.

![alt text](https://github.com/eclements/portalcheckmini/blob/master/portalcheckmini_grafana_dashboard.png?raw=true)
## Why?
Reduce the time it takes to create your own Selenium Java robot to monitor your web portal.  Use PortalCheckMini as a **template** to quickly create steps that click automatically through your web portal, including login and logout boilerplates. Don't waste time to configure Grafana dashboards queries, use the template and build your customisations on top. 

## How?
To use this project simply follow the "Getting Started" steps. Once up and running:
1. **Change the portal login details** to your site
2. **Edit each of the methods "doStep01", "doStep02", "doStep03"** to perform the main actions on your site.
3. The Grafana dashboard will automatically read the results from these steps and show in your dashboard.
4. Note that your **class name is important**, if you change "Trello_Flow01" to your own name, the dashboard will have to be updated to point to that new Metric key name. TestNG sends the class name and method name as KEYS to InfluxDB/Grafana.
## Dependancies
### Maven Dependancies
The programme relies on the following dependancies that will automatically be downloaded and compiled with Maven: (see pom.xml for details)
* Selenium WebDriver - for web page interactions (example case is based on the Firefox WebDriver / geckodriver)
* Maven testNG Plugin - definition of test classes and methods. To generate the PASS/FAIL asserts.
* InfluxDB Java Client - time series database for PASS/FAIL data point metrics.
* Log4J - for logging
* [JSWaiter](https://www.swtestacademy.com/selenium-wait-javascript-angular-ajax/) - by By Onur Baskirt. For Javascript and Jquery waits in Selenium (included as a class)
### Manual Dependancies
Please ensure you have the following dependancies installed/downloaded on your machine:
* [Selenium Firefox (Gecko) WebDriver](https://github.com/mozilla/geckodriver/releases) - Download the appropriate WebDriver for your environment. For Ubuntu 18.04 make sure to download the previous version of Gecko WebDriver v25 from https://github.com/mozilla/geckodriver/releases/tag/v0.25.0
```
wget https://github.com/mozilla/geckodriver/releases/download/v0.25.0/geckodriver-v0.25.0-linux64.tar.gz
tar -zxvf geckodriver-v0.25.0-linux64.tar.gz
cp geckodriver /usr/bin/
```
* [Firefox Binary](https://www.mozilla.org/en-US/firefox/new/) - You will need a firefox binary to run PortalCheckMini. Set the path to your binary in PortalCheckMini's config.properties file. For Ubuntu you can do:
```
sudo apt install firefox
```
* Maven
```
sudo apt install maven
```
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
mkdir /opt/portalcheckmini
cd /opt/portalcheckmini
git clone https://github.com/eclements/portalcheckmini.git /opt/portalcheckmini
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
0,10,20,30,40,50 4-19 * * 0-5   ubuntu     /usr/bin/flock -n /tmp/portalcheckmini.lockfile /usr/local/bin/runportalcheckmini.sh >> /var/log/portalcheckmini/test.log 2>&1
```
Example bash script for cron job. Create this script at /usr/local/bin/runportalcheckmini.sh:
```
#!/bin/bash
cd /var/log/portalcheckmini
mvn -f /opt/portalcheckmini/java/pom.xml test
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
