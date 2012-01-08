package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.io.Opener;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UtilAyvTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testAfterWork() {
		int imagesOpened;
		int nonImagesOpened;

		String path1 = ".\\Test4\\B003_testP2";
		UtilAyv.openImageMaximized(path1);
		ImagePlus imp1 = new Opener().openImage(path1);
		IJ.run(imp1, "Measure", "");
		IJ.error("MESSAGGIO");
		IJ.wait(200);
		UtilAyv.afterWork();
		imagesOpened = WindowManager.getWindowCount();
		nonImagesOpened = WindowManager.getNonImageWindows().length;
		assert (imagesOpened == 0);
		assert (nonImagesOpened == 0);
	}

	@Test
	public final void testBackgroundEnhancement() {
		String[] list1 = InputOutput
				.readStringArrayFromFile("./data/list1.txt");
		ImagePlus imp1 = new Opener().openImage(list1[0]);
		imp1.show();

		UtilAyv.backgroundEnhancement(0, 0, 10, imp1);
		IJ.wait(300);
	}

	@Test
	public final void testGetPos() {
		String xData = "div-by-zero=";
		UtilAyv.getPos(xData);
	}

	@Test
	public final void testConvertToDouble() {
		String str = "1234.5678";
		double result = UtilAyv.convertToDouble(str);
		assertEquals(1234.5678, result, 1e-25);
	}

	@Test
	public final void testConvertToDoubleNull() {
		String str = null;
		double result = UtilAyv.convertToDouble(str);
		assertEquals(Double.NaN, result, 1e-25);
	}

	@Test
	public final void testConvertToDoubleBlank() {
		String str = "";
		double result = UtilAyv.convertToDouble(str);
		assertEquals(Double.NaN, result, 1e-25);
	}

	@Test
	public final void testPrintDoubleDecimals() {
		Double in1 = 1234.567891234;
		int prec = 4;
		String str = UtilAyv.printDoubleDecimals(in1, prec);
		assertEquals("1234.5679", str);
	}

	@Test
	public final void testCoord2D2() {
		double cx = 5.5;
		double cy = 6.5;
		double x2 = 57.62;
		double y2 = 27.34;
		double x1 = 126.95;
		double y1 = 147.46;
		double[] vetReference = new double[4];
		vetReference[0] = x1;
		vetReference[1] = y1;
		vetReference[2] = x2;
		vetReference[3] = y2;
		boolean debug = false;
		double[] result = UtilAyv.coord2D2(vetReference, cx, cy, debug);
		assertEquals(134.9627564, result[0], 1e-7);
		assertEquals(150.3402315, result[1], 1e-7);
	}

	@Test
	public final void testCoord2D() {
		double cx = 5.5;
		double cy = 6.5;
		double x2 = 57.62;
		double y2 = 27.34;
		double x1 = 126.95;
		double y1 = 147.46;
		boolean debug = false;
		double[] result = UtilAyv.coord2D(x1, y1, x2, y2, cx, cy, debug);
		assertEquals(134.9627564, result[0], 1e-7);
		assertEquals(150.3402315, result[1], 1e-7);
	}

	@Test
	public final void testCompareVectorsIntegerNull() {
		int[] vect1 = null;
		int[] vect2 = null;
		boolean result = UtilAyv.compareVectors(vect1, vect2, "");
		assertFalse("Input vectors null, the test must fail", result);
	}

	@Test
	public final void testCompareVectorsIntegerEqual() {
		int[] vect1 = { 2, 4, 5, 7, 9, 12, 15 };
		int[] vect2 = { 2, 4, 5, 7, 9, 12, 15 };
		boolean result = UtilAyv.compareVectors(vect1, vect2, "");
		assertTrue("Input vectors differ, the test must success", result);
	}

	@Test
	public final void testCompareVectorsLongEqual() {
		long[] vect1 = { 2, 4, 5, 7, 9, 12, 15 };
		long[] vect2 = { 2, 4, 5, 7, 9, 12, 15 };
		boolean result = UtilAyv.compareVectors(vect1, vect2, "");
		assertTrue("Input vectors differ, the test must success", result);
	}

	@Test
	public final void testCompareVectorsIntegerDiffLength() {
		int[] vect1 = { 2, 4, 5, 7, 9, 12, 15, 17 };
		int[] vect2 = { 2, 4, 5, 7, 9, 12, 15 };
		boolean result = UtilAyv.compareVectors(vect1, vect2, "");
		assertFalse("Input vectors differ, the test must fail", result);
	}

	@Test
	public final void testCompareVectorsIntegerDiffValue() {
		int[] vect1 = { 2, 4, 5, 7, 9, 12, 16 };
		int[] vect2 = { 2, 4, 5, 7, 9, 12, 15 };
		boolean result = UtilAyv.compareVectors(vect1, vect2, "");
		assertFalse("Input vectors differ, the test must fail", result);
	}

	@Test
	public final void testCompareVectorsDoubleEquals() {
		double[] vect1 = { 2.0, 4.12, 5.1, 7.3, 9.2334456, 12.0, 15.1111 };
		double[] vect2 = { 2.0, 4.12, 5.1, 7.3, 9.2334456, 12.0, 15.1111 };
		boolean result = UtilAyv.compareVectors(vect1, vect2, 1e-8, "");
		assertTrue("Input vectors equals, the test must success", result);
	}

	@Test
	public final void testCompareVectorsDoubleDiffValue() {
		double[] vect1 = { 2.0, 4.12, 5.1, 7.3, 9.2334456, 12.0, 15.1111 };
		double[] vect2 = { 2.0, 4.12, 5.1, 7.3, 9.23344561, 12.0, 15.1111 };
		boolean result = UtilAyv.compareVectors(vect1, vect2, 1e-8, "");
		assertFalse("Input vectors differents, the test must fail", result);
	}

	@Test
	public final void testCompareVectorsFloatEquals() {
		float[] vect1 = { 2, (float) 4.001, (float) 5.3456, (float) 7.789612 };
		float[] vect2 = { 2, (float) 4.001, (float) 5.3456, (float) 7.789612 };
		boolean result = UtilAyv.compareVectors(vect1, vect2, (float) 1e-8, "");
		assertTrue("Input vectors equals, the test must success", result);
	}

	@Test
	public final void testCompareVectorsFloatDiffValue() {
		float[] vect1 = { 2, (float) 4.001, (float) 5.3456, (float) 7.789612 };
		float[] vect2 = { 2, (float) 4.002, (float) 5.3456, (float) 7.789612 };
		boolean result = UtilAyv.compareVectors(vect1, vect2, (float) 1e-8, "");
		assertFalse("Input vectors differents, the test must fail", result);
	}

	@Test
	public final void testCompareVectorsStringEquals() {
		String[] vect1 = { "pippo", "pluto", "", " ", ";:.@@@@" };
		String[] vect2 = { "pippo", "pluto", "", " ", ";:.@@@@" };
		boolean result = UtilAyv.compareVectors(vect1, vect2, "");
		assertTrue("Input vectors equals, the test must success", result);
	}

	@Test
	public final void testCompareVectorsStringDiffValue() {
		String[] vect1 = { "pippo", "pluto", "", " ", ";:.@@@@" };
		String[] vect2 = { "pippo", "pluto1", "", " ", ";:.@@@@" };
		boolean result = UtilAyv.compareVectors(vect1, vect2, "");
		assertFalse("Input vectors differents, the test must fail", result);
	}

	@Test
	public final void testVetMean() {
		double[] vet1 = InputOutput.readDoubleArrayFromFile("./data/vet02.txt");
		double calcExcel = 1044.522676;
		// Log.logVector(vet1, "vet1");
		double mean = UtilAyv.vetMean(vet1);
		assertEquals(calcExcel, mean, 1e-6);
	}

	@Test
	public final void testVetSdKnuth() {
		double[] vet1 = InputOutput.readDoubleArrayFromFile("./data/vet02.txt");
		double calcExcel = 1.846457;
		double mean = UtilAyv.vetSdKnuth(vet1);
		assertEquals(calcExcel, mean, 1e-6);
	}

	@Test
	public final void testImagesToStack16() {
		String[] list1 = InputOutput
				.readStringArrayFromFile("./data/list1.txt");
		ImagePlus imp1 = UtilAyv.imagesToStack16(list1);
		int len = imp1.getImageStackSize();
		imp1.show();
		MyLog.waitHere();
		assertEquals(16, len);
	}

	@Test
	public final void testImagesToStack32() {
		String[] list1 = InputOutput
				.readStringArrayFromFile("./data/list1.txt");
		ImagePlus imp1 = UtilAyv.imagesToStack32(list1);
		int len = imp1.getImageStackSize();
		assertEquals(16, len);
		imp1.show();
		MyLog.waitHere();
		int depth = imp1.getBitDepth();
		// IJ.log("stack bitDepth= " + depth);
		assertEquals(32, depth);
	}

	@Test
	public final void testImageFromStack() {
		String[] list1 = InputOutput
				.readStringArrayFromFile("./data/list1.txt");
		ImagePlus imp1 = UtilAyv.imagesToStack16(list1);
		int len = imp1.getImageStackSize();
		assertEquals(16, len);
		// --- stack ready
		ImagePlus imp2 = UtilAyv.imageFromStack(imp1, 8);
		ImagePlus imp3 = new Opener().openImage(list1[7]);
		String sliceInfo2 = (String) imp2.getProperty("Info");
		String sliceInfo3 = (String) imp3.getProperty("Info");
		assertEquals(sliceInfo2, sliceInfo3);
	}

	@Test
	public final void testGenImaDifference() {
		String[] list1 = InputOutput
				.readStringArrayFromFile("./data/list1.txt");
		ImagePlus imp1 = new Opener().openImage(list1[0]);
		ImagePlus imp2 = new Opener().openImage(list1[1]);
		ImagePlus imp3 = UtilAyv.genImaDifference(imp1, imp2);
		ImagePlus impExpected = new Opener().openImage("./Test4/diff0102.tif");
		boolean res = UtilAyv.compareImagesByImageProcessors(imp3, impExpected);
		assertTrue("Image generated different than expected", res);
	}

	@Test
	public final void testGenImaDifference2() {
		String[] list1 = InputOutput
				.readStringArrayFromFile("./data/list1.txt");
		ImagePlus imp1 = new Opener().openImage(list1[0]);
		ImagePlus imp2 = new Opener().openImage(list1[1]);
		ImagePlus imp3 = UtilAyv.genImaDifference(imp1, imp2);
		ImagePlus impExpected = new Opener().openImage("./Test4/diff0102.tif");
		boolean res = UtilAyv.compareImagesByDifference(imp3, impExpected);
		assertTrue("Image generated different than expected", res);
	}

	@Test
	public final void testDiffIma() {
		String[] list1 = InputOutput
				.readStringArrayFromFile("./data/list1.txt");
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(list1[0], false);
		ImagePlus imp2 = UtilAyv.openImageNoDisplay(list1[1], false);
		ImagePlus imp3 = UtilAyv.diffIma(imp1, imp2);
		ImagePlus impExpected = UtilAyv.openImageNoDisplay(
				"./Test4/diff0102.tif", false);

		boolean res = UtilAyv.compareImagesByImageProcessors(imp3, impExpected);
		assertTrue("Image generated different than expected", res);
	}

	@Test
	public final void testCompareImages() {
		// yes, the same.
		testGenImaDifference();
	}

	@Test
	public final void testCloseResultsWindow() {
		String[] list1 = InputOutput
				.readStringArrayFromFile("./data/list1.txt");
		ImagePlus imp1 = new Opener().openImage(list1[0]);
		IJ.run(imp1, "Measure", "");
		UtilAyv.closeResultsWindow();
		// / questo metodo, usato per il test potrebbe essere candidato a fare
		// lui il close, togliendo i commenti
		// if (WindowManager.getFrame("Results") != null) {
		// IJ.selectWindow("Results");
		// IJ.run("Close");
		// }
		assertTrue("ResultsWindows not closed",
				WindowManager.getFrame("Results") == null);
	}

	@Test
	public final void testResetResultsTable() {
		String[] list1 = InputOutput
				.readStringArrayFromFile("./data/list1.txt");
		ImagePlus imp1 = new Opener().openImage(list1[0]);
		new Analyzer(imp1).measure();
		ResultsTable rt1 = Analyzer.getResultsTable();
		assertTrue(rt1.getCounter() > 0);
		UtilAyv.resetResultsTable();
		assertEquals("Results windows length not 0", 0, rt1.getCounter());
	}

	@Test
	public final void testCompareStacks() {
		String[] list1 = InputOutput
				.readStringArrayFromFile("./data/list1.txt");
		ImagePlus imp1 = UtilAyv.imagesToStack16(list1);
		ImagePlus imp2 = UtilAyv.imagesToStack16(list1);

		boolean result = UtilAyv.compareStacks(imp1, imp2);
		assertTrue("Stacks differ", result);

		list1[0] = ".\\Test4\\01diff";
		imp2 = UtilAyv.imagesToStack16(list1);
		boolean result2 = UtilAyv.compareStacks(imp1, imp2);
		assertFalse("Stacks must differ!", result2);
	}

	@Test
	public final void testVetFromMatrix() {
		String matPath = "./data/mat02.txt";
		double[][] mat1 = InputOutput.readDoubleMatrixFromFile(matPath);
		double[] vetExpected = InputOutput
				.readDoubleArrayFromFile("./data/vet11.txt");
		MyLog.logVector(vetExpected, "vetExpected");

		int index = 2;
		double[] vet1 = UtilAyv.vetFromMatrix(mat1, index);
		MyLog.logVector(vet1, "vet1");
		assertTrue("testVetFromMatrix.vectors are different",

		UtilAyv.compareVectors(vetExpected, vet1, 1e-8, ""));

	}

	@Test
	public final void testFirstCoil() {
		String codeInput = "C:BA1,2;SP4";
		// IJ.log("codice ingresso =" + codeInput);
		String coil = UtilAyv.firstCoil(codeInput);
		// IJ.log("codice letto =" + coil);
		assertTrue(coil.equals("C:BA1,2"));
	}

	@Test
	public final void testFirstCoilWithoutSemicolon() {
		String codeInput = "C:BA1,2";
		// IJ.log("codice ingresso =" + codeInput);
		String coil = UtilAyv.firstCoil(codeInput);
		// IJ.log("codice letto =" + coil);
		assertTrue(coil.equals("C:BA1,2"));
	}

	@Test
	public final void testFindMaximumPosition() {
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(".\\Test4\\B003_testP2",
				true);
		double[] expected = { 185.0, 33.0, 2597.0 };
		double[] out = UtilAyv.findMaximumPosition(imp1);
		// MyLog.logVector(out, "out");
		UtilAyv.compareVectors(expected, out, 1e-8, "");
	}

	@Test
	public final void testTruncateDecimals() {
		double in1 = 23.123456789;

		double out = UtilAyv.truncateDoubleDecimals(in1, 3);
		MyLog.waitHere("" + out);
	}

	@Test
	public final void testRoundDecimals() {
		double in1 = 23.123556789;

		double out = UtilAyv.roundDoubleDecimals(in1, 2);
		MyLog.waitHere("" + out);
	}

}
