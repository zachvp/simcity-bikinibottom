package bank;

import CommonSimpleClasses.CityLocation;
import agent.interfaces.Person;
import bank.interfaces.BankCustomer;

public class BusinessBankCustomerRole extends BankCustomerRole implements BankCustomer {

	double money;
	int accountId = -1;
	
	public BusinessBankCustomerRole(Person person, CityLocation bank) {
		super(person, bank);
	}
	
	public void msgUpdateRestaurantMoney(double money) {//sent from CashierRole
		this.money += money;
	}
	
}