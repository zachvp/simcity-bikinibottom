package market.gui;



import market.interfaces.*;
import market.ItemCollectorRole;




import java.awt.*;

public class ItemCollectorGui implements Gui {

    private ItemCollector agent = null;

    private int xPos = 140, yPos = 320;//default cashier position
    private int xDestination = 140, yDestination = 320;//default start position
    
    private static final int ItemCollectorWidth = 10;
    private static final int ItemCollectorHeight = 10;
    
    private static final int HomePosX = 140;
    private static final int HomePosY = 320;
    
    private static final int CollectItemX = 120;
    private static final int CollectItemY = 360;

    private MarketPanel panel;
    
    private enum Command {noCommand, GoHome, CollectItem};
	private Command command=Command.noCommand;
    
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
			if (command==Command.GoHome) 
				agent.Ready();
			else if (command==Command.CollectItem) {
				agent.AtCollectStation();
			}
			command=Command.noCommand;
        }

        
        
    }
    public void UpdateInventoryLevel(){
    	panel.UpdateInventoryLevelWithoutButton();
    }
    
    public void BackReadyStation(){
    	xDestination = HomePosX;
    	yDestination = HomePosY;
    	command=Command.GoHome;
    	UpdateInventoryLevel();
    }
    
    public void CollectItems(){
    	xDestination = CollectItemX;
    	yDestination = CollectItemY;
    	command=Command.CollectItem;
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
