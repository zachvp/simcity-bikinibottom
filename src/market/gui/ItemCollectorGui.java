package market.gui;



import market.interfaces.*;
import market.ItemCollectorRole;




import java.awt.*;

public class ItemCollectorGui implements Gui {

    private ItemCollector agent = null;

    private int xPos = 180, yPos = -30;//default cashier position
    private int xDestination = ExitX1, yDestination = ExitY1;//default start position
    
    private static final int ItemCollectorWidth = 10;
    private static final int ItemCollectorHeight = 10;
    
    private static final int HomePosX = 140;
    private static final int HomePosY = 320;
<<<<<<< HEAD
    
    private static final int CollectItemX = 120;
    private static final int CollectItemY = 360;

    private MarketPanel panel;
    
    private enum Command {noCommand, GoHome, CollectItem};
	private Command command=Command.noCommand;
=======
    
    private static final int CollectItemX = 120;
    private static final int CollectItemY = 360;
    
    private static final int ExitX1 = 80;
    private static final int ExitY1 = 250;
    
    private static final int ExitX = 180;
    private static final int ExitY = -30;

    private MarketPanel panel;
    
    private enum Command {noCommand, GoHome, CollectItem, GoToExit, GoToExit1 , GoToWork};
	private Command command=Command.GoToWork;
>>>>>>> master
    
    public ItemCollectorGui(ItemCollector ic, MarketPanel p) {
        this.agent = ic;
        panel = p;
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
<<<<<<< HEAD
			if (command==Command.GoHome) 
				agent.Ready();
			else if (command==Command.CollectItem) {
				agent.AtCollectStation();
			}
			command=Command.noCommand;
        }

        panel.UpdateInventoryLevel();
        
=======
        	if (command==Command.GoToWork){
        		BackReadyStation();
        	}
        	else if (command==Command.GoHome) {
				agent.Ready();
        	}
			else if (command==Command.CollectItem) {
				agent.AtCollectStation();
			}
			else if (command==Command.GoToExit1){
				ContinueOffWork();
			}
			else if (command==Command.GoToExit){
				agent.AtExit();
			}
        	
			command=Command.noCommand;
        }

        
        
    }
    public void UpdateInventoryLevel(){
    	panel.UpdateInventoryLevelWithoutButton();
    }
    
    public void GoToWork(){
    	xDestination = ExitX1;
    	yDestination = ExitY1;
    	command = command.GoToWork;
>>>>>>> master
    }
    
    public void BackReadyStation(){
    	xDestination = HomePosX;
    	yDestination = HomePosY;
    	command=Command.GoHome;
<<<<<<< HEAD
=======
    	UpdateInventoryLevel();
>>>>>>> master
    }
    
    public void CollectItems(){
    	xDestination = CollectItemX;
    	yDestination = CollectItemY;
    	command=Command.CollectItem;
<<<<<<< HEAD
=======
    }
    
    public void OffWork(){
    	xDestination = ExitX1;
    	yDestination = ExitY1;
    	command=Command.GoToExit1;
    }
    
    public void ContinueOffWork(){
    	xDestination = ExitX;
    	yDestination = ExitY;
    	command=Command.GoToExit;
>>>>>>> master
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.PINK);
        g.fillRect(xPos, yPos, ItemCollectorWidth, ItemCollectorHeight);
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
}
