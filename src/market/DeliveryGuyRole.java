package market;


import java.util.List;
import java.util.concurrent.Semaphore;

import market.ItemCollectorRole.ItemCollectorstate;
import market.gui.DeliveryGuyGui;
import market.gui.MarketBuilding;
import market.interfaces.Cashier;
import market.interfaces.Customer;
import market.interfaces.DeliveryGuy;
import market.interfaces.DeliveryGuyGuiInterfaces;
import market.interfaces.DeliveryReceiver;
import market.test.mock.EventLog;
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
	public EventLog log = new EventLog();
	
	private DeliveryGuyGuiInterfaces deliveryguyGui;
	private String name;
	private boolean Available = true;
	private Cashier cashier;
	private CommonSimpleClasses.CityBuilding Market;
	private Order CurrentOrder = null;
	
	private Semaphore atDeliver = new Semaphore (0,true);
	private Semaphore atExit = new Semaphore (0,true);
	
	public enum DeliveryGuystate {NotAtWork, GoingToWork, Idle, OffWork, Delivering};
	private DeliveryGuystate state = DeliveryGuystate.NotAtWork;


		
		/**
		 * The one and only one constructor for the DeliveryGuyRole
		 * @param NA name of the person
		 * @param person person himself
		 * @param Market the Market that the deliveryGuy is working in
		 */
	public DeliveryGuyRole(Person person, MarketBuilding Market){
		super(person, Market);

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
			setState(DeliveryGuystate.OffWork);
			stateChanged();
		}

		/**
		 * The message from Cashier to go deliver item to the building (restaurant)
		 */
		public void msgDeliverIt(List<Item> DeliveryList , DeliveryReceiver deliveryReceiver , CommonSimpleClasses.CityLocation building) {
			setCurrentOrder(new Order(DeliveryList, deliveryReceiver, building));
			 Available = false;
			 stateChanged();
		}
		
		/**
		 * a msg from outside world that notifying the role has arrived a location (either Restaurant or Market)
		 */
		public void msgArrivedDestination(){
			if (person.getPassengerRole().getLocation().type() == LocationTypeEnum.Restaurant)
			{
				getCurrentOrder().getDeliveryReceiver().msgHereIsYourItems(getCurrentOrder().getDeliveryList());
				person.getPassengerRole().msgGoToLocation(super.person.getWorkRole().getLocation(), false);
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
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		if (getState() == DeliveryGuystate.NotAtWork){
			GoToWork();
			return true;
		}
		if (CurrentOrder != null){
			//if (CurrentOrder.Building.isOpen())
			if (Available == false){
				GoDeliver();
				return true;
			}
		}
		if (Available == true && getState() == DeliveryGuystate.OffWork){
			OffWork();
				return true;
		}
		return false;
	}

	//Action
	private void GoToWork(){
		setState(DeliveryGuystate.GoingToWork);
		deliveryguyGui.BackReadyStation();
	}
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
		person.getPassengerRole().msgGoToLocation(getCurrentOrder().getBuilding(), false);
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
		setState(DeliveryGuystate.NotAtWork);
	}
	
	
	//Utilities
	public void setGui (DeliveryGuyGuiInterfaces dgGui){
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

		public boolean isAtWork(){
			if (this.isActive())
				return true;
			else
				return false;
		}
		public boolean isOnBreak(){
			return false;
		}

	public Order getCurrentOrder() {
			return CurrentOrder;
		}

		public void setCurrentOrder(Order currentOrder) {
			CurrentOrder = currentOrder;
		}

	public Cashier getCashier() {
			return cashier;
		}

	public DeliveryGuystate getState() {
		return state;
	}

	public void setState(DeliveryGuystate state) {
		this.state = state;
	}

	public class Order{
		private List<Item> DeliveryList;
		private DeliveryReceiver deliveryReceiver;
		private CommonSimpleClasses.CityLocation Building;
		
		Order(List<Item> DList, DeliveryReceiver dR, CommonSimpleClasses.CityLocation CB){
			setDeliveryList(DList);
			setDeliveryReceiver(dR);
			setBuilding(CB);
		}

		public List<Item> getDeliveryList() {
			return DeliveryList;
		}

		public void setDeliveryList(List<Item> deliveryList) {
			DeliveryList = deliveryList;
		}

		public DeliveryReceiver getDeliveryReceiver() {
			return deliveryReceiver;
		}

		public void setDeliveryReceiver(DeliveryReceiver deliveryReceiver) {
			this.deliveryReceiver = deliveryReceiver;
		}

		public CommonSimpleClasses.CityLocation getBuilding() {
			return Building;
		}

		public void setBuilding(CommonSimpleClasses.CityLocation building) {
			Building = building;
		}
		
		public Person getPerson(){
			return person;
		}
	}

}