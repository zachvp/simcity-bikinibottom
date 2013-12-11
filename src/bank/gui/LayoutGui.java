package bank.gui;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import agent.gui.Gui;


public class LayoutGui implements Gui{

	private boolean isPresent = true;
	private final int WINDOWX = 600;
	private final int WINDOWY = 490;

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

	BufferedImage image;
	ImageIcon icon;

	boolean isOpen;
	private int hour;
	private int minute;
	
	public LayoutGui(){ //HostAgent m) {
		xPos = deskXPos;
		yPos = deskYPos;
		xDestination = deskXPos;
		yDestination = deskYPos;

		try {
			image = ImageIO.read(getClass().getResource("bank_floor.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		icon = new ImageIcon(image);

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
		g2.fillRect(0, 0, WINDOWX, WINDOWY);
		g2.drawImage(icon.getImage(), 0, 0, null);

		Graphics2D tellerDesk = (Graphics2D)g;
		tellerDesk.setColor(Color.YELLOW);
		tellerDesk.fillRect(150, 150, 300, 20);

		Graphics2D loanManagerDesk = (Graphics2D)g;
		loanManagerDesk.setColor(Color.YELLOW);
		loanManagerDesk.fillRect(50, 140, 20, 60);
		loanManagerDesk.fillRect(0, 200, 70, 20);
		
		if(isOpen) {
			drawLetter(g2, "open");
		}
		else {
			drawLetter(g2, "closed");
		}

	}


	public void drawLetter(Graphics2D g, String letter) {
		g.setColor(Color.black);
		g.drawString(letter, xPos+130, yPos-5);
		g.drawString("" + hour + " - " + minute, xPos+170, yPos-5);
//		g.drawString("" + minute, xPos+200, yPos+5);

	}
	
	public void setOpen(boolean is, int hour, int minute) {
		isOpen = is;
		this.hour = hour;
		this.minute = minute;
	}

	public boolean isPresent() {
		return isPresent;
	}


	public void setPresent(boolean p) {
		isPresent = p;
	}


}

