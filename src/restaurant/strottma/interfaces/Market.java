package restaurant.strottma.interfaces;

import java.util.Map;

public interface Market {
	String getName();
	public void msgLowOnFood(Map<String, Integer> cannotDeliver, int marketNum);
	public void msgHereIsPayment(double payment); // from cashier
}
