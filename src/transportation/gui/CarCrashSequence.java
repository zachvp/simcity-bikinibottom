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

import transportation.interfaces.Corner;
import kelp.Kelp;
import kelp.KelpClass;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import CommonSimpleClasses.SingletonTimer;
import CommonSimpleClasses.XYPos;
import agent.gui.Gui;

public class CarCrashSequence implements Gui {
	
	private int posX1;
	private int posY1;
	private int posX2;
	private int posY2;
	private boolean explosion = false;
	int aniCount = 0;
	
	List<BufferedImage> explosionList = new ArrayList<BufferedImage>();
	
	class RemovalTask extends TimerTask {
		
		CarCrashSequence carCrashSequence;
		
		public RemovalTask(CarCrashSequence carCrashSequence) {
			this.carCrashSequence = carCrashSequence;
		}
		
		@Override
		public void run() {
			TransportationGuiController.getInstance()
				.removeGui(carCrashSequence);
		}
		
	}
	
	public CarCrashSequence() {
		Kelp kelp = KelpClass.getKelpInstance();
		Corner bottomLeftCorner = (Corner) kelp.placesNearMe
				(new XYPos(0,20000), LocationTypeEnum.Corner).get(0);
		Corner bottomRightCorner = (Corner) kelp.placesNearMe
				(new XYPos(20000,20000), LocationTypeEnum.Corner).get(0);
		
		posX1 = bottomLeftCorner.position().x - 500;
		posY1 = bottomLeftCorner.position().y - VehicleGuiClass.VEHICLEH;
		posX2 = bottomRightCorner.position().x + 500;
		posY2 = bottomRightCorner.position().y - VehicleGuiClass.VEHICLEH;
		
		for (int i = 1; i <= 17; i++) {
			try {
				String filename = String.format("explosion/explosion%03d.png", i);
				explosionList.add(ImageIO.read(getClass().getResource(filename)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void updatePosition() {
		if (posX1 < posX2){
			posX1 += 8;
			posX2 -= 8;
		} else if (aniCount < 16) {
			explosion = true;
			aniCount++;
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if (!explosion) {
			g.setColor(Color.RED);
			g.fillRect(posX1, posY1,
				VehicleGuiClass.VEHICLEH, VehicleGuiClass.VEHICLEH);
			g.fillRect(posX2, posY2,
				VehicleGuiClass.VEHICLEH, VehicleGuiClass.VEHICLEH);
		} else if (aniCount<16) {
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
	}

	@Override
	public boolean isPresent() {
		return true;
	}

}
