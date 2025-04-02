import static org.junit.Assert.*;

import org.junit.*;

/**
 * AdminProfile osztályhoz test osztály
 */
public class TestAdmProf {
	AdminProfile p1;
	
	@Before
	public void setup() {
		p1 = new AdminProfile("testName1", "testPass1");
	}
	
	@Test
	public void testGetters() {
		assertEquals("testName1", p1.getUsername());
		assertEquals("testPass1", p1.getPassword());
	}
	
	@Test
	public void testLogin() {
		assertFalse(p1.logIn("falseinfo", "falseinfo"));
		assertTrue(p1.logIn("testName1", "testPass1"));
		assertEquals(p1, ProgramState.getLoggedInProfile());
	}
	
	@Test
	public void testLogout() {
		AdminProfile.logOut();
		assertEquals(null, ProgramState.getLoggedInProfile());
	}
}
