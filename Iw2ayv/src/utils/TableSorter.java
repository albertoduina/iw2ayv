package utils;

import ij.IJ;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.sun.tools.javac.code.Attribute.Array;

public class TableSorter {

	public static String[][] loadTable(String path) {
		boolean absolute = false;
		String[][] tableCode1 = new InputOutput().readFile6LIKE(path, absolute);
		String[][] tableCode = InputOutput.removeColumn(tableCode1, 1);
		return tableCode;
	}

	public static String[][] loadMultipleTable(String[] pathComplete) {
		boolean absolute = false;
		String path = "";
		String path2 = "";
		String[][] tableCode1 = null;
		String[][] tableCode = null;
		String[][] sumTableCode = null;
		for (int i1 = 0; i1 < pathComplete.length; i1++) {
			path = pathComplete[i1];
			path2 = InputOutput.findResource(path);
			if (path2 == null)
				continue;
			tableCode1 = new InputOutput().readFile6LIKE(path, absolute);
			tableCode = InputOutput.removeColumn(tableCode1, 1);
			sumTableCode = TableUtils.sumMultipleTable(sumTableCode, tableCode);
		}
		// MyLog.logMatrix(sumTableCode, "sumTableCode");
		// MyLog.waitHere();
		return sumTableCode;
	}

	/**
	 * Effettua il bubble sort della tabella delle sequenze, utilizza
	 * l'algoritmo bubblesort
	 * 
	 * @param tableIn
	 * @return
	 */
	public String[][] bubbleSortSequenceTable(String[][] tableIn) {

		if (tableIn == null) {
			IJ.log("bubbleSortTable.tableIn == null");
			return null;
		}
		long[] bubblesort = new long[tableIn.length];
		String[][] tableOut = new TableUtils().duplicateTable(tableIn);
		for (int i1 = 0; i1 < tableOut.length; i1++) {
			String acqTime = TableSequence.getAcqTime(tableOut, i1);
			if (acqTime == null)
				acqTime = "9999999999999999";
			bubblesort[i1] = Long.parseLong(acqTime);
		}
		String[] tempRiga = new String[tableOut[0].length];
		boolean sorted = false;
		while (!sorted) {
			sorted = true;
			for (int i1 = 0; i1 < (bubblesort.length - 1); i1++) {
				if (bubblesort[i1] > bubblesort[i1 + 1]) {
					long temp = bubblesort[i1];
					// N.B. i2 in questo caso partir� da 1, poich� la colonna 0
					// che contiene il numero della riga NON deve venire sortata
					for (int i2 = 1; i2 < tableOut[0].length; i2++)
						tempRiga[i2] = tableOut[i1][i2];
					bubblesort[i1] = bubblesort[i1 + 1];
					for (int i2 = 1; i2 < tableOut[0].length; i2++)
						tableOut[i1][i2] = tableOut[i1 + 1][i2];
					bubblesort[i1 + 1] = temp;
					for (int i2 = 1; i2 < tableOut[0].length; i2++)
						tableOut[i1 + 1][i2] = tempRiga[i2];
					sorted = false;
				}
			}
		}
		return tableOut;
	}

	/**
	 * Effettua il bubble sort della tabella delle sequenze, utilizza
	 * l'algoritmo bubblesort
	 * 
	 * @param tableIn
	 * @return
	 */
	public String[][] bubbleSortSequenceTable2(String[][] tableIn) {

		if (tableIn == null) {
			IJ.log("bubbleSortTable.tableIn == null");
			return null;
		}
		long[] bubblesort1 = new long[tableIn.length];
		int[] bubblesort2 = new int[tableIn.length];
		String[][] tableOut = new TableUtils().duplicateTable(tableIn);
		for (int i1 = 0; i1 < tableOut.length; i1++) {
			String acqTime = TableSequence.getAcqTime(tableOut, i1);
			if (acqTime == null)
				acqTime = "9999999999999999";
			bubblesort1[i1] = Long.parseLong(acqTime);
		}
		for (int i1 = 0; i1 < tableOut.length; i1++) {
			String numIma = TableSequence.getNumIma(tableOut, i1);
			bubblesort2[i1] = ReadDicom.readInt(numIma);
		}

		String[] tempRiga = new String[tableOut[0].length];
		boolean sorted = false;
		while (!sorted) {
			sorted = true;
			for (int i1 = 0; i1 < (bubblesort1.length - 1); i1++) {
				if (bubblesort1[i1] > bubblesort1[i1 + 1]) {
					long temp = bubblesort1[i1];
					// N.B. i2 in questo caso partir� da 1, poich� la colonna 0
					// che contiene il numero della riga NON deve venire sortata
					for (int i2 = 1; i2 < tableOut[0].length; i2++)
						tempRiga[i2] = tableOut[i1][i2];
					bubblesort1[i1] = bubblesort1[i1 + 1];
					for (int i2 = 1; i2 < tableOut[0].length; i2++)
						tableOut[i1][i2] = tableOut[i1 + 1][i2];
					bubblesort1[i1 + 1] = temp;
					for (int i2 = 1; i2 < tableOut[0].length; i2++)
						tableOut[i1 + 1][i2] = tempRiga[i2];
					sorted = false;
				} else if ((bubblesort1[i1] == bubblesort1[i1 + 1]) && (bubblesort2[i1] > bubblesort2[i1 + 1])) {
					int temp2 = bubblesort2[i1];
					// N.B. i2 in questo caso partir� da 1, poich� la colonna 0
					// che contiene il numero della riga NON deve venire sortata
					for (int i2 = 1; i2 < tableOut[0].length; i2++)
						tempRiga[i2] = tableOut[i1][i2];
					bubblesort2[i1] = bubblesort2[i1 + 1];
					for (int i2 = 1; i2 < tableOut[0].length; i2++)
						tableOut[i1][i2] = tableOut[i1 + 1][i2];
					bubblesort2[i1 + 1] = temp2;
					for (int i2 = 1; i2 < tableOut[0].length; i2++)
						tableOut[i1 + 1][i2] = tempRiga[i2];
					sorted = false;
				}
			}
		}
		return tableOut;
	}

