package restaurant.vdea;

import gui.trace.AlertTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

import restaurant.vdea.gui.CashierGui;
import restaurant.vdea.interfaces.*;
import restaurant.vdea.test.mock.EventLog;
import restaurant.vdea.test.mock.LoggedEvent;
import CommonSimpleClasses.CityLocation;
import agent.WorkRole;
import agent.interfaces.Person;

public class CashierRole extends WorkRole implements Cashier{

	public List<Transaction> transactions = Collections.synchronizedList(new ArrayList<Transaction>());
	public List<MarketBill> bills = Collections.synchronizedList(new ArrayList<MarketBill>());
	public enum PaymentStatus
	{pending, computed, paymentReceived, paid, notEnough, done};
	//public PaymentStatus status = PaymentStatus.pending;//The start state
	
	public enum BillStatus {pending, paid};
	public BillStatus bStatus = BillStatus.pending;

	private String name;
	private double bank;
	private CashierGui gui = null;
	
	public EventLog log = new EventLog();
	
	private boolean offWork = false;
	
	public CashierRole(Person person, CityLocation location) {
		super(person, location);
		
		this.name = super.getName();
		bank = 60;
	}
	
	@Override
	public void activate() {
		super.activate();
		offWork = false;
		gui.DoGoToCashRegister();
	}
	
	// Messages
	
		//from waiter
		public void msgComputeBill(Waiter w, Customer c, String choice, int table) {
			log.add(new LoggedEvent("Received msgComputeBill from waiter."));
			Do(AlertTag.RESTAURANT,"Recieved new bill to compute");
			transactions.add(new Transaction(w, c, choice, table));
			//status = PaymentStatus.pending;
			stateChanged();
		}
		
		//from customer
		public void msgPayment(Customer c, double check, double cash){
			log.add(new LoggedEvent("Received msgPayment from customer. Check = "+ check + " Cash = " + cash));
			System.out.printf(name + ": recieved $%.2f cash from customer " + c.getName() + "\n", cash);
			for (Transaction t: transactions){
				if (t.c == c){
					if (cash >= check){
						t.status = PaymentStatus.paymentReceived;
					}
					else{
						Do(AlertTag.RESTAURANT, c.getName() + " doesn't have enough money!");
						t.status = PaymentStatus.notEnough;
					}
					t.payment = cash;
					stateChanged();
				}
			}
		}
			
		//from Market
		/*public void msgMarketBill(Market m, double bill){
			log.add(new LoggedEvent("Received MarketBill from market. Bill = "+ bill));
			Do(AlertTag.RESTAURANT, "recieved bill from market " + m.getName());
			bills.add(new MarketBill(m, bill));
			bStatus = BillStatus.pending;
			stateChanged();
		}*/
		
		@Override
		public void msgHereIsYourTotal(double total, market.interfaces.Cashier cashier) {
			log.add(new LoggedEvent("Received MarketBill from market."));
			Do(AlertTag.RESTAURANT, "recieved bill from market");
			bills.add(new MarketBill(total, cashier));
			bStatus = BillStatus.pending;
			stateChanged();
			
		}
		

		/**
		 * Scheduler.  Determine what action is called for, and do it.
		 */
		public boolean pickAndExecuteAnAction() {
			synchronized(bills){
				for (MarketBill b: bills){
					if (bStatus == BillStatus.pending){
						payMarketBill(b);
						bStatus = BillStatus.paid;
						return true;
					}
				}
			}
			synchronized(transactions){
				for (Transaction t : transactions) {
					if(t.status == PaymentStatus.pending){
						calculateBill(t);
						t.status = PaymentStatus.computed;
					}
					if (t.status == PaymentStatus.notEnough){
						debtCustomer(t);
						t.status = PaymentStatus.done;
						return true;
					}
					if (t.status == PaymentStatus.paymentReceived){
						chargeIt(t);
						t.status = PaymentStatus.paid;
						return true;
					}
				}
			}
			if (offWork) {
				deactivate();
				gui.DoLeave();
			}
			return false;
		}

		// Actions
		
		private void payMarketBill(MarketBill b){
			double check = 0;
			if(bank>=b.bill){
				check = b.bill;	//write check for amnt
				bank -= b.bill;			//deduct from bank
				Do(AlertTag.RESTAURANT, "Paying bill to market: " + b.m.getName());
				b.m.msgHereIsPayment(check, this);
				bills.remove(b);
			}
			else{
				check = bank;
				bank = 0;
				b.m.msgHereIsPayment(check,this);
				b.debt = b.bill-check;
			}
			
		}

		private void calculateBill(Transaction t){
			Do(AlertTag.RESTAURANT, "Calculating bill for table " + t.table);
			t.w.msgHereIsCheck(t.total, t.table);
		}
		
		private void debtCustomer(Transaction t){
			double debt = t.total - t.payment;
			System.out.printf(name + ": Customer " + t.c.getName() + " is $%.2f in debt%n", debt);
			t.c.msgYouHaveDebt(debt);
			transactions.remove(t);
		}

		private void chargeIt(Transaction t){
			Do(AlertTag.RESTAURANT, "calculating change...");
			bank += t.payment;
			double change = t.getChange();
			t.c.msgHereIsYourChange(change);
			transactions.remove(t);
			
			try{
			for(MarketBill b: bills){
				if(b.debt>0){
					if(b.debt>bank){
						b.debt-=bank;
						bank = 0;
					}
					else
					{
						bank-=b.debt;
						b.m.msgHereIsPayment(b.debt, this);
						bills.remove(b); //TODO
						
					}
				}
			}
			}
			catch(ConcurrentModificationException e){}
		}

		public class Transaction{
			public Waiter w;
			public Customer c;
			public int table;
			public Food choice;
			public double total;
			public double payment;
			public PaymentStatus status = PaymentStatus.pending;

			Transaction(Waiter inW, Customer inC, String inChoice, int t){
				w = inW;
				c = inC;
				table = t;
				choice = new Food(inChoice);
				total = choice.price;
			}
			
			public double getChange(){
				return payment-total;
			}
		}
		
		public class MarketBill{
			//public Market m;
			//public double bill;
			public double debt = 0;
			double bill;
			market.interfaces.Cashier m;
			
			MarketBill(double t, market.interfaces.Cashier c){
				//m = newM;
				//bill = newBill;
				bill = t;
				m = c;
				
			}
		}

		@Override
		public boolean isAtWork() {
			return isActive() && !isOnBreak();
		}

		@Override
		public boolean isOnBreak() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void msgLeaveWork() {
			offWork = true;
		}

		public void setGui(CashierGui cashierGui) {
			gui = cashierGui;
		}

	


}
