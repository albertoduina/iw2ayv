package utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ij.IJ;

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utils.InputOutput;
import utils.MyLog;
import utils.TableUtils;
import utils.UtilAyv;

public class InputOutputTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testFindCSV() {
		String fileName = "codiciNew.csv";
		boolean ok = new InputOutput().findCSV(fileName);

	}

	
	
	@Test
	public final void testReadDoubleMatrixFromFile() {
		String matPath = "./data/mat02.txt";
		double[][] expected = {
				{ 48.0, 28.444444444444443, 26.80952380952381,
						25.35135135135135 },
				{ 47.0, 30.22222222222222, 29.19047619047619,
						27.486486486486488 },
				{ 40.0, 30.22222222222222, 27.857142857142858,
						28.37837837837838 },
				{ 49.0, 29.666666666666668, 27.857142857142858,
						27.945945945945947 } };

		double[][] mat1 = InputOutput.readDoubleMatrixFromFile(matPath);
		// MyLog.logMatrix(mat1, matPath);
		assertTrue(UtilAyv.compareMatrix(mat1, expected, "errore comparazione"));

	}

	@Test
	public final void testReadFloatMatrixFromFile() {
		String matPath = "./data/mat02.txt";

		float[][] expected = {
				{ 48.0f, 28.444444444444443f, 26.80952380952381f,
						25.35135135135135f },
				{ 47.0f, 30.22222222222222f, 29.19047619047619f,
						27.486486486486488f },
				{ 40.0f, 30.22222222222222f, 27.857142857142858f,
						28.37837837837838f },
				{ 49.0f, 29.666666666666668f, 27.857142857142858f,
						27.945945945945947f } };

		float[][] mat1 = InputOutput.readFloatMatrixFromFile(matPath);
		// MyLog.logMatrix(mat1, matPath);
		assertTrue(UtilAyv.compareMatrix(mat1, expected, "errore comparazione"));

	}

	@Test
	public final void testReadIntMatrixFromFile() {
		String TEST_FILE = "./data/vet14.txt";
		int[][] expected = { { 0, 2169, 90, 2042 }, { 1, 2193, 91, 2042 },
				{ 18, 2163, 108, 2023 } };
		int[][] result = InputOutput.readIntMatrixFromFile(TEST_FILE);
		// MyLog.logMatrix(result, "result");
		assertTrue(UtilAyv.compareMatrix(result, expected,
				"errore comparazione"));
	}

	@Test
	public final void testReadStringMatrixFromFile() {
		String TEST_FILE = "./data/vet14.txt";
		String[][] expected = { { "0", "2169", "90", "2042" },
				{ "1", "2193", "91", "2042" }, { "18", "2163", "108", "2023" } };
		String[][] result = InputOutput.readStringMatrixFromFile(TEST_FILE);
		// MyLog.logMatrix(result, "result");
		// MyLog.logMatrix(expected, "expected");
		assertTrue(UtilAyv.compareMatrix(result, expected,
				"errore comparazione"));
	}

	@Test
	public final void testCreateDir() {

		String path = "./test33/";
		if (InputOutput.checkDir(path)) {
			boolean success = InputOutput.deleteDir(new File(path));
			assertTrue(
					"fallita cancellazione directory esistente, prima della creazione",
					success);
		}
		boolean success1 = InputOutput.createDir(new File(path));

		assertTrue("fallita creazione directory", success1);
		// assertTrue(InputOutput.checkDir("./test3/testAyv1/"));
	}

	@Test
	public final void testDeleteDir() {
		// assure directory exist
		String path = "./test33/";
		if (!InputOutput.checkDir(path)) {
			boolean success = InputOutput.createDir(new File(path));
			assertTrue(
					"fallita creazione directory inesistente, prima della cancellazione",
					success);
		}
		boolean success1 = InputOutput.deleteDir(new File(path));
		assertTrue("fallita cancellazione directory", success1);
	}

	@Test
	public final void testReadFile1() {
		String CODE_FILE = "/codici1.txt";
		String[][] expected = InputOutput
				.readStringMatrixFromFile("./data/codici1Read.txt");
		String tableCode[][] = new InputOutput().readFile1(CODE_FILE, 4);
		// Log.logMatrix(tableCode, "tableCode");
		assertTrue(TableUtils.compareTable(expected, tableCode));
	}

	@Test
	public final void testReadFile3() {
		String CODE_FILE = "codici3.txt";
		String[][] expected = InputOutput
				.readStringMatrixFromFile("./data/codici3Read.txt");
		ArrayList<ArrayList<String>> tabella1 = new InputOutput()
				.readFile3(CODE_FILE);

		String[][] tableCode = new InputOutput()
				.fromArrayListToStringTable(tabella1);
		// TableUtils.dumpTable(tableCode, "tableCode");
		assertTrue(TableUtils.compareTable(expected, tableCode));
	}

	@Test
	public final void testReadFile5() {
		String CODE_FILE = "./data/iw2ayv.txt";
		String[][] expected = InputOutput
				.readStringMatrixFromFile("./data/iw2ayvRead.txt");
		ArrayList<ArrayList<String>> tabella1 = new InputOutput()
				.readFile5(CODE_FILE);
		String[][] tableCode = new InputOutput()
				.fromArrayListToStringTable(tabella1);
		// Log.logMatrix(tableCode, "tableCode");
		// TableUtils.dumpTable(tableCode, "tableCode");
		assertTrue(TableUtils.compareTable(expected, tableCode));
	}

	@Test
	public final void testIsCommentTrue() {
		String riga = "// Questo è un commento";
		boolean result = InputOutput.isComment(riga);
		assertTrue("fallito riconoscimento commento", result);
	}

	@Test
	public final void testIsCommentFalse() {
		String riga = "Questo NON è un commento";
		boolean result = InputOutput.isComment(riga);
		assertFalse("fallito riconoscimento NON commento", result);
	}

	@Test
	public final void testStripSlashComment() {
		String riga = "Questo programma è/ una// grandissima stronzata";
		String result = InputOutput.stripSlashComment(riga);
		// System.out.printf("\nresult= " + result);
		assertTrue(result.equals("Questo programma è/ una"));
	}

	@Test
	public final void testStripSlashCommentInesistent() {
		String riga = "Questo programma è/ una/ grandissima stronzata";
		String result = InputOutput.stripSlashComment(riga);
		// System.out.printf("\nresult= " + result);
		assertTrue(result
				.equals("Questo programma è/ una/ grandissima stronzata"));
	}

	@Test
	public final void testStripComment() {
		String riga = "Questo programma è una /* grandissima */stronzata";
		String result = InputOutput.stripComment(riga);
		// System.out.printf("\nresult= " + result);
		assertTrue(result.equals("Questo programma è una stronzata"));

	}

	@Test
	public final void testStripCommentInexistent() {
		String riga = "Questo programma è una grandissima */stronzata";
		String result = InputOutput.stripComment(riga);
		// System.out.printf("\nresult= " + result);
		assertTrue(result
				.equals("Questo programma è una grandissima */stronzata"));
	}

	@Test
	public final void testStripAllComments() {
		String riga = "Questo /*programma*/ è /*una*/ cagata /* bellissimo*/ molto //cagoso";
		String result = InputOutput.stripAllComments(riga);
		// System.out.printf("\nresult= " + result);
		assertTrue(result.equals("Questo  è  cagata  molto "));

	}

	@Test
	public final void testReadDoubleArrayFromFile() {
		String TEST_FILE = "./data/vet05.txt";
		double[] expected = { 1048.927215, 1043.721519, 1042.943038,
				1042.183544, 1042.867089 };
		double[] result = InputOutput.readDoubleArrayFromFile(TEST_FILE);
		// Log.logVector(result, "result");
		assertTrue(UtilAyv.compareVectors(result, expected, 1e-8,
				"errore comparazione"));
	}

	@Test
	public final void testReadFloatArrayFromFile() {
		String TEST_FILE = "./data/vet05.txt";
		float[] expected = { (float) 1048.9272, (float) 1043.7216,
				(float) 1042.943, (float) 1042.1836, (float) 1042.8671 };
		float[] result = InputOutput.readFloatArrayFromFile(TEST_FILE);
		// Log.logVector(result, "result");
		assertTrue(UtilAyv.compareVectors(result, expected, (float) 1e-8,
				"errore comparazione"));

	}

	@Test
	public final void testReadIntArrayFromFile() {
		String TEST_FILE = "./data/vet04.txt";
		int[] expected = { 1, 54, 5678, 123, 5, 45, 777, 234, 0, };
		int[] result = InputOutput.readIntArrayFromFile(TEST_FILE);
		// attenzione, le righe bianche vengono lette come zeri
		// Log.logVector(result, "result");
		assertTrue(UtilAyv.compareVectors(result, expected,
				"errore comparazione"));
	}

	@Test
	public final void testReadStringArrayFromFile() {
		String TEST_FILE = "./data/vet05.txt";
		String[] expected = { "1048.927215", "1043.721519", "1042.943038",
				"1042.183544", "1042.867089" };

		String[] result = InputOutput.readStringArrayFromFile(TEST_FILE);
		// Log.logVector(result, "result");
		assertTrue(UtilAyv.compareVectors(result, expected,
				"errore comparazione"));

	}

	@Test
	public final void testAbsoluteToRelative() {
		String inStr = "C:\\2_eclipse_2\\eclipse\\workspace_fmri2\\ControlliMensili\\bin\\test3\\BTMA_01testP6";
		String outStr = InputOutput.absoluteToRelative(inStr);
		IJ.log(outStr);

	}

	@Test
	public final void testReadFile4() {
		String CODE_FILE = "./data/limiti.csv";
		String[][] expected = InputOutput
				.readStringMatrixFromFile("./data/iw2ayvRead.txt");
		String[][] tabella1 = new InputOutput().readFile4(CODE_FILE);
		// Log.logMatrix(tableCode, "tableCode");
		TableUtils.dumpTable(tabella1, "tabella1e");
		// assertTrue(TableUtils.compareTable(expected, tableCode));
	}

	@Test
	public final void testReadFile6() {

		// String CODE_FILE = "./data/limiti.csv"; // nota bene che è locale in
		// /data di iw2ayv, non in ControlliMensili !!!
		String CODE_FILE = "./data/codiciNew.csv"; // nota bene che è locale in
													// /data di iw2ayv, non in
													// ControlliMensili !!!
		// String[][] expected = InputOutput
		// .readStringMatrixFromFile("./data/iw2ayvRead.txt");
		String[][] tabella1 = new InputOutput().readFile6(CODE_FILE);
		// Log.logMatrix(tableCode, "tableCode");
		TableUtils.dumpTable(tabella1, "tabella1");
		// assertTrue(TableUtils.compareTable(expected, tableCode));
	}

	@Test
	public final void testReadFile7() {

		// String CODE_FILE = "./data/limiti.csv"; // nota bene che è locale in
		// /data di iw2ayv, non in ControlliMensili !!!
		String CODE_FILE = "./data/codiciNew.csv"; // nota bene che è locale in
													// /data di iw2ayv, non in
													// ControlliMensili !!!
		// String[][] expected = InputOutput
		// .readStringMatrixFromFile("./data/iw2ayvRead.txt");
		String[][] tabella1 = new InputOutput().readFile7(CODE_FILE);
		// Log.logMatrix(tableCode, "tableCode");
		TableUtils.dumpTable(tabella1, "tabella1");
		// assertTrue(TableUtils.compareTable(expected, tableCode));
	}

}
