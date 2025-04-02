import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;

/**
 * A VKeyListhez test osztály
 */
public class TestVKeyList {
	VKeyList vkl1;
	VKeyList vkl2;
	@Before
	public void setup() {
		vkl1 = new VKeyList(10);
		ArrayList<String> testList = new ArrayList<String>();
		testList.add("11111111");
		testList.add("22222222");
		vkl2 = new VKeyList(testList);
	}
	@Test
	public void testSize() {
		assertEquals("vkl1 correct size created", 10, vkl1.getKeysCount());
		assertEquals("vkl2 correct size loaded", 2, vkl2.getKeysCount());
		vkl2.useKey("11111111");
		assertEquals("vkl2 correct size after key use", 1, vkl2.getKeysCount());
	}
	@Test
	public void testUseKey() {
		assertTrue("vkl1 using generated key", vkl1.useKey(vkl1.getKeyList().get(0)));
		assertFalse("vkl1 using false key", vkl1.useKey("123"));
		assertTrue("using loaded key", vkl2.useKey("11111111"));
	}
}
