package transportation;

import java.util.ArrayList;
import java.util.List;

import kelp.Kelp;
import kelp.KelpClass;
import sun.util.logging.resources.logging;
import transportation.PassengerRole.PassengerStateEnum;
import transportation.gui.PassengerGuiClass;
import transportation.gui.interfaces.PassengerGui;
import transportation.interfaces.Bus;
import transportation.interfaces.Busstop;
import transportation.interfaces.Car;
import transportation.interfaces.Corner;
import transportation.interfaces.PassengerRequester;
import transportation.interfaces.Vehicle;
import transportation.test.mock.MockPassengerGui;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.Role;
import agent.RoleFactory;
import agent.interfaces.Person;

public class RealPassengerRole extends PassengerRole {

	//CityLocation the Passenger is ultimately trying to get to.
	private CityLocation destination;

	//Path to follow to get to destination.
	private List<CityLocation> path = new ArrayList<CityLocation>();

	//Pointer to GUI
	PassengerGui gui;

	//Pointer to Kelp
	Kelp kelp = KelpClass.getKelpInstance();

	//Stores what the Passenger is doing.
	private PassengerStateEnum state = PassengerStateEnum.Initial;

	//`Vehicle` the `Passenger` is currently on.
	Vehicle currentVehicle = null;

	//True if `Passenger` has a `Car`.
	boolean hasCar = true;
	
	//True if `Passenger` is willing to take the bus.
	boolean useBus = false;

	//Role that requested the movement, if any.
	private PassengerRequester requesterRole = null;

	public RealPassengerRole(Person person, CityLocation location) {
		super(person, location);
		this.gui = new PassengerGuiClass(this, location);
	}
	
	//CONSTRUCTOR FOR TESTING ONLY!
	public RealPassengerRole(Person person, CityLocation location,
			MockPassengerGui gui) {
		super(person, location);
		this.gui = gui;
	}

	//TODO add input for if has car, if want bus, etc
	@Override
	public void msgGoToLocation(CityLocation loc, boolean willingToUseBus) {
		destination = loc;
		state = PassengerStateEnum.DecisionTime;
		hasCar = (getPerson().getCar() != null);
		stateChanged();

	}
	
	public void msgGoToLocation(CityLocation loc, boolean willingToUseBus,
			PassengerRequester requesterRole) {
		this.requesterRole  = requesterRole;
		msgGoToLocation(loc, willingToUseBus);
	}

	@Override
	public void msgWelcomeToBus(Bus b, double fare) {
		currentVehicle = b;
		state = PassengerStateEnum.InBus;
		gui.doGetInBus(b);
		// TODO pay fare?
		stateChanged();
	}

	@Override
	//From Vehicle or GUI
	public void msgWeHaveArrived(CityLocation loc) throws Exception {
		if (path.get(0) != loc &&
				state != PassengerStateEnum.InBus) {
			throw new Exception("Arrived message sent, but passenger"
					+ " wasn't going there");
		}
		location = loc;
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


	private void decide() {
		if (path.isEmpty() && location != destination){
			boolean useBusNow = useBus && !hasCar;
			path = kelp.routeFromAToB(location, destination, useBusNow);
			return;
		} else if (path.isEmpty()) {
			state = PassengerStateEnum.Initial;
			deactivate();

			System.out.println("GOT TO A PLACE");

			if (requesterRole == null) ((Person) getPerson())
										.msgArrivedAtDestination();
			else {
				requesterRole.msgArrivedAtDestination();
				requesterRole = null;
			}

			return;
		}

		if (path.get(0) == location) {
			path.remove(0);
			if(currentVehicle != null && currentVehicle instanceof Bus) {
				Bus bus = (Bus) currentVehicle;
				bus.msgExiting(this);
				gui.doExitVehicle(location);
				currentVehicle = null;
			} else if (currentVehicle != null && currentVehicle instanceof Car) {
				currentVehicle = null;
				gui.doExitVehicle(location);
			}
			return;
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
				state = PassengerStateEnum.GettingInCar;
				gui.doBringOutCar();
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
		car.msgTakeMeHere(carPath,this,gui.getPos());
		if (car instanceof CarAgent) {
			CarAgent carAgent = (CarAgent) car;
			carAgent.startThread();
		}
		car.startVehicle();
	}

	/**
	 * @return the destination
	 */
	public CityLocation destination() {
		return destination;
	}

	/**
	 * @return the path
	 */
	public List<CityLocation> path() {
		return path;
	}

	/**
	 * @return the state
	 */
	public PassengerStateEnum state() {
		return state;
	}



}
