package utils;

import ij.IJ;
import ij.gui.Plot;
import ij.gui.PlotWindow;
import ij.util.Tools;

import java.awt.Color;
import java.awt.Font;

public class MyFwhm {

	
	/***
	 * Analisi del profilo, per calcolare l'FWHM
	 * @param profi1
	 * @param dimPixel
	 * @param invert
	 * @param step
	 * @return
	 */
	public static double[] analyzeProfile(double[] profi1, double dimPixel,
			boolean invert, boolean step) {

		double[] profi2;
		if (invert) {
			profi2 = invertProfile(profi1);
		} else {
			profi2 = profi1;
		}
		double[] profi3 = smooth3(profi2, 10);
		int[] vetHalfPoint = halfPointSearch(profi3);
		double fwhm = calcFwhm(vetHalfPoint, profi3) * dimPixel;
		double peak = peakPosition(profi3);
		double[] outFwhm = new double[2];
		outFwhm[0] = fwhm;
		outFwhm[1] = peak;
		return (outFwhm);
	}

	/***
	 * Calcolo valore FWHM
	 * 
	 * @param vetUpDwPoints
	 *            vettore con le coordinate dei pixel sopra e sotto la metà, a
	 *            dx e sx
	 * @param profile
	 *            profilo di cui calcolare l'FWHM
	 * @return FWHM calcolata in pixels
	 */
	private static double calcFwhm(int[] vetUpDwPoints, double[] profile) {

		double[] a = Tools.getMinMax(profile);
		double min = a[0];
		double max = a[1];

		// interpolazione lineare sinistra
		double px0 = vetUpDwPoints[0];
		double px1 = vetUpDwPoints[1];
		double py0 = profile[vetUpDwPoints[0]];
		double py1 = profile[vetUpDwPoints[1]];
		double py2 = (max - min) / 2.0 + min;

		double sx = linearInterpolation(px0, py0, px1, py1, py2);
		// px2 = px0 + (px1 - px0) / (py1 - py0) * (py2 - py0);

		// interpolazione lineare destra
		px0 = vetUpDwPoints[2];
		px1 = vetUpDwPoints[3];
		py0 = profile[vetUpDwPoints[2]];
		py1 = profile[vetUpDwPoints[3]];
		py2 = (max - min) / 2.0 + min;
		double dx = linearInterpolation(px0, py0, px1, py1, py2);

		// px2 = px0 + (px1 - px0) / (py1 - py0) * (py2 - py0);
		double fwhm = dx - sx; // NOTA BENE è in pixels
		return (fwhm);
	}

	/***
	 * Posizione del singolo picco (se si vogliono trovare tutti i picchi c'è
	 * PeakDet (p10rmn_)
	 * 
	 * @param profile
	 *            profilo
	 * @return posizione x del picco (in pixel)
	 */
	private static double peakPosition(double[] profile) {

		double[] a = Tools.getMinMax(profile);
		double min = a[0];
		double peakX = Double.NaN;
		for (int i1 = 0; i1 < profile.length; i1++) {
			if (profile[i1] == min)
				peakX = i1;
		}
		return (peakX);
	}

	/**
	 * Mostra a video un profilo con linea a metà picco
	 * 
	 * @param profile1
	 *            Vettore con il profilo da analizzare
	 * @param bslab
	 *            Flag slab che qui mettiamo sempre true
	 */

