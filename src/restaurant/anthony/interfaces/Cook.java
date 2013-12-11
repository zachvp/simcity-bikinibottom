package restaurant.anthony.interfaces;

import java.util.List;
import java.util.Map;

import restaurant.InfoPanel;
import restaurant.anthony.Food;
import restaurant.anthony.HostRole;
import restaurant.anthony.RevolvingOrderList;
import restaurant.anthony.WaiterRoleBase;
import restaurant.anthony.gui.CookGui;
import restaurant.anthony.RevolvingOrderList.Order;

public interface Cook extends market.interfaces.DeliveryReceiver {

	public abstract String getMaitreDName();

	public abstract String getName();

	public abstract List getMyOrders();

	// Messages	
	public abstract void msgAtFridge();

	public abstract void msgAtStove(int i);

	public abstract void msgAtPlatingArea(int i);

	public abstract void msgAtHome();

	public abstract void HeresTheOrder(String s, int t , Waiter w);

	public abstract void msgIGetOrder(int tnumb);

	public abstract void msgLeaveWork();

	public abstract void atExit();

	public abstract boolean isAtWork();

	public abstract boolean isOnBreak();

	public abstract void setGui(CookGui cGui);

	public abstract void setHost(HostRole h);

	public abstract void setInventoryList(Map<String, Food> iList);

	public abstract void setInfoPanel(InfoPanel infoPanel);

	public abstract void setRevolvingOrders(
			RevolvingOrderList revolvingOrderList);

	public abstract boolean hasAnyFood();

}