package transportation;

import transportation.interfaces.Passenger;
import CommonSimpleClasses.CityLocation;
import agent.Role;
import agent.interfaces.Person;

public abstract class PassengerRole extends Role implements Passenger {
	public PassengerRole(Person person, CityLocation location) {
		super(person, location);
	}
	
	public PassengerRole() {
		super();
	}
	
	public enum PassengerStateEnum {
		Initial,
		DecisionTime,
		Walking,
		WaitingForBus,
		InBus,
		GettingInCar,
		StartingCar,
		InCar
	}
	
	@Override
	public abstract boolean pickAndExecuteAnAction();
}
