package market;


import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import market.ItemCollectorRole.ItemCollectorstate;
import market.gui.DeliveryGuyGui;
import market.gui.MarketBuilding;
import market.interfaces.Cashier;
import market.interfaces.Customer;
import market.interfaces.DeliveryGuy;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.Agent;
import agent.Constants;
import agent.PersonAgent;
import agent.Role;
import agent.TimeManager;
import agent.WorkRole;
import agent.gui.Gui;
import agent.interfaces.Person;

public class DeliveryGuyRole extends WorkRole implements DeliveryGuy{
	private MarketBuilding workingBuilding = null;
	private DeliveryGuyGui deliveryguyGui = null;
	private String name;
	private boolean Available = true;
	private Cashier cashier;
	private CommonSimpleClasses.CityBuilding Market;
	private Order CurrentOrder;
	
	private Semaphore atDeliver = new Semaphore (0,true);
	private Semaphore atExit = new Semaphore (0,true);
	
	public enum DeliveryGuystate {GoingToWork, Idle, OffWork, Delivering};
	DeliveryGuystate state = DeliveryGuystate.GoingToWork;

	//Working Hour
		int startinghour = 8;
		int startingminutes = 29;
		int endinghour = 18;
		int endingminutes = 0;
		
	public DeliveryGuyRole(String NA, Person person, MarketBuilding Market){
		super(person, Market);
		name = NA;
		workingBuilding = Market;

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

		
		public void msgDeliverIt(List<Item> DeliveryList , Customer OrderPerson , CommonSimpleClasses.CityBuilding building) {
			CurrentOrder = new Order(DeliveryList, OrderPerson, building);
			 Available = false;
			 stateChanged();
		}
		
		public void msgArrivedDestination(){
			if (person.getPassengerRole().getLocation().type() == LocationTypeEnum.Restaurant)
			{
				CurrentOrder.OrderPerson.msgHereisYourItem(CurrentOrder.DeliveryList);
				person.getPassengerRole().msgGoToLocation(workingBuilding, false);
				person.getPassengerRole().activate();
			}
			if (person.getPassengerRole().getLocation().type() == LocationTypeEnum.Market)
			{
				deliveryguyGui.BackReadyStation();
			}
		}
		
	//Animations
		public void Ready(){
			Available = true;
		}
		
		public void AtDeliverExit(){
			atDeliver.release();
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
			atDeliver.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		person.getPassengerRole().activate();
		person.getPassengerRole().msgGoToLocation(CurrentOrder.Building, false);
		//stateChanged()?
		
		
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

	private class Order{
		List<Item> DeliveryList;
		Customer OrderPerson;
		CommonSimpleClasses.CityBuilding Building;
		
		Order(List<Item> DList, Customer OP, CommonSimpleClasses.CityBuilding CB){
			DeliveryList = DList;
			OrderPerson = OP;
			Building = CB;
		}
	}

}