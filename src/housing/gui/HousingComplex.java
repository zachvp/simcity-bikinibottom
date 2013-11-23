package housing.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.XYPos;

/**
 * HousingComplex is the equivalent of one building unit. It has 4 subdivisions
 * that each contain a smaller residential unit. 
 * @author Zach VP
 *
 */
public class HousingComplex extends JFrame implements CityBuilding {
	/* --- Data --- */
	private final int UNIT_COUNT = 4;
	int WINDOWX = 550;
    int WINDOWY = 600;
	
	private List<HousingGui> housingUnits = new ArrayList<HousingGui>();
	
	public HousingComplex() {
		for(int i = 0; i < UNIT_COUNT; i++){
			HousingGui gui = new HousingGui(i);
			housingUnits.add(gui);
			add(gui);
		}
		setBounds(50, 50, WINDOWX, WINDOWY);
	}

	@Override
	public LocationTypeEnum type() {
		return LocationTypeEnum.Apartment;
	}

	@Override
	public XYPos position() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XYPos entrancePos() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// TODO this is simply a test main() method
	public static void main(String[] args) {
		HousingComplex housingComplex = new HousingComplex();
		housingComplex.setTitle("Housing Complex");
		housingComplex.setVisible(true);
		housingComplex.setResizable(false);
		housingComplex.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
