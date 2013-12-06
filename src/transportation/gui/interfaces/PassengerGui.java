package transportation.gui.interfaces;

import transportation.interfaces.Bus;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.XYPos;
import agent.gui.Gui;

public interface PassengerGui extends Gui {

	void doWalkTo(CityLocation cityLocation);

	void doGetInBus(Bus b);

	void doExitVehicle(CityLocation loc);

	void doBringOutCar();

	void doSetLocation(CityLocation loc);

	XYPos getPos();

	void doExitBus(Corner location, boolean orientation);

	void startShowing();

	void hide();

}
