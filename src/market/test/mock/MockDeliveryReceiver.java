package market.test.mock;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import agent.PersonAgent;
import agent.WorkRole;
import agent.interfaces.Person;
import CommonSimpleClasses.Constants;
import market.test.mock.LoggedEvent;
import market.test.mock.EventLog;
import market.Item;
import market.ItemCollectorRole.ItemCollectorstate;
import market.interfaces.Cashier;
import market.interfaces.Customer;
import market.interfaces.DeliveryReceiver;
import market.interfaces.ItemCollector;
import market.interfaces.ItemCollectorGuiInterfaces;
import market.interfaces.PhonePayer;

public class MockDeliveryReceiver extends Mock implements DeliveryReceiver {

	public EventLog log = new EventLog();
	
	public MockDeliveryReceiver(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void msgHereIsYourItems(List<Item> DeliverList) {
		// TODO Auto-generated method stub
		
		
	}	
	public void msgHereIsMissingItems(List<Item> MissingItemList, int orderNum) {

	}

}