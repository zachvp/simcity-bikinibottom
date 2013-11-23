package transportation;

import transportation.interfaces.Bus;
import transportation.interfaces.Passenger;
import CommonSimpleClasses.CityLocation;
import agent.PersonAgent;
import agent.Role;

public class PassengerRole extends Role implements Passenger {

	//CityLocation the Passenger is ultimately trying to get to.
	CityLocation destination = null;

	//Passenger's current location
	CityLocation currentLocation;
	
	public PassengerRole(CityLocation startingLocation) {
		currentLocation = startingLocation;
	}

	@Override
	public void msgGoToLocation(CityLocation loc) {
		destination = loc;
		stateChanged();
	}

	@Override
	public void msgWelcomeToBus(Bus b, double fare) {}

	@Override
	public void msgWeHaveArrived(CityLocation loc) {}

	@Override
	protected boolean pickAndExecuteAnAction() {
		if (destination != null) {
			currentLocation = destination;
			destination = null;
			((PersonAgent) getPerson()).msgArrivedAtDestination();
			deactivate();
			return true;
		}
		return false;
	}


}
