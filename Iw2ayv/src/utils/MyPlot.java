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

}
