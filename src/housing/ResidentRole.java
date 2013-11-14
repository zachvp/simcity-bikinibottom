package housing;

import housing.interfaces.Resident;
import agent.Role;

public class ResidentRole extends Role implements Resident {

	public ResidentRole() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void msgPaymentDue(double amount, int accountNumber) {
		// TODO Auto-generated method stub
		
	}

}
