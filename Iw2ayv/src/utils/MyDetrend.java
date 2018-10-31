package utils;

import java.awt.Color;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.measure.CurveFitter;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;

public class MyDetrend {
	
	
	/**
	 * Effettua un fit POLY2 (2nd Degree Polynomial), utilizzando il fit di ImageJ
	 * ed avendo come input il vettore tempi ed il vettore dei pixel
	 * 
	 * @param vetTempi
	 *            vettore dei tempi
	 * @param vetPixels
	 *            vettore dei pixels
	 * @return vettore detrend
	 */
	public static double[] vetDetrend(double[] vetTempi, double[] vetPixels) {
		if (vetTempi == null) {
			MyLog.waitHere("vetTempi==null");
			MyLog.waitThere("chiamante= ");
			return null;
		}
		if (vetPixels == null) {
			MyLog.waitHere("vetPixels==null");
			MyLog.waitThere("chiamante= ");
			return null;
		}
		double[] vetOut = new double[vetTempi.length];
		// viene effettuato il fit POLYY2
		CurveFitter cf1 = fitCurve(vetTempi, vetPixels);
		// i parametri finali del fit vanno in param1
		double[] param1 = cf1.getParams();

		// in vetTrend viene ricostruita la curva data dai parametri del fit
		double[] vetTrend = fitResult(vetTempi, param1);
		// viene effettuato il DETREND e messo in vetOut
		for (int i1 = 0; i1 < vetTempi.length; i1++) {
			vetOut[i1] = vetPixels[i1] - vetTrend[i1];
		}
		return vetOut;
	}


	/**
	 * Fit di una curva poly2 utilizzando CurveFitter di ImageJ
	 * 
	 * @param vetX
	 *            vettore ascisse
	 * @param vetY
	 *            vettore ordinate
	 * @return parametri curve fitter dopo il fit
	 */
	public static CurveFitter fitCurve(double[] vetX, double[] vetY) {
		CurveFitter cf = new CurveFitter(vetX, vetY);
		cf.doFit(CurveFitter.POLY2);
		return cf;
	}

	/**
	 * Dati i parametri di una curva ed un vettore x, calcola i corrispondenti
	 * valori di y
	 * 
	 * @param vetX
	 *            vettore delle ascisse
	 * @param para
	 *            parametri della curva
	 * @return vettore delle ordinate
	 */
	public static double[] fitResult(double[] vetX, double[] para) {
		double[] out = new double[vetX.length];

		for (int i1 = 0; i1 < vetX.length; i1++) {
			double x = vetX[i1];
			double y = para[0] + para[1] * x + para[2] * x * x;
			out[i1] = y;
		}
		return out;
	}



}