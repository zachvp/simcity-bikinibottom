package housing.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import agent.gui.Gui;

/**
 * LayoutGui stores all the positional information about housing items
 * (furniture, appliances, etc) and displays them.
 */
public class LayoutGui implements Gui {
	/* --- Layout Item Positions --- */
	//window size = 550x400
	private final int ROOM_WIDTH;
	private final int ROOM_HEIGHT;
	private final int BASE_SIZE = 20;
	
	// constants for appliance positions
	private final Dimension STOVE_POSITION;
	private final Dimension TABLE_POSITION;
	final Dimension POTTED_PLANT_POSITION;
	final Dimension DOOR_POSITION;
	final Dimension REFRIGERATOR_POSITION;
	
	public LayoutGui(int roomWidth, int roomHeight) {
		ROOM_WIDTH = roomWidth;
		ROOM_HEIGHT = roomHeight;
		
		// place all of the residential items
		STOVE_POSITION = new Dimension(ROOM_WIDTH/2, ROOM_HEIGHT/3);
		TABLE_POSITION = new Dimension(ROOM_WIDTH/4, ROOM_HEIGHT/4);
		POTTED_PLANT_POSITION = new Dimension(ROOM_WIDTH/4, 5);
		DOOR_POSITION = new Dimension(0, ROOM_HEIGHT/4);
		REFRIGERATOR_POSITION = new Dimension(ROOM_WIDTH/2, 0);
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		
		// draw shapes
		g.fillRect(STOVE_POSITION.width, STOVE_POSITION.height, BASE_SIZE, BASE_SIZE);
		g.fillRect(TABLE_POSITION.width, TABLE_POSITION.height, BASE_SIZE, BASE_SIZE);
		g.fillRect(POTTED_PLANT_POSITION.width, POTTED_PLANT_POSITION.height, BASE_SIZE, BASE_SIZE);
		g.fillRect(DOOR_POSITION.width, DOOR_POSITION.height, BASE_SIZE, BASE_SIZE);
		g.fillRect(REFRIGERATOR_POSITION.width, REFRIGERATOR_POSITION.height, BASE_SIZE, BASE_SIZE);
		
		// draw labels
		g.setColor(Color.BLACK);
		g.drawString("Stove", STOVE_POSITION.width, STOVE_POSITION.height + BASE_SIZE);
		g.drawString("Table", TABLE_POSITION.width, TABLE_POSITION.height  + BASE_SIZE);
		g.drawString("Potted Plant", POTTED_PLANT_POSITION.width, POTTED_PLANT_POSITION.height + BASE_SIZE);
		g.drawString("Door", DOOR_POSITION.width, DOOR_POSITION.height  + BASE_SIZE);
		g.drawString("Fridge", REFRIGERATOR_POSITION.width, REFRIGERATOR_POSITION.height + BASE_SIZE);
	}

	@Override
	public boolean isPresent() {
		return true;
	}

	public Dimension getStovePosition() {
		return STOVE_POSITION;
	}

	public Dimension getTablePosition() {
		return TABLE_POSITION;
	}
	
	public Dimension getRefrigeratorPosition() {
		return REFRIGERATOR_POSITION;
	}

}
