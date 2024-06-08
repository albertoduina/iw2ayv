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
import ij.gui.PlotWindow;
import ij.gui.PointRoi;
import ij.gui.WaitForUserDialog;
import ij.io.Opener;
import ij.measure.CurveFitter;
import ij.util.Tools;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProfileUtilsTest {
	String path1 = ".\\data\\1RAD_HT2A";
	String path2 = ".\\data\\1RAD_HT5A";
	String path3 = ".\\data\\profi3.txt";
	int number = 2;
	String title = "slab 1";

	@Before
	public void setUp() throws Exception {
		new ImageJ(ImageJ.NORMAL);
	}

	@After
	public void tearDown() throws Exception {
	}

	private double[] readProfile(String path1, int number) {
		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);
		double ax = 0;
		double ay = 0;
		double bx = 0;
		double by = 0;

		switch (number) {
		case 1:
			ax = 61;
			ay = 76;
			bx = 198;
			by = 76;
			break;
		case 2:
			ax = 61;
			ay = 112;
			bx = 198;
			by = 112;
			break;
		case 3:
			ax = 61;
			ay = 146;
			bx = 198;
			by = 146;
			break;
		case 4:
			ax = 61;
			ay = 180;
			bx = 198;
			by = 180;
			break;
		}

		double[] profile1 = ProfileUtils.readLine(imp1, ax, ay, bx, by);
		// imp1.show();
		// MyLog.waitHere();
		return profile1;
	}

	@Test
	public final void testDifferentialQuotientDerivative() {

		double[] profile1 = readProfile(path1, number);
		double[] profile22 = ProfileUtils.squareSmooth2(profile1);
		double[] profile33 = ProfileUtils.squareSmooth2(profile22);
		double[] profile12 = ProfileUtils.derivataDQD(profile33);
		double[] profile3 = ProfileUtils.squareSmooth2(profile12);
		double[] profile4 = ProfileUtils.squareSmooth2(profile3);
		double[] profile2 = ProfileUtils.derivata(profile4);
		double[] vetx = new double[profile1.length];
		for (int i1 = 0; i1 < profile1.length; i1++) {
			vetx[i1] = i1;
		}
		Plot plot2 = new Plot("SEGNALE ORIGINALE " + title, "pixel", "valore");
		plot2.setColor(Color.red);
		plot2.addPoints(vetx, profile1, Plot.LINE);
		plot2.addLegend("originale " + title);
		plot2.setLimitsToFit(true);
		plot2.show();
		MyLog.waitHere("SEGNALE ORIGINALE");
		Plot plot3 = new Plot("SEGNALE DERIVATA + title", "pixel", "valore");
		plot3.setColor(Color.blue);
		plot3.addPoints(vetx, profile12, Plot.LINE);
		plot3.addLegend("derivata " + title);
		plot3.setLimitsToFit(true);
		plot3.show();
		MyLog.waitHere("SEGNALE DERIVATA " + title);
		Plot plot4 = new Plot("SEGNALE DQD + title", "pixel", "valore");
		plot4.setColor(Color.blue);
		plot4.addPoints(vetx, profile2, Plot.LINE);
		plot4.addLegend("dqd " + title);
		plot4.setLimitsToFit(true);
		plot4.show();
		MyLog.waitHere("SEGNALE DQD " + title);

	}

	@Test
	public final void testLocalizzatoreDopoDQD() {

		double[] profile1 = readProfile(path1, number);

		double[] profile2 = ProfileUtils.derivataDQD(profile1);
		double[] vetx = new double[profile1.length];
		for (int i1 = 0; i1 < profile1.length; i1++) {
			vetx[i1] = i1;
		}

		double threshold = 20.0;
		int[] out = ProfileUtils.localizzatoreDopoDQD(profile2, threshold);

		double[] outx = new double[2];
		outx[0] = out[0];
		outx[1] = out[1];
		double[] outy = new double[2];
		outy[0] = profile1[out[0]];
		outy[1] = profile1[out[1]];

		Plot plot2 = new Plot("SEGNALE ORIGINALE LIMITS", "pixel", "valore");
		plot2.setColor(Color.red);
		plot2.addPoints(vetx, profile1, Plot.LINE);
		plot2.setColor(Color.blue);
		plot2.addPoints(outx, outy, Plot.CIRCLE);
		plot2.addLegend("originale " + title);
		plot2.setLimitsToFit(true);
		plot2.show();
		MyLog.waitHere("SEGNALE ORIGINALE LIMITS " + title);
		outy[0] = profile2[out[0]];
		outy[1] = profile2[out[1]];

		Plot plot3 = new Plot("SEGNALE DQD LIMITS", "pixel", "valore");
		plot3.setColor(Color.red);
		plot3.addPoints(vetx, profile2, Plot.LINE);
		plot3.setColor(Color.blue);
		plot3.addPoints(outx, outy, Plot.CIRCLE);
		plot3.addLegend("elaborato " + title);
		plot3.setLimitsToFit(true);
		plot3.show();
		MyLog.waitHere("SEGNALE DQD LIMITS");

	}

	@Test
	public final void testRimuoviPicco() {

		double[] profileY = readProfile(path1, number);

		double[] profile2 = ProfileUtils.derivataDQD(profileY);
		double[] profileX = new double[profileY.length];
		for (int i1 = 0; i1 < profileY.length; i1++) {
			profileX[i1] = i1;
		}

		double threshold = 20.0;
		int[] out = ProfileUtils.localizzatoreDopoDQD(profile2, threshold);

		double[] outx = new double[2];
		outx[0] = out[0];
		outx[1] = out[1];
		double[] outy = new double[2];
		outy[0] = profileY[out[0]];
		outy[1] = profileY[out[1]];

		Plot plot2 = new Plot("SEGNALE ORIGINALE LIMITI", "pixel", "valore");
		plot2.setColor(Color.red);
		plot2.addPoints(profileX, profileY, Plot.LINE);
		plot2.setColor(Color.blue);
		plot2.addPoints(outx, outy, Plot.CIRCLE);
		plot2.addLegend("originale " + title);
		plot2.setLimitsToFit(true);
		plot2.show();
		MyLog.waitHere("SEGNALE ORIGINALE LIMITI");
		outy[0] = profile2[out[0]];
		outy[1] = profile2[out[1]];

		
		double[][] profile11= ProfileUtils.encode(profileX, profileY);

		double[][] profile3 = ProfileUtils.peakRemover(profile11, out[0], out[1], true);
		double[] xprofile3 = ProfileUtils.decodeX(profile3);
		double[] yprofile3 = ProfileUtils.decodeY(profile3);

		Plot plot3 = new Plot("DOPO POTATURA", "pixel", "valore");
		plot3.setColor(Color.red);
		plot3.addPoints(profileX, profileY, Plot.LINE);
		plot3.setColor(Color.blue);
		plot3.addPoints(xprofile3, yprofile3, Plot.X);
		plot3.addLegend("potatura " + title);
		plot3.setLimitsToFit(true);
		plot3.show();
		MyLog.waitHere("DOPO POTATURA");

		CurveFitter cf1 = new CurveFitter(xprofile3, yprofile3);
		cf1.doFit(CurveFitter.POLY2);
		double[] param1 = cf1.getParams();
		double[] vetfit = ProfileUtils.fitResult3(profileX, param1);
		double[] correctedProfile = new double[profileX.length];
		// double minC4 = ArrayUtils.vetMin(correctedM4);
		for (int i1 = 0; i1 < profileX.length; i1++) {
			correctedProfile[i1] = (profileY[i1] - vetfit[i1]);
		}
		double minM4 = ArrayUtils.vetMin(profileY);
		double minF4 = ArrayUtils.vetMin(correctedProfile);
		double kappa = minM4 - minF4;
		for (int i1 = 0; i1 < profileX.length; i1++) {
			correctedProfile[i1] = correctedProfile[i1] + kappa;
		}

		Plot plot6 = new Plot("SEGNALE CORRETTO", "pixel", "valore");
		plot6.setColor(Color.red);
		plot6.addPoints(profileX, profileY, Plot.LINE);
		plot6.setColor(Color.green);
		plot6.addPoints(profileX, correctedProfile, Plot.LINE);
		plot6.addLegend("originale " + title + "\ncorretto " + title);
		plot6.setLimitsToFit(true);
		plot6.show();
		MyLog.waitHere("SEGNALE CORRETTO");

	}

	@Test
	public final void testPeakWidth() {

		double[] profile1 = readProfile(path2, number);
		double[] profile1smooth = ProfileUtils.triangleSmooth3(profile1);
		double[] derivata1 = ProfileUtils.derivata(profile1smooth);
		double[] derivata1smooth = ProfileUtils.triangleSmooth3(derivata1);
		double[] derivata2 = ProfileUtils.derivata(derivata1smooth);

		double[][] profile2 = new double[2][derivata2.length];
		for (int i1 = 0; i1 < derivata2.length; i1++) {
			profile2[0][i1] = i1;
			profile2[1][i1] = derivata2[i1];
		}

		MyLog.logMatrixVertical(profile2, "profile2");
		MyLog.waitHere();

		double delta = 2;
		ArrayList<ArrayList<Double>> matOut = ProfileUtils.peakDetModificato(profile2, delta);
		double[][] out = new InputOutput().fromArrayListToDoubleTable(matOut);
		double[] outx = new double[out.length];
		double[] outy = new double[out.length];

		if (out[0].length > 1) {
			for (int j = 0; j < out.length; j++)
				outx[j] = out[0][j];
			for (int j = 0; j < out.length; j++)
				outy[j] = out[1][j];
		} else
			MyLog.waitHere("nessun risultato");

		// double[] profile4s = ProfileUtils.squareSmooth3(profile4);
		// double[] profile5 = ProfileUtils.derivata(profile4s);
		double[] vetx = new double[profile1.length];
		for (int i1 = 0; i1 < profile1.length; i1++)

		{
			vetx[i1] = i1;
		}

		Plot plot2 = new Plot("SEGNALE ORIGINALE " + title, "pixel", "valore");
		plot2.setColor(Color.red);
		plot2.addPoints(vetx, profile1, Plot.LINE);
		plot2.addLegend("originale " + title);
		plot2.setLimitsToFit(true);
		plot2.show();

		Plot plot3 = new Plot("SEGNALE DERIVATA1 + title", "pixel", "valore");
		plot3.setColor(Color.blue);
		plot3.addPoints(vetx, derivata1, Plot.LINE);
		plot3.addLegend("derivata1 " + title);
		plot3.setLimitsToFit(true);
		plot3.show();

		Plot plot4 = new Plot("SEGNALE DERIVATA2 + title", "pixel", "valore");
		plot4.setColor(Color.red);
		plot4.addPoints(vetx, derivata2, Plot.LINE);
		plot4.addLegend("derivata2 " + title);
		plot4.setColor(Color.blue);
		plot4.addPoints(outx, outy, Plot.X);
		plot4.setLimitsToFit(true);
		plot4.show();

		// Plot plot5 = new Plot("SEGNALE DERIVATA3 + title", "pixel",
		// "valore");
		// plot5.setColor(Color.blue);
		// plot5.addPoints(vetx, profile5, Plot.LINE);
		// plot5.addLegend("derivata3 "+title);
		// plot5.setLimitsToFit(true);
		// plot5.show();
		MyLog.waitHere("SEGNALE DERIVATA2 " + title);

	}

	@Test
	public final void testPeakDet() {

		// String fileName = InputOutput.findResource("BADProfile.txt");

		double[][] profile1 = InputOutput.readDoubleMatrixFromFile(path3);

		// MyLog.logMatrix(profile1, "profile1");
		// MyLog.waitHere();

		double[] vetx = new double[profile1.length];
		double[] vety = new double[profile1.length];
		for (int j = 0; j < profile1.length; j++)
			vetx[j] = profile1[j][0];
		for (int j = 0; j < profile1.length; j++)
			vety[j] = profile1[j][1]; // *(-1);

		double[][] profile2 = new double[profile1.length][2];
		for (int i1 = 0; i1 < profile1.length; i1++) {
			profile2[i1][0] = vetx[i1];
			profile2[i1][1] = vety[i1];
		}

		double delta = -1;
		ArrayList<ArrayList<Double>> matOut = ProfileUtils.peakDetModificato(profile2, delta);

		double[][] out = new InputOutput().fromArrayListToDoubleTable(matOut);
		if (out.length == 0)
			MyLog.waitHere("zero elements");
		int count0 = 0;
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		for (int i1 = 0; i1 < out[0].length; i1++) {
			if (!UtilAyv.isNaN(out[0][i1]))
				count0++;
		}
		for (int i1 = 0; i1 < out[1].length; i1++) {
			if (!UtilAyv.isNaN(out[1][i1]))
				count1++;
		}
		for (int i1 = 0; i1 < out[2].length; i1++) {
			if (!UtilAyv.isNaN(out[2][i1]))
				count2++;
		}
		for (int i1 = 0; i1 < out[3].length; i1++) {
			if (!UtilAyv.isNaN(out[3][i1]))
				count3++;
		}
		double[] minx = new double[count0];
		double[] miny = new double[count1];
		double[] maxx = new double[count2];
		double[] maxy = new double[count3];
		if (out[0].length == 0)
			MyLog.waitHere("zero length");
		for (int j = 0; j < count0; j++)
			minx[j] = out[0][j];
		for (int j = 0; j < count1; j++)
			miny[j] = out[1][j];
		for (int j = 0; j < count2; j++)
			maxx[j] = out[2][j];
		for (int j = 0; j < count3; j++)
			maxy[j] = out[3][j];

		// MyLog.logVector(minx, "outx");
		// MyLog.logVector(miny, "outy");
		// MyLog.waitHere("verifica quei vettori !!!!!");

		Plot plot1 = new Plot("SEGNALE", "pixel", "valore");
		plot1.setColor(Color.green);
		plot1.addPoints(vetx, vety, Plot.LINE);
		plot1.setColor(Color.blue);
		plot1.addPoints(minx, miny, Plot.CIRCLE);
		plot1.setColor(Color.red);
		plot1.addPoints(maxx, maxy, Plot.CIRCLE);
		plot1.addLegend("originale " + title);
		plot1.setLimitsToFit(true);
		plot1.show();
		MyLog.waitHere("SEGNALE");

		double[][] expected = { { 9.9609375, 128.3203125 }, { 445.2493818993196, 199.34767076939997 } };
		assertTrue(UtilAyv.compareMatrix(out, expected, ""));

		// assertEquals(expected, out[3][1], 1e-12);
	}

