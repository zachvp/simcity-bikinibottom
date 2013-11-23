package transportation.gui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import transportation.gui.interfaces.PassengerGui;
import agent.gui.Gui;

public class TransportationGuiController implements Gui {
	static TransportationGuiController instance = null; 
	
	private List<Gui> guis = new ArrayList<Gui>();

	private List<PassengerGui> passengerGuis =
			new ArrayList<PassengerGui>();
	
	private TransportationGuiController() {}
	
	public static TransportationGuiController getInstance(){
		if (instance == null) {
			instance = new TransportationGuiController();
		}
		return instance;
	}

	@Override
	public void updatePosition() {
		for (Gui gui : passengerGuis ) {
			gui.updatePosition();
		}
		for (Gui gui : guis ) {
			gui.updatePosition();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		for (Gui gui : passengerGuis ) {
			gui.draw(g);
		}
		
		for (Gui gui : guis ) {
			gui.draw(g);
		}
	}

	@Override
	public boolean isPresent() {
		return true;
	}
	
	public void addAbstractGui(Gui gui) {
		guis.add(gui);
	}

	public void addPassengerGUI(PassengerGui passengerGui) {
		passengerGuis.add(passengerGui);		
	}

}
