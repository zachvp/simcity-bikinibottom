package market.interfaces;

import java.util.List;

import market.Item;
import agent.WorkRole;

public interface PhonePayer {
	
	public abstract void msgHereIsYourTotal(double total, Cashier cashier);

	
}
