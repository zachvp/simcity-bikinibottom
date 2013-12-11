package agent;

import gui.trace.AlertTag;
import housing.interfaces.ResidentGui;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;

import CommonSimpleClasses.CityLocation;
import agent.interfaces.Person;

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
    private ScheduledExecutorService executor;
    
    /**
     * Sets the Role's agent and location.
     * 
     * @see #getPerson()
     * @see #getLocation()
     */
    protected Role(Person person, CityLocation location) {
    	this.person = person;
    	this.location = location;
    	
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
    public void stateChanged() {
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
     * 
     * @see #waitForInput()
     * @see #doneWaitingForInput()
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
     * @see #doneWaitingForInput()
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
     * @see #waitForInput()
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
    	return getClass().getSimpleName() + " at " + getLocation();
    }
    
    /**
     * The simulated action code for the log display
     * @param tag labels the source of the message
     * @param message is the message itself
     */
    protected void Do(AlertTag tag, String msg) {
    	if (person != null) {
    		person.agentDo(tag, this.getName(), msg);
    	} else {
    		// well i guess we can't do anything
    	}
    }
    
    
    /**
     * The simulated action code for simple printing 
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
}

