package role;

import agent.Agent;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Base class for simple roles
 */
public abstract class Role {
    private Agent agent;
    private boolean active = false;

    protected Role() {
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
     * Returns whether the role is active; that is, whether the role's
     * {@link #pickAndExecuteAnAction} should be called.
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Allows the role's scheduler to be called upon agent state change.
     * 
     * @see #isActive
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Prevents the role's scheduler from being called.
     * 
     * @see #isActive
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Return role name for messages.  Default is to return agent name.
     */
    public String getName() {
        return agent.getName());
    }

    /**
     * The simulated action code
     */
    protected void Do(String msg) {
        print(msg, null);
    }

    /**
     * Print message
     */
    protected void print(String msg) {
        print(msg, null);
    }

    /**
     * Print message with exception stack trace
     */
    protected void print(String msg, Throwable e) {
        StringBuffer sb = new StringBuffer();
        sb.append(getName());
        sb.append(": ");
        sb.append(msg);
        sb.append("\n");
        if (e != null) {
            sb.append(StringUtil.stackTraceString(e));
        }
        System.out.print(sb.toString());
    }

}

