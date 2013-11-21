package bank.gui;


import javax.swing.*;



import bank.BankCustomerRole;




import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel implements ActionListener {

    //Host, cook, waiters and customers
	private JButton pauseButton = new JButton("Pause");//added for v2

    private JPanel restLabel = new JPanel();
 private JPanel group = new JPanel();

    private BankGui gui; //reference to main gui
    
//    BankCustomerRole bankCustomer;
//    BankCustomerGui bankCustomerGui = new BankCustomerGui(bankCustomer);

    private int waiterIdlePosition = 1;
    
    int customerWaitPosition = 1;
    
    boolean callPause = true;//calls pause/ changes text to "restart" on pauseButton

    public RestaurantPanel(BankGui gui) {

    	
        this.gui = gui;
        
        
//        BankCustomerRole bankCustomer = new BankCustomerRole("Jack");
//        
//        BankCustomerGui bankCustomergui = new BankCustomerGui(bankCustomer);
//        bankCustomer.setGui(bankCustomergui);
//        gui.animationPanel.addGui(bankCustomergui);
//        bankCustomer.startThread();
//        bankCustomer.setGui(bankCustomerGui);
//        waiter.setGui(waiterGui);

//        host.addWaiter(waiter);
        

//        gui.animationPanel.addGui(waiterGui);

        
//        add(pauseButton);
//        pauseButton.addActionListener(this);//FIXIT
//        

        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));



        initRestLabel();
        add(restLabel);
        add(group);
    }
    

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());              
        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
//    public void showInfo(String type, String name) {
////    	System.out.println("Freakin " + name);
//    	if (type.equals("Customers")) {
////    		System.out.println("DONT");
//    		for (int i = 0; i < customers.size(); i++) {
//    			CustomerAgent temp = customers.get(i);
//    			if (temp.getName() == name)
//    				gui.updateInfoPanel(temp);
//    		}
//    	}
//        if (type.equals("Waiters")) {
////        	System.out.println("MERP2");
//        	System.out.println("S " + waiters.size());//ZEROZ
//        	for (int i = 0; i < waiters.size(); i++) {
//
//        		WaiterAgent temp = waiters.get(i);
////        		System.out.println("HMM" + temp.getName());
//        		if (temp.getName()==name) {
//        			System.out.println(name);
//        			gui.updateInfoPanel(temp);
//        		}
//        	}
//        }     
//    }
    
    public void showCB() {
//    	gui.showCB();
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
//    public void addPerson(String type, String name, boolean isHungry) {
//
//    	if (type.equals("Customers")) {
//    		CustomerAgent c = new CustomerAgent(name);	
//    		CustomerGui g = new CustomerGui(c, gui);
//    		
//    		gui.animationPanel.addGui(g);// dw
//    		c.setHost(host);
//    		c.setGui(g);
////    		c.setWaiter(waiter);
//    		if(isHungry) {
//        		c.getGui().setHungry(customerWaitPosition);
//        		customerWaitPosition++;
//        		if(customerWaitPosition > 8) {
//        			customerWaitPosition = 1;
//        		}
//    		}
//    		customers.add(c);
//    		c.startThread();
//
//    		
//    	}
//    	
////    	if (type.equals("Waiters")) {
////    		System.out.println("HM1M");
////    		WaiterAgent w = new WaiterAgent(name, host, cook);
////    		waiters.add(w);
////    	}
//    }
    
//    public void addWaiter(String type, String name) {//called from waiterListPanel, adds waiter to gui
//
//    	if (type.equals("Waiters")) {//TODO add idle to constructor
////    		System.out.println("LOOKY");
//    		WaiterAgent w = new WaiterAgent(name, host, cook, cashier, waiterIdlePosition);
//    		waiterIdlePosition++;
//    		waiters.add(w);
//    		WaiterGUI g = new WaiterGUI(w);
//    		w.setGui(g);
//    		gui.animationPanel.addGui(g);
//    		w.startThread();
//    		host.addWaiter(w);
//
//    	}
//    		
//    	
//    }
    
//    public void putWaiterOnBreak(String waiterName) {
//    	host.setWaiterBreak(waiterName);
//    }

    public void actionPerformed(ActionEvent e) {
//    	System.out.println("pausing");
//    		if(e.getSource()==pauseButton && callPause)
//    		{
//    			System.out.println("pausing");
//    			host.pause();
//    			host.pauseAll();
////    			waiter.pause();
////    			waiter.pauseAllCustomers();
//    			cook.pause();
//    			pauseButton.setText("Restart");
//    			callPause = false;
//    		}
//    		else if(!callPause) {
//    			System.out.println("restarting");
//    			host.restart();
//    			host.restartAll();
////    			waiter.restart();
////    			waiter.restartAllCustomers();
//    			cook.restart();
//    			pauseButton.setText("Pause");;
//    			callPause = true;
//    		}

        }
    
    public void incrementCustomerWaitPosition() {
    	customerWaitPosition++;
    }
    public void setCustomerWaitPosition(int num) {
    	customerWaitPosition = num;
    }

}
