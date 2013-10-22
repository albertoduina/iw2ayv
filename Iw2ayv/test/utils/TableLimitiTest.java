package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TableLimitiTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testLoadtable() {
		String EXPAND_FILE = "limiti.txt";
		String[][] matLimiti = TableExpand.loadTable(EXPAND_FILE);
		MyLog.logMatrix(matLimiti, "matLimiti");
	}

	@Test
	public final void testLoadtableCSV() {
		String EXPAND_FILE = "limiti.csv";
		String[][] matLimiti = TableExpand.loadTableCSV(EXPAND_FILE);
		MyLog.logMatrix(matLimiti, "matLimiti");
	}
}