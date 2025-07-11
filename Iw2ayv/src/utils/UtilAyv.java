package utils;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.Prefs;
import ij.WindowManager;
import ij.gui.ImageWindow;
import ij.gui.NonBlockingGenericDialog;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.gui.PointRoi;
import ij.gui.Roi;
import ij.gui.WaitForUserDialog;
import ij.io.OpenDialog;
import ij.io.Opener;
import ij.measure.Calibration;
import ij.measure.ResultsTable;
import ij.plugin.ImageCalculator;
import ij.plugin.filter.Analyzer;
import ij.process.FloatProcessor;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;
import ij.text.TextWindow;
import ij.util.DicomTools;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Window;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/*
 * Copyright (C) 2007 Alberto Duina, SPEDALI CIVILI DI BRESCIA, Brescia ITALY
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

/**
 * Contiene le utility generiche
 * 
 * @author Alberto Duina - SPEDALI CIVILI DI BRESCIA - Servizio di Fisica
 *         Sanitaria
 * 
 * 
 */
public class UtilAyv {
	public static String VERSION = "UtilAyv-v4.10_10dec08_";

	public static int location;

	public static void stopHere() {
		IJ.log("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + "line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber() + " class="
				+ Thread.currentThread().getStackTrace()[2].getClassName() + " method="
				+ Thread.currentThread().getStackTrace()[2].getMethodName());
		new WaitForUserDialog("Do something, then click OK.").show();
	}

	/**
	 * legge un double da una stringa
	 * 
	 * @param str stringa in input
	 * @return double letto nella stringa
	 */
	public static double convertToDouble(String str) {
		try {
			Double d = Double.valueOf(str);
			return d.doubleValue();
		} catch (Exception e) {
			return Double.NaN;
		}
	}

	/**
	 * stampa di un double con un numero di decimali selezionabile
	 * 
	 * @param n1   double da stampare
	 * @param prec precisione
	 * @return stringa di stampa
	 */
	public static String printDoubleDecimals(double n1, int prec) {
		String str;
		if (Math.round(n1) == n1)
			str = IJ.d2s(n1, 0);
		else
			str = IJ.d2s(n1, prec);
		return str;
	}

