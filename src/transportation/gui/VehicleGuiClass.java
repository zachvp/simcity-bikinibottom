/**
 * 
 */
package transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import com.sun.swing.internal.plaf.basic.resources.basic;

import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.XYPos;
import transportation.gui.interfaces.VehicleGui;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;
import transportation.interfaces.Vehicle;

/**
 * @author diegovonbeck
 *
 */
public class VehicleGuiClass implements VehicleGui {
	
	private static final int VEHICLEW = 8;
	private static final int VEHICLEH = 8;
	private int xPos, yPos;
	private Corner destination;
	private Corner startLocation;
	private Vehicle vehicle;
	private boolean isBus;
	private boolean isPresent;
	private VehicleGuiStateEnum state = VehicleGuiStateEnum.NotMoving;
	enum VehicleGuiStateEnum {
		Moving,
		NotMoving
	}
	
	public VehicleGuiClass(Vehicle vehicle,
			Corner currentLocation, boolean isBus) {
		this.vehicle = vehicle;
		this.startLocation =  currentLocation;
		this.destination = currentLocation;
		this.isBus = isBus;
		this.isPresent = isBus;
		resetXY();
		TransportationGuiController.getInstance().addVehicleGUI(this);
	}
	
	public void setLocation(Corner loc) {
		startLocation = loc;
		destination = loc;
		resetXY();
	}


	@Override
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
		if (state == VehicleGuiStateEnum.Moving){
			state = VehicleGuiStateEnum.NotMoving;
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
		
		XYPos drawingPos;
		
		try {
			drawingPos = calculateDrawingPosition();
		} catch (Exception e) {
			drawingPos = new XYPos(xPos,yPos);
			e.printStackTrace();
		}
		
		g.fillRect(drawingPos.x, drawingPos.y, VEHICLEW, VEHICLEH);
	}

	private XYPos calculateDrawingPosition() throws Exception {
		XYPos response;
		
		switch (vehicle.currentDirection()) {
		case North:
			response = new XYPos(xPos+Constants.SPACE_BETWEEN_BUILDINGS/4,
					yPos);
			break;
		case South:
			response = new XYPos(xPos-Constants.SPACE_BETWEEN_BUILDINGS/4,
					yPos);
			break;
		case West:
			response = new XYPos(xPos,
					yPos-Constants.SPACE_BETWEEN_BUILDINGS/4);
			break;
		case East:
			response = new XYPos(xPos,
					yPos+Constants.SPACE_BETWEEN_BUILDINGS/4);
			break;
		default:
			throw new Exception("This shouldn't happen");
		}
		
		//adjust from center to corner
		response.x -= VEHICLEW/2;
		response.y -= VEHICLEH/2;
		return response;
	}


	@Override
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setPresent(boolean isPresent) {
		this.isPresent = isPresent;
	}

	@Override
	public void doMoveToCorner(Corner destination) {
		state = VehicleGuiStateEnum.Moving;
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

	@Override
	public void setLocation(XYPos startingPos) {
		xPos = startingPos.x;
		yPos = startingPos.y;
	}

}
