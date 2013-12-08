package housing.gui;

import housing.interfaces.DwellingLayoutGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import CommonSimpleClasses.Constants;
import CommonSimpleClasses.ScheduleTask;
import agent.gui.Gui;

/**
 * LayoutGui stores all the positional information about housing items
 * (furniture, appliances, etc) and displays them.
 */
public class DwellingGui implements Gui, DwellingLayoutGui {
	/* --- Layout Item Positions --- */
	
	//window size = 550x400
	private final int ROOM_WIDTH;
	private final int ROOM_HEIGHT;
	
	// used to draw shapes of furniture/appliances 
	private final int BASE_SIZE = 20;
	
	// constants for appliance positions
	private final Dimension STOVE_POSITION;
	private final Dimension TABLE_POSITION;
	final Dimension POTTED_PLANT_POSITION;
	final Dimension DOOR_POSITION;
	final Dimension REFRIGERATOR_POSITION;
	
	// images
	private BufferedImage backgroundImage;
	private ImageIcon icon;
	
	private BufferedImage garyImage;
	private ImageIcon garyIcon;

	// gary position
	int garyX = 30, garyY = 50;
	int garyXDestination = garyX, garyYDestination = garyY;
	
	// timer for gary
	ScheduleTask move = ScheduleTask.getInstance(); 
	
	public DwellingGui(int index) {
		ROOM_WIDTH = Constants.ANIMATION_PANEL_WIDTH / 3;
		ROOM_HEIGHT = Constants.ANIMATION_PANEL_HEIGHT / 3;
		
		// place all of the residential items
		STOVE_POSITION = new Dimension(ROOM_WIDTH - 50, ROOM_HEIGHT / 2 - 10);
		TABLE_POSITION = new Dimension(ROOM_WIDTH / 2 - 10, ROOM_HEIGHT / 2 - 10);
		POTTED_PLANT_POSITION = new Dimension(ROOM_WIDTH - 85, ROOM_HEIGHT - 40);
		DOOR_POSITION = new Dimension(ROOM_WIDTH - 80, 10);
		REFRIGERATOR_POSITION = new Dimension(30, ROOM_HEIGHT / 2 - 25);
		
		// add the background image
		try {
			backgroundImage = ImageIO.read(getClass().getResource("apartment_background.png"));
			garyImage = ImageIO.read(getClass().getResource("gary.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		icon = new ImageIcon(backgroundImage);
		garyIcon = new ImageIcon(garyImage);
	}

	@Override
	public void updatePosition() {
		if (garyX < garyXDestination) garyX++;
		
		else if (garyX > garyXDestination) garyX--;

		if (garyY < garyYDestination) garyY++;
		
		else if (garyY > garyYDestination) garyY--;
	}

	@Override
	public void draw(Graphics2D g) {
		// draw the background image
		g.drawImage(icon.getImage(), 0, 0, null);
		
		// draw gary
		g.drawImage(garyIcon.getImage(), garyX, garyY, null);
		
//		g.setColor(Color.WHITE);
		
		// draw labels
		g.setColor(Color.BLACK);
		g.drawString("Stove", STOVE_POSITION.width, STOVE_POSITION.height + BASE_SIZE);
		g.drawString("Table", TABLE_POSITION.width, TABLE_POSITION.height  + BASE_SIZE);
		g.drawString("Plant", POTTED_PLANT_POSITION.width, POTTED_PLANT_POSITION.height + BASE_SIZE);
		g.drawString("Door", DOOR_POSITION.width, DOOR_POSITION.height  + BASE_SIZE);
		g.drawString("Fridge", REFRIGERATOR_POSITION.width, REFRIGERATOR_POSITION.height + BASE_SIZE);
	}
	
	@Override
	public void DoMoveGary(){
		Random generator = new Random(); 
		int position = generator.nextInt(4);
		
		switch(position){
			case 0 : { garyXDestination = STOVE_POSITION.width; garyYDestination = STOVE_POSITION.height - 15; break; }
			case 1 : { garyXDestination = TABLE_POSITION.width; garyYDestination = TABLE_POSITION.height - + 15; break; }
			case 2 : { garyXDestination = POTTED_PLANT_POSITION.width; garyYDestination = POTTED_PLANT_POSITION.height - 15; break; }
			case 3 : { garyXDestination = DOOR_POSITION.width; garyYDestination = DOOR_POSITION.height; break; }
			case 4 : { garyXDestination = REFRIGERATOR_POSITION.width; garyYDestination = REFRIGERATOR_POSITION.height + 15; break; }
		}
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