	/**
	 * genera l'immagine differenza pixel-by-pixel
	 * 
	 * @param imp1 immagine minuendo
	 * @param imp2 immagine sottraendo
	 * @return immagine differenza
	 */
	public static ImagePlus genImaDifference(ImagePlus imp1, ImagePlus imp2) {

		ImageProcessor ip1;
		ImageProcessor ip2;
		double v1, v2, v3;
		boolean debug = false;
		if (UtilAyv.compareImagesByPixel(imp1, imp2, debug))
			MyLog.waitThere("ATTENZIONE SONO STATE PASSATE A GENIMADIFFERENCE \n"
					+ "DUE IMMAGINU UGUALI, L'IMMAGINE DIFFERENZA VARRA' \n"
					+ "PERTANTO ZERO E SI AVRA' UN SNR INFINITY");

		if (imp1 == null)
			return (null);
		if (imp2 == null)
			return (null);
		int width = imp1.getWidth();
		int height = imp1.getHeight();
		float[] pixels = new float[width * height];
		ip1 = imp1.getProcessor();
		ip2 = imp2.getProcessor();
		ImageProcessor ip3 = new FloatProcessor(width, height, pixels, null);
		ImagePlus imp3 = new ImagePlus("Immagine differenza pixel-by-pixel", ip3);
		ip3 = imp3.getProcessor();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				v1 = ip1.getPixelValue(x, y);
				v2 = ip2.getPixelValue(x, y);
				v3 = v1 - v2;
				ip3.putPixelValue(x, y, v3);
			}
		}
		return imp3;
	}

	/**
	 * genera l'immagine differenza pixel-by-pixel
	 * 
	 * @param i1 immagine minuendo
	 * @param i2 immagine sottraendo
	 * @return immagine differenza
	 */
	public static ImagePlus diffIma(ImagePlus imp1, ImagePlus imp2) {

		ImageCalculator ic1 = new ImageCalculator();
		String params = "Subtract create 32-bit";
		ImagePlus imp3 = ic1.run(params, imp1, imp2);
		return imp3;
	}

	public static String getFiveLetters(String seriesDescription) {

		String codice;
		String blob1 = "DelRe";
		int len1 = seriesDescription.length();

		if (len1 >= 5) {
			codice = seriesDescription.substring(0, 5).trim();
			// la seguente modifica permette di leggere le ultime 5 lettere dei codici che
			// iniziano con "DelRe", modifica per il CAE (11/08/2023)- non dovrebbe
			// intervenire in nessun altro caso
			if (codice.equalsIgnoreCase(blob1)) {
				codice = seriesDescription.substring(len1 - 5, len1);
			}
		} else {
			codice = "_xxx_";
		}
		return codice;
	}

	/**
	 * 
	 * @param vetReference sono le coordinate dei punti estremi del segmento di
	 *                     riferimento verticale, posizionato dall'operatruce
	 * @param cx           coordinata x da rototraslare
	 * @param cy           coordinata y da rototraslare
	 * @param debug1       attiva stampe di debug
	 * @return msd[0] coordinata x rototraslata, msd[1] coordinata y rototraslata
	 */

	public static double[] coord2D2(double[] vetReference, double cx, double cy, boolean debug1, double ottusangolo) {
		double x1;
		double y1;
		double msd[];

		double angle1 = Math.atan((vetReference[1] - vetReference[3]) / (vetReference[0] - vetReference[2]));
		if (debug1) {
			IJ.log("-------- vetReference --------");
			IJ.log("vetReference[0]= " + vetReference[0] + " vetReference[1]= " + vetReference[1]);
			IJ.log("vetReference[2]= " + vetReference[2] + " vetReference[3]= " + vetReference[3]);
			IJ.log("-------- input --------");
			IJ.log("CX= " + cx + " CY= " + cy);
			IJ.log("-------- coord2D2 --------");
			IJ.log("angolo= " + Math.toDegrees(angle1) + "  sin= " + Math.sin(angle1));
			IJ.log("angolo= " + Math.toDegrees(angle1) + "  cos= " + Math.cos(angle1));
			IJ.log("-------- OTTUSANGOLO --------");
			IJ.log("REFERENCE ANGLE= " + Math.toDegrees(ottusangolo));
			IJ.log("REFERENCE ANGLE= " + ottusangolo);
			IJ.log("--------------------------");
		}

		String aux = "";

		if (Math.sin(angle1) < 0) {
			aux = "MENO";
			x1 = -cx * Math.sin(angle1) - cy * Math.cos(angle1) + vetReference[0];
			y1 = cx * Math.cos(angle1) - cy * Math.sin(angle1) + vetReference[1];
		} else {
			aux = "PIU";
			x1 = cx * Math.sin(angle1) + cy * Math.cos(angle1) + vetReference[0];
			y1 = -cx * Math.cos(angle1) + cy * Math.sin(angle1) + vetReference[1];
		}

		msd = new double[2];
		msd[0] = x1;
		msd[1] = y1;
		if (debug1) {
			IJ.log(aux);
			IJ.log("coord2D output cx= " + cx + "  cy= " + cy + "  x1= " + x1 + "  y1= " + y1);
			IJ.log("--------------------------");
		}

		return msd;
	} // coord2D

	/**
	 * calcoli per rototraslazione delle coordinate rispetto ad un segmento di
	 * riferimento
	 * 
	 * @param ax     coord x inizio segmento di riferimento
	 * @param ay     coord y inizio segmento di riferimento
	 * @param bx     coord x fine segmento di riferimento
	 * @param by     coord y fine segmento di riferimento
	 * @param cx     coordinata x da rototraslare
	 * @param cy     coordinata y da rototraslare
	 * @param debug1 true per debug
	 * @return msd[0] coordinata x rototraslata, msd[1] coordinata y rototraslata
	 */
	public static double[] coord2D(double ax, double ay, double bx, double by, double cx, double cy, boolean debug1) {
		double x1;
		double y1;
		double msd[];

		double angle1 = Math.atan((ay - by) / (ax - bx));
		if (debug1) {
			IJ.log("-------- coord2D --------");

			IJ.log("angolo= " + Math.toDegrees(angle1) + "  sin= " + Math.sin(angle1));
			IJ.log("angolo= " + Math.toDegrees(angle1) + "  cos= " + Math.cos(angle1));
		}
		if (Math.sin(angle1) < 0) {
			// piu30
			x1 = -cx * Math.sin(angle1) - cy * Math.cos(angle1) + ax;
			y1 = cx * Math.cos(angle1) - cy * Math.sin(angle1) + ay;
		} else {
			// meno30
			x1 = cx * Math.sin(angle1) + cy * Math.cos(angle1) + ax;
			y1 = -cx * Math.cos(angle1) + cy * Math.sin(angle1) + ay;
		}
		msd = new double[2];
		msd[0] = x1;
		msd[1] = y1;
		if (debug1) {
			IJ.log("coord2D output cx= " + cx + "  x1= " + x1 + "  cy= " + cy + "  y1= " + y1);
			IJ.log("--------------------------");

		}
		return msd;
	} // coord2D

	/**
	 * 
	 * @param vetReference sono le coordinate dei punti estremi del segmento di
	 *                     riferimento verticale, posizionato dall'operatruce
	 * @param cx           coordinata x da rototraslare
	 * @param cy           coordinata y da rototraslare
	 * @param debug1       attiva stampe di debug
	 * @return msd[0] coordinata x rototraslata, msd[1] coordinata y rototraslata
	 */

	public static double[] coord2D2(double[] vetReference, double cx, double cy, boolean debug1) {
		double x1;
		double y1;
		double msd[];

		double angle1 = Math.atan((vetReference[1] - vetReference[3]) / (vetReference[0] - vetReference[2]));
		if (debug1) {
			IJ.log("-------- coord2D2 --------");
			IJ.log("angolo= " + Math.toDegrees(angle1) + "  sin= " + Math.sin(angle1));
			IJ.log("angolo= " + Math.toDegrees(angle1) + "  cos= " + Math.cos(angle1));
		}

		if (Math.sin(angle1) < 0) {
			// piu30
			x1 = -cx * Math.sin(angle1) - cy * Math.cos(angle1) + vetReference[0];
			y1 = cx * Math.cos(angle1) - cy * Math.sin(angle1) + vetReference[1];
		} else {
			// meno30
			x1 = cx * Math.sin(angle1) + cy * Math.cos(angle1) + vetReference[0];
			y1 = -cx * Math.cos(angle1) + cy * Math.sin(angle1) + vetReference[1];
		}
		msd = new double[2];
		msd[0] = x1;
		msd[1] = y1;
		if (debug1) {
			IJ.log("coord2D output cx= " + cx + "  x1= " + x1 + "  cy= " + cy + "  y1= " + y1);
			IJ.log("--------------------------");
		}

		return msd;
	} // coord2D

	public static void setMyPrecision() {
		Analyzer.setPrecision(9);
	}

	/**
	 * impostazione di Analyzer SetMeasurements
	 * 
	 * @param misure2 maschera bits misure da attivare
	 * @return le misure selezionate in precedenza
	 */
	public static int setMeasure(int misure2) {
		int misure1 = Analyzer.getMeasurements();
		// defined in Set Measurements dialog

		// measurements |= AREA; // 1
		// measurements |= MEAN; // 2
		// measurements |= STD_DEV; // 4
		// measurements |= MODE; // 8
		// measurements |= MIN_MAX; // 16
		// measurements |= CENTROID; // 32;
		// measurements |= CENTER_OF_MASS; // 64
		// measurements |= PERIMETER; // 128
		// measurements |= LIMIT; // 256
		// measurements |= RECT; // 512
		// measurements |= LABELS; // 1024
		// measurements |= ELLIPSE; // 2048
		// measurements |= INVERT_Y; // 4096
		// measurements |= CIRCULARITY; // 8192
		// measurements |= FERET; // 16384

		Analyzer.setMeasurements(misure2);
		return misure1;
	}

	/**
	 * ripristina in Analyzer SetMeasurements le misure precedenti
	 * 
	 * @param misure1 misure selezionate in precedenza
	 */
	public static void resetMeasure(int misure1) {
		Analyzer.setMeasurements(misure1);
	}

	/**
	 * chiude tutte le finestre e la ResultsTable, ovvia ad un malfunzionamento di
	 * CloseAllWindows se viene selezionato RIFAI pi� volte
	 */

	public static void cleanUp() {
		if (WindowManager.getFrame("Results") != null) {
			IJ.selectWindow("Results");
			IJ.run("Clear Results");
			IJ.run("Close");
		}

		if (WindowManager.getFrame("Log") != null) {
			IJ.selectWindow("Log");
			IJ.run("Close");
		}

		if (WindowManager.getFrame("Risultati") != null) {
			IJ.selectWindow("Risultati");
			IJ.run("Close");
		}

		// metodo alternativo -> IJ.runMacro("if (isOpen('Results'))
		// {selectWindow('Results'); run('Close');}");

		while (WindowManager.getWindowCount() > 0) {
			IJ.wait(100);
			IJ.run("Close");
			IJ.wait(100);
		}
	}

	/**
	 * chiude tutte le finestre e la ResultsTable, ovvia ad un malfunzionamento di
	 * CloseAllWindows se viene selezionato RIFAI pi� volte
	 */
	public static void cleanUp2() {

		while (WindowManager.getWindowCount() > 0) {
			IJ.wait(100);
			IJ.run("Close");
			IJ.wait(100);
		}
	}

	/**
	 * estrae da una stringa delle preferenze di ImageJ i dati, separati da ;
	 * 
	 * @param xData stringa preferenze
	 * @return vettore coi dati decodificati
	 */
	public static int[] getPos(String xData) {

		StringTokenizer st = new StringTokenizer(xData, ";");
		int nTokens = st.countTokens();
		if (nTokens < 1)
			return new int[0];
		int n = nTokens;
		int data[] = new int[n];
		for (int i = 0; i < n; i++) {
			data[i] = getNum(st);
		}
		return data;
	}

	/***
	 * estrae da una stringa delle preferenze di ImageJ i dati, separati da ;
	 * 
	 * @param xData
	 * @param lato
	 * @return
	 */
	public static int[] getPos2(String xData, int lato) {

		StringTokenizer st = new StringTokenizer(xData, ";");
		int nTokens = st.countTokens();
		if (nTokens < 1)
			return new int[0];
		int n = nTokens;
		int data[] = new int[n];
		double aux1;
		for (int i = 0; i < n; i++) {
			aux1 = getNum2(st) * lato;
			data[i] = (int) Math.round(aux1);
		}
		return data;
	}

	/***
	 * mette
	 * 
	 * @param positions
	 * @param lato
	 * @return
	 */
	public static String putPos2(int[] positions, int lato) {

		String positionsString = "";
		double aux1;
		for (int i1 = 0; i1 < positions.length; i1++) {
			aux1 = (double) positions[i1] / (double) lato;
			positionsString = positionsString + aux1 + ";";
		}
		return positionsString;
	}

	/**
	 * estrae i tokens dallo string tokenizer
	 * 
	 * @param st string tokenizer con i dati
	 * @return il token estratto (a ogni chiamata viene letto il token via via
	 *         successivo)
	 */
	public static int getNum(StringTokenizer st) {

		int d1 = 0;
		String token = st.nextToken();
		try {
			d1 = ReadDicom.readInt(token);
		} catch (NumberFormatException e) {
			IJ.log("getNum error");
		}
		return (d1);
	}

	public static double getNum2(StringTokenizer st) {

		double d1 = 0;
		String token = st.nextToken();
		try {
			d1 = ReadDicom.readDouble(token);
		} catch (NumberFormatException e) {
			IJ.log("getNum error");
		}
		return (d1);
	}

	/**
	 * estrae il valore reale dei pixels dalle immagini calibrate e non calibrate
	 * 
	 * @param imp immagine da cui estrarre il valore dei pixels
	 * @return array contenente il valore reale dei pixels
	 */
	public static short[] truePixels(ImagePlus imp) {

		ImageProcessor ip = imp.getProcessor();
		Calibration cal = imp.getCalibration();
		short[] pixels = (short[]) ip.getPixelsCopy();
		for (int i1 = 0; i1 < pixels.length; i1++) {
			pixels[i1] = (short) cal.getRawValue(pixels[i1]);
		}
		return (pixels);
	} // truePixels

	public static boolean compareImagesByDifference(ImagePlus imp1, ImagePlus imp2) {
		ImageCalculator ical = new ImageCalculator();

		ImagePlus imp3 = ical.run("Subtract create 32-bit", imp1, imp2);
		ImageStatistics stat3 = imp3.getStatistics();
		boolean result = false;
		if (stat3.mean == 0.) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public static boolean compareImagesByImageProcessors(ImagePlus imp1, ImagePlus imp2) {

		if (imp1.getBitDepth() != imp2.getBitDepth()) {
			IJ.log("compareImagesByImageProcessors.bit depth difference");
			return false;
		}
		if (imp1.getBitDepth() == 32) {
			float[] ip1 = (float[]) imp1.getProcessor().getPixels();
			float[] ip2 = (float[]) imp2.getProcessor().getPixels();
			return Arrays.equals(ip1, ip2);
		} else if (imp1.getBitDepth() == 16) {
			short[] ip1 = (short[]) imp1.getProcessor().getPixels();
			short[] ip2 = (short[]) imp2.getProcessor().getPixels();
			return Arrays.equals(ip1, ip2);
		} else if (imp1.getBitDepth() == 8) {
			byte[] ip1 = (byte[]) imp1.getProcessor().getPixels();
			byte[] ip2 = (byte[]) imp2.getProcessor().getPixels();
			return Arrays.equals(ip1, ip2);
		} else {
			IJ.log("compareImagesByImageProcessors.unexpected format");
			return false;
		}

	}

	public static boolean compareImagesByPixel(ImagePlus imp1, ImagePlus imp2, boolean debug) {
		if (imp1.getWidth() != imp2.getWidth() || imp1.getHeight() != imp2.getHeight()) {
			IJ.log("compareImagesByPixel.images of different dimensions");
			return false;
		}
		if (imp1.getBitDepth() != imp2.getBitDepth()) {
			IJ.log("compareImagesByPixel.images of different bitDepth");
			return false;
		}

		ImageProcessor ip1 = imp1.getProcessor();
		ImageProcessor ip2 = imp2.getProcessor();
		if (imp1.getBitDepth() == 8) {
			byte[] pixels1 = (byte[]) ip1.getPixels();
			byte[] pixels2 = (byte[]) ip2.getPixels();
			for (int i1 = 0; i1 < pixels1.length; i1++) {
				if (pixels1[i1] != pixels2[i1]) {
					if (debug)
						IJ.log("first difference found in pixel " + i1 + "/" + pixels1.length + " value= " + pixels1[i1]
								+ " instead of " + pixels2[i1]);
					return false;
				}
			}
			IJ.log("compareImagesByPixel.images are identical by pixel");
			return true;
		}
		if (imp1.getBitDepth() == 16) {
			short[] pixels1 = (short[]) ip1.getPixels();
			short[] pixels2 = (short[]) ip2.getPixels();
			for (int i1 = 0; i1 < pixels1.length; i1++) {
				if (pixels1[i1] != pixels2[i1]) {
					if (debug)
						IJ.log("first difference found in pixel " + i1 + "/" + pixels1.length + " value= " + pixels1[i1]
								+ " instead of " + pixels2[i1]);
					return false;
				}
			}
			IJ.log("compareImagesByPixel.images are identical by pixel");
			return true;
		}

		if (imp1.getBitDepth() == 32) {
			float[] pixels1 = (float[]) ip1.getPixels();
			float[] pixels2 = (float[]) ip2.getPixels();
			for (int i1 = 0; i1 < pixels1.length; i1++) {
				if (pixels1[i1] != pixels2[i1]) {
					if (debug)
						IJ.log("first difference found in pixel " + i1 + "/" + pixels1.length + " value= " + pixels1[i1]
								+ " instead of " + pixels2[i1]);
					return false;
				}
			}
			IJ.log("compareImagesByPixel.images are identical by pixel");
			return true;
		}
		IJ.log("compareImagesByPixel.unexpected route");

		return false;
	}

	/**
	 * Calculates the standard deviation of an array of numbers. see Knuth's The Art
	 * Of Computer Programming Volume II: Seminumerical Algorithms This algorithm is
	 * slower, but more resistant to error propagation.
	 * 
	 * @param data Numbers to compute the standard deviation of. Array must contain
	 *             two or more numbers.
	 * @return standard deviation estimate of population ( to get estimate of
	 *         sample, use n instead of n-1 in last line )
	 * @deprecated see ArrayUtils.vetSdKnuth
	 */

	@Deprecated
	public static double vetSdKnuth(double[] data) {
		final int n = data.length;
		if (n < 2) {
			return Double.NaN;
		}
		double avg = data[0];
		double sum = 0;
		// yes, i1 below starts from 1
		for (int i1 = 1; i1 < data.length; i1++) {
			double newavg = avg + (data[i1] - avg) / (i1 + 1);
			sum += (data[i1] - avg) * (data[i1] - newavg);
			avg = newavg;
		}
		return Math.sqrt(sum / (n - 1));
	}

	@Deprecated
	public static double vetSdKnuth(float[] data) {
		final int n = data.length;
		if (n < 2) {
			return Float.NaN;
		}
		double avg = (double) data[0];
		double sum = 0;
		// yes, i1 below starts from 1
		for (int i1 = 1; i1 < data.length; i1++) {
			double newavg = avg + (data[i1] - avg) / (i1 + 1);
			sum += (data[i1] - avg) * (data[i1] - newavg);
			avg = newavg;
		}
		return Math.sqrt(sum / (n - 1));
	}

	@Deprecated
	public static double vetSdKnuth(int[] data) {
		final int n = data.length;
		if (n < 2) {
			return Double.NaN;
		}
		double avg = (double) data[0];
		double sum = 0;
		// yes, i1 below starts from 1
		for (int i1 = 1; i1 < data.length; i1++) {
			double newavg = avg + (data[i1] - avg) / (i1 + 1);
			sum += (data[i1] - avg) * (data[i1] - newavg);
			avg = newavg;
		}
		return Math.sqrt(sum / (n - 1));
	}

	@Deprecated
	public static double vetSdKnuth(short[] data) {
		final int n = data.length;
		if (n < 2) {
			return Double.NaN;
		}
		double avg = (double) data[0];
		double sum = 0;
		// yes, i1 below starts from 1
		for (int i1 = 1; i1 < data.length; i1++) {
			double newavg = avg + (data[i1] - avg) / (i1 + 1);
			sum += (data[i1] - avg) * (data[i1] - newavg);
			avg = newavg;
		}
		return Math.sqrt(sum / (n - 1));
	}

	@Deprecated
	public static int vetMax(int[] data) {
		final int n = data.length;
		if (n < 1) {
			return Integer.MIN_VALUE;
		}
		int max = Integer.MIN_VALUE;
		for (int i1 = 0; i1 < data.length; i1++) {
			if (data[i1] > max) {
				max = data[i1];
			}
		}
		return max;
	}

	/***
	 * Trova il max di un vettore
	 * 
	 * @param data
	 * @return
	 * @Deprecated instead use ArrayUtils vetMax(data)
	 */
	@Deprecated
	public static double vetMax(double[] data) {
		final int n = data.length;
		if (n < 1) {
			return Double.NaN;
		}
		double max = Double.MIN_VALUE;
		for (int i1 = 0; i1 < data.length; i1++) {
			if (data[i1] > max) {
				max = data[i1];
			}
		}
		return max;
	}

	/***
	 * Trova il max di un vettore
	 * 
	 * @param data
	 * @return
	 * @Deprecated instead use ArrayUtils vetMax(data)
	 */
	@Deprecated
	public static short vetMax(short[] data) {
		final int n = data.length;
		if (n < 1) {
			return Short.MIN_VALUE;
		}
		short max = Short.MIN_VALUE;
		for (int i1 = 0; i1 < data.length; i1++) {
			if (data[i1] > max) {
				max = data[i1];
			}
		}
		return max;
	}

	/**
	 * @deprecated see ArrayUtils.vetMean
	 */
	@Deprecated
	public static double vetMean(double[] data) {
		final int n = data.length;
		if (n < 1) {
			return Double.NaN;
		}
		double sum = 0;
		// i1 below starts from 0
		for (int i1 = 0; i1 < data.length; i1++) {
			sum += data[i1];
		}
		double mean = sum / data.length;
		return mean;
	}

	@Deprecated
	public static double vetMean(int[] data) {
		final int n = data.length;
		if (n < 1) {
			return Double.NaN;
		}
		double sum = 0;
		// i1 below starts from 0
		for (int i1 = 0; i1 < data.length; i1++) {
			sum += (double) data[i1];
		}
		double mean = sum / data.length;
		return mean;
	}

	@Deprecated
	public static double vetMean(short[] data) {
		final int n = data.length;
		if (n < 1) {
			return Double.NaN;
		}
		double sum = 0;
		// i1 below starts from 0
		for (int i1 = 0; i1 < data.length; i1++) {
			sum += (double) data[i1];
		}
		double mean = sum / data.length;
		return mean;
	}

	/**
	 * chiude la eventuale finestra "Results" aperta
	 */
	public static void closeResultsWindow() {
		TextWindow win1 = ResultsTable.getResultsWindow();
		if (win1 != null) {
			// win1.close();
			IJ.selectWindow("Results");
			IJ.run("Close");
		}
	}

	/**
	 * resetta la ResultsTable
	 */
	public static void resetResultsTable() {
		ResultsTable rt1 = Analyzer.getResultsTable();
		rt1.disableRowLabels();
		rt1.reset();
	}

	/**
	 * selezione di un immagine da parte dell'utilizzatore
	 * 
	 * @param message messaggio per l'utilizzatore
	 * @return path dell'immagine selezionata
	 */
	public static String imageSelection(String message) {

		OpenDialog openDial = new OpenDialog(message, "");
		String directory = openDial.getDirectory();
		String name = openDial.getFileName();
		if (name == null) {
			MyLog.waitThere("imageSelection==null");
			return null;

		}
		String path1 = directory + name;
		return path1;
	} // imageSelection

	/**
	 * selezione di un immagine da parte dell'utilizzatore
	 * 
	 * @param message messaggio per l'utilizzatore
	 * @return path dell'immagine selezionata
	 */
	public static String imageSelection(String message, String defaultDirectory) {

		// defaultDirectory=defaultDirectory+"/";
		// IJ.log("defaultDirectory= "+defaultDirectory);
		OpenDialog openDial = new OpenDialog(message, defaultDirectory);

		String directory = openDial.getDirectory();
		String name = openDial.getFileName();
		if (name == null) {
			MyLog.waitThere("imageSelection==null");
			return null;
		}
		String path1 = directory + name;
		return path1;
	} // imageSelection

	/**
	 * apre e mostra una immagine ingrandita
	 * 
	 * @param path path dell'immagine da mostrare
	 * @return puntatore ImagePlus all'immagine
	 */
	public static ImagePlus openImageMaximized(String path) {

		ImagePlus imp = new Opener().openImage(path);
		if (imp == null) {
			MyLog.waitThere("Immagine " + path + " inesistente o non visualizzabile");
			return null;
		}
		// IJ.log("OpenImageEnlarged");
		showImageMaximized(imp);
		return imp;
	} // openImageEnlarged

	/**
	 * apre e mostra una immagine
	 * 
	 * @param path path dell'immagine da mostrare
	 * @return puntatore ImagePlus all'immagine
	 */
	public static ImagePlus openImageNormal(String path) {

		Opener opener = new Opener();
		ImagePlus imp = opener.openImage(path);
		if (imp == null) {
			MyLog.waitThere("Immagine " + path + " inesistente o non visualizzabile");
			return null;
		}
		// IJ.log("OpenImageNormal");
		imp.show();
		return imp;
	} // openImageNormal

	/**
	 * apre un immagine senza display
	 * 
	 * @param path path dell'immagine
	 * @return puntatore ImagePlus all'immagine
	 */
	public static ImagePlus openImageNoDisplay(String path, boolean verbose) {

		Opener opener = new Opener();
		ImagePlus imp = opener.openImage(path);
		if (imp == null) {
			if (verbose)
				MyLog.waitThere("Immagine " + path + " inesistente o non visualizzabile");
			return null;
		}
		return imp;
	}

	/**
	 * apre un immagine senza display
	 * 
	 * @param path path dell'immagine
	 * @return puntatore ImagePlus all'immagine
	 */
	public static ImagePlus openImageNoDisplay(File nome, boolean verbose) {

		Opener opener = new Opener();
		ImagePlus imp = opener.openImage(nome.getPath());
		if (imp == null) {
			if (verbose)
				MyLog.waitThere("Immagine " + nome.getPath() + " inesistente o non visualizzabile");
			return null;
		}
		return imp;
	}

	/**
	 * massimizza una immagine
	 * 
	 * @param imp puntatore ImagePlus all'immagine
	 */
	public static void showImageMaximized2(ImagePlus imp) {
		imp.show();
		ImageWindow win = imp.getWindow();
		// win.maximize(); // troppo a destra
		win.setExtendedState(ImageWindow.MAXIMIZED_BOTH);
		IJ.wait(20);
		win.setExtendedState(ImageWindow.MAXIMIZED_BOTH);
	}

	public static ImageWindow showImageMaximized(ImagePlus imp) {

		double mag1 = 0;
		if (imp == null) {
			MyLog.waitThere("imp==null");
			return null;
		}
		ImageWindow win = imp.getWindow();
		if (win != null) {
			// MyLog.waitThere("immagine gia' visualizzata !");
		} else {
			imp.show();
			do {
				win = imp.getWindow();
				mag1 = win.getCanvas().getMagnification();
				if (mag1 > 1.0) {
					MyLog.waitThere("dimensione immagine errata");
					win.close();
					imp.show();
				}
			} while (mag1 > 1.0);
		}
		win = imp.getWindow();
		win.setBounds(win.getMaximumBounds());
		win.maximize();
		win.toFront();
		// double mag2 = win.getCanvas().getMagnification();
		// IJ.log("mag2= " + mag2);
		// IJ.wait(10);
		// // win.setBounds(win.getMaximumBounds());
		// win.maximize();
		// double mag3 = win.getCanvas().getMagnification();
		// IJ.log("mag3= " + mag3);
		// // MyLog.waitHere();
		//
		// IJ.wait(10);
		// // win.setBounds(win.getMaximumBounds());
		// win.maximize();
		return win;
	}

	/**
	 * menu selezione siemens/ge
	 * 
	 * @return tasto selezionato
	 */
	public static int siemensGe() {

		int selection = ButtonMessages.ModalMsg("Seleziona immagine ", "Siemens", "GE");
		// N.B.: Siemens =2 !!!!
		return selection;
	}

	/**
	 * menu selezione manuale
	 * 
	 * @param version stringa con la versione
	 * @param type    stringa con la dicitura
	 * @return tasto selezionato
	 */
	public static int userSelectionManual1(String version, String type) {

		int userSelection1 = ButtonMessages.ModelessMsg(
				version + type + "\n \n" + "Scegliere modo funzionamento AUTOM o un PASSO alla volta", "AUTOM", "PASSO",
				"PROVA", "ABOUT", "CHIUDI");
		return (userSelection1);
	}

	/**
	 * menu selezione manuale
	 * 
	 * @param version stringa con la versione
	 * @param type    stringa con la dicitura
	 * @return tasto selezionato
	 */
	public static int userSelectionManual2(String version, String type, int preset) {

		int userSelection1 = ButtonMessages.ModelessMsg2(
				version + type + "\n \n" + "Scegliere modo funzionamento AUTOM o un PASSO alla volta", "AUTOM", "PASSO",
				"PROVA", "ABOUT", "CHIUDI", preset);

		return (userSelection1);
	}

	/**
	 * menu selezione manuale
	 * 
	 * @param version stringa con la versione
	 * @param type    stringa con la dicitura
	 * @return tasto selezionato
	 */
	public static int userSelectionManual(String version, String type) {

		String question = version + type + "\n \n" + "Scegliere modo funzionamento AUTOM o un PASSO alla volta";
		String[] vetPulsanti = { "AUTOM", "PASSO", "PROVA", "ABOUT", "CHIUDI" };
		boolean modal = false;
		String autoStr = null;
		AutoDialog ad1 = new AutoDialog(question, vetPulsanti, autoStr, modal);
		int risposta = vetPulsanti.length - ad1.answer() + 1;

		return (risposta);
	}

	public static int userSelectionManual(String version, String type, String autoStr) {

		String question = version + type + "\n \n" + "Scegliere modo funzionamento AUTOM o un PASSO alla volta";
		String[] vetPulsanti = { "AUTOM", "PASSO", "PROVA", "ABOUT", "CHIUDI" };
		boolean modal = false;
		AutoDialog ad1 = new AutoDialog(question, vetPulsanti, autoStr, modal);
		int risposta = vetPulsanti.length - ad1.answer();

		return (risposta);
	}

	/**
	 * menu selezione automatica
	 * 
	 * @param version stringa con la versione
	 * @param type    stringa con la dicitura
	 * @return tasto selezionato
	 */
	public static int userSelectionAuto(String version, String type) {

		int userSelection2 = ButtonMessages.ModelessMsg(version + type + "\n \n", "AUTOM", "PASSO", "ABOUT", "CHIUDI");
		return (userSelection2);
	}

	/**
	 * menu selezione automatica
	 * 
	 * @param version stringa con la versione
	 * @param type    stringa con la dicitura
	 * @return tasto selezionato
	 */
	public static int userSelectionAuto(String version, String type, String code, String coil, int riga, int length) {

		int userSelection2 = ButtonMessages.ModelessMsg(
				version + type + MyConst.NEWLINE + MyConst.NEWLINE + " CONTROLLO " + riga + "/" + length
						+ "    CODICE= " + code + "    COIL= " + coil + MyConst.NEWLINE + MyConst.NEWLINE,
				"AUTOM", "PASSO", "ABOUT", "CHIUDI");
		return (userSelection2);
	}

	// public boolean versionLess(String versionLimit) {
	// String versionIJ = IJ.getVersion();
	// boolean lessThan = versionIJ.compareTo(versionLimit) < 0;
	// return lessThan;
	// }

	public static ImageProcessor calibratedProcessor(ImagePlus imp) {
		if (imp == null)
			return null;
		ImageProcessor ip = imp.getProcessor();
		Calibration cal = imp.getCalibration();
		if (cal.calibrated())
			ip.setCalibrationTable(cal.getCTable());
		else
			ip.setCalibrationTable(null);
		return ip;
	}

	/**
	 * 
	 * @param vect1
	 * @param vect2
	 * @param precision
	 * @param msg
	 * @return
	 * @Deprecated "use ArrayUtils.compareVectors"
	 */
	@Deprecated

	public static boolean compareVectors(double[] vect1, double[] vect2, double precision, String msg) {
		if ((vect1 == null) || (vect2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning vector = null");
			}
			return false;
		}
		if (vect1.length != vect2.length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Vectors with different length");
			}
			return false;
		}
		for (int i1 = 0; i1 < vect1.length; i1++) {
			double diff = vect1[i1] - vect2[i1];
			if (Math.abs(diff) > precision) {
				return false;
			}
		}
		return true;
	}

	@Deprecated
	public static boolean compareVectors(float[] vect1, float[] vect2, float precision, String msg) {
		if ((vect1 == null) || (vect2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning vector = null");
			}
			return false;
		}
		if (vect1.length != vect2.length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Vectors with different length");
			}
			return false;
		}
		for (int i1 = 0; i1 < vect1.length; i1++) {
			double diff = vect1[i1] - vect2[i1];
			if (Math.abs(diff) > precision) {
				return false;
			}
		}
		return true;
	}

	@Deprecated
	public static boolean compareVectors(int[] vect1, int[] vect2, String msg) {
		if ((vect1 == null) || (vect2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning vector = null");
			}
			return false;
		}
		if (vect1.length != vect2.length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Vectors with different length");
			}
			return false;
		}
		for (int i1 = 0; i1 < vect1.length; i1++) {
			if (vect1[i1] != vect2[i1]) {
				return false;
			}
		}
		return true;
	}

	@Deprecated
	public static boolean compareVectors(long[] vect1, long[] vect2, String msg) {
		if ((vect1 == null) || (vect2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning vector = null");
			}
			return false;
		}
		if (vect1.length != vect2.length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Vectors with different length");
			}
			return false;
		}
		for (int i1 = 0; i1 < vect1.length; i1++) {
			if (vect1[i1] != vect2[i1]) {
				return false;
			}
		}
		return true;
	}

	@Deprecated
	public static boolean compareVectors(String[] vect1, String[] vect2, String msg) {
		if ((vect1 == null) || (vect2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning vector = null");
			}
			return false;
		}
		if (vect1.length != vect2.length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Vectors with different length");
			}
			return false;
		}
		for (int i1 = 0; i1 < vect1.length; i1++) {
			if (!vect1[i1].equals(vect2[i1])) {
				if (msg.length() > 0) {
					IJ.log(msg + " At pos " + i1 + " " + vect1[i1] + " differ " + vect2[i1]);
				}
				return false;
			}
		}
		return true;
	}

	@Deprecated
	public static boolean compareMatrix(double[][] mat1, double[][] mat2, String msg) {
		if ((mat1 == null) || (mat2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning matrix = null");
				return false;
			}
		}
		if (mat1.length != mat2.length || mat1[0].length != mat2[0].length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Matrices with different dimensions");
				return false;
			}
		}
		for (int i2 = 0; i2 < mat1[0].length; i2++) {
			for (int i1 = 0; i1 < mat1.length; i1++) {
				// IJ.log("compare
				// mat1["+i1+"]["+i2+"]="+mat1[i1][i2]+"\tmat2["+i1+"]["+i2+"]="+mat2[i1][i2]);
				if (mat1[i1][i2] != mat2[i1][i2]) {
					IJ.log("compareMatrix difference in " + i1 + "," + i2);
					return false;
				}
			}
		}
		return true;
	}

	@Deprecated
	public static boolean compareMatrix(float[][] mat1, float[][] mat2, String msg) {
		if ((mat1 == null) || (mat2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning matrix = null");
				return false;
			}
		}
		if (mat1.length != mat2.length || mat1[0].length != mat2[0].length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Matrices with different dimensions");
				return false;
			}
		}
		for (int i2 = 0; i2 < mat1[0].length; i2++) {
			for (int i1 = 0; i1 < mat1.length; i1++) {
				// IJ.log("compare
				// mat1["+i1+"]["+i2+"]="+mat1[i1][i2]+"\tmat2["+i1+"]["+i2+"]="+mat2[i1][i2]);
				if (mat1[i1][i2] != mat2[i1][i2]) {
					IJ.log("compareMatrix difference in " + i1 + "," + i2);
					return false;
				}
			}
		}
		return true;
	}

	@Deprecated
	public static boolean compareMatrix(int[][] mat1, int[][] mat2, String msg) {
		if ((mat1 == null) || (mat2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning matrix = null");
				return false;
			}
		}
		if (mat1.length != mat2.length || mat1[0].length != mat2[0].length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Matrices with different dimensions");
				return false;
			}
		}
		for (int i2 = 0; i2 < mat1[0].length; i2++) {
			for (int i1 = 0; i1 < mat1.length; i1++) {
				// IJ.log("compare
				// mat1["+i1+"]["+i2+"]="+mat1[i1][i2]+"\tmat2["+i1+"]["+i2+"]="+mat2[i1][i2]);
				if (mat1[i1][i2] != mat2[i1][i2]) {
					IJ.log("compareMatrix difference in " + i1 + "," + i2);
					return false;
				}
			}
		}
		return true;
	}

	@Deprecated
	public static boolean compareMatrix(String[][] mat1, String[][] mat2, String msg) {
		if ((mat1 == null) || (mat2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning matrix = null");
				return false;
			}
		}
		if (mat1.length != mat2.length || mat1[0].length != mat2[0].length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Matrices with different dimensions");
				return false;
			}
		}
		for (int i2 = 0; i2 < mat1[0].length; i2++) {
			for (int i1 = 0; i1 < mat1.length; i1++) {
				// IJ.log("compare
				// mat1["+i1+"]["+i2+"]="+mat1[i1][i2]+"\tmat2["+i1+"]["+i2+"]="+mat2[i1][i2]);
				if (!mat1[i1][i2].equals(mat2[i1][i2])) {
					IJ.log("compareMatrix difference in " + i1 + "," + i2);
					return false;
				}
			}
		}
		return true;
	}

	public static boolean compareDoublesWithTolerance(double aa, double bb, double tolerance) {
		if (Double.compare(aa, bb) == 0)
			return true;
		return Math.abs(aa - bb) < tolerance;
	}

	public static boolean compareDoublesWithTolerance(double aa, double bb, int digits) {

		// IJ.log ("aa= "+aa+" bb="+bb+" digits=" +digits);

		double uno = roundDoubleDecimals(aa, digits);
		double due = roundDoubleDecimals(bb, digits);
		double tre = Math.abs(roundDoubleDecimals(aa, digits) - roundDoubleDecimals(bb, digits));
		// IJ.log ("uno= "+uno+" due="+due+" tre=" +tre);
		// MyLog.waitHere();

		return tre == 0;
	}

	// public static double setPrecision2(double num, int precision) {
	// double prec= Math.pow(10, precision);
	// MyLog.waitHere("prec= "+prec);
	// int intPart = (int) num;
	// MyLog.waitHere("intPart= "+intPart);
	// double fractionalPart = num-intPart;
	// MyLog.waitHere("fractionalPart= "+fractionalPart);
	// fractionalPart *= prec;
	// MyLog.waitHere("fractionalPart= "+fractionalPart);
	// long fractPart = (long) fractionalPart;
	// MyLog.waitHere("fractlPart= "+fractPart);
	// double out= (double) intPart+(double) (fractPart)/prec;
	// return out;
	// }

	// /****
	// * Parrebbe una soluzione elegante di regolazione numero dei digits con
	// * arrotondamento
	// *
	// * @param num
	// * double di cui regolare la precisione
	// * @param precision
	// * numero dei digits
	// * @return
	// */
	// public static double setPrecision(double num, int precision) {
	//
	// Locale loc = Locale.US;
	// NumberFormat nf = NumberFormat.getInstance(loc);
	// nf.setMaximumFractionDigits(precision);
	// String myString = nf.format(num);
	// MyLog.waitHere("myString= "+myString);
	// double out = Double.valueOf(myString);
	// return out;
	// }

	// public static boolean compareDoublesWithTolerance(double aa, double bb,
	// int digits) {
	// double aa = Double.
	//
	// return false;
	// }

	public static String howmanyMemory() {
		long used3 = IJ.currentMemory();
		double used2 = (double) used3 / 1048576L;
		String used = IJ.d2s(used2, used2 > 50 ? 0 : 2) + "MB";
		long avail3 = IJ.maxMemory();
		double avail2 = (double) avail3 / 1048576L;
		String avail = IJ.d2s(avail2, avail2 > 50 ? 0 : 2) + "MB";
		String result = "used=" + used + " avail=" + avail;
		return result;
	}

	/**
	 * calculate startTime - actualTime in seconds
	 * 
	 * @param startTime
	 * @return
	 */
	public static double calcTime(double startTime) {
		long actualTime = System.currentTimeMillis();
		double elapsedTime = ((double) actualTime - startTime) / 1000.;
		return elapsedTime;
	}

	/**
	 * return the actualTime
	 * 
	 * @return
	 */
	public static double calcTime() {
		long actualTime = System.currentTimeMillis();
		return (double) actualTime;
	}

	/**
	 * 
	 * @param mat1
	 * @param index
	 * @return
	 */
	public static double[] vetFromMatrix(double[][] mat1, int index) {
		// Log.logMatrix(mat1, "mat1");
		if (mat1 == null) {
			IJ.log("vetFromMatrix.mat1 == null");
			return null;
		}
		if (index > mat1[0].length) {
			IJ.log("vetFromMatrix.index > mat1[0]");
			return null;
		}

		double[] vet1 = new double[mat1[index].length];
		for (int i1 = 0; i1 < mat1[index].length; i1++) {
			vet1[i1] = mat1[index][i1];
		}
		return vet1;
	}

	/**
	 * 
	 * @param matrix
	 * @param firstIndex
	 * @return
	 */
	public static double[][] subMatrix(double[][][] matrix, int firstIndex) {
		double[][] sub = new double[matrix[0].length][matrix[0][0].length];
		for (int i1 = 0; i1 < matrix[0].length; i1++) {
			for (int i2 = 0; i2 < matrix[0][0].length; i2++) {
				sub[i1][i2] = matrix[firstIndex][i1][i2];
			}
		}
		return sub;
	}

	/**
	 * 
	 * @param vetResults
	 * @param vetReference
	 * @param verbose
	 * @return
	 */
	public static boolean verifyResults1(double[] vetResults, double[] vetReference, String[] vetName) {
		boolean testok = true;

		if (vetResults == null || vetReference == null) {
			MyLog.logVector(vetResults, "vetResults");
			MyLog.logVector(vetReference, "vetReference");
			MyLog.logVector(vetName, "vetName");
			MyLog.waitThere("verifyResults1 vector null");
			MyLog.caller("verifyResults1 vector null");

			return false;
		}

		if (vetResults.length != vetReference.length || vetResults.length != vetName.length) {
			MyLog.logVector(vetResults, "vetResults");
			MyLog.logVector(vetReference, "vetReference");
			MyLog.logVector(vetName, "vetName");
			MyLog.waitThere("verifyResults.vectors of different length");
			return false;
		}

		if (vetName == null) {
			vetName = new String[vetResults.length];
		}

		for (int i1 = 0; i1 < vetResults.length; i1++) {

			if (Double.compare(vetResults[i1], vetReference[i1]) != 0) {
				IJ.log(vetName[i1] + " ERRATO " + vetResults[i1] + " anzich� " + vetReference[i1]);
				testok = false;
			}
		}
		return testok;
	}

	/**
	 * 
	 * @param vetResults
	 * @param vetReference
	 * @param verbose
	 * @return
	 */
	public static boolean verifyResults1(double[] vetResults, double[] vetReference) {
		boolean testok = true;

		if (vetResults == null || vetReference == null) {
			MyLog.logVector(vetResults, "vetResults");
			MyLog.logVector(vetReference, "vetReference");
			MyLog.waitHere("vector null");

			return false;
		}

		if (vetResults.length != vetReference.length) {
			MyLog.waitHere("verifyResults.vectors of different length");
			MyLog.logVector(vetResults, "vetResults");
			MyLog.logVector(vetReference, "vetReference");
			return false;
		}

		for (int i1 = 0; i1 < vetResults.length; i1++) {

			if (vetResults[i1] != vetReference[i1]) {
				MyLog.waitHere("ERRATO " + vetResults[i1] + " anzich� " + vetReference[i1]);
				testok = false;
			}
		}
		return testok;
	}

	public static boolean verifyResults2(int[] xMeasuredPoints, int[] yMeasuredPoints, int[] xRefPoints,
			int[] yRefPoints, String what) {

		boolean testok = true;
		for (int i1 = 0; i1 < xMeasuredPoints.length; i1++) {
			if (xMeasuredPoints[i1] != xRefPoints[i1]) {
				IJ.log("Coordinata  X " + what + (i1 + 1) + " ERRATA =" + xMeasuredPoints[i1] + " anzich� "
						+ xRefPoints[i1]);
				testok = false;
			}
		}
		for (int i1 = 0; i1 < yMeasuredPoints.length; i1++) {
			if (yMeasuredPoints[i1] != yRefPoints[i1]) {
				IJ.log("Coordinata  Y " + what + (i1 + 1) + " ERRATA =" + yMeasuredPoints[i1] + " anzich� "
						+ yRefPoints[i1]);
				testok = false;
			}
		}
		return testok;
	}

	public static boolean verifyResults3(final int[][] arr1, final int[][] arr2) {

		if (arr1 == null) {
			MyLog.waitHere("arr1==null");
			return false;
		}

		if (arr2 == null) {
			MyLog.waitHere("arr2==null");
			return false;
		}

		if (arr1.length != arr2.length) {
			MyLog.waitHere("different length");
			return false;
		}

		for (int i1 = 0; i1 < arr1.length; i1++) {
			if (!Arrays.equals(arr1[i1], arr2[i1])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * draw a ROI in the center of image
	 * 
	 * @param imp1
	 */
	public static void presetRoi(ImagePlus imp1, int diamRoi, boolean circular) {

		int xRoi1 = imp1.getWidth() / 2 - diamRoi / 2;
		int yRoi1 = imp1.getHeight() / 2 - diamRoi / 2;

		if (imp1.isVisible()) {
			imp1.getWindow().toFront();
		}
		if (circular) {
			imp1.setRoi(new OvalRoi(xRoi1, yRoi1, diamRoi, diamRoi));
		} else {
			imp1.setRoi(xRoi1, yRoi1, diamRoi, diamRoi);
		}
		imp1.updateAndDraw();
		return;
	}

	/**
	 * draw a ROI in the center of image
	 * 
	 * @param imp1
	 */
	public static void presetRoi(ImagePlus imp1, int diamRoi, int xOffset, int yOffset, boolean circular) {

		int xRoi1 = imp1.getWidth() / 2 - diamRoi / 2 + xOffset;
		int yRoi1 = imp1.getHeight() / 2 - diamRoi / 2 + yOffset;

		if (imp1.isVisible()) {
			imp1.getWindow().toFront();
		}
		if (circular) {
			imp1.setRoi(new OvalRoi(xRoi1, yRoi1, diamRoi, diamRoi));
		} else {
			imp1.setRoi(xRoi1, yRoi1, diamRoi, diamRoi);
		}
		imp1.updateAndDraw();
		return;
	}

	public static Polygon selectionPointsClick3(ImagePlus imp1, String messageLabel, String buttonLabel) {

		String oldTool = IJ.getToolName();
		imp1.killRoi();
		IJ.setTool("multi");

		// ButtonMessages.ModelessMsg(messageLabel, buttonLabel);
		NonBlockingGenericDialog nonBlockingGenericDialog = new NonBlockingGenericDialog("Action Required");
		nonBlockingGenericDialog.addMessage(messageLabel);
		nonBlockingGenericDialog.setOKLabel(buttonLabel);
		nonBlockingGenericDialog.setCancelLabel("ANNULLA");
		nonBlockingGenericDialog.centerDialog(true);
		nonBlockingGenericDialog.setAlwaysOnTop(true);
		nonBlockingGenericDialog.showDialog();

		if (nonBlockingGenericDialog.wasCanceled()) {
			return null;
		}
		if (imp1.getRoi() == null)
			return null;

		Polygon p1 = imp1.getRoi().getPolygon();

		IJ.setTool(oldTool);
		return p1;
	}

	public static Polygon selectionPointsClick(ImagePlus imp1, String messageLabel, String buttonLabel) {

		String oldTool = IJ.getToolName();
		imp1.killRoi();
		IJ.setTool("multi");
		int resp = ButtonMessages.ModelessMsg(messageLabel, buttonLabel, "<ANNULLA>");
		if (resp == 1)
			return null;
		if (imp1.getRoi() == null)
			return null;

		Polygon p1 = imp1.getRoi().getPolygon();

		IJ.setTool(oldTool);
		return p1;
	}

	public static Polygon clickSimulation(ImagePlus imp1, int[] vetX, int[] vetY) {

		PointRoi pRoi = new PointRoi(vetX, vetY, vetX.length);
		imp1.setOverlay(pRoi, Roi.getColor(), 2, Color.white);
		imp1.updateAndDraw();
		Polygon p1 = pRoi.getPolygon();
		return p1;
	}

	/**
	 * 
	 * @param vetRiga
	 * @param fileDir
	 * @param iw2ayvTable
	 */
	public static void saveResults2(int[] vetRiga, String fileDir, String[][] iw2ayvTable) {

		// IJ.run("Excel...", "select...=[" + fileDir + MyConst.XLS_FILE + "]");
		TableSequence lr = new TableSequence();
		for (int i1 = 0; i1 < vetRiga.length; i1++) {
			lr.putDone(iw2ayvTable, vetRiga[i1]);
		}
		lr.writeTable(fileDir + MyConst.SEQUENZE_FILE, iw2ayvTable);
	}

	// /**
	// *
	// * @param vetRiga
	// * @param fileDir
	// * @param iw2ayvTable
	// */
	// public static void saveResults(int[] vetRiga, String fileDir,
	// String[][] iw2ayvTable) {
	//
	// // IJ.run("Excel...", "select...=[" + fileDir + MyConst.XLS_FILE + "]");
	//
	// try {
	// mySaveAs(fileDir + MyConst.TXT_FILE);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// TableSequence lr = new TableSequence();
	// for (int i1 = 0; i1 < vetRiga.length; i1++) {
	// lr.putDone(iw2ayvTable, vetRiga[i1]);
	// }
	// lr.writeTable(fileDir + MyConst.SEQUENZE_FILE, iw2ayvTable);
	// }

	/**
	 * 
	 * @param vetRiga
	 * @param fileDir
	 * @param iw2ayvTable
	 */
	public static void saveResults(int[] vetRiga, String fileDir, String[][] iw2ayvTable, ResultsTable rt) {

		// IJ.run("Excel...", "select...=[" + fileDir + MyConst.XLS_FILE + "]");

		try {
			mySaveAs(fileDir + MyConst.TXT_FILE, rt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TableSequence lr = new TableSequence();
		for (int i1 = 0; i1 < vetRiga.length; i1++) {
			lr.putDone(iw2ayvTable, vetRiga[i1]);
		}
		lr.writeTable(fileDir + MyConst.SEQUENZE_FILE, iw2ayvTable);
	}

	/**
	 * 
	 * @param vetRiga
	 * @param fileDir
	 * @param iw2ayvTable
	 */
	public static void saveResults33(int[] vetRiga, String fileDir, String[][] iw2ayvTable) {

		try {
			mySaveAs2(fileDir + MyConst.TXT_FILE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TableSequence lr = new TableSequence();
		for (int i1 = 0; i1 < vetRiga.length; i1++) {
			lr.putDone(iw2ayvTable, vetRiga[i1]);
		}
		lr.writeTable(fileDir + MyConst.SEQUENZE_FILE, iw2ayvTable);
	}

	/**
	 * Saves this ResultsTable as a tab or comma delimited text file. The table is
	 * saved as a CSV (comma-separated values) file if 'path' ends with ".csv".
	 * Displays a file save dialog if 'path' is empty or null. Does nothing if the
	 * table is empty.
	 */
	public static void mySaveAs2(String path) throws IOException {

		ResultsTable rt = ResultsTable.getResultsTable();
		if (rt.getCounter() == 0) {
			MyLog.waitThere("NO_SAVE table empty!");
			return;
		}
		PrintWriter pw = null;
		boolean append = true;
		FileOutputStream fos = new FileOutputStream(path, append);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		pw = new PrintWriter(bos);
		String headings = rt.getColumnHeadings();
		pw.println("## " + headings);
		for (int i = 0; i < rt.getCounter(); i++)
			pw.println(rt.getRowAsString(i));
		pw.close();
	}

	/**
	 * Saves this ResultsTable as a tab or comma delimited text file. The table is
	 * saved as a CSV (comma-separated values) file if 'path' ends with ".csv".
	 * Displays a file save dialog if 'path' is empty or null. Does nothing if the
	 * table is empty.
	 */
	public static void mySaveAs(String path, ResultsTable rt) throws IOException {

		if (rt == null) {
			MyLog.waitHere("NO_SAVE table null!");
			return;
		}
		if (rt.getCounter() == 0) {
			MyLog.waitHere("NO_SAVE table empty!");
			return;
		}
		PrintWriter pw = null;
		boolean append = true;
		FileOutputStream fos = new FileOutputStream(path, append);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		pw = new PrintWriter(bos);
		String headings = rt.getColumnHeadings();
		pw.println("## " + headings);
		for (int i = 0; i < rt.getCounter(); i++)
			pw.println(rt.getRowAsString(i));
		pw.close();
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	public static int[] decodeTokens(String args) {
		StringTokenizer st = new StringTokenizer(args, "#");
		int nTokens = st.countTokens();
		int[] vetRiga = new int[nTokens];
		for (int i1 = 0; i1 < nTokens; i1++) {
			vetRiga[i1] = Integer.parseInt(st.nextToken());
		}
		return vetRiga;
	}

	/**
	 * 
	 */
	public static void afterWork() {
		UtilAyv.resetResultsTable();
		if (new File(MyConst.TEST_DIRECTORY).exists())
			InputOutput.deleteDir(new File(MyConst.TEST_DIRECTORY));
		UtilAyv.cleanUp();
	}

	/**
	 * Vectorize the ResutlsTable
	 * 
	 * @param rt1 results table
	 * @return
	 */
	public static int[][] vectorizeResults2(ResultsTable rt1) {

		int startColumn = 0;
		String uno = rt1.getColumnHeading(startColumn);
		String due = rt1.getColumnHeading(startColumn + 1);

		int standardInfoLength = ReportStandardInfo.getStandardInfoLength();
		int[][] results = new int[rt1.getCounter() - standardInfoLength][2];
		for (int i1 = 0; i1 < rt1.getCounter() - standardInfoLength; i1++) {
			int row = i1 + standardInfoLength;
			results[i1][0] = (int) rt1.getValue(uno, row);
			results[i1][1] = (int) rt1.getValue(due, row);
			// IJ.log("" + i1 + " " + results[i1]);
		}
		return results;
	}

	/**
	 * Vectorize the ResutlsTable
	 * 
	 * @param rt1 results table
	 * @return
	 */
	public static double[] vectorizeResults(ResultsTable rt1) {

		int startColumn = 0;

		int standardInfoLength = ReportStandardInfo.getStandardInfoLength();

		double[] results = new double[rt1.getCounter() - standardInfoLength];
		for (int i1 = 0; i1 < rt1.getCounter() - standardInfoLength; i1++) {
			int row = i1 + standardInfoLength;
			results[i1] = rt1.getValueAsDouble(startColumn, row);
			// IJ.log("" + i1 + " " + results[i1] + " at col=" + startColumn + "
			// row=" + row);
		}

		return results;
	}

	/**
	 * Vectorize the ResutlsTable
	 * 
	 * @param rt1 results table
	 * @return
	 */
	public static double[] vectorizeResultsNew(ResultsTable rt1) {

		int startColumn = 1;

		int standardInfoLength = ReportStandardInfo.getStandardInfoLength();

		double[] results = new double[rt1.getCounter() - standardInfoLength];
		for (int i1 = 0; i1 < rt1.getCounter() - standardInfoLength; i1++) {
			int row = i1 + standardInfoLength;
			results[i1] = rt1.getValueAsDouble(startColumn, row);
			// IJ.log("" + i1 + " " + results[i1] + " at col=" + startColumn + "
			// row=" + row);
		}

		return results;
	}

	/**
	 * Vectorize the ResutlsTable
	 * 
	 * @param rt1 results table
	 * @return
	 */
	public static double[] vectorizeResultsMultiple(ResultsTable rt1, int columns) {

		int startColumn = 0;

		// rt1.show("Results");

		int standardInfoLength = ReportStandardInfo.getStandardInfoLength();
		// MyLog.waitHere("standardInfoLength = " + standardInfoLength);

		int len = rt1.getCounter() - standardInfoLength;
		double[] results = new double[len * columns];
		int count = 0;
		for (int i1 = 0; i1 < len; i1++) {
			int row = i1 + standardInfoLength;
			for (int i2 = 0; i2 < columns; i2++) {
				results[count] = rt1.getValueAsDouble(startColumn + i2, row);
				count++;
			}
		}

		return results;
	}

	public static void dumpResultsTable(ResultsTable rt1) {

		IJ.log("------ ResultsTable --------");
		for (int i1 = 0; i1 < rt1.getCounter(); i1++)
			IJ.log(rt1.getRowAsString(i1));
		IJ.log("----------------------------");

	}

	public void demoPixelPertainRoiTest(ImageProcessor ip) {
		/**
		 * questo e' solo un appunto sui metodi che possiamo utilizzare per stabilire se
		 * un pixel e' all'interno di una ROI.
		 */

		// byte[] pixels = (byte[]) ip.getPixels();
		// int width = ip.getWidth();
		Rectangle r = ip.getRoi();
		ImageProcessor mask = ip.getMask();
		for (int y = r.y; y < (r.y + r.height); y++) {
			for (int x = r.x; x < (r.x + r.width); x++) {
				if (mask == null || mask.getPixel(x - r.x, y - r.y) != 0) {
					// ...DO What YOU WISH TO DO
					/**
					 * Roi roi = Imp.getRoi(); Rectangle rect = roi.getBounds(); rx = rect.x; ry =
					 * rect.y; w = rect.width; h = rect.height; for(int y=ry; y<ry+h; y++) { for(int
					 * x=rx; x<rx+w; x++) { if(roi.contains(x, y)) {
					 */

				}
			}
		}
	}

	public static String reverseSlash(String strIn) {
		String strOut = strIn.replaceAll("\\\\", "/");
		return strOut;
	}

	/**
	 * progettato per le SymphonyTim per ovviare al fatto che quando si fa un save
	 * uncombined non viene scritto il parametro dicom relativo alla receiver coil
	 * (software syngo MR B17 )
	 * 
	 * @param path1
	 * @return restituisce la sigla della bobina utilizzata
	 */
	public String kludge(String path1) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		String blob = "";
		String out = "";
		if (path1 == null || path1.equals("")) {
			out = "_";
			return out;
		}

		try {
			fis = new FileInputStream(path1);
			bis = new BufferedInputStream(fis);
			int size = bis.available();
			String header = getString(bis, size - 20);
			int fromIndex = 1;
			int i1 = 0;
			int index1 = header.indexOf("ImaCoilString", fromIndex) + 100;
			boolean continua = true;
			while (continua) {
				i1++;
				blob = header.substring(index1, index1 + i1);
				char cLetter = blob.charAt(i1 - 1);
				byte bLetter = (byte) cLetter;
				if (bLetter == 0x00) {
					continua = false;
				} else {
					out = blob;
				}
			}
		} catch (IOException e) {
			return null;
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
			bis = null; // BufferedInputStream.close() erst ab Java 1.2
						// definiert
		}
		if (out.length() == 0)
			out = "_";
		return out;
	}

	public static void logResizer() {
		Frame lw = WindowManager.getFrame("Log");
		if (lw == null)
			return;
		lw.setSize(300, 400);
		lw.setLocation(100, 200);
		return;
	}

	public static void logResizer(int width, int height, int posx, int posy) {
		Frame lw = WindowManager.getFrame("Log");
		if (lw == null)
			return;
		lw.setSize(width, height);
		lw.setLocation(posx, posy);
		return;
	}

	public static void myWait() {
		new WaitForUserDialog("Press a key ....").show();
	}

	public static String getString(BufferedInputStream bo, int len) throws IOException {
		int pos = 0;
		byte[] buf = new byte[len];
		// int size = bo.available();
		while (pos < len) {
			int count = bo.read(buf, pos, len - pos);
			pos += count;
		}
		location += len;
		return new String(buf);
	}

	// public static String getFirstCoil(String total) {
	// int i1 = total.indexOf(";");
	// String out = "";
	// if (i1 == -1) {
	// out = total;
	// } else {
	// out = total.substring(0, i1);
	// }
	// return out;
	// }

	public static boolean isInfinite(Number number) {
		if (number instanceof Double && ((Double) number).isInfinite())
			return true;
		if (number instanceof Float && ((Float) number).isInfinite())
			return true;
		return false;
	}

	public static boolean isNaN(Number number) {
		if (number instanceof Double && ((Double) number).isNaN())
			return true;
		if (number instanceof Float && ((Float) number).isNaN())
			return true;
		return false;
	}

	/**
	 * From http://rosettacode.org/wiki/Determine_if_a_string_is_numeric
	 * 
	 * @param inputData
	 * @return boolean
	 */
	public static boolean isNumeric(String inputData) {
		return inputData.matches("[-+]?\\d+(\\.\\d+)?");
	}

	/**
	 * Verifica se un valore calcolato � nei limiti assegnati
	 * 
	 * @param signal valore calcolato
	 * @param low    limite inferiore accettabilit�
	 * @param high   limite superiore accettabilit�
	 * @param title  stringa con nome valore
	 * @return true se accettato
	 */
	public static int checkLimits(double signal, double low, double high, String title) {
		int userSelection = 0;

		if ((Double.isNaN(signal)) || (Double.isInfinite(signal)) || (signal < low) || (signal > high)) {

			userSelection = ButtonMessages.ModelessMsg(
					"IL VALORE " + title + "= " + signal + " E'AL DI FUORI DAI LIMITI " + low + " - " + high,
					"SUCCESSIVA", "VISUALIZZA", "CONTINUA");
			return userSelection;
		} else
			return 0;
	}

	public static boolean checkLimits2(double signal, double low, double high, String title) {

		if ((Double.isNaN(signal)) || (Double.isInfinite(signal)) || (signal < low) || (signal > high)) {
			MyLog.waitThere(
					"IL VALORE DI " + title + " = " + signal + " E'AL DI FUORI DEI LIMITI " + low + " , " + high);
			return true;
		} else
			return false;
	}

	/***
	 * Verifica se un valore calcolato � nei limiti assegnati, overloaded senza
	 * messaggio
	 * 
	 * @param signal
	 * @param low
	 * @param high
	 * @return
	 */
	public static boolean checkLimits(double signal, double low, double high) {

		if ((Double.isNaN(signal)) || (Double.isInfinite(signal)) || (signal < low) || (signal > high))
			return false;
		else

			return true;
	}

	public static boolean myTestEquals(double uno, double due, double delta) {
		if (Math.abs(uno - due) <= delta)
			return true;
		else
			return false;
	}

	public static boolean myTestEquals(float uno, float due, float delta) {
		if (Math.abs(uno - due) <= delta)
			return true;
		else
			return false;
	}

	public static double truncateDoubleDecimals(double x1, int num) {
		double mult1 = Math.pow(10, num);
		int mult = (int) mult1;
		double y1;
		double y2;
		if (x1 > 0) {
			y1 = Math.floor(x1 * mult);
			y2 = y1 / (mult);
		} else {
			y1 = Math.ceil(x1 * mult);
			y2 = y1 / (mult);
		}
		return y2;
	}

	public static double roundDoubleDecimals(double x1, int decimalPlaces) {
		BigDecimal bd = new BigDecimal(x1);
		bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_DOWN);
		return bd.doubleValue();
	}

	/***
	 * Conversione da double a float
	 * 
	 * @param in1
	 * @return
	 */
	public static float toFloat(double in1) {
		return (float) in1;
	}

	public static float[] toFloat(double[] in1) {
		if (in1 == null)
			return null;
		float[] out1 = new float[in1.length];
		for (int i1 = 0; i1 < in1.length; i1++) {
			out1[i1] = (float) in1[i1];
		}
		return out1;
	}

	public static float[][] toFloat(double[][] in1) {
		if (in1 == null)
			return null;
		float[][] out1 = new float[in1.length][in1[0].length];
		for (int i1 = 0; i1 < in1.length; i1++) {
			for (int i2 = 0; i2 < in1[0].length; i2++) {
				out1[i1][i2] = (float) in1[i1][i2];
			}
		}
		return out1;
	}

	public static double toDouble(float in1) {
		return (double) in1;
	}

	public static double[] toDouble(float[] in1) {
		if (in1 == null)
			return null;
		double[] out1 = new double[in1.length];
		for (int i1 = 0; i1 < in1.length; i1++) {
			out1[i1] = (double) in1[i1];
		}
		return out1;
	}

	public static double[][] toDouble(float[][] in1) {
		if (in1 == null)
			return null;
		double[][] out1 = new double[in1.length][in1[0].length];
		for (int i1 = 0; i1 < in1.length; i1++) {
			for (int i2 = 0; i2 < in1[0].length; i2++) {
				out1[i1][i2] = (double) in1[i1][i2];
			}
		}
		return out1;
	}

	public static String[] decoderLimiti(String[][] tableLimiti, String vetName) {
		String[] result;
		if (tableLimiti == null)
			MyLog.waitHere("tableLimiti == null");
		if (vetName == null || vetName == "")
			MyLog.waitHere("vetName == none");
		// MyLog.logMatrix(tableLimiti, "tableLimiti");
		// IJ.log("ricerca= "+vetName);
		// MyLog.waitHere();
		for (int i1 = 0; i1 < tableLimiti.length; i1++) {
			if (tableLimiti[i1][0].equals(vetName)) {
				result = tableLimiti[i1];
				return result;
			}
		}
		return null;
	}

	public static double[] doubleLimiti(String[] in1) {
		if (in1 == null)
			return null;
		double[] result = new double[in1.length - 1];
		int i2 = 0;
		// MyLog.logVector(in1, "in1");
		// MyLog.waitHere();
		for (int i1 = 1; i1 < in1.length; i1++) {

			result[i2++] = ReadDicom.readDouble(in1[i1]);
		}
		return result;
	}

	/**
	 * Test sulle immagini ricevute in automatico da Sequenze. In caso di problemi
	 * viene dato un messaggio esplicativo e restituito false. Vengono eseguiti i
	 * seguenti controlli sul gruppo di righe (e quindi di immagini) passate al
	 * plugin. I controlli effettuati sono: verifica che le immagini siano acquisite
	 * tutte dalla stessa sequenza. Verifica che tutte le immagini siano acquisite
	 * dalla stessa bobina (oppure MISSIONG). Verifiche sugli echi delle immagini:
	 * Caso singola immagine p4, p6, p8: non ne devo passare pi� di una. Caso due
	 * immagini p3, p10 e p12: due sole immagini acquisite una di seguito all'altra
	 * con lo stesso eco. Caso di p5 e p10 e p11 quattro immagini, due gruppi di due
	 * echi diversi, acquisiti uno dopo l'altro il primo eco deve essere inferiore
	 * al secondo eco
	 * 
	 * @param vetRiga
	 * @param iw2ayvTable
	 * @param sel
	 * @return
	 */
	public static boolean checkImages_OLD(int[] vetRiga, String[][] iw2ayvTable, int sel, boolean debug) {
		String[] coil = new String[vetRiga.length];
		String[] descr = new String[vetRiga.length];
		String[] echo = new String[vetRiga.length];
		String[] serie = new String[vetRiga.length];
		String[] acq = new String[vetRiga.length];
		String[] ima = new String[vetRiga.length];
		String[] manufacturer = new String[vetRiga.length];
		ImagePlus imp1;
		String stampa = "#";
		for (int i1 = 0; i1 < vetRiga.length; i1++) {
			stampa += vetRiga[i1] + "#";
			String path1 = TableSequence.getPath(iw2ayvTable, vetRiga[i1]);
			imp1 = UtilAyv.openImageNoDisplay(path1, true);
			descr[i1] = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_SERIES_DESCRIPTION);
			coil[i1] = ReadDicom.getAllCoils(imp1);
			echo[i1] = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_ECHO_TIME);
			serie[i1] = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_SERIES_NUMBER);
			acq[i1] = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_ACQUISITION_NUMBER);
			ima[i1] = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_IMAGE_NUMBER);
			manufacturer[i1] = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_MANUFACTURER);
		}

		// Sicuramente le immagini da analizzare dovranno tutte essere acquisite
		// con la stessa sequenza
		String descr0 = descr[0];
		// Tutte le immagini da analizzare dovranno essere acquisite dalla
		// stessa bobina (oppure dalla MISSING)
		String coil0 = coil[0];

		for (int i1 = 1; i1 < vetRiga.length; i1++) {
			if (!descr0.equals(descr[i1])) {

				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "la descrizione delle sequenze ricevute � differente " + stampa + "   " + descr0 + "  "
						+ descr[i1], debug);
				return false;
			}
			if (!coil0.equals(coil[i1])) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "le immagini ricevute devono essere tutte acquisite \n" + "con la stessa bobina" + stampa
						+ "  " + coil0 + "  " + coil[i1], debug);
				return false;
			}
		}

		switch (sel) {
		case 1:
			// questo � il caso della singola immagine p4, p6, p8
			if (vetRiga.length != 1) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "errore sul numero parametri ricevuti da Sequenze \n" + "previsti= 1 reali= " + stampa,
						debug);
				return false;
			}
			break;

		case 2:
			// questo � il caso tipico di p3, p10 e p12
			// due sole immagini acquisite una di seguito all'altra.
			// hanno lo stesso eco
			if (vetRiga.length != 2) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "errore sul numero parametri ricevuti da Sequenze \n" + "previsti= 2 reali= " + stampa,
						debug);
				return false;
			}
			if (!echo[0].equals(echo[1])) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "immagini ricevute con tempi di echo differenti " + stampa, debug);
				return false;
			}
			if ((!ima[0].equals("1") || !ima[1].equals("1")) && (manufacturer[0].equals("SIEMENS"))) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "non soddisfatta la condizione ima1= 1 && ima2= 1 \n" + "" + stampa, debug);
				return false;
			}
			break;
		case 3:

			// questo � il caso tipico di p5 e p10 e p11
			// quattro immagini, due gruppi di due echi diversi, acquisiti uno
			// dopo l'altro
			// il primo eco deve essere inferiore al secondo eco
			if (vetRiga.length != 4) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "errore sul numero parametri ricevuti da Sequenze \n" + "previsti= 4 reali= " + stampa,
						debug);
				return false;
			}
			if (!echo[0].equals(echo[2]) || !echo[1].equals(echo[3])) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "i tempi di echo devono essere a due a due uguali \n" + "" + stampa + "\n \nechi= " + echo[0]
						+ " " + echo[1] + " " + echo[2] + " " + echo[3], debug);
				return false;
			}
			if (!(ReadDicom.readInt(echo[0]) < ReadDicom.readInt(echo[1]))) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "i tempi di echo delle prime immagini devono essere \n" + "inferiori a quelli delle seconde "
						+ stampa + "\n \nechi= " + echo[0] + " " + echo[1] + " " + echo[2] + " " + echo[3], debug);

				return false;
			}
			if (!(ima[0].equals("1") && ima[1].equals("2") && ima[2].equals("1") && ima[3].equals("2")
					|| !(ima[0].equals("1") && ima[1].equals("4") && ima[2].equals("3") && ima[3].equals("6"))
					|| !(ima[0].equals("2") && ima[1].equals("5") && ima[2].equals("1") && ima[3].equals("4"))
					|| !(ima[0].equals("3") && ima[1].equals("6") && ima[2].equals("2") && ima[3].equals("5")))) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "non soddisfatta la condizione ima1= 1, ima2= 2, ima3= 1, ima4=2 \n" + "" + stampa + "  ima="
						+ ima[0] + " " + ima[1] + " " + ima[2] + " " + ima[3], debug);
				return false;
			}
			break;

		case 4:

			// questo � il caso tipico di p3 e p10 e p12, nell'ipotetico caso di
			// quattro immagini
			if (vetRiga.length != 4) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "errore sul numero parametri ricevuti da Sequenze \n" + "previsti= 4 reali= " + stampa,
						debug);
				return false;
			}
			if (!echo[0].equals(echo[1]) || !echo[0].equals(echo[2]) || !echo[0].equals(echo[3])) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "immagini ricevute con tempi di echo differenti \n" + "" + stampa + "\n \nechi= " + echo[0]
						+ " " + echo[1] + " " + echo[2] + " " + echo[3], debug);
				return false;
			}
			if (!ima[0].equals("1") || !ima[1].equals("2") || !ima[2].equals("1") || !ima[3].equals("2")) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "non soddisfatta la condizione ima1= 1, ima2= 2, ima3= 1, ima4=2 \n" + "" + stampa + "  ima="
						+ ima[0] + " " + ima[1] + " " + ima[2] + " " + ima[3], debug);
				return false;
			}
			break;

		}

		return true;
	}

	/**
	 * Test sulle immagini ricevute in automatico da Sequenze. In caso di problemi
	 * viene dato un messaggio esplicativo e restituito false. Vengono eseguiti i
	 * seguenti controlli sul gruppo di righe (e quindi di immagini) passate al
	 * plugin. I controlli effettuati sono: verifica che le immagini siano acquisite
	 * tutte dalla stessa sequenza. Verifica che tutte le immagini siano acquisite
	 * dalla stessa bobina (oppure MISSIONG). Verifiche sugli echi delle immagini:
	 * Caso singola immagine p4, p6, p8: non ne devo passare pi� di una. Caso due
	 * immagini p3, p10 e p12: due sole immagini acquisite una di seguito all'altra
	 * con lo stesso eco. Caso di p5 e p10 e p11 quattro immagini, due gruppi di due
	 * echi diversi, acquisiti uno dopo l'altro il primo eco deve essere inferiore
	 * al secondo eco
	 * 
	 * @param vetRiga
	 * @param iw2ayvTable
	 * @param sel
	 * @return
	 */
	public static boolean checkImages0(int[] vetRiga, String[][] iw2ayvTable, int sel, boolean debug) {

		// IJ.log("checkImages riceve vetRiga= ");
		// MyLog.logVector(vetRiga, "vetRiga");

		String[] coil = new String[vetRiga.length];
		String[] position = new String[vetRiga.length];
		String[] location = new String[vetRiga.length];
		String[] descr = new String[vetRiga.length];
		String[] echo = new String[vetRiga.length];
		String[] serie = new String[vetRiga.length];
		String[] acq = new String[vetRiga.length];
		String[] ima = new String[vetRiga.length];
		String[] manufacturer = new String[vetRiga.length];
		ImagePlus imp1;
		String stampa = "#";
		for (int i1 = 0; i1 < vetRiga.length; i1++) {
			stampa += vetRiga[i1] + "#";
			String path1 = TableSequence.getPath(iw2ayvTable, vetRiga[i1]);
			imp1 = UtilAyv.openImageNoDisplay(path1, true);
			// IJ.log("checkImages apre path1= " + path1);
			descr[i1] = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_SERIES_DESCRIPTION);
			position[i1] = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_IMAGE_POSITION);
			location[i1] = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_SLICE_LOCATION);
			coil[i1] = ReadDicom.getAllCoils(imp1);
			echo[i1] = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_ECHO_TIME);
			serie[i1] = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_SERIES_NUMBER);
			acq[i1] = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_ACQUISITION_NUMBER);
			ima[i1] = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_IMAGE_NUMBER);
			manufacturer[i1] = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_MANUFACTURER);
		}

		// Sicuramente le immagini da analizzare dovranno tutte essere acquisite
		// con la stessa sequenza
		String descr0 = descr[0];
		// Tutte le immagini da analizzare dovranno essere acquisite dalla
		// stessa bobina (oppure dalla MISSING)
		String coil0 = coil[0];
		String position0 = position[0];
		String location0 = location[0];

		for (int i1 = 1; i1 < vetRiga.length; i1++) {
			if (!descr0.equals(descr[i1])) {

				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "la descrizione delle sequenze ricevute e'differente " + stampa + "  " + descr0 + "   <>    "
						+ descr[i1], debug);
				return false;
			}
			if (!coil0.equals(coil[i1])) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "le immagini ricevute devono essere tutte acquisite \n" + "con la stessa bobina" + stampa
						+ "  " + coil0 + "   <>   " + coil[i1], debug);
				return false;
			}

			if (!position0.equals(position[i1])) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "le immagini ricevute devono essere tutte acquisite \n" + "con la stessa POSITION" + stampa
						+ "  " + position0 + "    <>    " + position[i1], debug);
				return false;
			}

			if (!location0.equals(location[i1])) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "le immagini ricevute devono essere tutte acquisite \n" + "con la stessa LOCATION" + stampa
						+ "  " + location0 + "   <>   " + location[i1], debug);
				return false;
			}

		}

		switch (sel) {
		case 1:
			// questo e' il caso della singola immagine p4, p6, p8
			if (vetRiga.length != 1) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "errore sul numero parametri ricevuti da Sequenze \n" + "previsti= 1 reali= " + stampa,
						debug);
				return false;
			}
			break;

		case 2:
			// questo e' il caso tipico di p3, p10 e p12
			// due sole immagini acquisite una di seguito all'altra.
			// hanno lo stesso eco
			if (vetRiga.length != 2) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "errore sul numero parametri ricevuti da Sequenze \n" + "previsti= 2 reali= " + stampa,
						debug);
				return false;
			}
			if (!echo[0].equals(echo[1])) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "immagini ricevute con tempi di echo differenti " + stampa, debug);
				return false;
			}
			if ((!ima[0].equals(ima[1])) && (manufacturer[0].equals("SIEMENS"))) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "non soddisfatta la condizione numero ima1= numero ima2 \n" + "" + stampa, debug);
				return false;
			}
			break;
		case 3:

			// questo e' il caso tipico di p5 e p10 e p11
			// quattro immagini, due gruppi di due echi diversi, acquisiti uno
			// dopo l'altro
			// il primo eco deve essere inferiore al secondo eco
			if (vetRiga.length != 4) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "errore sul numero parametri ricevuti da Sequenze \n" + "previsti= 4 reali= " + stampa,
						debug);
				return false;
			}
			if (!echo[0].equals(echo[2]) || !echo[1].equals(echo[3])) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "i tempi di echo devono essere a due a due uguali \n" + "" + stampa + "\n \nechi= " + echo[0]
						+ " " + echo[1] + " " + echo[2] + " " + echo[3], debug);
				return false;
			}
			if (!(ReadDicom.readInt(echo[0]) < ReadDicom.readInt(echo[1]))) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "i tempi di echo delle prime immagini devono essere \n" + "inferiori a quelli delle seconde "
						+ stampa + "\n \nechi= " + echo[0] + " " + echo[1] + " " + echo[2] + " " + echo[3], debug);

				return false;
			}
			if (!(ima[0].equals("1") && ima[1].equals("2") && ima[2].equals("1") && ima[3].equals("2")
					|| !(ima[0].equals("1") && ima[1].equals("4") && ima[2].equals("3") && ima[3].equals("6"))
					|| !(ima[0].equals("2") && ima[1].equals("5") && ima[2].equals("1") && ima[3].equals("4"))
					|| !(ima[0].equals("3") && ima[1].equals("6") && ima[2].equals("2") && ima[3].equals("5")))) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "non soddisfatta la condizione ima1= 1, ima2= 2, ima3= 1, ima4=2 \n" + "" + stampa + "  ima="
						+ ima[0] + " " + ima[1] + " " + ima[2] + " " + ima[3], debug);
				return false;
			}
			break;

		case 4:

			// questo e' il caso tipico di p3 e p10 e p12, nell'ipotetico caso di
			// quattro immagini
			if (vetRiga.length != 4) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "errore sul numero parametri ricevuti da Sequenze \n" + "previsti= 4 reali= " + stampa,
						debug);
				return false;
			}
			if (!echo[0].equals(echo[1]) || !echo[0].equals(echo[2]) || !echo[0].equals(echo[3])) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "immagini ricevute con tempi di echo differenti \n" + "" + stampa + "\n \nechi= " + echo[0]
						+ " " + echo[1] + " " + echo[2] + " " + echo[3], debug);
				return false;
			}
			if (!ima[0].equals("1") || !ima[1].equals("2") || !ima[2].equals("1") || !ima[3].equals("2")) {
				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "non soddisfatta la condizione ima1= 1, ima2= 2, ima3= 1, ima4=2 \n" + "" + stampa + "  ima="
						+ ima[0] + " " + ima[1] + " " + ima[2] + " " + ima[3], debug);
				return false;
			}
			break;

		}

		return true;
	}

	/**
	 * checkImages2 effettua il controllo di coerenza delle immagini
	 *
	 * @param path1 path prima immagine
	 * @param path2 path seconda immagine
	 * @param debug
	 * @return
	 */
	public static boolean checkImages2(String path1, String path2, boolean debug) {

		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, true);
		ImagePlus imp2 = UtilAyv.openImageNoDisplay(path2, true);
		boolean discosta = false;

