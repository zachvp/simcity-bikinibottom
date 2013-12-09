package bank.gui;

import gui.Building;
import gui.StaffDisplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.TimeManager;
import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.WorkRole;
import agent.interfaces.Person;
import bank.AccountManagerRole;
import bank.BankCustomerRole;
import bank.LoanManagerRole;
import bank.RobberRole;
import bank.SecurityGuardRole;
import bank.TellerRole;
import bank.interfaces.SecurityGuard;
import bank.interfaces.Teller;

//creates animation panel and starts building
public class BankBuilding extends Building {

	//	CityBuilding cityBuilding;// = new CityBuilding();
	XYPos entrancePosition;// = new XYPos(300, 500);
	SecurityGuardRole securityGuardRole;// = new SecurityGuardRole(person);
	BankCustomerRole bankCustomerRole;
//	BankRoleFactory bankRoleFactory = new BankRoleFactory(this);
	InfoPanel infoPanel;
	StaffDisplay staff;
	
	Map<Person, BankCustomerRole> existingRoles;// = new HashMap<Person, bank.BankCustomerRole>();
	Map<Person, RobberRole> existingRobberRoles = new HashMap<Person, RobberRole>();
	private CityLocation bank;

	// Constants for staggering opening/closing time
	private static int instanceCount = 0;
	private static final int timeDifference = 12;

	//private AnimationPanel animationPanel = new AnimationPanel();
	BankGui bankGui;
	
	private SecurityGuardRole security;
	AccountManagerRole account;
	LoanManagerRole loan;
	List<TellerRole> tellers;
	
	int openHour, closeHour;

	public BankBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		staff = super.getStaffPanel();
		
		entrancePosition = new XYPos(width/2, height);
		bankGui = new BankGui();
		this.bank = this;
		this.existingRoles = new HashMap<Person, BankCustomerRole>();

		// Stagger opening/closing time
		this.timeOffset = (instanceCount * timeDifference) % 2;
		instanceCount++;
		
		infoPanel = new InfoPanel(this);
		initRoles();
		
