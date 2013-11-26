package bank.test;

import gui.Building;
import junit.framework.TestCase;
import CommonSimpleClasses.CityLocation;
import agent.PersonAgent;
import bank.TellerRole;
import bank.gui.BankBuilding;
import bank.test.mock.MockAccountManager;
import bank.test.mock.MockBankCustomer;
import bank.test.mock.MockLoanManager;

public class TellerTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.

	TellerRole teller;
	MockBankCustomer bankCustomer;
	MockAccountManager accountManager;// = new MockAccountManager("accountManager");
	MockLoanManager loanManager;

    Building testBankBuilding = new BankBuilding(0,0,0,0);

	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();  

		PersonAgent tellerPerson = new PersonAgent("testTeller");
		bankCustomer = new MockBankCustomer("mockBankCustomer");
		teller = new TellerRole(tellerPerson, testBankBuilding);
		teller.setAccountManager(accountManager);
        teller.setLoanManager(loanManager);
        teller.setDeskPosition(0);
		loanManager = new MockLoanManager("mockLoanManager");
		accountManager = new MockAccountManager("mockAM");
		
		teller.setAccountManager(accountManager);
		teller.setLoanManager(loanManager);
	}        
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */

	public void testCreateNewAccount() {
		double initialDeposit = 20;
		int IdNumber = 1001;
		
		assertEquals(teller.getMyCustomers().size(), 0);
		
		teller.msgIWantToOpenAccount(bankCustomer, initialDeposit);
		assertEquals(teller.getMyCustomers().size(), 1);
		assertEquals(teller.getMyCustomers().get(0).getBankCustomer(), bankCustomer);
		assertEquals(teller.getMyCustomers().get(0).getDeposit(), initialDeposit);
		
		assertTrue("Scheduler not called", teller.pickAndExecuteAnAction());
//		assertEquals("MyCustomer state not updated", teller.getMyCustomers().get(0).state, CustomerState.waitingForAccountManager);
		assertTrue("message not received", accountManager.log.containsString("new account"+ initialDeposit));
		
		teller.msgNewAccountVerified(bankCustomer, IdNumber);
		assertEquals(teller.getMyCustomers().size(), 1);
		assertTrue("Scheduler not called", teller.pickAndExecuteAnAction());
		assertEquals(teller.getMyCustomers().size(), 0);
		assertTrue("message not received", bankCustomer.log.containsString("new account" + IdNumber));
		
	}
	
	public void testDepositMoney() {
		double depositAmount = 20;
		int accountId = 1001;
		
		assertEquals(teller.getMyCustomers().size(), 0);

		teller.msgDepositMoney(bankCustomer, accountId , depositAmount);
		assertEquals(teller.getMyCustomers().size(), 1);
		assertEquals(teller.getMyCustomers().get(0).getBankCustomer(), bankCustomer);
		assertEquals(teller.getMyCustomers().get(0).getDeposit(), depositAmount);
		
		assertTrue("Scheduler not called", teller.pickAndExecuteAnAction());
//		assertEquals("MyCustomer state not updated", teller.getMyCustomers().get(0).state, CustomerState.waitingForAccountManager);
		assertTrue("message not received", accountManager.log.containsString("deposit"+ depositAmount));

		teller.msgDepositSuccessful(bankCustomer, accountId, depositAmount);
		assertEquals(teller.getMyCustomers().size(), 1);
		assertEquals(teller.getMyCustomers().get(0).getBankCustomer(), bankCustomer);
		assertEquals(teller.getMyCustomers().get(0).getDeposit(), depositAmount);
		
		assertTrue("Scheduler not called", teller.pickAndExecuteAnAction());
	
		assertTrue("message not received", bankCustomer.log.containsString("deposited"+ depositAmount));

		assertEquals(teller.getMyCustomers().size(), 0);
	}
	
	public void testSendToLoanManager() {
		assertEquals(teller.getMyCustomers().size(), 0);

		teller.msgINeedALoan(bankCustomer);
		assertEquals(teller.getMyCustomers().size(), 1);
		assertEquals(teller.getMyCustomers().get(0).getBankCustomer(), bankCustomer);
		
		assertTrue("Scheduler not called", teller.pickAndExecuteAnAction());

		assertTrue("message not received", bankCustomer.log.containsString("sent to loanmanager"));
		assertEquals(teller.getMyCustomers().size(), 0);

	}
	
	public void testWithdraw() {
		int accountId = 1001;
		double withdrawAmount = 20;
		
		assertEquals(teller.getMyCustomers().size(), 0);

		teller.msgWithdrawMoney(bankCustomer, accountId, withdrawAmount);
		assertEquals(teller.getMyCustomers().size(), 1);
		assertEquals(teller.getMyCustomers().get(0).getBankCustomer(), bankCustomer);
		assertEquals(teller.getMyCustomers().get(0).getWithdrawal(), withdrawAmount);
		
		assertTrue("Scheduler not called", teller.pickAndExecuteAnAction());
		assertTrue("message not received", accountManager.log.containsString("withdraw"+ withdrawAmount));

		teller.msgWithdrawSuccessful(bankCustomer, accountId, withdrawAmount);
		assertEquals(teller.getMyCustomers().size(), 1);
		assertEquals(teller.getMyCustomers().get(0).getBankCustomer(), bankCustomer);
		assertEquals(teller.getMyCustomers().get(0).getWithdrawal(), withdrawAmount);
	
		assertTrue("Scheduler not called", teller.pickAndExecuteAnAction());
		
		assertTrue("message not received", bankCustomer.log.containsString("withdraw"+ withdrawAmount));

		assertEquals(teller.getMyCustomers().size(), 0);

	}

}