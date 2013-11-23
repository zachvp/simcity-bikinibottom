package transportation.interfaces;

import java.util.List;

import transportation.CornerAgent.MyCorner;

/* Describes methods needed by agents that request
 *  a Corner's neighbors.
 */
public interface AdjCornerRequester {
	
	/* Message to be received from a Corner 
	 * after the request for its neighbors.
	 */
	void msgMyAdjCorners(List<MyCorner> cList); // TODO Change in DD

}