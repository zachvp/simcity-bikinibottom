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
	PersonAgent Customer;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		

		
		
		PriceList = Constants.MarketPriceList;
		
		Customer = new PersonAgent ("John");
		
		//Setting up the InventoryList
		Customer.addItemsToInventory("Krabby Patty", 0);
		Customer.addItemsToInventory("Kelp Shake", 0);
		Customer.addItemsToInventory("Coral Bits", 0);
		Customer.addItemsToInventory("Kelp Rings", 0);
		Customer.addItemsToInventory("LamboFinny", 0);
		Customer.addItemsToInventory("Toyoda", 0);
		
		
		Cashier = new MockCashier("MockCashier");
		
		
		
		
		
		
	}	
	
	/**
	 * This tests the cashier under very simple terms: 1 MarketCustomer comes to market and 1 ItemCollector (Enough Money)
	 */
	public void testOne1MarketCustomerHasEnoughMoneyScenario()
	{
		ShoppingList = new ArrayList<Item>();
		{
			ShoppingList.add(new Item("Krabby Patty", 0));
			ShoppingList.add(new Item("Kelp Shake", 0));
			ShoppingList.add(new Item("Coral Bits", 0));
			ShoppingList.add(new Item("Kelp Rings", 1));
			ShoppingList.add(new Item("LamboFinny", 0));
			ShoppingList.add(new Item("Toyoda", 0));
		}
		
		Customer1 = new CustomerRole("John", 30, ShoppingList, Customer);	
		MockCustomerGui customerGui = new MockCustomerGui(Customer1);
		Customer1.setCashier(Cashier);
		Customer1.setGui(customerGui);
		Customer1.setPriceList(PriceList);
		Cashier.setCustomer(Customer1);
		
		//check preconditions
			//Check Shoppinglist
				assertEquals("Customer should have 6 Items in the ItemList in it. It doesn't.",ShoppingList.size(), 6);
				for (int i=0;i<ShoppingList.size();i++){
					if (ShoppingList.get(i).name == "Kelp Rings")
						assertEquals("Customer should have 1 item in Kelp Rings in his inventorylist. It doesn't.", ShoppingList.get(i).amount, 1);
					else
						assertEquals("Customer should have 0 item in each type of the item in his inventorylist. It doesn't.", ShoppingList.get(i).amount , 0);
				}
			//Check money
				assertEquals("Customer should have $30 in it. It doesn't.", (int) Customer1.getCash(), 30);
			//Check Cashier.PaEaA calls no function (Do nothing)
				assertFalse("Customer's scheduler shouldn't have returned true , but didn't.", Customer1.pickAndExecuteAnAction());
			//Check Name
				assertEquals("Customer should have the name of John. It doesn't", "John", Customer1.getName());
					
		//Step 1 SetCustomer goingToBuy
			Customer1.goingToBuy();
			
		//Check Cashier.PaEaA calls a function (GoFindCashier)
			assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());
		//Check Conditions
			assertEquals("Customer should have the state as EnteringMarket. It doesn't", Customerstate.GoingToOrder, Customer1.getState());
			assertEquals("Customer should have the event as GoingToLine. It doesn't", Customerevent.WaitingInLine, Customer1.getEvent());
		//Check MyCustomerlist
			assertEquals("Customer should have 6 Items in the ItemList in it. It doesn't.",ShoppingList.size(), 6);
		//Check money
			assertEquals("Customer should have $30 in it. It doesn't.", (int) Customer1.getCash(), 30);
			
		//Check Cashier.PaEaA calls a function (OrderItems)
			assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());	
		//Check Conditions
			assertEquals("Customer should have the state as EnteringMarket. It doesn't", Customerstate.Waiting, Customer1.getState());
			assertEquals("Customer should have the event as GoingToLine. It doesn't", Customerevent.Paying, Customer1.getEvent());
		
		//By This time, The customer should have receive the invoice from the Cashier (And the shoppinglist is always fulfilled)
			//Check money
				assertEquals("Customer should have $30 in it. It doesn't.", (int) Customer1.getCash(), 30);
			//Check ActualCost (The invoice)
				assertEquals("Customer should be $5 in it. It doesn't.", (int) Customer1.getActualCost(), 5);
				
			//Check if customer's inventory changes
				for (int i=0;i<ShoppingList.size();i++){
					int CurrentInventoryLevel = Customer.getInventory().get(ShoppingList.get(i).name);
					assertEquals("Customer should have no item in each type of item in his inventorylist. It doesn't.", CurrentInventoryLevel, 0);
				}
			
			
		//Check Cashier.PaEaA calls a function (GoToFindCashier)
			assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());
			
		//By This time, The customer should have pay the Cashier for the items and Collected items
			//Check The InventoryList
			for (int i=0;i<ShoppingList.size();i++){
				int CurrentInventoryLevel = Customer.getInventory().get(ShoppingList.get(i).name);
				if (ShoppingList.get(i).name == "Kelp Rings")
					assertEquals("Customer should have 1 item in Kelp Rings in his inventorylist. It doesn't.", CurrentInventoryLevel, 1);
				else
					assertEquals("Customer should have no item in each type of item in his inventorylist (except Kelp Rings). It doesn't.", CurrentInventoryLevel, 0);	
			}
			//Check money
				assertEquals("Customer should have $25 in it. It doesn't.", (int) Customer1.getCash(), 25);
				
			//Check Conditions
				assertEquals("Customer should have the state as EnteringMarket. It doesn't", Customerstate.Paid, Customer1.getState());
				assertEquals("Customer should have the event as GoingToLine. It doesn't", Customerevent.Leaving, Customer1.getEvent());
			
			//Check Cashier.PaEaA calls a function (Leaving)
				assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());
			
			//Check Conditions
				assertEquals("Customer should have the state as EnteringMarket. It doesn't", Customerstate.NotAtMarket, Customer1.getState());
				assertEquals("Customer should have the event as GoingToLine. It doesn't", Customerevent.doneLeaving, Customer1.getEvent());
				
			//check post-conditions
				//Check InventoryList
					for (int i=0;i<ShoppingList.size();i++){
						int CurrentInventoryLevel = Customer.getInventory().get(ShoppingList.get(i).name);
						if (ShoppingList.get(i).name == "Kelp Rings")
							assertEquals("Customer should have 1 item in Kelp Rings in his inventorylist. It doesn't.", CurrentInventoryLevel, 1);
						else
							assertEquals("Customer should have no item in each type of item in his inventorylist (except Kelp Rings). It doesn't.", CurrentInventoryLevel, 0);	
					}
				//Check money
					assertEquals("Customer should have $30 in it. It doesn't.", (int) Customer1.getCash(), 25);
				//Check Cashier.PaEaA calls no function (Do nothing)
					assertFalse("Customer's scheduler shouldn't have returned true , but didn't.", Customer1.pickAndExecuteAnAction());
				//Check Name
					assertEquals("Customer should have the name of John. It doesn't", "John", Customer1.getName());
						
	}
	
	/**
	 * This tests the cashier under very simple terms: 1 MarketCustomer comes to market and Cashier (Not Enough Money)
	 */
	public void testTwoMarketCustomerHasNotEnoughMoneyScenario()
	{
		ShoppingList = new ArrayList<Item>();
		{
			ShoppingList.add(new Item("Krabby Patty", 0));
			ShoppingList.add(new Item("Kelp Shake", 0));
			ShoppingList.add(new Item("Coral Bits", 0));
			ShoppingList.add(new Item("Kelp Rings", 0));
			ShoppingList.add(new Item("LamboFinny", 1));
			ShoppingList.add(new Item("Toyoda", 0));
		}
		
		Customer1 = new CustomerRole("John", 30, ShoppingList, Customer);	
		MockCustomerGui customerGui = new MockCustomerGui(Customer1);
		Customer1.setCashier(Cashier);
		Customer1.setGui(customerGui);
		Customer1.setPriceList(PriceList);
		Cashier.setCustomer(Customer1);
		
		//check preconditions
			//Check Shoppinglist
				assertEquals("Customer should have 6 Items in the ItemList in it. It doesn't.",ShoppingList.size(), 6);
				for (int i=0;i<ShoppingList.size();i++){
					if (ShoppingList.get(i).name == "LamboFinny")
						assertEquals("Customer should have 1 item in LamboFinny in his Shoppinglist. It doesn't.", ShoppingList.get(i).amount, 1);
					else
						assertEquals("Customer should have 0 item in each type of the item in his Shoppinglist. It doesn't.", ShoppingList.get(i).amount , 0);
				}
			//Check money
				assertEquals("Customer should have $30 in it. It doesn't.", (int) Customer1.getCash(), 30);
			//Check Cashier.PaEaA calls no function (Do nothing)
				assertFalse("Customer's scheduler shouldn't have returned true , but didn't.", Customer1.pickAndExecuteAnAction());
			//Check Name
				assertEquals("Customer should have the name of John. It doesn't", "John", Customer1.getName());
					
		//Step 1 SetCustomer goingToBuy
			Customer1.goingToBuy();
			
		//Check Cashier.PaEaA calls a function (GoFindCashier)
			assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());
		//Check Conditions
			assertEquals("Customer should have the state as EnteringMarket. It doesn't", Customerstate.GoingToOrder, Customer1.getState());
			assertEquals("Customer should have the event as GoingToLine. It doesn't", Customerevent.WaitingInLine, Customer1.getEvent());
		//Check MyCustomerlist
			assertEquals("Customer should have 6 Items in the ItemList in it. It doesn't.",ShoppingList.size(), 6);
		//Check money
			assertEquals("Customer should have $30 in it. It doesn't.", (int) Customer1.getCash(), 30);
			
		//Check Cashier.PaEaA calls a function (OrderItems)
			assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());	
		//Check Conditions
			assertEquals("Customer should have the state as EnteringMarket. It doesn't", Customerstate.Waiting, Customer1.getState());
			assertEquals("Customer should have the event as GoingToLine. It doesn't", Customerevent.Paying, Customer1.getEvent());
		
		//By This time, The customer should have receive the invoice from the Cashier (And the shoppinglist is always fulfilled)
			//Check money
				assertEquals("Customer should have $30 in it. It doesn't.", (int) Customer1.getCash(), 30);
			//Check ActualCost (The invoice)
				assertEquals("Customer should be $300 in it. It doesn't.", (int) Customer1.getActualCost(), 300);
				
			//Check if customer's inventory changes
				for (int i=0;i<ShoppingList.size();i++){
					int CurrentInventoryLevel = Customer.getInventory().get(ShoppingList.get(i).name);
					assertEquals("Customer should have no item in each type of item in his inventorylist. It doesn't.", CurrentInventoryLevel, 0);
				}
			
			
		//Check Cashier.PaEaA calls a function (GoToFindCashier)
			assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());
			
		//By This time, The customer should have pay the Cashier for the items and Collected items
			//Check The InventoryList
			for (int i=0;i<ShoppingList.size();i++){
				int CurrentInventoryLevel = Customer.getInventory().get(ShoppingList.get(i).name);
				if (ShoppingList.get(i).name == "LamboFinny")
					assertEquals("Customer should have 1 item in LamboFinny in his inventorylist. It doesn't.", CurrentInventoryLevel, 1);
				else
					assertEquals("Customer should have no item in each type of item in his inventorylist (except LamboFinny). It doesn't.", CurrentInventoryLevel, 0);	
			}
			//Check money
				assertEquals("Customer should have $0 in it. It doesn't.", (int) Customer1.getCash(), 0);
				
			//Check Conditions
				assertEquals("Customer should have the state as EnteringMarket. It doesn't", Customerstate.Paid, Customer1.getState());
				assertEquals("Customer should have the event as GoingToLine. It doesn't", Customerevent.Leaving, Customer1.getEvent());
			
			//Check Cashier.PaEaA calls a function (Leaving)
				assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());
			
			//Check Conditions
				assertEquals("Customer should have the state as EnteringMarket. It doesn't", Customerstate.NotAtMarket, Customer1.getState());
				assertEquals("Customer should have the event as GoingToLine. It doesn't", Customerevent.doneLeaving, Customer1.getEvent());
				
			//check post-conditions
				//Check InventoryList
					for (int i=0;i<ShoppingList.size();i++){
						int CurrentInventoryLevel = Customer.getInventory().get(ShoppingList.get(i).name);
						if (ShoppingList.get(i).name == "LamboFinny")
							assertEquals("Customer should have 1 item in LamboFinny in his inventorylist. It doesn't.", CurrentInventoryLevel, 1);
						else
							assertEquals("Customer should have no item in each type of item in his inventorylist (except LamboFinny). It doesn't.", CurrentInventoryLevel, 0);	
					}
				//Check money
					assertEquals("Customer should have $0 in it. It doesn't.", (int) Customer1.getCash(), 0);
				//Check Cashier.PaEaA calls no function (Do nothing)
					assertFalse("Customer's scheduler shouldn't have returned true , but didn't.", Customer1.pickAndExecuteAnAction());
				//Check Name
					assertEquals("Customer should have the name of John. It doesn't", "John", Customer1.getName());
						
	}	
	
	/**
	 * This tests the cashier under very simple terms: 1 MarketCustomer comes to market and Cashier (No Money At All)
	 */
	public void testThreeMarketCustomerHasNoMoneyAtAllScenario()
	{
		ShoppingList = new ArrayList<Item>();
		{
			ShoppingList.add(new Item("Krabby Patty", 0));
			ShoppingList.add(new Item("Kelp Shake", 0));
			ShoppingList.add(new Item("Coral Bits", 0));
			ShoppingList.add(new Item("Kelp Rings", 0));
			ShoppingList.add(new Item("LamboFinny", 1));
			ShoppingList.add(new Item("Toyoda", 0));
		}
		
		Customer1 = new CustomerRole("John", 0, ShoppingList, Customer);	
		MockCustomerGui customerGui = new MockCustomerGui(Customer1);
		Customer1.setCashier(Cashier);
		Customer1.setGui(customerGui);
		Customer1.setPriceList(PriceList);
		Cashier.setCustomer(Customer1);
		
		//check preconditions
			//Check Shoppinglist
				assertEquals("Customer should have 6 Items in the ItemList in it. It doesn't.",ShoppingList.size(), 6);
				for (int i=0;i<ShoppingList.size();i++){
					if (ShoppingList.get(i).name == "LamboFinny")
						assertEquals("Customer should have 1 item in LamboFinny in his Shoppinglist. It doesn't.", ShoppingList.get(i).amount, 1);
					else
						assertEquals("Customer should have 0 item in each type of the item in his Shoppinglist. It doesn't.", ShoppingList.get(i).amount , 0);
				}
			//Check money
				assertEquals("Customer should have $0 in it. It doesn't.", (int) Customer1.getCash(), 0);
			//Check Cashier.PaEaA calls no function (Do nothing)
				assertFalse("Customer's scheduler shouldn't have returned true , but didn't.", Customer1.pickAndExecuteAnAction());
			//Check Name
				assertEquals("Customer should have the name of John. It doesn't", "John", Customer1.getName());
					
		//Step 1 SetCustomer goingToBuy
			Customer1.goingToBuy();
			
		//Check Cashier.PaEaA calls a function (GoFindCashier)
			assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());
		//Check Conditions
			assertEquals("Customer should have the state as EnteringMarket. It doesn't", Customerstate.GoingToOrder, Customer1.getState());
			assertEquals("Customer should have the event as GoingToLine. It doesn't", Customerevent.WaitingInLine, Customer1.getEvent());
		//Check MyCustomerlist
			assertEquals("Customer should have 6 Items in the ItemList in it. It doesn't.",ShoppingList.size(), 6);
		//Check money
			assertEquals("Customer should have $30 in it. It doesn't.", (int) Customer1.getCash(), 30);
			
		//Check Cashier.PaEaA calls a function (OrderItems)
			assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());	
		//Check Conditions
			assertEquals("Customer should have the state as EnteringMarket. It doesn't", Customerstate.Waiting, Customer1.getState());
			assertEquals("Customer should have the event as GoingToLine. It doesn't", Customerevent.Paying, Customer1.getEvent());
		
		//By This time, The customer should have receive the invoice from the Cashier (And the shoppinglist is always fulfilled)
			//Check money
				assertEquals("Customer should have $30 in it. It doesn't.", (int) Customer1.getCash(), 30);
			//Check ActualCost (The invoice)
				assertEquals("Customer should be $300 in it. It doesn't.", (int) Customer1.getActualCost(), 300);
				
			//Check if customer's inventory changes
				for (int i=0;i<ShoppingList.size();i++){
					int CurrentInventoryLevel = Customer.getInventory().get(ShoppingList.get(i).name);
					assertEquals("Customer should have no item in each type of item in his inventorylist. It doesn't.", CurrentInventoryLevel, 0);
				}
			
			
		//Check Cashier.PaEaA calls a function (GoToFindCashier)
			assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());
			
		//By This time, The customer should have pay the Cashier for the items and Collected items (MockCashier is going to give item anyway)
			//Check The InventoryList
			for (int i=0;i<ShoppingList.size();i++){
				int CurrentInventoryLevel = Customer.getInventory().get(ShoppingList.get(i).name);
				if (ShoppingList.get(i).name == "LamboFinny")
					assertEquals("Customer should have 1 item in LamboFinny in his inventorylist. It doesn't.", CurrentInventoryLevel, 1);
				else
					assertEquals("Customer should have no item in each type of item in his inventorylist (except LamboFinny). It doesn't.", CurrentInventoryLevel, 0);	
			}
			//Check money
				assertEquals("Customer should have $0 in it. It doesn't.", (int) Customer1.getCash(), 0);
				
			//Check Conditions
				assertEquals("Customer should have the state as EnteringMarket. It doesn't", Customerstate.Paid, Customer1.getState());
				assertEquals("Customer should have the event as GoingToLine. It doesn't", Customerevent.Leaving, Customer1.getEvent());
			
			//Check Cashier.PaEaA calls a function (Leaving)
				assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());
			
			//Check Conditions
				assertEquals("Customer should have the state as EnteringMarket. It doesn't", Customerstate.NotAtMarket, Customer1.getState());
				assertEquals("Customer should have the event as GoingToLine. It doesn't", Customerevent.doneLeaving, Customer1.getEvent());
				
			//check post-conditions
				//Check InventoryList
					for (int i=0;i<ShoppingList.size();i++){
						int CurrentInventoryLevel = Customer.getInventory().get(ShoppingList.get(i).name);
						if (ShoppingList.get(i).name == "LamboFinny")
							assertEquals("Customer should have 1 item in LamboFinny in his inventorylist. It doesn't.", CurrentInventoryLevel, 1);
						else
							assertEquals("Customer should have no item in each type of item in his inventorylist (except LamboFinny). It doesn't.", CurrentInventoryLevel, 0);	
					}
				//Check money
					assertEquals("Customer should have $0 in it. It doesn't.", (int) Customer1.getCash(), 0);
				//Check Cashier.PaEaA calls no function (Do nothing)
					assertFalse("Customer's scheduler shouldn't have returned true , but didn't.", Customer1.pickAndExecuteAnAction());
				//Check Name
					assertEquals("Customer should have the name of John. It doesn't", "John", Customer1.getName());
						
	}	/**
	 * This tests the cashier under very simple terms: 1 MarketCustomer comes to market and Cashier (ShoppingList is Empty)
	 */
	public void testThreeMarketCustomerShoppingListIsEmptyScenario()
	{
		ShoppingList = new ArrayList<Item>();
		{
			ShoppingList.add(new Item("Krabby Patty", 0));
			ShoppingList.add(new Item("Kelp Shake", 0));
			ShoppingList.add(new Item("Coral Bits", 0));
			ShoppingList.add(new Item("Kelp Rings", 0));
			ShoppingList.add(new Item("LamboFinny", 0));
			ShoppingList.add(new Item("Toyoda", 0));
		}
		
		Customer1 = new CustomerRole("John", 30, ShoppingList, Customer);	
		MockCustomerGui customerGui = new MockCustomerGui(Customer1);
		Customer1.setCashier(Cashier);
		Customer1.setGui(customerGui);
		Customer1.setPriceList(PriceList);
		Cashier.setCustomer(Customer1);
		
		//check preconditions
			//Check Shoppinglist
				assertEquals("Customer should have 6 Items in the ItemList in it. It doesn't.",ShoppingList.size(), 6);
				for (int i=0;i<ShoppingList.size();i++){
					if (ShoppingList.get(i).name == "LamboFinny")
						assertEquals("Customer should have 0 item in LamboFinny in his Shoppinglist. It doesn't.", ShoppingList.get(i).amount, 0);
					else
						assertEquals("Customer should have 0 item in each type of the item in his Shoppinglist. It doesn't.", ShoppingList.get(i).amount , 0);
				}
			//Check money
				assertEquals("Customer should have $30 in it. It doesn't.", (int) Customer1.getCash(), 30);
			//Check Cashier.PaEaA calls no function (Do nothing)
				assertFalse("Customer's scheduler shouldn't have returned true , but didn't.", Customer1.pickAndExecuteAnAction());
			//Check Name
				assertEquals("Customer should have the name of John. It doesn't", "John", Customer1.getName());
					
		//Step 1 SetCustomer goingToBuy
			Customer1.goingToBuy();
			
		//Check Cashier.PaEaA calls a function (GoFindCashier)
			assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());
		//Check Conditions
			assertEquals("Customer should have the state as EnteringMarket. It doesn't", Customerstate.GoingToOrder, Customer1.getState());
			assertEquals("Customer should have the event as GoingToLine. It doesn't", Customerevent.WaitingInLine, Customer1.getEvent());
		//Check MyCustomerlist
			assertEquals("Customer should have 6 Items in the ItemList in it. It doesn't.",ShoppingList.size(), 6);
		//Check money
			assertEquals("Customer should have $30 in it. It doesn't.", (int) Customer1.getCash(), 30);
			
		//Check Cashier.PaEaA calls a function (OrderItems)
			assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());	
		//Check Conditions
			assertEquals("Customer should have the state as EnteringMarket. It doesn't", Customerstate.Waiting, Customer1.getState());
			assertEquals("Customer should have the event as GoingToLine. It doesn't", Customerevent.Paying, Customer1.getEvent());
		
		//By This time, The customer should have receive the invoice from the Cashier (And the shoppinglist is always fulfilled)
			//Check money
				assertEquals("Customer should have $30 in it. It doesn't.", (int) Customer1.getCash(), 30);
			//Check ActualCost (The invoice)
				assertEquals("Customer should be $0 in it. It doesn't.", (int) Customer1.getActualCost(), 0);
				
			//Check if customer's inventory changes
				for (int i=0;i<ShoppingList.size();i++){
					int CurrentInventoryLevel = Customer.getInventory().get(ShoppingList.get(i).name);
					assertEquals("Customer should have no item in each type of item in his inventorylist. It doesn't.", CurrentInventoryLevel, 0);
				}
			
			
		//Check Cashier.PaEaA calls a function (GoToFindCashier)
			assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());
			
		//By This time, The customer should have pay the Cashier for the items and Collected items
			//Check The InventoryList
			for (int i=0;i<ShoppingList.size();i++){
				int CurrentInventoryLevel = Customer.getInventory().get(ShoppingList.get(i).name);
				if (ShoppingList.get(i).name == "LamboFinny")
					assertEquals("Customer should have 0 item in LamboFinny in his inventorylist. It doesn't.", CurrentInventoryLevel, 0);
				else
					assertEquals("Customer should have no item in each type of item in his inventorylist (except LamboFinny). It doesn't.", CurrentInventoryLevel, 0);	
			}
			//Check money
				assertEquals("Customer should have $30 in it. It doesn't.", (int) Customer1.getCash(), 30);
				
			//Check Conditions
				assertEquals("Customer should have the state as EnteringMarket. It doesn't", Customerstate.Paid, Customer1.getState());
				assertEquals("Customer should have the event as GoingToLine. It doesn't", Customerevent.Leaving, Customer1.getEvent());
			
			//Check Cashier.PaEaA calls a function (Leaving)
				assertTrue("Customer's scheduler shouldn't have returned false , but didn't.", Customer1.pickAndExecuteAnAction());
			
			//Check Conditions
				assertEquals("Customer should have the state as EnteringMarket. It doesn't", Customerstate.NotAtMarket, Customer1.getState());
				assertEquals("Customer should have the event as GoingToLine. It doesn't", Customerevent.doneLeaving, Customer1.getEvent());
				
			//check post-conditions
				//Check InventoryList
					for (int i=0;i<ShoppingList.size();i++){
						int CurrentInventoryLevel = Customer.getInventory().get(ShoppingList.get(i).name);
						if (ShoppingList.get(i).name == "LamboFinny")
							assertEquals("Customer should have 0 item in LamboFinny in his inventorylist. It doesn't.", CurrentInventoryLevel, 0);
						else
							assertEquals("Customer should have no item in each type of item in his inventorylist (except LamboFinny). It doesn't.", CurrentInventoryLevel, 0);	
					}
				//Check money
					assertEquals("Customer should have $30 in it. It doesn't.", (int) Customer1.getCash(), 30);
				//Check Cashier.PaEaA calls no function (Do nothing)
					assertFalse("Customer's scheduler shouldn't have returned true , but didn't.", Customer1.pickAndExecuteAnAction());
				//Check Name
					assertEquals("Customer should have the name of John. It doesn't", "John", Customer1.getName());
						
	}	
	
	
	
	
	
	
	
	
	
	
	
}