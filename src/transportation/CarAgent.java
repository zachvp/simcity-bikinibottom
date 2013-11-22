package transportation;

import java.util.ArrayList;
import java.util.List;

import transportation.interfaces.Car;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;

public class CarAgent extends VehicleAgent implements Car {

	Passenger driver;
	
	@Override
	public void msgTakeMeHere(List<Corner> path, Passenger driver) {
		currentPath = new ArrayList<Corner> (path);
		this.driver = driver;
		event = VehicleEventEnum.StartedVehicle;
		stateChanged();
	}

	@Override
	void endTravel() {
		driver.msgWeHaveArrived(currentCorner);
		state = VehicleStateEnum.Initial;
		event = VehicleEventEnum.None;
	}

}
