package transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import CommonSimpleClasses.Constants;
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
		Color verticalColor;
		Color horizontalColor;
		
		switch (corner.getCurrDir()) {
		case vertical:
			verticalColor = Color.GREEN;
			horizontalColor = Color.RED;
			break;
			
		case horizontal:
			verticalColor = Color.RED;
			horizontalColor = Color.GREEN;
			break;
		case transitioningToVertical:
			verticalColor = Color.RED;
			horizontalColor = Color.YELLOW;
			break;
		case transitioningToHorizontal:
		default:
			verticalColor = Color.YELLOW;
			horizontalColor = Color.RED;
			break;
		}
		
		int x1 = xPos - 4;
		int x2 = xPos + 4;
		int y1 = yPos - 4;
		int y2 = yPos + 4;
		
		g.setColor(verticalColor);
		g.drawLine(x1, y1, x2, y1);
		g.drawLine(x1, y2, x2, y2);
		
		g.setColor(horizontalColor);
		g.drawLine(x1, y1, x1, y2);
		g.drawLine(x2, y1, x2, y2);
	}

	@Override
	public boolean isPresent() {
		return true;
	}

}
