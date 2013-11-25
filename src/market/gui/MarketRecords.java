package market.gui;

import market.CashierRole;
import market.CustomerRole;
import market.Item;
import market.ItemCollectorRole;
import market.DeliveryGuyRole;
import market.interfaces.*;
import gui.BuildingRecords;

import javax.swing.*;

import transportation.FakePassengerRole;
import transportation.PassengerRole;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import CommonSimpleClasses.XYPos;
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
	MarketInfoPanel marketControlPanel;
	MarketBuilding building;
	
    private PersonAgent CashierPerson = new PersonAgent("Cashier");
    CashierRole ca = new CashierRole("Cashier", 100, CashierPerson);
	private CashierGui cashierGui;

    private List<ItemCollector> ItemCollectors = new Vector<ItemCollector>();
    private List<DeliveryGuy> DeliveryGuys = new Vector<DeliveryGuy>();
    
    private PersonAgent ItemCollectorPerson = new PersonAgent("ItemCollector1");
    private ItemCollectorRole ic = new ItemCollectorRole("ItemCollector1", ItemCollectorPerson);
    private ItemCollectorGui icGui;
   
    private PersonAgent ItemCollectorPerson1 = new PersonAgent("ItemCollector2");
    private ItemCollectorRole ic1 = new ItemCollectorRole("ItemCollector2", ItemCollectorPerson1);
    private ItemCollectorGui ic1Gui;
    
    private PersonAgent DeliveryGuyPerson = new PersonAgent("DeliveryGuy1");
    private DeliveryGuyRole dg = new DeliveryGuyRole("DeliveryGuy1", DeliveryGuyPerson, building);
    private DeliveryGuyGui dgGui = new DeliveryGuyGui(dg);

    List<Item> tempInventoryList = new ArrayList<Item>();
	{
		tempInventoryList.add(new Item("Toyoda", 1));
		tempInventoryList.add(new Item("LamboFinny", 0));
		tempInventoryList.add(new Item("Krabby Patty", 1));
		tempInventoryList.add(new Item("Kelp Shake", 0));
		tempInventoryList.add(new Item("Coral Bits", 0));
		tempInventoryList.add(new Item("Kelp Rings", 0));
		
	}
    
	/*
    private PersonAgent CustomerPerson = new PersonAgent("Customer1");
    private CustomerRole cust = new CustomerRole("Customer1", 100, tempInventoryList, CustomerPerson);
    private PassengerRole pr = new FakePassengerRole(fakeLoc);
    {CustomerPerson.addRole(pr);}
    private CustomerGui custGui = new CustomerGui(cust);
    
    private PersonAgent CustomerPerson1 = new PersonAgent("Customer2");
    private CustomerRole cust1 = new CustomerRole("Customer2", 100, tempInventoryList, CustomerPerson1);
    {CustomerPerson.addRole(pr);}
    private CustomerGui custGui1 = new CustomerGui(cust1);
    */
    

    private AnimationPanel gui; //reference to main gui

    public MarketRecords(AnimationPanel gui, MarketBuilding market) {
    	//marketControlPanel = controlPanel;
    	building = market;
    	cashierGui = new CashierGui(ca);
    	icGui = new ItemCollectorGui(ic);
    	ic1Gui = new ItemCollectorGui(ic1);
        this.gui = gui;
        
            ic.setGui(icGui);
            ic1.setGui(ic1Gui);
            dg.setGui(dgGui);
            ca.setGui(cashierGui);

            /*
            cust.setGui(custGui);
            cust1.setGui(custGui1);
			*/

        
        ItemCollectors.add(ic);
        ItemCollectors.add(ic1);
        DeliveryGuys.add(dg);

        
        ca.setICList(ItemCollectors);
        ca.setDGList(DeliveryGuys);
        ic.setCashier(ca);
        ic1.setCashier(ca);
        dg.setCashier(ca);
        /*
        cust.setCashier(ca);
        cust1.setCashier(ca);
        cust.setPriceList(ca.getPriceList());
        cust1.setPriceList(ca.getPriceList());
        */
        ic.setInventoryList(ca.getInventoryList());
        ic1.setInventoryList(ca.getInventoryList());
        

        //Customer Gui
        /*
        gui.addGui(custGui);
        gui.addGui(custGui1);
		*/

        //ItemCollector Gui
        gui.addGui(icGui);
        gui.addGui(ic1Gui);
        
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
        ic1Gui.setItemCollectorNumber(1);
        ic.setInventoryList(ca.getInventoryList());
        
        ItemCollectorPerson1.startThread();
        ItemCollectorPerson1.addRole(ic1);
        ic1.activate();
        icGui.setItemCollectorNumber(2);
        ic1.setInventoryList(ca.getInventoryList());
        
        /*
        CustomerPerson.startThread();
        CustomerPerson.addRole(cust);
        cust.setPriceList(ca.getPriceList());
        cust.activate();
        custGui.setBuying();
        
        CustomerPerson1.startThread();
        CustomerPerson1.addRole(cust1);
        cust.setPriceList(ca.getPriceList());
        cust1.activate();
        custGui1.setBuying();
        */
      
    }

	public LocationTypeEnum getType() {
		return LocationTypeEnum.Market;
	}

	@Override
	public Role addPerson(/*Person person,*/ String role, String name) {
		if (role == "Cashier"){
			
		}
		if (role == "ItemCollector"){
			ItemCollectorRole ic = new ItemCollectorRole("ItemCollector1", ItemCollectorPerson);
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
