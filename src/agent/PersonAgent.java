package agent;

import housing.ResidentRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.Item;
import kelp.Kelp;
import kelp.KelpClass;
import transportation.PassengerRole;
import transportation.interfaces.Car;
import CommonSimpleClasses.CityLocation;
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
	
	private Kelp kelp;
	
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
	private Map<String, Item> inventory;
	
	private Car car;
	
	public PersonAgent(String name){
		super();
		
		this.name = name;
		this.roles = new ArrayList<Role>();
		
		this.event = PersonEvent.NONE;
		this.hungerLevel = HungerLevel.NEUTRAL;
		
		this.timeManager = TimeManager.getInstance();
		
		this.kelp = KelpClass.getKelpInstance();
		
		this.lastTimeEatingOut = timeManager.fakeStartTime();
		this.eatingOutWaitPeriod = 1000 * 60 * 60 * 24; // one day
		
		this.workStartThreshold = 1000 * 60 * 30; // 30 minutes
		
		this.wallet = new Wallet(); // medium income level
		this.inventory = new HashMap<String, Item>();
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
		
		boolean roleExecuted = false;
		
		for (Role r : roles) {
			if (r.isActive()) {
//				if (r.isAwaitingInput()) {
//					// The Role is paused. Return false, but don't activate
//					// another Role!
//					return false;
//				} else {
					roleExecuted = r.pickAndExecuteAnAction() || roleExecuted;
//				}
			}
		}
		
		// if at least one role's scheduler returned true, return true
		if (roleExecuted) { return true; }
		
		// If you just arrived somewhere, activate the appropriate Role. 
		if (event == PersonEvent.ARRIVED_AT_LOCATION) {
			// TODO IMPORTANT: CORRECTLY DETERMINE WHETHER I SHOULD BE AT WORK
			activateRoleForLoc(getPassengerRole().getLocation(), false);
		}
		

		// If you didn't just arrive somewhere, decide what to do next.
		if (workStartsSoon()) {
			goToWork();
			return true;
		} else if (isHungry()) {
			if (wantsToEatOut()) {
				 CityLocation restaurant = chooseRestaurant();
				 goToRestaurant(restaurant);
				return true;
			} else if (hasFoodAtHome()) {
				goHome();
				return true;
			} else {
				 CityLocation market = chooseMarket();
				 goToMarket(market);
				return true;
			}
		} else if (needToGoToBank()) {
			 CityLocation bank = chooseBank();
			 goToBank(bank);
			return true;
		}

		// No actions were performed.
		return false;
	}

	/* -------- Actions -------- */
	
	/**
	 * Starts up the {@link PassengerRole} to take the Person to the given
	 * {@link CityLocation}. This is one of PersonAgent's most important
	 * actions - and several other actions can simply call this one.
	 */
	private void goToLoc(CityLocation loc) {
		PassengerRole pass = getPassengerRole();
		pass.msgGoToLocation(loc);
		pass.activate();
	}
	
	private void goHome() {
		 CityLocation home = getResidentRole().getLocation();
		 goToLoc(home);
	}
	
	private void goToWork() {
		CityLocation work = getWorkRole().getLocation();
		goToLoc(work);
	}
	
	private void goToRestaurant(CityLocation restaurant) {
		goToLoc(restaurant);
	}
	
	private void goToMarket(CityLocation market) {
		goToLoc(market);
	}
	
	private void goToBank(CityLocation bank) {
		goToLoc(bank);
	}
	
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
	
	private void activateRoleForLoc(CityLocation loc, boolean work) {
		WorkRole workRole = getWorkRole();
		if (work) {
			workRole.activate();
			return;
		}
		
		PassengerRole passRole = getPassengerRole();
		
		for (Role r : roles) {
			if (loc.equals(r.getLocation())
					&& !r.equals(workRole)
					&& !r.equals(passRole)) {
				
				r.activate();
				return;
			}
		}
		
		// There is no role for this location! Create one.
		Role role = null;
		role.activate();
	}
	
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
	public long getLastTimeEatingOut() {
		return this.lastTimeEatingOut;
	}
	
	/** Modifies the last time the person thinks he or she ate out. */
	public void setLastTimeEatingOut(long newLastTime) {
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
			this.lastTimeEatingOut = timeManager.currentSimTime();
		}
		this.hungerLevel = newHungerLevel;
	}
	
	/**
	 * Sets the hunger level to the given, without modifying lastTimeEatingOut.
	 * Convenience for {@link #setHungerLevel(HungerLevel, boolean)}.
	 * 
	 * @param newHungerLevel
	 * @see #getLastTimeEatingOut()
	 */
	public void setHungerLevel(HungerLevel newHungerLevel) {
		setHungerLevel(newHungerLevel, false);
	}
	
	/**
	 * Sets the hunger level to full; if eatingOut, updates lastTimeEatingOut.
	 * Convenience for {@link #setHungerLevel(HungerLevel, boolean)}.
	 * 
	 * @param newHungerLevel
	 * @param eatingOut whether this hunger modification was due to eating out
	 * @see #getLastTimeEatingOut()
	 */
	public void setHungerToFull(boolean eatingOut) {
		setHungerLevel(HungerLevel.FULL, eatingOut);
	}
	
	/**
	 * Sets the hunger level to full, without modifying lastTimeEatingOut.
	 * Convenience for {@link #setHungerLevel(HungerLevel, boolean)}.
	 * 
	 * @param newHungerLevel
	 * @see #getLastTimeEatingOut()
	 */
	public void setHungerToFull() {
		setHungerToFull(false);
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
	
	// ---- Methods for finding special roles
	
	@Override
	public PassengerRole getPassengerRole() {
		for (Role r : roles) {
			if (r instanceof PassengerRole) {
				return (PassengerRole) r;
			}
		}
		return null;
	}

	@Override
	public ResidentRole getResidentRole() {
		for (Role r : roles) {
			if (r instanceof ResidentRole) {
				return (ResidentRole) r;
			}
		}
		return null;
	}
	
	@Override
	public WorkRole getWorkRole() {
		for (Role r : roles) {
			if (r instanceof WorkRole) {
				return (WorkRole) r;
			}
		}
		return null;
	}
	
		
	// ---- Choosing locations to patronize

	private CityLocation chooseRestaurant() {
		// get a list of nearby restaurants
		List<CityLocation> restaurants = kelp.placesNearMe(
				getPassengerRole().getLocation(),
				CityLocation.LocationTypeEnum.Restaurant);
		
		// choose a restaurant
		for (CityLocation r : restaurants) {
			// TODO don't just choose the first restaurant
			return r;
		}
		
		// if no restaurants exist
		return null;
	}
	
	private CityLocation chooseMarket() {
		// get a list of nearby markets
		List<CityLocation> markets = kelp.placesNearMe(
				getPassengerRole().getLocation(),
				CityLocation.LocationTypeEnum.Market);
		
		// choose a market
		for (CityLocation m : markets) {
			// TODO don't just choose the first market
			return m;
		}
		
		// if no markets exist
		return null;
	}
	
	private CityLocation chooseBank() {
		// get a list of nearby banks
		List<CityLocation> banks = kelp.placesNearMe(
				getPassengerRole().getLocation(),
				CityLocation.LocationTypeEnum.Bank);
		
		// choose a bank
		for (CityLocation b : banks) {
			// TODO don't just choose the first bank
			return b;
		}
		
		// if no banks exist
		return null;
	}
	
	// ---- Boolean methods (for deciding what to do next)
	
	@Override
	public boolean workStartsSoon() {
		WorkRole workRole = getWorkRole();
		if (workRole == null) {
			return false;
		}
		long workStartTime = workRole.startTime();
		return timeManager.timeUntil(workStartTime) <= this.workStartThreshold;
	}
	
	@Override
	public boolean isStarving() {
		return hungerLevel == HungerLevel.STARVING;
	}
	
	public boolean isHungry() {
		return hungerLevel == HungerLevel.STARVING ||
				hungerLevel == HungerLevel.HUNGRY; 
	}
	
	/**
	 * Whether this person wants to eat out. Based on the last time this person
	 * ate out and on how long this person likes to wait between each time
	 * eating out.
	 */
	public boolean wantsToEatOut() {
		return -timeManager.timeUntil(lastTimeEatingOut)
				>= this.eatingOutWaitPeriod;
	}
	
	/** Whether this person has food at home. */
	public boolean hasFoodAtHome() {
		 return getResidentRole().thereIsFoodAtHome();
	}
	
	public boolean needToGoToBank() {
		return wallet.hasTooMuch() || wallet.hasTooLittle()
				|| wallet.needsMoney();
	}
	
	// ---- Market/Inventory
	
	public Map<String, Item> getInventory() {
		return this.inventory;
	}
	
	public void addItemsToInventory(String name, int amount) {
		Item item = this.inventory.get(name);
		if (item == null) {
			item = new Item(name, amount);
		} else {
			item.ItemEqual(item.amount + amount);
		}
		this.inventory.put(name, item);
	}
	
	// ---- Enumerations
	
	private enum PersonEvent {NONE, ARRIVED_AT_LOCATION}
	public enum HungerLevel {UNKNOWN, STARVING, HUNGRY, NEUTRAL, SATISFIED,
			FULL}
	
	// ---- Methods to avoid weird inheritance issues
	@Override
	public void agentStateChanged() {
		stateChanged();
	}

	@Override
	public void printMsg(String msg) {
		print(msg);
	}

	@Override
	public void agentDo(String msg) {
		Do(msg);
	}
	
}

