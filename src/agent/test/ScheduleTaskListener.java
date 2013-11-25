package agent.test;

import java.util.EventListener;

import agent.interfaces.ScheduleTaskInterface;

/**
 * Listens for the task scheduler to finish any one of its routines.
 * @author Zach VP
 *
 */
public interface ScheduleTaskListener extends EventListener {
	void taskFinished(ScheduleTaskInterface Event);
}
