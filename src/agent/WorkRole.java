package agent;

import CommonSimpleClasses.CityLocation;

/**
 * Common interface for jobs that people can work at. Provides a couple of
 * convenience methods for finding the next time work starts.
 * 
 * @author Erik Strottmann
 */
public abstract class WorkRole extends Role {
	
	TimeManager tm = TimeManager.getInstance();
	
	/**
	 * The hour of day this person's work shift starts, in 24-hour time.
	 * @return an integer in the range [0,23]
	 */
	public abstract int getShiftStartHour();
	/**
	 * The minute of the hour this person's work shift starts.
	 * @return an integer in the range [0,59]
	 */
	public abstract int getShiftStartMinute();
	/**
	 * The hour of day this person's work shift ends, in 24-hour time.
	 * @return an integer in the range [0,23]
	 */
	public abstract int getShiftEndHour();
	/**
	 * The minute of the hour this person's work shift ends.
	 * @return an integer in the range [0,59]
	 */
	public abstract int getShiftEndMinute();
	
	/**
	 * Whether this person's shift starts before midnight and ends after
	 * midnight
	 */
	public boolean worksThroughMidnight() {
		return getShiftStartHour() > getShiftEndHour() || (getShiftStartHour() == getShiftEndHour()
				&& getShiftStartMinute() > getShiftEndMinute());
	}
	
	/** Whether the person is currently at work */
	public abstract boolean isAtWork();
	/**
	 * Whether the person is on a break from work (implies returning to work
	 * soon)
	 */
	public abstract boolean isOnBreak();
	
	/**
	 * The duration of work, in milliseconds. Accounts for daily and hourly
	 * overflow, but assumes that no single shift is longer than 23 hours and
	 * 59 minutes.
	 */
	public long workDuration() {
		int durationHours = getShiftEndHour() - getShiftStartHour();
		int durationMinutes = getShiftEndMinute() - getShiftStartMinute();
		
		// Account for overflow at midnight, if necessary
		if (worksThroughMidnight()) {
			durationHours += 24;
		}
		// Account for hourly overflow, if necessary
		if (getShiftStartMinute() > getShiftEndMinute()) {
			durationMinutes += 60;
			durationHours -= 1;
		}
		
		return durationHours * Constants.HOUR +
				durationMinutes * Constants.MINUTE;
	}
	
	/** The time the next shift starts, in milliseconds since the epoch. */
	public long nextShiftStartTime() {
		return tm.nextSuchTime(getShiftStartHour(), getShiftStartMinute());
	}
	/** The time the next shift ends, in milliseconds since the epoch. */
	public long nextShiftEndTime() {
		return TimeManager.nextSuchTime(nextShiftStartTime(),
				getShiftEndHour(), getShiftEndMinute());
	}
	/**
	 * The time the current or previous shift started, in milliseconds since
	 * the epoch.
	 */
	public long previousShiftStartTime() {
		return tm.previousSuchTime(getShiftStartHour(), getShiftStartMinute());
	}
	/**
	 * The time the current or previous shift ends or will end, in milliseconds
	 * since the epoch.
	 */
	public long previousShiftEndTime() {
		return TimeManager.nextSuchTime(previousShiftStartTime(),
				getShiftEndHour(), getShiftEndMinute());
	}
	
	/** Whether the person should be at work right now. */
	public boolean shouldBeAtWork() {
		return tm.isNowBetween(previousShiftStartTime(),
				previousShiftEndTime());
	}
	
	/**
	 * Whether the person is late for work. That is, whether the person should
	 * be at work, but isn't.
	 */
	public boolean isLate() {
		return !isAtWork() && !isOnBreak() && shouldBeAtWork(); 
	}
	
	/**
	 * Returns the start time (in milliseconds since the epoch) of the current
	 * or next shift.
	 */
	public long startTime() {
		if (shouldBeAtWork()) {
			return previousShiftStartTime();
		} else {
			return nextShiftStartTime();
		}
	}
}
