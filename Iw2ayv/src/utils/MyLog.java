package utils;

import java.awt.Color;
import java.awt.Frame;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.NonBlockingGenericDialog;
import ij.gui.WaitForUserDialog;
import ij.measure.ResultsTable;

public class MyLog {

	/**
	 * 
	 * @param part1
	 * @param part2
	 * @return
	 */
	public String nameTable(String part1, String part2) {
		String target_file; // fileThatYouWantToFilter
		List<String> list1 = new ArrayList<String>();
		URL url3 = this.getClass().getClassLoader().getResource("contMensili/Sequenze_.class");
		String myString = url3.toString();
		int start = myString.indexOf("plugins");
		int end = myString.lastIndexOf("!");
		String myPart1 = myString.substring(start, end);
		end = myPart1.lastIndexOf("/");
		String myPart2 = myPart1.substring(0, end + 1);
		File folderToScan = new File(myPart2);
		File[] listOfFiles = folderToScan.listFiles();
		for (int i1 = 0; i1 < listOfFiles.length; i1++) {
			if (listOfFiles[i1].isFile()) {
				target_file = listOfFiles[i1].getName();
				if (target_file.startsWith(part1) && target_file.endsWith(part2)) {
					list1.add(target_file);
				}
			}
		}
		String[] list2 = ArrayUtils.arrayListToArrayString(list1);
//		String[] list2 = ArrayUtils.arrayListToArrayString((ArrayList<String>) list1);
		String nome1 = list2[0];
		return nome1;
	}

	/**
	 * 
	 * @param in1
	 * @param title
	 */
	public static void resultsLog(int[] in1, String title) {
		if (in1 == null) {
			MyLog.waitThere("Warning input vector " + title + " null");
		} else {
			ResultsTable rt1 = new ResultsTable();
			for (int i1 = 0; i1 < in1.length; i1++) {
				rt1.incrementCounter();
				rt1.addValue(title, in1[i1]);
			}
			rt1.show(title);
			return;
		}
	}

	/**
	 * 
	 * @param in1
	 * @param title
	 */
	public static void resultsLog(short[] in1, String title) {
		if (in1 == null) {
			MyLog.waitThere("Warning input vector " + title + " null");
		} else {
			ResultsTable rt1 = new ResultsTable();
			for (int i1 = 0; i1 < in1.length; i1++) {
				rt1.incrementCounter();
				rt1.addValue(title, in1[i1]);
			}
			rt1.show(title);
			return;
		}
	}

	/**
	 * 
	 * @param in1
	 * @param title
	 */
	public static void resultsLog(double[] in1, String title) {
		if (in1 == null) {
			MyLog.waitThere("Warning input vector " + title + " null");
		} else {
			ResultsTable rt1 = new ResultsTable();
			for (int i1 = 0; i1 < in1.length; i1++) {
				rt1.incrementCounter();
				rt1.addValue(title, in1[i1]);
			}
			rt1.show(title);
			return;
		}
	}

	/**
	 * 
	 * @param in1
	 * @param title
	 */
	public static void resultsLog(float[] in1, String title) {
		if (in1 == null) {
			MyLog.waitThere("Warning input vector " + title + " null");
		} else {
			ResultsTable rt1 = new ResultsTable();
			for (int i1 = 0; i1 < in1.length; i1++) {
				rt1.incrementCounter();
				rt1.addValue(title, in1[i1]);
			}
			rt1.show(title);
			return;
		}
	}

	/**
	 * 
	 * @param in1
	 * @param title
	 */
	public static void resultsLog(byte[] in1, String title) {
		if (in1 == null) {
			MyLog.waitThere("Warning input vector " + title + " null");
		} else {
			ResultsTable rt1 = new ResultsTable();
			for (int i1 = 0; i1 < in1.length; i1++) {
				rt1.incrementCounter();
				rt1.addValue(title, (byte) in1[i1] & 0xFF);
			}
			rt1.show(title);
			return;
		}
	}

	/**
	 * 
	 * @param in1
	 * @param in2
	 * @param title1
	 * @param title2
	 * @param name
	 */
	public static void resultsLog2(int[] in1, double[] in2, String title1, String title2, String name) {
		ResultsTable rt1 = new ResultsTable();
		for (int i1 = 0; i1 < in1.length; i1++) {
			rt1.incrementCounter();
			rt1.addValue(title1, in1[i1]);
			rt1.addValue(title2, in2[i1]);
		}
		rt1.show(name);
		return;

	}

