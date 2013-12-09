package housing.backend;

import gui.trace.AlertTag;
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
	private ScheduleTask schedule = ScheduleTask.getInstance();
	
	// graphics
	MaintenanceWorkerGui gui;
	
	
	/* --- Constants --- */
	
	// time it takes before deactivating
	private final int FIX_TIME = 15;
	
	// price for fixing a dwelling
	private final double SERVICE_CHARGE = 16;
	
	enum WorkOrderState {FILED, FIXED, FIXING}
	private class WorkOrder {
		Dwelling dwelling;
		WorkOrderState state;
		
		WorkOrder(Dwelling dwelling){
			this.dwelling = dwelling;
			state = WorkOrderState.FILED;
		}
	}
	
	public MaintenanceWorkerRole(PersonAgent agent, CityLocation building) {
		super(agent, building);
		this.gui = new MaintenanceWorkerRoleGui(this);
	}
	
	public void setComplex(HousingComplex complex) {
		gui.setComplex(complex);
	}

	/* --- Messages --- */
	public void msgFileWorkOrder(Dwelling dwelling) {
		if(dwelling == null) {
			Do("Can't fulfill work order. Dwelling does not exits.");
		}
		
		// add a new work order
		Do("Adding Work Order");
		workOrders.add(new WorkOrder(dwelling));
		stateChanged();
	}
	
	public void msgAtDestination(){
		doneWaitingForInput();
	}
	
	@Override
	public boolean pickAndExecuteAnAction() {
		
//		if(task == TaskState.FIRST_TASK){
//			task = TaskState.NONE;
//			return true;
//		}
		
		synchronized(workOrders) {
			for(WorkOrder wo : workOrders) {
				if(wo.state == WorkOrderState.FILED){
					fixProblem(wo);
					return true;
				}
			}
		}
		
		return false;
	}
	
	private void fixProblem(WorkOrder wo) {
//		TODO animation details
		wo.state = WorkOrderState.FIXING;
		
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
		
		Do("Fixed problem.");
		
		DoReturnHome(wo.dwelling.getIDNumber());
		waitForInput();
	}
	
	/* -- Animation Routines --- */
	private void DoGoToDwelling(int unitNumber){
//		Do("Going to dwelling.");
		gui.DoGoToDwelling(unitNumber);
	}
	
	private void DoFixProblem(){
		Do("Fixing problem.");
		gui.DoFixProblem();
		
		Runnable command = new Runnable() {
			public void run(){
				Do("Done fixing problems.");
				doneWaitingForInput();
			}
		};
		// schedule a delay for fixing the problem
		listener.taskFinished(schedule);
		schedule.scheduleTaskWithDelay(command, FIX_TIME * Constants.MINUTE);
	}
	
	private void DoReturnHome(int unit){
		Do("Returning home");
		gui.DoReturnHome(unit);
		deactivate();
	}
	
	/* --- From Role --- */
	@Override
	protected void Do(String msg) {
		Do(AlertTag.HOUSING, msg);
	}

	/* --- Overridden from WorkRole --- */
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

	@Override
	public void setDwelling(Dwelling dwelling) {
		// This is only relevant for the mock worker
		
	}
}
