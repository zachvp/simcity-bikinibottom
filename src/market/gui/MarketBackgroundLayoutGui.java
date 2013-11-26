package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import agent.gui.Gui;


/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class MarketBackgroundLayoutGui implements Gui {

	    private final int WINDOWX = 600;
	    private final int WINDOWY = 490;
	    
	    BufferedImage image;
	    ImageIcon icon;
	    
	    public MarketBackgroundLayoutGui(){
	    	try {
				image = ImageIO.read(getClass().getResource("market_floor.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			icon = new ImageIcon(image);
	    }
	    
		@Override
		public void updatePosition() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void draw(Graphics2D g) {
			// TODO Auto-generated method stub
			Graphics2D g2 = (Graphics2D)g;
			
	        //Clear the screen by painting a rectangle the size of the frame
			g2.fillRect(0, 0, WINDOWX, WINDOWY);
	        g2.drawImage(icon.getImage(), 0, 0, null);
	        
	        //Front Desk
	        g2.setColor(Color.GREEN);
	        g2.fillRect(140, 120, 180, 20);
	        
	        //Bench
	        g2.setColor(Color.blue);
	        g2.fillRect(210, 180, 40, 15);
	        
	        //Backyard (With Inventories)
	        g2.setColor(Color.darkGray);
	        g2.fillRect(140, 310, 180, 20);
	        
	        /*
	        g2.setColor(Color.BLUE);
	        g2.fillRect(70, 280, 7, 7);
	        */
		}

		@Override
		public boolean isPresent() {
			// TODO Auto-generated method stub
			return true;
		}

}   

