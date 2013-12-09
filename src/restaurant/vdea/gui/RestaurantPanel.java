package restaurant.vdea.gui;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;
import restaurant.HostAgent;
import restaurant.CookAgent;
import restaurant.CashierAgent;
import restaurant.MarketAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including waiter, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Waiter, cook, waiters and customers
    //private WaiterAgent waiter = new WaiterAgent("Sam");
   // private WaiterGui waiterGui = new WaiterGui(waiter);
    
    private HostAgent host = new HostAgent("Sarah");
    private HostGui hostGui = new HostGui(host);
    private CashierAgent cashier = new CashierAgent("Steve");
    private CookAgent cook = new CookAgent("Bob");
    private CookGui cookGui = new CookGui(cook);
    private MarketAgent marketA = new MarketAgent("Super Market");
  	private MarketAgent marketB = new MarketAgent("Marketplace");
  	private MarketAgent marketC = new MarketAgent("Food Mart");
    
    //private CookGui cookGui = new CookGui(cook);
    
    

    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();
    
    private JRadioButton waiterRB = new JRadioButton("Waiters");
    private JRadioButton customerRB = new JRadioButton("Customers");

    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
       // waiter.setGui(waiterGui); 
       host.setGui(hostGui);
       cook.setGui(cookGui);
        
       cook.addMarket(marketA);
       cook.addMarket(marketB);
       cook.addMarket(marketC);

       cook.setCashier(cashier);

       // marketA.setCashier(cashier);
       // marketB.setCashier(cashier);
       // marketC.setCashier(cashier);


       gui.animationPanel.addGui(hostGui);
       gui.animationPanel.addGui(cookGui);

       host.startThread();
       cook.startThread();
       cashier.startThread(); //TODO Thread stopped for Unit testing
       marketA.startThread();
       marketB.startThread();
       marketC.startThread();

       setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new BorderLayout());
                
        //RadioButtons to select type of person to add
        JPanel radioButtons = new JPanel();
        radioButtons.setLayout(new BorderLayout());
        ButtonGroup rbGroup = new ButtonGroup();
        waiterRB.setSelected(true);
        rbGroup.add(waiterRB);
        rbGroup.add(customerRB);
        radioButtons.add(waiterRB, BorderLayout.WEST);
        radioButtons.add(customerRB, BorderLayout.EAST);
        group.add(radioButtons, BorderLayout.NORTH);
        
        
        JPanel cards = new JPanel(new CardLayout());
        cards.add(customerPanel);
        cards.add(waiterPanel);
        group.add(cards);
        
        //Display panels
        customerPanel.setVisible(false);
        waiterPanel.setVisible(true);

        //Action Listeners for RadioButtons
        waiterRB.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e){
        		customerPanel.setVisible(false);
        		waiterPanel.setVisible(true);
        	}
        });
        customerRB.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e){
        		waiterPanel.setVisible(false);
        		customerPanel.setVisible(true);
        	}
        });
        
        initRestLabel();
        add(restLabel);
        add(group);


    }


    /**
     * Sets up the restaurant label that includes the menu,
     * and waiter and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
       // restLabel.setLayout(new FlowLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>Host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label);//, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "));//, BorderLayout.EAST);
        restLabel.add(new JLabel("               "));//, BorderLayout.WEST);
        
        //restLabel.add(waiterPanel);
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
                CustomerAgent temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        
        if (type.equals("Waiters")) {

            for (int i = 0; i < waiters.size(); i++) {
                WaiterAgent temp = waiters.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        
        
      
        
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(c);
    		c.startThread();
    	}
    	
    	//add waiters like customers
    	if (type.equals("Waiters")) {
    		int pos = waiters.size();
    		
    		WaiterAgent w = new WaiterAgent(name);	
    		WaiterGui g = new WaiterGui(w, gui, pos+1);

    		gui.animationPanel.addGui(g);
    		w.setHost(host);
    		w.setCook(cook);
    		w.setCashier(cashier);
    		w.setGui(g);
    		waiters.add(w);
    		host.addWaiter(w);
    		w.startThread();
    	}
    }
    
    public void setHunger(boolean hungry){
    	gui.makeHungry(hungry);
    }
    
    public HostAgent getHost(){
    	return host;
    }
    
    public void pauseAgents(){
    	//waiter.pause();
    	host.pause();
    	cook.pause();
    	cashier.pause();
    	marketA.pause();
    	marketB.pause();
    	marketC.pause();
    	for (WaiterAgent w : waiters){
    		w.pause();
    	}
    	for (CustomerAgent c : customers){
    		c.pause();
    	}
    	//add a way to pause calling updatePosition to stop gui==============
    	System.out.println("Paused");
    }
    
    public void restartAgents(){
    	//waiter.restart();
    	host.restart();
    	cook.restart();
    	cashier.restart();
    	marketA.restart();
    	marketB.restart();
    	marketC.restart();
    	for (CustomerAgent c : customers){
    		c.restart();
    	}
    	for (WaiterAgent w : waiters){
    		w.restart();
    	}
    	System.out.println("Restart");
    }

}
