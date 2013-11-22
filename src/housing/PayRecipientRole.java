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

public class PayRecipientRole extends Role implements PayRecipient {
	/* ----- Data ----- */
	public EventLog log = new EventLog();
	
	/* ----- Constants -----*/
	private final double RENT_AMOUNT = 5;
	
	/* ----- Resident Data ----- */
	private List<MyResident> residents = Collections.synchronizedList(new ArrayList<MyResident>());
	enum PaymentState { PAYMENT_DUE, PAYMENT_PAID, DONE };
	private class MyResident{
		Resident resident;
		int unitNumber;
		double monthlyPaymentRate;//how much resident must pay every month
		double residentOwes;//how much the resident owes
		double paymentAmount;//how much the resident actually paid
		PaymentState state;
		
		MyResident(Resident resident, ){
			
		}
	}
	
	public PayRecipientRole(PersonAgent agent) {
		super(agent);
		Timer timer = person.getTimer();
		
		// ask everyone for rent
		TimerTask task = new TimerTask(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for(MyResident r : residents){
					r.resident.msgPaymentDue(RENT_AMOUNT);
				}
			}
		};
		
		// at noon
		Date firstTime = new Date(TimeManager.getInstance().nextSuchTime(6, 0));
		
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
		log.add("Received message 'here is payment' " + amount + " from " + mr.unitNumber);
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
		//TODO implement actual time mechanic
//		if(globalTime%monthlyTime == 0){
			for(MyResident mr : residents){
				chargeResident(mr);
				return true;
			}
//		}
		return false;
	}

	/* ----- Actions ----- */
	private void checkResidentPayment(MyResident mr){
		log.add("Checking resident payment " + mr.unitNumber);
		mr.residentOwes -= mr.paymentAmount;
		if(mr.residentOwes == 0){
			mr.state = PaymentState.DONE;
			log.add("Resident has paid in full, now owes " + mr.residentOwes);
		}
		
	}
	private void chargeResident(MyResident r){
		r.residentOwes += r.monthlyPaymentRate;
		r.state = PaymentState.PAYMENT_DUE;
		r.resident.msgPaymentDue(r.monthlyPaymentRate);
		log.add("Charged resident " + r.unitNumber);
	}
	
	/* ----- Utilities ----- */
	private MyResident findResident(Resident r){
		for(MyResident mr : residents){
			if(mr.resident == r){
				return mr;
			}
		}
		log.add("Unable to find resident in list!");
		return null;
	}
	
	public void addResident(Resident resident){
		residents.add(new MyResident(resident));
	}

	@Override
	public String getRoleType() {
		return "PayRecipientRole";
	}
}
