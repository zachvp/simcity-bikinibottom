package bank;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.PersonAgent;
import agent.Role;
import bank.gui.SecurityGuardGui;
import bank.interfaces.BankCustomer;
import bank.interfaces.SecurityGuard;
import bank.interfaces.Teller;

/**
 * Restaurant customer agent.
 */

//Build should not be problem
public class SecurityGuardRole extends Role implements SecurityGuard {
	private String name;
	private Semaphore active = new Semaphore(0, true);
	SecurityGuardGui securityGuardGui;
	

	
	class MyCustomer {
		BankCustomer bc;
		
	}
	
	class TellerPosition {
		TellerPosition(Teller t, int x, int y, boolean oc) {
			this.t = t;
			this.xPos = x;
			this.yPos = y;
			this.occupied = oc;
		}
		Teller t;
		int xPos;
		int yPos;
		boolean occupied;
	}
	
	List<TellerPosition> tellerPositions = new ArrayList<TellerPosition>();
	List<BankCustomer> waitingCustomers = new ArrayList<BankCustomer>();
	
	public SecurityGuardRole(PersonAgent person) {
		super(person);
//		this.name = name;
		
	}
	
	public String getCustomerName() {
		return name;
	}
	// Messages
	
	public void addTeller(Teller t, int deskX) {
		tellerPositions.add(new TellerPosition(t, 250 + (deskX *50), 170, false));
		stateChanged();
	}
	
	public void msgCustomerArrived(BankCustomer bc) {
		Do("customer arrived in bank");
		waitingCustomers.add(bc);
		stateChanged();
	}
	public void msgTellerOpen(Teller t) {
		Do("teller is open");
		for(TellerPosition tp: tellerPositions) {
			if(tp.t == t) {
				tp.occupied = false;
			}
		}
		stateChanged();
	}

	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
	if(!waitingCustomers.isEmpty()){
		for(TellerPosition tp: tellerPositions) {
			if(!tp.occupied) {
				sendToTeller(waitingCustomers.get(0), tp);
				return true;
			}
		}
	}
	
		
		return false;
	}
	// Actions
	
	private void sendToTeller(BankCustomer bc, TellerPosition tp) {
		Do("sending customer to teller" + tellerPositions.size());
		bc.msgGoToTeller(tp.xPos, tp.t);
		waitingCustomers.remove(bc);
		tp.occupied = true;
	}
	
//	private void
	
	
	//ANIMATION #####################
	public void msgAtDestination() {
		active.release();
	}
//	private void doGoToDesk() {
//		loanManagerGui.DoGoToDesk();
//	}
//	private void doGoToComputer() {
//		loanManagerGui.DoGoToComputer();
//	}
//	private void doLeaveBank(){ //call at end of shift
//		loanManagerGui.DoLeaveBank();
//	}
//	
	public void setGui(SecurityGuardGui sg) {
		securityGuardGui = sg;
	}
	

	// Accessors, etc.

	private void acquireSemaphore(Semaphore s) {
		try {
			s.acquire();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	

	
	public String toString() {
		return "customer " + getName();
	}



	




}

