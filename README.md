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
## Why?
Reduce the time it takes to create your own Selenium Java robot to monitor your web portal.  Use PortalCheckMini as a **template** to quickly create steps that click automatically through your web portal, including login and logout boilerplates. Don't waste time to configure Grafana dashboards queries, use the template and build your customisations on top. 
## Example: End-To-End Monitoring of Trello.com 
To showcase the robot the project uses "Trello.com" as an example case study. All the robot code in this Master branch (specifically the Trello_flow01.java file, and config.properties example) is setup to perform 7x steps on the Trello website. The robot will login to Trello, select a board, create a card, edit the card, move the card and archive the card. This is the 7x steps that make up the Syntehic test "Trello_Flow01" as shown in the example Grafana dashboard below.

![alt text](https://github.com/eclements/portalcheckmini/blob/master/portalcheckmini_grafana_dashboard.png?raw=true)


## How?
To use this project you simply need: 
1. Have a server ready (I recommend an Ubuntu 18.04 instance on AWS EC2)
2. Follow the [Install steps](https://github.com/eclements/portalcheckmini/blob/master/INSTALL.md).
1. **Change the portal login details** in config.properties to your site (use config.properties.example as a base)
2. **Adapt each of the DO methods** (doStep01", "doStep02", "doStep03") to perform the clicks you want on your web portal.
3. The Grafana dashboard will automatically read the results from these steps and show in your dashboard if you use the provided template.

*Note: that your **class name is important**, if you change "Trello_Flow01.java" to your own name, the dashboard will have to be updated to point to that new Metric key name. TestNG sends the class name and method name as TAGS to InfluxDB/Grafana. An improvement is planned to make the Class selectable in a Grafana variable at the top of the dashboard*
## Dependancies
### Maven Dependancies
The programme relies on the following dependancies that will automatically be downloaded and compiled with Maven: (see pom.xml for details)
* Selenium WebDriver - for web page interactions (example case is based on the Firefox WebDriver / geckodriver)
* Maven testNG Plugin - definition of test classes and methods. To generate the PASS/FAIL asserts.
* InfluxDB Java Client - time series database for PASS/FAIL data point metrics.
* Log4J - for logging
* [JSWaiter](https://www.swtestacademy.com/selenium-wait-javascript-angular-ajax/) - by By Onur Baskirt. For Javascript and Jquery waits in Selenium (included as a class)

## Support
For support or questions contact [Eric Clements](https://www.linkedin.com/in/eric-clements-15591613)

## References
The following articles and examples were used to build PortalCheckMini. It is recommended to familiarise yourself with these articles to use PortalCheckMini:
* This work was inspired by the BLOG post [Make your Selenium Test Results Live using Grafana & InfluxDB](https://blog.testproject.io/2020/05/12/make-your-selenium-test-results-live-using-grafana-and-influxdb/) by Giridhar Rajkumar
* A good place to start with InfluxDB is [Getting started with InfluxBD](https://docs.influxdata.com/influxdb/v1.8/introduction/get-started/) on the Influx community site.
* See [TestNG for Eclipse](https://testng.org/doc/download.html) - If you plan to use Eclipse for your Java IDE.
* [EGIT](https://projects.eclipse.org/projects/technology.egit) - GIT Integration for Eclipse 

## Contributors 

Become one of the first contributes to this project. Send a pull request with your contribution. 
