package restaurant.anthony.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import agent.gui.Gui;


/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class RestaurantBackgroundLayoutGui implements Gui {

	private final int WINDOWX = 600;
    private final int WINDOWY = 490;
    
    //The Magic Number
    private int numberTable = 3;
    private final int table_width = 25;
    private final int table_height= 25;
    private final int table_coordinateX = 150;
    private final int table_coordinateY = 80;
    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();
	    
	    public RestaurantBackgroundLayoutGui(){

	    }
	    
		@Override
		public void updatePosition() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void draw(Graphics2D g, boolean gradingView) {

			Graphics2D g2 = (Graphics2D)g;

	        //Here is the table
	        
	        //First Table
	        g2.setColor(Color.ORANGE);
	        g2.fillRect(table_coordinateX, table_coordinateY, table_width, table_height);
	        
	        //Second Table
	        g2.setColor(Color.ORANGE);
	        g2.fillRect(table_coordinateX + 100, table_coordinateY, table_width, table_height);
	        
	        //Third Table
	        g2.setColor(Color.ORANGE);
	        g2.fillRect(table_coordinateX + 200, table_coordinateY, table_width, table_height);
	        
	        /*
	        for (int i=0; i<numberTable; i++){
	        g2.setColor(Color.ORANGE);
	        g2.fillRect(table_coordinateX+i*100, table_coordinateY, table_width, table_height);//200 and 250 need to be table params
	        }
	        */
	        
	        g2.setColor(Color.GREEN);
	        g2.fillRect(190, 240, 180, 20);
	        
	        g2.setColor(Color.RED);
	        g2.fillRect(190, 310, 180, 20);
	        
	        g2.setColor(Color.BLUE);
	        g2.fillRect(170, 280, 7, 7);
		}

		@Override
		public boolean isPresent() {
			// TODO Auto-generated method stub
			return true;
		}

}   

