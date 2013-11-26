package housing;

import housing.gui.MaintenanceWorkerRoleGui;
import housing.interfaces.Dwelling;
import housing.interfaces.MaintenanceWorker;
import housing.interfaces.MaintenanceWorkerGui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mock.EventLog;
import mock.MockScheduleTaskListener;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.ScheduleTask;
import CommonSimpleClasses.Constants.Condition;
import agent.PersonAgent;
import agent.WorkRole;

public class MaintenanceWorkerRole extends WorkRole implements MaintenanceWorker {
	/* --- Data --- */
	public EventLog log = new EventLog();
	private List<WorkOrder> workOrders = Collections.synchronizedList(new ArrayList<WorkOrder>());
	
	// listens to the schedule for completion
	MockScheduleTaskListener listener = new MockScheduleTaskListener();
	
	// used to create time delays and schedule events
	private ScheduleTask schedule = new ScheduleTask();
	
	// prevents the role from being deactivated prematurely
	enum TaskState { FIRST_TASK, NONE, DOING_TASK }
	TaskState task = TaskState.FIRST_TASK;
	
	// graphics
	MaintenanceWorkerGui gui = new MaintenanceWorkerRoleGui(this);
	
	
	/* --- Constants --- */
	private final int SHIFT_START_HOUR = 6;
	private final int SHIFT_START_MINUTE = 0;
	private final int SHIFT_END_HOUR = 12;
	private final int SHIFT_END_MINUTE = 0;
	
	// time it takes before deactivating
	private final int IMPATIENCE_TIME = 7;
	
	// price for fixing a dwelling
	private final double SERVICE_CHARGE = 16;
	
	enum WorkOrderState {FILED, FIXED}
	private class WorkOrder {
		Dwelling dwelling;
		WorkOrderState state;
		
		WorkOrder(Dwelling dwelling){
			this.dwelling = dwelling;
			state = WorkOrderState.FILED;
		}
	}
	
	public MaintenanceWorkerRole(PersonAgent agent, CityLocation residence) {
		super(agent, residence);
	}

	public MaintenanceWorkerRole(PersonAgent person) {
		super(person);
	}

	/* --- Messages --- */
	public void msgFileWorkOrder(Dwelling dwelling) {
		if(dwelling == null) {
			log.add("Can't fulfill work order. Dwelling does not exits.");
		}
		
		// add a new work order
		log.add("Adding Work Order");
		workOrders.add(new WorkOrder(dwelling));
		stateChanged();
	}
	
	public void msgAtDestination(){
		doneWaitingForInput();
	}
	
	@Override
	public boolean pickAndExecuteAnAction() {
		
		if(task == TaskState.FIRST_TASK){
			task = TaskState.NONE;
			return true;
		}
		
		synchronized(workOrders) {
			for(WorkOrder wo : workOrders) {
				if(wo.state == WorkOrderState.FILED){
					fixProblem(wo);
					return true;
				}
			}
		}
		
		
		// check for idleness
		if(task == TaskState.NONE){
			Runnable command = new Runnable() {
				public void run(){
					Do("Deactivating role");
					task = TaskState.FIRST_TASK;
					deactivate();
				}
			};
			// schedule a delay for food consumption
			listener.taskFinished(schedule);
			schedule.scheduleTaskWithDelay(command, IMPATIENCE_TIME * Constants.MINUTE);
		}
		
		return false;
	}
	
	private void fixProblem(WorkOrder wo) {
//		TODO animation details
		task = TaskState.DOING_TASK;
		DoGoToDwelling(wo.dwelling.getIDNumber());
		waitForInput();
		
		DoFixProblem();
		waitForInput();
		
		// update the state and inform the resident that the problem is fixed
		wo.state = WorkOrderState.FIXED;
		wo.dwelling.getResident().msgDwellingFixed();
		wo.dwelling.setCondition(Condition.GOOD);
		
		// charge the landlord
		wo.dwelling.getPayRecipient().msgServiceCharge(SERVICE_CHARGE, this);
		
		workOrders.remove(wo);
		
		log.add("Fixed problem.");
		
		DoReturnHome();
		waitForInput();
		task = TaskState.NONE;
	}
	
	/* -- Animation Routines --- */
	private void DoGoToDwelling(int unitNumber){
		Do("Going to dwelling.");
		gui.DoGoToDwelling();
		// deactivate role
	}
	
	private void DoFixProblem(){
		Do("Fixing problem.");
		gui.DoFixProblem();
	}
	
	private void DoReturnHome(){
		Do("Returning home");
		gui.DoReturnHome();
	}

	/* --- Abstract methods from WorkRole --- */
	@Override
	public int getShiftStartHour() {
		return SHIFT_START_HOUR;
	}

	@Override
	public int getShiftStartMinute() {
		return SHIFT_START_MINUTE;
	}

	@Override
	public int getShiftEndHour() {
		return SHIFT_END_HOUR;
	}

	@Override
	public int getShiftEndMinute() {
		return SHIFT_END_MINUTE;
	}

	@Override
	public boolean isAtWork() {
		return isActive();
	}

	@Override
	public boolean isOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgLeaveWork() {
		deactivate();
	}
	
	public void setGui(MaintenanceWorkerGui gui){
		this.gui = gui;
	}

	/* --- Getters for testing purposes --- */
	public List<WorkOrder> getWorkOrders() {
		return workOrders;
	}

	public void setWorkOrders(List<WorkOrder> workOrders) {
		this.workOrders = workOrders;
	}

	public void msgHereIsPayment(double bill) {
		person.getWallet().addCash(bill);
	}
}
