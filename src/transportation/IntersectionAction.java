package transportation;

import transportation.interfaces.Corner;
import transportation.interfaces.Vehicle;

/* Describes an action a Vehicle intends to 
 * perform on an intersection.
 */
public class IntersectionAction {
	
	//Corner the Vehicle wants to drive to.
	Corner destination;
	
	//Vehicle that wants to do the action.
	Vehicle v;
	
}
