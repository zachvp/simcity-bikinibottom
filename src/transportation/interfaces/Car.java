package transportation.interfaces;

//A Vehicle whose route can be set by a Passenger.
public interface Car extends Vehicle {
	
	//Message to set a destination.
	public void msgTakeMeHere();
}
