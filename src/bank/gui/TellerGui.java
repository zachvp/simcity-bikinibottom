package bank.gui;


import java.awt.*;
import java.util.concurrent.Semaphore;

import bank.gui.Gui;
import bank.BankCustomerRole;
import bank.TellerRole;


public class TellerGui implements Gui{

	private TellerRole agent = null;
	private boolean isPresent = true;


	//private HostAgent host;
	BankGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;
	
	String choiceDisplay = "";
	
	private Semaphore active = new Semaphore(0);

	public static final int xTable = 200;
	public static final int yTable = 250;
	public static final int agentDim = 20;
	
	int deskXPos = 250;
	int deskYPos = 130;
	
	boolean canRelease = false;

	public TellerGui(TellerRole c){ //HostAgent m) {
		agent = c;
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
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == xDestination && yPos == yDestination) {
			if(canRelease) {
				atDestination();
			}
			
			}
			command=Command.noCommand;
		}
	

	public void draw(Graphics2D g) {//abstract definition, needed for Graphics
		
		g.setColor(Color.RED);
		g.fillRect(xPos, yPos, agentDim, agentDim);
				

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
	
	public void DoGoToAccountManager() {
		canRelease = true;
		xDestination = 300;
		yDestination = 50;
	}
	
	public void DoGoToLoanManager() {
		canRelease = true; 
		xDestination = 70;
	}
	
	public void DoGoToDesk(int xFactor) {
		canRelease = true;
		xDestination = deskXPos + (xFactor * 50);
		yDestination = deskYPos;
	}
	
    public void DoGoToWorkstation(int xFactor) {
    	canRelease = false;
    	xDestination = deskXPos + (xFactor * 50);
    	yDestination = deskYPos;
    }
    
    public void DoEndWorkDay(){
    	canRelease = true;
    	xDestination = 300;
    	yDestination = 500;
    }
	
	public void DoLeaveBank() {
		canRelease = true;
		xDestination = 500;
		yDestination = 500;
	}

    public void atDestination() {
//    	System.out.println("Made it");
    	canRelease = false;
    	agent.msgAtDestination();
    	
    }
	
}

