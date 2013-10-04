package utils;

import ij.IJ;
import ij.gui.Plot;
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

}