	public static String[][] minsort(String[][] tableIn, int key) {

		String[][] tableOut = new TableUtils().duplicateTable(tableIn);
		double[] vetKey = new double[tableIn.length];
		int[] vetIndex = new int[tableIn.length];

		for (int i1 = 0; i1 < tableOut.length; i1++) {
			String strKey = TableSequence.getKey(tableOut, i1, key);
			if (strKey == null)
				strKey = "9999999999999999";
			if (UtilAyv.isNumeric(strKey))
				vetKey[i1] = Double.parseDouble(strKey);
			else
				vetKey[i1] = Double.NaN;
			vetIndex[i1] = i1;
		}

		// effettuo minsort su key, gli altri campi andranno in parallelo
		double aux1 = 0;
		int aux2 = 0;
		for (int i1 = 0; i1 < vetKey.length; i1++) {
			for (int i2 = i1 + 1; i2 < vetKey.length; i2++) {
				if (vetKey[i2] < vetKey[i1]) {
					aux1 = vetKey[i1];
					vetKey[i1] = vetKey[i2];
					vetKey[i2] = aux1;
					aux2 = vetIndex[i1];
					vetIndex[i1] = vetIndex[i2];
					vetIndex[i2] = aux2;
				}
			}
		}

		// MyLog.logVector(vetIndex, "vetIndex");
		// MyLog.logVector(vetKey, "vetKey");

		// a questo punto usando vetIndex, posso riordinare la tabella in un
		// unica passata
		for (int i1 = 0; i1 < tableOut[0].length; i1++) {
			for (int i2 = 0; i2 < vetIndex.length; i2++) {
				tableOut[i2][i1] = tableIn[vetIndex[i2]][i1];
			}
			tableOut[0][i1] = "" + i1;
		}
		return tableOut;
	}

	/***
	 * Riceve in input la tabella gia' sortata, riordinata e verificata e pronta
	 * per l'utilizzo. Raggruppa le acquisizioni effettuate con slices multiple
	 * utilizzando la slicePos. Per renderlo VERAMENTE SMART (funzionante) ho
	 * dovuto rinumerare con il progressivo
	 * 
	 * @param tableIn
	 *            tabella da riordinare
	 * @param myCode
	 *            codici su cui effettuare il riordino
	 * @return
	 */
	public static String[][] tableModifierSmart(String[][] tableIn, String[] myCode) {

		String[][] tableOut = new String[tableIn.length][tableIn[0].length];
		String[] vetCode = new String[tableIn.length];
		String[] vetCoil = new String[tableIn.length];
		String[] vetPosiz = new String[tableIn.length];
		// String[] vetAcq = new String[tableIn.length];

		// carico nei vettori indice i relativi valori
		for (int i10 = 0; i10 < tableIn.length; i10++) {
			vetCode[i10] = TableSequence.getCode(tableIn, i10);
			vetCoil[i10] = TableSequence.getCoil(tableIn, i10);
			vetPosiz[i10] = TableSequence.getPosiz(tableIn, i10);
			// vetAcq[i10] = TableSequence.getNumAcq(tableIn, i10);
		}

		String oldCode = "";
		String oldCoil = "";
		String oldPosiz = "";
		String newCode = "";
		String newCoil = "";
		String newPosiz = "";
		int outOrder = -1;

		for (int i1 = 0; i1 < vetCode.length; i1++) {
			if (vetCode[i1] != "") {
				oldCode = vetCode[i1];
				oldCoil = vetCoil[i1];
				oldPosiz = vetPosiz[i1];
			}

			for (int i4 = i1; i4 < vetCode.length; i4++) {
				newCode = vetCode[i4];
				newCoil = vetCoil[i4];
				newPosiz = vetPosiz[i4];
				if ((newCode.equals(oldCode)) && (newCoil.equals(oldCoil)) && (newPosiz.equals(oldPosiz))) {
					// se giungo qui vuol dire che devo trasferire il codice nel
					// file ordinato
					outOrder++;
					tableOut[outOrder][0] = String.valueOf(outOrder);
					for (int i5 = 1; i5 < tableIn[0].length; i5++) {
						tableOut[outOrder][i5] = tableIn[i4][i5];
					}
					vetCode[i4] = "";
				}
			}
		}
		return tableOut;
	}

