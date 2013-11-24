package housing;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import housing.interfaces.Dwelling;
import housing.interfaces.PayRecipient;
import housing.interfaces.Resident;
import agent.mock.EventLog;
import agent.Constants;
import agent.PersonAgent;
import agent.Role;
import agent.TimeManager;

/**
 * PayRecipient is the generic money collector that charges
 * residents and tracks dues.
 * @author Zach VP
 *
 */
public class PayRecipientRole extends Role implements PayRecipient {
	/* ----- Data ----- */
	public EventLog log = new EventLog();
	ScheduleTask task = new ScheduleTask();
	
	/* ----- Resident Data ----- */
	private List<MyResident> residents = Collections.synchronizedList(new ArrayList<MyResident>());
	enum PaymentState { NONE, PAYMENT_DUE, PAYMENT_PAID, DONE, PAYMENT_RECEIVED };
	
	private class MyResident{
		Dwelling dwelling;
		double owes;
		double hasPaid;
		PaymentState state;
		
		MyResident(Dwelling dwelling){
			this.dwelling = dwelling;
			state = PaymentState.NONE;
		}
	}
	
	public PayRecipientRole(PersonAgent agent) {
		super(agent);
		
		// ask everyone for rent
		Runnable command = new Runnable(){
			@Override
			public void run() {
				for(MyResident mr : residents){
					chargeResident(mr);
				}
			}
		};
		
		// every day at noon
		int hour = 6;
		int minute = 5;
		
		task.scheduleDailyTask(command, hour, minute);
	}
	
	/* ----- Messages ----- */
	public void msgHereIsPayment(double amount, Resident r) {
		MyResident mr = findResident(r);
		if(mr == null) return;
		mr.hasPaid = amount;
		mr.state = PaymentState.PAYMENT_RECEIVED;
		log.add("Received message 'here is payment' " + amount + " from " + mr.dwelling.getIDNumber());
		stateChanged();
	}

	/* ----- Scheduler ----- */
	protected boolean pickAndExecuteAnAction() {
		for(MyResident mr : residents){
			if(mr.state == PaymentState.PAYMENT_RECEIVED){
				checkResidentPayment(mr);
				return true;
			}
		}
		return false;
	}

	/* ----- Actions ----- */
	private void checkResidentPayment(MyResident mr){
		log.add("Checking resident payment " + mr.dwelling.getIDNumber());
		mr.owes -= mr.hasPaid;
		
		if(mr.owes == 0){
			mr.state = PaymentState.DONE;
			log.add("Resident has paid in full, now owes " + mr.owes);
		}
		else if(mr.owes > 0) {
			log.add("Resident " + mr.dwelling.getIDNumber() + " still owes money!");
			mr.state = PaymentState.PAYMENT_DUE;
		}
		// this should never happen
		else {
			log.add("Resident overpaid! Money will go to next month.");
		}
	}
	
	private void chargeResident(MyResident r){
		r.owes += r.dwelling.getMonthlyPaymentAmount();
		r.state = PaymentState.PAYMENT_DUE;
		r.dwelling.getResident().msgPaymentDue(r.dwelling.getMonthlyPaymentAmount());
		log.add("Charged resident " + r.dwelling.getIDNumber());
	}
	
	/* ----- Utilities ----- */
	private MyResident findResident(Resident r){
		for(MyResident mr : residents){
			if(mr.dwelling.getResident() == r){
				return mr;
			}
		}
		log.add("Unable to find resident in list!");
		return null;
	}
	
	public void addResident(Dwelling dwelling){
		residents.add(new MyResident(dwelling));
		stateChanged();
	}

	public List<MyResident> getResidents() {
		return residents;
	}

	public void setResidents(List<MyResident> residents) {
		this.residents = residents;
	}
}
