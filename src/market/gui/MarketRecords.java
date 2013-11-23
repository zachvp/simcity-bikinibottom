package market.gui;

import market.CashierRole;
import market.CustomerRole;
import market.Item;
import market.ItemCollectorRole;
import market.DeliveryGuyRole;
import market.interfaces.*;
import gui.BuildingRecords;

import javax.swing.*;

import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.PersonAgent;
import agent.Role;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class MarketRecords implements BuildingRecords {
	MarketControlPanel marketControlPanel;
	

    private static final CityBuilding CityBuilding = null;

   
	
    private PersonAgent CashierPerson = new PersonAgent("Cashier");
    CashierRole ca = new CashierRole("Cashier", 100, CashierPerson);
	private CashierGui cashierGui;

    
    private List<ItemCollector> ItemCollectors = new Vector<ItemCollector>();
    private List<DeliveryGuy> DeliveryGuys = new Vector<DeliveryGuy>();
    private List<Customer> customers = new Vector<Customer>();
    
    private PersonAgent ItemCollectorPerson = new PersonAgent("ItemCollector1");
    private ItemCollectorRole ic = new ItemCollectorRole("ItemCollector1", ItemCollectorPerson);
    private ItemCollectorGui icGui;
   
    private PersonAgent DeliveryGuyPerson = new PersonAgent("DeliveryGuy1");
    private DeliveryGuyRole dg = new DeliveryGuyRole("DeliveryGuy1", CityBuilding , DeliveryGuyPerson);
    private DeliveryGuyGui dgGui = new DeliveryGuyGui(dg);

    List<Item> tempInventoryList = new ArrayList<Item>();
	{
		tempInventoryList.add(new Item("CheapCar", 1));
		tempInventoryList.add(new Item("ExpensiveCar", 0));
		tempInventoryList.add(new Item("Pizza", 1));
		tempInventoryList.add(new Item("Sandwich", 0));
		tempInventoryList.add(new Item("Chicken", 0));
	}
    
    private PersonAgent CustomerPerson = new PersonAgent("Customer1");
    private CustomerRole cust = new CustomerRole("Customer1", 100, tempInventoryList, CustomerPerson);
    private CustomerGui custGui = new CustomerGui(cust);
    
    private PersonAgent CustomerPerson1 = new PersonAgent("Customer2");
    private CustomerRole cust1 = new CustomerRole("Customer2", 100, tempInventoryList, CustomerPerson1);
    private CustomerGui custGui1 = new CustomerGui(cust1);


    //private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();
    //private Vector<MarketAgent> markets = new Vector<MarketAgent>();
    

    
   
    
    
    //private ListPanel customerPanel = new ListPanel(this, "Customers");
    //private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    
   


    private AnimationPanel gui; //reference to main gui

    public MarketRecords(AnimationPanel gui) {
    	//marketControlPanel = controlPanel;
    	cashierGui = new CashierGui(ca);
    	icGui = new ItemCollectorGui(ic);
        this.gui = gui;
        
            ic.setGui(icGui);
            dg.setGui(dgGui);
            ca.setGui(cashierGui);

            cust.setGui(custGui);
            cust1.setGui(custGui1);

        
        ItemCollectors.add(ic);
        DeliveryGuys.add(dg);
        customers.add(cust);
        customers.add(cust1);
        
        ca.setICList(ItemCollectors);
        ca.setDGList(DeliveryGuys);
        ic.setCashier(ca);
        dg.setCashier(ca);
        cust.setCashier(ca);
        cust1.setCashier(ca);
        ic.setInventoryList(ca.getInventoryList());
        

        //Customer Gui
        gui.addGui(custGui);
        gui.addGui(custGui1);

        //ItemCollector Gui
        gui.addGui(icGui);
        
        //Cashier Gui
        gui.addGui(cashierGui);
        
        //DeliveryGuy Gui
        gui.addGui(dgGui);
        
        CashierPerson.startThread();
        CashierPerson.addRole(ca);
        ca.activate();
        
        DeliveryGuyPerson.startThread();
        DeliveryGuyPerson.addRole(dg);
        dg.activate();
        
        ItemCollectorPerson.startThread();
        ItemCollectorPerson.addRole(ic);
        ic.activate();
        
        //Start the thread
        /*
        ca.startThread();
        ic.startThread();
        dg.startThread();
		*/

       // setLayout(new GridLayout(1, 2, 20, 20));
      


        
        CustomerPerson.startThread();
        CustomerPerson.addRole(cust);
        cust.activate();
        custGui.setBuying();
        
        CustomerPerson1.startThread();
        CustomerPerson1.addRole(cust1);
        cust1.activate();
        custGui1.setBuying();
        
        //ca.msgIWantItem(tempInventoryList, cust);
        //ca.msgPhoneOrder(tempInventoryList, cust, null);
      
    }

	public LocationTypeEnum getType() {
		return LocationTypeEnum.Market;
	}

	@Override
	public Role addPerson(String role, String name) {
		//setMarketControlPanel(marketControlPanel)
		
		
		
		return null;
	}


 
    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    
    /*
    public void addPerson(String type, String name, boolean hun) {
    	
    	if (type.equals("Waiters")) {
    		WaiterAgent wa = new WaiterAgent(name);	
    		WaiterGui g = new WaiterGui(wa);

    		gui.animationPanel.addGui(g);// dw

    		wa.setHost(host);
    		wa.setCook(co);
    		wa.setCashier(ca);
    		wa.setGui(g);
    		waiters.add(wa);
    		wa.startThread();
    		waiters.add(wa);
    		host.AddWaiter(wa);
    		
    	}
  
    	if (type.equals("Customers")) {
    		
    		CustomerAgent c = new CustomerAgent(name, hun);	
    		CustomerGui g = new CustomerGui(c, gui);
    		
    		
            
    		
    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(ca);
    		c.setGui(g);
    		customers.add(c);
    		
    		if (hun)
    		c.getGui().setHungry();
    		
    		c.startThread();
    		
    	}
    	
    }
    */

    

}
