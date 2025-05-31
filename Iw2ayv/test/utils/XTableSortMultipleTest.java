package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This program demonstrates how to sort a list collection by multiple
 * attributes using a chained comparator.
 *
 * @author www.codejava.net
 *
 */
public class XTableSortMultipleTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testTableSort() {

		System.out.println("===== SORTING BY MULTIPLE ATTRIBUTES =====");

		List<XTable> listTable = new ArrayList<XTable>();

		listTable.add(new XTable(0, "path0", "C1AS_", "B12", 0, 0, 0, 0, 0, 0, 0, 1, 1, "154614312500.0", 20, 110.0,
				"hdx", 30, 0));
		listTable.add(new XTable(1, "path1", "C1AS_", "B11", 1, 1, 1, 1, 1, 1, 1, 2, 2, "154614312500.0", 20, 110.0,
				"hdx", 30, 0));
		listTable.add(new XTable(2, "path2", "C1AS_", "B11", 2, 2, 2, 2, 2, 2, 2, 1, 16, "154614312500.0", 100, 110.0,
				"hdx", 30, 0));
		listTable.add(new XTable(3, "path3", "C1AS_", "B13", 3, 3, 3, 3, 3, 3, 3, 1, 4, "154614312500.0", 20, 110.0,
				"hdx", 30, 0));
		listTable.add(new XTable(4, "path4", "C1AS_", "B13", 4, 4, 4, 4, 4, 4, 4, 1, 18, "154614312500.0", 100, 110.0,
				"hdx", 30, 0));
		listTable.add(new XTable(5, "path5", "C1AS_", "B13", 5, 5, 5, 5, 5, 5, 5, 2, 4, "154614312500.0", 20, 110.0,
				"hdx", 30, 0));
		listTable.add(new XTable(6, "path6", "C1AS_", "B12", 6, 6, 6, 6, 6, 6, 6, 2, 15, "154614312500.0", 100, 110.0,
				"hdx", 30, 0));
		listTable.add(new XTable(7, "path7", "C1AS_", "B12", 7, 7, 7, 7, 7, 7, 7, 1, 15, "154614312500.0", 100, 110.0,
				"hdx", 30, 0));
		listTable.add(new XTable(8, "path8", "C1AS_", "B14", 8, 8, 8, 8, 8, 8, 8, 1, 3, "154614312500.0", 20, 110.0,
				"hdx", 30, 0));
		listTable.add(new XTable(9, "path9", "C1AS_", "B14", 9, 9, 9, 9, 9, 9, 9, 1, 17, "154614312500.0", 100, 110.0,
				"hdx", 30, 0));
//		listTable.add(new Table(10, "C1AS_", "B14", 10, 10, 10, 10, 2, 17, 100.0, 110.0));
//		listTable.add(new Table(11, "C1AS_", "B12", 11, 11, 11, 11, 2, 1, 20.0, 110.0));
//		listTable.add(new Table(12, "C1AS_", "B11", 12, 12, 12, 12, 2, 16, 100.0, 110.0));
//		listTable.add(new Table(13, "C1AS_", "B15", 13, 13, 13, 13, 1, 6, 20.0, 110.0));
//		listTable.add(new Table(14, "C1AS_", "B15", 14, 14, 14, 14, 2, 20, 100.0, 110.0));
//		listTable.add(new Table(15, "C1AS_", "B15", 15, 15, 15, 15, 2, 6, 20.0, 110.0));
//		listTable.add(new Table(16, "C1AS_", "B16", 16, 16, 16, 16, 1, 5, 20.0, 110.0));
//		listTable.add(new Table(17, "C1AS_", "B16", 17, 17, 17, 17, 2, 19, 100.0, 110.0));
//		listTable.add(new Table(18, "C1AS_", "B16", 18, 18, 18, 18, 1, 19, 100.0, 110.0));
//		listTable.add(new Table(19, "C1AS_", "B11", 19, 19, 19, 19, 1, 2, 20.0, 110.0));
//		listTable.add(new Table(20, "C1AS_", "B16", 20, 20, 20, 20, 2, 5, 20.0, 110.0));
//		listTable.add(new Table(21, "C1AS_", "B15", 21, 21, 21, 21, 1, 20, 100.0, 110.0));
//		listTable.add(new Table(22, "C1AS_", "B14", 22, 22, 22, 22, 2, 3, 20.0, 110.0));
//		listTable.add(new Table(23, "C1AS_", "B13", 23, 23, 23, 23, 2, 18, 100.0, 110.0));

		System.out.println("*** Before sorting:");
		String aux1 = "ROW\tCODE\tCOIL\tI_PASS\tI_ORD\tI_INCR\tI_MULT\tSPA1\tSPA2\tSERIE\tACQ\tIMA\tACQTIME\tECHO\tPOS\tDIR\tPROF\tDON\tLENE";
		System.out.println(aux1);
		for (XTable emp : listTable) {
			System.out.println(emp);
		}

		Collections.sort(listTable,
				new XTableChainedComparator(new XTableCodeComparator(), new XTableCoilComparator(),
						new XTableAcqComparator(), new XTableImaComparator(), new XTableEchoComparator(),
						new XTablePosComparator()));

		System.out.println("\n*** After sorting:");
		System.out.println(aux1);
		for (XTable emp : listTable) {
			System.out.println(emp);
		}

		MyLog.waitHere("SEMBRA FUNZIONARE BENE !!!!!!!");
	}
}