package restaurant.vonbeck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import restaurant.vonbeck.interfaces.Cashier;
import restaurant.vonbeck.interfaces.Customer;
import restaurant.vonbeck.interfaces.Market;
import restaurant.vonbeck.interfaces.Waiter.Order;
import restaurant.vonbeck.test.mock.LoggedEvent;
import agent.Agent;


public class CashierAgent extends Agent implements Cashier {
	private List<Order> waiterOrders =
			Collections.synchronizedList(new ArrayList<Order>());
	private Map<String, Integer> priceList = 
			Collections.synchronizedMap(new HashMap<String, Integer>());
	private Integer wallet = 0;
	
	private class MyMarketOrder {
		Market market;
		Integer price;
		
		public MyMarketOrder(Market m, int p) {
			market = m;
			price = p;
		}
	}
	
	private List<MyMarketOrder> marketOrders = new ArrayList<MyMarketOrder>();
	
	public CashierAgent(boolean runThread) {
		priceList.put("Steak", 1599);
		priceList.put("Chicken", 1099);
		priceList.put("Salad", 599);
		priceList.put("Pizza", 899);
		
		wallet = 5000+(int)(Math.random()*5000);
		
		if (runThread == true) startThread();
	}
	
	//Messages
	
	public void msgNeedBill(Order o) {
		synchronized (waiterOrders) {
			waiterOrders.add(o);
		}
		stateChanged();
	}
	
	public void msgPay(Customer c, int price) {
		log.add("Received msgPay");
		Do(c.getName() + " payed " + price + " cents.");
		synchronized (wallet) {
			wallet += price;
		}
	}

	public void msgIDontHaveEnoughMoney(Customer customerAgent,
			int priceToPay, int moneyIOwe) {
		log.add("Received msgIDontHaveEnoughMoney");
		wallet += (priceToPay-moneyIOwe);
		Do("Pay me back next time, because I believe goodness is"+
			" a quality inherent in everyone's hearts.");
	}

	public void msgPayDebt(Customer customerAgent, int debt) {
		Do("Thanks, son. May good grades rain upon you.");
		Do(customerAgent.getName() + " paid his debt with " + debt + " cents.");
		synchronized (wallet) {
			wallet += debt;
		}
		
	}
	
	@Override
	public void msgBillFromMarket(Integer price, Market market) {
		Do("Received bill from market");
		synchronized (marketOrders) {
			marketOrders.add(new MyMarketOrder(market, price));
		}
		stateChanged();
	}
	
	//Scheduler
	@Override
	public boolean pickAndExecuteAnAction() {
		synchronized (marketOrders) {
			if (!marketOrders.isEmpty()) {
				payMarket(marketOrders.get(0));
				marketOrders.remove(0);
				return true;
			}
		}
		
		synchronized (waiterOrders) {
			if (!waiterOrders.isEmpty()) {
				sendBill(waiterOrders.get(0));
				waiterOrders.remove(0);
				return true;
			}
		}
		return false;
	}
	
	private void payMarket(MyMarketOrder o) {
		Do("Paying " + o.price + " to market.");
		synchronized (wallet) {
			wallet -= o.price;
		}
		o.market.msgPay(o.price);
	}

	//Actions
	private void sendBill(Order o) {
		o.waiter.msgBill(priceList.get(o.food), o.customer);
	}

	/**
	 * @return the orders
	 */
	public List<Order> getOrders() {
		List<Order> response;
		
		synchronized (waiterOrders) {
			response = new ArrayList<Order>(waiterOrders);
		}
		
		return response;
	}

	/**
	 * @return the marketOrders
	 */
	public List<MyMarketOrder> getMarketOrders() {
		return marketOrders;
	}

	/**
	 * @param marketOrders the marketOrders to set
	 */
	public void setMarketOrders(List<MyMarketOrder> marketOrders) {
		this.marketOrders = marketOrders;
	}

	/**
	 * @return the wallet
	 */
	public Integer getWallet() {
		return wallet;
	}

	/**
	 * @param wallet the wallet to set
	 */
	public void setWallet(Integer wallet) {
		this.wallet = wallet;
	}


	
}
