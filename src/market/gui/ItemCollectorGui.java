package market.gui;


import market.interfaces.*;
import market.ItemCollectorAgent;


import java.awt.*;

public class ItemCollectorGui implements Gui {

    private ItemCollector agent = null;

    private int xPos = 140, yPos = 320;//default cashier position
    private int xDestination = 140, yDestination = 320;//default start position
    
    private static final int ItemCollectorWidth = 10;
    private static final int ItemCollectorHeight = 10;
    
    private static final int HomePosX = 10;
    private static final int HomePosY = 10;
    
    private static final int CollectItemX = 10;
    private static final int CollectItemY = 10;

    public ItemCollectorGui(ItemCollector ic) {
        this.agent = ic;
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
    
    public void BackReadyStation(){
    	xDestination = HomePosX;
    	yDestination = HomePosY;
    }
    
    public void CollectItems(){
    	xDestination = CollectItemX;
    	yDestination = CollectItemY;
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
