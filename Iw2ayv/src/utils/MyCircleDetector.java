package utils;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Line;
import ij.gui.NewImage;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.gui.Plot;
import ij.gui.PlotWindow;
import ij.gui.PointRoi;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.gui.WaitForUserDialog;
import ij.measure.Measurements;
import ij.process.AutoThresholder;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;
import ij.process.ShortProcessor;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class MyCircleDetector {

	private static boolean previous = false;
	private static boolean init1 = true;
	@SuppressWarnings("unused")
	private static boolean pulse = false; // lasciare, serve anche se segnalato
											// inutilizzato

	/**
	 * Ricerca della posizione della ROI per il calcolo dell'uniformit�
	 * 
	 * @param imp11
	 *            immagine di input
	 * @param profond
	 *            profondit� ROI
	 * @param info1
	 *            messaggio esplicativo
	 * @param autoCalled
	 *            flag true se chiamato in automatico
	 * @param step
	 *            flag true se funzionamento passo - passo
	 * @param verbose
	 *            flag true se funzionamento verbose
	 * @param test
	 *            flag true se in test
	 * @param fast
	 *            flag true se modo batch
	 * @return vettore con dati ROI
	 */
	public static int[] circleDetector(ImagePlus imp11, boolean step) {
		//
		// ================================================================================
		// Inizio calcoli geometrici
		// ================================================================================
		//

		Overlay over14 = new Overlay();

		// int width = imp11.getWidth();
		// int height = imp11.getHeight();

		ImagePlus imp12 = imp11.duplicate();
		if (step) {
			UtilAyv.showImageMaximized(imp12);
		}

		//
		// -------------------------------------------------
		// Determinazione del cerchio
		// -------------------------------------------------
		//

		ImageProcessor ip12 = imp12.getProcessor();
		ip12.findEdges();
		imp12.updateAndDraw();

		ImagePlus imp14 = applyThreshold(imp12);
		ByteProcessor ip14 = (ByteProcessor) imp14.getProcessor();
		byte[] pixels14 = (byte[]) ip14.getPixels();

		ImagePlus imp16 = circleOutline(imp14);

		if (step) {
			UtilAyv.showImageMaximized(imp16);
			MyLog.waitHere("maschera pixel bordo stretto");
		}

		imp16.setOverlay(over14);
		over14.clear();
		ImageProcessor ip16 = imp16.getProcessor();
		byte[] pixels16 = (byte[]) ip16.getPixels();

		// acquisisco le coordinate dei pixels a 255

		List<Integer> vetX14 = new ArrayList<Integer>();
		List<Integer> vetY14 = new ArrayList<Integer>();

		for (int i1 = 0; i1 < ip14.getWidth(); i1++) {
			for (int i2 = 0; i2 < ip14.getHeight(); i2++) {
				int aux1 = i1 * ip14.getWidth() + i2;
				if (pixels16[aux1] != 0) {
					vetX14.add(i2);
					vetY14.add(i1);
				}
			}
		}

		int[] xPoints = ArrayUtils.arrayListToArrayInt(vetX14);
		int[] yPoints = ArrayUtils.arrayListToArrayInt(vetY14);

		imp14.setRoi(new PointRoi(xPoints, yPoints, xPoints.length));

		// disegno i punti
		over14.addElement(imp14.getRoi());
		imp14.updateAndDraw();
		// MyLog.waitHere();

		fitCircle(imp14);
		// if (step) {

		// disegno il cerchio
		over14.addElement(imp14.getRoi());
		over14.setStrokeColor(Color.red);

		imp14.setColor(Color.white);
		imp14.draw();
		imp14.updateAndDraw();

		Rectangle boundingRectangle14 = imp14.getProcessor().getRoi();
		int diamRoi = (int) boundingRectangle14.width;
		int xRoi = boundingRectangle14.x
				+ ((boundingRectangle14.width - diamRoi) / 2);
		int yRoi = boundingRectangle14.y
				+ ((boundingRectangle14.height - diamRoi) / 2);

		xRoi += 1;
		yRoi += 1;
		int[] out2 = new int[3];
		out2[0] = xRoi;
		out2[1] = yRoi;
		out2[2] = diamRoi;
		return out2;
	}

	/**
	 * Applica il threshold automatico di ImageJ all'immagine
	 * 
	 * @param imp1
	 * @return
	 */
	public static ImagePlus applyThreshold(ImagePlus imp1) {
		int slices = 1;
		ImageProcessor ip1 = imp1.getProcessor();
		short[] pixels1 = (short[]) ip1.getPixels();
		int threshold = ip1.getAutoThreshold();
		ImagePlus imp2 = NewImage.createByteImage("Thresholded",
				imp1.getWidth(), imp1.getHeight(), slices, NewImage.FILL_BLACK);
		ByteProcessor ip2 = (ByteProcessor) imp2.getProcessor();
		byte[] pixels2 = (byte[]) ip2.getPixels();
		for (int i1 = 0; i1 < pixels2.length; i1++) {
			if (pixels1[i1] >= threshold) {
				pixels2[i1] = (byte) 100;
			} else {
				pixels2[i1] = (byte) 0;
			}
		}
		ip2.resetMinAndMax();
		return imp2;
	}

	/***
	 * Riceve una ImagePlus, con impostata una Line, restituisce le coordinate
	 * dei 2 picchi
	 * 
	 * @param imp1
	 * @param dimPixel
	 * @return
	 */
	public static int[] profileAnalyzer(ImagePlus imp1, String title,
			boolean showProfiles) {
		Roi roi11 = imp1.getRoi();
		double[] profi1 = ((Line) roi11).getPixels();
		double[] profi2y = profi1;
		double[] profi2x = new double[profi2y.length];
		double xval = 0.;
		for (int i1 = 0; i1 < profi2y.length; i1++) {
			profi2x[i1] = xval;
			xval += 1;
		}
		int foundSx = 99999;
		int len = profi2y.length;
		for (int i1 = 0; i1 < len; i1++) {
			if (profi2y[i1] == 255) {
				foundSx = i1;
				break;
			}
		}
		int foundDx = 99999;
		for (int i1 = (len - 1); i1 >= 0; i1--) {
			if (profi2y[i1] == 255) {
				foundDx = i1;
				break;
			}
		}
		int[] peaks = new int[2];
		peaks[0] = foundSx;
		peaks[1] = foundDx;
		return peaks;
	}

	/***
	 * Riceve una ImagePlus con impostata una Line, restituisce le coordinate
	 * dei 2 picchi
	 * 
	 * @param imp1
	 * @param dimPixel
	 * @return
	 */
	public static double[][] profileAnalyzer2(ImagePlus imp1, String title,
			boolean showProfiles) {
		Roi roi11 = imp1.getRoi();
		double[] profi1 = ((Line) roi11).getPixels();

		double[] profi2y = smooth3(profi1, 1);

		double[] profi2x = new double[profi2y.length];
		double xval = 0.;
		for (int i1 = 0; i1 < profi2y.length; i1++) {
			profi2x[i1] = xval;
			xval += 1;
		}

		double[][] profi3 = new double[profi2y.length][2];
		for (int i1 = 0; i1 < profi2y.length; i1++) {
			profi3[i1][0] = profi2x[i1];
			profi3[i1][1] = profi2y[i1];
		}
		ArrayList<ArrayList<Double>> matOut = peakDet(profi3, 100.);
		double[][] peaks1 = new InputOutput()
				.fromArrayListToDoubleTable(matOut);

		double[] xPoints = new double[peaks1[2].length];
		double[] yPoints = new double[peaks1[2].length];
		for (int i1 = 0; i1 < peaks1[2].length; i1++) {
			xPoints[i1] = peaks1[2][i1];
			yPoints[i1] = peaks1[3][i1];

		}

		if (showProfiles) {
			Plot plot2 = MyPlot.basePlot(profi2x, profi2y, title, Color.GREEN);
			plot2.draw();
			plot2.setColor(Color.red);
			plot2.addPoints(xPoints, yPoints, PlotWindow.CIRCLE);
			plot2.show();
			new WaitForUserDialog("002 premere  OK").show();
		}

		return peaks1;
	}

	/***
	 * Questo � il fitCircle preso da ImageJ (ij.plugins.Selection.java, con
	 * sostituito imp.setRoi a IJ.makeOval
	 * 
	 * if selection is closed shape, create a circle with the same area and
	 * centroid, otherwise use<br>
	 * the Pratt method to fit a circle to the points that define the line or
	 * multi-point selection.<br>
	 * Reference: Pratt V., Direct least-squares fitting of algebraic surfaces",
	 * Computer Graphics, Vol. 21, pages 145-152 (1987).<br>
	 * Original code: Nikolai Chernov's MATLAB script for Newton-based Pratt
	 * fit.<br>
	 * (http://www.math.uab.edu/~chernov/cl/MATLABcircle.html)<br>
	 * Java version:
	 * https://github.com/mdoube/BoneJ/blob/master/src/org/doube/geometry
	 * /FitCircle.java<br>
	 * 
	 * authors: Nikolai Chernov, Michael Doube, Ved Sharma
	 */
	public static void fitCircle(ImagePlus imp) {
		Roi roi = imp.getRoi();

		if (roi == null) {
			IJ.error("Fit Circle", "Selection required");
			return;
		}

		if (roi.isArea()) { // create circle with the same area and centroid
			ImageProcessor ip = imp.getProcessor();
			ip.setRoi(roi);
			ImageStatistics stats = ImageStatistics.getStatistics(ip,
					Measurements.AREA + Measurements.CENTROID, null);
			double r = Math.sqrt(stats.pixelCount / Math.PI);
			imp.killRoi();
			int d = (int) Math.round(2.0 * r);
			imp.setRoi(new OvalRoi((int) Math.round(stats.xCentroid - r),
					(int) Math.round(stats.yCentroid - r), d, d));

			// IJ.makeOval((int) Math.round(stats.xCentroid - r),
			// (int) Math.round(stats.yCentroid - r), d, d);
			return;
		}

		Polygon poly = roi.getPolygon();
		int n = poly.npoints;
		int[] x = poly.xpoints;
		int[] y = poly.ypoints;
		if (n < 3) {
			IJ.error("Fit Circle",
					"At least 3 points are required to fit a circle.");
			return;
		}

		// calculate point centroid
		double sumx = 0, sumy = 0;
		for (int i = 0; i < n; i++) {
			sumx = sumx + poly.xpoints[i];
			sumy = sumy + poly.ypoints[i];
		}
		double meanx = sumx / n;
		double meany = sumy / n;

		// calculate moments
		double[] X = new double[n], Y = new double[n];
		double Mxx = 0, Myy = 0, Mxy = 0, Mxz = 0, Myz = 0, Mzz = 0;
		for (int i = 0; i < n; i++) {
			X[i] = x[i] - meanx;
			Y[i] = y[i] - meany;
			double Zi = X[i] * X[i] + Y[i] * Y[i];
			Mxy = Mxy + X[i] * Y[i];
			Mxx = Mxx + X[i] * X[i];
			Myy = Myy + Y[i] * Y[i];
			Mxz = Mxz + X[i] * Zi;
			Myz = Myz + Y[i] * Zi;
			Mzz = Mzz + Zi * Zi;
		}
		Mxx = Mxx / n;
		Myy = Myy / n;
		Mxy = Mxy / n;
		Mxz = Mxz / n;
		Myz = Myz / n;
		Mzz = Mzz / n;

		// calculate the coefficients of the characteristic polynomial
		double Mz = Mxx + Myy;
		double Cov_xy = Mxx * Myy - Mxy * Mxy;
		double Mxz2 = Mxz * Mxz;
		double Myz2 = Myz * Myz;
		double A2 = 4 * Cov_xy - 3 * Mz * Mz - Mzz;
		double A1 = Mzz * Mz + 4 * Cov_xy * Mz - Mxz2 - Myz2 - Mz * Mz * Mz;
		double A0 = Mxz2 * Myy + Myz2 * Mxx - Mzz * Cov_xy - 2 * Mxz * Myz
				* Mxy + Mz * Mz * Cov_xy;
		double A22 = A2 + A2;
		double epsilon = 1e-12;
		double ynew = 1e+20;
		int IterMax = 20;
		double xnew = 0;
		int iterations = 0;

		// Newton's method starting at x=0
		for (int iter = 1; iter <= IterMax; iter++) {
			iterations = iter;
			double yold = ynew;
			ynew = A0 + xnew * (A1 + xnew * (A2 + 4. * xnew * xnew));
			if (Math.abs(ynew) > Math.abs(yold)) {
				if (IJ.debugMode)
					IJ.log("Fit Circle: wrong direction: |ynew| > |yold|");
				xnew = 0;
				break;
			}
			double Dy = A1 + xnew * (A22 + 16 * xnew * xnew);
			double xold = xnew;
			xnew = xold - ynew / Dy;
			if (Math.abs((xnew - xold) / xnew) < epsilon)
				break;
			if (iter >= IterMax) {
				if (IJ.debugMode)
					IJ.log("Fit Circle: will not converge");
				xnew = 0;
			}
			if (xnew < 0) {
				if (IJ.debugMode)
					IJ.log("Fit Circle: negative root:  x = " + xnew);
				xnew = 0;
			}
		}
		if (IJ.debugMode)
			IJ.log("Fit Circle: n=" + n + ", xnew=" + IJ.d2s(xnew, 2)
					+ ", iterations=" + iterations);

		// calculate the circle parameters
		double DET = xnew * xnew - xnew * Mz + Cov_xy;
		double CenterX = (Mxz * (Myy - xnew) - Myz * Mxy) / (2 * DET);
		double CenterY = (Myz * (Mxx - xnew) - Mxz * Mxy) / (2 * DET);
		double radius = Math.sqrt(CenterX * CenterX + CenterY * CenterY + Mz
				+ 2 * xnew);
		if (Double.isNaN(radius)) {
			IJ.error("Fit Circle", "Points are collinear.");
			return;
		}

		CenterX = CenterX + meanx;
		CenterY = CenterY + meany;
		imp.killRoi();

		// messo imp.setRoi anzich� IJ.makeOval perch� permette di non mostrare
		// l'immagine
		imp.setRoi(new OvalRoi((int) Math.round(CenterX - radius), (int) Math
				.round(CenterY - radius), (int) Math.round(2 * radius),
				(int) Math.round(2 * radius)));
	}

	/***
	 * Copied from http://billauer.co.il/peakdet.htm Peak Detection using MATLAB
	 * Author: Eli Billauer
	 * 
	 * @param profile
	 * @param delta
	 * @return
	 */
	public static ArrayList<ArrayList<Double>> peakDet(double[][] profile,
			double delta) {

		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		ArrayList<ArrayList<Double>> matout = new ArrayList<ArrayList<Double>>();

		ArrayList<Double> maxtabx = new ArrayList<Double>();
		ArrayList<Double> maxtaby = new ArrayList<Double>();
		ArrayList<Double> mintabx = new ArrayList<Double>();
		ArrayList<Double> mintaby = new ArrayList<Double>();

		double[] vetx = new double[profile.length];
		double[] vety = new double[profile.length];
		for (int i1 = 0; i1 < profile.length; i1++) {
			vetx[i1] = profile[i1][0];
			vety[i1] = profile[i1][1];
		}
		double maxpos = -1.0;
		double minpos = -1.0;
		boolean lookformax = true;

		for (int i1 = 0; i1 < vety.length; i1++) {
			double valy = vety[i1];
			if (valy > max) {
				max = valy;
				maxpos = vetx[i1];
			}
			if (valy < min) {
				min = valy;
				minpos = vetx[i1];
			}
			stateChange(lookformax);

			if (lookformax) {
				if (valy < max - delta) {
					maxtabx.add((Double) maxpos);
					maxtaby.add((Double) max);
					min = valy;
					minpos = vetx[i1];
					lookformax = false;
				}
			} else {
				if (valy > min + delta) {
					mintabx.add((Double) minpos);
					mintaby.add((Double) min);
					max = valy;
					maxpos = vetx[i1];
					lookformax = true;
				}
			}

		}
		// MyLog.logArrayList(mintabx, "############## mintabx #############");
		// MyLog.logArrayList(mintaby, "############## mintaby #############");
		// MyLog.logArrayList(maxtabx, "############## maxtabx #############");
		// MyLog.logArrayList(maxtaby, "############## maxtaby #############");
		matout.add(mintabx);
		matout.add(mintaby);
		matout.add(maxtabx);
		matout.add(maxtaby);

		return matout;
	}

	/***
	 * Impulso al fronte di salita
	 * 
	 * @param input
	 */
	public static void stateChange(boolean input) {
		pulse = false;
		if ((input != previous) && !init1)
			pulse = true;
		init1 = false;
		return;
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
		double[] profile2 = new double[len1];
		for (int j1 = 1; j1 < len1 - 1; j1++) {
			profile2[j1] = profile1[j1];
		}

		for (int i1 = 0; i1 < loops; i1++) {
			for (int j1 = 1; j1 < len1 - 1; j1++)
				profile2[j1] = (profile2[j1 - 1] + profile2[j1] + profile2[j1 + 1]) / 3;
		}
		return profile2;

	}

	/**
	 * Partendo da una immagine a cui � stato applicato il treshold automatico
	 * di ImageJ, traccia il perimetro esterno, di larghezza 1 pixel, del
	 * cerchio
	 * 
	 * @param imp1
	 * @return
	 */

	public static ImagePlus circleOutline(ImagePlus imp1) {
		ImageProcessor ip1 = imp1.getProcessor();
		byte[] pixels1 = (byte[]) ip1.getPixels();
		int width = imp1.getWidth();
		int height = imp1.getHeight();
		boolean previous;
		boolean actual;
		boolean inside;
		int auxPrevious = 0;
		int auxActual = 0;

		// scansione verticale
		boolean[] pixels2 = new boolean[pixels1.length];
		for (int i1 = 0; i1 < width; i1++) {
			inside = false;
			for (int i2 = 0; i2 < height; i2++) {
				previous = false;
				actual = false;
				String deb = "deb= ";
				auxActual = i1 + i2 * height;
				auxPrevious = auxActual - height;
				if (auxPrevious < 0)
					auxPrevious = auxActual;
				if (pixels1[auxActual] != 0)
					actual = true;
				if (pixels1[auxPrevious] != 0)
					previous = true;
				if (!inside && !previous && actual) { // fronte salita
					deb = deb + "a";
					pixels2[auxActual] = true;
				} else if (!inside && previous && !actual) { // fronte discesa
					deb = deb + "b";
					inside = true;
				} else if (inside && previous && !actual) {
					deb = deb + "c";
					pixels2[auxPrevious] = true;
					inside = false;
				}
			}
		}
		// scansione orizzontale
		boolean[] pixels3 = new boolean[pixels1.length];
		for (int i1 = 0; i1 < width; i1++) {
			inside = false;
			for (int i2 = 0; i2 < height; i2++) {
				previous = false;
				actual = false;
				String deb = "deb= ";
				auxActual = i1 * width + i2;
				auxPrevious = auxActual - 1;
				if (auxPrevious < 0)
					auxPrevious = auxActual;
				if (pixels1[auxActual] != 0)
					actual = true;
				if (pixels1[auxPrevious] != 0)
					previous = true;
				if (!inside && !previous && actual) { // fronte salita
					deb = deb + "a";
					pixels3[auxActual] = true;
				} else if (!inside && previous && !actual) { // fronte discesa
					deb = deb + "b";
					inside = true;
				} else if (inside && previous && !actual) {
					deb = deb + "c";
					pixels3[auxPrevious] = true;
					inside = false;
				}
			}
		}
		ImagePlus imp15 = NewImage.createByteImage("OUTLINE", imp1.getWidth(),
				imp1.getHeight(), 1, NewImage.FILL_BLACK);
		ByteProcessor ip15 = (ByteProcessor) imp15.getProcessor();
		byte[] pixels15 = (byte[]) ip15.getPixels();

		for (int i1 = 0; i1 < pixels1.length; i1++) {
			if (pixels2[i1] || pixels3[i1]) {
				pixels15[i1] = (byte) 255;
			}
		}
		return imp15;
	}
	
	
	/**
	 * Imposta una Roi circolare diametro 4 in corrispondenza delle coordinate
	 * passate, importa la Roi nell'Overlay. La routine e' utilizzata per
	 * disegnare il centro di un cerchio su di un overlay.
	 * 
	 * @param imp1
	 * @param over1
	 * @param xCenterCircle
	 * @param yCenterCircle
	 * @param color1
	 */
	public static void drawCenter(ImagePlus imp1, Overlay over1,
			int xCenterCircle, int yCenterCircle, Color color1) {
		// imp1.setOverlay(over1);
		imp1.setRoi(new OvalRoi(xCenterCircle - 2, yCenterCircle - 2, 4, 4));
		Roi roi1 = imp1.getRoi();
		roi1.setFillColor(color1);
		roi1.setStrokeColor(color1);
		over1.addElement(imp1.getRoi());
		imp1.killRoi();
	}

	public static void drawCenter(ImagePlus imp1, Overlay over1,
			double xCenterCircle, double yCenterCircle, Color color1) {
		// imp1.setOverlay(over1);
		imp1.setRoi(new OvalRoi(xCenterCircle - 2, yCenterCircle - 2, 4, 4));
		Roi roi1 = imp1.getRoi();
		roi1.setFillColor(color1);
		roi1.setStrokeColor(color1);
		over1.addElement(imp1.getRoi());
		imp1.killRoi();
	}
	
	
}
