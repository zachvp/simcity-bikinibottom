package restaurant.vonbeck.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import agent.gui.Gui;

public class FoodGui implements Gui{

	private static final int GRILLH = 18;
	private static final int GRILLW = 10;
	
	private static final int GRILLX = 450+32+64+4;
	private static final int GRILLY = 120+GRILLH/2;

	RestaurantGui gui;
	
	private boolean isPresent = true;
	private boolean isCooking = false;
	private int plateCount = 0;
	
	
	
	public FoodGui(RestaurantGui gui){ 
		this.gui = gui;
		gui.getAnimationPanel().addGui(this);
	}

	public void draw(Graphics2D g) {
		if (isCooking) {
			g.setColor(Color.YELLOW);
			g.fillRect(GRILLX, GRILLY, GRILLW, GRILLH);
		}
		
		g.setColor(Color.WHITE);
		for (int i = 0; i < plateCount; i++) {
			g.fillOval(GRILLX-95, GRILLY-4-32+20*i, 15, 15);
		}
	}
	
	public void toggleCooking() {
		isCooking = !isCooking;
	}
	
	public void addPlate() {
		plateCount++;
	}
	
	public void removePlate() {
		plateCount--;
	}

	@Override
	public void updatePosition() {}

	@Override
	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}


}
