package agent.test;

import java.util.Date;

import agent.TimeManager;
import junit.framework.TestCase;

/**
 * This is a JUnit test class to test TimeManager's static time comparison
 * capabilities. Since the non-static methods rely on current system time, they
 * cannot be directly tested. Instead, we will test only the static methods,
 * which are testable independently of current system time, and which the
 * non-static methods directly call.
 *  
 * @author Erik Strottmann
 */
public class TimeManagerTest extends TestCase {
	TimeManager tm;
	static final long FIRST_TIME = 1000L; // 1 second after the epoch
	static final long SECOND_TIME = 1000L * 60L * 60L; // 1 hour post-epoch
	static final Date firstDate = new Date(FIRST_TIME);
	static final Date secondDate = new Date(SECOND_TIME);
	
	public void setUp() throws Exception {
		super.setUp();
		
		tm = TimeManager.getInstance();
	}
	
	/**
	 * This tests that the TimeManager is a singleton; all calls to getInstance
	 * should return the same instance.
	 */
	public void testGetInstance() {
		TimeManager temp = TimeManager.getInstance();
		assertTrue("TimeManager should have one unique instance, but two "
				+ "distinct instances were found.", tm == temp);
	}
	
	/**
	 * This tests that TimeManager's static time comparison methods work. Since
	 * the non-static methods {@link TimeManager#timeUntil(long)} and
	 * {@link TimeManager#timeUntil(java.util.Date)} simply call timeBetween,
	 * this also serves to test those methods. 
	 */
	public void testTimeBetween() {
		// Test timeBetween(long)
		assertEquals("The time between two times should be the difference "
				+ "between the times, but it isn't.",
				TimeManager.timeBetween(FIRST_TIME, SECOND_TIME),
				SECOND_TIME - FIRST_TIME);
		assertTrue("The time between a later time and an earlier time "
				+ "should be negative, but isn't.",
				TimeManager.timeBetween(SECOND_TIME, FIRST_TIME) < 0L);
		assertEquals("The time between times A and B should be the opposite"
				+ " of the time between B and A, but it isn't.",
				TimeManager.timeBetween(FIRST_TIME, SECOND_TIME),
				-TimeManager.timeBetween(SECOND_TIME, FIRST_TIME));
		assertEquals("The time between a time and itself should be zero, but "
				+ "it isn't.",
				TimeManager.timeBetween(FIRST_TIME, FIRST_TIME), 0L);
		
		// Test timeBetween(java.util.Date)
		assertEquals("The time between two dates should be the difference "
				+ "between the dates' times, but it isn't.",
				TimeManager.timeBetween(firstDate, secondDate),
				SECOND_TIME - FIRST_TIME);
		assertTrue("The time between a later time and an earlier time "
				+ "should be negative, but isn't.",
				TimeManager.timeBetween(secondDate, firstDate) < 0L);
		assertEquals("The time between times A and B should be the opposite"
				+ " of the time between B and A, but it isn't.",
				TimeManager.timeBetween(firstDate, secondDate),
				-TimeManager.timeBetween(secondDate, firstDate));
		assertEquals("The time between a time and itself should be zero, but "
				+ "it isn't.",
				TimeManager.timeBetween(firstDate, firstDate), 0L);
	}
}
