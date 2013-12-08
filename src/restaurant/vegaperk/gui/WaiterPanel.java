package restaurant.vegaperk.gui;

import restaurant.vegaperk.CustomerAgent;
import restaurant.vegaperk.HostAgent;
import restaurant.vegaperk.WaiterAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */

public class WaiterPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<WaiterListItem> waiterList = new ArrayList<WaiterListItem>();
    private int MAX_WAITERS = 4;
    
    private JButton addWaiter = new JButton("Add Waiter");
    
    private JTextField nameField = new JTextField();//text box for name input

    private RestaurantPanel restPanel;
    private String type;
    
    private class WaiterListItem extends JPanel implements ActionListener{
    	private JButton name;
    	private String storeName;
    	private	JButton isFatigued;
    	private WaiterGui wGui;
    	
	    WaiterListItem(String n){
	    	setLayout(new FlowLayout());
	    	name = new JButton(n);
	    	storeName = n;
	    	isFatigued = new JButton("Tired?");
	    	isFatigued.addActionListener(this);
	    	add(isFatigued);
	    	add(name);
	    	
	    	wGui = restPanel.addWaiter(type, storeName);
	    }
	    
	    public void actionPerformed(ActionEvent e){
	    	if(isFatigued.getText().equals("Tired?")){
	    		isFatigued.setText("Rested?");
	    		isFatigued.setSelected(false);
	    		wGui.setFatigued();
	    	}
	    	else if(isFatigued.getText().equals("Rested?")){
	    		isFatigued.setText("Tired?");
	    		isFatigued.setSelected(false);
	    		wGui.offBreak();
	    	}
	    } 
    }

    /**
     * Constructor for WaiterPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    
    
    public WaiterPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameField.getPreferredSize().height));//add the name field to the GUI and size it properly
        add(nameField);

        addWaiter.addActionListener(this);
        add(addWaiter);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addWaiter){
        	addPerson(nameField.getText());
        	nameField.setText("");
        }
        if(waiterList.size() == MAX_WAITERS){
        	addWaiter.setEnabled(false);
        }
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addPerson(String name) {
        if (name != null) {
        	WaiterListItem waiter = new WaiterListItem(name);
            waiterList.add(waiter);
            view.add(waiter);
            validate();
        }
    }
    public void denyBreak(WaiterAgent w){
    	for(WaiterListItem temp:waiterList){
    		if(temp.wGui != null && temp.wGui.getAgent() == w){
    			temp.isFatigued.setText("Tired?");
    		}
    	}
    }
}