		openHour = this.getOpeningHour();
		closeHour = this.getClosingHour();
		changeOpenSign(TimeManager.getInstance().isNowBetween(openHour, closeHour), openHour, closeHour);
	}

	private void initRoles() {
		// Create the roles
		setSecurity(new SecurityGuardRole(null, this));
		account = new AccountManagerRole(null, this);
		loan = new LoanManagerRole(null, this);
		TellerRole tell1 = new TellerRole(null, this);
		TellerRole tell2 = new TellerRole(null, this);
		TellerRole tell3 = new TellerRole(null, this);
		
		tellers = new ArrayList<TellerRole>();
		tellers.add(tell1);
		tellers.add(tell2);
		tellers.add(tell3);

		// Tell the security guard about the roles
		getSecurity().addRole(account);
		getSecurity().addRole(loan);
		getSecurity().addRole(tell1);
		getSecurity().addRole(tell2);
		getSecurity().addRole(tell3);

		// Give the tellers a position
		getSecurity().addTeller(tell1, 0);
		getSecurity().addTeller(tell2, 1);
		getSecurity().addTeller(tell3, 2);
		
		// Sets initial desk position for tellers, merp
		tell1.setDeskPosition(0);
		tell2.setDeskPosition(1);
		tell3.setDeskPosition(2);

		// Tell the tellers about the security guard
		tell1.setSecurityGuard(getSecurity());
		tell2.setSecurityGuard(getSecurity());
		tell3.setSecurityGuard(getSecurity());

		// Tell the tellers about the account manager
		tell1.setAccountManager(account);
		tell2.setAccountManager(account);
		tell3.setAccountManager(account);

		// Tell the tellers about the account manager
		tell1.setLoanManager(loan);
		tell2.setLoanManager(loan);
		tell3.setLoanManager(loan);

		//Create and set up the guis
		SecurityGuardGui sgGui = new SecurityGuardGui(getSecurity());
		AccountManagerGui accountGui = new AccountManagerGui(account);
		LoanManagerGui loanGui = new LoanManagerGui(loan);
		TellerGui tGui1 = new TellerGui(tell1);
		TellerGui tGui2 = new TellerGui(tell2);
		TellerGui tGui3 = new TellerGui(tell3);

		//set Guis
		getSecurity().setGui(sgGui);
		account.setGui(accountGui);
		loan.setGui(loanGui);
		loan.setAccountManager(account);
		tell1.setGui(tGui1);
		tell2.setGui(tGui2);
		tell3.setGui(tGui3);

		//add to animationpanel
		bankGui.getAnimationPanel().addGui(sgGui);
		bankGui.getAnimationPanel().addGui(accountGui);
		bankGui.getAnimationPanel().addGui(loanGui);
		bankGui.getAnimationPanel().addGui(tGui1);
		bankGui.getAnimationPanel().addGui(tGui2);
		bankGui.getAnimationPanel().addGui(tGui3);
		
		//add infopanel to account manager to update
		account.setInfoPanel(infoPanel);
		staff.addAllWorkRolesToStaffList();
	}

	@Override
	public XYPos entrancePos() {
		return entrancePosition;
	}

	@Override
	public Role getGreeter() {
		return securityGuardRole;
	}

	@Override
	public LocationTypeEnum type() {
		return LocationTypeEnum.Bank;
	}

	@Override
	public Role getCustomerRole(Person person) {
		
		BankCustomerRole role = existingRoles.get(person);
		if(role == null) {//they have not been to bank and need a customer role
			role = new BankCustomerRole(person, bank);
			BankCustomerGui bcg = new BankCustomerGui(role);
			role.setGui(bcg);
			bankGui.getAnimationPanel().addGui(bcg);
			role.setLocation(bank);
			existingRoles.put(person, role);
			person.addRole(role);
		}
		else {
//			role.setPerson(person);
		}
		role.msgGoToSecurityGuard(getSecurity());

		return role;
	}
	/**
	 * Gives person a robber role when they
	 * are told to rob bank in infoPanel
	 * @param person
	 * @return
	 */
	public Role getRobberRole(Person person) {
		
		RobberRole role = existingRobberRoles.get(person);
		if(role == null) {//they have not been to bank and need a customer role
			role = new RobberRole(person, bank);
			RobberGui rg = new RobberGui(role);
			role.setGui(rg);
			bankGui.getAnimationPanel().addGui(rg);
			role.setLocation(bank);
			existingRobberRoles.put(person, role);
			person.addRole(role);
		}
		else {
//			role.setPerson(person);
		}
		role.msgGoToSecurityGuard(getSecurity());

		return role;
	}
	/*
	 * method to set open sign on bank counter to TRUE when open or FALSE when closed
	 * called by SecurityGuard
	 */
	public void changeOpenSign(boolean b, int hour, int minute) {
		bankGui.getLayoutGui().setOpen(b, hour, minute);
	}

	@Override
	public JPanel getAnimationPanel() {
		return bankGui.getAnimationPanel();
	}

	@Override
	public JPanel getInfoPanel() {
		return infoPanel;
	}
	
	@Override
	public StaffDisplay getStaffPanel() {
		return staff;
	}

	public SecurityGuardRole getSecurity() {
		return security;
	}

	public void setSecurity(SecurityGuardRole security) {
		this.security = security;
	}
	
	@Override
	public boolean isOpen() {
		return securityGuardOnDuty() && accountManagerOnDuty()
				&& loanManagerOnDuty() && tellerOnDuty();
	}
	
	public boolean securityGuardOnDuty() {
		return security != null && security.isAtWork();
	}
	
	public boolean accountManagerOnDuty() {
		return account != null && account.isAtWork();
	}
	
	public boolean loanManagerOnDuty() {
		return loan != null && loan.isAtWork();
	}
	
	public double getMoneyInBank() {
		return account.getMoneyInBank();
	}
	
	public boolean tellerOnDuty() {
		for (TellerRole teller : tellers) {
			if (teller.isAtWork()) { return true; }
		}
		return false;
	}
	
	public int getNumberOfTellers() {
		return tellers.size();
	}

}