	/**
	 * 
	 * @param in1
	 * @param titles
	 * @param name
	 */
	public static void resultsLogMatrix(String[][] in1, String[] titles, String name) {
		ResultsTable rt1 = new ResultsTable();
		// MyLog.waitHere("in1.length= " + in1.length + " in1[0].length= " +
		// in1[0].length);

		if (in1 == null) {
			MyLog.waitThere("Warning input vector " + name + " null");
		} else {
			for (int i1 = 0; i1 < in1.length; i1++) {
				rt1.incrementCounter();
				for (int i2 = 0; i2 < in1[0].length; i2++) {
					String aux = "";
					if (i2 < titles.length)
						aux = titles[i2];
					else
						aux = "" + i2;

					rt1.addValue(aux, (String) in1[i1][i2]);
				}
			}
		}
		rt1.show(name);
		return;
	}

	/**
	 * 
	 * @param in1
	 * @param titles
	 * @param name
	 */
	public static void resultsLogMatrix(double[][] in1, String[] titles, String name) {
		ResultsTable rt1 = new ResultsTable();
		// MyLog.waitHere("in1.length= " + in1.length + " in1[0].length= " +
		// in1[0].length);

		if (in1 == null) {
			MyLog.waitThere("Warning input vector " + name + " null");
		} else {
			for (int i1 = 0; i1 < in1.length; i1++) {
				rt1.incrementCounter();
				for (int i2 = 0; i2 < in1[0].length; i2++) {
					String aux = "";
					if (i2 < titles.length)
						aux = titles[i2];
					else
						aux = "" + i2;

					rt1.addValue(aux, (double) in1[i1][i2]);
				}
			}
		}
		rt1.show(name);
		return;
	}

	/**
	 * 
	 * @param tmp
	 * @param title
	 */
	public static void logArrayListInteger(List<int[]> tmp, String title) {
		if (tmp == null) {
			IJ.log("Warning vector " + title + " = null");
		} else {
			IJ.log("----------- " + title + "  [ " + tmp.size() + " ] -----------");
			String logRiga = "";
			for (int j1 = 0; j1 < tmp.size(); j1++) {
				logRiga += tmp.get(j1) + ",  ";
			}
			IJ.log(logRiga);
		}
	}

	/**
	 * 
	 * @param arrList
	 * @param title
	 */
	public static void logArrayList(ArrayList<Double> arrList, String title) {
		if (arrList == null) {
			IJ.log("Warning vector " + title + " = null");
		} else {
			IJ.log("----------- " + title + "  [ " + arrList.size() + " ] -----------");
			String logRiga = "";
			for (int j1 = 0; j1 < arrList.size(); j1++) {
				logRiga += arrList.get(j1) + ",  ";
			}
			IJ.log(logRiga);
		}
	}

	/**
	 * 
	 * @param arrList
	 * @param title
	 */
	public static void logArrayListInteger(ArrayList<Integer> arrList, String title) {
		if (arrList == null) {
			IJ.log("Warning vector " + title + " = null");
		} else {
			IJ.log("----------- " + title + "  [ " + arrList.size() + " ] -----------");
			String logRiga = "";
			for (int j1 = 0; j1 < arrList.size(); j1++) {
				logRiga += arrList.get(j1) + ",  ";
			}
			IJ.log(logRiga);
		}
	}

	/**
	 * 
	 * @param arrList
	 */
	public static void logArrayList(ArrayList<String> arrList) {
		if (arrList == null) {
			IJ.log("Warning vector = null");
		} else {
			IJ.log("----------- [ " + arrList.size() + " ] -----------");
			String logRiga = "";
			for (int j1 = 0; j1 < arrList.size(); j1++) {
				logRiga += arrList.get(j1) + ",  ";
			}
			IJ.log(logRiga);
		}
	}

	/**
	 * 
	 * @param arrList
	 */
	public static void logArrayListVertical(ArrayList<String> arrList) {
		if (arrList == null) {
			IJ.log("Warning vector = null");
		} else {
			IJ.log("----------- [ " + arrList.size() + " ] -----------");
			for (int j1 = 0; j1 < arrList.size(); j1++) {
				IJ.log(arrList.get(j1));
			}
		}
	}

