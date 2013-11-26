package market.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agent.PersonAgent;
import junit.framework.TestCase;
import market.Item;
import market.ItemCollectorRole;
import market.CashierRole.Cashierstate;
import market.ItemCollectorRole.ItemCollectorstate;
import market.gui.MarketBuilding;
import market.interfaces.ItemCollector;
import market.interfaces.ItemCollectorGuiInterfaces;
import market.test.mock.MockCashier;
import market.test.mock.MockCashierGui;
import market.test.mock.MockCustomer;
import market.test.mock.MockItemCollectorGui;

public class ItemCollectorTest extends TestCase
{
	MockCashier Cashier = new MockCashier("MockCashier");
	MockCashierGui cashierGui;
	MockCustomer Customer1;
	MockCustomer Customer2;
	ItemCollectorRole ItemCollector;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		

		PersonAgent ItemCollectorGuy = new PersonAgent ("John");
		ItemCollector = new ItemCollectorRole(ItemCollectorGuy, new MarketBuilding(1,1,1,1));	
		ItemCollectorGuiInterfaces itemCollectorGui = new MockItemCollectorGui(ItemCollector);
		ItemCollector.setGui(itemCollectorGui);
		ItemCollector.setCashier(Cashier);
		Customer1 = new MockCustomer("mockcustomer");		
		Customer2 = new MockCustomer("mockcustomer");

		
	}	
	
	/**
	 * This tests the cashier under very simple terms: 1 MarketCashier in the market and ask the only 1 ItemCollector to get items(Enough Inventory)
	 */
	public void testOneEnoughItemsScenario()
	{
		
			//Map<String, Integer> InventoryList
		Map<String,Integer> InventoryList = new HashMap<String,Integer>();
		{		//Initially The market has 100 inventory on each Item
			for (int i = 0 ; i < CommonSimpleClasses.Constants.FOODS.size(); i++){
				InventoryList.put(CommonSimpleClasses.Constants.FOODS.get(i), 100);
			}
			for (int i = 0 ; i < CommonSimpleClasses.Constants.CARS.size(); i++){
				InventoryList.put(CommonSimpleClasses.Constants.CARS.get(i), 100);
			}
		}
		ItemCollector.setInventoryList(InventoryList);
			//Walk to the Ready Station (He is Ready)
		ItemCollector.setState(ItemCollectorstate.Idle);
		
		List<Item> collectingItemList = new ArrayList<Item>();
		collectingItemList.add(new Item("Kelp Rings", 40));
		collectingItemList.add(new Item("LamboFinny", 30));
		collectingItemList.add(new Item("Kelp Shake", 20));
		collectingItemList.add(new Item("Toyoda", 15));
		collectingItemList.add(new Item("Krabby Patty", 10));
		collectingItemList.add(new Item("Coral Bits", 0));
		
		//check preconditions
				//Check Orders
					assertEquals("ItemCollector should have 0 order in List of Orders. It doesn't.",ItemCollector.getOrders().size(), 0);
				//Check Cashier
					assertEquals("ItemCollector should have a non-null Cashier in it. It doesn't.",ItemCollector.getCashier() , Cashier);
				//Check InventoryList
					assertEquals("ItemCollector's InventoryList should have 100 Kelp Rings in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Rings"), 100);
					assertEquals("ItemCollector's InventoryList should have 100 LamboFinny in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("LamboFinny"), 100);
					assertEquals("ItemCollector's InventoryList should have 100 Kelp Shake in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Shake"), 100);
					assertEquals("ItemCollector's InventoryList should have 100 Toyoda in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Toyoda"), 100);
					assertEquals("ItemCollector's InventoryList should have 100 Krabby Patty in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Krabby Patty"), 100);
					assertEquals("ItemCollector's InventoryList should have 100 Coral Bits in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Coral Bits"), 100);
				//Check Cashier.PaEaA calls no function (Do nothing)
					assertFalse("Cashier's scheduler shouldn't have returned true , but didn't.", ItemCollector.pickAndExecuteAnAction());
		
		//Step 1
				ItemCollector.msgGetTheseItem(collectingItemList);
		
				//Check Orders
					assertEquals("ItemCollector should have 1 order in List of Orders. It doesn't.",ItemCollector.getOrders().size(), 1);
				//Check ItemCollector.PaEaA calls a function (GoGetItems)	
					assertTrue("Cashier's scheduler shouldn't have returned true , but didn't.", ItemCollector.pickAndExecuteAnAction());
					
				//Check Orders (Got removed the order from Orders)
					assertEquals("ItemCollector should have 1 order in List of Orders. It doesn't.",ItemCollector.getOrders().size(), 1);
				//Check InventoryList should be decreased
					assertEquals("ItemCollector's InventoryList should have 60 Kelp Rings in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Rings"), 60);
					assertEquals("ItemCollector's InventoryList should have 70 LamboFinny in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("LamboFinny"), 70);
					assertEquals("ItemCollector's InventoryList should have 80 Kelp Shake in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Shake"), 80);
					assertEquals("ItemCollector's InventoryList should have 85 Toyoda in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Toyoda"), 85);
					assertEquals("ItemCollector's InventoryList should have 90 Krabby Patty in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Krabby Patty"), 90);
					assertEquals("ItemCollector's InventoryList should have 100 Coral Bits in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Coral Bits"), 100);
				
				//Check ItemCollector.PaEaA calls a function (GoGetItems)	
					assertTrue("Cashier's scheduler shouldn't have returned true , but didn't.", ItemCollector.pickAndExecuteAnAction());
					
				//Check DeliverList if it is correct
					assertEquals("ItemCollector's DeliverList should have 40 Kelp Rings in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(0).amount, 40);
					assertEquals("ItemCollector's DeliverList should have 30 LamboFinny in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(1).amount, 30);
					assertEquals("ItemCollector's DeliverList should have 20 Kelp Shake in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(2).amount, 20);
					assertEquals("ItemCollector's DeliverList should have 15 Toyoda in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(3).amount, 15);
					assertEquals("ItemCollector's DeliverList should have 10 Krabby Patty in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(4).amount, 10);
					assertEquals("ItemCollector's DeliverList should have 0 Coral Bits in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(5).amount, 0);
				//Check MissingItemList if it is correct
					assertEquals("ItemCollector's MissingItemList should have a list size of 0. It doesn't", (int) Cashier.MissingList.size(), 0);
					/*
					assertEquals("ItemCollector's InventoryList should have 0 Kelp Rings in the InventoryList initially. It doesn't", (int) Cashier.MissingList.get(0).amount, 0);
					assertEquals("ItemCollector's InventoryList should have 0 LamboFinny in the InventoryList initially. It doesn't", (int) Cashier.MissingList.get(1).amount, 0);
					assertEquals("ItemCollector's InventoryList should have 0 Kelp Shake in the InventoryList initially. It doesn't", (int) Cashier.MissingList.get(2).amount, 0);
					assertEquals("ItemCollector's InventoryList should have 0 Toyoda in the InventoryList initially. It doesn't", (int) Cashier.MissingList.get(3).amount, 0);
					assertEquals("ItemCollector's InventoryList should have 0 Krabby Patty in the InventoryList initially. It doesn't", (int) Cashier.MissingList.get(4).amount, 0);
					assertEquals("ItemCollector's InventoryList should have 0 Coral Bits in the InventoryList initially. It doesn't", (int) Cashier.MissingList.get(5).amount, 0);
					*/
				
		//Check post-conditions
					//Check Orders
						assertEquals("ItemCollector should have 0 order in List of Orders. It doesn't.",ItemCollector.getOrders().size(), 0);
					//Check Cashier
						assertEquals("ItemCollector should have a non-null Cashier in it. It doesn't.",ItemCollector.getCashier() , Cashier);
					//Check InventoryList
						assertEquals("ItemCollector's InventoryList should have 60 Kelp Rings in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Rings"), 60);
						assertEquals("ItemCollector's InventoryList should have 70 LamboFinny in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("LamboFinny"), 70);
						assertEquals("ItemCollector's InventoryList should have 80 Kelp Shake in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Shake"), 80);
						assertEquals("ItemCollector's InventoryList should have 85 Toyoda in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Toyoda"), 85);
						assertEquals("ItemCollector's InventoryList should have 90 Krabby Patty in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Krabby Patty"), 90);
						assertEquals("ItemCollector's InventoryList should have 100 Coral Bits in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Coral Bits"), 100);
					//Check Cashier.PaEaA calls no function (Do nothing)
						assertFalse("Cashier's scheduler shouldn't have returned true , but didn't.", ItemCollector.pickAndExecuteAnAction());
					
	}
	/**
	 * The Inventories have not enough items to satisfy the cashier's order demands
	 */
	public void test2NotEnoughItemScenario()
	{
		
			//Map<String, Integer> InventoryList
		Map<String,Integer> InventoryList = new HashMap<String,Integer>();
		{		//Initially The market has 100 inventory on each Item
			for (int i = 0 ; i < CommonSimpleClasses.Constants.FOODS.size(); i++){
				InventoryList.put(CommonSimpleClasses.Constants.FOODS.get(i), 100);
			}
			for (int i = 0 ; i < CommonSimpleClasses.Constants.CARS.size(); i++){
				InventoryList.put(CommonSimpleClasses.Constants.CARS.get(i), 100);
			}
		}
		ItemCollector.setInventoryList(InventoryList);
			//Walk to the Ready Station (He is Ready)
		ItemCollector.setState(ItemCollectorstate.Idle);
		
		List<Item> collectingItemList = new ArrayList<Item>();
		collectingItemList.add(new Item("Kelp Rings", 40));
		collectingItemList.add(new Item("LamboFinny", 30));
		collectingItemList.add(new Item("Kelp Shake", 20));
		collectingItemList.add(new Item("Toyoda", 15));
		collectingItemList.add(new Item("Krabby Patty", 200));
		collectingItemList.add(new Item("Coral Bits", 105));
		
		//check preconditions
				//Check Orders
					assertEquals("ItemCollector should have 0 order in List of Orders. It doesn't.",ItemCollector.getOrders().size(), 0);
				//Check Cashier
					assertEquals("ItemCollector should have a non-null Cashier in it. It doesn't.",ItemCollector.getCashier() , Cashier);
				//Check InventoryList
					assertEquals("ItemCollector's InventoryList should have 100 Kelp Rings in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Rings"), 100);
					assertEquals("ItemCollector's InventoryList should have 100 LamboFinny in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("LamboFinny"), 100);
					assertEquals("ItemCollector's InventoryList should have 100 Kelp Shake in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Shake"), 100);
					assertEquals("ItemCollector's InventoryList should have 100 Toyoda in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Toyoda"), 100);
					assertEquals("ItemCollector's InventoryList should have 100 Krabby Patty in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Krabby Patty"), 100);
					assertEquals("ItemCollector's InventoryList should have 100 Coral Bits in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Coral Bits"), 100);
				//Check Cashier.PaEaA calls no function (Do nothing)
					assertFalse("Cashier's scheduler shouldn't have returned true , but didn't.", ItemCollector.pickAndExecuteAnAction());
		
		//Step 1
				ItemCollector.msgGetTheseItem(collectingItemList);
		
				//Check Orders
					assertEquals("ItemCollector should have 1 order in List of Orders. It doesn't.",ItemCollector.getOrders().size(), 1);
				//Check ItemCollector.PaEaA calls a function (GoGetItems)	
					assertTrue("Cashier's scheduler shouldn't have returned true , but didn't.", ItemCollector.pickAndExecuteAnAction());
					
				//Check Orders (Got removed the order from Orders)
					assertEquals("ItemCollector should have 1 order in List of Orders. It doesn't.",ItemCollector.getOrders().size(), 1);
				//Check InventoryList should be decreased
					assertEquals("ItemCollector's InventoryList should have 60 Kelp Rings in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Rings"), 60);
					assertEquals("ItemCollector's InventoryList should have 70 LamboFinny in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("LamboFinny"), 70);
					assertEquals("ItemCollector's InventoryList should have 80 Kelp Shake in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Shake"), 80);
					assertEquals("ItemCollector's InventoryList should have 85 Toyoda in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Toyoda"), 85);
					assertEquals("ItemCollector's InventoryList should have 0 Krabby Patty in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Krabby Patty"), 0);
					assertEquals("ItemCollector's InventoryList should have 0 Coral Bits in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Coral Bits"), 0);
					
				//Check ItemCollector.PaEaA calls a function (GoGetItems)	
					assertTrue("Cashier's scheduler shouldn't have returned true , but didn't.", ItemCollector.pickAndExecuteAnAction());
					
				//Check DeliverList if it is correct
					assertEquals("ItemCollector's DeliverList should have a list size of 6. It doesn't", (int) Cashier.DeliverList.size(), 6);
					assertEquals("ItemCollector's DeliverList should have 40 Kelp Rings in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(0).amount, 40);
					assertEquals("ItemCollector's DeliverList should have 30 LamboFinny in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(1).amount, 30);
					assertEquals("ItemCollector's DeliverList should have 20 Kelp Shake in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(2).amount, 20);
					assertEquals("ItemCollector's DeliverList should have 15 Toyoda in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(3).amount, 15);
					assertEquals("ItemCollector's DeliverList should have 100 Krabby Patty in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(4).amount, 100);
					assertEquals("ItemCollector's DeliverList should have 100 Coral Bits in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(5).amount, 100);
				//Check MissingItemList if it is correct
					assertEquals("ItemCollector's MissingItemList should have a list size of 2. It doesn't", (int) Cashier.MissingList.size(), 2);
					assertEquals("ItemCollector's MissingItemList should have 100 Krabby Patty in the InventoryList initially. It doesn't", (int) Cashier.MissingList.get(0).amount, 100);
					assertEquals("ItemCollector's MissingItemList should have 5 Coral Bits in the InventoryList initially. It doesn't", (int) Cashier.MissingList.get(1).amount, 5);
					
				
		//Check post-conditions
					//Check Orders
						assertEquals("ItemCollector should have 0 order in List of Orders. It doesn't.",ItemCollector.getOrders().size(), 0);
					//Check Cashier
						assertEquals("ItemCollector should have a non-null Cashier in it. It doesn't.",ItemCollector.getCashier() , Cashier);
					//Check InventoryList
						assertEquals("ItemCollector's InventoryList should have 60 Kelp Rings in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Rings"), 60);
						assertEquals("ItemCollector's InventoryList should have 70 LamboFinny in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("LamboFinny"), 70);
						assertEquals("ItemCollector's InventoryList should have 80 Kelp Shake in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Shake"), 80);
						assertEquals("ItemCollector's InventoryList should have 85 Toyoda in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Toyoda"), 85);
						assertEquals("ItemCollector's InventoryList should have 0 Krabby Patty in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Krabby Patty"), 0);
						assertEquals("ItemCollector's InventoryList should have 0 Coral Bits in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Coral Bits"), 0);
					//Check Cashier.PaEaA calls no function (Do nothing)
						assertFalse("Cashier's scheduler shouldn't have returned true , but didn't.", ItemCollector.pickAndExecuteAnAction());
					
	}
	/**
	 * The Inventory is COMPLETELY EMPTY
	 */
	public void test3HaveNoItemsAtAllScenario()
	{
		
			//Map<String, Integer> InventoryList
		Map<String,Integer> InventoryList = new HashMap<String,Integer>();
		{		//Initially The market has 100 inventory on each Item
			for (int i = 0 ; i < CommonSimpleClasses.Constants.FOODS.size(); i++){
				InventoryList.put(CommonSimpleClasses.Constants.FOODS.get(i), 0);
			}
			for (int i = 0 ; i < CommonSimpleClasses.Constants.CARS.size(); i++){
				InventoryList.put(CommonSimpleClasses.Constants.CARS.get(i), 0);
			}
		}
		ItemCollector.setInventoryList(InventoryList);
			//Walk to the Ready Station (He is Ready)
		ItemCollector.setState(ItemCollectorstate.Idle);
		
		List<Item> collectingItemList = new ArrayList<Item>();
		collectingItemList.add(new Item("Kelp Rings", 1));
		collectingItemList.add(new Item("LamboFinny", 2));
		collectingItemList.add(new Item("Kelp Shake", 3));
		collectingItemList.add(new Item("Toyoda", 4));
		collectingItemList.add(new Item("Krabby Patty", 5));
		collectingItemList.add(new Item("Coral Bits", 6));
		
		//check preconditions
				//Check Orders
					assertEquals("ItemCollector should have 0 order in List of Orders. It doesn't.",ItemCollector.getOrders().size(), 0);
				//Check Cashier
					assertEquals("ItemCollector should have a non-null Cashier in it. It doesn't.",ItemCollector.getCashier() , Cashier);
				//Check InventoryList
					assertEquals("ItemCollector's InventoryList should have 0 Kelp Rings in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Rings"), 0);
					assertEquals("ItemCollector's InventoryList should have 0 LamboFinny in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("LamboFinny"), 0);
					assertEquals("ItemCollector's InventoryList should have 0 Kelp Shake in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Shake"), 0);
					assertEquals("ItemCollector's InventoryList should have 0 Toyoda in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Toyoda"), 0);
					assertEquals("ItemCollector's InventoryList should have 0 Krabby Patty in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Krabby Patty"), 0);
					assertEquals("ItemCollector's InventoryList should have 0 Coral Bits in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Coral Bits"), 0);
				//Check Cashier.PaEaA calls no function (Do nothing)
					assertFalse("Cashier's scheduler shouldn't have returned true , but didn't.", ItemCollector.pickAndExecuteAnAction());
		
		//Step 1
				ItemCollector.msgGetTheseItem(collectingItemList);
		
				//Check Orders
					assertEquals("ItemCollector should have 1 order in List of Orders. It doesn't.",ItemCollector.getOrders().size(), 1);
				//Check ItemCollector.PaEaA calls a function (GoGetItems)	
					assertTrue("Cashier's scheduler shouldn't have returned true , but didn't.", ItemCollector.pickAndExecuteAnAction());
					
				//Check Orders (Got removed the order from Orders)
					assertEquals("ItemCollector should have 1 order in List of Orders. It doesn't.",ItemCollector.getOrders().size(), 1);
				//Check InventoryList should be decreased
					assertEquals("ItemCollector's InventoryList should have 0 Kelp Rings in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Rings"), 0);
					assertEquals("ItemCollector's InventoryList should have 0 LamboFinny in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("LamboFinny"), 0);
					assertEquals("ItemCollector's InventoryList should have 0 Kelp Shake in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Shake"), 0);
					assertEquals("ItemCollector's InventoryList should have 0 Toyoda in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Toyoda"), 0);
					assertEquals("ItemCollector's InventoryList should have 0 Krabby Patty in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Krabby Patty"), 0);
					assertEquals("ItemCollector's InventoryList should have 0 Coral Bits in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Coral Bits"), 0);
					
				//Check ItemCollector.PaEaA calls a function (GoGetItems)	
					assertTrue("Cashier's scheduler shouldn't have returned true , but didn't.", ItemCollector.pickAndExecuteAnAction());
					
				//Check DeliverList if it is correct
					assertEquals("ItemCollector's DeliverList should have a list size of 6. It doesn't", (int) Cashier.DeliverList.size(), 6);
					assertEquals("ItemCollector's DeliverList should have 0 Kelp Rings in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(0).amount, 0);
					assertEquals("ItemCollector's DeliverList should have 0 LamboFinny in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(1).amount, 0);
					assertEquals("ItemCollector's DeliverList should have 0 Kelp Shake in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(2).amount, 0);
					assertEquals("ItemCollector's DeliverList should have 0 Toyoda in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(3).amount, 0);
					assertEquals("ItemCollector's DeliverList should have 0 Krabby Patty in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(4).amount, 0);
					assertEquals("ItemCollector's DeliverList should have 0 Coral Bits in the DeliverList. It doesn't", (int) Cashier.DeliverList.get(5).amount, 0);
				//Check MissingItemList if it is correct
					assertEquals("ItemCollector's MissingItemList should have a list size of 6. It doesn't", (int) Cashier.MissingList.size(), 6);
					assertEquals("ItemCollector's MissingItemList should have 6 Krabby Patty in the InventoryList initially. It doesn't", (int) Cashier.MissingList.get(0).amount, 1);
					assertEquals("ItemCollector's MissingItemList should have 5 Coral Bits in the InventoryList initially. It doesn't", (int) Cashier.MissingList.get(1).amount, 2);
					assertEquals("ItemCollector's MissingItemList should have 4 Krabby Patty in the InventoryList initially. It doesn't", (int) Cashier.MissingList.get(2).amount, 3);
					assertEquals("ItemCollector's MissingItemList should have 3 Coral Bits in the InventoryList initially. It doesn't", (int) Cashier.MissingList.get(3).amount, 4);
					assertEquals("ItemCollector's MissingItemList should have 2 Krabby Patty in the InventoryList initially. It doesn't", (int) Cashier.MissingList.get(4).amount, 5);
					assertEquals("ItemCollector's MissingItemList should have 1 Coral Bits in the InventoryList initially. It doesn't", (int) Cashier.MissingList.get(5).amount, 6);
				
		//Check post-conditions
					//Check Orders
						assertEquals("ItemCollector should have 0 order in List of Orders. It doesn't.",ItemCollector.getOrders().size(), 0);
					//Check Cashier
						assertEquals("ItemCollector should have a non-null Cashier in it. It doesn't.",ItemCollector.getCashier() , Cashier);
					//Check InventoryList
						assertEquals("ItemCollector's InventoryList should have 0 Kelp Rings in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Rings"), 0);
						assertEquals("ItemCollector's InventoryList should have 0 LamboFinny in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("LamboFinny"), 0);
						assertEquals("ItemCollector's InventoryList should have 0 Kelp Shake in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Kelp Shake"), 0);
						assertEquals("ItemCollector's InventoryList should have 0 Toyoda in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Toyoda"), 0);
						assertEquals("ItemCollector's InventoryList should have 0 Krabby Patty in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Krabby Patty"), 0);
						assertEquals("ItemCollector's InventoryList should have 0 Coral Bits in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryMap().get("Coral Bits"), 0);
					//Check Cashier.PaEaA calls no function (Do nothing)
						assertFalse("Cashier's scheduler shouldn't have returned true , but didn't.", ItemCollector.pickAndExecuteAnAction());
					
	}
	
	
	
	
	
}