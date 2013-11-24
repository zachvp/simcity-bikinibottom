package transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.XYPos;
import transportation.gui.interfaces.PassengerGui;
import transportation.interfaces.Bus;
import transportation.interfaces.Passenger;

public class PassengerGuiClass implements PassengerGui {

	private static final int PASSENGERW = 20;
	private static final int PASSENGERH = 20;
	private int xPos, yPos;
	private Passenger passenger;
	private CityLocation destination;
	private CityLocation startLocation;
	private boolean isPresent = true;

	public PassengerGuiClass(Passenger passenger,
			CityLocation location) {
		setPassenger(passenger, location);
	}

	private void setPassenger(Passenger passenger, 
			CityLocation currentLocation) {
		this.passenger = passenger;
		this.startLocation =  currentLocation;
		this.destination = currentLocation;
		resetXY();
		TransportationGuiController.getInstance().addPassengerGUI(this);
	}
	
	public void updatePosition() {
		int xDestination = destination.position().x;
		int yDestination = destination.position().y;
		if (xPos < xDestination)
			xPos += 1;
		else if (xPos > xDestination)
			xPos -= 1;

		if (yPos < yDestination)
			yPos += 1;
		else if (yPos > yDestination)
			yPos -= 1;
		
		if (xPos == xDestination && yPos == yDestination) {
			onPlace();
		}

	}

	private void onPlace() {
		if (startLocation != destination){
			startLocation = destination;
			try {
				passenger.msgWeHaveArrived(destination);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void resetXY() {
		xPos = startLocation.position().x;
		yPos = startLocation.position().y;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, PASSENGERW, PASSENGERH);
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}

	public void doSetLocation (CityLocation loc) {
		startLocation = loc;
		resetXY();
	}

	@Override
	public void doWalkTo(CityLocation cityLocation) {
		destination = cityLocation;
	}

	@Override
	public void doGetInBus(Bus b) {
		isPresent = false;
	}

	@Override
	public void doExitVehicle(CityLocation location) {
		startLocation = location;
		resetXY();
		isPresent = true;
	}

	@Override
	public void doBringOutCar() {
		passenger.msgGotInCar();
	}

}