	public static String[][] tableModifierSmart2(String[][] tableIn, String[] myCode, boolean debug1) {

		String[][] tableOut = new TableUtils().duplicateTable(tableIn);
		String[] vetCode = new String[tableIn.length];
		String[] vetCoil = new String[tableIn.length];
		String[] vetPosiz = new String[tableIn.length];
		String[] vetAcq = new String[tableIn.length];
		// carico nei vettori indice i relativi valori
		for (int i10 = 0; i10 < tableOut.length; i10++) {
			vetCode[i10] = TableSequence.getCode(tableOut, i10);
			vetCoil[i10] = TableSequence.getCoil(tableOut, i10);
			vetPosiz[i10] = TableSequence.getPosiz(tableOut, i10);
			vetAcq[i10] = TableSequence.getNumAcq(tableOut, i10);
		}
		int order = 0;
		int index2 = 0;
		int index3 = 0;
		int index4 = 0;
		ArrayList<String> posiz = new ArrayList<String>();
		ArrayList<String> code = new ArrayList<String>();
		ArrayList<String> coil = new ArrayList<String>();
		ArrayList<String> acq = new ArrayList<String>();
		for (int i1 = 0; i1 < myCode.length; i1++) {
			for (int i10 = 0; i10 < tableOut.length; i10++) {
				if ((TableSequence.getCode(tableOut, i10).equals(myCode[i1]))) {
					code.add(TableSequence.getCode(tableOut, i10));
					coil.add(TableSequence.getCoil(tableOut, i10));
					posiz.add(TableSequence.getPosiz(tableOut, i10));
					acq.add(TableSequence.getNumAcq(tableOut, i10));
				}
			}
		}
		String[] newCode = ArrayUtils.arrayListToArrayString(code);
		String[] newCoil = ArrayUtils.arrayListToArrayString(coil);
		String[] newPosiz = ArrayUtils.arrayListToArrayString(posiz);
		// ===========================================================================
		for (int i1 = 0; i1 < newCode.length; i1++) {
			// carico nei vettori indice i relativi valori
			for (int i10 = 0; i10 < tableOut.length; i10++) {
				vetCode[i10] = TableSequence.getCode(tableOut, i10);
				vetCoil[i10] = TableSequence.getCoil(tableOut, i10);
				vetPosiz[i10] = TableSequence.getPosiz(tableOut, i10);
				vetAcq[i10] = TableSequence.getNumAcq(tableOut, i10);
			}
			for (int i4 = 0; i4 < vetCode.length - 2; i4++) {
				if ((newCode[i1].equals(vetCode[i4])) && (newCoil[i1].equals(vetCoil[i4]))
						&& (newPosiz[i1].equals(vetPosiz[i4]))) {
					order++;
					if (order == 2) {
						index2 = i4;
					}
					if (order == 3) {
						index3 = i4;
					}
					if (order == 4) {
						index4 = i4;
					}
					if (order == 4) {
						IJ.log("index2= " + index2 + "  index3= " + index3 + "  index4= " + index4);
						// ho identificato le quattro righe
						// contenenti le quattro immagini che ci
						// interessano, e' il momento di effettuare
						// lo swap ed uscire dal loop
						tableSwapper(tableOut, index2 + 1, index3);
						tableSwapper(tableOut, index2 + 2, index4);
						order = 0;
						break;
					}
				}
			}
		}
		return tableOut;
	}

	/***
	 * Effettua lo swap delle due righe richieste
	 * 
	 * @param tableIn
	 * @param riga1
	 * @param riga2
	 * @return
	 */
	public static void tableSwapper(String[][] tableOut, int riga1, int riga2) {
		String aux1 = "";

		// if (riga1>= tableOut.length) MyLog.waitHere("BOH 1");
		// if (riga2>= tableOut.length) MyLog.waitHere("BOH 2");
		//// IJ.log("tableOut.length= " + tableOut.length + " riga1= " + riga1 +
		// "riga2= " + riga2);

		for (int i1 = 0; i1 < tableOut[0].length; i1++) {
			aux1 = tableOut[riga1][i1];
			tableOut[riga1][i1] = tableOut[riga2][i1];
			tableOut[riga2][i1] = aux1;
		}
		return;
	}

}
