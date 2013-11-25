package market.gui;

import market.CashierRole;
import market.CustomerRole;
import market.Item;
import market.ItemCollectorRole;
import market.DeliveryGuyRole;
import market.interfaces.*;

import javax.swing.*;

import agent.PersonAgent;
import agent.interfaces.Person;

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
public class MarketPanel extends JPanel {

    private static final CommonSimpleClasses.CityBuilding CityBuilding = null;

    private JLabel MarketLabel;
    //Market Label
	private JLabel ExpensiveCarInvent = null;
	private JLabel CheapCarInvent = null;
	private JLabel ChickenInvent = null;
	private JLabel PizzaInvent = null;
	private JLabel SandwichInvent = null;
	
	public JTextField ExpensiveCarText = null;
	public JTextField CheapCarText = null;
	public JTextField ChickenText = null;
	public JTextField PizzaText = null;
	public JTextField SandwichText = null;
	
    private PersonAgent CashierPerson = new PersonAgent("Cashier");
    private CashierRole ca = new CashierRole("Cashier", 100, CashierPerson);

    private CashierGui cashierGui = new CashierGui(ca);
    
    private String ExpensiveCarInventoryLevel = "Current Inventory Level";
    private String CheapCarInventoryLevel = "Current Inventory Level";
    private String PizzaInventoryLevel = "Current Inventory Level";
    private String SandwichInventoryLevel = "Current Inventory Level";
    private String ChickenInventoryLevel = "Current Inventory Level";
    
    private List<ItemCollector> ItemCollectors = new Vector<ItemCollector>();
    private List<DeliveryGuy> DeliveryGuys = new Vector<DeliveryGuy>();
    private List<Customer> customers = new Vector<Customer>();
    
    private PersonAgent ItemCollectorPerson = new PersonAgent("ItemCollector1");
    private ItemCollectorRole ic = new ItemCollectorRole("ItemCollector1", ItemCollectorPerson);
    private ItemCollectorGui icGui = new ItemCollectorGui(ic);
   
    private PersonAgent DeliveryGuyPerson = new PersonAgent("DeliveryGuy1");
    //private DeliveryGuyRole dg = new DeliveryGuyRole("DeliveryGuy1",DeliveryGuyPerson);
    //private DeliveryGuyGui dgGui = new DeliveryGuyGui(dg);

    List<Item> tempInventoryList = new ArrayList<Item>();
	{
		tempInventoryList.add(new Item("CheapCar", 1));
		tempInventoryList.add(new Item("ExpensiveCar", 0));
		tempInventoryList.add(new Item("Pizza", 1));
		tempInventoryList.add(new Item("Sandwich", 0));
		tempInventoryList.add(new Item("Chicken", 0));
	}
    
    private PersonAgent CustomerPerson = new PersonAgent("Customer1Person");
    private CustomerRole cust = new CustomerRole("Customer1", 150, tempInventoryList, CustomerPerson);
    private CustomerGui custGui = new CustomerGui(cust);
    
    private PersonAgent CustomerPerson1 = new PersonAgent("Customer2Person");
    private CustomerRole cust1 = new CustomerRole("Customer2", 150, tempInventoryList, CustomerPerson1);
    private CustomerGui custGui1 = new CustomerGui(cust1);


    //private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();
    //private Vector<MarketAgent> markets = new Vector<MarketAgent>();
    

    
    private JPanel marketLabel = new JPanel();
    
    
    //private ListPanel customerPanel = new ListPanel(this, "Customers");
    //private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    
    //group is the UI interface
    private JPanel group = new JPanel();


    private MarketGui gui; //reference to main gui

    public MarketPanel(MarketGui gui) {
        this.gui = gui;
        
            ic.setGui(icGui);
           // dg.setGui(dgGui);
            ca.setGui(cashierGui);

            cust.setGui(custGui);
            cust1.setGui(custGui1);

        
        ItemCollectors.add(ic);
       // DeliveryGuys.add(dg);
        customers.add(cust);
        customers.add(cust1);
        
        ca.setICList(ItemCollectors);
        ca.setDGList(DeliveryGuys);
        ic.setCashier(ca);
        //dg.setCashier(ca);
        cust.setCashier(ca);
        cust1.setCashier(ca);
        ic.setInventoryList(ca.getInventoryList());
        

        //Customer Gui
        gui.animationPanel.addGui(custGui);
        gui.animationPanel.addGui(custGui1);

        //ItemCollector Gui
        gui.animationPanel.addGui(icGui);
        
        //Cashier Gui
        gui.animationPanel.addGui(cashierGui);
        
        //DeliveryGuy Gui
        //gui.animationPanel.addGui(dgGui);
        
        CashierPerson.startThread();
        CashierPerson.addRole(ca);
        ca.activate();
        
        DeliveryGuyPerson.startThread();
       // DeliveryGuyPerson.addRole(dg);
        //dg.activate();
        
        ItemCollectorPerson.startThread();
        ItemCollectorPerson.addRole(ic);
        ic.activate();
        
        //Start the thread
        /*
        ca.startThread();
        ic.startThread();
        dg.startThread();
		*/

        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(2, 2, 10, 10));


