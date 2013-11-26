package bank.interfaces;


public interface Teller {

        public abstract void msgIWantToOpenAccount(BankCustomer bc, double initialDeposit);
        
        public abstract void msgDepositMoney(BankCustomer bc, int accountId, double depositAmount) ;
        
        public abstract void msgWithdrawMoney(BankCustomer bc, int accountId, double withdrawAmount) ;
        
        public abstract void msgNewAccountVerified(BankCustomer bc, int accountId);
        
        public abstract void msgDepositSuccessful(BankCustomer bc, int accountId, double depositAmount);
        
        public abstract void msgWithdrawSuccessful(BankCustomer bc, int accountId, double withdrawAmount);
        
        public abstract void msgINeedALoan(BankCustomer bc);
        
        public abstract void msgAtDestination();
        }