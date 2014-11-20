package utils;



import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;






public class MyInputTest {

	@Before
	public void setUp() throws Exception {
	}

	
	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testMyIntegerInput() {
		int out = MyInput.myIntegerInput("messaggio1", "messaggio2", 147, 0);
		MyLog.waitHere("out= "+out);
	}

}

