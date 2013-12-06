package market.interfaces;

import java.awt.Graphics2D;

import agent.gui.Gui;

public interface DeliveryGuyGuiInterfaces extends Gui{

	public abstract void updatePosition();

	public abstract void BackReadyStation();

	public abstract void GoDeliver();

	public abstract void OffWork();

	public abstract void draw(Graphics2D g);

	public abstract boolean isPresent();

	public abstract int getXPos();

	public abstract int getYPos();

}