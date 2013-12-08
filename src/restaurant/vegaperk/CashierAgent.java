package restaurant.vegaperk;

import agent.Agent;

import java.text.DecimalFormat;
import java.util.*;

import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;
import restaurant.test.mock.EventLog;

/**
 * Cook Agent
 */
public class CashierAgent extends Agent implements Cashier {
	String name;
	private double money = 500.00;
	
	public EventLog log = new EventLog();
	
	private DecimalFormat df = new DecimalFormat("#.##");
	
	private List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	private List<MyBill> bills = Collections.synchronizedList(new ArrayList<MyBill>());
	
	// create an anonymous Map class to initialize the foods and cook times

	public CashierAgent(String name) {
		super();
		this.name = name;
		
		setCustomers(Collections.synchronizedList(new ArrayList<MyCustomer>()));
	}

	/** Accessor and setter methods */
	public String getName() {
		return name;
	}
	
	/** Messages from other agents */
	
	
	/** From WaiterAgent */
	public void msgDoneEating(Customer c, double b, Waiter w){
		MyCustomer mc = findCustomer(c);
		if(mc == null){
			getCustomers().add(new MyCustomer(c, w, b));
		}
		else{
			mc.waiter = w;
			mc.setPayment(0);
			mc.setBill(mc.getBill() + b);
			mc.state = CustomerState.DONE_EATING;
		}
		stateChanged();
	}
	
	/** From Customer */
	public void msgHereIsPayment(Customer c, double p){
		Do("Received customer payment");
		MyCustomer mc = findCustomer(c);
		mc.setPayment(p);
		mc.state = CustomerState.PAID;
		stateChanged();
	}
	
	/** From Market */
	public void msgHereIsBill(double bill, Market m) {
		Do("Received bill");
		getBills().add(new MyBill(bill, m));
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction(){
		synchronized(bills){
			for(MyBill bill : bills){
				if(bill.state == BillState.PENDING){
					payMarket(bill);
					return true;
				}
			}
		}
		
		synchronized(customers){
			for(MyCustomer c : getCustomers()){
				if(c.state==CustomerState.DONE_EATING){
					calculateCheck(c);
					return true;
				}
			}
			for(MyCustomer c : getCustomers()){
				if(c.state==CustomerState.PAID){
					giveChange(c);
					return true;
				}
			}
		}
		return false;
	}
	
	/** Actions */
	private void calculateCheck(MyCustomer c){
		Do("Calculating bill. Bill is " + c.getBill());
		c.waiter.msgHereIsCheck(c.getCustomer(), c.getBill());
		c.state = CustomerState.BILLED;
	}
	private void giveChange(MyCustomer c){
		double change = c.getPayment() - c.getBill();
		
		if(change >= 0){
			Do("Giving change " + change);
			c.getCustomer().msgHereIsChange(change);
			c.setBill(0.00);
		}
		else{
			Do("Still owe " + df.format(Math.abs(change)));
			c.getCustomer().msgHereIsChange(0.00);
		}
		c.state = CustomerState.DONE;
		stateChanged();
	}
	private void payMarket(MyBill bill){
		bill.state = BillState.DONE;
		if(money >= bill.getAmount()){
			bill.market.msgHereIsPayment(bill.getAmount());
			money -= bill.getAmount();
			Do("Paid market bill " + bill.getAmount());
		}
		else{
			bill.market.msgHereIsPayment(money);
			Do("Don't have enough money to pay bill! Paid all my money " + money);
			money = 0;
		}
		bills.remove(bill);
	}
	
	/** Utilities */
	private MyCustomer findCustomer(Customer c){
		synchronized(getCustomers()){
			for(MyCustomer mc : getCustomers()){
				if(mc.getCustomer() == c){
					return mc;
				}
			}
		}
		return null;
	}
	
	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public List<MyBill> getBills() {
		return bills;
	}

	public void setBills(List<MyBill> bills) {
		this.bills = bills;
	}

	public List<MyCustomer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<MyCustomer> customers) {
		this.customers = customers;
	}

	/** Classes */
	public enum CustomerState { DONE_EATING, BILLED, PAID, DONE };
	public class MyCustomer{
		private Customer customer;
		Waiter waiter;
		private double bill;
		private double payment;
		public CustomerState state;
		
		MyCustomer(Customer c, Waiter w, double b){
			setCustomer(c);
			waiter = w;
			setBill(b);
			setPayment(0);
			state = CustomerState.DONE_EATING;
		}

		public double getPayment() {
			return payment;
		}

		public void setPayment(double payment) {
			this.payment = payment;
		}

		public double getBill() {
			return bill;
		}

		public void setBill(double bill) {
			this.bill = bill;
		}

		public Customer getCustomer() {
			return customer;
		}

		public void setCustomer(Customer customer) {
			this.customer = customer;
		}
	}

	enum BillState { PENDING, DONE }
	public class MyBill{
		private double amount;
		Market market;
		BillState state;
		
		MyBill(double a, Market m){
			setAmount(a);
			market = m;
			state = BillState.PENDING;
		}
		
		public Market getMarket(){
			return market;
		}

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
		}
	}
}