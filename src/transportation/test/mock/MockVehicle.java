package transportation.test.mock;

import java.util.List;

import CommonSimpleClasses.DirectionEnum;
import mock.Mock;
import transportation.CornerAgent.MyCorner;
import transportation.interfaces.Corner;
import transportation.interfaces.Vehicle;

public class MockVehicle extends Mock implements Vehicle {

	public MockVehicle(String name) {
		super(name);
	}

	@Override
	public void msgMyAdjCorners(List<MyCorner> cList) {
		// TODO Auto-generated method stub

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

}
