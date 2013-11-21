package transportation;

import java.util.ArrayList;
import java.util.List;

import kelp.Kelp;

import com.sun.org.apache.xpath.internal.axes.WalkerFactory;

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

public class PassengerRole extends Role implements Passenger {
	
	//CityLocation the Passenger is ultimately trying to get to.
	CityLocation destination;
	
	//Path to follow to get to destination. TODO add to DD
	List<CityLocation> path;
	
	// TODO add to DD?
	CityLocation currentLocation;
	
	//Pointer to GUI TODO Add to DD
	PassengerGui gui;
	
	//Pointer to Kelp TODO Add to DD
	Kelp kelp;
	
	//TODO update DD
	//Stores what the Passenger is doing.
	PassengerStateEnum state;
	enum PassengerStateEnum {
		Initial,
		DecisionTime,
		Walking,
		WaitingForBus,
		InBus,
		GettingInCar,
		InCar
	}
	
	Vehicle currentVehicle = null;
	
	//TODO implement passing down from person
	boolean hasCar = false;
	boolean useBus = true;

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
		currentLocation = loc;
		gui.doUpdateLoc(loc);
		state = PassengerStateEnum.DecisionTime;
		stateChanged();
	}

	@Override
	protected boolean pickAndExecuteAnAction() {
		if (state == PassengerStateEnum.DecisionTime) {
			decide();
			return true;
		} else if (state == PassengerStateEnum.GettingInCar) {
			getInCar();
			return true;
		}
		
		return false;
		
	}


	//TODO add car handling
	private void decide() {
		if (path.isEmpty() && currentLocation != destination){
			// TODO maybe some other priority
			boolean useBusNow = useBus && !hasCar;
			path = kelp.routeFromAToB(currentLocation, destination, useBusNow);
		} else if (path.isEmpty()) {
			state = PassengerStateEnum.Initial;
			deactivate();
			((PersonAgent) getAgent()).msgArrivedAtDestination();
			return;
		}
		
		if (path.get(0) == currentLocation) {
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
		
		
		
		if (currentLocation.type() == LocationTypeEnum.Busstop) {
			Busstop busstop = (Busstop)currentLocation;
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
				Car car = ((PersonAgent)getAgent()).car();
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
				state = PassengerStateEnum.InCar;
			}
		}
	}

	private void getInCar() {
		// TODO Auto-generated method stub
		
	}

}
