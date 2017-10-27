package utils;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ij.ImageJ;
import ij.ImagePlus;

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
		if (demo)
			UtilAyv.showImageMaximized(imp1);
		else
			imp1.show();
		double[] center = MySphere.centerSphere(imp1, demo);
		MyLog.logVector(center, "center sphere");
		MyLog.waitHere();
		demo = false;
		double[] out = MySphere.searchSpotSphere(imp1, center, demo);
		MyLog.logVector(out, "dati spot finali");
		MyLog.waitHere("==== FINE ====");
	}

}
