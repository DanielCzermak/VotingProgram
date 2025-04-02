import static org.junit.Assert.*;

import org.junit.*;

/**
 * Candidate osztályhoz test osztály
 */
public class TestCandidate {
	Candidate c1;
	Candidate c2;
	
	@Before
	public void setup() {
		c1 = new Candidate("test1");
		c2 = new Candidate("test2", 5);
	}
	
	@Test
	public void testCastVote() {
		c1.castVote();
		assertEquals(1, c1.getVotes());
		for(int i = 0; i < 10; i++) {
			c2.castVote();
		}
		assertEquals(15, c2.getVotes());
	}
	
	@Test
	public void testToPrintable() {
		assertEquals("test1;0", c1.toPrintable());
		assertEquals("test2;5", c2.toPrintable());
	}
}
