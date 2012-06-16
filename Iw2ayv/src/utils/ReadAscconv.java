package utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import ij.IJ;
import ij.ImagePlus;

public class ReadAscconv {

	public int location;
	private BufferedInputStream bis;

	public String read(String path1) {
		String blob = "";
		try {
			FileInputStream fis = new FileInputStream(path1);
			BufferedInputStream bis = new BufferedInputStream(fis);
			int size = bis.available();
			String header = getString(bis, size - 20);
			int fromIndex = 1;
			int index1 = header.indexOf("### ASCCONV BEGIN", fromIndex);
			int index2 = header.indexOf("ASCCONV END ###", fromIndex + 1);
//			IJ.log("indexBegin= " + index1 + "  indexEnd= " + index2);

			while ((index1 > 0) && (index2 > 0)) {
				blob = header.substring(index1, index2 + 19);
				fromIndex = index2;
				index1 = header.indexOf("### ASCCONV BEGIN", fromIndex);
				index2 = header.indexOf("ASCCONV END ###", fromIndex + 1);
			}

		} catch (IOException e) {
			return null;
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
			bis = null; // BufferedInputStream.close() erst ab Java 1.2
						// definiert
		}
//		IJ.log(blob);
		return blob;
	}

	public String getString(BufferedInputStream bo, int len) throws IOException {
		int pos = 0;
		byte[] buf = new byte[len];
		int size = bo.available();
		while (pos < len) {
			int count = bo.read(buf, pos, len - pos);
			pos += count;
		}
		location += len;
		return new String(buf);
	}
	
	public static String readAscParameter(String header, String userInput) {
		String attribute="????";
		if (header != null) {
			int idx1 = header.indexOf(userInput);
			int idx2 = header.indexOf("=", idx1);
			int idx3 = header.indexOf("\n", idx2);
			if (idx1 >= 0  && idx3 >= 0) {
				try {
					attribute = header.substring(idx2+1, idx3);
					attribute = attribute.trim();
					return (attribute);
				} catch (Throwable e) { // Anything else
					MyLog.here("value PROBLEM");
					return (attribute);
				}
			} else {
				attribute = "MISSING";
				return (attribute);
			}
		} else {
			return (null);
		}
	}

	/**
	 * legge un valore double da una stringa
	 * 
	 * @param s1
	 *            stringa input
	 * @return valore double letto in s1
	 */
	public static double readDouble(String s1) {
		double x = 0;
		try {
			x = (new Double(s1)).doubleValue();
		} catch (Exception e) {
			// IJ.error("readDouble >> invalid floating point number");
			// tolto il messaggio per evitare isterismi nell'utenza
		}
		return x;
	}

	/**
	 * legge un valore float da una stringa
	 * 
	 * @param s1
	 *            stringa input
	 * @return valore float letto in s1
	 */
	public static float readFloat(String s1) {
		float x = 0;
		try {
			x = (new Float(s1)).floatValue();
		} catch (Exception e) {
			// IJ.error("readFloat >> invalid floating point number");
		}
		return x;
	}

	/**
	 * legge un valore integer da una stringa
	 * 
	 * @param s1
	 *            stringa input
	 * @return valore integer letto in s1
	 */
	public static int readInt(String s1) {
		int x = 0;
		try {
			x = (new Integer(s1)).intValue();
		} catch (Exception e) {
			// IJ.error(" readInt >> invalid integer number ");
		}
		return x;
	}


}