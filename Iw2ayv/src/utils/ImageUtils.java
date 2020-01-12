package utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Window;
import java.util.ArrayList;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.gui.ImageWindow;
import ij.gui.NewImage;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.gui.PointRoi;
import ij.gui.Roi;
import ij.io.FileSaver;
import ij.measure.Calibration;
import ij.measure.Measurements;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;
import ij.process.ShortProcessor;

public class ImageUtils {

	private static boolean previous = false; // utilizzati per impulso
	private static boolean init1 = true; // utilizzati per impulso
	@SuppressWarnings("unused")
	// utilizzati per impulso
	private static boolean pulse = false; // utilizzati per impulso, lasciare,
											// serve anche se segnalato
											// inutilizzato

	public static ImagePlus generaSimulata5Colori(int xRoi, int yRoi, int diamRoi, ImagePlus imp, boolean step,
			boolean verbose, boolean test) {

		int xRoiSimulata = xRoi + (diamRoi - MyConst.P3_DIAM_FOR_450_PIXELS) / 2;
		int yRoiSimulata = yRoi + (diamRoi - MyConst.P3_DIAM_FOR_450_PIXELS) / 2;
		ImagePlus impSimulata = simulata5Colori(xRoiSimulata, yRoiSimulata, MyConst.P3_DIAM_FOR_450_PIXELS, imp);
		if (verbose) {
			UtilAyv.showImageMaximized(impSimulata);
			ImageUtils.backgroundEnhancement(0, 0, 10, impSimulata);
		}
		impSimulata.updateAndDraw();
		return impSimulata;
	}

