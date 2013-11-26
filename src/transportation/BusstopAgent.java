package transportation;

import java.util.ArrayList;
import java.util.List;

import transportation.gui.BusstopGuiClass;
import transportation.gui.interfaces.BusstopGui;
import transportation.interfaces.Bus;
import transportation.interfaces.Busstop;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;
import CommonSimpleClasses.Constants;
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
	
	//Direction in route
	boolean directionInRoute;
	
	//Bus currently in the Busstop.
	Bus currentBus = null;

	private BusstopGui gui;
	
	public BusstopAgent(Corner corner, DirectionEnum direction,
			boolean directionInRoute) {
		this.corner = corner;
		this.direction = direction;
		this.directionInRoute = directionInRoute;
		
		this.gui = new BusstopGuiClass(this);
	}

	@Override
	public LocationTypeEnum type() {
		return LocationTypeEnum.Busstop;
	}

	@Override
	public XYPos position() {
		int x = corner.position().x;
		int y = corner.position().y;
		
		switch (direction) {
		case North:
			x += Constants.BUSSTOP_OFFESET_PERPENDICULAR;
			y += Constants.BUSSTOP_OFFESET_PARALLEL;
			break;
		case South:
			x -= Constants.BUSSTOP_OFFESET_PERPENDICULAR;
			y -= Constants.BUSSTOP_OFFESET_PARALLEL;
			break;
		case East:
			x -= Constants.BUSSTOP_OFFESET_PARALLEL;
			y += Constants.BUSSTOP_OFFESET_PERPENDICULAR;
			break;
		case West:
			x += Constants.BUSSTOP_OFFESET_PARALLEL;
			y -= Constants.BUSSTOP_OFFESET_PERPENDICULAR;
			break;
			
		default:
			break;
		}
		
		return new XYPos(x,y);
	}

	@Override
	public DirectionEnum direction() {
		return direction;
	}

	@Override
	public void msgIAmHere(Passenger p) {
		synchronized (peopleWaiting) {
			peopleWaiting.add(p);
		}
	}

	@Override
	public void msgGiveMePeople(Bus b) {
		currentBus = b;
		stateChanged();
	}

	@Override
	protected boolean pickAndExecuteAnAction() {
		if (currentBus != null) {
			givePassengers();
			return true;
		}
		return false;
	}
	//Gives the passenger list to the bus and clears it.
	public void givePassengers() {
		synchronized (peopleWaiting) {
			currentBus.msgHereArePeople(peopleWaiting);
			peopleWaiting.clear();
		}
		currentBus = null;
	}

	@Override
	public Corner corner() {
		return corner;
	}

}
