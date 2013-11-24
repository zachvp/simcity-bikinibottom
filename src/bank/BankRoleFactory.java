package bank;

import java.util.HashMap;
import java.util.Map;

import agent.Role;
import agent.RoleFactory;
import agent.interfaces.Person;



public class BankRoleFactory implements RoleFactory{

	Map<Person,bank.BankCustomerRole> bankCustomerMap = new HashMap<Person, bank.BankCustomerRole>();

	@Override
	public Role getCustomerRole(Person person) {
		
		if(bankCustomerMap.containsKey(person)){
			return bankCustomerMap.get(person);
		}
		else {
			BankCustomerRole bankCustomerRole = new BankCustomerRole(person);
			bankCustomerRole.setPerson(person);
			person.addRole(bankCustomerRole);
			return bankCustomerRole;
		}
	}
}