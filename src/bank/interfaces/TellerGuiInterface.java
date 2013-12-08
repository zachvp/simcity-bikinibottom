package bank.interfaces;


public interface TellerGuiInterface {
	public void DoGoToAccountManager();
	
	public void DoGoToLoanManager();
	
	public void DoGoToDesk(int xFactor);
	
	public void DoGoToWorkstation(int xFactor);
	
	public void DoEndWorkDay();
	
	public void DoLeaveBank();
	
	public void DoGoToFrontDesk();
	
	public void DoGoToBackDesk();
	
	public void DoGoToRightOfFrontDesk();
	
}