package transportation;

import java.util.ArrayList;
import java.util.List;

import com.xuggle.mediatool.IMediaDebugListener.Event;

import CommonSimpleClasses.CityLocation;
import transportation.interfaces.Car;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;
import transportation.interfaces.Vehicle;

public class CarAgent extends VehicleAgent implements Car {

	Passenger driver;
	
	@Override
	public void msgTakeMeHere(List<Corner> path) {
		currentPath = new ArrayList<Corner> (path);
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
