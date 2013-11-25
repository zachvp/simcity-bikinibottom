package bank.gui;

import gui.Building;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.TimeManager;
import agent.interfaces.Person;
import bank.AccountManagerRole;
import bank.BankCustomerRole;
import bank.LoanManagerRole;
import bank.SecurityGuardRole;
import bank.TellerRole;

//creates animation panel and starts building
public class BankBuilding extends Building {

//	CityBuilding cityBuilding;// = new CityBuilding();
	XYPos entrancePosition;// = new XYPos(300, 500);
	SecurityGuardRole securityGuardRole;// = new SecurityGuardRole(person);
	BankCustomerRole bankCustomerRole;
//	BankRoleFactory bankRoleFactory = new BankRoleFactory(this);
	
	
	Map<Person, BankCustomerRole> existingRoles;// = new HashMap<Person, bank.BankCustomerRole>();
	private CityLocation bank;
	
	int startHour = 9;
	int startMinute = 0;
	int endHour = 16;
	int endMinute = 30;
	
	//private AnimationPanel animationPanel = new AnimationPanel();
	BankGui bankGui;
	
	public BankBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		entrancePosition = new XYPos(width/2, height);
		bankGui = new BankGui();
		this.bank = this;
		this.existingRoles = new HashMap<Person, BankCustomerRole>();
		// TODO Auto-generated constructor stub
		initRoles();
	}
	
	private void initRoles() {
		// Create the roles
		SecurityGuardRole security = new SecurityGuardRole(null, this);
		AccountManagerRole account = new AccountManagerRole(null, this);
		LoanManagerRole loan = new LoanManagerRole(null, this);
		TellerRole tell1 = new TellerRole(null, this);
		TellerRole tell2 = new TellerRole(null, this);
		TellerRole tell3 = new TellerRole(null, this);
		
		// Tell the security guard about the roles
		security.addRole(account);
		security.addRole(loan);
		security.addRole(tell1);
		security.addRole(tell2);
		security.addRole(tell3);
		
		// Give the tellers a position
		security.addTeller(tell1, 1);
		security.addTeller(tell2, 2);
		security.addTeller(tell3, 3);
		
		// Tell the tellers about the security guard
		tell1.setSecurityGuard(security);
		tell2.setSecurityGuard(security);
		tell3.setSecurityGuard(security);
		
		// Tell the tellers about the account manager
		tell1.setAccountManager(account);
		tell2.setAccountManager(account);
		tell3.setAccountManager(account);
		
		// Tell the tellers about the account manager
		tell1.setLoanManager(loan);
		tell2.setLoanManager(loan);
		tell3.setLoanManager(loan);
		
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
		if(role == null) {
			role = new BankCustomerRole(person, bank);
			BankCustomerGui bcg = new BankCustomerGui(role);
			role.setGui(bcg);
			bankGui.getAnimationPanel().addGui(bcg);
			role.setLocation(bank);
		}
		else {
			role.setPerson(person);
		}
		person.addRole(role);
		return role;
	}

	@Override
	public JPanel getAnimationPanel() {
		return bankGui.getAnimationPanel();
	}

	@Override
	public JPanel getInfoPanel() {
		return new JPanel();
	}
	
	public boolean isOpen() {
		return TimeManager.getInstance().isNowBetween(startHour, startMinute, endHour, endMinute);
	
	}
	
	
}