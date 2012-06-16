package utils;

import ij.ImagePlus;
import ij.gui.Roi;
import ij.process.ImageProcessor;

import java.awt.Rectangle;

/***
 * Copied from the ImageJ site.
 * 
 * Oval_Profile takes the image region bounded by an oval region and samples the
 * oval at N equal angles around the oval. The program generates a ProfilePlot
 * of Pixel intensities at equiangular sample points along the oval profile.
 * 
 * @author Bill O'Connell (woc at attbi.com)
 * 
 *         Modified by AlbertoDuina
 */

public class MyOvalProfile {

	/***
	 * 
	 * @param roi
	 * @param imp1
	 * @param start
	 *            angolo di partenza (espresso come numero facente parte di
	 *            npoints)
	 * @param npoints
	 * @return
	 */
	public static double[] getOvalProfile(ImagePlus imp1, int start, int npoints) {
		Roi roi = imp1.getRoi();
		Rectangle b = roi.getBounds();
		double width = b.width;
		double height = b.height;
		double w2 = width * width / 4.0;
		double h2 = height * height / 4.0;
		// get radii from oval center
		double cx = b.x + width / 2.0 + .5;
		double cy = b.y + height / 2.0 + .5;
		double tink = 2 * Math.PI / npoints;
		double[] profile = new double[npoints];
		double[] xValues = new double[npoints];
		double[] xCoordinate = new double[npoints];
		double[] yCoordinate = new double[npoints];
		ImageProcessor ip1 = imp1.getProcessor();
		double theta = 0;
		// int i1 = 0;
		int i2 = 0;
		for (int i1 = 0; i1 < npoints; i1++) {
			// for (theta = 0; (theta < 2 * Math.PI) && (i1 < npoints); theta +=
			// tink) {
			i2 = i1 + start;
			if (i2 > npoints)
				i2 = i2 - npoints;
			theta = (double) i2 * tink;
			double dx = Math.cos(theta);
			double dy = Math.sin(theta);
			double x = cx;
			double y = cy;
			double hotx = 0;
			double hoty = 0;
			while (roi.contains((int) x, (int) y)) {
				x += dx;
				y += dy;
			}
			double m = Math.sqrt(w2 * h2 / (dx * dx * h2 + dy * dy * w2));
			hotx = cx + dx * m;
			hoty = cy + dy * m;
			profile[i1] = ip1.getInterpolatedPixel(hotx, hoty);
			xCoordinate[i1] = hotx;
			yCoordinate[i1] = hoty;
			xValues[i1] = Math.toDegrees(theta);
			// i1++;
		}
		MyLog.logVector(xCoordinate, "xCoordinate");
		MyLog.logVector(yCoordinate, "yCoordinate");
		MyLog.logVector(xValues, "xValues");
		MyLog.logVector(profile, "profile");
		return profile;
	}

}
