/**
 * 
 */
package transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import CommonSimpleClasses.CityLocation;
import transportation.gui.interfaces.VehicleGui;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;
import transportation.interfaces.Vehicle;

/**
 * @author diegovonbeck
 *
 */
public class VehicleGuiClass implements VehicleGui {
	
	private static final int VEHICLEW = 20;
	private static final int VEHICLEH = 20;
	private int xPos, yPos;
	private Corner destination;
	private Corner startLocation;
	private Vehicle vehicle;
	private boolean isBus;
	
	public VehicleGuiClass(Vehicle vehicle,
			Corner currentLocation, boolean isBus) {
		this.vehicle = vehicle;
		this.startLocation =  currentLocation;
		this.destination = currentLocation;
		this.isBus = isBus;
		resetXY();
		TransportationGuiController.getInstance().addVehicleGUI(this);
	}


	@Override
	public void updatePosition() {
		int xDestination = destination.position().x;
		int yDestination = destination.position().y;
		if (xPos < xDestination)
			xPos += 2;
		else if (xPos > xDestination)
			xPos -= 2;

		if (yPos < yDestination)
			yPos += 2;
		else if (yPos > yDestination)
			yPos -= 2;
		
		if (xPos == xDestination && yPos == yDestination) {
			onPlace();
		}
	}

	private void onPlace() {
		if (startLocation != destination){
			startLocation = destination;
			try {
				vehicle.msgArrivedAtCorner(destination);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if (isBus) g.setColor(Color.YELLOW);
		else g.setColor(Color.RED);
		
		g.fillRect(xPos, yPos, VEHICLEW, VEHICLEH);
	}

	@Override
	public boolean isPresent() {
		return true;
	}

	@Override
	public void doMoveToCorner(Corner destination) {
		this.destination = destination;
	}

	@Override
	public void doTraverseAndMoveToCorner(Corner currentCorner,
			Corner destination) {
		// TODO update here for corner management things
		doMoveToCorner(destination);
	}
	
	private void resetXY() {
		xPos = startLocation.position().x;
		yPos = startLocation.position().y;
	}

}