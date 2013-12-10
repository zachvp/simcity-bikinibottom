package market;


import gui.Building;
import gui.trace.AlertTag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import market.gui.MarketBuilding;
import market.interfaces.Cashier;
import market.interfaces.DeliveryGuy;
import market.interfaces.DeliveryGuyGuiInterfaces;
import market.interfaces.DeliveryReceiver;
import market.test.mock.EventLog;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
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
	private Cashier cashier;
	private CommonSimpleClasses.CityBuilding Market;
	private boolean present;
	private List<Order> orders = new ArrayList<Order>();
	
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
		//Do(AlertTag.MARKET, "Creating DeliveryGuyRole");

	}
	
	//Messages
	/**
	 * It will return whether the DeliveryGuy is on a mission or not
	 */
		public boolean msgAreYouAvailable() {
			//Do(AlertTag.MARKET, "Receive message asking if the DeliveryGuy is available");
			return (getOrders().isEmpty() || present);
		}

		
		/**
		 * The msg for calling the DeliveryGuy to leave work
		 */
		public void msgLeaveWork(){
			//Do(AlertTag.MARKET, ",message Leave Work");
			setState(DeliveryGuystate.OffWork);
			stateChanged();
		}

		/**
		 * The message from Cashier to go deliver item to the building (restaurant)
		 */
		public void msgDeliverIt(List<Item> DeliveryList , DeliveryReceiver deliveryReceiver , CommonSimpleClasses.CityLocation building) {
			//Do(AlertTag.MARKET, "Receive msg from Cashier to Deliver Item to Restaurant");
			getOrders().add(new Order(DeliveryList, deliveryReceiver, building));
			 stateChanged();
		}
		
		/**
		 * a msg from outside world that notifying the role has arrived a location (either Restaurant or Market)
		 */
		public void msgArrivedDestination(){
			//Do(AlertTag.MARKET, "Receive msg delivery Guy has arrived Destination");
			for (int i = 0 ; i<getOrders().size();i++){
				if (person.getPassengerRole().getLocation().equals(getOrders().get(i).getBuilding()))
				{
					if (getOrders().get(i).getBuilding() instanceof gui.Building){
						if (((Building) getOrders().get(i).getBuilding()).isOpen()){
							getOrders().get(i).getDeliveryReceiver().msgHereIsYourItems(getOrders().get(i).getDeliveryList());
							getOrders().remove(i);
						}
						else{
							getOrders().get(i).retry = true;
							Order currentOrder = getOrders().get(i);
							//put the currentOrder at the back of the orderlist
							getOrders().remove(i);
							getOrders().add(currentOrder);
						}
						person.getPassengerRole().msgGoToLocation(getLocation(), false);
						person.getPassengerRole().activate();
					}
				}
				if (person.getPassengerRole().getLocation().equals(getLocation()))
				{
					deliveryguyGui.BackReadyStation();
				}
			}
		}
		 
	//Animations
		/**
		 * Animation!
		 */
		public void Ready(){
			//Do(AlertTag.MARKET, "Ready!");
			present = true;
		}
		
		/**
		 * Animation!
		 */
		public void AtDeliverExit(){
			//Do(AlertTag.MARKET, "At Deliver Exit");
			atDeliver.release();
		}
		
		/**
		 * Animation!
		 */
		public void AtExit(){
			//Do(AlertTag.MARKET, "At Market Exit");
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
		
		if (present == true){
			for (int i=0;i<getOrders().size();i++){
				if(!getOrders().get(i).retry){
					GoDeliver(getOrders().get(i));
					return true;
				}
			}
			
			for (int i=0;i<getOrders().size();i++){
				if(getOrders().get(i).retry){
					GoDeliver(getOrders().get(i));
					return true;
				}
			}
		}
		
		
		if (present == true && getState() == DeliveryGuystate.OffWork){
			OffWork();
				return true;
		}
		return false;
	}

	//Action
	private void GoToWork(){
		//Do(AlertTag.MARKET, "Going To Work");
		setState(DeliveryGuystate.GoingToWork);
		deliveryguyGui.BackReadyStation();
	}
	/**
	 * Action to go deliver items!
	 */
	private void GoDeliver(Order o){
		//Do(AlertTag.MARKET, "Going To Deliver");
		// animation to go to the location (Building)
		present = false;
		deliveryguyGui.GoDeliver();
		try {
			atDeliver.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		person.getPassengerRole().msgGoToLocation(o.getBuilding(), false);
		person.getPassengerRole().activate();
		deactivate();
		//stateChanged()?
		
		
		// animation go back to the market
		
		//Available = true;
	}
	
	/**
	 * call off work
	 */
	private void OffWork(){
		//Do(AlertTag.MARKET, "Going to OffWork");
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
		
	public Cashier getCashier() {
			return cashier;
		}

	public DeliveryGuystate getState() {
		return state;
	}

	public void setState(DeliveryGuystate state) {
		this.state = state;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public class Order{
		private List<Item> DeliveryList;
		private DeliveryReceiver deliveryReceiver;
		private CommonSimpleClasses.CityLocation Building;
		private boolean retry;
		
		Order(List<Item> DList, DeliveryReceiver dR, CommonSimpleClasses.CityLocation CB){
			setDeliveryList(DList);
			setDeliveryReceiver(dR);
			setBuilding(CB);
			retry = false;
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