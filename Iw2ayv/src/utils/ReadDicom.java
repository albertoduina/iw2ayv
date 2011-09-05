package utils;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.StringTokenizer;

public class ReadDicom {

	private static final int PIXEL_DATA = 0x7FE00010;

	/**
	 * La seguente routine, che si occupa di estrarre dati dall'header delle
	 * immagini è tratta da QueryDicomHeader.java di Anthony Padua & Daniel
	 * Barboriak - Duke University Medical Center. *** modified version ***
	 * Alberto Duina - Spedali Civili di Brescia - Servizio di Fisica Sanitaria
	 * 2006
	 * 
	 * @param imp
	 *            immagine di cui leggere l'header
	 * @param userInput
	 *            stringa di 9 caratteri contenente "group,element"
	 *            esempio:"0020,0013"
	 * @return stringa col valore del parametro
	 */
	public static String readDicomParameter(ImagePlus imp, String userInput) {
		// N.B. userInput => 9 characs [group,element] in format: xxxx,xxxx (es:
		// "0020,0013")
		// boolean bAbort;
		String attribute = "???";
		String value = "???";
		if (imp == null)
			return ("");
		int currSlice = imp.getCurrentSlice();
		ImageStack stack = imp.getStack();
		
		

		String header = stack.getSize() > 1 ? stack.getSliceLabel(currSlice)
				: (String) imp.getProperty("Info");

		if (header != null) {
			int idx1 = header.indexOf(userInput);
			int idx2 = header.indexOf(":", idx1);
			int idx3 = header.indexOf("\n", idx2);
			if (idx1 >= 0 && idx2 >= 0 && idx3 >= 0) {
				try {
					attribute = header.substring(idx1 + 9, idx2);
					attribute = attribute.trim();
					value = header.substring(idx2 + 1, idx3);
					value = value.trim();
					return (value);
				} catch (Throwable e) { // Anything else
					MyLog.here("value PROBLEM");
					return (value);
				}
			} else {
				attribute = "MISSING";
				return (attribute);
			}
		} else {
			String aux1 = (String) imp.getProperty("Info");
			IJ.log("aux1= "+aux1);

			IJ.log("" +imp.getTitle());
			
			IJ.error("readDicomParameter WARNING!! Header is null.");
			attribute = null;
			return (attribute);
		}
	}

	public static boolean hasHeader(ImagePlus imp) {
		if (imp == null)
			return false;
		int currSlice = imp.getCurrentSlice();
		ImageStack stack = imp.getStack();

		String header = stack.getSize() > 1 ? stack.getSliceLabel(currSlice)
				: (String) imp.getProperty("Info");

		if (header != null)
			return true;
		else
			return false;
	}

	public static String readHeader(ImagePlus imp) {
		if (imp == null)
			return null;
		int currSlice = imp.getCurrentSlice();
		ImageStack stack = imp.getStack();

		String header = stack.getSize() > 1 ? stack.getSliceLabel(currSlice)
				: (String) imp.getProperty("Info");
		return header;
	}

	/**
	 * estrae una parte di parametro dicom costituito da una stringa multipla
	 * 
	 * @param s1
	 *            stringa multipla
	 * @param number
	 *            selezione parte da restituire
	 * @return stringa con la parte selezionata
	 */
	public static String readSubstring(String s1, int number) {
		StringTokenizer st = new StringTokenizer(s1, "\\");
		int nTokens = st.countTokens();
		String substring = "ERROR";
		if (number > nTokens)
			return substring;
		else
			substring = st.nextToken();
		for (int i1 = 1; i1 < number; i1++) {
			substring = st.nextToken();
		}
		return substring;
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

	/**
	 * filtra i file non dicom e/o non contenenti pixel_data (per ovviare al
	 * problema dei dicom reports che in imagej danno un out of memory error
	 * 
	 * @param fileName1
	 *            path del file da validare
	 * @return true se è un immagine valida
	 */
	public static boolean isDicomOld(String fileName1) {
		int totalFileLen = 0;
		byte[] fBufCopy = null;

		boolean pixelsDataFound = false;

		try {
			BufferedInputStream f = new BufferedInputStream(
					new FileInputStream(fileName1));
			totalFileLen = f.available();
			fBufCopy = new byte[totalFileLen];
			f.read(fBufCopy, 0, totalFileLen);
			f.close();
		} catch (Exception e) {
			IJ.showMessage("preFilter", "Error opening " + fileName1
					+ "\n \n\"" + e.getMessage() + "\"");
		}

		for (int i1 = 0; i1 < totalFileLen - 4; i1++) {
			int b0 = 0xFF & fBufCopy[i1 + 0];
			int b1 = 0xFF & fBufCopy[i1 + 1];
			int b2 = 0xFF & fBufCopy[i1 + 2];
			int b3 = 0xFF & fBufCopy[i1 + 3];
			int littleEnd = ((b2 << 24) + (b3 << 16) + (b0 << 8) + b1);
			int bigEnd = ((b1 << 24) + (b0 << 16) + (b3 << 8) + b2);
			// non potendo sapere se sono immagini little o big endian cerco
			// tutte e due le occorrenze
			if (littleEnd == PIXEL_DATA) {
				pixelsDataFound = true;
				break;
			}
			if (bigEnd == PIXEL_DATA) {
				pixelsDataFound = true;
				break;
			}
		}
		return pixelsDataFound;
	}

}