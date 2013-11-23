package housing.gui;

import housing.PayRecipientRole;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import agent.PersonAgent;
import agent.Role;
import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.XYPos;

/**
 * HousingComplex is the equivalent of one building unit. It has 4 subdivisions
 * that each contain a smaller residential unit. 
 * @author Zach VP
 *
 */
public class HousingComplex extends JPanel implements CityBuilding {
	/* --- Data --- */
	private final int UNIT_COUNT = 4;
	int WINDOWX = 600;
	int WINDOWY = 600;
	
	private final int ROWS = 2;
	private final int COLUMNS = 2;
	
	// layout manager
	GridLayout complexLayout = new GridLayout(ROWS, COLUMNS);

	// payRecipient who manages the complex 
 	PersonAgent payRecipientPerson = new PersonAgent("Pay Recipient");
 	PayRecipientRole payRecipientRole = new PayRecipientRole(payRecipientPerson);
	
	private List<HousingGui> housingUnits = new ArrayList<HousingGui>();
	
	public HousingComplex() {
//		this.setLayout(complexLayout);
		
		for(int i = 0; i < UNIT_COUNT; i++){
			HousingGui gui = new HousingGui(i, payRecipientRole, WINDOWX/COLUMNS-50, WINDOWY/ROWS-50);
			this.add(gui);
			housingUnits.add(gui);
		}
		// activate complex manager
		startAndActivate(payRecipientPerson, payRecipientRole);
	}

	private void startAndActivate(PersonAgent agent, Role role) {
		agent.startThread();
		agent.addRole(role);
		role.activate();
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
	
	@Override
	public Role getGreeter() {
		return payRecipientRole;
	}
}
