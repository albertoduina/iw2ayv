package utils;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.ImageWindow;
import ij.gui.MessageDialog;
import ij.gui.WaitForUserDialog;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MyLog {

	public static void logArrayList(ArrayList<Double> arrList, String title) {
		if (arrList == null) {
			IJ.log("Warning vector " + title + " = null");
		} else {
			IJ.log("----------- " + title + "  [ " + arrList.size()
					+ " ] -----------");
			String logRiga = "";
			for (int j1 = 0; j1 < arrList.size(); j1++) {
				logRiga += arrList.get(j1) + ",  ";
			}
			IJ.log(logRiga);
		}
	}

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

	public static void logArrayListTable(
			ArrayList<ArrayList<Double>> matrixTable, String title) {
		// ArrayList<Double> row1 = new ArrayList<Double>();
		if (matrixTable == null) {
			IJ.log("fromArrayListToStringTable.matrixTable == null");
			return;
		}
		IJ.log("####### " + title + "[" + matrixTable.get(0).size() + " x "
				+ matrixTable.size() + "] ########");
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

	public static void logArrayListTable2(
			ArrayList<ArrayList<String>> matrixTable, String title) {
		// ArrayList<Double> row1 = new ArrayList<Double>();
		if (matrixTable == null) {
			IJ.log("logArrayListTable == null");
			return;
		}
		IJ.log("####### " + title + "[" + matrixTable.get(0).size() + " x "
				+ matrixTable.size() + "] ########");
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
			IJ.log("Warning matrix " + nome + " = null");
		} else {
			rows = mat.length;
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

	public static void logMatrixVertical(double mat[][], String nome) {
		String stri = "";
		int rows = 0;
		int columns = 0;
		if (mat == null) {
			IJ.log("Warning matrix " + nome + " = null");
		} else {
			rows = mat.length;
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
			IJ.log("Warning matrix " + nome + " = null");
		} else {
			rows = mat.length;
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
			IJ.log("Warning matrix " + nome + " = null");
		} else {
			rows = mat.length;
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
			IJ.log("Warning matrix " + nome + " = null");
		} else {
			rows = mat.length;
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

	public static void logVector(double vect[], String nome) {
		String stri = "";
		if (vect == null) {
			IJ.log("Warning vector " + nome + " = null");
		} else {
			IJ.log("----------- " + nome + "  [ " + vect.length
					+ " ] -----------");

			for (int i1 = 0; i1 < vect.length; i1++) {
				stri = stri + vect[i1] + ",  ";
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
			IJ.log("----------- " + nome + "  [ " + vect.length
					+ " ] -----------");

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

			IJ.log("----------- " + nome + "  [ " + vect.length
					+ " ] -----------");
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

			IJ.log("----------- " + nome + "  [ " + vect.length
					+ " ] -----------");
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

			IJ.log("----------- " + nome + "  [ " + vect.length
					+ " ] -----------");
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

			IJ.log("----------- " + nome + "  [ " + vect.length
					+ " ] -----------");
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
			IJ.log("----------- " + nome + "  [ " + vect.length
					+ " ] -----------");

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
			IJ.log("----------- " + nome + "  [ " + vect.length
					+ " ] -----------");

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
			IJ.log("----------- " + nome + "  [ " + vect.length
					+ " ] -----------");

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
		IJ.log("matrice " + nome + " [ " + mat1.length + " ] x [ "
				+ mat1[0].length + " ] x [ " + mat1[0][0].length + " ]");
	}

	public static void logMatrixDimensions(double[][] mat1, String nome) {
		IJ.log("matrice " + nome + " [ " + mat1.length + " ] x [ "
				+ mat1[0].length + " ]");
	}

	public static void logMatrixDimensions(float[][][] mat1, String nome) {
		IJ.log("matrice " + nome + " [ " + mat1.length + " ] x [ "
				+ mat1[0].length + " ] x [ " + mat1[0][0].length + " ]");
	}

	public static void logMatrixDimensions(float[][] mat1, String nome) {
		IJ.log("matrice " + nome + " [ " + mat1.length + " ] x [ "
				+ mat1[0].length + " ]");
	}

	public static void logMatrixDimensions(int[][][] mat1, String nome) {
		IJ.log("matrice " + nome + " [ " + mat1.length + " ] x [ "
				+ mat1[0].length + " ] x [ " + mat1[0][0].length + " ]");
	}

	public static void logMatrixDimensions(int[][] mat1, String nome) {
		IJ.log("matrice " + nome + " [ " + mat1.length + " ] x [ "
				+ mat1[0].length + " ]");
	}

	public static void logMatrixDimensions(String[][][] mat1, String nome) {
		IJ.log("matrice " + nome + " [ " + mat1.length + " ] x [ "
				+ mat1[0].length + " ] x [ " + mat1[0][0].length + " ]");
	}

	public static void logMatrixDimensions(String[][] mat1, String nome) {
		IJ.log("matrice " + nome + " [ " + mat1.length + " ] x [ "
				+ mat1[0].length + " ]");
	}

	public static void here() {
		IJ.log("file="
				+ Thread.currentThread().getStackTrace()[2].getFileName() + " "
				+ "line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ " class="
				+ Thread.currentThread().getStackTrace()[2].getClassName()
				+ " method="
				+ Thread.currentThread().getStackTrace()[2].getMethodName());

	}

	public static void here(String str) {
		IJ.log("file="
				+ Thread.currentThread().getStackTrace()[2].getFileName() + " "
				+ "line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ " class="
				+ Thread.currentThread().getStackTrace()[2].getClassName()
				+ " method="
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ " " + str);
	}

	public static String qui() {
		String out = ("file="
				+ Thread.currentThread().getStackTrace()[2].getFileName() + " "
				+ " line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ " class="
				+ Thread.currentThread().getStackTrace()[2].getClassName()
				+ " method="
				+ Thread.currentThread().getStackTrace()[2].getMethodName() + "\n \n");
		return out;
	}

	public static void waitMessage(String str) {
		new WaitForUserDialog(str).show();
	}

	public static void waitHere(String str) {
		new WaitForUserDialog("file="
				+ Thread.currentThread().getStackTrace()[2].getFileName() + " "
				+ " line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "\n \n" + str).show();
	}

	public static void waitHere(String str, boolean debug) {
		if (debug) {
			IJ.beep();
			new WaitForUserDialog("file="
					+ Thread.currentThread().getStackTrace()[2].getFileName()
					+ " " + " line="
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "\n \n" + str).show();
		} else {
			IJ.beep();
			new WaitForUserDialog(str).show();
		}
	}

	public static void waitHere(String str, boolean debug, String uno,
			String due) {
		if (debug) {
			IJ.beep();
			ButtonMessages.ModelessMsg(("file="
					+ Thread.currentThread().getStackTrace()[2].getFileName()
					+ " " + " line="
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "\n \n" + str), uno, due);
		} else {
			IJ.beep();
			ButtonMessages.ModelessMsg(str, uno, due);
		}
	}

	public static void waitHere() {
		new WaitForUserDialog("file="
				+ Thread.currentThread().getStackTrace()[2].getFileName() + " "
				+ " line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber())
				.show();
	}

	public static void waitThere(String str) {
		new WaitForUserDialog("file="
				+ Thread.currentThread().getStackTrace()[3].getFileName() + " "
				+ " line="
				+ Thread.currentThread().getStackTrace()[3].getLineNumber()
				+ "\n \n" + str).show();
	}

	public static void waitThere(String str, boolean debug) {
		if (debug) {
			IJ.beep();

			new WaitForUserDialog("file="
					+ Thread.currentThread().getStackTrace()[3].getFileName()
					+ " " + " line="
					+ Thread.currentThread().getStackTrace()[3].getLineNumber()
					+ "\n \n" + str).show();
		} else {
			IJ.beep();
			new WaitForUserDialog(str).show();
		}
	}

	public static void caller() {
		// dovrebbe ricavare dallo stackTrace l'albero delle chiamate SLOW but
		// USEFUL!
		for (int i1 = 2; i1 < Thread.currentThread().getStackTrace().length - 24; i1++) {
			IJ.log("file="
					+ Thread.currentThread().getStackTrace()[i1].getFileName()
					+ " "
					+ "line="
					+ Thread.currentThread().getStackTrace()[i1]
							.getLineNumber()
					+ " class="
					+ Thread.currentThread().getStackTrace()[i1].getClassName()
					+ " method="
					+ Thread.currentThread().getStackTrace()[i1]
							.getMethodName());
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

			IJ.log("file="
					+ Thread.currentThread().getStackTrace()[i1].getFileName()
					+ " "
					+ "line="
					+ Thread.currentThread().getStackTrace()[i1]
							.getLineNumber()
					+ " class="
					+ Thread.currentThread().getStackTrace()[i1].getClassName()
					+ " method="
					+ Thread.currentThread().getStackTrace()[i1]
							.getMethodName() + " " + str);
		}
		IJ.log("___________________________________________");
	}

	/***
	 * Append a line to a log file
	 * 
	 * @param path
	 * @param linea
	 */
	public static void appendLog(String path, String linea) {

		BufferedWriter out;
		String time = new SimpleDateFormat("yyyy-MM-dd hh:mm")
				.format(new Date());

		try {
			out = new BufferedWriter(new FileWriter(path, true));
			out.write(time + " " + linea);
			out.newLine();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/***
	 * Initialize a log file
	 * 
	 * @param path
	 */
	public static void initLog(String path) {
		File f1 = new File(path);
		if (f1.exists()) {
			f1.delete();
		}
		appendLog(path, "---- INIZIO ---------");
	}

	public static void waitHere(String str, int milliseconds, boolean debug) {

		String stri1 = "file="
				+ Thread.currentThread().getStackTrace()[2].getFileName() + " "
				+ " line="
				+ Thread.currentThread().getStackTrace()[2].getLineNumber();

		ScheduledExecutorService s = Executors
				.newSingleThreadScheduledExecutor();

		final ModelessDialog wfud = new ModelessDialog("\n" + stri1 + "\n\n\n"
				+ str);

		// JFrame f = new JFrame();
		//
		// final JDialog jd1 = new JDialog(f, "Messaggio"+stri1, true);
		s.schedule(new Runnable() {
			@Override
			public void run() {
				wfud.dispose();
			}
		}, milliseconds, TimeUnit.SECONDS);
		wfud.setVisible(true);
	}

	public static void logDebug(int riga, String prg, String fileDir) {
		String[][] table = new TableSequence().loadTable(fileDir
				+ MyConst.SEQUENZE_FILE);
		String tableRiga = TableSequence.getRow(table, riga);
		String tablePath = TableSequence.getPath(table, riga);
		File f1 = new File(tablePath);
		String tableCode = TableSequence.getCode(table, riga);
		String tableCoil = TableSequence.getCoil(table, riga);
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(tablePath, true);
		String imaCode = ReadDicom
				.readDicomParameter(imp1, MyConst.DICOM_SERIES_DESCRIPTION)
				.substring(0, 5).trim();
		String imaCoil = ReadDicom.getAllCoils(imp1);
		MyLog.appendLog(fileDir + "MyLog.txt",
				prg + " > logDebug name= " + f1.getName() + "  tableRiga= "
						+ tableRiga + " tableCode= " + tableCode + " imaCode= "
						+ imaCode + " tableCoil=" + tableCoil + " imaCoil= "
						+ imaCoil);
	}

}
