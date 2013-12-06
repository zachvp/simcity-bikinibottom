package transportation.test.mock;

import CommonSimpleClasses.DirectionEnum;
import CommonSimpleClasses.XYPos;
import mock.Mock;
import transportation.interfaces.Bus;
import transportation.interfaces.Busstop;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;

public class MockBusstop extends Mock implements Busstop {

	public MockBusstop(String name) {
		super(name);
	}

	@Override
	public LocationTypeEnum type() {
		return LocationTypeEnum.Busstop;
	}

	@Override
	public XYPos position() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectionEnum direction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgIAmHere(Passenger p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgGiveMePeople(Bus b) {
		// TODO Auto-generated method stub

	}

	@Override
	public Corner corner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startThread() {
		// TODO Auto-generated method stub

	}

}
