package market.interfaces;

import java.awt.Graphics2D;

import agent.gui.Gui;
import market.gui.MarketInfoPanel;

public interface ItemCollectorGuiInterfaces extends Gui{

	public abstract void setItemCollectorNumber(int i);

	public abstract void setMarketControlPanel(MarketInfoPanel p);

	public abstract void updatePosition();

	public abstract void UpdateInventoryLevel();

	public abstract void BackReadyStation();

	public abstract void CollectItems();

	public abstract void OffWork();

	public abstract void draw(Graphics2D g);

	public abstract boolean isPresent();

	public abstract int getXPos();

	public abstract int getYPos();

	public abstract void GoToWork();

}