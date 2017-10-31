package utils;

import java.awt.Frame;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.plugin.Duplicator;
import ij.plugin.Orthogonal_Views;
import ij.process.ImageConverter;

public class MySphereTest {

	@Before
	public void setUp() throws Exception {
		new ImageJ(ImageJ.NORMAL);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testCircleCannyEdge() {

		String path1 = "./Data/uno/002A";
		IJ.log("=== start ====");
		Frame frame = WindowManager.getFrame("Log");
		frame.setSize(240, 280);

		// ImagePlus imp1 = UtilAyv.openImageMaximized(path1);
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, false);
		double maxFitError = 21.0;
		double maxBubbleGapLimit = 3.0;
		boolean demo = false;
		// -------------------------------------------------------------
		// stabilisco la direzione con un troiaio, poiche' GE la scrive
		// in modo creativo con lo zero signed
		// -------------------------------------------------------------
		String direction1 = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_IMAGE_ORIENTATION);
		String delims = "\\\\";
		String[] directionCosines = direction1.split(delims);
		int[] dirCosines = new int[directionCosines.length];
		for (int i1 = 0; i1 < directionCosines.length; i1++) {
			dirCosines[i1] = Integer.parseInt(directionCosines[i1]);
		}
		// 0020,0037 Image Orientation (Patient): 1\0\0\0\1\0 tutti
		// 0020,0037 Image Orientation (Patient): 1\-0\0\-0\1\0 esine
		int[] iaxial = { 1, 0, 0, 0, 1, 0 };
		// 0020,0037 Image Orientation (Patient): 0\1\0\0\0\-1 tutti
		// 0020,0037 Image Orientation (Patient): -0\1\0\-0\-0\-1 esine
		int[] isagittal = { 0, 1, 0, 0, 0, -1 };
		int direction = 0;
		if (Arrays.equals(dirCosines, iaxial))
			direction = 1;
		if (Arrays.equals(dirCosines, isagittal))
			direction = 2;
		// -------------------------------------------------------------

		double[] center = MySphere.centerCircleCannyEdge(imp1, direction, maxFitError, maxBubbleGapLimit, demo);
		MyLog.logVector(center, "dati cerchio finali");
		MyLog.waitHere("==== FINE ====");
	}

	@Test
	public final void testCenterSphere() {

		String path1 = "./Data2/HC1-7";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, false);
		Boolean demo = false;
		if (demo)
			UtilAyv.showImageMaximized(imp1);
		else
			imp1.show();

		double[] center = MySphere.centerSphere(imp1, demo);
		MyLog.logVector(center, "dati sfera finali");
		MyLog.waitHere("==== FINE ====");
	}

	/***
	 * questo e'il test di come potrebbe essere la ricerca di uno spot sferico
	 * (cerco i tre spot circolari nelle tre direzioni)
	 */
	@Test
	public final void testSearchSpotSphere() {

		Boolean demo = true;
		int demoLevel = 0;

		if (demo) {
			IJ.log("=== start ====");
			Frame frame = WindowManager.getFrame("Log");
			frame.setSize(600, 1200);
		}

		String path1 = "./Data2/HC1-7";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, false);
		ImagePlus imp204 = new Duplicator().run(imp1);
		if (demo)
			UtilAyv.showImageMaximized(imp1);
		else
			imp1.show();

		double[] center = MySphere.centerSphere(imp204, demo);
		if (demo) {
			MyLog.logVector(center, "center sphere");
			IJ.log("==================");
		}

		
		
		
		int demolevel = 1;

		double[] out = MySphere.searchCircularSpot(imp204, center, "XY ", demolevel);
		if (demo)
			MyLog.logVector(out, "XY dati spot finali");

		ImagePlus imp111 = MySphere.createOrthogonalStack(imp1, 1, demo);
		demo = true;
		demolevel = 2;
		double[] out2 = MySphere.searchCircularSpot(imp111, center, "XZ ", demolevel);
		double[] reorder1 = new double[4];
		reorder1[0] = out2[0];
		reorder1[1] = out2[2];
		reorder1[2] = out2[1];
		reorder1[3] = out2[3];
		if (demo)
			MyLog.logVector(reorder1, "XZ dati spot finali");

		ImagePlus imp222 = MySphere.createOrthogonalStack(imp1, 2, demo);
		demo = true;
		demolevel = 3;
		double[] out3 = MySphere.searchCircularSpot(imp222, center, "YZ ", demolevel);
		double[] reorder2 = new double[4];
		reorder2[0] = out3[2];
		reorder2[1] = out3[1];
		reorder2[2] = out3[0];
		reorder2[3] = out3[3];

		if (demo) {
			MyLog.logVector(reorder2, "YZ dati spot finali");
		}
		IJ.showMessage("==== FINE ====");
	}

	@Test
	public final void testOrthogonalStack1() {

		String path1 = "./Data2/HC1-7";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, false);
		Boolean demo = false;
		imp1.show();

		ImagePlus imp111 = MySphere.createOrthogonalStack(imp1, 1, demo);
		imp111.show();
		MyLog.waitHere("==== FINE ====");
	}

	@Test
	public final void testOrthogonalStack2() {

		String path1 = "./Data2/HC1-7";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, false);
		Boolean demo = false;
		imp1.show();

		ImagePlus imp111 = MySphere.createOrthogonalStack(imp1, 2, demo);
		imp111.show();
		MyLog.waitHere("==== FINE ====");
	}

	@Test
	public final void testMaskHotSphere() {

		String path1 = "./Data2/HC1-7";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, false);
		// IJ.run(imp1, "AND...", "value=00 stack");

		imp1.show();
		MyLog.waitHere("prima");
		ImageStack imaStack = imp1.getImageStack();

		int xc = 220;
		int yc = 220;
		int zc = 90;
		int radius = 20;
		int value = 4000;

		MySphere.maskHotSphere(imaStack, xc, yc, zc, radius, value);
		imp1.updateAndDraw();
		MyLog.waitHere("dopo");
		MyLog.waitHere("==== FINE ====");
	}

	@Test
	public final void testMaskRGBHotSphere() {

		String path1 = "./Data2/HC1-7";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, false);
		IJ.run(imp1, "RGB Color", "");
		ImageConverter ic1 = new ImageConverter(imp1);
		ic1.convertToRGBStack();

		imp1.show();
		MyLog.waitHere("prima");
		ImageStack imaStack = imp1.getImageStack();

		int xc = 100;
		int yc = 150;
		int zc = 80;
		int radius = 50;
		int value = 10000;

		MySphere.maskRGBHotSphere(imaStack, xc, yc, zc, radius, value);
		imp1.updateAndDraw();
		MyLog.waitHere("dopo");
		MyLog.waitHere("==== FINE ====");
	}

}
