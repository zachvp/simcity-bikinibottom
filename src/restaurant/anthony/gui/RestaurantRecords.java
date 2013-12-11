package restaurant.anthony.gui;

import gui.AnimationPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import restaurant.anthony.CashierRole;
import restaurant.anthony.CookRole;
import restaurant.anthony.CustomerRole;
import restaurant.anthony.Food;
import restaurant.anthony.HostRole;
import restaurant.anthony.WaiterRole;
import restaurant.anthony.WaiterRoleBase;
import restaurant.anthony.interfaces.Cook;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.Role;
import agent.interfaces.Person;

/**
 * Panel in frame that contains all the Market information,
 * including cashier, itemcollectors, deliveryguys and marketcustomers.
 */
public class RestaurantRecords {
	//RestaurantPanel restControlPanel;
	RestaurantBuilding building;
	
	HostRole host;
	private CashierRole cashier;
	
	private RestaurantBackgroundLayoutGui restBackgroundLayout;
	
	private Map<String,Double> priceList = new HashMap<String,Double>(){
		{
			put("Kelp Rings", (double) 10);
			put("Kelp Shake", (double) 20);
			put("Krabby Patty", (double) 30);
			put("Coral Bits", (double) 50);


		}
	};

	private Map<String, Food>InventoryList = new HashMap<String, Food>(){
		{
			Food KrabbyPatty = new Food("Krabby Patty", 400, 100);
			Food KelpShake = new Food("Kelp Shake", 300, 100);
			Food CoralBits = new Food("Coral Bits", 500, 100);
			Food KelpRings = new Food("Kelp Rings", 200, 100);
		put("Krabby Patty", KrabbyPatty);
		put("Kelp Shake", KelpShake);
		put("Coral Bits", CoralBits);
		put("Kelp Rings", KelpRings);
		}
	};
	
    
	
    //private PersonAgent CustomerPerson = new PersonAgent("Customer1");
    //private CustomerRole cust;
    //private CustomerGui custGui;
    
    //private PersonAgent CustomerPerson1 = new PersonAgent("Customer2");
    //private CustomerRole cust1;
    //private CustomerGui custGui1;
    
    
    

    private AnimationPanel gui; //reference to main gui

    public RestaurantRecords(AnimationPanel gui, RestaurantBuilding rest) {
    	//restControlPanel = controlPanel;
    	building = rest;
    	restBackgroundLayout = new RestaurantBackgroundLayoutGui();

        this.gui = gui;
        
        

        	gui.addGui(restBackgroundLayout);
        	initRoles();
    }
    
    public void initRoles(){
    	
    	
    	
    	//HostRole
    	host = new HostRole(null, building);
    	HostGui hostGui = new HostGui(host);
    	host.setGui(hostGui);
    	gui.addGui(hostGui);
    	
    	//WaiterRole
    	WaiterRoleBase waiter1 = new WaiterRole(null, building);
    	WaiterRoleBase waiter2 = new WaiterRole(null, building);
    	WaiterRoleBase waiter3 = new WaiterRole(null, building);
    	
    	WaiterGui waiterGui1 = new WaiterGui(waiter1, 0);
    	WaiterGui waiterGui2 = new WaiterGui(waiter2, 1);
    	WaiterGui waiterGui3 = new WaiterGui(waiter3, 2);
    	
    	waiter1.setGui(waiterGui1);
    	waiter2.setGui(waiterGui2);
    	waiter3.setGui(waiterGui3);
    	gui.addGui(waiterGui1);
    	gui.addGui(waiterGui2);
    	gui.addGui(waiterGui3);
    	
    	//CookRole
    	Cook cook = new CookRole(null, building);
    	CookGui cookGui = new CookGui(cook);
    	cook.setInventoryList(InventoryList);
    	cook.setGui(cookGui);
    	gui.addGui(cookGui);
    	
    	//CashierRole
    	cashier = new CashierRole(null, building);
    	CashierGui cashierGui = new CashierGui(cashier);
    	cashier.setPriceList(priceList);
    	cashier.setGui(cashierGui);
    	gui.addGui(cashierGui);
    	
    	//Add the waiter into host
    	host.AddWaiter(waiter1);
    	host.AddWaiter(waiter2);
    	host.AddWaiter(waiter3);
    	
    	waiter1.setHost(host);
    	waiter2.setHost(host);
    	waiter3.setHost(host);
    	
    	waiter1.setCook(cook);
    	waiter2.setCook(cook);
    	waiter3.setCook(cook);
    	
    	waiter1.setCashier(cashier);
    	waiter2.setCashier(cashier);
    	waiter3.setCashier(cashier);
    	
    	cook.setHost(host);
    	cashier.setHost(host);
    	
    	host.setCashier(cashier);
    	host.setCook(cook);
    	
    	CustomerRole cust = new CustomerRole(null, building);
    	CustomerGui custGui = new CustomerGui(cust);
    	cust.setGui(custGui);
    	gui.addGui(custGui);
    	cust.setHost(host);
    	
    	//Waiter is being added in host.AddWaiter(waiter)
    }

