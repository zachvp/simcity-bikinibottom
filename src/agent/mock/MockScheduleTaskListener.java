package agent.mock;

import agent.interfaces.ScheduleTaskInterface;
import agent.test.ScheduleTaskListener;

/**
 * Upon completion of the ScheduleTask event, MockScheduleTaskListener will
 * notify the test that should be waiting for a response.
 * @author Zach VP
 *
 */
public class MockScheduleTaskListener implements ScheduleTaskListener {
	private ScheduleTaskInterface Event;
	
	public MockScheduleTaskListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void taskFinished(ScheduleTaskInterface Event) {
		this.Event = Event;
		
		synchronized(this) {
			notifyAll();
		}
	}
	
	public ScheduleTaskInterface getAsynchronousRequest(){
		return Event;
	}

}
