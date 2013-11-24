package transportation.interfaces;

import java.util.List;

import CommonSimpleClasses.XYPos;

//A Vehicle whose route can be set by a Passenger.
public interface Car extends Vehicle {
	
	//TODO update DD
	//Message to set a destination.
	public void msgTakeMeHere(List<Corner> path, 
			Passenger driver, XYPos startingPos);
}
