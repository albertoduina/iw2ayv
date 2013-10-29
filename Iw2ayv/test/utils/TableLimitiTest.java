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
		String EXPAND_FILE1 = "limiti.txt";
		String[][] matLimiti1 = TableLimiti.loadTable(EXPAND_FILE1);
		String EXPAND_FILE2 = "limiti.csv";
		String[][] matLimiti2 = TableLimiti.loadTable(EXPAND_FILE2);
		boolean ok= TableUtils.compareTable(matLimiti1, matLimiti2);
		assertTrue("matLimiti diverse", ok);
	}

	
}