//		String series1 = DicomTools.getTag(imp1, MyConst.DICOM_SERIES_DESCRIPTION);
		String series1 = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_SERIES_DESCRIPTION);

//		String position1 = DicomTools.getTag(imp1, MyConst.DICOM_IMAGE_POSITION);
		String position1 = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_IMAGE_POSITION);
//		MyLog.waitHere("series1= "+series1+"\nECCOLA CAZZO position1= " + position1);
		String position1x = ReadDicom.readSubstring(position1, 1);
		double pos1x = ReadDicom.readDouble(position1x);
		String position1y = ReadDicom.readSubstring(position1, 2);
		double pos1y = ReadDicom.readDouble(position1y);
		String position1z = ReadDicom.readSubstring(position1, 3);
		double pos1z = ReadDicom.readDouble(position1z);

//		String location1 = DicomTools.getTag(imp1, MyConst.DICOM_SLICE_LOCATION);
		String location1 = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_SLICE_LOCATION);
		double loc1 = ReadDicom.readDouble(location1);

//		String echo1a = DicomTools.getTag(imp1, MyConst.DICOM_ECHO_TIME);
		String echo1a = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_ECHO_TIME, MyConst.DICOM_EFFECTIVE_ECHO_TIME);
		int echo1 = ReadDicom.readInt(echo1a);

