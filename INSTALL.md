# Install Guide
## Geting Started
To use this project, you will need to go through 6x installation steps:
1. **Trello Setup (Optional: for the Example)** - Create a Trello board, so that the example Robot Flow can interact with the board.
2. **Server Side** - Install InfluxDB and Grafana on your server
3. **Client Side (Browser WebDriver)** - Setup of your browser binary and browser driver
4. **Client Side (Java App)** - Configuring and compiling the java robot that will run the flow.
5. **Grafana Setup** - Importing of the PortalCheckMini Grafana dashboard template that will listen for results from the robot via InfluxDB.
6. **Scheduling** - Use Jenkins or Crontab to schedule the robot to run every 10mins.

### Trello Setup (for the Example)
If you plan to run the Java Robot with the example flow. You will need to have a Trello test account. I suggest you create a new Trello account for this purpose. Then setup as follows:
1. Create a board called "portalcheckmini"
2. Create the following lists: "Todo", "Doing", "Done" - Ensure the names are spelt exactly right.
3. Mark this board as a favourite to ensure it shows at the top of your home screen otherwise the Robot will not find it.

### Server Side
1. Install InfluxDB - See Andre Miller's [install guide](http://www.andremiller.net/content/grafana-and-influxdb-quickstart-on-ubuntu)
2. Install Grafana  
3. Create the InfluxDB database for PortalCheckMini:

        CREATE DATABASE portalcheckmini_monitoring_results

4. Create the user needed for the java app to connect to InfluxDB:

        CREATE USER java_client WITH PASSWORD 'yourowngoodpassword'
        GRANT WRITE ON portalcheckmini_monitoring_results TO java_client

4. Create the user needed for Grafana to connect to InfluxDB:

        CREATE USER grafana_client WITH PASSWORD 'yourowngoodpassword'
        GRANT READ ON portalcheckmini_monitoring_results TO grafana_client

###  Client Side (Browser & WebDriver)
Install the following dependancies:
1. **Selenium Firefox (Gecko) WebDriver** - [Download ](https://github.com/mozilla/geckodriver/releases) the appropriate WebDriver for your environment. For Ubuntu 18.04 make sure to download the previous version of Gecko WebDriver from [GITHUB](https://github.com/mozilla/geckodriver/releases/tag/v0.25.0) v0.25.0 tag.

        wget https://github.com/mozilla/geckodriver/releases/download/v0.25.0/geckodriver-v0.25.0-linux64.tar.gz
        tar -zxvf geckodriver-v0.25.0-linux64.tar.gz
        cp geckodriver /usr/bin/

2. **Firefox Binary**
        1. You will need a [firefox binary](https://www.mozilla.org/en-US/firefox/new/) to run PortalCheckMini.
        2. Set the path to your firefox binary in config.properties file.

        sudo apt install firefox
        sudo apt install maven

### Client Side (Java App)
To compile and run the java program:
1. **Create a folder** for the project: 

        mkdir /opt/portalcheckmini
        cd /opt/portalcheckmini

1. **Clone** or download the PortalCheckMini repo to get the source code

        git clone https://github.com/eclements/portalcheckmini.git /opt/portalcheckmini

4. Use the example config file to **create your own config file**:

        cd ./portalcheckmini/java/src/main/resources/
        cp config.properties.example config.properties

5. **Edit the config.properties file** to set your own values. 

    vi config.properties

6. Set your **browser binary and driver paths**:

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

7. Decide if you will run with browser GUI or without (**headless**):

        isHeadless=false

8. Define your Trello **login details** (to use the out of box Trello steps example)

        portalURL=https://www.trello.com
        portalUsername=youremail@gmail.com
        portalPassword=YOUR_TRELLO_PASSWORD

9. Set the **InfluxDB Host connection details**:

        influxDbHostURL=http://hostname.domain.com:8086
        influxDbUsername=java_client
        influxDbPassword=JAVACLIENT_PASSWORD 
        influxDbName=portalcheckmini_monitoring_results

10. Compile the code

        cd ./portalcheckmini/java
        mvn compile

    You should see "BUILD SUCCESS"
10. Run the programme:

        mvn test

11. Watch the robot run. You should see the following **output on your console**. You should also see a **Firefox window opening** and going to the Trello website.

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
### Grafana
1. Login to your Grafana instance.
2. **Create a new datasource** called **portalcheckmini_monitoring_results**. 
2.1 Select "InfluxDB" type. 
2.2 Configure it with "Basic AUthentication" to point to your InfluxDB instance created in "Server Side" above. 
3. **Create a new Grafana dashboard** by **importing** the PortalCheckMini template (click + icon and then import)
3.1 Use the [template provided](https://github.com/eclements/portalcheckmini/blob/master/grafana/Synthetic%20Dashboard%20for%20Java%20Selenium%20Robot%20-%20PortalCheckMini.json) in the /grafana folder in this repo.
4. Set the dashboard **datasource** to **portalcheckmini_monitoring_results**, the datasource you just created.
5. You should see your Robot Dashboard in Grafana.

### Scheduling
1. Schedule your Java app to run every 10 minutes. 
You can use Jenkins but for simplicity I use crontab:

        0,10,20,30,40,50 4-19 * * 0-5   ubuntu     /usr/bin/flock -n /tmp/portalcheckmini.lockfile /usr/local/bin/runportalcheckmini.sh >> /var/log/portalcheckmini/test.log 2>&1

2. Create a bash script for the cron job. Create this script at     /usr/local/bin/runportalcheckmini.sh:

        #!/bin/bash
        cd /var/log/portalcheckmini
        mvn -f /opt/portalcheckmini/java/pom.xml test

That should be it. Your robot should be up and running every 10minutes and feeding data to InfluxDB.

