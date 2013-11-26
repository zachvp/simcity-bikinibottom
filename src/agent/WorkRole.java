package agent;

import gui.Building;
import CommonSimpleClasses.CityLocation;
import agent.interfaces.Person;
import classifieds.ClassifiedsClass;
import housing.interfaces.Dwelling;
import agent.interfaces.Person;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.ScheduleTask;
import CommonSimpleClasses.TimeManager;

/**
 * Common interface for jobs that people can work at. Provides a couple of
 * convenience methods for finding the next time work starts.
 * 
 * @author Erik Strottmann
 */
public abstract class WorkRole extends Role {
	
	TimeManager tm = TimeManager.getInstance();
	ScheduleTask tasker = new ScheduleTask();
	
	public WorkRole() {
		super();
		scheduleShiftStart();
		ClassifiedsClass.getClassifiedsInstance().addWorkRole(this);
	}
	
	public WorkRole(Person person) {
		super(person);
		scheduleShiftStart();
		ClassifiedsClass.getClassifiedsInstance().addWorkRole(this);
	}
	
	public WorkRole(Person person, Building building) {
		super(person, building);
		scheduleShiftStart();
		ClassifiedsClass.getClassifiedsInstance().addWorkRole(this);
	}
	
	public WorkRole(Building building) {
		super(building);
		scheduleShiftStart();
		ClassifiedsClass.getClassifiedsInstance().addWorkRole(this);
	}
	
	@Override
	public void setPerson(Person person) {
		super.setPerson(person);
		ClassifiedsClass.getClassifiedsInstance().notifyListeners();
	}
	
	/**
	 * Sets the WorkRole's location. MUST be an instance of {@link Building}
	 * 
	 * @throws IllegalArgumentException if loc is not a Building
	 */
	@Override
	public void setLocation(CityLocation loc) {
		if (!(loc instanceof Building)) {
			throw new IllegalArgumentException("WorkRole's location must be "
					+ "a Building!");
		}
	}
	
    /**
     * Sets the {@link Building} corresponding to this Role's place on
     * the map.
     * 
     * @param loc the building that applies to this work role,
     * 		  or null if this role doesn't have a particular location
     */
	public void setLocation(Building loc) {
		setLocation(loc);
	}
	
	protected void scheduleShiftStart() {
		// wake up
		Runnable command = new Runnable(){
			@Override
			public void run() {
				stateChanged();
			}
		};
		
		// every day at the end of the shift
		int hour = getShiftStartHour() - workSoonThresholdHour();
		int minute = getShiftStartMinute() - workSoonThresholdMinute();
		
		tasker.scheduleDailyTask(command, hour, minute);
	}
	
	/**
	 * Schedules a recurring task: leave work at the end of the shift.
	 */
	protected void scheduleShiftEnd() {
		// go off work
		Runnable command = new Runnable(){
			@Override
			public void run() {
				msgLeaveWork();
			}
		};
		
		// every day at the end of the shift
		int hour = getShiftEndHour();
		int minute = getShiftEndMinute();
		
		tasker.scheduleDailyTask(command, hour, minute);
	}
	
	/**
	 * The hour of day this person's work shift starts, in 24-hour time.
	 * @return an integer in the range [0,23]
	 */
	public final int getShiftStartHour() {
		return ((Building) getLocation()).getOpeningHour();
	}
	/**
	 * The minute of the hour this person's work shift starts.
	 * @return an integer in the range [0,59]
	 */
	public final int getShiftStartMinute() {
		return ((Building) getLocation()).getOpeningMinute();
	}
	/**
	 * The hour of day this person's work shift ends, in 24-hour time.
	 * @return an integer in the range [0,23]
	 */
	public final int getShiftEndHour() {
		return ((Building) getLocation()).getClosingHour();
	}
	/**
	 * The minute of the hour this person's work shift ends.
	 * @return an integer in the range [0,59]
	 */
	public final int getShiftEndMinute() {
		return ((Building) getLocation()).getClosingMinute();
	}
	
	/**
	 * Whether this person's shift starts before midnight and ends after
	 * midnight
	 */
	public final boolean worksThroughMidnight() {
		return getShiftStartHour() > getShiftEndHour() ||
				(getShiftStartHour() == getShiftEndHour()
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
	public final long workDuration() {
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
	public final long nextShiftStartTime() {
		return tm.nextSuchTime(getShiftStartHour(), getShiftStartMinute());
	}
	/** The time the next shift ends, in milliseconds since the epoch. */
	public final long nextShiftEndTime() {
		return TimeManager.nextSuchTime(nextShiftStartTime(),
				getShiftEndHour(), getShiftEndMinute());
	}
	/**
	 * The time the current or previous shift started, in milliseconds since
	 * the epoch.
	 */
	public final long previousShiftStartTime() {
		return tm.previousSuchTime(getShiftStartHour(), getShiftStartMinute());
	}
	/**
	 * The time the current or previous shift ends or will end, in milliseconds
	 * since the epoch.
	 */
	public final long previousShiftEndTime() {
		return TimeManager.nextSuchTime(previousShiftStartTime(),
				getShiftEndHour(), getShiftEndMinute());
	}
	
	/** Whether the person should be at work right now. */
	public final boolean shouldBeAtWork() {
		return tm.isNowBetween(previousShiftStartTime(),
				previousShiftEndTime());
	}
	
	/**
	 * Whether the person is late for work. That is, whether the person should
	 * be at work, but isn't.
	 */
	public final boolean isLate() {
		return !isAtWork() && !isOnBreak() && shouldBeAtWork(); 
	}
	
	/**
	 * Returns the start time (in milliseconds since the epoch) of the current
	 * or next shift.
	 */
	public final long startTime() {
		if (shouldBeAtWork()) {
			return previousShiftStartTime();
		} else {
			return nextShiftStartTime();
		}
	}
	
	public final boolean workStartsSoon() {
		return TimeManager.getInstance().timeUntil(startTime())
				<= workSoonThreshold();
	}
		
	protected final long workSoonThreshold() {
		return workSoonThresholdHour() * Constants.HOUR +
				workSoonThresholdMinute() * Constants.MINUTE;
	}
	
	/**
	 * If there are fewer than this many hours and
	 * {@link #workSoonThresholdMinute} minutes until work, consider work to
	 * start soon.
	 */
	protected int workSoonThresholdHour() {
		return 2;
	}
	
	/**
	 * If there are fewer than {@link #workSoonThresholdHour} hours and
	 * this many minutes until work, consider work to start soon.
	 */
	protected int workSoonThresholdMinute() {
		return 0;
	}
	
	/**
	 * Ends the work day as soon as possible - for example, when a Waiter
	 * finishes helping all customers.
	 */
	public abstract void msgLeaveWork();
}
