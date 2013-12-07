package transportation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.sun.org.apache.bcel.internal.generic.NEW;

import kelp.Kelp;
import kelp.KelpClass;
import transportation.gui.CornerGuiClass;
import transportation.gui.interfaces.CornerGui;
import transportation.interfaces.AdjCornerRequester;
import transportation.interfaces.Busstop;
import transportation.interfaces.BusstopRequester;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;
import transportation.interfaces.Vehicle;
import CommonSimpleClasses.CardinalDirectionEnum;
import CommonSimpleClasses.SingletonTimer;
import CommonSimpleClasses.XYPos;
import agent.Agent;

public class CornerAgent extends Agent implements Corner {
	public class MyCorner {
		public Corner c;
		//Direction in which the Corner is.
		public CardinalDirectionEnum d;
		
		public MyCorner(Corner c, CardinalDirectionEnum d) {
			this.c = c;
			this.d = d;
		}
	}

	private static final long YELLOW_LIGHT_LENGTH_MS = 700;

	private static final long GREEN_LIGHT_LENGTH_MS = 2000;
	
	// TODO update DD
	//True when a Vehicle is crossing through the intersection.
	private Integer crossroadBusy = 1;
	
	private Object synchingTheCounter = new Object();
	
	//List of corners adjacent to this one.
	private List<MyCorner> adjacentCorners = new ArrayList<MyCorner>();
	
	//List of all the bus stops in this corner.
	List<Busstop> busstopList = new ArrayList<Busstop>();
	
	//Position of the center of the intersection in the city map.
	XYPos pos;
	
	/*List of Vehicles waiting to cross and the Corners they 
	 * want to drive to.
	 */
	private List<IntersectionAction> waitingToCross =
			Collections.synchronizedList(new ArrayList<IntersectionAction>());
	
	// TODO Update DD
	/*List of Passengers waiting to walk through the intersection.
	 */
	private Queue<Passenger> waitingToWalk =
			new ConcurrentLinkedQueue<Passenger>();
	
	//List of entities waiting to get a copy of busstopList.
	private Queue<BusstopRequester> waitingForBusstops =
			new ConcurrentLinkedQueue<BusstopRequester>();
	
	//List of entities waiting to get a copy of adjacentCorners.
	private Queue<AdjCornerRequester> waitingForCorners =
			new ConcurrentLinkedQueue<AdjCornerRequester>();
	
	//Reference to Kelp
	Kelp kelp;

	private String name;
	
	// TODO Update DD
	CornerDirectionEnum cornerDir = CornerDirectionEnum.horizontal;
	public enum CornerDirectionEnum {
		horizontal,
		transitioningToVertical,
		vertical,
		transitioningToHorizontal
	}

	private CornerGui gui;
	private Timer timer = SingletonTimer.getInstance(); 

	private boolean changingDir;
	
	public CornerAgent(String name, XYPos pos) {
		this.pos = pos;
		this.name = name;
		
		// TODO Obsolete try catch?
		try {
			kelp = KelpClass.getKelpInstance();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Corner was instantiated before Kelp,"
					+ " buses will misbehave and NullPointerExceptions"
					+ " will be thrown.");
		}
		
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				msgChangeDir();
			}
		}, 0, GREEN_LIGHT_LENGTH_MS);
		
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				msgChangeDir();
			}
		}, YELLOW_LIGHT_LENGTH_MS, GREEN_LIGHT_LENGTH_MS);
		
		this.gui = new CornerGuiClass(this);
		
	}
	
	public String toString() {
		return name + " " + position();
		
	}
	
	public void addAdjacentCorner(Corner c, CardinalDirectionEnum d) {
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
	
	// TODO Update DD
	@Override
	public void msgDoneCrossing() {
		synchronized (synchingTheCounter) {
			crossroadBusy++;
		}
		stateChanged();
	}
	
	// TODO Update DD
	@Override
	public void msgIAmCrossing() {
		synchronized (synchingTheCounter) {
			crossroadBusy--;
		}
		stateChanged();
	}
	
	// TODO Update DD
	@Override
	public void msgChangeDir(){
		incrementDirEnum();
		changingDir = !changingDir;
		stateChanged();
	}

	@Override
	public boolean pickAndExecuteAnAction() { // TODO Update DD

		if (!waitingForBusstops.isEmpty()) {
			sendBusstopInfo();
			return true;
		} else if (!waitingForCorners.isEmpty()) {
			sendAdjCornerInfo();
			return true;
		} else synchronized (synchingTheCounter) {
			if (!waitingToCross.isEmpty() && crossroadBusy > 0) {
				synchronized (waitingToCross) {
					for (IntersectionAction iA : waitingToCross){
						if (shouldCross(iA)) {
							letSomeoneThrough(iA.v);
							waitingToCross.remove(iA);
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	private boolean shouldCross(IntersectionAction iA) {
		boolean verticalMov = 
				(iA.v.currentDirection() 
						== CardinalDirectionEnum.North
						|| iA.v.currentDirection() 
						== CardinalDirectionEnum.South);
		if (iA.turning) { if (cornerDir == CornerDirectionEnum.transitioningToHorizontal
				|| cornerDir == CornerDirectionEnum.transitioningToVertical) {
			return true;
		}} else if ((verticalMov && cornerDir == CornerDirectionEnum.vertical)
				|| (!verticalMov && cornerDir == CornerDirectionEnum.horizontal) ){
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
	private void letSomeoneThrough(Vehicle v) {
		//crossroadBusy--;
		v.msgDriveNow();
	}

	private void incrementDirEnum() {
		cornerDir = CornerDirectionEnum
				.values()[(cornerDir.ordinal()+1)
				%CornerDirectionEnum.values().length];
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
	public Corner getCornerForDir(CardinalDirectionEnum dir) throws Exception {

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
		
		CardinalDirectionEnum directionToGo = adjacentCorners.get(currIndex).d;
		
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
	public CardinalDirectionEnum getDirForCorner(Corner corner) throws Exception {
		for (MyCorner adjCorner : adjacentCorners) {
			if (corner == adjCorner.c) {
				return adjCorner.d;
			}
		}
		throw new Exception("Couldn't find nextCorner in adjCorners.");
	}

	/**
	 * @return the crossroadBusy
	 */
	public Integer getCrossroadBusy() {
		return crossroadBusy;
	}

	/**
	 * @return the waitingForBusstops
	 */
	public Queue<BusstopRequester> getWaitingForBusstops() {
		return waitingForBusstops;
	}

	/**
	 * @return the waitingForCorners
	 */
	public Queue<AdjCornerRequester> getWaitingForCorners() {
		return waitingForCorners;
	}

	/**
	 * @return the waitingToCross
	 */
	public List<IntersectionAction> getWaitingToCross() {
		return waitingToCross;
	}

	@Override
	public CornerDirectionEnum getCurrDir() {
		return cornerDir;
	}





}
