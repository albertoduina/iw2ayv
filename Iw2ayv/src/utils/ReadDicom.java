package utils;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.WaitForUserDialog;
import ij.io.Opener;
import ij.plugin.DICOM;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

public class ReadDicom {

	private static final int PIXEL_DATA = 0x7FE00010;
	private static final int NON_IMAGE = 0x7FE10010;
	private static final int BINARY_DATA = 0x7FE11010;
	public int location;

	/**
	 * La seguente routine, che si occupa di estrarre dati dall'header delle
	 * immagini fu copiata dal QueryDicomHeader.java di Anthony Padua & Daniel
	 * Barboriak - Duke University Medical Center. *** modified version *** Alberto
	 * Duina - Spedali Civili di Brescia - Servizio di Fisica Sanitaria 2006
	 * 
	 * @param imp       immagine di cui leggere l'header
	 * @param userInput stringa di 9 caratteri contenente "group,element"
	 *                  esempio:"0020,0013"
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
//		IJ.log(header);
//		MyLog.waitHere();

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
			// MyLog.waitThere("aux1= " + aux1);

			IJ.log("" + imp.getTitle());
			MyLog.trace("readDicomParameter WARNING!! Header is null.", true);
			MyLog.trace("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + " line="
					+ Thread.currentThread().getStackTrace()[2].getLineNumber() + "\n" + "file="
					+ Thread.currentThread().getStackTrace()[3].getFileName() + " " + " line="
					+ Thread.currentThread().getStackTrace()[3].getLineNumber(), true);

//			IJ.error("readDicomParameter WARNING!! Header is null.");
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

	/***
	 * Questa modifica permette di leggere due TAG, uno per le immagini RMN salvate
	 * in modalita' normale ed il secondo per le immagini salvate in modalita'
	 * ENHANCED
	 * 
	 * @param header
	 * @param userInput1
	 * @param userInput2
	 * @return
	 */
	public static String readDicomParameter(String header, String userInput1, String userInput2) {
		// N.B. userInput => 9 characs [group,element] in format: xxxx,xxxx (es:
		// "0020,0013")
		// boolean bAbort;
		String attribute = "???";
		String value = "???";
		if (header != null) {

			int first = header.indexOf(userInput1);
			int second = header.indexOf(userInput2);
			MyLog.waitHere("first= " + first + " second= " + second);

			int idx1 = header.indexOf(userInput1);
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

	public static String readDicomParameter(ImagePlus imp, String userInput1, String userInput2) {
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

			int uno = header.indexOf(userInput1);
			int due = header.indexOf(userInput2);
			String userInput = "";

			if (due > 0)
				userInput = userInput2;
			else
				userInput = userInput1;

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
			// MyLog.waitThere("aux1= " + aux1);

			IJ.log("" + imp.getTitle());
			MyLog.trace("readDicomParameter WARNING!! Header is null.", true);
			MyLog.trace("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + " line="
					+ Thread.currentThread().getStackTrace()[2].getLineNumber() + "\n" + "file="
					+ Thread.currentThread().getStackTrace()[3].getFileName() + " " + " line="
					+ Thread.currentThread().getStackTrace()[3].getLineNumber(), true);

//			IJ.error("readDicomParameter WARNING!! Header is null.");
			attribute = null;
			return (attribute);
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
	 * @param s1     stringa multipla
	 * @param number selezione parte da restituire
	 * @return stringa con la parte selezionata
	 */
	public static String readSubstring(String s1, int number) {
//		MyLog.waitHere("stringa del casso= "+s1);
		StringTokenizer st = new StringTokenizer(s1, "\\ ");
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
	 * Estrae tutte le parti di un parametro Dicom costituito da una stringa
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
	 * @param s1 stringa input
	 * @return valore double letto in s1
	 */
	public static double readDouble(String s1) {
		double x = 0;
		try {
			x = Double.valueOf(s1);
		} catch (Exception e) {
			// IJ.error("readDouble >> invalid floating point number");
			// tolto il messaggio per evitare isterismi nell'utenza
		}
		return x;
	}

	/**
	 * legge un valore float da una stringa
	 * 
	 * @param s1 stringa input
	 * @return valore float letto in s1
	 */
	public static float readFloat(String s1) {
		float x = 0;
		try {
			x = Float.valueOf(s1);
		} catch (Exception e) {
			// IJ.error("readFloat >> invalid floating point number");
		}
		return x;
	}

	/**
	 * legge un valore integer da una stringa
	 * 
	 * @param s1 stringa input
	 * @return valore integer letto in s1
	 */
	public static int readInt(String s1) {
		int x = 0;
		try {
			x = Integer.valueOf(s1);
		} catch (Exception e) {
			// IJ.error(" readInt >> invalid integer number ");
		}
		return x;
	}

	/**
	 * filtra i file non dicom e/o non contenenti pixel_data (per ovviare al
	 * problema dei dicom reports che in imagej danno un out of memory error
	 * 
	 * @param fileName1 path del file da validare
	 * @return true se e' un immagine valida
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
	 * Testa se e' un file dicom ed e' un immagine visualizzabile
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
	 * Testa se e' un file dicom ed e' un immagine visualizzabile
	 * 
	 * @param fileName1
	 * @return
	 */
	public static boolean isDicomEnhanced(ImagePlus imp1) {
		// IJ.redirectErrorMessages();
		String sopClassUID = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_SOP_CLASS_UID);
		String MRimageStorage = "1.2.840.10008.5.1.4.1.1.4";
		String EnhancedMRimageStorage = "1.2.840.10008.5.1.4.1.1.4.1";
		if (sopClassUID.equals(MRimageStorage)) {
			return false;
		} else if (sopClassUID.equals(EnhancedMRimageStorage)) {
			return true;
		}
		MyLog.waitHere("ATTENZIONE il TAG DICOM " + MyConst.DICOM_SOP_CLASS_UID + " ha un valore inatteso");
		return false;
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

	/// prova del 20250223

	public static String getAllCoils(ImagePlus imp1) {

		String total1 = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_COIL1);
		String total2 = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_COIL2);
		String total3 = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_COIL3);
		String total = "MISSING";
		if (total1.equals("MISSING") == false)
			total = total1;
		if (total2.equals("MISSING") == false)
			total = total2;
		if (total3.equals("MISSING") == false) {
			total = total3;
		}
		return total;
	}

	public static String getFirstCoil(ImagePlus imp1) {

		String total = ReadDicom.getAllCoils(imp1);

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
	 * @param vetCoils vettore coi le coils di cui verificare la presenza
	 * @return
	 */
	public static String getThisCoil(ImagePlus imp1, String[] vetCoils) {

		if (vetCoils == null) {
			// MyLog.waitHere("null1");
			return null;
		}

		String total = ReadDicom.getAllCoils(imp1);

		// #################################################################
		// MODIFICA 190224
		// ---------------------------------
		// IJ.log("total1= " + total);
		total = total.replace("BAL;BAR;BCL;BCR", "BAL+BAR+BCL+BCR");
		total = total.replace("BL;BR", "BL+BR");
		total = total.replace("PL1;PR1", "PL1+PR1");
		total = total.replace("PL2;PR2", "PL2+PR2");
		total = total.replace("PL3;PR3", "PL3+PR3");
		total = total.replace("PL4;PR4", "PL4+PR4");
		// IJ.log("total2= " + total);
		// MyLog.waitHere();
		// #################################################################

		// String total = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_COIL);
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

//	public static String getAllCoils(ImagePlus imp1) {
//
//		String total = ReadDicom.getPrivateCoil(imp1);
//		return total;
//	}

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

	/**
	 * whatManufacturer ricava il nome del costruttore dall'header dell'immagine
	 * 
	 * man = 1 SIEMENS man = 2 PHILIPS man = 3 GE man = 4 PICKER
	 * 
	 * @param imp1
	 * @return man
	 */
	public static int whatManufacturer(ImagePlus imp1) {
		String manufacturer1 = ReadDicom.readDicomParameter(imp1, "0008,0070");
		String manufacturer = "";

		if (manufacturer1.length() > 3) {
			manufacturer = manufacturer1.substring(0, 2);
		}

		int man = 0;
		if (manufacturer.equalsIgnoreCase("SIE")) {
			man = 1;
			// MyLog.waitHere("SIEMENS DETECTED");
		} else if (manufacturer.equalsIgnoreCase("PHI")) {
			man = 2;
			// MyLog.waitHere("PHILIPS DETECTED");
		} else if (manufacturer.equalsIgnoreCase("GE ")) {
			man = 3;
			// MyLog.waitHere("GE DETECTED");
		} else if (manufacturer.equalsIgnoreCase("Pic")) {
			man = 4;
		}
		return man;

	}

	/***
	 * Copia i dati header da una ImagePlus ad un altra
	 * 
	 * @param imp1 immagine a cui aggiungere i dati dicom
	 * @param imp2 immagine da cui copiare dati dicom
	 * @return immagine risultato con dati dicom
	 */
	public static ImagePlus copyDicom(ImagePlus imp1, ImagePlus imp2) {
		String info = (String) imp2.getProperty("Info");
		ImagePlus imp3 = imp1.duplicate();
		if (info != null)
			imp3.setProperty("Info", info);
		return imp3;
	}
	
	
	/***
	 * Questo workaround deriva da ReadAscconv e va utilizzato per leggere parametri
	 * dell'header ignorati da ImageJ, solitamente legati all'incauto passaggio
	 * delle immagini attraverso i PACS. (CAPRE, CAPRE, CAPRE ....)
	 * 
	 * @param path1
	 * @param ricerca
	 * @return
	 */

	public static String piedeDiPorco(String fileName1, String tag) {
		int len1;
		
		
		
		byte[] x1 = hexStringToByteArray(tag);
		String out1 = "";

		try {
			BufferedInputStream f1 = new BufferedInputStream(new FileInputStream(fileName1));
			len1 = f1.available();
			byte[] buffer1 = new byte[len1];
			f1.read(buffer1, 0, len1); // get copy of entire file as byte[]
			f1.close();

			// cerco in buffer1 il tag di fine header, serve per evitare di sconfinare nei
			// byte dei pixel immagine
			byte[] y1 = new byte[4];
			y1[0] = (byte) 0xE0;
			y1[1] = (byte) 0x7F;
			y1[2] = (byte) 0x10;
			y1[3] = (byte) 0x00;
			int offset1 = localizeHexWord(buffer1, y1, buffer1.length);
			int offset2 = localizeHexWord(buffer1, x1, offset1);

			short len2 = Short.parseShort(byte2hex(buffer1[offset2 + 4]), 16);
			offset2 = offset2 + 8;

			byte[] buffer2 = new byte[len2];

			for (int i1 = 0; i1 < len2; i1++) {
				buffer2[i1] = buffer1[offset2 + i1];
			}

			out1 = new String(buffer2);

			/// IJ.log("output >>> " + out1);

		} catch (Exception e) {
			IJ.showMessage("piedeDiPorco>>> ", "Exception " + "\n \n\"" + e.getMessage() + "\"");
		}
		return out1;
	}

	public static int localizeHexWord(byte[] bImage, byte[] what, int limit) {
		int conta = 0;
		int locazione = 0;

		// IJ.log("what =" + byte2hex(what[0]) + byte2hex(what[1])
		// + byte2hex(what[2]) + byte2hex(what[3]));

		for (int i1 = 0; i1 < limit - 4; i1++) {

			if (bImage[i1 + 0] == what[0] && bImage[i1 + 1] == what[1] && bImage[i1 + 2] == what[2]
					&& bImage[i1 + 3] == what[3]) {
				locazione = i1;
				conta++;
				// IJ.log("conta=" + conta + " locazione=" + locazione);
				break;
			}
		}

		if (conta > 0) {
			return locazione;
		} else {
			return -1; // non trovato
		}
	}

	public static String byte2hex(byte by) {
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] buf2 = new char[2];
		buf2[1] = hexDigits[by & 0xf];
		by >>>= 4;
		buf2[0] = hexDigits[by & 0xf];
		return new String(buf2);
	} // end byte2hex

	public static final byte[] short2Byte(short s) {
		byte[] out = new byte[2];

		out[0] = (byte) ((s >>> 8) & 0xFF);
		out[1] = (byte) ((s >>> 0) & 0xFF);

		return out;
	}

	/***
	 * conversione da string hexto byte array s1 deve essere di lunghezza pari
	 * 
	 * @param s1
	 * @return
	 */
	public static byte[] hexStringToByteArray(String s2) {
		String s1= s2.replace(",","");
		int len = s1.length();
		byte[] data = new byte[len / 2];
		for (int i1 = 0; i1 < len; i1 += 2) {
			data[i1 / 2] = (byte) ((Character.digit(s1.charAt(i1), 16) << 4) + Character.digit(s1.charAt(i1 + 1), 16));
		}
		return data;

	}

	public static String getString(BufferedInputStream bo, int start, int len) throws IOException {

		// IJ.log("entro in getString");
		int pos = 1;
		// IJ.log("getString 001");
		byte[] buf = new byte[len];
		// IJ.log("getString 002");
		int size = bo.available();
		// IJ.log("getString 003");
		while (pos < len) {
			int count = bo.read(buf, pos, len);
			pos += count;
		}

		return new String(buf);
	}



}