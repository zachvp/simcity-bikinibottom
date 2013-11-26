package market.interfaces;

import java.awt.Graphics2D;

import agent.gui.Gui;

public interface CustomerGuiInterfaces extends Gui{

	public static final int OffScreenX = 230;
	public static final int OffScreenY = -150;
	public static final int CustomerWidth = 15;
	public static final int CustomerHeight = 15;
	public static final int xFrontDesk = 220;
	public static final int yFrontDesk = 100;
	public static final int xTable = 50;
	public static final int yTable = 50;

	public abstract void updatePosition();

	public abstract void draw(Graphics2D g);

	public abstract boolean isPresent();

	public abstract void setBuying();

	public abstract boolean isBuying();

	public abstract void setPresent(boolean p);

	public abstract void DoGoToFrontDesk();

	public abstract void DoExitMarket();

}