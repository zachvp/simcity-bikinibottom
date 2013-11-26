package housing.interfaces;

import CommonSimpleClasses.Constants.Condition;

public interface Dwelling {

	double getMonthlyPaymentAmount();

	Resident getResident();

	int getIDNumber();

	void setCondition(Condition condition);

	Condition getCondition();

	MaintenanceWorker getWorker();

	void setPayRecipient(PayRecipient payRecipient);

}