        initMarketLabel();
        add(marketLabel);


        
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

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initMarketLabel() {
    	
    	
    	//Creating the header of the UI
    	double cash = ca.getCash();
    	JPanel NorthPanel = new JPanel();
    	NorthPanel.setLayout(new FlowLayout());
    	MarketLabel = new JLabel();
    	MarketLabel.setText("Today's Staff :    " + ca.getName() + "                     " + "Market's Current Cash :   " + cash);
    	NorthPanel.add(new JLabel("               "));
    	NorthPanel.add(MarketLabel);
    	NorthPanel.add(new JLabel("               "));
    	
    	//Creating the UI
    	JPanel panel = new JPanel();
    	panel.setLayout(new GridLayout(0,1,0,0));
    	JLabel ExpensiveCarLabel = new JLabel("               ExpensiveCar");
    	JLabel CheapCarLabel = new JLabel("               CheapCar");
    	JLabel ChickenLabel = new JLabel("               Chicken");
    	JLabel PizzaLabel = new JLabel("               Pizza");
    	JLabel SandwichLabel = new JLabel("               Sandwich");
    	
    	ExpensiveCarInventoryLevel = "Current Inventory Level";
    	CheapCarInventoryLevel = "Current Inventory Level";
    	PizzaInventoryLevel = "Current Inventory Level";
    	SandwichInventoryLevel = "Current Inventory Level";
    	ChickenInventoryLevel = "Current Inventory Level";
    	

    	
    	ExpensiveCarInvent = new JLabel();
    	ExpensiveCarInvent.setText(ExpensiveCarInventoryLevel);
    	CheapCarInvent = new JLabel();
    	CheapCarInvent.setText(CheapCarInventoryLevel);
    	ChickenInvent = new JLabel();
    	ChickenInvent.setText(ChickenInventoryLevel);
    	PizzaInvent = new JLabel();
    	PizzaInvent.setText(PizzaInventoryLevel);
    	SandwichInvent = new JLabel();
    	SandwichInvent.setText(SandwichInventoryLevel);
    	
    	
    	
    	ExpensiveCarText = new JTextField("");
    	ExpensiveCarText.setEditable(true);
    	CheapCarText = new JTextField("");
    	CheapCarText.setEditable(true);
    	ChickenText = new JTextField("");
    	ChickenText.setEditable(true);
    	PizzaText = new JTextField("");
    	PizzaText.setEditable(true);
    	SandwichText = new JTextField("");
    	SandwichText.setEditable(true);
    	
    	JPanel ExpensiveCarPanel = new JPanel();
    	ExpensiveCarPanel.setLayout(new GridLayout(1,0,0,0));
    	ExpensiveCarPanel.add(ExpensiveCarLabel);
    	ExpensiveCarPanel.add(ExpensiveCarInvent);
    	ExpensiveCarPanel.add(ExpensiveCarText);
    	
    	JPanel CheapCarPanel = new JPanel();
    	CheapCarPanel.setLayout(new GridLayout(1,0,0,0));
    	CheapCarPanel.add(CheapCarLabel);
    	CheapCarPanel.add(CheapCarInvent);
    	CheapCarPanel.add(CheapCarText);
    	
    	JPanel ChickenPanel = new JPanel();
    	ChickenPanel.setLayout(new GridLayout(1,0,0,0));
    	ChickenPanel.add(ChickenLabel);
    	ChickenPanel.add(ChickenInvent);
    	ChickenPanel.add(ChickenText);
    	
    	JPanel PizzaPanel = new JPanel();
    	PizzaPanel.setLayout(new GridLayout(1,0,0,0));
    	PizzaPanel.add(PizzaLabel);
    	PizzaPanel.add(PizzaInvent);
    	PizzaPanel.add(PizzaText);
    	
    	JPanel SandwichPanel = new JPanel();
    	SandwichPanel.setLayout(new GridLayout(1,0,0,0));
    	SandwichPanel.add(SandwichLabel);
    	SandwichPanel.add(SandwichInvent);
    	SandwichPanel.add(SandwichText);
    	
    	
    	//Default Panel
    	JPanel DefaultPanel = new JPanel();
    	DefaultPanel.setLayout(new GridLayout(1,0,0,0));
    	DefaultPanel.add(new JLabel("                     Type"));
    	DefaultPanel.add(new JLabel("      Current Inventory Level"));
    	DefaultPanel.add(new JLabel("      Desire Inventory Level"));
    	
    	panel.add(DefaultPanel);
    	panel.add(ExpensiveCarPanel);
    	panel.add(CheapCarPanel);
    	panel.add(ChickenPanel);
    	panel.add(PizzaPanel);
    	panel.add(SandwichPanel);

    	
    	
    	marketLabel.setLayout(new BorderLayout());
    	marketLabel.add(NorthPanel, BorderLayout.NORTH);
    	marketLabel.add(panel, BorderLayout.CENTER);
    	
    	UpdateInventoryLevelWithoutButton();
    	
    	/*
        JLabel label = new JLabel();
        marketLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Today's Staff</u></h3><table><tr><td>host:</td><td>" + ca.getName());

        marketLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        marketLabel.add(label, BorderLayout.CENTER);
        marketLabel.add(new JLabel("               "), BorderLayout.EAST);
        marketLabel.add(new JLabel("               "), BorderLayout.WEST);
        */
    }

