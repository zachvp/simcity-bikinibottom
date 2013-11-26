package bank.test.mock;


import java.util.Map;

import bank.interfaces.BankCustomer;
import bank.interfaces.Teller;


/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Jack Lucas
 *
 */
public class MockTeller extends Mock implements Teller {

        /**
         * Reference to the Cashier under test that can be set by the unit test.
         */
	
	public EventLog log = new EventLog();

        public MockTeller(String name) {
                super(name);

        }

		@Override
		public void msgIWantToOpenAccount(BankCustomer bc, double initialDeposit) {
			log.add("received message" + initialDeposit);
			
		}

		@Override
		public void msgDepositMoney(BankCustomer bc, int accountId,
				double depositAmount) {
			log.add("deposit" + depositAmount);
			
		}

		@Override
		public void msgWithdrawMoney(BankCustomer bc, int accountId,
				double withdrawAmount) {
			log.add("withdraw" + withdrawAmount);
			
		}

		@Override
		public void msgNewAccountVerified(BankCustomer bc, int accountId) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgDepositSuccessful(BankCustomer bc, int accountId,
				double depositAmount) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgWithdrawSuccessful(BankCustomer bc, int accountId,
				double withdrawAmount) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgINeedALoan(BankCustomer bc) {
			log.add("received loan message");

			
		}

		@Override
		public void msgAtDestination() {
			// TODO Auto-generated method stub
			
		}


		
       

}