package gui;

import CommonSimpleClasses.CityLocation;

public interface RestaurantFakeOrderInterface extends CityLocation {
	market.interfaces.DeliveryReceiver getCook();
	market.interfaces.PhonePayer getCashier();
}
