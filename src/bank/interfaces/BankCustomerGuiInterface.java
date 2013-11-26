package bank.interfaces;

public interface BankCustomerGuiInterface {
	
	public void DoGoToTeller(int xLoc, double money, double account);
	
	public void DoLeaveBank(double money, double account);
	
	public void DoGoToLoanManager(int x, double money, double account);
	
	public void DoGoToSecurityGuard(double money, double account);
	
	public void DoEndWorkDay();
	
}