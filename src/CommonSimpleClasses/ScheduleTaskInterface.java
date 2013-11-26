package CommonSimpleClasses;

/**
 * An interface for ScheduleTask that allows for flexible timing and
 * repeated events. 
 * @author Zach VP
 *
 */
public interface ScheduleTaskInterface {
	
	/**
     * Executes the command every day at hour:minute.
     */
	public void scheduleDailyTask(Runnable command, int hour, int minute);
	
	/**
	 * Executes the scheduled task at firstTime and every delay
	 * milliseconds thereafter (game time).
	 */
	public void scheduleTaskAtInterval(Runnable command, long firstTime,
			long delay);
	
	/**
	 * Executes the command one time, at the next occurrence of hour:minute.
	 */
	public void scheduleTaskAtTime(Runnable command, int hour, int minute);
	
	/**
	 * Executes the command one time with the given delay.
	 * 
	 * @param command
	 * @param delay IMPORTANT: this is game time delay, not real time delay.
	 * 				To make the task execute in 1 minute of game time (half a
	 * 				second real time), delay should be 60000.
	 */
	public void scheduleTaskWithDelay(Runnable command, long delay);
}
