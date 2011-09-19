package utils;

import ij.IJ;
import ij.gui.Plot;

import java.awt.Color;
import java.util.ArrayList;

public class MyPlot {

	public static Plot basePlot(double[] profile, String title, Color color) {
		int len1 = profile.length;
		double[] xcoord1 = new double[len1];
		for (int j = 0; j < len1; j++)
			xcoord1[j] = j;
		Plot plot = new Plot(title, "pixel", "valore", xcoord1, profile);
		plot.setColor(color);
		return plot;
	}

}
