package restaurant.anthony.interfaces;

import java.util.List;

import restaurant.anthony.CookRole;
import restaurant.anthony.Food;

public interface Market {

	public abstract String getMaitreDName();

	public abstract String getName();

	// Messages
	public abstract void BuyFood(List<Food> shoppingList2, CookRole co);

	public abstract void HeresTheMoney(double payment);

	public abstract void setCashier(Cashier ca);

}