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

public class MyFilterTest {
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

	@Test
	public final void testMaxPosition5x5() {

		String path1 = "./data/C001_testP10";
		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);
		double[] position1 = MyFilter.maxPosition5x5(imp1);
		imp1.setRoi((int) Math.round(position1[0] - 2), (int) Math.round(position1[1] - 2), 5, 5);
		double[] expected = { 199.0, 77.0, 757.0 };
		assertTrue(UtilAyv.compareVectors(position1, expected, 1e-11, ""));
	}

	@Test
	public final void testMaxPosition7x7() {

		String path1 = "./data/C001_testP10";
		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);
		double[] position1 = MyFilter.maxPosition7x7(imp1);

		imp1.setRoi((int) Math.round(position1[0] - 3), (int) Math.round(position1[1] - 3), 7, 7);
		double[] expected = { 198.0, 77.0, 769.0204081632653 };
		assertTrue(UtilAyv.compareVectors(position1, expected, 1e-11, ""));
	}

	@Test
	public final void testMaxPosition9x9() {

		String path1 = "./data/C001_testP10";
		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);
		double[] position1 = MyFilter.maxPosition9x9(imp1);

		imp1.setRoi((int) Math.round(position1[0] - 4), (int) Math.round(position1[1] - 4), 7, 7);
		double[] expected = { 197.0, 78.0, 758.8395061728395 };
		assertTrue(UtilAyv.compareVectors(position1, expected, 1e-11, ""));
	}

	@Test
	public final void testMaxPosition11x11() {

		String path1 = "./data/C001_testP10";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, true);
		double[] position1 = MyFilter.maxPosition11x11(imp1);
		imp1.setRoi((int) Math.round(position1[0] - 4), (int) Math.round(position1[1] - 4), 7, 7);
		double[] expected = { 196.0, 78.0, 743.3719008264463 };
		assertTrue(UtilAyv.compareVectors(position1, expected, 1e-12, ""));
	}


//	@Test
//	public final void testMaxPositionGeneric() {
//
//		String path1 = "./data/C001_testP10";
//		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);
//
//		double[] position1 = MyFilter.maxPositionGeneric(imp1, 5);
//
//		Overlay over1 = new Overlay();
//		imp1.setOverlay(over1);
//		ImageUtils.plotPoints(imp1, over1, (int) position1[0], (int) position1[1]);
//		MyLog.waitHere();
//	}

	@Test
	public final void testMaxPosition11x11MARK() {

		String path1 = "./data/C001_testP10";
		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);
		double[] position1 = MyFilter.maxPosition11x11(imp1);
		Overlay over1 = new Overlay();
		imp1.setOverlay(over1);
		ImageUtils.plotPoints(imp1, over1, (int) position1[0], (int) position1[1]);
		MyLog.waitHere();
	}

	@Test
	public final void testMaxPosition11x11_PROBLEM() {

		String path1 = "./data/DUP_XZ_128.tif";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, true);
		double[] position1 = MyFilter.maxPosition11x11(imp1);
		MyLog.waitHere();
		imp1.setRoi((int) Math.round(position1[0] - 4), (int) Math.round(position1[1] - 4), 7, 7);
		double[] expected = { 196.0, 78.0, 743.3719008264463 };
		assertTrue(UtilAyv.compareVectors(position1, expected, 1e-12, ""));
	}

}