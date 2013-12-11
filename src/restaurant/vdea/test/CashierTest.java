package restaurant.vdea.test;


import restaurant.vdea.CashierRole;
import restaurant.vdea.CashierRole.BillStatus;
import restaurant.vdea.CashierRole.PaymentStatus;
import restaurant.vdea.test.mock.*;
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
	CashierRole cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MockMarket market;
	MockMarket market2;
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		// cashier = new CashierRole("cashier", 200);
		cashier = new CashierRole(null, null); // TODO ERIK WAS HERE, FIXING YOUR COMPILE ERRORS
		customer = new MockCustomer("mockcustomer");	
		waiter = new MockWaiter("mockwaiter");
		market = new MockMarket("mockmarket");
		market2 = new MockMarket("mockmarket2");
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testOneNormalCustomerScenario()
	{
		//setUp() runs first before this test!
		System.out.println("TESTING: Normal customer scenario");
		customer.cashier = cashier;	
		customer.waiter = waiter;
		waiter.cashier = cashier;
		waiter.customer = customer;
		
		//check preconditions
		assertEquals("Cashier should have 0 transactions in it. It doesn't.",cashier.transactions.size(), 0);
		assertEquals("Cashier should have 0 bills to pay. It doesn't.",cashier.bills.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's ComputeBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
				+ customer.log.toString(), 0, customer.log.size());
		assertEquals("MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockMarket's event log reads: "
				+ market.log.toString(), 0, market.log.size());
		
		//step 1 of the test
		//public Bill(Cashier, Customer, int tableNum, double price) {
		//Bill bill = new Bill(cashier, customer, 2, 7.98);
		String choice = "steak";
		double price = 15.99;
		double cash = 15.99;
		cashier.msgComputeBill(waiter, customer, choice, 1);//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 1 transaction in it. It doesn't.", cashier.transactions.size(), 1);
		assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("MockWaiter should have logged \"msgHereIsCheck\" but didn't. His log reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received HereIsCheck from cashier."));
		assertTrue("MockCustomer should have logged \"msgHereIsCheck\" but didn't. His log reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgHereIsCheck from waiter."));
		assertTrue("Cashier status should be computed. It isn't.",
				cashier.transactions.get(0).status == PaymentStatus.computed);
		
		//step 2 of the test
		cashier.msgPayment(customer, price, cash); //from customer
		
		//check postconditions for step 2 / preconditions for step 3
		assertTrue("Cashier status should be paymentRecieved. It isn't.", cashier.transactions.get(0).status == PaymentStatus.paymentReceived);
		assertTrue("Cashier should have logged \"Received msgPayment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgPayment from customer."));
		assertTrue("CashierBill should contain a bill of price = $15.99 It contains something else instead: $" 
				+ cashier.transactions.get(0).total, cashier.transactions.get(0).total == 15.99);
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.transactions.get(0).c == customer);
		
		//step 3
		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgPayment), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4 
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 0.0"));
		assertEquals("Cashier should have 0 transactions in it. It doesn't.",cashier.transactions.size(), 0);		
		//last
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
	
	}//end one normal customer scenario
	
	//Customer does not have enough money to pay for food
	public void testBadCustomerScenario(){ 
		System.out.println("TESTING: Bad customer scenario"); 
		customer.cashier = cashier;	
		customer.waiter = waiter;
		waiter.cashier = cashier;
		waiter.customer = customer;

		//check preconditions
		assertEquals("Cashier should have 0 transactions in it. It doesn't.",cashier.transactions.size(), 0);
		assertEquals("Cashier should have 0 bills to pay. It doesn't.",cashier.bills.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's ComputeBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
				+ customer.log.toString(), 0, customer.log.size());
		assertEquals("MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockMarket's event log reads: "
				+ market.log.toString(), 0, market.log.size());

		//step 1 of the test
		String choice = "steak";
		double price = 15.99;
		double cash = 9.99;	//<-- not have enough money to pay for food
		cashier.msgComputeBill(waiter, customer, choice, 1);//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 1 transaction in it. It doesn't.", cashier.transactions.size(), 1);
		assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("MockWaiter should have logged \"msgHereIsCheck\" but didn't. His log reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received HereIsCheck from cashier."));
		assertTrue("MockCustomer should have logged \"msgHereIsCheck\" but didn't. His log reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgHereIsCheck from waiter."));
		assertTrue("Cashier status should be computed. It isn't.", cashier.transactions.get(0).status == PaymentStatus.computed);

		//step 2 of the test  /TODO
		cashier.msgPayment(customer, price, cash); //from customer

		//check postconditions for step 2 / preconditions for step 3
		assertTrue("Cashier status should be notEnough. It isn't.", cashier.transactions.get(0).status == PaymentStatus.notEnough);
		assertTrue("Cashier should have logged \"Received msgPayment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgPayment from customer."));
		assertTrue("CashierBill should contain a bill of price = $15.99 It contains something else instead: $" 
				+ cashier.transactions.get(0).total, cashier.transactions.get(0).total == 15.99);
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
				cashier.transactions.get(0).c == customer);

		//step 3
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgPayment), but didn't.", 
				cashier.pickAndExecuteAnAction());	
		
		//check postconditions for step 3
		assertTrue("MockCustomer should have logged an event for receiving \"YouHaveDebt\" with the correct leftover payment, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received YouOweUs from cashier. Debt = 6"));
		assertEquals("Cashier should have 0 transactions in it. It doesn't.",cashier.transactions.size(), 0);		
		//last
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}// end bad customer scenario
	
	public void testNormalCustomerAndMarketScenario(){
		System.out.println("TESTING: Normal customer AND market scenario");
		customer.cashier = cashier;	
		customer.waiter = waiter;
		waiter.cashier = cashier;
		waiter.customer = customer;
		
		//check preconditions (empty logs and lists)
		assertEquals("Cashier should have 0 transactions in it. It doesn't.",cashier.transactions.size(), 0);
		assertEquals("Cashier should have 0 bills to pay. It doesn't.",cashier.bills.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's ComputeBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
				+ customer.log.toString(), 0, customer.log.size());
		assertEquals("MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockMarket's event log reads: "
				+ market.log.toString(), 0, market.log.size());
		
		//step C1: get check for customer
		String choice = "steak";
		double price = 15.99;
		double cash = 15.99;
		cashier.msgComputeBill(waiter, customer, choice, 1); //send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 1 transaction in it. It doesn't.", cashier.transactions.size(), 1);
		assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("MockWaiter should have logged \"msgHereIsCheck\" but didn't. His log reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received HereIsCheck from cashier."));
		assertTrue("MockCustomer should have logged \"msgHereIsCheck\" but didn't. His log reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgHereIsCheck from waiter."));
		assertTrue("Cashier transaction status should be computed. It isn't.", cashier.transactions.get(0).status == PaymentStatus.computed);
		
		//step C2: msgPayment from customer
		cashier.msgPayment(customer, price, cash); //from customer
		
		//check postconditions for step C2 / preconditions for step C3
		assertTrue("Cashier status should be paymentRecieved. It isn't.", cashier.transactions.get(0).status == PaymentStatus.paymentReceived);
		assertTrue("Cashier should have logged \"Received msgPayment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgPayment from customer."));
		assertTrue("CashierBill should contain a bill of price = $15.99 It contains something else instead: $" 
				+ cashier.transactions.get(0).total, cashier.transactions.get(0).total == 15.99);
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", cashier.transactions.get(0).c == customer);

		//Step M1: Testing when the Cashier needs to pay the bill from the Market
		double bill = 100;
		cashier.msgMarketBill(market, bill); //from market

		//check postconditions for step M1 and preconditions for step M2
		assertTrue("Cashier's bill status should be pending. It isn't.",
				cashier.bStatus == BillStatus.pending);
		assertTrue("Cashier should have logged an event for receiving \"msgMarketBill\", but his last event logged reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received MarketBill from market. Bill = 100"));
		assertTrue("Market Bill should contain a bill of price = $100 It contains something else instead: $" 
				+ cashier.bills.get(0).bill, cashier.bills.get(0).bill == 100);
		assertTrue("Market Bill should contain a market named mockmarket. It contains something else instead: " 
				+ cashier.bills.get(0).m.getName(), cashier.bills.get(0).m.getName().equals("mockmarket"));

		//step 3: call pick and exe
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgPayment and msgMarketBill), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step C4
		assertTrue("Market should have logged an event for receiving \"msgBillPayment\", but his last event logged reads instead: " 
				+ market.log.getLastLoggedEvent().toString(), market.log.containsString("Received payment from cashier"));
		assertTrue("MockWaiter should have logged an event for receiving \"HereIsCheck\" with the check from cashier, but his last event logged reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received HereIsCheck from cashier."));
		assertTrue("Cashier's bill status should be paid. It isn't.", cashier.bStatus == BillStatus.paid);
		
		//step C4: call pick and exe for customer' change
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgPayment and msgMarketBill), but didn't.", 
				cashier.pickAndExecuteAnAction());
		//check postconditions for step 4
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 0.0"));
		assertEquals("Cashier should have 0 transactions in it. It doesn't.",cashier.transactions.size(), 0);		
		//last
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}//end NormalCustomerAndMarketScenario
	
	
	
	
	public void testNormalCashierPayOneMarketBillScenario(){
		System.out.println("TESTING: Market Billing interaction with ONE market");
		
		//check preconditions
		assertEquals("Cashier should have 0 bills from the market to pay. It doesn't.",cashier.bills.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's ComputeBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockMarket's event log reads: "
				+ market.log.toString(), 0, market.log.size());
		//other preconditions
		assertEquals("Cashier should have 0 transactions in it. It doesn't.",cashier.transactions.size(), 0);
		assertEquals("MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
				+ customer.log.toString(), 0, customer.log.size());
		

		//Step 1: Testing when the Cashier needs to pay the bill from the Market
		double bill = 100;
		cashier.msgMarketBill(market, bill); //from market

		//check postconditions for step 1 and preconditions for step 2
		assertTrue("Cashier's bill status should be pending. It isn't.",
				cashier.bStatus == BillStatus.pending);
		assertTrue("Cashier should have logged an event for receiving \"msgMarketBill\", but his last event logged reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received MarketBill from market. Bill = 100"));
		assertTrue("Market Bill should contain a bill of price = $100 It contains something else instead: $" 
				+ cashier.bills.get(0).bill, cashier.bills.get(0).bill == 100);
		assertTrue("Market Bill should contain a market named mockmarket. It contains something else instead: " 
				+ cashier.bills.get(0).m.getName(), cashier.bills.get(0).m.getName().equals("mockmarket"));

		//Step 2: Pick and execute
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgPayment), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3
		assertTrue("Market should have logged an event for receiving \"msgBillPayment\", but his last event logged reads instead: " 
				+ market.log.getLastLoggedEvent().toString(), market.log.containsString("Received payment from cashier"));
		assertTrue("Cashier's bill status should be paid. It isn't.", cashier.bStatus == BillStatus.paid);

		//last
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}//end CashierPayOneMarketBill
	
	public void testNormalCashierPayTwoMarketBillsScenario(){
		System.out.println("TESTING: Market Billing interaction with TWO markets");
		
		//check preconditions
		assertEquals("Cashier should have 0 bills from the market to pay. It doesn't.",cashier.bills.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's ComputeBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockMarket's event log reads: "
				+ market.log.toString(), 0, market.log.size());
		assertEquals("MockMarket2 should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockMarket2's event log reads: "
				+ market2.log.toString(), 0, market2.log.size());
		//other preconditions
		assertEquals("Cashier should have 0 transactions in it. It doesn't.",cashier.transactions.size(), 0);
		assertEquals("MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
				+ customer.log.toString(), 0, customer.log.size());
		
		//Step 1: Testing when the Cashier needs to pay the bill from the Market
		double bill = 100;
		cashier.msgMarketBill(market, bill); //from market

		//check postconditions for step 1 and preconditions for step 2
		assertTrue("Cashier's bill status should be pending. It isn't.", cashier.bStatus == BillStatus.pending);
		assertTrue("Cashier should have logged an event for receiving \"msgMarketBill\", but his last event logged reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received MarketBill from market. Bill = 100"));
		assertTrue("Market Bill should contain a bill of price = $100 It contains something else instead: $" 
				+ cashier.bills.get(0).bill, cashier.bills.get(0).bill == 100);
		assertTrue("Market Bill should contain a market named mockmarket. It contains something else instead: " 
				+ cashier.bills.get(0).m.getName(), cashier.bills.get(0).m.getName().equals("mockmarket"));

		//Step 2: Pick and execute
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgPayment), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		assertTrue("Market should have logged an event for receiving \"msgBillPayment\", but his last event logged reads instead: " 
				+ market.log.getLastLoggedEvent().toString(), market.log.containsString("Received payment from cashier"));
		assertTrue("Cashier's bill status should be paid. It isn't.", cashier.bStatus == BillStatus.paid);
		assertEquals("MockMarket2 should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockMarket2's event log reads: "
				+ market2.log.toString(), 0, market2.log.size());

		//Step 4: Second Market
		System.out.println("Running second market");
		double bill2 = 80;
		cashier.msgMarketBill(market2, bill2); //from market

		//check postconditions for step 4 and preconditions for step 5
		assertTrue("Cashier's bill status should be pending. It isn't.", cashier.bStatus == BillStatus.pending);
		assertTrue("Cashier should have logged an event for receiving \"msgMarketBill\", but his last event logged reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received MarketBill from market. Bill = 80"));
		assertTrue("Market Bill should contain a bill of price = $80 It contains something else instead: $" 
				+ cashier.bills.get(0).bill, cashier.bills.get(0).bill == 80);
		assertTrue("Market Bill should contain a market named mockmarket2. It contains something else instead: " 
				+ cashier.bills.get(0).m.getName(), cashier.bills.get(0).m.getName().equals("mockmarket2"));

		//Step 5: Pick and execute
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgPayment), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 5
		assertTrue("Market2 should have logged an event for receiving \"msgBillPayment\", but his last event logged reads instead: " 
				+ market2.log.getLastLoggedEvent().toString(), market2.log.containsString("Received payment from cashier"));
		assertTrue("Cashier's bill status should be paid. It isn't.", cashier.bStatus == BillStatus.paid);
		
		//last
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}// end CashierPayTwoMarketBill

	
	//Tests one bad customer, one normal customer, and one market scenario
	/*public void testOneBadOneGoodCustomerOneMarketBillScenario(){
		System.out.println("TESTING: One bad customer, one good customer, and one market scenario");
		customer.cashier = cashier;	
		customer.waiter = waiter;
		badCustomer.cashier = cashier;	
		badCustomer.waiter = waiter;
		waiter.cashier = cashier;
		waiter.customer = customer;
		waiter.customer2 = badCustomer;
		
		//check preconditions (empty logs and lists)
		assertEquals("Cashier should have 0 transactions in it. It doesn't.",cashier.transactions.size(), 0);
		assertEquals("Cashier should have 0 bills to pay. It doesn't.",cashier.bills.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's ComputeBill is called. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cashier.log.size());
		assertEquals("MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
				+ customer.log.toString(), 0, customer.log.size());
		assertEquals("BadCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the BadCustomer's event log reads: "
				+ badCustomer.log.toString(), 0, badCustomer.log.size());
		assertEquals("MockMarket should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockMarket's event log reads: "
				+ market.log.toString(), 0, market.log.size());


		//step C1: get check for customer
		String choice = "steak";
		double price = 15.99;
		double cash = 15.99;
		cashier.msgComputeBill(waiter, customer, choice, 1); //send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
				+ waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 1 transaction in it. It doesn't.", cashier.transactions.size(), 1);
		assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("MockWaiter should have logged \"msgHereIsCheck\" but didn't. His log reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received HereIsCheck from cashier."));
		assertTrue("MockCustomer should have logged \"msgHereIsCheck\" but didn't. His log reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgHereIsCheck from waiter."));
		assertTrue("Cashier transaction status should be computed. It isn't.", cashier.transactions.get(0).status == PaymentStatus.computed);

		//step C2: msgPayment from customer
		cashier.msgPayment(customer, price, cash); //from customer

		//check postconditions for step C2 / preconditions for step C3
		assertTrue("Cashier status should be paymentRecieved. It isn't.", cashier.transactions.get(0).status == PaymentStatus.paymentReceived);
		assertTrue("Cashier should have logged \"Received msgPayment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgPayment from customer."));
		assertTrue("CashierBill should contain a bill of price = $15.99 It contains something else instead: $" 
				+ cashier.transactions.get(0).total, cashier.transactions.get(0).total == 15.99);
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", cashier.transactions.get(0).c == customer);

		//Step M1: Testing when the Cashier needs to pay the bill from the Market
		double bill = 100;
		cashier.msgMarketBill(market, bill); //from market

		//check postconditions for step M1 and preconditions for step M2
		assertTrue("Cashier's bill status should be pending. It isn't.",
				cashier.bStatus == BillStatus.pending);
		assertTrue("Cashier should have logged an event for receiving \"msgMarketBill\", but his last event logged reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received MarketBill from market. Bill = 100"));
		assertTrue("Market Bill should contain a bill of price = $100 It contains something else instead: $" 
				+ cashier.bills.get(0).bill, cashier.bills.get(0).bill == 100);
		assertTrue("Market Bill should contain a market named mockmarket. It contains something else instead: " 
				+ cashier.bills.get(0).m.getName(), cashier.bills.get(0).m.getName().equals("mockmarket"));

		//step 3: call pick and exe
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgPayment and msgMarketBill), but didn't.", 
				cashier.pickAndExecuteAnAction());

		//check postconditions for step 3 / preconditions for step C4
		assertTrue("Market should have logged an event for receiving \"msgBillPayment\", but his last event logged reads instead: " 
				+ market.log.getLastLoggedEvent().toString(), market.log.containsString("Received payment from cashier"));
		assertTrue("MockWaiter should have logged an event for receiving \"HereIsCheck\" with the check from cashier, but his last event logged reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received HereIsCheck from cashier."));
		assertTrue("Cashier's bill status should be paid. It isn't.", cashier.bStatus == BillStatus.paid);

		//step C4: call pick and exe for customer' change
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's msgPayment and msgMarketBill), but didn't.", 
				cashier.pickAndExecuteAnAction());
		//check postconditions for step 4
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 0.0"));
		assertEquals("Cashier should have 0 transactions in it. It doesn't.",cashier.transactions.size(), 0);
		
		//step 1 of the test
		double lowCash = 9.99;	//<-- not have enough money to pay for food
		cashier.msgComputeBill(waiter, badCustomer, choice, 2);//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("Cashier should have 1 transaction in it. It doesn't.", cashier.transactions.size(), 1);
		assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("MockWaiter should have logged \"msgHereIsCheck\" but didn't. His log reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received HereIsCheck from cashier."));
		assertTrue("MockCustomer should have logged \"msgHereIsCheck\" but didn't. His log reads instead: " 
				+ badCustomer.log.getLastLoggedEvent().toString(), badCustomer.log.containsString("Received msgHereIsCheck from waiter."));
		assertTrue("Cashier status should be computed. It isn't.", cashier.transactions.get(0).status == PaymentStatus.computed);

		//step 2 of the test  /TODO
		cashier.msgPayment(badCustomer, price, lowCash); //from badCustomer

		//check postconditions for step 2 / preconditions for step 3
		assertTrue("Cashier status should be notEnough. It isn't.", cashier.transactions.get(0).status == PaymentStatus.notEnough);
		assertTrue("Cashier should have logged \"Received msgPayment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgPayment from customer."));
		assertTrue("CashierBill should contain a bill of price = $15.99 It contains something else instead: $" 
				+ cashier.transactions.get(0).total, cashier.transactions.get(0).total == 15.99);
		assertTrue("CashierBill should contain a bill with the right badCustomer in it. It doesn't.", 
				cashier.transactions.get(0).c == badCustomer);

		//step 3
		assertTrue("Cashier's scheduler should have returned true (needs to react to badCustomer's msgPayment), but didn't.", 
				cashier.pickAndExecuteAnAction());	

		//check postconditions for step 3
		assertTrue("MockCustomer should have logged an event for receiving \"YouHaveDebt\" with the correct leftover payment, but his last event logged reads instead: " 
				+ badCustomer.log.getLastLoggedEvent().toString(), badCustomer.log.containsString("Received YouOweUs from cashier. Debt = 6"));
		assertEquals("Cashier should have 0 transactions in it. It doesn't.",cashier.transactions.size(), 0);
		
		//last
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}//OneBadOneGoodCustomerOneMarketBillScenario*/
}
