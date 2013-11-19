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
	private double cash;
	private List<MyCustomer> MyCustomerList = new ArrayList<MyCustomer>();
	private List<ItemCollector> ICList = new ArrayList<ItemCollector>();
	private List<DeliveryGuy> DGList = new ArrayList<DeliveryGuy>();
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
	
	public enum Customerstate {Arrived, Ordered, Collected, Paid, OrderPlaced, WaitingForCheck, GivenItems, Failed, EpicFailed}
	
	public class MyCustomer {
		Customer c;
		CityBuilding Building;
		List<Item> OrderList = new ArrayList<Item>();
		private List<Item> DeliveryList = new ArrayList<Item>();
		private List<Item> MissingItemList = new ArrayList<Item>();
		ItemCollector itemCollector;
		DeliveryGuy deliveryGuy;
		public Customerstate state = Customerstate.Arrived;
		public List<Item> getDeliveryList() {
			return DeliveryList;
		}
		public void setDeliveryList(List<Item> deliveryList) {
			DeliveryList = deliveryList;
		}
	}
	
    public CashierAgent(String NA, double money){
		name = NA;
		setCash(money);
	}
	
	//Cashier Message 
	public void msgPhoneOrder(List<Item>ShoppingList, Customer C, CityBuilding building)	
	{				//The Customer will be the phone calling guy
		MyCustomer MC = new MyCustomer();
		MC.c = C;
		MC.state = Customerstate.Ordered;
		MC.Building = building;
		for (int i=0;i<ShoppingList.size();i++){
			MC.OrderList.add(ShoppingList.get(i));
		}
		getMyCustomerList().add(MC);
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
		getMyCustomerList().add(MC);
		stateChanged();
	}

	public void msgHereAreItems(List<Item> Items, List<Item> MissingItems, Customer c)
	{

		
		for (int i=0;i<getMyCustomerList().size();i++){
			if (getMyCustomerList().get(i).c == c)
			{
				//When there is no item in the shoppinglist can be satisified
				if (Items.isEmpty()){
					getMyCustomerList().get(i).state = Customerstate.EpicFailed;
					getMyCustomerList().get(i).MissingItemList = MissingItems;
					break;
				}
				//When there is some items that cannot be fulfilled
				else if (!MissingItems.isEmpty()){
					getMyCustomerList().get(i).state = Customerstate.Failed;
					getMyCustomerList().get(i).MissingItemList = MissingItems;
					getMyCustomerList().get(i).setDeliveryList(Items);
					break;
				}
				//All items can be fulfilled
				else
					getMyCustomerList().get(i).state = Customerstate.Collected;
					getMyCustomerList().get(i).setDeliveryList(Items);
			}
			
				
		}
		
		
		
		stateChanged();
	}			
	
	public void msgHereIsPayment(double payment, Customer c)
	{
		for (int i=0;i<getMyCustomerList().size();i++){
			if (getMyCustomerList().get(i).c == c){
				getMyCustomerList().get(i).state = Customerstate.Paid;
				setCash(getCash() + payment);
			}
		}
		stateChanged();
	}


	//Scheduler
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		for (int i=0;i<getMyCustomerList().size();i++){
			if (getMyCustomerList().get(i).state == Customerstate.Ordered){
				ItemCollector tempIC = getICList().get(0);
				for (int j=1;j<getICList().size();j++){
					if (getICList().get(j).msgHowManyOrdersYouHave() <= tempIC.msgHowManyOrdersYouHave())
						tempIC = getICList().get(j);
					else
						continue;
				}
				GoGetItems(getMyCustomerList().get(i),tempIC);
				return true;
			}
		}
		//No item is fulfilled
		for (int i=0;i<getMyCustomerList().size();i++){
			if (getMyCustomerList().get(i).state == Customerstate.EpicFailed){
				TellCustomerEpicFail(getMyCustomerList().get(i));
				return true;
			}
		}
		//Some or All items are fulfilled
		for (int i=0;i<getMyCustomerList().size();i++){
			if (getMyCustomerList().get(i).state == Customerstate.Collected){
				CalculatePayment(getMyCustomerList().get(i));
				return true;
			}
		}
		
		for (int i=0;i<getMyCustomerList().size();i++){
			if (getMyCustomerList().get(i).state == Customerstate.Paid){
				GiveItems(getMyCustomerList().get(i));
				return true;
			}
		}

		return false;

	}
	
	//Actions
	private void GoGetItems(MyCustomer MC, ItemCollector IC){
		MC.itemCollector = IC;
		MC.state = Customerstate.OrderPlaced;
		MC.itemCollector.msgGetTheseItem(MC.OrderList, MC.c);
		
	}

	private void TellCustomerEpicFail(MyCustomer MC){
		MC.state = Customerstate.Paid;
		MC.c.msgNoItem();
		
	}
	
	private void CalculatePayment(MyCustomer MC){
		double total = 0;
		for (int i=0;i<MC.getDeliveryList().size();i++){
			double CurrentPrice = PriceList.get(MC.getDeliveryList().get(i).name);
			total += CurrentPrice*MC.getDeliveryList().get(i).amount;
		}
		MC.state = Customerstate.WaitingForCheck;
		MC.c.msgHereisYourTotal(total, MC.MissingItemList);
		
	}

	private void GiveItems (MyCustomer MC){
		MC.state = Customerstate.GivenItems;
		if (MC.Building == null){
			MC.c.msgHereisYourItem(MC.getDeliveryList());
			for (int i=0;i<MyCustomerList.size();i++){
				if (MC == MyCustomerList.get(i)){
					MyCustomerList.remove(i);
				}
			}
		}
		else
			for (int i=0;i<getDGList().size();i++){
				if(getDGList().get(i).msgAreYouAvailable()){
					MC.deliveryGuy = getDGList().get(i);
					MC.deliveryGuy.msgDeliverIt(MC.getDeliveryList(), MC.c, MC.Building);
					for (int j=0;j<MyCustomerList.size();j++){
						if (MC == MyCustomerList.get(j)){
							MyCustomerList.remove(j);
							break;
						}
					}
					break;
				}
			}
	}

	//Utilities
	public void setDGList(List<DeliveryGuy> list){
		DGList = list;
	}
	public void addDGList(DeliveryGuy DG){
		getDGList().add(DG);
	}
	public void setICList(List<ItemCollector> list){
		ICList = list;
	}
	public void addICList(ItemCollector IC){
		getICList().add(IC);
	}
	public String getMaitreDName(){
		return name;
	}
	public String getName(){
		return name;
	}

	public List<MyCustomer> getMyCustomerList() {
		return MyCustomerList;
	}
	public void setMyCustomerList(List<MyCustomer> myCustomerList) {
		MyCustomerList = myCustomerList;
	}

	public List<ItemCollector> getICList() {
		return ICList;
	}

	public List<DeliveryGuy> getDGList() {
		return DGList;
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}
	
	
}