	private static void createPlot(double profile1[], double dimPixel,
			boolean bslab, boolean bLabelSx, boolean verbose) {

		int[] vetUpDwPoints = halfPointSearch(profile1);
		double[] a = Tools.getMinMax(profile1);
		double min = a[0];
		double max = a[1];
		double xmax = 0;
		double half = (max - min) / 2.0 + min;

		double[] xVectPointsM = new double[1];
		double[] yVectPointsM = new double[1];
		yVectPointsM[0] = max;

		double[] xVectPointsX = new double[2];
		double[] yVectPointsX = new double[2];

		double[] xVectPointsO = new double[2];
		double[] yVectPointsO = new double[2];

		double[] xVetLineHalf = new double[2];
		double[] yVetLineHalf = new double[2];
		int len1 = profile1.length;
		double[] xcoord1 = new double[len1];
		for (int j1 = 0; j1 < len1; j1++) {
			xcoord1[j1] = j1;
			if (profile1[j1] == max) {
				xmax = (double) j1;
			}
		}
		xVectPointsM[0] = xmax;

		// PlotWindow plot = new PlotWindow("Plot profilo penetrazione",
		// "pixel",
		// "valore", xcoord1, profile1);
		xVectPointsX[0] = (double) vetUpDwPoints[0];
		xVectPointsX[1] = (double) vetUpDwPoints[2];
		yVectPointsX[0] = profile1[vetUpDwPoints[0]];
		yVectPointsX[1] = profile1[vetUpDwPoints[2]];
		xVectPointsO[0] = (double) vetUpDwPoints[1];
		xVectPointsO[1] = (double) vetUpDwPoints[3];
		yVectPointsO[0] = profile1[vetUpDwPoints[1]];
		yVectPointsO[1] = profile1[vetUpDwPoints[3]];

		// interpolazione lineare sinistra
		double px0 = vetUpDwPoints[0];
		double px1 = vetUpDwPoints[1];
		double px2 = 0;
		double py0 = profile1[vetUpDwPoints[0]];
		double py1 = profile1[vetUpDwPoints[1]];
		double py2 = 0;
		py2 = half;
		px2 = px0 + (px1 - px0) / (py1 - py0) * (py2 - py0);
		double sx = px2;
		// interpolazione lineare destra
		px0 = vetUpDwPoints[2];
		px1 = vetUpDwPoints[3];
		py0 = profile1[vetUpDwPoints[2]];
		py1 = profile1[vetUpDwPoints[3]];
		py2 = half;
		px2 = px0 + (px1 - px0) / (py1 - py0) * (py2 - py0);
		double dx = px2;
		double fwhm = (dx - sx) * dimPixel;
		double labelPosition = 0;

		Plot plot = new Plot("P R O F I L O", "pixel", "valore", xcoord1,
				profile1);
		plot.setLimits(0, len1, min, max * 1.05);
		plot.setLineWidth(1);
		plot.setColor(Color.RED);
		plot.addPoints(xVectPointsM, yVectPointsM, PlotWindow.TRIANGLE);

		plot.setColor(Color.RED);
		plot.addPoints(xVectPointsX, yVectPointsX, PlotWindow.CIRCLE);
		plot.addPoints(xVectPointsO, yVectPointsO, PlotWindow.CIRCLE);
		plot.changeFont(new Font("Helvetica", Font.PLAIN, 10));

		if (bLabelSx)
			labelPosition = 0.10;
		else
			labelPosition = 0.60;

		if (verbose) {
			plot.addLabel(labelPosition, 0.45,
					"peak / 2=   " + IJ.d2s(max / 2, 2));
			plot.addLabel(labelPosition, 0.50, "down sx " + vetUpDwPoints[0]
					+ "  =   " + IJ.d2s(profile1[vetUpDwPoints[0]], 2));
			plot.addLabel(labelPosition, 0.55, "down dx " + vetUpDwPoints[2]
					+ "  =   " + IJ.d2s(profile1[vetUpDwPoints[2]], 2));
			plot.addLabel(labelPosition, 0.60, "up      sx " + vetUpDwPoints[1]
					+ "  =   " + IJ.d2s(profile1[vetUpDwPoints[1]], 2));
			plot.addLabel(labelPosition, 0.65, "up      dx " + vetUpDwPoints[3]
					+ "  =   " + IJ.d2s(profile1[vetUpDwPoints[3]], 2));
			plot.addLabel(labelPosition, 0.70,
					"sx interp       =  " + IJ.d2s(sx, 2));
			plot.addLabel(labelPosition, 0.75,
					"dx interp       =  " + IJ.d2s(dx, 2));
			plot.addLabel(labelPosition, 0.80,
					"fwhm            =  " + IJ.d2s(fwhm, 2));
		}
		plot.setColor(Color.PINK);
		xVetLineHalf[0] = 0;
		xVetLineHalf[1] = len1;
		yVetLineHalf[0] = half;
		yVetLineHalf[1] = half;
		plot.addPoints(xVetLineHalf, yVetLineHalf, PlotWindow.LINE);
		plot.setColor(Color.GREEN);
		PlotWindow pwin = plot.show();
		// float[] aaa = pwin.getXValues();
		// MyLog.logVector(aaa, "aaa");
		// float[] bbb = pwin.getYValues();
		// MyLog.logVector(bbb, "bbb");

		// plot.draw();
	} // createPlot2

