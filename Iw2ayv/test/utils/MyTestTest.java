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

public class MyTestTest {

	public static float[][] imageFloatData;

	public static float[][] combined;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testImagePlusToByteArray() {
		String path = ".\\Test4\\aaa.dcm";
		ImagePlus imp1 = new Opener().openImage(path);
		if (imp1 == null)
			fail("Manca immagine");
		ImageProcessor ip1 = imp1.getProcessor();
		short[] pixels = (short[]) ip1.getPixels();
		byte[] out1 = MyTest.imagePlusToByteArray(imp1);
		StringBuffer hexString = new StringBuffer();
		StringBuffer decString = new StringBuffer();
		// for (int i1 = 0; i1 < out1.length; i1++) {
		for (int i1 = 0; i1 < 1000; i1++) {
			String s2 = Integer.toString((out1[i1] & 0xff) + 0x100, 16)
					.substring(1) + " ";
			hexString.append(s2);
		}
		String outString = hexString.toString();
		IJ.log(outString);
		for (int i1 = 0; i1 < 1000; i1++) {
			decString.append(pixels[i1] + " ");
		}
		outString = decString.toString();
		IJ.log(outString);
	}

	@Test
	public final void testGetSHA1DigestFromByteArray() {
		String path = ".\\Test4\\aaa.dcm";
		ImagePlus imp1 = new Opener().openImage(path);
		if (imp1 == null)
			fail("Manca immagine");
		byte[] out1 = MyTest.imagePlusToByteArray(imp1);
		String out2 = MyTest.getSHA1DigestFromByteArray(out1);
		IJ.log(out2);

		ImagePlus imp2 = new Opener().openImage(path);
		if (imp2 == null)
			fail("Manca immagine");
		
		byte[] out3 = MyTest.imagePlusToByteArray(imp2);
		out3[100]=0x1;
		String out4 = MyTest.getSHA1DigestFromByteArray(out3);
		IJ.log(out4);
	}

}
