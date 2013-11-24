package housing.test.mock;

import agent.mock.EventLog;
import housing.interfaces.Dwelling;
import housing.interfaces.PayRecipient;
import housing.interfaces.Resident;

public class MockDwelling implements Dwelling {
	EventLog log = new EventLog();
	
	// role info
	public Resident resident;
	public PayRecipient payRecipient;
	
	// hard dwelling data
	public double monthlyPaymentAmount;
	public int IDNumber;
	public String startCondition;
	public String condition;
	
	// constants
	public static int MAX_PAYMENT = 64;
	
	public MockDwelling(Resident resident, PayRecipient payRecipient, String condition) {
		this.resident = resident;
		this.payRecipient = payRecipient;
		
		condition.toLowerCase();
		if(condition.equals("good")) monthlyPaymentAmount = MAX_PAYMENT;
		if(condition.equals("fair")) monthlyPaymentAmount = MAX_PAYMENT * 0.75;
		if(condition.equals("poor")) monthlyPaymentAmount = MAX_PAYMENT * 0.5;
		if(condition.equals("broken")) monthlyPaymentAmount = MAX_PAYMENT * 0.25;
	}

	@Override
	public double getMonthlyPaymentAmount() { 
		return monthlyPaymentAmount; }

	@Override
	public Resident getResident() {
		return resident;
	}

	@Override
	public int getIDNumber() {
		return 0;
	}

	@Override
	public void setCondition(String condition) {
		condition.toLowerCase();
		
		if(condition.equals("good") || condition.equals("fair") ||
				condition.equals("poor") || condition.equals("broken")){
			this.condition = condition;
		}
		else log.add("Error! Cannot set Condition of Mock dwelling.");
	}

}
