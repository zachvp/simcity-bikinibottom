package market.gui;


import market.CashierAgent;
import market.CustomerAgent;
import market.interfaces.Cashier;

import java.awt.*;

public class CashierGui implements Gui {

    private Cashier agent = null;

    private int xPos = 170, yPos = 250;//default cashier position
    private int xDestination = 170, yDestination = 250;//default start position
    
    private static final int CashierWidth = 15;
    private static final int CashierHeight = 15;

    public CashierGui(Cashier ca) {
        this.agent = ca;
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
}
