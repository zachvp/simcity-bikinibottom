package transportation;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import transportation.interfaces.AdjCornerRequester;
import transportation.interfaces.Busstop;
import transportation.interfaces.BusstopRequester;
import transportation.interfaces.Corner;
import CommonSimpleClasses.DirectionEnum;
import CommonSimpleClasses.XYPos;
import agent.Agent;

public class CornerAgent extends Agent implements Corner {
	public class MyCorner {
		Corner c;
		//Direction in which the Corner is.
		DirectionEnum d; // TODO add Direction enum as DD standalone?
	}
	
	//True when a Vehicle is crossing through the intersection.
	boolean crossroadBusy = false;
	
	//List of corners adjacent to this one.
	List<MyCorner> adjacentCorners;
	
	//List of all the bus stops in this corner.
	List<Busstop> busstopList;
	
	//Position of the center of the intersection in the city map.
	XYPos pos;
	
	/*List of Vehicles waiting to cross and the Corners they 
	 * want to drive to.
	 */
	Queue<IntersectionAction> waitingToCross;
	
	//List of entities waiting to get a copy of busstopList.
	Queue<BusstopRequester> waitingForBusstops;
	
	//List of entities waiting to get a copy of adjacentCorners.
	Queue<AdjCornerRequester> waitingForCorners; 


	@Override
	public void msgIWantToDriveTo(IntersectionAction a) {
		waitingToCross.add(a);
		stateChanged();
	}

	@Override
	public void msgYourBusStop(BusstopRequester b) {
		waitingForBusstops.add(b);
		stateChanged();
	}

	@Override
	public void msgYourAdjCorners(AdjCornerRequester c) {
		waitingForCorners.add(c);
		stateChanged();
	}

	@Override
	public void msgDoneCrossing() {
		crossroadBusy = false;
		stateChanged();
	}

	@Override
	protected boolean pickAndExecuteAnAction() { // TODO Update DD
		if (!waitingForBusstops.isEmpty()) {
			sendBusstopInfo();
			return true;
		} else if (!waitingForCorners.isEmpty()) {
			sendAdjCornerInfo();
			return true;
		} else if (!waitingToCross.isEmpty() && !crossroadBusy) {
			letSomeoneThrough();
			return true;
		}
		return false;
	}

	// TODO Add to DD
	private void sendBusstopInfo() {
		BusstopRequester bsR = waitingForBusstops.remove();
		bsR.msgMyBusStop(new ArrayList<Busstop>(busstopList));
	}

	// TODO Add to DD
	private void sendAdjCornerInfo() {
		AdjCornerRequester cR = waitingForCorners.remove();
		cR.msgMyAdjCorners(new ArrayList<MyCorner>(adjacentCorners));
	}

	// TODO Add to DD
	private void letSomeoneThrough() {
		crossroadBusy = true;
		IntersectionAction iA = waitingToCross.remove();
		iA.v.msgDriveNow();
	}

	@Override
	public LocationTypeEnum type() {
		return LocationTypeEnum.Corner;
	}

	@Override
	public XYPos position() {
		return pos;
	}

}
