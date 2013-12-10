package transportation.gui;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;

import transportation.gui.interfaces.BusstopGui;
import transportation.gui.interfaces.CornerGui;
import transportation.gui.interfaces.PassengerGui;
import transportation.gui.interfaces.VehicleGui;
import CommonSimpleClasses.CardinalDirectionEnum;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.SingletonTimer;
import agent.gui.Gui;

public class TransportationGuiController implements Gui {
	private static final int VEHICLE_SPEED_MULT = 3;

	static TransportationGuiController instance = null; 
	
	private List<Gui> guis = 
			Collections.synchronizedList(new ArrayList<Gui>());

	private List<PassengerGui> passengerGuis =
			Collections.synchronizedList(new ArrayList<PassengerGui>());

	private List<VehicleGui> vehicleGuis =
			Collections.synchronizedList(new ArrayList<VehicleGui>());

	private List<BusstopGui> busstopGuis =
			Collections.synchronizedList(new ArrayList<BusstopGui>());

	private List<CornerGui> cornerGuis = 
			Collections.synchronizedList(new ArrayList<CornerGui>());;
	
	private TransportationGuiController() {
		SingletonTimer.getInstance().schedule(new TimerTask() {

			@Override
			public void run() {
				startPedestrianCrashSequence();

			}
		}, 7000);
		
	}
	
	public static TransportationGuiController getInstance(){
		if (instance == null) {
			instance = new TransportationGuiController();
		}
		return instance;
	}

	@Override
	public void updatePosition() {
		
		synchronized (cornerGuis) {
			for (Gui gui : cornerGuis) {
				if (gui.isPresent()) gui.updatePosition();
			}
		}
		
		synchronized (busstopGuis) {
			for (Gui gui : busstopGuis) {
				if (gui.isPresent()) gui.updatePosition();
			}
		}
		synchronized (passengerGuis) {
			for (Gui gui : passengerGuis ) {
				if (gui.isPresent()) gui.updatePosition();
			}
		}
		synchronized (vehicleGuis) {
			for (VehicleGui gui : vehicleGuis) {
				if (gui.isPresent()){
					CardinalDirectionEnum dir = gui.currentDirection();
					boolean canMove = true;
					for (VehicleGui gui2 : vehicleGuis) if (gui2.isPresent()) {
						CardinalDirectionEnum dir2 = gui2.currentDirection();
						if (dir == dir2 && sameRoad(gui, gui2)) {
							int front = gui.front();
							int back = gui2.back();
							
							if (Math.abs(front - back) < 2 * VEHICLE_SPEED_MULT) {
								canMove = false;
							}
						}
					}
					if (canMove) {
						for (int i = 0; i < VEHICLE_SPEED_MULT; i++) 
							gui.updatePosition();
					}
				}
			}
		}
		synchronized (guis) {
			for (Gui gui : guis) {
				if (gui.isPresent()) gui.updatePosition();
			}
		}
	}

	private boolean sameRoad(VehicleGui gui, VehicleGui gui2) {
		int coord = gui.getCoordinatePerpendicularToMovement();
		int coord2 = gui2.getCoordinatePerpendicularToMovement();
		return (Math.abs(coord - coord2) < Constants.SPACE_BETWEEN_BUILDINGS);
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
		synchronized (cornerGuis) {
			for (Gui gui : cornerGuis) {
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
		busstopGuis.add(busstopGui);
	}

	public void addCornerGUI(CornerGui cornerGui) {
		cornerGuis.add(cornerGui);
		
	}
	
	public void startCarCrashSequence() {
		guis.add(new CarCrashSequence());
	}
	
	public void startPedestrianCrashSequence() {
		guis.add(new PedestrianCrashSequence());
	}

	public void removeGui(Gui gui) {
		guis.remove(gui);
	}

}
