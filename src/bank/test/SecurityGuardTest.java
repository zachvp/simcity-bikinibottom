package bank.test;

import junit.framework.TestCase;
import CommonSimpleClasses.CityLocation;
import agent.PersonAgent;
import agent.WorkRole;
import bank.SecurityGuardRole;
import bank.TellerRole;
import bank.gui.BankBuilding;
import bank.interfaces.AccountManager;
import bank.interfaces.LoanManager;
import bank.interfaces.SecurityGuard;
import bank.interfaces.Teller;
import bank.test.mock.MockAccountManager;
import bank.test.mock.MockBankCustomer;
import bank.test.mock.MockLoanManager;
import bank.test.mock.MockSecurityGuard;
import bank.test.mock.MockSecurityGuardGui;
import bank.test.mock.MockTeller;
import bank.test.mock.MockTellerGui;

public class SecurityGuardTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.

	SecurityGuardRole sg;
	MockBankCustomer bankCustomer;
	AccountManager accountManager;// = new MockAccountManager("accountManager");
	LoanManager loanManager;
	Teller teller;
//	SecurityGuard securityGuard;
	MockSecurityGuardGui mockGui;

	CityLocation testLocation = new BankBuilding(0,0,0,0);

	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();  

		PersonAgent sgPerson = new PersonAgent("testTeller");
		bankCustomer = new MockBankCustomer("mockBankCustomer");
		sg = new SecurityGuardRole(sgPerson, testLocation) ;
		bankCustomer = new MockBankCustomer("bankCust");
		loanManager = new MockLoanManager("mockLoanManager");
		accountManager = new MockAccountManager("mockAM");
		teller = new MockTeller("teller");
		mockGui = new MockSecurityGuardGui(sg);
		
		sg.setGui(mockGui);
//		sg.addRole((WorkRole) loanManager);
//		sg.addRole((WorkRole) accountManager);
//		sg.addRole((WorkRole) teller);
		sg.addTeller(teller, 0);
		
		
//		securityGuard = new MockSecurityGuard("mocksecurityguard");
		
		
	}        
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */

	public void testInitialize() {
		assertEquals("waitingcustomers list wrong size", sg.getWaitingCustomersSize(), 0);
		assertEquals("tellerpositions list wrong size", sg.getTellerPositionsSize(), 1);

	}
	
	public void testWelcomeCustomerToBank() {
		
		
		sg.msgCustomerArrived(bankCustomer);
		assertEquals("waitingcustomers list wrong size", sg.getWaitingCustomersSize(), 1);
		assertTrue("scheduler not returning", sg.pickAndExecuteAnAction());
		//send to teller action called
		assertTrue("message sent", bankCustomer.log.containsString("received" + teller));
		
	}
	
}