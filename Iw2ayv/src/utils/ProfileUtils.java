package utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Window;
import java.util.ArrayList;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.ImageWindow;
import ij.gui.Line;
import ij.gui.NewImage;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.gui.Plot;
import ij.gui.PointRoi;
import ij.gui.Roi;
import ij.io.FileSaver;
import ij.measure.Calibration;
import ij.measure.Measurements;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;
import ij.process.ShortProcessor;
import ij.util.Tools;

public class ProfileUtils {

	private static boolean previous = false; // utilizzati per impulso
	private static boolean init1 = true; // utilizzati per impulso
	@SuppressWarnings("unused")
	// utilizzati per impulso
	private static boolean pulse = false; // utilizzati per impulso, lasciare,
											// serve anche se segnalato
											// inutilizzato

	public static double[] autoprofile(int len) {
		double[] profile = new double[len];
		for (int i1 = 0; i1 < len; i1++)
			profile[i1] = (double) i1;

		return profile;
	}

	public static double[] readLine(ImagePlus imp1, double c2x, double c2y, double d2x, double d2y) {
		Line.setWidth(11);
		imp1.setRoi(new Line(c2x, c2y, d2x, d2y));
		Line lin2 = (Line) imp1.getRoi();
		double[] profile = lin2.getPixels();
		return profile;
	}

	public static double[] derivata(double[] profile1) {

		double[] profile2 = new double[profile1.length];
		for (int i1 = 0; i1 < profile1.length - 1; i1++)
			profile2[i1] = (profile1[i1] - profile1[i1 + 1]);
		// metto l'ultimo pixel uguale al penultimo
		profile2[profile2.length - 1] = profile2[profile2.length - 4];
		profile2[profile2.length - 2] = profile2[profile2.length - 4];
		profile2[profile2.length - 3] = profile2[profile2.length - 4];
		return profile2;
	}

	/**
	 * calcolo della derivata prima fatto MALE
	 * 
	 * @param profile1
	 * @return
	 */
	public static double[] derivataPrima(double profile1[]) {

		int len1 = profile1.length;

		double[] profileOut = new double[len1];
		for (int i1 = 1; i1 < len1 - 1; i1++) {
			profileOut[i1] = -0.5 * profile1[i1 - 1] + 0.5 * profile1[i1 + 1];
		}

		return profileOut;
	}

	public static double[][] derivataPrima(double profile1[][]) {

		double[] profileX = decodeX(profile1);
		double[] profileY = decodeY(profile1);
		int len1 = profile1.length;
		double[] profileOut = new double[len1];

		for (int i1 = 1; i1 < len1 - 1; i1++) {
			profileOut[i1] = -0.5 * profileY[i1 - 1] + 0.5 * profileY[i1 + 1];
		}
		double[][] deriveOut = encode(profileX, profileOut);

		return deriveOut;
	}

	/**
	 * calcolo della derivata seconda fatto MALE ESALTA ECCESSIVAMENTE IL FONDO MALE
	 * MALE MALE
	 * 
	 * @param profile4smooth array su cui eseguire l'operazione
	 */
	public static double[][] derivataSeconda_FATTA_MALE(double[][] profile1) {

		int len1 = profile1.length;
		double[][] profile11 = profile1.clone();
		double[] profileX = decodeX(profile11);
		double[] profileY = decodeY(profile11);
		double[] profileOut = new double[len1];

		for (int i1 = 1; i1 < profileY.length - 1; i1++)
			profileOut[i1] = profileY[i1 - 1] - 2 * profileY[i1] + profileY[i1 + 1];
		profileOut[0] = profileOut[1];
		profileOut[len1 - 1] = profileOut[len1 - 2];

		double[][] deriveOut = encode(profileX, profileOut);

		return deriveOut;

	}

