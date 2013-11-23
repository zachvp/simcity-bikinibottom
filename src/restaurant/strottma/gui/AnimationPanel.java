package restaurant.strottma.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {

    //private static final int WINDOWX = 1000;
    //private static final int WINDOWY = 350;
	private static final int WINDOWX = 600;
    private static final int WINDOWY = 490;
    private static final int TIMER_DELAY = 20;
    private static final int TABLE_X = 200;
    private static final int TABLE_Y = 250;
    private static final int TABLE_W = 50;
    private static final int TABLE_H = 50;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        setBackground(new Color(230, 230, 230));
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(TIMER_DELAY, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        /*
         * now drawn in HostGui
        //Here is the table
        g2.setColor(Color.ORANGE);
        g2.fillRect(TABLE_X, TABLE_Y, TABLE_W, TABLE_H);//200 and 250 need to be table params
        */


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
        System.out.println("Gui added");
    }
    
}
