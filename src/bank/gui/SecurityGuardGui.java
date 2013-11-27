package bank.gui;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;

import agent.gui.Gui;
import bank.SecurityGuardRole;
import bank.interfaces.SecurityGuard;
import bank.interfaces.SecurityGuardGuiInterface;


public class SecurityGuardGui implements Gui, SecurityGuardGuiInterface{

	private SecurityGuard agent = null;
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
	public static final int agentDimW = 20;
	public static final int agentDimH = 40;
	
	int deskXPos = 300;
	int deskYPos = 360;
	
	int entranceX = 300;
	int entranceY = 500;
	
	boolean canRelease = false;
	BufferedImage image;
	public SecurityGuardGui(SecurityGuard c){ //HostAgent m) {
		agent = c;
		xPos = entranceX;
		yPos = entranceY;
		xDestination = entranceX;
		yDestination = entranceY;
//		c.msgGotToTeller();
	
//		DoGoToTeller(200);
		//maitreD = m;
//		this.gui = gui;
		try {
			image = ImageIO.read(getClass().getResource("security_guard.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		
		Rectangle2D r = new Rectangle2D.Double(xPos, yPos, agentDimW, agentDimH);
		Rectangle2D tr = new Rectangle2D.Double(xPos, yPos, agentDimW, agentDimH);
		TexturePaint tp = new TexturePaint(image, tr);
		g.setPaint(tp);
		g.fill(r);
		//g.setColor(Color.BLUE);
		//g.fillRect(xPos, yPos, agentDim, agentDim);
		drawInfo(g, agent.getName(), "Security");

	}
	
	public void drawInfo(Graphics2D g, String name, String occupation) {
		g.setColor(Color.black);
		g.drawString(name, xPos, yPos-10);
		g.drawString(occupation, xPos, yPos);
		
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
	

	
	public void DoGoToDesk() {
		canRelease = true;
		xDestination = deskXPos;
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

