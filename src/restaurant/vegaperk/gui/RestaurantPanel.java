package restaurant.vegaperk.gui;

import restaurant.vegaperk.backend.CashierRole;
import restaurant.vegaperk.backend.CookRole;
import restaurant.vegaperk.backend.CustomerRole;
import restaurant.vegaperk.backend.HostRole;
import restaurant.vegaperk.backend.MarketAgent;
import restaurant.vegaperk.backend.WaiterAgent;

import javax.swing.*;

import CommonSimpleClasses.Constants;
import agent.PersonAgent;
import agent.Role;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */

@SuppressWarnings("serial")
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
	private ArrayList<Role> agentList = new ArrayList<Role>();
    
    private PersonAgent hostPerson;
    private HostRole host = new HostRole("Mr. Krabs");
    
    // TODO the table map should be stored in the table gui
    private TableGui tableGui = new TableGui(host.getTableMap());
    
    private PersonAgent cookPerson;
    private CookRole cook = new CookRole("Cook");
    
    private PersonAgent cashierPerson;
    private CashierRole cashier = new CashierRole("Squidward");
    
    private MarketAgent m1 = new MarketAgent("Market 1");
    private MarketAgent m2 = new MarketAgent("Market 2");
    private MarketAgent m3 = new MarketAgent("Market 3");
    
    private Vector<CustomerRole> customers = new Vector<CustomerRole>();

    private JPanel restLabel = new JPanel();
    private CustomerPanel customerPanel = new CustomerPanel(this, "Customers");
    private WaiterPanel waiterPanel = new WaiterPanel(this, "Waiters");
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui
    
    private CookGui cookGui = new CookGui(cook, gui);

    public RestaurantPanel(RestaurantGui gui) {
    	// add all of the agents to the list so they can be paused
    	agentList.add(cook);
    	agentList.add(host);
    	agentList.add(cashier);
//    	agentList.add(m1);
//    	agentList.add(m2);
//    	agentList.add(m3);
    	
        this.gui = gui;
        gui.getAnimationPanel().addGui(tableGui);
        gui.getAnimationPanel().addGui(cookGui);

        cook.setGui(cookGui);
        cook.addMarket(m1);
        cook.addMarket(m2);
        cook.addMarket(m3);
        
        m1.setInventory(1);
        m2.setInventory(2);
        m3.setInventory(3);
        
        m1.setCook(cook);
        m2.setCook(cook);
        m3.setCook(cook);
        
        m1.setCashier(cashier);
        m2.setCashier(cashier);
        m2.setCashier(cashier);
        
        m1.startThread();
        m2.startThread();
        m3.startThread();
        
        if(Constants.TEST_POPULATE_RESTAURANT){
        	// set up the host
        	hostPerson = new PersonAgent("Host");
        	hostPerson.addRole(host);
        	host.setPerson(hostPerson);
        	host.activate();
        	hostPerson.startThread();
        	
        	// set up cook
        	cookPerson = new PersonAgent("Cook");
        	cookPerson.addRole(cook);
        	cook.setPerson(cookPerson);
        	cook.activate();
        	cookPerson.startThread();
        	
        	// set up the cashier
        	cashierPerson = new PersonAgent("Cashier");
        	cashierPerson.addRole(cashier);
        	cashier.setPerson(cashierPerson);
        	cashier.activate();
        	cashierPerson.startThread();
        }
        
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);
        group.add(waiterPanel);

        initRestLabel();
        add(restLabel);
        add(group);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Galley Grub </u></h3><table><tr><td>Krabby Patty</td><td>$1.25</td></tr><tr><td>Kelp Rings with Salty Sauce</td><td>$2.00</td></tr><tr><td>Coral Bits</td><td>$1.50</td></tr><tr><td>Kelp Shake</td><td>$2.00</td></tr></table><br></html>");

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

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public CustomerGui addCustomer(String type, String name) {

    	if (type.equals("Customers")) {
    		// new role and person stuff
    		PersonAgent person = new PersonAgent("Customer");
    		
    		CustomerRole c = new CustomerRole(person, null);
    		CustomerGui g = new CustomerGui(c, gui);
    		
    		person.addRole(c);
    		c.setPerson(person);
    		c.activate();
    		
    		// add the role to the list
    		agentList.add(c);

    		gui.getAnimationPanel().addGui(g);
    		c.setHost(host);
    		c.setGui(g);
    		customers.add(c);
    		person.startThread();
    		return g;
    	}
    return null;
    }
    public WaiterGui addWaiter(String type, String name){
    	if(type.equals("Waiters")){
    		// new person/role stuff
    		PersonAgent person = new PersonAgent("Waiter");
    		
    		WaiterAgent w = new WaiterAgent(name, cashier);
    		WaiterGui wg = new WaiterGui(w, gui);
    		
    		person.addRole(w);
    		w.setPerson(person);
    		w.activate();
    		person.startThread();
    		
    		agentList.add(w);
    		w.setHost(host);
    		w.setCook(cook);
    		
    		gui.getAnimationPanel().addGui(wg);
    		w.setGui(wg);
    		host.addWaiter(w);
    		return wg;
    	}
   		return null;    		
    }
    public void setCustomerEnabled(CustomerRole c){
    	customerPanel.setCustomerEnabled(c);
    }
    public void denyBreak(WaiterAgent w){
    	waiterPanel.denyBreak(w);
    }
    
    public HostRole getHost(){
    	return host;
    }
}