	 public void UpdateInventoryLevelWithButton(){
		 Map<String,Integer> IList = ca.getInventoryList();
		 
		 if(  isInteger(ExpensiveCarText.getText())  )
			 IList.put("ExpensiveCar", Integer.parseInt(ExpensiveCarText.getText()));
		 if(  isInteger(CheapCarText.getText())  )
			 IList.put("CheapCar", Integer.parseInt(CheapCarText.getText()));
		 if(  isInteger(PizzaText.getText())  )
			 IList.put("Pizza", Integer.parseInt(PizzaText.getText()));
		 if(  isInteger(SandwichText.getText())  )
			 IList.put("Sandwich", Integer.parseInt(SandwichText.getText()));
		 if(  isInteger(ChickenText.getText())  )
			 IList.put("Chicken", Integer.parseInt(ChickenText.getText()));
		 
		 
		 	ExpensiveCarInventoryLevel	= Integer.toString(IList.get("ExpensiveCar"));
	    	CheapCarInventoryLevel		= Integer.toString(IList.get("CheapCar"));
	    	PizzaInventoryLevel 		= Integer.toString(IList.get("Pizza"));
	    	SandwichInventoryLevel		= Integer.toString(IList.get("Sandwich"));
	    	ChickenInventoryLevel 		= Integer.toString(IList.get("Chicken"));
	    	ExpensiveCarInvent.setText("                    " + ExpensiveCarInventoryLevel);
	    	CheapCarInvent.setText("                    " + CheapCarInventoryLevel);
	    	ChickenInvent.setText("                    " + ChickenInventoryLevel);
	    	PizzaInvent.setText("                    " + PizzaInventoryLevel);
	    	SandwichInvent.setText("                    " + SandwichInventoryLevel);
	    	
	    	ExpensiveCarText.setText("");
	    	CheapCarText.setText("");
	    	PizzaText.setText("");
	    	ChickenText.setText("");
	    	SandwichText.setText("");
	    	
	    	MarketLabel.setText("Today's Staff :    " + ca.getName() + "                     " + "Market's Current Cash :   " + ca.getCash());
	    	
	 }
	 
	 public void UpdateInventoryLevelWithoutButton(){
		 Map<String,Integer> IList = ca.getInventoryList();
		 	ExpensiveCarInventoryLevel	= Integer.toString(IList.get("ExpensiveCar"));
	    	CheapCarInventoryLevel		= Integer.toString(IList.get("CheapCar"));
	    	PizzaInventoryLevel 		= Integer.toString(IList.get("Pizza"));
	    	SandwichInventoryLevel		= Integer.toString(IList.get("Sandwich"));
	    	ChickenInventoryLevel 		= Integer.toString(IList.get("Chicken"));
	    	ExpensiveCarInvent.setText("                    " + ExpensiveCarInventoryLevel);
	    	CheapCarInvent.setText("                    " + CheapCarInventoryLevel);
	    	ChickenInvent.setText("                    " + ChickenInventoryLevel);
	    	PizzaInvent.setText("                    " + PizzaInventoryLevel);
	    	SandwichInvent.setText("                    " + SandwichInventoryLevel);
	    	
	    	MarketLabel.setText("Today's Staff :    " + ca.getName() + "                     " + "Market's Current Cash :   " + ca.getCash());
	    	System.out.println (ca.getCash());
	    	
	 }

	 public boolean isInteger(String str) {
		    int size = str.length();

		    for (int i = 0; i < size; i++) {
		        if (!Character.isDigit(str.charAt(i))) {
		            return false;
		        }
		    }

		    return size > 0;
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
