package utils;



import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;






public class MyTimeUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	
	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testMilliTime() {
		long com = MyTimeUtils.milliTime("102923.489999");
		assertEquals(37763489L, com);
	}

}

