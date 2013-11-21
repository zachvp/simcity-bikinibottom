package market;

import java.util.List;
import java.util.concurrent.Semaphore;

import market.gui.DeliveryGuyGui;
import market.gui.Gui;
import market.interfaces.Cashier;
import market.interfaces.CityBuilding;
import market.interfaces.Customer;
import market.interfaces.DeliveryGuy;
import agent.Agent;
import agent.PersonAgent;
import agent.Role;

public class DeliveryGuyRole extends Role implements DeliveryGuy{
	private DeliveryGuyGui deliveryguyGui = null;
	private String name;
	private boolean Available = true;
	private Cashier cashier;
	private CityBuilding Market;
	private Order CurrentOrder;
	
	private Semaphore atExit = new Semaphore (0,true);
	
	public DeliveryGuyRole(String NA, CityBuilding MA, PersonAgent person){
		super(person);
		name = NA;
		Market = MA;
	}
	
	//Messages
		public boolean msgAreYouAvailable() {
			return Available;
		}

		
		public void msgDeliverIt(List<Item> DeliveryList , Customer OrderPerson , CityBuilding building) {
			CurrentOrder = new Order(DeliveryList, OrderPerson, building);
			 Available = false;
			 stateChanged();
		}
		
	//Animations
		public void Ready(){
			Available = true;
		}
		
		public void AtExit(){
			atExit.release();
		}
	
	//Scheduler
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		if (Available == false){
			GoDeliver();
				return true;
		}
		return false;
	}

	//Action
	private void GoDeliver(){
		
		// animation to go to the location (Building)
		deliveryguyGui.GoDeliver();
		try {
			atExit.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CurrentOrder.OrderPerson.msgHereisYourItem(CurrentOrder.DeliveryList);
		
		// animation go back to the market
		
		//Available = true;
	}
	
	//Utilities
	public void setGui (DeliveryGuyGui dgGui){
		deliveryguyGui = dgGui;
	}
	public Gui getGui (){
		return deliveryguyGui;
	}
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setCashier(Cashier ca){
		cashier = ca;
	}

	private class Order{
		List<Item> DeliveryList;
		Customer OrderPerson;
		CityBuilding Building;
		
		Order(List<Item> DList, Customer OP, CityBuilding CB){
			DeliveryList = DList;
			OrderPerson = OP;
			Building = CB;
		}
	}

}