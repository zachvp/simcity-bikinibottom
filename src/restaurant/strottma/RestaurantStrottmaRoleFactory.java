package restaurant.strottma;

import CommonSimpleClasses.CityLocation;
import agent.PersonAgent;
import agent.Role;
import agent.RoleFactory;

public class RestaurantStrottmaRoleFactory implements RoleFactory {
	public static final String CASHIER_ROLE = "CashierRole";
	public static final String COOK_ROLE = "CookRole";
	public static final String CUSTOMER_ROLE = "CustomerRole";
	public static final String HOST_ROLE = "HostRole";
	public static final String WAITER_ROLE = "WaiterRole";
	public static final String SHARED_DATA_WAITER_ROLE = "SharedDataWaiterRole";
	
	private CityLocation restaurant;
	
	/**
	 * @param restaurant the CityLocation of the restaurant; added to every
	 * 		  Role this factory creates
	 */
	public RestaurantStrottmaRoleFactory(CityLocation restaurant) {
		this.restaurant = restaurant;
	}
	
	/**
	 * Instantiates the Role corresponding to roleType, sets the Role's
	 * Agent to the given PersonAgent, sets the Role's location to the
	 * restaurant passed into this's constructor, and adds the Role to the
	 * PersonAgent's list of Roles. Returns the Role.
	 * 
	 * @throws IllegalArgumentException when the roleType doesn't exist
	 */
	public Role getRole(String roleType, PersonAgent personAgent) {
		Role role = null;
		
		if (roleType.equals(CASHIER_ROLE)) {
			role = new CashierRole(personAgent);
			
		} else if (roleType.equals(COOK_ROLE)) {
			role = new CookRole(personAgent);
			
		} else if (roleType.equals(CUSTOMER_ROLE)) {
			role = new CustomerRole(personAgent);
			
		} else if (roleType.equals(HOST_ROLE)) {
			role = new HostRole(personAgent);
			
		} else if (roleType.equals(WAITER_ROLE)) {
			role = new WaiterRole(personAgent);
			
		} else if (roleType.equals(SHARED_DATA_WAITER_ROLE)) {
			System.out.println("ERROR in RestaurantStrottmaRoleFactory: "
					+ "SharedDataWaiterRole not yet defined.");
		} else {
			throw new IllegalArgumentException("The given roleType does not "
					+ "exist in package restaurant.strottma!");
		}
		
		if (role != null) {
			role.setLocation(restaurant);
			personAgent.addRole(role);
		}
		
		return role;
	}

}