	/**
	 * 
	 * @param matrixTable
	 * @param title
	 */
	public static void logArrayListTable(ArrayList<ArrayList<Double>> matrixTable, String title) {
		// ArrayList<Double> row1 = new ArrayList<Double>();
		if (matrixTable == null) {
			IJ.log("fromArrayListToStringTable.matrixTable == null");
			return;
		}
		IJ.log("####### " + title + "[" + matrixTable.get(0).size() + " x " + matrixTable.size() + "] ########");
		// ArrayList<String> riga = matrixTable.get(0);
		for (int i1 = 0; i1 < matrixTable.size(); i1++) {
			ArrayList<Double> arrayList = matrixTable.get(i1);
			String logRiga = "";
			for (int j1 = 0; j1 < matrixTable.get(i1).size(); j1++) {
				logRiga += arrayList.get(j1) + ",  ";
			}
			IJ.log(logRiga);
		}
		return;
	}

	/**
	 * 
	 * @param matrixTable
	 * @param title
	 */
	public static void logArrayListTable2(ArrayList<ArrayList<String>> matrixTable, String title) {
		// ArrayList<Double> row1 = new ArrayList<Double>();
		if (matrixTable == null) {
			IJ.log("logArrayListTable == null");
			return;
		}
		IJ.log("####### " + title + "[" + matrixTable.get(0).size() + " x " + matrixTable.size() + "] ########");
		// ArrayList<String> riga = matrixTable.get(0);
		for (int i1 = 0; i1 < matrixTable.size(); i1++) {
			ArrayList<String> arrayList = matrixTable.get(i1);
			String logRiga = "";
			for (int j1 = 0; j1 < matrixTable.get(0).size(); j1++) {
				logRiga += arrayList.get(j1) + ",  ";
			}
			IJ.log(logRiga);
		}
		return;
	}

	public static void logMatrix(double mat[][], String nome) {
		String stri = "";
		int rows = 0;
		int columns = 0;
		if (mat == null) {
			MyLog.waitThere("Warning matrix " + nome + " = null");
			return;
		} else {
			rows = mat.length;
			if (rows == 0) {
				MyLog.waitThere("Warning matrix " + nome + " length=0");
				return;
			}
			columns = mat[0].length;
			// MyLog.waitThere("rows=" + rows + " columns= " + columns);

			IJ.log("---- " + nome + " [ " + rows + "x" + columns + " ] ----");
			for (int i1 = 0; i1 < rows; i1++) {
				stri = "";
				for (int i2 = 0; i2 < columns; i2++) {
					stri += mat[i1][i2] + ",  ";
				}
				IJ.log("" + i1 + ",  " + stri);
			}
		}
		IJ.log("---------------------------------------------");
	}

	public static void logMatrixVertical(double mat[][], String nome) {
		String stri = "";
		int rows = 0;
		int columns = 0;
		if (mat == null) {
			MyLog.waitThere("Warning matrix " + nome + " = null");
			return;
		} else {
			rows = mat.length;
			if (rows == 0) {
				MyLog.waitThere("Warning matrix " + nome + " length=0");
				return;
			}

			columns = mat[0].length;
			// IJ.log("rows=" + rows + " columns= " + columns);

			IJ.log("---- " + nome + " [ " + rows + "x" + columns + " ] ----");
			for (int i1 = 0; i1 < columns; i1++) {
				stri = "";
				for (int i2 = 0; i2 < rows; i2++) {
					stri += mat[i2][i1] + ",  ";
				}
				IJ.log(stri);
			}
		}
		IJ.log("---------------------------------------------");
	}

	public static void logMatrix(float mat[][], String nome) {
		String stri = "";
		int rows = 0;
		int columns = 0;
		if (mat == null) {
			MyLog.waitThere("Warning matrix " + nome + " = null");
			return;
		} else {
			rows = mat.length;
			if (rows == 0) {
				MyLog.waitThere("Warning matrix " + nome + " length=0");
				return;
			}

			columns = mat[0].length;
			// IJ.log("rows=" + rows + " columns= " + columns);

			IJ.log("---- " + nome + " [ " + rows + "x" + columns + " ] ----");
			for (int i1 = 0; i1 < rows; i1++) {
				stri = "";
				for (int i2 = 0; i2 < columns; i2++) {
					stri += mat[i1][i2] + ",  ";
				}
				IJ.log(stri);
			}
		}
		IJ.log("---------------------------------------------");
	}

