package gui.test;

import java.awt.Color;

import javax.swing.JPanel;

import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.interfaces.Person;
import gui.Building;

public class MockBuilding extends Building{
	
	JPanel animationPanel = new JPanel();
	LocationTypeEnum type = LocationTypeEnum.None;
	JPanel info;

	public MockBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		animationPanel.setBackground(Color.blue);
	}

	@Override
	public XYPos entrancePos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role getGreeter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocationTypeEnum type() {
		return type;
	}

	@Override
	public Role getCustomerRole(Person person) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getAnimationPanel() {
		return animationPanel;
	}

	@Override
	public JPanel getInfoPanel() {
		// TODO Auto-generated method stub
		return new JPanel();
	}



}
