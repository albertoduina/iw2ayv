/**
 * 
 */
package utils;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author DuinaA
 * 
 */
public class TableCodeTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadTable() {

	String fileName1 = "codiciNew.txt";	
	String[][] out1 = TableCode.loadTable(fileName1);
	String fileName2 = "codiciNew.csv";	
	String[][] out2 = TableCode.loadTable(fileName2);
	boolean ok= TableUtils.compareTable(out1, out2);
	assertTrue("tableCode diverse", ok);
	}
	

}