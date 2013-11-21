package transportation;

import java.util.List;

import com.sun.org.apache.xpath.internal.axes.WalkerFactory;

import transportation.interfaces.Bus;
import transportation.interfaces.Busstop;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;
import CommonSimpleClasses.CityLocation;
import agent.Role;

public class PassengerRole extends Role implements Passenger {
	
	//CityLocation the Passenger is ultimately trying to get to.
	CityLocation destination;
	
	//Path to follow to get to destination. TODO add to DD
	List<Corner> path;
	
	// TODO add to DD?
	CityLocation currentLocation;
	
	//Stores what the Passenger is doing.
	PassengerStateEnum state;
	enum PassengerStateEnum {
		DecisionTime,
		Walking,
		InBus,
		GettingInCar,
		InCar
	}

	@Override
	public void msgMyBusStop(List<Busstop> bsList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void msgGoToLocation(CityLocation loc) {
		destination = loc;
		state = PassengerStateEnum.DecisionTime;
		stateChanged();

	}

	@Override
	public void msgWelcomeToBus(Bus b, double fare) {
		state = PassengerStateEnum.InBus;
		// TODO call gui and get in bus
		stateChanged();

	}

	@Override
	public void msgWeHaveArrived(CityLocation loc) {
		currentLocation = loc;
		// TODO check if person needs to get off bus (do in scheduler)
		stateChanged();
	}

	@Override
	protected boolean pickAndExecuteAnAction() {
		if (state == PassengerStateEnum.DecisionTime) {
			decide();
			return true;
		} else if (state == PassengerStateEnum.Walking) {
			walk();
			return true;
		} else if (state == PassengerStateEnum.GettingInCar) {
			getInCar();
			return true;
		}
		
		return false;
		
	}

	private void walk() {
		// TODO Auto-generated method stub
		
	}

	private void decide() {
		// TODO Auto-generated method stub
		
	}

	private void getInCar() {
		// TODO Auto-generated method stub
		
	}

}
