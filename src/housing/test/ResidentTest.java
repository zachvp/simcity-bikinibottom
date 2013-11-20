package housing.test;

import housing.PayRecipientRole;
import housing.ResidentRole;
import junit.framework.TestCase;

public class ResidentTest extends TestCase {
	/* --- Testing Roles and Agents --- */
//	PersonAgent person;
	ResidentRole resident;
	PayRecipientRole payRecipient;
	
	protected void setUp() throws Exception {
		super.setUp();
		
//		resident = new ResidentRole("Resident");
//		payRecipient = new PayRecipient("Pay Recipient");
	}
	
	/** Tests the simplest case of the resident paying */
	public void normativeCaseResidentPaysDues(){
		/** Test Preconditions */
		//moneyOwed should be 0
	}
	/** Tests the simplest resident cooking and eating case */
	public void normativeCookAndEat(){
		/** Test Preconditions */
		//food should be null
		//hunger level should be 0
		//hungerThreshold should be 100
		//eatTime should be 8
	}
}
