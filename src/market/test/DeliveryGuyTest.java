package market.test;

import gui.Building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;
import agent.PersonAgent;
import junit.framework.TestCase;
import market.CashierRole;
import market.CashierRole.Cashierstate;
import market.DeliveryGuyRole;
import market.DeliveryGuyRole.DeliveryGuystate;
import market.Item;
import market.CashierRole.Customerstate;
import market.gui.CashierGui;
import market.gui.MarketBuilding;
import market.interfaces.DeliveryGuyGuiInterfaces;
import market.interfaces.ItemCollector;
import market.test.mock.EventLog;
import market.test.mock.MockCashier;
import market.test.mock.MockCashierGui;
import market.test.mock.MockDeliveryGuyGui;
import market.test.mock.MockDeliveryReceiver;
import market.test.mock.MockItemCollector;
import market.test.mock.MockCustomer;
import market.test.mock.MockPhonePayer;

public class DeliveryGuyTest extends TestCase
{
	public EventLog log = new EventLog();
	DeliveryGuyRole deliveryGuyRole;
	DeliveryGuyGuiInterfaces deliveryGuyGui;
	MockCashier cashier;
	MockDeliveryReceiver deliveryReceiver;
	List<Item>DeliveryList;
	CityLocation building;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		

		PersonAgent deliveryGuyAgent = new PersonAgent ("John");
		deliveryGuyRole = new DeliveryGuyRole(deliveryGuyAgent, new MarketBuilding(1,1,1,1));
		deliveryGuyGui = new MockDeliveryGuyGui(deliveryGuyRole);
		deliveryGuyRole.setGui(deliveryGuyGui);
		
		cashier = new MockCashier("MockCashier");
		deliveryGuyRole.setCashier(cashier);
		deliveryReceiver = new MockDeliveryReceiver("MockDeliveryReceiver");
		
		List<Item>DeliveryList = new ArrayList<Item>();
		building = null;
		
		
	}
	
	/**
	 * This tests the cashier under very simple terms: 1 MarketCustomer comes to market and 1 ItemCollector (Enough Inventory)
	 */
	public void testOneDeliverGuyAndCashierSendMessageScenario()
	{
		//Check pre-conditions
		assertEquals("DeliveryGuyRole's CurrentOrder should be null.",deliveryGuyRole.getCurrentOrder(), null);
		assertEquals("DeliveryGuyRole's Cashier should be MockCashier", deliveryGuyRole.getCashier(), cashier);
		assertEquals("DeliveryGuyRole's Available boolean should be true initially", true, deliveryGuyRole.msgAreYouAvailable());
		
		assertEquals("DeliveryGuyRole's DeliveryGuystate should be NotAtWork", deliveryGuyRole.getState(), DeliveryGuystate.NotAtWork);
		deliveryGuyRole.setState(DeliveryGuystate.Idle);
		//Check Cashier.PaEaA calls no function (Do nothing)
		assertFalse("DeliveryGuyRole's scheduler shouldn't have returned true , but didn't.", deliveryGuyRole.pickAndExecuteAnAction());
		
		//Step 1
		deliveryGuyRole.msgDeliverIt( DeliveryList, deliveryReceiver, null);
		
		//Check Post-Conditions
		assertEquals("DeliveryGuyRole CurrentOrder's DeliveryList should be the same as the local variable DeliveryList.",deliveryGuyRole.getCurrentOrder().getDeliveryList(), DeliveryList);
		assertEquals("DeliveryGuyRole CurrentOrder's DeliveryReceiver should be the same as the local variable DeliveryReceiver.",deliveryGuyRole.getCurrentOrder().getDeliveryReceiver(), deliveryReceiver);
		assertEquals("DeliveryGuyRole CurrentOrder's Building should be null as we inputted in", deliveryGuyRole.getCurrentOrder().getBuilding(),null);
		
		//Check Cashier.PaEaA calls no function (GoDeliver)
			//assertTrue("DeliveryGuyRole's scheduler shouldn't have returned false , but didn't.", deliveryGuyRole.pickAndExecuteAnAction());
		
		
	}
}