	/**
	 * differentialQuotientDerivative VEDI:
	 * http://www.labcognition.com/onlinehelp/en/derivative.htm#
	 * Differential_Quotient_Derivative_Algorithm
	 * 
	 * PURTROPPO DALLE PROVE LE DUE DERIVATE SONO APPARENTEMENTE IDENTICHE, SALVO
	 * PRIMO ED ULTIMO PIXEL (MAGARI NON MI SONO ACCORTO CHE LE ISTRUZIONI SI
	 * EQUIVALGONO) PERCIO' SEMPLICEMENTE ACCANTONO LA DQD
	 * 
	 * @param profile1
	 * @return
	 */
	public static double[] derivataDQD(double[] profile1) {

		double[] profile2 = new double[profile1.length];
		double slope1 = 0;
		double slope2 = 0;
		double slope3 = 0;

		for (int i1 = 1; i1 < profile1.length - 1; i1++) {
			double x2 = i1;
			double y2 = profile1[i1];
			double x1 = i1 - 1;
			double y1 = profile1[i1 - 1];
			slope1 = (y2 - y1) / (x2 - x1);
			double x3 = i1 + 1;
			double y3 = profile1[i1 + 1];
			slope2 = (y3 - y2) / (x3 - x2);
			slope3 = (slope1 + slope2) / 2;
			profile2[i1] = slope3;
		}
		// metto l'ultimo pixel uguale al penultimo
		profile2[profile2.length - 1] = profile2[profile2.length - 3];
		profile2[profile2.length - 2] = profile2[profile2.length - 3];
		profile2[0] = profile2[1];
		return profile2;
	}

	public static double[][] derivataDQD(double[][] profile3) {

		double[] profileX = ProfileUtils.decodeX(profile3);
		double[] profileY = ProfileUtils.decodeY(profile3);

		double[] profile2 = new double[profileY.length];
		double slope1 = 0;
		double slope2 = 0;
		double slope3 = 0;

		for (int i1 = 1; i1 < profileY.length - 1; i1++) {
			double x2 = i1;
			double y2 = profileY[i1];
			double x1 = i1 - 1;
			double y1 = profileY[i1 - 1];
			slope1 = (y2 - y1) / (x2 - x1);
			double x3 = i1 + 1;
			double y3 = profileY[i1 + 1];
			slope2 = (y3 - y2) / (x3 - x2);
			slope3 = (slope1 + slope2) / 2;
			profile2[i1] = slope3;
		}
		// metto l'ultimo pixel uguale al penultimo
		profile2[profile2.length - 1] = profile2[profile2.length - 3];
		profile2[profile2.length - 2] = profile2[profile2.length - 3];
		profile2[0] = profile2[1];
		double[][] out = ProfileUtils.encode(profileX, profile2);
		return out;
	}

	public static int[] localizzatoreDopoDQD(double[] profile1, double threshold) {
		int start = 0;
		int end = 0;
		int[] out = new int[2];
		boolean find1 = false;
		boolean find2 = false;

		int posmin = ArrayUtils.posMin(profile1);
		int posmax = ArrayUtils.posMax(profile1);
		MyLog.waitHere("posmin= " + posmin + " posmax= " + posmax);
		for (int i1 = 0; i1 < profile1.length; i1++) {
			if (!find1 && i1 < posmin && profile1[i1] < (threshold * (-1))) {
				start = i1;
				find1 = true;
			}
			if (!find2 && i1 > posmax && profile1[i1] < threshold) {
				find2 = true;
				end = i1;
			}
		}
		out[0] = start;
		out[1] = end;
		return out;

	}

	/**
	 * La routine rimuove tutti i punti del picco sia dalle coordinate y che quelle
	 * x pertanto restituisce una matrice profilo con un buco, nel senso che la
	 * coordinata x[sinistra] e' seguita dalla coordinata x[destra], lo stesso
	 * Sragionamento vale per y
	 * 
	 * @param profile1 profilo completo
	 * @param sinistra limite sx picco
	 * @param destra   limite dx picco
	 * @param step
	 * @return
	 */

