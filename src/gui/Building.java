package gui;

import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.XYPos;
import agent.RoleFactory;

/**
 * Every building must have a class that represents
 * that building. For example, Anthony will program
 * a Market class. This class must extend Building.
 *
 *Your building subclass must:
 *      Implement its interfaces and abstract methods correctly
 *      Implement the MarketBuyer interface Anthony will code if 
 *      		they need to buy something from the market
 *      Implement methods to buy things if it's the Market class
 *      Instantiate the corresponding JPanel that gets your
 *      	building up and running
 *      
 */
public abstract class Building extends Rectangle2D.Double
						implements CityBuilding, RoleFactory {
	String name;
	JPanel animationPanel;	
	LocationTypeEnum type;
	JPanel controls;
	
	public Building(int x, int y, int width, int height) {
		super( x, y, width, height );
	}
	
	public void setName(String n){
		name = n;
	}
	
	public String getName(){
		return name;
	}
	
	public XYPos position() {
		return new XYPos((int)getX(), (int)getY());
	}
	
	public abstract JPanel getAnimationPanel();
	
	
}
