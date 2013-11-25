package market.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agent.PersonAgent;
import junit.framework.TestCase;
import market.CashierRole;
import market.CashierRole.Cashierstate;
import market.Item;
import market.CashierRole.Customerstate;
import market.gui.CashierGui;
import market.gui.MarketBuilding;
import market.test.mock.MockCashierGui;
import market.test.mock.MockItemCollector;
import market.test.mock.MockCustomer;

public class CashierTest extends TestCase
{
	CashierRole Cashier;
	MockCashierGui cashierGui;
	MockCustomer Customer1;
	MockCustomer Customer2;
	MockItemCollector ItemCollector1;
	MockItemCollector ItemCollector2;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		

		PersonAgent cashier = new PersonAgent ("John");
		Cashier = new CashierRole("John", 100, cashier, new MarketBuilding(1,1,1,1));	
		
		Customer1 = new MockCustomer("mockcustomer");		
		Customer2 = new MockCustomer("mockcustomer");
		ItemCollector1 = new MockItemCollector("ItemCollector1");
		ItemCollector2 = new MockItemCollector("ItemCollector2");
		
		
	}	
	
	/**
	 * This tests the cashier under very simple terms: 1 MarketCustomer comes to market and 1 ItemCollector (Enough Inventory)
	 */
	public void testOne1MarketCustomer1ItemCollectorScenario()
	{
		
		Customer1.Cashier = Cashier;
		Customer2.Cashier = Cashier;
		ItemCollector1.Cashier = Cashier;
		ItemCollector1.setInventoryList(Cashier.getInventoryList());
		ItemCollector1.setInventoryList(Cashier.getInventoryList());
		List<Item> tempInventoryList = new ArrayList<Item>();
		{
			tempInventoryList.add(new Item("Kelp Rings", 1));
			tempInventoryList.add(new Item("LamboFinny", 0));
			tempInventoryList.add(new Item("Kelp Shake", 1));
			tempInventoryList.add(new Item("Toyoda", 1));
			tempInventoryList.add(new Item("Krabby Patty", 0));
			tempInventoryList.add(new Item("Coral Bits", 0));
		}
		cashierGui = new MockCashierGui(Cashier);
		Cashier.setGui(cashierGui);
		if (Cashier.getGui() == null)
			System.out.println("Its null");
		
		//check preconditions
		//Check MyCustomerlist
			assertEquals("Cashier should have 0 Customer in it. It doesn't.",Cashier.getMyCustomerList().size(), 0);
		//Check MyIClist
			assertEquals("Cashier should have 0 IC in it. It doesn't.",Cashier.getICList().size(), 0);
		//Check MyDGlist
			assertEquals("Cashier should have 0 DG in it. It doesn't.",Cashier.getDGList().size(), 0);
		//Check MyName
			assertEquals("Cashier should have name of John in it. It doesn't.",Cashier.getName(), "John");
		//Check money
			assertEquals("Cashier should have $100 in it. It doesn't.", (int) Cashier.getCash(), 100);
		//Check Cashier.PaEaA calls no function (Do nothing)
			assertFalse("Cashier's scheduler shouldn't have returned true , but didn't.", Cashier.pickAndExecuteAnAction());
			
			Cashier.setState(Cashierstate.Idle);
			
		//Step 1
			Cashier.addICList(ItemCollector1, Cashier.getInventoryList());
		//Check MyIClist
			assertEquals("Cashier should have 1 IC in it. It doesn't.",Cashier.getICList().size(), 1);
		//Check Cashier.PaEaA calls no function (Do nothing)
			assertFalse("Cashier's scheduler shouldn't have returned true , but didn't.", Cashier.pickAndExecuteAnAction());

		//Step 2
			Cashier.msgIWantItem(tempInventoryList, Customer1);
		//Check MyCustomerList	
			assertEquals("Cashier should have 1 Customer in it. It doesn't.",Cashier.getMyCustomerList().size(), 1);
		//Check money
			assertEquals("Cashier should have $100 in it. It doesn't.", (int) Cashier.getCash(), 100);
		//Check MyDGlist
			assertEquals("Cashier should have 0 DG in it. It doesn't.",Cashier.getDGList().size(), 0);
		//Check MyIClist
			assertEquals("Cashier should have 1 IC in it. It doesn't.",Cashier.getICList().size(), 1);
		//Check Cashier.PaEaA calls a function (GoGetItem)
			assertTrue("Cashier's scheduler shouldn't have returned false , but didn't.", Cashier.pickAndExecuteAnAction());
		
		//Checking the DeliveryList
			assertEquals("The second one in the Delivery List should be ExpensiveCar",Cashier.getMyCustomerList().get(0).getDeliveryList().get(1).name, "LamboFinny");
			assertEquals("The Expensive Car should be having an amount 0",Cashier.getMyCustomerList().get(0).getDeliveryList().get(1).amount, 0);
			assertEquals("The first one in the Delivery List should be CheapCar",Cashier.getMyCustomerList().get(0).getDeliveryList().get(3).name, "Toyoda");
			assertEquals("The Cheap Car should be having an amount 1",Cashier.getMyCustomerList().get(0).getDeliveryList().get(3).amount, 1);
			assertEquals("The third one in the Delivery List should be KrabbyPatty",Cashier.getMyCustomerList().get(0).getDeliveryList().get(4).name, "Krabby Patty");
			assertEquals("The Expensive Car should be having an amount 1",Cashier.getMyCustomerList().get(0).getDeliveryList().get(4).amount, 0);
			assertEquals("The fourth one in the Delivery List should be KelpShake",Cashier.getMyCustomerList().get(0).getDeliveryList().get(2).name, "Kelp Shake");
			assertEquals("The Expensive Car should be having an amount 1",Cashier.getMyCustomerList().get(0).getDeliveryList().get(2).amount, 1);
			assertEquals("The fifth one in the Delivery List should be CoralBits",Cashier.getMyCustomerList().get(0).getDeliveryList().get(5).name, "Coral Bits");
			assertEquals("The Expensive Car should be having an amount 0",Cashier.getMyCustomerList().get(0).getDeliveryList().get(5).amount, 0);
			assertEquals("The fifth one in the Delivery List should be KelpRings",Cashier.getMyCustomerList().get(0).getDeliveryList().get(0).name, "Kelp Rings");
			assertEquals("The Expensive Car should be having an amount 0",Cashier.getMyCustomerList().get(0).getDeliveryList().get(0).amount, 1);

			
		//Checking The State **Collected**
			boolean stateTest = false;
			if (Cashier.getMyCustomerList().get(0).state == Customerstate.Collected)
				stateTest = true;
			assertTrue("The state of the Cashier Customer should be Collected, but it doesn't", stateTest);
			
		//Check Cashier.PaEaA calls a function (CalculatePayment)
			assertTrue("Cashier's scheduler should have returned true , but didn't.", Cashier.pickAndExecuteAnAction());
			
		//Checking The State **Paid**
			boolean stateTest1 = false;
			if (Cashier.getMyCustomerList().get(0).state == Customerstate.Paid)
				stateTest1 = true;
			assertTrue("The state of the Cashier Customer should be Collected, but it doesn't", stateTest1);
			
		//Check MyCustomerlist
			assertEquals("Cashier should have 1 Customer in it. It doesn't.",Cashier.getMyCustomerList().size(), 1);
		//Check Cashier.PaEaA calls a function (GiveItems)
			assertTrue("Cashier's scheduler shouldn't have returned false , but it did.", Cashier.pickAndExecuteAnAction());
		
		//Check Cashier .PaEaA calls no function (Nothing)
			assertFalse("Cashier's scheduler shouldn't have returned true , but it did.", Cashier.pickAndExecuteAnAction());
			
		//check postconditions
		//Check MyCustomerlist
			assertEquals("Cashier should have 0 Customer in it. It doesn't.",Cashier.getMyCustomerList().size(), 0);
		//Check MyIClist
			assertEquals("Cashier should have 0 IC in it. It doesn't.",Cashier.getICList().size(), 1);
		//Check MyDGlist
			assertEquals("Cashier should have 0 DG in it. It doesn't.",Cashier.getDGList().size(), 0);
		//Check MyName
			assertEquals("Cashier should have name of John in it. It doesn't.",Cashier.getName(), "John");
		//Check money
			assertEquals("Cashier should have $220 in it. It doesn't.", (int) Cashier.getCash(), 215);
		//Check Cashier.PaEaA calls no function (Do nothing)
			assertFalse("Cashier's scheduler shouldn't have returned true , but didn't.", Cashier.pickAndExecuteAnAction());
			
			
	}
	/**
	 * This tests the cashier under very simple terms: 1 MarketCustomer comes to market and 1 ItemCollector (NotEnoughItems)
	 */
	public void testTwo1MarketCustomer1ItemCollectorNotEnoughScenario()
	{
		Customer1.Cashier = Cashier;
		Customer2.Cashier = Cashier;
		ItemCollector1.Cashier = Cashier;
		ItemCollector2.Cashier = Cashier;
		List<Item> tempInventoryList = new ArrayList<Item>();
		{
			tempInventoryList.add(new Item("Kelp Rings", 1));
			tempInventoryList.add(new Item("LamboFinny", 0));
			tempInventoryList.add(new Item("Kelp Shake", 1));
			tempInventoryList.add(new Item("Toyoda", 0));
			tempInventoryList.add(new Item("Krabby Patty", 1));
			tempInventoryList.add(new Item("Coral Bits", 1));
		}
		
		cashierGui = new MockCashierGui(Cashier);
		Cashier.setGui(cashierGui);
		if (Cashier.getGui() == null)
			System.out.println("Its null");
		
		//check preconditions
		//Check MyCustomerlist
			assertEquals("Cashier should have 0 Customer in it. It doesn't.",Cashier.getMyCustomerList().size(), 0);
		//Check MyIClist
			assertEquals("Cashier should have 0 IC in it. It doesn't.",Cashier.getICList().size(), 0);
		//Check MyDGlist
			assertEquals("Cashier should have 0 DG in it. It doesn't.",Cashier.getDGList().size(), 0);
		//Check MyName
			assertEquals("Cashier should have name of John in it. It doesn't.",Cashier.getName(), "John");
		//Check money
			assertEquals("Cashier should have $100 in it. It doesn't.", (int) Cashier.getCash(), 100);
		//Check Cashier.PaEaA calls no function (Do nothing)
			assertFalse("Cashier's scheduler shouldn't have returned true , but didn't.", Cashier.pickAndExecuteAnAction());
			
			Cashier.setState(Cashierstate.Idle);
			Map<String,Integer> InventoryList = new HashMap<String,Integer>();
			{		//Initially The market has 100 inventory on each Item
				for (int i = 0 ; i < agent.Constants.FOODS.size(); i++){
					InventoryList.put(agent.Constants.FOODS.get(i), 100);
				}
				for (int i = 0 ; i < agent.Constants.CARS.size(); i++){
					InventoryList.put(agent.Constants.CARS.get(i), 0);
				}
			}
			
		//Step 1
			Cashier.addICList(ItemCollector1, InventoryList);
		//Check MyIClist
			assertEquals("Cashier should have 1 IC in it. It doesn't.",Cashier.getICList().size(), 1);
		//Check Cashier.PaEaA calls no function (Do nothing)
			assertFalse("Cashier's scheduler shouldn't have returned true , but didn't.", Cashier.pickAndExecuteAnAction());

		//Step 2
			Cashier.msgIWantItem(tempInventoryList, Customer1);
		//Check MyCustomerList	
			assertEquals("Cashier should have 1 Customer in it. It doesn't.",Cashier.getMyCustomerList().size(), 1);
		//Check money
			assertEquals("Cashier should have $100 in it. It doesn't.", (int) Cashier.getCash(), 100);
		//Check MyDGlist
			assertEquals("Cashier should have 0 DG in it. It doesn't.",Cashier.getDGList().size(), 0);
		//Check MyIClist
			assertEquals("Cashier should have 1 IC in it. It doesn't.",Cashier.getICList().size(), 1);
		//Check Cashier.PaEaA calls a function (GoGetItem)
			assertTrue("Cashier's scheduler shouldn't have returned false , but didn't.", Cashier.pickAndExecuteAnAction());
		
		//Checking the DeliveryList
			assertEquals("The second one in the Delivery List should be ExpensiveCar",Cashier.getMyCustomerList().get(0).getDeliveryList().get(1).name, "LamboFinny");
			assertEquals("The Expensive Car should be having an amount 0",Cashier.getMyCustomerList().get(0).getDeliveryList().get(1).amount, 0);
			assertEquals("The first one in the Delivery List should be CheapCar",Cashier.getMyCustomerList().get(0).getDeliveryList().get(3).name, "Toyoda");
			assertEquals("The Cheap Car should be having an amount 1",Cashier.getMyCustomerList().get(0).getDeliveryList().get(3).amount, 0);
			assertEquals("The third one in the Delivery List should be KrabbyPatty",Cashier.getMyCustomerList().get(0).getDeliveryList().get(4).name, "Krabby Patty");
			assertEquals("The Expensive Car should be having an amount 1",Cashier.getMyCustomerList().get(0).getDeliveryList().get(4).amount, 1);
			assertEquals("The fourth one in the Delivery List should be KelpShake",Cashier.getMyCustomerList().get(0).getDeliveryList().get(2).name, "Kelp Shake");
			assertEquals("The Expensive Car should be having an amount 1",Cashier.getMyCustomerList().get(0).getDeliveryList().get(2).amount, 1);
			assertEquals("The fifth one in the Delivery List should be CoralBits",Cashier.getMyCustomerList().get(0).getDeliveryList().get(5).name, "Coral Bits");
			assertEquals("The Expensive Car should be having an amount 0",Cashier.getMyCustomerList().get(0).getDeliveryList().get(5).amount, 1);
			assertEquals("The fifth one in the Delivery List should be KelpRings",Cashier.getMyCustomerList().get(0).getDeliveryList().get(0).name, "Kelp Rings");
			assertEquals("The Expensive Car should be having an amount 0",Cashier.getMyCustomerList().get(0).getDeliveryList().get(0).amount, 1);
			
		//Checking The State **Collected**
			boolean stateTest = false;
			if (Cashier.getMyCustomerList().get(0).state == Customerstate.Collected)
				stateTest = true;
			assertTrue("The state of the Cashier Customer should be Collected, but it doesn't", stateTest);
			
		//Check Cashier.PaEaA calls a function (CalculatePayment)
			assertTrue("Cashier's scheduler should have returned true , but didn't.", Cashier.pickAndExecuteAnAction());
			
		//Checking The State **Paid**
			boolean stateTest1 = false;
			if (Cashier.getMyCustomerList().get(0).state == Customerstate.Paid)
				stateTest1 = true;
			assertTrue("The state of the Cashier Customer should be Collected, but it doesn't", stateTest1);
			
		//Check MyCustomerlist
			assertEquals("Cashier should have 1 Customer in it. It doesn't.",Cashier.getMyCustomerList().size(), 1);
		//Check Cashier.PaEaA calls a function (GiveItems)
			assertTrue("Cashier's scheduler shouldn't have returned false , but it did.", Cashier.pickAndExecuteAnAction());
		
		//Check Cashier .PaEaA calls no function (Nothing)
			assertFalse("Cashier's scheduler shouldn't have returned true , but it did.", Cashier.pickAndExecuteAnAction());
			
		//check postconditions
		//Check MyCustomerlist
			assertEquals("Cashier should have 0 Customer in it. It doesn't.",Cashier.getMyCustomerList().size(), 0);
		//Check MyIClist
			assertEquals("Cashier should have 0 IC in it. It doesn't.",Cashier.getICList().size(), 1);
		//Check MyDGlist
			assertEquals("Cashier should have 0 DG in it. It doesn't.",Cashier.getDGList().size(), 0);
		//Check MyName
			assertEquals("Cashier should have name of John in it. It doesn't.",Cashier.getName(), "John");
		//Check money
			assertEquals("Cashier should have $150 in it. It doesn't.", (int) Cashier.getCash(), 150);
		//Check Cashier.PaEaA calls no function (Do nothing)
			assertFalse("Cashier's scheduler shouldn't have returned true , but didn't.", Cashier.pickAndExecuteAnAction());
			
			
	}
	
	/**
	 * This tests the cashier under very simple terms: 1 MarketCustomer comes to market and 1 ItemCollector (Only 1 item)
	 */
	public void testThree1MarketCustomer1ItemCollectorOnly1ItemScenario()
	{
		Customer1.Cashier = Cashier;
		Customer2.Cashier = Cashier;
		ItemCollector1.Cashier = Cashier;
		ItemCollector2.Cashier = Cashier;
		List<Item> tempInventoryList = new ArrayList<Item>();
		{
			tempInventoryList.add(new Item("Kelp Rings", 1));
			tempInventoryList.add(new Item("LamboFinny", 0));
			tempInventoryList.add(new Item("Kelp Shake", 0));
			tempInventoryList.add(new Item("Toyoda", 0));
			tempInventoryList.add(new Item("Krabby Patty", 0));
			tempInventoryList.add(new Item("Coral Bits", 0));
		}
		
		cashierGui = new MockCashierGui(Cashier);
		Cashier.setGui(cashierGui);
		if (Cashier.getGui() == null)
			System.out.println("Its null");
		
		//check preconditions
		//Check MyCustomerlist
			assertEquals("Cashier should have 0 Customer in it. It doesn't.",Cashier.getMyCustomerList().size(), 0);
		//Check MyIClist
			assertEquals("Cashier should have 0 IC in it. It doesn't.",Cashier.getICList().size(), 0);
		//Check MyDGlist
			assertEquals("Cashier should have 0 DG in it. It doesn't.",Cashier.getDGList().size(), 0);
		//Check MyName
			assertEquals("Cashier should have name of John in it. It doesn't.",Cashier.getName(), "John");
		//Check money
			assertEquals("Cashier should have $100 in it. It doesn't.", (int) Cashier.getCash(), 100);
		//Check Cashier.PaEaA calls no function (Do nothing)
			assertFalse("Cashier's scheduler shouldn't have returned true , but didn't.", Cashier.pickAndExecuteAnAction());
			
			Cashier.setState(Cashierstate.Idle);
			Map<String,Integer> InventoryList = new HashMap<String,Integer>();
			{		//Initially The market has 100 inventory on each Item
				for (int i = 0 ; i < agent.Constants.FOODS.size(); i++){
					InventoryList.put(agent.Constants.FOODS.get(i), 100);
				}
				for (int i = 0 ; i < agent.Constants.CARS.size(); i++){
					InventoryList.put(agent.Constants.CARS.get(i), 0);
				}
			}
			
		//Step 1
			Cashier.addICList(ItemCollector1, InventoryList);
		//Check MyIClist
			assertEquals("Cashier should have 1 IC in it. It doesn't.",Cashier.getICList().size(), 1);
		//Check Cashier.PaEaA calls no function (Do nothing)
			assertFalse("Cashier's scheduler shouldn't have returned true , but didn't.", Cashier.pickAndExecuteAnAction());

		//Step 2
			Cashier.msgIWantItem(tempInventoryList, Customer1);
		//Check MyCustomerList	
			assertEquals("Cashier should have 1 Customer in it. It doesn't.",Cashier.getMyCustomerList().size(), 1);
		//Check money
			assertEquals("Cashier should have $100 in it. It doesn't.", (int) Cashier.getCash(), 100);
		//Check MyDGlist
			assertEquals("Cashier should have 0 DG in it. It doesn't.",Cashier.getDGList().size(), 0);
		//Check MyIClist
			assertEquals("Cashier should have 1 IC in it. It doesn't.",Cashier.getICList().size(), 1);
		//Check Cashier.PaEaA calls a function (GoGetItem)
			assertTrue("Cashier's scheduler shouldn't have returned false , but didn't.", Cashier.pickAndExecuteAnAction());
		
		//Checking the DeliveryList
			assertEquals("The second one in the Delivery List should be ExpensiveCar",Cashier.getMyCustomerList().get(0).getDeliveryList().get(1).name, "LamboFinny");
			assertEquals("The Expensive Car should be having an amount 0",Cashier.getMyCustomerList().get(0).getDeliveryList().get(1).amount, 0);
			assertEquals("The first one in the Delivery List should be CheapCar",Cashier.getMyCustomerList().get(0).getDeliveryList().get(3).name, "Toyoda");
			assertEquals("The Cheap Car should be having an amount 1",Cashier.getMyCustomerList().get(0).getDeliveryList().get(3).amount, 0);
			assertEquals("The third one in the Delivery List should be KrabbyPatty",Cashier.getMyCustomerList().get(0).getDeliveryList().get(4).name, "Krabby Patty");
			assertEquals("The Expensive Car should be having an amount 1",Cashier.getMyCustomerList().get(0).getDeliveryList().get(4).amount, 0);
			assertEquals("The fourth one in the Delivery List should be KelpShake",Cashier.getMyCustomerList().get(0).getDeliveryList().get(2).name, "Kelp Shake");
			assertEquals("The Expensive Car should be having an amount 1",Cashier.getMyCustomerList().get(0).getDeliveryList().get(2).amount, 0);
			assertEquals("The fifth one in the Delivery List should be CoralBits",Cashier.getMyCustomerList().get(0).getDeliveryList().get(5).name, "Coral Bits");
			assertEquals("The Expensive Car should be having an amount 0",Cashier.getMyCustomerList().get(0).getDeliveryList().get(5).amount, 0);
			assertEquals("The fifth one in the Delivery List should be KelpRings",Cashier.getMyCustomerList().get(0).getDeliveryList().get(0).name, "Kelp Rings");
			assertEquals("The Expensive Car should be having an amount 0",Cashier.getMyCustomerList().get(0).getDeliveryList().get(0).amount, 1);
			
		//Checking The State **Collected**
			boolean stateTest = false;
			if (Cashier.getMyCustomerList().get(0).state == Customerstate.Collected)
				stateTest = true;
			assertTrue("The state of the Cashier Customer should be Collected, but it doesn't", stateTest);
			
		//Check Cashier.PaEaA calls a function (CalculatePayment)
			assertTrue("Cashier's scheduler should have returned true , but didn't.", Cashier.pickAndExecuteAnAction());
			
		//Checking The State **Paid**
			boolean stateTest1 = false;
			if (Cashier.getMyCustomerList().get(0).state == Customerstate.Paid)
				stateTest1 = true;
			assertTrue("The state of the Cashier Customer should be Collected, but it doesn't", stateTest1);
			
		//Check MyCustomerlist
			assertEquals("Cashier should have 1 Customer in it. It doesn't.",Cashier.getMyCustomerList().size(), 1);
		//Check Cashier.PaEaA calls a function (GiveItems)
			assertTrue("Cashier's scheduler shouldn't have returned false , but it did.", Cashier.pickAndExecuteAnAction());
		
		//Check Cashier .PaEaA calls no function (Nothing)
			assertFalse("Cashier's scheduler shouldn't have returned true , but it did.", Cashier.pickAndExecuteAnAction());
			
		//check postconditions
		//Check MyCustomerlist
			assertEquals("Cashier should have 0 Customer in it. It doesn't.",Cashier.getMyCustomerList().size(), 0);
		//Check MyIClist
			assertEquals("Cashier should have 0 IC in it. It doesn't.",Cashier.getICList().size(), 1);
		//Check MyDGlist
			assertEquals("Cashier should have 0 DG in it. It doesn't.",Cashier.getDGList().size(), 0);
		//Check MyName
			assertEquals("Cashier should have name of John in it. It doesn't.",Cashier.getName(), "John");
		//Check money
			assertEquals("Cashier should have $105 in it. It doesn't.", (int) Cashier.getCash(), 105);
		//Check Cashier.PaEaA calls no function (Do nothing)
			assertFalse("Cashier's scheduler shouldn't have returned true , but didn't.", Cashier.pickAndExecuteAnAction());
			
			
	}
	
}