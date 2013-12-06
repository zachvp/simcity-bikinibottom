package gui.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import CommonSimpleClasses.XYPos;
import agent.Role;
import agent.interfaces.Person;
import gui.Building;

public class MockBuilding extends Building{
	
	BufferedImage image;
	ImageIcon icon;
	JPanel animationPanel = new JPanel();
	LocationTypeEnum type = LocationTypeEnum.None;
	JPanel info;

	public MockBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		//animationPanel.setBackground(Color.blue);
		
		try {
			image = ImageIO.read(getClass().getResource("welcomeCard.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		icon = new ImageIcon(image);
		JLabel background = new JLabel(icon);
		animationPanel.setLayout(new BorderLayout());
		animationPanel.add(background, BorderLayout.CENTER);
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

	@Override
	public int getOpeningHour() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getOpeningMinute() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getClosingHour() {
		// TODO Auto-generated method stub
		return 23;
	}

	@Override
	public int getClosingMinute() {
		// TODO Auto-generated method stub
		return 59;
	}



}
