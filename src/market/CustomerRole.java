package market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import market.gui.CustomerGui;
import market.gui.MarketBuilding;
import market.interfaces.Cashier;
import market.interfaces.Customer;
import market.interfaces.CustomerGuiInterfaces;
import agent.Agent;
import agent.PersonAgent;
import agent.Role;
import agent.gui.Gui;
import agent.interfaces.Person;

/**
 * The Role of Customers in Market!
 * @author AnThOnY
 *
 */
public class CustomerRole extends Role implements Customer{
	String name;
	//private MarketBuilding workingBuilding = null;
	private CustomerGuiInterfaces customerGui = null;
	
	private List<Item> ShoppingList = new ArrayList<Item>();
	boolean atBuilding;
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
	
	/**
	 * this is the default (one and only one) constructor of the customerRole
	 * @param NA This is the customerRole's name
	 * @param money The is the current-onHand money that Customer has
	 * @param SL This is the shoppingList of the CustomerRole
	 * @param person The person itself
	 *  
	 */
	public CustomerRole(String NA, double money, List<Item>SL, Person person){
		super(person);
		setCash(money);
		name = NA;
		ShoppingList = SL;
		
		//workingBuilding = Market;
	}
	
	//Message
	/**
	 * ALERT :: ONLY CALLS THIS FUNCTION IF THE CUSTOMER PERSON IS AT THE MARKET
	 * This is the function to call that actually wakes The CustomerRole that it is going to buy stuff in the market
	 */
	public void goingToBuy(){
		if (person.getWorkRole()!= null && person.getWorkRole().isAtWork()){
			atBuilding = false;
			setState(Customerstate.GoingToOrder);
			setEvent(Customerevent.WaitingInLine);
			stateChanged();
		}	
		else{
			setCash(person.getWallet().getCashOnHand());
			atBuilding = true;
			setState(Customerstate.EnteringMarket);
			setEvent(Customerevent.GoingToLine);
			stateChanged();
		}
	}
	
	/**
	 * This is the message from the cashier that providing the invoice (cost) of the deliveryitems and also giving the list of missingitems from the customer's order
	 * @param cost the invoice
	 * @param MissingItems a list of items that are missing from the order
	 */
	public void msgHereisYourTotal(double cost, List<Item> MissingItems){	
		/*
		 *   ** NOW GOING TO BUY ANYWAY **
		 * 
		 * if MissingItems!=Empty()?
		 * 		Leave or Continue to Buy
		 */
		setActualCost(cost);
		setEvent(Customerevent.Paying);
		stateChanged();
	}

	/**
	 * 
	 * This is the message from the cashier
	 * @param Items The DeliveryList
	 */
	public void msgHereisYourItem(List<Item> Items) {
		//print ("Receive items from Cashier");
		
		for (int i=0;i<Items.size();i++){
			person.addItemsToInventory(Items.get(i).name, Items.get(i).amount);
		}
		/*
		Map<String,Integer> CurrentInvent = person.getInventory();
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
		if (atBuilding){
			setState(Customerstate.Paid);
			setEvent(Customerevent.Leaving);
			stateChanged();
		}
	}
	
	/**
	 * When the cashier said that no item can be satisfied at all
	 */
	public void msgNoItem(){
		setState(Customerstate.Paid);
		setEvent(Customerevent.Leaving);
		stateChanged();
	}
	
	/**
	 * Animation!
	 * getting the semaphore release
	 */
	public void msgAnimationFinishedGoToCashier(){
		//print ("At FrontDesk now");
		atFrontDesk.release();
		setState(Customerstate.GoingToOrder);
		setEvent(Customerevent.WaitingInLine);
		stateChanged();
	}
	
	/**
	 * Animation!
	 * getting the semaphore release
	 */
	public void msgAnimationFinishedLeaveMarket(){
		atExit.release();
		setState(Customerstate.NotAtMarket);
		setEvent(Customerevent.doneLeaving);
		this.deactivate();
	}
	
	//Scheduler
	/**
	 * This is the CustomerRole's scheduler and do all the actions
	 */
	public boolean pickAndExecuteAnAction() {
		
		if (getState() == Customerstate.EnteringMarket && getEvent() == Customerevent.GoingToLine) 
		{
			GoToFindCashier();
			return true;
		}
		if (atBuilding && getState() == Customerstate.GoingToOrder && getEvent() == Customerevent.WaitingInLine) 
		{
			OrderItems(ShoppingList);
			return true;
		}
		if (getState() == Customerstate.Waiting && getEvent() == Customerevent.Paying)
		{
				PayItems(getActualCost());
				return true;
		}
		if (getState() == Customerstate.Paid && getEvent() == Customerevent.Leaving)
		{
				Leaving();
				return true;
		}
			return false;

	}
	
	//Action
	/**
	 * Animation for walking to the front desk
	 */
	private void GoToFindCashier(){
		//print ("Going to the Front Desk");
		if(customerGui!=null)
		customerGui.DoGoToFrontDesk();
		try {
			atFrontDesk.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * The actions to buy items IN THE MARKET
	 * @param ShoppingList The list of items that are going to buy
	 */
	private void OrderItems(List<Item> ShoppingList){
		//print ("Order Items");
		cashier.msgIWantItem(ShoppingList, this);
		ExpectedCost = 0;
		for (int i=0;i<ShoppingList.size();i++){
			double CurrentPrice = PriceList.get(ShoppingList.get(i).name);
			ExpectedCost = ExpectedCost + CurrentPrice*ShoppingList.get(i).amount;
		}
		setState(Customerstate.Waiting);
	}
	
	/**
	 * TODO Work on the set Cash if the customer is not at the market (DeliveryGuy)
	 * @param cost The invoice
	 */
	private void PayItems(double cost){
		//print ("Pay Items");
		setState(Customerstate.Paid);
		if (cost == ExpectedCost){
			if (getCash() >= cost){
				cashier.msgHereIsPayment(cost, this);
				setCash(getCash() - cost);
				setState(Customerstate.Paid);
			}
			else{	//not enough money
				cashier.msgHereIsPayment(getCash(), this);
				setCash(0);
			}
		}
		if (cost != ExpectedCost){	//doesn’t match with the expected cost
			if (getCash() >= cost){
				cashier.msgHereIsPayment(cost, this);
				setCash(getCash() - cost);
				setState(Customerstate.Paid);
			}
			else{	//not enough money
				cashier.msgHereIsPayment(getCash(), this);
				setCash(0);
			}
		}
		
		person.getWallet().setCashOnHand(getCash());
	}
	
	/**
	 * The animation to leave the market and deactivate the role
	 */
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
	public void setGui (Gui cuGui){
		customerGui = (CustomerGuiInterfaces) cuGui;
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
	
	public void setCashOnHand (double money){
		this.getPerson().getWallet().setCashOnHand(money);
	}
	
	public double getCashOnHand(){
		return this.getPerson().getWallet().getCashOnHand();
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public Customerstate getState() {
		return state;
	}

	public void setState(Customerstate state) {
		this.state = state;
	}

	public Customerevent getEvent() {
		return event;
	}

	public void setEvent(Customerevent event) {
		this.event = event;
	}

	public double getActualCost() {
		return ActualCost;
	}

	public void setActualCost(double actualCost) {
		ActualCost = actualCost;
	}

	


	
}