	public LocationTypeEnum getType() {
		return LocationTypeEnum.Market;
	}

	public Role addPerson(/*Person person,*/ String role, String name) {
		/*
		if (role == "Cashier"){
			
		}
		if (role == "ItemCollector"){
			ItemCollectorRole ic = new ItemCollectorRole("ItemCollector1", ItemCollectorPerson, building);
		    ItemCollectorGui icGui = new ItemCollectorGui(ic);
		    ic.setGui(icGui);
		    ItemCollectors.add(ic);
		    ic.setCashier(ca);
		    ic.setInventoryList(ca.getInventoryList());
		    gui.addGui(icGui);
		    ItemCollectorPerson.startThread();
	        ItemCollectorPerson.addRole(ic);
	        ic.activate();
		}
		if (role == "Customer"){
			
		}
		if (role == "DeliveryGuy"){
			
		}
		//setMarketControlPanel(marketControlPanel)
		
		
		*/
		return null;
	}

  
public Role CreateCustomerRole( Person person){
    CustomerRole role = new CustomerRole(person, building); 	
    CustomerGui cGui = new CustomerGui(role);
 		role.setGui(cGui);
     	role.setCashier(cashier);
     	role.setHost(host);
     	//CustomerPerson1.startThread();
     	//CustomerPerson1.addRole(cust1);
     	//custGui1.setBuying();
     	gui.addGui(cGui);
     	person.addRole(role);
     	role.activate();
     	return role;
     	}

public Map<String, Food> getInventoryList() {
	
	return InventoryList;
}

public Map<String,Double> getPriceList() {
	return priceList;
}

public void setPriceList(Map<String,Double> pList) {
	priceList = pList;
}

public void LowFood() {
	for(Map.Entry<String, Food> f: InventoryList.entrySet()){
		
	}
	
	InventoryList.clear();
	Food KrabbyPatty = new Food("Krabby Patty", 400, 1);
	Food KelpShake = new Food("Kelp Shake", 300, 1);
	Food CoralBits = new Food("Coral Bits", 500, 1);
	Food KelpRings = new Food("Kelp Rings", 200, 1);
	InventoryList.put("Krabby Patty", KrabbyPatty);
	InventoryList.put("Kelp Shake", KelpShake);
	InventoryList.put("Coral Bits", CoralBits);
	InventoryList.put("Kelp Rings", KelpRings);
	
	Map<String, Integer> UpdateList = new HashMap<String, Integer>();
	UpdateList.put("Krabby Patty", 1);
	UpdateList.put("Kelp Shake", 1);
	UpdateList.put("Coral Bits", 1);
	UpdateList.put("Kelp Rings", 1);
	building.infoPanel.UpdateInfoPanel(UpdateList);
}



}
