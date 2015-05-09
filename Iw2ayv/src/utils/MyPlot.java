package utils;

import ij.IJ;
import ij.gui.Plot;
import ij.gui.PlotWindow;
import ij.util.Tools;

import java.awt.Color;
import java.util.ArrayList;

public class MyPlot {

	public static Plot basePlot(double[] profiley, String title, Color color) {
		double[] profilex = new double[profiley.length];
		for (int j = 0; j < profiley.length; j++)
			profilex[j] = j;
		double[] a = Tools.getMinMax(profilex);
		double[] b = Tools.getMinMax(profiley);

		Plot plot = new Plot(title, "pixel", "valore", profilex, profiley);
		plot.setLimits(a[0], a[1], b[0], b[1] * 1.1);

		plot.setColor(color);
		return plot;
	}

	public static Plot basePlot(double[] profilex, double[] profiley,
			String title, Color color) {
		double[] a = Tools.getMinMax(profilex);
		double[] b = Tools.getMinMax(profiley);

		Plot plot = new Plot(title, "pixel", "valore", profilex, profiley);
		plot.setLimits(a[0], a[1], b[0], b[1] * 1.1);
		plot.setColor(color);
		return plot;
	}

	public static Plot basePlot(double[][] profile, String title, Color color) {

		double[] profilex = new double[profile.length];
		double[] profiley = new double[profile.length];

		for (int i1 = 0; i1 < profile.length; i1++) {
			profilex[i1] = profile[i1][0];
			profiley[i1] = profile[i1][1];
		}

		double[] a = Tools.getMinMax(profilex);
		double[] b = Tools.getMinMax(profiley);

		Plot plot = new Plot(title, "pixel", "valore", profilex, profiley);
		plot.setLimits(a[0], a[1], b[0], b[1] * 1.1);
		plot.setColor(color);
		return plot;
	}

	/**
	 * Attenzione che in questa versione la struttura della matrice è
	 * completamente cambiata
	 * 
	 * @param profile
	 * @param title
	 * @param color
	 * @return
	 */
	public static Plot basePlot2(double[][] profile, String title, Color color) {

		double[] profilex = new double[profile[0].length];
		double[] profileZ = new double[profile[0].length];

		for (int i1 = 0; i1 < profile[0].length; i1++) {
			profilex[i1] = profile[0][i1];
			profileZ[i1] = profile[2][i1];
		}

		double[] a = Tools.getMinMax(profilex);
		double[] b = Tools.getMinMax(profileZ);

		Plot plot = new Plot(title, "pixel", "valore", profilex, profileZ);
		plot.setLimits(a[0], a[1], b[0], b[1] * 1.1);
		plot.setColor(color);
		return plot;
	}

	/**
	 * Attenzione che in questa versione la struttura della matrice è
	 * completamente cambiata
	 * 
	 * @param profile
	 * @param title
	 * @param color
	 * @return
	 */
	public static Plot basePlot2(double[][] profile, double[] vetXpoints,
			String title, Color color) {

		
		
		MyLog.logMatrix(profile, "profile");
		double[] profileX = new double[profile[0].length];
		double[] profileZ= new double[profile[0].length];

		for (int i1 = 0; i1 < profile[0].length; i1++) {
			profileX[i1] = profile[0][i1];
			profileZ[i1] = profile[2][i1];
		}

		double[] a = Tools.getMinMax(profileX);
		double[] b = Tools.getMinMax(profileZ);

		Plot plot = new Plot(title, "pixel", "valore", profileX, profileZ);
		plot.setLimits(a[0], a[1], b[0], b[1] * 1.1);
		plot.setColor(color);

		double[] vetYpoints = new double[vetXpoints.length];
		for (int i1 = 0; i1 < vetXpoints.length; i1++) {
			for (int i2 = 0; i2 < profileX.length; i2++) {
				
//				public static boolean compareDoublesWithTolerance(double aa, double bb,
//						int digits) {

				if (UtilAyv.compareDoublesWithTolerance(vetXpoints[i1], profileX[i2], 1)){	
					vetYpoints[i1]=profileZ[i2];
					}
				}
		}

		MyLog.logVector(vetXpoints, "vetXpoints");
		MyLog.logVector(vetYpoints, "vetYpoints");
		MyLog.waitHere();

		plot.addPoints(vetXpoints, vetYpoints, PlotWindow.CIRCLE);

		return plot;
	}

}
