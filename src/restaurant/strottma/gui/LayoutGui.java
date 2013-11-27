package restaurant.strottma.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.Timer;

import agent.gui.Gui;
import restaurant.strottma.CookRole.GrillOrPlate;
import restaurant.strottma.CookRole.Order;
import restaurant.strottma.HostRole.Table;
import restaurant.strottma.interfaces.Cook;
import restaurant.strottma.interfaces.Customer;

public class LayoutGui implements Gui{

	private boolean isPresent = true;
	
	 private static final int WINDOWX = 600;
	    private static final int WINDOWY = 490;
	    
	    public static final int TABLE_WIDTH = 50;
	    public static final int TABLE_HEIGHT = 50;
	    static final int NTABLES = 4; // a global for the number of tables.
		static final int TABLE_SPACING = 80;
		static final int TABLE_X_OFFSET = 440-400; // how far right and down to bump all tables
		static final int TABLE_Y_OFFSET = 130;
		static final int TABLES_PER_COLUMN = 2;
		public Collection<Table> tables;

		// refrigerator location
	    private static final int FRIDGE_X = 950-400;
	    private static final int FRIDGE_Y = 174;
		
	    private List<GrillOrPlate> grills = new ArrayList<GrillOrPlate>();
		private List<GrillOrPlate> plateAreas = new ArrayList<GrillOrPlate>();
		
		
	    BufferedImage image, tableImage, grillImage, plateImage;
	    ImageIcon icon;

	    public LayoutGui() {
	    	try {
				image = ImageIO.read(getClass().getResource("floor.png"));
				tableImage = ImageIO.read(getClass().getResource("table.png"));
				grillImage = ImageIO.read(getClass().getResource("grill.png"));
				plateImage = ImageIO.read(getClass().getResource("plate_area.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			icon = new ImageIcon(image);
			
			
			tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
			for (int ix = 0; ix < NTABLES; ix++) {
				int x = (ix / TABLES_PER_COLUMN) * TABLE_SPACING + TABLE_X_OFFSET;
				int y = (ix % TABLES_PER_COLUMN) * TABLE_SPACING + TABLE_Y_OFFSET;
				tables.add(new Table(ix, x, y));//how you add to a collections
			}
			
			// create grills
			for (int i = 0; i < 5; i++) {
				grills.add(new GrillOrPlate(850-400, 114 + 30*i));
			}
			
			// create plating areas
			for (int i = 0; i < 5; i++) {
				plateAreas.add(new GrillOrPlate(750-400, 114 + 30*i));
			}
	    }
	    
	    @Override
	    public void draw(Graphics2D g) {
	        Graphics2D g2 = (Graphics2D)g;

	        //Clear the screen by painting a rectangle the size of the frame
	        g2.fillRect(0, 0, WINDOWX, WINDOWY);
	        g2.drawImage(icon.getImage(), 0, 0, null);
	        
	        
	        //g.setColor(Color.YELLOW);
	        for (Table t : tables) {
	        	Rectangle2D r = new Rectangle2D.Double(t.x, t.y, TABLE_WIDTH,TABLE_HEIGHT);
	    		Rectangle2D tr = new Rectangle2D.Double(t.x, t.y,TABLE_WIDTH,TABLE_HEIGHT);
	    		TexturePaint tp = new TexturePaint(tableImage, tr);
	    		g2.setPaint(tp);
	    		g2.fill(r);
	        	//g.fillRect(t.x, t.y, TABLE_WIDTH, TABLE_HEIGHT);
	        }
	        
	     // draw the grills
	    	for (GrillOrPlate grill : grills) {
	    		//g.setColor(Color.DARK_GRAY);
	    		//g.drawRect(grill.getX(), grill.getY(), 30, 30);
	    		Rectangle2D r = new Rectangle2D.Double(grill.getX(), grill.getY(), 30,30);
	    		Rectangle2D tr = new Rectangle2D.Double(grill.getX(), grill.getY(),30,30);
	    		TexturePaint tp = new TexturePaint(grillImage, tr);
	    		g2.setPaint(tp);
	    		g2.fill(r);
	    		
	    		
	    		if (grill.orderVisible() && grill.getOrder() != null) {
	    			g.setColor(Color.WHITE);
	    			g.drawString(grill.getOrder().getChoice().substring(0, 2),
	    					grill.getX()+10, grill.getY()+20);
	    		}
	    	}
	    	
	    	// draw the plating areas
	    	for (GrillOrPlate plateArea : plateAreas) {
	    		//g.setColor(Color.LIGHT_GRAY);
	    		//g.drawRect(plateArea.getX(), plateArea.getY(), 30, 30);
	    		Rectangle2D r = new Rectangle2D.Double(plateArea.getX(), plateArea.getY(), 30,30);
	    		Rectangle2D tr = new Rectangle2D.Double(plateArea.getX(), plateArea.getY(),30,30);
	    		TexturePaint tp = new TexturePaint(plateImage, tr);
	    		g2.setPaint(tp);
	    		g2.fill(r);
	    		
	    		if (plateArea.orderVisible()) {
	    			g.setColor(Color.BLACK);
	    			g.drawString(plateArea.getOrder().getChoice().substring(0, 2),
	    					plateArea.getX()+10, plateArea.getY()+20);
	    		}
	    	}
	    	
	    	// draw the fridge
	    	g.setColor(Color.BLACK);
	    	g.fillRect(FRIDGE_X, FRIDGE_Y, 30, 30);
	    	
	       
	     
	    }

		@Override
		public void updatePosition() {
			// TODO Auto-generated method stub
			
		}

		public boolean isPresent() {
			return isPresent;
		}


		public void setPresent(boolean p) {
			isPresent = p;
		}
		

		public static class Table {
			Customer occupiedBy;
			int tableNumber;
			int x, y;

			Table(int tableNumber, int x, int y) {
				this.tableNumber = tableNumber;
				this.x = x;
				this.y = y;
			}
			}
		
		public class GrillOrPlate extends Cook.GrillOrPlate {
			int x;
			int y;
			Order order = null;
			boolean showOrder = false;
			
			GrillOrPlate(int x, int y) {
				this.x = x;
				this.y = y;
			}
			
			public int getX() { return this.x; }
			public int getY() { return this.y; }
			
			public synchronized Order getOrder() { return this.order; }
			public synchronized void setOrder(Order o) { this.order = o; }
			public synchronized void removeOrder() { order = null; }
			
			public synchronized void showOrder() { showOrder = true; }
			public synchronized void hideOrder() { showOrder = false; }
			public synchronized boolean orderVisible() { return showOrder; }
		}

}
