package restaurant.vdea.test.mock;

import java.util.List;

import restaurant.vdea.Food;
import restaurant.vdea.interfaces.*;

public class MockCook extends Mock implements Cook {
	
	public EventLog log = new EventLog();

	public MockCook(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgThereIsAnOrder(Waiter w, String choice, int table) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgOrderFufillment(List<Food> shipment, boolean orderFull) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gotFood(int table) {
		// TODO Auto-generated method stub

	}

}