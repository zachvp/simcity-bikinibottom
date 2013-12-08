package restaurant.vegaperk.test;

import restaurant.vegaperk.CashierAgent;
import restaurant.vegaperk.CashierAgent.CustomerState;
import restaurant.vegaperk.test.mock.MockCustomer;
import restaurant.vegaperk.test.mock.MockMarket;
import restaurant.vegaperk.test.mock.MockWaiter;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MockMarket market;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierAgent("cashier");		
		customer = new MockCustomer("mockcustomer");		
		waiter = new MockWaiter("mockwaiter");
		market = new MockMarket("mockmarket");
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testCustomerPaysWithExactChange(){
		customer.cashier = cashier;//You can do almost anything in a unit test.			
		
		//check preconditions
		assertEquals("Cashier should have 0 customers in it. It doesn't.", cashier.getCustomers().size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//run scenario
		double bill = 2.00;
		cashier.msgDoneEating(customer, bill, waiter);//send message from the waiter
		customer.msgHereIsCheck(bill, cashier);

		//check log preconditions
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		//check cashier preconditions
		assertEquals("Cashier should have 1 customer in it. It doesn't.", cashier.getCustomers().size(), 1);
		
		//log conditions
		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		//message: receive payment from customer
		cashier.msgHereIsPayment(customer, bill);
		
		assertEquals("Customers should contain a customer with state == PAID. It doesn't.",
				CustomerState.PAID, cashier.getCustomers().get(0).state);
		
		assertEquals("Customer should contain a bill of price = $2.00. It contains something else instead.",
				2.0, cashier.getCustomers().get(0).getPayment());
		
		assertTrue("Customer should contain a bill with the right customer in it. It doesn't.", 
					cashier.getCustomers().get(0).getCustomer() == customer);
		//interleaving message
		cashier.msgHereIsBill(bill, market);
		assertTrue("Cashier's scheduler should have returned true (needs to react to Market's hereIsBill), but didn't.", 
				cashier.pickAndExecuteAnAction());
		//scheduler
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's DoneEating), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//action
		assertEquals("Cashier should give no change to the customer, but does give some change.",
				bill, cashier.getCustomers().get(0).getPayment() - cashier.getCustomers().get(0).getBill());
		assertEquals("Customer state should be set to DONE, but is not.",
				CustomerState.DONE, cashier.getCustomers().get(0).state);
		
		//scheduler
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
	
	public void testCustomerPaysAndReceivesChange(){
		//set up the scenario
		customer.cashier = cashier;
		customer.money = 5.0;
		
		//check preconditions
		assertEquals("Cashier should have 0 customers in it. It doesn't.", cashier.getCustomers().size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//run scenario
		double bill = 2.00;
		cashier.msgDoneEating(customer, bill, waiter);//send message from the waiter
		customer.msgHereIsCheck(bill, cashier);

		//check log preconditions
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		//check cashier preconditions
		assertEquals("Cashier should have 1 customer in it. It doesn't.", cashier.getCustomers().size(), 1);
		
		//log conditions
		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		//message: receive payment from customer
		cashier.msgHereIsPayment(customer, bill);
		
		assertEquals("Customers should contain a customer with state == PAID. It doesn't.",
				CustomerState.PAID, cashier.getCustomers().get(0).state);
		
		assertEquals("Customer should contain a bill of price = $2.00. It contains something else instead.",
				2.0, cashier.getCustomers().get(0).getPayment());
		
		assertTrue("Customer should contain a bill with the right customer in it. It doesn't.", 
					cashier.getCustomers().get(0).getCustomer() == customer);
		
		//action: cashier gives proper change
				double change = customer.money - bill;
				assertEquals("Cashier should give " + change + " in change, but does not.",
						change, customer.money - cashier.getCustomers().get(0).getBill());
		
		//interleaving message
		cashier.msgHereIsBill(bill, market);
		assertTrue("Cashier's scheduler should have returned true (needs to react to Market's hereIsBill), but didn't.", 
				cashier.pickAndExecuteAnAction());
				
		//scheduler
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's DoneEating), but didn't.", 
					cashier.pickAndExecuteAnAction());
		assertEquals("Customer state should be set to DONE, but is not.",
				CustomerState.DONE, cashier.getCustomers().get(0).state);
		
		//scheduler
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
	}
	
	public void testCustomerCannotPay(){
		customer.cashier = cashier;			
		
		//check preconditions
		assertEquals("Cashier should have 0 customers in it. It doesn't.", cashier.getCustomers().size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//run scenario
		double bill = 2.00;
		cashier.msgDoneEating(customer, bill, waiter);//send message from the waiter
		customer.msgHereIsCheck(bill, cashier);

		//check log preconditions
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		//check cashier preconditions
		assertEquals("Cashier should have 1 customer in it. It doesn't.", cashier.getCustomers().size(), 1);
		
		//log conditions
		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		//message: receive payment from customer
		cashier.msgHereIsPayment(customer, bill);
		
		assertEquals("Customers should contain a customer with state == PAID. It doesn't.",
				CustomerState.PAID, cashier.getCustomers().get(0).state);
		
		assertEquals("Customer should contain a bill of price = $2.00. It contains something else instead.",
				2.0, cashier.getCustomers().get(0).getPayment());
		
		assertTrue("Customer should contain a bill with the right customer in it. It doesn't.", 
					cashier.getCustomers().get(0).getCustomer() == customer);
		//interleaving message
		cashier.msgHereIsBill(bill, market);
		assertTrue("Cashier's scheduler should have returned true (needs to react to Market's hereIsBill), but didn't.", 
				cashier.pickAndExecuteAnAction());
		//scheduler
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's DoneEating), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//action: customer does not have enough to pay cashier. Bill should stay at whatever the customer owes
		customer.money = 0.0;
		assertEquals("Cashier should give no change to the customer, but does give some change.",
				bill, cashier.getCustomers().get(0).getPayment() - cashier.getCustomers().get(0).getBill());
		assertEquals("Cashier should save the customer's bill for their next visit, but does not",
				bill, customer.bill);
		assertEquals("Customer state should be set to DONE, but is not.",
				CustomerState.DONE, cashier.getCustomers().get(0).state);
		
		//scheduler
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
	
	public void testNormativeInteractionWithOneMarket(){
		//preconditions:
		assertTrue("Cashier should have no bills to start, but does.",
				cashier.getBills().size() == 0);
		assertTrue("Cashier should have $500 to start, but doesn't.",
				cashier.getMoney() == 500);
		assertTrue("Market should have no money to start, but does.",
				market.money == 0);
		
		//run scenario
		market.cashier = cashier;
		double bill = 25;

		//message: market bills cashier
		cashier.msgHereIsBill(bill, market);
		//cashier creates a new bill
		assertEquals("Cashier should have one bill, but doesn't",
				1, cashier.getBills().size());
		
		//scheduler
		assertTrue("Cashier's scheduler should return true after receiving msgHereIsBill() from the market, but doesn't",
				cashier.pickAndExecuteAnAction());
		//action: cashier subtracts own money, pays market, and removes bill
		assertEquals("Cashier should have $500 - $25 = $475 to pay market, but doesn't.",
				475.0, cashier.getMoney());
		assertEquals("Market should have $25 in it, but doesn't.",
				25.0, market.money);
		assertEquals("Cashier should have removed the bill (and have no more bills), but didn't.", 
				0, cashier.getBills().size());
	}
	
	public void testNormativeInteractionWithTwoMarkets(){
		//preconditions:
		assertTrue("Cashier should have no bills to start, but does.",
				cashier.getBills().size() == 0);
		assertTrue("Cashier should have $500 to start, but doesn't.",
				cashier.getMoney() == 500);
		assertTrue("Market should have no money to start, but does.",
				market.money == 0);
		
		//run scenario
		double bill = 25;
		MockMarket market2 = new MockMarket("mockmarket2");
		
		//message: cashier gets first market's bill
		cashier.msgHereIsBill(bill, market);
		cashier.msgHereIsBill(bill, market2);
		//cashier creates a new bill
		assertEquals("Cashier should have two bills, but doesn't",
				2, cashier.getBills().size());
		
		//scheduler
		assertTrue("Cashier's scheduler should return true after receiving msgHereIsBill() from the market, but doesn't",
				cashier.pickAndExecuteAnAction());
		//action: cashier subtracts own money, pays first market, and removes bill
		assertEquals("Cashier should have $500 - $25 = $475 to pay market, but doesn't.",
				475.0, cashier.getMoney());
		assertEquals("Market should have $25 in it, but doesn't.",
				25.0, market.money);
		assertEquals("Cashier should have removed the bill (and have one more bill), but didn't.", 
				1, cashier.getBills().size());
		
		//second call to scheduler
		assertTrue("Cashier's scheduler should return true after receiving msgHereIsBill() from the market, but doesn't",
				cashier.pickAndExecuteAnAction());
		//action: cashier subtracts own money, pays first market, and removes bill
		assertEquals("Cashier should have $475 - $25 = $450 to pay market, but doesn't.",
				450.0, cashier.getMoney());
		assertEquals("Market2 should have $25 in it, but doesn't.",
				25.0, market2.money);
		assertEquals("Cashier should have removed the bill (and have no more bills), but didn't.", 
				0, cashier.getBills().size());
	}
	
	public void testCashierCannotPayMarket(){
		//preconditions:
		assertTrue("Cashier should have no bills to start, but does.",
				cashier.getBills().size() == 0);
		assertTrue("Cashier should have $500 to start, but doesn't.",
				cashier.getMoney() == 500);
		assertTrue("Market should have no money to start, but does.",
				market.money == 0);
		
		//run scenario
		double bill = 1000;
		
		//message: cashier gets first market's bill
		cashier.msgHereIsBill(bill, market);
		//cashier creates a new bill
		assertEquals("Cashier should have one bill, but doesn't",
				1, cashier.getBills().size());
		assertEquals("Bill should be 1000, but isn't.",
				1000.0, cashier.getBills().get(0).getAmount());
		//scheduler
		assertTrue("Cashier's scheduler should return true after receiving msgHereIsBill() from the market, but doesn't",
				cashier.pickAndExecuteAnAction());
		//action: cashier should give the market all of his money
		assertEquals("Cashier should have no money now, but does",
				0.0, cashier.getMoney());
		assertEquals("Market should have $500, but doesn't.",
				500.0, market.money);
		assertEquals("Cashier should have removed the bill (and have no more bills), but didn't.", 
				0, cashier.getBills().size());
	}
}
