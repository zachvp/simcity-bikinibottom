package gui;

import javax.swing.*;

import agent.gui.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/** This panel will be used for displaying animation within buildings after.
 * Panel will display in a cardlayout after the user has clicked on a building.
 *  */
public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 600;//this.getParent().getWidth();
    private final int WINDOWY = 490;//this.getParent().getHeight();
    Timer timer;
    private final int REFRESH_RATE = 5;

    /** Keeps track of all the guis in the panel */
    private List<Gui> guis = 
    		Collections.synchronizedList(new ArrayList<Gui>());
    
    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
    	timer = new Timer(REFRESH_RATE, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		synchronized (guis) {
			for (Gui gui : guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
    }
    
    public void stopTimer(){
    	timer.stop();
    }
    
    public void resumeTimer(){
    	timer.start();
    }

    public void addGui(Gui gui) {
        guis.add(gui);
    }
}
