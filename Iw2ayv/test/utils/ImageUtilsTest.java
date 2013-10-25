package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import ij.IJ;
import ij.gui.Plot;
import ij.gui.WaitForUserDialog;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ImageUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
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
		double y1 = 0;
		double x2 = 255;
		double y2 = 0;
		double[] out1 = null;

		x1 = 0;
		y1 = 0;
		x2 = 0;
		y2 = 255;

		out1 = ImageUtils.fromPointsToEquLineExplicit(x1, y1, x2, y2);

		MyLog.logVector(out1, "out1");
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

		MyLog.logVector(out1, "out1");
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

		MyLog.logVector(out, "out");
	}

	@Test
	public final void testPeakDet2() {

		// ATTENZIONE NECESSITA DI MODIFICHE DOVUTE AL FATTO CHE HO CAMBIATO LA
		// STRUTTURA DELLA MATRICE

		double[][] profile2 = InputOutput
				.readDoubleMatrixFromFile((new InputOutput()
						.findResource("profile3d.txt")));		
		double [][] profile1 = TableUtils.rotateTable(profile2);
//		double[] vetx = new double[profile1.length];
//		double[] vety = new double[profile1.length];
//		double[] vetz = new double[profile1.length];
//		for (int j = 0; j < profile1.length; j++) {
//			vetx[j] = profile1[j][0];
//			vety[j] = profile1[j][1];
//			vetz[j] = profile1[j][2];
//		}
//		Plot plot2 = MyPlot.basePlot(vetx, vety, "P R O F I L O", Color.blue);
//		plot2.show();
//		new WaitForUserDialog("Do something, then click OK.").show();
//		IJ.wait(200);

		double delta = 100.0;
		ArrayList<ArrayList<Double>> matOut = ImageUtils.peakDet2(profile1,
				delta);
		MyLog.logArrayListTable(matOut, "matOut");
//		MyLog.waitHere();

		double[][] out = new InputOutput().fromArrayListToDoubleTable(matOut);

		double expected = 66.796875;
		assertEquals(expected, out[0][0], 1e-12);
		expected = 14.09287783266325;
		assertEquals(expected, out[1][0], 1e-12);
		expected = 9.9609375;
		assertEquals(expected, out[2][0], 1e-12);
		expected = 128.3203125;
		assertEquals(expected, out[2][1], 1e-12);
		expected = 445.2493818993196;
		assertEquals(expected, out[3][0], 1e-12);
		expected = 199.34767076939997;
		assertEquals(expected, out[3][1], 1e-12);
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
		double expected = 66.796875;
		assertEquals(expected, out[0][0], 1e-12);
		expected = 14.09287783266325;
		assertEquals(expected, out[1][0], 1e-12);
		expected = 9.9609375;
		assertEquals(expected, out[2][0], 1e-12);
		expected = 128.3203125;
		assertEquals(expected, out[2][1], 1e-12);
		expected = 445.2493818993196;
		assertEquals(expected, out[3][0], 1e-12);
		expected = 199.34767076939997;
		assertEquals(expected, out[3][1], 1e-12);
	}

	@Test
	public final void testPeakDetAAAAAA() {

		// 16 dec 2011 sistemato, ora funziona in automatico senza bisogno di
		// visualizzare il profilo

		double[][] profile1 = InputOutput
				.readDoubleMatrixFromFile((new InputOutput()
						.findResource("/profi3.txt")));
		double delta = 100.0;
		ArrayList<ArrayList<Double>> matOut = ImageUtils.peakDet2(profile1,
				delta);
		double[][] out = new InputOutput().fromArrayListToDoubleTable(matOut);

		double[] vetx = new double[profile1.length];
		double[] vety = new double[profile1.length];

		for (int j = 0; j < profile1.length; j++)
			vetx[j] = profile1[j][0];
		for (int j = 0; j < profile1.length; j++)
			vety[j] = profile1[j][1];

		Plot plot2 = MyPlot.basePlot(vetx, vety, "P R O F I L O", Color.blue);
		plot2.show();
		new WaitForUserDialog("Do something, then click OK.").show();
		IJ.wait(200);

		MyLog.logMatrix(out, "out");
		MyLog.waitHere();

		double expected = 66.796875;
		assertEquals(expected, out[0][0], 1e-12);
		expected = 14.09287783266325;
		assertEquals(expected, out[1][0], 1e-12);
		expected = 9.9609375;
		assertEquals(expected, out[2][0], 1e-12);
		expected = 128.3203125;
		assertEquals(expected, out[2][1], 1e-12);
		expected = 445.2493818993196;
		assertEquals(expected, out[3][0], 1e-12);
		expected = 199.34767076939997;
		assertEquals(expected, out[3][1], 1e-12);
	}

}
