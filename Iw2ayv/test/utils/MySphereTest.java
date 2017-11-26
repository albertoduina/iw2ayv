package utils;

import static org.junit.Assert.assertTrue;

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
import ij.gui.OvalRoi;
import ij.gui.Roi;
import ij.io.Opener;
import ij.plugin.Duplicator;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

public class MySphereTest {

	@Before
	public void setUp() throws Exception {
		new ImageJ(ImageJ.NORMAL);
	}

	@After
	public void tearDown() throws Exception {
	}

	/***
	 * Ricerca del centro di un fantoccio circolare testato e funzionante
	 * 01/11/2017
	 */
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

		double[] expected = { 136, 131, 113 };
		assertTrue(ArrayUtils.compareVectors(center, expected, 1e-8, "errore comparazione"));

		MyLog.waitHere("==== FINE ====");
	}

	/***
	 * Ricerca del centro di una sfera testato e funzionante 01/11/2017
	 */

	@Test
	public final void testCenterSphere() {

		String path1 = "./Data2/HC1-7";
		IJ.log("=== start ====");
		Frame frame = WindowManager.getFrame("Log");
		frame.setSize(240, 280);

		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, false);
		Boolean demo = false;
		if (demo)
			UtilAyv.showImageMaximized(imp1);
		else
			imp1.show();

		double[] center = MySphere.centerSphere(imp1, demo);
		MyLog.logVector(center, "dati sfera finali x, y, z, diam");

		double[] expected = { 127, 130, 87, 171 };
		assertTrue(ArrayUtils.compareVectors(center, expected, 1e-8, "errore comparazione"));

		MyLog.waitHere("==== FINE ====");
	}

	/***
	 * questo e'il test di come potrebbe essere la ricerca di uno spot sferico
	 * (cerco i tre spot circolari nelle tre direzioni)
	 */
	@Test
	public final void testSearchSpotSphere() {

		Boolean demo = false;
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

		double[] center1 = MySphere.centerSphere(imp204, demo);
		if (true) {
			IJ.log("==================");
			MyLog.logVector(center1, "XYZ center sphere x,y,z,diam");
		}

		int diamsearch = 14;
		double[] out = MySphere.searchCircularSpot(imp204, center1, diamsearch, "XY ", demoLevel);
		if (true)
			MyLog.logVector(out, "XYZ dati spot finali x,y,z,mean");

		ImagePlus imp111 = MySphere.createOrthogonalStack(imp1, 1, demo);

		double[] center2 = MySphere.centerSphere(imp111, demo);

		if (true) {
			IJ.log("==================");
			MyLog.logVector(center2, "XZY center sphere x,z,y,diam");
		}

		demo = true;
		demoLevel = 0;
		double[] out2 = MySphere.searchCircularSpot(imp111, center2, diamsearch, "XZ ", demoLevel);
		double[] reorder1 = new double[4];
		reorder1[0] = out2[0];
		reorder1[1] = out2[2];
		reorder1[2] = out2[1];
		reorder1[3] = out2[3];
		if (demo)
			MyLog.logVector(reorder1, "XZY dati spot finali x,y,z,mean");

		ImagePlus imp222 = MySphere.createOrthogonalStack(imp1, 2, demo);
		double[] center3 = MySphere.centerSphere(imp222, demo);
		if (true) {
			IJ.log("==================");
			MyLog.logVector(center3, "YZX center sphere y,z,x,diam");
		}
		demo = true;
		demoLevel = 0;
		double[] out3 = MySphere.searchCircularSpot(imp222, center3, diamsearch, "YZ ", demoLevel);
		double[] reorder2 = new double[4];
		reorder2[0] = out3[2];
		reorder2[1] = out3[1];
		reorder2[2] = out3[0];
		reorder2[3] = out3[3];

		if (demo) {
			MyLog.logVector(reorder2, "YZX dati spot finali x,y,z,mean");
			IJ.log("==================");
		}
		MyLog.waitHere("==== FINE ====");
	}

	/***
	 * Creazione di uno stack ortogonale 1= XZ, 2= YZ
	 */
	@Test
	public final void testCreateOrthogonalStack1() {

		String path1 = "./Data2/HC1-7";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, false);
		Boolean demo = false;
		imp1.show();

		ImagePlus imp111 = MySphere.createOrthogonalStack(imp1, 1, demo);
		imp111.show();
		MyLog.waitHere("==== FINE ====");
	}

	/***
	 * Creazione di uno stack ortogonale 1= XZ, 2= YZ
	 */

	@Test
	public final void testCreateOrthogonalStack2() {

		String path1 = "./Data2/HC1-7";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, false);
		Boolean demo = false;
		imp1.show();

		ImagePlus imp111 = MySphere.createOrthogonalStack(imp1, 2, demo);
		imp111.show();
		MyLog.waitHere("==== FINE ====");
	}

	@Test
	public final void testAddSphere() {

		String path1 = "./Data2/HC1-7";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, false);
		boolean generate = true;
		ImagePlus impMapR = null;
		ImagePlus impMapG = null;
		ImagePlus impMapB = null;
		ImagePlus impMapRGB = null;
		ImageStack stackRGB = null;
		int width = imp1.getWidth();
		int height = imp1.getHeight();
		int depth = imp1.getImageStackSize();
		int bitdepth = 24;
		int myColors = 2;
		if (generate) {
			impMapR = MySphere.generaMappazzaVuota16(width, height, depth, "impMappazzaR");
			impMapG = MySphere.generaMappazzaVuota16(width, height, depth, "impMappazzaG");
			impMapB = MySphere.generaMappazzaVuota16(width, height, depth, "impMappazzaB");
			stackRGB = ImageStack.create(width, height, depth, bitdepth);
			impMapRGB = new ImagePlus("MAPPAZZA_" + myColors, stackRGB);
			generate = false;
		}

		double[] sphere1 = new double[4];
		sphere1[0] = imp1.getWidth() / 2;
		sphere1[1] = imp1.getHeight() / 2;
		sphere1[2] = imp1.getImageStackSize() / 2;
		sphere1[3] = depth * 3 / 4 - 4;

		int[] colorRGB3 = { 150, 150, 150 };
		int[] bounds = new int[3];
		bounds[0] = imp1.getWidth();
		bounds[1] = imp1.getHeight();
		bounds[2] = imp1.getImageStackSize();

		MyLog.logVector(sphere1, "sphere1");

		MySphere.addSphere(impMapR, impMapG, impMapB, sphere1, bounds, colorRGB3, true);

		int radius = 10;
		double[] sphere2 = new double[4];
		sphere2[0] = (imp1.getWidth() - radius) / 2;
		sphere2[1] = (imp1.getHeight() - radius) / 2;

		sphere2[0] = 100;
		sphere2[1] = 150;

		sphere2[2] = 40;
		sphere2[3] = radius;

		int[] colorRGB = { 150, 150, 0 };
		MyLog.logVector(bounds, "bounds");
		MyLog.logVector(sphere2, "sphere2");
		MySphere.addSphere(impMapR, impMapG, impMapB, sphere2, bounds, colorRGB, false);

		radius = 20;
		double[] sphere3 = new double[4];
		sphere3[0] = sphere2[0] + radius;
		sphere3[1] = sphere2[1] - radius;
		sphere3[2] = sphere2[2];
		sphere3[3] = radius;

		int[] colorRGB2 = { 100, 10, 200 };
		MyLog.logVector(sphere3, "sphere3");
		MySphere.addSphere(impMapR, impMapG, impMapB, sphere3, bounds, colorRGB2, false);

		impMapR.show();
		impMapG.show();
		impMapB.show();

		MySphere.compilaMappazzaCombinata(impMapR, impMapG, impMapB, impMapRGB, myColors);
		impMapRGB.show();

		MyLog.waitHere("==== FINE ====");
	}

	@Test
	public final void testVectorizeSphericalSpot() {

		String path1 = "./Data2/HC1-7";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, false);
		int demolevel = 0;
		boolean demo = false;
		imp1.show();
		double[] sphere1 = MySphere.centerSphere(imp1, demo);
		MyLog.logVector(sphere1, "sphere1");

		ImagePlus impMapR = null;
		ImagePlus impMapG = null;
		ImagePlus impMapB = null;
		ImagePlus impMapRGB = null;
		ImageStack stackRGB = null;
		int width = imp1.getWidth();
		int height = imp1.getHeight();
		int depth = imp1.getImageStackSize();
		int bitdepth = 24;
		int myColors = 2;

		impMapR = MySphere.generaMappazzaVuota16(width, height, depth, "impMappazzaR");
		impMapG = MySphere.generaMappazzaVuota16(width, height, depth, "impMappazzaG");
		impMapB = MySphere.generaMappazzaVuota16(width, height, depth, "impMappazzaB");
		stackRGB = ImageStack.create(width, height, depth, bitdepth);
		impMapRGB = new ImagePlus("MAPPAZZA_" + myColors, stackRGB);

		double[] sphere2 = new double[4];
		sphere2[0] = 120;
		sphere2[1] = 90;
		sphere2[2] = 80;
		sphere2[3] = 4;

		double[] vetpixel = MySphere.vectorizeSphericalSpot(imp1, sphere1, sphere2);
		MyLog.logVector(vetpixel, "vetpixel");

		int[] colorRGB3 = { 0, 100, 100 };
		int[] bounds = new int[3];
		bounds[0] = imp1.getWidth();
		bounds[1] = imp1.getHeight();
		bounds[2] = imp1.getImageStackSize();

		MySphere.addSphere(impMapR, impMapG, impMapB, sphere2, bounds, colorRGB3, false);
		MySphere.compilaMappazzaCombinata(impMapR, impMapG, impMapB, impMapRGB, myColors);

		impMapR.show();
		impMapG.show();
		impMapB.show();
		impMapRGB.show();

		MyLog.waitHere("==== FINE ====");
	}

	@Test
	public final void testSimulataGrigio16() {

		String path1 = "./Data2/HC1-7";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, false);
		imp1.show();
		int width = imp1.getWidth();
		int height = imp1.getHeight();
		int depth = imp1.getImageStackSize();
