package restaurant.anthony;

import CommonSimpleClasses.CityLocation;
import agent.WorkRole;
import agent.interfaces.Person;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.anthony.gui.CashierGui;
import restaurant.anthony.interfaces.Cashier;
import restaurant.anthony.interfaces.Customer;
import restaurant.anthony.interfaces.Waiter;
import restaurant.anthony.interfaces.Market;

/**
 * Restaurant Cashier Agent
 */
// Cashier Agent
public class CashierRole extends WorkRole implements Cashier {

	public double money ;
	Timer timer = new Timer();
	public CashierGui cashierGui = null;
	private HostRole host;
	private boolean leaveWork = false;
	private List<Check> MyCheckList = Collections.synchronizedList(new ArrayList<Check>());
	private List<Debt> DebtList = Collections.synchronizedList(new ArrayList<Debt>());
	Map<String, Double> priceList = new HashMap<String, Double>();
	private enum Cashierstate {Idle, OffWork, NotAtWork};
	private Cashierstate state = Cashierstate.NotAtWork;
	
	private Semaphore atHome = new Semaphore (0,true);

	public CashierRole(Person person, CityLocation location) {
		super(person, location);

		this.money = 200;

	}


	// Messages
	/* (non-Javadoc)
	 * @see restaurant.Cashier#ComputeBill(java.lang.String, int, restaurant.interfaces.Waiter)
	 */
	public void ComputeBill(String choice, int table, Waiter waiter) {
		Check check = new Check(choice, table, waiter);
		getMyCheckList().add(check);

		stateChanged();
	}
	
	public void HeresTheIngredientPrice(double price, Market ma){
		/*
		if (money - price >= 0){
			print ("I am able to pay for the ingredients");
			money = money - price;
			ma.HeresTheMoney(price);
		}
		*/
		
		print ("Receive the Debt msg from Market");
		Debt d = new Debt(price,ma);
		getDebtList().add(d);
		stateChanged();
		
		/*
		if (money - price < 0 ){
			print ("I am not able to pay for the ingredients");
			print ("I will give all my money first and pay the rest when I have enough money");
			ma.HeresTheMoney(money);
			double debt = price - money;
			Debt d = new Debt(debt,ma);
			money = 0;
			DebtList.add(d);
		}
		*/
	}

	/* (non-Javadoc)
	 * @see restaurant.Cashier#Payment(restaurant.CashierAgent.Check, double, restaurant.interfaces.Customer)
	 */
	public void Payment(Check check, double cash, restaurant.anthony.interfaces.Customer cust) {
		print("Payment received");
		check.Customer = cust;
		check.payment = cash;
		check.paid = true;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Cashier#PayDebt(double)
	 */
	public void PayDebt(double p) {
		print("Received Money from customer paying the previous meal");
		money = money + p;
	}
	

	public void msgAtHome(){
		state = Cashierstate.Idle;
		atHome.release();
		stateChanged();
	}

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		
		if (state == Cashierstate.NotAtWork){
			GoToWork();
			return true;
		}
		if (state == Cashierstate.Idle){
			synchronized(MyCheckList){
				for (int i = 0; i < getMyCheckList().size(); i++) {
					if (getMyCheckList().get(i).getPrice() == 0 && !getMyCheckList().get(i).GetIt) {
						getMyCheckList().get(i).GetIt = true;
						DoComputeCheck(getMyCheckList().get(i), i);
						return true;
					}
				}
			}
			synchronized(MyCheckList){
				for (int i = 0; i < getMyCheckList().size(); i++) {
					if (getMyCheckList().get(i).paid) {
						DoGiveChange(getMyCheckList().get(i));
						return true;
					}
				}
			}

			if (!getDebtList().isEmpty()){
				PayDebt();
				return true;
			}
		}

		return false;
	}

	// Actions
	private void GoToWork(){
		cashierGui.GoToWork();
		try {
			atHome.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void DoComputeCheck(final Check check, final int i) {


				print("Done computing " + check.choice);
				double p = priceList.get(check.choice);

				check.setPrice(p);
				check.Waiter.HereIsCheck(check);

	}

	private void DoGiveChange(final Check check) {
		if (check.payment >= check.getPrice()) {
			double change = check.payment - check.getPrice();
			money = money + check.getPrice();
			check.Customer.HereIsYourChange(change);
			synchronized(MyCheckList){
			for (int i = 0; i < getMyCheckList().size(); i++) {
				if (getMyCheckList().get(i) == check)
					getMyCheckList().remove(i);
			}
			}

		} else {
			double debt = check.getPrice() - check.payment;
			money = money + check.payment;
			check.Customer.HereIsYourDebt(debt);
			synchronized(MyCheckList){
			for (int i = 0; i < getMyCheckList().size(); i++) {
				if (getMyCheckList().get(i) == check)
					getMyCheckList().remove(i);
			}
			}
		}
	}
	
	private void PayDebt(){
		print ("I am going to pay debt");
		synchronized(DebtList){
		for (int i=0;i<getDebtList().size();i++){
			if (money >= getDebtList().get(i).debt){
				print ("I am able to pay the debt");
				getDebtList().get(i).ma.HeresTheMoney(getDebtList().get(i).debt);
				money = money - getDebtList().get(i).debt;
				getDebtList().remove(i);
			}
		}
		}
		
	}

	public List<Check> getMyCheckList() {
		return MyCheckList;
	}

	public void setMyCheckList(List<Check> myCheckList) {
		MyCheckList = myCheckList;
	}

	public List<Debt> getDebtList() {
		return DebtList;
	}

	public void setDebtList(List<Debt> debtList) {
		DebtList = debtList;
	}

	public class Debt {
		Market ma;
		double debt;
		Debt (double d, Market m){
			ma = m;
			debt = d;
		}
	}
	public class Check {
		int table;
		String choice;
		private double price;
		double payment;
		restaurant.anthony.interfaces.Waiter Waiter;
		restaurant.anthony.interfaces.Customer Customer;
		boolean doneComputing;
		boolean paid;
		boolean GetIt;

		Check(String food, int TN, restaurant.anthony.interfaces.Waiter WA) {
			choice = food;
			table = TN;
			Waiter = WA;
			// -1 means it hasn't calculate yet
			setPrice(0);
			payment = 0;
			GetIt = false;
			doneComputing = false;
			paid = false;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}
	}
	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return person.getName();
	}


	@Override
	public boolean isAtWork() {
		// TODO Auto-generated method stub
		return isActive() && !isOnBreak();
	}


	@Override
	public boolean isOnBreak() {

		return false;
	}


	@Override
	public void msgLeaveWork() {
		leaveWork = true;
		
	}
	
	public void setPriceList(Map<String, Double> pList){
		priceList = pList;
	}


	public void setGui(CashierGui caGui) {
		cashierGui = caGui;
		
	}
	
	public void setHost(HostRole h){
		host = h;
	}

}
