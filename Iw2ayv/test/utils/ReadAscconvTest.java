package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utils.MyLog;

public class ReadAscconvTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testReadAscconv() {
		String path1 = ".\\Test4\\01";
		ReadAscconv rasc = new ReadAscconv();
		String header = rasc.read(path1);
		IJ.log("header= " + header);
		MyLog.waitHere();
	}

	@Test
	public final void testReadAscParameterContains() {
		String path1 = ".\\Test4\\17";
		ReadAscconv rasc = new ReadAscconv();
		String header = rasc.read(path1);
		String[] userInput = { "sCoilSelectMeas", "asList[0].sCoilElementID.tCoilID" };

		String param = rasc.readAscParameterContains(header, userInput);
		IJ.log("param= " + param);
	}

	@Test
	public final void testReadDouble() {
		double numero = ReadAscconv.readDouble("63.659153 ");
		IJ.log("numero= " + numero);
		assertEquals(63.659153, numero, 1e-25);
	}

}
