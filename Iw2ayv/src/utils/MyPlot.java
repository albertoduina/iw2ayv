package utils;

import java.awt.Color;

import ij.ImagePlus;
import ij.gui.Plot;
import ij.gui.PlotWindow;
import ij.util.Tools;

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

	public static Plot basePlot(double[] profilex, double[] profiley, String title, Color color) {
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
	 * Attenzione che in questa versione la struttura della matrice � completamente
	 * cambiata
	 * 
	 * @param profile
	 * @param title
	 * @param color
	 * @return
	 */
	public static Plot basePlot2(double[][] profile, String title, Color color, boolean vertical) {

		double[] profilex = new double[profile[0].length];
		double[] profileZ = new double[profile[0].length];

		for (int i1 = 0; i1 < profile[0].length; i1++) {
			if (vertical)
				profilex[i1] = profile[1][i1];
			else
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
	 * Attenzione che in questa versione la struttura della matrice � completamente
	 * cambiata
	 * 
	 * @param profile
	 * @param title
	 * @param color
	 * @return
	 */
	public static Plot basePlot2(double[][] profile, double[] vetXpoints, String title, Color color) {

		MyLog.logMatrix(profile, "profile");
		double[] profileX = new double[profile[0].length];
		double[] profileZ = new double[profile[0].length];

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

				if (UtilAyv.compareDoublesWithTolerance(vetXpoints[i1], profileX[i2], 1)) {
					vetYpoints[i1] = profileZ[i2];
				}
			}
		}

		MyLog.logVector(vetXpoints, "vetXpoints");
		MyLog.logVector(vetYpoints, "vetYpoints");
		MyLog.waitHere();

		plot.addPoints(vetXpoints, vetYpoints, PlotWindow.CIRCLE);

		return plot;
	}

	/**
	 * Plotta un profilo
	 * 
	 * @param profile1
	 * @param sTitolo
	 * @return
	 */
	public static Plot plotPoints(double[] xpoints, double[] ypoints, String sTitolo) {

		int len1 = xpoints.length;
		double[] xcoord = new double[len1];
		for (int i1 = 0; i1 < len1; i1++) {
			xcoord[i1] = (double) i1;
		}

		Plot plot = new Plot(sTitolo, "pixel", "valore");
		plot.setColor(Color.black);
		plot.addLabel(0.01, 0.99, sTitolo);

		plot.setColor(Color.red);
		plot.setLineWidth(2);
		plot.addPoints(xpoints, ypoints, Plot.CIRCLE);
		plot.show();

		return plot;

	}

	public static Plot plotPoints(double[][] points, String sTitolo) {

		int len1 = points.length;

		double[] xpoints = new double[len1];
		double[] ypoints = new double[len1];
		for (int i1 = 0; i1 < len1; i1++) {
			xpoints[i1] = points[i1][0];
			ypoints[i1] = points[i1][1];
		}

		Plot plot = new Plot(sTitolo, "pixel", "valore");
		plot.setColor(Color.black);
		plot.addLabel(0.01, 0.99, sTitolo);

		plot.setColor(Color.red);
		plot.setLineWidth(2);
		plot.addPoints(xpoints, ypoints, Plot.CIRCLE);
		plot.show();

		return plot;

	}

	/**
	 * Plotta un profilo
	 * 
	 * @param profile1
	 * @param sTitolo
	 * @return
	 */
	public static Plot plot1(double[] profile1, String sTitolo) {

		int len1 = profile1.length;
		double[] xcoord = new double[len1];
		for (int i1 = 0; i1 < len1; i1++) {
			xcoord[i1] = (double) i1;
		}

		Plot plot = new Plot(sTitolo, "pixel", "valore");
		plot.setColor(Color.black);
		plot.addLabel(0.01, 0.99, sTitolo);

		plot.setColor(Color.red);
		plot.add("line", xcoord, profile1);

		return plot;

	}

	public static Plot plot1(double[][] profile1, String sTitolo) {

		Plot plot = new Plot(sTitolo, "pixel", "valore");
		plot.setColor(Color.black);
		plot.addLabel(0.01, 0.99, sTitolo);

		plot.setColor(Color.red);
		plot.add("line", ProfileUtils.decodeX(profile1), ProfileUtils.decodeY(profile1));

		return plot;

	}

	/**
	 * Plotta due profili
	 * 
	 * @param profile1
	 * @param profile2
	 * @param bslab
	 * @param bLabelSx
	 * @param sTitolo
	 * @param bFw
	 * @param sTeorico
	 * @param dimPixel
	 * @return
	 */

	public static Plot plot2(double[] profile1, double[] profile2, String sTitolo) {

		int len1 = profile1.length;
		double[] xcoord = new double[len1];
		for (int i1 = 0; i1 < len1; i1++) {
			xcoord[i1] = (double) i1;
		}

		Plot plot = new Plot(sTitolo, "pixel", "valore");
		plot.setColor(Color.black);
		plot.addLabel(0.01, 0.99, sTitolo);

		plot.setColor(Color.red);
		plot.add("line", xcoord, profile1);
		plot.setColor(Color.blue);
		plot.add("line", xcoord, profile2);
		plot.show();
		return plot;
	}

	public static Plot plot2(double[][] profile1, double[][] profile2, String sTitolo) {

		Plot plot = new Plot(sTitolo, "pixel", "valore");
		plot.setColor(Color.black);
		plot.addLabel(0.01, 0.99, sTitolo);

		plot.setColor(Color.red);
		plot.add("line", ProfileUtils.decodeX(profile1), ProfileUtils.decodeY(profile1));
		plot.setColor(Color.blue);
		plot.add("line", ProfileUtils.decodeX(profile2), ProfileUtils.decodeY(profile2));
		plot.show();
		return plot;
	}

	/**
	 * Plotta un profilo con punti sovrapposti
	 * 
	 * @param profile1
	 * @param xpoints2
	 * @param ypoints2
	 * @param sTitolo
	 * @return
	 */
	public static Plot plot1points(double[] profile1, double[] xpoints2, double[] ypoints2, String sTitolo) {

		int len1 = profile1.length;
		double[] xcoord = new double[len1];
		for (int i1 = 0; i1 < len1; i1++) {
			xcoord[i1] = (double) i1;
		}

		Plot plot = new Plot(sTitolo, "pixel", "valore");
		plot.setColor(Color.black);
		plot.addLabel(0.01, 0.99, sTitolo);

		plot.setColor(Color.red);
		plot.add("line", xcoord, profile1);
		plot.setLineWidth(2);
		plot.setColor(Color.blue);
		plot.addPoints(xpoints2, ypoints2, Plot.CIRCLE);
		plot.setLineWidth(1);

		plot.show();
		return plot;
	}
	
	public static Plot plot1points(double[][] profile1, double[] xpoints2, double[] ypoints2, String sTitolo) {

	
		Plot plot = new Plot(sTitolo, "pixel", "valore");
		plot.setColor(Color.black);
		plot.addLabel(0.01, 0.99, sTitolo);

		plot.setColor(Color.red);
		plot.add("line", ProfileUtils.decodeX(profile1), ProfileUtils.decodeY(profile1));
		plot.setLineWidth(2);
		plot.setColor(Color.blue);
		plot.addPoints(xpoints2, ypoints2, Plot.CIRCLE);
		plot.setLineWidth(1);

		plot.show();
		return plot;
	}

	/**
	 * Plotta due profili con punti sovrapposti
	 * 
	 * @param profile1
	 * @param profile2
	 * @param xpoints2
	 * @param ypoints2
	 * @param sTitolo
	 * @return
	 */
	public static Plot plot2points(double[] profile1, double[] profile2, double[] xpoints2, double[] ypoints2,
			String sTitolo) {

		int len1 = profile1.length;
		double[] xcoord = new double[len1];
		for (int i1 = 0; i1 < len1; i1++) {
			xcoord[i1] = (double) i1;
		}

		Plot plot = new Plot(sTitolo, "pixel", "valore");
		plot.setColor(Color.black);
		plot.addLabel(0.01, 0.99, sTitolo);

		plot.setColor(Color.red);
		plot.add("line", xcoord, profile1);
		plot.setColor(Color.blue);
		plot.add("line", xcoord, profile2);

		plot.setLineWidth(2);
		plot.setColor(Color.blue);
		plot.addPoints(xpoints2, ypoints2, Plot.CIRCLE);
		plot.setLineWidth(1);

		plot.show();
		return plot;
	}


}
