package market;


import java.util.ArrayList;

import agent.Constants;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import market.gui.CashierGui;
import market.gui.Gui;
import market.interfaces.Cashier;
import market.interfaces.CashierGuiInterfaces;
import market.interfaces.Customer;
import market.interfaces.DeliveryGuy;
import market.interfaces.ItemCollector;
import market.test.mock.MockCashierGui;
import agent.Agent;
import agent.Constants;
import agent.PersonAgent;
import agent.Role;
import agent.TimeManager;
import agent.WorkRole;
import agent.Role.ScheduleTask;
import agent.interfaces.Person;

public class CashierRole extends WorkRole implements Cashier {

private static final int startingminute = 0;
	//public EventLog log = new EventLog();
	private CashierGuiInterfaces cashierGui = null;
	private String name;
	private double MarketTotalMoney;
	ScheduleTask task = new ScheduleTask();
	
	private Semaphore atFrontDesk = new Semaphore(0,true);
	private Semaphore atBench = new Semaphore (0,true);
	private Semaphore atExit = new Semaphore (0,true);

	private List<MyCustomer> MyCustomerList = new ArrayList<MyCustomer>();
	private List<ItemCollector> ICList = new ArrayList<ItemCollector>();
	private List<DeliveryGuy> DGList = new ArrayList<DeliveryGuy>();
	
	private Map<String,Integer> InventoryList = new HashMap<String,Integer>();
	{		//Initially The market has 100 inventory on each Item
		for (int i = 0 ; i < agent.Constants.FOODS.size(); i++){
			InventoryList.put(agent.Constants.FOODS.get(i), 100);
		}
		for (int i = 0 ; i < agent.Constants.CARS.size(); i++){
			InventoryList.put(agent.Constants.CARS.get(i), 100);
		}
	}
	
	//Working Hour
	int startinghour = 8;
	int startingminutes = 29;
	int endinghour = 18;
	int endingminutes = 0;
	
	private Map<String,Double>PriceList = new HashMap<String, Double>();
	{
		double Toyoda = 100;
		double LamboFinny = 300;
		double KrabbyPatty = 20;
		double KelpShake = 10;
		double CoralBits = 15;
		double KelpRings = 5;
		PriceList.put("Krabby Patty", KrabbyPatty);
		PriceList.put("Kelp Shake", KelpShake);
		PriceList.put("Coral Bits", CoralBits);
		PriceList.put("Kelp Rings", KelpRings);
		PriceList.put("LamboFinny", LamboFinny);
		PriceList.put("Toyoda", Toyoda);
		
	}
	public enum Cashierstate {GoingToWork, Idle, OffWork, GoingToGetItems};
	private Cashierstate state = Cashierstate.GoingToWork; 
	public enum Customerstate {Arrived, Ordered, Collected, Paid, OrderPlaced, WaitingForCheck, GivenItems, Failed, EpicFailed}
	
	public class MyCustomer {
		Customer c;
		CommonSimpleClasses.CityBuilding Building;
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
	
    public CashierRole(String NA, double money, Person person){
    	super(person);
		name = NA;
		setCash(money);
		
		Runnable command = new Runnable(){
			@Override
			public void run() {
				msgLeaveWork();
			
			}
		};
		
		
		
		
		int hour = 6;
		int minute = 30;
		
		task.scheduleDailyTask(command, hour, minute);
			
	}
	
	//Cashier Message 
	public void msgPhoneOrder(List<Item>ShoppingList, Customer C, CommonSimpleClasses.CityBuilding building)	
	{				//The Customer will be the phone calling guy
		print ("Received Phone Order");
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
		print ("Received Msg from Customer");
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
		print ("Received Items from ItemCollector");
		setState(Cashierstate.GoingToGetItems);
		
		int ShoppingListSize = 0;
		for (int i=0;i<Items.size();i++){
			ShoppingListSize += Items.get(i).amount;
		}
		int MissingItemListSize = 0;
		for (int i=0;i<MissingItems.size();i++){
			MissingItemListSize += MissingItems.get(i).amount;
		}
		//print ("ShoppingListSize : " + ShoppingListSize);
		//print ("MissingItemListSize : " + MissingItemListSize);
		
		
		for (int i=0;i<getMyCustomerList().size();i++){
			if (getMyCustomerList().get(i).c == c)
			{
				//When there is no item in the shoppinglist can be satisified
				if (ShoppingListSize == 0){
					//print ("Epic Failed");
					getMyCustomerList().get(i).state = Customerstate.EpicFailed;
					getMyCustomerList().get(i).MissingItemList = MissingItems;
					break;
				}
				//When there is some items that cannot be fulfilled
				else if (MissingItemListSize != 0){
					//print ("failed");
					getMyCustomerList().get(i).state = Customerstate.Failed;
					getMyCustomerList().get(i).MissingItemList = MissingItems;
					getMyCustomerList().get(i).setDeliveryList(Items);
					break;
				}
				//All items can be fulfilled
				else
					//print ("no problem");
					getMyCustomerList().get(i).state = Customerstate.Collected;
					getMyCustomerList().get(i).setDeliveryList(Items);
					break;
			}
			
				
		}
		
		
		
		stateChanged();
	}			
	
	public void msgHereIsPayment(double payment, Customer c)
	{
		print ("Receive payment from Customer ");
		for (int i=0;i<getMyCustomerList().size();i++){
			if (getMyCustomerList().get(i).c == c){
				getMyCustomerList().get(i).state = Customerstate.Paid;
				setCash(getCash() + payment);
			}
		}
		cashierGui.Update();
		stateChanged();
	}
	
