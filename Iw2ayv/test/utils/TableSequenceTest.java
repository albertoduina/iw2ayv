/**
 * 
 */
package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ij.IJ;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utils.InputOutput;
import utils.TableSequence;
import utils.TableUtils;

// import contMensili.p6rmn_;

/**
 * @author DuinaA
 * 
 */
public class TableSequenceTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		String dirTest = "./test3/";
		if (InputOutput.checkDir(dirTest)) {

			boolean success1 = InputOutput.deleteDir(new File(dirTest));
			assertTrue("fallita cancellazione directory", success1);
		}
		boolean success2 = InputOutput.createDir(new File(dirTest));
		assertTrue("fallita creazione directory", success2);
		assertTrue("verificata presenza directory",
				InputOutput.checkDir(dirTest));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		// String dirTest = "./test3/";
		// InputOutput io = new InputOutput();
		// if (io.checkDir(dirTest)) {
		// boolean success1 = io.deleteDir(new File(dirTest));
		// assertTrue("fallita cancellazione directory", success1);
		// }
	}

	/**
	 * Metodo di verifica per
	 * {@link utils.OldListManager#caricaTabella(java.lang.String)}.
	 */
	@Test
	public final void testLoadtable() {
		String CODE_FILE = "./data/iw2ayv.txt";
		String[][] expected = InputOutput
				.readStringMatrixFromFile("./data/iw2ayvRead.txt");
		String[][] strTabella = new TableSequence().loadTable(CODE_FILE);
		boolean res = TableUtils.compareTable(expected, strTabella);
		assertTrue(res);
	}

	/**
	 * Metodo di verifica per
	 * {@link utils.TableSequence#countRows(java.lang.String)}.
	 */
	@Test
	public final void testContaRighe() {

		String CODE_FILE = "./data/iw2ayv.txt";
		int numeroRighe = TableSequence.countRows(CODE_FILE);
		assertEquals(4, numeroRighe);

	}

	/**
	 * Metodo di verifica per
	 * {@link utils.TableSequence#getPath(java.lang.String[][], int)} .
	 */
	@Test
	public final void testGetRow() {
		String CODE_FILE = "./data/iw2ayv.txt";
		String[][] strTabella = new TableSequence().loadTable(CODE_FILE);
		String risultato = TableSequence.getRow(strTabella, 2);
		assertEquals("la row non � corretta", "2", risultato);
	}
	
	/**
	 * Metodo di verifica per
	 * {@link utils.TableSequence#getPath(java.lang.String[][], int)} .
	 */
	@Test
	public final void testGetPath() {
		String CODE_FILE = "./data/iw2ayv.txt";
		String[][] strTabella = new TableSequence().loadTable(CODE_FILE);
		String risultato = TableSequence.getPath(strTabella, 2);
		assertEquals("il path non � corretto", "C:\\Dati\\cdqaera020811\\Study_1_20110802\\Series_34_BUSS_se_15b130_sag\\34_1331_BC_BUSS_", risultato);
	}

	/**
	 * Metodo di verifica per
	 * {@link utils.TableSequence#getCode(java.lang.String[][], int)} .
	 */
	@Test
	public final void testGetCode() {
		String CODE_FILE = "./data/iw2ayv.txt";
		String[][] strTabella = new TableSequence().loadTable(CODE_FILE);
		String risultato = TableSequence.getCode(strTabella, 2);
		assertEquals("il codice non � corretto", "BUSS_", risultato);
	}

	/**
	 * Metodo di verifica per
	 * {@link utils.TableSequence#getCode(java.lang.String[][], int)} .
	 */
	@Test
	public final void testGetCoil() {
		String CODE_FILE = "./data/iw2ayv.txt";
		String[][] strTabella = new TableSequence().loadTable(CODE_FILE);
		String risultato = TableSequence.getCoil(strTabella, 2);
		assertEquals("la Coil non � corretta ", "BC", risultato);
	}

	@Test
	public final void testGetEcho() {
		String CODE_FILE = "./data/iw2ayv.txt";
		String[][] strTabella = new TableSequence().loadTable(CODE_FILE);
		String risultato = TableSequence.getEcho(strTabella, 2);
		assertEquals("L'echo non � corretto", "20", risultato);
	}
	
	@Test
	public final void testGetDirez() {
		String CODE_FILE = "./data/iw2ayv.txt";
		String[][] strTabella = new TableSequence().loadTable(CODE_FILE);
		String risultato = TableSequence.getDirez(strTabella, 2);
		assertEquals("La direz non � corretta", "x", risultato);
	}
	
	@Test
	public final void testGetProfond() {
		String CODE_FILE = "./data/iw2ayv.txt";
		String[][] strTabella = new TableSequence().loadTable(CODE_FILE);
		String risultato = TableSequence.getProfond(strTabella, 2);
		assertEquals("La profond non � corretta", "x", risultato);
	}

	/**
	 * Metodo di verifica per
	 * {@link utils.TableSequence#getDone(java.lang.String[][], int)} .
	 */
	@Test
	public final void testGetFatto() {
		String CODE_FILE = "./data/iw2ayv.txt";
		String[][] strTabella = new TableSequence().loadTable(CODE_FILE);
		String risultato = TableSequence.getDone(strTabella, 2);
		assertEquals("il fatto non � corretto", "1", risultato);
	}

	@Test
	public final void testcompareTable() {
		String[][] strTabella1 = { { "aa11", "bb11", "cc11" },
				{ "aa22", "bb22", "cc22" } };
		String[][] strTabella2 = { { "aa11", "bb11", "cc11" },
				{ "aa22", "bb22", "cc22" } };
		boolean res = TableUtils.compareTable(strTabella1, strTabella2);
		assertTrue(res);
	}

	@Test
	public final void testcompareTableNull() {
		String[][] strTabella2 = { { "aa11", "bb11", "cc11" },
				{ "aa22", "bb22", "cc22" } };
		boolean res = TableUtils.compareTable(null, strTabella2);
		assertFalse(res);
	}

	@Test
	public final void testcompareTableDiffLen() {
		String[][] strTabella1 = { { "aa11", "bb11", "cc11" },
				{ "aa22", "bb22", "cc22" } };
		String[][] strTabella2 = { { "aa11", "bb11", "cc11" },
				{ "aa22", "bb22", "cc22" }, { "aa33", "bb33", "cc33" } };
		boolean res = TableUtils.compareTable(strTabella1, strTabella2);
		assertFalse(res);
	}

	@Test
	public final void testcompareTableDifferent() {
		String[][] strTabella1 = { { "aa11", "bb11", "cc11" },
				{ "aa22", "bb22", "cc22" } };
		String[][] strTabella2 = { { "aa12", "bb11", "cc11" },
				{ "aa22", "bb22", "cc22" } };
		boolean res = TableUtils.compareTable(strTabella1, strTabella2);
		assertFalse(res);
	}

}
