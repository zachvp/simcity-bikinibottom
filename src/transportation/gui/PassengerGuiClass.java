package transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.print.attribute.standard.Destination;

import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.XYPos;
import transportation.gui.interfaces.PassengerGui;
import transportation.interfaces.Bus;
import transportation.interfaces.Passenger;

public class PassengerGuiClass implements PassengerGui {
	private static final int PASSENGERW = 20;
	private static final int PASSENGERH = 20;
	private double xPos, yPos;
	private Passenger passenger;
	private CityLocation destination;
	private CityLocation startLocation;

	@Override
	public void setPassenger(Passenger passenger, 
			CityLocation currentLocation) {
		this.passenger = passenger;
		this.startLocation =  currentLocation;
		this.destination = currentLocation;
		xPos = startLocation.position().x;
		yPos = startLocation.position().y;
		TransportationGuiController.getInstance().addGui(this);
	}
	
	public void updatePosition() {
		int xDestination = destination.position().x;
		int yDestination = destination.position().y;
		if (xPos < xDestination)
			xPos += 0.1;
		else if (xPos > xDestination)
			xPos -= 0.1;

		if (yPos < yDestination)
			yPos += 0.1;
		else if (yPos > yDestination)
			yPos -= 0.1;
		
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

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect((int)xPos, (int)yPos, PASSENGERW, PASSENGERH);
	}

	@Override
	public boolean isPresent() {
		return true;
	}

	@Override
	public void doWalkTo(CityLocation cityLocation) {
		destination = cityLocation;
	}

	@Override
	public void doGetInBus(Bus b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doExitVehicle() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bringOutCar() {
		// TODO Auto-generated method stub
	}

}
