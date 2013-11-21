package bank.interfaces;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Jack Lucas
 *
 */
public interface BankCustomer {

        public abstract void msgGotToTeller();

        public abstract void msgAccountOpened(int accountIdNumber);
        
        public abstract void msgDepositSuccessful(double depositAmount);
        
        public abstract void msgWithdrawSuccessful(double withdrawAmound);
        
        public abstract void msgLoanApproved(double amount);
        
        public abstract void msgSpeakToLoanManager(LoanManager lm);
}