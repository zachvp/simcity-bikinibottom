package agent.test;

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
	static final long S_AFTER_EPOCH = 1000L;
	static final long H_AFTER_EPOCH = 1000L * 60L * 60L;
	static final long D_AFTER_EPOCH = 1000L * 60L * 60L * 24L;
	
	// Sat May 01 1999 06:00:00 GMT-0700 (PDT)
	// AKA 06:00 the day SpongeBob SquarePants first aired
	static final long SBSP_0600 = 925563600000L;
	// Sat May 01 1999 02:00:00 GMT-0700 (PDT)
	static final long SBSP_0200 = 925549200000L;
	// Sat May 01 1999 16:30:00 GMT-0700 (PDT)
	static final long SBSP_1630 = 925601400000L;
	// Fri Apr 30 1999 16:30:00 GMT-0700 (PDT)
	static final long SBSP_M1_1630 = 925515000000L;
	// Sun May 02 1999 02:00:00 GMT-0700 (PDT)
	static final long SBSP_P1_0200 = 925635600000L;
	// Sun May 02 1999 16:30:00 GMT-0700 (PDT)
	static final long SBSP_P1_1630 = 925687800000L;
	
	@Override
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
	 * This tests that TimeManager's static time comparison method works. Since
	 * the non-static methods {@link TimeManager#timeUntil(long)} and
	 * {@link TimeManager#isNowBetween} simply call howLongBetween, this also
	 * serves to test those methods. 
	 */
	public void testTimeComparisions() {
		// test howLongBetween
		assertEquals("The time between two times should be the difference "
				+ "between the times, but it isn't.",
				TimeManager.howLongBetween(S_AFTER_EPOCH, H_AFTER_EPOCH),
				H_AFTER_EPOCH - S_AFTER_EPOCH);
		assertTrue("The time between a later time and an earlier time "
				+ "should be negative, but isn't.",
				TimeManager.howLongBetween(H_AFTER_EPOCH,S_AFTER_EPOCH) < 0L);
		assertEquals("The time between times A and B should be the opposite"
				+ " of the time between B and A, but it isn't.",
				TimeManager.howLongBetween(S_AFTER_EPOCH, H_AFTER_EPOCH),
				-TimeManager.howLongBetween(H_AFTER_EPOCH, S_AFTER_EPOCH));
		assertEquals("The time between a time and itself should be zero, but "
				+ "it isn't.",
				TimeManager.howLongBetween(S_AFTER_EPOCH, S_AFTER_EPOCH), 0L);
		
		// test isTimeBefore
		assertTrue("An earlier time should be before a later time, but isn't.",
				TimeManager.isTimeBefore(S_AFTER_EPOCH, H_AFTER_EPOCH));
		assertFalse("A later time shouldn't be before an earlier time, but is.",
				TimeManager.isTimeBefore(H_AFTER_EPOCH, S_AFTER_EPOCH));
		assertFalse("A time shouldn't be before itself, but is.",
				TimeManager.isTimeBefore(S_AFTER_EPOCH, S_AFTER_EPOCH));
		
		// test isTimeAfter
		assertTrue("A later time should be after an earlier time, but isn't.",
				TimeManager.isTimeAfter(H_AFTER_EPOCH, S_AFTER_EPOCH));
		assertFalse("An earlier time shouldn't be after a later time, but is.",
				TimeManager.isTimeAfter(S_AFTER_EPOCH, H_AFTER_EPOCH));
		assertFalse("A time shouldn't be after itself, but is.",
				TimeManager.isTimeAfter(S_AFTER_EPOCH, S_AFTER_EPOCH));
		
		// test isTimeBetween
		assertTrue("A middle time should be between an earlier and a later "
				+ "time, but isn't.", TimeManager.isTimeBetween(H_AFTER_EPOCH,
						S_AFTER_EPOCH, D_AFTER_EPOCH));
		assertTrue("A middle time should be between a later time and an "
				+ "earlier time, but isn't.",
				TimeManager.isTimeBetween(H_AFTER_EPOCH, D_AFTER_EPOCH,
						S_AFTER_EPOCH));
		assertFalse("An earlier time shouldn't be between two later times, "
				+ "but is.", TimeManager.isTimeBetween(S_AFTER_EPOCH,
						H_AFTER_EPOCH, D_AFTER_EPOCH));
		assertFalse("A later time shouldn't be between two earlier times, "
				+ "but is.", TimeManager.isTimeBetween(D_AFTER_EPOCH,
						H_AFTER_EPOCH, S_AFTER_EPOCH));
		assertTrue("A time should be between itself and a later time, but "
				+ "isn't.", TimeManager.isTimeBetween(S_AFTER_EPOCH,
						H_AFTER_EPOCH, S_AFTER_EPOCH));
	}
	
	/**
	 * This tests that TimeManager's static methods to find a given time on
	 * today (or any given number of days before or after today) work correctly.
	 * Since the non-static methods {@link TimeManager#timeToday},
	 * {@link TimeManager#timeTomorrow}, and {@link TimeManager#timeYesterday}
	 * directly call these static methods, this also serves to test those
	 * methods. 
	 */
	public void testTimeOnADay() {
		// test timeOnSameDay
		assertEquals("timeOnSameDay doesn't compute an earlier time on the "
				+ "same day properly.",
				TimeManager.timeOnSameDay(SBSP_0600, 2, 0), SBSP_0200);
		assertEquals("timeOnSameDay doesn't compute a later time on the same "
				+ "day properly.",
				TimeManager.timeOnSameDay(SBSP_0600, 16, 30), SBSP_1630);
		assertEquals("timeOnSameDay doesn't compute the same time on the same "
				+ "day properly.",
				TimeManager.timeOnSameDay(SBSP_0600, 6, 0), SBSP_0600);
		
		// test timeNDaysFromNow
		assertEquals("timeNDaysFromNow doesn't compute a time one day ago "
				+ "properly.",
				TimeManager.timeNDaysFromNow(SBSP_0600, 16, 30, -1),
				SBSP_M1_1630);
		assertEquals("timeNDaysFromNow doesn't compute a time one day from "
				+ "now properly.",
				TimeManager.timeNDaysFromNow(SBSP_0600, 16, 30, 1),
				SBSP_P1_1630);
	}
	
	/**
	 * This tests TimeManager's static methods that find the next and
	 * previous occurrences of a given time, relative to a given reference
	 * time. Since the non-static overloaded versions of these methods simply
	 * call the static versions, this also serves to test the non-static
	 * methods.
	 */
	public void testNextAndPreviousTime() {
		// test nextSuchTime
		assertEquals("nextSuchTime doesn't correctly find a later time today.",
				TimeManager.nextSuchTime(SBSP_0600, 16, 30), SBSP_1630);
		assertEquals("nextSuchTime doesn't correctly find a time tomorrow "
				+ "when it's already passed today.",
				TimeManager.nextSuchTime(SBSP_0600, 2, 0), SBSP_P1_0200);
		
		// test previousSuchTime
		assertEquals("previousSuchTime doesn't correctly find an earlier time "
				+ "today.",
				TimeManager.previousSuchTime(SBSP_0600, 2, 0), SBSP_0200);
		assertEquals("previousSuchTime doesn't correctly find a time "
				+ "yesterday when it hasn't happened yet today.",
				TimeManager.previousSuchTime(SBSP_0600, 16, 30), SBSP_M1_1630);
	}
}
