package market;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import market.gui.ItemCollectorGui;
import market.gui.MarketBuilding;
import market.interfaces.Cashier;
import market.interfaces.Customer;
import market.interfaces.ItemCollector;
import market.interfaces.ItemCollectorGuiInterfaces;
import agent.Agent;
import agent.Constants;
import agent.PersonAgent;
import agent.Role;
import agent.TimeManager;
import agent.WorkRole;
import agent.gui.Gui;
import agent.interfaces.Person;

public class ItemCollectorRole extends WorkRole implements ItemCollector{

	private ItemCollectorGuiInterfaces itemcollectorGui = null;
	private String name;
	private Cashier cashier;
	private Map<String, Integer> InventoryList = null;
	private List<Order> Orders = Collections.synchronizedList(new ArrayList<Order>());

	private Semaphore atStation = new Semaphore (0,true);
	private Semaphore atHome = new Semaphore (0,true);
	private Semaphore atExit = new Semaphore (0,true);
	
	public enum ItemCollectorstate {GoingToWork, Idle, OffWork, GettingItem};
	ItemCollectorstate state = ItemCollectorstate.GoingToWork;
	
	private class Order {
		public Customer c;
		public List<Item> ItemList = new ArrayList<Item>();
	}
	
	public ItemCollectorRole(String na, Person person, MarketBuilding cL){
		super(person, cL);
		name = na;
		
		
		
	}
	
	//Working Hour
		int startinghour = 8;
		int startingminutes = 29;
		int endinghour = 18;
		int endingminutes = 0;
	
	
	//Messages	
	public void msgGetTheseItem(List<Item> ItemList, Customer c){
		//print ("Received msg to get items");
		Order o = new Order();
		o.c = c;
		o.ItemList = ItemList;
		synchronized(getOrders()){
			getOrders().add(o);
		}
		stateChanged();
	}

	public int msgHowManyOrdersYouHave(){
		return getOrders().size();
	}
	
	@Override
	public void msgLeaveWork(){
		state = ItemCollectorstate.OffWork;
		stateChanged();
	}
	
	//Animations
	public void AtCollectStation(){
		atStation.release();
	}
	
	public void Ready(){
		state = ItemCollectorstate.Idle;
		atHome.release();
	}
	
	public void AtExit(){
		atExit.release();
	}
	
	
	//Scheduler
	public boolean pickAndExecuteAnAction() {
		//if (state == ItemCollectorstate.Idle)
		if(getOrders().size()!=0){
			GoGetItems(getOrders().get(0));
			return true;
		}
		
		if (state == ItemCollectorstate.OffWork && getOrders().size()==0){
			OffWork();
			return true;
		}
		return false;
	}
	
	//Actions
	private void GoGetItems(Order o){
		//print("Going to get items");
		itemcollectorGui.CollectItems();
		try {
			atStation.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		synchronized(getOrders()){
			for (int i=0;i<getOrders().size();i++){
				if (o == getOrders().get(i)){
					getOrders().remove(i);
				}
			}
		}
		List<Item> MissingList = new ArrayList<Item>();
		List<Item> DeliverList = new ArrayList<Item>();
		for(int i=0;i<o.ItemList.size();i++){
			int CurrentItem = getInventoryList().get(o.ItemList.get(i).name);	//Retrieve the item type from the InventoryList
			if (CurrentItem >= o.ItemList.get(i).amount){	//enough inventories to satisfy
				CurrentItem -= o.ItemList.get(i).amount;
				getInventoryList().put(o.ItemList.get(i).name, CurrentItem);
				Item tempitem = new Item(o.ItemList.get(i).name, o.ItemList.get(i).amount);
				DeliverList.add(tempitem);
			}
			else		//not enough inventories to satisfy the order
			{			//Add into it anyway (Try to satisfy the order)
				Item tempitem = new Item(o.ItemList.get(i).name, CurrentItem);
				CurrentItem = 0;
				getInventoryList().put(o.ItemList.get(i).name, CurrentItem);
				DeliverList.add(tempitem);
				Item Missingitem = new Item(o.ItemList.get(i).name, o.ItemList.get(i).amount - CurrentItem);
				MissingList.add(Missingitem);
			}
		}
		itemcollectorGui.BackReadyStation();
		try {
			atHome.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getCashier().msgHereAreItems(DeliverList, MissingList, o.c);
		
		return;
	}
	
	private void OffWork(){
		itemcollectorGui.OffWork();
		try {
			atExit.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.deactivate();
	}
	

	//Utilities
	public void setInventoryList(Map<String,Integer> IList){
		InventoryList = IList;
	}
	public void setGui (ItemCollectorGuiInterfaces icGui){
		itemcollectorGui = icGui;
	}
	public Gui getGui (){
		return itemcollectorGui;
	}
	public String getMaitreDName(){
		return name;
	}

	public String getName(){
		return name;
	}
	
	public void setCashier(Cashier ca){
		cashier = ca;
	}
	
	public void setState(ItemCollectorstate s) {
		state = s;
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

	public Map<String, Integer> getInventoryList() {
		return InventoryList;
	}

	public List<Order> getOrders() {
		return Orders;
	}

	public void setOrders(List<Order> orders) {
		Orders = orders;
	}

	public Cashier getCashier() {
		return cashier;
	}

	
}