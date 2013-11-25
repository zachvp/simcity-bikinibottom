package transportation.interfaces;

import java.util.List;

import CommonSimpleClasses.XYPos;

//A Vehicle whose route can be set by a Passenger.
public interface Car extends Vehicle {

	//Message to set a path, driver and starting position.
	public void msgTakeMeHere(List<Corner> path, 
			Passenger driver, XYPos startingPos);
}
