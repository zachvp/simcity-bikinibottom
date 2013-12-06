package market.interfaces;

import java.awt.Graphics2D;

import agent.gui.Gui;
import market.gui.MarketInfoPanel;

public interface CashierGuiInterfaces extends Gui {

	public abstract void setMarketInfoPanel(MarketInfoPanel p);

	public abstract void updatePosition();

	public abstract void GoToFrontDesk();

	public abstract void GoToBench();

	public abstract void OffWork();

	public abstract void ContinueOffWork();

	public abstract void Update();

	public abstract void draw(Graphics2D g);

	public abstract boolean isPresent();

	public abstract int getXPos();

	public abstract int getYPos();

	void WalkToWork();

	public abstract void GoToWork();

};
