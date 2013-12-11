package restaurant.vdea.gui;

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
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
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
	public void draw(Graphics2D g) {
		Graphics2D g2 = (Graphics2D)g;

		g2.fillRect(0, 0, WINDOWX, WINDOWY);
		g2.drawImage(icon.getImage(), 0, 0, null);

		//Here is the table
		//g2.setColor(Color.ORANGE);
		//g2.fillRect(rectX, rectY, rectW, rectH);//200 and 250 need to be table params

		//g2.fillRect(rectX+100, rectY, rectW, rectH); //table 2 (250, 150)
		//g2.fillRect(rectX+200, rectY, rectW, rectH); //table 3 (150, 250)
		//g2.fillRect(rectX+300, rectY, rectW, rectH); //table 4 (250, 250)
		for(int i = 0; i<4; i++){
			Rectangle2D r = new Rectangle2D.Double(rectX + i*100, rectY, rectW, rectH);
			Rectangle2D tr = new Rectangle2D.Double(rectX + i*100, rectY, rectW, rectH);
			TexturePaint tp = new TexturePaint(tableImage, tr);
			g2.setPaint(tp);
			g2.fill(r);
		}

		for (int i = 0; i<4; i++){
			Rectangle2D r = new Rectangle2D.Double(grillX + i*(grillW/4), grillY, grillW/4, grillH);
			Rectangle2D tr = new Rectangle2D.Double(grillX + i*(grillW/4), grillY, grillW/4, grillH);
			TexturePaint tp = new TexturePaint(grillImage, tr);
			g2.setPaint(tp);
			g2.fill(r);
		}

		//g2.setColor(Color.red);
		//g2.fillRect(grillX, grillY, grillW, grillH);

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

	@Override
	public boolean isPresent() {
		return isPresent;
	}

}
