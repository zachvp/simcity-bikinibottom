package restaurant.strottma.interfaces;

import java.util.Map;

public interface Cook extends market.interfaces.DeliveryReceiver {

	String getName();

//	void msgCannotDeliver(Map<String, Integer> cannotDeliver, int marketNumber);

//	void msgHereIsDelivery(Map<String, Integer> delivery);
		
	public abstract class GrillOrPlate{
		public abstract void showOrder();
		public abstract void hideOrder();
		public abstract void removeOrder();
	}

	boolean hasAnyFood();
}
