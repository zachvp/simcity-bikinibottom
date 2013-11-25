package bank.gui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import bank.BankCustomerRole;
import bank.BankRoleFactory;
import bank.SecurityGuardRole;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.interfaces.Person;
import gui.Building;
import CommonSimpleClasses.XYPos;

//creates animation panel and starts building
public class BankBuilding extends Building {

//	CityBuilding cityBuilding;// = new CityBuilding();
	XYPos entrancePosition = new XYPos(300, 500);
	SecurityGuardRole securityGuardRole;// = new SecurityGuardRole(person);
	BankCustomerRole bankCustomerRole;
//	BankRoleFactory bankRoleFactory = new BankRoleFactory(this);
	
	JPanel infoPanel = new JPanel();
	
	Map<Person, Role> existingRoles;// = new HashMap<Person, bank.BankCustomerRole>();
	private CityLocation bank;
	
	private AnimationPanel animationPanel = new AnimationPanel();
	
	public BankBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
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
			role = new BankCustomerRole(person);
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
		return animationPanel;
	}

	@Override
	public JPanel getInfoPanel() {
		infoPanel.setBackground(Color.BLUE);
		return infoPanel;
	}
	
	
}