	@Override
	public void msgLeaveWork(){
		setState(Cashierstate.OffWork);
		stateChanged();
	}

	//Animations
	public void AtFrontDesk(){
		atFrontDesk.release();
		setState(Cashierstate.Idle);
		stateChanged();
	}
	
	public void AtBench(){
		atBench.release();
		stateChanged();
	}
	
	public void AtExit(){
		atExit.release();
	}

	//Scheduler
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		for (int i=0;i<getMyCustomerList().size();i++){
			if (getMyCustomerList().get(i).state == Customerstate.Ordered && getState() == Cashierstate.Idle){
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
		if (getState() == Cashierstate.GoingToGetItems){
			CollectItemsFromBench();
			return true;
		}
		//No item is fulfilled
		for (int i=0;i<getMyCustomerList().size();i++){
			if (getMyCustomerList().get(i).state == Customerstate.EpicFailed && getState() == Cashierstate.Idle){
				TellCustomerEpicFail(getMyCustomerList().get(i));
				return true;
			}
		}
		//Some or All items are fulfilled
		for (int i=0;i<getMyCustomerList().size();i++){
			if ((getMyCustomerList().get(i).state == Customerstate.Collected || getMyCustomerList().get(i).state == Customerstate.Failed) && getState() == Cashierstate.Idle){
				CalculatePayment(getMyCustomerList().get(i));
				return true;
			}
		}
		
		for (int i=0;i<getMyCustomerList().size();i++){
			if (getMyCustomerList().get(i).state == Customerstate.Paid && getState() == Cashierstate.Idle){
				GiveItems(getMyCustomerList().get(i));
				return true;
			}
		}
		
		if (getMyCustomerList().isEmpty() && getState() == Cashierstate.OffWork){
			OffWork();
			return true;
		}

		return false;

	}
	
	//Actions
	private void GoGetItems(MyCustomer MC, ItemCollector IC){
		print ("Going to ask ItemCollector to get Items");
		cashierGui.GoToBench();
		try {
			atBench.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MC.itemCollector = IC;
		MC.state = Customerstate.OrderPlaced;
		MC.itemCollector.msgGetTheseItem(MC.OrderList, MC.c);
		
		cashierGui.GoToFrontDesk();
		try {
			atFrontDesk.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void CollectItemsFromBench(){
		cashierGui.GoToBench();
		try {
			atBench.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cashierGui.GoToFrontDesk();
		try {
			atFrontDesk.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void TellCustomerEpicFail(MyCustomer MC){
		print ("Going to tell customers that none of the item on the shoppinglist can be fulfilled");
		MC.state = Customerstate.Paid;
		MC.c.msgNoItem();
		for (int i=0;i<MyCustomerList.size();i++){
			if (MC == MyCustomerList.get(i)){
				MyCustomerList.remove(i);
			}
		}
		
	}
	
	private void CalculatePayment(MyCustomer MC){
		print ("Calculating the total for the customer");
		double total = 0;
		for (int i=0;i<MC.getDeliveryList().size();i++){
			double CurrentPrice = PriceList.get(MC.getDeliveryList().get(i).name);
			total += CurrentPrice*MC.getDeliveryList().get(i).amount;
		}
		MC.state = Customerstate.WaitingForCheck;
		MC.c.msgHereisYourTotal(total, MC.MissingItemList);
		
	}

	private void GiveItems (MyCustomer MC){
		print ("Going to Give/Deliver Item");
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
	
	private void OffWork(){
		DomsgAllWorkersToOffWork();
		cashierGui.OffWork();
		try {
			atExit.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.deactivate();
	}
	

	private void DomsgAllWorkersToOffWork() {
		for (int i=0; i<ICList.size();i++){
			ICList.get(i).msgLeaveWork();
		}
		for (int i=0; i<DGList.size();i++){
			DGList.get(i).msgLeaveWork();
		}
		
	}

	//Utilities
	public Map<String,Integer> getInventoryList(){
		return InventoryList;
	}
	public void setGui(CashierGuiInterfaces caGui){
		cashierGui = caGui;
	}
	public Gui getGui(){
		return cashierGui;
	}
	public void setDGList(List<DeliveryGuy> list){
		DGList = list;
	}
	public void addDGList(DeliveryGuy DG){
		getDGList().add(DG);
	}
	public void setICList(List<ItemCollector> list){
		ICList = list;
	}
	public void addICList(ItemCollector IC, Map<String,Integer> InventoryList){
		IC.setInventoryList(InventoryList);
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
		return MarketTotalMoney;
	}

	public void setCash(double cash) {
		this.MarketTotalMoney = cash;
	}
	
	//Shifts
	public int getShiftStartHour(){
		return startinghour;
	}
	public int getShiftStartMinute(){
		return startingminutes;
	}
	public int getShiftEndHour(){
		return endinghour;
	}
	public int getShiftEndMinute(){
		return endingminutes;
	}
	public boolean isAtWork(){
		if (this.isActive())
			return true;
		else
			return false;
	}
	public boolean isOnBreak(){
		return false;
	}

	public void setInventoryList(Map<String, Integer> iList) {
		InventoryList = iList;
		
	}

	public Map<String,Double> getPriceList(){
		return PriceList;
	}

	public Cashierstate getState() {
		return state;
	}

	public void setState(Cashierstate state) {
		this.state = state;
	}

}