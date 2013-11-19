package agent;

import java.util.Date;

/**
 * Singleton that keeps track of time in the Agent world.
 * 
 * Simulation time is accessed through a singleton rather than static methods
 * because the singleton's constructor allows us to avoid dealing with a
 * compulsory init() method or checking for initialization on each method call. 
 * 
 * @author Erik Strottmann
 */
public class TimeManager {
	
	/* -------- Singleton enforcement -------- */ 
	
	private TimeManager() {
		realStartTime = System.currentTimeMillis();
	}
	
	private static TimeManager instance;
	
	public static TimeManager getInstance() {
		if (instance == null) {
			instance = new TimeManager();
		}
		return instance;
	}
	
	// -------- Static and member data -------- */
	
	/**
	 * Multiply a time period in ms by this number to convert to game time.
	 * One hour of game time corresponds to thirty seconds of real time.
	 * */
	public static final int CONVERSION_RATE =  60 * 2; // 60 minutes per hour * 2 thirty-second periods per minute
	/**
	 * The time and date at which the simulation "starts". Added to the actual
	 * elapsed time to calculate the in-world date and time. Currently set to
	 * the date of the first airing of SpongeBob SquarePants:
	 * Saturday, 5/1/1999, 6:00:00 AM PDT (UTC-7)
	 * */
	public static final long FAKE_START_TIME = 925563600000L;
	/**
	 * The time at which the simulation actually started. Used to calculate the
	 * actual elapsed time since the simulation began.
	 */
	public final long realStartTime;
	
	/* -------- Current time -------- */
	
	/**
	 * The in-world time, in milliseconds.
	 */
	public long currentSimTimeMillis() {
		long currentSimTime = FAKE_START_TIME +
				(System.currentTimeMillis() - realStartTime) * CONVERSION_RATE;
		return currentSimTime;
	}
	
	/**
	 * The in-world time, as a java.util.Date. Included for convenience.
	 */
	public Date currentSimDate() {
		return new Date(currentSimTimeMillis());
	}
	
	/* -------- Time until -------- */
	
	/**
	 * Returns how much time is between now and the given time.
	 * 
	 * @param otherSimTime some other time in milliseconds since the epoch
	 * @return the time between now and otherSimTime; negative if otherSimTime
	 * 		   is in the past
	 */
	public long timeUntil(long otherSimTime) {
		return otherSimTime - currentSimTimeMillis();
	}
	
	/**
	 * Returns how much time is between now and the given time.
	 * 
	 * @param otherSimTime a java.util.Date representing some other time
	 * @return the time between now and otherSimTime; negative if otherSimTime
	 * 		   is in the past
	 */
	public long timeUntil(Date otherSimTime) {
		return timeUntil(otherSimTime.getTime());
	}
	
}
