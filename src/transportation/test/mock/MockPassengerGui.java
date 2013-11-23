package transportation.test.mock;

import java.awt.Graphics2D;

import agent.mock.EventLog;

import CommonSimpleClasses.CityLocation;
import transportation.gui.interfaces.PassengerGui;
import transportation.interfaces.Bus;

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
	public void doExitVehicle() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bringOutCar() {
		// TODO Auto-generated method stub

	}

}
