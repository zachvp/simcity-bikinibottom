package transportation.interfaces;

import java.util.List;

/* Describes methods needed by agents that request 
 * Busstops from a Corner.
 */
public interface BusstopRequester {
	
	/* Message to be received from a Corner
	 *  after the request for its Busstops.
	 */
	void msgMyBusStop(List<Busstop> bsList);
}