//		String series2 = DicomTools.getTag(imp2, MyConst.DICOM_SERIES_DESCRIPTION);
//		String position2 = DicomTools.getTag(imp2, MyConst.DICOM_IMAGE_POSITION);
		String series2 = ReadDicom.readDicomParameter(imp2, MyConst.DICOM_SERIES_DESCRIPTION);
		String position2 = ReadDicom.readDicomParameter(imp2, MyConst.DICOM_IMAGE_POSITION);
		String position2x = ReadDicom.readSubstring(position2, 1);
		double pos2x = ReadDicom.readDouble(position2x);
		String position2y = ReadDicom.readSubstring(position2, 2);
		double pos2y = ReadDicom.readDouble(position2y);
		String position2z = ReadDicom.readSubstring(position2, 3);
		double pos2z = ReadDicom.readDouble(position2z);

//		String location2 = DicomTools.getTag(imp2, MyConst.DICOM_SLICE_LOCATION);
		String location2 = ReadDicom.readDicomParameter(imp2, MyConst.DICOM_SLICE_LOCATION);
		double loc2 = ReadDicom.readDouble(location2);

//		String echo2a = DicomTools.getTag(imp2, MyConst.DICOM_ECHO_TIME);
		String echo2a = ReadDicom.readDicomParameter(imp2, MyConst.DICOM_ECHO_TIME, MyConst.DICOM_EFFECTIVE_ECHO_TIME);
		int echo2 = ReadDicom.readInt(echo2a);

		if (!series1.equals(series2)) {
			MyLog.waitHere("ATTENZIONE: stai per analizzare immagini di serie diverse: /n" + series1
					+ " differerisce da /n" + series2);
		}

		double tolerance = 0.01; // metto un centesimo di millimetro
		if (myOutTolerance(pos1x, pos2x, tolerance) || myOutTolerance(pos1y, pos2y, tolerance)
				|| myOutTolerance(pos1z, pos2z, tolerance)) {
			discosta = true;
			MyLog.waitHere("ATTENZIONE: stai per analizzare immagini con differenti posizioni: /n" + position1
					+ " differisce da /n" + position2);
		}
		if (myOutTolerance(loc1, loc2, tolerance)) {
			discosta = true;
			MyLog.waitHere("ATTENZIONE: stai per analizzare slices con differente location: /n" + location1
					+ " differisce da /n" + location2);
		}
		if (echo1 != echo2) {
			discosta = true;
			MyLog.waitHere("ATTENZIONE: stai per analizzare slices con differente echo time: /n" + echo1
					+ " differisce da /n" + echo2);
		}

		String coil1 = ReadDicom.getAllCoils(imp1);
		String coil2 = ReadDicom.getAllCoils(imp2);
		if (!coil1.equals(coil2)) {
			discosta = true;
			MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
					+ "la descrizione delle sequenze ricevute e'differente per le bobine utilizzate: " + coil1
					+ "   <>    " + coil2, debug);
			return false;
		}

		if (discosta) {
			int res = ButtonMessages.ModalMsg(
					"ATTENZIONE se si preme CONTINUA ELABORAZIONE i risultati possono essere fuori specifiche!!",
					"CONTINUA ELABORAZIONE", "SALTA A IMMAGINE SUCCESSIVA");

			if (res == 1)
				return false;
		}

		return true;
	}

	/**
	 * checkImages2alfa effettua il controllo di coerenza delle immagini
	 *
	 * @param path1 path prima immagine
	 * @param path2 path seconda immagine
	 * @param debug
	 * @return
	 */
	public static boolean checkImages2alfa(String path1, String path2, boolean debug) {

		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, true);
		ImagePlus imp2 = UtilAyv.openImageNoDisplay(path2, true);

		String[] vetString = { MyConst.DICOM_SERIES_DESCRIPTION, MyConst.DICOM_IMAGE_POSITION,
				MyConst.DICOM_SLICE_LOCATION, MyConst.DICOM_ECHO_TIME };

		for (int i1 = 0; i1 < vetString.length; i1++) {
			String item1 = ReadDicom.readDicomParameter(imp1, vetString[i1]);
			String item2 = ReadDicom.readDicomParameter(imp2, vetString[i1]);

			if (!item1.equals(item2)) {

				MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "la descrizione delle sequenze ricevute e'differente " + vetString[i1] + "  " + item1
						+ "   <>    " + item2, debug);
				int res = ButtonMessages.ModalMsg(
						"ATTENZIONE se si preme CONTINUA ELABORAZIONE i risultati possono essere fuori specifiche!!",
						"CONTINUA ELABORAZIONE", "SALTA A IMMAGINE SUCCESSIVA");

				if (res == 1)
					return false;
			}
		}

		String coil1 = ReadDicom.getAllCoils(imp1);
		String coil2 = ReadDicom.getAllCoils(imp2);
		if (!coil1.equals(coil2)) {

			MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n"
					+ "la descrizione delle sequenze ricevute e'differente per le bobine utilizzate: " + coil1
					+ "   <>    " + coil2, debug);
			return false;
		}

		return true;
	}

	/**
	 * checkImages4 effettua il controllo di coerenza delle immagini
	 *
	 * @param path1  // prima acquisizione primo eco (commento 2025)
	 * @param path2  // seconda acquisizione primo eco (commento 2025)
	 * @param path11 // prima acquisizione secondo eco (commento 2025)
	 * @param path21 // seconda acquisizione secondo eco (commento 2025)
	 * @param debug	 // abilita lo stop in caso di messaggio
	 * @return
	 */
	public static boolean checkImages4(String path1, String path2, String path11, String path21, boolean debug) {

		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, true);		// prima acquisizione primo eco (commento 2025)
		ImagePlus imp2 = UtilAyv.openImageNoDisplay(path2, true);		// seconda acquisizione primo eco (commento 2025)
		ImagePlus imp11 = UtilAyv.openImageNoDisplay(path11, true);		// prima acquisizione secondo eco (commento 2025)
		ImagePlus imp21 = UtilAyv.openImageNoDisplay(path21, true);		// seconda acquisizione secondo eco (commento 2025)

		String[] vetString = { MyConst.DICOM_SERIES_DESCRIPTION, MyConst.DICOM_IMAGE_POSITION,
				MyConst.DICOM_SLICE_LOCATION };

		// Effettuo il controllo confrontando DESCRIPTION, POSITION e LOCATION per tutte
		// e quattro le immagini di input

		for (int i1 = 0; i1 < vetString.length; i1++) {
			String item1 = ReadDicom.readDicomParameter(imp1, vetString[i1]);
			String item2 = ReadDicom.readDicomParameter(imp2, vetString[i1]);
			String item11 = ReadDicom.readDicomParameter(imp11, vetString[i1]);
			String item21 = ReadDicom.readDicomParameter(imp21, vetString[i1]);
			if (!(item1.equals(item2) && item1.equals(item11) && item1.equals(item21))) {
				MyLog.waitThere("checkImages4 Problema sui dati ricevuti in AUTOMATICO: \n"
						+ "la descrizione delle sequenze ricevute e'differente " + vetString[i1] + "  " + item1
						+ "   <>    " + item2 + "   <>    " + item11 + "   <>    " + item21, debug);
				return false;
			}
		}

		String eco1 = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_ECHO_TIME, MyConst.DICOM_EFFECTIVE_ECHO_TIME);
		String eco2 = ReadDicom.readDicomParameter(imp2, MyConst.DICOM_ECHO_TIME, MyConst.DICOM_EFFECTIVE_ECHO_TIME);
		String eco11 = ReadDicom.readDicomParameter(imp11, MyConst.DICOM_ECHO_TIME, MyConst.DICOM_EFFECTIVE_ECHO_TIME);
		String eco21 = ReadDicom.readDicomParameter(imp21, MyConst.DICOM_ECHO_TIME, MyConst.DICOM_EFFECTIVE_ECHO_TIME);
		// Effettuo il controllo sugli echi delle quattro immagini, essi devono essere
		// innanzitutto leggibili
		if (eco1 == null || eco2 == null || eco11 == null || eco21 == null) {
			MyLog.waitThere(
					"checkImages4 Problema lettura ECO TIME da almeno una immagine: \n"
							+ "i tempi di eco sono irregolari  " + eco1 + "  " + eco2 + "  " + eco11 + "  " + eco21,
					debug);

			return false;
		}
		// Effettuo il controllo sugli echi delle quattro immagini, essi devono essere
		// eco1=eco2 e poi eco 11=eco21
		// inoltre eco1<eco2 MA QUI E'SBAGLIATO, CHE XXXZZO HO SCRITTO ????????
		//if (!(eco1.equals(eco2)) && (eco11.equals(eco21)) || (!(Double.parseDouble(eco1) < Double.parseDouble(eco2)))) {
		// corretto il 160525
			if (!(eco1.equals(eco2)) && (eco11.equals(eco21)) || (!(Double.parseDouble(eco1) < Double.parseDouble(eco11)))) {

			MyLog.waitThere("checkImages4 Problema sui dati ricevuti in AUTOMATICO: \n"
					+ "i tempi di eco sono irregolari  " + eco1 + "  " + eco2 + "  " + eco11 + "  " + eco21 + " >>> "
					+ Double.parseDouble(eco1) + " >>> " + Double.parseDouble(eco11), debug);
			return false;
		}

		// if (!(Integer.parseInt(eco1) < Integer.parseInt(eco11))) {
		//
		// MyLog.waitThere("Problema sui dati ricevuti in AUTOMATICO: \n" + "i
		// tempi di eco sono irregolari 222222",
		// debug);
		// return false;
		// }

		String coil1 = ReadDicom.getAllCoils(imp1);
		String coil2 = ReadDicom.getAllCoils(imp2);
		String coil11 = ReadDicom.getAllCoils(imp11);
		String coil21 = ReadDicom.getAllCoils(imp21);
		// Effettuo il controllo che le immaginio siano tutte prese con la stessa bobina
		if (!(coil1.equals(coil2) || coil1.equals(coil11) || coil1.equals(coil21)))

		{
			MyLog.waitThere("checkImages4 Problema sui dati ricevuti in AUTOMATICO: \n"
					+ "la descrizione delle sequenze ricevute e'differente per le bobine utilizzate: " + coil1
					+ "   <>    " + coil2, debug);
			return false;
		}

		return true;

	}


	/**
	 * Ricerca posizione del fondo
	 * 
	 * @param imp1
	 * @param diamBkg
	 * @param guard
	 * @param info1
	 * @param autoCalled
	 * @param step
	 * @param demo
	 * @param test
	 * @param fast
	 * @param irraggiungibile
	 * @return
	 */
	public static double[] positionSearch15(ImagePlus imp1, double[] circleData, double xBkg, double yBkg,
			double diamBkg, int guard, int select, String info1, boolean circle, boolean autoCalled, boolean step,
			boolean demo, boolean test, boolean fast, boolean irraggiungibile) {

		boolean debug = true;

		Overlay over2 = new Overlay();
		over2.setStrokeColor(Color.red);
		imp1.deleteRoi();

		ImagePlus imp2 = imp1.duplicate();

		imp2.setOverlay(over2);
		if (demo) {
			IJ.setMinAndMax(imp2, 10, 50);
		}

		int height = imp1.getHeight();
		int xCenterCircle = 0;
		int yCenterCircle = 0;
		int diamCircle = 0;

		if (circleData != null) {
			xCenterCircle = (int) circleData[2];
			yCenterCircle = (int) circleData[3];
			diamCircle = (int) circleData[9];

			if (demo) {
				UtilAyv.showImageMaximized(imp2);
				ImageUtils.imageToFront(imp2);
				imp2.setRoi(new OvalRoi(xCenterCircle - diamCircle / 2, yCenterCircle - diamCircle / 2, diamCircle,
						diamCircle));
				imp2.getRoi().setStrokeColor(Color.red);
				over2.addElement(imp2.getRoi());
				MyLog.waitHere("Ricerca della posizione per calcolo background", debug);
			}
		}

		int a = 0;
		if (irraggiungibile && demo) {
			MyLog.waitHere("impostato irraggiungibile");

			a = 1;
		}
		double px = 0;
		double py = 0;
		int incr = 0;
		boolean pieno = false;
		double xcentBkg = 0;
		double ycentBkg = 0;

		do {
			if (select == 1) {
				px = xBkg;
				py = yBkg + incr;
			} else if (select == 2) {
				px = xBkg + incr;
				py = yBkg + incr;
			} else if (select == 3) {
				px = xBkg - incr;
				py = yBkg - incr;
			}
			xcentBkg = px + diamBkg / 2;
			ycentBkg = py + diamBkg / 2;

			double critic_0 = 9999;
			if (circleData != null) {
				critic_0 = UtilAyv.criticalDistanceCalculation((int) xcentBkg, (int) ycentBkg, (int) diamBkg / 2,
						xCenterCircle, yCenterCircle, diamCircle / 2);
			}

			if (critic_0 >= guard) {
				pieno = verifyBackgroundRoiMean(imp2, (int) xcentBkg, (int) ycentBkg, (int) diamBkg, circle, test,
						demo);
				if (circle)
					imp2.setRoi(new OvalRoi(px, py, diamBkg, diamBkg));
				else
					imp2.setRoi((int) px, (int) py, (int) diamBkg, (int) diamBkg);
				imp2.getRoi().setStrokeColor(Color.yellow);
				over2.addElement(imp2.getRoi());
				IJ.wait(5);
			}
			incr++;
		} while ((pieno || a > 0) && (ycentBkg + diamBkg < height));

		if (demo) {
			MyLog.waitHere("Evidenziata la posizione per il calcolo del fondo", debug);
		}
		imp2.close();
		double[] out1 = new double[3];
		out1[0] = xcentBkg;
		out1[1] = ycentBkg;
		out1[2] = diamBkg;

		return out1;
	}

	/**
	 * Ricerca posizione del fondo
	 * 
	 * @param imp1
	 * @param diamBkg
	 * @param guard
	 * @param info1
	 * @param autoCalled
	 * @param step
	 * @param demo
	 * @param test
	 * @param fast
	 * @param irraggiungibile
	 * @return
	 */
	public static double[] positionSearch15(ImagePlus imp1, double[] circleData, double xBkg, double yBkg,
			double diamBkg, int guard, int select, String info1, boolean circle, int mode, boolean irraggiungibile) {

		boolean autoCalled = false;
		boolean step = false;
		boolean demo = false;
		boolean test = false;
		// boolean fast
		boolean debug = true;

		switch (mode) {
		case 1:
			break;
		}

		Overlay over2 = new Overlay();
		over2.setStrokeColor(Color.red);
		imp1.deleteRoi();

		ImagePlus imp2 = imp1.duplicate();

		imp2.setOverlay(over2);
		if (demo) {
			IJ.setMinAndMax(imp2, 10, 50);
		}

		int height = imp1.getHeight();
		int xCenterCircle = 0;
		int yCenterCircle = 0;
		int diamCircle = 0;

		if (circleData != null) {
			xCenterCircle = (int) circleData[2];
			yCenterCircle = (int) circleData[3];
			diamCircle = (int) circleData[9];

			if (demo) {
				UtilAyv.showImageMaximized(imp2);
				ImageUtils.imageToFront(imp2);
				imp2.setRoi(new OvalRoi(xCenterCircle - diamCircle / 2, yCenterCircle - diamCircle / 2, diamCircle,
						diamCircle));
				imp2.getRoi().setStrokeColor(Color.red);
				over2.addElement(imp2.getRoi());
				MyLog.waitHere("Ricerca della posizione per calcolo background", debug);
			}
		}

		int a = 0;
		if (irraggiungibile && demo) {
			MyLog.waitHere("impostato irraggiungibile");

			a = 1;
		}
		double px = 0;
		double py = 0;
		int incr = 0;
		boolean pieno = false;
		double xcentBkg = 0;
		double ycentBkg = 0;

		do {
			switch (select) {
			case 1:
				px = xBkg;
				py = yBkg + incr;
				break;
			case 2:
				px = xBkg + incr;
				py = yBkg + incr;
				break;
			case 3:
				px = xBkg - incr;
				py = yBkg - incr;
				break;
			}
			xcentBkg = px + diamBkg / 2;
			ycentBkg = py + diamBkg / 2;

			double critic_0 = 9999;
			if (circleData != null) {
				critic_0 = UtilAyv.criticalDistanceCalculation((int) xcentBkg, (int) ycentBkg, (int) diamBkg / 2,
						xCenterCircle, yCenterCircle, diamCircle / 2);
			}

			if (critic_0 >= guard) {
				pieno = verifyBackgroundRoiMean(imp2, (int) xcentBkg, (int) ycentBkg, (int) diamBkg, circle, test,
						demo);
				if (circle)
					imp2.setRoi(new OvalRoi(px, py, diamBkg, diamBkg));
				else
					imp2.setRoi((int) px, (int) py, (int) diamBkg, (int) diamBkg);
				imp2.getRoi().setStrokeColor(Color.yellow);
				over2.addElement(imp2.getRoi());
				IJ.wait(5);
			}
			incr++;
		} while ((pieno || a > 0) && (ycentBkg + diamBkg < height));

		if (demo) {
			MyLog.waitHere("Evidenziata la posizione per il calcolo del fondo", debug);
		}
		imp2.close();
		double[] out1 = new double[3];
		out1[0] = xcentBkg;
		out1[1] = ycentBkg;
		out1[2] = diamBkg;

		return out1;
	}

	/**
	 * Calcolo delle distanza minima tra due circonferenze esterne
	 * 
	 * @param x1 coordinata x cerchio 1
	 * @param y1 coordinata y cerchio 1
	 * @param r1 raggio cerchio 1
	 * @param x2 coordinata x cerchio 2
	 * @param y2 coordinata y cerchio 2
	 * @param r2 raggio cerchio 2
	 * @return distanza minima tra i cechi
	 */
	public static int criticalDistanceCalculation(int x1, int y1, int r1, int x2, int y2, int r2) {

		double dCentri = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		double critical = dCentri - (r1 + r2);
		return (int) Math.round(critical);
	}

	public static boolean verifyBackgroundRoiMean(ImagePlus imp1, int xRoi, int yRoi, int diamRoi, boolean circle,
			boolean test, boolean demo) {

		if (circle)
			imp1.setRoi(new OvalRoi(xRoi - diamRoi / 2, yRoi - diamRoi / 2, diamRoi, diamRoi));
		else
			imp1.setRoi(xRoi - diamRoi / 2, yRoi - diamRoi / 2, diamRoi, diamRoi);
		ImageStatistics stat1 = imp1.getStatistics();
		double mean1 = stat1.mean;
		if (mean1 > 0)
			return false;
		return true;
	}

	public static int[] reverseVector(int[] vetIn) {
		int[] vetOut = new int[vetIn.length];
		for (int i1 = 0; i1 < vetIn.length; i1++) {
			vetOut[i1] = vetIn[vetIn.length - i1 - 1];
		}
		return vetOut;
	}

	public static float[] reverseVector(float[] vetIn) {
		float[] vetOut = new float[vetIn.length];
		for (int i1 = 0; i1 < vetIn.length; i1++) {
			vetOut[i1] = vetIn[vetIn.length - i1 - 1];
		}
		return vetOut;
	}

	public static double[] reverseVector(double[] vetIn) {
		double[] vetOut = new double[vetIn.length];
		for (int i1 = 0; i1 < vetIn.length; i1++) {
			vetOut[i1] = vetIn[vetIn.length - i1 - 1];
		}
		return vetOut;
	}

	public static String[] reverseVector(String[] vetIn) {
		String[] vetOut = new String[vetIn.length];
		for (int i1 = 0; i1 < vetIn.length; i1++) {
			vetOut[i1] = vetIn[vetIn.length - i1 - 1];
		}
		return vetOut;
	}

	/***
	 * Ordina vet1 con l'algoritmo minsort
	 * 
	 * @param vet1
	 */
	public static void minsort(int[] vet1) {

		for (int i1 = 0; i1 < vet1.length; i1++) {
			for (int i2 = i1 + 1; i2 < vet1.length; i2++) {
				if (vet1[i2] < vet1[i1]) {
					int aux1 = vet1[i1];
					vet1[i1] = vet1[i2];
					vet1[i2] = aux1;
				}
			}
		}
	}

	/***
	 * Ordina vet1 con l'algoritmo minsort, e sposta gli elementi di vet2 in
	 * sincronia (non saprei come spiegarmi in altro modo)
	 * 
	 * @param vet1
	 */
	public static void minsort(int[] vet1, int[] vet2) {

		int aux1 = 0;
		for (int i1 = 0; i1 < vet1.length; i1++) {
			for (int i2 = i1 + 1; i2 < vet1.length; i2++) {
				if (vet1[i2] < vet1[i1]) {
					aux1 = vet1[i1];
					vet1[i1] = vet1[i2];
					vet1[i2] = aux1;
					aux1 = vet2[i1];
					vet2[i1] = vet2[i2];
					vet2[i2] = aux1;
				}
			}
		}
	}

	public static void minsort(double[] vet1, double[] vet2, double[] vet3) {

		double aux1 = 0;
		for (int i1 = 0; i1 < vet1.length; i1++) {
			for (int i2 = i1 + 1; i2 < vet1.length; i2++) {
				if (vet1[i2] < vet1[i1]) {
					aux1 = vet1[i1];
					vet1[i1] = vet1[i2];
					vet1[i2] = aux1;
					aux1 = vet2[i1];
					vet2[i1] = vet2[i2];
					vet2[i2] = aux1;
					aux1 = vet3[i1];
					vet3[i1] = vet3[i2];
					vet3[i2] = aux1;

				}
			}
		}
	}

	public static void minsort(double[] vet1, double[] vet2) {

		double aux1 = 0;
		for (int i1 = 0; i1 < vet1.length; i1++) {
			for (int i2 = i1 + 1; i2 < vet1.length; i2++) {
				if (vet1[i2] < vet1[i1]) {
					aux1 = vet1[i1];
					vet1[i1] = vet1[i2];
					vet1[i2] = aux1;
					aux1 = vet2[i1];
					vet2[i1] = vet2[i2];
					vet2[i2] = aux1;
				}
			}
		}
	}

	public static void minsort(double[] vet1) {

		double aux1 = 0;
		for (int i1 = 0; i1 < vet1.length; i1++) {
			for (int i2 = i1 + 1; i2 < vet1.length; i2++) {
				if (vet1[i2] < vet1[i1]) {
					aux1 = vet1[i1];
					vet1[i1] = vet1[i2];
					vet1[i2] = aux1;
				}
			}
		}
	}

	/**
	 * Effettua lo shift circolare degli elementi di un array
	 * 
	 * @param array
	 * @param stepSize
	 * @return
	 */
	public static int[] shiftArray(int[] array, int stepSize) {
		if (stepSize == 0)
			return array;
		int shiftStep = (Math.abs(stepSize) > array.length ? stepSize % array.length : stepSize);
		MyLog.waitHere("shiftStep= " + shiftStep);
		int[] array2 = new int[array.length];
		boolean safe = false;
		for (int i1 = 0; i1 < array.length; i1++) {
			safe = (i1 + 1) - shiftStep >= 0;
			if (safe) {
				array2[i1] = array[i1 - shiftStep];
			} else {
				array2[i1] = array[array.length - shiftStep + i1];
			}
		}
		return array2;
	}

	/**
	 * messaggio mancanza test2.jar
	 * 
	 */
	public static void noTest2() {

		ButtonMessages.ModelessMsg("Per questa funzione bisogna installare test2.jar (albertoduina@virgilio.it)",
				"CONTINUA");
	}

	public static String dirSeparator(String path1) {
		String path2 = "";
		if (path1.lastIndexOf(File.separator) > 0)
			path2 = path1.substring(0, path1.lastIndexOf(File.separator));
		else
			return path1;
		return path2;
	}

	public static boolean jarCount(String nome1) {
		List<File> lista1 = listJars2(new File(ij.Menus.getPlugInsPath()));
		String[] list = new String[lista1.size()];
		int j1 = 0;
		for (File file : lista1) {
			list[j1++] = file.getName();
		}
		int count = 0;
		for (File file : lista1) {
			String str = file.getName();
			if (str.startsWith(nome1)) {
				count++;
			}
		}
		String msg = "";
		if (count < 1) {
			IJ.error("ATTENZIONE, manca il file " + nome1 + "xxx.jar");
		}
		if (count > 1) {
			for (File file : lista1) {
				String str = file.getName();
				if (str.startsWith(nome1)) {
					msg = msg + "\n" + file.getPath();
				}
			}
			IJ.error("ATTENZIONE, si vedono versioni multiple del file " + nome1 + "xxx.jar" + msg);
		}
		if (count == 1)
			return true;
		else
			return false;
	}

	public static List<File> listJars2(File startingDir) {
		List<File> result = new ArrayList<File>();
		File[] filesAndDirs = startingDir.listFiles();
		if (filesAndDirs == null)
			return null;
		List<File> filesDirs = Arrays.asList(filesAndDirs);
		for (File file : filesDirs) {
			if (!file.isFile()) {
				List<File> deeperList = listJars2(file);
				result.addAll(deeperList);
			} else {
				if (file.getName().endsWith(".jar")) {
					result.add(file);
				}
			}
		}
		return result;
	}

	public static boolean myOutTolerance(double in1, double in2, double tolerance) {

		double diff = Math.abs(in1 - in2);
		if (diff > tolerance)
			return true;
		else
			return false;
	}

} // UtilAyv
