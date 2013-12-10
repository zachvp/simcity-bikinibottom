package transportation.test.mock;

import gui.trace.AlertTag;
import housing.backend.ResidentRole;

import java.util.Map;

import mock.EventLog;
import mock.Mock;
import transportation.PassengerRole;
import transportation.interfaces.Car;
import agent.PersonAgent.HungerLevel;
import agent.Role;
import agent.WorkRole;
import agent.interfaces.Person;

public class MockPerson extends Mock implements Person{
	
	public EventLog log = new EventLog();

	public MockPerson(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgArrivedAtDestination() {
		log.add("Received msgArrivedAtDestination()");
		
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

	@Override
	public void agentStateChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void agentDo(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printMsg(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHungerLevel(HungerLevel hungry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Integer> getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addItemsToInventory(String name, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeItemsFromInventory(String name, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printMsg(String msg, Throwable e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Integer> getShoppingList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void agentDo(AlertTag tag, String name, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRole(Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasSomethingToDo() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