//	@Test
//	public final void testSecondDerivativeZeroCrossing() {
//
//		// String fileName = InputOutput.findResource("BADProfile.txt");
//
//		double[][] profile1 = InputOutput.readDoubleMatrixFromFile(path3);
//
//		// MyLog.logMatrix(profile1, "profile1");
//		// MyLog.waitHere();
//
//		double[] vetx = new double[profile1.length];
//		double[] vety = new double[profile1.length];
//		for (int j = 0; j < profile1.length; j++)
//			vetx[j] = profile1[j][0];
//		for (int j = 0; j < profile1.length; j++)
//			vety[j] = profile1[j][1]; // *(-1);
//
//		double[][] profile2 = new double[profile1.length][2];
//		for (int i1 = 0; i1 < profile1.length; i1++) {
//			profile2[i1][0] = vetx[i1];
//			profile2[i1][1] = vety[i1];
//		}
//
//		// Plot plot2 = new Plot("SEGNALE ORIGINALE " + title, "pixel",
//		// "valore");
//		// plot2.setColor(Color.red);
//		// plot2.addPoints(vetx, vety, Plot.LINE);
//		// plot2.addLegend("originale " + title);
//		// plot2.setLimitsToFit(true);
//		// plot2.show();
//
//		double delta = -1;
//		ArrayList<ArrayList<Double>> matOut = ProfileUtils.peakDetModificato(profile2, delta);
//
//		double[][] out = new InputOutput().fromArrayListToDoubleTable(matOut);
//		if (out.length == 0)
//			MyLog.waitHere("zero elements");
//
//		// MyLog.logMatrix(out, "out");
//
//		int count0 = 0;
//		int count1 = 0;
//		int count2 = 0;
//		int count3 = 0;
//		for (int i1 = 0; i1 < out[0].length; i1++) {
//			if (!UtilAyv.isNaN(out[0][i1]))
//				count0++;
//		}
//		for (int i1 = 0; i1 < out[1].length; i1++) {
//			if (!UtilAyv.isNaN(out[1][i1]))
//				count1++;
//		}
//		for (int i1 = 0; i1 < out[2].length; i1++) {
//			if (!UtilAyv.isNaN(out[2][i1]))
//				count2++;
//		}
//		for (int i1 = 0; i1 < out[3].length; i1++) {
//			if (!UtilAyv.isNaN(out[3][i1]))
//				count3++;
//		}
//		double[] minx = new double[count0];
//		double[] miny = new double[count1];
//		double[] maxx = new double[count2];
//		double[] maxy = new double[count3];
//		if (out[0].length == 0)
//			MyLog.waitHere("zero length");
//		for (int j = 0; j < count0; j++)
//			minx[j] = out[0][j];
//		for (int j = 0; j < count1; j++)
//			miny[j] = out[1][j];
//		for (int j = 0; j < count2; j++)
//			maxx[j] = out[2][j];
//		for (int j = 0; j < count3; j++)
//			maxy[j] = out[3][j];
//
//		// MyLog.logVector(minx, "minx");
//		// MyLog.logVector(miny, "miny");
//		// MyLog.logVector(maxx, "maxx");
//		// MyLog.logVector(maxy, "maxy");
//		// MyLog.waitHere("verifica quei vettori !!!!!");
//
//		Plot plot1 = new Plot("SEGNALE", "pixel", "valore");
//		plot1.setColor(Color.green);
//		plot1.addPoints(vetx, vety, Plot.LINE);
//		plot1.setColor(Color.blue);
//		plot1.addPoints(minx, miny, Plot.CIRCLE);
//		plot1.setColor(Color.red);
//		plot1.addPoints(maxx, maxy, Plot.CIRCLE);
//		plot1.addLegend("originale " + title);
//
//		double[] zerox1 = new double[2];
//		double[] zeroy1 = new double[2];
//		zerox1[0] = ProfileUtils.zeroCrossing(profile2, minx[0], maxx[0]);
//		zerox1[1] = ProfileUtils.zeroCrossing(profile2, minx[2], maxx[1]);
//		zeroy1[0] = 0;
//		zeroy1[1] = 0;
//		plot1.setColor(Color.black);
//		plot1.addPoints(zerox1, zeroy1, Plot.X);
//		plot1.setLimitsToFit(true);
//		plot1.show();
//		double pixfwhm = zerox1[1] - zerox1[0];
//		MyLog.waitHere("FWHM= " + pixfwhm * 0.78125);
//		double sTeor = 5;
//		double dimPix = 0.78125;
//		double[] spess = ProfileUtils.spessStrato(pixfwhm, pixfwhm, sTeor, dimPix);
//		MyLog.waitHere("SPESSORE= " + spess[0]);
//	}

