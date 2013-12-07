package transportation.interfaces;

import agent.Role;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.CardinalDirectionEnum;

/* Role a person takes when he needs to move between 
 * two places in the city.
 */
public interface Passenger {
	
	// Message from Person that sets the destination.
	void msgGoToLocation(CityLocation loc, boolean willingToUseBus);
	
	/* Message a Passenger receives when the bus he is
	 * waiting for arrives.
	 */
	public void msgWelcomeToBus(Bus b, double fare);
	
	/*  Message a `Passenger` receives when the bus he is on 
	 * arrives to each `Corner` with a `Busstop`, when the 
	 * `Car` he is in arrives to destination, or when the 
	 * `PassengerGui` arrives to the corner he was walking to.
	 */
	public void msgWeHaveArrived(CityLocation loc) throws Exception;
	
	public CityLocation getLocation();

	/* Message from a `PassengerRequester` `Role` that sets
	 *  the destination.
	 */
	void msgGoToLocation(CityLocation loc, boolean willingToUseBus,
			PassengerRequester requesterRole);
	
	//Message received from the `PersonGui` when he gets in his car.
	void msgGotInCar();

	CardinalDirectionEnum currentDirection();

	void setLocation(CityLocation startLocation);

}
