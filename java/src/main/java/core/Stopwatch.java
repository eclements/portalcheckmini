package core;

import org.apache.log4j.Logger;

import portalcheck.Trello_Flow01;

public class Stopwatch {
	
	final static Logger logger = Logger.getLogger(Trello_Flow01.class);
	
	private long startTime;
	
	public Stopwatch () {
		startStopWatch();
	}
	
	public long startStopWatch() {
		this.startTime = System.currentTimeMillis(); // Start stopwatch
		return startTime;
	}
	
	public long getStopWatchNow() {
		long timeNow = System.currentTimeMillis(); // stop the stopwatch
		long timeTaken = (timeNow - this.startTime);
		return timeTaken;
	}	
	
	public void logTimeTaken() {
		long timeTakenMs = getStopWatchNow();		
		logger.debug("TIME TAKEN: \t" + timeTakenMs);	
	}
	

	

}
