package housing.interfaces;

import housing.backend.ResidentRole;
import CommonSimpleClasses.Constants.Condition;

public interface Dwelling {

	double getMonthlyPaymentAmount();

	Resident getResident();

	int getIDNumber();

	void setCondition(Condition condition);

	Condition getCondition();

	MaintenanceWorker getWorker();

	void setPayRecipient(PayRecipient payRecipient);
	
	public PayRecipient getPayRecipient();

	public void degradeCondition();

	void setWorker(MaintenanceWorker worker);

	void setResident(ResidentRole resident);

}
