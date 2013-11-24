package housing.test.mock;

import housing.interfaces.Dwelling;
import housing.interfaces.PayRecipient;
import housing.interfaces.Resident;

public class MockDwelling implements Dwelling {
	public Resident resident;
	public PayRecipient payRecipient;
	public double monthlyPaymentAmount;
	public int IDNumber;
	public String startCondition;
	
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

}
