package restaurant.vdea.gui;

import javax.swing.*;

import restaurant.vdea.CustomerRole;
import restaurant.vdea.WaiterRole;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	//JFrame animationFrame = new JFrame("Restaurant Animation");
	AnimationPanel animationPanel = new AnimationPanel();
	
	private JPanel halfPanel = new JPanel();
	
    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel
    private JButton pauseButton;

    private Object currentPerson;/* Holds the Role that the info is about.
    								Seems like a hack */
    
    boolean paused = false;

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {
        int WINDOWX = 1200;
        int WINDOWY = 490;

       // animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
        //animationFrame.setVisible(true);
    	//animationFrame.add(animationPanel); 
        setBounds(50, 50, WINDOWX, WINDOWY);

        setLayout(new BorderLayout(5,10));
        
        Dimension halfDim = new Dimension((int)(WINDOWX * .5), (int) (WINDOWY * .9));
        halfPanel.setPreferredSize(halfDim);
        halfPanel.setMinimumSize(halfDim);
        halfPanel.setMaximumSize(halfDim);
        halfPanel.setLayout(new BorderLayout());
        

        Dimension restDim = new Dimension((int)(WINDOWX * .5), (int) (WINDOWY * .6));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        halfPanel.add(restPanel, BorderLayout.WEST);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension((int)(WINDOWX * .5), (int) (WINDOWY * .25));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
        halfPanel.add(infoPanel, BorderLayout.SOUTH);
        
        //customer hungry checkbox, later use for waiter breaks
        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);
        
        
        

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(this);
        infoPanel.add(pauseButton);
        
        add(halfPanel, BorderLayout.WEST);
        
        
        Dimension animDim = new Dimension((int)(WINDOWX * .5), (int) (WINDOWY * .6));
        animationPanel.setPreferredSize(animDim);
        animationPanel.setMinimumSize(animDim);
        animationPanel.setMaximumSize(animDim);
        animationPanel.setBorder(BorderFactory.createTitledBorder("Restaurant Animation"));
        add(animationPanel, BorderLayout.EAST);
        
        
        //Lab 1
        /*
        JPanel me = new JPanel();
        me.setLayout(new FlowLayout());
        me.setPreferredSize(restDim);
        me.setMinimumSize(restDim);
        me.setMaximumSize(restDim);
        
        JLabel mLabel = new JLabel();
        mLabel.setText("Victoria Dea");
        me.add(mLabel);
        
        ImageIcon image = new ImageIcon("C://Users//Victoria Dea//restaurant_vdea//images.jpg");
        JLabel imageL = new JLabel(image);
        
       // imageL.setSize(new Dimension(10, 10));
       
     
        me.add(imageL);
        
       
        add(me);*/
    }
    
    
    public void makeHungry(boolean hungry){
    	if (currentPerson instanceof CustomerRole) {
            CustomerRole c = (CustomerRole) currentPerson;
            c.getGui().setHungry();
            stateCB.setEnabled(false);
        }
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
        
        if(person instanceof WaiterRole){
        	WaiterRole waiter = (WaiterRole) person;
        	infoLabel.setText("<html><pre>     Name: " + waiter.getName() + " </pre></html>");
        	stateCB.setText("Go on Break");
        	stateCB.setSelected(waiter.getGui().isOnBreak());
            stateCB.setEnabled(true);
        	
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
            if (currentPerson instanceof WaiterRole){
            	WaiterRole w = (WaiterRole) currentPerson;
            	
            	if (stateCB.isSelected()){
            		System.out.println("checkbox clicked");            		
            		boolean canGoOnBreak = restPanel.getHost().allowBreak();
            		if(canGoOnBreak){
            			//stateCB.setEnabled(false);
            			System.out.println("allowed to go on break");
            			w.getGui().setOnBreak();
            		}
            		else{
            			stateCB.setSelected(false);
            			System.out.println("There are not enough waiters. You cannot go on break!");
            		}
            	}
            	else{
            		System.out.println("Coming off break");
            		w.getGui().setOffBreak();
            	}
            		
            }
            
            //TODO waiter on break
        }
        
        if (e.getSource() == pauseButton){
        	if (paused){
        		paused = false;	//add change button text to restart===================
        		pauseButton.setText("Pause");
        		restPanel.restartRoles();
        		animationPanel.restartAnimation();
        		
        	}
        	else
        	{
        		paused = true;
        		pauseButton.setText("Restart");
        		restPanel.pauseRoles();
        		animationPanel.pauseAnimation();
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
