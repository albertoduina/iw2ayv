
package utils;

import static org.junit.Assert.assertTrue;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.Line;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.io.Opener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MyLineTest {
	@Before
	public void setUp() throws Exception {
		new ImageJ(ImageJ.NORMAL);
	}

	
	@After
	public void tearDown() throws Exception {
	}


	@Test
	public final void testMyLineDecomposer() {

		String path1 = ".\\Test4\\aaa.dcm";
		ImagePlus imp1 = new Opener().openImage(path1);
		int[] xcoord = new int[2];
		int[] ycoord = new int[2];
		xcoord[0] = imp1.getWidth()*1/8;
		ycoord[0] = 0;
		xcoord[1] = imp1.getWidth();
		ycoord[1] = imp1.getHeight() * 3 / 8+3;
		imp1.setRoi(new Line(xcoord[0], ycoord[0], xcoord[1], ycoord[1]));
		IJ.setMinAndMax(imp1, 1, 20);
		UtilAyv.showImageMaximized(imp1);	
		double[][] out1 = MyLine.decomposer(imp1);	
		double[][] expected= InputOutput.readDoubleMatrixFromFile("./data/decomposed1.txt");
		IJ.wait(100);	
		assertTrue(UtilAyv.compareMatrix(out1, expected, ""));
	}
	
	@Test
	public final void testMyLineDecomposer3() {

		String path1 = ".\\Test4\\aaa.dcm";
		ImagePlus imp1 = new Opener().openImage(path1);
		int[] xcoord = new int[2];
		int[] ycoord = new int[2];
	
		xcoord[0] = imp1.getWidth()*1/8;
		ycoord[0] = 0;
		xcoord[1] = imp1.getWidth();
		ycoord[1] = imp1.getHeight() * 3 / 8+3;
		
		
//		xcoord[0] = imp1.getWidth()*1/8;
//		ycoord[0] = 0;
//		xcoord[1] = imp1.getWidth()*1/8;
//		ycoord[1] = imp1.getHeight();
//		
//		
//		imp1.setRoi(new PolygonRoi(xcoord, ycoord, 2, Roi.POLYLINE));
		imp1.setRoi(new Line(xcoord[0], ycoord[0], xcoord[1], ycoord[1]));
//
//		IJ.setMinAndMax(imp1, 1, 20);
//		UtilAyv.showImageMaximized(imp1);	

		MyLine.decomposer3(imp1);
//		double[][] out1 = MyLine.decomposer2(imp1);
//		MyLog.logMatrix(out1, "out1");
//		MyLog.waitHere();

	}


}