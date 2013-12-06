package transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import sun.java2d.loops.DrawRect;
import transportation.BusstopAgent;
import transportation.gui.interfaces.BusstopGui;
import transportation.interfaces.Busstop;
import agent.gui.Gui;

public class BusstopGuiClass implements BusstopGui {
	
	private static final int BUSSTOPW = 10;
	private static final int BUSSTOPH = 10;
	private Busstop busstop;
	private int xPos;
	private int yPos;

	BufferedImage image;
	
	public BusstopGuiClass(Busstop busstop) {
		this.busstop = busstop;
		resetXY();
		TransportationGuiController.getInstance().addBusstopGUI(this);
		
		try {
			image = ImageIO.read(getClass().getResource("bus_stop.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void resetXY() {
		xPos = busstop.position().x;
		yPos = busstop.position().y;
		
		//Moving from corner to center
		xPos -= BUSSTOPW/2;
		yPos -= BUSSTOPH/2;
	}

	@Override
	public void updatePosition() {}

	@Override
	public void draw(Graphics2D g) {
		//g.setColor(Color.BLUE);
		//g.fillRect(xPos, yPos, BUSSTOPW, BUSSTOPH);
		Rectangle2D r = new Rectangle2D.Double(xPos, yPos,	BUSSTOPW,BUSSTOPH);
		Rectangle2D tr = new Rectangle2D.Double(xPos, yPos,
				BUSSTOPW,BUSSTOPH);
		TexturePaint tp = new TexturePaint(image, tr);
		g.setPaint(tp);
		g.fill(r);
	}

	@Override
	public boolean isPresent() {
		return true;
	}

}
