package market;


import java.util.List;
import java.util.concurrent.Semaphore;

import market.gui.DeliveryGuyGui;
import market.gui.MarketBuilding;
import market.interfaces.Cashier;
import market.interfaces.Customer;
import market.interfaces.DeliveryGuy;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.TimeManager;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.Agent;
import agent.PersonAgent;
import agent.Role;
import agent.WorkRole;
import agent.gui.Gui;
import agent.interfaces.Person;

/**
 * The role in the market to deliver items
 * @author AnThOnY
 *
 */
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
	
	/**
	 * The one and only one constructor for the DeliveryGuyRole
	 * @param NA name of the person
	 * @param person person himself
	 * @param Market the Market that the deliveryGuy is working in
	 */
	public DeliveryGuyRole(String NA, Person person, MarketBuilding Market){
		super(person, Market);
		name = NA;
		workingBuilding = Market;

	}
	
	//Messages
	/**
	 * It will return whether the DeliveryGuy is on a mission or not
	 */
		public boolean msgAreYouAvailable() {
			return Available;
		}

		
		/**
		 * The msg for calling the DeliveryGuy to leave work
		 */
		public void msgLeaveWork(){
			state = DeliveryGuystate.OffWork;
			stateChanged();
		}

		/**
		 * The message from Cashier to go deliver item to the building (restaurant)
		 */
		public void msgDeliverIt(List<Item> DeliveryList , Customer OrderPerson , CommonSimpleClasses.CityLocation building) {
			CurrentOrder = new Order(DeliveryList, OrderPerson, building);
			 Available = false;
			 stateChanged();
		}
		
		/**
		 * a msg from outside world that notifying the role has arrived a location (either Restaurant or Market)
		 */
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
		/**
		 * Animation!
		 */
		public void Ready(){
			Available = true;
		}
		
		/**
		 * Animation!
		 */
		public void AtDeliverExit(){
			atDeliver.release();
		}
		
		/**
		 * Animation!
		 */
		public void AtExit(){
			atExit.release();
		}
		
	
	//Scheduler
		/**
		 * DeliveryGuy's PaEaA
		 * he is either available or not
		 * AND offWork thats it
		 */
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
	/**
	 * Action to go deliver items!
	 */
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
	
	/**
	 * call off work
	 */
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
		CommonSimpleClasses.CityLocation Building;
		
		Order(List<Item> DList, Customer OP, CommonSimpleClasses.CityLocation CB){
			DeliveryList = DList;
			OrderPerson = OP;
			Building = CB;
		}
	}

}