	public static void logMatrix(int mat[][], String nome) {
		String stri = "";
		int rows = 0;
		int columns = 0;
		if (mat == null) {
			MyLog.waitThere("Warning matrix " + nome + " = null");
			return;
		} else {
			rows = mat.length;
			if (rows == 0) {
				MyLog.waitThere("Warning matrix " + nome + " length=0");
				return;
			}

			columns = mat[0].length;
			// IJ.log("rows=" + rows + " columns= " + columns);

			IJ.log("---- " + nome + " [ " + rows + "x" + columns + " ] ----");
			for (int i1 = 0; i1 < rows; i1++) {
				stri = "";
				for (int i2 = 0; i2 < columns; i2++) {
					stri += mat[i1][i2] + ",  ";
				}
				IJ.log(stri);
			}
		}
		IJ.log("---------------------------------------------");
	}

	public static void logMatrix(String mat[][], String nome) {
		String stri = "";
		int rows = 0;
		int columns = 0;
		if (mat == null) {
			// MyLog.waitThere("Warning matrix " + nome + " = null");
			return;
		} else {
			rows = mat.length;
			if (rows == 0) {
				MyLog.waitThere("Warning matrix " + nome + " length=0");
				return;
			}

			columns = mat[0].length;
			// IJ.log("rows=" + rows + " columns= " + columns);

			IJ.log("---- " + nome + " [ " + rows + "x" + columns + " ] ----");
			for (int i1 = 0; i1 < rows; i1++) {
				stri = "";
				for (int i2 = 0; i2 < columns; i2++) {
					stri += mat[i1][i2] + ";  ";
				}
				IJ.log(stri);
			}
		}
		IJ.log("---------------------------------------------");
	}

	public static void logMatrixTable(String mat[][], String nome) {
		String stri = "";
		int rows = 0;
		int columns = 0;
		if (mat == null) {
			// MyLog.waitThere("Warning matrix " + nome + " = null");
			return;
		} else {
			rows = mat.length;
			if (rows == 0) {
				MyLog.waitThere("Warning matrix " + nome + " length=0");
				return;
			}

			columns = mat[0].length;
			// IJ.log("rows=" + rows + " columns= " + columns);

			IJ.log("---- " + nome + " [ " + rows + "x" + columns + " ] ----");

			IJ.log("ROW; PATH; CODE; COIL; IMA_PASS; IMA_ORDER; IMA_INCREMENT; MULTIPLI; SPARE2; SPARE3; SERIE; ACQ; IMA; ACQ_TIME; ECHO_TIME; POSITION; DIREZ; PROF; DONE");


			for (int i1 = 0; i1 < rows; i1++) {
				stri = "";
				for (int i2 = 0; i2 < columns; i2++) {
					stri += mat[i1][i2] + ";  ";
				}
				IJ.log(stri);
			}
		}
		IJ.log("---------------------------------------------");
	}

	public static void logVector(double vect[], String nome) {
		String stri = "";
		if (vect == null) {
			IJ.log("Warning vector " + nome + " = null");
		} else {
			IJ.log("----------- " + nome + "  [ " + vect.length + " ] -----------");

			for (int i1 = 0; i1 < vect.length; i1++) {
				stri = stri + vect[i1] + ";  ";
			}
			IJ.log(stri);
		}
		IJ.log("---------------------------------------------");
	}

	public static void logVectorVertical(double vect[], String nome) {
		String stri = "";
		if (vect == null) {
			IJ.log("Warning vector " + nome + " = null");
		} else {
			IJ.log("----------- " + nome + "  [ " + vect.length + " ] -----------");

			for (int i1 = 0; i1 < vect.length; i1++) {
				stri = stri + vect[i1] + "\n";
			}
			IJ.log(stri);
		}
		IJ.log("---------------------------------------------");
	}

	public static void logVectorVertical(short vect[], String nome) {
		String stri = "";
		if (vect == null) {
			IJ.log("Warning vector " + nome + " = null");
		} else {
			IJ.log("----------- " + nome + "  [ " + vect.length + " ] -----------");

			for (int i1 = 0; i1 < vect.length; i1++) {
				stri = stri + vect[i1] + "\n";
			}
			IJ.log(stri);
		}
		IJ.log("---------------------------------------------");
	}

	public static void logVectorVertical(int vect[], String nome) {
		String stri = "";
		if (vect == null) {
			IJ.log("Warning vector " + nome + " = null");
		} else {
			IJ.log("----------- " + nome + "  [ " + vect.length + " ] -----------");

			for (int i1 = 0; i1 < vect.length; i1++) {
				stri = stri + vect[i1] + "\n";
			}
			IJ.log(stri);
		}
		IJ.log("---------------------------------------------");
	}

