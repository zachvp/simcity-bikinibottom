package restaurant.strottma.gui;

import gui.Building;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import restaurant.strottma.CustomerRole;
import restaurant.strottma.HostRole;
import CommonSimpleClasses.XYPos;
import agent.Constants;
import agent.Role;
import agent.interfaces.Person;

/**
 * Represents Erik Strottmann's restaurant.
 * 
 * @author Erik Strottmann
 * @see Building
 */
public class RestaurantStrottmaBuilding extends Building {
	private Map<Person, Role> existingRoles;
	private HostRole host;
	private InfoPanel infoPanel = new InfoPanel();
	private AnimationPanel animationPanel = new AnimationPanel();
	
	public RestaurantStrottmaBuilding(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.existingRoles = new HashMap<Person, Role>();
	}
	
	public void setHost(HostRole host) {
		this.host = host;
	}
	
	/**
	 * Returns the entrance of this building: on its bottom side.
	 */
	@Override
	public XYPos entrancePos() {
		XYPos pos = position();
		pos.x +=  (int) Constants.BUILDING_WIDTH/2;
		pos.y += 0;
		return pos;
	}
	
	@Override
	public Role getGreeter() {
		return host;
	}
	
	@Override
	public LocationTypeEnum type() {
		return LocationTypeEnum.Restaurant;
	}
	
	@Override
	public Role getCustomerRole(Person person) {
		Role role = existingRoles.get(person);
		if (role == null) {
			// Create a new role if none exists
			role = new CustomerRole(person, this);
			role.setLocation(this);
		} else {
			// Otherwise use the existing role
			role.setPerson(person);
		}
		
		// Add the role to the person, and return it.
		person.addRole(role);
		return role;
	}
	
	@Override
	public JPanel getAnimationPanel() {
		// TODO initialize the animation panel
		//JPanel animationPanel = new agent.gui.AnimationPanel();
		return animationPanel;
	}
	
	@Override
	public JPanel getInfoPanel() {
		// TODO initialize the info panel
		return infoPanel;
	}
	
}
