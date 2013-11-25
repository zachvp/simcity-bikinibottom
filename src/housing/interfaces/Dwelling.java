package housing.interfaces;

import agent.Constants.Condition;

public interface Dwelling {

	double getMonthlyPaymentAmount();

	Resident getResident();

	int getIDNumber();

	void setCondition(Condition condition);

	Condition getCondition();

}
