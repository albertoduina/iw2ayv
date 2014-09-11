package utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import ij.IJ;
import ij.ImagePlus;

/***
 * Questa routine deriva da un vecchio lavoro, sviluppato in fortran,
 * indipendentemente dalle informazioni presenti su internet. Unico tool
 * utilizzato ai temi era l'HexWorkshop. Ora i miei "findings" sono stati
 * trovati anche da altri. Su internet ho trovato, per matlab una routine
 * facente parte del Dicom Toolbox, di Dirk-Jan Kroon
 * "www.mathworks.com/matlabcentral/fileexchange/27941-dicom-toolbox/content/SiemensInfo.m"
 * in essa viene fatta una ricerca all'interno del Siemens Private Tag
 * 0029,1020. Questo Tag, purtroppo per non è conosciuto da ImageJ per cui non è
 * possibile estrarlo automaticamente nè dall'header nè dalle Info. Pertanto la
 * strada è quella di leggere dal file stream
 * 
 */

public class ReadAscconv {

	public int location;
	private BufferedInputStream bis;

	public String read(String path1) {
		String blob = "";
		int index1 = 0;
		int index2 = 0;
		int fromIndex = 0;

		try {
			FileInputStream fis = new FileInputStream(path1);
			BufferedInputStream bis = new BufferedInputStream(fis);
			int size = bis.available();
			String header = getString(bis, size - 20);
			fromIndex = 1;
			index1 = header.indexOf("### ASCCONV BEGIN ", fromIndex);
//			if (index1 > 0) {
//				MyLog.waitHere("trovato begin in " + index1);
//			}

			index2 = header.indexOf("### ASCCONV END ###", index1 + 1);

//			if (index1 > 0) {
//				MyLog.waitHere("trovato end in " + index2);
//			}

//			IJ.log("indexBegin= " + index1 + "  indexEnd= " + index2);

			while ((index1 > 0) && (index2 > 0)) {
				blob = header.substring(index1 + 21, index2);
				fromIndex = index2;
				index1 = header.indexOf("### ASCCONV BEGIN ", fromIndex);
				index2 = header.indexOf("### ASCCONV END ###", fromIndex + 1);
			}
//			MyLog.waitHere("blob= " + blob);

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
		// IJ.log(blob);
		return blob;
	}

	public String[] parseParameters(String blob) {
		String[] primoPasso = blob.split("\n");
		return primoPasso;
	}

	public String[][] separateParameters(String[] primoPasso) {
		String[][] secondoPasso = new String[2][primoPasso.length];
		for (int i1 = 0; i1 < primoPasso.length; i1++) {
			String aux1 = primoPasso[i1];
			if (aux1.trim().length() == 0) {
				secondoPasso[0][i1] = "";
				secondoPasso[1][i1] = "";
				continue;
			}
			String[] aux2 = aux1.split("=");
			secondoPasso[0][i1] = aux2[0];
			secondoPasso[1][i1] = aux2[1];
		}
		return secondoPasso;
	}

	public String[][] searchPartialName(String[][] allParameters, String partial) {
		int count = 0;
		for (int i1 = 0; i1 < allParameters[0].length; i1++) {
			if (allParameters[0][i1].contains(partial))
				count++;
		}
		String[][] out = new String[2][count];
		count = 0;
		for (int i1 = 0; i1 < allParameters[0].length; i1++) {
			if (allParameters[0][i1].contains(partial)) {
				out[0][count] = allParameters[0][i1];
				out[1][count] = allParameters[1][i1];
				count++;
			}
		}
		return out;
	}

	public String[] searchPartialName(String[] allParameters, String partial) {
		int count = 0;
		for (int i1 = 0; i1 < allParameters.length; i1++) {
			if (allParameters[i1].contains(partial))
				count++;
		}
		String[] out = new String[count];
		count = 0;
		for (int i1 = 0; i1 < allParameters.length; i1++) {
			if (allParameters[i1].contains(partial)) {
				out[count] = allParameters[i1];
				count++;
			}
		}
		return out;
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
		String attribute = "????";
		if (header != null) {
			int idx1 = header.indexOf(userInput);
			int idx2 = header.indexOf("=", idx1);
			int idx3 = header.indexOf("\n", idx2);
			if (idx1 >= 0 && idx3 >= 0) {
				try {
					attribute = header.substring(idx2 + 1, idx3);
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

	public static String readAscParameterContains(String header,
			String[] userInput) {
		String attribute = "????";
		if (header != null) {
			boolean found = false;
			String delims = "\n";
			String[] tokens = header.split(delims);
			String token = "  ";
			boolean ok2 = false;

			for (int i1 = 0; i1 < tokens.length; i1++) {
				// MyLog.waitHere("tokens " + i1 + " = " + tokens[i1]);
				token = tokens[i1].trim();
				boolean ok = true;
				// boolean debug = false;
				// if (token
				// .equals("sCoilSelectMeas.aRxCoilSelectData[0].asList[0].sCoilElementID.tCoilID	 = 	\"\"HeadNeck_20\"\""))
				// {
				// debug = true;
				// }
				//
				// if (debug)
				// IJ.log("token= " + token);
				for (int i2 = 0; i2 < userInput.length; i2++) {
					String test = userInput[i2];
					// if (debug)
					// IJ.log("testo contro " + test + " risulta= "
					// + token.indexOf(test));
					if ((token.indexOf(test)) < 0)
						ok = false;
				}
				if (ok) {
					int idx2 = token.indexOf("=");
					// IJ.log("token= " + token + " idx2= " + idx2);
					try {
						attribute = token.substring(idx2 + 1);
						attribute = attribute.trim();
						ok2 = true;
						// IJ.log("trovato= " + attribute);
						return (attribute);
					} catch (Throwable e) { // Anything else
						MyLog.here("value PROBLEM");
						ok2 = false;
						return (attribute);
					}

				}

			}
		} else
			attribute = "MISSING";
		return (attribute);
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