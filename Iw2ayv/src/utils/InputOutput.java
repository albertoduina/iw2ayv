package utils;

import utils.ReadDicom;
import ij.IJ;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class InputOutput {
	//
	// Absolute VS relative path in Eclipse, my experiences:
	// 1) relative path: is relative to the bin directory in Eclipse, i.e:
	// C:\eclipse2\eclipse\workspace_fmri2\ControlliMensili\bin\test3\
	// in relative path is \test3\
	// 2) you can access to the files in the relative path with
	// getClass().getResource(name) or can transform the relative path in
	// absolute
	//

	/**
	 * Trova un file risorsa, partendo dal nome del file.
	 * 
	 * @param name
	 *            nome del file
	 * @return path del file
	 */
	public static String findResource(String name) {
		URL url1 = new InputOutput().getClass().getClassLoader()
				.getResource(name);
		String path = "";
		if (url1 == null)
			return null;
		else
			path = url1.getPath();
		return path;
	}

	/**
	 * Trova un file risorsa, partendo dal nome del file contenente wildcards.
	 * Per fare questo occorre conoscere il nome di ALMENO una delle risorse. A
	 * questo punto si manipola il path trovato, in modo da poter trovare i nomi
	 * completi delle altre risorse.
	 * 
	 * @param name
	 *            nome del file
	 * @return path del file
	 */
	public static String findUnknownResource(String knownName,
			String wildcardName) {
		URL url1 = new InputOutput().getClass().getClassLoader()
				.getResource(knownName);
		String path = url1.getPath();

		return path;
	}

	public static String absoluteToRelativeBBB(String absolutePath) {
		int start = absolutePath.indexOf("bin");
		String relativePath = absolutePath.substring(start + 3);
		String outPath = UtilAyv.reverseSlash(relativePath);
		return outPath;
	}

	/**
	 * ricerca dei file csv, se il file esiste lo lascia stare, altrimenti lo
	 * estrae dal file jar. NOTA BENE: non ho trovato come farne la prova in
	 * junit
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean findCSV(String fileName) {

		URL url3 = this.getClass().getClassLoader()
				.getResource("contMensili/Sequenze_.class");
		String myString = url3.toString();
		int start = myString.indexOf("plugins");
		int end = myString.lastIndexOf("!");
		String myPart1 = myString.substring(start, end);
		end = myPart1.lastIndexOf("/");
		String myPart2 = myPart1.substring(0, end + 1);
		// definizione del nome del file che andremo a scrivere
		File outFile = new File(myPart2 + fileName);
		// Viene testata l'esistenza del file, se esiste non lo si copia, così
		// vengono mantenute eventuali modifiche dell'utlizzatore
		boolean present = checkFile(outFile.getPath());
		if (present) {
			// NON CANCELLO; in modo che l'utilizzatore possa personalizzare il
			// file. Per me organizzo in modo che sia lo script di
			// distribuzione a cancellare i file
			// outFile.delete();
			// MyLog.waitHere("skip perchè file già esistente");
			return true;
		}
		// ricerco la risorsa da copiare, perchè qui arrivo solo se la risorsa
		// non esiste al di fuori del file jar
		URL url1 = this.getClass().getResource("/" + fileName);
		if (url1 == null) {
			MyLog.waitHere("file " + fileName + " not found in jar");
			return false;
		}
		try {
			// tento la copia
			InputStream is = this.getClass()
					.getResourceAsStream("/" + fileName);
			FileOutputStream fos = new FileOutputStream(outFile);
			while (is.available() > 0) {
				// MyLog.waitHere("SCRIVO "+fileName);
				fos.write(is.read());
			}
			fos.close();
			is.close();
		} catch (IOException e) {
			MyLog.waitHere("ERRORE ACCESSO");
		}
		present = checkFile(outFile.getPath());
		if (present) {
			// MyLog.waitHere("file estratto");
		} else {
			MyLog.waitHere("FALLIMENTO, FILE NON COPIATO");
		}
		return present;
	}

	/**
	 * Copied from
	 * www.coderanch.com/t/278095/Streams/java/Wildcard-delete-File-Object
	 * Author Edwin Dalorzo
	 */
	public static void deleteMultipleFiles(File dir, final String ext) {

		File[] toBeDeleted = dir.listFiles(new FileFilter() {

			public boolean accept(File theFile) {
				if (theFile.isFile()) {
					return theFile.getName().endsWith("." + ext);
				}
				return false;
			}
		});

		for (File deletableFile : toBeDeleted) {
			deletableFile.delete();
		}
	}

	/**
	 * Deletes all files and subdirectories under dir. Returns true if all
	 * deletions were successful. If a deletion fails, the method stops
	 * attempting to delete and returns false.
	 * http://javaalmanac.com/egs/java.io/DeleteDir.html
	 */
	public static boolean deleteDir(File dir) {

		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					IJ.log("errore delete dir");
					return false;
				}
			}
		}
		// The directory is now empty so delete it
		return dir.delete();
	}

	/**
	 * Create a directory; all ancestor directories must exist
	 * http://javaalmanac.com/egs/java.io/DeleteDir.html
	 * 
	 * @param dir
	 * @return booleano true se ok
	 */
	public static boolean createDir(File dir) {

		boolean success = dir.mkdir();
		if (!success) {
			return false;
		} else
			return true;
	}

	/**
	 * separa il nome directory dal path
	 * 
	 * @param path
	 *            path
	 * @return directory
	 */
	public String extractDirectory(String path) {
		int pos = path.lastIndexOf('\\');
		String dir = path.substring(0, pos);
		return dir;
	}

	/**
	 * separa il filename dal path
	 * 
	 * @param path
	 *            path
	 * @return filename
	 */
	public String extractFileName(String path) {

		int pos = path.lastIndexOf('\\');
		String fileName = path.substring(pos + 1);
		return fileName;
	}

	// ###############################################################################
	/**
	 * legge e carica in memoria il file VERSIONE BASE. Legge anche i file
	 * contenuti in un file JAR
	 * 
	 * @param fileName
	 *            path del file
	 * @return
	 */
	public ArrayList<String> readFileGeneric(String fileName, boolean absolute) {

		ArrayList<String> matrixTable = new ArrayList<String>();
		try {
			BufferedReader br = null;
			String path = "";
			if (absolute) {
				path = fileName;
				br = new BufferedReader(new FileReader(path));
			} else {
				path = findResource(fileName);
				InputStream is = new InputOutput().getClass().getClassLoader()
						.getResourceAsStream(fileName);
				br = new BufferedReader(new InputStreamReader(is));
			}
			while (br.ready()) {
				String line = br.readLine();
				matrixTable.add(line);
			}
			br.close();
		} catch (Exception e) {
			// MyLog.waitThere("readFilegeneric error <" + fileName + "> "
			// + e.getMessage());
			return null;
		}
		return matrixTable;
	}

	/***
	 * legge e carica in memoria il file. Legge anche i file contenuti in un
	 * file JAR
	 * 
	 * @param fileName
	 *            nome del file
	 * @return tabella col contenuto del file
	 */
	public ArrayList<ArrayList<String>> readFile3LIKE(String fileName) {
		boolean absolute = false;
		ArrayList<ArrayList<String>> matrixTable = new ArrayList<ArrayList<String>>();
		ArrayList<String> arr1 = readFileGeneric(fileName, absolute);
		String[] line = ArrayUtils.arrayListToArrayString(arr1);
		for (int i1 = 0; i1 < line.length; i1++) {
			String riga = line[i1];
			if (riga.trim().length() == 0)
				continue;
			if (!isComment(riga)) {
				ArrayList<String> row = new ArrayList<String>();
				String result = InputOutput.stripAllComments(riga);
				String[] splitted = result.split("\\s+");
				for (int i2 = 0; i2 < splitted.length; i2++) {
					row.add(splitted[i2]);
				}
				matrixTable.add(row);
			}
		}
		return matrixTable;
	}

	public ArrayList<ArrayList<String>> readFile5LIKE(String fileName,
			boolean absolute) {
		ArrayList<ArrayList<String>> matrixTable = new ArrayList<ArrayList<String>>();
		ArrayList<String> arr1 = readFileGeneric(fileName, absolute);
		String[] line = ArrayUtils.arrayListToArrayString(arr1);
		for (int i1 = 0; i1 < line.length; i1++) {
			String riga = line[i1];
			if (riga.trim().length() == 0)
				continue;
			if (!isComment(riga)) {
				ArrayList<String> row1 = new ArrayList<String>();
				String result = InputOutput.stripAllComments(riga);
				String[] splitted = splitStringGeneric(result, "#");
				for (int i2 = 0; i2 < splitted.length; i2++) {
					row1.add(splitted[i2]);
				}
				ArrayList<String> row2 = new ArrayList<String>();
				for (int i2 = 1; i2 < splitted.length; i2 += 2) {
					row2.add(row1.get(i2));
				}
				matrixTable.add(row2);
			}
		}
		return matrixTable;
	}

	public String[][] readFile6LIKE(String fileName, boolean absolute) {

		ArrayList<ArrayList<String>> matrixTable = new ArrayList<ArrayList<String>>();
		ArrayList<String> row1 = new ArrayList<String>();
		ArrayList<String> arr1 = readFileGeneric(fileName, absolute);
		if (arr1 == null)
			return null;
		String[] line = ArrayUtils.arrayListToArrayString(arr1);
		for (int i1 = 0; i1 < line.length; i1++) {
			String riga = line[i1];
			if (riga.trim().length() == 0)
				continue;
			if (!isComment(riga)) {
				String substr = riga.substring(0, 2);
				if (line.equals("") || substr.equals("//")
						|| substr.equals("/*")) {
					continue;
				}
				ArrayList<String> row = new ArrayList<String>();
				String result = InputOutput.stripAllComments(riga);
				String[] splitted = splitStringGeneric(result, ";");
				for (int i2 = 0; i2 < splitted.length; i2++) {
					row.add(splitted[i2]);
				}
				matrixTable.add(row);
			}
		}
		String[][] table = new String[matrixTable.size()][matrixTable.get(0)
				.size()];
		for (int i1 = 0; i1 < matrixTable.size(); i1++) {
			ArrayList<String> arrayList = matrixTable.get(i1);
			row1 = arrayList;
			for (int j1 = 0; j1 < matrixTable.get(0).size(); j1++) {
				table[i1][j1] = (String) row1.get(j1);
			}
		}
		return (table);
	}

	/**
	 * Rimozione di una colonna da una matrice stringa. Utilizzata per eliminare
	 * commenti e/o informazioni non desiderate dai file di risorsa
	 * 
	 * @param in
	 * @param salta
	 *            numero della colonna (parte da 0)
	 * @return
	 */
	public static String[][] removeColumn(String[][] in, int salta) {
		int len1 = in.length;
		int len2 = in[0].length;

		String[][] out = new String[len1][len2 - 1];
		int count = 0;
		for (int i1 = 0; i1 < len1; i1++) {
			count = 0;
			for (int i2 = 0; i2 < len2; i2++) {
				if (i2 != salta) {
					out[i1][count++] = in[i1][i2];
				}
			}
		}
		return out;
	}

	/***
	 * Legge i dati da un file e li restituisce in un array double
	 * 
	 * @param fileName
	 * @return
	 */

	public static double[] readDoubleArrayFromFile(String fileName) {
		ArrayList<Double> vetList = new ArrayList<Double>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String str = "";

			while ((str = in.readLine()) != null) {
				vetList.add(ReadDicom.readDouble(str));
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ora trasferiamo tutto nel vettore
		double[] vetResult = new double[vetList.size()];
		for (int i1 = 0; i1 < vetList.size(); i1++) {
			vetResult[i1] = vetList.get(i1);
		}
		return vetResult;
	}

	/***
	 * Legge i dati da un file e li restituisce in un array float
	 * 
	 * @param fileName
	 * @return
	 */
	public static float[] readFloatArrayFromFile(String fileName) {
		ArrayList<Float> vetList = new ArrayList<Float>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String str = "";
			while ((str = in.readLine()) != null) {
				vetList.add(ReadDicom.readFloat(str));
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ora trasferiamo tutto nel vettore
		float[] vetResult = new float[vetList.size()];
		for (int i1 = 0; i1 < vetList.size(); i1++) {
			vetResult[i1] = vetList.get(i1);
		}
		return vetResult;
	}

	/***
	 * Legge i dati da un file e li restituisce in un array int
	 * 
	 * @param fileName
	 * @return
	 */
	public static int[] readIntArrayFromFile(String fileName) {
		ArrayList<Integer> vetList = new ArrayList<Integer>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String str = "";
			while ((str = in.readLine()) != null) {
				vetList.add(ReadDicom.readInt(str));
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ora trasferiamo tutto nel vettore
		int[] vetResult = new int[vetList.size()];
		for (int i1 = 0; i1 < vetList.size(); i1++) {
			vetResult[i1] = vetList.get(i1);
		}
		return vetResult;
	}

	/***
	 * Legge i dati da un file e li restituisce in un array string
	 * 
	 * @param fileName
	 * @return
	 */
	public static String[] readStringArrayFromFile(String fileName) {

		File file = new File(fileName);
		if (!file.exists()) {
			IJ.log("readStringArrayFromFile.fileNotExists " + fileName);
		}

		ArrayList<String> vetList = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String str = "";
			while ((str = in.readLine()) != null) {
				vetList.add(str);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ora trasferiamo tutto nel vettore
		String[] vetResult = new String[vetList.size()];
		for (int i1 = 0; i1 < vetList.size(); i1++) {
			vetResult[i1] = vetList.get(i1).trim();
		}
		return vetResult;
	}

	/***
	 * Legge i dati da una stringa e li restituisce in un ArrayList
	 * 
	 * @param strIn
	 * @return
	 */
	public static ArrayList<String> readStringArrayListFromString(String strIn) {
		ArrayList<String> arrList = new ArrayList<String>();
		StringTokenizer parser = new StringTokenizer(strIn, " \t\\,\\;");
		int total = parser.countTokens();
		for (int i1 = 0; i1 < total; i1++) {
			String next = parser.nextToken();
			arrList.add(next);
		}
		return arrList;
	}

	/***
	 * Legge i dati da un file e li restituisce come double matrix
	 * 
	 * @param fileName
	 * @return
	 */
	public static double[][] readDoubleMatrixFromFile(String fileName) {
		ArrayList<ArrayList<String>> vetList = new ArrayList<ArrayList<String>>();
		int rows = 0;
		int columns = 0;

		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String str = "";
			while ((str = in.readLine()) != null) {
				rows++;
				// qui ho la linea, ora devo separare i tokens
				ArrayList<String> arrList1 = readStringArrayListFromString(str);
				columns = arrList1.size();
				vetList.add(arrList1);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ora trasferiamo tutto nel vettore
		double[][] vetResult = new double[rows][columns];

		for (int row = 0; row < rows; row++) {
			ArrayList<String> stringRiga = vetList.get(row);
			for (int col = 0; col < columns; col++) {
				vetResult[row][col] = ReadDicom.readDouble(stringRiga.get(col))	;
			}
		}
		return vetResult;
	}

	/***
	 * Legge i dati da un file e li restituisce come float matrix
	 * 
	 * @param fileName
	 * @return
	 */
	public static float[][] readFloatMatrixFromFile(String fileName) {
		ArrayList<ArrayList<String>> vetList = new ArrayList<ArrayList<String>>();
		int rows = 0;
		int columns = 0;

		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String str = "";
			while ((str = in.readLine()) != null) {
				rows++;
				// qui ho la linea, ora devo separare i tokens
				ArrayList<String> arrList1 = readStringArrayListFromString(str);
				columns = arrList1.size();
				vetList.add(arrList1);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ora trasferiamo tutto nel vettore
		float[][] vetResult = new float[rows][columns];

		for (int row = 0; row < rows; row++) {
			ArrayList<String> stringRiga = vetList.get(row);
			for (int col = 0; col < columns; col++) {
				vetResult[row][col] = ReadDicom.readFloat(stringRiga.get(col));
			}
		}
		return vetResult;
	}

	/***
	 * Legge i dati da un file e li restituisce come int matrix
	 * 
	 * @param fileName
	 * @return
	 */
	public static int[][] readIntMatrixFromFile(String fileName) {
		ArrayList<ArrayList<String>> vetList = new ArrayList<ArrayList<String>>();
		int rows = 0;
		int columns = 0;

		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String str = "";
			while ((str = in.readLine()) != null) {
				rows++;
				// qui ho la linea, ora devo separare i tokens
				ArrayList<String> arrList1 = readStringArrayListFromString(str);
				columns = arrList1.size();
				vetList.add(arrList1);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ora trasferiamo tutto nel vettore
		int[][] vetResult = new int[rows][columns];

		for (int row = 0; row < rows; row++) {
			ArrayList<String> stringRiga = vetList.get(row);
			for (int col = 0; col < columns; col++) {
				vetResult[row][col] = ReadDicom.readInt(stringRiga.get(col));
			}
		}
		return vetResult;
	}

	/***
	 * Legge i dati da un file e li restituisce come string matrix
	 * 
	 * @param fileName
	 * @return
	 */
	public static String[][] readStringMatrixFromFile(String fileName) {
		ArrayList<ArrayList<String>> vetList = new ArrayList<ArrayList<String>>();
		int rows = 0;
		int columns = 0;

		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String str = "";
			while ((str = in.readLine()) != null) {
				rows++;
				// qui ho la linea, ora devo separare i tokens
				ArrayList<String> arrList1 = readStringArrayListFromString(str);
				columns = arrList1.size();
				vetList.add(arrList1);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// IJ.log("righe= " + rows + " colonne= " + columns);
		// ora trasferiamo tutto nel vettore
		String[][] vetResult = new String[rows][columns];
		for (int i1 = 0; i1 < vetList.size(); i1++) {
			ArrayList<String> stringRiga = vetList.get(i1);
			for (int i2 = 0; i2 < stringRiga.size(); i2++) {
				vetResult[i1][i2] = stringRiga.get(i2);
			}
		}
		return vetResult;
	}

	/***
	 * Legge i dati da un file e li restituisce come string matrix
	 * 
	 * @param fileName
	 * @return
	 */
	public static String[][] readStringMatrixFromFileNew(String fileName,
			String separator) {
		ArrayList<String> vetList = new ArrayList<String>();
		try {
			URL url1 = new InputOutput().getClass().getClassLoader()
					.getResource(fileName);
			if (url1 == null) {
				IJ.log("readFile7: file " + fileName + " not visible or null");
				return null;
			}
			InputStream is = new InputOutput().getClass().getClassLoader()
					.getResourceAsStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String str = br.readLine();
			while (str != null) {
				vetList.add(str);
				str = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] list = splitStringGeneric(vetList.get(0), separator);
		String[][] vetResult = new String[vetList.size()][list.length];
		for (int i1 = 0; i1 < vetList.size(); i1++) {
			list = splitStringGeneric(vetList.get(i1), separator);
			for (int i2 = 0; i2 < list.length; i2++) {
				vetResult[i1][i2] = list[i2];
			}
		}
		return vetResult;
	}

	/***
	 * Legge i dati da una stringa e li restituisce in un vettore
	 * 
	 * @param strIn
	 * @return
	 */
	// public static String[] splitString(String strIn) {
	// // return strIn.split(";\\s+|;|\\s+;", -1);
	// return strIn.split(";\\s+|;", -1);
	// }
	//
	// public static String[] splitString2(String strIn) {
	// return strIn.split("#\\s+|#|\\s+#", -1);
	// }

	public static String[] splitStringGeneric(String strIn, String separator) {

		String analyzer = separator + "\\s+|" + separator + "|\\s+" + separator;
		return strIn.split(analyzer, -1);
	}

	/**
	 * Viene utilizzato da FFTJ
	 * 
	 * @param mat1
	 * @param mat2
	 * @param index
	 * @return
	 */
	// // NB: rinominato temporaneamente per vedere se lo ho utilizzato da
	// qualche parte od era un progetto orfano

	public static double[][][] addMatrix(double[][][] mat1, double[][] mat2,
			int index) {
		int slices1 = mat1.length;
		int rows1 = mat1[0].length;
		int columns1 = mat1[0][0].length;
		int rows2 = mat2.length;
		int columns2 = mat2[0].length;

		if (rows1 != rows2 || columns1 != columns2) {
			IJ.log("addMatrix different dimensions matrices");
			return null;
		}
		if (index > slices1) {
			IJ.log("addMatrix index too big");
			return null;
		}
		double[][][] mat3 = cloneMatrix(mat1);
		for (int i1 = 0; i1 < rows2; i1++) {
			for (int i2 = 0; i2 < columns2; i2++) {
				mat3[index][i1][i2] = mat2[i1][i2];
			}
		}
		return mat3;
	}

	/***
	 * Data una matrice, la duplica
	 * 
	 * @param mat1
	 * @return
	 */
	public static double[][][] cloneMatrix(double[][][] mat1) {
		int slices1 = mat1.length;
		int rows1 = mat1[0].length;
		int columns1 = mat1[0][0].length;
		double[][][] mat2 = new double[slices1][rows1][columns1];
		for (int i1 = 0; i1 < slices1; i1++) {
			for (int i2 = 0; i2 < rows1; i2++) {
				for (int i3 = 0; i3 < columns1; i3++) {
					mat2[i1][i2][i3] = mat1[i1][i2][i3];
				}
			}
		}
		return mat2;

	}

	/***
	 * Trasforma un arrayList in una matrice stringa
	 * 
	 * @param matrixTable
	 * @return
	 */
	public String[][] fromArrayListToStringTable(
			ArrayList<ArrayList<String>> matrixTable) {
		ArrayList<String> row1 = new ArrayList<String>();
		if (matrixTable == null) {
			MyLog.here("fromArrayListToStringTable.matrixTable == null");
			return null;
		}
		if (matrixTable.size() == 0) {
			MyLog.here("fromArrayListToStringTable.matrixTable == 0");
			return null;
		}

		// ora trasferiamo tutto nella table
		String[][] table = new String[matrixTable.size()][matrixTable.get(0)
				.size()];
		for (int i1 = 0; i1 < matrixTable.size(); i1++) {
			ArrayList<String> arrayList = matrixTable.get(i1);
			row1 = arrayList;
			for (int j1 = 0; j1 < matrixTable.get(0).size(); j1++) {
				table[i1][j1] = (String) row1.get(j1);
			}
		}
		return (table);
	}

	/***
	 * Trasforma un arrayList in una matrice double
	 * 
	 * @param matrixTable
	 * @return
	 */
	public double[][] fromArrayListToDoubleTable(
			ArrayList<ArrayList<Double>> matrixTable) {
		int rows = 0;
		int columns = 0;

		ArrayList<Double> row1 = new ArrayList<Double>();
		if (matrixTable == null) {
			MyLog.here("fromArrayListToDoubleTable.matrixTable == null");
			return null;
		}
		if (matrixTable.size() == 0) {
			MyLog.here("fromArrayListToDoubleTable.matrixTable == 0");
			return null;
		}
		rows = matrixTable.size();
		columns = 0;

		for (int i1 = 0; i1 < matrixTable.size(); i1++) {
			if (matrixTable.get(i1).size() > columns)
				columns = matrixTable.get(i1).size();
		}
		// IJ.log("rows=" + rows + " columns= " + columns);

		// ora trasferiamo tutto nella table
		double[][] table = new double[rows][columns];
		for (int i1 = 0; i1 < rows; i1++) {
			for (int j1 = 0; j1 < columns; j1++) {
				table[i1][j1] = Double.NaN;
			}
		}

		for (int i1 = 0; i1 < matrixTable.size(); i1++) {
			ArrayList<Double> arrayList = matrixTable.get(i1);
			row1 = arrayList;
			for (int j1 = 0; j1 < matrixTable.get(i1).size(); j1++) {
				table[i1][j1] = (Double) row1.get(j1).doubleValue();
			}
		}
		return (table);
	}

	/***
	 * Trasforma un arrayList in una matrice double
	 * 
	 * @param matrixTable
	 * @return
	 */
	public double[][] fromArrayListToDoubleTableSwapped(
			ArrayList<ArrayList<Double>> matrixTable) {
		int rows = 0;
		int columns = 0;

		ArrayList<Double> row1 = new ArrayList<Double>();
		if (matrixTable == null) {
			MyLog.here("fromArrayListToDoubleTable.matrixTable == null");
			return null;
		}
		if (matrixTable.size() == 0) {
			MyLog.here("fromArrayListToDoubleTable.matrixTable == 0");
			return null;
		}
		rows = matrixTable.size();
		columns = 0;

		for (int i1 = 0; i1 < matrixTable.size(); i1++) {
			if (matrixTable.get(i1).size() > columns)
				columns = matrixTable.get(i1).size();
		}
		// IJ.log("rows=" + rows + " columns= " + columns);

		// ora trasferiamo tutto nella table
		double[][] table = new double[columns][rows];
		for (int i1 = 0; i1 < columns; i1++) {
			for (int j1 = 0; j1 < rows; j1++) {
				table[i1][j1] = Double.NaN;
			}
		}

		for (int i1 = 0; i1 < matrixTable.get(i1).size(); i1++) {
			ArrayList<Double> arrayList = matrixTable.get(i1);
			row1 = arrayList;
			for (int j1 = 0; j1 < matrixTable.size(); j1++) {
				table[i1][j1] = (Double) row1.get(j1).doubleValue();
			}
		}
		return (table);
	}

	/***
	 * Effettua il dump di un ArrayList<ArrayList<String>>
	 * 
	 * @param matrixTable
	 * @param title
	 */

	public static void dumpArrayListTable(
			ArrayList<ArrayList<String>> matrixTable, String title) {
		ArrayList<String> row1 = new ArrayList<String>();
		if (matrixTable == null) {
			IJ.log("fromArrayListToStringTable.matrixTable == null");
			return;
		}
		IJ.log("---- " + title + " ----");
		// ArrayList<String> riga = matrixTable.get(0);
		for (int i1 = 0; i1 < matrixTable.size(); i1++) {
			ArrayList<String> arrayList = matrixTable.get(i1);
			row1 = arrayList;
			for (int j1 = 0; j1 < matrixTable.get(0).size(); j1++) {
				IJ.log((String) row1.get(j1));
			}
		}
		return;
	}

	/***
	 * Verifica la disponibilità di una directory
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkDir(String name) {
		File dirCheck = new File(name);
		if (!dirCheck.exists())
			return false;
		else
			return true;
	}

	/***
	 * Verifica la disponibilità di un file jar
	 * 
	 * @param source
	 * @return
	 */
	public boolean checkJar(String source) {
		URL url1 = this.getClass().getResource("/" + source);
		if (url1 != null)
			return true;
		else
			return false;
	}

	/***
	 * Verifica la disponibilità di un file
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkFile(String name) {
		File fileCheck = new File(name);
		if (!fileCheck.exists())
			return false;
		else
			return true;
	}

	/**
	 * verifica codice
	 * 
	 * @param codice
	 *            codice da verificare
	 * @param tab2
	 *            tabella contenente codici.txt
	 * @return true se il codice esiste
	 */
	public static boolean isCode(String codice, String[][] tab2) {
		if (codice == null)
			IJ.log("codice == null");
		if (tab2 == null)
			IJ.log("tab2 == null");
		boolean trovato = false;
		for (int i1 = 0; i1 < tab2.length; i1++) {
			if (codice.compareTo(tab2[i1][0]) == 0)
				trovato = true;
		}
		return trovato;
	}

	/**
	 * Estrae al volo un file da un archivio .jar by Réal Gagnon
	 * (www.rgagnon.com/javadetails/java-0429.html) for correct work in junit
	 * the source file (ie: test2.jar) must be in the iw2ayv "data" sourceFolder
	 * 
	 * 
	 * 
	 * 
	 * @param source
	 *            il nome del file jar es: "test2.jar"
	 * @param object
	 *            il nome del file da estrarre es: "BT2A_testP6"
	 * @param dest
	 *            la directory di destinazione es: "./Test2/" in caso di
	 *            eccezioni stampa lo stack trace
	 */

	public void extractFromJAR(String source, String object, String dest) {

		long count = 0;

		try {
			URL url1 = this.getClass().getResource("/" + source);
			if (url1 == null) {
				IJ.log("extractFromJAR: file " + source
						+ " not visible or null");
				return;
			}
			String home = url1.getPath();
			// allo scopo di separare il nome della directory da quello del
			// file utilizzo le due righe seguenti
			File file = new File(home);
			String home2 = file.getParent();

			JarFile jar = new JarFile(home);

			ZipEntry entry = jar.getEntry(object);

			if (!checkDir(home2 + dest)) {
				boolean ok = createDir(new File(home2 + dest));
				if (!ok) {
					MyLog.caller("failed directory creation=" + home2 + dest);
					return;
				}
			}

			File efile = new File(home2 + dest, entry.getName());

			InputStream in = new BufferedInputStream(jar.getInputStream(entry));
			OutputStream out = new BufferedOutputStream(new FileOutputStream(
					efile));
			byte[] buffer = new byte[2048];
			for (;;) {
				int nBytes = in.read(buffer);
				if (nBytes <= 0)
					break;
				out.write(buffer, 0, nBytes);
				count = count + nBytes;
			}
			out.flush();
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Estrae al volo un file da un archivio .jar by Réal Gagnon
	 * (www.rgagnon.com/javadetails/java-0429.html) for correct work in junit
	 * the source file (ie: test2.jar) must be in the iw2ayv "data" sourceFolder
	 * 
	 * 
	 * 
	 * 
	 * @param source
	 *            il nome del file jar es: "test2.jar"
	 * @param object
	 *            il nome del file da estrarre es: "BT2A_testP6"
	 * @param dest
	 *            la directory di destinazione es: "./Test2/" in caso di
	 *            eccezioni stampa lo stack trace
	 */

	public long extractFromJAR2(String source, String object, String dest) {

		long count = 0;

		try {
			URL url1 = this.getClass().getResource("/" + source);
			if (url1 == null) {
				IJ.log("extractFromJAR: file " + source
						+ " not visible or null");
				return -1L;
			}
			String home = url1.getPath();
			// allo scopo di separare il nome della directory da quello del
			// file utilizzo le due righe seguenti
			File file = new File(home);
			String home2 = file.getParent();

			JarFile jar = new JarFile(home);

			ZipEntry entry = jar.getEntry(object);

			if (!checkDir(home2 + dest)) {
				boolean ok = createDir(new File(home2 + dest));
				if (!ok) {
					MyLog.caller("failed directory creation=" + home2 + dest);
					return -2L;
				}
			}

			File efile = new File(home2 + dest, entry.getName());

			InputStream in = new BufferedInputStream(jar.getInputStream(entry));
			OutputStream out = new BufferedOutputStream(new FileOutputStream(
					efile));
			byte[] buffer = new byte[2048];
			for (;;) {
				int nBytes = in.read(buffer);
				if (nBytes <= 0)
					break;
				out.write(buffer, 0, nBytes);
				count = count + nBytes;
			}
			out.flush();
			out.close();
			in.close();
			return count;
		} catch (Exception e) {

			e.printStackTrace();
			return -3L;
		}
	}

	/**
	 * Estrae le immagini di test da un file jar
	 * 
	 * @param source
	 * @param list
	 * @param destination
	 * @return
	 */
	public String findListTestImages(String source, String[] list,
			String destination) {
		if (list == null) {
			IJ.log("findListTestImages.list==null");
			return null;
		}
		for (int i1 = 0; i1 < list.length; i1++) {
			extractFromJAR(source, list[i1], destination);
		}
		URL url1 = this.getClass().getResource(destination);
		if (url1 == null) {
			IJ.log("findListTestImages.url1==null");
			return null;
		}
		String home1 = url1.getPath();
		return (home1);
	}

	/**
	 * estrae le immagini di test da un file jar
	 * 
	 * @param source
	 * @param list
	 * @param destination
	 * @return
	 */
	public String[] findListTestImages2(String source, String[] list,
			String destination) {
		if (list == null) {
			MyLog.here("list==null");
			return null;
		}
		for (int i1 = 0; i1 < list.length; i1++) {
			extractFromJAR(source, list[i1], destination);
		}
		URL url1 = this.getClass().getResource(destination);
		if (url1 == null) {
			MyLog.caller("url1==null");
			return null;
		}

		String home1 = url1.getPath();
		// nb: home è un path assoluto (preceduto da un "/")
		String[] path = new String[list.length];
		for (int i1 = 0; i1 < list.length; i1++) {
			path[i1] = home1 + "/" + list[i1];
		}
		return (path);
	}

	/**
	 * verifica se la riga è un commento (inizia con //)
	 * 
	 * @param riga
	 * @return
	 */
	public static boolean isComment(String riga) {
		if (riga.length() < 2) {
			return false;
		}
		String dueCaratteri = riga.substring(0, 2);
		if (dueCaratteri.equals("//")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * toglie i commenti da una stringa (quelli \*....*\)
	 * 
	 * @param riga
	 * @return
	 */
	public static String stripComment(String riga) {
		int beginComment = 0;
		int endComment = 0;
		int fromIndex = 0;
		String noComment = "";

		beginComment = riga.indexOf("/*", fromIndex);
		if (beginComment > 0) {
			fromIndex = beginComment;
			endComment = riga.indexOf("*/", fromIndex);
		} else {
			return riga;
		}
		if (endComment < fromIndex)
			return null;
		if (endComment < 0)
			endComment = riga.length();

		noComment = riga.substring(0, beginComment)
				+ riga.substring(endComment + 2, riga.length());
		return noComment;
	}

	/**
	 * toglie i commenti da una stringa (quelli// messo non all'inizio)
	 * 
	 * @param riga
	 * @return
	 */
	public static String stripSlashComment(String riga) {
		int beginComment = 0;
		beginComment = riga.indexOf("//");
		String noComment = "";
		if (beginComment > 0) {
			noComment = riga.substring(0, beginComment);
		} else
			noComment = riga;
		return noComment;
	}

	/**
	 * toglie i commenti da una stringa (tutti)
	 * 
	 * @param riga
	 * @return
	 */
	public static String stripAllComments(String riga) {
		String pass1 = stripSlashComment(riga);
		String pass2 = "";
		int count = 0;
		while (!pass2.equals(pass1)) {
			if (count > 0)
				pass1 = pass2;
			count++;
			pass2 = stripComment(pass1);
		}
		return pass2;
	}

	/**
	 * from:
	 * "http://www.roseindia.net/java/example/java/util/ZipRetrieveElements.shtml"
	 * 
	 * @param sourcefile
	 * @param destination
	 */
	public static void ZipRetrieveElements(String sourcefile, String destination) {
		OutputStream out = null;

		try {
			if (!sourcefile.endsWith(".zip")) {
				// System.out.println("Invalid file name!");
				// System.exit(0);
			} else if (!new File(sourcefile).exists()) {
				System.out.println("File not exist!");
				System.exit(0);
			}
			ZipInputStream zis1 = new ZipInputStream(new FileInputStream(
					sourcefile));
			ZipFile zf1 = new ZipFile(sourcefile);
			int count = 0;
			for (Enumeration<? extends ZipEntry> em = zf1.entries(); em
					.hasMoreElements();) {
				String targetfile = em.nextElement().toString();
				out = new FileOutputStream(destination + "/" + targetfile);
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = zis1.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				count += 1;
			}
			if (count > 0)
				// System.out.println("" + count + " files unzipped.");
				out.close();
			zis1.close();
		} catch (IOException e) {
			System.out.println("Error: Operation failed!");
			System.exit(0);
		}
	}

	public static void debugListFiles222(String dir) {
		File fdir = new File(dir);
		String[] list = fdir.list();
		for (int i1 = 0; i1 < list.length; i1++) {
			IJ.log("" + i1 + " " + list[i1]);
		}
	}

	public static void unZip(String zipFile, String destDir) {
		final int BUFFER = 2048;

		try {
			BufferedOutputStream dest = null;
			FileInputStream fis = new FileInputStream(zipFile);
			ZipInputStream zis = new ZipInputStream(
					new BufferedInputStream(fis));
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				// System.out.println("Extracting: " +entry);
				int count;
				byte data[] = new byte[BUFFER];
				// write the files to the disk
				FileOutputStream fos = new FileOutputStream(destDir + "\\"
						+ entry.getName());
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = zis.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
			}
			zis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}