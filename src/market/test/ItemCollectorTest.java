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
	MockCashier Cashier;
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
		ItemCollector = new ItemCollectorRole("John", ItemCollectorGuy, new MarketBuilding(1,1,1,1));	
		ItemCollectorGuiInterfaces itemCollectorGui = new MockItemCollectorGui(ItemCollector);
		ItemCollector.setGui(itemCollectorGui);
		Customer1 = new MockCustomer("mockcustomer");		
		Customer2 = new MockCustomer("mockcustomer");

		
	}	
	
	/**
	 * This tests the cashier under very simple terms: 1 MarketCashier in the market and ask the only 1 ItemCollector to get items(Enough Inventory)
	 */
	public void testOne1MarketCashierCallsItemCollectorScenario()
	{
		
			//Map<String, Integer> InventoryList
		Map<String,Integer> InventoryList = new HashMap<String,Integer>();
		{		//Initially The market has 100 inventory on each Item
			for (int i = 0 ; i < agent.Constants.FOODS.size(); i++){
				InventoryList.put(agent.Constants.FOODS.get(i), 100);
			}
			for (int i = 0 ; i < agent.Constants.CARS.size(); i++){
				InventoryList.put(agent.Constants.CARS.get(i), 100);
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
				//Check MyName
					assertEquals("ItemCollector should have name of John in it. It doesn't.",ItemCollector.getName(), "John");
				//Check InventoryList
					assertEquals("ItemCollector's InventoryList should have 100 Kelp Rings in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryList().get("Kelp Rings"), 100);
					assertEquals("ItemCollector's InventoryList should have 100 LamboFinny in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryList().get("LamboFinny"), 100);
					assertEquals("ItemCollector's InventoryList should have 100 Kelp Shake in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryList().get("Kelp Shake"), 100);
					assertEquals("ItemCollector's InventoryList should have 100 Toyoda in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryList().get("Toyoda"), 100);
					assertEquals("ItemCollector's InventoryList should have 100 Krabby Patty in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryList().get("Krabby Patty"), 100);
					assertEquals("ItemCollector's InventoryList should have 100 Coral Bits in the InventoryList initially. It doesn't", (int) ItemCollector.getInventoryList().get("Coral Bits"), 100);
				//Check Cashier.PaEaA calls no function (Do nothing)
					assertFalse("Cashier's scheduler shouldn't have returned true , but didn't.", ItemCollector.pickAndExecuteAnAction());
		
		//Step 1
				ItemCollector.msgGetTheseItem(collectingItemList, Customer1);
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}