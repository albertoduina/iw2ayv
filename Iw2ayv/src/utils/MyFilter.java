package utils;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Line;
import ij.gui.Plot;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.gui.WaitForUserDialog;
import ij.process.FloatPolygon;
import ij.process.ImageProcessor;

public class MyFilter {

	public static double[] maxPosition1x1(ImagePlus imp1) {

		// ImageStatistics stat1 = imp1.getStatistics();
		// double max1 = stat1.max;
		ImageProcessor ip1 = imp1.getProcessor();
		short[] pixels = (short[]) ip1.getPixels();
		double max2 = Double.NEGATIVE_INFINITY;
		int maxPos = -9999;
		for (int i1 = 0; i1 < pixels.length; i1++) {
			if (pixels[i1] > max2) {
				max2 = pixels[i1];
				maxPos = i1;
			}
		}
		int colonna = maxPos / imp1.getWidth();
		int riga = maxPos - (colonna * imp1.getWidth());
		double[] out = new double[3];
		out[0] = riga;
		out[1] = colonna;
		out[2] = max2;

		return out;
	}

	/**
	 * Ricerca posizione del massimo con una roi 3x3
	 * 
	 * @param imp1
	 * @return
	 */
	public static double[] maxPosition3x3(ImagePlus imp1) {

		// double startNanoTime = System.nanoTime();
		int width = imp1.getWidth();
		int height = imp1.getHeight();
		long sum9 = 0;
		double mean9 = 0;
		double max9 = 0;
		int xmax9 = 0;
		int ymax9 = 0;
		int offset = 0;
		ImageProcessor ip1 = imp1.getProcessor();
		short[] pixels1 = (short[]) ip1.getPixels();
		for (int i1 = 1; i1 < width - 1; i1++) {
			for (int i2 = 1; i2 < height - 1; i2++) {
				sum9 = 0;
				offset = (i1 - 1) * width + i2;
				for (int i3 = -1; i3 < 1; i3++) {
					sum9 = sum9 + (pixels1[offset + i3]);
				}
				offset = (i1) * width + i2;
				for (int i3 = -1; i3 < 1; i3++) {
					sum9 = sum9 + (pixels1[offset + i3]);
				}
				offset = (i1 + 1) * width + i2;
				for (int i3 = -1; i3 < 1; i3++) {
					sum9 = sum9 + (pixels1[offset + i3]);
				}
				mean9 = sum9 / 9.0;
				if (mean9 > max9) {
					max9 = mean9;
					xmax9 = i2;
					ymax9 = i1;
				}
			}
		}
		double[] out = new double[3];
		out[0] = (double) xmax9;
		out[1] = (double) ymax9;
		out[2] = max9;
		// IJ.log("maxPosition3x3 "
		// + IJ.d2s(((System.nanoTime() - startNanoTime) / 1000000000.0),
		// 6) + " seconds");
		return out;
	}

	/**
	 * Ricerca posizione del massimo con una roi 5x5
	 * 
	 * @param imp1
	 * @return
	 */
	public static double[] maxPosition5x5(ImagePlus imp1) {

		// double startNanoTime = System.nanoTime();
		int width = imp1.getWidth();
		int height = imp1.getHeight();
		long sum25 = 0;
		double mean25 = 0;
		double max25 = 0;
		int xmax25 = 0;
		int ymax25 = 0;
		int offset = 0;
		ImageProcessor ip1 = imp1.getProcessor();
		short[] pixels1 = (short[]) ip1.getPixels();
		for (int i1 = 2; i1 < width - 2; i1++) {
			for (int i2 = 2; i2 < height - 2; i2++) {
				sum25 = 0;
				offset = (i1 - 2) * width + i2;
				for (int i3 = -2; i3 < 2; i3++) {
					sum25 = sum25 + (pixels1[offset + i3]);
				}
				offset = (i1 - 1) * width + i2;
				for (int i3 = -2; i3 < 2; i3++) {
					sum25 = sum25 + (pixels1[offset + i3]);
				}
				offset = (i1) * width + i2;
				for (int i3 = -2; i3 < 2; i3++) {
					sum25 = sum25 + (pixels1[offset + i3]);
				}
				offset = (i1 + 1) * width + i2;
				for (int i3 = -2; i3 < 2; i3++) {
					sum25 = sum25 + (pixels1[offset + i3]);
				}
				offset = (i1 + 2) * width + i2;
				for (int i3 = -2; i3 < 2; i3++) {
					sum25 = sum25 + (pixels1[offset + i3]);
				}
				mean25 = sum25 / 25.0;
				if (mean25 > max25) {
					max25 = mean25;
					xmax25 = i2;
					ymax25 = i1;
				}
			}
		}
		double[] out = new double[3];
		out[0] = xmax25;
		out[1] = ymax25;
		out[2] = max25;

		// IJ.log("maxPosition5x5 "
		// + IJ.d2s(((System.nanoTime() - startNanoTime) / 1000000000.0),
		// 6) + " seconds");
		return out;
	}

