package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

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
		boolean absolute = false;
		String expected = "";
		ArrayList<String> letto = new InputOutput().readFileGeneric(
				"asconv.txt", absolute);
		for (int i1 = 0; i1 < letto.size(); i1++) {
			expected += (String) letto.get(i1);
			if (i1<letto.size()-1) expected+="\n";
		}
//		IJ.log("-----------expected---------------");
//		IJ.log(expected);
//		IJ.log("------------header----------------");
//		IJ.log(header);
//		MyLog.waitHere();
//		IJ.log("------------stop----------------");
		boolean result = header.equals(expected);
		assertTrue("header differs from expected", result);

	}

	@Test
	public final void testReadAscParameterContains() {
		String path1 = ".\\Test4\\17";
		ReadAscconv rasc = new ReadAscconv();
		String header = rasc.read(path1);
		String[] userInput = { "sCoilSelectMeas",
				"asList[0].sCoilElementID.tCoilID" };

		String param = rasc.readAscParameterContains(header, userInput);
		String expected= "\"\"HeadNeck_20\"\"";
		boolean result = param.equals(expected);
		assertTrue("param differs from expected", result);
	}

	@Test
	public final void testReadDouble() {
		double numero = ReadAscconv.readDouble("63.659153 ");
//		IJ.log("numero= " + numero);
		assertEquals(63.659153, numero, 1e-25);
	}

}
