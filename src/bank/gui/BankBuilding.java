package bank.gui;

import gui.Building;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.TimeManager;
import agent.gui.AnimationPanel;
import agent.interfaces.Person;
import bank.BankCustomerRole;
import bank.SecurityGuardRole;

//creates animation panel and starts building
public class BankBuilding extends Building {

//	CityBuilding cityBuilding;// = new CityBuilding();
	XYPos entrancePosition;// = new XYPos(300, 500);
	SecurityGuardRole securityGuardRole;// = new SecurityGuardRole(person);
	BankCustomerRole bankCustomerRole;
//	BankRoleFactory bankRoleFactory = new BankRoleFactory(this);
	
	
	Map<Person, Role> existingRoles;// = new HashMap<Person, bank.BankCustomerRole>();
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
		this.existingRoles = new HashMap<Person, Role>();
		// TODO Auto-generated constructor stub
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
		Role role = existingRoles.get(person);
		if(role == null) {
			role = new BankCustomerRole(person, bank);
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
		return bankGui;
	}

	@Override
	public JPanel getInfoPanel() {
		return new JPanel();
	}
	
	public boolean isOpen() {
		return TimeManager.getInstance().isNowBetween(startHour, startMinute, endHour, endMinute);
	
	}
	
	
}