package housing.test.mock;

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
	private final Dimension STOVE_POSITION = new Dimension(400, 100);
	private final Dimension TABLE_POSITION = new Dimension(100, 150);
	final Dimension POTTED_PLANT_POSITION = new Dimension(0, 0);
	final Dimension DOOR_POSITION = new Dimension(0, 380);
	final Dimension REFRIGERATOR_POSITION = new Dimension(450, 0);
	
	public LayoutGui() {
		
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GRAY);
		g.fillRect(STOVE_POSITION.width, STOVE_POSITION.height, 20, 20);
		g.fillRect(TABLE_POSITION.width, TABLE_POSITION.height, 20, 20);
		g.fillRect(POTTED_PLANT_POSITION.width, POTTED_PLANT_POSITION.height, 20, 20);
		g.fillRect(DOOR_POSITION.width, DOOR_POSITION.height, 20, 20);
		g.fillRect(REFRIGERATOR_POSITION.width, REFRIGERATOR_POSITION.height, 20, 20);
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

}
