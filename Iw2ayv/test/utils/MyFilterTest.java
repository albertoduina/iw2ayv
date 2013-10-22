package utils;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.Line;
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

		imp1.setRoi((int) Math.round(position1[0] - 1),
				(int) Math.round(position1[1] - 1), 3, 3);

		MyLog.logVector(position1, "position1");
		// MyLog.waitHere();
	}

	@Test
	public final void testMaxPosition3x3() {

		String path1 = "./data/C001_testP10";
		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);
		double[] position1 = MyFilter.maxPosition3x3(imp1);

		imp1.setRoi((int) Math.round(position1[0] - 1),
				(int) Math.round(position1[1] - 1), 3, 3);

		MyLog.logVector(position1, "position1");
		// MyLog.waitHere();
	}

	@Test
	public final void testMaxPosition5x5() {

		String path1 = "./data/C001_testP10";
		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);
		double[] position1 = MyFilter.maxPosition5x5(imp1);

		imp1.setRoi((int) Math.round(position1[0] - 2),
				(int) Math.round(position1[1] - 2), 5, 5);

		MyLog.logVector(position1, "position1");
		// MyLog.waitHere();
	}

	@Test
	public final void testMaxPosition7x7() {

		String path1 = "./data/C001_testP10";
		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);
		double[] position1 = MyFilter.maxPosition7x7(imp1);

		imp1.setRoi((int) Math.round(position1[0] - 3),
				(int) Math.round(position1[1] - 3), 7, 7);

		MyLog.logVector(position1, "position1");
		// MyLog.waitHere();
	}

	@Test
	public final void testMaxPosition9x9() {

		String path1 = "./data/C001_testP10";
		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);
		double[] position1 = MyFilter.maxPosition9x9(imp1);

		imp1.setRoi((int) Math.round(position1[0] - 4),
				(int) Math.round(position1[1] - 4), 7, 7);

		MyLog.logVector(position1, "position1");
		MyLog.waitHere();
	}

	@Test
	public final void testMaxPosition11x11() {

		String path1 = "./data/C001_testP10";
		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);
		double[] position1 = MyFilter.maxPosition11x11(imp1);

		imp1.setRoi((int) Math.round(position1[0] - 4),
				(int) Math.round(position1[1] - 4), 7, 7);

		MyLog.logVector(position1, "position1");
		// MyLog.waitHere();
	}

}