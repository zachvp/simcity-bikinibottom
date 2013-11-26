package transportation;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParseException;

import transportation.CornerAgent.MyCorner;
import transportation.gui.VehicleGuiClass;
import transportation.gui.interfaces.VehicleGui;
import transportation.interfaces.Corner;
import transportation.interfaces.Vehicle;
import CommonSimpleClasses.DirectionEnum;
import agent.Agent;

public abstract class VehicleAgent extends Agent implements Vehicle {
	
	VehicleStateEnum state = VehicleStateEnum.Initial;
	enum VehicleStateEnum {
		Initial,
		OnStreet,
		Requesting,
		OnCorner
	}
	
	//Events triggered.
	protected VehicleEventEnum event = VehicleEventEnum.None;
	enum VehicleEventEnum {
		None,
		StartedVehicle,
		ArrivedAtCorner,
		ReceivedAdjCorners,
		AuthorizedToCross,
		ReceivedAdjCornersAndBusS
	}
	
	//Pointer to Vehicle GUI object
	VehicleGui gui;
	
	//True when animating
	boolean isAnimating = false;
	
	// List of corners to traverse to get to the destination.
	protected Corner currentCorner;
	
	//List of corners to traverse to get to the destination.
	protected List<Corner> currentPath = new ArrayList<Corner>();
	
	//List of corners adjacent to currentCorner.
	protected List<MyCorner> adjCorners = new ArrayList<MyCorner>();
	
	//Direction the `Vehicle` is currently moving towards.
	private DirectionEnum currentDirection = DirectionEnum.West;
	
	Timer timer = new Timer();
	
	
	public VehicleAgent(Corner currentCorner, boolean isBus) {
		this.currentCorner = currentCorner;
		this.gui = new VehicleGuiClass(this, currentCorner, isBus);
	}
	
	@Override
	public void msgMyAdjCorners(List<MyCorner> cList) {
		adjCorners = new ArrayList<MyCorner> (cList);
		event = VehicleEventEnum.ReceivedAdjCorners;
		stateChanged();
	}

	@Override
	public void msgArrivedAtCorner(Corner c) {
		currentCorner = c;
		currentPath.remove(c);
		event = VehicleEventEnum.ArrivedAtCorner;
		isAnimating = false;
		stateChanged();
	}
	
	public void msgDriveNow() {
		event = VehicleEventEnum.AuthorizedToCross;
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				currentCorner.msgDoneCrossing();
				
			}
		}, 400);
		
		stateChanged();
	}

	@Override
	protected boolean pickAndExecuteAnAction() {
		if (!isAnimating) {
			if (currentPath.isEmpty() && event != VehicleEventEnum.None) {
				endTravel();
				return true;
			} else if (state == VehicleStateEnum.Initial && 
					event == VehicleEventEnum.StartedVehicle) {
				moveToCorner(currentPath.get(0));
				return true;
			} else if (state == VehicleStateEnum.OnStreet
					&& event == VehicleEventEnum.ArrivedAtCorner
					&& !currentPath.isEmpty()) {
				currentCorner.msgYourAdjCorners(this);
				state = VehicleStateEnum.Requesting;
				return true;
			} else if (state == VehicleStateEnum.Requesting 
					&& (event == VehicleEventEnum.ReceivedAdjCorners
					|| event == VehicleEventEnum.ReceivedAdjCornersAndBusS)) {
				try {
					verifyDirection();
				} catch (ParseException e) {
					e.printStackTrace();
					state = VehicleStateEnum.OnStreet;
					event = VehicleEventEnum.None;
					endTravel();
				}
				askPermissionToCross();
				state = VehicleStateEnum.OnCorner;
				return true;
			} else if (state == VehicleStateEnum.OnCorner &&
					event == VehicleEventEnum.AuthorizedToCross) {
				traverseCorner();
				state = VehicleStateEnum.OnStreet;
				return true;
			}
		}
		return false;
	}

	abstract void endTravel();
	
	//Starts moving to that corner.
	private void moveToCorner(Corner corner) {
		gui.doMoveToCorner(corner);
		isAnimating = true;
		state = VehicleStateEnum.OnStreet;
	}

	//Makes sure the next corner is connected to this one.
	protected void verifyDirection() throws ParseException {
		
		for (MyCorner myCorner : adjCorners) {
			if (myCorner.c == currentPath.get(0)) {
				currentDirection = myCorner.d;
				return;
			}
		}
		
		//Throw exception if next corner is not in adjCorners
		throw new ParseException("Vehicle wanted to traverse"
				+ "between two disjoint corners");
		
	}
	
	//Requests permission to cross from `currentCorner`.
	private void askPermissionToCross() {
		IntersectionAction a = new IntersectionAction
				(currentPath.get(0), this);
		
		currentCorner.msgIWantToDriveTo(a);
		
	}

	// Makes the `Vehicle` cross the intersection in the `currentCorner`.
	protected void traverseCorner() {
		try {
			currentDirection = currentCorner.getDirForCorner(currentPath.get(0));
		} catch (Exception e) {
			System.out.println("Vehicle will move through incorrect lane.");
			e.printStackTrace();
		}
		gui.doTraverseAndMoveToCorner(currentCorner, currentPath.get(0));
	}
	
	//Starts the `Vehicle`'s motion.
	public void startVehicle() {
		gui.setPresent(true);
		event = VehicleEventEnum.StartedVehicle;
		stateChanged();
	}

	/**
	 * @return the currentDirection
	 */
	public DirectionEnum currentDirection() {
		return currentDirection;
	}

}
