package restaurant.vegaperk.gui;

import restaurant.CustomerAgent;
import restaurant.HostAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and waiters
 */

public class CustomerPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<CustomerListItem> cList = new ArrayList<CustomerListItem>();
    
    private FlowLayout AddButtons = new FlowLayout();
    private JButton addCustomer = new JButton("Add Customer");
    
    private JTextField nameField = new JTextField();//text box for name input
    private JButton pauseButton = new JButton("Pause!");

    private RestaurantPanel restPanel;
    private String type;

    private class CustomerListItem extends JPanel implements ActionListener{
    	private JButton nameButton;
    	private String name;
    	private	JCheckBox isHungry;
    	private CustomerGui cGui;
    	
	    CustomerListItem(String n){
	    	setLayout(new FlowLayout());
	    	nameButton = new JButton(n);
	    	name = n;
	    	isHungry = new JCheckBox("Hungry?");
	    	isHungry.addActionListener(this);
	    	add(isHungry);
	    	add(nameButton);
	    	
	    	cGui = restPanel.addCustomer(type, name);
	    }
	    
	    public void actionPerformed(ActionEvent e){
	    	if(isHungry.isEnabled()){
	    		cGui.setHungry();
	    		isHungry.setEnabled(false);
	    	}
	    } 
    }
    
    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    
    
    public CustomerPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameField.getPreferredSize().height));//add the name field to the GUI and size it properly
        add(nameField);

        addCustomer.addActionListener(this);
        addCustomer.setLayout(AddButtons);
        
        add(addCustomer);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
        
        pauseButton.addActionListener(this);
        add(pauseButton);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addCustomer) {
        	// Chapter 2.19 describes showInputDialog()
        	addPerson(nameField.getText());//adds a customer to the list
        	nameField.setText("");
        }
        else if(e.getSource() == pauseButton){
        	if(pauseButton.getText().equals("Pause!")){
        	restPanel.pauseAnimation();
        	pauseButton.setText("Resume!");
        	}
        	else if(pauseButton.getText().equals("Resume!")){
        		restPanel.resumeAnimation();
        		pauseButton.setText("Pause!");
        	}
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
            CustomerListItem customer = new CustomerListItem(name);
            cList.add(customer);
            view.add(customer);
            validate();
        }
    }
    public void setCustomerEnabled(CustomerAgent c){
    	for(CustomerListItem temp:cList){
    		if(temp.cGui != null && temp.cGui.getAgent() == c){
    			temp.isHungry.setSelected(false);
    			temp.isHungry.setEnabled(true);
    		}
    	}
    }
}