package market.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CommonSimpleClasses.Constants;
import agent.PersonAgent;
import junit.framework.TestCase;
import market.CashierRole;
import market.CashierRole.Cashierstate;
import market.Item;
import market.CashierRole.Customerstate;
import market.gui.CashierGui;
import market.gui.MarketBuilding;
import market.interfaces.ItemCollector;
import market.test.mock.MockCashierGui;
import market.test.mock.MockItemCollector;
import market.test.mock.MockCustomer;

public class DeliveryGuyTest extends TestCase
{
	CashierRole Cashier;
	MockCashierGui cashierGui;
	MockCustomer Customer1;
	MockCustomer Customer2;
	MockItemCollector ItemCollector1;
	Map<String, Integer> InventoryList;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		

		InventoryList = new HashMap<String, Integer>();
		{
			InventoryList.put("Krabby Patty", 100);
			InventoryList.put("Kelp Shake", 100);
			InventoryList.put("Coral Bits", 100);
			InventoryList.put("Kelp Rings", 100);
			InventoryList.put("LamboFinny", 100);
			InventoryList.put("Toyoda", 100);
		}
		
		
		PersonAgent cashier = new PersonAgent ("John");
		Cashier = new CashierRole(cashier, new MarketBuilding(1,1,1,1), InventoryList);	
		
		Customer1 = new MockCustomer("mockcustomer");		
		Customer2 = new MockCustomer("mockcustomer");
		ItemCollector1 = new MockItemCollector("ItemCollector1");
		List<ItemCollector> ICList = new ArrayList<ItemCollector>();
		ICList.add(ItemCollector1);
		
		
	}	
	
	/**
	 * This tests the cashier under very simple terms: 1 MarketCustomer comes to market and 1 ItemCollector (Enough Inventory)
	 */
	public void testOne1MarketCustomer1ItemCollectorScenario()
	{