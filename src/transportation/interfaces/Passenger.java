package transportation.interfaces;

import CommonSimpleClasses.CityLocation;

/* Role a person takes when he needs to move between 
 * two places in the city.
 */
public interface Passenger extends BusstopRequester {
	
	//Message from Person that sets the destination.
	public void msgGoToLocation(CityLocation loc);
	
	/* Message a Passenger receives when the bus he is
	 * waiting for arrives.
	 */
	public void msgWelcomeToBus(Bus b, double fare);
	
	/*Message a Passenger receives when the bus he is 
	 * on arrives to each Busstop.
	 */
	public void msgWeHaveArrived(CityLocation loc);
	
}
