package gui;

import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.TimeManager;
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
	JPanel info;
	StaffDisplay staff = new StaffDisplay();
	
	protected static final int defaultStartHour = 8;
	protected static final int defaultStartMinute = 0;
	protected static final int defaultEndHour = 22;
	protected static final int defaultEndMinute = 0;
	
	protected int timeOffset;
	
	public Building(int x, int y, int width, int height) {
		super( x, y, width, height );
		staff.setBuilding(this);
	}
	
	public void setName(String n){
		name = n;
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public XYPos position() {
		return new XYPos((int)getX(), (int)getY());
	}
	
	//Return your animation panel here
	public abstract JPanel getAnimationPanel();
	
	//Return your control panel here (null if you don't have one)
	public abstract JPanel getInfoPanel();
	
	//Return panel listing staff
	public StaffDisplay getStaffPanel(){
		return staff;
	}
	
	/** Whether the building is open right now. */
	public boolean isOpen() {
		return TimeManager.getInstance().isNowBetween(getOpeningHour(),
				getOpeningMinute(), getClosingHour(), getClosingMinute());
	}
	
	/** The hour of day this building opens. */
	public int getOpeningHour() {
		return (defaultStartHour + timeOffset) % 24;
	}
	
	/** The minute of day this building opens. */
	public int getOpeningMinute() {
		return defaultStartMinute;
	}
	
	/** The hour of day this building closes. */
	public int getClosingHour() {
		return (defaultEndHour + timeOffset) % 24;
	}
	
	/** The minute of day this building closes. */
	public int getClosingMinute() {
		return defaultEndMinute;
	}	
	
}
