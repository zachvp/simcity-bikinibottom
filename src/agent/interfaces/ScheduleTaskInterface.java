package agent.interfaces;

/**
 * An interface for ScheduleTask that allows for flexible timing and
 * repeated events. 
 * @author Zach VP
 *
 */
public interface ScheduleTaskInterface {
	public void scheduleDailyTask(Runnable command, int hour, int minute);
	
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
