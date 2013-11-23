package housing;

import housing.interfaces.Resident;
import housing.interfaces.PayRecipient;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.XYPos;

public class Dwelling implements CityBuilding {
	/* --- Data --- */
	private Resident resident;
	private PayRecipient payRecipient;
	private double monthlyPaymentAmount;
	
	// example: Apartment unit number or house address
	private int IDNumber;

	// Tracks the deterioration of the building
	enum Condition { GOOD, FAIR, POOR, BROKEN }
	Condition condition = Condition.GOOD;
	
	
	public Dwelling(Resident resident, PayRecipient payRecipient) {
		super();
		this.resident = resident;
		this.payRecipient = payRecipient;
	}

	/* --- From Super Class --- */
	@Override
	public LocationTypeEnum type() {
		return null;
	}

	@Override
	public XYPos position() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XYPos entrancePos() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getIDNumber() {
		return IDNumber;
	}

	public void setIDNumber(int iDNumber) {
		IDNumber = iDNumber;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public PayRecipient getPayRecipient() {
		return payRecipient;
	}

	public void setPayRecipient(PayRecipient payRecipient) {
		this.payRecipient = payRecipient;
	}

	public double getMonthlyPaymentAmount() {
		return monthlyPaymentAmount;
	}

	public void setMonthlyPaymentAmount(double monthlyPaymentAmount) {
		this.monthlyPaymentAmount = monthlyPaymentAmount;
	}

}
