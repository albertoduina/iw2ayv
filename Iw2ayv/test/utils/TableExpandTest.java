package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TableExpandTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testLoadtable() {
		String EXPAND_FILE = "expand.txt";
		String[][] strExpand = TableExpand.loadTable(EXPAND_FILE);
		MyLog.logMatrix(strExpand, "strExpand");
	}

	@Test
	public final void testLoadtableCSV() {
		String EXPAND_FILE = "expand.csv";
		String[][] strExpand = TableExpand.loadTableCSV(EXPAND_FILE);
		MyLog.logMatrix(strExpand, "strExpand");
	}
}