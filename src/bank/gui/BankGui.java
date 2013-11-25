package bank.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import CommonSimpleClasses.CityLocation;
import agent.PersonAgent;
import agent.WorkRole;
import agent.gui.AnimationPanel;
import bank.AccountManagerRole;
import bank.BankCustomerRole;
import bank.LoanManagerRole;
import bank.SecurityGuardRole;
import bank.TellerRole;
//import restaurant.CustomerAgent;
//import restaurant.WaiterAgent;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class BankGui extends JPanel implements ActionListener {

	AnimationPanel animationPanel = new AnimationPanel();
	JFrame optionFrame;// = new JFrame();

    private JLabel infoLabel; //part of infoPanel
    private JPanel graphicPanel; //added for Lab1
    private JLabel graphicLabel;
    private JLabel graphicImage;
    private JCheckBox stateCB;//part of infoLabel
    private JCheckBox breakCB;
    private GridLayout optionGridLayout;
    
    //OPTION FRAME STUFF
    private JButton addCustomerButton;
    private JButton addTellerButton;
    private JButton endWorkDayButton;
    private JButton resumeWorkButton;
    private JTextField text1;
    private JTextField text2;

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    private int testAccountId = 2000;
    
    CityLocation bank = new BankBuilding(0, 0, 0, 0);
    
    List<WorkRole> workRoles = new ArrayList<WorkRole>();
    
    private TellerRole teller;
    private AccountManagerRole accountManager;
    private SecurityGuardRole securityGuard;
    private LoanManagerRole loanManager;
    
    private TellerGui tellerGui;
    private AccountManagerGui accountManagerGui;
    private LoanManagerGui loanManagerGui;
    private SecurityGuardGui securityGuardGui;
    
    private LayoutGui layoutGui;
    
    GridLayout layout = new GridLayout(1,1);
    
    int tellerDesk = 0;
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public BankGui() {
    	
    	
        int WINDOWX = 600;
        int WINDOWY = 490;
        
        layoutGui = new LayoutGui();
        animationPanel.addGui(layoutGui);
        
        resumeWorkButton = new JButton("resume work day, bitch");
        endWorkDayButton = new JButton("end work day");
        resumeWorkButton.addActionListener(this);
        endWorkDayButton.addActionListener(this);
        optionFrame = new JFrame();
        addCustomerButton = new JButton("add customer");
        addTellerButton = new JButton("add teller");
        text1 = new JTextField();
        text2 = new JTextField();
        optionGridLayout = new GridLayout(3, 2);
        optionFrame.setLayout(optionGridLayout);
       
        addCustomerButton.addActionListener(this);
        addTellerButton.addActionListener(this);
        optionFrame.add(addCustomerButton);
        optionFrame.add(text1);
        optionFrame.add(addTellerButton);
        optionFrame.add(text2);
        optionFrame.add(endWorkDayButton);
        optionFrame.add(resumeWorkButton);
        
//        animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
//        animationFrame.setVisible(true);
//    	animationFrame.add(animationPanel); 
//    	
        
        addCustomerButton.setVisible(true);
       
        optionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        optionFrame.setBounds(WINDOWX, 50, WINDOWX, WINDOWY);
        optionFrame.setVisible(true);
        
        optionFrame.add(addCustomerButton);
    	setBounds(50, 50, WINDOWX, WINDOWY);

   

        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .3));

        PersonAgent accountManagerPerson = new PersonAgent("accountManager");
        PersonAgent loanManagerPerson = new PersonAgent("loanManagement");
        accountManager = new AccountManagerRole(accountManagerPerson, bank);
        accountManagerGui = new AccountManagerGui(accountManager);
        animationPanel.addGui(accountManagerGui);
        accountManager.setGui(accountManagerGui);
        loanManager = new LoanManagerRole(loanManagerPerson, bank);
        accountManagerPerson.addRole(accountManager);
        accountManager.activate();
        accountManagerPerson.startThread();
        
        loanManagerGui = new LoanManagerGui(loanManager);
        animationPanel.addGui(loanManagerGui);
        loanManager.setGui(loanManagerGui);
        loanManagerPerson.addRole(loanManager);
        loanManager.activate();
        loanManagerPerson.startThread();
        
        PersonAgent securityGuardPerson = new PersonAgent("securityguard");
        securityGuardPerson.startThread();
        securityGuard = new SecurityGuardRole(securityGuardPerson, bank);
        
        

        securityGuardGui = new SecurityGuardGui(securityGuard);
        securityGuard.setGui(securityGuardGui);
        animationPanel.addGui(securityGuardGui);
        securityGuardPerson.addRole(securityGuard);
        securityGuard.activate();
        
        securityGuard.addRole(loanManager);
        securityGuard.addRole(accountManager);
        
        workRoles.add(securityGuard);
        workRoles.add(loanManager);
        workRoles.add(accountManager);
        
        Dimension graphicDim = new Dimension(WINDOWX, (int) (WINDOWY * .30));
