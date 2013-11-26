package housing.interfaces;

import housing.PayRecipientRole;
import CommonSimpleClasses.Constants.Condition;

public interface Dwelling {

	double getMonthlyPaymentAmount();

	Resident getResident();

	int getIDNumber();

	void setCondition(Condition condition);

	Condition getCondition();

	MaintenanceWorker getWorker();

	void setPayRecipient(PayRecipientRole payRecipient);
	
	public PayRecipient getPayRecipient();

}
