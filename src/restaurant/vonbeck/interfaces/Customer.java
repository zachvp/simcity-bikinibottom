package restaurant.vonbeck.interfaces;

import java.util.Map;

import restaurant.vonbeck.HostRole;
import restaurant.vonbeck.WaiterRole;
import restaurant.vonbeck.gui.CustomerGui;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {

	public void setHost(HostRole host);

	// Getting name
	public String getCustomerName();
	
	//Messages
	public void msgSitAtTable(int tableNum, WaiterRole w, Map<String, Double> menu);

	public void msgAnimationFinishedGoToSeat();
	
	public void msgAnimationFinishedLeaveRestaurant();
	
	public void msgAnimationFinishedGoToCashier();
	
	public void msgHeresYourFood(String food);
	
	public void msgOutOfFood(String food);

	public void msgBill(double numData, Cashier cashier);
	
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