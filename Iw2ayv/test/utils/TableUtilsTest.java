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
	public void testRotatetable() {

		double[][] inTable = { { 1.0, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9 },
				{ 2.0, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8, 2.9 },
				{ 3.0, 3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.7, 3.8, 3.9 } };
		MyLog.logMatrix(inTable, "inTable");
		double[][] rotated = TableUtils.rotateTable(inTable);
		MyLog.logMatrix(rotated, "rotated");
		MyLog.waitHere();
	}
}
