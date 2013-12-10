package transportation.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import transportation.gui.CarCrashSequence.RemovalTask;
import transportation.interfaces.Corner;
import kelp.Kelp;
import kelp.KelpClass;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import CommonSimpleClasses.SingletonTimer;
import CommonSimpleClasses.XYPos;
import agent.gui.Gui;

public class PedestrianCrashSequence implements Gui {
	
	private int posX1;
	private int posY1;
	private int posX2;
	private int posY2;
	private boolean explosion = false;
	int aniCount = 0;
	
	BufferedImage flyingDutchmanImage;
	private int posX3;
	private int posY3;
	
	class RemovalTask extends TimerTask {
		
		PedestrianCrashSequence carCrashSequence;
		
		public RemovalTask(PedestrianCrashSequence carCrashSequence) {
			this.carCrashSequence = carCrashSequence;
		}
		
		@Override
		public void run() {
			TransportationGuiController.getInstance()
				.removeGui(carCrashSequence);
		}
		
	}
	
	public PedestrianCrashSequence() {
		Kelp kelp = KelpClass.getKelpInstance();
		Corner bottomLeftCorner = (Corner) kelp.placesNearMe
				(new XYPos(0,20000), LocationTypeEnum.Corner).get(0);
		Corner bottomRightCorner = (Corner) kelp.placesNearMe
				(new XYPos(20000,20000), LocationTypeEnum.Corner).get(0);
		
		posX1 = bottomLeftCorner.position().x - 900;
		posY1 = bottomLeftCorner.position().y - VehicleGuiClass.VEHICLEH;
		posX2 = bottomRightCorner.position().x + 100;
		posY2 = bottomRightCorner.position().y - PassengerGuiClass.PASSENGERH;
		
			try {
				String filename = "flying_dutchman.png";
				flyingDutchmanImage = ImageIO.read(getClass().getResource(filename));
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}

	@Override
	public void updatePosition() {
		posX1 += 8;
		if (posX1 < posX2){
			posX2 -= 1;
		} else if (aniCount == 0) {
			posX3 = posX2-40;
			posY3 = posY2+100;
			aniCount++;
		} else if (aniCount == 1) {
			posY3--;
			if (posY3 < posY2-30) {
				aniCount++;
			}
		} else if (aniCount >= 2 && aniCount <= 30) {
			aniCount++;
		} else if (aniCount == 31) {
			posY3++;
			if (posY3 > posY2+100) {
				aniCount++;
			}
		} else if (aniCount == 32) {
			aniCount++;
			SingletonTimer.getInstance().schedule
				(new RemovalTask(this), 0);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if (aniCount < 31) {
			g.setColor(Color.GREEN);
			g.fillRect(posX2, posY2,
					PassengerGuiClass.PASSENGERH, PassengerGuiClass.PASSENGERH);
			g.setColor(Color.RED);
			g.fillRect(posX1, posY1,
					VehicleGuiClass.VEHICLEH, VehicleGuiClass.VEHICLEH);
		}
		if (aniCount > 0) {
			Rectangle2D r = new Rectangle2D.Double(posX3, posY3,
					40, 40);
			Rectangle2D tr = new Rectangle2D.Double(posX3, posY3,
					40, 40);
			TexturePaint tp = new TexturePaint(flyingDutchmanImage, tr);
			g.setPaint(tp);
			g.fill(r);
		}
		/*
		if (aniCount<16) {
			Rectangle2D r = new Rectangle2D.Double(posX1-15, posY1-15,
					30, 30);
			Rectangle2D tr = new Rectangle2D.Double(posX1-15, posY1-15,
					30, 30);
			TexturePaint tp = new TexturePaint(explosionList.get(aniCount), tr);
			g.setPaint(tp);
			g.fill(r);
		} else if (aniCount < 17) {
			aniCount++;
			SingletonTimer.getInstance().schedule
				(new RemovalTask(this), 0);
		}
		*/
	}

	@Override
	public boolean isPresent() {
		return true;
	}

}
