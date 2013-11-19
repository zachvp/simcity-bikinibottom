package market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.interfaces.Cashier;
import market.interfaces.Customer;
import market.interfaces.ItemCollector;
import agent.Agent;

public class ItemCollectorAgent extends Agent implements ItemCollector{

	private String name;
	private Cashier cashier;
	private Map<String,Item> InventoryList = new HashMap<String,Item>();
	{		//Initially The market has 100 inventory on each Item
		InventoryList.put("CheapCar",		 new Item("CheapCar", 100));
		InventoryList.put("ExpensiveCar", 	 new Item("ExpensiveCar", 100));
		InventoryList.put("Pizza",			 new Item("Pizza", 100));
		InventoryList.put("Sandwich",		 new Item("Sandwich", 100));
		InventoryList.put("Chicken",		 new Item("Chicken", 100));
	}
	private List<Order> Orders = new ArrayList<Order>();
	
	private class Order {
		public Customer c;
		public List<Item> ItemList = new ArrayList<Item>();
	}
	
	ItemCollectorAgent(String na){
		name = na;
	}
	
	
	//Messages	
	public void msgGetTheseItem(List<Item> ItemList, Customer c){
		Order o = new Order();
		o.c = c;
		o.ItemList = ItemList;
		Orders.add(o);
		stateChanged();
	}

	public int msgHowManyOrdersYouHave(){
		return Orders.size();
	}
	//Scheduler
	protected boolean pickAndExecuteAnAction() {
		if(Orders.get(0)!=null){
			GoGetItems(Orders.get(0));
			return true;
		}
		return false;
	}
	
	//Actions
	private void GoGetItems(Order o){
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
		cashier.msgHereAreItems(DeliverList, MissingList, o.c);
		return;
	}

	//Utilities
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