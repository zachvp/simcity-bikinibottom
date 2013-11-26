package agent.interfaces;

import java.util.Map;

import market.Item;
import housing.ResidentRole;
import transportation.PassengerRole;
import transportation.interfaces.Car;
import agent.PersonAgent.HungerLevel;
import agent.Role;
import agent.WorkRole;

public interface Person {
	/* -------- Messages -------- */
	
	/** Called by PassengerRole when it arrives at its destination. */
	public void msgArrivedAtDestination();
		
	/* -------- Utilities -------- */
	
	/**
	 * Adds the given Role to the Person's list. The person should not call
	 * the Role's scheduler if the Role is inactive.
	 */
	public void addRole(Role r);
	
	/**
	 * @see Wallet
	 */
	public Wallet getWallet();
	
	/**
	 * @see Car
	 */
	public Car getCar();
	
	/**
	 * @see Car
	 */
	public void setCar(Car car);
	
	
	// ---- Methods for finding special roles
	
	/**
	 * Returns the PersonAgent's PassengerRole, or the first one if there's
	 * more than one for some reason.
	 * 
	 * @return the first PassengerRole; null if none exists
	 */
	public PassengerRole getPassengerRole();
	
	/**
	 * Returns the PersonAgent's ResidentRole, or the first one if there's more
	 * than one for some reason.
	 * 
	 * @return the first ResidentRole; null if none exists
	 */
	public ResidentRole getResidentRole();
	
	/**
	 * Returns the PersonAgent's WorkRole, or the first one if there's more
	 * than one. Multiple-job support is planned for v3.
	 * 
	 * @return the first WorkRole; null if none exists
	 */
	public WorkRole getWorkRole();
	
	
	// ---- Boolean methods (for deciding what to do next)
	
	/**
	 * If work starts soon, the Person shouldn't start any tasks that are less
	 * important than work.
	 */
	public boolean workStartsSoon();
	
	/**
	 * If starving, finding food should be one of the Person's highest
	 * priorities.
	 */
	public boolean isStarving();
	
	/** If hungry, the person should try to get food! */
	public boolean isHungry();
	
	
	// ---- Inner classes
	
	/**
	 * Holds a Person's money on hand, and thresholds for when the Person will
	 * go to the bank to withdraw or deposit cash.
	 */
	public static class Wallet {
		private double cashOnHand;
		private double tooMuch;
		private double tooLittle;
		private double moneyNeeded;
		
		private IncomeLevel incomeLevel;
		
		public enum IncomeLevel {POOR, MEDIUM, RICH}
				
		/**
		 * Creates a wallet with amounts of money corresponding to the
		 * income level.
		 * 
		 * @param incomeLevel one of POOR, MEDIUM, or RICH
		 */
		public Wallet(IncomeLevel incomeLevel) {
			this.incomeLevel = incomeLevel;
			
			switch (incomeLevel) {
				case POOR:
					this.cashOnHand = 5;
					this.tooMuch = 50;
					this.tooLittle = 0;
					this.moneyNeeded = 0;
					break;
				case RICH:
					this.cashOnHand = 100;
					this.tooMuch = 300;
					this.tooLittle = 50;
					this.moneyNeeded = 0;
					break;
				case MEDIUM:
					this.cashOnHand = 30;
					this.tooMuch = 60;
					this.tooLittle = 15;
					this.moneyNeeded = 0;
					// fall through to default
				default:
					break;
			}
		}
		
		/**
		 * Creates a new medium income level wallet.
		 * 
		 * @see #Wallet(IncomeLevel)
		 */
		
		public IncomeLevel getIncomeLevel() {
			return this.incomeLevel;
		}
		
		public double getCashOnHand() { return this.cashOnHand; }
		public double getTooMuch() { return this.tooMuch; }
		public double getTooLittle() { return this.tooLittle; }
		public double getMoneyNeeded() { return this.moneyNeeded; }
		
		public void setCashOnHand(double coh) { this.cashOnHand = coh; }
		public void setTooMuch(double tm) { this.tooMuch = tm; }
		public void setTooLittle(double tl) { this.tooLittle = tl; }
		public void setMoneyNeeded(double mn) { this.moneyNeeded = mn; }
		
		/** Adds cash to cashOnHand. */
		public void addCash(double cash) { this.cashOnHand += cash; }
		/**
		 * Subtracts cash from cashOnHand. Careful! This method doesn't verify
		 * that cashOnHand has enough cash, so you have to do that yourself.
		 */
		public void subtractCash(double cash) { this.cashOnHand -= cash; }
		
		public boolean hasTooMuch() {
			return cashOnHand > tooMuch;
		}
		
		public boolean hasTooLittle() {
			return cashOnHand < tooLittle;
		}

		public boolean needsMoney() {
			return moneyNeeded > 0;
		}
	}

	/** Call this when state changes, requiring the Preson's attention. */
	public void agentStateChanged();
	
    /** Return Person name for messages.*/
	public String getName();
	
    /** The simulated action code */
	public void agentDo(String msg);
	
	/** Print message */
	public void printMsg(String msg);
	
    /** Print message with error */
	public void printMsg(String msg, Throwable e);

	public void setHungerLevel(HungerLevel hungry);
	
	/** The {@link Item}s a person currently has. */
	Map<String, Integer> getInventory();
	
	/** Adds amount {@link Item}s of type name to the inventory. */
	void addItemsToInventory(String name, int amount);
	
	/**
	 * Removes up to amount {@link Item}s of type name from the inventory. If
	 * there aren't that many, remove all.
	 */
	void removeItemsFromInventory(String name, int amount);
	
}
