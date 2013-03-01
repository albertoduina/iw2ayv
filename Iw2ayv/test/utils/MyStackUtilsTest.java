package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.io.Opener;
import ij.process.ImageStatistics;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MyStackUtilsTest {

	public static float[][] imageFloatData;

	public static float[][] combined;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testImageFromStack() {
		String[] list1 = InputOutput
				.readStringArrayFromFile("./data/list1.txt");
		ImagePlus imp1 = MyStackUtils.imagesToStack16(list1);
		int len = imp1.getImageStackSize();
		assertEquals(16, len);
		// --- stack ready
		ImagePlus imp2 = MyStackUtils.imageFromStack(imp1, 8);
		ImagePlus imp3 = new Opener().openImage(list1[7]);
		String sliceInfo2 = (String) imp2.getProperty("Info");
		IJ.log(sliceInfo2);

		String sliceInfo3 = (String) imp3.getProperty("Info");
		assertEquals(sliceInfo2, sliceInfo3);
	}

	@Test
	public final void testImagesToStack16() {
		String[] list1 = InputOutput
				.readStringArrayFromFile("./data/list1.txt");
		ImagePlus imp1 = MyStackUtils.imagesToStack16(list1);
		int len = imp1.getImageStackSize();
		imp1.show();
		IJ.wait(200);
		assertEquals(16, len);
	}

	@Test
	public final void testImagesToStack32() {
		String[] list1 = InputOutput
				.readStringArrayFromFile("./data/list1.txt");
		ImagePlus imp1 = MyStackUtils.imagesToStack32(list1);
		int len = imp1.getImageStackSize();
		assertEquals(16, len);
		imp1.show();
		IJ.wait(200);
		int depth = imp1.getBitDepth();
		// IJ.log("stack bitDepth= " + depth);
		assertEquals(32, depth);
	}

	@Test
	public final void testImageFromMosaic() {
		String path = "./data/mosaic.dcm";

		ImagePlus imp1 = UtilAyv.openImageMaximized(path);
		// MyLog.waitHere();
		for (int i1 = 0; i1 < 36; i1++) {
			ImagePlus imp2 = MyStackUtils.imageFromMosaic(imp1, i1);
			imp2.show();
			IJ.wait(1000);
		}

	}

	@Test
	public final void testImageFromMosaicWithOffset() {
		String path = "./data/mosaic.dcm";

		ImagePlus imp1 = UtilAyv.openImageMaximized(path);
		// MyLog.waitHere();
		for (int i1 = 0; i1 < 36; i1++) {
			ImagePlus imp2 = MyStackUtils.imageFromMosaicWithOffset(imp1, i1,
					5, 5);
			imp2.show();
			IJ.wait(1000);
		}

	}

	@Test
	public final void testCompareStacks() {
		String[] list1 = InputOutput
				.readStringArrayFromFile("./data/list1.txt");
		ImagePlus imp1 = MyStackUtils.imagesToStack16(list1);
		ImagePlus imp2 = MyStackUtils.imagesToStack16(list1);

		boolean result = MyStackUtils.compareStacks(imp1, imp2);
		assertTrue("Stacks differ", result);

		list1[0] = ".\\Test4\\01diff";
		imp2 = MyStackUtils.imagesToStack16(list1);
		boolean result2 = MyStackUtils.compareStacks(imp1, imp2);
		assertFalse("Stacks must differ!", result2);
	}

	@Test
	public final void testStackStatistics() {
		String[] list1 = InputOutput
				.readStringArrayFromFile("./data/list1.txt");
		ImagePlus imp1 = MyStackUtils.imagesToStack16(list1);
		int len = imp1.getImageStackSize();
		assertEquals(16, len);
		// --- stack ready

		int width = imp1.getWidth();
		int height = imp1.getHeight();
		imp1.setRoi(width / 3, height / 3, width / 3, height / 3);
		Roi roi1 = imp1.getRoi();

		ImageStatistics[] stat1 = MyStackUtils.stackStatistics(imp1, roi1);

		for (int i1 = 0; i1 < stat1.length; i1++) {
			IJ.log("slice= " + (i1 + 1) + " mean= " + stat1[i1].mean);
		}
	}

}
