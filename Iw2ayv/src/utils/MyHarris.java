package utils;

import java.util.List;

import ij.ImagePlus;
import ij.gui.PointRoi;
import ij.process.ImageProcessor;

/**
 * This plugin implements the Harris corner detector.
 * 
 * @version 2013/07/09
 */
public class MyHarris {

	static int nmax = 0; // points to show

	public ImagePlus execute(ImagePlus imp1) {
		ImageProcessor ip1 = imp1.getProcessor();
		HarrisCornerDetector.Parameters params = new HarrisCornerDetector.Parameters();
//		params.alpha = 0.2;
//		params.threshold = 15000;
//		params.doCleanUp = true;
		params.alpha = 0.2;
		params.threshold = 200000;
		params.doCleanUp = true;
		HarrisCornerDetector hcd = new HarrisCornerDetector(ip1, params);
		hcd.findCorners();
		
		
		
		ImageProcessor ip2 = hcd.showCornerPoints(ip1, nmax);
		// (new ImagePlus("Corners from " + imp1.getTitle(), ip2)).show();
		ImagePlus imp2 = new ImagePlus("Corners from " + imp1.getTitle(), ip2);
		return imp2;
	}


}