package restaurant.vdea.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import agent.gui.Gui;
import restaurant.vdea.CashierRole;

public class CashierGui implements Gui{

	CashierRole role = null;

	private int xPos = -50, yPos = 50;
	private int xDestination = -50, yDestination = 50;//default start position
	private int xHome, yHome;


	public CashierGui(CashierRole role) {
		xHome = 130;
		yHome = 15;
	}


	public void DoGoToCashRegister(){
		xDestination = xHome;
		yDestination = yHome;
	}
	
	 public void DoLeave(){
	    	xDestination = -50;
			yDestination = 50;
	    }

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
	}

	public boolean isPresent() {
		return true;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void draw(Graphics2D g, boolean gradingView) {
		g.setColor(Color.PINK);
		g.fillRect(xPos, yPos, 20, 20);
		
		if(gradingView){
			g.setColor(Color.BLACK);
			g.drawString("Cashier", xPos, yPos+20);
		}
	}

	
	
}
