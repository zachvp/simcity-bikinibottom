package bank.gui;

import javax.swing.JPanel;

import bank.BankCustomerRole;
import bank.BankRoleFactory;
import bank.SecurityGuardRole;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.interfaces.Person;
import gui.Building;
import CommonSimpleClasses.XYPos;

//creates animation panel and starts building
public class BankBuilding extends Building {

//	CityBuilding cityBuilding;// = new CityBuilding();
	XYPos entrancePosition = new XYPos(300, 500);
	SecurityGuardRole securityGuardRole;
	BankCustomerRole bankCustomerRole;
	BankRoleFactory bankRoleFactory = new BankRoleFactory();
	
	private AnimationPanel animationPanel = new AnimationPanel();;
	
	public BankBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
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
		return bankRoleFactory.getCustomerRole(person);
	}

	@Override
	public JPanel getAnimationPanel() {
		return animationPanel;
	}

	@Override
	public JPanel getControlPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}