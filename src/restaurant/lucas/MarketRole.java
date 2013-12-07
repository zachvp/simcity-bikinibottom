package restaurant.lucas;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.lucas.interfaces.Cook;
import restaurant.lucas.interfaces.Market;

import CommonSimpleClasses.CityLocation;
import agent.PersonAgent;
import agent.Role;

/**
 * Restaurant Market Agent
 * 
 * @author Jack Lucas
 */
public class MarketRole extends Role implements Market {

	@Override
	public void msgLowOnFood(List<String> foods,
			List<Integer> amountsRequested, Cook cook) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}
	

}