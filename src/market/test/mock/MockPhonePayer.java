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
import market.interfaces.ItemCollector;
import market.interfaces.ItemCollectorGuiInterfaces;
import market.interfaces.PhonePayer;

public class MockPhonePayer extends Mock implements PhonePayer {

	
	public MockPhonePayer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public EventLog log = new EventLog();
	public Cashier Cashier;
	Map<String, Integer> tempInventoryList = null;
	@Override
	
	public void msgHereIsYourTotal(double total, Cashier cashier) {

		log.add(new LoggedEvent("MockPhonePayer :  Received msgHereIsYourTotal"));
		cashier.msgHereIsPayment(total, (PhonePayer)this);
		
		return;
	}
	
}