	public static void logVector(float vect[], String nome) {
		String stri = "";
		int init;
		int end;
		int remain;

		if (vect == null) {
			IJ.log("Warning vector " + nome + " = null");
		} else {

			IJ.log("----------- " + nome + "  [ " + vect.length + " ] -----------");
			if (vect.length <= 255) {
				for (int i1 = 0; i1 < vect.length; i1++) {
					stri = stri + vect[i1] + ",  ";
				}
				IJ.log(stri);

			} else {
				init = 0;
				end = 255;
				remain = vect.length;
				while (remain > 255) {
					for (int i1 = init; i1 < end; i1++) {
						stri = stri + vect[i1] + ",  ";
					}
					IJ.log(stri);
					init = end + 1;
					end = end + 255;
					remain = vect.length - end;
				}
			}
			IJ.log("---------------------------------------------");
		}
	}

	public static void logVector(short vect[], String nome) {
		String stri = "";
		if (vect == null) {
			IJ.log("Warning vector " + nome + " = null");
		} else {

			IJ.log("----------- " + nome + "  [ " + vect.length + " ] -----------");
			for (int i1 = 0; i1 < vect.length; i1++) {
				stri = stri + vect[i1] + ",  ";
			}
			IJ.log(stri);
			IJ.log("---------------------------------------------");
		}
	}

	public static void logVector(int vect[], String nome) {
		String stri = "";
		if (vect == null) {
			IJ.log("Warning vector " + nome + " = null");
		} else {

			IJ.log("----------- " + nome + "  [ " + vect.length + " ] -----------");
			for (int i1 = 0; i1 < vect.length; i1++) {
				stri = stri + vect[i1] + ",  ";
			}
			IJ.log(stri);
			IJ.log("---------------------------------------------");
		}
	}

	public static void logVector(byte vect[], String nome) {
		String stri = "";
		if (vect == null) {
			IJ.log("Warning vector " + nome + " = null");
		} else {

			IJ.log("----------- " + nome + "  [ " + vect.length + " ] -----------");
			for (int i1 = 0; i1 < vect.length; i1++) {
				stri = stri + vect[i1] + ",  ";
			}
			IJ.log(stri);
			IJ.log("---------------------------------------------");
		}
	}

	public static void logVector(String vect[], String nome) {
		String stri = "";
		if (vect == null) {
			IJ.log("Warning vector " + nome + " = null");
		} else {
			IJ.log("----------- " + nome + "  [ " + vect.length + " ] -----------");

			for (int i1 = 0; i1 < vect.length; i1++) {
				stri = stri + vect[i1] + ",  ";
			}
			IJ.log(stri);
		}
		IJ.log("---------------------------------------------");
	}

	public static void logVectorVertical(String vect[], String nome) {
		String stri = "";
		if (vect == null) {
			IJ.log("Warning vector " + nome + " = null");
		} else {
			IJ.log("----------- " + nome + "  [ " + vect.length + " ] -----------");

			for (int i1 = 0; i1 < vect.length; i1++) {
				stri = stri + i1 + "  " + vect[i1] + "\n";
			}
			IJ.log(stri);
		}
		IJ.log("---------------------------------------------");
	}

	public static void logVector(boolean vect[], String nome) {
		String stri = "";
		if (vect == null) {
			IJ.log("Warning vector " + nome + " = null");
		} else {
			IJ.log("----------- " + nome + "  [ " + vect.length + " ] -----------");

			for (int i1 = 0; i1 < vect.length; i1++) {
				if (vect[i1])
					stri = stri + "T, ";
				else
					stri = stri + "F, ";
			}
			IJ.log(stri);
		}
		IJ.log("---------------------------------------------");
	}

	public static void logMatrixDimensions(double[][][] mat1, String nome) {
		IJ.log("matrice " + nome + " [ " + mat1.length + " ] x [ " + mat1[0].length + " ] x [ " + mat1[0][0].length
				+ " ]");
	}

	public static void logMatrixDimensions(double[][] mat1, String nome) {
		IJ.log("matrice " + nome + " [ " + mat1.length + " ] x [ " + mat1[0].length + " ]");
	}

	public static void logMatrixDimensions(float[][][] mat1, String nome) {
		IJ.log("matrice " + nome + " [ " + mat1.length + " ] x [ " + mat1[0].length + " ] x [ " + mat1[0][0].length
				+ " ]");
	}

	public static void logMatrixDimensions(float[][] mat1, String nome) {
		IJ.log("matrice " + nome + " [ " + mat1.length + " ] x [ " + mat1[0].length + " ]");
	}

