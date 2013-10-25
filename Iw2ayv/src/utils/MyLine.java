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
	 * Restituisce le coordinate x ed y dei punti appartenenti alla linea
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
		MyLog.logVector(profiZ, "profiZ");
		MyLog.waitHere();

		FloatPolygon fp1 = line.getInterpolatedPolygon();
		int len = fp1.npoints - 1;
		double[] profiX = new double[len];
		double[] profiY = new double[len];
		for (int i1 = 0; i1 < len; i1++) {
			profiX[i1] = (double) fp1.xpoints[i1];
			profiY[i1] = (double) fp1.ypoints[i1];
		}
		// if (profiX.length != profiZ.length) {
		// IJ.log("different length, profi2x= " + profiX.length + " profi2z= "
		// + profiZ.length);
		// }

		double[][] out1 = new double[3][profiX.length];
		out1[0] = profiX;
		out1[1] = profiY;
		out1[2] = profiZ;
		MyLog.logMatrix(out1, "out1");
		MyLog.waitHere();
		return out1;
	}

	public static void decomposer2(ImagePlus imp1) {

		imp1.setRoi(new Line(imp1.getWidth() * 1 / 8, 0, imp1.getWidth(), imp1
				.getHeight() * 3 / 8 + 5));
		imp1.show();
		IJ.setMinAndMax(imp1, 1, 10);

		IJ.run(imp1, "Set Scale...", "distance=0 known=0 pixel=1 unit=pixel");

		Line line = (Line) imp1.getRoi();

		// IJ.log("line.getlength= " + line.getLength());
		FloatPolygon fp2 = line.getInterpolatedPolygon();
		float[] profiX = fp2.xpoints;
		float[] profiY = fp2.ypoints;
		double[] profiZ = line.getPixels();

		MyLog.waitHere("line.getLength= " + line.getLength()
				+ " profiZ.length= " + profiZ.length + " XYpoints="
				+ (fp2.npoints));

		for (int i1 = 0; i1 < fp2.npoints - 1; i1++) {
			IJ.log("" + i1 + " x= " + profiX[i1] + "y= " + profiY[i1] + " z="
					+ profiZ[i1]);
		}

		new WaitForUserDialog("Do something, then click OK.").show();

		for (int i1 = 0; i1 < profiX.length; i1++) {
			IJ.log("" + i1 + " x= " + profiX[i1] + "y= " + profiY[i1]);
		}

		new WaitForUserDialog("Do something, then click OK.").show();

	}

	public static double[][] decomposer3(ImagePlus imp1) {

		Line line = (Line) imp1.getRoi();
		if (line == null || !(line.isLine())) {
			IJ.error("Line selection required.");
			return null;
		}
		double[] profiZ = line.getPixels();

		IJ.run(imp1, "Interpolate", "interval=1.0");
		//
		//
		//
		// // now for obtain the pixels coordinate i need a PolygonRoi
		// float[] xcoord = new float[2];
		// float[] ycoord = new float[2];
		// xcoord[0] = (float) line.x1d;
		// ycoord[0] = (float) line.y1d;
		// xcoord[1] = (float) line.x2d;
		// ycoord[1] = (float) line.y2d;
		// imp1.setRoi(new PolygonRoi(xcoord, ycoord, 2, Roi.POLYLINE));
		// PolygonRoi polyline = (PolygonRoi) imp1.getRoi();
		//
		//
		// polyline.fitSplineForStraightening();
		FloatPolygon fp1 = line.getFloatPolygon();
		// double interval = 1.0;
		// boolean smooth = false;
		// FloatPolygon fp1 = polyline.getInterpolatedPolygon(1.0,true);
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