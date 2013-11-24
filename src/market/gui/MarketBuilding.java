package market.gui;

import javax.swing.JPanel;

import market.MarketRoleFactory;
import CommonSimpleClasses.XYPos;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.Role;
import agent.interfaces.Person;




public class MarketBuilding extends gui.Building {

	String name;
	JPanel animationPanel = new market.gui.AnimationPanel();	
	LocationTypeEnum type;
	JPanel controls;
	MarketRoleFactory marketroleFactory = new MarketRoleFactory();
	
	public MarketBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public XYPos entrancePos() {
		// TODO Auto-generated method stub
			//Pass down proper entrance position
		return (new XYPos(0,0));
	}

	@Override
	public Role getGreeter() {
		// TODO Auto-generated method stub
			//Pass Cashier
		return null;
	}

	@Override
	public LocationTypeEnum type() {
		// TODO Auto-generated method stub
		return LocationTypeEnum.Market;
	}

	@Override
	public Role getCustomerRole(Person person) {
		// TODO Auto-generated method stub
		return marketroleFactory.getCustomerRole(person);
	}

	@Override
	public JPanel getAnimationPanel() {
		// TODO Auto-generated method stub
		return animationPanel;
	}

	@Override
	public JPanel getControlPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	
}