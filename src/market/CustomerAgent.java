package market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.interfaces.Cashier;
import market.interfaces.Customer;
import agent.Agent;

public class CustomerAgent extends Agent implements Customer{
	String name;
	private List<Item> tempInventoryList = new ArrayList<Item>();
	{
		tempInventoryList.add(new Item("CheapCar", 0));
		tempInventoryList.add(new Item("ExpensiveCar", 0));
		tempInventoryList.add(new Item("Pizza", 0));
		tempInventoryList.add(new Item("Sandwich", 0));
		tempInventoryList.add(new Item("Chicken", 0));
	}
	
	private List<Item> Inventory = tempInventoryList;
	private List<Item> ShoppingList = new ArrayList<Item>();
	private double cash;
	private double ExpectedCost;
	private double ActualCost;
	private Cashier cashier;
	private Customerstate state;
	private Customerevent event;
	private Map<String,Double>PriceList = new HashMap<String, Double>();
	{
		double CheapCar = 100;
		double ExpensiveCar = 300;
		double Pizza = 20;
		double Sandwich = 10;
		double Chicken = 15;
		PriceList.put("Chicken", Chicken);
		PriceList.put("Sandwich", Sandwich);
		PriceList.put("Pizza", Pizza);
		PriceList.put("ExpensiveCar", ExpensiveCar);
		PriceList.put("CheapCar", CheapCar);	
	}
	public enum Customerstate {Idle, GoingToOrder, Waiting, Paid};
	public enum Customerevent {Nothing, WaitingInLine, Paying, Leaving, doneLeaving};
	
	CustomerAgent(String NA, double money, List<Item>SL){
		cash = money;
		name = NA;
		ShoppingList = SL;
	}
	
	//Message
	public void msgHereisYourTotal(double cost){		
		ActualCost = cost;
		event = Customerevent.Paying;
		stateChanged();
	}

	public void msgHereisYourItem(List<Item> Items) {
		
		for (int i=0;i<Inventory.size();i++){
			Item CurrentItem = Inventory.get(i);
			for (int j=0;j<Items.size();j++){
				if (Items.get(j) == CurrentItem){
					CurrentItem.amount = CurrentItem.amount + Items.get(j).amount;
				}
			}
		}
			state = Customerstate.Paid;
			event = Customerevent.Leaving;
		stateChanged();
	}
	
	public void msgAtMarket() {
		state = Customerstate.GoingToOrder;
		event = Customerevent.WaitingInLine;
		stateChanged();
	}

	
	//Scheduler
	protected boolean pickAndExecuteAnAction() {
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
	private void OrderItems(List<Item> ShoppingList){
		cashier.msgIWantItem(ShoppingList, this);
		ExpectedCost = 0;
		for (int i=0;i<ShoppingList.size();i++){
			double CurrentPrice = PriceList.get(ShoppingList.get(i).name);
			ExpectedCost = ExpectedCost + CurrentPrice*ShoppingList.get(i).amount;
		}
		state = Customerstate.Waiting;
	}
	
	private void PayItems(double cost){
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
			//What to Do?
		}
	}
	
	private void Leaving() {
		/*
		if (InMarket)
			//Animation to leave the market
		Event = doneLeaving;
		return;
		*/
	}
	
	//Utilities
	public void setCashier(Cashier ca){
		cashier = ca;
	}
	
	public String getMaitreDName(){
		return name;
	}

	public String getName(){
		return name;
	}
	

	
}