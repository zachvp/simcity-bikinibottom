package transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import transportation.gui.interfaces.CornerGui;
import transportation.interfaces.Corner;

public class CornerGuiClass implements CornerGui {

	private Corner corner;
	private int xPos;
	private int yPos;

	public CornerGuiClass(Corner corner) {
		this.corner = corner;
		resetXY();
		TransportationGuiController
			.getInstance().addCornerGUI(this);
		
		
	}
	
	private void resetXY() {
		xPos = corner.position().x;
		yPos = corner.position().y;
		
	}
	
	@Override
	public void updatePosition() {
		// Nothing
	}

	@Override
	public void draw(Graphics2D g) {
		switch (corner.getCurrDir()) {
		case vertical:
			g.setColor(Color.GREEN);
			break;
			
		case horizontal:
			g.setColor(Color.RED);
			break;
			
		default:
			g.setColor(Color.YELLOW);
			break;
		}
		
		g.drawRect(xPos, yPos, 6, 6);

	}

	@Override
	public boolean isPresent() {
		return true;
	}

}
