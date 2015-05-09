package utils;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Line;
import ij.gui.Plot;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.gui.WaitForUserDialog;
import ij.process.FloatPolygon;

public class MyLine {
	/**
	 * Restituisce le coordinate x, y e z dei punti appartenenti alla linea, in
	 * cui x ed y sono le coordinate del pixel (relative all'immagine di
	 * partenza) e z è il valore di segnale
	 * 
	 * @param imp1
	 * @return
	 */
	public static double[][] decomposer(ImagePlus imp1) {

		Line line = (Line) imp1.getRoi();
		if (line == null || !(line.isLine())) {
			IJ.error("Line selection required.");
			return null;
		}
		double[] profiZ = line.getPixels();
		FloatPolygon fp1 = line.getInterpolatedPolygon();
		int len = fp1.npoints - 1;
		double[] profiX = new double[len];
		double[] profiY = new double[len];
		for (int i1 = 0; i1 < len; i1++) {
			profiX[i1] = (double) fp1.xpoints[i1];
			profiY[i1] = (double) fp1.ypoints[i1];
		}
		double[][] out1 = new double[3][profiX.length];
		out1[0] = profiX;
		out1[1] = profiY;
		out1[2] = profiZ;
		return out1;
	}

	public static double[][] decomposer3(ImagePlus imp1) {

		Line line = (Line) imp1.getRoi();
		if (line == null || !(line.isLine())) {
			IJ.error("Line selection required.");
			return null;
		}
		double[] profiZ = line.getPixels();
		IJ.run(imp1, "Interpolate", "interval=1.0");
		FloatPolygon fp1 = line.getFloatPolygon();
		int len = fp1.xpoints.length;
		double[] profiX = new double[len];
		double[] profiY = new double[len];
		for (int i1 = 0; i1 < len; i1++) {
			profiX[i1] = (double) fp1.xpoints[i1];
			profiY[i1] = (double) fp1.ypoints[i1];
		}
		if (profiX.length != profiZ.length) {
			IJ.log("different length, profi2x= " + profiX.length + " profi2z= "
					+ profiZ.length);
		}
		double[][] out1 = new double[3][profiX.length];
		out1[0] = profiX;
		out1[1] = profiY;
		out1[2] = profiZ;
		return out1;
	}

}