package bank;

import gui.trace.AlertTag;

import java.util.ArrayList;

import bank.gui.BankBuilding;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.ScheduleTask;
import CommonSimpleClasses.sound.Sound;
import agent.PersonAgent;
import agent.Role;
import agent.WorkRole;
import agent.gui.Gui;
import agent.interfaces.Person;
import bank.gui.SecurityGuardGui;
import bank.interfaces.AccountManager;
import bank.interfaces.BankCustomer;
import bank.interfaces.Robber;
import bank.interfaces.SecurityGuard;
import bank.interfaces.SecurityGuardGuiInterface;
import bank.interfaces.Teller;

/**
 * Restaurant customer agent.
 */

//Build should not be problem
public class SecurityGuardRole extends WorkRole implements SecurityGuard {
	private String name;
	private Semaphore active = new Semaphore(0, true);
	private Sound robBank = Sound.getInstance();
	SecurityGuardGuiInterface securityGuardGui;
	
	BankBuilding bankBuilding;
	
	ScheduleTask task = ScheduleTask.getInstance();
	
	boolean endWorkShift = false;
	boolean atWork;
	
	double paycheckAmount = 200;
	
	List<WorkRole> workRoles = Collections.synchronizedList(new ArrayList<WorkRole>());
	AccountManager accountManager;
	
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
	
	enum robberState {entered, robbing, leaving, done};
	class MyRobber{
		MyRobber(Robber r, robberState state) {
			this.r = r;
			this.state = state;
		}
		Robber r;
		robberState state;
	}
	List<MyRobber> robbers = new ArrayList<MyRobber>();
	
	int startHour;
	int startMinute;
	int endHour;
	int endMinute;
	
	public SecurityGuardRole(Person person, CityLocation bank) {
		super(person, bank);
		bankBuilding = (BankBuilding) bank;
		
//		this.name = name;
		atWork = false;
		Runnable command = new Runnable(){
			@Override
			public void run() {
				//do stuff
				
				msgLeaveWork();
				
				bankBuilding.changeOpenSign(false, startHour, endHour);
//				System.out.println("ClOCKS RUN");
				}
			
		};
		
		// every day at TIME to end
		int hour = ((BankBuilding) bank).getClosingHour();
		int minute = ((BankBuilding) bank).getClosingMinute();
		
		bankBuilding . changeOpenSign(false, startHour, endHour);
		task.scheduleDailyTask(command, hour, minute);
		
		startHour = ((BankBuilding) bank).getOpeningHour();
		startMinute = ((BankBuilding) bank).getOpeningMinute();
		endHour = ((BankBuilding) bank).getClosingHour();
		endMinute =((BankBuilding) bank).getClosingMinute();
		
//		System.out.println("sdfghjhgfdfghjhgfgh " + startHour);
	}
	
	public String getCustomerName() {
		return name;
	}
	// Messages
	
	public void msgLeaveWork() {
		endWorkShift = true;
		stateChanged();
	}
	public void addTeller(Teller t, int deskX) {//initializes tellers and welcomes new ones to the banco
		tellerPositions.add(new TellerPosition(t, 250 + (deskX *50), 170, false));
		stateChanged();
	}
	/*
	 * starts bank process by either qeueuing up customer in line or directing to teller
	 * @see bank.interfaces.SecurityGuard#msgCustomerArrived(bank.interfaces.BankCustomer)
	 */
	public void msgCustomerArrived(BankCustomer bc) {
		Do(AlertTag.BANK, "customer arrived in bank");
		waitingCustomers.add(new WaitingCustomer(bc, customerState.waiting));
		stateChanged();
	}
	/*
	 * allows another customer to go to teller, or allows employees to go home if it is the end of the day
	 * @see bank.interfaces.SecurityGuard#msgLeavingBank(bank.interfaces.BankCustomer)
	 */
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

	public void msgTellerOpen(Teller t) {
		Do(AlertTag.BANK, "teller is open");
		for(TellerPosition tp: tellerPositions) {
			if(tp.t == t) {
				tp.occupied = false;
			}
		}
		stateChanged();
	}
	
	public void msgIAmRobbingTheBank(Robber r) {
		robbers.add(new MyRobber(r, robberState.entered));
		stateChanged();
	}

	public void msgRobberLeavingBank(Robber r) {
		for(MyRobber mr: robbers) {
			if(mr.r == r) {
				mr.state = robberState.leaving;
			}
		}
		stateChanged();
	}

	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
	
		
		
		if(!atWork) {
			goToWork();
			return true;
		}
		
		for(MyRobber m: robbers){
			if(m.state == robberState.entered){
				tryToStopRobber(m);
				return true;
			}
		}
		
		for(MyRobber m: robbers){
			if(m.state == robberState.leaving){
				removeRobber(m);
				return true;
			}
		}
		
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
	
	private void tryToStopRobber(MyRobber mr) {
		robBank.playSound("RobABank.wav");
		mr.r.msgAttemptToStop(accountManager, false); //TODO, make success of stop attemp random
		mr.state = robberState.robbing;
	}
	
	private void removeRobber(MyRobber mr) {
		mr.state = robberState.done;
	}
	
	private void goToWork() {
		atWork = true;
		bankBuilding.changeOpenSign(true, startHour, endHour);
		doGoToDesk();
		acquireSemaphore(active);
	}
	
	private void sendToTeller(WaitingCustomer wc, TellerPosition tp) {
		Do(AlertTag.BANK, "sending customer to teller" + tellerPositions.size());
		wc.bc.msgGoToTeller(tp.xPos, tp.t);
		wc.state = customerState.inBank;
		tp.occupied = true;
	}
	
	private void goOffWork() {
		
		addPaycheckToWallet();
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
			this.deactivate();
			atWork = false;
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
	/*
	 * Called at end of work day
	 * adds to persons wallet
	 * 
	 */
	private void addPaycheckToWallet() {
		this.getPerson().getWallet().addCash(paycheckAmount);
	}
	
//	private void
	
	
	//ANIMATION #####################
	public void msgAtDestination() {
		active.release();
	}
	
	public void doGoToDesk() {
		securityGuardGui.DoGoToDesk();
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
	public void setGui(SecurityGuardGuiInterface sg) {
		securityGuardGui = sg;
	}
	

	// Accessors, etc.

	public int getWaitingCustomersSize() {
		return waitingCustomers.size();
	}
	
	public int getTellerPositionsSize() {
		return tellerPositions.size();
	}
	
	public void addRole(WorkRole r) {
		workRoles.add(r);
		if(r instanceof AccountManager) {
			accountManager = (AccountManager) r;//TODO Test, this is for robber to know who AM is
		}
	}
	
	private void acquireSemaphore(Semaphore s) {
		try {
			s.acquire();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	
	@Override
	public int getShiftStartHour() {
		return startHour;
	}

	@Override
	public int getShiftStartMinute() {
		return startMinute;
	}

	@Override
	public int getShiftEndHour() {
		return endHour;
	}

	@Override
	public int getShiftEndMinute() {
		return endMinute;
	}

	@Override
	public boolean isAtWork() {
		// TODO Auto-generated method stub
		return isActive();
	}

	@Override
	public boolean isOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}










	




}

