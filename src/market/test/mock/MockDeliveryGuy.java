package market.test.mock;


import java.util.List;


import java.util.Map;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;
import market.Item;
import market.interfaces.Cashier;
import market.interfaces.Customer;
import market.interfaces.DeliveryGuy;
import market.interfaces.DeliveryReceiver;

public class MockDeliveryGuy extends Mock implements DeliveryGuy {

	public MockDeliveryGuy(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean msgAreYouAvailable() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void msgDeliverIt(List<Item> DeliveryList,
			DeliveryReceiver OrdePerson, CityLocation building) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgArrivedDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeaveWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCashier(Cashier ca) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Ready() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AtExit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AtDeliverExit() {
		// TODO Auto-generated method stub
		
	}
}