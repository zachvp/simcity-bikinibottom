package transportation;

import java.util.ArrayList;
import java.util.List;

import CommonSimpleClasses.DirectionEnum;
import transportation.interfaces.Bus;
import transportation.interfaces.Busstop;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;

//TODO will instantly get and remove passengers, should fix?
public class BusAgent extends VehicleAgent implements Bus {

	//List of Passengers in the bus.
	List<Passenger> passengerList = new ArrayList<Passenger>(); 
	
	//List of Passengers waiting in the current Busstop TODO add to DD
	List<Passenger> waitingPassengerList; 

	//Route the bus must load.
	List<Corner> busRoute; 

	//Busstop the Bus is currently in.
	Busstop currentBusstop;
		
	//State the bus is in.
	BusStateEnum busState = BusStateEnum.Moving; 
	
	boolean direction;
	
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
		DirectionEnum myDir;
		try {
			myDir = myDirection();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception: Failed to find bus "
					+ "direction, will skip a busstop.");
			busEvent = BusEventEnum.PassengersOnBus;
			busState = BusStateEnum.CallingPassengers;
			return;
		} 
		for (Busstop bs : bsList) {
			if (bs.direction() == myDir) {
				currentBusstop = bs;
				busEvent = BusEventEnum.ReceivedBusstop;
				stateChanged();
				return;
			}
		}
		busState = BusStateEnum.RequestingBusstop;
		busEvent = BusEventEnum.ReceivedBusstop;
		stateChanged();
		return;

	}

	private DirectionEnum myDirection() throws Exception {
		Corner nextCorner = currentPath.get(0);
		int i;
		for (i = 0; i < adjCorners.size(); i++) {
			if (adjCorners.get(i).c == nextCorner) {
				break;
			}
		}
		
		if (i == adjCorners.size()) {
			throw new Exception("Next corner was not found "
					+ "in adjCorners");
		}
		return adjCorners.get(i).d;
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

	// TODO this does nothing cause we're not synchronizing the exiting of passengers.
	@Override
	public void msgExiting(Passenger p) {
		passengerList.remove(p);
		/* TODO add counting mechanism if going to make 
		 * passengers exit in order
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
		for (Passenger passenger : passengerList) {
			passenger.msgWeHaveArrived(currentCorner);
		}
		
		//TODO here we're not waiting for passengers to exit
		busEvent = BusEventEnum.PassengersLeft;
	}
	
	private void letPassengersIn() {
		for (Passenger passenger : waitingPassengerList) {
			passenger.msgWelcomeToBus(this, 0); //TODO give fare?
			passengerList.add(passenger);
		}
		// TODO Everyone will come in instantly
		busEvent = BusEventEnum.PassengersOnBus;
	}

	@Override
	void endTravel() {
		currentPath = new ArrayList<Corner> (busRoute);
		if (!direction) { 
			java.util.Collections.reverse(currentPath);
		}
	}

}
