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

/**
 * @author DuinaA
 * 
 */
public class TableUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRotateTable() {

		double[][] inTable = { { 1.0, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9 },
				{ 2.0, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8, 2.9 },
				{ 3.0, 3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.7, 3.8, 3.9 } };
		double[][] rotated = TableUtils.rotateTable(inTable);
		double[][] expected = { { 1.0, 2.0, 3.0 }, { 1.2, 2.1, 3.1 },
				{ 1.3, 2.2, 3.2 }, { 1.4, 2.3, 3.3 }, { 1.5, 2.4, 3.4 },
				{ 1.6, 2.5, 3.5 }, { 1.7, 2.6, 3.6 }, { 1.8, 2.7, 3.7 },
				{ 1.9, 2.8, 3.8 } };
		boolean ok = TableUtils.compareTable(rotated, expected);
		assertTrue("matLimiti diverse", ok);
	}

	@Test
	public void testSumMultipleTable() {

		String[][] tableCodeTotal = { { "tot1.1", "tot1.2", "tot1.3", "tot1.4", "tot1.5" },
				{ "tot2.0", "tot2.1", "tot2.2", "tot2.3", "tot2.4", "tot2.5"},
				{ "tot3.0", "tot3.1", "tot3.2", "tot3.3", "tot3.4", "tot3.5" } };
		
		String[][] tableCodeAdded = { { "add1.1", "add1.2", "add1.3", "add1.4", "add1.5" },
				{ "add2.0", "add2.1", "add2.2", "add2.3", "add2.4", "add2.5"},
				{ "add3.0", "add3.1", "add3.2", "add3.3", "add3.4", "add3.5" } };
		
		
		
		 String[][] result = TableUtils.sumMultipleTable( tableCodeTotal,
				 tableCodeAdded);
		 MyLog.logMatrix(tableCodeTotal, "tableCodeTotal");
		 MyLog.logMatrix(tableCodeAdded, "tableCodeAdded");
		 MyLog.logMatrix(result, "result");
		 MyLog.waitHere();

	}
	
	
	@Test
	public void testSumMultipleTableNull() {

		String[][] tableCodeTotal = { { "tot1.1", "tot1.2", "tot1.3", "tot1.4", "tot1.5" },
				{ "tot2.0", "tot2.1", "tot2.2", "tot2.3", "tot2.4", "tot2.5"},
				{ "tot3.0", "tot3.1", "tot3.2", "tot3.3", "tot3.4", "tot3.5" } };
		
		String[][] tableCodeAdded = { { "add1.1", "add1.2", "add1.3", "add1.4", "add1.5" },
				{ "add2.0", "add2.1", "add2.2", "add2.3", "add2.4", "add2.5"},
				{ "add3.0", "add3.1", "add3.2", "add3.3", "add3.4", "add3.5" } };
		
		
		
		 String[][] result1 = TableUtils.sumMultipleTable( null,
				 tableCodeTotal);
		 String[][] result2 = TableUtils.sumMultipleTable( result1,
				 tableCodeAdded);
		 String[][] result3 = TableUtils.sumMultipleTable( result2,
				 tableCodeAdded);
		 MyLog.logMatrix(tableCodeTotal, "tableCodeTotal");
		 MyLog.logMatrix(tableCodeAdded, "tableCodeAdded");
		 MyLog.logMatrix(result1, "result1");
		 MyLog.logMatrix(result2, "result2");
		 MyLog.logMatrix(result3, "result3");
		 
		 MyLog.waitHere();

	}
}
