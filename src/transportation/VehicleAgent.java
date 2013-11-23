package transportation;

import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParseException;

import transportation.CornerAgent.MyCorner;
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
		Requesting, // TODO Add to DD
		OnCorner
	}
	
	//Events triggered. TODO Add to DD
	protected VehicleEventEnum event = VehicleEventEnum.None;
	enum VehicleEventEnum {// TODO Add to DD
		None,
		StartedVehicle,
		ArrivedAtCorner,
		ReceivedAdjCorners,
		AuthorizedToCross
	}
	
	//Pointer to Vehicle GUI TODO add to DD
	VehicleGui gui;
	
	//True when animating
	boolean isAnimating = false;
	
	// List of corners to traverse to get to the destination.
	protected Corner currentCorner;
	
	//List of corners to traverse to get to the destination.
	protected List<Corner> currentPath;
	
	//List of corners adjacent to currentCorner. TODO add to DD.
	protected List<MyCorner> adjCorners;

	private DirectionEnum currentDirection; //TODO Remove from DD
	
	@Override
	public void msgMyAdjCorners(List<MyCorner> cList) {
		adjCorners = new ArrayList<MyCorner> (cList);
		event = VehicleEventEnum.ReceivedAdjCorners;
		stateChanged();
	}

	@Override
	// TODO is argument necessary?
	public void msgArrivedAtCorner(Corner c) {
		currentCorner = c; // could be currentCorner = currentPath.get(0);
		currentPath.remove(c);
		event = VehicleEventEnum.ArrivedAtCorner;
		isAnimating = false;
		stateChanged();
	}
	
	public void msgDriveNow() {
		event = VehicleEventEnum.AuthorizedToCross;
		stateChanged();
	}

	@Override
	protected boolean pickAndExecuteAnAction() { // TODO Update DD
		if (!isAnimating) {
			if (currentPath.isEmpty()) {
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
					&& event == VehicleEventEnum.ReceivedAdjCorners) {

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
	
	// TODO ADD to DD
	private void moveToCorner(Corner corner) {
		gui.doMoveToCorner(corner);
		isAnimating = true;
		
	}

	// TODO ADD to DD
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
	
	//TODO add to DD
	private void askPermissionToCross() {
		IntersectionAction a = new IntersectionAction
				(currentPath.get(0), this);
		
		currentCorner.msgIWantToDriveTo(a);
		
	}

	// TODO Add to DD? or is it there?
	protected void traverseCorner() {
		gui.doTraverseAndMoveToCorner(currentCorner, currentPath.get(0));
	}

}
