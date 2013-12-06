package transportation.gui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import transportation.gui.interfaces.BusstopGui;
import transportation.gui.interfaces.PassengerGui;
import transportation.gui.interfaces.VehicleGui;
import agent.gui.Gui;

public class TransportationGuiController implements Gui {
	static TransportationGuiController instance = null; 
	
	private List<Gui> guis = 
			Collections.synchronizedList(new ArrayList<Gui>());

	private List<PassengerGui> passengerGuis =
			Collections.synchronizedList(new ArrayList<PassengerGui>());

	private List<VehicleGui> vehicleGuis =
			Collections.synchronizedList(new ArrayList<VehicleGui>());

	private List<BusstopGui> busstopGuis =
			Collections.synchronizedList(new ArrayList<BusstopGui>());
	
	private TransportationGuiController() {}
	
	public static TransportationGuiController getInstance(){
		if (instance == null) {
			instance = new TransportationGuiController();
		}
		return instance;
	}

	@Override
	public void updatePosition() {
		
		synchronized (busstopGuis) {
			for (Gui gui : busstopGuis) {
				gui.updatePosition();
			}
		}
		synchronized (passengerGuis) {
			for (Gui gui : passengerGuis ) {
				gui.updatePosition();
			}
		}
		synchronized (vehicleGuis) {
			for (Gui gui : vehicleGuis) {
				gui.updatePosition();
				gui.updatePosition();
				gui.updatePosition();
			}
		}
		synchronized (guis) {
			for (Gui gui : guis) {
				gui.updatePosition();
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		synchronized (busstopGuis) {
			for (Gui gui : busstopGuis) {
				gui.draw(g);
			}
		}
		synchronized (passengerGuis) {
			for (Gui gui : passengerGuis) {
				if (gui.isPresent())
					gui.draw(g);
			}
		}
		synchronized (vehicleGuis) {
			for (Gui gui : vehicleGuis) {
				if (gui.isPresent())
					gui.draw(g);
			}
		}
		synchronized (guis) {
			for (Gui gui : guis) {
				gui.draw(g);
			}
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

	public void addVehicleGUI(VehicleGui vehicleGui) {
		vehicleGuis.add(vehicleGui);
	}

	public void addBusstopGUI(BusstopGui busstopGui) {
		busstopGuis .add(busstopGui);
	}

}
