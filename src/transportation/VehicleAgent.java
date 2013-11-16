package transportation;

import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParseException;

import transportation.CornerAgent.MyCorner;
import transportation.interfaces.Corner;
import transportation.interfaces.Vehicle;
import CommonSimpleClasses.DirectionEnum;
import agent.Agent;

public abstract class VehicleAgent extends Agent implements Vehicle {
	enum VehicleStateEnum {
		Initial,
		OnStreet,
		Requesting, // TODO Add to DD
		OnCorner
	}
	enum VehicleEventEnum {// TODO Add to DD
		None,
		StartedVehicle,
		ArrivedAtCorner,
		ReceivedAdjCorners,
		AuthorizedToCross
	}
	
	VehicleStateEnum state = VehicleStateEnum.Initial;
	
	// List of corners to traverse to get to the destination.
	protected Corner currentCorner;
	
	//Events triggered. TODO Add to DD
	protected VehicleEventEnum event = VehicleEventEnum.None;
	
	//List of corners to traverse to get to the destination.
	protected List<Corner> currentPath;
	
	//
	
	//List of corners adjacent to currentCorner. TODO add to DD.
	private List<MyCorner> adjCorners;

	private DirectionEnum currentDirection;

	
	@Override
	public void msgMyAdjCorners(List<MyCorner> cList) {
		adjCorners = new ArrayList<MyCorner> (cList);
		event = VehicleEventEnum.ReceivedAdjCorners;
		stateChanged();
	}

	@Override
	public void msgArrivedAtCorner(Corner c) {
		currentCorner = c;
		event = VehicleEventEnum.ArrivedAtCorner;
		stateChanged();
	}
	
	public void msgDriveNow() {
		event = VehicleEventEnum.AuthorizedToCross;
		stateChanged();
	}

	@Override
	protected boolean pickAndExecuteAnAction() { // TODO Update DD
		if (currentPath.isEmpty() && 
				event == VehicleEventEnum.ArrivedAtCorner) {
			endTravel();
			return true;
		} else if (state == VehicleStateEnum.Initial && 
				event == VehicleEventEnum.StartedVehicle) {
			moveToCorner(currentPath.get(0));
		
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
		return false;
	}

	abstract void endTravel();
	
	// TODO ADD to DD
	private void moveToCorner(Corner corner) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		moveToCorner(currentPath.get(0));
	}

}
