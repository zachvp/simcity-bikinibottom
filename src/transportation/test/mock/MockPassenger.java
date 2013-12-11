package transportation.test.mock;

import agent.interfaces.Person;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.CardinalDirectionEnum;
import transportation.interfaces.Bus;
import transportation.interfaces.Passenger;
import transportation.interfaces.PassengerRequester;

public class MockPassenger implements Passenger {

	@Override
	public void msgGoToLocation(CityLocation loc, boolean willingToUseBus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgWelcomeToBus(Bus b, double fare) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgWeHaveArrived(CityLocation loc) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public CityLocation getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgGoToLocation(CityLocation loc, boolean willingToUseBus,
			PassengerRequester requesterRole) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgGotInCar() {
		// TODO Auto-generated method stub

	}

	@Override
	public CardinalDirectionEnum currentDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLocation(CityLocation startLocation) {
		// TODO Auto-generated method stub

	}

	@Override
	public Person getPerson() {
		// TODO Auto-generated method stub
		return null;
	}

}
