package market.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CommonSimpleClasses.Constants;
import agent.PersonAgent;
import junit.framework.TestCase;
import market.CashierRole;
import market.CustomerRole;
import market.CustomerRole.Customerevent;
import market.Item;
import market.CustomerRole.Customerstate;
import market.gui.CashierGui;
import market.gui.MarketBuilding;
import market.interfaces.ItemCollector;
import market.test.mock.MockCashier;
import market.test.mock.MockCashierGui;
import market.test.mock.MockCustomerGui;
import market.test.mock.MockItemCollector;
import market.test.mock.MockCustomer;

public class CustomerTest extends TestCase
{
	MockCashier Cashier;
	CustomerRole Customer1;
	public HashMap<String, Double> PriceList;
	List<Item> ShoppingList;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		

		ShoppingList = new ArrayList<Item>();
		{
			ShoppingList.add(new Item("Krabby Patty", 20));
			ShoppingList.add(new Item("Kelp Shake", 20));
			ShoppingList.add(new Item("Coral Bits", 10));
			ShoppingList.add(new Item("Kelp Rings", 15));
			ShoppingList.add(new Item("LamboFinny", 30));
			ShoppingList.add(new Item("Toyoda", 40));
		}
		
		PriceList = Constants.MarketPriceList;
		
		PersonAgent Customer = new PersonAgent ("John");
		Customer1 = new CustomerRole("John", 150, ShoppingList, Customer);	
		
		Cashier = new MockCashier("MockCashier");
		Customer1.setCashier(Cashier);
		
		MockCustomerGui customerGui = new MockCustomerGui(Customer1);
		Customer1.setGui(customerGui);
		Customer1.setPriceList(PriceList);
	}	
	
	/**
	 * This tests the cashier under very simple terms: 1 MarketCustomer comes to market and 1 ItemCollector (Enough Inventory)
	 */
	public void testOne1MarketCustomer1ItemCollectorScenario()
	{
		
		//check preconditions
			//Check MyCustomerlist
				assertEquals("Customer should have 6 Items in the ItemList in it. It doesn't.",ShoppingList.size(), 6);
			//Check money
				assertEquals("Customer should have $150 in it. It doesn't.", (int) Customer1.getCash(), 150);
			//Check Cashier.PaEaA calls no function (Do nothing)
				assertFalse("Customer's scheduler shouldn't have returned true , but didn't.", Customer1.pickAndExecuteAnAction());
			//Check Name
				assertEquals("Customer should have the name of John. It doesn't", "John", Customer1.getName());
					
		//Step 1 SetCustomer goingToBuy
			Customer1.goingToBuy();
			
		//Check Cashier.PaEaA calls no function (Do nothing)
			assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());
		//Check Conditions
			assertEquals("Customer should have the state as EnteringMarket. It doesn't", Customerstate.GoingToOrder, Customer1.getState());
			assertEquals("Customer should have the event as GoingToLine. It doesn't", Customerevent.WaitingInLine, Customer1.getEvent());
		//Check MyCustomerlist
			assertEquals("Customer should have 6 Items in the ItemList in it. It doesn't.",ShoppingList.size(), 6);
		//Check money
			assertEquals("Customer should have $30 in it. It doesn't.", (int) Customer1.getCash(), 30);
			
		//Check Cashier.PaEaA calls no function (OrderItems)
			assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());	
		
			
		//Step 2 When Customer arrives At the FrontDesk
			Customer1.setState(Customerstate.GoingToOrder);
			Customer1.setEvent(Customerevent.WaitingInLine);
			
		//Check Cashier.PaEaA calls no function (GoToFindCashier)
			assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());
			
			
			
					
					
					
					
					
					
	}
}