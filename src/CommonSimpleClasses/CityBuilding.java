package CommonSimpleClasses;

import agent.Role;

public interface CityBuilding extends CityLocation {
	/* Coordinates of the entrance relative to the 
	 * upper left corner of the building
	 */
	XYPos entrancePos();
	
	/**
	 * The first Role that any customer must message to start the interaction.
	 */
	Role getGreeter();
}
