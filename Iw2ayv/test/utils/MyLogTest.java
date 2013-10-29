package utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utils.MyLog;

public class MyLogTest {
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testLogVectorDouble() {
		double[] vect1 = { 2, 4, 5.678, 7.234, 9.0000, 12.12345566789, 15 };
		MyLog.logVector(vect1, "vect1");
	}

	@Test
	public final void testLogVectorFloat() {
		float[] vect1 = { 2, 4, (float) 5.678, (float) 7.234, (float) 9.0000,
				(float) 12.12345566789, 15 };
		MyLog.logVector(vect1, "vect1");
	}

	@Test
	public final void testLogVectorInteger() {
		int[] vect1 = { 2, 4, 5, 7, 9, 12, 15 };
		MyLog.logVector(vect1, "vect1");
	}

	@Test
	public final void testLogVectorString() {
		String[] vect1 = { "2", "pippo", "pluto", "7.234", "paperino", };
		MyLog.logVector(vect1, "vect1");
	}

	
}
