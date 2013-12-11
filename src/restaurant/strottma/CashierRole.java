package restaurant.strottma;

import gui.trace.AlertTag;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import restaurant.strottma.interfaces.Cashier;
import restaurant.strottma.interfaces.Customer;
import restaurant.strottma.interfaces.Waiter;
import restaurant.strottma.test.mock.EventLog;
import CommonSimpleClasses.CityLocation;
import agent.WorkRole;
import agent.interfaces.Person;

/**
 * Restaurant Cashier Role
 * 
 * @author Erik Strottmann
 */
public class CashierRole extends WorkRole implements Cashier {

	public EventLog log = new EventLog();

	// private CashierGui cashierGui = null;
	private List<MyBill> bills = Collections.synchronizedList(new ArrayList<MyBill>()); // money owed to markets
	private List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	DecimalFormat df = new DecimalFormat("#.##");
	private double money;
	private boolean leaveWork = false;
	
	public CashierRole(Person person, CityLocation location) {
		super(person, location);
		
		
		// TODO Is this timer doing anything at all? -Diego
		new Timer();
		this.money = 200.00; // default
	}
	
	@Override
	public void activate() {
		super.activate();
		leaveWork = false;
	}

	/* Messages */
	// from Waiter
	public void msgDoneEating(Customer c, Waiter w, double bill) {
		Do(AlertTag.RESTAURANT, "Received msg that " + c + " is done eating.");
		MyCustomer mc = findCustomer(c);
		if (mc == null) {
			mc = new MyCustomer(c, w, bill, CState.DONE_EATING);
			customers.add(mc);
		} else {
			mc.waiter = w;
			mc.bill += bill;
			mc.payment = 0;
			mc.setState(CState.DONE_EATING);
		}
		stateChanged();
	}
	
	// from Customer
	public void msgHereIsPayment(Customer c, double payment) {
		MyCustomer mc = findCustomer(c);
		mc.payment = payment;
		mc.setState(CState.PAID);
		stateChanged();
	}

	@Override
	// from market cashier
	/* TODO LOOK HERE FOR HELP WITH RESTAURANT-MARKET INTEGRATION (1/3) */
	public void msgHereIsYourTotal(double total,
			market.interfaces.Cashier cashier) {
		// TODO Auto-generated method stub
		Do(AlertTag.RESTAURANT, "Received a bill from " + cashier);
		bills.add(new MyBill(total, cashier));
		stateChanged();
	}
	
	@Override
	public void msgLeaveWork() {
		leaveWork = true;
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		
		if (money != 0) {
			synchronized (bills) {
				for (MyBill bill : bills) {
					payBill(bill);
					return true;
				}
			}
		}
		
		synchronized (customers) {
			for (MyCustomer mc : customers) {
				if (mc.getState() == CState.DONE_EATING) {
					billCustomer(mc);
					return true;
				}
			}
		}
		
		synchronized (customers) {
			for (MyCustomer mc : customers) {
				if (mc.getState() == CState.PAID) {
					giveChange(mc);
					return true;
				}
			}
		}
		
		if (leaveWork) { deactivate(); }
		
		// We have tried all our rules and found nothing to do. So return false
		// to main loop of abstract Role and wait.
		return false;
	}

	/* Actions */
	private void billCustomer(MyCustomer mc) {
		Do(AlertTag.RESTAURANT, "Customer " + mc.customer.getName() +
				" owes $" + df.format(mc.bill));
		mc.waiter.msgHereIsBill(mc.customer, mc.bill);
		mc.setState(CState.BILLED);
	}
	
	private void giveChange(MyCustomer mc) {
		// DoGiveChange(mc.customer) // animation -disabled (hopefully forever)
		if (mc.payment > mc.bill) {
			double change = mc.payment - mc.bill;
			Do(AlertTag.RESTAURANT, "Customer " + mc.customer
					+ "'s change is $" + df.format(change));
			mc.customer.msgHereIsChange(change);
			money += mc.bill;
			mc.bill = 0.00;
			
		} else if (mc.payment < mc.bill) {
			double debt = mc.bill - mc.payment;
			Do(AlertTag.RESTAURANT, "Customer " + mc.customer +
					" still owes $" + df.format(debt));
			mc.customer.msgHereIsChange(0.00);
			mc.bill = debt;
			money += mc.payment;
			
		} else {
			Do(AlertTag.RESTAURANT, "Customer " + mc.customer +
					" paid with exact change.");
			mc.customer.msgHereIsChange(0.00);
			mc.bill = 0.00;
			money += mc.payment;
		}
		mc.setState(CState.DONE);
	}
	
	
	/* TODO LOOK HERE FOR HELP WITH RESTAURANT-MARKET INTEGRATION (2/3) */
	private void payBill(MyBill bill) {
		if (money >= bill.amount) {
			Do(AlertTag.RESTAURANT, "Paying $" + df.format(bill.amount) +
					" to " + bill.cashier.getName());
			bill.cashier.msgHereIsPayment(bill.amount, this);
			money -= bill.amount;
			bills.remove(bill);
			
		} else {
			// extra credit
			Do(AlertTag.RESTAURANT, "Cannot pay the market in full. "
					+ "Paying as much as I can...");
			bill.cashier.msgHereIsPayment(money, this);
			money = 0.00;
			bill.amount -= money;
		}
	}

	/* The animation DoXYZ() routines */
	

	/* Utilities */
		
	/*
	public void setGui(CashierGui gui) {
		cashierGui = gui;
	}

	public CashierGui getGui() {
		return cashierGui;
	}
	*/
	
	public double getMoney() {
		return money;
	}
	
	public void setMoney(double money) {
		this.money = money;
	}
	
	public List<MyBill> getBills() {
		return bills;
	}
	
	private MyCustomer findCustomer(Customer c) {
		synchronized (customers) {
			for (MyCustomer mc : customers) {
				if (mc.customer.equals(c)) {
					return mc;
				}
			}	
		}
		return null;
	}
	
	public List<MyCustomer> getCustomers() {
		return customers;
	}
	
	public enum CState {DONE_EATING, BILLED, PAID, DONE};
	public class MyCustomer {
		Customer customer;
		Waiter waiter;
		double bill;
		double payment;
		private CState state;
		
		public MyCustomer(Customer c, Waiter w, double total, CState state) {
			this.customer = c;
			this.waiter = w;
			this.bill = total;
			this.payment = 0.00;
			this.state = state;
		}

		public void setState(CState state) {
			this.state = state;
		}

		public CState getState() {
			return state;
		}

		public double getBill() {
			return bill;
		}
		
		public Customer getCustomer() {
			return customer;
		}

		public double getPayment() {
			return payment;
		}
	}
	
	/* TODO LOOK HERE FOR HELP WITH RESTAURANT-MARKET INTEGRATION (3/3) */
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
	
	@Override
	public boolean isAtWork() {
		return isActive() && !isOnBreak();
	}

	@Override
	public boolean isOnBreak() {
		// TODO maybe cashiers can go on breaks in v3
		return false;
	}
	
}

