package restaurant.anthony.gui;


import restaurant.anthony.CookRole;
import restaurant.anthony.CustomerRole;
import restaurant.anthony.HostRole;
import restaurant.anthony.interfaces.Cashier;

import java.awt.*;

import agent.gui.Gui;

public class CashierGui implements Gui {

    private Cashier agent = null;

    private int xPos = 50, yPos = -50;//default cashier position
    private int xDestination = 50, yDestination = -50;//default start position
    
    public static final int ExitX = 50;
	public static final int ExitY = -50;
	
	public static final int ExitX1 = 430;
	public static final int ExitY1 = 60;	
	
	public static final int IdlePositionX = 435;
	public static final int IdlePositionY = 165;	
    
    private static final int CashierWidth = 10;
    private static final int CashierHeight = 10;

    private enum Command {NotAtWork, Idle, GoToWork, GoToWork1, GoToExit, GoToExit1, NoCommand};
    private Command command = Command.NotAtWork;
    
    public CashierGui(Cashier ca) {
        this.agent = ca;
    }
    
    public void GoToWork(){
    	command=Command.GoToWork1;
    	xDestination = ExitX1;
    	yDestination = ExitY1;
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

        if (xPos == xDestination && yPos == yDestination){
        	if (command == Command.GoToExit1){
        		ContinueToExit();
        		return;
        	}
        	else if (command == Command.GoToExit){
        		agent.msgAtExit();
        	}
        	else if (command == Command.GoToWork1){
        		ContinueToWork();
        		return;
        	}
        	else if(command == Command.GoToWork){
        		agent.msgAtHome();
        	}
        	command = Command.NoCommand;
        }

    }
    public void ContinueToWork(){
    	command=Command.GoToWork;
    	xDestination = IdlePositionX;
    	yDestination = IdlePositionY;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillRect(xPos, yPos, CashierWidth, CashierHeight);
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

	public void GoToExit() {
		command=Command.GoToExit1;
    	xDestination = ExitX1;
    	yDestination = ExitY1;
	}
	public void ContinueToExit(){
		command = Command.GoToExit;
		xDestination = ExitX;
    	yDestination = ExitY;
	}
}