	public static double[][] peakRemover(double[][] profile1, int sinistra, int destra, boolean step) {
		// in precedenza la routine era chiamata potatura (e non si capiva che
		// cazzabubbola facesse)
		//
		// a questo punto devo rimuovere dal profilo tutti i punti interni tra sinistra
		// e destra del picco, utilizzo ArrayList <PER SEMPLICITA'> :)
		//
		if ((destra - sinistra) <= 0)
			MyLog.waitHere("deve essere POSITIVO");
		if ((destra | sinistra) == 0)
			MyLog.waitHere("deve essere >0");
		if ((destra | sinistra) == profile1.length)
			MyLog.waitHere("deve essere < length");

		double[] profileX = ProfileUtils.decodeX(profile1);
		double[] profileY = ProfileUtils.decodeY(profile1);

		ArrayList<ArrayList<Double>> arrprofile2 = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> arrY = new ArrayList<Double>();
		ArrayList<Double> arrX = new ArrayList<Double>();
		for (int i1 = 0; i1 < profile1.length; i1++) {
			if (i1 <= sinistra || i1 >= destra) {
				arrX.add(profileX[i1]);
				arrY.add(profileY[i1]);
			}
		}
		double[] profileX2 = ArrayUtils.arrayListToArrayDouble(arrX);
		double[] profileY2 = ArrayUtils.arrayListToArrayDouble(arrY);
		double[][] profile2 = ProfileUtils.encode(profileX2, profileY2);

		return profile2;
	}

