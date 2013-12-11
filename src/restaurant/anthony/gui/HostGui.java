package restaurant.anthony.gui;


import restaurant.anthony.HostRole;
import restaurant.anthony.interfaces.Customer;

import java.awt.*;

import agent.gui.Gui;

public class HostGui implements Gui {

    private HostRole agent = null;

    private int xPos = 50, yPos = -50;//default waiter position
    private int xDestination = 50, yDestination = -50;//default start position

    public static final int xTable = 50;
    public static final int yTable = 50;
    
    private static int idlePositionX = 10;
	private static int idlePositionY = 200;
    
    public static final int ExitX = 50;
	public static final int ExitY = -50;
	
	private enum Command {NotAtWork, Idle, GoToWork, NoCommand};
	private Command command = Command.NotAtWork;

    public HostGui(HostRole agent) {
        this.agent = agent;
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
        	if (command == Command.GoToWork){
        		agent.AtWork();
        	}
        	if (command == Command.NotAtWork){
        		agent.AtExit();
        	}
        	command = Command.NoCommand;
        }

        
        
    }

    public void draw(Graphics2D g, boolean gradingView) {
        g.setColor(Color.lightGray);
        g.fillRect(xPos, yPos, 10, 10);
    }

    public boolean isPresent() {
        return true;
    }
    
    public void GoToWork(){
    	command=Command.GoToWork;
    	xDestination = idlePositionX;
    	yDestination = idlePositionY;
    }

    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	public void GoToExit() {
		command = Command.NotAtWork;
		xDestination = ExitX;
    	yDestination = ExitY;
	}
}
