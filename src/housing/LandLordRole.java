package housing;

import housing.ResidentRole.FoodState;
import housing.interfaces.Landlord;
import housing.interfaces.Resident;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import agent.PersonAgent;
import agent.Role;
import agent.mock.EventLog;

public class LandLordRole extends Role implements Landlord {
	/** DATA */
	public EventLog log = new EventLog();
	/* ----- Person Data ----- */
	PersonAgent person;
	
	/* ----- Temporary Hacks ----- */
	//TODO: un-hack these
	
	/* ----- Rent Data ----- */
	
	/* ----- Food Data ----- */
	
	/* ----- Constant Variables ----- */

	
	/* ----- Class Data ----- */

	
	public LandLordRole(PersonAgent pa) {
		super();
		this.person = pa;
	}
	
	/* ----- Messages ----- */
	public void msgFileWorkOrder(Resident complainant, int unitNumber, String problemType) {
		
	}

	/* ----- Scheduler ----- */
	@Override
	protected boolean pickAndExecuteAnAction() {
		
		return false;
	}

	@Override
	public String getRoleType() {
		return "LandlordRole";
	}

	/* ----- Actions ----- */

	
	/* ----- Utility Functions ----- */
	
}