	public static void logMatrixDimensions(int[][][] mat1, String nome) {
		IJ.log("matrice " + nome + " [ " + mat1.length + " ] x [ " + mat1[0].length + " ] x [ " + mat1[0][0].length
				+ " ]");
	}

	public static void logMatrixDimensions(int[][] mat1, String nome) {
		IJ.log("matrice " + nome + " [ " + mat1.length + " ] x [ " + mat1[0].length + " ]");
	}

	public static void logMatrixDimensions(String[][][] mat1, String nome) {
		IJ.log("matrice " + nome + " [ " + mat1.length + " ] x [ " + mat1[0].length + " ] x [ " + mat1[0][0].length
				+ " ]");
	}

	public static void logMatrixDimensions(String[][] mat1, String nome) {
		IJ.log("matrice " + nome + " [ " + mat1.length + " ] x [ " + mat1[0].length + " ]");
	}

	public static void here() {
		IJ.log("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + "line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber() + " class="
				+ Thread.currentThread().getStackTrace()[2].getClassName() + " method="
				+ Thread.currentThread().getStackTrace()[2].getMethodName());

	}

	public static void here(String str) {
		IJ.log("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + "line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber() + " class="
				+ Thread.currentThread().getStackTrace()[2].getClassName() + " method="
				+ Thread.currentThread().getStackTrace()[2].getMethodName() + " " + str);
	}

