package market;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agent.Role;
import agent.RoleFactory;
import agent.interfaces.Person;

	//Check if the customer has CustomerRole or not
	//If not creates one of them
	//Use a map of person to role
public class MarketRoleFactory implements RoleFactory{

	Map<Person,market.CustomerRole> MarketCustomerMap = new HashMap<Person, market.CustomerRole>();
	
	@Override
	public Role getCustomerRole(Person person) {
		List<Item> ShoppingList = new ArrayList<Item>();
		Map<String,Integer> GroceryList = person.getResidentRole().getGroceries();
		{
			//FOODS
			for (int i=0;i<agent.Constants.FOODS.size();i++){
				ShoppingList.add(new Item(agent.Constants.FOODS.get(i),GroceryList.get(agent.Constants.FOODS.get(i))));
			}
			//CARS
			for (int i=0;i<agent.Constants.CARS.size();i++){
				ShoppingList.add(new Item(agent.Constants.CARS.get(i),GroceryList.get(agent.Constants.CARS.get(i))));
			}
		}
		
		if (MarketCustomerMap.containsKey(person)){
			MarketCustomerMap.get(person).setShoppingList(ShoppingList);
			return MarketCustomerMap.get(person);
		}
		else {
			CustomerRole role = new CustomerRole(person.getName(), person.getWallet().getCashOnHand(), ShoppingList, person);
			role.setLocation(person.getPassengerRole().getLocation());
			role.setPerson(person);
			person.addRole(role);
			return role;
		}
		
	}
	

}