	/**
	 * ricerca dei punti a metà altezza
	 * 
	 * @param profile1
	 *            Vettore con il profilo da analizzare
	 * @return isd[0] punto sotto half a sx, isd[1] punto sopra half a sx,
	 * @return isd[2] punto sotto half a dx, isd[3] punto sopra half a dx
	 */
	private static int[] halfPointSearch(double profile1[]) {

		double[] a = Tools.getMinMax(profile1);
		double min = a[0];
		double max = a[1];

		int upSx = 0;
		int downSx = 0;
		int upDx = 0;
		int downDx = 0;

		// calcolo metà altezza
		double half = (max - min) / 2 + min;
		// parto da sx e percorro il profilo in cerca del primo valore che
		// supera half
		int len1 = profile1.length;
		for (int i1 = 0; i1 < len1 - 1; i1++) {
			if (profile1[i1] > half) {
				downSx = i1;
				break;
			}
		}
		// torno indietro e cerco il primo valore sotto half
		for (int i1 = downSx; i1 > 0; i1--) {
			if (profile1[i1] < half) {
				upSx = i1;
				break;
			}
		}
		// parto da dx e percorro il profilo in cerca del primo valore che
		// supera half
		for (int i1 = len1 - 1; i1 > 0; i1--) {
			if (profile1[i1] > half) {
				downDx = i1;
				break;
			}
		}
		// torno indietro e cerco il primo valore sotto half
		for (int i1 = downDx; i1 < len1 - 1; i1++) {
			if (profile1[i1] < half) {
				upDx = i1;
				break;
			}
		}
		int[] vetHalfPoint = new int[4];
		vetHalfPoint[0] = downSx;
		vetHalfPoint[1] = upSx;
		vetHalfPoint[2] = downDx;
		vetHalfPoint[3] = upDx;
		return vetHalfPoint;
	}

	/***
	 * Effettua lo smooth su 3 pixels di un profilo
	 * 
	 * @param profile1
	 *            profilo
	 * @param loops
	 *            numerompassaggi
	 * @return profilo dopo smooth
	 */
	public static double[] smooth3(double[] profile1, int loops) {

		int len1 = profile1.length;
		for (int i1 = 0; i1 < loops; i1++) {
			for (int j1 = 1; j1 < len1 - 1; j1++)
				profile1[j1] = (profile1[j1 - 1] + profile1[j1] + profile1[j1 + 1]) / 3;
		}
		return profile1;

	}

	/***
	 * Inverte il profilo (utilizzato per i cunei)
	 * 
	 * @param profile1
	 * @return
	 */
	public static double[] invertProfile(double[] profile1) {

		double[] invert1 = new double[profile1.length];

		double[] a = Tools.getMinMax(profile1);
		double max = a[1];
		for (int i1 = 0; i1 < profile1.length; i1++) {
			invert1[i1] = max - profile1[i1];
		}
		return invert1;
	}

	/**
	 * Esegue l'interpolazione lineare tra due punti dati
	 * 
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param x2
	 * @return
	 */
	public static double linearInterpolation(double x0, double y0, double x1,
			double y1, double x2) {

		double y2 = y1 - ((y1 - y0) * (x1 - x2) / (x1 - x0));

		return y2;
	}

}