	public static String qui() {
		String out = ("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber());
//		+ " class="
//				+ Thread.currentThread().getStackTrace()[2].getClassName() + " method="
//				+ Thread.currentThread().getStackTrace()[2].getMethodName());
		return out;
	}

	public static void waitMessage(String str) {
		new WaitForUserDialog(str).show();
	}

	public static void mark(String str) {
		IJ.log("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + " line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber() + " ===> " + str);
	}

	public static void waitHere(String str) {
		new WaitForUserDialog("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + " line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber() + "\n \n" + str).show();
	}

	public static boolean waitHereModeless1(String str20) {

//		new ij.gui.NonBlockingGenericDialog("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + " line="
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber() + "\n \n" + str20).showDialog();

		NonBlockingGenericDialog nonBlockingGenericDialog = new NonBlockingGenericDialog("Action Required");
//		nonBlockingGenericDialog.addMessage("Confirmation Dialog");
//			nonBlockingGenericDialog.setFont(defaultFont);
		nonBlockingGenericDialog.addMessage(("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " "
				+ " line=" + Thread.currentThread().getStackTrace()[2].getLineNumber() + "\n") + str20);
		nonBlockingGenericDialog.setCancelLabel("<ANNULLA>");
		nonBlockingGenericDialog.showDialog();
		if (nonBlockingGenericDialog.wasCanceled()) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean waitHereModeless(String str20) {

		NonBlockingGenericDialog nonBlockingGenericDialog = new NonBlockingGenericDialog("Action Required");
		nonBlockingGenericDialog.addMessage(("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " "
				+ " line=" + Thread.currentThread().getStackTrace()[2].getLineNumber() + "\n") + str20);
		nonBlockingGenericDialog.setCancelLabel("<ANNULLA>");
		nonBlockingGenericDialog.showDialog();
		if (nonBlockingGenericDialog.wasCanceled()) {
			return true;
		} else {
			return false;
		}

	}

	public static void waitHere(String str, boolean debug) {
		if (debug) {
			IJ.beep();
			new WaitForUserDialog("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + " line="
					+ Thread.currentThread().getStackTrace()[2].getLineNumber() + "\n \n" + str).show();
		} else {
			IJ.beep();
			new WaitForUserDialog(str).show();
		}
	}

	public static int waitHere(String str, boolean debug, String uno, String due) {
		int resp = 0;
		if (debug) {
			IJ.beep();
			resp = ButtonMessages
					.ModelessMsg(
							("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + " line="
									+ Thread.currentThread().getStackTrace()[2].getLineNumber() + "\n \n" + str),
							uno, due);
		} else {
			IJ.beep();
			resp = ButtonMessages.ModelessMsg(str, uno, due);
		}
		return resp;
	}

	public static void waitHere() {
		new WaitForUserDialog("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + " line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()).show();
	}

	// public static void waitHere1(String str, boolean debug, final int milli)
	// {
	// if (milli != 0) {
	//
	// String where = "file="
	// + Thread.currentThread().getStackTrace()[2].getFileName()
	// + " " + " line="
	// + Thread.currentThread().getStackTrace()[2].getLineNumber();
	//
	// JFrame f = new JFrame();
	// JTextArea myarea = new JTextArea("\n " + where
	// + " \n" + "\n " + str + " \n");
	// final JDialog dialog = new JDialog(f, "Auto cancel msg", true);
	// dialog.setLocation(500, 500);
	// dialog.add(myarea);
	// dialog.pack();
	// ScheduledExecutorService s = Executors
	// .newSingleThreadScheduledExecutor();
	//
	// s.schedule(new Runnable() {
	// public void run() {
	// dialog.setVisible(false);
	// dialog.dispose();
	// }
	// }, milli, TimeUnit.MILLISECONDS);
	// dialog.setVisible(true);
	// } else {
	// waitThere(str, debug);
	//
	// }
	// }

	public static void traceStart() {
		IJ.log("");
		Frame lw = WindowManager.getFrame("Log");
		if (lw == null)
			return;
		lw.setSize(600, 1000);
		lw.setLocation(10, 10);

	}

	public static void trace(String str, boolean trok) {

		if (trok)
			// TextWindow tw = new TextWindow("Sequenze", "<-- INIZIO Sequenze -->", 300,
			// 200);

			IJ.log("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + " line="
					+ Thread.currentThread().getStackTrace()[2].getLineNumber() + "    " + str);
	}

	public static void trace(boolean trok) {
		if (trok)
			IJ.log("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + " line="
					+ Thread.currentThread().getStackTrace()[2].getLineNumber());
	}

	public static void trace2(String str) {
		IJ.log("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + " line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber() + "    " + str);
	}

	public static void trace2() {
		IJ.log("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + " line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber());
	}

	public static void waitThere(String str) {
		new WaitForUserDialog("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + " line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber() + "\n" + "file="
				+ Thread.currentThread().getStackTrace()[3].getFileName() + " " + " line="
				+ Thread.currentThread().getStackTrace()[3].getLineNumber() + "\n \n" + str).show();
	}

	public static void waitThere(String str, boolean debug) {
		if (debug) {
			IJ.beep();

			new WaitForUserDialog("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + " line="
					+ Thread.currentThread().getStackTrace()[2].getLineNumber() + "\n" + "file="
					+ Thread.currentThread().getStackTrace()[3].getFileName() + " " + " line="
					+ Thread.currentThread().getStackTrace()[3].getLineNumber() + "\n \n" + str).show();
		} else {
			IJ.beep();
			new WaitForUserDialog(str).show();
		}
	}

	public static String method() {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		StackTraceElement e = stacktrace[2];// coz 0th will be getStackTrace so 1st
		String methodName = e.getMethodName();
		return methodName;
	}

	public static void caller() {
		// dovrebbe ricavare dallo stackTrace l'albero delle chiamate SLOW but
		// USEFUL!
		for (int i1 = 2; i1 < Thread.currentThread().getStackTrace().length - 24; i1++) {
			IJ.log("file=" + Thread.currentThread().getStackTrace()[i1].getFileName() + " " + "line="
					+ Thread.currentThread().getStackTrace()[i1].getLineNumber() + " class="
					+ Thread.currentThread().getStackTrace()[i1].getClassName() + " method="
					+ Thread.currentThread().getStackTrace()[i1].getMethodName());
		}
	}

	public static void caller(String str) {
		// dovrebbe ricavare dallo stackTrace l'albero delle chiamate SLOW but
		// USEFUL!
		IJ.log("___________________________________________");

		int len = Thread.currentThread().getStackTrace().length;

		for (int i1 = 1; i1 < len; i1++) {
			if (Thread.currentThread().getStackTrace()[i1].getFileName() == null)
				break;

			IJ.log("file=" + Thread.currentThread().getStackTrace()[i1].getFileName() + " " + "line="
					+ Thread.currentThread().getStackTrace()[i1].getLineNumber() + " class="
					+ Thread.currentThread().getStackTrace()[i1].getClassName() + " method="
					+ Thread.currentThread().getStackTrace()[i1].getMethodName() + " " + str);
		}
		IJ.log("___________________________________________");
	}

	// /***
	// * Append a line to a log file
	// *
	// * @param path
	// * @param linea
	// */
	// public static void appendLog(String path, String linea) {
	//
	// BufferedWriter out;
	// String time = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new
	// Date());
	//
	// try {
	// out = new BufferedWriter(new FileWriter(path, true));
	// out.write(time + " " + linea);
	// out.newLine();
	// out.close();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

	public static void appendLog2(String path, String linea) {

		BufferedWriter out;
		// String time = new SimpleDateFormat("yyyy-MM-dd hh:mm")
		// .format(new Date());

		try {
			out = new BufferedWriter(new FileWriter(path, true));
			out.write("$> " + linea);
			out.newLine();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void appendLog3(String path, String linea) {

		BufferedWriter out;

		try {
			out = new BufferedWriter(new FileWriter(path, true));
			out.write(linea);
			out.newLine();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// /***
	// * Initialize a log file
	// *
	// * @param path
	// */
	// public static void initLog(String path) {
	// File f1 = new File(path);
	// if (f1.exists()) {
	// f1.delete();
	// }
	// appendLog(path, "---- INIZIO ---------");
	// }

	public static void initLog3(String path) {
		File f1 = new File(path);
		if (f1.exists()) {
			f1.delete();
		}
	}

	/***
	 * Il programma mostra un WaitForUserDialog ma, passato il timeout, chiude il
	 * dialogo, proprio come fosse stato premuto ok
	 * 
	 * @param str     messtaggio
	 * @param debug   switch di attivazione
	 * @param timeout millisecondi per il timeout
	 */

	public static void waitHere(String str, boolean debug, int timeout) {

		String where = "";
		if (debug)
			where = " \nfile=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + " line="
					+ Thread.currentThread().getStackTrace()[2].getLineNumber() + " \n \n";

		if (timeout > 0) {
			final WaitForUserDialog wfud = new WaitForUserDialog(where + str);

			ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
			s.schedule(new Runnable() {
				public void run() {
					wfud.close();
					wfud.dispose();
				}
			}, timeout, TimeUnit.MILLISECONDS);
			wfud.setBackground(Color.yellow);
			wfud.show();
		} else {
			final WaitForUserDialog wfud = new WaitForUserDialog(where + str);

			wfud.show();
		}
	}

	public static void waitHere(String str, boolean debug, int timeout, boolean animate) {

		String where = "";
		if (debug)
			where = " \nfile=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + " line="
					+ Thread.currentThread().getStackTrace()[2].getLineNumber() + " \n \n";

		if ((timeout > 0) && animate) {
			final WaitForUserDialog wfud = new WaitForUserDialog(where + str);

			ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
			s.schedule(new Runnable() {
				public void run() {
					wfud.close();
					wfud.dispose();
				}
			}, timeout, TimeUnit.MILLISECONDS);
			wfud.setBackground(Color.yellow);
			wfud.show();
		} else {
			final WaitForUserDialog wfud = new WaitForUserDialog(where + str);

			wfud.show();
		}
	}

	public static void logDebug(int riga, String prg, String fileDir) {
		String[][] table = new TableSequence().loadTable(fileDir + MyConst.SEQUENZE_FILE);
		String tableRiga = TableSequence.getRow(table, riga);
		String tablePath = TableSequence.getPath(table, riga);
		File f1 = new File(tablePath);
		String tableCode = TableSequence.getCode(table, riga);
		String tableCoil = TableSequence.getCoil(table, riga);
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(tablePath, true);
		String aux1 = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_SERIES_DESCRIPTION);
		String imaCode = "";
		if (aux1.length() >= 5)
			imaCode = aux1.substring(0, 5).trim();
		else
			imaCode = aux1.substring(0, aux1.length()).trim();
		String imaCoil = ReadDicom.getAllCoils(imp1);
		MyLog.appendLog(fileDir + "MyLog.txt",
				prg + " > logDebug name= " + f1.getName() + "  tableRiga= " + tableRiga + " tableCode= " + tableCode
						+ " imaCode= " + imaCode + " tableCoil=" + tableCoil + " imaCoil= " + imaCoil);
	}

	public static void appendLog(String path, String linea) {

		BufferedWriter out;
		String time = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date());
		String where = ("file=" + Thread.currentThread().getStackTrace()[2].getFileName() + " " + " line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber() + " ===> ");

		try {
			out = new BufferedWriter(new FileWriter(path, true));
			out.write(time + " " + where + " " + linea);
			out.newLine();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void initLog(String path) {
		File f1 = new File(path);
		if (f1.exists()) {
			f1.delete();
		}
		appendLog(path, "---- INIZIO ---------");
	}

	public static void saveLog(String path) {
		IJ.selectWindow("Log");
		IJ.saveAs("text", path);
		return;
	}

}
