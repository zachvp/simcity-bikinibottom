package restaurant.vonbeck.interfaces;

import market.interfaces.PhonePayer;
import restaurant.vonbeck.interfaces.Waiter.Order;

public interface Cashier extends PhonePayer {
	void msgNeedBill(Order o);
	
	void msgPay(Customer c, double priceToPay);
	
	public void msgIDontHaveEnoughMoney(Customer customerAgent,
			double priceToPay, double moneyIOwe);

	public void msgPayDebt(Customer customerAgent, double debt);

	void msgBillFromMarket(Double price, Market market);
	
}
