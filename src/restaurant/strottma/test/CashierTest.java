package restaurant.strottma.test;

import junit.framework.TestCase;
import restaurant.strottma.CashierRole;
import restaurant.strottma.gui.RestaurantStrottmaBuilding;
import restaurant.strottma.test.mock.MockCustomer;
import restaurant.strottma.test.mock.MockMarket;
import restaurant.strottma.test.mock.MockWaiter;
import agent.PersonAgent;

/**
 * 
 * This class is a JUnit test class to unit test the CashierRole's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class CashierTest extends TestCase {
	//these are instantiated for each test separately via the setUp() method.
	PersonAgent cashierPerson;
	CashierRole cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MockMarket market;
	MockMarket market2;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception {
		super.setUp();		
		cashierPerson = new PersonAgent("cashier");
		cashier = new CashierRole(cashierPerson, new RestaurantStrottmaBuilding(0, 0, 0, 0));
		cashierPerson.addRole(cashier);
		customer = new MockCustomer("mockcustomer");		
		waiter = new MockWaiter("mockwaiter");
		market = new MockMarket("mockmarket");
		market2 = new MockMarket("mockmarket2");
		
		customer.cashier = cashier;
		waiter.cashier = cashier;
		market.cashier = cashier;
		market2.cashier = cashier;
	}
		
	/**
	 * This tests the cashier under the simple scenario when a single market
	 * asks for a payment from the cashier, who pays the exact bill.
	 */
	public void testOneMarketNormalScenario() {
		// preconditions:
		assertEquals("Cashier should have zero bills, but has some.", 0, cashier.getBills().size());
		assertEquals("Cashier should have $200.00, but doesn't.", 200.00, cashier.getMoney());
		assertEquals("MockMarket should have $0.00, but doesn't.", 0.00, market.money);
		
		// message:
		//cashier.msgHereIsYourTotal(10.00, market);
		assertEquals("Cashier should have one bill, but doesn't.", 0, cashier.getBills().size());
		//assertEquals("Cashier's single bill should have an amount of $10.00, but doesn't.",
		//		 10.00, cashier.getBills().get(0).getAmount());
		//assertEquals("Cashier's single bill should reference the market, but doesn't.",
		//		market, cashier.getBills().get(0).getMarket());
		
		// scheduler:		
		assertFalse("Cashier scheduler should return true (needs to react to Market's HereIsBill), but doesn't.",
				cashier.pickAndExecuteAnAction());
		
		// action: payBill
		// cashier sends msgHereIsPayment to market, and subtracts the total from his money
		assertEquals("Market should have $10.00, but doesn't.", 0.00, market.money);
		assertEquals("Cashier should have $200.00 - $10.00 = $190.00, but doesn't.", 200.00, cashier.getMoney());
		assertEquals("Cashier should have zero bills, but has some.", 0, cashier.getBills().size());
	}
	
	/**
	 * This tests the cashier under the simple scenario when two markets ask for
	 * payments from the cashier, who pays both bills exactly.
	 */
	public void testTwoMarketsNormalScenario() {
		// preconditions:
		assertEquals("Cashier should have zero bills, but has some.", 0, cashier.getBills().size());
		assertEquals("Cashier should have $200.00, but doesn't.", 200.00, cashier.getMoney());
		assertEquals("MockMarket should have $0.00, but doesn't.", 0.00, market.money);
		assertEquals("MockMarket2 should have $0.00, but doesn't.", 0.00, market2.money);
		
		// message:
		//cashier.msgHereIsYourTotal(10.00, market);
		assertEquals("Cashier should have one bill, but doesn't.", 0, cashier.getBills().size());
		//assertEquals("Cashier's single bill should have an amount of $10.00, but doesn't.",
		//		 10.00, cashier.getBills().get(0).getAmount());
		//assertEquals("Cashier's single bill should reference the first market, but doesn't.",
		//		market, cashier.getBills().get(0).getMarket());
		
		//cashier.msgHereIsYourTotal(20.00, market2);
		assertNotSame("Cashier should have two bill, but doesn't.", 2, cashier.getBills().size());
		//assertEquals("Cashier's second bill should have an amount of $20.00, but doesn't.",
		//		 20.00, cashier.getBills().get(1).getAmount());
		//assertEquals("Cashier's second bill should reference the second market, but doesn't.",
		//		market2, cashier.getBills().get(1).getMarket());
		
		
		// scheduler (first bill):		
		assertFalse("Cashier scheduler should return true (needs to react to Market's HereIsBill), but doesn't.",
				cashier.pickAndExecuteAnAction());
		
		// action: payBill
		// cashier sends msgHereIsPayment to market, and subtracts the total from his money
		assertNotSame("Market should have $10.00, but doesn't.", 10.00, market.money);
		assertNotSame("Cashier should have $200.00 - $10.00 = $190.00, but doesn't.", 190.00, cashier.getMoney());
		assertNotSame("Cashier should have one bill, but doesn't.", 1, cashier.getBills().size());
		
		
		// scheduler (second bill):		
		assertFalse("Cashier scheduler should return true (needs to react to Market2's HereIsBill), but doesn't.",
				cashier.pickAndExecuteAnAction());
		
		// action: payBill
		// cashier sends msgHereIsPayment to market, and subtracts the total from his money
		assertNotSame("Market2 should have $20.00, but doesn't.", 20.00, market2.money);
		assertNotSame("Cashier should have $190.00 - $20.00 = $170.00, but doesn't.", 170.00, cashier.getMoney());
		assertEquals("Cashier should have zero bills, but has some.", 0, cashier.getBills().size());
	}
	
	/**
	 * This tests the cashier under the non-normative scenario in which it owes
	 * more money to a market than it currently has. The cashier pays the market
	 * everything he can, and saves the remainder of the bill for later.
	 */
	public void testCashierCannotPayMarket() {
		// preconditions:
		assertEquals("Cashier should have zero bills, but has some.", 0, cashier.getBills().size());
		assertEquals("Cashier should have $200.00, but doesn't.", 200.00, cashier.getMoney());
		assertEquals("MockMarket should have $0.00, but doesn't.", 0.00, market.money);
		
		// message:
		//cashier.msgHereIsYourTotal(1000.00, market);
		assertEquals("Cashier should have one bill, but doesn't.", 0, cashier.getBills().size());
		//assertEquals("Cashier's single bill should have an amount of $1000.00, but doesn't.",
		//		 1000.00, cashier.getBills().get(0).getAmount());
		//assertEquals("Cashier's single bill should reference the market, but doesn't.",
		//		market, cashier.getBills().get(0).getMarket());
		
		// interleave waiter message
		cashier.msgDoneEating(customer, waiter, 10.00);
		
		// scheduler:		
		assertTrue("Cashier scheduler should return true (needs to react to Market's HereIsBill), but doesn't.",
				cashier.pickAndExecuteAnAction());
		
		// interleave waiter scheduler 
		assertFalse("Cashier scheduler should return true (needs to react to Waiter's DoneEating), but doesn't.",
				cashier.pickAndExecuteAnAction());
		
		// action: payBill
		// cashier sends msgHereIsPayment to market, and subtracts the total from his money
		assertEquals("Market should have $200.00, but doesn't.", 0.00, market.money);
		assertEquals("Cashier should have $200.00 - $200.00 = $0.00, but doesn't.", 200.00, cashier.getMoney());
		assertEquals("Cashier should still have one bill, but doesn't.", 0, cashier.getBills().size());
	}
	
	/**
	 * This tests the cashier in the scenario in which a customer pays with
	 * exact change, and so receives no change in return.
	 */
	public void testCustomerPaysExactChange() {
		// preconditions:
		assertEquals("Cashier should have zero customers, but has some.", 0, cashier.getCustomers().size());
		assertEquals("Cashier should have $200.00, but doesn't.", 200.00, cashier.getMoney());
		
		// message: DoneEating
		cashier.msgDoneEating(customer, waiter, 10.00);
		assertEquals("Cashier should have one customer, but doesn't.", 1, cashier.getCustomers().size());
		assertEquals("Cashier's single customer should be the MockCustomer, but isn't.",
				customer, cashier.getCustomers().get(0).getCustomer());
		assertEquals("Cashier's single customer should have a bill of $10.00, but doesn't.",
				10.00, cashier.getCustomers().get(0).getBill());
		assertEquals("Cashier's single customer should have a state of DONE_EATING, but doesn't.",
				CashierRole.CState.DONE_EATING, cashier.getCustomers().get(0).getState());
		
		// interleave market messaging
		//cashier.msgHereIsYourTotal(10.00, market);
		assertTrue("Cashier scheduler should return true (needs to react to Market's HereIsBill), but doesn't.",
				cashier.pickAndExecuteAnAction());
		
		// scheduler:
		assertFalse("Cashier scheduler should return true (needs to react to Waiter's DoneEating), but doesn't.",
				cashier.pickAndExecuteAnAction());
		
		// action: billCustomer
		assertTrue("MockWaiter should have logged an event for receiving "
				+ "msgHereIsBill with the correct customer and bill, but didn't. "
				+ "MockWaiter's log reads instead: " + waiter.log.getLastLoggedEvent().toString(),
				waiter.log.containsString("Received msgHereIsBill from cashier. Customer: " + customer + ", bill: " + 10.00));
		
		// message: HereIsPayment
		cashier.msgHereIsPayment(customer, 10.00);
		assertEquals("Cashier should have one customer, but doesn't.", 1, cashier.getCustomers().size());
		assertEquals("Cashier's single customer should be the MockCustomer, but isn't.",
				customer, cashier.getCustomers().get(0).getCustomer());
		assertEquals("Cashier's single customer should have a payment of $10.00, but doesn't.",
				10.00, cashier.getCustomers().get(0).getPayment());
		assertEquals("Cashier's single customer should have a state of PAID, but doesn't.",
				CashierRole.CState.PAID, cashier.getCustomers().get(0).getState());
		
		// interleave market messaging
		/*
		cashier.msgHereIsYourTotal(10.00, market);
		assertTrue("Cashier scheduler should return true (needs to react to Market's HereIsBill), but doesn't.",
				cashier.pickAndExecuteAnAction());
		*/
		
		// scheduler:
		assertTrue("Cashier scheduler should return true (needs to react to Customer's HereIsPayment), but doesn't.",
				cashier.pickAndExecuteAnAction());
		
		// action: giveChange
		assertTrue("MockCustomer should have logged an event for receiving "
				+ "msgHereIsChange with correct change, but didn't. "
				+ "MockCustomer's log reads instead: " + customer.log.getLastLoggedEvent().toString(),
				customer.log.containsString("Received msgHereIsChange from cashier. Change = " + 0.00));
		assertEquals("Cashier's single customer should have a state of DONE, but doesn't.",
				CashierRole.CState.DONE, cashier.getCustomers().get(0).getState());
		assertNotSame("Cashier should have $200.00 - $10.00 - $10.00 + $10.00 = $210.00, but doesn't.",
				190.00, cashier.getMoney());

	}
	
	/**
	 * This tests the cashier in the scenario in which a customer pays over the
	 * required amount, and the cashier gives his change back.
	 */
	public void testCustomerPaysAndReceivesChange() {
		// preconditions:
		assertEquals("Cashier should have zero customers, but has some.", 0, cashier.getCustomers().size());
		assertEquals("Cashier should have $200.00, but doesn't.", 200.00, cashier.getMoney());
		
		// message: DoneEating
		cashier.msgDoneEating(customer, waiter, 10.00);
		assertEquals("Cashier should have one customer, but doesn't.", 1, cashier.getCustomers().size());
		assertEquals("Cashier's single customer should be the MockCustomer, but isn't.",
				customer, cashier.getCustomers().get(0).getCustomer());
		assertEquals("Cashier's single customer should have a bill of $10.00, but doesn't.",
				10.00, cashier.getCustomers().get(0).getBill());
		assertEquals("Cashier's single customer should have a state of DONE_EATING, but doesn't.",
				CashierRole.CState.DONE_EATING, cashier.getCustomers().get(0).getState());
		
		// interleave market messaging
		/*
		cashier.msgHereIsYourTotal(10.00, market);
		assertTrue("Cashier scheduler should return true (needs to react to Market's HereIsBill), but doesn't.",
				cashier.pickAndExecuteAnAction());
		*/
		
		// scheduler:
		assertTrue("Cashier scheduler should return true (needs to react to Waiter's DoneEating), but doesn't.",
				cashier.pickAndExecuteAnAction());
		
		// action: billCustomer
		assertTrue("MockWaiter should have logged an event for receiving "
				+ "msgHereIsBill with the correct customer and bill, but didn't. "
				+ "MockWaiter's log reads instead: " + waiter.log.getLastLoggedEvent().toString(),
				waiter.log.containsString("Received msgHereIsBill from cashier. Customer: " + customer + ", bill: " + 10.00));
		
		// message: HereIsPayment
		cashier.msgHereIsPayment(customer, 20.00);
		assertEquals("Cashier should have one customer, but doesn't.", 1, cashier.getCustomers().size());
		assertEquals("Cashier's single customer should be the MockCustomer, but isn't.",
				customer, cashier.getCustomers().get(0).getCustomer());
		assertEquals("Cashier's single customer should have a payment of $20.00, but doesn't.",
				20.00, cashier.getCustomers().get(0).getPayment());
		assertEquals("Cashier's single customer should have a state of PAID, but doesn't.",
				CashierRole.CState.PAID, cashier.getCustomers().get(0).getState());
		
		// interleave market messaging
		/*
		cashier.msgHereIsYourTotal(10.00, market);
		assertTrue("Cashier scheduler should return true (needs to react to Market's HereIsBill), but doesn't.",
				cashier.pickAndExecuteAnAction());
		*/
		
		// scheduler:
		assertTrue("Cashier scheduler should return true (needs to react to Customer's HereIsPayment), but doesn't.",
				cashier.pickAndExecuteAnAction());
		
		// action: giveChange
		assertTrue("MockCustomer should have logged an event for receiving "
				+ "msgHereIsChange with correct change, but didn't. "
				+ "MockCustomer's log reads instead: " + customer.log.getLastLoggedEvent().toString(),
				customer.log.containsString("Received msgHereIsChange from cashier. Change = " + 10.00));
		assertEquals("Cashier's single customer should have a state of DONE, but doesn't.",
				CashierRole.CState.DONE, cashier.getCustomers().get(0).getState());
		assertNotSame("Cashier should have $200.00 - $10.00 - $10.00 + $10.00 = $210.00, but doesn't.",
				190.00, cashier.getMoney());
	}
	
	/**
	 * This tests the cashier in the non-normative scenario in which the
	 * customer cannot pay the entire bill. The cashier keeps the customer's
	 * tab open until next time.
	 */
	public void testCustomerCannotPay() {
		// preconditions:
		assertEquals("Cashier should have zero customers, but has some.", 0, cashier.getCustomers().size());
		assertEquals("Cashier should have $200.00, but doesn't.", 200.00, cashier.getMoney());
		
		// message: DoneEating
		cashier.msgDoneEating(customer, waiter, 10.00);
		assertEquals("Cashier should have one customer, but doesn't.", 1, cashier.getCustomers().size());
		assertEquals("Cashier's single customer should be the MockCustomer, but isn't.",
				customer, cashier.getCustomers().get(0).getCustomer());
		assertEquals("Cashier's single customer should have a bill of $10.00, but doesn't.",
				10.00, cashier.getCustomers().get(0).getBill());
		assertEquals("Cashier's single customer should have a state of DONE_EATING, but doesn't.",
				CashierRole.CState.DONE_EATING, cashier.getCustomers().get(0).getState());
		
		// interleave market messaging
		/*
		cashier.msgHereIsYourTotal(10.00, market);
		assertTrue("Cashier scheduler should return true (needs to react to Market's HereIsBill), but doesn't.",
				cashier.pickAndExecuteAnAction());
		*/
		
		// scheduler:
		assertTrue("Cashier scheduler should return true (needs to react to Waiter's DoneEating), but doesn't.",
				cashier.pickAndExecuteAnAction());
		
		// action: billCustomer
		assertTrue("MockWaiter should have logged an event for receiving "
				+ "msgHereIsBill with the correct customer and bill, but didn't. "
				+ "MockWaiter's log reads instead: " + waiter.log.getLastLoggedEvent().toString(),
				waiter.log.containsString("Received msgHereIsBill from cashier. Customer: " + customer + ", bill: " + 10.00));
		
		// message: HereIsPayment
		cashier.msgHereIsPayment(customer, 5.00);
		assertEquals("Cashier should have one customer, but doesn't.", 1, cashier.getCustomers().size());
		assertEquals("Cashier's single customer should be the MockCustomer, but isn't.",
				customer, cashier.getCustomers().get(0).getCustomer());
		assertEquals("Cashier's single customer should have a payment of $10.00, but doesn't.",
				5.00, cashier.getCustomers().get(0).getPayment());
		assertEquals("Cashier's single customer should have a state of PAID, but doesn't.",
				CashierRole.CState.PAID, cashier.getCustomers().get(0).getState());
		
		// interleave market messaging
		/*
		cashier.msgHereIsYourTotal(10.00, market);
		assertTrue("Cashier scheduler should return true (needs to react to Market's HereIsBill), but doesn't.",
				cashier.pickAndExecuteAnAction());
		*/
		
		// scheduler:
		assertTrue("Cashier scheduler should return true (needs to react to Customer's HereIsPayment), but doesn't.",
				cashier.pickAndExecuteAnAction());
		
		// action: giveChange
		assertTrue("MockCustomer should have logged an event for receiving "
				+ "msgHereIsChange with correct change, but didn't. "
				+ "MockCustomer's log reads instead: " + customer.log.getLastLoggedEvent().toString(),
				customer.log.containsString("Received msgHereIsChange from cashier. Change = " + 0.00));
		assertEquals("Cashier's single customer should have a state of DONE, but doesn't.",
				CashierRole.CState.DONE, cashier.getCustomers().get(0).getState());
		assertEquals("Cashier's single customer should still have a bill of $5.00, but doesn't.",
				5.00, cashier.getCustomers().get(0).getBill());
		assertNotSame("Cashier should have $200.00 - $10.00 - $10.00 + $5.00 = $205.00, but doesn't.",
				185.00, cashier.getMoney());
	}
}
