package transportation.gui.interfaces;

import transportation.interfaces.Corner;
import CommonSimpleClasses.CardinalDirectionEnum;
import CommonSimpleClasses.XYPos;
import agent.gui.Gui;

public interface VehicleGui extends Gui {

	void doMoveToCorner(Corner destination);

	void doTraverseAndMoveToCorner(Corner currentCorner, Corner destination);

	void setLocation(XYPos startingPos);

	void setPresent(boolean isPresent);

	CardinalDirectionEnum currentDirection();

	int front();

	int back();

	int getCoordinatePerpendicularToMovement();
	

}
