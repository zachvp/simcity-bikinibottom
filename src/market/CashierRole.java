package market;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import market.gui.MarketBuilding;
import market.interfaces.Cashier;
import market.interfaces.CashierGuiInterfaces;
import market.interfaces.Customer;
import market.interfaces.DeliveryGuy;
import market.interfaces.ItemCollector;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.ScheduleTask;
import agent.WorkRole;
import agent.gui.Gui;
import agent.interfaces.Person;

/**
 * The role of Cashier in Market, he acts as the Host in the market that receives message from Customers and ItemCollectors
 * @author AnThOnY
 *
 */
public class CashierRole extends WorkRole implements Cashier {

private static final int startingminute = 0;
	//public EventLog log = new EventLog();
	private CashierGuiInterfaces cashierGui = null;
	private double MarketTotalMoney;
	ScheduleTask task = new ScheduleTask();
	
	private Semaphore atFrontDesk = new Semaphore(0,true);
	private Semaphore atBench = new Semaphore (0,true);
	private Semaphore atExit = new Semaphore (0,true);

	private List<MyCustomer> MyCustomerList = Collections.synchronizedList(new ArrayList<MyCustomer>());
	private List<ItemCollector> ICList = Collections.synchronizedList(new ArrayList<ItemCollector>());
	private List<DeliveryGuy> DGList = Collections.synchronizedList(new ArrayList<DeliveryGuy>() );
	
	private Map<String,Integer> InventoryList ;
	
	//Working Hour
	int startinghour = 6;
	int startingminutes = 29;
	int endinghour = 18;
	int endingminutes = 0;
	
	/**
	 * PriceList in the market
	 */
	private Map<String,Double>PriceList;
	public enum Cashierstate {GoingToWork, Working, OffWork, NotAtWork};
	private Cashierstate state = Cashierstate.NotAtWork; 
	public enum Customerstate {Arrived, Ordered, Collected, Paid, OrderPlaced, WaitingForCheck, GivenItems, Failed, EpicFailed}
	
	/**
	 * This is the class for cashier to keep track of the customers in the market
	 * @author AnThOnY
	 *
	 */
	public class MyCustomer {
		Customer c;
		CommonSimpleClasses.CityLocation Building;
		List<Item> OrderList = new ArrayList<Item>();
		private List<Item> DeliveryList = new ArrayList<Item>();
		private List<Item> MissingItemList = new ArrayList<Item>();
		ItemCollector itemCollector;
		DeliveryGuy deliveryGuy;
		public Customerstate state = Customerstate.Arrived;
		public List<Item> getDeliveryList() {
			return DeliveryList;
		}
		public void setDeliveryList(List<Item> deliveryList) {
			DeliveryList = deliveryList;
		}
	}
	
	/**
	 * This is the only constructor of the CashierRole in the Market
	 * @param NA The name of the CashierRole
	 * @param money The amount of money that the market has it
	 * @param person The Person of the CashierRole
	 * @param cL The MarketBuilding that the CashierRole's in
	 * @param inList The InventoryList of the Market
	 */
    public CashierRole(Person person, MarketBuilding cL, Map<String,Integer> inList){
    	super(person, cL);
		setCash(Constants.MarketInitialMoney);
		PriceList = Constants.MarketPriceList;
		InventoryList = inList;
		
		Runnable command = new Runnable(){
			@Override
			public void run() {
				msgLeaveWork();
			
			}
		};
		
		
		
		
		int hour = 18;
		int minute = 0;
		
		task.scheduleDailyTask(command, hour, minute);
			
	}
	
	//Cashier Message 
    /**
     * This is the message r from the restaurant
     * @param ShoppingList This is the List of Items that the customer wants to buy
     * @param C the customer himself
     * @param CityBuilding the building that is going to deliver to
     */
	public void msgPhoneOrder(List<Item>ShoppingList, Customer C, CommonSimpleClasses.CityLocation building)	
	{				//The Customer will be the phone calling guy
		//print ("Received Phone Order");
		MyCustomer MC = new MyCustomer();
		MC.c = C;
		MC.state = Customerstate.Ordered;
		MC.Building = building;
		for (int i=0;i<ShoppingList.size();i++){
			MC.OrderList.add(ShoppingList.get(i));
		}
		getMyCustomerList().add(MC);
		stateChanged();
	}
	