//       animationPanel.setBackground(Color.RED);
       this.setLayout(layout);
        this.add(animationPanel);//animation panel added here FIXIT

    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */

    public void showCB() {
    	stateCB.setVisible(true);
    }

    /**
     * Main routine to get gui started
     */
 
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == addCustomerButton) {
			System.out.println(text1.getText() + text2.getText());
			addCustomer(text1.getText());
		}
		if(e.getSource() == addTellerButton) {
			System.out.println("add teller");
			addTeller(text2.getText());
		}
		if(e.getSource() == endWorkDayButton) {
			System.out.println("ending work day");
			securityGuard.msgLeaveWork();
			
		}
		if(e.getSource() == resumeWorkButton) {
			System.out.println("resuming work day");
//			securityGuard.resumeWorkDay();
		}
		
	}
	
	private void addTeller(String name) {
        PersonAgent tellerPerson2 = new PersonAgent(name);
        tellerPerson2.startThread();
        TellerRole teller2 = new TellerRole(tellerPerson2, bank);
        teller2.setAccountManager(accountManager);
        teller2.setLoanManager(loanManager);
        teller2.setDeskPosition(tellerDesk);
        TellerGui tellerGui2 = new TellerGui(teller2);
        teller2.setGui(tellerGui2);
        animationPanel.addGui(tellerGui2);
        tellerPerson2.addRole(teller2);
        teller2.activate();
        securityGuard.addTeller(teller2, tellerDesk);
        teller2.doGoToWorkstation();
        teller2.setSecurityGuard(securityGuard);
        workRoles.add(teller2);
        securityGuard.addRole(teller2);
        tellerDesk++;
	}
	
	private void addCustomer(String name) {
		testAccountId++;
		if(name.equals("hasAccount")) {
	        PersonAgent bankCustomerPerson = new PersonAgent(name);
	        bankCustomerPerson.startThread();
	        BankCustomerRole bankCustomer = new BankCustomerRole(bankCustomerPerson, bank, testAccountId, name);
	       
	        accountManager.hackAddAccount(bankCustomer, 200, testAccountId);
	        
	        bankCustomer.addTeller(teller);
	        bankCustomerPerson.addRole(bankCustomer);
	        bankCustomer.activate();
	        BankCustomerGui bankCustomergui = new BankCustomerGui(bankCustomer);
	        bankCustomer.setGui(bankCustomergui);
	        animationPanel.addGui(bankCustomergui);
	        bankCustomer.msgGoToSecurityGuard(securityGuard);
	        workRoles.add(bankCustomer);
	        return;
		}
		if(name.equals("withdraw")){
	        PersonAgent bankCustomerPerson = new PersonAgent(name);
	        bankCustomerPerson.startThread();
	        BankCustomerRole bankCustomer = new BankCustomerRole(bankCustomerPerson, bank, testAccountId, name);
	        
	        accountManager.hackAddAccount(bankCustomer, 300, testAccountId);
	        
	        bankCustomer.addTeller(teller);
	        bankCustomerPerson.addRole(bankCustomer);
	        bankCustomer.activate();
	        BankCustomerGui bankCustomergui = new BankCustomerGui(bankCustomer);
	        bankCustomer.setGui(bankCustomergui);
	        animationPanel.addGui(bankCustomergui);
	        bankCustomer.msgGoToSecurityGuard(securityGuard);
	        workRoles.add(bankCustomer);
	        return;
		}
		if(name.equals("loan")){
	        PersonAgent bankCustomerPerson = new PersonAgent(name);
	        bankCustomerPerson.startThread();
	        BankCustomerRole bankCustomer = new BankCustomerRole(bankCustomerPerson,bank, testAccountId, name);
	        
	        accountManager.hackAddAccount(bankCustomer, 0, testAccountId);

	        bankCustomer.addTeller(teller);
	        bankCustomerPerson.addRole(bankCustomer);
	        bankCustomer.activate();
	        BankCustomerGui bankCustomergui = new BankCustomerGui(bankCustomer);
	        bankCustomer.setGui(bankCustomergui);
	        animationPanel.addGui(bankCustomergui);
	        bankCustomer.msgGoToSecurityGuard(securityGuard);
	        workRoles.add(bankCustomer);
	        return;
		}
		if(name.equals("deposit")){
	        PersonAgent bankCustomerPerson = new PersonAgent(name);
	        bankCustomerPerson.startThread();
	        BankCustomerRole bankCustomer = new BankCustomerRole(bankCustomerPerson,bank, testAccountId, name);
	       
	        accountManager.hackAddAccount(bankCustomer, 0, testAccountId);

	        bankCustomer.addTeller(teller);
	        bankCustomerPerson.addRole(bankCustomer);
	        bankCustomer.activate();
	        BankCustomerGui bankCustomergui = new BankCustomerGui(bankCustomer);
	        bankCustomer.setGui(bankCustomergui);
	        animationPanel.addGui(bankCustomergui);
	        bankCustomer.msgGoToSecurityGuard(securityGuard);
	        workRoles.add(bankCustomer);
	        return;
		}
		else {//make a customer with no account
	        PersonAgent bankCustomerPerson = new PersonAgent(name);
	        bankCustomerPerson.startThread();
	        BankCustomerRole bankCustomer = new BankCustomerRole(bankCustomerPerson, bank);
	        bankCustomer.addTeller(teller);
	        bankCustomerPerson.addRole(bankCustomer);
	        bankCustomer.activate();
	        BankCustomerGui bankCustomergui = new BankCustomerGui(bankCustomer);
	        bankCustomer.setGui(bankCustomergui);
	        animationPanel.addGui(bankCustomergui);
	        bankCustomer.msgGoToSecurityGuard(securityGuard);
	        workRoles.add(bankCustomer);
	        return;
		}
		

	}
	

}
