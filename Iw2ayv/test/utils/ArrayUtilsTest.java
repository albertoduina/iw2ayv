package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ij.IJ;
import utils.AboutBox;

public class ArrayUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testRotateArrayLeft() {

		double[] myArray = { 10.3, 12.4, 5.7, 8.2, 0, 11.3 };
		MyLog.logVector(myArray, "myArray");

		double[] out1 = ArrayUtils.rotateArrayLeft(myArray);

		MyLog.logVector(out1, "out1");
		MyLog.waitHere();
	}

	@Test
	public final void testVetSd() {

		double[] myArray = { 10.3, 12.4, 5.7, 8.2, 0, 11.3 };
		MyLog.logVector(myArray, "myArray");

		double sd1 = ArrayUtils.vetSd(myArray);
		
		
		double expected = 4.57970159144312;

		IJ.log("testVetSd (diviso enne)");		
		IJ.log("sd1= " + sd1);
		MyLog.waitHere();
	}

	
	@Test
	public final void testVetSdKnuth() {

		double[] myArray = { 10.3, 12.4, 5.7, 8.2, 0, 11.3 };
		MyLog.logVector(myArray, "myArray");

		double sd1 = ArrayUtils.vetSdKnuth(myArray);
		
		
		double expected = 4.57970159144312;

		IJ.log("testVetSdKnuth (diviso enne meno uno)");		
		IJ.log("sd1= " + sd1);
		MyLog.waitHere();
	}

}