	/**
	 * This is the message from normal customer (Customers that are in the building
	 * @param ShoppingList This is the list of items that the customer wants to buy
	 * @param C Customer himself
	 */
	public void msgIWantItem(List<Item> ShoppingList, Customer C) //[Customer to Cashier]
	{
		//print ("Received Msg from Customer");
		MyCustomer MC = new MyCustomer();
		MC.c = C;
		MC.state = Customerstate.Ordered;
		MC.Building = null;
		for (int i=0;i<ShoppingList.size();i++){
			MC.OrderList.add(ShoppingList.get(i));
		}
		getMyCustomerList().add(MC);
		stateChanged();
	}

	/**
	 * This is the message from the ItemCollectors that giving back the result from collecting items in the backyard of the market
	 * @param Items This is the Deliver List
	 * @param MissingItems A list that contains all the missing items
	 * @param c The customer
	 */
	public void msgHereAreItems(List<Item> Items, List<Item> MissingItems, Customer c)
	{
		//print ("Received Items from ItemCollector");
		

		synchronized(getMyCustomerList()){
			for (int i=0;i<getMyCustomerList().size();i++){
				if (getMyCustomerList().get(i).c == c)
				{
					//When there is no item in the shoppinglist can be satisified
					if (Items.size() == 0){
						//print ("Epic Failed");
						getMyCustomerList().get(i).state = Customerstate.EpicFailed;
						getMyCustomerList().get(i).MissingItemList = MissingItems;
						break;
					}
					//When there is some items that cannot be fulfilled
					else if (MissingItems.size() != 0){
						//print ("failed");
						getMyCustomerList().get(i).state = Customerstate.Failed;
						getMyCustomerList().get(i).MissingItemList = MissingItems;
						getMyCustomerList().get(i).setDeliveryList(Items);
						break;
					}
					//All items can be fulfilled
					else
						//print ("no problem");
						getMyCustomerList().get(i).state = Customerstate.Collected;
						getMyCustomerList().get(i).setDeliveryList(Items);
						break;
				}
			
				
			}
		}
		
		
		
		stateChanged();
	}			
	
	/**
	 * This is the message from the customer that receiving payment
	 * @param payment the total amount of the customer that can pay
	 * @param c customer himself
	 */
	public void msgHereIsPayment(double payment, Customer c)
	{
		//print ("Receive payment from Customer ");
		synchronized(getMyCustomerList()){
			for (int i=0;i<getMyCustomerList().size();i++){
				if (getMyCustomerList().get(i).c == c){
					getMyCustomerList().get(i).state = Customerstate.Paid;
					setCash(getCash() + payment);
				}
			}
		}
		cashierGui.Update();
		stateChanged();
	}
	//Animations
	/**
	 * Animation!
	 * getting the semaphore release
	 */
	public void msgLeaveWork(){
		setState(Cashierstate.OffWork);
		stateChanged();
	}

	/**
	 * Animation!
	 * getting the semaphore release
	 */
	public void AtFrontDesk(){
		//System.out.println("AtFrontDesk");
		atFrontDesk.release();
		setState(Cashierstate.Working);
		//stateChanged();
	}
	
	/**
	 * Animation!
	 * getting the semaphore release
	 */
	public void AtBench(){
		atBench.release();
		//stateChanged();
	}
	
	/**
	 * Animation!
	 * getting the semaphore release
	 */
	public void AtExit(){
		atExit.release();
	}

