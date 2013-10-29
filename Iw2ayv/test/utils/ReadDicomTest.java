package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utils.ReadDicom;

public class ReadDicomTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testReadDicomParameter() {
		String path = ".\\Test4\\01";
		ImagePlus imp = new Opener().openImage(path);
		if (imp == null) {
			fail("Manca immagine");
		}
		String parameter = ReadDicom.readDicomParameter(imp, "0008,0005");
		assertEquals("ISO_IR 100", parameter);
	}

	@Test
	public final void testContainsCoilParameter() {
		String path = ".\\Test4\\15_18130558_HE3,4vNE1,2vSP1,2_O12S_";
		ImagePlus imp = new Opener().openImage(path);
		if (imp == null) {
			fail("Manca immagine");
		}

		String coil = ReadDicom.getAllCoils(imp);
//		IJ.log("Coil= " + coil);

		boolean ok = coil.contains("ne1,2");
		ok = coil.toLowerCase().contains("nE1,2".toLowerCase());	
		assertTrue("non trovato nome coil", ok);
	}

	@Test
	public final void testHasHeader() {

		String path = ".\\Test4\\01";
		ImagePlus imp = new Opener().openImage(path);
		if (imp == null) {
			fail("Manca immagine");
		}

		boolean esiste = ReadDicom.hasHeader(imp);
		assertTrue("manca header", esiste);
	}

	@Test
	public final void testReadSubstring() {
		String sub = ReadDicom.readSubstring("-101\\-102\\103", 2);
		assertEquals("-102", sub);
	}

	@Test
	public final void testReadDouble() {
		double numero = ReadDicom.readDouble("63.659153 ");
		assertEquals(63.659153, numero, 1e-25);
	}

	@Test
	public final void testReadFloat() {
		float numero = ReadDicom.readFloat("63.659153 ");
		assertEquals((float) 63.659153, numero, 1e-25);
	}

	@Test
	public final void testReadInt() {
		int numero = ReadDicom.readInt("63");
		assertEquals(63, numero);
	}

	@Test
	public final void testIsDicomOld() {
		String path = ".\\Test4\\01";
		ImagePlus imp = new Opener().openImage(path);
		if (imp == null) {
			fail("Manca immagine");
		}
		boolean dic = ReadDicom.isDicomOld(".\\Test4\\01");
		assertTrue("non dicom", dic);
	}

}
