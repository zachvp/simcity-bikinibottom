package agent;

import housing.ResidentRole;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import agent.Role;

/**
 * Person Agent.
 * 
 * @author Erik Strottmann
 */
public class PersonAgent extends Agent {
	private String name;
	private List<Role> roles;
	
	private PersonEvent event;
	private HungerLevel hungerLevel;
	
	private TimeManager timeManager;
	private Timer timer;
	
	private long lastTimeEatingOut;
	/**
	 * How long this person likes to wait between each time eating out (in game
	 * time, not real time)
	 */
	private long eatingOutWaitPeriod;
	
	/**
	 * If there is less than this much time before work starts, the person
	 * considers work to be starting soon. (In game time, not real time.)
	 */
	private long workStartThreshold;
	
	private Wallet wallet;
	
	public PersonAgent(String name){
		super();
		
		this.name = name;
		this.roles = new ArrayList<Role>();
		
		this.event = PersonEvent.NONE;
		this.hungerLevel = HungerLevel.NEUTRAL;
		
		this.timeManager = TimeManager.getInstance();
		this.timer = new Timer();
		
		this.lastTimeEatingOut = timeManager.fakeStartTime();
		this.eatingOutWaitPeriod = 1000 * 60 * 60 * 24; // one day
		
		this.workStartThreshold = 1000 * 60 * 30; // 30 minutes
		
		// TODO Add meaningful defaults for wallet
		this.wallet = new Wallet(20.00, 50.00, 10.00);
	}
	
	/* -------- Messages -------- */
	
	// from PassengerRole
	void msgArrivedAtDestination() {
		event = PersonEvent.ARRIVED_AT_LOCATION;
		stateChanged();
	}
	
	/* -------- Scheduler -------- */
	/**
	 * Scheduler - determine what action is called for, and do it.
	 */
	@Override
	protected boolean pickAndExecuteAnAction() {
		// First, the Role rules.
		for (Role r : roles) {
			if (r.isActive() && r.pickAndExecuteAnAction()) {
				return true;
				// Note: only one role should be active at a time
			}
		}

		// If you just arrived somewhere, activate the appropriate Role. 
		
		if (event == PersonEvent.ARRIVED_AT_LOCATION) {
			// TODO uncomment scheduler
			// activateRole(getPassengerRole().getLocation());
		}
		

		// If you didn't just arrive somewhere, decide what to do next.
		
		if (workStartsSoon()) {
			goToWork();
			return true;
		} else if (isStarving()) {
			if (wantsToEatOut()) {
				// Restaurant r = chooseRestaurant();
				// goToRestaurant(r);
				return true;
			} else if (hasFoodAtHome()) {
				goHome();
				return true;
			} else {
				// Market m = chooseMarket();
				// goToMarket(m);
				return true;
			}
		} else if (needToGoToBank()) {
			// Bank b = chooseBank();
			// goToBank(b);
			return true;
		}

		// No actions were performed.
		return false;
	}

	/* -------- Actions -------- */
	
	void goHome() {
		// TODO implement goHome()
	}
	void goToWork() {
		// TODO implement goToWork()
	}
	/*
	void goToRestaurant(Restaurant r) {
		// TODO implement goToRestaurant
	}
	*/
	/*
	void goToMarket(Market m) {
		// TODO implement goToMarket
	}
	*/
	/*
	void goToBank(Bank b) {
		// TODO implement goToBank
	}
	*/
	
