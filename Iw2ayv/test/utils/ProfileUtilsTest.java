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
	int number = 1;
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
//		imp1.show();
//		MyLog.waitHere();
		return profile1;
	}

	@Test
	public final void testDifferentialQuotientDerivative() {

		double[] profile1 = readProfile(path1, number);
		double[] profile22 = ProfileUtils.squareSmooth2(profile1);
		double[] profile33 = ProfileUtils.squareSmooth2(profile22);	
		double[] profile12 = ProfileUtils.differentialQuotientDerivative(profile33);
		double[] profile3 = ProfileUtils.squareSmooth2(profile12);
		double[] profile4 = ProfileUtils.squareSmooth2(profile3);
		double[] profile2 = ProfileUtils.derivata(profile4);
		double[] vetx = new double[profile1.length];
		for (int i1 = 0; i1 < profile1.length; i1++) {
			vetx[i1] = i1;
		}
		Plot plot2 = new Plot("SEGNALE ORIGINALE "+title, "pixel", "valore");
		plot2.setColor(Color.red);
		plot2.addPoints(vetx, profile1, Plot.LINE);
		plot2.addLegend("originale "+title);
		plot2.setLimitsToFit(true);
		plot2.show();
		MyLog.waitHere("SEGNALE ORIGINALE");
		Plot plot3 = new Plot("SEGNALE DERIVATA + title", "pixel", "valore");
		plot3.setColor(Color.blue);
		plot3.addPoints(vetx, profile12, Plot.LINE);
		plot3.addLegend("derivata "+title);
		plot3.setLimitsToFit(true);
		plot3.show();
		MyLog.waitHere("SEGNALE DERIVATA "+title);
		Plot plot4 = new Plot("SEGNALE DQD + title", "pixel", "valore");
		plot4.setColor(Color.blue);
		plot4.addPoints(vetx, profile2, Plot.LINE);
		plot4.addLegend("dqd "+title);
		plot4.setLimitsToFit(true);
		plot4.show();
		MyLog.waitHere("SEGNALE DQD "+title);

	}

	@Test
	public final void testWedgeLimitsAfterDQD() {

		double[] profile1 = readProfile(path1, number);

		double[] profile2 = ProfileUtils.differentialQuotientDerivative(profile1);
		double[] vetx = new double[profile1.length];
		for (int i1 = 0; i1 < profile1.length; i1++) {
			vetx[i1] = i1;
		}

		double threshold = 20.0;
		int[] out = ProfileUtils.wedgeLimitsAfterDQD(profile2, threshold);

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
		plot2.addLegend("originale "+title);
		plot2.setLimitsToFit(true);
		plot2.show();
		MyLog.waitHere("SEGNALE ORIGINALE LIMITS "+title);
		outy[0] = profile2[out[0]];
		outy[1] = profile2[out[1]];

		Plot plot3 = new Plot("SEGNALE DQD LIMITS", "pixel", "valore");
		plot3.setColor(Color.red);
		plot3.addPoints(vetx, profile2, Plot.LINE);
		plot3.setColor(Color.blue);
		plot3.addPoints(outx, outy, Plot.CIRCLE);
		plot3.addLegend("elaborato "+title);
		plot3.setLimitsToFit(true);
		plot3.show();
		MyLog.waitHere("SEGNALE DQD LIMITS");

	}

	@Test
	public final void testPotatura() {

		double[] profile1 = readProfile(path1, number);

		double[] profile2 = ProfileUtils.differentialQuotientDerivative(profile1);
		double[] vetx = new double[profile1.length];
		for (int i1 = 0; i1 < profile1.length; i1++) {
			vetx[i1] = i1;
		}

		double threshold = 20.0;
		int[] out = ProfileUtils.wedgeLimitsAfterDQD(profile2, threshold);

		double[] outx = new double[2];
		outx[0] = out[0];
		outx[1] = out[1];
		double[] outy = new double[2];
		outy[0] = profile1[out[0]];
		outy[1] = profile1[out[1]];

		Plot plot2 = new Plot("SEGNALE ORIGINALE LIMITI", "pixel", "valore");
		plot2.setColor(Color.red);
		plot2.addPoints(vetx, profile1, Plot.LINE);
		plot2.setColor(Color.blue);
		plot2.addPoints(outx, outy, Plot.CIRCLE);
		plot2.addLegend("originale "+title);
		plot2.setLimitsToFit(true);
		plot2.show();
		MyLog.waitHere("SEGNALE ORIGINALE LIMITI");
		outy[0] = profile2[out[0]];
		outy[1] = profile2[out[1]];

		ArrayList<ArrayList<Double>> arrprofile3 = ProfileUtils.potatura(profile1, out[0], out[1]);
		double[] xprofile3 = ArrayUtils.arrayListToArrayDouble(arrprofile3.get(0));
		double[] yprofile3 = ArrayUtils.arrayListToArrayDouble(arrprofile3.get(1));

		Plot plot3 = new Plot("DOPO POTATURA", "pixel", "valore");
		plot3.setColor(Color.red);
		plot3.addPoints(vetx, profile1, Plot.LINE);
		plot3.setColor(Color.blue);
		plot3.addPoints(xprofile3, yprofile3, Plot.X);
		plot3.addLegend("potatura "+title);
		plot3.setLimitsToFit(true);
		plot3.show();
		MyLog.waitHere("DOPO POTATURA");

		CurveFitter cf1 = new CurveFitter(xprofile3, yprofile3);
		cf1.doFit(CurveFitter.POLY2);
		double[] param1 = cf1.getParams();
		double[] vetfit = ProfileUtils.fitResult3(vetx, param1);
		double[] correctedProfile = new double[vetx.length];
		// double minC4 = ArrayUtils.vetMin(correctedM4);
		for (int i1 = 0; i1 < vetx.length; i1++) {
			correctedProfile[i1] = (profile1[i1] - vetfit[i1]);
		}
		double minM4 = ArrayUtils.vetMin(profile1);
		double minF4 = ArrayUtils.vetMin(correctedProfile);
		double kappa = minM4 - minF4;
		for (int i1 = 0; i1 < vetx.length; i1++) {
			correctedProfile[i1] = correctedProfile[i1] + kappa;
		}

		Plot plot6 = new Plot("SEGNALE CORRETTO", "pixel", "valore");
		plot6.setColor(Color.red);
		plot6.addPoints(vetx, profile1, Plot.LINE);
		plot6.setColor(Color.green);
		plot6.addPoints(vetx, correctedProfile, Plot.LINE);
		plot6.addLegend("originale "+title+"\ncorretto "+title);
		plot6.setLimitsToFit(true);
		plot6.show();
		MyLog.waitHere("SEGNALE CORRETTO");

	}

	@Test
	public final void testPeakWidth() {

		double[] profile1 = readProfile(path2, number);
		double[] profile2 = ProfileUtils.squareSmooth3(profile1);
		double[] profile3 = ProfileUtils.derivata(profile2);	
		double[] profile3s = ProfileUtils.squareSmooth3(profile3);
		double[] profile4 = ProfileUtils.derivata(profile3s);
		double[] profile4s = ProfileUtils.squareSmooth3(profile4);
		double[] profile5 = ProfileUtils.derivata(profile4s);
		double[] vetx = new double[profile1.length];
		for (int i1 = 0; i1 < profile1.length; i1++) {
			vetx[i1] = i1;
		}
		Plot plot2 = new Plot("SEGNALE ORIGINALE "+title, "pixel", "valore");
		plot2.setColor(Color.red);
		plot2.addPoints(vetx, profile1, Plot.LINE);
		plot2.addLegend("originale "+title);
		plot2.setLimitsToFit(true);
		plot2.show();
		
		Plot plot3 = new Plot("SEGNALE DERIVATA1 + title", "pixel", "valore");
		plot3.setColor(Color.blue);
		plot3.addPoints(vetx, profile3, Plot.LINE);
		plot3.addLegend("derivata1 "+title);
		plot3.setLimitsToFit(true);
		plot3.show();
		
		Plot plot4 = new Plot("SEGNALE DERIVATA2 + title", "pixel", "valore");
		plot4.setColor(Color.blue);
		plot4.addPoints(vetx, profile4, Plot.LINE);
		plot4.addLegend("derivata2 "+title);
		plot4.setLimitsToFit(true);
		plot4.show();

		Plot plot5 = new Plot("SEGNALE DERIVATA3 + title", "pixel", "valore");
		plot5.setColor(Color.blue);
		plot5.addPoints(vetx, profile5, Plot.LINE);
		plot5.addLegend("derivata3 "+title);
		plot5.setLimitsToFit(true);
		plot5.show();
		MyLog.waitHere("SEGNALE DERIVATA3 "+title);


	}

	

	
}
