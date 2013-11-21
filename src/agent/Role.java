package agent;

/**
 * Base class for simple roles
 * 
 * @author Erik Strottmann
 */
public abstract class Role {
    protected Agent agent;
    private boolean active = false;
    private boolean awaitingInput = false;

    protected Role() {
    	
    }
    
    protected Role(Agent agent) {
    	setAgent(agent);
    }
    
    public Agent getAgent() {
    	return this.agent;
    }
    
    public void setAgent(Agent agent) {
    	this.agent = agent;
    }

    /**
     * This should be called whenever state has changed that might cause
     * the agent to do something.
     */
    protected void stateChanged() {
        agent.stateChanged();
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
     * {@link #pickAndExecuteAnAction} should be called, instead use
     * {@link #isReady}.
     */
    public boolean isActive() {
        return this.active;
    }
    
    /**
     * Returns whether the role is awaiting input. If true, this usually means
     * that the Role is waiting for GUI input of some kind, especially 
     */
    public boolean isAwaitingInput() {
        return this.awaitingInput;
    }
    
    /**
     * Returns whether the role is active and ready; that is, whether the role's
     * {@link #pickAndExecuteAnAction} should be called.
     */
    public boolean isReady() {
        return this.active && !this.awaitingInput;
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
        return agent.getName();
    }

    /**
     * The simulated action code
     */
    protected void Do(String msg) {
        agent.Do(msg);
    }

    /**
     * Print message
     */
    protected void print(String msg) {
        agent.print(msg);
    }

    /**
     * Print message with exception stack trace
     */
    protected void print(String msg, Throwable e) {
        agent.print(msg);
    }

}

