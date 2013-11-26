package market.gui;



import market.interfaces.*;
import market.ItemCollectorRole;







import java.awt.*;

import agent.gui.Gui;

public class ItemCollectorGui implements Gui, ItemCollectorGuiInterfaces {

    private ItemCollector agent = null;

    private int xPos = 130, yPos = -30;//default cashier position
    private int xDestination = 130, yDestination = -30;//default start position
    
    private static final int ItemCollectorWidth = 10;
    private static final int ItemCollectorHeight = 10;
    
    private  int HomePosX = 190;
    private  int HomePosY = 195;
    
    private  int CollectItemX = 170;
    private  int CollectItemY = 260;
    
    private static final int ExitX1 = 130;
    private static final int ExitY1 = 150;
    
    private static final int ExitX = 130;
    private static final int ExitY = -30;

    private MarketInfoPanel panel;
    
    private enum Command {noCommand, GoHome, CollectItem, GoToExit, GoToExit1 , GoToWork, NotAtWork, GoToWork1};
	private Command command=Command.NotAtWork;
    
    public ItemCollectorGui(ItemCollector ic) {
        this.agent = ic;
    }
    
    /* (non-Javadoc)
	 * @see market.gui.ItemCollectorGuiInterfaces#setItemCollectorNumber(int)
	 */
    @Override
	public void setItemCollectorNumber(int i){
    	if (i == 0){
    		HomePosX = 195;
    	    CollectItemX = 170;
    	}
    	else{
    		HomePosX = 255;
    	    CollectItemX = 280;
    	}
    }
    
    /* (non-Javadoc)
	 * @see market.gui.ItemCollectorGuiInterfaces#setMarketControlPanel(market.gui.MarketInfoPanel)
	 */
    @Override
	public void setMarketControlPanel(MarketInfoPanel p){
    	panel = p;
    }

    /* (non-Javadoc)
	 * @see market.gui.ItemCollectorGuiInterfaces#updatePosition()
	 */
    @Override
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

        	if (command==Command.GoToWork){
        		GoToWork();
        		return;
        	}
        	else if (command==Command.GoToWork1){
        		ContinueToWork();
        		return;
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
				command= Command.NotAtWork;
				agent.AtExit();
				return;
			}
        	
			command=Command.noCommand;
        }

        
        
    }
    /* (non-Javadoc)
	 * @see market.gui.ItemCollectorGuiInterfaces#UpdateInventoryLevel()
	 */
    @Override
	public void UpdateInventoryLevel(){
    	if (panel == null)
    		return;
    	panel.UpdateInventoryLevelWithoutButton();
    }
    
    /* (non-Javadoc)
	 * @see market.gui.ItemCollectorGuiInterfaces#GoToWork()
	 */
    @Override
	public void GoToWork(){
    	xDestination = ExitX1;
    	yDestination = ExitY1;
    	command = command.GoToWork1;
    }
    
    public void ContinueToWork(){
    	xDestination = HomePosX;
    	yDestination = HomePosY;
    	command = command.GoHome;
    }
    
    /* (non-Javadoc)
	 * @see market.gui.ItemCollectorGuiInterfaces#BackReadyStation()
	 */
    @Override
	public void BackReadyStation(){
    	xDestination = HomePosX;
    	yDestination = HomePosY;
    	command=Command.GoHome;
    	UpdateInventoryLevel();
    }
    
    /* (non-Javadoc)
	 * @see market.gui.ItemCollectorGuiInterfaces#CollectItems()
	 */
    @Override
	public void CollectItems(){
    	xDestination = CollectItemX;
    	yDestination = CollectItemY;
    	command=Command.CollectItem;
    }
    
    /* (non-Javadoc)
	 * @see market.gui.ItemCollectorGuiInterfaces#OffWork()
	 */
    @Override
	public void OffWork(){
    	xDestination = ExitX1;
    	yDestination = ExitY1;
    	command=Command.GoToExit1;
    }
    
    /* (non-Javadoc)
	 * @see market.gui.ItemCollectorGuiInterfaces#ContinueOffWork()
	 */
    @Override
	public void ContinueOffWork(){
    	xDestination = ExitX;
    	yDestination = ExitY;
    	command=Command.GoToExit;
    }

    /* (non-Javadoc)
	 * @see market.gui.ItemCollectorGuiInterfaces#draw(java.awt.Graphics2D)
	 */
    @Override
	public void draw(Graphics2D g) {
        g.setColor(Color.PINK);
        g.fillRect(xPos, yPos, ItemCollectorWidth, ItemCollectorHeight);
    }

    /* (non-Javadoc)
	 * @see market.gui.ItemCollectorGuiInterfaces#isPresent()
	 */
    @Override
	public boolean isPresent() {
        return true;
    }

    /* (non-Javadoc)
	 * @see market.gui.ItemCollectorGuiInterfaces#getXPos()
	 */
    @Override
	public int getXPos() {
        return xPos;
    }

    /* (non-Javadoc)
	 * @see market.gui.ItemCollectorGuiInterfaces#getYPos()
	 */
    @Override
	public int getYPos() {
        return yPos;
    }
}
