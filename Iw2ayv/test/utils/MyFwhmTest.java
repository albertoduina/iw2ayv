package utils;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Plot;
import ij.gui.PlotWindow;
import ij.io.Opener;
import ij.util.Tools;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utils.MyLog;

public class MyFwhmTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testAnalyzeProfile() {

		double[] profi1 = InputOutput
				.readDoubleArrayFromFile("./data/vet12.txt");
		double dimPixel = 0.98765432;
		boolean invert = true;
		boolean step = false;
		String title = "profi1";
		// Color color = Color.red;
		// Plot plot = MyPlot.basePlot(profi1, title, color);
		// plot.show();
		// MyLog.waitHere();

		double[] outFwhm = MyFwhm.analyzeProfile(profi1, dimPixel, title,
				invert, step);
		double[] expected = { 30.980876270156863, 65.0 };
		// MyLog.logVector(outFwhm, "outFwhm");
		assertTrue(UtilAyv.compareVectors(outFwhm, expected, 1e-12,
				"errore comparazione"));
	}

	@Test
	public final void testHalfPointSearch() {

		double[] profi1 = InputOutput
				.readDoubleArrayFromFile("./data/vet12.txt");
		double[] profi2 = MyFwhm.invertProfile(profi1);
		int[] out2 = MyFwhm.halfPointSearch(profi2);
		double[] xVectPoints = new double[4];
		double[] yVectPoints = new double[4];
		xVectPoints[0] = (double) out2[0];
		xVectPoints[1] = (double) out2[1];
		xVectPoints[2] = (double) out2[2];
		xVectPoints[3] = (double) out2[3];
		yVectPoints[0] = (double) profi2[out2[0]];
		yVectPoints[1] = (double) profi2[out2[1]];
		yVectPoints[2] = (double) profi2[out2[2]];
		yVectPoints[3] = (double) profi2[out2[3]];
		// Plot plot2 = MyPlot.basePlot(profi2, "profi2", Color.blue);
		// plot2.show();
		// plot2.setColor(Color.RED);
		// plot2.addPoints(xVectPoints, yVectPoints, PlotWindow.CIRCLE);
		// MyLog.logVector(xVectPoints, "xVectPoints");
		// MyLog.logVector(yVectPoints, "yVectPoints");
		// MyLog.waitHere();
		int[] expected = { 51, 50, 81, 82 };
		assertTrue(UtilAyv
				.compareVectors(out2, expected, "errore comparazione"));
	}

	@Test
	public final void testCalcFwhm() {

		double[] profi1 = InputOutput
				.readDoubleArrayFromFile("./data/vet12.txt");
		double[] profi2 = MyFwhm.invertProfile(profi1);
		int[] out2 = MyFwhm.halfPointSearch(profi2);
		boolean printPlot = true;
		double dimPixel = 1;
		double fwhm = MyFwhm.calcFwhm(out2, profi2, dimPixel, "title",
				printPlot);
		MyLog.waitHere();
		// IJ.log("fwhm=" + fwhm);
		double expected = 31.36813725490196;
		assertEquals(expected, fwhm, 1e-12);
	}

	@Test
	public final void testPeakPosition() {

		double[] profi1 = InputOutput
				.readDoubleArrayFromFile("./data/vet12.txt");
		double[] profi2 = MyFwhm.invertProfile(profi1);
		double position = MyFwhm.peakPosition(profi2);
		double expected = 65.0;
		assertEquals(expected, position, 1e-12);
	}

	@Test
	public final void testYLinearInterpolation() {

		double x0 = 51;
		double y0 = 504;
		double x1 = 50;
		double y1 = 453;
		double x2 = 50.5;
		double interp = MyFwhm.yLinearInterpolation(x0, y0, x1, y1, x2);
		double expected = 478.5;
		assertEquals(expected, interp, 1e-12);
	}

	@Test
	public final void testXLinearInterpolation() {

		double x0 = 51;
		double y0 = 504;
		double x1 = 50;
		double y1 = 453;
		double y2 = 480.5;
		double interp = MyFwhm.xLinearInterpolation(x0, y0, x1, y1, y2);
		double expected = 50.53921568627451;
		assertEquals(expected, interp, 1e-12);
	}

	@Test
	public final void testMinimalPlot() {

		double[] profi1 = InputOutput
				.readDoubleArrayFromFile("./data/vet12.txt");
		double[] profi2 = MyFwhm.invertProfile(profi1);
		int[] upDwPoints = MyFwhm.halfPointSearch(profi2);
		boolean printPlot = false;
		String title = "";
		double dimPixel = 1.;
		double fwhm = MyFwhm.calcFwhm(upDwPoints, profi2, dimPixel, title,
				printPlot) * 1.00;
		MyFwhm.minimalPlot(profi2, upDwPoints, "P L O T", "base", "altezza",
				fwhm, true);
		MyLog.waitHere();
	}
}
