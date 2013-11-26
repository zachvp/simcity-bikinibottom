package agent;

import java.util.concurrent.Semaphore;

import mock.EventLog;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.StringUtil;

/**
 * Base class for simple agents
 */
public abstract class Agent {
    Semaphore stateChange = new Semaphore(1, true);//binary semaphore, fair
    private AgentThread agentThread;
    private EventLog log = new EventLog();
    
    private boolean pause = false;
    
    protected Agent() {}

    /**
     * This should be called whenever state has changed that might cause
     * the agent to do something.
     */
    protected void stateChanged() {
        stateChange.release();
    }
    
    /** The number of permits the stateChange semaphore has. */
    public int getStateChangePermits() {
    	return stateChange.availablePermits();
    }

    /**
     * Agents must implement this scheduler to perform any actions appropriate for the
     * current state.  Will be called whenever a state change has occurred,
     * and will be called repeated as long as it returns true.
     *
     * @return true iff some action was executed that might have changed the
     *         state.
     */
    protected abstract boolean pickAndExecuteAnAction();

    /**
     * Return agent name for messages.  Default is to return java instance
     * name.
     */
    public String getName() {
        return StringUtil.shortName(this);
    }

    /**
     * The simulated action code
     */
    protected void Do(String msg) {
        // print(msg, null);
        log.add(msg);
    }

    /**
     * Print message
     */
    protected void print(String msg) {
        // print(msg, null);
    	log.add(msg);
    }

    /**
     * Print message with exception stack trace
     */
    protected void print(String msg, Throwable e) {
        if (!Constants.PRINT) { return; }
    	
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

    /**
     * Start agent scheduler thread.  Should be called once at init time.
     */
    public synchronized void startThread() {
        if (agentThread == null) {
            agentThread = new AgentThread(getName(), this);
            agentThread.start(); // causes the run method to execute in the AgentThread below
        } else {
            agentThread.interrupt();//don't worry about this for now
        }
    }

    /**
     * Stop agent scheduler thread.
     */
    //In this implementation, nothing calls stopThread().
    //When we have a user interface to agents, this can be called.
    public void stopThread() {
        if (agentThread != null) {
            agentThread.stopAgent();
            agentThread = null;
        }
    }
    
    /**
     * Temporarily pause agent scheduler thread.
     */
    public void pauseThread() {
    	pause = true;
    }
    
    /**
     * Resume a previously paused agent scheduler thread.
     * 
     * @pre must have previously called pauseThread()
     */
    public void resumeThread() {
    	pause = false;
    	stateChanged();
    }
    
    /**
     * Agent scheduler thread, calls respondToStateChange() whenever a state
     * change has been signaled.
     */
    private class AgentThread extends Thread {
        private volatile boolean goOn = false;
        //Pointer for debugging purposes
		private Agent agent;

        private AgentThread(String name, Agent agent) {
            super(name);
            this.agent = agent;
        }

        public void run() {
            goOn = true;

            while (goOn) {
            	
                try {
                    // The agent sleeps here until someone calls, stateChanged(),
                    // which causes a call to stateChange.give(), which wakes up agent.
                    stateChange.acquire();
                    
                    //The next while clause is the key to the control flow.
                    //When the agent wakes up it will call respondToStateChange()
                    //repeatedly until it returns FALSE.
                    //You will see that pickAndExecuteAnAction() is the agent scheduler.
                    while (!pause && pickAndExecuteAnAction());
                    
                } catch (InterruptedException e) {
                    // no action - expected when stopping or when deadline changed
                }
            }
        }

        private void stopAgent() {
            goOn = false;
            this.interrupt();
        }
    }
}

