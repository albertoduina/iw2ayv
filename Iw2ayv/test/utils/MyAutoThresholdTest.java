package utils;

import static org.junit.Assert.assertTrue;
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

public class MyAutoThresholdTest {
	@Before
	public void setUp() throws Exception {
		new ImageJ(ImageJ.NORMAL);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testMyAutoThreshold() {
		String path1 = "./data/B003_testP2";
		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);

		boolean noBlack = false;
		boolean noWhite = false;
		boolean doWhite = true;
		boolean doSet = false;
		boolean doLog = true;
		String myMethod = "Mean";
		ImagePlus imp2 = MyAutoThreshold.threshold(imp1, myMethod, noBlack,
				noWhite, doWhite, doSet, doLog);
		UtilAyv.showImageMaximized(imp2);
		MyLog.waitHere();

	}
	@Test
	public final void testMyHuangThreshold() {
		String path1 = "./data/HWSA_testP7";
		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);

		boolean noBlack = false;
		boolean noWhite = false;
		boolean doWhite = true;
		boolean doSet = false;
		boolean doLog = true;
		String myMethod = "Huang";
		ImagePlus imp2 = MyAutoThreshold.threshold(imp1, myMethod, noBlack,
				noWhite, doWhite, doSet, doLog);
		UtilAyv.showImageMaximized(imp2);
		MyLog.waitHere();

	}

	@Test
	public final void testMyLiThreshold() {
		String path1 = "./data/HWSA_testP7";
		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);

		boolean noBlack = false;
		boolean noWhite = false;
		boolean doWhite = true;
		boolean doSet = false;
		boolean doLog = true;
		String myMethod = "Li";
		ImagePlus imp2 = MyAutoThreshold.threshold(imp1, myMethod, noBlack,
				noWhite, doWhite, doSet, doLog);
		UtilAyv.showImageMaximized(imp2);
		MyLog.waitHere();

	}
}