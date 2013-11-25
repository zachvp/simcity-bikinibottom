package transportation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import kelp.Kelp;
import kelp.KelpClass;
import transportation.interfaces.AdjCornerRequester;
import transportation.interfaces.Busstop;
import transportation.interfaces.BusstopRequester;
import transportation.interfaces.Corner;
import CommonSimpleClasses.DirectionEnum;
import CommonSimpleClasses.XYPos;
import agent.Agent;

public class CornerAgent extends Agent implements Corner {
	public class MyCorner {
		public Corner c;
		//Direction in which the Corner is.
		public DirectionEnum d;
		
		public MyCorner(Corner c, DirectionEnum d) {
			this.c = c;
			this.d = d;
		}
	}
	
	//True when a Vehicle is crossing through the intersection.
	boolean crossroadBusy = false;
	
	//List of corners adjacent to this one.
	private List<MyCorner> adjacentCorners = new ArrayList<MyCorner>();
	
	//List of all the bus stops in this corner.
	List<Busstop> busstopList = new ArrayList<Busstop>();
	
	//Position of the center of the intersection in the city map.
	XYPos pos;
	
	/*List of Vehicles waiting to cross and the Corners they 
	 * want to drive to.
	 */
	Queue<IntersectionAction> waitingToCross =
			new LinkedList<IntersectionAction>();
	
	//List of entities waiting to get a copy of busstopList.
	Queue<BusstopRequester> waitingForBusstops =
			new LinkedList<BusstopRequester>();
	
	//List of entities waiting to get a copy of adjacentCorners.
	Queue<AdjCornerRequester> waitingForCorners =
			new LinkedList<AdjCornerRequester>();
	
	//Reference to Kelp
	Kelp kelp;

	private String name;
	
	public CornerAgent(String name, XYPos pos) {
		this.pos = pos;
		this.name = name;
		try {
			kelp = KelpClass.getKelpInstance();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Corner was instantiated before Kelp,"
					+ " buses will misbehave and NullPointerExceptions"
					+ " will be thrown.");
		}
		
	}
	
	public String toString() {
		return name + " " + position();
		
	}
	
	public void addAdjacentCorner(Corner c, DirectionEnum d) {
		adjacentCorners.add(new MyCorner(c, d));
	}
	
	public void addBusstop(Busstop b){
		busstopList.add(b);
	}


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

	// Sends `busstopList` to a requester.
	private void sendBusstopInfo() {
		BusstopRequester bsR = waitingForBusstops.remove();
		bsR.msgMyBusStop(new ArrayList<Busstop>(busstopList));
	}

	// Sends `adjacentCorners` to a requester.
	private void sendAdjCornerInfo() {
		AdjCornerRequester cR = waitingForCorners.remove();
		cR.msgMyAdjCorners(new ArrayList<MyCorner>(adjacentCorners));
	}

	// Lets a waiting `Vehicle` cross the intersection.
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

	@Override
	public Corner getCornerForDir(DirectionEnum dir) throws Exception {
		for (MyCorner corner : adjacentCorners) {
			if (corner.d == dir) {
				return corner.c;
			}
		}
		throw new Exception("Corner not found for the given directon.");
	}

	@Override
	public List<Busstop> getBusstops() {
		return busstopList;
	}

	@Override
	public Busstop getBusstopWithDirection
		(boolean busDirection) throws Exception {
		if (busstopList.isEmpty()) throw new Exception
			("Busstop to a direction was requested, corner has "
					+ "no busstops.");
		List<Corner> busRoute = kelp.busRoute();
		
		int currIndex;
		for(currIndex = 0; currIndex < busRoute.size();
				currIndex++) {
			if (busRoute.get(currIndex) == this) {
				break;
			}
		}
		if (currIndex == busRoute.size()) throw new Exception
			("Corner with busstops not found in busroute");
		
		int nextIndex;
		
		if (busDirection) {
			nextIndex = (currIndex + 1) % busRoute.size();
		} else {
			nextIndex = (currIndex - 1) % busRoute.size();
		}
		
		Corner nextCornerInRoute = busRoute.get(nextIndex);
		
		for (currIndex = 0; currIndex < adjacentCorners.size();
				currIndex++) {
			Corner currCorner = adjacentCorners.get(currIndex).c;
			if (currCorner == nextCornerInRoute) {
				break;
			}
		}
		if (currIndex == adjacentCorners.size()) throw new Exception
			("Couldn't find nextCorner in adjCorners.");
		
		DirectionEnum directionToGo = adjacentCorners.get(currIndex).d;
		
		for (currIndex = 0; currIndex < busstopList.size();
				currIndex++){
			Busstop currentBusstop = busstopList.get(currIndex);
			if (currentBusstop.direction() == directionToGo){
				break;
			}
		}
		if (currIndex == busstopList.size()) throw new Exception
			("Couldn't find a busstop that goes in the right direction.");
		
		return busstopList.get(currIndex);
	}

	@Override
	public void startThreads() {
		for(Busstop busstop : busstopList) {
			busstop.startThread();
		}
		
		startThread();
		
	}

	/**
	 * @return the adjacentCorners
	 */
	public List<MyCorner> getAdjacentCorners() {
		return adjacentCorners;
	}

	@Override
	public DirectionEnum getDirForCorner(Corner corner) throws Exception {
		for (MyCorner adjCorner : adjacentCorners) {
			if (corner == adjCorner.c) {
				return adjCorner.d;
			}
		}
		throw new Exception("Couldn't find nextCorner in adjCorners.");
	}


}
