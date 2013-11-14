package transportation;

import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParseException;
import com.xuggle.mediatool.IMediaDebugListener.Event;

import transportation.interfaces.Corner;
import transportation.interfaces.Vehicle;
import agent.Agent;

public abstract class VehicleAgent extends Agent implements Vehicle {
	enum VehicleStateEnum {
		OnStreet,
		Requesting, // TODO Add to DD
		OnCorner
	}
	enum VehicleEventEnum {// TODO Add to DD
		ArrivedAtCorner,
		ReceivedAdjCorners,
		None
	}
	
	VehicleStateEnum state = VehicleStateEnum.OnStreet;
	private Corner currentCorner;
	private VehicleEventEnum event = VehicleEventEnum.None;
	private List<Corner> currentPath;
	private List<Corner> adjCorners;
	
	@Override
	public void msgMyAdjCorners(List<Corner> cList) {
		adjCorners = new ArrayList<Corner> (cList);
	}

	@Override
	public void msgArrivedAtCorner(Corner c) {
		currentCorner = c;
		event = VehicleEventEnum.ArrivedAtCorner;
		stateChanged();
	}

	@Override
	protected boolean pickAndExecuteAnAction() { // TODO Upadte DD
		if (currentPath.isEmpty()) {
			endTravel();
			return true;
		} else if (state == VehicleStateEnum.OnStreet
				&& event == VehicleEventEnum.ArrivedAtCorner) {
			currentCorner.msgYourAdjCorners(this);
			state = VehicleStateEnum.Requesting;
			return true;
		} else if (state == VehicleStateEnum.Requesting 
				&& event == VehicleEventEnum.ReceivedAdjCorners) {
			state = VehicleStateEnum.OnCorner;
			event = VehicleEventEnum.None;
			try {
				verifyDirection();
			} catch (ParseException e) {
				e.printStackTrace();
				state = VehicleStateEnum.OnStreet;
				event = VehicleEventEnum.None;
				endTravel();
			}
			return true;
		} else if (state == VehicleStateEnum.OnCorner) {
			traverseCorner();
			state = VehicleStateEnum.OnStreet;
			return true;
		}
		return false;
	}

	abstract void endTravel();

	protected void verifyDirection() throws ParseException {
		if ( !adjCorners.contains(currentPath.get(0)) ) {
			throw new ParseException("Vehicle wanted to traverse"
					+ "between two disjoint corners");
		}
		
	}

	protected void traverseCorner() {
		// TODO Auto-generated method stub
		
	}

}
