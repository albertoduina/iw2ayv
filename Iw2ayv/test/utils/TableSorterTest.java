/**
 * 
 */
package utils;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

// import contMensili.p6rmn_;

/**
 * @author DuinaA
 * 
 */
public class TableSorterTest {

	/**
	 * Metodo di verifica per
	 * {@link utils.OldListManager#caricaTabella(java.lang.String)}.
	 */
	@Test
	public final void testLoadtable1() {
		ArrayList<ArrayList<String>> tabella1 = new InputOutput().readFile3LIKE("TableSequenceLoaded1.txt");
		String[][] tableCode1 = new InputOutput().fromArrayListToStringTable(tabella1);
		TableUtils.dumpTable(tableCode1);
		MyLog.waitHere();
	}

	@Test
	public final void testLoadtable2() {
		String[][] tableCode2 = InputOutput.readStringMatrixFromFileNew("TableSequenceLoaded1.txt", ";");
		TableUtils.dumpTable(tableCode2);
		MyLog.waitHere();
	}

	@Test
	public final void testMinsortString() {

		// SORT STRINGA ESPANSO ANCHE ALLE ALTRE COLONNE

		String[][] tableCode1 = InputOutput.readStringMatrixFromFileNew("TableSequenceLoaded1.txt", ";");

		TableUtils.dumpTable(tableCode1);

		String[][] tableOut1 = TableSorter.minsortString(tableCode1, TableSequence.CODE, "");

		TableUtils.dumpTable(tableOut1);
		MyLog.waitHere("DUMP_OUTPUT1");

	}

	@Test
	public final void testMinsortInteger() {

		// SORT INTEGER ESPANSO ANCHE ALLE ALTRE COLONNE

		String[][] tableCode2 = InputOutput.readStringMatrixFromFileNew("TableSequenceLoaded2.txt", ";");

		TableUtils.dumpTable(tableCode2);

		String[][] tableOut2 = TableSorter.minsortInteger(tableCode2, TableSequence.SPARE_2, "");

		TableUtils.dumpTable(tableOut2);
		MyLog.waitHere("DUMP_OUTPUT2");

	}

	@Test
	public final void testMinsortDouble() {

		// SORT DOUBLE ESPANSO ANCHE ALLE ALTRE COLONNE

		String[][] tableCode3 = InputOutput.readStringMatrixFromFileNew("TableSequenceLoaded2.txt", ";");

		TableUtils.dumpTable(tableCode3);

		String[][] tableOut3 = TableSorter.minsortDouble(tableCode3, TableSequence.ACQ, "");

		TableUtils.dumpTable(tableOut3);
		MyLog.waitHere("DUMP_OUTPUT3");

	}

	@Test
	public final void testTableSwapper() {

		String CODE_FILE = "TableSequenceLoaded.txt";

		String[][] tableCode = InputOutput.readStringMatrixFromFileNew(CODE_FILE, ";");
		MyLog.logMatrix(tableCode, "TableCode");
		MyLog.waitHere();

		TableSorter.tableSwapper(tableCode, 10, 20);
		MyLog.logMatrix(tableCode, "TableCode");
		MyLog.waitHere();

	}

	@Test
	public final void testTableModifierSmart1() {

		String CODE_FILE = "TableSequenceLoaded.txt";

		String[][] tableCode = InputOutput.readStringMatrixFromFileNew(CODE_FILE, ";");
		MyLog.logMatrix(tableCode, "TableCode");
		MyLog.waitHere();
		boolean debug1 = true;
		String[] myCode = { "BL2F_", "BL2S_", "BR2F_", "BR2S_" };
		TableSorter.tableModifierSmart(tableCode, myCode);

		MyLog.logMatrix(tableCode, "TableCode");
		MyLog.waitHere();

	}

	@Test
	public final void testTableModifierSmart2() {

		String CODE_FILE = "TableSequenceReordered.txt";

		String[][] tableCode = InputOutput.readStringMatrixFromFileNew(CODE_FILE, ";");
//		MyLog.logMatrix(tableCode, "TableCode");
//		MyLog.waitHere();
		boolean debug1 = true;
		String[] myCode = { "JUS1A", "JUSAA" };
		String[][] tableOut = TableSorter.tableModifierSmart(tableCode, myCode);

		MyLog.logMatrix(tableOut, "TableOut");
		MyLog.waitHere();

	}
	
	@Test
	public final void testMergeSortDouble() {
		
		double[] vetIn2 = { 6.5, 9.9, 8.2, 2.1, 4.0, 1.7 };
		
		MyLog.logVector(vetIn2, "vetIn prima");
		
		TableSorter.mergeSortDouble(vetIn2);

		MyLog.logVector(vetIn2, "vetIn2 dopo");
		MyLog.waitHere();
	}
	
	
	@Test
	public final void testMergeSortInteger() {

		int[] vetIn1 = { 6, 9, 8, 2, 4, 1 };
	
		MyLog.logVector(vetIn1, "vetIn prima");
		
		TableSorter.mergeSortInteger(vetIn1);
	
		MyLog.logVector(vetIn1, "vetIn dopo");
		MyLog.waitHere();
	}

	
	@Test
	public final void testMergeSortString() {

		String[] vetIn1 = { "c6", "aa9", "b8", "cazzo", "tre", "uno" };
		
		MyLog.logVector(vetIn1, "vetIn prima");
		
		TableSorter.mergeSortString(vetIn1);
	
		MyLog.logVector(vetIn1, "vetIn dopo");
		MyLog.waitHere();
	}

	
	

}
