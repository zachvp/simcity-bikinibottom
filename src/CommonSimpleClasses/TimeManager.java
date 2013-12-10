package CommonSimpleClasses;

import java.util.Calendar;

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
	 * Multiply a time period in milliseconds by this number to convert to game
	 * time. One hour of game time corresponds to thirty seconds of real time.
	 */
	// 60 minutes per hour * 2 thirty-second periods per minute
	public static final int CONVERSION_RATE =  60 * 2;
	
	/**
	 * The time and date at which the simulation "starts". Added to the actual
	 * elapsed time to calculate the in-world date and time. Currently set to
	 * the date at which the United States government forced the residents of
	 * Bikini Atoll to relocate in order that it might carry out nuclear
	 * testing "for the good of mankind and to end all world wars":
	 * Thu Mar 07 1946 08:00:00 GMT-0800 (PST)
	 */
	private static final long FAKE_START_TIME = -751708800000L;
	
	/**
	 * The time at which the simulation actually started. Used to calculate the
	 * actual elapsed time since the simulation began.
	 */
	public final long realStartTime;
	
	/* -------- Simple time getters -------- */
	
	/**
	 * @return the simulation "start" time in milliseconds
	 * @see #FAKE_START_TIME
	 */
	public long fakeStartTime() {
		return FAKE_START_TIME;
	}
		
	/**
	 * The in-world time, in milliseconds.
	 */
	public long currentSimTime() {
		long currentSimTime = FAKE_START_TIME +
				(System.currentTimeMillis() - realStartTime) * CONVERSION_RATE;
		return currentSimTime;
	}
		
	/* -------- Time comparisons -------- */
	
	/**
	 * Calculates how much time is between now and the given time.
	 * 
	 * @param otherSimTime some other time in milliseconds since the epoch
	 * @return the time between now and otherSimTime; negative if otherSimTime
	 * 		   is in the past
	 */
	public long timeUntil(long otherSimTime) {
		return howLongBetween(currentSimTime(), otherSimTime);
	}
		
	/**
	 * Calculates how much time is between the given two times; if firstTime
	 * comes after secondTime, this value is negative.
	 */
	public static long howLongBetween(long firstTime, long secondTime) {
		return secondTime - firstTime;
	}
	
	/** Determines whether firstTime is strictly before secondTime. */
	public static boolean isTimeBefore(long firstTime, long secondTime) {
		return howLongBetween(firstTime, secondTime) > 0;
	}
	/** Determines whether firstTime is strictly after secondTime. */
	public static boolean isTimeAfter(long firstTime, long secondTime) {
		return howLongBetween(firstTime, secondTime) < 0;
	}
		
	/**
	 * Determines whether "now" is between "otherOne" and "otherTwo". Works
	 * whether otherOne or otherTwo comes first, and even if any of the three
	 * times are equal.
	 * 
	 * @param now the time in question: is now between otherOne and otherTwo?
	 * @param otherOne
	 * @param otherTwo
	 */
	public static boolean isTimeBetween(long now, long otherOne, long otherTwo) {
		if (now == otherOne || isTimeBefore(now, otherOne)) {
			// Now is equal to or before otherOne, so now should be equal to or
			// after otherTwo.
			return now == otherTwo || isTimeAfter(now, otherTwo);
		} else {
			// Now is after otherOne, so now should be equal to or before
			// otherTwo.
			return now == otherTwo || isTimeBefore(now, otherTwo);
		}
	}
	
	/**
	 * Determines whether the current sim time is between "otherOne" and
	 * "otherTwo". Works whether otherOne or otherTwo comes first, and even if
	 * any of the three times are equal.
	 */
	public boolean isNowBetween(long otherOne, long otherTwo) {
		return isTimeBetween(currentSimTime(), otherOne, otherTwo);
	}
	
	/**
	 * Determines whether the current sim time is between
	 * "firstHour:firstMinute" and "secondHour:secondMinute". Works whether
	 * firstHour:firstMinute or secondHour:secondMinute comes first, and even
	 * if any of the three times are equal. 
	 */
	public boolean isNowBetween(int firstHour, int firstMinute, int secondHour,
			int secondMinute) {
		
		return isNowBetween(timeToday(firstHour, firstMinute),
				timeToday(secondHour, secondMinute));
	}
	
	/* -------- Occurrence of a time in h:m on a given day -------- */
	
	/**
	 * Given a reference time (in milliseconds since the epoch) and a second
	 * time (in hours and seconds), calculates the time (in milliseconds) that
	 * the second time occurred on the same day as the first time.
	 * 
	 * @param now a reference time, in milliseconds since the epoch
	 * @param hours in 24-hour time (e.g. 14 instead of 2 PM)
	 * @param minutes
	 */
	public static long timeOnSameDay(long now, int hours, int minutes) {
		// Set up a Calendar with the current simulation time
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(now);
				
		// Adjust the Calendar to the appropriate time of day
		cal.set(Calendar.HOUR_OF_DAY, hours);
		cal.set(Calendar.MINUTE, minutes);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTimeInMillis();
	}
	
	/**
	 * Calculates the time (in milliseconds since the epoch) that the given
	 * hours and minutes occurred or will occur, n days from now. Negative
	 * values of n are treated as "n days ago".
	 * 
	 * @param now a reference time, in milliseconds since the epoch
	 * @param hours in 24-hour time (e.g. 14 instead of 2 PM)
	 * @param minutes
	 * @param n how many days in the future to consider, with negative values
	 * 		  representing days in the past
	 */
	public static long timeNDaysFromNow(long now, int hours, int minutes,
			int n) {
		
		// Set up a Calendar with the given time on the same day as now
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeOnSameDay(now, hours, minutes));
		
		// Adjust the calendar to n days from now
		cal.add(Calendar.DAY_OF_YEAR, n);
		
		return cal.getTimeInMillis();
	}
		
	/**
	 * Calculates the time today (in milliseconds since the epoch) that the
	 * given hours and minutes occurred or will occur.
	 * 
	 * @param hours in 24-hour time (e.g. 14 instead of 2 PM)
	 * @param minutes
	 */
	public long timeToday(int hours, int minutes) {
		return timeOnSameDay(currentSimTime(), hours, minutes);
	}
	
	/**
	 * Calculates the time tomorrow (in milliseconds since the epoch) that the
	 * given hours and minutes will occur.
	 * 
	 * @param hours in 24-hour time (e.g. 14 instead of 2 PM)
	 * @param minutes
	 */
	public long timeTomorrow(int hours, int minutes) {
		return timeNDaysFromNow(currentSimTime(), hours, minutes, 1);
	}
	
	/**
	 * Calculates the time today (in milliseconds since the epoch) that the
	 * given hours and minutes occurred.
	 * 
	 * @param hours in 24-hour time (e.g. 14 instead of 2 PM)
	 * @param minutes
	 */
	public long timeYesterday(int hours, int minutes) {
		return timeNDaysFromNow(currentSimTime(), hours, minutes, -1);
	}
	
	/* -------- Next/previous occurrence of a time in h:m -------- */
	
	/**
	 * Calculates the next time the given hours and minutes will occur,
	 * relative to "now". Will be either later today or tomorrow.
	 * 
	 * @param now a reference time, in milliseconds since the epoch
	 * @param hours in 24-hour time (e.g. 14 instead of 2 PM)
	 * @param minutes
	 */
	public static long nextSuchTime(long now, int hours, int minutes) {
		long timeToday = timeOnSameDay(now, hours, minutes);
		if (howLongBetween(now, timeToday) >= 0) {
			// This time today hasn't yet passed.
			return timeToday;
		} else {
			// This time today has already passed. Use tomorrow instead.
			return timeNDaysFromNow(now, hours, minutes, 1);
		}
	}
	
	/**
	 * Calculates the next time the given hours and minutes will occur,
	 * relative to the current sim time. Will be either later today or tomorrow.
	 * 
	 * @param hours in 24-hour time (e.g. 14 instead of 2 PM)
	 * @param minutes
	 */
	public long nextSuchTime(int hours, int minutes) {
		return nextSuchTime(currentSimTime(), hours, minutes);
	}
	
	/**
	 * Calculates the previous time the given hours and minutes occurred,
	 * relative to "now". Will be either yesterday or earlier today.
	 * 
	 * @param now a reference time, in milliseconds since the epoch
	 * @param hours in 24-hour time (e.g. 14 instead of 2 PM)
	 * @param minutes
	 */
	public static long previousSuchTime(long now, int hours, int minutes) {
		long timeToday = timeOnSameDay(now, hours, minutes);
		if (howLongBetween(now, timeToday) <= 0) {
			// This time today has already passed.
			return timeToday;
		} else {
			// This time today hasn't yet passed. Use yesterday instead.
			return timeNDaysFromNow(now, hours, minutes, -1);
		}
	}
	
	/**
	 * Calculates the previous time the given hours and minutes occurred,
	 * relative to the current sim time. Will be either yesterday or earlier
	 * today.
	 * 
	 * @param hours in 24-hour time (e.g. 14 instead of 2 PM)
	 * @param minutes
	 */
	public long previousSuchTime(int hours, int minutes) {
		return previousSuchTime(currentSimTime(), hours, minutes);
	}

	public static boolean isTimeOnWeekend(long workStartTime) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(workStartTime);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
	}
	
	public boolean isNowOnWeekend() {
		return isTimeOnWeekend(currentSimTime());
	}
	
}
