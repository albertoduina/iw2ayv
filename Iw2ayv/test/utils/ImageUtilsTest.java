package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.ImageWindow;
import ij.gui.Overlay;
import ij.gui.Plot;
import ij.gui.PointRoi;
import ij.gui.WaitForUserDialog;
import ij.io.Opener;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ImageUtilsTest {

	@Before
	public void setUp() throws Exception {
//		new ImageJ(ImageJ.NORMAL);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testgetCircleLineCrossingPoints() {

		double x0 = 130;
		double y0 = 118;
		double x1 = 185;
		double y1 = 175;
		double xc = 130;
		double yc = 118;
		double rc = 87;
		double[] crossings = ImageUtils.getCircleLineCrossingPoints(x0, y0, x1,
				y1, xc, yc, rc);
		double[] expected = { 69.58988187652089, 55.393150308394375,
				190.4101181234791, 180.60684969160562 };
		assertTrue(UtilAyv.compareVectors(crossings, expected, 1e-11, ""));
	}

	@Test
	public final void testFromPointsToEquCirconferenceImplicit() {

		double cx = 150;
		double cy = 186;
		double radius = 120;

		double[] circonference = ImageUtils
				.fromPointsToEquCirconferenceImplicit(cx, cy, radius);
		double[] expected = { -300.0, -372.0, 42696.0 };
		assertTrue(UtilAyv.compareVectors(circonference, expected, 1e-11, ""));
	}

	@Test
	public final void testFitCircle() {

		int[] xPoints3 = { 58, 170, 213, 195, 86 };
		int[] yPoints3 = { 61, 39, 111, 165, 191 };

		ImagePlus imp12 = UtilAyv.openImageMaximized(".\\Test4\\bbb");
		Overlay over12 = new Overlay();
		imp12.setOverlay(over12);
		imp12.setRoi(new PointRoi(xPoints3, yPoints3, xPoints3.length));
		over12.addElement(imp12.getRoi());

		ImageUtils.fitCircle(imp12);
		Rectangle boundRec = imp12.getProcessor().getRoi();
		int xCenterCircle = Math.round(boundRec.x + boundRec.width / 2);
		int yCenterCircle = Math.round(boundRec.y + boundRec.height / 2);
		int diamCircle = boundRec.width;
		IJ.wait(200);

		assertEquals(xCenterCircle, 126);
		assertEquals(yCenterCircle, 113);
		assertEquals(diamCircle, 173);
	}

	@Test
	public final void testImageToFrontForImageWindow() {
		ImagePlus imp1 = UtilAyv.openImageMaximized(".\\Test4\\aaa");
		ImageWindow iw1 = imp1.getWindow();

		UtilAyv.openImageMaximized(".\\Test4\\01");
		UtilAyv.openImageMaximized(".\\Test4\\02");
		UtilAyv.openImageMaximized(".\\Test4\\03");

		ImageUtils.imageToFront(iw1);
		ImageWindow iw2 = (ImageWindow) WindowManager.getActiveWindow();
		assertTrue(iw1 == iw2);
	}

	@Test
	public final void testImageToFrontForImagePlus() {
		ImagePlus imp1 = UtilAyv.openImageMaximized(".\\Test4\\aaa");
		ImageWindow iw1 = imp1.getWindow();

		UtilAyv.openImageMaximized(".\\Test4\\01");
		UtilAyv.openImageMaximized(".\\Test4\\02");
		UtilAyv.openImageMaximized(".\\Test4\\03");

		ImageUtils.imageToFront(imp1);
		ImageWindow iw2 = (ImageWindow) WindowManager.getActiveWindow();
		assertTrue(iw1 == iw2);
	}

	@Test
	public final void testBackgroundEnhancement() {
		String[] list1 = InputOutput
				.readStringArrayFromFile("./data/list1.txt");
		ImagePlus imp1 = new Opener().openImage(list1[0]);
		imp1.show();

		ImageUtils.backgroundEnhancement(0, 0, 10, imp1);
		IJ.wait(300);
	}

	@Test
	public final void testGeneraSimulata12Classi() {

		String path1 = ".\\Test4\\aaa";
		int xCenterRoi = 100;
		int yCenterRoi = 100;
		int latoRoi = 50;
		boolean step = false;
		boolean verbose = false;
		boolean test = false;

		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);
		int[][] aux1 = ImageUtils.generaSimulata12classi(xCenterRoi,
				yCenterRoi, latoRoi, imp1, "", step, verbose, test);

	}

	@Test
	public final void testSimulata12Classi() {

		String path1 = ".\\Test4\\aaa";
		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);
		int sqX = 100;
		int sqY = 100;
		int sqR = 50;
		ImagePlus impSimulata = ImageUtils
				.simulata12Classi(sqX, sqY, sqR, imp1);
		UtilAyv.showImageMaximized(impSimulata);
	}

	@Test
	public final void testCrossing() {

		double x0 = 54.14;
		double y0 = 99.69;
		double x1 = 94.53;
		double y1 = 57.58;
		double width = 220;
		double height = 220;

		double[] out = ImageUtils.crossingFrame(x0, y0, x1, y1, width, height);
		String[] dummy = { "aa", "bb", "cc", "dd" };

		double[] vetResults1 = { 149.75812158632155, 0.0, 0.0, 156.135540975489 };
		boolean ok = UtilAyv.verifyResults1(vetResults1, out, dummy);
		assertTrue(ok);

		x0 = 25.78;
		y0 = 205.05868;
		x1 = 151.25;
		y1 = 132.34;

		out = ImageUtils.crossingFrame(x0, y0, x1, y1, width, height);
		// MyLog.logVector(out, "out");

		double[] vetResults2 = { 2.0627437110885412E-6, 220.0, 220.0,
				92.49454491113413 };
		ok = UtilAyv.verifyResults1(vetResults2, out, dummy);
		assertTrue(ok);
	}

	@Test
	public final void testFromPointsToEquLineExplicit() {

		double x1 = 0;
		double y1 = 128;
		double x2 = 255;
		double y2 = 0;
		double[] out1 = null;

		// x1 = 0;
		// y1 = 0;
		// x2 = 0;
		// y2 = 255;

		out1 = ImageUtils.fromPointsToEquLineExplicit(x1, y1, x2, y2);

		// MyLog.logVector(out1, "out1");
		double[] expected = { -0.5019607843137255, 128.0 };
		assertTrue(UtilAyv.compareVectors(out1, expected, 1e-11, ""));

	}

	@Test
	public final void testFromPointsToEquLineImplicit() {

		double x1 = 0;
		double y1 = 0;
		double x2 = 255;
		double y2 = 0;
		double[] out1 = null;

		x1 = 120;
		y1 = 30;
		x2 = 120;
		y2 = 50;

		out1 = ImageUtils.fromPointsToEquLineImplicit(x1, y1, x2, y2);

		// MyLog.logVector(out1, "out1");
		double[] expected = { -20.0, 0.0, 2400.0 };
		assertTrue(UtilAyv.compareVectors(out1, expected, 1e-11, ""));
	}

	@Test
	public final void testLiangBarsky() {

		double edgeLeft = 0.;
		double edgeRight = 255.;
		double edgeBottom = 0.;
		double edgeTop = 255.;
		double x0src = 20.;
		double y0src = -30.;
		double x1src = 200.;
		double y1src = 290.;

		double[] out = ImageUtils.liangBarsky(edgeLeft, edgeRight, edgeBottom,
				edgeTop, x0src, y0src, x1src, y1src);

		// MyLog.logVector(out, "out");
		// MyLog.waitHere();
		double[] expected = { 36.875, 0.0, 180.3125, 255.0 };
		assertTrue(UtilAyv.compareVectors(out, expected, 1e-11, ""));
	}

	@Test
	public final void testPeakDet2() {

		// ATTENZIONE NECESSITA DI MODIFICHE DOVUTE AL FATTO CHE HO CAMBIATO LA
		// STRUTTURA DELLA MATRICE

		double[][] profile2 = InputOutput
				.readDoubleMatrixFromFile((new InputOutput()
						.findResource("profile3d.txt")));
		double[][] profile1 = TableUtils.rotateTable(profile2);
		// double[] vetx = new double[profile1.length];
		// double[] vety = new double[profile1.length];
		// double[] vetz = new double[profile1.length];
		// for (int j = 0; j < profile1.length; j++) {
		// vetx[j] = profile1[j][0];
		// vety[j] = profile1[j][1];
		// vetz[j] = profile1[j][2];
		// }
		// Plot plot2 = MyPlot.basePlot(vetx, vety, "P R O F I L O",
		// Color.blue);
		// plot2.show();
		// new WaitForUserDialog("Do something, then click OK.").show();
		// IJ.wait(200);

		double delta = 100.0;
		ArrayList<ArrayList<Double>> matOut = ImageUtils.peakDet2(profile1,
				delta);
		double[][] out = new InputOutput().fromArrayListToDoubleTable(matOut);

		// MyLog.logMatrix(out, "out");
		// MyLog.waitHere();

		double[][] expected = { { 197.18881225585938, 73.15223693847656 },
				{ 62.81118392944336, 184.84776306152344 }, { 555.0, 2069.0 } };

		assertTrue(UtilAyv.compareMatrix(out, expected, ""));

	}

	@Test
	public final void testPeakDet() {

		// 16 dec 2011 sistemato, ora funziona in automatico senza bisogno di
		// visualizzare il profilo

		double[][] profile1 = InputOutput
				.readDoubleMatrixFromFile((new InputOutput()
						.findResource("BADProfile.txt")));
		double[] vetx = new double[profile1.length];
		double[] vety = new double[profile1.length];
		for (int j = 0; j < profile1.length; j++)
			vetx[j] = profile1[j][0];
		for (int j = 0; j < profile1.length; j++)
			vety[j] = profile1[j][1];
		// Plot plot2 = MyPlot.basePlot(vetx, vety, "P R O F I L O",
		// Color.blue);
		// plot2.show();
		// new WaitForUserDialog("Do something, then click OK.").show();
		// IJ.wait(200);
		double delta = 100.0;
		ArrayList<ArrayList<Double>> matOut = ImageUtils.peakDet(profile1,
				delta);

		double[][] out = new InputOutput().fromArrayListToDoubleTable(matOut);

		double[][] expected = { { 9.9609375, 128.3203125 },
				{ 445.2493818993196, 199.34767076939997 } };

		// MyLog.logMatrix(out, "out");
		// MyLog.logMatrix(expected, "expected");

		assertTrue(UtilAyv.compareMatrix(out, expected, ""));

		// assertEquals(expected, out[3][1], 1e-12);
	}

}
