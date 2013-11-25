package bank.gui;


import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.Semaphore;

import agent.gui.Gui;


public class LayoutGui implements Gui{

	private boolean isPresent = true;


	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	String choiceDisplay = "";


	public static final int xTable = 200;
	public static final int yTable = 250;
	public static final int agentDim = 20;

	int deskXPos = 30;
	int deskYPos = 170;

	boolean canRelease = false;

	public LayoutGui(){ //HostAgent m) {
		xPos = deskXPos;
		yPos = deskYPos;
		xDestination = deskXPos;
		yDestination = deskYPos;
		//		c.msgGotToTeller();

		//		DoGoToTeller(200);
		//maitreD = m;
		//		this.gui = gui;
	}

	public void updatePosition() {
		//		if (xPos < xDestination)
		//			xPos++;
		//		else if (xPos > xDestination)
		//			xPos--;
		//
		//		if (yPos < yDestination)
		//			yPos++;
		//		else if (yPos > yDestination)
		//			yPos--;
		//
		//		if (xPos == xDestination && yPos == yDestination) {
		//			if(canRelease) {
		//				atDestination();
		//			}
		//			
		//			}
		//			command=Command.noCommand;
	}


	public void draw(Graphics2D g) {//abstract definition, needed for Graphics

		Graphics2D g2 = (Graphics2D)g;

		Graphics2D tellerDesk = (Graphics2D)g;
		tellerDesk.setColor(Color.YELLOW);
		tellerDesk.fillRect(150, 150, 300, 20);

		Graphics2D loanManagerDesk = (Graphics2D)g;
		loanManagerDesk.setColor(Color.YELLOW);
		loanManagerDesk.fillRect(50, 140, 20, 60);
		loanManagerDesk.fillRect(0, 200, 70, 20);

	}


	public void drawLetter(Graphics2D g, String letter) {
		g.setColor(Color.black);
		g.drawString(letter, xPos+10, yPos+10);

	}

	public boolean isPresent() {
		return isPresent;
	}


	public void setPresent(boolean p) {
		isPresent = p;
	}


}

