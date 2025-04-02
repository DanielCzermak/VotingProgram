import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;

/**
 * Vote osztályhoz teszt osztály
 */
public class TestVote {
	Vote v1;
	Vote v2;
	
	@Before
	public void setup() {
		ArrayList<Candidate> cl = new ArrayList<Candidate>();
		cl.add(new Candidate("testCandidate1"));
		cl.add(new Candidate("testCandidate2"));
		v1 = new Vote("testVote1", "testQuestion1", 3, cl);
		
		cl.add(new Candidate("testCandidate3"));
		VKeyList kl = new VKeyList(5);
		v2 = new Vote(1, "testVote2", "testQuestion2", 5, cl, kl);
	}
	
	@Test
	public void testLoadConstructor() {
		assertEquals(1, v2.getState());
		assertEquals("testVote2", v2.getName());
		assertEquals("testQuestion2", v2.getQuestion());
		assertEquals(5, v2.getPossibleVotes());
		assertEquals("testCandidate3", v2.getCandidate("testCandidate3").getName());
		assertEquals(5, v2.getKeyList().getKeysCount());
	}
	
	@Test
	public void testStartVote() {
		assertTrue(v1.startVote());
		assertEquals(v1, ProgramState.getRunningVote());
		assertFalse(v2.startVote());
	}
	
	@Test
	public void testCloseVote() {
		assertFalse(v1.closeVote());
		assertTrue(v2.closeVote());
		assertEquals(null, ProgramState.getRunningVote());
	}
}
