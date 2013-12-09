package transportation.interfaces;

import CommonSimpleClasses.CardinalDirectionEnum;

//Describes vehicles that drive through a sequence of Corners.
public interface Vehicle extends AdjCornerRequester {
	
	//Message received when the vehicle gets to a corner.
	public void msgArrivedAtCorner(Corner c);
	
	//Message received when you can cross a corner
	public void msgDriveNow();

	CardinalDirectionEnum currentDirection();

	void startVehicle();
	
}
