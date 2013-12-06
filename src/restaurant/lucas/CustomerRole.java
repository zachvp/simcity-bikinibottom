package restaurant.lucas;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant.lucas.interfaces.Cashier;
import restaurant.lucas.interfaces.Customer;
import restaurant.lucas.interfaces.Waiter;

import CommonSimpleClasses.CityLocation;
import agent.PersonAgent.HungerLevel;
import agent.Role;
import agent.interfaces.Person;
import agent.interfaces.Person.Wallet;

/**
 * Restaurant customer role.
 * 
 * @author Jack Lucas
 */
public class CustomerRole extends Role implements Customer {

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRestarauntIsFull() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFollowMeToTable(Waiter w, int x, int y, Map<String, Double> m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfChoice(Map<String, Double> newMenu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedDecidingOrder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsCheck(Cashier c, double check) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneEating() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsChange(double change) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}
	

}