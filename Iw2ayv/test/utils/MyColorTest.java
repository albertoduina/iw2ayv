package utils;

import static org.junit.Assert.assertTrue;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.Line;
import ij.gui.Overlay;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.io.Opener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MyColorTest {
	@Before
	public void setUp() throws Exception {
		new ImageJ(ImageJ.NORMAL);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testMaxPosition1x1() {
		String path1 = "./data/C001_testP10";
		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);
		double[] position1 = MyFilter.maxPosition3x3(imp1);
		imp1.setRoi((int) Math.round(position1[0] - 1), (int) Math.round(position1[1] - 1), 3, 3);
		double[] expected = { 200.0, 76.0, 664.6666666666666 };
		assertTrue(UtilAyv.compareVectors(position1, expected, 1e-11, ""));
	}

	@Test
	public final void testMaxPosition3x3() {

		String path1 = "./data/C001_testP10";
		// String path1 = "C001_testP10";
		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);
		double[] position1 = MyFilter.maxPosition3x3(imp1);
		imp1.setRoi((int) Math.round(position1[0] - 1), (int) Math.round(position1[1] - 1), 3, 3);
		double[] expected = { 200.0, 76.0, 664.6666666666666 };
		assertTrue(UtilAyv.compareVectors(position1, expected, 1e-11, ""));
	}

}