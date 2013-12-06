package market.interfaces;

import java.awt.Component;
import java.util.List;






import agent.interfaces.Person;
import market.Item;

public interface Customer {
	
	//Message
	public abstract void msgHereisYourTotal(double cost, List<Item> MissingItems);
	
	public abstract void msgHereisYourItem(List<Item> Items);
	
	public abstract void msgNoItem();
	
	public abstract void goingToBuy();

	//Utilities
	public abstract String getMaitreDName();
	public abstract String getName();
	public abstract void setCashier(Cashier ca);
	public abstract void setShoppingList(List<Item>SL);
	public abstract double getCash();
	public abstract List<Item> getShoppingList();
	public abstract Person getPerson();

	//Animations
	public abstract void msgAnimationFinishedGoToCashier();
	public abstract void msgAnimationFinishedLeaveMarket();

	

	



	

	
}