package restaurant.strottma.gui;

import gui.AnimationPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;

import restaurant.strottma.CashierRole;
import restaurant.strottma.CookRole;
import restaurant.strottma.CustomerRole;
import restaurant.strottma.HostRole;
import restaurant.strottma.MarketRole;
import restaurant.strottma.WaiterRole;
import agent.Agent;
import agent.PersonAgent;
import agent.Role;

public class RestaurantGui extends JPanel implements ActionListener {

	AnimationPanel animationPanel = new AnimationPanel();
	
    //Host, cook, waiters and customers
    private ArrayList<Agent> agents = new ArrayList<Agent>();
    private ArrayList<Role> roles = new ArrayList<Role>();
    
    private PersonAgent sarah = new PersonAgent("Sarah");
    private PersonAgent john = new PersonAgent("John");
    private PersonAgent mike = new PersonAgent("Mike");
    
    // TODO Markets probably shouldn't be people.
    private PersonAgent marketPerson1 = new PersonAgent("Market1");
    private PersonAgent marketPerson2 = new PersonAgent("Market2");
    
    
   // private HostRole host = new HostRole(sarah, building);
   // private CookRole cook = new CookRole(john, building);
   // private CashierRole cashier = new CashierRole(mike, building);
   // private MarketRole market1 = new MarketRole(marketPerson1, building);
   // private MarketRole market2 = new MarketRole(marketPerson2, building);
        
    private Vector<CustomerRole> customers = new Vector<CustomerRole>();
    private Vector<WaiterRole> waiters = new Vector<WaiterRole>();

    private JPanel restLabel = new JPanel();
    //private ListPanel customerPanel = new ListPanel(this, "Customers");
   // private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();
	
    private LayoutGui layoutGui;
	
	public RestaurantGui() {
		int WINDOWX = 600;
        int WINDOWY = 490;
        
        layoutGui = new LayoutGui();
        animationPanel.addGui(layoutGui);
	
	}

	
	public AnimationPanel getAnimationPanel()  {
    	return animationPanel;
    }


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
