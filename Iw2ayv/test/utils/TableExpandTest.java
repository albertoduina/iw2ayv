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
		String fileName1 = "expand.csv";
		String[][] out1 = TableExpand.loadTable(fileName1);
		String fileName2 = "expand.txt";
		String[][] out2 = TableExpand.loadTable(fileName2);
	boolean ok= TableUtils.compareTable(out1, out2);
	assertTrue("expandCode diverse", ok);
	}

}

