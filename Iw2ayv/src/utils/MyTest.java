package utils;

import ij.ImagePlus;
import ij.process.ImageProcessor;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyTest {

	public static byte[] imagePlusToByteArray(ImagePlus imp1) {
		ImageProcessor ip1 = imp1.getProcessor();
		byte[] pixels = null;
		switch (imp1.getBitDepth()) {
		case 8:
			pixels = (byte[]) ip1.getPixels();
			break;
		case 16:
			pixels = shortToByteArray((short[]) ip1.getPixels());
			break;
		case 32:
			pixels = floatToByteArray((float[]) ip1.getPixels());
			break;
		}
		return pixels;
	}

	public static byte[] floatToByteArray(float[] floatArray) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		for (float m1 : floatArray) {
			try {
				out.writeFloat(m1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}

	public static byte[] shortToByteArray(short[] shortArray) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		for (short m1 : shortArray) {
			try {
				out.writeShort(m1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}

	public static String getSHA1DigestFromByteArray(byte[] b) {
		MessageDigest md1 = null;

		// assign a message digest from type SHA1
		try {
			md1 = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
			return null;
		}

		for (byte bte : b)
			md1.update(bte);
		byte[] mdbytes = md1.digest();

		// convert the byte to hex format method 2
		StringBuffer hexString = new StringBuffer();
		for (int i1 = 0; i1 < mdbytes.length; i1++) {
			hexString.append(Integer.toHexString(0xFF & mdbytes[i1]));
		}

		String outString = hexString.toString();

		return outString;
	}

}
