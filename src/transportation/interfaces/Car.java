package transportation.interfaces;

import java.util.List;

import CommonSimpleClasses.CityLocation;

//A Vehicle whose route can be set by a Passenger.
public interface Car extends Vehicle {
	
	//Message to set a destination.
	public void msgTakeMeHere(List<Corner> path, Passenger driver);
}
