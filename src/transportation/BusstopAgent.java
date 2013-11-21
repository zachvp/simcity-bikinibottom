package transportation;

import java.util.ArrayList;
import java.util.List;

import transportation.interfaces.Bus;
import transportation.interfaces.Busstop;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;
import CommonSimpleClasses.DirectionEnum;
import CommonSimpleClasses.XYPos;
import agent.Agent;

public class BusstopAgent extends Agent implements Busstop {
	
	//List of Passengers waiting for the bus.
	List<Passenger> peopleWaiting = new ArrayList<Passenger>();
	
	//Pointer to the Corner where this bus stop is.
	Corner corner;

	//Direction the bus is going when it reaches this stop.
	DirectionEnum direction;
	
	//Bus currently in the Busstop.
	Bus currentBus = null;
	
	XYPos pos; //TODO add to DD

	@Override
	public LocationTypeEnum type() {
		return LocationTypeEnum.Busstop;
	}

	@Override
	public XYPos position() {
		return pos;
	}

	@Override
	public DirectionEnum direction() {
		return direction;
	}

	@Override
	public void msgIAmHere(Passenger p) {
		peopleWaiting.add(p);
	}

	@Override
	public void msgGiveMePeople(Bus b) {
		currentBus = b;
		stateChanged();
	}

	@Override
	protected boolean pickAndExecuteAnAction() {
		if (currentBus != null) {
			currentBus.msgHereArePeople(peopleWaiting);
			peopleWaiting.clear();
			currentBus = null;
			return true;
		}
		return false;
	}

	@Override
	public Corner corner() {
		return corner;
	}

}
