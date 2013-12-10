package restaurant.vdea.gui;

import javax.swing.*;

import agent.gui.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {

   /* static final int rectX = 200;
    static final int rectY = 250;*/

	private final int WINDOWX = 600;//450;
    private final int WINDOWY = 490;//350;
    
    static final int rectW = 50;
    static final int rectH = 50;
	private int rectX = 150;
	private int rectY = 150;
	
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

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(10, this );//increase speed by decreasing int
    	timer.start();
    }
    
    public void pauseAnimation(){
    	pauseAnimation = true;
    }
    
    public void restartAnimation(){
    	pauseAnimation  = false;
    }
    

	public void actionPerformed(ActionEvent e) {
		if(!pauseAnimation){
		repaint();  //Will have paintComponent called
		}
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(rectX, rectY, rectW, rectH);//200 and 250 need to be table params
        
        g2.fillRect(rectX+100, rectY, rectW, rectH); //table 2 (250, 150)
        g2.fillRect(rectX, rectY+100, rectW, rectH); //table 3 (150, 250)
        g2.fillRect(rectX+100, rectY+100, rectW, rectH); //table 4 (250, 250)
        
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
                gui.draw(g2);
            }
        }
        
    }
    
    public void addGui(Gui gui) {
        guis.add(gui);
    }

   /* public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(HostGui gui) {
        guis.add(gui);
    }
    
    public void addGui(WaiterGui gui) {
        guis.add(gui);
    }
    
    public void addGui(CookGui gui) {
        guis.add(gui);
    }
    /*public void addGui(CookGui gui) {
        guis.add(gui);
    }*/
}