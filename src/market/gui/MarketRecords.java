package market.gui;

import gui.BuildingRecords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import market.CashierRole;
import market.CustomerRole;
import market.DeliveryGuyRole;
import market.Item;
import market.ItemCollectorRole;
import market.interfaces.Cashier;
import market.interfaces.DeliveryGuy;
import market.interfaces.ItemCollector;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.Constants;
import agent.PersonAgent;
import agent.Role;
import agent.gui.AnimationPanel;
import agent.interfaces.Person;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class MarketRecords implements BuildingRecords {
	MarketInfoPanel marketControlPanel;
	MarketBuilding building;
	
    //private PersonAgent CashierPerson = new PersonAgent("Cashier");
    CashierRole ca;
	private CashierGui cashierGui;

	//List of ItemCollectors and DeliveryGuys
    private List<ItemCollector> ItemCollectors = new Vector<ItemCollector>();
    private List<DeliveryGuy> DeliveryGuys = new Vector<DeliveryGuy>();
    
    //private PersonAgent ItemCollectorPerson = new PersonAgent("ItemCollector1");
    //private PersonAgent ItemCollectorPerson1 = new PersonAgent("ItemCollector2");
    private ItemCollectorRole ic;
    private ItemCollectorGui icGui;
   
    //private PersonAgent DeliveryGuyPerson = new PersonAgent("DeliveryGuy1");
    private DeliveryGuyRole dg;
    private DeliveryGuyGui dgGui;

    private MarketBackgroundLayoutGui marketBackgroundLayout;
	private static Map<String,Double>PriceList = Constants.MarketPriceList;

	private Map<String, Integer>InventoryList = new HashMap<String, Integer>(){
		{
		put("Krabby Patty", Constants.KrabbyPattyInitialAmount);
		put("Kelp Shake", Constants.KelpShakeInitialAmount);
		put("Coral Bits", Constants.CoralBitsInitialAmount);
		put("Kelp Rings", Constants.KelpRingsInitialAmount);
		put("LamboFinny", Constants.LamboFinnyInitialAmount);
		put("Toyoda", Constants.ToyodaInitialAmount);
		}
	};
    
	
    //private PersonAgent CustomerPerson = new PersonAgent("Customer1");
    //private CustomerRole cust;
    //private CustomerGui custGui;
    
    //private PersonAgent CustomerPerson1 = new PersonAgent("Customer2");
    //private CustomerRole cust1;
    //private CustomerGui custGui1;
    
    
    

    private AnimationPanel gui; //reference to main gui

    public MarketRecords(AnimationPanel gui, MarketBuilding market) {
    	//marketControlPanel = controlPanel;
    	building = market;
    	marketBackgroundLayout = new MarketBackgroundLayoutGui();
    	
    	
    	
    	

        this.gui = gui;
        
        /*
        Role crTemp = CreateCashierRole(CashierPerson.getName(), CashierPerson.getWallet().getCashOnHand(), CashierPerson, building);
        //TODO Attention!!
        CashierPerson.startThread();
        crTemp.activate();
            
        
        Role icTemp = CreateItemCollectorRole(ItemCollectorPerson.getName(), ItemCollectorPerson, building, (CashierRole)crTemp);
        ItemCollectorPerson.startThread();
        icTemp.activate();
        
        Role icTemp1 = CreateItemCollectorRole(ItemCollectorPerson1.getName(), ItemCollectorPerson1, building, (CashierRole)crTemp);
        ItemCollectorPerson1.startThread();
        icTemp1.activate();
        
        Role dgTemp = CreateDeliveryGuyRole(DeliveryGuyPerson.getName(), DeliveryGuyPerson, building, (CashierRole)crTemp);
        DeliveryGuyPerson.startThread();
        dgTemp.activate();
        */
        
        
            /*
       cust = new CustomerRole("Customer1", 1000, tempInventoryList, CustomerPerson);     
       custGui = new CustomerGui(cust);
    		cust.setGui(custGui);
    		cust.setCashier(ca);
    		cust.setPriceList(ca.getPriceList());
    		CustomerPerson.startThread();
        	CustomerPerson.addRole(cust);
        	cust.setPriceList(ca.getPriceList());
        	cust.activate();
        	//custGui.setBuying();
        	gui.addGui(custGui);
        
       cust1 = new CustomerRole("Customer2", 1000, tempInventoryList, CustomerPerson1); 	
       custGui1 = new CustomerGui(cust1);
    		cust1.setGui(custGui1);
        	cust1.setCashier(ca);
        	cust1.setPriceList(ca.getPriceList());
        	CustomerPerson1.startThread();
        	CustomerPerson1.addRole(cust1);
        	cust1.setPriceList(ca.getPriceList());
        	cust1.activate();
        	//custGui1.setBuying();
        	gui.addGui(custGui1);
        	*/

        	gui.addGui(marketBackgroundLayout);
    }

	public LocationTypeEnum getType() {
		return LocationTypeEnum.Market;
	}

	@Override
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

public void SetCashierMarketInfoPanel(MarketInfoPanel p){
		cashierGui.setMarketInfoPanel(p);
	return;
}

 
    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    
public Role CreateCashierRole(String name, double money, Person person, MarketBuilding b){
    ca = new CashierRole(name, money, person, b, InventoryList);
    cashierGui = new CashierGui(ca);   
        ca.setGui(cashierGui);
        ca.setICList(ItemCollectors);
        ca.setDGList(DeliveryGuys);
        gui.addGui(cashierGui);
        //CashierPerson.startThread();
        person.addRole(ca); 
        //ca.activate();
        return ca;
    }

public Role CreateItemCollectorRole(String name, Person person, MarketBuilding b, Cashier cashier){
    ic = new ItemCollectorRole(name, person, building);
    icGui = new ItemCollectorGui(ic);
        ic.setGui(icGui);
        ItemCollectors.add(ic);
        ic.setCashier(cashier);
        ic.setInventoryList(cashier.getInventoryList());
        gui.addGui(icGui);
        //ItemCollectorPerson.startThread();
        person.addRole(ic);
        //ic.activate();
        icGui.setItemCollectorNumber((int)(Math.random()*2));
        return ic;
    }

public Role CreateDeliveryGuyRole(String name, Person person, MarketBuilding b, Cashier cashier){
    dg = new DeliveryGuyRole(name, person, b);
    dgGui = new DeliveryGuyGui(dg); 
        dg.setGui(dgGui);
        DeliveryGuys.add(dg);
        dg.setCashier(cashier);
        gui.addGui(dgGui);
        //DeliveryGuyPerson.startThread();
        person.addRole(dg);
        //dg.activate();
        return dg;
    }

public Map<String, Integer> getInventoryList() {
	
	return InventoryList;
}

public Map<String,Double> getPriceList() {
	return PriceList;
}

public void setPriceList(Map<String,Double> priceList) {
	PriceList = priceList;
}



}
