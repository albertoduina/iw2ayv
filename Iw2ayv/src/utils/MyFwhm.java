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
	 * 
	 * @param profi1
	 * @param dimPixel
	 * @param invert
	 * @param step
	 * @return
	 */
	public static double[] analyzeProfile(double[] profi1, double dimPixel,
			String title, boolean invert, boolean step) {

		double[] profi2;
		if (invert) {
			profi2 = invertProfile(profi1);
		} else {
			profi2 = profi1;
		}
		double[] profi3 = smooth3(profi2, 10);
		int[] vetHalfPoint = halfPointSearch(profi2);
		boolean printPlot = step;
		double fwhm = calcFwhm(vetHalfPoint, profi2, dimPixel, title, printPlot);
		double peak = peakPosition(profi2);
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
	public static double calcFwhm(int[] vetUpDwPoints, double[] profile,
			double dimPixel, String title, boolean printPlot) {

		double[] a = Tools.getMinMax(profile);
		double min = a[0];
		double max = a[1];

		// interpolazione lineare sinistra
		double px0 = vetUpDwPoints[0];
		double px1 = vetUpDwPoints[1];
		double py0 = profile[vetUpDwPoints[0]];
		double py1 = profile[vetUpDwPoints[1]];
		double py2 = (max - min) / 2.0 + min;

		double sx = xLinearInterpolation(px0, py0, px1, py1, py2);

		// IJ.log("punto interpolato a sx= " + sx);
		// px2 = px0 + (px1 - px0) / (py1 - py0) * (py2 - py0);

		// interpolazione lineare destra
		px0 = vetUpDwPoints[2];
		px1 = vetUpDwPoints[3];
		py0 = profile[vetUpDwPoints[2]];
		py1 = profile[vetUpDwPoints[3]];
		py2 = (max - min) / 2.0 + min;
		double dx = xLinearInterpolation(px0, py0, px1, py1, py2);
		// IJ.log("punto interpolato a dx= " + dx);
		//
		// qui posso mettere il plot con magari uno switch per escluderlo
		//

		// px2 = px0 + (px1 - px0) / (py1 - py0) * (py2 - py0);
		double fwhm = (dx - sx) * dimPixel; // NOTA BENE è in pixels

		if (printPlot)
			createPlot(profile, title, vetUpDwPoints, fwhm);

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
	public static double peakPosition(double[] profile) {

		double[] a = Tools.getMinMax(profile);
		double max = a[1];
		double peakX = Double.NaN;
		for (int i1 = 0; i1 < profile.length; i1++) {
			if (profile[i1] == max)
				peakX = i1;
		}
		return (peakX);
	}

	public static void minimalPlot(double[] profi1, int[] upDwPoints,
			String title, String text1, String text2, double value,
			boolean verbose) {

		double[] xcoord1 = new double[profi1.length];
		for (int i1 = 0; i1 < profi1.length; i1++) {
			xcoord1[i1] = (double) i1;
		}
		double[] a = Tools.getMinMax(profi1);
		double min = a[0];
		double max = a[1];
		Plot plot1 = new Plot(title, text1, text2, xcoord1, profi1);
		plot1.setColor(Color.BLUE);
		plot1.show();
		double half = (max - min) / 2 + min;
		double[] xHalf = { 0, profi1.length };
		double[] yHalf = { half, half };
		plot1.setColor(Color.GREEN);
		plot1.addPoints(xHalf, yHalf, PlotWindow.LINE);

		double[] xUpDwPoints = { upDwPoints[0], upDwPoints[1], upDwPoints[2],
				upDwPoints[3] };
		double[] yUpDwPoints = { profi1[upDwPoints[0]], profi1[upDwPoints[1]],
				profi1[upDwPoints[2]], profi1[upDwPoints[3]] };
		plot1.setColor(Color.RED);
		plot1.addPoints(xUpDwPoints, yUpDwPoints, PlotWindow.CIRCLE);
		plot1.changeFont(new Font("Arial", Font.PLAIN, 22));
		double xlabelPosition = 0.05;
		double ylabelPosition = 0.15;
		plot1.addLabel(xlabelPosition, ylabelPosition,
				"fwhm= " + IJ.d2s(value, 2) + " mm");
	}

	/**
	 * ricerca dei punti a metà altezza
	 * 
	 * @param profile1
	 *            Vettore con il profilo da analizzare
	 * @return isd[0] punto sotto half a sx, isd[1] punto sopra half a sx,
	 * @return isd[2] punto sotto half a dx, isd[3] punto sopra half a dx
	 */
	public static int[] halfPointSearch(double profile1[]) {

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
		int[] vetHalfPoint = { downSx, upSx, downDx, upDx };
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
	 * Esegue l'interpolazione lineare tra due punti dati, conoscendo la x
	 * 
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param x2
	 * @return
	 */
	public static double yLinearInterpolation(double x0, double y0, double x1,
			double y1, double x2) {

		double y2 = y1 - ((y1 - y0) * (x1 - x2) / (x1 - x0));

		return y2;
	}

	/**
	 * Esegue l'interpolazione lineare tra due punti dati, conoscendo la y
	 * 
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @param x2
	 * @return
	 */
	public static double xLinearInterpolation(double x0, double y0, double x1,
			double y1, double y2) {

		double x2 = x1 - ((x1 - x0) * (y1 - y2) / (y1 - y0));

		return x2;
	}

	/***
	 * 
	 * @param x0
	 * @param y0
	 * @param x1
	 * @param y1
	 * @return
	 */
	public static double lengthCalculation(double x0, double y0, double x1,
			double y1) {

		double len = Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0));

		return len;
	}

	public static void createPlot(double profile1[], String title,
			int[] vetUpDwPoints, double fwhm2) {

		double[] a = Tools.getMinMax(profile1);
		double min = a[0];
		double max = a[1];
		double half = (max - min) / 2.0 + min;

		double[] xVectPointsX = new double[2];
		double[] yVectPointsX = new double[2];

		double[] xVectPointsO = new double[2];
		double[] yVectPointsO = new double[2];

		double[] xVetLineHalf = new double[2];
		double[] yVetLineHalf = new double[2];
		int len1 = profile1.length;
		double[] xcoord1 = new double[len1];
		for (int j = 0; j < len1; j++)
			xcoord1[j] = j;
		Plot plot = new Plot("Profilo penetrazione__" + title, "pixel",
				"valore", xcoord1, profile1);
		plot.setLimits(0, len1, min, max);
		plot.setLineWidth(1);
		plot.setColor(Color.blue);
		xVectPointsX[0] = (double) vetUpDwPoints[0];
		xVectPointsX[1] = (double) vetUpDwPoints[2];
		yVectPointsX[0] = profile1[vetUpDwPoints[0]];
		yVectPointsX[1] = profile1[vetUpDwPoints[2]];
		plot.addPoints(xVectPointsX, yVectPointsX, PlotWindow.BOX);

		xVectPointsO[0] = (double) vetUpDwPoints[1];
		xVectPointsO[1] = (double) vetUpDwPoints[3];
		yVectPointsO[0] = profile1[vetUpDwPoints[1]];
		yVectPointsO[1] = profile1[vetUpDwPoints[3]];
		plot.addPoints(xVectPointsO, yVectPointsO, PlotWindow.CIRCLE);

		plot.changeFont(new Font("Helvetica", Font.PLAIN, 12));

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
		double xlabel = 0.02;
		double ylabel = 0.58;

		plot.addLabel(xlabel, ylabel, title);
		plot.addLabel(xlabel, ylabel + 0.05,
				"peak / 2       =     " + IJ.d2s(max / 2, 2));
		plot.addLabel(xlabel, ylabel + 0.10, "dw_sx     " + vetUpDwPoints[0]
				+ " =   " + IJ.d2s(profile1[vetUpDwPoints[0]], 2));
		plot.addLabel(xlabel, ylabel + 0.15, "dw_dx    " + vetUpDwPoints[2]
				+ " =   " + IJ.d2s(profile1[vetUpDwPoints[2]], 2));
		plot.addLabel(xlabel, ylabel + 0.20, "up_sx     " + vetUpDwPoints[1]
				+ " =   " + IJ.d2s(profile1[vetUpDwPoints[1]], 2));
		plot.addLabel(xlabel, ylabel + 0.25, "up_dx   " + vetUpDwPoints[3]
				+ " =   " + IJ.d2s(profile1[vetUpDwPoints[3]], 2));
		plot.addLabel(xlabel, ylabel + 0.30,
				"interp_sx      =  " + IJ.d2s(sx, 2));
		plot.addLabel(xlabel, ylabel + 0.35,
				"interp_dx      =  " + IJ.d2s(dx, 2));
		plot.addLabel(xlabel, ylabel + 0.40,
				"fwhm            =  " + IJ.d2s(fwhm2, 2));
		plot.setColor(Color.green);
		xVetLineHalf[0] = 0;
		xVetLineHalf[1] = len1;
		yVetLineHalf[0] = half;
		yVetLineHalf[1] = half;
		plot.addPoints(xVetLineHalf, yVetLineHalf, PlotWindow.LINE);
		plot.setColor(Color.red);
		plot.show();

		plot.draw();
	}

}
