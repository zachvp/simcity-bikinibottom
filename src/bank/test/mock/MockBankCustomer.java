package bank.test.mock;


import bank.interfaces.BankCustomer;
import bank.interfaces.LoanManager;
import bank.interfaces.SecurityGuard;
import bank.interfaces.Teller;



/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Jack Lucas
 *
 */
public class MockBankCustomer extends Mock implements BankCustomer {

        /**
         * Reference to the Cashier under test that can be set by the unit test.
         */
	
	public EventLog log = new EventLog();
	

        public MockBankCustomer(String name) {
                super(name);

        }

		@Override
		public void msgGotToTeller() {
			
			
		}
		
		public void msgGoToTeller(int xLoc, Teller t){
			
		}

		@Override
		public void msgAccountOpened(int accountIdNumber) {
			log.add("new account"+ accountIdNumber);
			
		}

		@Override
		public void msgDepositSuccessful(double depositAmount) {
			log.add("deposited"+ depositAmount);
			
		}

		@Override
		public void msgWithdrawSuccessful(double withdrawAmount) {
			log.add("withdraw"+ withdrawAmount);
			
		}

		@Override
		public void msgLoanApproved(double amount) {
			
			
		}

		@Override
		public void msgSpeakToLoanManager(LoanManager lm, int xLoc) {
			log.add("sent to loanmanager" + lm);
			
		}
		
		public void msgGoToSecurityGuard(SecurityGuard sg) {
			
		}

		@Override
		public void msgAtDestination() {
			// TODO Auto-generated method stub
			
		}

		

}