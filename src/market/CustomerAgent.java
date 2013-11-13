package market;

import java.util.ArrayList;
import java.util.List;

import market.interfaces.Cashier;
import market.interfaces.Customer;
import agent.Agent;

public class CustomerAgent extends Agent implements Customer{
	String name;
	private List<Item> Inventory = new ArrayList<Item>();
	private List<Item> ShoppingList = new ArrayList<Item>();
	private double cash;
	private double ExpectedCost;
	private double ActualCost;
	private Cashier cashier;
	private Customerstate state;
	private Customerevent event;
	
	public enum Customerstate {Idle, GoingToOrder, Waiting, Paid};
	public enum Customerevent {Nothing, WaitingInLine, Paying, Leaving, doneLeaving};
	
	//Message
	public String getMaitreDName(){
		return name;
	}

	public String getName(){
		return name;
	}
	
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

	
}