	/**
	 * Ricerca posizione del massimo con una roi 7x7
	 * 
	 * @param imp1
	 * @return
	 */
	public static double[] maxPosition7x7(ImagePlus imp1) {

		// double startNanoTime = System.nanoTime();
		int width = imp1.getWidth();
		int height = imp1.getHeight();
		long sum49 = 0;
		double mean49 = 0;
		double max49 = 0;
		int xmax49 = 0;
		int ymax49 = 0;
		int offset = 0;
		ImageProcessor ip1 = imp1.getProcessor();
		short[] pixels1 = (short[]) ip1.getPixels();
		for (int i1 = 3; i1 < width - 3; i1++) {
			for (int i2 = 3; i2 < height - 3; i2++) {
				sum49 = 0;
				offset = (i1 - 3) * width + i2;
				for (int i3 = -3; i3 < 3; i3++) {
					sum49 = sum49 + (pixels1[offset + i3]);
				}
				offset = (i1 - 2) * width + i2;
				for (int i3 = -3; i3 < 3; i3++) {
					sum49 = sum49 + (pixels1[offset + i3]);
				}
				offset = (i1 - 1) * width + i2;
				for (int i3 = -3; i3 < 3; i3++) {
					sum49 = sum49 + (pixels1[offset + i3]);
				}
				offset = (i1) * width + i2;
				for (int i3 = -3; i3 < 3; i3++) {
					sum49 = sum49 + (pixels1[offset + i3]);
				}
				offset = (i1 + 1) * width + i2;
				for (int i3 = -3; i3 < 3; i3++) {
					sum49 = sum49 + (pixels1[offset + i3]);
				}
				offset = (i1 + 2) * width + i2;
				for (int i3 = -3; i3 < 3; i3++) {
					sum49 = sum49 + (pixels1[offset + i3]);
				}
				offset = (i1 + 3) * width + i2;
				for (int i3 = -3; i3 < 3; i3++) {
					sum49 = sum49 + (pixels1[offset + i3]);
				}

				mean49 = sum49 / 49.0;
				if (mean49 > max49) {
					max49 = mean49;
					xmax49 = i2;
					ymax49 = i1;
				}
			}
		}
		double[] out = new double[3];
		out[0] = xmax49;
		out[1] = ymax49;
		out[2] = max49;
		// IJ.log("maxPosition7x7 "
		// + IJ.d2s(((System.nanoTime() - startNanoTime) / 1000000000.0),
		// 6) + " seconds");
		return out;
	}

	/**
	 * Ricerca posizione del massimo con una roi 9x9
	 * 
	 * @param imp1
	 * @return
	 */
	public static double[] maxPosition9x9(ImagePlus imp1) {

		// double startNanoTime = System.nanoTime();
		int width = imp1.getWidth();
		int height = imp1.getHeight();
		long sum81 = 0;
		double mean81 = 0;
		double max81 = 0;
		int xmax81 = 0;
		int ymax81 = 0;
		int offset = 0;
		ImageProcessor ip1 = imp1.getProcessor();
		short[] pixels1 = (short[]) ip1.getPixels();
		for (int i1 = 4; i1 < width - 4; i1++) {
			for (int i2 = 4; i2 < height - 4; i2++) {
				sum81 = 0;
				offset = (i1 - 4) * width + i2;
				for (int i3 = -4; i3 < 4; i3++) {
					sum81 = sum81 + (pixels1[offset + i3]);
				}
				offset = (i1 - 3) * width + i2;
				for (int i3 = -4; i3 < 4; i3++) {
					sum81 = sum81 + (pixels1[offset + i3]);
				}
				offset = (i1 - 2) * width + i2;
				for (int i3 = -4; i3 < 4; i3++) {
					sum81 = sum81 + (pixels1[offset + i3]);
				}
				offset = (i1 - 1) * width + i2;
				for (int i3 = -4; i3 < 4; i3++) {
					sum81 = sum81 + (pixels1[offset + i3]);
				}
				offset = (i1) * width + i2;
				for (int i3 = -4; i3 < 4; i3++) {
					sum81 = sum81 + (pixels1[offset + i3]);
				}
				offset = (i1 + 1) * width + i2;
				for (int i3 = -4; i3 < 4; i3++) {
					sum81 = sum81 + (pixels1[offset + i3]);
				}
				offset = (i1 + 2) * width + i2;
				for (int i3 = -4; i3 < 4; i3++) {
					sum81 = sum81 + (pixels1[offset + i3]);
				}
				offset = (i1 + 3) * width + i2;
				for (int i3 = -4; i3 < 4; i3++) {
					sum81 = sum81 + (pixels1[offset + i3]);
				}
				offset = (i1 + 4) * width + i2;
				for (int i3 = -4; i3 < 4; i3++) {
					sum81 = sum81 + (pixels1[offset + i3]);
				}
				mean81 = sum81 / 81.0;
				if (mean81 > max81) {
					max81 = mean81;
					xmax81 = i2;
					ymax81 = i1;
				}
			}
		}
		double[] out = new double[3];
		out[0] = xmax81;
		out[1] = ymax81;
		out[2] = max81;
		// IJ.log("maxPosition9x9 "
		// + IJ.d2s(((System.nanoTime() - startNanoTime) / 1000000000.0),
		// 6) + " seconds");
		return out;
	}

