package agent;

import housing.ResidentRole;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import transportation.PassengerRole;
import transportation.interfaces.Car;

import agent.Role;
import agent.interfaces.Person;

/**
 * A PersonAgent is the heart and soul of SimCity. Nearly all interactions in
 * the city are between PersonAgents and the {@link Role}s they control.
 * 
 * @author Erik Strottmann
 */
public class PersonAgent extends Agent implements Person {
	private String name;
	private List<Role> roles;
	
	private PersonEvent event;
	private HungerLevel hungerLevel;
	
	private TimeManager timeManager;
	private Timer timer;
	
	private Date lastTimeEatingOut;
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
	
	private Car car;
	
	public PersonAgent(String name){
		super();
		
		this.name = name;
		this.roles = new ArrayList<Role>();
		
		this.event = PersonEvent.NONE;
		this.hungerLevel = HungerLevel.NEUTRAL;
		
		this.timeManager = TimeManager.getInstance();
		this.timer = new Timer();
		
		this.lastTimeEatingOut = timeManager.fakeStartDate();
		this.eatingOutWaitPeriod = 1000 * 60 * 60 * 24; // one day
		
		this.workStartThreshold = 1000 * 60 * 30; // 30 minutes
		
		this.wallet = new Wallet(); // medium income level
	}
	
	/* -------- Messages -------- */
	
	// from PassengerRole
	@Override
	public void msgArrivedAtDestination() {
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
	
	private void goHome() {
		// TODO implement goHome()
	}
	
	private void goToWork() {
		// TODO implement goToWork()
	}
	
	// TODO uncomment when Restaurant interface exists
	/*
	private void goToRestaurant(Restaurant r) {
		// TODO implement goToRestaurant
	}
	*/
	
	// TODO uncomment when Market interface exists
	/*
	private void goToMarket(Market m) {
		// TODO implement goToMarket
	}
	*/
	
	// TODO uncomment when Bank interface exists
	/*
	private void goToBank(Bank b) {
		// TODO implement goToBank
	}
	*/
	
	/* -------- Utilities -------- */
	
	@Override
	/** Return the PersonAgent's name for output e.g. messages. */
	public String getName() {
		return name;
	}
	
	@Override
	/**
	 * Returns the string representation of this person: the word "person"
	 * followed by the person's name.
	 */
	public String toString() {
		return "person " + getName();
	}
	
	/**
	 * Adds the given Role to the PersonAgent's list. The person will not call
	 * the Role's scheduler if the Role is inactive. Also calls
	 * {@link #stateChanged()}.
	 */
	@Override
	public void addRole(Role r) {
		this.roles.add(r);
		stateChanged();
	}
	
	/*
	void activateRole(CityLocation loc) {
		// TODO implement activateRole()
	}
	*/
	
	@Override
	public Wallet getWallet() {
		return this.wallet;
	}
	
	@Override
	public Car getCar() {
		return this.car;
	}
	
	@Override
	public void setCar(Car car) {
		this.car = car;
	}
	
	// ---- Eating out
	
	/**
	 * @return how long this person likes to wait between each time eating out
	 * 		   (in game time, not real time)
	 */
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
	public Date getLastTimeEatingOut() {
		return this.lastTimeEatingOut;
	}
	
	/** Modifies the last time the person thinks he or she ate out. */
	public void setLastTimeEatingOut(Date newLastTime) {
		this.lastTimeEatingOut = newLastTime;
	}
	
	
	// ---- Hunger
	
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
			this.lastTimeEatingOut = timeManager.currentSimDate();
		}
		this.hungerLevel = newHungerLevel;
	}
	
	public void setHungerLevel(HungerLevel newHungerLevel) {
		setHungerLevel(newHungerLevel, false);
	}
	
	// ---- Work starting soon
	
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
	
	
	// ---- Methods for finding special roles
	
	/**
	 * Returns the PersonAgent's PassengerRole, or the first one if there's
	 * more than one for some reason.
	 * 
	 * @return the PassengerRole; null if none exists
	 */
	@Override
	public PassengerRole getPassengerRole() {
		// TODO implement getPassengerRole
		for (Role r : roles) {
			if (r instanceof PassengerRole) {
				return (PassengerRole) r;
			}
		}
		return null;
	}

	/**
	 * Returns the PersonAgent's ResidentRole, or the first one if there's more
	 * than one for some reason.
	 * 
	 * @return the ResidentRole; null if none exists
	 */
	@Override
	public ResidentRole getResidentRole() {
		for (Role r : roles) {
			if (r instanceof ResidentRole) {
				return (ResidentRole) r;
			}
		}
		return null;
	}
	
	/*
	@Override
	public WorkRole getWorkRole() {
		// TODO implement when WorkRole is merged into master
		return null;
	}
	*/
		
	// ---- Choosing locations to patronize
	
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
	
	// ---- Boolean methods (for deciding what to do next)
	
	@Override
	public boolean workStartsSoon() {
		// TODO implement workStartsSoon()
		/*
		Date workStartTime = getWorkRole.startTime();
		return timeManager.timeUntil(workStartTime) <= this.workStartThreshold;
		*/
		return false;
	}
	
	@Override
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
	
	public boolean needToGoToBank() {
		return wallet.hasTooMuch() || wallet.hasTooLittle();
	}
	
	// ---- Enumerations
	
	private enum PersonEvent {NONE, ARRIVED_AT_LOCATION}
	public enum HungerLevel {UNKNOWN, STARVING, HUNGRY, NEUTRAL, SATISFIED,
			FULL}
	
}

