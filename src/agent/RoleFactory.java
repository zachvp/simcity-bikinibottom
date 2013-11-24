package agent;

import agent.interfaces.Person;

/**
 * An interface with methods that, given a String, create the corresponding
 * Role.
 * 
 * @author Erik Strottmann
 */
public interface RoleFactory {		
	/**
	 * Instantiates the CustomerRole for your building, sets the Role's
	 * Agent to the given PersonAgent, and adds the Role to the PersonAgent's
	 * list of Roles. Returns the Role.
	 * 
	 * NOTE: Keep a Map<Person, Role> of all existing roles, so that you
	 * do not instantiate a new one when asked for the role a second time.
	 * 
	 * @throws IllegalArgumentException when the roleType doesn't exist
	 */
	public Role getCustomerRole(Person person)
			throws IllegalArgumentException;
}
