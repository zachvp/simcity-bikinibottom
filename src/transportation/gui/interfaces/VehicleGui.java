package transportation.gui.interfaces;

import transportation.interfaces.Corner;
import agent.gui.Gui;

public interface VehicleGui extends Gui {

	void doMoveToCorner(Corner destination);

	void doTraverseAndMoveToCorner(Corner currentCorner, Corner destination);
	

}
