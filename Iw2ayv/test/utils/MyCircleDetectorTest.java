package utils;

import static org.junit.Assert.assertTrue;
import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageProcessor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MyCircleDetectorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testMyCircleDetector() {

		String path1 = ".\\Test4\\17";
		ImagePlus imp1 = new Opener().openImage(path1);
		ImagePlus impMini = MyStackUtils.imageFromMosaic(imp1, 12);
		boolean step = false;
		int[] dati = MyCircleDetector.circleDetector(impMini, step);
		MyLog.logVector(dati, "dati");
		int[] expected = { 12, 13, 40 };
		assertTrue(UtilAyv
				.compareVectors(dati, expected, "errore comparazione"));
	}

	@Test
	public final void testMyCircleDetectorSevere() {

		String path1 = ".\\Test4\\18";
		ImagePlus imp1 = new Opener().openImage(path1);
		ImagePlus impMini = MyStackUtils.imageFromMosaicWithOffset(imp1, 5, 0,
				0);
		boolean step = true;
		int[] dati = MyCircleDetector.circleDetector(impMini, step);
		MyLog.logVector(dati, "dati");
		MyLog.waitHere();

		// int[] expected = { 12, 13, 40 };
		// assertTrue(UtilAyv
		// .compareVectors(dati, expected, "errore comparazione"));

	}

	@Test
	public final void testApplyThreshold() {

		String path1 = ".\\Test4\\17";
		ImagePlus imp1 = new Opener().openImage(path1);
		ImagePlus impMini = MyStackUtils.imageFromMosaic(imp1, 12);
		UtilAyv.showImageMaximized(impMini);
		ImagePlus imp2 = MyCircleDetector.applyThreshold(impMini);
		UtilAyv.showImageMaximized(imp2);
		MyLog.waitHere();
	}


	@Test
	public final void testCircleOutline() {

		String path1 = ".\\Test4\\17";
		ImagePlus imp1 = new Opener().openImage(path1);
		ImagePlus impMini = MyStackUtils.imageFromMosaic(imp1, 12);
		UtilAyv.showImageMaximized(impMini);
		ImageProcessor ipMini = impMini.getProcessor();
		ipMini.findEdges();
		impMini.updateAndDraw();
		ImagePlus imp2 = MyCircleDetector.applyThreshold(impMini);
		UtilAyv.showImageMaximized(imp2);
		ImagePlus imp3 =  MyCircleDetector.circleOutline(imp2);
		UtilAyv.showImageMaximized(imp3);
		MyLog.waitHere();
	}




}
