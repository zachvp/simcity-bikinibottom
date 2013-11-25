package transportation.test.mock;

import java.awt.Graphics2D;

import agent.mock.EventLog;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.XYPos;
import transportation.gui.interfaces.PassengerGui;
import transportation.interfaces.Bus;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;

public class MockPassengerGui implements PassengerGui {
	
	public EventLog log = new EventLog();

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void doWalkTo(CityLocation cityLocation) {
		log.add("Walking to " + cityLocation);

	}

	@Override
	public void doGetInBus(Bus b) {
		// TODO Auto-generated method stub

	}


	@Override
	public void doBringOutCar() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSetLocation(CityLocation loc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doExitVehicle(CityLocation loc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public XYPos getPos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doExitBus(Corner location, boolean orientation) {
		// TODO Auto-generated method stub
		
	}

}
