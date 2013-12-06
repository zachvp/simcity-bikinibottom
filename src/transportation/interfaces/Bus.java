package transportation.interfaces;

import java.util.List;

/*A Vehicle with a fixed route, that can carry multiple Passengers.*/
public interface Bus extends Vehicle, BusstopRequester {
	
	//Message from Busstop with all the Passengers.
	public void msgHereArePeople(List<Passenger> people);
	
	//Message from Passenger when paying his fare.
	public void msgPayingFare(double fare);
	
	//Message from Passenger when exiting the bus.
	public void msgExiting(Passenger p);

	public boolean orientation();
	
}
