package utils;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import ij.plugin.Orthogonal_Views;

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

	@Test
	public final void testSearchSpotSphere() {

		String path1 = "./Data2/HC1-7";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, false);
		Boolean demo = false;
		ImagePlus imp204 = new Duplicator().run(imp1);
		if (demo)
			UtilAyv.showImageMaximized(imp1);
		else
			imp1.show();

		double[] center = MySphere.centerSphere(imp204, demo);
		MyLog.logVector(center, "center sphere");
		IJ.log("==================");

		demo = false;
		double[] out = MySphere.searchSpotSphere(imp204, center, "XY ", demo);
		MyLog.logVector(out, "XY dati spot finali");

		ImagePlus imp111 = MySphere.orthogonalStack(imp1, 1, demo);
		demo = false;
		double[] out2 = MySphere.searchSpotSphere(imp111, center, "XZ ", demo);
		double[] reorder1 = new double[4];
		reorder1[0] = out2[0];
		reorder1[1] = out2[2];
		reorder1[2] = out2[1];
		reorder1[3] = out2[3];

		MyLog.logVector(reorder1, "XZ dati spot finali");

		ImagePlus imp222 = MySphere.orthogonalStack(imp1, 2, demo);
		demo = false;
		double[] out3 = MySphere.searchSpotSphere(imp222, center, "YZ ", demo);
		double[] reorder2 = new double[4];
		reorder2[0] = out3[2];
		reorder2[1] = out3[1];
		reorder2[2] = out3[0];
		reorder2[3] = out3[3];

		MyLog.logVector(reorder2, "YZ dati spot finali");

		MyLog.waitHere("==== FINE ====");
	}

	@Test
	public final void testOrthogonalStack1() {

		String path1 = "./Data2/HC1-7";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, false);
		Boolean demo = false;
		imp1.show();

		ImagePlus imp111 = MySphere.orthogonalStack(imp1, 1, demo);
		imp111.show();
		MyLog.waitHere("==== FINE ====");
	}

	@Test
	public final void testOrthogonalStack2() {

		String path1 = "./Data2/HC1-7";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, false);
		Boolean demo = false;
		imp1.show();

		ImagePlus imp111 = MySphere.orthogonalStack(imp1, 2, demo);
		imp111.show();
		MyLog.waitHere("==== FINE ====");
	}

}
