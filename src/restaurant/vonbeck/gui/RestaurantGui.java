package restaurant.vonbeck.gui;

import gui.AnimationPanel;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import restaurant.vonbeck.CustomerRole;
import restaurant.vonbeck.WaiterRole;
import restaurant.vonbeck.interfaces.Waiter.BreakState;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
@SuppressWarnings("serial")
public class RestaurantGui implements ActionListener {
	private static final int SPACE_FOR_ANIMATION_PANEL = 300;
	private static final int WINDOWX = 750;
	private static final int WINDOWY = 350;
	/* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	private AnimationPanel animationPanel;
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel;
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     * @param castedAnimationPanel 
     */
    public RestaurantGui(AnimationPanel animationPanel) {
    	
    	this.animationPanel = animationPanel;
        
    	
        
        
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        currentPerson = person;

        if (person instanceof CustomerRole) {
            CustomerRole customer = (CustomerRole) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        } else if (person instanceof WaiterRole) {
            WaiterRole waiter = (WaiterRole) person;
            stateCB.setText("Break?");
          //Should checkmark be there? 
            stateCB.setSelected(waiter.getBreakState() != BreakState.Working);
            stateCB.setEnabled(waiter.getBreakState() == BreakState.Working);
            infoLabel.setText(
               "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == stateCB) {
            if (currentPerson instanceof CustomerRole) {
                CustomerRole c = (CustomerRole) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            } else if (currentPerson instanceof WaiterRole) {
            	WaiterRole w = (WaiterRole) currentPerson;
            	w.msgGUIGoOnBreak();
            	stateCB.setEnabled(false);
            }
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerRole c) {
        if (currentPerson instanceof CustomerRole) {
            CustomerRole cust = (CustomerRole) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    
    public void setWaiterEnabled(WaiterRole w) {
        if (currentPerson instanceof WaiterRole) {
            WaiterRole wait = (WaiterRole) currentPerson;
            if (w == wait) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    
    /**
	 * @return the animationPanel
	 */
	public AnimationPanel getAnimationPanel() {
		return animationPanel;
	}
	/**
	 * @param animationPanel the animationPanel to set
	 */
	public void setAnimationPanel(AnimationPanel animationPanel) {
		this.animationPanel = animationPanel;
	}
	public void setRestPanel(RestaurantPanel restPanel) {
		this.restPanel = restPanel;
		
		
	}
}
