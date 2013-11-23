package transportation.gui.interfaces;

import transportation.interfaces.Bus;
import CommonSimpleClasses.CityLocation;
import agent.gui.Gui;

public interface PassengerGui extends Gui {

	void doWalkTo(CityLocation cityLocation);

	void doGetInBus(Bus b);

	void doExitVehicle();

	void bringOutCar();

}
