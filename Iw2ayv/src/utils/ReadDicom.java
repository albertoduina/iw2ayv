package utils;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.io.Opener;
import ij.plugin.DICOM;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ReadDicom {

	private static final int PIXEL_DATA = 0x7FE00010;
	private static final int NON_IMAGE = 0x7FE10010;
	private static final int BINARY_DATA = 0x7FE11010;

	/**
	 * La seguente routine, che si occupa di estrarre dati dall'header delle
	 * immagini � tratta da QueryDicomHeader.java di Anthony Padua & Daniel
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
		// int sSize = stack.getSize();
		// String sLabel = stack.getSliceLabel(currSlice);
		// String iLabel = (String) imp.getProperty("Info");
		// IJ.log("-------------------------");
		// IJ.log("sSize= "+sSize);
		// IJ.log("sLabel= "+sLabel);
		// IJ.log("iLabel= "+iLabel);
		// MyLog.waitHere();

		String header = stack.getSize() > 1 ? stack.getSliceLabel(currSlice) : (String) imp.getProperty("Info");

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
			MyLog.waitHere("aux1= " + aux1);

			IJ.log("" + imp.getTitle());

			IJ.error("readDicomParameter WARNING!! Header is null.");
			attribute = null;
			return (attribute);
		}
	}

	public static String readDicomParameter(String header, String userInput) {
		// N.B. userInput => 9 characs [group,element] in format: xxxx,xxxx (es:
		// "0020,0013")
		// boolean bAbort;
		String attribute = "???";
		String value = "???";
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
			return (null);
		}
	}

	public static boolean hasHeader(ImagePlus imp) {
		if (imp == null)
			return false;
		int currSlice = imp.getCurrentSlice();
		ImageStack stack = imp.getStack();

		String header = stack.getSize() > 1 ? stack.getSliceLabel(currSlice) : (String) imp.getProperty("Info");

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

		String header = stack.getSize() > 1 ? stack.getSliceLabel(currSlice) : (String) imp.getProperty("Info");
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
	 * Ertrae tutte le parti di un parametro Dicom costituito da una stringa
	 * multipla
	 * 
	 * @param s1
	 * @return
	 */
	public static String[] parseString(String s1) {
		StringTokenizer parser = new StringTokenizer(s1, " \t\\;");
		int total = parser.countTokens();
		String[] vet = new String[total];
		for (int i2 = 0; i2 < total; i2++)
			vet[i2] = parser.nextToken();
		return vet;
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
	 * @return true se � un immagine valida
	 */
	public static boolean isDicomOld(String fileName1) {
		int totalFileLen = 0;
		byte[] fBufCopy = null;

		boolean pixelsDataFound = false;

		try {
			BufferedInputStream f = new BufferedInputStream(new FileInputStream(fileName1));
			totalFileLen = f.available();
			fBufCopy = new byte[totalFileLen];
			f.read(fBufCopy, 0, totalFileLen);
			f.close();
		} catch (Exception e) {
			IJ.showMessage("preFilter", "Error opening " + fileName1 + "\n \n\"" + e.getMessage() + "\"");
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

	/***
	 * Testa se � un file dicom ed � un immagine visualizzabile
	 * 
	 * @param fileName1
	 * @return
	 */
	public static boolean isDicomImage(String fileName1) {
		// IJ.redirectErrorMessages();
		int type = (new Opener()).getFileType(fileName1);
		if (type != Opener.DICOM) {
			return false;
		}
		ImagePlus imp1 = new Opener().openImage(fileName1);
		if (imp1 == null) {
			return false;
		}
		String info = new DICOM().getInfo(fileName1);
		if (info == null || info.length() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/***
	 * Legge il codice dall'header Dicom
	 * 
	 * @param imp1
	 * @return
	 */
	public static String getCode(ImagePlus imp1) {

		String seriesDescription = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_SERIES_DESCRIPTION);
		String codice = "";
		if (seriesDescription.length() >= 5) {
			codice = seriesDescription.substring(0, 5).trim();
		}
		return codice;
	}

	public static String getFirstCoil(ImagePlus imp1) {

		String total = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_COIL);
		int i1 = total.indexOf(";");
		String coil = "";
		if (i1 == -1) {
			coil = total;
		} else {
			coil = total.substring(0, i1);
		}
		return coil;
	}

	/***
	 * getThisCoil
	 * 
	 * @param imp1
	 * @param vetCoils
	 *            vettore coi le coils di cui verificare la presenza
	 * @return
	 */
	public static String getThisCoil(ImagePlus imp1, String[] vetCoils) {

		if (vetCoils == null) {
			// MyLog.waitHere("null1");
			return null;
		}
		String total = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_COIL);
		// MyLog.waitHere("total= " + total);

		String[] listCoils = splitCoils(total);
		// MyLog.waitHere("listCoils[0]= " + listCoils[0]);
		for (int i1 = 0; i1 < listCoils.length; i1++) {
			for (int i2 = 0; i2 < vetCoils.length; i2++) {
				// MyLog.waitHere("total= "+total+ " vetCoils["+i2+"]=
				// "+vetCoils[i2]);
				if (total.equals(vetCoils[i2])) {
					// MyLog.waitHere("trovato?");
					return total;
				}
				if (vetCoils[0].equals("xxx")) {
					return total;
				}
				if (listCoils[i1].equals(vetCoils[i2]))
					return vetCoils[i2];
			}
		}
		// MyLog.logVector(vetCoils, "vetCoils");
		// MyLog.logVector(listCoils, "listCoils");
		// MyLog.waitHere("null2");
		return null;
	}

	public static String getAllCoils(ImagePlus imp1) {

		String total = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_COIL);
		return total;
	}

	public static String[] splitCoils(String multiCoil) {
		String[] subCoils;
		if (multiCoil.contains(";")) {
			subCoils = multiCoil.split(";");
		} else {
			subCoils = new String[1];
			subCoils[0] = multiCoil;
		}
		return subCoils;
	}

}