package bank.test.mock;


import java.util.Map;

import bank.interfaces.BankCustomer;
import bank.interfaces.LoanManager;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Jack Lucas
 *
 */
public class MockLoanManager extends Mock implements LoanManager {

        /**
         * Reference to the Cashier under test that can be set by the unit test.
         */
	
	public EventLog log = new EventLog();
	
        public MockLoanManager(String name) {
                super(name);

        }

		@Override
		public void msgINeedALoan(BankCustomer bc, double loanAmount) {
			log.add("received loan message" + loanAmount);
			
		}

		
       

}