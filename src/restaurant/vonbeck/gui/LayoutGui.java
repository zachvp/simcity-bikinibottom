package restaurant.vonbeck.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import agent.gui.Gui;



public class LayoutGui implements Gui {
	
    static final int HOSTH = 20;
	static final int HOSTW = 20;
	static final int HOSTY = 30;
	static final int HOSTX = 100;
    static final int TABLEX = 200;
    static final int TABLEY = 50;
    private static final int TABLEW = 50;
    static final int TABLEH = 50;
    private static final int KITCHENTILEW = 16;
    static final int KITCHENTILEH = 16;
    static final int KITCHENX = 450+32;
    static final int KITCHENY = 120-2*KITCHENTILEH;
    private static final int TABLEMOVE = 75;
    private static final int NUMTABLES = 3;

	@Override
	public void updatePosition() {/*Nothing*/}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		//Here is the table
        g.setColor(Color.ORANGE);
        for (int i = 0; i < NUMTABLES; i++) {
        	g.fillRect(TABLEX + TABLEMOVE*i, TABLEY, TABLEW, TABLEH);
        }
        
        //Kitchen Floor
        g.setColor(Color.WHITE);
        g.fillRect(KITCHENX, 
    			KITCHENY, 
    			KITCHENTILEW*4, KITCHENTILEH*8);
        g.setColor(Color.BLACK);
        for (int i = 0; i < 4; i++) for (int j = 0; j < 4; j++) {
        	g.fillRect(KITCHENX + KITCHENTILEW*(j), 
        			KITCHENY+KITCHENTILEH*(2*i + (j%2)), 
        			KITCHENTILEW, KITCHENTILEH);
        }
        
        //Cook
        /*
        g.setColor(Color.BLUE);
        g.fillRect(KITCHENX+16-4+10, 
    			(int)(KITCHENY+KITCHENTILEH*3.5), 
    			20, 20);
    	*/
        
        //Plating Table
        g.setColor(new Color(153, 76, 0));
        g.fillRect(KITCHENX-32, 
    			KITCHENY, 
    			KITCHENTILEW*2, KITCHENTILEH*8);
        
        
        
        //Cooking Table
        
        g.setColor(new Color(66, 66, 66));
        g.fillRect(KITCHENX+64, 
    			KITCHENY, 
    			KITCHENTILEW*2, KITCHENTILEH*8);
        
        
	}

	@Override
	public boolean isPresent() {
		return true;
	}

}