	//Scheduler
	/**
	 * This is the scheduler and
	 * the first loop is planning to ask itemcollectors to get items
	 * the second loop is to tell customers if none of the items in the customers shoppinglists are satisfied
	 * the third loop is to give the invoice to the customers
	 * the fourth loop is to give items to the customers
	 * the fifth loop is to call everyone in the market to offwork as the workperiod has passed
	 * the sixth loop is an animation that being call to walk to the bench and collect items
	 */
	public boolean pickAndExecuteAnAction() {

		if (state == Cashierstate.NotAtWork){
			GoToWork();
			return true;
		}
		
		if (getMyCustomerList().get(0) != null){
			
			if (getMyCustomerList().get(0).state == Customerstate.Ordered && getState() == Cashierstate.Working){
				if(getICList().get(0).getPerson()!=null){
					ItemCollector tempIC = getICList().get(0);
					for (int j=1;j<getICList().size();j++){
						if ((getICList().get(j).getPerson()!=null) && getICList().get(j).msgHowManyOrdersYouHave() <= tempIC.msgHowManyOrdersYouHave())
							tempIC = getICList().get(j);
						else
							continue;
					}
					GoGetItems(getMyCustomerList().get(0),tempIC);
					return true;
				}
			}
			
			if(getMyCustomerList().get(0).state == Customerstate.EpicFailed && getState() == Cashierstate.Working){
				TellCustomerEpicFail(getMyCustomerList().get(0));
				return true;
			}
			
			if ((getMyCustomerList().get(0).state == Customerstate.Failed || getMyCustomerList().get(0).state == Customerstate.Collected) && getState() == Cashierstate.Working){
				CalculatePayment(getMyCustomerList().get(0));
				return true;
			}
			
			if(getMyCustomerList().get(0).state == Customerstate.Paid && getState() == Cashierstate.Working){
				GiveItems(getMyCustomerList().get(0));
				return true;
			}
		}
		
		/*
		synchronized(getMyCustomerList()){
			for (int i=0;i<getMyCustomerList().size();i++){
				if (getMyCustomerList().get(i).state == Customerstate.Ordered && getState() == Cashierstate.Idle){
					synchronized(getICList()){
						if(getICList().get(0).getPerson()!=null){
							ItemCollector tempIC = getICList().get(0);
							for (int j=1;j<getICList().size();j++){
								if ((getICList().get(j).getPerson()!=null) && getICList().get(j).msgHowManyOrdersYouHave() <= tempIC.msgHowManyOrdersYouHave())
									tempIC = getICList().get(j);
								else
									continue;
							}
							GoGetItems(getMyCustomerList().get(i),tempIC);
							return true;
						}
					}
				}
			}
		}
		
		//No item is fulfilled
		synchronized(getMyCustomerList()){
			for (int i=0;i<getMyCustomerList().size();i++){
				if (getMyCustomerList().get(i).state == Customerstate.EpicFailed && getState() == Cashierstate.Idle){
					TellCustomerEpicFail(getMyCustomerList().get(i));
					return true;
				}
			}
		}
		//Some or All items are fulfilled
		synchronized(getMyCustomerList()){
			for (int i=0;i<getMyCustomerList().size();i++){
				if ((getMyCustomerList().get(i).state == Customerstate.Collected || getMyCustomerList().get(i).state == Customerstate.Failed) && getState() == Cashierstate.Idle){
					CalculatePayment(getMyCustomerList().get(i));
					return true;
				}
			}
		}
		
		synchronized(getMyCustomerList()){
			for (int i=0;i<getMyCustomerList().size();i++){
				if (getMyCustomerList().get(i).state == Customerstate.Paid && getState() == Cashierstate.Idle){
					GiveItems(getMyCustomerList().get(i));
					return true;
				}
			}
		}
		*/
		
		if (getMyCustomerList().isEmpty() && getState() == Cashierstate.OffWork){
			OffWork();
			return true;
		}
		


		return false;

	}
	
