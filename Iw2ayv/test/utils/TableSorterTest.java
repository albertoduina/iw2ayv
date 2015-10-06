/**
 * 
 */
package utils;

import java.util.ArrayList;

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
	public final void testLoadtable() {
		boolean absolute = false;
		String CODE_FILE = "TableSequenceLoaded.txt";
		ArrayList<ArrayList<String>> tabella1 = new InputOutput()
				.readFile3LIKE(CODE_FILE);
		String[][] tableCode = new InputOutput()
				.fromArrayListToStringTable(tabella1);
		TableUtils.dumpTable(tableCode);
		MyLog.waitHere();
	}

	@Test
	public final void testMinsort() {

		//
		// dimostra che � possibile eseguire pi� di un sort, in coda ad un
		// altro. Questo serve quando si vuole fare un sort in base a criteri
		// multipli
		//
		boolean absolute = false;
		String CODE_FILE = "TableSequenceLoaded.txt";
		// String[][] tableCode = new InputOutput().readFile6LIKE(CODE_FILE,
		// absolute);

		String[][] tableCode = InputOutput.readStringMatrixFromFileNew(
				CODE_FILE, ";");

		// MyLog.logMatrix(tableCode, "TableCode");
		// MyLog.waitHere();
		// TableUtils.dumpTable(tableCode);
		// MyLog.waitHere();

		String[][] tableOut1 = TableSorter.minsort(tableCode, 9);
		MyLog.logMatrix(tableOut1, "TableOut1");
		MyLog.waitHere();

		String[][] tableOut2 = TableSorter.minsort(tableOut1, 8);
		MyLog.logMatrix(tableOut2, "TableOut2");
		MyLog.waitHere();

		// TableUtils.dumpTable(tableOut1);
		// MyLog.waitHere();

	}

	@Test
	public final void testTableSwapper() {

		String CODE_FILE = "TableSequenceLoaded.txt";

		String[][] tableCode = InputOutput.readStringMatrixFromFileNew(
				CODE_FILE, ";");
		MyLog.logMatrix(tableCode, "TableCode");
		MyLog.waitHere();

		TableSorter.tableSwapper(tableCode, 10, 20);
		MyLog.logMatrix(tableCode, "TableCode");
		MyLog.waitHere();

	
	}
	
	@Test
	public final void testTableModifierSmart1() {

		String CODE_FILE = "TableSequenceLoaded.txt";

		String[][] tableCode = InputOutput.readStringMatrixFromFileNew(
				CODE_FILE, ";");
		MyLog.logMatrix(tableCode, "TableCode");
		MyLog.waitHere();
		boolean debug1=true;
		String[] myCode= {"BL2F_","BL2S_", "BR2F_", "BR2S_"};
		TableSorter.tableModifierSmart(tableCode, myCode);
		
		MyLog.logMatrix(tableCode, "TableCode");
		MyLog.waitHere();

	
	}
	
	@Test
	public final void testTableModifierSmart2() {

		String CODE_FILE = "TableSequenceReordered.txt";

		String[][] tableCode = InputOutput.readStringMatrixFromFileNew(
				CODE_FILE, ";");
//		MyLog.logMatrix(tableCode, "TableCode");
//		MyLog.waitHere();
		boolean debug1=true;
		String[] myCode= { "JUS1A", "JUSAA"};
		String[][] tableOut = TableSorter.tableModifierSmart(tableCode, myCode);
		
		MyLog.logMatrix(tableOut, "TableOut");
		MyLog.waitHere();

	
	}

	

}
