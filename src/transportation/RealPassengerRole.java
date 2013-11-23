package transportation;

import java.util.ArrayList;
import java.util.List;

import kelp.Kelp;
import kelp.KelpClass;
import transportation.gui.interfaces.PassengerGui;
import transportation.interfaces.Bus;
import transportation.interfaces.Busstop;
import transportation.interfaces.Car;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;
import transportation.interfaces.Vehicle;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.PersonAgent;
import agent.Role;
import agent.interfaces.Person;

public class RealPassengerRole extends Role implements Passenger {
	
	//CityLocation the Passenger is ultimately trying to get to.
	CityLocation destination;
	
	//Path to follow to get to destination. TODO add to DD
	List<CityLocation> path;
	
	//Pointer to GUI TODO Add to DD
	PassengerGui gui;
	
	//Pointer to Kelp TODO Add to DD
	Kelp kelp = KelpClass.getKelpInstance();
	
	//TODO update DD
	//Stores what the Passenger is doing.
	PassengerStateEnum state = PassengerStateEnum.Initial;
	enum PassengerStateEnum {
		Initial,
		DecisionTime,
		Walking,
		WaitingForBus,
		InBus,
		GettingInCar,
		StartingCar,
		InCar
	}
	
	Vehicle currentVehicle = null;
	
	//TODO implement passing down from person
	boolean hasCar = false;
	boolean useBus = true;
	
	public RealPassengerRole(Person person, CityLocation location,
			PassengerGui gui) {
		super(person, location);
		this.location = location;
	}

	//TODO add input for if has car, if want bus, etc
	@Override
	public void msgGoToLocation(CityLocation loc) {
		destination = loc;
		state = PassengerStateEnum.DecisionTime;
		stateChanged();

	}

	@Override
	public void msgWelcomeToBus(Bus b, double fare) {
		state = PassengerStateEnum.InBus;
		gui.doGetInBus(b);
		// TODO pay fare?
		stateChanged();
	}

	@Override
	//From Vehicle or GUI
	public void msgWeHaveArrived(CityLocation loc) {
		location = loc;
		gui.doUpdateLoc(loc);
		state = PassengerStateEnum.DecisionTime;
		stateChanged();
	}
	
	//From GUI
	public void msgGotInCar() {
		state = PassengerStateEnum.StartingCar;
		stateChanged();
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		if (state == PassengerStateEnum.DecisionTime) {
			decide();
			return true;
		} else if (state == PassengerStateEnum.StartingCar) {
			startCar();
			return true;
		}
		
		return false;
		
	}


	//TODO add car handling
	private void decide() {
		if (path.isEmpty() && location != destination){
			// TODO maybe some other priority
			boolean useBusNow = useBus && !hasCar;
			path = kelp.routeFromAToB(location, destination, useBusNow);
		} else if (path.isEmpty()) {
			state = PassengerStateEnum.Initial;
			deactivate();
			((Person) getPerson()).msgArrivedAtDestination();
			return;
		}
		
		if (path.get(0) == location) {
			path.remove(0);
			if(currentVehicle != null && currentVehicle instanceof Bus) {
				Bus bus = (Bus) currentVehicle;
				bus.msgExiting(this);
				path.remove(0);
				gui.doExitVehicle();
				currentVehicle = null;
				return;
			} else if (currentVehicle != null && currentVehicle instanceof Car) {
				currentVehicle = null;
				path.remove(0);
				gui.doExitVehicle();
				return;
			} else {
				path.remove(0);
				return;
			}
		} else {
			if (currentVehicle != null && currentVehicle instanceof Bus) {
				state = PassengerStateEnum.InBus;
				return;
			}
		}
		
		
		
		if (location.type() == LocationTypeEnum.Busstop) {
			Busstop busstop = (Busstop)location;
			busstop.msgIAmHere(this);
			state = PassengerStateEnum.WaitingForBus;
			return;
		} else {
			if (!hasCar || path.get(0).type() != LocationTypeEnum.Corner) {
				gui.doWalkTo(path.get(0));
				state = PassengerStateEnum.Walking;
				return;
			} else {
				gui.bringOutCar();
				state = PassengerStateEnum.GettingInCar;
			}
		}
	}

	private void startCar() {
		state = PassengerStateEnum.InCar;
		Car car = ((Person)getPerson()).getCar();
		currentVehicle = car;
		
		List<Corner> carPath = new ArrayList<Corner>();
		int i = 0;
		while (path.get(i).type() == LocationTypeEnum.Corner
				&& i < path.size()) {
			carPath.add((Corner) path.get(i));
			i++;
		}
		for (int j = 0; j < i-1; j++) {
			path.remove(0);
		}
		car.msgTakeMeHere(carPath,this);
	}

}
