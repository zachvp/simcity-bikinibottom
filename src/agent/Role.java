package agent;

import housing.interfaces.ResidentGui;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import agent.interfaces.ScheduleTaskInterface;
import agent.interfaces.Person;
import CommonSimpleClasses.CityLocation;

/**
 * Base class for simple roles
 * 
 * @author Erik Strottmann
 */
public abstract class Role {
    protected Person person;
    protected CityLocation location;
    private boolean active = false;
    // private boolean awaitingInput = false;
    private Semaphore awaitingInputSem = new Semaphore(0, true);
    
    /**
     * Sets the Role's person and location.
     * 
     * @see #getPerson
     * @see #getLocation
     */
    protected Role(Person person, CityLocation location) {
    	this.person = person;
    	this.location = location;
    }
    
    /**
     * Sets the Role's person. Don't forget to set the location!
     * 
     * @see #getPerson
     * @see #setLocation
     */
    protected Role(Person person) {
    	this(person, null);
    }
    
    /**
     * Sets the Role's location but not its person. The Role won't run without
     * a person!
     * 
     * @see #setPerson
     * @see #getLocation
     */
    protected Role(CityLocation location) {
    	this(null, location);
    }
    
    /**
     * Creates a Role, but doesn't set its person or location. Don't forget to
     * set those before using the role!
     * 
     * @see #setPerson
     * @see #setLocation
     */
    protected Role() {
    	this(null, null);
    }
    
    public Person getPerson() {
    	return this.person;
    }
    
    public void setPerson(Person person) {
    	this.person = person;
    }
    
    /**
     * Returns the {@link CityLocation} corresponding to this Role's place on
     * the map. For roles that takes place in a building, it's obvious what
     * this should be, but what about PassengerRole, for example? Just return
     * null.
     * 
     * @return the building (or other location) that applies to this role, or
     * 		   null if this role doesn't have a particular location
     */
    public CityLocation getLocation() {
    	return this.location;
    }
    
    /**
     * Sets the {@link CityLocation} corresponding to this Role's place on
     * the map. For roles that takes place in a building, it's obvious what
     * this should be, but what about PassengerRole, for example? Just use
     * null.
     * 
     * @param loc the building (or other location) that applies to this role,
     * 		  or null if this role doesn't have a particular location
     */
    public void setLocation(CityLocation loc) {
    	this.location = loc;
    }
    
    /**
     * This should be called whenever state has changed that might cause
     * the agent to do something.
     */
    protected void stateChanged() {
    	if (person != null) {
    		person.agentStateChanged();
    	}
    }

    /**
     * Roles must implement this scheduler to perform any actions appropriate
     * for the current state.  Will only be called when the role is active.
     * If active, this will be called whenever an agent state change has
     * occurred, and will be called repeated as long as the agent's scheduler
     * returns true.
     *
     * @return true iff some action was executed that might have changed the
     *         state.
     */
    protected abstract boolean pickAndExecuteAnAction();

    /**
     * Returns whether the role is active. To determine whether
     * {@link #pickAndExecuteAnAction} should be called, use both this and
     * {@link #isAwaitingInput()}.
     */
    public boolean isActive() {
        return this.active;
    }
    
    /**
     * Returns whether the role is awaiting input. If true, this usually means
     * that the Role is waiting for GUI input of some kind, especially moving
     * for a multi-step action. Use this in conjunction with
     * {@link #isActive()} to determine whether
     * {@link #pickAndExecuteAnAction()} should be called.
     */
    public boolean isAwaitingInput() {
        // return this.awaitingInput;
    	return this.awaitingInputSem.availablePermits() < 0;
    }
    
    /**
     * Allows the role's scheduler to be called upon agent state change.
     * 
     * @see #isActive()
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Prevents the role's scheduler from being called.
     * 
     * @see #isActive()
     */
    public void deactivate() {
        this.active = false;
    }
    
    /**
     * Temporarily pauses the Role.
     * 
     * @see #isAwaitingInput()
     */
    public void waitForInput() {
    	// this.awaitingInput = true;
    	try {
			this.awaitingInputSem.acquire();
		} catch (InterruptedException e) {
			// thread interrupted on semaphore acquire!
			e.printStackTrace();
		}
    }
    
    /**
     * Resumes the Role after it has been temporarily paused by
     * {@link #waitForInput}.
     * 
     * @see #isAwaitingInput()
     */
    public void doneWaitingForInput() {
    	// this.awaitingInput = false;
    	this.awaitingInputSem.release();
    	stateChanged();
    }

    /**
     * Return role name for messages.  Default is to return agent name.
     */
    public String getName() {
    	if (person != null) {
    		return person.getName();
    	} else {
    		return "Nobody";
    	}
    }
    
    @Override
    public String toString() {
    	if (person != null) {
    		return getName() + "'s " + getClass().getSimpleName();
    	} else {
    		return getClass().getSimpleName() + " at " + getLocation();
    	}
    }
    
    /**
     * The simulated action code
     */
    protected void Do(String msg) {
    	if (person != null) {
    		person.agentDo(msg);
    	} else {
    		// well i guess we can't do anything
    	}
    }

    /**
     * Print message
     */
    protected void print(String msg) {
    	if (person != null) {
    		person.printMsg(msg);
    	} else {
    		// well i guess we can't print anything
    	}
    }

    /**
     * Print message with exception stack trace
     */
    protected void print(String msg, Throwable e) {
    	if (person != null) {
    		person.printMsg(msg, e);
    	} else {
    		// well i guess we can't print anything
    	}
    }
    
	// ---- Schedule tasks
    public static class ScheduleTask implements ScheduleTaskInterface {
    	private TimeManager tm = TimeManager.getInstance();
        private ScheduledExecutorService executor
        		= Executors.newSingleThreadScheduledExecutor();

    	/**
         * Executes the command every day at hour:minute.
         */
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
		
		/**
		 * Executes the scheduled task at firstTime and every delay
		 * milliseconds thereafter (game time).
		 */
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
    
	
		/**
		 * Executes the command one time, at the next occurrence of hour:minute.
		 */
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
		
		/**
		 * Executes the command one time with the given delay.
		 * 
		 * @param command
		 * @param delay IMPORTANT: this is game time delay, not real time delay.
		 * 				To make the task execute in 1 minute of game time (half a
		 * 				second real time), delay should be 60000.
		 */
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

	public void setGui(ResidentGui gui) {
		// TODO Auto-generated method stub
		
	}
}