	/**
	 * Copied from http://billauer.co.il/peakdet.htm Peak Detection using MATLAB
	 * Author: Eli Billauer
	 * 
	 * @param profile profilo da analizzare
	 * @param delta
	 * @return ArrayList con le posizioni del picco minimo e del picco massimo
	 */
	public static ArrayList<ArrayList<Double>> peakDetModificato(double[][] profile, double delta) {

		double max = 0;
		double min = 0;
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
		if (delta < 0) {
			delta = 2 * ArrayUtils.vetSdKnuth(vety);
		}

		double maxpos = -1.0;
		double minpos = -1.0;
		boolean lookformax = true;
		boolean first = true;
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
			if (first) {
				if (valy < max - delta) {
					// MyLog.waitHere("i1= " + i1 + " valy= " + valy + " max= "
					// + max + " delta= " + delta);
					lookformax = false;
					first = false;
				}
				if (valy > min + delta) {
					// MyLog.waitHere("i1= " + i1 + " valy= " + valy + " max= "
					// + max + " delta= " + delta);
					lookformax = true;
					first = false;
				}
			}

			if (lookformax) {
				if (valy < max - delta) {
					// MyLog.waitHere("maxpos= " + maxpos + " max= " + max);
					maxtabx.add((Double) maxpos);
					maxtaby.add((Double) max);
					min = valy;
					minpos = vetx[i1];
					lookformax = false;
				}
			} else {
				if (valy > min + delta) {
					// MyLog.waitHere("minpos= " + minpos + " min= " + min);
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
		// MyLog.waitHere();
		matout.add(mintabx);
		matout.add(mintaby);
		matout.add(maxtabx);
		matout.add(maxtaby);

		return matout;
	}

	/**
	 * Copied from http://billauer.co.il/peakdet.htm Peak Detection using MATLAB
	 * Author: Eli Billauer
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

	/**
	 * Analizzo la derivata seconda per trovare i due zerocrossing al limite del
	 * picco
	 * 
	 * @param profile
	 * @param xpoint1
	 * @param xpoint2
	 * @return
	 */
	public static int[] zeroCrossing(double[][] derivata2, String thisfirst, String thissecond, String thisthird,
			int thiscontaxx, boolean thisSpy, boolean thisStep) {

		double[] profi2X = ProfileUtils.decodeX(derivata2);
		double[] profi2Y = ProfileUtils.decodeY(derivata2);
		double[] minMax2 = Tools.getMinMax(profi2Y);
		int max2 = 9999;
		double zeroSX_X = 9999;
		double zeroSX_Y = 9999;
		double zeroDX_X = 9999;
		double zeroDX_Y = 9999;
		boolean negativo = false;
		for (int i1 = 0; i1 < profi2Y.length; i1++) {
			if (profi2Y[i1] == minMax2[1]) {
				max2 = (int) profi2X[i1];
				break;
			}
		}
		negativo = false;
		for (int i1 = max2; i1 >= 0; i1--) {
			if (profi2Y[i1] < 0) {
				negativo = true;
			}
			if ((profi2Y[i1] >= 0) && negativo) {
				zeroSX_X = profi2X[i1];
				zeroSX_Y = profi2Y[i1];
				if (thisStep)
					IJ.log("zeroCross1 ad i1= " + i1 + " zeroSX_X= " + zeroSX_X + " zeroSX_Y= " + zeroSX_Y);
				break;
			}
		}
		negativo = false;
		for (int i1 = max2; i1 < profi2Y.length; i1++) {
			if (profi2Y[i1] < 0) {
				negativo = true;
			}
			if ((profi2Y[i1] >= 0) && negativo) {
				zeroDX_X = profi2X[i1];
				zeroDX_Y = profi2Y[i1];
				if (thisStep)
					IJ.log("zeroCross2 ad i1= " + i1 + " zeroDX_X= " + zeroDX_X + " zeroDX_Y= " + zeroDX_Y);
				break;
			}
		}

		double[] pointsX = new double[2];
		double[] pointsY = new double[2];
		pointsX[0] = zeroSX_X;
		pointsX[1] = zeroDX_X;
		pointsY[0] = zeroSX_Y;
		pointsY[1] = zeroDX_Y;

		if (thisStep) {
			String thisname = "Q001 - DERIVATA SECONDA CON ZEROCROSSINGS EVIDENZIATI";
			Plot plot14 = MyPlot.plot1points(derivata2, pointsX, pointsY, thisname);
			ImagePlus imp14 = plot14.show().getImagePlus();
			if (thisSpy)
				saveDebugImage(imp14, thisfirst, thissecond, thisname, thiscontaxx, thisSpy);
			MyLog.waitHere(thisname);
		}

		int[] out = new int[2];
		out[0] = (int) zeroSX_X;
		out[1] = (int) zeroDX_X;

		return out;

	}

	public static void saveDebugImage(ImagePlus imp1, String first, String second, String name, int thiscontaxx,
			boolean spy) {
		ImagePlus impS3 = imp1.flatten();
		String newName = first + second + "\\" + name + "@" + IJ.pad(thiscontaxx++, 3) + ".jpg";
		FileSaver fs = new FileSaver(impS3);
		// FileSaver.setJpegQuality(100);
		if (spy) {
			// MyLog.waitHere("salvo " + newName);
			fs.saveAsJpeg(newName);
		}
	}

	public static double linearInterpolation(double xa, double ya, double xb, double yb, double yc) {
		return (xa + ((ya - yc) / (ya - yb)) * (xb - xa));
	}

	/***
	 * per intercambiabilita' con peakDet2 restituisco un ArrayList<ArrayList<>>
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

	/**
	 * Dati i parametri di una curva ed un vettore x, calcola i corrispondenti
	 * valori di y per 3 parametri (curva)
	 * 
	 * @param vetX vettore delle ascisse
	 * @param para parametri della curva
	 * @return vettore delle ordinate
	 */
	public static double[] fitResult3(double[] vetX, double[] para) {
		double[] out = new double[vetX.length];

		for (int i1 = 0; i1 < vetX.length; i1++) {
			double x = vetX[i1];
			double y = para[0] + para[1] * x + para[2] * x * x;
			out[i1] = y;
		}
		return out;
	}

	/**
	 * Dati i parametri di una curva ed un vettore x, calcola i corrispondenti
	 * valori di y per 2 parametri (retta)
	 * 
	 * @param vetX vettore delle ascisse
	 * @param para parametri della linea
	 * @return vettore delle ordinate
	 */

	public static double[] fitResult2(double[] vetX, double[] para) {
		double[] out = new double[vetX.length];

		for (int i1 = 0; i1 < vetX.length; i1++) {
			double x = vetX[i1];
			double y = para[0] + para[1] * x;
			out[i1] = y;
		}
		return out;
	}

	/**
	 * 
	 * @param profile1
	 * @return
	 */
	public static double[] squareSmooth2(double[] profile1) {

		double[] profile2 = new double[profile1.length];
		for (int i1 = 0; i1 < profile1.length - 1; i1++)
			profile2[i1] = (profile1[i1] + profile1[i1 + 1]) / 2;
		// metto l'ultimo pixel uguale al penultimo
		profile2[profile2.length - 1] = profile2[profile2.length - 2];
		return profile2;
	}

	/**
	 * 
	 * @param profile1
	 * @return
	 */
	public static double[] squareSmooth3(double[] profile1) {

		double[] profile2 = new double[profile1.length];
		for (int i1 = 0; i1 < profile1.length - 2; i1++)
			profile2[i1] = profile1[i1] * 0.25 + profile1[i1 + 1] * 0.5 + profile1[i1 + 2] * 0.25;
		profile2[profile2.length - 1] = profile2[profile2.length - 3];
		profile2[profile2.length - 2] = profile2[profile2.length - 3];
		profile2[0] = profile2[2];
		profile2[1] = profile2[2];
		return profile2;
	}

	/**
	 * 
	 * @param profile1
	 * @return
	 */
	public static double[] triangleSmooth3(double[] profile1) {

		double[] profile2 = new double[profile1.length];
		for (int i1 = 0; i1 < profile1.length - 2; i1++)
			profile2[i1] = (profile1[i1] + profile1[i1 + 1] * 2 + profile1[i1 + 2]) / 4;
		profile2[profile2.length - 1] = profile2[profile2.length - 3];
		profile2[profile2.length - 2] = profile2[profile2.length - 3];
		profile2[0] = profile2[2];
		profile2[1] = profile2[2];
		return profile2;
	}

	/*
	 * effettua la ricerca dei due zero crossings della derivata seconda del profilo
	 */
	public static double[] zeroCrossings_CANCELLABILE(double[][] profile) {

		double delta = 0.5;
		ArrayList<ArrayList<Double>> out1 = peakDet(profile, delta);

		return null;
	}

	/**
	 * calcolo spessore di strato effettivo, apportando le correzioni per
	 * inclinazione e tilt (cio' che veniva effettuato dal foglio Excel)
	 * 
	 * @param R1
	 * @param R2
	 * @param sTeor spessore teorico
	 * @return spessore dello strato
	 */
	public static double[] spessStrato(double R1, double R2, double sTeor, double dimPix2) {

		double spessArray[]; // qui verranno messi i risultati
		spessArray = new double[4];
		double S1 = R1 * Math.tan(Math.toRadians(11));
		double S2 = R2 * Math.tan(Math.toRadians(11));
		double Sen22 = Math.sin(Math.toRadians(22));
		double aux1 = -(S1 - S2) / (S1 + S2);
		double aux4 = Math.asin(Sen22 * aux1);
		double tilt1Ramp = Math.toDegrees(0.5 * aux4);
		double aux2 = Math.tan(Math.toRadians(11.0 - tilt1Ramp));
		double aux3 = Math.tan(Math.toRadians(11.0 + tilt1Ramp));
		double S1Cor = aux3 * R1;
		double S2Cor = aux2 * R2;
		double accurSpess = 100.0 * (S1Cor - sTeor) / sTeor;
		double erroreR1 = dimPix2 * aux3;
		double erroreR2 = dimPix2 * aux2;
		double erroreTot = Math.sqrt(erroreR1 * erroreR1 + erroreR2 * erroreR2);
		double erroreSper = 100.0 * erroreTot / sTeor;
		spessArray[0] = S1Cor;
		spessArray[1] = S2Cor;
		spessArray[2] = erroreSper;
		spessArray[3] = accurSpess;
		return spessArray;
	}

	/**
	 * permette di riunire i profili X ed Y in una matrice (array a due dimensioni)
	 * 
	 * @param profiX
	 * @param profiY
	 * @return
	 */
	public static double[][] encode(double[] profiX, double[] profiY) {
		double[][] out2 = new double[profiX.length][2];
		for (int i1 = 0; i1 < profiX.length; i1++) {
			out2[i1][0] = profiX[i1];
			out2[i1][1] = profiY[i1];
		}
		return out2;
	}

	/**
	 * crea un profilo X continuo che parte da 0 e lo unisce all'Y in una matrice
	 * (array a due dimensioni)
	 * 
	 * @param profiY
	 * @return
	 */
	public static double[][] encode(double[] profiY) {
		double[][] out2 = new double[profiY.length][2];
		for (int i1 = 0; i1 < profiY.length; i1++) {
			out2[i1][0] = (double) i1;
			out2[i1][1] = profiY[i1];
		}
		return out2;
	}

	/**
	 * estrae il profilo X dalla matrice
	 * 
	 * @param in1
	 * @return
	 */
	public static double[] decodeX(double[][] in1) {
		double[] out2 = new double[in1.length];
		for (int i1 = 0; i1 < in1.length; i1++) {
			out2[i1] = in1[i1][0];
		}
		return out2;
	}

	/**
	 * estrae il profilo Y dalla matrice
	 * 
	 * @param in1
	 * @return
	 */
	public static double[] decodeY(double[][] in1) {
		double[] out2 = new double[in1.length];
		for (int i1 = 0; i1 < in1.length; i1++) {
			out2[i1] = in1[i1][1];
		}
		return out2;
	}

	public static double[][] smooth(double[][] profile1) {

		double[] profileY = ProfileUtils.decodeY(profile1);
		for (int i1 = 1; i1 < profileY.length - 1; i1++)
			profileY[i1] = (profileY[i1 - 1] + profileY[i1] + profileY[i1 + 1]) / 3.0;
		double[][] profileOut = ProfileUtils.encode(ProfileUtils.decodeX(profile1), profileY);

		return profileOut;
	}

	public static double[] smooth(double[] profile1) {

		double[] profile2 = profile1.clone();

		for (int i1 = 1; i1 < profile2.length - 1; i1++)
			profile2[i1] = (profile2[i1 - 1] + profile2[i1] + profile2[i1 + 1]) / 3.0;

		return profile2;
	}

	/**
	 * effettua lo smooth del segnale con una media mobile su 5 pixel. Taglia i
	 * pixel ai bordi
	 * 
	 * @param in1
	 * @return
	 */
	public static double[][] smooth5(double[][] profile1) {

		int len = profile1.length;
		double[] profiY = ProfileUtils.decodeY(profile1);

		ArrayList<Double> arrX = new ArrayList<Double>();
		ArrayList<Double> arrY = new ArrayList<Double>();
		double aux1 = 0;

		for (int i1 = 2; i1 < len - 3; i1++) {
			aux1 = (profiY[i1 - 2] + profiY[i1 - 1] + profiY[i1] + profiY[i1 + 1] + profiY[i1 + 2]) / 5;
			arrX.add((double) i1);
			arrY.add(aux1);
		}

		double[] smoothX = ArrayUtils.arrayListToArrayDouble(arrX);
		double[] smoothY = ArrayUtils.arrayListToArrayDouble(arrY);
		double[][] smoothProfile = ProfileUtils.encode(smoothX, smoothY);

		return smoothProfile;
	}

}
