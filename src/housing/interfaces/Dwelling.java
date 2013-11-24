package housing.interfaces;

public interface Dwelling {

	double getMonthlyPaymentAmount();

	Resident getResident();

	int getIDNumber();

	void setCondition(String string);

}