	public static ImagePlus generaSimulataMultiColori(double mean11, ImagePlus imp1, int[] minimi, int[] massimi,
			int[] myColor) {

		if (imp1 == null) {
			IJ.error("generaSimulataMultiColori ricevuto null");
			return (null);
		}
		int width = imp1.getWidth();
		int height = imp1.getHeight();
		short[] pixels1 = UtilAyv.truePixels(imp1);

		double mean = mean11;
		int livello = minimi.length;
		int appoggioColore = 0;

		double[] myMinimi = new double[livello];
		double[] myMassimi = new double[livello];
		for (int i1 = 0; i1 < livello; i1++) {
			myMinimi[i1] = ((100.0 + (double) minimi[i1]) / 100) * mean;
			myMassimi[i1] = ((100.0 + (double) massimi[i1]) / 100) * mean;
		}

		// genero una immagine nera

		ImageProcessor ipSimulata = new ColorProcessor(width, height);
		int[] pixelsSimulata = (int[]) ipSimulata.getPixels();

		short pixSorgente = 0;
		int posizioneArrayImmagine = 0;
		int colorOUT = ((0 & 0xff) << 16) | ((0 & 0xff) << 8) | (0 & 0xff); // nero

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				boolean cerca = true;
				posizioneArrayImmagine = y * width + x;
				pixSorgente = pixels1[posizioneArrayImmagine];

				for (int i1 = 0; i1 < livello; i1++) {
					if (cerca && (pixSorgente > myMinimi[i1]) && (pixSorgente <= myMassimi[i1])) {
						appoggioColore = myColor[i1];
						cerca = false;
					}
				}
				if (cerca) {
					appoggioColore = colorOUT;
					cerca = false;
				}
				pixelsSimulata[posizioneArrayImmagine] = appoggioColore;
			}
		}

		ipSimulata.resetMinAndMax();
		ImagePlus impSimulata = new ImagePlus("ColorSimulata", ipSimulata);
		impSimulata.updateAndDraw();
		return impSimulata;
	}

	/**
	 * 
	 * @param sqX
	 * @param sqY
	 * @param sqR
	 * @param imp1
	 * @return
	 */
	public static ImagePlus simulata5Colori(int sqX, int sqY, int sqR, ImagePlus imp1) {

		if (imp1 == null) {
			IJ.error("Simula5Colori ricevuto null");
			return (null);
		}
		int width = imp1.getWidth();
		int height = imp1.getHeight();
		short[] pixels1 = UtilAyv.truePixels(imp1);

		imp1.setRoi(new OvalRoi(sqX, sqY, sqR, sqR));
		ImageStatistics stat1 = imp1.getStatistics();
		imp1.deleteRoi();

		double mean = stat1.mean;
		double minus20 = mean * MyConst.MINUS_20_PERC;
		double minus10 = mean * MyConst.MINUS_10_PERC;
		double plus10 = mean * MyConst.PLUS_10_PERC;
		double plus20 = mean * MyConst.PLUS_20_PERC;
		// genero una immagine nera

		ImageProcessor ipSimulata = new ColorProcessor(width, height);
		int[] pixelsSimulata = (int[]) ipSimulata.getPixels();

		short pixSorgente = 0;
		int pixSimulata = 0;
		int posizioneArrayImmagine = 0;

		// int color1 = ((255 & 0xff) << 16) | ((127 & 0xff) << 8) | (127 &
		// 0xff);
		// int color2 = ((127 & 0xff) << 16) | ((255 & 0xff) << 8) | (127 &
		// 0xff);
		// int color3 = ((127 & 0xff) << 16) | ((127 & 0xff) << 8) | (255 &
		// 0xff);
		// int color4 = ((255 & 0xff) << 16) | ((0 & 0xff) << 8) | (0 & 0xff);
		// int color5 = ((0 & 0xff) << 16) | ((255 & 0xff) << 8) | (0 & 0xff);
		// int color6 = ((0 & 0xff) << 16) | ((0 & 0xff) << 8) | (0 & 0xff);

		int colorP20 = ((255 & 0xff) << 16) | ((0 & 0xff) << 8) | (0 & 0xff);
		int colorP10 = ((255 & 0xff) << 16) | ((165 & 0xff) << 8) | (0 & 0xff);
		int colorMED = ((255 & 0xff) << 16) | ((255 & 0xff) << 8) | (0 & 0xff);
		int colorM10 = ((124 & 0xff) << 16) | ((252 & 0xff) << 8) | (50 & 0xff);
		int colorM20 = ((0 & 0xff) << 16) | ((128 & 0xff) << 8) | (0 & 0xff);

		int colorOUT = ((0 & 0xff) << 16) | ((0 & 0xff) << 8) | (0 & 0xff); // nero

		for (int y = 0; y < width; y++) {
			for (int x = 0; x < width; x++) {
				posizioneArrayImmagine = y * width + x;
				pixSorgente = pixels1[posizioneArrayImmagine];
				if (pixSorgente > plus20)
					pixSimulata = colorP20;
				else if (pixSorgente > plus10)
					pixSimulata = colorP10;
				else if (pixSorgente > minus10)
					pixSimulata = colorMED;
				else if (pixSorgente > minus20)
					pixSimulata = colorM10;
				else if (pixSorgente > 100)
					pixSimulata = colorM20;
				else
					pixSimulata = colorOUT;
				pixelsSimulata[posizioneArrayImmagine] = pixSimulata;
			}
		}

		ipSimulata.resetMinAndMax();
		ImagePlus impSimulata = new ImagePlus("ColorSimulata", ipSimulata);

		return impSimulata;
	}

	public static ImagePlus generaSimulata5Classi(int xRoi, int yRoi, int diamRoi, ImagePlus imp, boolean step,
			boolean verbose, boolean test) {

		int xRoiSimulata = xRoi + (diamRoi - MyConst.P3_DIAM_FOR_450_PIXELS) / 2;
		int yRoiSimulata = yRoi + (diamRoi - MyConst.P3_DIAM_FOR_450_PIXELS) / 2;
		ImagePlus impSimulata = simulata5Classi(xRoiSimulata, yRoiSimulata, MyConst.P3_DIAM_FOR_450_PIXELS, imp);
		if (verbose) {
			UtilAyv.showImageMaximized(impSimulata);
			ImageUtils.backgroundEnhancement(0, 0, 10, impSimulata);
		}
		impSimulata.updateAndDraw();
		return impSimulata;
	}

	/**
	 * Simulated 5 classes image
	 * 
	 * @param xRoi    x roi coordinate
	 * @param yRoi    y roi coordinate
	 * @param diamRoi roi diameter
	 * @param imp     original image
	 * @param step    step-by-step mode
	 * @param test    autotest mode
	 * @return pixel counts of classes of the simulated image
	 */
	public static int[][] generaSimulata5Classi(int xRoi, int yRoi, int diamRoi, ImagePlus imp, String filename,
			boolean step, boolean verbose, boolean test) {

		int xRoiSimulata = xRoi + (diamRoi - MyConst.P3_DIAM_FOR_450_PIXELS) / 2;
		int yRoiSimulata = yRoi + (diamRoi - MyConst.P3_DIAM_FOR_450_PIXELS) / 2;
		ImagePlus impSimulata = simulata5Classi(xRoiSimulata, yRoiSimulata, MyConst.P3_DIAM_FOR_450_PIXELS, imp);
		if (verbose) {
			UtilAyv.showImageMaximized(impSimulata);
			ImageUtils.backgroundEnhancement(0, 0, 10, impSimulata);
		}
		impSimulata.updateAndDraw();
		int[][] classiSimulata = numeroPixelsClassi(impSimulata);
		String patName = ReadDicom.readDicomParameter(imp, MyConst.DICOM_PATIENT_NAME);

		String codice1 = ReadDicom.readDicomParameter(imp, MyConst.DICOM_SERIES_DESCRIPTION);

		String codice = UtilAyv.getFiveLetters(codice1);

		String simName = filename + patName + codice + "sim.zip";

		if (!test)

			new FileSaver(impSimulata).saveAsZip(simName);
		return classiSimulata;
	}

	/**
	 * 
	 * @param sqX
	 * @param sqY
	 * @param sqR
	 * @param imp1
	 * @return
	 */
	public static ImagePlus simulata5Classi(int sqX, int sqY, int sqR, ImagePlus imp1) {

		if (imp1 == null) {
			IJ.error("Simula5Classi ricevuto null");
			return (null);
		}
		int width = imp1.getWidth();
		short[] pixels1 = UtilAyv.truePixels(imp1);

		imp1.setRoi(new OvalRoi(sqX, sqY, sqR, sqR));
		ImageStatistics stat1 = imp1.getStatistics();
		imp1.deleteRoi();

		double mean = stat1.mean;
		double minus20 = mean * MyConst.MINUS_20_PERC;
		double minus10 = mean * MyConst.MINUS_10_PERC;
		double plus10 = mean * MyConst.PLUS_10_PERC;
		double plus20 = mean * MyConst.PLUS_20_PERC;
		// genero una immagine nera
		ImagePlus impSimulata = NewImage.createShortImage("Simulata", width, width, 1, NewImage.FILL_BLACK);
		ShortProcessor processorSimulata = (ShortProcessor) impSimulata.getProcessor();
		short[] pixelsSimulata = (short[]) processorSimulata.getPixels();

		short pixSorgente = 0;
		short pixSimulata = 0;
		int posizioneArrayImmagine = 0;

		for (int y = 0; y < width; y++) {
			for (int x = 0; x < width; x++) {
				posizioneArrayImmagine = y * width + x;
				pixSorgente = pixels1[posizioneArrayImmagine];
				if (pixSorgente > plus20)
					pixSimulata = MyConst.LEVEL_5;
				else if (pixSorgente > plus10)
					pixSimulata = MyConst.LEVEL_4;
				else if (pixSorgente > minus10)
					pixSimulata = MyConst.LEVEL_3;
				else if (pixSorgente > minus20)
					pixSimulata = MyConst.LEVEL_2;
				else
					pixSimulata = MyConst.LEVEL_1;
				pixelsSimulata[posizioneArrayImmagine] = pixSimulata;
			}
		}
		processorSimulata.resetMinAndMax();
		return impSimulata;
	}

	/**
	 * generazione di un immagine simulata, display e salvataggio
	 * 
	 * @param xCenterRoi coordinata x roi
	 * @param yRoi       coordinata y roi
	 * @param diamRoi    diametro roi
	 * @param imp1       puntatore ImagePlus alla immagine originale
	 * @param step       funzionamento passo passo
	 * @param verbose
	 * 
	 * @param test       modo autotest
	 * @return numeriosit� classi simulata
	 */
	public static int[][] generaSimulata12classi(int xCenterRoi, int yCenterRoi, int latoRoi, ImagePlus imp1,
			String filePath, boolean step, boolean verbose, boolean test) {

		boolean debug = true;
		if (imp1 == null) {
			MyLog.waitHere("generaSimulata12classi imp1==null");
			return (null);
		}

		ImagePlus impSimulata = simulata12Classi(xCenterRoi, yCenterRoi, latoRoi, imp1);
		if (verbose) {
			UtilAyv.showImageMaximized(impSimulata);
			IJ.run("Enhance Contrast", "saturated=0.5");
		}

		// MyLog.waitHere("simulata1= " + filePath);

		if (step)
			MyLog.waitHere("Immagine Simulata", debug);
		int[][] classiSimulata = numeroPixelsClassi(impSimulata);

		if (!test)
			new FileSaver(impSimulata).saveAsZip(filePath);
		return classiSimulata;
	}

	/**
	 * generazione di un immagine simulata, display e salvataggio
	 * 
	 * @param xCenterRoi coordinata x roi
	 * @param yRoi       coordinata y roi
	 * @param diamRoi    diametro roi
	 * @param imp1       puntatore ImagePlus alla immagine originale
	 * @param step       funzionamento passo passo
	 * @param verbose
	 * 
	 * @param test       modo autotest
	 * @return numeriosita' classi simulata
	 */
	public static int[][] generaSimulata12classi(int xCenterRoi, int yCenterRoi, int latoRoi, ImagePlus imp1,
			String filePath, String aux1, int mode, int timeout) {

		boolean debug = true;
		if (imp1 == null) {
			MyLog.waitHere("generaSimulata12classi imp1==null");
			return (null);
		}
		boolean verbose = false;
		boolean step = false;
		boolean test = false;
		if (mode == 0) {
			test = false;
		}

		if (mode == 10 || mode == 3) {
			verbose = true;
			test = true;
		}

		ImagePlus impSimulata = simulata12Classi(xCenterRoi, yCenterRoi, latoRoi, imp1);
		if (verbose) {
			UtilAyv.showImageMaximized(impSimulata);
			IJ.run("Enhance Contrast", "saturated=0.5");
			MyLog.waitHere("Immagine Simulata", debug, timeout);
		}
		if (step)
			MyLog.waitHere("Immagine Simulata", debug);
		int[][] classiSimulata = numeroPixelsClassi(impSimulata);

		// MyLog.waitHere("simulata2= "+filePath);
		if (!test)
			impSimulata.setTitle(aux1 + "sim");
		new FileSaver(impSimulata).saveAsZip(filePath);

		return classiSimulata;
	}

	/**
	 * Genera l'immagine simulata a 11+1 livelli
	 * 
	 * @param imp1 immagine da analizzare
	 * @param sqX  coordinata x della Roi centrale
	 * @param sqY  coordinata y della Roi centrale
	 * @param sqR  diametro della Roi centrale
	 * @return immagine simulata a 11+1 livelli
	 */

	static ImagePlus simulata12Classi(int sqX, int sqY, int sqR, ImagePlus imp1) {

		if (imp1 == null) {
			MyLog.waitHere("Simula12 imp1 == null");
			return (null);
		}

		int width = imp1.getWidth();
		int height = imp1.getHeight();
		short[] pixels1 = UtilAyv.truePixels(imp1);
		//
		// disegno MROI per calcoli
		//
		imp1.setRoi(sqX - sqR / 2, sqY - sqR / 2, sqR, sqR);

		ImageStatistics stat1 = imp1.getStatistics();
		double mean = stat1.mean;
		//
		// limiti classi
		//
		double minus90 = mean * MyConst.MINUS_90_PERC;
		double minus80 = mean * MyConst.MINUS_80_PERC;
		double minus70 = mean * MyConst.MINUS_70_PERC;
		double minus60 = mean * MyConst.MINUS_60_PERC;
		double minus50 = mean * MyConst.MINUS_50_PERC;
		double minus40 = mean * MyConst.MINUS_40_PERC;
		double minus30 = mean * MyConst.MINUS_30_PERC;
		double minus20 = mean * MyConst.MINUS_20_PERC;
		double minus10 = mean * MyConst.MINUS_10_PERC;
		double plus10 = mean * MyConst.PLUS_10_PERC;
		double plus20 = mean * MyConst.PLUS_20_PERC;
		// genero una immagine nera
		ImagePlus impSimulata = NewImage.createShortImage("Simulata", width, height, 1, NewImage.FILL_BLACK);
		//
		// nuova immagine simulata vuota
		//
		ShortProcessor processorSimulata = (ShortProcessor) impSimulata.getProcessor();
		short[] pixelsSimulata = (short[]) processorSimulata.getPixels();
		//
		// riempimento immagine simulata
		//
		short pixSorgente;
		short pixSimulata;
		int posizioneArrayImmagine = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				posizioneArrayImmagine = y * width + x;
				pixSorgente = pixels1[posizioneArrayImmagine];
				if (pixSorgente > plus20)
					pixSimulata = MyConst.LEVEL_12;
				else if (pixSorgente > plus10)
					pixSimulata = MyConst.LEVEL_11;
				else if (pixSorgente > minus10)
					pixSimulata = MyConst.LEVEL_10;
				else if (pixSorgente > minus20)
					pixSimulata = MyConst.LEVEL_9;
				else if (pixSorgente > minus30)
					pixSimulata = MyConst.LEVEL_8;
				else if (pixSorgente > minus40)
					pixSimulata = MyConst.LEVEL_7;
				else if (pixSorgente > minus50)
					pixSimulata = MyConst.LEVEL_6;
				else if (pixSorgente > minus60)
					pixSimulata = MyConst.LEVEL_5;
				else if (pixSorgente > minus70)
					pixSimulata = MyConst.LEVEL_4;
				else if (pixSorgente > minus80)
					pixSimulata = MyConst.LEVEL_3;
				else if (pixSorgente > minus90)
					pixSimulata = MyConst.LEVEL_2;
				else
					pixSimulata = MyConst.LEVEL_1;

				pixelsSimulata[posizioneArrayImmagine] = pixSimulata;
			}
		}
		processorSimulata.resetMinAndMax();
		return impSimulata;
	}

	public static ImagePlus generaSimulata12colori(double mean, ImagePlus imp1, boolean step, boolean verbose,
			boolean test) {

		int timeout = 100;
		boolean debug = true;
		if (imp1 == null) {
			MyLog.waitHere("generaSimulata12colori imp1==null");
			return (null);
		}

		ImagePlus impSimulata = simulata12Colori(mean, imp1);
		// if (verbose) {
		// UtilAyv.showImageMaximized(impSimulata);
		// IJ.run("Enhance Contrast", "saturated=0.5");
		// MyLog.waitHere("Immagine Simulata", debug, timeout);
		// }
		return impSimulata;
	}

	/**
	 * Genera l'immagine simulata a 11+1 livelli
	 * 
	 * @param imp1 immagine da analizzare
	 * @param sqX  coordinata x della Roi centrale
	 * @param sqY  coordinata y della Roi centrale
	 * @param sqR  diametro della Roi centrale
	 * @return immagine simulata a 11+1 livelli
	 */

	static ImagePlus simulata12Colori(double mean, ImagePlus imp1) {

		if (imp1 == null) {
			MyLog.waitHere("Simula12 imp1 == null");
			return (null);
		}
		int width = imp1.getWidth();
		int height = imp1.getHeight();
		short[] pixels1 = UtilAyv.truePixels(imp1);

		//
		// limiti classi
		//
		double minus90 = mean * MyConst.MINUS_90_PERC;
		double minus80 = mean * MyConst.MINUS_80_PERC;
		double minus70 = mean * MyConst.MINUS_70_PERC;
		double minus60 = mean * MyConst.MINUS_60_PERC;
		double minus50 = mean * MyConst.MINUS_50_PERC;
		double minus40 = mean * MyConst.MINUS_40_PERC;
		double minus30 = mean * MyConst.MINUS_30_PERC;
		double minus20 = mean * MyConst.MINUS_20_PERC;
		double minus10 = mean * MyConst.MINUS_10_PERC;
		double plus10 = mean * MyConst.PLUS_10_PERC;
		double plus20 = mean * MyConst.PLUS_20_PERC;
		// genero una immagine nera
		// ImagePlus impSimulata = NewImage.createShortImage("Simulata", width,
		// height, 1, NewImage.FILL_BLACK);
		//
		// nuova immagine simulata vuota
		//
		// ShortProcessor processorSimulata = (ShortProcessor)
		// impSimulata.getProcessor();
		// short[] pixelsSimulata = (short[]) processorSimulata.getPixels();

		int colorM90 = ((25 & 0xff) << 16) | ((25 & 0xff) << 8) | (112 & 0xff); // Midnight
																				// Blue
		int colorM80 = ((0 & 0xff) << 16) | ((0 & 0xff) << 8) | (205 & 0xff); // Medium
																				// Blue
		int colorM70 = ((138 & 0xff) << 16) | ((43 & 0xff) << 8) | (226 & 0xff); // blue
																					// violet
		int colorM60 = ((0 & 0xff) << 16) | ((100 & 0xff) << 8) | (0 & 0xff); // dark
																				// green
		int colorM50 = ((0 & 0xff) << 16) | ((128 & 0xff) << 8) | (0 & 0xff); // green
		int colorM40 = ((50 & 0xff) << 16) | ((205 & 0xff) << 8) | (50 & 0xff); // lime
																				// green
		int colorM30 = ((128 & 0xff) << 16) | ((128 & 0xff) << 8) | (0 & 0xff); // olive
		int colorM20 = ((255 & 0xff) << 16) | ((255 & 0xff) << 8) | (0 & 0xff); // yellow
		int colorM10 = ((255 & 0xff) << 16) | ((165 & 0xff) << 8) | (0 & 0xff); // orange
		int colorP10 = ((250 & 0xff) << 16) | ((128 & 0xff) << 8) | (114 & 0xff); // salmon
		int colorP20 = ((255 & 0xff) << 16) | ((0 & 0xff) << 8) | (0 & 0xff); // red

		int colorOUT = ((0 & 0xff) << 16) | ((0 & 0xff) << 8) | (0 & 0xff); // nero

		ImageProcessor ipSimulata = new ColorProcessor(width, height);
		int[] pixelsSimulata = (int[]) ipSimulata.getPixels();

		//
		// riempimento immagine simulata
		//
		short pixSorgente;
		int pixSimulata;
		int posizioneArrayImmagine = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				posizioneArrayImmagine = y * width + x;
				pixSorgente = pixels1[posizioneArrayImmagine];

				if (pixSorgente > plus20)
					pixSimulata = colorP20;
				else if (pixSorgente > plus10)
					pixSimulata = colorP10;
				else if (pixSorgente > minus10)
					pixSimulata = colorM10;
				else if (pixSorgente > minus20)
					pixSimulata = colorM20;
				else if (pixSorgente > minus30)
					pixSimulata = colorM30;
				else if (pixSorgente > minus40)
					pixSimulata = colorM40;
				else if (pixSorgente > minus50)
					pixSimulata = colorM50;
				else if (pixSorgente > minus60)
					pixSimulata = colorM60;
				else if (pixSorgente > minus70)
					pixSimulata = colorM70;
				else if (pixSorgente > minus80)
					pixSimulata = colorM80;
				else if (pixSorgente > minus90)
					pixSimulata = colorM90;
				else
					pixSimulata = colorOUT;
				pixelsSimulata[posizioneArrayImmagine] = pixSimulata;
			}
		}

		ipSimulata.resetMinAndMax();
		ImagePlus impSimulata = new ImagePlus("ColorSimulata", ipSimulata);

		ipSimulata.resetMinAndMax();
		return impSimulata;
	}

	/**
	 * Genera l'immagine simulata a 11+1 livelli
	 * 
	 * @param imp1 immagine da analizzare
	 * @param sqX  coordinata x della Roi centrale
	 * @param sqY  coordinata y della Roi centrale
	 * @param sqR  diametro della Roi centrale
	 * @return immagine simulata a 11+1 livelli
	 */

	static public ImagePlus generaScalaColori(int[] myColor, String[] myLabels) {

		// genero una immagine nera
		int matrix = 256;
		ImageProcessor ip1 = new ColorProcessor(matrix, matrix);
		int[] pixels1 = (int[]) ip1.getPixels();
		ip1.resetMinAndMax();
		ImagePlus imp1 = null;
		int lato = 0;
		int gap = 0;
		int offset = 0;
		int wi1 = ip1.getStringWidth("-10 +10");
		int[] a1 = new int[myColor.length];

		if (myColor.length > 5) {
			ip1.setColor(new Color(220, 220, 220));
			ip1.setFont(new Font("Arial", Font.TYPE1_FONT, 8));
		} else {
			ip1.setColor(new Color(10, 10, 10));
			ip1.setFont(new Font("Arial", Font.BOLD, 13));
		}

		ip1.setJustification(ImageProcessor.LEFT_JUSTIFY);
		ip1.setAntialiasedText(true);

		if (myColor.length > 5) {
			imp1 = new ImagePlus("Scala colori", ip1);
			lato = 200;
			for (int i1 = myColor.length - 1; i1 >= 0; i1--) {
				lato = (i1 + 1) * 20;
				gap = (matrix - lato) / 2;
				for (int y1 = 0; y1 < lato; y1++) {
					offset = (y1 + gap) * matrix;
					for (int x1 = 0; x1 < lato; x1++) {
						pixels1[offset + gap + x1] = myColor[i1];
						a1[i1] = y1 + gap;
					}
				}
				imp1.updateAndDraw();
			}
		} else {
			imp1 = new ImagePlus("Scala colori", ip1);
			lato = 200;
			for (int i1 = myColor.length - 1; i1 >= 0; i1--) {
				lato = (i1 + 1) * 45;
				gap = (matrix - lato) / 2;
				for (int y1 = 0; y1 < lato; y1++) {
					offset = (y1 + gap) * matrix;
					for (int x1 = 0; x1 < lato; x1++) {
						pixels1[offset + gap + x1] = myColor[i1];
						a1[i1] = y1 + gap;
					}
				}
			}
		}

		int wi2 = imp1.getWidth() / 2;
		for (int i1 = 0; i1 < a1.length; i1++) {
			ip1.drawString(myLabels[i1], wi2 - wi1 / 2, a1[i1]);
		}
		imp1.updateAndRepaintWindow();
		ip1.resetMinAndMax();
		imp1.updateImage();
		return imp1;
	}

	static public ImagePlus colorStrips(int[] myColor) {

		// genero una immagine nera
		int matrix = 256;
		ImageProcessor ip1 = new ColorProcessor(matrix, matrix);
		int[] pixels1 = (int[]) ip1.getPixels();
		ip1.resetMinAndMax();
		int lato = 0;
		int gap = 0;
		int offset = 0;
		int wi1 = ip1.getStringWidth("-10 +10");
		int[] a1 = new int[myColor.length];

		if (myColor.length > 5) {
			ip1.setColor(new Color(220, 220, 220));
			ip1.setFont(new Font("Arial", Font.TYPE1_FONT, 8));
		} else {
			ip1.setColor(new Color(10, 10, 10));
			ip1.setFont(new Font("Arial", Font.BOLD, 13));
		}

		ip1.setJustification(ImageProcessor.LEFT_JUSTIFY);
		ip1.setAntialiasedText(true);

		ImagePlus imp1 = new ImagePlus("Scala colori", ip1);
		lato = 200;
		for (int i1 = myColor.length - 1; i1 >= 0; i1--) {
			lato = (i1 + 1) * 45;
			gap = (matrix - lato) / 2;
			for (int y1 = 0; y1 < lato; y1++) {
				offset = (y1 + gap) * matrix;
				for (int x1 = 0; x1 < lato; x1++) {

					pixels1[offset + gap + x1] = myColor[i1];
					a1[i1] = y1 + gap;
				}
			}
		}

		imp1.updateAndRepaintWindow();
		ip1.resetMinAndMax();
		imp1.updateImage();
		return imp1;
	}

	/**
	 * Estrae la numerosita' delle classi dalla simulata
	 * 
	 * @param imp1 immagine simulata da analizzare
	 * @return numerosit� delle classi in cui per ogni elemento abbiamo
	 *         [valore][numerosit�]
	 */

	public static int[][] numeroPixelsClassi(ImagePlus imp1) {

		if (imp1 == null) {
			IJ.error("numeroPixelClassi ricevuto null");
			return (null);
		}
		int width = imp1.getWidth();
		int height = imp1.getHeight();

		ImageProcessor ip1 = imp1.getProcessor();
		short[] pixels1 = (short[]) ip1.getPixels();
		int offset = 0;
		int pix1 = 0;

		int[][] vetClassi = { { MyConst.LEVEL_12, 0 }, { MyConst.LEVEL_11, 0 }, { MyConst.LEVEL_10, 0 },
				{ MyConst.LEVEL_9, 0 }, { MyConst.LEVEL_8, 0 }, { MyConst.LEVEL_7, 0 }, { MyConst.LEVEL_6, 0 },
				{ MyConst.LEVEL_5, 0 }, { MyConst.LEVEL_4, 0 }, { MyConst.LEVEL_3, 0 }, { MyConst.LEVEL_2, 0 },
				{ MyConst.LEVEL_1, 0 } };
		boolean manca = true;

		for (int y1 = 0; y1 < height; y1++) {
			for (int x1 = 0; x1 < (width); x1++) {
				offset = y1 * width + x1;
				pix1 = pixels1[offset];
				manca = true;
				for (int i1 = 0; i1 < vetClassi.length; i1++)
					if (pix1 == vetClassi[i1][0]) {
						vetClassi[i1][1] = vetClassi[i1][1] + 1;
						manca = false;
						break;
					}
				if (manca) {
					ButtonMessages.ModelessMsg("SIMULATA CON VALORE ERRATO=" + pix1 + "   <38>", "CONTINUA");
					return (null);
				}
			}
		}
		return (vetClassi);

	} // classi

	public static void addOverlayRoi(ImagePlus imp1, Color color, double width) {
		Roi roi1 = imp1.getRoi();
		roi1.setStrokeColor(color);
		roi1.setStrokeWidth(width);
		Overlay over1 = imp1.getOverlay();
		if (over1 == null) {
			MyLog.waitThere("overlay null!!");
			return;
		}
		over1.add(roi1);
	}

	/***
	 * Porta l'immagine in primo piano Pare che questa routine funzioni molto meglio
	 * delle precedenti. Se, anziche' la ImagePlus si conosce la ImageWindow, si
	 * puo' utilizzare iw1.getImagePlus() per ottenere la image plus "on the fly"
	 * 
	 * @param iw1
	 */
	public static void imageToFront(ImageWindow iw1) {

		if (iw1 == null)
			return;
		ImagePlus imp1 = iw1.getImagePlus();
		if (imp1 == null)
			return;
		imageToFront(imp1);
	}

	/***
	 * Porta l'immagine in primo piano Pare che questa routine funzioni molto meglio
	 * delle precedenti. Se, anzich� la ImagePlus si conosce la ImageWindow, si
	 * pu� utilizzare iw1.getImagePlus() per ottenere la image plus "on the fly"
	 * 
	 * @param imp1
	 */
	public static void imageToFront(ImagePlus imp1) {

		ImageWindow iw1 = null;
		if (imp1.isVisible()) {
			iw1 = imp1.getWindow();
		} else {
			// MyLog.waitThere("ImagePlus non visualizzata, impossibile portarla
			// to front");
			return;
		}
		// String nome1 = iw1.toString();
		// IJ.selectWindow(nome1);

		WindowManager.setWindow(iw1);

		// String nome2 = w2.toString();
		// MyLog.waitHere("active= " + nome2);
	}

	/***
	 * Questo e' il fitCircle preso da ImageJ (ij.plugins.Selection.java, con
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
			ImageStatistics stats = ImageStatistics.getStatistics(ip, Measurements.AREA + Measurements.CENTROID, null);
			double r = Math.sqrt(stats.pixelCount / Math.PI);
			imp.killRoi();
			int d = (int) Math.round(2.0 * r);
			imp.setRoi(new OvalRoi((int) Math.round(stats.xCentroid - r), (int) Math.round(stats.yCentroid - r), d, d));

			// IJ.makeOval((int) Math.round(stats.xCentroid - r),
			// (int) Math.round(stats.yCentroid - r), d, d);
			return;
		}

		Polygon poly = roi.getPolygon();
		int n = poly.npoints;
		int[] x = poly.xpoints;
		int[] y = poly.ypoints;
		if (n < 3) {
			IJ.error("Fit Circle", "At least 3 points are required to fit a circle.");
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
		double A0 = Mxz2 * Myy + Myz2 * Mxx - Mzz * Cov_xy - 2 * Mxz * Myz * Mxy + Mz * Mz * Cov_xy;
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
			if (Math.abs((xnew - xold) / xnew) < epsilon) {
				// qui e'dove se le cose vanno bene, viene fatta terminare
				// l'iterazione
				break;
			}
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
			IJ.log("Fit Circle: n=" + n + ", xnew=" + IJ.d2s(xnew, 2) + ", iterations=" + iterations);

		// calculate the circle parameters
		double DET = xnew * xnew - xnew * Mz + Cov_xy;
		double CenterX = (Mxz * (Myy - xnew) - Myz * Mxy) / (2 * DET);
		double CenterY = (Myz * (Mxx - xnew) - Mxz * Mxy) / (2 * DET);
		double radius = Math.sqrt(CenterX * CenterX + CenterY * CenterY + Mz + 2 * xnew);
		if (Double.isNaN(radius)) {
			IJ.error("Fit Circle", "Points are collinear.");
			return;
		}

		CenterX = CenterX + meanx;
		CenterY = CenterY + meany;
		imp.deleteRoi();

		// messo imp.setRoi anziche' IJ.makeOval perche' permette di non
		// mostrare
		// l'immagine
		imp.setRoi(new OvalRoi((int) Math.round(CenterX - radius), (int) Math.round(CenterY - radius),
				(int) Math.round(2 * radius), (int) Math.round(2 * radius)));
	}

	/***
	 * Questo e' il fitCircle preso da ImageJ (ij.plugins.Selection.java, con
	 * sostituito imp.setRoi a IJ.makeOval. La versione NEW, oltre a impostare una
	 * ROI circolare sull'immagine, ne restituisce anche i valori numerici:
	 * coordinate del centro e raggio.
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
	public static double[] fitCircleNew(ImagePlus imp) {
		Roi roi = imp.getRoi();

		if (roi == null) {
			IJ.error("Fit Circle", "Selection required");
			return null;
		}

		if (roi.isArea()) { // create circle with the same area and centroid
			ImageProcessor ip = imp.getProcessor();
			ip.setRoi(roi);
			ImageStatistics stats = ImageStatistics.getStatistics(ip, Measurements.AREA + Measurements.CENTROID, null);
			double r = Math.sqrt(stats.pixelCount / Math.PI);
			imp.killRoi();
			int d = (int) Math.round(2.0 * r);
			imp.setRoi(new OvalRoi((int) Math.round(stats.xCentroid - r), (int) Math.round(stats.yCentroid - r), d, d));

			// IJ.makeOval((int) Math.round(stats.xCentroid - r),
			// (int) Math.round(stats.yCentroid - r), d, d);
			return null;
		}

		Polygon poly = roi.getPolygon();
		int n = poly.npoints;
		int[] x = poly.xpoints;
		int[] y = poly.ypoints;
		if (n < 3) {
			IJ.error("Fit Circle", "At least 3 points are required to fit a circle.");
			return null;
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
		double A0 = Mxz2 * Myy + Myz2 * Mxx - Mzz * Cov_xy - 2 * Mxz * Myz * Mxy + Mz * Mz * Cov_xy;
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
			if (Math.abs((xnew - xold) / xnew) < epsilon) {
				// qui e'dove se le cose vanno bene, viene fatta terminare
				// l'iterazione
				break;
			}
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
			IJ.log("Fit Circle: n=" + n + ", xnew=" + IJ.d2s(xnew, 2) + ", iterations=" + iterations);

		// calculate the circle parameters
		double DET = xnew * xnew - xnew * Mz + Cov_xy;
		double CenterX = (Mxz * (Myy - xnew) - Myz * Mxy) / (2 * DET);
		double CenterY = (Myz * (Mxx - xnew) - Mxz * Mxy) / (2 * DET);
		double radius = Math.sqrt(CenterX * CenterX + CenterY * CenterY + Mz + 2 * xnew);
		if (Double.isNaN(radius)) {
			IJ.error("Fit Circle", "Points are collinear.");
			return null;
		}

		CenterX = CenterX + meanx;
		CenterY = CenterY + meany;
		imp.deleteRoi();

		// messo imp.setRoi anziche' IJ.makeOval perche' permette di non
		// mostrare
		// l'immagine
		imp.setRoi(new OvalRoi((int) Math.round(CenterX - radius), (int) Math.round(CenterY - radius),
				(int) Math.round(2 * radius), (int) Math.round(2 * radius)));

		double[] out = new double[4];
		out[0] = CenterX;
		out[1] = CenterY;
		out[2] = radius;
		out[3] = DET;
		return out;
	}

	/***
	 * Liang-Barsky function by Daniel White
	 * http://www.skytopia.com/project/articles/compsci/clipping.html .This function
	 * inputs 8 numbers, and outputs 4 new numbers (plus a boolean value to say
	 * whether the clipped line is drawn at all). //
	 * 
	 * @param edgeLeft   lato sinistro, coordinata minima x = 0
	 * @param edgeRight  lato destro, coordinata max x = width
	 * @param edgeBottom lato inferiore, coordinata max y = height
	 * @param edgeTop    lato superiore, coordinata minima y = 0
	 * @param x0src      punto iniziale segmento
	 * @param y0src      punto iniziale segmento
	 * @param x1src      punto finale segmento
	 * @param y1src      punto finale segmento
	 * @return
	 */
	public static double[] liangBarsky(double edgeLeft, double edgeRight, double edgeBottom, double edgeTop,
			double x0src, double y0src, double x1src, double y1src) {

		double t0 = 0.0;
		double t1 = 1.0;
		double xdelta = x1src - x0src;
		double ydelta = y1src - y0src;
		double p = 0;
		double q = 0;
		double r = 0;
		double[] clips = new double[4];

		for (int edge = 0; edge < 4; edge++) { // Traverse through left, right,
												// bottom, top edges.
			if (edge == 0) {
				p = -xdelta;
				q = -(edgeLeft - x0src);
			}
			if (edge == 1) {
				p = xdelta;
				q = (edgeRight - x0src);
			}
			if (edge == 2) {
				p = -ydelta;
				q = -(edgeBottom - y0src);
			}
			if (edge == 3) {
				p = ydelta;
				q = (edgeTop - y0src);
			}
			r = q / p;
			if (p == 0 && q < 0) {
				IJ.log("null 001");
				return null; // Don't draw line at all. (parallel line outside)
			}
			if (p < 0) {
				if (r > t1) {
					IJ.log("null 002");
					return null; // Don't draw line at all.
				} else if (r > t0)
					t0 = r; // Line is clipped!
			} else if (p > 0) {
				if (r < t0) {
					IJ.log("null 003");
					return null; // Don't draw line at all.
				} else if (r < t1)
					t1 = r; // Line is clipped!
			}
		}

		double x0clip = x0src + t0 * xdelta;
		double y0clip = y0src + t0 * ydelta;
		double x1clip = x0src + t1 * xdelta;
		double y1clip = y0src + t1 * ydelta;

		clips[0] = x0clip;
		clips[1] = y0clip;
		clips[2] = x1clip;
		clips[3] = y1clip;

		return clips;
	}

	/**
	 * Determinazione dei crossing points tra un raggio, di cui si conoscono solo
	 * due punti e la circonferenza.
	 * https://stackoverflow.com/questions/13053061/circle-line-intersection- points
	 * 
	 * @param x0 coord x punto 0
	 * @param y0 coord y punto 0
	 * @param x1 coord x punto 1
	 * @param y1 coord y punto 1
	 * @param xc coord x centro
	 * @param yc coord y centro
	 * @param rc raggio
	 * @return
	 */
	public static double[] getCircleLineCrossingPoints(double x0, double y0, double x1, double y1, double xc, double yc,
			double rc) {

		double[] out = null;
		double bax = x1 - x0;
		double bay = y1 - y0;
		double cax = xc - x0;
		double cay = yc - y0;
		double a = bax * bax + bay * bay;
		double bby2 = bax * cax + bay * cay;
		double c = cax * cax + cay * cay - rc * rc;
		double pby2 = bby2 / a;
		double q = c / a;
		double disc = pby2 * pby2 - q;
		if (disc < 0)
			return null;

		double tmpSqrt = Math.sqrt(disc);
		double abScaling1 = -pby2 + tmpSqrt;
		double abScaling2 = -pby2 - tmpSqrt;
		double o1x = x0 - bax * abScaling1;
		double o1y = y0 - bay * abScaling1;
		if (disc == 0) {
			out = new double[2];
			out[0] = o1x;
			out[1] = o1y;
		}
		double o2x = x0 - bax * abScaling2;
		double o2y = y0 - bay * abScaling2;
		out = new double[4];
		out[0] = o1x;
		out[1] = o1y;
		out[2] = o2x;
		out[3] = o2y;
		return out;
	}

	/**
	 * Determinazione dei crossing points tra la retta della prosecuzione di un
	 * segmento ed i lati del frame. ATTENZIONE: si limita a trovare i punti di
	 * crossing, non li mette in ordine
	 * 
	 * @param x0     coordinata X inizio
	 * @param y0     coordinata Y inizio
	 * @param x1     coordinata X fine
	 * @param y1     coordinata Y fine
	 * @param width  larghezza immagine
	 * @param height altezza immagine
	 * @return vettore con coordinate clipping points
	 */
	public static double[] crossingFrame(double x0, double y0, double x1, double y1, double width, double height) {

		double[] out1 = MyGeometry.fromPointsToEquLineImplicit(x0, y0, x1, y1);

		// in out1 ottengo i valori di a,b,c da sostituire nella equazione
		// implicita della retta, nella forma ax+by+c=0

		// determinazione dei crossing points, in questi punti io conosco la x,
		// per i lati verticali e la y per gli orizzontali

		double tolerance = 1e-6;
		double a = out1[0];
		double b = out1[1];
		double c = out1[2];

		double x;
		double y;
		boolean upperLeftVertex = false;
		boolean upperRightVertex = false;
		boolean lowerLeftVertex = false;
		boolean lowerRightVertex = false;

		// MyLog.waitHere("a= " + a + " b= " + b + " c= " + c + " width= " +
		// width
		// + " height= " + height);

		double[] clippingPoints = new double[4];
		int count = 0;

		// ora andr� a calcolare il crossing per i vari lati dell'immagine. Mi
		// aspetto di avere due soli crossing. Esiste per� un eccezione � il
		// caso particolare in cui il crossing avviene esattamente su di un
		// angolo dell'immagine: in tal caso avr� che is between mi dar� il
		// crossing sia per il lato orizzontale che per il lato verticale, per
		// cui mi trover� con 3 crossing. Nel caso ancora pi� particolare di una
		// diagonale del quadrato mi trover� con quattro crossing, anzich� due.
		// ed io devo passare ad imageJ le coordinate di solo due punti.

		// lato superiore
		y = 0;
		x = -(b * y + c) / a;

		// IJ.log("lato superiore x= " + x + " y= " + y);

		upperLeftVertex = UtilAyv.myTestEquals(x, 0D, tolerance);
		upperRightVertex = UtilAyv.myTestEquals(x, width, tolerance);
		if (isBetween(x, 0, width, tolerance)) {
			if (count <= 2) {
				clippingPoints[count++] = x;
				clippingPoints[count++] = y;
			} else {
				MyLog.waitHere("001 ERROR count= " + count);
				return null;
			}
		}

		// lato inferiore
		y = height;
		x = -(b * y + c) / a;
		// IJ.log("lato inferiore x= " + x + " y= " + y);
		lowerLeftVertex = UtilAyv.myTestEquals(x, 0D, tolerance);
		lowerRightVertex = UtilAyv.myTestEquals(x, width, tolerance);

		if (isBetween(x, 0, width, tolerance)) {
			if (count <= 2) {
				clippingPoints[count++] = x;
				clippingPoints[count++] = y;
			} else {
				MyLog.waitHere("002 ERROR count= " + count);
				return null;
			}
		}

		// lato sinistro
		x = 0;
		y = -(a * x + c) / b;
		// IJ.log("lato sinistro x= " + x + " y= " + y);
		if (isBetween(y, 0, height, tolerance) && (!upperLeftVertex) && (!lowerLeftVertex)) {
			// if (isBetween(y, 0, height, tolerance)) {
			if (count <= 2) {
				clippingPoints[count++] = x;
				clippingPoints[count++] = y;
			} else {
				MyLog.waitHere("003 ERROR count= " + count);
				return null;
			}
		}

		// lato destro
		x = width;
		y = -(a * x + c) / b;
		// IJ.log("lato destro x= " + x + " y= " + y);
		if (isBetween(y, 0, height, tolerance) && (!upperRightVertex) && (!lowerRightVertex)) {
			// if (isBetween(y, 0, height, tolerance)) {
			if (count <= 2) {
				clippingPoints[count++] = x;
				clippingPoints[count++] = y;
			} else {
				MyLog.waitHere("004 ERROR count= " + count);
				return null;
			}
		}
		return clippingPoints;
	}

	/**
	 * Verifica se un valore e' all'interno dei limiti assegnati, con una certa
	 * tolleranza
	 * 
	 * @param x1        valore calcolato
	 * @param low       limite inferiore
	 * @param high      limite superiore
	 * @param tolerance tolleranza
	 * @return true se il valore � valido (entro i limiti)
	 */
	public static boolean isBetween(double x1, double low, double high, double tolerance) {

		if (low < high) {
			return ((x1 >= (low - tolerance)) && (x1 <= (high + tolerance)));
		} else {
			return ((x1 >= (high - tolerance)) && (x1 <= (low + tolerance)));
		}
	}

	/**
	 * Calcolo della distanza tra un punto ed una circonferenza
	 * 
	 * @param x1 coord. x punto
	 * @param y1 coord. y punto
	 * @param x2 coord. x centro
	 * @param y2 coord. y centro
	 * @param r2 raggio
	 * @return distanza
	 */
	public static double pointCirconferenceDistance(int x1, int y1, int x2, int y2, int r2) {

		double dist = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)) - r2;
		return dist;
	}

	/**
	 * Disegna una serie di punti nell'overlay di una immagine
	 * 
	 * @param imp1
	 * @param over1
	 * @param peaks1
	 */
	public static void plotPoints(ImagePlus imp1, Overlay over1, double[][] peaks1) {

		float[] xPoints = new float[peaks1[0].length];
		float[] yPoints = new float[peaks1[0].length];

		for (int i1 = 0; i1 < peaks1[0].length; i1++) {
			xPoints[i1] = (float) peaks1[0][i1];
			yPoints[i1] = (float) peaks1[1][i1];
		}

		// MyLog.logVector(xPoints, "xPoints");
		// MyLog.logVector(yPoints, "yPoints");
		PointRoi pr1 = new PointRoi(xPoints, yPoints, xPoints.length);
		pr1.setPointType(2);
		pr1.setSize(4);

		imp1.setRoi(pr1);
		imp1.getRoi().setStrokeColor(Color.green);
		over1.addElement(imp1.getRoi());
	}

	public static void plotPoints(ImagePlus imp1, Overlay over1, int[] xPoints1, int[] yPoints1) {

		if (xPoints1 == null)
			return;

		MyLog.logVector(xPoints1, "xPoints1");
		MyLog.logVector(yPoints1, "yPoints1");

		float[] xPoints = new float[xPoints1.length];
		float[] yPoints = new float[yPoints1.length];

		for (int i1 = 0; i1 < xPoints1.length; i1++) {
			xPoints[i1] = (float) xPoints1[i1];
			yPoints[i1] = (float) yPoints1[i1];
		}
		MyLog.logVector(xPoints, "xPoints");
		MyLog.logVector(yPoints, "yPoints");
		MyLog.waitHere();

		PointRoi pr1 = new PointRoi(xPoints, yPoints, xPoints.length);
		pr1.setPointType(2);
		pr1.setSize(4);

		imp1.setRoi(pr1);
		imp1.getRoi().setStrokeColor(Color.green);
		over1.addElement(imp1.getRoi());
		return;
	}

	public static void plotPoints(ImagePlus imp1, Overlay over1, int xPoints1, int yPoints1) {

		float[] xPoints = new float[1];
		float[] yPoints = new float[1];

		xPoints[0] = (float) xPoints1;
		yPoints[0] = (float) yPoints1;
		// MyLog.logVector(xPoints, "xPoints");
		// MyLog.logVector(yPoints, "yPoints");
		// MyLog.waitHere();

		PointRoi pr1 = new PointRoi(xPoints, yPoints, xPoints.length);
		pr1.setPointType(2);
		pr1.setSize(4);

		imp1.setRoi(pr1);
		imp1.getRoi().setStrokeColor(Color.green);
		over1.addElement(imp1.getRoi());
		return;
	}

	public static void plotPoints(ImagePlus imp1, Overlay over1, int xPoints1, int yPoints1, Color color) {

		float[] xPoints = new float[1];
		float[] yPoints = new float[1];

		xPoints[0] = (float) xPoints1;
		yPoints[0] = (float) yPoints1;
		// MyLog.logVector(xPoints, "xPoints");
		// MyLog.logVector(yPoints, "yPoints");
		// MyLog.waitHere();

		PointRoi pr1 = new PointRoi(xPoints, yPoints, xPoints.length);
		pr1.setPointType(2);
		pr1.setSize(4);

		imp1.setRoi(pr1);
		imp1.getRoi().setStrokeColor(color);
		over1.addElement(imp1.getRoi());
		return;
	}

	public static void plotPoints(ImagePlus imp1, Overlay over1, int xPoints1, int yPoints1, Color color,
			double width) {

		float[] xPoints = new float[1];
		float[] yPoints = new float[1];

		xPoints[0] = (float) xPoints1;
		yPoints[0] = (float) yPoints1;
		// MyLog.logVector(xPoints, "xPoints");
		// MyLog.logVector(yPoints, "yPoints");
		// MyLog.waitHere();

		PointRoi pr1 = new PointRoi(xPoints, yPoints, xPoints.length);
		pr1.setPointType(2);
		pr1.setSize(2);

		imp1.setRoi(pr1);
		imp1.getRoi().setStrokeColor(color);
		imp1.getRoi().setStrokeWidth(width);
		over1.addElement(imp1.getRoi());
		return;
	}

	/**
	 * Copied from http://billauer.co.il/peakdet.html Peak Detection using MATLAB
	 * Author: Eli Billauer Vedi anche https://github.com/xuphys/peakdetect
	 * 
	 * 
	 * @param profile profilo da analizzare
	 * @param delta
	 * @return ArrayList con le posizioni del picco minimo e del picco massimo
	 */
	public static ArrayList<ArrayList<Double>> peakDet(double[][] profile, double delta) {

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
		// matout.add(mintabx);
		// matout.add(mintaby);
		matout.add(maxtabx);
		matout.add(maxtaby);

		return matout;
	}

	/***
	 * per intercambiabilit� con peakDet2 restituisco un ArrayList<ArrayList<>>
	 * anche se mi propongo di trovare solo UN massimo e basta
	 * 
	 * @param profile
	 * @param delta
	 * @return
	 */

	public static ArrayList<ArrayList<Double>> peakDet1(double[][] profile, double delta) {

		ArrayList<ArrayList<Double>> matout = new ArrayList<ArrayList<Double>>();

		ArrayList<Double> maxtabx = new ArrayList<Double>();
		ArrayList<Double> maxtaby = new ArrayList<Double>();
		ArrayList<Double> maxtabz = new ArrayList<Double>();
		// ArrayList<Double> mintabx = new ArrayList<Double>();
		// ArrayList<Double> mintaby = new ArrayList<Double>();
		// ArrayList<Double> mintabz = new ArrayList<Double>();

		double max = Double.MIN_VALUE;

		double[] vetx = new double[profile[0].length];
		double[] vety = new double[profile[0].length];
		double[] vetz = new double[profile[0].length];
		for (int i1 = 0; i1 < profile[0].length; i1++) {
			vetx[i1] = profile[0][i1];
			vety[i1] = profile[1][i1];
			vetz[i1] = profile[2][i1];
		}

		// ora ho i due vettori. Si tratta di trovare il max di vetz e
		// determinare i corrispondenti valore di vetx e vety

		double maxposx = -1.0;
		double maxposy = -1.0;
		boolean validMax = false;

		for (int i1 = 0; i1 < vetx.length; i1++) {
			if (vetz[i1] < delta) {
				validMax = true;
			}
			if (vetz[i1] > max && validMax) {
				max = vetz[i1];
				maxposx = vetx[i1];
				maxposy = vety[i1];
			}
		}

		// pongo delle condizioni al max trovato:
		// 1) deve essere superiore a delta
		// 2) il segnale, prima del max deve essere stato inferiore a delta
		// almeno 1 pixel (vedi validMax)

		if (max > delta) {
			maxtabx.add((Double) maxposx);
			maxtaby.add((Double) maxposy);
			maxtabz.add((Double) max);
		} else {
		}

		matout.add(maxtabx);
		matout.add(maxtaby);
		matout.add(maxtabz);

		return matout;
	}

	/**
	 * Copied from http://billauer.co.il/peakdet.htm Peak Detection using MATLAB
	 * Author: Eli Billauer Riceve in input un profilo di una linea, costituito da
	 * una matrice con i valori x, y , z di ogni punto. Restituisce le coordinate x,
	 * y, z degli eventuali minimi e maximi
	 * 
	 * @param profile
	 * @param delta
	 * @return
	 */
	public static ArrayList<ArrayList<Double>> peakDet2(double[][] profile, double delta) {

		// for (int i1 = 0; i1 < profile[0].length; i1++) {
		// IJ.log(""+profile[0][i1]+ "; "+profile[1][i1]+ "; "+profile[2][i1]);
		// }
		// MyLog.waitHere();

		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		ArrayList<ArrayList<Double>> matout = new ArrayList<ArrayList<Double>>();

		ArrayList<Double> maxtabx = new ArrayList<Double>();
		ArrayList<Double> maxtaby = new ArrayList<Double>();
		ArrayList<Double> maxtabz = new ArrayList<Double>();
		ArrayList<Double> mintabx = new ArrayList<Double>();
		ArrayList<Double> mintaby = new ArrayList<Double>();
		ArrayList<Double> mintabz = new ArrayList<Double>();
		double[] vetx = new double[profile[0].length];
		double[] vety = new double[profile[0].length];
		double[] vetz = new double[profile[0].length];
		for (int i1 = 0; i1 < profile[0].length; i1++) {
			vetx[i1] = profile[0][i1];
			vety[i1] = profile[1][i1];
			vetz[i1] = profile[2][i1];
		}
		double maxposx = -1.0;
		double minposx = -1.0;
		double maxposy = -1.0;
		double minposy = -1.0;
		boolean lookformax = true;

		for (int i1 = 0; i1 < vetz.length; i1++) {
			if (vetz[i1] > max) {
				max = vetz[i1];
				maxposx = vetx[i1];
				maxposy = vety[i1];
			}
			if (vetz[i1] < min) {
				min = vetz[i1];
				minposx = vetx[i1];
				minposy = vety[i1];
			}

			stateChange(lookformax);

			if (lookformax) {
				if (vetz[i1] < (max - delta)) {
					min = vetz[i1];
					minposx = vetx[i1];
					minposy = vety[i1];
					maxtabx.add((Double) maxposx);
					maxtaby.add((Double) maxposy);
					maxtabz.add((Double) max);
					lookformax = false;
				}
			} else {
				if (vetz[i1] > (min + delta)) {
					// if (valy > min + delta + mean1 * 10) {
					max = vetz[i1];
					// -------------------------------
					// aggiungo 0.5 alle posizioni trovate
					// -------------------------------
					maxposx += .5;
					maxposy += .5;
					maxposx = vetx[i1];
					maxposy = vety[i1];
					mintabx.add((Double) minposx);
					mintaby.add((Double) minposy);
					mintabz.add((Double) min);
					lookformax = true;

				}
			}
		}
		// MyLog.logArrayList(mintabx, "############## mintabx #############");
		// MyLog.logArrayList(mintaby, "############## mintaby #############");
		// MyLog.logArrayList(mintabz, "############## mintabz #############");
		// MyLog.logArrayList(maxtabx, "############## maxtabx #############");
		// MyLog.logArrayList(maxtaby, "############## maxtaby #############");
		// MyLog.logArrayList(maxtabz, "############## maxtabz #############");

		// tolgo i minimi, che non mi interessano del resto, altrimenti posso
		// trovarmi un numero diverso di
		// massimi e minimi ed avere guai nel creare la struttura dati per la
		// restituzione dei risultati

		// matout.add(mintabx);
		// matout.add(mintaby);
		// matout.add(mintabz);

		matout.add(maxtabx);
		matout.add(maxtaby);
		matout.add(maxtabz);

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

	public static void autoAdjust(ImagePlus imp) {
		double min, max;

		ImageProcessor ip = imp.getProcessor();
		Calibration cal = imp.getCalibration();
		imp.setCalibration(null);
		ImageStatistics stats = imp.getStatistics();
		imp.setCalibration(cal);
		int[] histogram = stats.histogram;
		int threshold = stats.pixelCount / 5000;
		int i = -1;
		boolean found = false;
		do {
			i++;
			found = histogram[i] > threshold;
		} while (!found && i < 255);
		int hmin = i;
		i = 256;
		do {
			i--;
			found = histogram[i] > threshold;
		} while (!found && i > 0);
		int hmax = i;
		if (hmax > hmin) {
			imp.killRoi();
			min = stats.histMin + hmin * stats.binSize;
			max = stats.histMin + hmax * stats.binSize;
			ip.setMinAndMax(min, max);
		}
		Roi roi = imp.getRoi();
		if (roi != null) {
			ImageProcessor mask = roi.getMask();
			if (mask != null)
				ip.reset(mask);
		}
	}

	/**
	 * esegue l'autoAdjust del contrasto immagine
	 * 
	 * Author Terry Wu, Ph.D., University of Minnesota, <JavaPlugins@yahoo.com>
	 * (from ij.plugin.frame. ContrastAdjuster by Wayne Rasband
	 * <wayne@codon.nih.gov>)*** modified version *** Alberto Duina - Spedali Civili
	 * di Brescia - Servizio di Fisica Sanitaria 2006
	 * 
	 * 
	 * @param imp ImagePlus da regolare
	 * @param ip  ImageProcessor dell'immagine
	 * 
	 */
	public static void autoAdjust(ImagePlus imp, ImageProcessor ip) {
		double min, max;

		Calibration cal = imp.getCalibration();
		imp.setCalibration(null);
		ImageStatistics stats = imp.getStatistics();
		imp.setCalibration(cal);
		int[] histogram = stats.histogram;
		int threshold = stats.pixelCount / 5000;
		int i = -1;
		boolean found = false;
		do {
			i++;
			found = histogram[i] > threshold;
		} while (!found && i < 255);
		int hmin = i;
		i = 256;
		do {
			i--;
			found = histogram[i] > threshold;
		} while (!found && i > 0);
		int hmax = i;
		if (hmax > hmin) {
			imp.killRoi();
			min = stats.histMin + hmin * stats.binSize;
			max = stats.histMin + hmax * stats.binSize;
			ip.setMinAndMax(min, max);
		}
		Roi roi = imp.getRoi();
		if (roi != null) {
			ImageProcessor mask = roi.getMask();
			if (mask != null)
				ip.reset(mask);
		}
	}

	/**
	 * esegue posizionamento e calcolo roi circolare sul fondo
	 * 
	 * @param xRoi  coordinata x roi
	 * @param yRoi  coordinata y roi
	 * @param imp   puntatore ImagePlus alla immagine
	 * @param bstep funzionamento passo passo
	 * @return dati statistici
	 */
	public static ImageStatistics backCalc(int xRoi, int yRoi, int diaRoi, ImagePlus imp, boolean bstep,
			boolean circular, boolean selftest) {

		ImageStatistics stat = null;
		boolean redo = false;
		int i1 = 0;
		do {
			i1++;
			if (imp.isVisible())
				imp.getWindow().toFront();
			if (circular)
				imp.setRoi(new OvalRoi(xRoi, yRoi, diaRoi, diaRoi));
			else
				imp.setRoi(xRoi, yRoi, diaRoi, diaRoi);

			if (!selftest) {
				if (redo) {
					ButtonMessages.ModelessMsg("ATTENZIONE segnale medio fondo =0 SPOSTARE LA ROI E PREMERE CONTINUA",
							"CONTINUA");

				} else {
					ButtonMessages.ModelessMsg("Posizionare ROI fondo e premere CONTINUA", "CONTINUA " + i1);
				}
			}
			stat = imp.getStatistics();
			if (stat.mean == 0)
				redo = true;
			else
				redo = false;
			if (bstep)
				ButtonMessages.ModelessMsg("Segnale medio =" + stat.mean, "CONTINUA");
		} while (redo);
		return stat;
	} // backCalc

	/**
	 * esegue posizionamento e calcolo roi circolare sul fondo
	 * 
	 * @param xRoi  coordinata x roi
	 * @param yRoi  coordinata y roi
	 * @param imp   puntatore ImagePlus alla immagine
	 * @param bstep funzionamento passo passo
	 * @return dati statistici
	 */
	public static ImageStatistics backCalc3(int xRoi, int yRoi, int diaRoi, ImagePlus imp, boolean bstep,
			boolean circular, boolean selftest) {

		ImageStatistics stat = null;
		boolean redo = false;
		int i1 = 0;
		do {
			i1++;
			if (imp.isVisible())
				imp.getWindow().toFront();
			if (circular)
				imp.setRoi(new OvalRoi(xRoi, yRoi, diaRoi, diaRoi));
			else
				imp.setRoi(xRoi, yRoi, diaRoi, diaRoi);

			ImageUtils.autoAdjust(imp, imp.getProcessor());

			if (circular)
				imp.setRoi(new OvalRoi(xRoi, yRoi, diaRoi, diaRoi));
			else
				imp.setRoi(xRoi, yRoi, diaRoi, diaRoi);

			if (!selftest) {
				if (redo) {
					ButtonMessages.ModelessMsg("ATTENZIONE segnale medio fondo =0 SPOSTARE LA ROI E PREMERE CONTINUA",
							"CONTINUA");

				} else {
					ButtonMessages.ModelessMsg("Posizionare ROI fondo e premere CONTINUA", "CONTINUA " + i1);
				}
			}
			stat = imp.getStatistics();
			if (stat.mean == 0)
				redo = true;
			else
				redo = false;
			if (bstep)
				ButtonMessages.ModelessMsg("Segnale medio =" + stat.mean, "CONTINUA");
		} while (redo);
		return stat;
	} // backCalc

	/**
	 * esegue posizionamento e calcolo roi circolare sul fondo
	 * 
	 * @param xRoi  coordinata x roi
	 * @param yRoi  coordinata y roi
	 * @param imp   puntatore ImagePlus alla immagine
	 * @param bstep funzionamento passo passo
	 * @return dati statistici
	 */
	public static ImageStatistics backCalc2(int xRoi, int yRoi, int diaRoi, ImagePlus imp, boolean bstep,
			boolean circular, boolean selftest) {

		ImageStatistics stat = null;
		boolean redo = false;
		boolean debug = true;

		int yIncr = 0;

		do {
			if (imp.isVisible())
				imp.getWindow().toFront();
			if (circular) {
				// imp.setRoi(new OvalRoi(xRoi - diaRoi / 2, yRoi - diaRoi / 2,
				// diaRoi, diaRoi));
				imp.setRoi(new OvalRoi(xRoi, yRoi + yIncr, diaRoi, diaRoi));
				// imp.updateAndDraw();
			} else {
				// imp.setRoi(xRoi - diaRoi / 2, yRoi - diaRoi / 2, diaRoi,
				// diaRoi);
				imp.setRoi(xRoi, yRoi + yIncr, diaRoi, diaRoi);
				// imp.updateAndDraw();
			}

			if (!selftest) {
				if (redo) {
					MyLog.waitHere("ATTENZIONE segnale medio fondo =0 spostare la ROI e premere OK", debug);

				}
			}
			stat = imp.getStatistics();
			if (stat.mean == 0) {
				redo = true;
				yIncr = yIncr + 10;
			} else
				redo = false;
		} while (redo);
		return stat;
	}

	/**
	 * evidenzia il fondo, richiede una roi sul fondo
	 * 
	 * @param xRoi coordinata x centro
	 * @param yRoi coordinata y centro
	 * @param imp1 puntatore ImagePlus alla immagine
	 */
	public static void backgroundEnhancement(int xRoi, int yRoi, int diaRoi, ImagePlus imp1) {

		ImageUtils.autoAdjust(imp1, imp1.getProcessor());
		imp1.updateAndDraw();
		imp1.getWindow().toFront();
	} // backgroundEnhancement

	/**
	 * messaggio mancanza test2.jar
	 * 
	 */
	public static void noTest2() {

		ButtonMessages.ModelessMsg("Per questa funzione bisogna installare test2.jar (albertoduina@virgilio.it)",
				"CONTINUA");
	}

	/***
	 * restituisce coordinate centro e lato/raggio della ROI su imp1
	 * 
	 * @param imp1
	 * @return
	 */

	public static double[] roiCenter(ImagePlus imp1) {
		Rectangle boundRec1 = imp1.getProcessor().getRoi();
		double leftHX = boundRec1.x;
		double leftHY = boundRec1.y;
		double centerX = boundRec1.getCenterX();
		double centerY = boundRec1.getCenterY();
		double width1 = boundRec1.getWidth();
		double height1 = boundRec1.getHeight();
		Roi cazz1 = imp1.getRoi();
		String aux1 = cazz1.getTypeAsString();

		ImageStatistics statGh2 = imp1.getStatistics();
		String aux2 = statGh2.toString();
		// MyLog.waitHere("aux1= " + aux1 + " aux2= " + aux2);
		return null;
	}

	// public static void fillRoiPixels(ImagePlus imp1, int newValue) {
	// ImageProcessor ip1 = imp1.getProcessor();
	// int[] pixels = (int[]) ip1.getPixels();
	// // byte[] pixels = (byte[]) ip.getPixels();
	// int width = ip1.getWidth();
	// Rectangle r1 = ip1.getRoi();
	// int aux1;
	// ImageProcessor mask1 = ip1.getMask();
	// for (int y1 = r1.y; y1 < (r1.y + r1.height); y1++) {
	// for (int x1 = r1.x; x1 < (r1.x + r1.width); x1++) {
	// aux1= x1 - r1.x, y1 - r1.y
	//
	// if (mask1 == null || mask1.getPixel(x1 - r1.x, y1 - r1.y) != 0) {
	// mask1.
	// }
	//
	// pixels[i1] = newValue;
	//
	//
	// ;
	// byte[] maskPix = ip1.getMaskArray();
	// for (int i1 = 0; i1 < pixels.length; i1++) {
	// if (maskPix[i1] != 0)
	// pixels[i1] = newValue;
	// }
	// ip1.resetMinAndMax();
	// imp1.updateImage();
	// }

	public void demoPixelPertainRoiTest(ImageProcessor ip) {
		/**
		 * questo � solo un appunto sui metodi che possiamo utilizzare per stabilire
		 * se un pixel � all'interno di una ROI.
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

	public static ImagePlus generaStandardDeviationImage(ImagePlus imp1, int lato) {

		if (imp1 == null) {
			IJ.error("generaStandardDeviationImage ricevuto null");
			return (null);
		}
		int width = imp1.getWidth();
		int height = imp1.getHeight();
		ImagePlus impSDeviation = NewImage.createFloatImage("devSTimage", width, width, 1, NewImage.FILL_BLACK);
		FloatProcessor ipSDeviation = (FloatProcessor) impSDeviation.getProcessor();
		int border = lato / 2;
		lato = border * 2;
		ImageStatistics stat1 = null;
		for (int y1 = border; y1 < height - border; y1++) {
			for (int x1 = border; x1 < width - border; x1++) {
				imp1.setRoi(x1, y1, lato, lato);
				stat1 = imp1.getStatistics();
				ipSDeviation.putPixelValue(x1 + border, y1 + border, stat1.stdDev);
			}
		}

		ipSDeviation.resetMinAndMax();
		impSDeviation.updateAndDraw();
		return impSDeviation;
	}

	/**
	 * Imposta il colore del pixel di un overlay
	 * 
	 * @param imp1 image plus a cui è riferito l'overlay
	 * @param x1   coordinata x pixel
	 * @param y1   coordinata y pixel
	 * @param col1 colore 1
	 * @param col2 colorie 2
	 * @param ok   boolean scelta colore
	 */
	public static void setOverlayPixels(ImagePlus imp1, int x1, int y1, Color col1, Color col2, boolean ok) {
		Overlay over1 = imp1.getOverlay();
		if (over1 == null)
			return;
		imp1.setRoi(x1, y1, 1, 1);
		if (ok) {
			imp1.getRoi().setStrokeColor(col1);
			imp1.getRoi().setFillColor(col1);
		} else {
			imp1.getRoi().setStrokeColor(col1);
			imp1.getRoi().setFillColor(col2);
		}
		over1.addElement(imp1.getRoi());
		imp1.deleteRoi();
	}

	public static ImagePlus createStripRGB(int[][] colorMap) {

		int width = colorMap.length * 20;
		int height = colorMap.length * 20;
		;
		int slices = 1;
		String title = "R-G-B";
		int fill = ImageProcessor.BLACK;

		ImagePlus impOut = NewImage.createRGBImage(title, width, height, slices, fill);
		ImageProcessor ipOut = impOut.getProcessor();
		int[] pixelsOut = (int[]) ipOut.getPixels();
		int offset = 0;
		int z1 = -1;
		int w1 = -1;
		int colorRGB = 0;
		int stepx = height / colorMap.length;
		int stepy = height / colorMap.length;
		// MyLog.waitHere("steps= " + steps + " len=" + colorMap.length);

		for (int y1 = 0; y1 < height; y1++) {
			if ((y1 % stepy) == 0) {
				z1++;
			}
			offset = y1 * width;
			for (int x1 = 0; x1 < width; x1++) {
				if ((x1 % stepx) == 0) {
					w1++;
				}
				if (w1 > colorMap.length - 1)
					w1 = 0;

				colorRGB = ((colorMap[w1][0] & 0xff) << 16) | ((colorMap[w1][1] & 0xff) << 8)
						| (colorMap[w1][2] & 0xff);
				pixelsOut[offset + x1] = colorRGB;
			}
		}
		impOut.resetDisplayRange();

		return impOut;
	}

	public static void closeImageWindow(ImagePlus imp1) {
		if (imp1.isVisible()) {
			ImageWindow iw1 = imp1.getWindow();
			iw1.close();
		}
	}

}
