package transportation;

import java.util.ArrayList;
import java.util.List;

import CommonSimpleClasses.XYPos;
import transportation.interfaces.Car;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;

public class CarAgent extends VehicleAgent implements Car {

	public CarAgent(Corner currentCorner) {
		super(currentCorner, false);
	}

	Passenger driver;
	
	@Override
	public void msgTakeMeHere(List<Corner> path, Passenger driver, 
			XYPos startingPos) {
		currentPath = new ArrayList<Corner> (path);
		gui.setLocation(startingPos);
		this.driver = driver;
		event = VehicleEventEnum.StartedVehicle;
		stateChanged();
	}

	@Override
	void endTravel() {
		try {
			if (driver != null) driver.msgWeHaveArrived(currentCorner);
		} catch (Exception e) {
			e.printStackTrace();
		}
		gui.setPresent(false);
		state = VehicleStateEnum.Initial;
		event = VehicleEventEnum.None;
	}

}
