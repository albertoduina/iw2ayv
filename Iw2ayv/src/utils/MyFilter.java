package utils;

import java.awt.Color;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;

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
	 * Ricerca posizione del massimo con una roi 11x11, restituisce le
	 * coordinate del centro
	 * 
	 * @param imp1
	 * @return
	 */
	public static double[] maxPosition11x11_NEW3333333(ImagePlus imp1) {
		int width = imp1.getWidth();
		int height = imp1.getHeight();
		long sum121 = 0;
		double mean121 = 0;
		double max121 = 0;
		int xmax121 = 0;
		int ymax121 = 0;
		int offset = 0;
		int conta = 0;
		ImageProcessor ip1 = imp1.getProcessor();
		// MyLog.waitHere("bitDepth= " + imp1.getBitDepth());
		// if (imp1.getBitDepth() == 16) {
		short pixels1[] = (short[]) ip1.getPixels();
		// scansione sulle coordinate del centro roi 11x11
		for (int i1 = 5; i1 < height - 5; i1++) {
			for (int i2 = 5; i2 < width - 5; i2++) {
				sum121 = 0;
				conta = 0;
				// mi posiziono sulla riga
				for (int i4 = -5; i4 < 6; i4++) {
					offset = (i1 + i4) * width + i2;
					for (int i3 = -5; i3 < 6; i3++) {
						sum121 = sum121 + (pixels1[offset + i3]);
						conta++;
					}
				}
				// MyLog.waitHere("conta= " + conta);
				mean121 = sum121 / (double) conta;
				if (mean121 > max121) {
					max121 = mean121;
					xmax121 = i2;
					ymax121 = i1;
				}
			}
		}
		// } else
		// return null;
		if (max121 < 50.0) // filtro per evitare di restitruire il fondo
			return null;
		double[] out = new double[3];
		out[0] = xmax121;
		out[1] = ymax121;
		out[2] = max121;
		return out;
	}

	public static double[] maxCircularPosition11x11_NEW(ImagePlus imp1) {
		// double startNanoTime = System.nanoTime();
		int width = imp1.getWidth();
		int height = imp1.getHeight();
		long sum121 = 0;
		double mean121 = 0;
		double max121 = 0;
		int xmax121 = 0;
		int ymax121 = 0;
		int offset = 0;
		int address = 0;
		int count = 0;
		if (imp1.getBytesPerPixel() != 2)
			return null;
		ImageProcessor ip1 = imp1.getProcessor();
		short[] pixels1 = (short[]) ip1.getPixels();
		// scansione sulle coordinate del centro roi 11x11
		for (int i1 = 5; i1 < height - 5; i1++) {
			for (int i2 = 5; i2 < width - 5; i2++) {
				sum121 = 0;
				count = 0;

				// mi posiziono sulla riga
				for (int i4 = -5; i4 < 6; i4++) {
					offset = (i1 + i4) * width + i2;
					for (int i3 = -5; i3 < 6; i3++) {
						address = offset + i3;
						sum121 = sum121 + (pixels1[address]);
						count++;
					}
				}
				mean121 = sum121 / count;
				if (count != 121)
					MyLog.waitHere("count= " + count);
				if (mean121 > max121) {
					max121 = mean121;
					xmax121 = i2;
					ymax121 = i1;
				}
			}
		}

		if (max121 < 50.0) // filtro per evitare di restitruire il fondo
			return null;

		double[] out = new double[3];
		out[0] = xmax121;
		out[1] = ymax121;
		out[2] = max121;
		return out;
	}

	/**
	 * Ricerca posizione del massimo con una roi 11x11, restituisce le
	 * coordinate del centro
	 * 
	 * @param imp1
	 * @return
	 */
	public static double[] maxPosition11x11_NEW(ImagePlus imp1) {
		// double startNanoTime = System.nanoTime();
		int width = imp1.getWidth();
		int height = imp1.getHeight();
		long sum121 = 0;
		double mean121 = 0;
		double max121 = 0;
		int xmax121 = 0;
		int ymax121 = 0;
		int offset = 0;
		int address = 0;
		int count = 0;
		if (imp1.getBytesPerPixel() != 2)
			return null;
		ImageProcessor ip1 = imp1.getProcessor();
		short[] pixels1 = (short[]) ip1.getPixels();
		// scansione sulle coordinate del centro roi 11x11
		for (int i1 = 5; i1 < height - 5; i1++) {
			for (int i2 = 5; i2 < width - 5; i2++) {
				sum121 = 0;
				count = 0;
				// mi posiziono sulla riga
				for (int i4 = -5; i4 < 6; i4++) {
					offset = (i1 + i4) * width + i2;
					for (int i3 = -5; i3 < 6; i3++) {
						address = offset + i3;
						sum121 = sum121 + (pixels1[address]);
						count++;
					}
				}
				mean121 = sum121 / count;
				if (count != 121)
					MyLog.waitHere("count= " + count);
				if (mean121 > max121) {
					max121 = mean121;
					xmax121 = i2;
					ymax121 = i1;
				}
			}
		}

		if (max121 < 50.0) // filtro per evitare di restitruire il fondo
			return null;

		double[] out = new double[3];
		out[0] = xmax121;
		out[1] = ymax121;
		out[2] = max121;
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

	/**
	 * Ricerca posizione del massimo con una roi programmabile di lato dispari,
	 * restituisce le coordinate del centro
	 * 
	 * @param imp1
	 * @return
	 */
	public static double[] maxPositionGeneric(ImagePlus imp1, int lato, boolean stampa2) {
		if ((lato & 1) == 0) {
			MyLog.waitHere("il latro del kernel deve essere dispari!!");
			return null;
		}

		int width = imp1.getWidth();
		int height = imp1.getHeight();
		long sum1 = 0;
		double mean1 = 0;
		double max1 = 0;
		int xmax1 = 0;
		int ymax1 = 0;
		int offset = 0;
		int address = 0;
		int count = 0;
		if (imp1.getBytesPerPixel() != 2)
			return null;
		ImageProcessor ip1 = imp1.getProcessor();
		short[] pixels1 = (short[]) ip1.getPixels();
		// short[] pippo1 = new short[lato * lato];

		// scansione sulle coordinate del centro roi yxy
		int pip = (lato - 1) / 2;
		for (int i1 = pip; i1 < height - pip; i1++) {
			for (int i2 = pip; i2 < width - pip; i2++) {
				sum1 = 0;
				count = 0;
				// mi posiziono sulla riga
				for (int i4 = -pip; i4 < pip + 1; i4++) {
					offset = (i1 + i4) * width + i2;
					for (int i3 = -pip; i3 < pip + 1; i3++) {
						address = offset + i3;
						sum1 = sum1 + (pixels1[address]);
						// pippo1[count] = pixels1[address];
						count++;
					}
				}
				mean1 = sum1 / count;
				if (count != lato * lato)
					MyLog.waitHere("errore count= " + count);
				if (mean1 > max1) {
					max1 = mean1;
					xmax1 = i2;
					ymax1 = i1;
				}
			}
		}
		// MyLog.resultsLog(pippo1, "pippo1");
		stampa2 = false;
		if (max1 < 50.0) // filtro per evitare di restitruire il fondo
			return null;

		double[] out = new double[3];
		out[0] = xmax1;
		out[1] = ymax1;
		out[2] = max1;
		return out;
	}

	/***
	 * Cerca l'hotspot all'interno di un cerchio posto sull'immagine reale
	 * 
	 * @param imp1
	 *            immagine da analizzare
	 * @param circleData
	 *            profilo esterno fantoccio
	 * @param circleSearch
	 *            cerchio di ricerca
	 * @return
	 */
	public static double[] positionSearchCircular(ImagePlus imp1, double[] circleData, double diam22, boolean demo) {

		Overlay over2 = new Overlay();
		imp1.deleteRoi();

		ImagePlus imp2 = imp1.duplicate();
		imp2.setOverlay(over2);

		int xCenter1 = (int) circleData[0];
		int yCenter1 = (int) circleData[1];
		int diam1 = (int) circleData[2];

		int diam2 = (int) diam22;

		// disegno il perimetro del fantoccio
		int xRoi0 = xCenter1 - diam1 / 2;
		int yRoi0 = yCenter1 - diam1 / 2;
		int diamRoi0 = diam1;

		ImageWindow iw1 = null;
		if (demo) {
			iw1 = UtilAyv.showImageMaximized(imp2);
		}
		// marco con un punto il centro del fantoccio

		imp2.setRoi(new OvalRoi(xRoi0, yRoi0, diamRoi0, diamRoi0));
		if (demo) {
			imp2.getRoi().setStrokeColor(Color.red);
			over2.addElement(imp2.getRoi());
		}

		// ora faccio la scansione del bounding rectangle del cerchio esterno,
		// utilizzando il cerchio interno, se risulta che il cerchio interno è
		// completamente all'interno ne marco il contorno in verde

		int xstart = (int) xCenter1 - diam1 / 2 + diam2 / 2;
		int xend = (int) xCenter1 + diam1 / 2 - diam2 / 2;
		int ystart = (int) yCenter1 - diam1 / 2 + diam2 / 2;
		int yend = (int) yCenter1 + diam1 / 2 - diam2 / 2;
		double max1 = -99999;
		int xmax = 0;
		int ymax = 0;
		for (int x2 = xstart; x2 < xend; x2++) {
			for (int y2 = ystart; y2 < yend; y2++) {
				if (MyGeometry.isCircleInside(xCenter1, yCenter1, diam1, x2, y2, diam2)) {
					imp2.setRoi(new OvalRoi(x2 - diam2 / 2, y2 - diam2 / 2, diam2, diam2));
					if (demo) {
						imp2.getRoi().setStrokeColor(Color.green);
						over2.addElement(imp2.getRoi());
					}

					ImageStatistics is1 = imp2.getStatistics();
					double med1 = is1.mean;
					if (med1 > max1) {
						max1 = med1;
						xmax = x2;
						ymax = y2;
					}
				}
			}
		}
		if (demo) {
			imp2.setRoi(new OvalRoi(xmax - diam2 / 2, ymax - diam2 / 2, diam2, diam2));
			imp2.getRoi().setStrokeColor(Color.blue);
			over2.addElement(imp2.getRoi());
		}
		IJ.wait(100);
		if (iw1 != null)
			iw1.close();
		double[] out = new double[3];
		out[0] = xmax;
		out[1] = ymax;
		out[2] = max1;
		return out;
	}

}