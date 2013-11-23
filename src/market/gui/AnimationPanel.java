package market.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class AnimationPanel extends JPanel implements ActionListener {

    private final int WINDOWX = 600;
    private final int WINDOWY = 490;
    
    //The Magic Number
    private int numberTable = 3;
    private final int table_width = 50;
    private final int table_height= 50;
    private final int table_coordinateX = 50;
    private final int table_coordinateY = 50;
    
    private Image bufferImage;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();

    public AnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(10, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, this.getWidth(), this.getHeight() );

        //Front Desk
        g2.setColor(Color.GREEN);
        g2.fillRect(90, 220, 180, 20);
        
        //Bench
        g2.setColor(Color.blue);
        g2.fillRect(160, 290, 40, 15);
        
        //Backyard (With Inventories)
        g2.setColor(Color.darkGray);
        g2.fillRect(90, 410, 180, 20);
        
        /*
        g2.setColor(Color.BLUE);
        g2.fillRect(70, 280, 7, 7);
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

    public void addGui(CustomerGui gui) {
        guis.add(gui);
    }

    public void addGui (CashierGui gui){
    	guis.add(gui);
    }
    
    public void addGui(DeliveryGuyGui gui) {
    	guis.add(gui);
    }
    
    public void addGui(ItemCollectorGui gui) {
        guis.add(gui);
    }

}
