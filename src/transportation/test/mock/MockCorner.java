package transportation.test.mock;

import java.util.List;

import transportation.CornerAgent.MyCorner;
import transportation.IntersectionAction;
import transportation.interfaces.AdjCornerRequester;
import transportation.interfaces.Busstop;
import transportation.interfaces.BusstopRequester;
import transportation.interfaces.Corner;
import CommonSimpleClasses.DirectionEnum;
import CommonSimpleClasses.XYPos;
import agent.mock.Mock;

public class MockCorner extends Mock implements Corner {

	public MockCorner(String name) {
		super(name);
	}

	@Override
	public LocationTypeEnum type() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XYPos position() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgIWantToDriveTo(IntersectionAction a) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgYourBusStop(BusstopRequester b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgYourAdjCorners(AdjCornerRequester c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgDoneCrossing() {
		// TODO Auto-generated method stub

	}

	@Override
	public Corner getCornerForDir(DirectionEnum dir) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Busstop> getBusstops() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Busstop getBusstopWithDirection(boolean busDirection)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAdjacentCorner(Corner c, DirectionEnum d) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addBusstop(Busstop b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startThreads() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<MyCorner> getAdjacentCorners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectionEnum getDirForCorner(Corner corner) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
