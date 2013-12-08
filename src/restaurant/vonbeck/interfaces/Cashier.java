package restaurant.vonbeck.interfaces;

import restaurant.vonbeck.interfaces.Waiter.Order;

public interface Cashier {
	void msgNeedBill(Order o);
	
	void msgPay(Customer c, int price);
	
	public void msgIDontHaveEnoughMoney(Customer customerAgent,
			int priceToPay, int moneyIOwe);

	public void msgPayDebt(Customer customerAgent, int debt);

	void msgBillFromMarket(Integer integer, Market market);
	
}
