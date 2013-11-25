package housing;

import housing.interfaces.Dwelling;
import housing.interfaces.MaintenanceWorker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import CommonSimpleClasses.CityLocation;
import agent.PersonAgent;
import agent.Role;
import agent.WorkRole;
import agent.mock.EventLog;

public class MaintenanceWorkerRole extends WorkRole implements MaintenanceWorker {
	/* --- Data --- */
	public EventLog log = new EventLog();
	private List<WorkOrder> workOrders = Collections.synchronizedList(new ArrayList<WorkOrder>());
	
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
//		DoGoToDwelling(wo.dwelling.getIDNumber());
//		DoFixProblem(wo.problemType);
		
		wo.state = WorkOrderState.FIXED;
		wo.dwelling.getResident().msgDwellingFixed();
		workOrders.remove(wo);
		
		log.add("Fixed problem.");
		
//		DoReturnHome();
	}

	@Override
	public int getShiftStartHour() {
		return 6;
	}

	@Override
	public int getShiftStartMinute() {
		return 0;
	}

	@Override
	public int getShiftEndHour() {
		return 11;
	}

	@Override
	public int getShiftEndMinute() {
		return 0;
	}

	@Override
	public boolean isAtWork() {
		return false;
	}

	@Override
	public boolean isOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgLeaveWork() {
		// TODO Auto-generated method stub
		
	}

	public List<WorkOrder> getWorkOrders() {
		return workOrders;
	}

	public void setWorkOrders(List<WorkOrder> workOrders) {
		this.workOrders = workOrders;
	}
}
