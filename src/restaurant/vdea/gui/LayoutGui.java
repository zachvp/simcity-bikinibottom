package restaurant.vdea.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import agent.gui.Gui;

public class LayoutGui implements Gui{
	private boolean isPresent = true;
	
	private final int WINDOWX = 600;//450;
    private final int WINDOWY = 490;//350;
    
    static final int rectW = 50;
    static final int rectH = 50;
	private int rectX = 120;
	private int rectY = 200;
	
	//kitchen coordinates
	//cook should stay within start pt(355,40)
	private final int buffer = 15;
	private final int grillW = 82;
	private final int grillH = 25;
	private final int grillX = WINDOWX-grillW-buffer;
	private final int grillY = buffer;
	
	private final int platingW = 25;
	private final int platingH = 80;
	private final int platingX = grillX-platingW;
	private final int platingY = grillY+grillH;

    private Image bufferImage;
    private Dimension bufferSize;
    private boolean pauseAnimation = false; 

    private List<Gui> guis = new ArrayList<Gui>();

    public LayoutGui() {
    	
    	
    }
    
    public void pauseAnimation(){
    	pauseAnimation = true;
    }
    
    public void restartAnimation(){
    	pauseAnimation  = false;
    }
    
    public void addGui(Gui gui) {
        guis.add(gui);
    }

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g, boolean gradingView) {
		Graphics2D g2 = (Graphics2D)g;
        
        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(rectX, rectY, rectW, rectH);//200 and 250 need to be table params
        
        g2.fillRect(rectX+100, rectY, rectW, rectH); //table 2 (250, 150)
        g2.fillRect(rectX+200, rectY, rectW, rectH); //table 3 (150, 250)
        g2.fillRect(rectX+300, rectY, rectW, rectH); //table 4 (250, 250)
        
        g2.setColor(Color.red);
        g2.fillRect(grillX, grillY, grillW, grillH);
        
        g2.setColor(Color.lightGray);
        g2.fillRect(platingX, platingY, platingW, platingH);

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2, gradingView);
            }
        }
		
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}

}
