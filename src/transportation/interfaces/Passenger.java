package transportation.interfaces;

import agent.Role;
import CommonSimpleClasses.CityLocation;

/* Role a person takes when he needs to move between 
 * two places in the city.
 */
// TODO Update DD, passenger no longer busstoprequester
public interface Passenger {
	
	// Message from Person that sets the destination.
	public void msgGoToLocation(CityLocation loc);
	
	/* Message a Passenger receives when the bus he is
	 * waiting for arrives.
	 */
	public void msgWelcomeToBus(Bus b, double fare);
	
	// TODO Update DD
	/*Message a Passenger receives when the bus he is 
	 * on arrives to each corner with a busstop.
	 */
	public void msgWeHaveArrived(CityLocation loc) throws Exception;
	
	
	public CityLocation getLocation();

	void msgGoToLocation(CityLocation loc, PassengerRequester requesterRole);

	void msgGotInCar();
	
}
