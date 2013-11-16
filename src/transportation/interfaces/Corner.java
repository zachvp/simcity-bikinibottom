package transportation.interfaces;

import CommonSimpleClasses.CityLocation;
import transportation.IntersectionAction;

/* Node of a grid defining the city's roads. Handles all 
 * necessary interactions when people or vehicles reach 
 * a corner.
 */
public interface Corner extends CityLocation {
	
	/* Message a vehicle sends to a corner 
	 * to request crossing the intersection.
	 */
	public void msgIWantToDriveTo(IntersectionAction a);
	
	/*Message sent to a corner by entities that need a 
	 * list of the corner's bus stops.
	 */
	public void msgYourBusStop(BusstopRequester b);
	
	/* Message sent to a corner by entities that need a
	 *  list of the corner's neighbors.
	 */
	public void msgYourAdjCorners(AdjCornerRequester c);
	
	/* Message received when a Vehicle is done crossing 
	 * the intersection. Sets crossroadBusy to false.
	 */
	public void msgDoneCrossing();
	
}
