package market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import market.gui.CustomerGui;
import market.gui.Gui;
import market.gui.MarketBuilding;
import market.interfaces.Cashier;
import market.interfaces.Customer;
import agent.Agent;
import agent.PersonAgent;
import agent.Role;
import agent.interfaces.Person;

public class CustomerRole extends Role implements Customer{
	String name;
	//private MarketBuilding workingBuilding = null;
	private CustomerGui customerGui = null;
	
	private List<Item> ShoppingList = new ArrayList<Item>();
	private double cash;
	private double ExpectedCost;
	private double ActualCost;
	private Cashier cashier;
	private Customerstate state;
	private Customerevent event;
	private Map<String,Double>PriceList;
	private Semaphore atFrontDesk = new Semaphore (0,true);
	private Semaphore atExit = new Semaphore (0,true);
	public enum Customerstate {Idle, GoingToOrder, Waiting, Paid, EnteringMarket, NotAtMarket};
	public enum Customerevent {Nothing, WaitingInLine, Paying, Leaving, doneLeaving, GoingToLine};
	
	public CustomerRole(String NA, double money, List<Item>SL, Person person){
		super(person);
		cash = person.getWallet().getCashOnHand();
		cash = money;
		name = NA;
		ShoppingList = SL;
		//workingBuilding = Market;
	}
	
	//Message
	public void goingToBuy(){
		//print ("In function 'going to buy'");
		state = Customerstate.EnteringMarket;
		event = Customerevent.GoingToLine;
		stateChanged();
	}
	
	public void msgHereisYourTotal(double cost, List<Item> MissingItems){	
		/*
		 *   ** NOW GOING TO BUY ANYWAY **
		 * 
		 * if MissingItems!=Empty()?
		 * 		Leave or Continue to Buy
		 */
		ActualCost = cost;
		event = Customerevent.Paying;
		stateChanged();
	}

	public void msgHereisYourItem(List<Item> Items) {
		print ("Receive items from Cashier");
		
		for (int i=0;i<Items.size();i++){
			person.addItemsToInventory(Items.get(i).name, Items.get(i).amount);
		}
		Map<String,Integer> CurrentInvent = person.getInventory();
		/*
		print ("I have " + CurrentInvent.get("Krabby Patty") + "Krabby Patty in my inventory");
		print ("I have " + CurrentInvent.get("Kelp Shake") + "Kelp Shake in my inventory");
		print ("I have " + CurrentInvent.get("Coral Bits") + "Coral Bits in my inventory");
		print ("I have " + CurrentInvent.get("Kelp Rings") + "Kelp Rings in my inventory");
		print ("I have " + CurrentInvent.get("LamboFinny") + "LamboFinny in my inventory");
		print ("I have " + CurrentInvent.get("Toyoda") + "Toyoda in my inventory");
		*/
		
		/*
		for (int i=0;i<Inventory.size();i++){
			Item CurrentItem = Inventory.get(i);
			for (int j=0;j<Items.size();j++){
				if (Items.get(j) == CurrentItem){
					CurrentItem.amount = CurrentItem.amount + Items.get(j).amount;
				}
			}
		}
		*/
			state = Customerstate.Paid;
			event = Customerevent.Leaving;
		stateChanged();
	}
	
	public void msgNoItem(){
		state = Customerstate.Paid;
		event = Customerevent.Leaving;
		stateChanged();
	}
	
	public void msgAnimationFinishedGoToCashier(){
		//print ("At FrontDesk now");
		atFrontDesk.release();
		state = Customerstate.GoingToOrder;
		event = Customerevent.WaitingInLine;
		stateChanged();
	}

	public void msgAnimationFinishedLeaveMarket(){
		atExit.release();
		state = Customerstate.NotAtMarket;
		event = Customerevent.doneLeaving;
	}
	
	//Scheduler
	protected boolean pickAndExecuteAnAction() {
		
		if (state == Customerstate.EnteringMarket && event == Customerevent.GoingToLine) 
		{
			GoToFindCashier();
			return true;
		}
		if (state == Customerstate.GoingToOrder && event == Customerevent.WaitingInLine) 
		{
			OrderItems(ShoppingList);
			return true;
		}
		if (state == Customerstate.Waiting && event == Customerevent.Paying)
		{
				PayItems(ActualCost);
				return true;
		}
		if (state == Customerstate.Paid && event == Customerevent.Leaving)
		{
				Leaving();
				return true;
		}
			return false;

	}
	
	//Action
	private void GoToFindCashier(){
		//print ("Going to the Front Desk");
		customerGui.DoGoToFrontDesk();
		try {
			atFrontDesk.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void OrderItems(List<Item> ShoppingList){
		//print ("Order Items");
		cashier.msgIWantItem(ShoppingList, this);
		ExpectedCost = 0;
		for (int i=0;i<ShoppingList.size();i++){
			double CurrentPrice = PriceList.get(ShoppingList.get(i).name);
			ExpectedCost = ExpectedCost + CurrentPrice*ShoppingList.get(i).amount;
		}
		state = Customerstate.Waiting;
	}
	
	private void PayItems(double cost){
		//print ("Pay Items");
		state = Customerstate.Paid;
		if (cost == ExpectedCost){
			if (cash >= cost){
				cashier.msgHereIsPayment(cost, this);
				cash -= cost;
				state = Customerstate.Paid;
			}
			else	//not enough money
				cashier.msgHereIsPayment(cash, this);
				cash = 0;
		}
		if (cost != ExpectedCost){	//doesn’t match with the expected cost
			if (cash >= cost){
				cashier.msgHereIsPayment(cost, this);
				cash -= cost;
				state = Customerstate.Paid;
			}
			else	//not enough money
				cashier.msgHereIsPayment(cash, this);
				cash = 0;
		}
		
		person.getWallet().setCashOnHand(cash);
	}
	
	private void Leaving() {
		//print ("Leaving Market");
		customerGui.DoExitMarket();
		try {
			atExit.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Utilities
	public void setGui (CustomerGui cuGui){
		customerGui = cuGui;
	}
	public Gui getGui (){
		return customerGui;
	}
	public void setCashier(Cashier ca){
		cashier = ca;
	}
	
	public String getMaitreDName(){
		return name;
	}

	public String getName(){
		return name;
	}

	public void setShoppingList(List<Item> SL) {
		ShoppingList = SL;
	}
	
	public void setPriceList (Map<String,Double> PL){
		PriceList = PL;
	}



	
}