package market.gui;

import gui.AnimationPanel;

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
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.PersonAgent;
import agent.Role;
import agent.interfaces.Person;

/**
 * Panel in frame that contains all the Market information,
 * including cashier, itemcollectors, deliveryguys and marketcustomers.
 */
public class MarketRecords {
	MarketInfoPanel marketControlPanel;
	MarketBuilding building;
	
    //private PersonAgent CashierPerson = new PersonAgent("Cashier");
	CashierRole cashier;
	CashierGui cashierGui;

	//List of ItemCollectors and DeliveryGuys
    List<ItemCollectorRole> ItemCollectors = new Vector<ItemCollectorRole>();
    List<DeliveryGuyRole> DeliveryGuys = new Vector<DeliveryGuyRole>();
    
    //private PersonAgent ItemCollectorPerson = new PersonAgent("ItemCollector1");
    //private PersonAgent ItemCollectorPerson1 = new PersonAgent("ItemCollector2");
    //private ItemCollectorRole ic;
    //private ItemCollectorGui icGui;
   
    //private PersonAgent DeliveryGuyPerson = new PersonAgent("DeliveryGuy1");
    //private DeliveryGuyRole dg;
    //private DeliveryGuyGui dgGui;

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
        
       
        	*/

        	gui.addGui(marketBackgroundLayout);
        	initRoles();
    }
    
    public void initRoles(){
    	
    	//CashierRole
    	cashier = new CashierRole(null, building, InventoryList);
    	cashierGui = new CashierGui(cashier);
    	cashier.setGui(cashierGui);
    	gui.addGui(cashierGui);
    	
    	//ItemCollectorRoles
    	ItemCollectorRole itemCollector1 = new ItemCollectorRole(null, building);
    	//ItemCollectorRole itemCollector2 = new ItemCollectorRole(null, building);
    	
    	//ItemCollectorGuis
    	ItemCollectorGui itemCollector1Gui = new ItemCollectorGui(itemCollector1);
    	//ItemCollectorGui itemCollector2Gui = new ItemCollectorGui(itemCollector2);
    	itemCollector1Gui.setItemCollectorNumber(0);
    	//itemCollector2Gui.setItemCollectorNumber(1);
    	itemCollector1.setGui(itemCollector1Gui);
    	//itemCollector2.setGui(itemCollector2Gui);
    	gui.addGui(itemCollector1Gui);
    	//gui.addGui(itemCollector2Gui);
    	
    	//DeliveryGuyRoles
    	DeliveryGuyRole deliveryGuy1 = new DeliveryGuyRole(null, building);
    	DeliveryGuyRole deliveryGuy2 = new DeliveryGuyRole(null, building);
    	DeliveryGuyRole deliveryGuy3 = new DeliveryGuyRole(null, building);
    	
    	//DeliveryGuyGuis
    	DeliveryGuyGui deliveryGuy1Gui = new DeliveryGuyGui(deliveryGuy1);
    	DeliveryGuyGui deliveryGuy2Gui = new DeliveryGuyGui(deliveryGuy2);
    	DeliveryGuyGui deliveryGuy3Gui = new DeliveryGuyGui(deliveryGuy3);
    	deliveryGuy1.setGui(deliveryGuy1Gui);
    	deliveryGuy2.setGui(deliveryGuy2Gui);
    	deliveryGuy3.setGui(deliveryGuy3Gui);
    	gui.addGui(deliveryGuy1Gui);
    	gui.addGui(deliveryGuy2Gui);
    	gui.addGui(deliveryGuy3Gui);
    	
    	//Setting the inventoryList
    	itemCollector1.setInventoryList(cashier.getInventoryList());
    	//itemCollector2.setInventoryList(cashier.getInventoryList());
    	
    	//Setting Cashier in the ItemCollectors
    	itemCollector1.setCashier(cashier);
    	//itemCollector2.setCashier(cashier);
    	
    	//Setting Cashier in the DeliveryGuys
    	deliveryGuy1.setCashier(cashier);
    	deliveryGuy2.setCashier(cashier);
    	deliveryGuy3.setCashier(cashier);
    	
    	//Put itemCollectors into the cashier's ItemCollector's list
    	cashier.getICList().add(itemCollector1);
    	//cashier.getICList().add(itemCollector2);
    	
    	//Put DeliveryGuys into the cashier's DeliveryGuy's list
    	cashier.getDGList().add(deliveryGuy1);
    	cashier.getDGList().add(deliveryGuy2);
    	cashier.getDGList().add(deliveryGuy3);
    	
    	//Put ItemCollectors and DeliveryGuys roles into the respective lists
    	ItemCollectors.add(itemCollector1);
    	DeliveryGuys.add(deliveryGuy1);
    	DeliveryGuys.add(deliveryGuy2);
    	DeliveryGuys.add(deliveryGuy3);
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
    
/*
public Role CreateCashierRole(String name, double money, MarketBuilding b){
    ca = new CashierRole(name, money, null, b, InventoryList);
    cashierGui = new CashierGui(ca);   
        ca.setGui(cashierGui);
        ca.setICList(ItemCollectors);
        ca.setDGList(DeliveryGuys);
        gui.addGui(cashierGui);
        //CashierPerson.startThread();
        	//person.addRole(ca); 
        //ca.activate();
        return ca;
    }

public Role CreateItemCollectorRole(String name, MarketBuilding b, Cashier cashier){
    ic = new ItemCollectorRole(name, null, building);
    icGui = new ItemCollectorGui(ic);
        ic.setGui(icGui);
        ItemCollectors.add(ic);
        ic.setCashier(cashier);
        ic.setInventoryList(cashier.getInventoryList());
        gui.addGui(icGui);
        //ItemCollectorPerson.startThread();
        	//person.addRole(ic);
        //ic.activate();
        icGui.setItemCollectorNumber((int)(Math.random()*2));
        return ic;
    }

public Role CreateDeliveryGuyRole(String name, MarketBuilding b, Cashier cashier){
    dg = new DeliveryGuyRole(name, null, b);
    dgGui = new DeliveryGuyGui(dg); 
        dg.setGui(dgGui);
        DeliveryGuys.add(dg);
        dg.setCashier(cashier);
        gui.addGui(dgGui);
        //DeliveryGuyPerson.startThread();
        	//person.addRole(dg);
        //dg.activate();
        return dg;
    }

	*/
public Role CreateCustomerRole(String name, double money, List<Item> SL, Person person){
    CustomerRole role = new CustomerRole(name, money, SL, person); 	
    CustomerGui cGui = new CustomerGui(role);
 		role.setGui(cGui);
     	role.setCashier(cashier);
     	role.setPriceList(Constants.MarketPriceList);
     	//CustomerPerson1.startThread();
     	//CustomerPerson1.addRole(cust1);
     	//custGui1.setBuying();
     	gui.addGui(cGui);
     	person.addRole(role);
     	return role;
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
