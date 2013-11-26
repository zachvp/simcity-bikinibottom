package housing;

import housing.interfaces.Dwelling;
import housing.interfaces.MaintenanceWorker;
import housing.interfaces.PayRecipient;
import housing.interfaces.Resident;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mock.EventLog;
import mock.MockScheduleTaskListener;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.ScheduleTask;
import agent.PersonAgent;
import agent.WorkRole;
import agent.interfaces.Person;

/**
 * PayRecipient is the generic money collector that charges
 * residents and tracks dues.
 * @author Zach VP
 *
 */
public class PayRecipientRole extends WorkRole implements PayRecipient {
	/* ----- Data ----- */
	public EventLog log = new EventLog();
	
	// for testing. Listens to the ScheduleTask 
	MockScheduleTaskListener listener = new MockScheduleTaskListener();
	
	// used to create time delays and schedule events
	ScheduleTask schedule = new ScheduleTask();
	
	/* --- Constants --- */
	// TODO when should shift end?
	private final int SHIFT_START_HOUR = 6;
	private final int SHIFT_START_MINUTE = 0;
	private final int SHIFT_END_HOUR = 12;
	private final int SHIFT_END_MINUTE = 0;
	
	
	// how long role waits before deactivating
	private final int IMPATIENCE_TIME = 7;
	
	enum TaskState { FIRST_TASK, NONE, DOING_TASK }
	TaskState task = TaskState.FIRST_TASK;
	
	
	// data for the worker
	MaintenanceWorkerRole worker;
	private List<MyBill> bills = Collections.synchronizedList(new ArrayList<MyBill>());
	
	
	// class MyBill from worker
	enum BillState { RECEIVED, PAYING, PAID }
	private class MyBill {
		double amount;
		BillState state;
		
		MyBill(double amount, MaintenanceWorkerRole role) {
			state = BillState.RECEIVED;
		}
	}
	
	/* ----- Resident Data ----- */
	private List<MyResident> residents = Collections.synchronizedList(new ArrayList<MyResident>());
	enum PaymentState { NONE, PAYMENT_DUE, PAYMENT_PENDING, PAYMENT_RECEIVED,
		PAYMENT_PAID, DONE, };
	
	// class is public for testing purposes
	public class MyResident {
		Dwelling dwelling;
		double owes;
		double hasPaid;
		PaymentState state;
		
		MyResident(Dwelling dwelling){
			this.dwelling = dwelling;
			state = PaymentState.NONE;
		}
		
		// setters and getters
		public void setDwelling(Dwelling dwelling) { this.dwelling = dwelling; }
		public double getOwes() { return owes; }
		public double getPaid() { return hasPaid; }
	}
	
	public PayRecipientRole(Person payRecipientPerson, CityLocation residence) {
		super(payRecipientPerson, residence);
		
		// ask everyone for rent
		Runnable command = new Runnable() {
			@Override
			public void run() {
				synchronized(residents) {
					for(MyResident mr : residents) {
						mr.state = PaymentState.PAYMENT_DUE;
						stateChanged();
					}
				}
			}
		};
		
		// every day at noon
		int hour = 12;
		int minute = 0;
		
		schedule.scheduleDailyTask(command, hour, minute);
	}
	
	public PayRecipientRole(PersonAgent payRecipientPerson) {
		super(payRecipientPerson);
	}

	/* ----- Messages ----- */
	public void msgHereIsPayment(double amount, Resident r) {
		MyResident mr = findResident(r);
		if(mr == null) { return; }
		mr.hasPaid = amount;
		mr.state = PaymentState.PAYMENT_RECEIVED;
		Do("Received message 'here is payment' " + amount + " from resident #" + mr.dwelling.getIDNumber());
		stateChanged();
	}
	
	public void msgServiceCharge(double charge, MaintenanceWorkerRole worker) {
		Do("Received bill for charge");
		this.worker = worker;
		bills.add(new MyBill(charge, worker));
		stateChanged();
	}

	/* ----- Scheduler ----- */
	public boolean pickAndExecuteAnAction() {
		
		if(task == TaskState.FIRST_TASK) {
			task = TaskState.NONE;
			return true;
		}
		
		// make payment(s) to the maintenance worker
		if(person.getWallet().getCashOnHand() > 0) {
			synchronized(bills){
				for(MyBill bill: bills){
					if(bill.state == BillState.RECEIVED){
						tryToMakePayment(bill);
						return true;
					}
				}
			}
		}
		
		
		// charge all residents after time of the month has passed
		synchronized(residents) {
			for(MyResident mr : residents){
				if(mr.state == PaymentState.PAYMENT_DUE) {
					chargeResident(mr);
					return true;
				}
			}
		}
		
		// see if the resident has paid the bill
		synchronized(residents) {
			for(MyResident mr : residents) {
				if(mr.state == PaymentState.PAYMENT_RECEIVED) {
					checkResidentPayment(mr);
					return true;
				}
			}
		}
		
		Runnable command = new Runnable() {
			@Override
			public void run() {
				deactivate();
			}
		};
		
		listener.taskFinished(schedule);
		schedule.scheduleTaskWithDelay(command, IMPATIENCE_TIME * Constants.MINUTE);
		
		return false;
	}

	/* ----- Actions ----- */
	private void tryToMakePayment(MyBill bill) {
		Do("Attempting to pay service charge");
		bill.state = BillState.PAYING;
		
		double cash = person.getWallet().getCashOnHand();
		
		if(cash >= bill.amount){
			worker.msgHereIsPayment(bill.amount);
			cash -= bill.amount;
			person.getWallet().setCashOnHand(cash);
			bill.state = BillState.PAID;
			bills.remove(bill);
		}
		
		else {
			Do("Not enough money to pay service charge.");
			person.getWallet().setMoneyNeeded(person.getWallet().getMoneyNeeded() + bill.amount);
		}
	}
	
	private void checkResidentPayment(MyResident mr) {
		Do("Checking payment for resident #" + mr.dwelling.getIDNumber());
		mr.owes -= mr.hasPaid;
		
		if(mr.owes == 0){
			mr.state = PaymentState.DONE;
			Do("Resident has paid in full, now owes " + mr.owes);
		}
		else if(mr.owes > 0) {
			Do("Resident #" + mr.dwelling.getIDNumber() + " still owes money!");
			mr.state = PaymentState.NONE;
		}
		// this should never happen
		else {
			Do("Resident overpaid! Money will go to next month.");
		}
	}
	
	public void chargeResident(MyResident mr){
		mr.state = PaymentState.PAYMENT_PENDING;
		mr.owes += mr.dwelling.getMonthlyPaymentAmount();
		
		mr.dwelling.getResident().msgPaymentDue(mr.dwelling.getMonthlyPaymentAmount());
		Do("Charged resident in unit #" + mr.dwelling.getIDNumber());
	}
	
	/* ----- Utilities ----- */
	private MyResident findResident(Resident r){
		for(MyResident mr : residents){
			if(mr.dwelling.getResident() == r){
				return mr;
			}
		}
		Do("Unable to find resident in list!");
		return null;
	}
	
	public void addResident(Dwelling dwelling){
		if(dwelling.getResident() != null){
			residents.add(new MyResident(dwelling));
			stateChanged();
		}
	}

	public List<MyResident> getResidents() {
		return residents;
	}

	public void setResidents(List<MyResident> residents) {
		this.residents = residents;
	}

	/* --- Abstract methods inherited from WorkRole --- */
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
		return false;
	}

	@Override
	public void msgLeaveWork() {
		deactivate();
	}
}