	//Actions
	private void GoToWork(){
		state = Cashierstate.GoingToWork;
		cashierGui.GoToWork();
		try {
			atFrontDesk.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		state = Cashierstate.Working;
	}
	
	/**
	 * This is the action to first move the cashier to the bench and ask the selected item collector to collect items for the order
	 * @param MC the class MyCustomer for the current Customer
	 * @param IC the selected ItemCollector to do this job
	 */
	private void GoGetItems(MyCustomer MC, ItemCollector IC){
		//print ("Going to ask ItemCollector to get Items");
		cashierGui.GoToBench();
		try {
			atBench.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MC.itemCollector = IC;
		MC.state = Customerstate.OrderPlaced;
		MC.itemCollector.msgGetTheseItem(MC.OrderList, MC.c);
		
		cashierGui.GoToFrontDesk();
		try {
			atFrontDesk.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	


	/**
	 * An action to tell the customer none of his order can be satisfied
	 * @param MC The customer that totally has no order can be satisfied
	 */
	private void TellCustomerEpicFail(MyCustomer MC){
		//print ("Going to tell customers that none of the item on the shoppinglist can be fulfilled");
		MC.state = Customerstate.Paid;
		MC.c.msgNoItem();
		synchronized(getMyCustomerList()){
			for (int i=0;i<MyCustomerList.size();i++){
				if (MC == MyCustomerList.get(i)){
					MyCustomerList.remove(i);
				}
			}
		}
		
	}
	
	/**
	 * This is the action to calculate the invoice (Measuring the total price of the deliver items) and send it to the customer
	 * @param MC The current Customer that is going to receive the invoice
	 */
	private void CalculatePayment(MyCustomer MC){
		//print ("Calculating the total for the customer");
		double total = 0;
		for (int i=0;i<MC.getDeliveryList().size();i++){
			double CurrentPrice = PriceList.get(MC.getDeliveryList().get(i).name);
			total += CurrentPrice*MC.getDeliveryList().get(i).amount;
		}
		MC.state = Customerstate.WaitingForCheck;
		MC.c.msgHereisYourTotal(total, MC.MissingItemList);
		
	}

	/**
	 * This is the action to give items to the customer (in the market) or find the appropriate deliveryguy to deliver item to the building
	 * @param MC The current Customer that is being handled
	 */
	private void GiveItems (MyCustomer MC){
		//print ("Going to Give/Deliver Item");
		MC.state = Customerstate.GivenItems;
		if (MC.Building == null){
			MC.c.msgHereisYourItem(MC.getDeliveryList());
			synchronized(getMyCustomerList()){
				for (int i=0;i<MyCustomerList.size();i++){
					if (MC == MyCustomerList.get(i)){
						MyCustomerList.remove(i);
						break;
					}
				}
			}
		}
		else
			synchronized(getDGList()){
				for (int i=0;i<getDGList().size();i++){
					if(getDGList().get(i).msgAreYouAvailable()){
						MC.deliveryGuy = getDGList().get(i);
						MC.deliveryGuy.msgDeliverIt(MC.getDeliveryList(), MC.c, MC.Building);
						synchronized(getMyCustomerList()){
							for (int j=0;j<MyCustomerList.size();j++){
								if (MC == MyCustomerList.get(j)){
									MyCustomerList.remove(j);
									break;
								}
							}
						}
						break;
					}
				}
			}
	}
	
	/**
	 * calling offwork (including himself)
	 */
	private void OffWork(){
		DomsgAllWorkersToOffWork();
		cashierGui.OffWork();
		try {
			atExit.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.deactivate();
		state = Cashierstate.NotAtWork;
	}
	

	/**
	 * Calling everyone offwork
	 */
	private void DomsgAllWorkersToOffWork() {
		synchronized(getICList()){
			for (int i=0; i<ICList.size();i++){
				ICList.get(i).msgLeaveWork();
			}
		}
		
		synchronized(getDGList()){
			for (int i=0; i<DGList.size();i++){
				DGList.get(i).msgLeaveWork();
			}
		}
		
	}

	//Utilities
	public Map<String,Integer> getInventoryList(){
		return InventoryList;
	}
	public void setGui(CashierGuiInterfaces caGui){
		cashierGui = caGui;
	}
	public Gui getGui(){
		return cashierGui;
	}
	public void setDGList(List<DeliveryGuy> list){
		DGList = list;
	}
	public void addDGList(DeliveryGuy DG){
		getDGList().add(DG);
	}
	public void setICList(List<ItemCollector> list){
		ICList = list;
	}
	public void addICList(ItemCollector IC, Map<String,Integer> InventoryList){
		IC.setInventoryList(InventoryList);
		getICList().add(IC);
	}
	public String getMaitreDName(){
		return super.getName();
	}
	public String getName(){
		return super.getName();
	}

	public List<MyCustomer> getMyCustomerList() {
		return MyCustomerList;
	}
	public void setMyCustomerList(List<MyCustomer> myCustomerList) {
		MyCustomerList = myCustomerList;
	}

	public List<ItemCollector> getICList() {
		return ICList;
	}

	public List<DeliveryGuy> getDGList() {
		return DGList;
	}

	public double getCash() {
		return MarketTotalMoney;
	}

	public void setCash(double cash) {
		this.MarketTotalMoney = cash;
	}
	
	public void setInventoryList(Map<String, Integer> iList) {
		InventoryList = iList;
		
	}

	public Map<String,Double> getPriceList(){
		return PriceList;
	}

	public Cashierstate getState() {
		return state;
	}

	public void setState(Cashierstate state) {
		this.state = state;
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



}