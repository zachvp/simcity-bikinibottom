package market.gui;

import market.CustomerRole;
import market.interfaces.Customer;

import javax.swing.*;

import agent.gui.AnimationPanel;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class MarketGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
	private MarketBuilding marketBuilding = new MarketBuilding(1,1,1,1);
	private MarketRecords marketRecords = new MarketRecords(animationPanel, marketBuilding);
    private MarketInfoPanel marketPanel = new MarketInfoPanel(marketRecords);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
     



    

    /**
     * Constructor for MarketGui class.
     * Sets up all the gui components.
     */
    public MarketGui() {
        int WINDOWX = 600;
        int WINDOWY = 490;

    	JPanel BigPanel = new JPanel();
    	BigPanel.setSize(200, 490);
    	setBounds(100, 100, 2*WINDOWX, 100+WINDOWY);

        setLayout(new GridLayout(1, 0));
        
        
        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .6));
        marketPanel.setPreferredSize(restDim);
        marketPanel.setMinimumSize(restDim);
        marketPanel.setMaximumSize(restDim);
        BigPanel.add(marketPanel);
        
        add(BigPanel);
        add(animationPanel);
        
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    /*
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof CustomerAgent) {
            Customer customer = (Customer) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
            WaiterButton.setVisible(false);
            StatusLabel.setVisible(false);
            
  
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        
        if (person instanceof WaiterAgent) {
            Waiter waiter = (Waiter) person;
            WaiterButton.setVisible(true);
            stateCB.setVisible(false);
            StatusLabel.setVisible(true);
            
            if (waiter.IsOnBreak()){
            	StatusLabel.setText(" Status : On Break");
            }
            else
            	StatusLabel.setText(" Status : Working");
            
            infoLabel.setText(
               "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
            
        }
        infoPanel.validate();
    }
    */
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    
    public void actionPerformed(ActionEvent e) {
 
    }
    

    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    
    /*
    public void setCustomerEnabled(Customer c) {
        if (currentPerson instanceof CustomerAgent) {
            restPanel.FinishEating(c);
            Customer cust = (Customer) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    */
    

    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        MarketGui gui = new MarketGui();
        gui.setTitle("csci201 SimCity Market");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}
