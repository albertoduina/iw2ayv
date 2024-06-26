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
	public final void testUserSelectionManual() {

		String version = "versione 0.0";
		String type = "TYPE";
		int risultato = 0;
		String autoStr = "PROVA";
		risultato = UtilAyv.userSelectionManual(version, type, autoStr);
		MyLog.waitHere("risultato = " + risultato);
		autoStr = "AUTOM";
		risultato = UtilAyv.userSelectionManual(version, type, autoStr);
		MyLog.waitHere("risultato = " + risultato);
		autoStr = "XXX";
		risultato = UtilAyv.userSelectionManual(version, type, autoStr);
		MyLog.waitHere("risultato = " + risultato);
		autoStr = "CHIUDI";
		risultato = UtilAyv.userSelectionManual(version, type, autoStr);
		MyLog.waitHere("risultato = " + risultato);

	}

	@Test
	public final void testCheckLimits() {

		double testSignal = 100.0;
		double low = 0.0;
		double high = 200.0;
		boolean ok = UtilAyv.checkLimits(testSignal, low, high);
		assertTrue(ok);

		testSignal = 200.0;
		ok = UtilAyv.checkLimits(testSignal, low, high);
		assertTrue(ok);

		testSignal = 250.0;
		ok = UtilAyv.checkLimits(testSignal, low, high);
		assertFalse(ok);

		testSignal = -200.0;
		ok = UtilAyv.checkLimits(testSignal, low, high);
		assertFalse(ok);

		testSignal = Double.NaN;
		ok = UtilAyv.checkLimits(testSignal, low, high);
		assertFalse(ok);

		testSignal = Double.NEGATIVE_INFINITY;
		ok = UtilAyv.checkLimits(testSignal, low, high);
		assertFalse(ok);

		testSignal = Double.POSITIVE_INFINITY;
		ok = UtilAyv.checkLimits(testSignal, low, high);
		assertFalse(ok);
	}

	@Test
	public final void testDecoderLimiti() {

		boolean absolute = false;
		String[][] limiti = new InputOutput().readFile6LIKE("LIMITI2.csv",
				absolute);
		String[] result = UtilAyv.decoderLimiti(limiti, "P10MAX");
		String[] expected = { "P10MAX", "2000", "1000", "800", "512", "1000",
				"1000", "1000", "1000", "1000", "1000", "1000" };
		boolean ok = UtilAyv.compareVectors(result, expected, "");
		assertTrue("result diverso da expected", ok);
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
		IJ.wait(50);
		WindowManager.closeAllWindows();
		// MyLog.waitHere();
		//
		// UtilAyv.afterWork();
		imagesOpened = WindowManager.getWindowCount();
		nonImagesOpened = WindowManager.getNonImageWindows().length;
		assertTrue(imagesOpened == 0);
		assertTrue(nonImagesOpened == 0);
	}

	// @Test
	// public final void testSetPrecision() {
	// double out = UtilAyv.setPrecision(98.55555555557, 3);
	// MyLog.waitHere("out= " + out);
	//
	// }

	@Test
	public final void testGetPos() {
		String xData = "div-by-zero=";
		UtilAyv.getPos(xData);
	}

	@Test
	public final void testLogResizer() {
		IJ.log("Messaggio di test");
		MyLog.waitHere();
		UtilAyv.logResizer();
		MyLog.waitHere();
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
	public final void testCompareDoubleWithTolerance1() {
		double num1 = 234.555555555555556;
		double num2 = 234.555555555555558;
		double tolerance = 0.000000000000001;
		boolean result = UtilAyv.compareDoublesWithTolerance(num1, num2,
				tolerance);
		MyLog.waitHere("result= " + result);
	}

	@Test
	public final void testCompareDoubleWithTolerance2() {
		/*
		 * durante i test ho notato che il test fallisce (o meglio non ottengo
		 * la stampa del valore introdotto) se utilizzo pi� di 13 digits,
		 * dovrebbero invece essere dai 14 ai 17 BOH??????
		 */
		double num1 = 234.5555555555556D;
		double num2 = 234.5555555555558D;
		int digits = 12;
		boolean result = UtilAyv
				.compareDoublesWithTolerance(num1, num2, digits);
		MyLog.waitHere("result= " + result);
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
		ImagePlus imp1 = MyStackUtils.imagesToStack16(list1);
		ImagePlus imp2 = MyStackUtils.imagesToStack16(list1);

		IJ.log("**** Stacks must differ ****");
		boolean result = MyStackUtils.compareStacks(imp1, imp2);
		assertTrue("Stacks differ", result);

		list1[0] = ".\\Test4\\01diff";
		imp2 = MyStackUtils.imagesToStack16(list1);
		boolean result2 = MyStackUtils.compareStacks(imp1, imp2);
		assertFalse("Stacks must differ!", result2);
	}

	@Test
	public final void testVetFromMatrix() {
		String matPath = "./data/mat02.txt";
		double[][] mat1 = InputOutput.readDoubleMatrixFromFile(matPath);
		// MyLog.logMatrix(mat1, "mat1");
		double[] vetExpected = { 40.0, 30.22222222222222, 27.857142857142858,
				28.37837837837838 };

		int index = 2;
		double[] vet1 = UtilAyv.vetFromMatrix(mat1, index);
		// MyLog.logVector(vet1, "vet1");

		assertTrue("testVetFromMatrix.vectors are different",
				UtilAyv.compareVectors(vetExpected, vet1, 1e-8, ""));

	}

	@Test
	public final void testTruncateDecimals() {
		double in1 = 23.123456789;
		double out = UtilAyv.truncateDoubleDecimals(in1, 3);
		double expected = 23.123;
		assertTrue(out == expected);
	}

	@Test
	public final void testRoundDecimals() {
		double in1 = 23.123556789;
		double out = UtilAyv.roundDoubleDecimals(in1, 2);
		double expected = 23.12;
		assertTrue(out == expected);
	}

	@Test
	public final void testToFloat0() {
		double in1 = 23.123556789;
		float out = UtilAyv.toFloat(in1);
		float expected = 23.123556f;
		assertTrue(out == expected);
	}

	@Test
	public final void testToFloat1() {
		double[] in1 = { 23.123556789, 11.1111111, 17.7776543 };
		float[] out = UtilAyv.toFloat(in1);
		float[] expected = { 23.123556f, 11.111111f, 17.777655f };
		assertTrue(UtilAyv.compareVectors(out, expected, 1e-8f, ""));
	}

	@Test
	public final void testToFloat2() {
		double[][] in1 = { { 23.123556789, 11.1111111, 17.7776543, 4 },
				{ 22.123556789, 12.1111111, 172.7776543, 3 },
				{ 24.123556789, 14.1111111, 174.7776543, 5 } };
		float[][] out = UtilAyv.toFloat(in1);
		float[][] expected = { { 23.123556f, 11.111111f, 17.777655f, 4.0f },
				{ 22.123556f, 12.111111f, 172.77765f, 3.0f },
				{ 24.123556f, 14.111111f, 174.77765f, 5.0f } };
		assertTrue(UtilAyv.compareMatrix(out, expected, ""));
	}

	@Test
	public final void testMinsort1() {
		int[] vet1 = { 11, 3, 8, 7, 5, 1, 12, 4, 7, 3, 2 };
		MyLog.logVector(vet1, "vet1 PRIMA");
		UtilAyv.minsort(vet1);
		MyLog.logVector(vet1, "vet1 DOPO");
		MyLog.waitHere();
	}

	@Test
	public final void testMinsort2() {
		int[] vet1 = { 11, 3, 8, 7, 5, 1, 12, 4, 7, 3, 2 };
		int[] vet2 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
		MyLog.logVector(vet1, "vet1 PRIMA");
		MyLog.logVector(vet2, "vet2 PRIMA");
		UtilAyv.minsort(vet1, vet2);
		MyLog.logVector(vet1, "vet1 DOPO");
		MyLog.logVector(vet2, "vet2 DOPO");
		MyLog.waitHere();
	}

	@Test
	public final void testShiftArray() {
		int[] vet1 = { 11, 3, 8, 7, 5, 1, 12, 4, 7, 3, 2 };
		MyLog.logVector(vet1, "vet1");
		int[] out1 = UtilAyv.shiftArray(vet1, -27);
		int[] expected = { 1, 12, 4, 7, 3, 2, 11, 3, 8, 7, 5 };
		MyLog.logVector(out1, "out1");
		MyLog.waitHere();

	}
}
