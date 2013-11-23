package market;


import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import market.ItemCollectorRole.ItemCollectorstate;
import market.gui.DeliveryGuyGui;
import market.gui.Gui;
import market.interfaces.Cashier;
import market.interfaces.CityBuilding;
import market.interfaces.Customer;
import market.interfaces.DeliveryGuy;
import agent.Agent;
import agent.Constants;
import agent.PersonAgent;
import agent.Role;
import agent.TimeManager;
import agent.WorkRole;

public class DeliveryGuyRole extends WorkRole implements DeliveryGuy{
	private DeliveryGuyGui deliveryguyGui = null;
	private String name;
	private boolean Available = true;
	private Cashier cashier;
	private CityBuilding Market;
	private Order CurrentOrder;
	
	private Semaphore atExit = new Semaphore (0,true);
	
	public enum DeliveryGuystate {GoingToWork, Idle, OffWork, Delivering};
	DeliveryGuystate state = DeliveryGuystate.GoingToWork;

	public DeliveryGuyRole(String NA, CityBuilding MA, PersonAgent person){
		super(person);
		name = NA;
		Market = MA;

		Runnable command = new Runnable(){
			@Override
			public void run() {
				msgOffWork();
			
			}
		};
		
		int hour = 18;
		int minute = 0;
		
		scheduleDailyTask(command, hour, minute);
	}
	
	//Messages
		public boolean msgAreYouAvailable() {
			return Available;
		}

		
		@Override
		public void msgLeaveWork(){
			state = DeliveryGuystate.OffWork;
			stateChanged();
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
		if (Available == true && state == DeliveryGuystate.OffWork){
			OffWork();
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
	
	private void OffWork(){
		deliveryguyGui.OffWork();
		try {
			atExit.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.deactivate();
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
	//Shifts
		public int getShiftStartHour(){
			return 8;
		}
		public int getShiftStartMinute(){
			return 29;
		}
		public int getShiftEndHour(){
			return 18;
		}
		public int getShiftEndMinute(){
			return 0;
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