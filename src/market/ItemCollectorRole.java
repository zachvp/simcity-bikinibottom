package market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import market.gui.Gui;
import market.gui.ItemCollectorGui;
import market.interfaces.Cashier;
import market.interfaces.Customer;
import market.interfaces.ItemCollector;
import agent.Agent;
import agent.PersonAgent;
import agent.Role;

public class ItemCollectorRole extends Role implements ItemCollector{

	private ItemCollectorGui itemcollectorGui = null;
	private String name;
	private Cashier cashier;
	private Map<String,Item> InventoryList = null;
	private List<Order> Orders = new ArrayList<Order>();

	private Semaphore atStation = new Semaphore (0,true);
	private Semaphore atHome = new Semaphore (0,true);
	
	private class Order {
		public Customer c;
		public List<Item> ItemList = new ArrayList<Item>();
	}
	
	public ItemCollectorRole(String na, PersonAgent person){
		super(person);
		name = na;
	}
	
	
	//Messages	
	public void msgGetTheseItem(List<Item> ItemList, Customer c){
		print ("Received msg to get items");
		Order o = new Order();
		o.c = c;
		o.ItemList = ItemList;
		Orders.add(o);
		stateChanged();
	}

	public int msgHowManyOrdersYouHave(){
		return Orders.size();
	}
	
	//Animations
	public void AtCollectStation(){
		atStation.release();
	}
	
	public void Ready(){
		atHome.release();
	}
	
	//Scheduler
	protected boolean pickAndExecuteAnAction() {
		if(Orders.size()!=0){
			GoGetItems(Orders.get(0));
			return true;
		}
		return false;
	}
	
	//Actions
	private void GoGetItems(Order o){
		print("Going to get items");
		itemcollectorGui.CollectItems();
		try {
			atStation.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i=0;i<Orders.size();i++){
			if (o == Orders.get(i)){
				Orders.remove(i);
			}
		}
		List<Item> MissingList = new ArrayList<Item>();
		List<Item> DeliverList = new ArrayList<Item>();
		for(int i=0;i<o.ItemList.size();i++){
			Item CurrentItem = InventoryList.get(o.ItemList.get(i).name);	//Retrieve the item type from the InventoryList
			if (CurrentItem.amount >= o.ItemList.get(i).amount){	//enough inventories to satisfy
				CurrentItem.ItemConsumed(o.ItemList.get(i).amount);
				Item tempitem = new Item(o.ItemList.get(i).name, o.ItemList.get(i).amount);
				DeliverList.add(tempitem);
			}
			else		//not enough inventories to satisfy the order
			{			//Add into it anyway (Try to satisfy the order)
				Item tempitem = new Item(o.ItemList.get(i).name, CurrentItem.amount);
				CurrentItem.ItemConsumed(CurrentItem.amount);
				DeliverList.add(tempitem);
				Item Missingitem = new Item(o.ItemList.get(i).name, o.ItemList.get(i).amount - CurrentItem.amount);
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
		
		cashier.msgHereAreItems(DeliverList, MissingList, o.c);
		
		return;
	}

	//Utilities
	public void setInventoryList(Map<String,Item> IList){
		InventoryList = IList;
	}
	public void setGui (ItemCollectorGui icGui){
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
}