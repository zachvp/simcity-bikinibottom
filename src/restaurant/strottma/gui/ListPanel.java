package restaurant.strottma.gui;

import restaurant.strottma.CustomerRole;
import restaurant.strottma.HostRole;
import restaurant.strottma.WaiterRole;

import javax.swing.*;

import agent.Agent;
import agent.Role;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<ListRowPanel> list = new ArrayList<ListRowPanel>();
    private JTextField addPersonT;
    private static final int TEXT_DIM_WIDTH = Integer.MAX_VALUE;
    private JButton addPersonB = new JButton("Add");

    private RestaurantPanel restPanel;
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));
        
       // if (type.equals("Customers")) {
       // 	addPersonT  = new JTextField("Customer");
       // } else if (type.equals("Waiters")) {
       // 	addPersonT = new JTextField("Waiter");
       // } else {
        	addPersonT = new JTextField("Name");
       // }
        
        Dimension textDim = addPersonT.getPreferredSize();
        textDim.setSize(TEXT_DIM_WIDTH, textDim.getHeight());
        addPersonT.setMaximumSize(textDim);
        add(addPersonT);
        
        addPersonB.addActionListener(this);
        add(addPersonB);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
        	
        	//TODO
        	/*ListRowPanel row = addPerson(restPanel.addPerson(type, addPersonT.getText()));
        	if (addPersonT.getText().equals("Hungry") && type.equals("Customers")) {
        		row.checkbox.setEnabled(false);
        		row.checkbox.setSelected(true);
        		((CustomerRole) row.person).getGui().setHungry();
        	}
        	
           // addPerson(JOptionPane.showInputDialog("Please enter a name:"));
        } else {
        	// Isn't the second for loop more beautiful?
            /*for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);*/
        	for (ListRowPanel temp:list){
                if (e.getSource() == temp.checkbox && type.equals("Customers")) {
                    CustomerRole c = (CustomerRole) temp.person;
                    if (c == null) {
                        // should never happen
                        System.out.println("ERROR: agent not found");
                    }
                    temp.checkbox.setEnabled(false);
                    c.getGui().setHungry();
                    
                } else if (e.getSource() == temp.breakButton && type.equals("Waiters")) {
                    WaiterRole w = (WaiterRole) temp.person;
                    if (w == null) {
                        // should never happen
                        System.out.println("ERROR: agent not found");
                    }
                    
                    if (w.isOnBreak()) {
                    	w.getGui().setEndBreak();
                    	temp.breakButton.setText("Request break");
                    } else {
                        w.getGui().setFatigued();
                        temp.breakButton.setText("End break");
                    }
                } 
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
    public ListRowPanel addPerson(Role person) {
        if (person != null) {
           // JButton button = new JButton(name);
           // button.setBackground(Color.white);
        	ListRowPanel row = new ListRowPanel(person);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    (int) (paneSize.height / 6));
            row.setPreferredSize(buttonSize);
            row.setMinimumSize(buttonSize);
            // row.setMaximumSize(buttonSize);
           // row.button.addActionListener(this);
            list.add(row);
            view.add(row);
            if (type.equals("Customers")) { row.checkbox.addActionListener(this); }
            if (type.equals("Waiters")) { row.breakButton.addActionListener(this); }
           // restPanel.addPerson(type, person.getName());//puts agent on list
           // restPanel.showInfo(type, person.getName());//puts hungry button on panel
            validate();
            
            return row;
        }
        return null;
    }
    
    private class ListRowPanel extends JPanel {
    	JLabel nameLabel;
    	JCheckBox checkbox;
    	JLabel hungryLabel;
    	JButton breakButton;
    	Role person;
    	
    	ListRowPanel(Role person) {
    		
    		this.person = person;
    		
    		setLayout(new FlowLayout());
    		
    		if (type.equals("Customers")) {
    			checkbox = new JCheckBox();
        		checkbox.setEnabled(true);
        		checkbox.setSelected(false);
        		add(checkbox);
        		
        		hungryLabel = new JLabel("Hungry?");
        		add(hungryLabel);
    		}
    		
    		if (type.equals("Waiters")) {
    			breakButton = new JButton("Request break");
    			add(breakButton);
    		}
    		
    		nameLabel = new JLabel(person.getName());
    		add(nameLabel);

    	}
    }
    
    public void setAgentEnabled(Agent a) {
        for (ListRowPanel row : list) {
        	if (row.person.equals(a)) {
        		if (type.equals("Customers")) {
        			row.checkbox.setEnabled(true);
                	row.checkbox.setSelected(false);
        		}
            	if (type.equals("Waiters")) {
                	row.breakButton.setText("Request break");
            	}
        	}
        }
    }
    public void setRoleEnabled(Role r) {
        for (ListRowPanel row : list) {
        	if (row.person.equals(r)) {
        		if (type.equals("Customers")) {
        			row.checkbox.setEnabled(true);
                	row.checkbox.setSelected(false);
        		}
            	if (type.equals("Waiters")) {
                	row.breakButton.setText("Request break");
            	}
        	}
        }
    }
}
