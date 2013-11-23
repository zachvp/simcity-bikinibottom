package restaurant.strottma.gui;

import restaurant.strottma.CustomerRole;

import javax.swing.*;

import agent.Agent;
import agent.Role;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    // constants for use in constructor
	private static final int MAIN_WINDOW_LEFT_BOUND = 50;
	private static final int MAIN_WINDOW_UPPER_BOUND = 50;
	private static final int MAIN_WINDOW_WIDTH = 1000;
	private static final int MAIN_WINDOW_HEIGHT = 600;
	private static final int ANIMATION_WINDOW_LEFT_BOUND = 100 + 
			MAIN_WINDOW_WIDTH;
	private static final int ANIMATION_WINDOW_UPPER_BOUND = 50;
	private static final int ANIMATION_WINDOW_WIDTH = 600;
	private static final int ANIMATION_WINDOW_HEIGHT = 600;
	private static final int REST_WIDTH = MAIN_WINDOW_WIDTH;
	private static final int REST_HEIGHT = (int) (MAIN_WINDOW_HEIGHT * .4);
	private static final int INFO_WIDTH = MAIN_WINDOW_WIDTH;
    private static final int INFO_HEIGHT = (int) (MAIN_WINDOW_HEIGHT * .25);
    private static final int INFO_ROWS = 1;
    private static final int INFO_COLUMNS = 2;
    private static final int INFO_HGAP = 30;
    private static final int INFO_VGAP = 0;
    private static final int ABOUT_ME_WIDTH = MAIN_WINDOW_WIDTH;
    private static final int ABOUT_ME_HEIGHT = (int) (MAIN_WINDOW_HEIGHT * .005);
	
	/* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	/* JFrame animationFrame = new JFrame("Restaurant Animation"); */
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(animationPanel);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    
    /* aboutMePanel gives a little info about @author Erik Strottmann */
    private JPanel aboutMePanel;
    private JLabel aboutMeLabel; // part of aboutMePanel
    private JButton pauseButton;

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
    	/*
    	animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(ANIMATION_WINDOW_LEFT_BOUND,
        		ANIMATION_WINDOW_UPPER_BOUND, ANIMATION_WINDOW_WIDTH,
        		ANIMATION_WINDOW_HEIGHT);
        animationFrame.setVisible(true);
    	animationFrame.add(animationPanel);
    	*/
    	
    	setBounds(MAIN_WINDOW_LEFT_BOUND, MAIN_WINDOW_UPPER_BOUND,
    			MAIN_WINDOW_WIDTH, MAIN_WINDOW_HEIGHT);
    	
        setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.Y_AXIS));

        Dimension restDim = new Dimension(REST_WIDTH, REST_HEIGHT);
       // restPanel.setPreferredSize(restDim);
       // restPanel.setMinimumSize(restDim);
       // restPanel.setMaximumSize(restDim);
       // add(restPanel);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(INFO_WIDTH, INFO_HEIGHT);
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);
        
        infoPanel.setLayout(new GridLayout(INFO_ROWS, INFO_COLUMNS, INFO_HGAP,
        		INFO_VGAP));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
//        add(infoPanel);
        
        this.add(animationPanel);

        
        // Set up the about me panel
        Dimension aboutMeDim = new Dimension(ABOUT_ME_WIDTH, ABOUT_ME_HEIGHT);
        aboutMePanel = new JPanel();
        aboutMePanel.setBorder(BorderFactory.createTitledBorder("About Me"));
        aboutMePanel.setLayout(new FlowLayout());
        aboutMePanel.setPreferredSize(aboutMeDim);
        aboutMePanel.setMinimumSize(aboutMeDim);
        
        aboutMeLabel = new JLabel();
        aboutMeLabel.setText("<html>This GUI has been modified by Erik "
        		+ "Strottmann.<br/>In progress! Further changes to come!</html>");
        
        ImageIcon icon = new ImageIcon("res/about-me-48.jpg");
        aboutMeLabel.setIcon(icon);
        
        pauseButton = new JButton("Pause!");
        pauseButton.addActionListener(this);
        
        aboutMePanel.add(aboutMeLabel);
        aboutMePanel.add(pauseButton);
        add(aboutMePanel);
        
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
            }
        } else if (e.getSource() == pauseButton) {
        	if (pauseButton.getText().equals("Resume!")) {
        		resumeAgents();
        	} else {
        		pauseAgents();
        	}
        }
    }
    
    private void resumeAgents() {
    	restPanel.resumeAgents();
    	pauseButton.setText("Pause!");
    }
    
    private void pauseAgents() {
    	restPanel.pauseAgents();
    	pauseButton.setText("Resume!");
    }
    
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the customer
     */
    public void setRoleEnabled(Role r) {
        restPanel.setRoleEnabled(r);
    	/*
    	if (currentPerson instanceof CustomerAgent) {
            CustomerAgent cust = (CustomerAgent) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
        */
    }
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