//	@Test
//	public final void testSlab() {
//
//		double[] profile0 = readProfile(path2, number);
//
//		double[][] profile1 = new double[profile0.length][2];
//		for (int i1 = 0; i1 < profile1.length; i1++) {
//			profile1[i1][0] = i1;
//			profile1[i1][1] = profile0[i1];
//		}
//		// String fileName = InputOutput.findResource("BADProfile.txt");
//
//		// double[][] profile1 = InputOutput.readDoubleMatrixFromFile(path3);
//
//		// MyLog.logMatrix(profile1, "profile1");
//		// MyLog.waitHere();
//
//		double[] vetx = new double[profile1.length];
//		double[] vety = new double[profile1.length];
//		for (int j = 0; j < profile1.length; j++)
//			vetx[j] = profile1[j][0];
//		for (int j = 0; j < profile1.length; j++)
//			vety[j] = profile1[j][1]; // *(-1);
//
//		Plot plot2 = new Plot("PROFILO ORIGINALE " + title, "pixel", "valore");
//		plot2.setColor(Color.red);
//		plot2.addPoints(vetx, vety, Plot.LINE);
//		plot2.addLegend("originale " + title);
//		plot2.setLimitsToFit(true);
//		plot2.show();
//		MyLog.waitHere();
//
//		double[] smooth00 = ProfileUtils.squareSmooth2(profile0);
//		double[] smooth0 = ProfileUtils.squareSmooth2(smooth00);
//		double[] derivata1 = ProfileUtils.derivata(smooth0);
//		double[] smooth11 = ProfileUtils.squareSmooth2(derivata1);
//		double[] smooth1 = ProfileUtils.squareSmooth2(smooth11);
//		double[] derivata2 = ProfileUtils.derivata(smooth1);
//
//		Plot plot3 = new Plot("SEGNALE PRIMA DERIVATA + title", "pixel", "valore");
//		plot3.setColor(Color.green);
//		plot3.addPoints(vetx, derivata1, Plot.LINE);
//		plot3.addLegend("prima derivata " + title);
//		plot3.setLimitsToFit(true);
//		plot3.show();
//		MyLog.waitHere();
//
//		Plot plot4 = new Plot("SEGNALE SECONDA DERIVATA + title", "pixel", "valore");
//		plot4.setColor(Color.blue);
//		plot4.addPoints(vetx, derivata2, Plot.LINE);
//		plot4.addLegend("seconda derivata " + title);
//		plot4.setLimitsToFit(true);
//		plot4.show();
//		MyLog.waitHere();
//
//		// double[] profile4 = ProfileUtils.squareSmooth2(profile3);
//
//		double[][] profile2 = new double[profile1.length][2];
//		for (int i1 = 0; i1 < profile1.length; i1++) {
//			profile2[i1][0] = vetx[i1];
//			profile2[i1][1] = derivata2[i1];
//		}
//
//		double delta = -1;
//		ArrayList<ArrayList<Double>> matOut = ProfileUtils.peakDetModificato(profile2, delta);
//
//		double[][] out = new InputOutput().fromArrayListToDoubleTable(matOut);
//		if (out.length == 0)
//			MyLog.waitHere("zero elements");
//
//		// MyLog.logMatrix(out, "out");
//
//		int count0 = 0;
//		int count1 = 0;
//		int count2 = 0;
//		int count3 = 0;
//		for (int i1 = 0; i1 < out[0].length; i1++) {
//			if (!UtilAyv.isNaN(out[0][i1]))
//				count0++;
//		}
//		for (int i1 = 0; i1 < out[1].length; i1++) {
//			if (!UtilAyv.isNaN(out[1][i1]))
//				count1++;
//		}
//		for (int i1 = 0; i1 < out[2].length; i1++) {
//			if (!UtilAyv.isNaN(out[2][i1]))
//				count2++;
//		}
//		for (int i1 = 0; i1 < out[3].length; i1++) {
//			if (!UtilAyv.isNaN(out[3][i1]))
//				count3++;
//		}
//		double[] minx = new double[count0];
//		double[] miny = new double[count1];
//		double[] maxx = new double[count2];
//		double[] maxy = new double[count3];
//		if (out[0].length == 0)
//			MyLog.waitHere("zero length");
//		for (int j = 0; j < count0; j++)
//			minx[j] = out[0][j];
//		for (int j = 0; j < count1; j++)
//			miny[j] = out[1][j];
//		for (int j = 0; j < count2; j++)
//			maxx[j] = out[2][j];
//		for (int j = 0; j < count3; j++)
//			maxy[j] = out[3][j];
//
//		MyLog.logVector(minx, "minx");
//		MyLog.logVector(miny, "miny");
//		MyLog.logVector(maxx, "maxx");
//		MyLog.logVector(maxy, "maxy");
//		MyLog.waitHere("verifica quei vettori !!!!!");
//
//		Plot plot1 = new Plot("SEGNALE", "pixel", "valore");
//		plot1.setColor(Color.black);
//		plot1.addPoints(vetx, derivata2, Plot.LINE);
//		plot1.setColor(Color.blue);
//		plot1.addPoints(minx, miny, Plot.CIRCLE);
//		plot1.setColor(Color.red);
//		plot1.addPoints(maxx, maxy, Plot.CIRCLE);
//		plot1.addLegend("seconda derivata con punti " + title);
//
//		double[] zerox1 = new double[2];
//		double[] zeroy1 = new double[2];
//		zerox1[0] = ProfileUtils.zeroCrossing(profile2, minx[0], maxx[0]);
//		zerox1[1] = ProfileUtils.zeroCrossing(profile2, minx[minx.length - 1], maxx[maxx.length - 1]);
//		zeroy1[0] = 0;
//		zeroy1[1] = 0;
//		plot1.setColor(Color.black);
//		plot1.addPoints(zerox1, zeroy1, Plot.X);
//		plot1.setLimitsToFit(true);
//		plot1.show();
//		MyLog.waitHere();
//
//		double pixfwhm = zerox1[1] - zerox1[0];
//		MyLog.waitHere("FWHM= " + pixfwhm * 0.78125);
//		double sTeor = 5;
//		double dimPix = 0.78125;
//		double[] spess = ProfileUtils.spessStrato(pixfwhm, pixfwhm, sTeor, dimPix);
//		MyLog.waitHere("SPESSORE= " + spess[0]);
//	}
	
	
//	@Test
//	public final void testCuneo() {
//
//		double[] profile0 = readProfile(path2, 3);
//		title="cuneo";
//
//		double[][] profile1 = new double[profile0.length][2];
//		for (int i1 = 0; i1 < profile1.length; i1++) {
//			profile1[i1][0] = i1;
//			profile1[i1][1] = profile0[i1];
//		}
//		// String fileName = InputOutput.findResource("BADProfile.txt");
//
//		// double[][] profile1 = InputOutput.readDoubleMatrixFromFile(path3);
//
//		// MyLog.logMatrix(profile1, "profile1");
//		// MyLog.waitHere();
//
//		double[] vetx = new double[profile1.length];
//		double[] vety = new double[profile1.length];
//		for (int j = 0; j < profile1.length; j++)
//			vetx[j] = profile1[j][0];
//		for (int j = 0; j < profile1.length; j++)
//			vety[j] = profile1[j][1]; // *(-1);
//
//		Plot plot2 = new Plot("PROFILO ORIGINALE " + title, "pixel", "valore");
//		plot2.setColor(Color.red);
//		plot2.addPoints(vetx, vety, Plot.LINE);
//		plot2.addLegend("originale " + title);
//		plot2.setLimitsToFit(true);
//		plot2.show();
//		MyLog.waitHere();
//
//		double[] smooth00 = ProfileUtils.squareSmooth2(profile0);
//		double[] smooth0 = ProfileUtils.squareSmooth2(smooth00);
//		double[] derivata1 = ProfileUtils.derivata(smooth0);
//		double[] smooth11 = ProfileUtils.squareSmooth2(derivata1);
//		double[] smooth1 = ProfileUtils.squareSmooth2(smooth11);
//		double[] derivata2 = ProfileUtils.derivata(smooth1);
//
//		Plot plot3 = new Plot("SEGNALE PRIMA DERIVATA + title", "pixel", "valore");
//		plot3.setColor(Color.green);
//		plot3.addPoints(vetx, derivata1, Plot.LINE);
//		plot3.addLegend("prima derivata " + title);
//		plot3.setLimitsToFit(true);
//		plot3.show();
//		MyLog.waitHere();
//
//		Plot plot4 = new Plot("SEGNALE SECONDA DERIVATA + title", "pixel", "valore");
//		plot4.setColor(Color.blue);
//		plot4.addPoints(vetx, derivata2, Plot.LINE);
//		plot4.addLegend("seconda derivata " + title);
//		plot4.setLimitsToFit(true);
//		plot4.show();
//		MyLog.waitHere();
//
//		// double[] profile4 = ProfileUtils.squareSmooth2(profile3);
//
//		double[][] profile2 = new double[profile1.length][2];
//		for (int i1 = 0; i1 < profile1.length; i1++) {
//			profile2[i1][0] = vetx[i1];
//			profile2[i1][1] = derivata2[i1];
//		}
//
//		double delta = -1;
//		ArrayList<ArrayList<Double>> matOut = ProfileUtils.peakDetModificato(profile2, delta);
//
//		double[][] out = new InputOutput().fromArrayListToDoubleTable(matOut);
//		if (out.length == 0)
//			MyLog.waitHere("zero elements");
//
//		// MyLog.logMatrix(out, "out");
//
//		int count0 = 0;
//		int count1 = 0;
//		int count2 = 0;
//		int count3 = 0;
//		for (int i1 = 0; i1 < out[0].length; i1++) {
//			if (!UtilAyv.isNaN(out[0][i1]))
//				count0++;
//		}
//		for (int i1 = 0; i1 < out[1].length; i1++) {
//			if (!UtilAyv.isNaN(out[1][i1]))
//				count1++;
//		}
//		for (int i1 = 0; i1 < out[2].length; i1++) {
//			if (!UtilAyv.isNaN(out[2][i1]))
//				count2++;
//		}
//		for (int i1 = 0; i1 < out[3].length; i1++) {
//			if (!UtilAyv.isNaN(out[3][i1]))
//				count3++;
//		}
//		double[] minx = new double[count0];
//		double[] miny = new double[count1];
//		double[] maxx = new double[count2];
//		double[] maxy = new double[count3];
//		if (out[0].length == 0)
//			MyLog.waitHere("zero length");
//		for (int j = 0; j < count0; j++)
//			minx[j] = out[0][j];
//		for (int j = 0; j < count1; j++)
//			miny[j] = out[1][j];
//		for (int j = 0; j < count2; j++)
//			maxx[j] = out[2][j];
//		for (int j = 0; j < count3; j++)
//			maxy[j] = out[3][j];
//
//		MyLog.logVector(minx, "minx");
//		MyLog.logVector(miny, "miny");
//		MyLog.logVector(maxx, "maxx");
//		MyLog.logVector(maxy, "maxy");
//		MyLog.waitHere("verifica quei vettori !!!!!");
//
//		Plot plot1 = new Plot("SEGNALE", "pixel", "valore");
//		plot1.setColor(Color.green);
//		plot1.addPoints(vetx, derivata2, Plot.LINE);
//		plot1.setColor(Color.blue);
//		plot1.addPoints(minx, miny, Plot.CIRCLE);
//		plot1.setColor(Color.red);
//		plot1.addPoints(maxx, maxy, Plot.CIRCLE);
//		plot1.addLegend("seconda derivata con punti " + title);
//
//		double[] zerox1 = new double[2];
//		double[] zeroy1 = new double[2];
//		zerox1[0] = ProfileUtils.zeroCrossing(profile2, minx[0], maxx[0]);
//		zerox1[1] = ProfileUtils.zeroCrossing(profile2, minx[minx.length - 1], maxx[maxx.length - 1]);
//		zeroy1[0] = 0;
//		zeroy1[1] = 0;
//		plot1.setColor(Color.black);
//		plot1.addPoints(zerox1, zeroy1, Plot.X);
//		plot1.setLimitsToFit(true);
//		plot1.show();
//		MyLog.waitHere();
//
//		double pixfwhm = zerox1[1] - zerox1[0];
//		MyLog.waitHere("FWHM= " + pixfwhm * 0.78125);
//		double sTeor = 5;
//		double dimPix = 0.78125;
//		double[] spess = ProfileUtils.spessStrato(pixfwhm, pixfwhm, sTeor, dimPix);
//		MyLog.waitHere("SPESSORE= " + spess[0]);
//	}


}
