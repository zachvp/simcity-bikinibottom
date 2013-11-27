package market.test.mock;


import java.awt.Graphics2D;
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
import market.interfaces.DeliveryGuy;
import market.interfaces.DeliveryGuyGuiInterfaces;
import market.interfaces.ItemCollector;
import market.interfaces.ItemCollectorGuiInterfaces;
import market.interfaces.PhonePayer;

public class MockDeliveryGuyGui extends Mock implements DeliveryGuyGuiInterfaces {

	
	public MockDeliveryGuyGui(DeliveryGuy deli) {
		super("MockDeliveryGuyGui");
		deliveryGuy = deli;
		// TODO Auto-generated constructor stub
	}
	public EventLog log = new EventLog();
	public DeliveryGuy deliveryGuy;
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void BackReadyStation() {
		// TODO Auto-generated method stub
		deliveryGuy.Ready();
	}
	@Override
	public void GoDeliver() {
		// TODO Auto-generated method stub
		deliveryGuy.Ready();
	}
	@Override
	public void OffWork() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public int getXPos() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getYPos() {
		// TODO Auto-generated method stub
		return 0;
	}
	

	
}