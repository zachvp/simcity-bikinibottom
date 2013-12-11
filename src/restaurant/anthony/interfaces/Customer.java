package restaurant.anthony.interfaces;

import restaurant.anthony.HostRole;
import restaurant.anthony.CashierRole.Check;
import restaurant.anthony.WaiterRoleBase.Menu;
import restaurant.anthony.gui.CustomerGui;

public interface Customer {

	
	/**
	 * hack to establish connection to Host agent.
	 */
	public abstract void setHost(HostRole host);

	public abstract void setWaiter(Waiter wa);

	public abstract void setCashier(Cashier ca);

	public abstract String getCustomerName();

	public abstract void gotHungry();

	public abstract void msgSitAtTable(int i, Waiter wa, Menu M);

	public abstract void msgOrderFail(Menu M);
	
	public abstract void msgYouAreInLine(int i);

	public abstract void OrderGotIt();

	public abstract void HeresYourOrder(restaurant.anthony.RevolvingOrderList.Order o);

	public abstract void HeresYourCheck(Check ch);

	public abstract void HereIsYourChange(double change);

	public abstract void HereIsYourDebt(double de);
	
	public abstract void msgEpicFail();

	public abstract void msgAnimationFinishedGoToSeat();

	public abstract void msgAnimationFinishedLeaveRestaurant();

	public abstract void msgAnimationFinishedGoToCashier();

	public abstract String getName();

	public abstract int getHungerLevel();

	public abstract void setHungerLevel(int hungerLevel);

	public abstract String toString();

	public abstract void setGui(CustomerGui g);

	public abstract CustomerGui getGui();

	public abstract void atWaitingLine();

}