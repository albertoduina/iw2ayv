package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utils.InputOutput;
import utils.MyLog;
import utils.TableUtils;
import utils.UtilAyv;

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
		ImagePlus imp1 = UtilAyv.imagesToStack16(list1);
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

}
