package restaurant.strottma;

import java.util.HashMap;
import java.util.Map;

import CommonSimpleClasses.CityLocation;
import agent.PersonAgent;
import agent.Role;
import agent.RoleFactory;
import agent.interfaces.Person;

/**
 * Creates Roles from restaurant.strottma.
 * TODO implement RoleFactory in RestaurantStrottma Building class instead
 * 
 * @author Erik Strottmann
 */
public class RestaurantStrottmaRoleFactory implements RoleFactory {
	private CityLocation restaurant;
	private Map<Person, Role> existingRoles;
	
	/**
	 * @param restaurant the CityLocation of the restaurant; added to every
	 * 		  Role this factory creates
	 */
	public RestaurantStrottmaRoleFactory(CityLocation restaurant) {
		this.restaurant = restaurant;
		this.existingRoles = new HashMap<Person, Role>();
	}
	
	@Override
	public Role getCustomerRole(Person person) {
		Role role = existingRoles.get(person);
		if (role == null) {
			role = new CustomerRole(person);
			role.setLocation(restaurant);
		} else {
			role.setPerson(person);
		}
		
		person.addRole(role);
		return role;
	}

}