	/* -------- Utilities -------- */
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return "person " + getName();
	}
	
	/**
	 * Adds the given Role to the PersonAgent's list. The person will not call
	 * the Role's scheduler if the Role is inactive.
	 */
	public void addRole(Role r) {
		this.roles.add(r);
	}
	
	public Wallet getWallet() {
		return this.wallet;
	}
	
	// Eating out
	
	/**
	 * @return how long this person likes to wait between each time eating out
	 * 		   (in game time, not real time)
	 * */
	public long getEatingOutWaitPeriod() {
		return this.eatingOutWaitPeriod;
	}
	
	/**
	 * @param newWait how long this person will now like to wait between each
	 * time eating out (in game time, not real time)
	 */
	public void setEatingOutWaitPeriod(long newWait) {
		this.eatingOutWaitPeriod = newWait;
	}
	
	/** @return the date and time of the last time the person ate out. */
	public long getLastTimeEatingOut() {
		return this.lastTimeEatingOut;
	}
	
	/** Modifies the last time the person thinks he or she ate out. */
	public void setLastTimeEatingOut(long newLastTime) {
		this.lastTimeEatingOut = newLastTime;
	}
	
	// Hunger
	
	public HungerLevel getHungerLevel() {
		return this.hungerLevel;
	}
	
	/**
	 * Sets the hunger level to the given; if eatingOut, updates
	 * lastTimeEatingOut.
	 * 
	 * @param newHungerLevel
	 * @param eatingOut whether this hunger modification was due to eating out
	 * @see #getLastTimeEatingOut()
	 */
	public void setHungerLevel(HungerLevel newHungerLevel, boolean eatingOut) {
		if (eatingOut) {
			this.lastTimeEatingOut = timeManager.currentSimTime();
		}
		this.hungerLevel = newHungerLevel;
	}
	
	public void setHungerLevel(HungerLevel newHungerLevel) {
		setHungerLevel(newHungerLevel, false);
	}
	
	// Work starting soon
	
	/**
	 * If there is less than this much time before work starts, the person
	 * considers work to be starting soon. (In game time, not real time.)
	 */
	public long getWorkStartThreshold() {
		return this.workStartThreshold;
	}
	
	/**
	 * @param if there is less than this much time before work starts, the
	 * person will now consider work to be starting soon. (In game time, not
	 * real time.)
	 */
	public void setWorkStartThreshold(long newThresh) {
		this.workStartThreshold = newThresh;
	}
	
	// TODO Implement a general PersonGui?
	/*
	public void setGui(PersonGui g) {
		personGui = g;
	}

	public PersonGui getGui() {
		return personGui;
	}
	*/
	
	
	// Convenience for finding special roles
	
	/**
	 * Returns the PersonAgent's PassengerRole, or the first one if there's
	 * more than one for some reason.
	 * 
	 * @return the PassengerRole; null if none exists
	 */
	/*
	public PassengerRole getPassengerRole() {
		// TODO implement getPassengerRole
		for (Role r : roles) {
			if (r instanceof PassengerRole) {
				return r;
			}
		}
		return null;
	}
	*/

	/**
	 * Returns the PersonAgent's ResidentRole, or the first one if there's more
	 * than one for some reason.
	 * 
	 * @return the ResidentRole; null if none exists
	 */
	public ResidentRole getResidentRole() {
		for (Role r : roles) {
			if (r instanceof ResidentRole) {
				return (ResidentRole) r;
			}
		}
		return null;
	}
	
	public WorkRole getWorkRole() {
		for (Role r : roles) {
			if (r instanceof WorkRole) {
				return (WorkRole) r;
			}
		}
		return null;
	}
	
	/*
	public void activateRole(CityLocation loc) {
		// TODO implement activateRole()
	}
	*/
	
	// Choosing locations to patronize
	
	/*
	Restaurant chooseRestaurant() {
		// TODO implement chooseRestaurant()
	}
	*/
	
	/*
	Market chooseMarket() {
		// TODO implement chooseRestaurant()
	}
	*/
	
	/*
	Bank chooseBank() {
		// TODO implement chooseBank()
	}
	*/
	
	// Booleans for determining which action should be called
	
	public boolean workStartsSoon() {
		long workStartTime = getWorkRole().startTime();
		return timeManager.timeUntil(workStartTime) <= this.workStartThreshold;
	}
	
	public boolean isStarving() {
		return hungerLevel == HungerLevel.STARVING;
	}
	
	/**
	 * Whether this person wants to eat out. Based on the last time this person
	 * ate out and on how long this person likes to wait between each time
	 * eating out.
	 */
	public boolean wantsToEatOut() {
		return timeManager.timeUntil(lastTimeEatingOut)
				>= this.eatingOutWaitPeriod;
	}
	
	public boolean hasFoodAtHome() {
		return getResidentRole().thereIsFoodAtHome();
	}
	
	boolean needToGoToBank() {
		return wallet.hasTooMuch() || wallet.hasTooLittle();
	}
	
	// Enumerations
	
	private enum PersonEvent {NONE, ARRIVED_AT_LOCATION}
	public enum HungerLevel {UNKNOWN, STARVING, HUNGRY, NEUTRAL, SATISFIED,
			FULL}
	
	// Inner classes
	
	public class Wallet {
		private double cashOnHand;
		private double tooMuch;
		private double tooLittle;
		
		/**
		 * @param cashOnHand
		 * @param tooMuch when cashOnHand > tooMuch, Person goes to bank
		 * @param tooLittle when cashOnHand < tooLittle, Person goes to bank
		 */
		public Wallet(double cashOnHand, double tooMuch, double tooLittle) {
			this.cashOnHand = cashOnHand;
			this.tooMuch = tooMuch;
			this.tooLittle = tooLittle;
		}
		
		public double getCashOnHand() { return this.cashOnHand; }
		public double getTooMuch() { return this.tooMuch; }
		public double getTooLittle() { return this.tooLittle; }
		
		public void setCashOnHand(double coh) { this.cashOnHand = coh; }
		public void setTooMuch(double tm) { this.tooMuch = tm; }
		public void setTooLittle(double tl) { this.tooLittle = tl; }
		
		public boolean hasTooMuch() {
			return cashOnHand > tooMuch;
		}
		
		public boolean hasTooLittle() {
			return cashOnHand < tooLittle;
		}
	}
}

