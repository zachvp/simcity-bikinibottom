package transportation;

import java.util.ArrayList;
import java.util.List;

import CommonSimpleClasses.DirectionEnum;
import transportation.VehicleAgent.VehicleEventEnum;
import transportation.VehicleAgent.VehicleStateEnum;
import transportation.interfaces.Bus;
import transportation.interfaces.Busstop;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;

public class BusAgent extends VehicleAgent implements Bus {

	//List of Passengers in the bus.
	List<Passenger> passengerList; 
	
	//List of Passengers waiting in the current Busstop TODO add to DD
	List<Passenger> waitingPassengerList; 

	//Route the bus must load.
	List<Corner> busRoute; 

	//Busstop the Bus is currently in.
	Busstop currentBusstop;
		
	//State the bus is in.
	BusStateEnum busState = BusStateEnum.Moving; 
	enum BusStateEnum { // TODO Update DD
		Moving,
		RequestingBusstop,
		LettingPassengersExit,
		RequestingPassengers,
		CallingPassengers
	}
		
	//Event the bus did.
	BusEventEnum busEvent = BusEventEnum.Initial;
	enum BusEventEnum { // TODO Update DD
		Initial,
		ReceivedBusstop,
		PassengersLeft,
		PassengersReceived,
		PassengersOnBus
	};
	
	@Override
	public void msgMyBusStop(List<Busstop> bsList) {
		for (Busstop bs : bsList) {
			if (bs.direction() == myDirection()) {
				currentBusstop = bs;
				busEvent = BusEventEnum.ReceivedBusstop;
				stateChanged();
				return;
			}
		}
		busState = BusStateEnum.CallingPassengers;
		busEvent = BusEventEnum.PassengersOnBus;
		stateChanged();
		return;

	}

	private DirectionEnum myDirection() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void msgHereArePeople(List<Passenger> people) {
		waitingPassengerList = new ArrayList<Passenger> (people);
		busEvent = BusEventEnum.PassengersReceived;
		stateChanged();
	}

	@Override
	public void msgPayingFare(double fare) { // TODO decide if we're doing this
		stateChanged();
	}

	@Override
	public void msgExiting() {
		/* TODO count number of exits, check against number of gui
		 * messages to know if everyone that's leaving left
		 */
		
	}
	
	@Override
	protected boolean pickAndExecuteAnAction() { // TODO Update scheduler in DD
		if (state == VehicleStateEnum.OnStreet
				&& event == VehicleEventEnum.ArrivedAtCorner
				&& !currentPath.isEmpty()
				&& busState == BusStateEnum.Moving) {
			busState = BusStateEnum.RequestingBusstop;
			currentCorner.msgYourBusStop(this);
			return true;
		} else if (busState == BusStateEnum.RequestingBusstop &&
				busEvent == BusEventEnum.ReceivedBusstop) {
			busState = BusStateEnum.LettingPassengersExit;
			letPassengersExit(); //TODO update action name in DD
			return true;
		} else if (busState == BusStateEnum.LettingPassengersExit
				&& busEvent == BusEventEnum.PassengersLeft){
			currentBusstop.msgGiveMePeople(this);
			busState = BusStateEnum.RequestingPassengers;
			return true;
		} else if (busState == BusStateEnum.RequestingPassengers
				&& busEvent == BusEventEnum.PassengersReceived) {
			busState = BusStateEnum.CallingPassengers;
			letPassengersIn(); //TODO update action name in DD
			return true;
		} else if (busState == BusStateEnum.CallingPassengers &&
				busEvent == BusEventEnum.PassengersOnBus) {
			busState = BusStateEnum.Moving;
			return super.pickAndExecuteAnAction();
		} else if (busState == BusStateEnum.Moving)
			return super.pickAndExecuteAnAction();
		else return false;
		
	}

	private void letPassengersExit() {
		// TODO Auto-generated method stub
		
	}
	
	private void letPassengersIn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	void endTravel() {
		// TODO Auto-generated method stub

	}

}
