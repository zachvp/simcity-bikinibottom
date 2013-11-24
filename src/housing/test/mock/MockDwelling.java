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
	
	public MockDwelling(Resident resident, PayRecipient payRecipient) {
		this.resident = resident;
		this.payRecipient = payRecipient;
	}

	@Override
	public double getMonthlyPaymentAmount() { 
		return monthlyPaymentAmount; }

	@Override
	public Resident getResident() {
		return null;
	}

	@Override
	public int getIDNumber() {
		return 0;
	}

}