	/**
	 * Ricerca posizione del massimo con una roi 11x11
	 * 
	 * @param imp1
	 * @return
	 */
	public static double[] maxPosition11x11(ImagePlus imp1) {

		// double startNanoTime = System.nanoTime();
		int width = imp1.getWidth();
		int height = imp1.getHeight();
		long sum121 = 0;
		double mean121 = 0;
		double max121 = 0;
		int xmax121 = 0;
		int ymax121 = 0;
		int offset = 0;
		ImageProcessor ip1 = imp1.getProcessor();
		short[] pixels1 = (short[]) ip1.getPixels();
		for (int i1 = 5; i1 < width - 5; i1++) {
			for (int i2 = 5; i2 < height - 5; i2++) {
				sum121 = 0;
				offset = (i1 - 5) * width + i2;
				for (int i3 = -5; i3 < 5; i3++) {
					sum121 = sum121 + (pixels1[offset + i3]);
				}
				offset = (i1 - 4) * width + i2;
				for (int i3 = -5; i3 < 5; i3++) {
					sum121 = sum121 + (pixels1[offset + i3]);
				}
				offset = (i1 - 3) * width + i2;
				for (int i3 = -5; i3 < 5; i3++) {
					sum121 = sum121 + (pixels1[offset + i3]);
				}
				offset = (i1 - 2) * width + i2;
				for (int i3 = -5; i3 < 5; i3++) {
					sum121 = sum121 + (pixels1[offset + i3]);
				}
				offset = (i1 - 1) * width + i2;
				for (int i3 = -5; i3 < 5; i3++) {
					sum121 = sum121 + (pixels1[offset + i3]);
				}
				offset = (i1) * width + i2;
				for (int i3 = -5; i3 < 5; i3++) {
					sum121 = sum121 + (pixels1[offset + i3]);
				}
				offset = (i1 + 1) * width + i2;
				for (int i3 = -5; i3 < 5; i3++) {
					sum121 = sum121 + (pixels1[offset + i3]);
				}
				offset = (i1 + 2) * width + i2;
				for (int i3 = -5; i3 < 5; i3++) {
					sum121 = sum121 + (pixels1[offset + i3]);
				}
				offset = (i1 + 3) * width + i2;
				for (int i3 = -5; i3 < 5; i3++) {
					sum121 = sum121 + (pixels1[offset + i3]);
				}
				offset = (i1 + 4) * width + i2;
				for (int i3 = -5; i3 < 5; i3++) {
					sum121 = sum121 + (pixels1[offset + i3]);
				}
				offset = (i1 + 5) * width + i2;
				for (int i3 = -5; i3 < 5; i3++) {
					sum121 = sum121 + (pixels1[offset + i3]);
				}
				mean121 = sum121 / 121.0;
				if (mean121 > max121) {
					max121 = mean121;
					xmax121 = i2;
					ymax121 = i1;
				}
			}
		}
		double[] out = new double[3];
		out[0] = xmax121;
		out[1] = ymax121;
		out[2] = max121;
		// IJ.log("maxPosition11x11 "
		// + IJ.d2s(((System.nanoTime() - startNanoTime) / 1000000000.0),
		// 6) + " seconds");
		return out;
	}

}