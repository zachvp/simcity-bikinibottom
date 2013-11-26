package bank.test.mock;


import java.util.Map;

import bank.interfaces.BankCustomer;
import bank.interfaces.LoanManager;

/**
 *
 * @author Jack Lucas
 *
 */
public class MockLoanManager extends Mock implements LoanManager {

	
	public EventLog log = new EventLog();
	
        public MockLoanManager(String name) {
                super(name);

        }

		@Override
		public void msgINeedALoan(BankCustomer bc, double loanAmount) {
			log.add("received loan message" + loanAmount);
			
		}

		
       

}