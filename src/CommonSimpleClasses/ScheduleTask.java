package CommonSimpleClasses;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Schedule a task for future execution. Can schedule both one-time and
 * recurring tasks.
 * 
 * @author Erik Strottmann
 */
public class ScheduleTask implements ScheduleTaskInterface {
	private TimeManager tm = TimeManager.getInstance();
    private ScheduledExecutorService executor
    		= Executors.newSingleThreadScheduledExecutor();
    
    private static ScheduleTask instance;
    
    private ScheduleTask() {}
    
    public static ScheduleTask getInstance() {
    	if (instance == null) { instance = new ScheduleTask(); }
    	return instance;
    }

    @Override
	public void scheduleDailyTask(Runnable command, int hour, int minute) {		
		long initialDelay = tm.timeUntil(tm.nextSuchTime(hour, minute))
				/TimeManager.CONVERSION_RATE;
		long delay = Constants.DAY/TimeManager.CONVERSION_RATE;
		TimeUnit unit = TimeUnit.MILLISECONDS;
		executor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
		
		if (Constants.DEBUG && Constants.PRINT) {
			System.out.println("ScheduleTask: next occurrence of " +
					hour + ":" + minute + " is in " + initialDelay/1000 +
					" seconds");
		}
	}
	
	@Override
	public void scheduleTaskAtInterval(Runnable command, long firstTime,
			long delay) {
		
		long initialDelay = tm.timeUntil(firstTime);
		long realDelay = delay/TimeManager.CONVERSION_RATE;
		executor.scheduleWithFixedDelay(command, initialDelay, realDelay,
				TimeUnit.MILLISECONDS);
		
		if (Constants.DEBUG && Constants.PRINT) {
			System.out.println("ScheduleTask: executing in " +
					realDelay / 1000 + " seconds");
		}
	}

	@Override
	public void scheduleTaskAtTime(Runnable command, int hour, int minute) {
		long delay = tm.timeUntil(tm.nextSuchTime(hour, minute))
				/TimeManager.CONVERSION_RATE;
		TimeUnit unit = TimeUnit.MILLISECONDS;
		executor.schedule(command, delay, unit);
		
		if (Constants.DEBUG && Constants.PRINT) {
			System.out.println("ScheduleTask: next occurrence of " + hour +
					":" + minute + " is in " + delay/1000 + " seconds");
		}
	}
	
	@Override
	public void scheduleTaskWithDelay(Runnable command, long delay) {
		long realDelay = delay/TimeManager.CONVERSION_RATE;
		TimeUnit unit = TimeUnit.MILLISECONDS;
		executor.schedule(command, realDelay, unit);
		
		if (Constants.DEBUG && Constants.PRINT) {
			System.out.println("ScheduleTask: executing in " +
					realDelay / 1000 + " seconds");
		}
	}
}
