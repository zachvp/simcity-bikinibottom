package transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;

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

	public BusstopGuiClass(Busstop busstop) {
		this.busstop = busstop;
		resetXY();
		TransportationGuiController.getInstance().addBusstopGUI(this);
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
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, BUSSTOPW, BUSSTOPH);
	}

	@Override
	public boolean isPresent() {
		return true;
	}

}
