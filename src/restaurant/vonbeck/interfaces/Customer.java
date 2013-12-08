package restaurant.vonbeck.interfaces;

import java.util.Map;

import restaurant.vonbeck.HostAgent;
import restaurant.vonbeck.WaiterAgent;
import restaurant.vonbeck.gui.CustomerGui;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {

	public void setHost(HostAgent host);

	// Getting name
	public String getCustomerName();
	
	//Messages
	public void msgSitAtTable(int tableNum, WaiterAgent w, Map<String, Integer> menu);

	public void msgAnimationFinishedGoToSeat();
	
	public void msgAnimationFinishedLeaveRestaurant();
	
	public void msgAnimationFinishedGoToCashier();
	
	public void msgHeresYourFood(String food);
	
	public void msgOutOfFood(String food);

	public void msgBill(int total, Cashier cashier);
	
	public String getOrder();
	
	// Accessors, etc.
	
	public String getName();

	public int getHungerLevel();

	public void setHungerLevel(int hungerLevel);

	public String toString();

	public void setGui(CustomerGui g);

	public CustomerGui getGui();

	/**
	 * @return the customerGui
	 */
	public CustomerGui getCustomerGui();

	/**
	 * @param customerGui the customerGui to set
	 */
	public void setCustomerGui(CustomerGui customerGui);
}