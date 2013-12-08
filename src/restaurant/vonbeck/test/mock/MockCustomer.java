package restaurant.vonbeck.test.mock;


import java.util.Map;

import restaurant.vonbeck.HostAgent;
import restaurant.vonbeck.WaiterAgent;
import restaurant.vonbeck.gui.CustomerGui;
import restaurant.vonbeck.interfaces.Cashier;
import restaurant.vonbeck.interfaces.Customer;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;

	public MockCustomer(String name) {
		super(name);

	}

	@Override
	public void msgBill(int total, Cashier cashier) {
		log.add(new LoggedEvent("Received msgBill from cashier. Total = "+ total));

		if(this.name.toLowerCase().contains("thief")){
			//test the non-normative scenario where the customer has no money if their name contains the string "theif"
			cashier.msgIDontHaveEnoughMoney(this, total, total-100);

		} /* else if (this.name.toLowerCase().contains("rich")){
			//test the non-normative scenario where the customer overpays if their name contains the string "rich"
			cashier.HereIsMyPayment(this, Math.ceil(total));

		} */ else{
			//test the normative scenario
			cashier.msgPay(this, total);
		}
	}

	@Override
	public void setHost(HostAgent host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCustomerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgSitAtTable(int tableNum, WaiterAgent w,
			Map<String, Integer> menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHeresYourFood(String food) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfFood(String food) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHungerLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setHungerLevel(int hungerLevel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(CustomerGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CustomerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomerGui getCustomerGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCustomerGui(CustomerGui customerGui) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	@Override
	public void HereIsYourChange(double total) {
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
	}
	*/
	
	/*
	@Override
	public void YouOweUs(double remaining_cost) {
		log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ remaining_cost));
	}
	*/

}
