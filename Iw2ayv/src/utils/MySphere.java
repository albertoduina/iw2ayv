package utils;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Rectangle;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.gui.ImageWindow;
import ij.gui.Line;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.gui.Plot;
import ij.gui.PlotWindow;
import ij.gui.PointRoi;
import ij.gui.Roi;
import ij.plugin.Duplicator;
import ij.plugin.Orthogonal_Views;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;

public class MySphere {

	/**
	 * Ricerca posizione ROI per calcolo uniformita'. Versione con Canny Edge
	 * Detector. Questa versione è da utilizzare per il fantoccio sferico
	 * 
	 * @param imp11
	 *            stack della sfera in input
	 * @return
	 */
	public static double[] centerCircleCannyEdge(ImagePlus imp11, int direction, double maxFitError,
			double maxBubbleGapLimit, boolean demo) {

		// ================================================================================
		// Inizio calcoli geometrici
		// ================================================================================
		//

		Color colore1 = Color.red;
		Color colore2 = Color.green;
		Color colore3 = Color.red;
		boolean debug;
		boolean demo0;
		boolean demo1;
		debug = demo;
		demo0 = demo;
		demo1 = demo;
		int timeout = 5000;
		int xCenterCircle = 0;
		int yCenterCircle = 0;
		int diamCircle = 0;
		int height = imp11.getHeight();
		int width = imp11.getWidth();

		// ----------------------------------------------------------
		// non posso stabilire la direzione utilizzando l'header dicom, poiche'
		// analizzo anche gli output di Zprojection quindi li ricevo
		// dall'esterno E BASTA!
		// ----------------------------------------------------------

		Overlay over12 = new Overlay();

		// utilizzo il MyCannyEdgeDetector per ricavare il profilo esterno del
		// fantoccio, che dovrebbe essere di spessore 1 pixel e intensità 255
		MyCannyEdgeDetector mce = new MyCannyEdgeDetector();
		mce.setGaussianKernelRadius(2.0f);
		mce.setLowThreshold(15.0f);
		mce.setHighThreshold(16.0f);
		mce.setContrastNormalized(false);

		ImagePlus imp12 = mce.process(imp11);
		imp12.setOverlay(over12);
		if (demo)
			UtilAyv.showImageMaximized(imp12);

		ImageStatistics stat12 = imp12.getStatistics();
		if (stat12.max < 255) {
			return null;
		}

		if (demo1)
			MyLog.waitHere("--- 000 ---\noutput CannyEdgeDetector");

		double[][] myPeaks = new double[4][1];
		ArrayList<Integer> xpoints = new ArrayList<Integer>();
		ArrayList<Integer> ypoints = new ArrayList<Integer>();

		int[] xcoord = new int[2];
		int[] ycoord = new int[2];
		// ----------------------------------------------------------------------
		// queste saranno le coordinate delle 8 linee, confidenzialmente dette
		// diagonali, con cui analizzeremo la immagine di uscita dal canny edge
		// detector
		// ----------------------------------------------------------------------
		int[] vetx0 = new int[8];
		int[] vetx1 = new int[8];
		int[] vety0 = new int[8];
		int[] vety1 = new int[8];

		vetx0[0] = 0;
		vety0[0] = height / 2;
		vetx1[0] = width;
		vety1[0] = height / 2;
		// ----
		vetx0[1] = width / 2;
		vety0[1] = 0;
		vetx1[1] = width / 2;
		vety1[1] = height;
		// ----
		vetx0[2] = 0;
		vety0[2] = 0;
		vetx1[2] = width;
		vety1[2] = height;
		// -----
		vetx0[3] = width;
		vety0[3] = 0;
		vetx1[3] = 0;
		vety1[3] = height;
		// -----
		vetx0[4] = width / 4;
		vety0[4] = 0;
		vetx1[4] = width * 3 / 4;
		vety1[4] = height;
		// ----
		vetx0[5] = width * 3 / 4;
		vety0[5] = 0;
		vetx1[5] = width / 4;
		vety1[5] = height;
		// ----
		vetx0[6] = width;
		vety0[6] = height * 1 / 4;
		vetx1[6] = 0;
		vety1[6] = height * 3 / 4;
		// ----
		vetx0[7] = 0;
		vety0[7] = height * 1 / 4;
		vetx1[7] = width;
		vety1[7] = height * 3 / 4;

		// -------------------------------
		// passiamo all'analisi delle 8 diagonali
		// multipurpose line analyzer
		// -------------------------------
		for (int i1 = 0; i1 < 8; i1++) {
			xcoord[0] = vetx0[i1];
			ycoord[0] = vety0[i1];
			xcoord[1] = vetx1[i1];
			ycoord[1] = vety1[i1];
			imp12.setRoi(new Line(xcoord[0], ycoord[0], xcoord[1], ycoord[1]));
			if (demo0) {
				imp12.getRoi().setStrokeColor(colore2);
				over12.addElement(imp12.getRoi());
				imp12.updateAndDraw();
			}

			myPeaks = cannyProfileAnalyzer(imp12, demo0, debug, timeout);

			if (myPeaks != null) {
				for (int i2 = 0; i2 < myPeaks[0].length; i2++) {
					if (direction == 2 && i1 == 0 && i2 == 0)
						continue; // evitiamo l'eventuale bolla d'aria
					if (direction == 1 && i1 == 1 && i2 == 0)
						continue; // evitiamo l'eventuale bolla d'aria
					xpoints.add((int) (myPeaks[3][i2]));
					ypoints.add((int) (myPeaks[4][i2]));

					ImageUtils.plotPoints(imp12, over12, (int) (myPeaks[3][i2]), (int) (myPeaks[4][i2]), colore1, 1);
					ImageUtils.plotPoints(imp12, over12, (int) (myPeaks[3][i2]), (int) (myPeaks[4][i2]), colore1, 0);
					imp12.updateAndDraw();
					ImageUtils.imageToFront(imp12);
				}
			}
		}
		if (demo0)
			MyLog.waitHere("--- 001 ---\nlinee analisi cannyProfileAnalyzer2");

		over12.clear();
		int[] myXpoints = ArrayUtils.arrayListToArrayInt(xpoints);
		int[] myYpoints = ArrayUtils.arrayListToArrayInt(ypoints);
		PointRoi pr12 = new PointRoi(myXpoints, myYpoints, myXpoints.length);
		pr12.setPointType(2);
		pr12.setSize(2);
		imp12.setRoi(pr12);

		if (demo0) {
			ImageUtils.addOverlayRoi(imp12, colore1, 3.1);
			pr12.setPointType(2);
			pr12.setSize(2);

		}
		// ---------------------------------------------------
		// eseguo ora fitCircle per trovare centro e dimensione del
		// fantoccio
		// ---------------------------------------------------
		if (myXpoints.length < 3) {
			// se avessi meno di 3 punti non posso trovare un cerchio
			ImageWindow iw112 = imp12.getWindow();
			if (iw112 != null)
				iw112.dispose();
			ImageWindow iw111 = imp11.getWindow();
			if (iw111 != null)
				iw111.dispose();
			return null;
		}

		ImageUtils.fitCircle(imp12);
		imp12.getRoi().setStrokeColor(colore1);
		over12.addElement(imp12.getRoi());
		Rectangle boundRec = imp12.getProcessor().getRoi();
		xCenterCircle = Math.round(boundRec.x + boundRec.width / 2);
		yCenterCircle = Math.round(boundRec.y + boundRec.height / 2);
		diamCircle = boundRec.width;
		MyCircleDetector.drawCenter(imp12, over12, xCenterCircle, yCenterCircle, colore3);
		if (demo1)
			MyLog.waitHere(
					"--- 002 ---\nla circonferenza risultante dal fit e' mostrata in rosso ed ha  \nxCenterCircle= "
							+ xCenterCircle + "  yCenterCircle= " + yCenterCircle + " diamCircle= " + diamCircle);

		// ----------------------------------------------------------
		// Misuro l'errore sul fit rispetto ai punti imposti
		// -----------------------------------------------------------
		double[] vetDist = new double[myXpoints.length];
		double sumError = 0;
		for (int i1 = 0; i1 < myXpoints.length; i1++) {
			vetDist[i1] = pointCirconferenceDistance(myXpoints[i1], myYpoints[i1], xCenterCircle, yCenterCircle,
					diamCircle / 2);
			sumError += Math.abs(vetDist[i1]);
		}
		if (sumError > maxFitError) {

			// -------------------------------------------------------------
			// disegno il cerchio ed i punti, in modo da date un feedback
			// grafico al messaggio di eccessivo errore nel fit
			// -------------------------------------------------------------
			// UtilAyv.showImageMaximized(imp11);
			over12.clear();
			imp11.setOverlay(over12);
			imp11.setRoi(new OvalRoi(xCenterCircle - diamCircle / 2, yCenterCircle - diamCircle / 2, diamCircle,
					diamCircle));
			imp11.getRoi().setStrokeColor(colore1);
			over12.addElement(imp11.getRoi());
			imp11.setRoi(new PointRoi(myXpoints, myYpoints, myXpoints.length));
			imp11.getRoi().setStrokeColor(colore2);
			over12.addElement(imp11.getRoi());
			imp11.deleteRoi();
			// MyLog.waitHere(listaMessaggi(16), debug, timeout1);
			MyLog.waitHere("--- 004 ---\nsumError= " + sumError + " maxFitError= " + maxFitError);
		}

		over12.clear();
		imp12.close();
		imp11.deleteRoi();
		imp11.updateImage();

		double[] out2 = new double[3];
		out2[0] = xCenterCircle;
		out2[1] = yCenterCircle;
		out2[2] = diamCircle;
		return out2;
	}

