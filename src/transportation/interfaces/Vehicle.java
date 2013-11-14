package transportation.interfaces;

//Describes vehicles that drive through a sequence of Corners.
public interface Vehicle extends AdjCornerRequester {
	
	//  Message received when the vehicle gets to a corner.
	public void msgArrivedAtCorner(Corner c);
	
}
