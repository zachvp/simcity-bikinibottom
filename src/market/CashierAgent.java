package market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.interfaces.Cashier;
import market.interfaces.CityBuilding;
import market.interfaces.Customer;
import market.interfaces.DeliveryGuy;
import market.interfaces.ItemCollector;
import agent.Agent;

public class CashierAgent extends Agent implements Cashier{

//public EventLog log = new EventLog();
	private String name;
	double cash;
	private List<MyCustomer> MyCustomerList = new ArrayList<MyCustomer>();
	private List<ItemCollector> ICList;
	private List<DeliveryGuy> DGList;
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
	
	public enum Customerstate {Arrived, Ordered, Collected, Paid, OrderPlaced, WaitingForCheck}
	
	private class MyCustomer {
		Customer c;
		CityBuilding Building;
		List<Item> OrderList = new ArrayList<Item>();
		List<Item> DeliveryList = new ArrayList<Item>();
		ItemCollector itemCollector;
		DeliveryGuy deliveryGuy;
		Customerstate state = Customerstate.Arrived;

	}
	
	//Cashier Message 
	public String getMaitreDName(){
		return name;
	}

	public String getName(){
		return name;
	}
	
	public void msgPhoneOrder(List<Item>ShoppingList, Customer C, CityBuilding building)	
	{				//The Customer will be the phone calling guy
		MyCustomer MC = new MyCustomer();
		MC.c = C;
		MC.state = Customerstate.Ordered;
		MC.Building = building;
		for (int i=0;i<ShoppingList.size();i++){
			MC.OrderList.add(ShoppingList.get(i));
		}
		MyCustomerList.add(MC);
		stateChanged();
	}
	
	public void msgIWantItem(List<Item> ShoppingList, Customer C) //[Customer to Cashier]
	{
		MyCustomer MC = new MyCustomer();
		MC.c = C;
		MC.state = Customerstate.Ordered;
		MC.Building = null;
		for (int i=0;i<ShoppingList.size();i++){
			MC.OrderList.add(ShoppingList.get(i));
		}
		MyCustomerList.add(MC);
		stateChanged();
	}

	public void msgHereAreItems(List<Item> Items, Customer c)
	{

		for (int i=0;i<MyCustomerList.size();i++){
			if (MyCustomerList.get(i).c == c)
			{
				MyCustomerList.get(i).state = Customerstate.Collected;
				MyCustomerList.get(i).DeliveryList = Items;
			}
		}
		
		stateChanged();
	}			
	
	public void msgHereIsPayment(double payment, Customer c)
	{
		for (int i=0;i<MyCustomerList.size();i++){
			if (MyCustomerList.get(i).c == c){
				MyCustomerList.get(i).state = Customerstate.Paid;
				cash += payment;
			}
		}
		stateChanged();
	}


	//Scheduler
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		for (int i=0;i<MyCustomerList.size();i++){
			if (MyCustomerList.get(i).state == Customerstate.Ordered){
				ItemCollector tempIC = ICList.get(0);
				for (int j=1;j<ICList.size();j++){
					if (ICList.get(j).msgHowManyOrdersYouHave() <= tempIC.msgHowManyOrdersYouHave())
						tempIC = ICList.get(j);
					else
						continue;
				}
				GoGetItems(MyCustomerList.get(i),tempIC);
				return true;
			}
		}
		
		for (int i=0;i<MyCustomerList.size();i++){
			if (MyCustomerList.get(i).state == Customerstate.Collected){
				CalculatePayment(MyCustomerList.get(i));
				return true;
			}
		}
		
		for (int i=0;i<MyCustomerList.size();i++){
			if (MyCustomerList.get(i).state == Customerstate.Paid){
				GiveItems(MyCustomerList.get(i));
				return true;
			}
		}

		return false;

	}
	
	//Actions
	private void GoGetItems(MyCustomer MC, ItemCollector IC){
		IC.msgGetTheseItem(MC.OrderList, MC.c);
		MC.itemCollector = IC;
		MC.state = Customerstate.OrderPlaced;
	}

	private void CalculatePayment(MyCustomer MC){
		double total = 0;
		for (int i=0;i<MC.DeliveryList.size();i++){
			double CurrentPrice = PriceList.get(MC.DeliveryList.get(i).name);
			total += CurrentPrice*MC.DeliveryList.get(i).amount;
		}
		MC.c.msgHereisYourTotal(total);
		MC.state = Customerstate.WaitingForCheck;
	}

	private void GiveItems (MyCustomer MC){
		if (MC.Building != null)
			MC.c.msgHereisYourItem(MC.DeliveryList);
		else
			for (int i=0;i<DGList.size();i++){
				if(DGList.get(i).msgAreYouAvailable()){
					DGList.get(i).msgDeliverIt(MC.DeliveryList, MC.Building);
				}
			}
	}

	
	
}