package bank.test.mock;


import bank.interfaces.AccountManager;
import bank.interfaces.BankCustomer;
import bank.interfaces.Robber;
import bank.interfaces.Teller;


/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Jack Lucas
 *
 */
public class MockAccountManager extends Mock implements AccountManager {

	
	public EventLog log = new EventLog();
	

        public MockAccountManager(String name) {
                super(name);

        }

		@Override
		public void msgOpenNewAccount(Teller t, BankCustomer bc,
				double initialDeposit) {
			log.add("new account" + initialDeposit);
			
		}

		@Override
		public void msgDepositMoney(Teller t, BankCustomer bc, int accountId,
				double amount) {
			log.add("deposit" + amount);
			
		}

		@Override
		public void msgWithdrawMoney(Teller t, BankCustomer bc, int AccountId,
				double amount) {
			log.add("withdraw" + amount);
					
		}

		@Override
		public void msgAtDestination() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgGiveMeTheMoney(Robber r, double amount) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgUpdateMoney(double amount) {
			// TODO Auto-generated method stub
			
		}

		
		
       

}