	/**
	 * Riceve una ImagePlus derivante da un CannyEdgeDetector con impostata una
	 * Line, restituisce le coordinate dei 2 picchi, se non sono esattamente 2
	 * restituisce null.
	 * 
	 * @param imp1
	 * @param dimPixel
	 * @param title
	 * @param showProfiles
	 * @param demo
	 * @param debug
	 * @return
	 */
	public static double[][] cannyProfileAnalyzer(ImagePlus imp1, boolean demo, boolean debug, int timeout) {

		double[][] profi3 = MyLine.decomposer(imp1);
		if (profi3 == null) {
			MyLog.waitHere("profi3 == null");
			return null;
		}
		// profi3 dovrebbe avere solo 2 punti a 255

		int count1 = 0;
		boolean ready1 = false;
		double max1 = 0;

		// conteggio dei punti a 255 (max), devono essere 2
		for (int i1 = 0; i1 < profi3[0].length; i1++) {

			if (profi3[2][i1] > max1) {
				max1 = profi3[2][i1];
				ready1 = true;
			}
			if ((profi3[2][i1] == 0) && ready1) {
				max1 = 0;
				count1++;
				ready1 = false;
			}
		}
		if (count1 != 2) {
			if (demo)
				MyLog.waitHere(
						"trovati un numero di punti diverso da 2, count= " + count1 + " scartiamo questi risultati");
			return null;
		}

		double[][] peaks1 = new double[6][count1];

		int count2 = 0;
		boolean ready2 = false;
		double max2 = 0;

		for (int i1 = 0; i1 < profi3[0].length; i1++) {

			if (profi3[2][i1] > max2) {
				peaks1[3][count2] = profi3[0][i1];
				peaks1[4][count2] = profi3[1][i1];
				max2 = profi3[2][i1];
				peaks1[5][count2] = max2;

				ready2 = true;
			}
			if ((profi3[2][i1] == 0) && ready2) {
				max2 = 0;
				count2++;
				ready2 = false;
			}
		}

		// ----------------------------------------
		// AGGIUNGO 1 AI PUNTI TROVATI
		// ---------------------------------------

		for (int i1 = 0; i1 < peaks1.length; i1++) {
			for (int i2 = 0; i2 < peaks1[0].length; i2++)
				if (peaks1[i1][i2] > 0)
					peaks1[i1][i2] = peaks1[i1][i2] + 1;
		}

		// verifico di avere trovato un max di 2 picchi
		if (peaks1[2].length > 2)
			MyLog.waitHere(
					"Attenzione trovate troppe intersezioni col cerchio, cioe' " + peaks1[2].length + "  VERIFICARE");
		if (peaks1[2].length < 2)
			MyLog.waitHere(
					"Attenzione trovata una sola intersezione col cerchio, cioe' " + peaks1[2].length + "  VERIFICARE");

		// MyLog.logMatrix(peaks1, "peaks1 " + title);

		return peaks1;
	}

