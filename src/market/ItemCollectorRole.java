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

import market.CashierRole.Cashierstate;
import market.gui.ItemCollectorGui;
import market.gui.MarketBuilding;
import market.interfaces.Cashier;
import market.interfaces.Customer;
import market.interfaces.ItemCollector;
import market.interfaces.ItemCollectorGuiInterfaces;
import agent.PersonAgent;
import agent.WorkRole;
import agent.gui.Gui;
import agent.interfaces.Person;

/**
 * The Role that is responsible to collect items in the market
 * @author AnThOnY
 *
 */
public class ItemCollectorRole extends WorkRole implements ItemCollector{

	private ItemCollectorGuiInterfaces itemcollectorGui = null;
	private String name;
	private Cashier cashier;
	private Map<String, Integer> InventoryList = null;
	private List<Order> Orders = Collections.synchronizedList(new ArrayList<Order>());

	private Semaphore atStation = new Semaphore (0,true);
	private Semaphore atHome = new Semaphore (0,true);
	private Semaphore atExit = new Semaphore (0,true);
	
	public enum ItemCollectorstate {GoingToWork, Idle, OffWork, GettingItem, NotAtWork};
	ItemCollectorstate state = ItemCollectorstate.NotAtWork;
	
	/**
	 * this is a private class for ItemCollector to keep track his work
	 * @author AnThOnY
	 *
	 */
	private class Order {
		public Customer c;
		public List<Item> ItemList = new ArrayList<Item>();
	}
	
	/**
	 * This is the only constructor of ItemCollectorRole 
	 * @param na name of the person
	 * @param person person himself
	 * @param cL The building that the ItemCollector is working in
	 */
	public ItemCollectorRole(Person person, MarketBuilding cL){
		super(person, cL);
	}
	
	//Working Hour
		int startinghour = 6;
		int startingminutes = 29;
		int endinghour = 18;
		int endingminutes = 0;
	
	
	//Messages	
		/**
		 * A message from Cashier that to collect items
		 * @param ItemList the list of Items that are requested
		 * @param c the customer
		 */
	public void msgGetTheseItem(List<Item> ItemList, Customer c){
		System.out.println("Receive msg from Cashier to get items");
		//print ("Received msg to get items");
		Order o = new Order();
		o.c = c;
		o.ItemList = ItemList;
		synchronized(getOrders()){
			getOrders().add(o);
		}
		stateChanged();
	}

	/**
	 * The current size of the orderlist
	 */
	public int msgHowManyOrdersYouHave(){
		return getOrders().size();
	}
	
	/**
	 * the message to call off work
	 */
	public void msgLeaveWork(){
		state = ItemCollectorstate.OffWork;
		stateChanged();
	}
	
	//Animations
	/**
	 * Animation!
	 */
	public void AtCollectStation(){
		atStation.release();
	}
	
	/**
	 * Animation!
	 */
	public void Ready(){
		state = ItemCollectorstate.Idle;
		atHome.release();
	}
	
	/**
	 * Animation!
	 */
	public void AtExit(){
		atExit.release();
	}
	
	
	//Scheduler
	/**
	 * The ItemCollectorRole's PaEaA that is either go and get items or call off work
	 */
	public boolean pickAndExecuteAnAction() {
		//if (state == ItemCollectorstate.Idle)
		if (state == ItemCollectorstate.NotAtWork){
			GoToWork();
		}
		
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
	private void GoToWork(){
		state = ItemCollectorstate.GoingToWork;
		itemcollectorGui.GoToWork();
		try {
			atHome.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		state = ItemCollectorstate.Idle;
	}
	/**
	 * This the action to move the itemcollector to the backyard collect items by check through inventoryMap
	 * @param o CurrentOrder
	 */
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
			int CurrentItem = getInventoryMap().get(o.ItemList.get(i).name);	//Retrieve the item type from the InventoryList
			if (CurrentItem >= o.ItemList.get(i).amount){	//enough inventories to satisfy
				CurrentItem -= o.ItemList.get(i).amount;
				getInventoryMap().put(o.ItemList.get(i).name, CurrentItem);
				Item tempitem = new Item(o.ItemList.get(i).name, o.ItemList.get(i).amount);
				DeliverList.add(tempitem);
			}
			else		//not enough inventories to satisfy the order
			{			//Add into it anyway (Try to satisfy the order)
				Item tempitem = new Item(o.ItemList.get(i).name, CurrentItem);
				
				
				DeliverList.add(tempitem);
				Item Missingitem = new Item(o.ItemList.get(i).name, o.ItemList.get(i).amount - CurrentItem);
				CurrentItem = 0;
				getInventoryMap().put(o.ItemList.get(i).name, CurrentItem);
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
	
	/**
	 * the action to offwork
	 */
	private void OffWork(){
		itemcollectorGui.OffWork();
		try {
			atExit.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.deactivate();
		state = ItemCollectorstate.GoingToWork;
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
	
	public PersonAgent getPerson(){
		return (PersonAgent)super.getPerson();
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

	public Map<String, Integer> getInventoryMap() {
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