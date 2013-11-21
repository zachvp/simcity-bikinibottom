package bank.interfaces;


public interface AccountManager {

	public abstract void msgOpenNewAccount(Teller t, BankCustomer bc, double intitialDeposit);
	
	public abstract void msgDepositMoney(Teller t, BankCustomer bc, int accountId, double amount);
	
	public abstract void msgWithdrawMoney(Teller t, BankCustomer bc, int AccountId, double amount);
	
}