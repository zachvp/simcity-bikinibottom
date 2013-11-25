package housing;

import housing.interfaces.Dwelling;
import housing.interfaces.MaintenanceWorker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.PersonAgent;
import agent.Role;
import agent.mock.EventLog;

public class MaintenanceWorkerRole extends Role implements MaintenanceWorker {
	/* --- Data --- */
	EventLog log = new EventLog();
	List<WorkOrder> workOrders = Collections.synchronizedList(new ArrayList<WorkOrder>());
	
	enum WorkOrderState {FILED, FIXED}
	private class WorkOrder {
		Dwelling dwelling;
		WorkOrderState state;
		
		WorkOrder(Dwelling dwelling){
			this.dwelling = dwelling;
			state = WorkOrderState.FILED;
		}
	}
	
	public MaintenanceWorkerRole(PersonAgent agent) {
		super(agent);
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
	protected boolean pickAndExecuteAnAction() {
		
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
}
