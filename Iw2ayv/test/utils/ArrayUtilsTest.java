package utils;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;




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


		double[] myArray= {10.3, 12.4, 5.7, 8.2, 0, 11.3};
		MyLog.logVector(myArray, "myArray");

		
		double[] out1=  ArrayUtils.rotateArrayLeft(myArray);

		MyLog.logVector(out1, "out1");
		MyLog.waitHere();
	}

	

}

