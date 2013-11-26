package housing;

import housing.gui.MaintenanceWorkerRoleGui;
import housing.interfaces.Dwelling;
import housing.interfaces.MaintenanceWorker;
import housing.interfaces.MaintenanceWorkerGui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mock.EventLog;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants.Condition;
import agent.PersonAgent;
import agent.WorkRole;

public class MaintenanceWorkerRole extends WorkRole implements MaintenanceWorker {
	/* --- Data --- */
	public EventLog log = new EventLog();
	private List<WorkOrder> workOrders = Collections.synchronizedList(new ArrayList<WorkOrder>());
	
	// graphics
	MaintenanceWorkerGui gui = new MaintenanceWorkerRoleGui(this);
	
	/* --- Constants --- */
	private final int SHIFT_START_HOUR = 6;
	private final int SHIFT_START_MINUTE = 0;
	private final int SHIFT_END_HOUR = 12;
	private final int SHIFT_END_MINUTE = 0;
	
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
		DoGoToDwelling(wo.dwelling.getIDNumber());
		waitForInput();
		
		DoFixProblem();
		waitForInput();
		
		// update the state and inform the resident that the problem is fixed
		wo.state = WorkOrderState.FIXED;
		wo.dwelling.getResident().msgDwellingFixed();
		wo.dwelling.setCondition(Condition.GOOD);
		workOrders.remove(wo);
		
		// TODO charge landlord for the service
		
		log.add("Fixed problem.");
		
		DoReturnHome();
		waitForInput();
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

	/* --- Getters for testing purposes --- */
	public List<WorkOrder> getWorkOrders() {
		return workOrders;
	}

	public void setWorkOrders(List<WorkOrder> workOrders) {
		this.workOrders = workOrders;
	}
}
