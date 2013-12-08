package restaurant.vonbeck.test;

import junit.framework.TestCase;
import restaurant.vonbeck.CashierAgent;
import restaurant.vonbeck.interfaces.Waiter.Order;
import restaurant.vonbeck.test.mock.MockCustomer;
import restaurant.vonbeck.test.mock.MockMarket;
import restaurant.vonbeck.test.mock.MockWaiter;

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
	MockCustomer thief;
	MockMarket market1;
	MockMarket market2;
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierAgent(false);	// TODO Should I add a name?	
		customer = new MockCustomer("mockcustomer");
		thief = new MockCustomer("thief");
		waiter = new MockWaiter("mockwaiter");
		market1 = new MockMarket("mockmarket 1");
		market2 = new MockMarket("mockmarket 2");
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testOneNormalCustomerScenario() //TODO Add wallet change verification
	{
		//setUp() runs first before this test!
		
		waiter.cashier = cashier;//You can do almost anything in a unit test.			
		
		//check preconditions
		
		
		assertEquals("CashierAgent should have an empty event log before the Cashier's "+
				"HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		Order o = new Order("Pizza", customer, waiter);
		cashier.msgNeedBill(o);//send the message from a waiter

		assertEquals("MockWaiter should have an empty event log before the Cashier's"+
				" scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 bill in it. It doesn't.",
				cashier.getOrders().size(), 1);
		
		
		assertTrue("Cashier's scheduler should have returned true (send bill back to"+
				" waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		
		assertTrue("MockCustomer should have logged an event for receiving \"msgBill\""+
				" with the correct balance, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(),
				customer.log.containsString("Received msgBill from cashier. Total = 899"));
	
			
		assertTrue("Cashier should have logged \"Received msgPay\" but didn't."+
				" His log reads instead: " + cashier.log.getLastLoggedEvent().toString(),
				cashier.log.containsString("Received msgPay"));
		
		assertFalse("Cashier's scheduler should have returned false, but didn't.", 
					cashier.pickAndExecuteAnAction());
		
	}//end one normal customer scenario
	
	public void testTwoNoMoneyCustomerScenario()
	{
		//setUp() runs first before this test!
		
		waiter.cashier = cashier;//You can do almost anything in a unit test.			
		
		//check preconditions
		
		
		assertEquals("CashierAgent should have an empty event log before the Cashier's "+
				"HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		Order o = new Order("Steak", thief, waiter);
		cashier.msgNeedBill(o);//send the message from a waiter

		assertEquals("MockWaiter should have an empty event log before the Cashier's"+
				" scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 bill in it. It doesn't.",
				cashier.getOrders().size(), 1);
		
		int moneyFirst = cashier.getWallet();

		
		assertTrue("Cashier's scheduler should have returned true (send bill back to"+
				" waiter), but didn't.", cashier.pickAndExecuteAnAction());

		int moneySecond = cashier.getWallet();

		
		assertTrue("MockCustomer should have logged an event for receiving \"msgBill\""+
				" with the correct balance, but his last event logged reads instead: " 
				+ thief.log.getLastLoggedEvent().toString(),
				thief.log.containsString("Received msgBill from cashier. Total = 1599"));
		
		assertEquals("Cashier should have 100 more cents. It actually got " +
				(moneySecond - moneyFirst), 100, moneySecond - moneyFirst);
			
		assertTrue("Cashier should have logged \"Received msgIDontHaveEnoughMoney\" but"+
		" didn't. His log reads instead: " + cashier.log.getLastLoggedEvent().toString(),
				cashier.log.containsString("Received msgIDontHaveEnoughMoney"));
		
		assertFalse("Cashier's scheduler should have returned false, but didn't.", 
					cashier.pickAndExecuteAnAction());
		
	}//end two normal customer scenario
	
	public void testThreeOneMarketScenario()
	{
		//setUp() runs first before this test!
		
		//check preconditions
		
		assertEquals("CashierAgent should have an empty event log before the Cashier's "+
				"HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		cashier.msgBillFromMarket(500, market1);

		assertEquals("Cashier should have 1 market bill in it. It doesn't.",
				1, cashier.getMarketOrders().size());
		
		int moneyFirst = cashier.getWallet();
		
		assertTrue("Cashier's scheduler should have returned true (pay market"+
				" bill), but didn't.", cashier.pickAndExecuteAnAction());
		
		int moneySecond = cashier.getWallet();
		
		assertEquals("Cashier should have 500 less cents. It actually lost " +
				(moneyFirst - moneySecond), 500, moneyFirst - moneySecond);
		
		assertTrue("MockMarket should have logged an event for receiving \"msgPay\""+
				" with the correct balance, but his last event logged reads instead: " 
				+ market1.log.getLastLoggedEvent().toString(),
				market1.log.containsString("Received msgPay from cashier. Total = 500"));
	
		
		assertEquals("Cashier should have no market bill in it. It has " +
		cashier.getMarketOrders().size(), 0, cashier.getMarketOrders().size());
		
		assertFalse("Cashier's scheduler should have returned false, but didn't.", 
					cashier.pickAndExecuteAnAction());
		
	}//end one market scenario
	
	public void testFourTwoMarketsScenario()
	{
		//setUp() runs first before this test!
		
		//check preconditions
		
		assertEquals("CashierAgent should have an empty event log before the Cashier's "+
				"HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		cashier.msgBillFromMarket(300, market1);

		assertEquals("Cashier should have 1 market bill in it. It doesn't.",
				1, cashier.getMarketOrders().size());
		
		cashier.msgBillFromMarket(200, market2);

		assertEquals("Cashier should have 2 market bills in it. It doesn't.",
				2, cashier.getMarketOrders().size());
		
		int moneyFirst = cashier.getWallet();
		
		assertTrue("Cashier's scheduler should have returned true (pay market"+
				" bill), but didn't.", cashier.pickAndExecuteAnAction());
		
		int moneySecond = cashier.getWallet();
		
		assertTrue("MockMarket should have logged an event for receiving \"msgPay\""+
				" with the correct balance, but his last event logged reads instead: " 
				+ market1.log.getLastLoggedEvent().toString(),
				market1.log.containsString("Received msgPay from cashier. Total = 300"));
		
		assertTrue("Cashier's scheduler should have returned true (pay second market"+
				" bill), but didn't.", cashier.pickAndExecuteAnAction());
		
		assertTrue("MockMarket should have logged an event for receiving \"msgPay\""+
				" with the correct balance, but his last event logged reads instead: " 
				+ market2.log.getLastLoggedEvent().toString(),
				market2.log.containsString("Received msgPay from cashier. Total = 200"));
		
		int moneyThird = cashier.getWallet();
		
		assertEquals("Cashier should have 500 less cents. It actually lost " +
				(moneyFirst - moneyThird), 500, moneyFirst - moneyThird);
		
		assertEquals("Cashier should have had 300 less cents after the first transaction."+
				" It actually lost " + (moneyFirst - moneySecond),
				300, moneyFirst - moneySecond);
		
		assertEquals("Cashier should have no market bill in it. It has " +
		cashier.getMarketOrders().size(), 0, cashier.getMarketOrders().size());
		
		assertFalse("Cashier's scheduler should have returned false, but didn't.", 
					cashier.pickAndExecuteAnAction());
		
	}//end two market scenario
	
	public void testFiveTwoCustomersScenario() //TODO Add wallet change verification
	{
		//setUp() runs first before this test!
		
		waiter.cashier = cashier;//You can do almost anything in a unit test.			
		
		//check preconditions
		
		
		assertEquals("CashierAgent should have an empty event log before the Cashier's "+
				"HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		Order o = new Order("Pizza", customer, waiter);
		cashier.msgNeedBill(o);//send the message from a waiter

		assertEquals("MockWaiter should have an empty event log before the Cashier's"+
				" scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 bill in it. It doesn't.",
				cashier.getOrders().size(), 1);
		
		
		assertTrue("Cashier's scheduler should have returned true (send bill back to"+
				" waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		
		assertTrue("MockCustomer should have logged an event for receiving \"msgBill\""+
				" with the correct balance, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(),
				customer.log.containsString("Received msgBill from cashier. Total = 899"));
	
			
		assertTrue("Cashier should have logged \"Received msgPay\" but didn't."+
				" His log reads instead: " + cashier.log.getLastLoggedEvent().toString(),
				cashier.log.containsString("Received msgPay"));
		
		assertFalse("Cashier's scheduler should have returned false, but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		
		

		//step 1 of the test
		Order p = new Order("Steak", thief, waiter);
		cashier.msgNeedBill(p);//send the message from a waiter

		assertEquals("MockWaiter should have an empty event log before the Cashier's"+
				" scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 bill in it. It doesn't.",
				cashier.getOrders().size(), 1);
		
		int moneyFirst = cashier.getWallet();

		
		assertTrue("Cashier's scheduler should have returned true (send bill back to"+
				" waiter), but didn't.", cashier.pickAndExecuteAnAction());

		int moneySecond = cashier.getWallet();

		
		assertTrue("MockCustomer should have logged an event for receiving \"msgBill\""+
				" with the correct balance, but his last event logged reads instead: " 
				+ thief.log.getLastLoggedEvent().toString(),
				thief.log.containsString("Received msgBill from cashier. Total = 1599"));
		
		assertEquals("Cashier should have 100 more cents. It actually got " +
				(moneySecond - moneyFirst), 100, moneySecond - moneyFirst);
			
		assertTrue("Cashier should have logged \"Received msgIDontHaveEnoughMoney\" but"+
		" didn't. His log reads instead: " + cashier.log.getLastLoggedEvent().toString(),
				cashier.log.containsString("Received msgIDontHaveEnoughMoney"));
		
		assertFalse("Cashier's scheduler should have returned false, but didn't.", 
					cashier.pickAndExecuteAnAction());
		
	}//end five two customers scenario
	
	public void testSixCustomerMarketScenario() //TODO Add wallet change verification
	{
		//setUp() runs first before this test!
		
		waiter.cashier = cashier;//You can do almost anything in a unit test.			
		
		//check preconditions
		
		
		assertEquals("CashierAgent should have an empty event log before the Cashier's "+
				"HereIsBill is called. Instead, the Cashier's event log reads: "
						+ cashier.log.toString(), 0, cashier.log.size());
		
		//step 1 of the test
		Order o = new Order("Pizza", customer, waiter);
		cashier.msgNeedBill(o);//send the message from a waiter

		assertEquals("MockWaiter should have an empty event log before the Cashier's"+
				" scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 bill in it. It doesn't.",
				cashier.getOrders().size(), 1);
		
		cashier.msgBillFromMarket(500, market1);

		assertEquals("Cashier should have 1 market bill in it. It doesn't.",
				1, cashier.getMarketOrders().size());
		
		assertTrue("Cashier's scheduler should have returned true (send bill back to"+
				" waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		assertTrue("MockMarket should have logged an event for receiving \"msgPay\""+
				" with the correct balance, but his last event logged reads instead: " 
				+ market1.log.getLastLoggedEvent().toString(),
				market1.log.containsString("Received msgPay from cashier. Total = 500"));
	
		
		assertEquals("Cashier should have no market bill in it. It has " +
		cashier.getMarketOrders().size(), 0, cashier.getMarketOrders().size());
	
		assertTrue("Cashier's scheduler should have returned true (send bill back to"+
				" market), but didn't.", cashier.pickAndExecuteAnAction());
		
		
		
		assertTrue("MockCustomer should have logged an event for receiving \"msgBill\""+
				" with the correct balance, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(),
				customer.log.containsString("Received msgBill from cashier. Total = 899"));
	
		assertTrue("Cashier should have logged \"Received msgPay\" but didn't."+
				" His log reads instead: " + cashier.log.getLastLoggedEvent().toString(),
				cashier.log.containsString("Received msgPay"));
		
		
		
		assertFalse("Cashier's scheduler should have returned false, but didn't.", 
					cashier.pickAndExecuteAnAction());
		
	}//end six customer market scenario
	
}