//		int bitdepth = 24;
//		int myColors = 3;
		int livello = 2;

		int[] minimiClassi = { 20, 10, -10, -20, -30, -40, -50, -60, -70, -80, -90, -100 };
		int[] massimiClassi = { 100, 20, 10, -10, -20, -30, -40, -50, -60, -70, -80, -90 };

		ImagePlus impMapR = MySphere.generaMappazzaVuota16(width, height, depth, "impMappazzaR");
		ImagePlus impMapG = MySphere.generaMappazzaVuota16(width, height, depth, "impMappazzaG");
		ImagePlus impMapB = MySphere.generaMappazzaVuota16(width, height, depth, "impMappazzaB");
//		ImageStack stackRGB = ImageStack.create(width, height, depth, bitdepth);
//		ImagePlus impMapRGB = new ImagePlus("MAPPAZZA_" + myColors, stackRGB);

		int slice = 0;
		int colorCoil = 0;
		for (int i1 = 0; i1 < depth; i1++) {
			slice = i1 + 1;
			ImagePlus imp2 = MyStackUtils.imageFromStack(imp1, slice);
			double mean2 = 50;
			int debuglevel = 2;
			int puntatore = 1;

			double[] circle = new double[3];
			circle[0] = width / 2;
			circle[1] = height / 2;
			circle[2] = 100;

			double[] sphere = new double[4];
			sphere[0] = width / 2;
			sphere[1] = height / 2;
			sphere[2] = imp2.getImageStackSize();
			sphere[3] = 200;

			// MySphere.simulataGrigio16(mean2, imp2, impMapR, impMapG, impMapB,verde
			// slice, livello, minimiClassi,
			// massimiClassi, colorCoil, myColors, puntatore, debuglevel);
			
			MySphere.simulataGrigio16(mean2, imp2, circle, impMapR, impMapG, impMapB, slice, livello, minimiClassi,
					massimiClassi, colorCoil, puntatore, debuglevel, sphere);

			colorCoil++;
			if (colorCoil > 5)
				colorCoil = 0;
		}
		impMapR.show();
		impMapG.show();
		impMapB.show();
		MyLog.waitHere();
	}

	@Test
	public final void testBoh() {

		String path1 = ".\\Test4\\17";
		ImagePlus imp2 = new Opener().openImage(path1);
		ImageProcessor ip2 = imp2.getProcessor();
		imp2.show();
		imp2.setRoi(new OvalRoi(100, 100, 80, 40));
		Roi roi2 = imp2.getRoi();
		ImageProcessor impMask = roi2.getMask();
		byte[] pixels = (byte[]) impMask.getPixels();
		if (pixels == null)
			IJ.log("pixels==null");

		byte[] maskarray1 = ip2.getMaskArray();
		if (maskarray1 == null)
			IJ.log("maskarray1==null");

		MyLog.waitHere();
	}

}
