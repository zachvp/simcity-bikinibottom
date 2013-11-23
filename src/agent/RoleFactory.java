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
	 * Instantiates the Role corresponding to roleType, sets the Role's
	 * Agent to the given PersonAgent, and adds the Role to the PersonAgent's
	 * list of Roles. Returns the Role.
	 * 
	 * @throws IllegalArgumentException when the roleType doesn't exist
	 */
	public Role getRole(String roleType, Person person)
			throws IllegalArgumentException;
}
