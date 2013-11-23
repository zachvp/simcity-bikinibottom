package transportation.test.mock;

import housing.ResidentRole;
import transportation.PassengerRole;
import transportation.interfaces.Car;
import agent.Role;
import agent.WorkRole;
import agent.interfaces.Person;
import agent.mock.Mock;

public class MockPerson extends Mock implements Person{

	public MockPerson(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgArrivedAtDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRole(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Wallet getWallet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Car getCar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCar(Car car) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PassengerRole getPassengerRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResidentRole getResidentRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkRole getWorkRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean workStartsSoon() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStarving() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHungry() {
		// TODO Auto-generated method stub
		return false;
	}

}
