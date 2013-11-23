package housing;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
	
	/* ----- Resident Data ----- */
	private List<MyResident> residents = Collections.synchronizedList(new ArrayList<MyResident>());
	enum PaymentState { NONE, PAYMENT_DUE, PAYMENT_PAID, DONE };
	
	private class MyResident{
		Dwelling dwelling;
		double residentOwes;
		double paymentAmount;// how much the resident has actually paid
		PaymentState state;
		
		MyResident(Dwelling dwelling){
			this.dwelling = dwelling;
			state = PaymentState.NONE;
		}
	}
	
	public PayRecipientRole(PersonAgent agent) {
		super(agent);
		Timer timer = person.getTimer();
		
		// ask everyone for rent
		TimerTask task = new TimerTask(){
			@Override
			public void run() {
				for(MyResident mr : residents){
					chargeResident(mr);
				}
			}
		};
		
		// at noon
		Date firstTime = new Date(TimeManager.getInstance().nextSuchTime(12, 0));
		
		// once every day
		int period = (int) Constants.DAY/TimeManager.CONVERSION_RATE;
		
		timer.schedule(task, firstTime, period);
	}
	
	/* ----- Messages ----- */
	public void msgHereIsPayment(double amount, Resident r) {
		MyResident mr = findResident(r);
		if(mr == null) return;
		mr.paymentAmount = amount;
		mr.state = PaymentState.PAYMENT_PAID;
		log.add("Received message 'here is payment' " + amount + " from " + mr.dwelling.getIDNumber());
		stateChanged();
	}

	/* ----- Scheduler ----- */
	protected boolean pickAndExecuteAnAction() {
		for(MyResident mr : residents){
			if(mr.state == PaymentState.PAYMENT_PAID){
				checkResidentPayment(mr);
				return true;
			}
		}
		return false;
	}

	/* ----- Actions ----- */
	private void checkResidentPayment(MyResident mr){
		log.add("Checking resident payment " + mr.dwelling.getIDNumber());
		mr.residentOwes -= mr.paymentAmount;
		if(mr.residentOwes == 0){
			mr.state = PaymentState.DONE;
			log.add("Resident has paid in full, now owes " + mr.residentOwes);
		}
		
	}
	private void chargeResident(MyResident r){
		r.residentOwes += r.dwelling.getMonthlyPaymentAmount();
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
}
