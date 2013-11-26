package transportation.test.mock;

import java.util.List;

import CommonSimpleClasses.DirectionEnum;
import transportation.CornerAgent.MyCorner;
import transportation.interfaces.Bus;
import transportation.interfaces.Busstop;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;
import mock.EventLog;
import mock.Mock;

public class MockBus extends Mock implements Bus {
	
	public EventLog log = new EventLog();

	public MockBus(String name) {
		super(name);
	}

	@Override
	public void msgArrivedAtCorner(Corner c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgDriveNow() {
		// TODO Auto-generated method stub

	}

	@Override
	public DirectionEnum currentDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startVehicle() {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgMyAdjCorners(List<MyCorner> cList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgMyBusStop(List<Busstop> bsList) {
		log.add("Received bsList of size " + bsList.size());
	}

	@Override
	public void msgHereArePeople(List<Passenger> people) {
		log.add("Received list people of size " + people.size());
	}

	@Override
	public void msgPayingFare(double fare) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgExiting(Passenger p) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean orientation() {
		// TODO Auto-generated method stub
		return false;
	}

}
