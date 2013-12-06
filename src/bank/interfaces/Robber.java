package bank.interfaces;


public interface Robber {

   public abstract void msgAttemptToStop(AccountManager am, boolean isSuccessful);
   
   public abstract void msgGiveMoneyToRobber(double amount);

	public abstract String getName();

	public abstract void msgAtDestination();
}