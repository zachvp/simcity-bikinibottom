package restaurant.strottma.gui;

import restaurant.strottma.CashierRole;
import restaurant.strottma.CookRole;
import restaurant.strottma.CustomerRole;
import restaurant.strottma.HostRole;
import restaurant.strottma.MarketRole;
import restaurant.strottma.WaiterRole;
import gui.BuildingRecords;
import gui.MainFrame;

import javax.swing.*;

import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.Agent;
import agent.PersonAgent;
import agent.Role;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantRecords implements BuildingRecords {
	// CONSTANTS FOR USE IN CONSTRUCTOR
	private static final int LAYOUT_ROWS = 1;
    private static final int LAYOUT_COLUMNS = 3;
    private static final int LAYOUT_HGAP = 20;
    private static final int LAYOUT_VGAP = 20;
    private static final int GROUP_LAYOUT_ROWS = 1;
    private static final int GROUP_LAYOUT_COLUMNS = 3;
    private static final int GROUP_LAYOUT_HGAP = 10;
    private static final int GROUP_LAYOUT_VGAP = 10;
    
    //private RestaurantGui gui; //reference to main gui
	private AnimationPanel gui;
    
    
    //Host, cook, waiters and customers
    private ArrayList<Agent> agents = new ArrayList<Agent>();
    private ArrayList<Role> roles = new ArrayList<Role>();
    
    private PersonAgent sarah = new PersonAgent("Sarah");
    private PersonAgent john = new PersonAgent("John");
    private PersonAgent mike = new PersonAgent("Mike");
    
    // TODO Markets probably shouldn't be people.
    private PersonAgent marketPerson1 = new PersonAgent("Market1");
    private PersonAgent marketPerson2 = new PersonAgent("Market2");
    
    private HostRole host = new HostRole(sarah);
    private CookRole cook = new CookRole(john);
    private CashierRole cashier = new CashierRole(mike);
    private MarketRole market1 = new MarketRole(marketPerson1);
    private MarketRole market2 = new MarketRole(marketPerson2);
    
    private Vector<CustomerRole> customers = new Vector<CustomerRole>();
    private Vector<WaiterRole> waiters = new Vector<WaiterRole>();

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();
    
   // private LocationTypeEnum type = LocationTypeEnum.Restaurant;
    
    public RestaurantRecords(AnimationPanel g) {
    	this.gui = g;
        
        agents.add(sarah);
        agents.add(john);
        agents.add(mike);
        agents.add(marketPerson1);
        agents.add(marketPerson2);
        
        roles.add(host);
        roles.add(cook);
        roles.add(market1);
        roles.add(market2);
        roles.add(cashier);
                
        sarah.addRole(host);
        john.addRole(cook);
        mike.addRole(cashier);
        marketPerson1.addRole(market1);
        marketPerson2.addRole(market2);
        
        HostGui hostGui = new HostGui(host);
        host.setGui(hostGui);
        gui.addGui(hostGui);
        
        CookGui cookGui = new CookGui(cook);
        cook.setGui(cookGui);
        gui.addGui(cookGui);
        
        market1.setCook(cook);
        market2.setCook(cook);
        market1.setCashier(cashier);
        market2.setCashier(cashier);
        cook.addMarket(market1);
        cook.addMarket(market2);
        
        for (Role r : roles) {
        	r.activate();
        }
        
        for (Agent a : agents) {
        	a.startThread();
        }
        
       
    }
    
    
    public LocationTypeEnum getType(){
    	return LocationTypeEnum.Restaurant;
    }
    

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                CustomerRole temp = customers.get(i);
                //if (temp.getName() == name)
                    //gui.updateInfoPanel(temp);
            }
        }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public Role addPerson(String type, String name) {
    	
    	if (type.equals("Customers")) {
    		PersonAgent p = new PersonAgent(name);
    		CustomerRole c = new CustomerRole(p);	
    		CustomerGui g = new CustomerGui(c, this);
    		
    		//TODO 
    		g.setHungry();
    		
    		gui.addGui(g);
    		c.setHost(host);
    		c.setGui(g);
    		agents.add(p);
    		customers.add(c);
    		roles.add(c);
    		c.activate();
    		p.addRole(c);
    		p.startThread();
    		
    		System.out.println("RESTPANEL: ADDED cust");
    		
    		return c;
    		
    	} else if (type.equals("Waiters")) {
    		PersonAgent p = new PersonAgent(name);
    		WaiterRole w = new WaiterRole(p);
    		w.setOthers(host, cook, cashier);
    		WaiterGui wg = new WaiterGui(w);
    		
    		w.setGui(wg);
    		agents.add(p);
    		waiters.add(w);
    		roles.add(w);
    		w.activate();
    		host.addWaiter(w);
    		p.addRole(w);
    		p.startThread();
    		gui.addGui(wg);
    		
    		System.out.println("RESTPANEL: ADDED WAITER");
    		
    		return w;
    	} else {
    		System.out.println(type + " is not a valid type.");
    	}
    	
    	return null;
    }
    
    /**
     * Finds a customer or waiter from the appropriate list.
     * 
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     * @return null if the Role can't be found
     */
    public Role findRole(String type, String name) {
    	if (type.equals("Customers")) {
    		CustomerRole c = null;
    		for (CustomerRole temp : customers) {
    			if (temp.getName().equals(name)) {
    				c = temp;
    			}
    		}
    		return c;
    	}
    	else return null;
    }
        
    public void setRoleEnabled(Role r) {
    	if (r instanceof CustomerRole) {
    		customerPanel.setRoleEnabled(r);
    	} else if (r instanceof WaiterRole) {
    		waiterPanel.setRoleEnabled(r);
    	}
        
    }
    
    public void pauseAgents() {
    	System.out.println("RestaurantPanel: pauseRoles() called");
    	for (Agent a : agents) {
    		a.pauseThread();
    	}
    }
    
    public void resumeAgents() {
    	System.out.println("RestaurantPanel: resumeRoles() called");
    	for (Agent a : agents) {
    		a.resumeThread();
    	}
    }

}
