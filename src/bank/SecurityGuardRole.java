package bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.PersonAgent;
import agent.Role;
import agent.WorkRole;
import bank.gui.SecurityGuardGui;
import bank.interfaces.BankCustomer;
import bank.interfaces.SecurityGuard;
import bank.interfaces.Teller;

/**
 * Restaurant customer agent.
 */

//Build should not be problem
public class SecurityGuardRole extends WorkRole implements SecurityGuard {
	private String name;
	private Semaphore active = new Semaphore(0, true);
	SecurityGuardGui securityGuardGui;
	
	ScheduleTask task = new ScheduleTask();
	
	boolean endWorkShift = false;
	
	List<WorkRole> workRoles = Collections.synchronizedList(new ArrayList<WorkRole>());

	public enum customerState{waiting, inBank, leaving};
	class WaitingCustomer {
		WaitingCustomer(BankCustomer b, customerState s){
			bc = b;
			state = s;
		}
		BankCustomer bc;
		customerState state;
		
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
	List<WaitingCustomer> waitingCustomers = new ArrayList<WaitingCustomer>();
	
	public SecurityGuardRole(PersonAgent person) {
		super(person);
//		this.name = name;
		Runnable command = new Runnable(){
			@Override
			public void run() {
				//do stuff
				
				msgLeaveWork();
				}
			
		};
		
		// every day at TIME
		int hour = 7;
		int minute = 0;
		
		task.scheduleDailyTask(command, hour, minute);
		
	}
	
	public String getCustomerName() {
		return name;
	}
	// Messages
	
	public void msgLeaveWork() {
		endWorkShift = true;
		stateChanged();
	}
	public void addTeller(Teller t, int deskX) {
		tellerPositions.add(new TellerPosition(t, 250 + (deskX *50), 170, false));
		stateChanged();
	}
	
	public void msgLeavingBank(BankCustomer bc) {
		synchronized(waitingCustomers) {
			for(WaitingCustomer w: waitingCustomers) {
				if(w.bc == bc){
					w.state = customerState.leaving;
				}
			}
		}
		stateChanged();
	}
	
	public void msgCustomerArrived(BankCustomer bc) {
		Do("customer arrived in bank");
		waitingCustomers.add(new WaitingCustomer(bc, customerState.waiting));
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
	
		for(TellerPosition tp: tellerPositions) {
			if(!tp.occupied) {
				for(WaitingCustomer w: waitingCustomers) {
					if(w.state == customerState.waiting){
						sendToTeller(w, tp);
						return true;
					}
				}

			}
		}

		synchronized(waitingCustomers) {
			for(WaitingCustomer w: waitingCustomers) {
				if(w.state == customerState.leaving) {
					removeCustomer(w);
					return true;
				}
			}
		}

		if(endWorkShift) {
//			goOffWork();
			endWorkDay();
			return true;
		}


		return false;
	}
	// Actions
	
	private void sendToTeller(WaitingCustomer wc, TellerPosition tp) {
		Do("sending customer to teller" + tellerPositions.size());
		wc.bc.msgGoToTeller(tp.xPos, tp.t);
		wc.state = customerState.inBank;
		tp.occupied = true;
	}
	
	private void goOffWork() {
		doEndWorkDay();
		acquireSemaphore(active);
		this.deactivate();
		
	}
	
	private void endWorkDay() {//tells all employees to leave
		if(waitingCustomers.isEmpty()){
			for(WorkRole r: workRoles) {
				r.msgLeaveWork();
			}
			goOffWork();
		}
	}
	
	public void resumeWorkDay() {//TODO doesnt work
		this.activate();
		securityGuardGui.DoGoToDesk();
		acquireSemaphore(active);
		for(WorkRole r: workRoles) {
			
		}
	}
	
	private void removeCustomer(WaitingCustomer w) {
		waitingCustomers.remove(w);
		if(waitingCustomers.isEmpty() && endWorkShift){
			endWorkDay();
		}
	}
	
//	private void
	
	
	//ANIMATION #####################
	public void msgAtDestination() {
		active.release();
	}
	
	private void doEndWorkDay() {
		securityGuardGui.DoEndWorkDay();
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

	public void addRole(WorkRole r) {
		workRoles.add(r);
	}
	
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

	@Override
	public int getShiftStartHour() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getShiftStartMinute() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getShiftEndHour() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getShiftEndMinute() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isAtWork() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}



	




}

