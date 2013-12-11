package restaurant.vonbeck;

import gui.trace.AlertTag;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import com.sun.org.apache.bcel.internal.generic.NEW;

import bank.test.mock.EventLog;
import restaurant.strottma.CashierRole.MyBill;
import restaurant.vonbeck.gui.CashierGui;
import restaurant.vonbeck.gui.RestaurantGui;
import restaurant.vonbeck.gui.RestaurantVonbeckBuilding;
import restaurant.vonbeck.interfaces.Cashier;
import restaurant.vonbeck.interfaces.Customer;
import restaurant.vonbeck.interfaces.Market;
import restaurant.vonbeck.interfaces.Waiter.Order;
import restaurant.vonbeck.test.mock.LoggedEvent;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.SingletonTimer;
import agent.Agent;
import agent.Role;
import agent.WorkRole;


public class CashierRole extends WorkRole implements Cashier {
	private List<Order> waiterOrders =
			Collections.synchronizedList(new ArrayList<Order>());
	private Map<String, Double> priceList = 
			Collections.synchronizedMap(new HashMap<String, Double>());
	private double wallet = 0;
	Object walletSyncObject = new Object();
	EventLog log = new EventLog();
	DecimalFormat df = new DecimalFormat("#.##");

	
	private class MyMarketOrder {
		Market market;
		double price;
		
		public MyMarketOrder(Market m, double p) {
			market = m;
			price = p;
		}
	}
	
	public class MyBill {
		private double amount;
		private market.interfaces.Cashier cashier;
		
		public MyBill(double amount, market.interfaces.Cashier cashier) {
			this.amount = amount;
			this.cashier = cashier;
		}
		
		public double getAmount() { return amount; }
		public void setAmount(double amount) { this.amount = amount; }
		
		public market.interfaces.Cashier getCashier() { return cashier; }
	}
	
	private List<MyMarketOrder> marketOrders = new ArrayList<MyMarketOrder>();
	private CashierGui cashierGui;
	private List<MyBill> bills = Collections.synchronizedList(new ArrayList<MyBill>()); // money owed to markets
	
	public CashierRole(RestaurantGui gui, RestaurantVonbeckBuilding building) {
		super(building);
		
		priceList.put(Constants.FOODS.get(0), 15.99);
		priceList.put(Constants.FOODS.get(1), 10.99);
		priceList.put(Constants.FOODS.get(2), 5.99);
		priceList.put(Constants.FOODS.get(3), 8.99);
		
		wallet = 50.00+(int)(Math.random()*50.00);
		
		this.cashierGui = new CashierGui(this);
		gui.getAnimationPanel().addGui(cashierGui);
	}
	
	public void activate() {
		super.activate();
		cashierGui.DoGoToWork();
	}
	
	//Messages
	
	public void msgNeedBill(Order o) {
		synchronized (waiterOrders) {
			waiterOrders.add(o);
		}
		stateChanged();
	}
	
	public void msgPay(Customer c, double price) {
		log.add("Received msgPay");
		Do(c.getName() + " payed " + price + " dollars.");
		synchronized (walletSyncObject) {
			wallet += price;
		}
	}

	public void msgIDontHaveEnoughMoney(Customer customerAgent,
			double priceToPay, double moneyIOwe) {
		log.add("Received msgIDontHaveEnoughMoney");
		wallet += (priceToPay-moneyIOwe);
		Do("Pay me back next time, because I believe goodness is"+
			" a quality inherent in everyone's hearts.");
	}

	public void msgPayDebt(Customer customerAgent, double debt) {
		Do("Thanks, son. May good grades rain upon you.");
		Do(customerAgent.getName() + " paid his debt with " + debt + " dollars.");
		synchronized (walletSyncObject) {
			wallet += debt;
		}
		
	}
	
	@Override
	public void msgBillFromMarket(Double price, Market market) {
		Do("Received bill from market");
		synchronized (marketOrders) {
			marketOrders.add(new MyMarketOrder(market, price));
		}
		stateChanged();
	}
	
	//Scheduler
	@Override
	public boolean pickAndExecuteAnAction() {

		if (wallet != 0) {
			synchronized (bills) {
				for (MyBill bill : bills) {
					payBill(bill);
					return true;
				}
			}
		}
		
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
	
	private void payBill(MyBill bill) {
		if (wallet >= bill.amount) {
			Do(AlertTag.RESTAURANT, "Paying $" + df.format(bill.amount) +
					" to " + bill.cashier.getName());
			bill.cashier.msgHereIsPayment(bill.amount, this);
			wallet -= bill.amount;
			bills.remove(bill);
			
		} else {
			// extra credit
			Do(AlertTag.RESTAURANT, "Cannot pay the market in full. "
					+ "Paying as much as I can...");
			bill.cashier.msgHereIsPayment(wallet, this);
			wallet = 0.00;
			bill.amount -= wallet;
		}		
	}

	private void payMarket(MyMarketOrder o) {
		Do("Paying " + o.price + " to market.");
		synchronized (walletSyncObject) {
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
	public double getWallet() {
		return wallet;
	}

	/**
	 * @param wallet the wallet to set
	 */
	public void setWallet(Double wallet) {
		this.wallet = wallet;
	}

	@Override
	public boolean isAtWork() {
		return isActive();
	}

	@Override
	public boolean isOnBreak() {
		return false;
	}

	@Override
	public void msgLeaveWork() {
		//Do nothing, wait for host signal
	}

	public void msgGoHome() {
		cashierGui.DoLeaveWork();
		deactivate();
	}

	@Override
	public void msgHereIsYourTotal(double total,
			market.interfaces.Cashier cashier) {
		Do(AlertTag.RESTAURANT, "Received a bill from " + cashier);
		bills.add(new MyBill(total, cashier));
		stateChanged();
	}


	
}
