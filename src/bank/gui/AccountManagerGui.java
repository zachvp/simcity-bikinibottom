package bank.gui;


import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.Semaphore;

import agent.gui.Gui;
import bank.AccountManagerRole;
import bank.interfaces.AccountManager;


public class AccountManagerGui implements Gui{

	private AccountManager agent = null;
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
	
	private int entranceX = 300;
	private int entranceY = 500;
	
	int deskXPos = 300;
	int deskYPos = 40;
	
	boolean canRelease = false;

	public AccountManagerGui(AccountManagerRole c){ //HostAgent m) {
		agent = c;
		xPos = entranceX;
		yPos = entranceY;
		xDestination = entranceX;
		yDestination = entranceY;
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
		
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, agentDim, agentDim);
		drawInfo(g, agent.getName(), "Account Manager");		

	}
	
	
	public void drawInfo(Graphics2D g, String name, String occupation) {
		g.setColor(Color.black);
		g.drawString(name, xPos, yPos-10);
		g.drawString(occupation, xPos, yPos);
		
	}

	public boolean isPresent() {
		return isPresent;
	}


	public void setPresent(boolean p) {
		isPresent = p;
	}
	

	
	public void DoGoToDesk() {
		canRelease = true;
		xDestination = deskXPos;
		yDestination = deskYPos;
	}
	
	public void DoGoToComputer() {
		canRelease = true;
		xDestination = deskXPos;
		yDestination = deskYPos -30;
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