	/**
	 * Calcolo della distanza tra un punto ed una circonferenza
	 * 
	 * @param x1
	 *            coord. x punto
	 * @param y1
	 *            coord. y punto
	 * @param x2
	 *            coord. x centro
	 * @param y2
	 *            coord. y centro
	 * @param r2
	 *            raggio
	 * @return distanza
	 */
	public static double pointCirconferenceDistance(int x1, int y1, int x2, int y2, int r2) {

		double dist = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)) - r2;
		return dist;
	}

	/***
	 * Ricerca del centro di una sfera nello stack. Utilizza Orthogonal_Views
	 * 
	 * @param imp1
	 *            stack immagini sfera
	 * @param demo
	 *            flag per grafica
	 * @return coordinate centro sfera (x, y, z, diam)
	 */

	public static double[] centerSphere(ImagePlus imp1, boolean demo) {

		double maxFitError = +20;
		double maxBubbleGapLimit = 2;

		// =================================================================
		// Utilizzo di ORTHOGONAL VIEWS per ricostruire le proiezioni nelle due
		// direzioni mancanti.
		// =================================================================

		imp1.show();
		IJ.run(imp1, "Orthogonal Views", "");
		Orthogonal_Views ort1 = Orthogonal_Views.getInstance();
		IJ.wait(10);
		if (demo)
			MyLog.waitHere(
					"--- 000 ---\noutput di 'Orthogonal Views', centrato di default sulla slice centrale dello stack\n"
							+ "ricostruisce le viste XZ ed YZ dalle quali viene ricavata la reale slice centrale della sfera");

		ImagePlus imp102 = ort1.getXZImage();
		if (imp102 == null)
			MyLog.waitHere("--- 001 ---\nimp102=null");
		ImagePlus impXZ = new Duplicator().run(imp102);
		IJ.wait(10);
		// imp202.setTitle("SAGITTALE ??");;
		ImagePlus imp103 = ort1.getYZImage();
		if (imp103 == null)
			MyLog.waitHere("--- 002 ---\nimp103=null");
		ImagePlus impYZ = new Duplicator().run(imp103);
		IJ.wait(10);
		// imp203.setTitle("CORONALE ??");;

		// chiudo ortogonal views cosi' non ci giocate gne gne gne!
		Orthogonal_Views.stop();

		// ===============================
		// PUNTO DUE A : DEFINIRE ROI 3D
		// ===============================

		// Ricerca posizione ROI per calcolo uniformita'. Versione con Canny
		// Edge Detector, da utilizzare per il fantoccio sferico. La coordinata
		// del centro della sfera verrà utilizzata per determinare quale è la
		// slice centrale dello stack

		int direction = 0;
		// String info10 = "position search XZimage";
		direction = 2;
		double outXZ[] = MySphere.centerCircleCannyEdge(impXZ, direction, maxFitError, maxBubbleGapLimit, false);
		if (outXZ == null) {
			MyLog.waitHere("--- 003 ---\nout202 == null");
			IJ.log("--- 004 ---\nposition search XZimage xCenterCircle= " + outXZ[0] + "yCenterCircle= " + outXZ[1]
					+ "diamCircle= " + outXZ[2]);
			MyLog.waitHere();
		}

		Overlay over202 = new Overlay();
		impXZ.setOverlay(over202);
		double xCenterEXT = outXZ[0];
		double yCenterEXT = outXZ[1];
		double diamEXT = outXZ[2];
		impXZ.setRoi(new OvalRoi(xCenterEXT - diamEXT / 2, yCenterEXT - diamEXT / 2, diamEXT, diamEXT));
		impXZ.getRoi().setStrokeColor(Color.red);
		over202.addElement(impXZ.getRoi());
		impXZ.deleteRoi();
		impXZ.setRoi(new OvalRoi(xCenterEXT - 2, yCenterEXT - 2, 4, 4));
		impXZ.getRoi().setStrokeColor(Color.red);
		impXZ.getRoi().setFillColor(Color.red);

		over202.addElement(impXZ.getRoi());
		impXZ.deleteRoi();
		// impXZ.show();
		MyLog.waitHere("center sphere  XZ", true, 200);

		// Ricerca posizione ROI per calcolo uniformita'. Versione con Canny
		// Edge Detector, da utilizzare per il fantoccio sferico. La coordinata
		// del centro della sfera verrà utilizzata per determinare quale è la
		// slice centrale dello stack
		// info10 = "position search YZimage";
		direction = 0;
		double outYZ[] = MySphere.centerCircleCannyEdge(impYZ, direction, maxFitError, maxBubbleGapLimit, false);
		if (outYZ == null) {
			MyLog.waitHere("--- 005 ---\nout203 == null", true, 200);
			IJ.log("--- 006 ---\nposition search YZimage xCenterCircle= " + outYZ[0] + "yCenterCircle= " + outYZ[1]
					+ "diamCircle= " + outYZ[2]);
			MyLog.waitHere("aaa", true, 100);
		}

		MyLog.waitHere("bbb", true, 500);
		Overlay over203 = new Overlay();
		impYZ.setOverlay(over203);
		xCenterEXT = outYZ[0];
		yCenterEXT = outYZ[1];
		diamEXT = outYZ[2];
		impYZ.setRoi(new OvalRoi(xCenterEXT - diamEXT / 2, yCenterEXT - diamEXT / 2, diamEXT, diamEXT));
		impYZ.getRoi().setStrokeColor(Color.red);
		over203.addElement(impYZ.getRoi());
		impYZ.deleteRoi();

		impYZ.setRoi(new OvalRoi(xCenterEXT - 2, yCenterEXT - 2, 4, 4));
		impYZ.getRoi().setStrokeColor(Color.red);
		impYZ.getRoi().setFillColor(Color.red);
		over203.addElement(impYZ.getRoi());
		impYZ.deleteRoi();
		// impYZ.show();
		MyLog.waitHere("center sphere YZ", true, 500);

		// ===============================
		// IMMAGINE DI CENTRO DELLA SFERA
		// ===============================
		// Determinazione della centerSlice, utilizzando le coordinate Z del
		// centro sfera determinate in precedenza.
		int centerSlice = 0;
		if ((outXZ[1] - outYZ[0]) < 2 || (outYZ[0] - outXZ[1]) < 2) {
			centerSlice = (int) outXZ[1]; // max incertezza permessa = 1
											// immagine
		} else
			MyLog.waitHere("--- 007 ---\nnon riesco a determinare la posizione Z, eccessiva incertezza");

		// in base alla centerSlice stabilita, estraiamo anche la VERA SLICE
		// CENTRALE della sfera
		ImagePlus imp101 = MyStackUtils.imageFromStack(imp1, centerSlice);
		if (imp101 == null) {
			MyLog.waitHere("--- 008 ---\nimp101=null");
		}
		imp101.setTitle("XY");
		ImagePlus impXY = imp101.duplicate();

		// Ricerca posizione ROI per calcolo uniformita'. Versione con Canny
		// Edge Detector, da utilizzare per il fantoccio sferico. In base alle
		// coordinate del centro e del raggio qui determinati, viene di seguito
		// costruita la sfera.
		direction = 1;
		double outXY[] = MySphere.centerCircleCannyEdge(impXY, direction, maxFitError, maxBubbleGapLimit, false);
		if (outXY == null) {
			MyLog.waitHere("--- 009 ---\nout201 null");
			IJ.log("--- 010 ---\nposition search XYimage xCenterCircle= " + outXY[0] + "yCenterCircle= " + outXY[1]
					+ "diamCircle= " + outXY[2]);
			MyLog.waitHere();
		}

		Overlay over201 = new Overlay();
		impXY.setOverlay(over201);
		xCenterEXT = outXY[0];
		yCenterEXT = outXY[1];
		diamEXT = outXY[2];
		impXY.setRoi(new OvalRoi(xCenterEXT - diamEXT / 2, yCenterEXT - diamEXT / 2, diamEXT, diamEXT));

		impXY.getRoi().setStrokeColor(Color.red);
		over201.addElement(impXY.getRoi());
		impXY.deleteRoi();

		impXY.setRoi(new OvalRoi(xCenterEXT - 2, yCenterEXT - 2, 4, 4));
		impXY.getRoi().setStrokeColor(Color.red);
		impXY.getRoi().setFillColor(Color.red);
		over201.addElement(impXY.getRoi());
		impXY.deleteRoi();

		// impXY.show();
		MyLog.waitHere("center sphere xy", true, 500);

		// stabiliamo i dati di output
		double[] out4 = new double[4];
		out4[0] = outXY[0];
		out4[1] = outXY[1];
		out4[2] = centerSlice - 1;
		out4[3] = outXY[2];

		return out4;

	}

	/***
	 * 
	 * @param width
	 *            width in pixels
	 * @param height
	 *            height in pixels
	 * @param depth
	 *            numbers of images
	 * @param titolo
	 *            titolo
	 * @return ImagePlus
	 */

	public static ImagePlus generaMappazzaVuota16(int width, int height, int depth, String titolo) {

		int bitdepth = 16;
		ImageStack newStack = ImageStack.create(width, height, depth, bitdepth);
		ImagePlus impMappazza = new ImagePlus(titolo, newStack);
		return impMappazza;
	}

	/**
	 * Creazione di una immagine RGB su cui plottare i modelli di sfera da noi
	 * ricavati. Si utilizza l'immagine dello stach fantoccio per ricavarne le
	 * dimensioni per creare il nuovo stackRGB nero
	 * 
	 * @param imp1
	 * @param in1
	 * @param demo
	 * @return
	 */
	public static ImagePlus createImageRGB(ImagePlus imp1, boolean demo) {

		int width = imp1.getWidth();
		int height = imp1.getHeight();
		int depth = imp1.getImageStackSize();
		int bitdepth = 24;
		ImageStack newStackOUT = ImageStack.create(width, height, depth, bitdepth);
		ImagePlus impMappazzaOUT = new ImagePlus("MAPPAZZA", newStackOUT);
		return impMappazzaOUT;
	}

	public static void addSphere(ImagePlus impMapR, ImagePlus impMapG, ImagePlus impMapB, double[] sphere, int[] bounds,
			int[] colorRGB, boolean surfaceOnly) {
		// public static void addSphere(ImagePlus impMapR, ImagePlus impMapG,
		// ImagePlus impMapB, int x0, int y0, int z0,
		// int diameter, int[] bounds, int[] colorRGB, boolean surfaceOnly) {
		int radius = (int) sphere[3] / 2;
		int diameter = (int) sphere[3];
		int r2 = radius * radius;
		int r1 = (radius - 1) * (radius - 1);
		int width = impMapR.getWidth();
		short auxR = 0;
		short auxG = 0;
		short auxB = 0;
		int x0 = (int) sphere[0];
		int y0 = (int) sphere[1];
		int z0 = (int) sphere[2];
		int x2 = 0;
		int y2 = 0;
		int z2 = 0;
		int xmin = x0 - radius;
		int xmax = xmin + diameter;
		int ymin = y0 - radius;
		int ymax = ymin + diameter;
		int zmin = z0 - radius;
		int zmax = zmin + diameter;
		for (int z1 = zmin; z1 <= zmax; z1++) {
			if (z1 < 0 || z1 > bounds[2] - 1)
				continue;
			int slice = z1 + 1;
			z2 = z1 - z0;
			ImageStack isR = impMapR.getStack();
			ImageStack isG = impMapG.getStack();
			ImageStack isB = impMapB.getStack();

			short[] pixelsMapR = (short[]) isR.getProcessor(slice).getPixels();
			short[] pixelsMapG = (short[]) isG.getProcessor(slice).getPixels();
			short[] pixelsMapB = (short[]) isB.getProcessor(slice).getPixels();
			for (int y4 = ymin; y4 <= ymax; y4++) {
				if (y4 < 0 || y4 > bounds[1])
					continue;
				y2 = y4 - y0;
				int offset = y4 * width;
				for (int x4 = xmin; x4 <= xmax; x4++) {
					if (x4 < 0 || x4 > bounds[0])
						continue;
					x2 = x4 - x0;
					int aux2 = x2 * x2 + y2 * y2 + z2 * z2;
					if (surfaceOnly) {
						if (aux2 <= r2 && aux2 > r1) {
							auxR = pixelsMapR[offset + x4];
							auxR += colorRGB[0];
							pixelsMapR[offset + x4] = auxR;
							auxG = pixelsMapG[offset + x4];
							auxG += colorRGB[1];
							pixelsMapG[offset + x4] = auxG;
							auxB = pixelsMapB[offset + x4];
							auxB += colorRGB[2];
							pixelsMapB[offset + x4] = auxB;
						}

					} else {
						if (aux2 <= r2) {
							auxR = pixelsMapR[offset + x4];
							auxR += colorRGB[0];
							pixelsMapR[offset + x4] = auxR;
							auxG = pixelsMapG[offset + x4];
							auxG += colorRGB[1];
							pixelsMapG[offset + x4] = auxG;
							auxB = pixelsMapB[offset + x4];
							auxB += colorRGB[2];
							pixelsMapB[offset + x4] = auxB;
						}
					}
				}
			}
			// devo ricaricare l'immagine!
			isR.setPixels(pixelsMapR, slice);
			isG.setPixels(pixelsMapG, slice);
			isB.setPixels(pixelsMapB, slice);
			impMapR.updateAndDraw();
		}
	}

	public static void generaMappazzaCombinata(ImagePlus impMappazzaR, ImagePlus impMappazzaG, ImagePlus impMappazzaB,
			ImagePlus impMappazzaRGB, int myColors) {

		double auxR = 0;
		double auxG = 0;
		double auxB = 0;

		int[] pixelsMappazzaRGB = null;
		short[] pixelsMappaR = null;
		short[] pixelsMappaG = null;
		short[] pixelsMappaB = null;
		short largestValue = Short.MIN_VALUE;
		short largestR = Short.MIN_VALUE;
		short largestG = Short.MIN_VALUE;
		short largestB = Short.MIN_VALUE;
		short[] searchMax = new short[4];

		for (int i10 = 0; i10 < impMappazzaR.getNSlices(); i10++) {
			pixelsMappazzaRGB = (int[]) impMappazzaRGB.getStack().getPixels(i10 + 1);
			pixelsMappaR = (short[]) impMappazzaR.getStack().getPixels(i10 + 1);
			pixelsMappaG = (short[]) impMappazzaG.getStack().getPixels(i10 + 1);
			pixelsMappaB = (short[]) impMappazzaB.getStack().getPixels(i10 + 1);
			largestR = ArrayUtils.vetMax(pixelsMappaR);
			largestG = ArrayUtils.vetMax(pixelsMappaG);
			largestB = ArrayUtils.vetMax(pixelsMappaB);
			if (largestR > searchMax[0])
				searchMax[0] = largestR;
			if (largestG > searchMax[1])
				searchMax[1] = largestG;
			if (largestB > searchMax[2])
				searchMax[2] = largestB;
			if (largestValue > searchMax[3])
				searchMax[3] = largestValue;
			largestValue = ArrayUtils.vetMax(searchMax);
		}

		largestR = searchMax[0];
		largestG = searchMax[1];
		largestB = searchMax[2];
		largestValue = searchMax[3];

		double kappa = 255.0 / largestValue;
		double kappaR = 255.0 / largestR;
		double kappaG = 255.0 / largestG;
		double kappaB = 255.0 / largestB;

		switch (myColors) {
		case 1:
			if (largestR == 0)
				largestR = 1;
			kappaR = 255.0 / largestR;
			if (largestG == 0)
				largestG = 1;
			kappaG = 255.0 / largestG;
			if (largestB == 0)
				largestB = 1;
			kappaB = 255.0 / largestB;
			break;
		case 2:
			kappaR = kappa;
			kappaG = kappa;
			kappaB = kappa;
			break;
		case 3:
			if (largestR == 0)
				largestR = 1;
			kappaR = 255.0 / largestR;
			if (largestG == 0)
				largestG = 1;
			kappaG = 255.0 / largestG;
			if (largestB == 0)
				largestB = 1;
			kappaB = 255.0 / largestB;
			break;
		}

		if (false) {
			IJ.log("generaMappazzaCombinata >> largestR= " + largestR);
			IJ.log("generaMappazzaCombinata >> largestG= " + largestG);
			IJ.log("generaMappazzaCombinata >> largestB= " + largestB);
			IJ.log("generaMappazzaCombinata >> largestValue= " + largestValue);
			IJ.log("generaMappazzaCombinata >> kappa= " + kappa);
			IJ.log("generaMappazzaCombinata >> kappaR= " + kappaR);
			IJ.log("generaMappazzaCombinata >> kappaG= " + kappaG);
			IJ.log("generaMappazzaCombinata >> kappaB= " + kappaB);
		}

		for (int i10 = 0; i10 < impMappazzaR.getNSlices(); i10++) {
			pixelsMappazzaRGB = (int[]) impMappazzaRGB.getStack().getPixels(i10 + 1);
			pixelsMappaR = (short[]) impMappazzaR.getStack().getPixels(i10 + 1);
			pixelsMappaG = (short[]) impMappazzaG.getStack().getPixels(i10 + 1);
			pixelsMappaB = (short[]) impMappazzaB.getStack().getPixels(i10 + 1);

			int colorRGB = 0;
			int red = 0;
			int green = 0;
			int blue = 0;

			for (int i1 = 0; i1 < pixelsMappaR.length; i1++) {

				auxR = (double) pixelsMappaR[i1] * kappaR;
				red = (int) auxR;
				auxG = (double) pixelsMappaG[i1] * kappaG;
				green = (int) auxG;
				auxB = (double) pixelsMappaB[i1] * kappaB;
				blue = (int) auxB;

				colorRGB = ((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff);
				pixelsMappazzaRGB[i1] = colorRGB;
			}
			impMappazzaRGB.updateAndRepaintWindow();
		}
		return;
	}

	/***
	 * ricerca gli hotspot circolari, slice per slice, restituendo il massimo
	 * utilizzeremo un diametro dello spot di 14 pixels con volume 153 voxels,
	 * il diametro di 12 pixels avrebbe volume 113 pixels, il diametro 13 pixels
	 * mi darebbe un raggio frazionario
	 * 
	 * @param imp1
	 * @param sphere1
	 * @param demo
	 * @return
	 */

	public static double[] searchCircularSpot(ImagePlus imp1, double[] sphere1, int diamsearch, String aa,
			int demolevel) {

		ArrayList<Double> xlist = new ArrayList<Double>();
		ArrayList<Double> ylist = new ArrayList<Double>();
		ArrayList<Double> zlist = new ArrayList<Double>();
		ArrayList<Double> maxlist = new ArrayList<Double>();

		int x1 = (int) sphere1[0];
		int y1 = (int) sphere1[1];
		int z1 = (int) sphere1[2];
		int d1 = (int) sphere1[3];
		int radius = (int) sphere1[3] / 2;
		boolean demo = false;
		if (demolevel > 0)
			demo = true;

		for (int i1 = 0; i1 < imp1.getImageStackSize(); i1++) {
			IJ.showStatus("" + aa + i1 + "/" + imp1.getImageStackSize());
			ImagePlus imp20 = MyStackUtils.imageFromStack(imp1, i1 + 1);
			if (demo)
				IJ.log("slice= " + i1);

			int zslice = i1;
			int distanceFromCenter = z1 - zslice;
			// calcolo della proiezione della sfera nella slice
			int diam33 = (int) Math.sqrt(radius * radius - distanceFromCenter * distanceFromCenter) * 2;
			if (demo)
				IJ.log("diametro= " + diam33);

			if (diam33 < 0)
				diam33 = 0;
			if (diam33 < diamsearch) {
				if (demo)
					IJ.log("skip");
				continue; // se il diametro della sfera è più piccolo del
							// diametro di ricerca lasciamo perdere
			}
			double[] out2 = new double[4];
			out2[0] = sphere1[0];
			out2[1] = sphere1[1];
			out2[2] = sphere1[2];
			out2[3] = diam33;

			double[] spot = MyFilter.positionSearchSphere(imp20, sphere1, diam33, diamsearch, zslice, demolevel);

			if (demo)
				MyLog.logVector(spot, "spot");

			xlist.add(spot[0]);
			ylist.add(spot[1]);
			zlist.add((double) i1);
			maxlist.add(spot[2]);
			// IJ.log("" + spot[0] + ", " + spot[1] + ", " + i1 + ", " +
			// spot[2]);
		}
		double maxval = -99999;
		double maxx = 0;
		double maxy = 0;
		double maxz = 0;
		for (int i1 = 0; i1 < xlist.size(); i1++) {
			if (maxlist.get(i1) > maxval) {
				maxval = maxlist.get(i1);
				maxx = xlist.get(i1);
				maxy = ylist.get(i1);
				maxz = zlist.get(i1);
			}
		}

		double[] out4 = new double[4];
		out4[0] = maxx;
		out4[1] = maxy;
		out4[2] = maxz;
		out4[3] = maxval;
		return out4;
	}

	/***
	 * 
	 * @param imp1
	 * @param sphere1
	 * @param diamsearch
	 * @param aa
	 * @param demolevel
	 * @return
	 */

	public static double[] vectorizeSphericalSpot(ImagePlus imp1, double[] sphere1, double[] sphere2, int demolevel) {

		boolean demo = false;
		if (demolevel > 0)
			demo = true;

		ArrayList<Double> pixlist = new ArrayList<Double>();

		int x2 = (int) sphere2[0];
		int y2 = (int) sphere2[1];
		int z2 = (int) sphere2[2];
		// int d2 = (int) sphere2[3];
		int rad2 = (int) sphere2[3] / 2;
		int diam2 = (int) sphere2[3];
		double volume = (4 / 3) * 3.14 * rad2 * rad2 * rad2;

		MyLog.logVector(sphere1, "sphere1");
		MyLog.logVector(sphere2, "sphere2");
		IJ.log("radius= " + rad2 + " volume teorico= " + volume + " voxels");

		// int xmin = x2 - rad2;
		// int xmax = xmin + diam2;
		// int ymin = y2 - rad2;
		// int ymax = y2 + diam2;
		int zmin = z2 - rad2;
		int zmax = zmin + diam2;
		int width = imp1.getWidth();
		// int height = imp1.getHeight();
		double aux1 = 0;
		double rad3 = 0;
		int dd = 0;
		for (int zz = zmin; zz < zmax; zz++) {
			dd = Math.abs(zz - z2);
			rad3 = Math.sqrt(rad2 * rad2 - dd * dd);
			// IJ.log("zz= " + zz + " dd= " + dd + " rad3= " + rad3 + " x2= " +
			// x2 + " y2= " + y2);
			ImagePlus imp2 = MyStackUtils.imageFromStack(imp1, zz + 1);
			imp2.setRoi(new OvalRoi(x2 - rad3, y2 - rad3, rad3 * 2, rad3 * 2));
			Roi roi1 = imp2.getRoi();
			Rectangle r3 = roi1.getBounds();
			ImageProcessor impMask = roi1.getMask();
			byte[] mask = (byte[]) impMask.getPixels();
			ImageProcessor ip2 = imp2.getProcessor();
			short[] pixels = (short[]) ip2.getPixels();
			int offset3 = 0;
			int offset1 = 0;
			if (mask == null)
				MyLog.waitHere("maskArray==null");

			// scansiono la mask e trovero' i pixel interessati della immagine
			// sorgente mediante calcoli
			int appx = 0;
			int appy = 0;

			for (int ww = 0; ww < r3.height; ww++) {
				offset3 = ww * (r3.width);
				appy = (y2 + ww - r3.height / 2);
				offset1 = appy * width;
				for (int rr = 0; rr < r3.width; rr++) {
					if (mask[offset3 + rr] != 0) {
						appx = x2 + rr - r3.width / 2;
						aux1 = (double) pixels[offset1 + appx];
						pixlist.add(aux1);
						IJ.log("" + appx + ", " + appy + ", " + zz + ", " + aux1);
						// pixels[offset1 + appx] = 10000;
					}
				}
			}
		}
		double[] pix = ArrayUtils.arrayListToArrayDouble(pixlist);
		imp1.updateAndDraw();
		return pix;
	}

	public void demoPixelPertainRoiTest(ImageProcessor ip) {
		/**
		 * questo � solo un appunto sui metodi che possiamo utilizzare per
		 * stabilire se un pixel � all'interno di una ROI.
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
					 * Roi roi = Imp.getRoi(); Rectangle rect = roi.getBounds();
					 * rx = rect.x; ry = rect.y; w = rect.width; h =
					 * rect.height; for(int y=ry; y<ry+h; y++) { for(int x=rx;
					 * x<rx+w; x++) { if(roi.contains(x, y)) {
					 */

				}
			}
		}
	}

	public static ImagePlus createOrthogonalStack(ImagePlus imp1, int direction, boolean demo) {
		imp1.show();
		IJ.run(imp1, "Orthogonal Views", "");
		Orthogonal_Views ort2 = Orthogonal_Views.getInstance();
		IJ.wait(10);
		ImageStack newStack = null;
		ImagePlus imp102 = null;
		ImageProcessor ip102 = null;
		if (direction == 1) {
			newStack = new ImageStack(imp1.getWidth(), imp1.getNSlices());
			for (int i1 = 0; i1 < imp1.getHeight(); i1++) {
				int crossx = imp1.getWidth() / 2;
				int crossy = i1;
				int crossz = imp1.getNSlices() / 2;
				ort2.setCrossLoc(crossx, crossy, crossz);
				IJ.wait(10);
				imp102 = ort2.getXZImage();
				ip102 = imp102.getProcessor();
				if (i1 == 0)
					newStack.update(ip102);
				newStack.addSlice("uno", ip102);
			}
		}
		if (direction == 2) {
			newStack = new ImageStack(imp1.getNSlices(), imp1.getHeight());
			for (int i1 = 0; i1 < imp1.getWidth(); i1++) {
				int crossx = i1;
				int crossy = imp1.getHeight() / 2;
				int crossz = imp1.getNSlices() / 2;
				ort2.setCrossLoc(crossx, crossy, crossz);
				IJ.wait(10);
				imp102 = ort2.getYZImage();
				ip102 = imp102.getProcessor();
				if (i1 == 0)
					newStack.update(ip102);
				newStack.addSlice("due", ip102);
			}
		}
		Orthogonal_Views.stop();
		ImagePlus newImpStack = new ImagePlus("INPUT_STACK", newStack);
		return newImpStack;
	}

}
