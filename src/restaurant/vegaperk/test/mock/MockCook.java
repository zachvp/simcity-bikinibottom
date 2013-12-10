package restaurant.vegaperk.test.mock;


import java.util.List;
import java.util.Map;

import market.Item;
import mock.EventLog;
import mock.Mock;
import restaurant.vegaperk.backend.MarketAgent;
import restaurant.vegaperk.gui.CookGui;
import restaurant.vegaperk.interfaces.Cashier;
import restaurant.vegaperk.interfaces.Cook;
import restaurant.vegaperk.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCook extends Mock implements Cook {
	public EventLog log = new EventLog();
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;

	public MockCook(String name) {
		super(name);
	}


	@Override
	public void msgHereIsOrder(Waiter w, String c, int t) {
		log.add("Received message here is order");
	}

	@Override
	public void msgCanDeliver(Map<String, Integer> canDeliver) {
		log.add("Received message can deliever");
	}


	@Override
	public void msgHereIsDelivery() {
		log.add("Received message here is delivery");
	}


	@Override
	public void msgAtDestination() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setGui(CookGui gui) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void addMarket(MarketAgent m1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgHereIsYourItems(List<Item> DeliverList) {
		log.add("Received items");
	}


	@Override
	public void msgHereIsMissingItems(List<Item> MissingItemList, int orderNum) {
		log.add("Received list of missing items");
	}
}
