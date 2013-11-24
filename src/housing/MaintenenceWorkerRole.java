package housing;

import housing.interfaces.Dwelling;
import housing.interfaces.MaintenenceWorker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import agent.Role;
import agent.mock.EventLog;

public class MaintenenceWorkerRole extends Role implements MaintenenceWorker {
	/* --- Data --- */
	EventLog log = new EventLog();
	List<WorkOrder> workOrders = Collections.synchronizedList(new ArrayList<WorkOrder>());
	
	enum WorkOrderState {FILED, FIXED}
	private class WorkOrder {
		Dwelling dwelling;
		WorkOrderState state;
		
		WorkOrder(Dwelling dwelling, String problem){
			this.dwelling = dwelling;
			state = WorkOrderState.FILED;
		}
	}
	
	public MaintenenceWorkerRole() {
		
	}

	/* --- Messages --- */
	public void msgFileWorkOrder(String problemType, Dwelling dwelling) {
		if(dwelling == null) {
			log.add("Can't fulfill work order. Dwelling does not exits.");
		}
		
		// add a new work order
		workOrders.add(new WorkOrder(dwelling, problemType));
		stateChanged();
	}
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		synchronized(workOrders) {
			for(WorkOrder wo : workOrders) {
				if(wo.state == WorkOrderState.FILED){
					fixProblem(wo);
				}
			}
		}
		
		return false;
	}
	
	private void fixProblem(WorkOrder wo) {
//		TODO animation details
//		DoGoToDwelling(wo.dwelling.getIDNumber());
//		DoFixProblem(wo.problemType);
		
		wo.dwelling.setConditionGood();
		wo.state = WorkOrderState.FIXED;
		workOrders.remove(wo);
		
//		DoReturnHome();
	}
}
