package agent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private boolean awaitingInput = false;
    private ScheduledExecutorService executor;
    
    /**
     * Sets the Role's agent and location.
     * 
     * @see #getPerson()
     * @see #getLocation()
     */
    protected Role(Person person, CityLocation location) {
    	setPerson(person);
    	setLocation(location);
    	
		this.executor = Executors.newSingleThreadScheduledExecutor();
    }
    
    /**
     * Sets the Role's agent. Don't forget to set the location!
     * 
     * @see #getPerson()
     * @see #setLocation()
     */
    protected Role(Person person) {
    	this(person, null);
    }
    
    /**
     * Creates a Role, but doesn't set its agent or location. Don't forget to
     * set those before using the role!
     * 
     * @see #setAgent()
     * @see #setLocation()
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
        person.agentStateChanged();
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
        return this.awaitingInput;
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
    	this.awaitingInput = true;
    }
    
    /**
     * Resumes the Role after it has been temporarily paused by
     * {@link #waitForInput}.
     * 
     * @see #isAwaitingInput()
     */
    public void doneWaitingForInput() {
    	this.awaitingInput = false;
    	stateChanged();
    }

    /**
     * Return role name for messages.  Default is to return agent name.
     */
    public String getName() {
        return person.getName();
    }
    
    @Override
    public String toString() {
    	return person.getName() + "'s " + getClass().getSimpleName();
    }
    
    /**
     * The simulated action code
     */
    protected void Do(String msg) {
        person.agentDo(msg);
    }

    /**
     * Print message
     */
    protected void print(String msg) {
        person.printMsg(msg);
    }

    /**
     * Print message with exception stack trace
     */
    protected void print(String msg, Throwable e) {
        person.printMsg(msg);
    }
    
	// ---- Schedule tasks
	
	public void scheduleDailyTask(Runnable command, int hour, int minute) {
		TimeManager tm = TimeManager.getInstance();		
		long initialDelay =(int) tm.timeUntil(tm.nextSuchTime(hour, minute))
				/TimeManager.CONVERSION_RATE;
		long delay = (int) Constants.DAY/TimeManager.CONVERSION_RATE;
		TimeUnit unit = TimeUnit.MILLISECONDS;
		executor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
		
		if (Constants.DEBUG) {
			Do("next occurrence of " + hour + ":" + minute + " is in " +
					initialDelay + " milliseconds");
		}
	}
	
}

