package utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ij.IJ;

// import com.sun.tools.javac.code.Attribute.Array;

public class TableCode {

	public static int CODE = 0;

	public static int IMA_PASS = 1;

	public static int IMA_TOTAL = 2;

	public static int IMA_ORDER = 3;

	public static int IMA_INCREMENT = 4;

	public static int SPARE_1 = 5;

	public static int SPARE_2 = 6;

	public static int SPARE_3 = 7;

	public static int COIL = 8;

	public static int DIREZ = 9;

	public static int PROFOND = 10;

	public static int PLUGIN = 11;

	/**
	 * Carica la tabella CODICI, permettendo di mettere la data all'interno del
	 * nome. Originariamente da file anche multipli, ora da un singolo file, come da
	 * messaggio di errore
	 * 
	 * @param part1
	 * @param part2
	 * @return
	 */
	public String[][] loadMultipleTable(String part1, String part2) {
		String target_file; // fileThatYouWantToFilter
		List<String> list1 = new ArrayList<String>();
		URL url3 = this.getClass().getClassLoader().getResource("contMensili/Sequenze_.class");
		String myString = url3.toString();
		int start = myString.indexOf("plugins");
		int end = myString.lastIndexOf("!");
		// MyLog.waitHere("start=" + start + " end1= " + end + "myString=" + myString);

		String myPart1 = myString.substring(start, end);
		end = myPart1.lastIndexOf("/");
		String myPart2 = myPart1.substring(0, end + 1);
		//MyLog.waitHere("end2= " + end);

		//IJ.log("myString= " + myString + " myPart1= " + myPart1 + " myPart2=" + myPart2);

		File folderToScan = new File(myPart2);

		File[] listOfFiles = folderToScan.listFiles();
		// MyLog.waitHere("listOfFiles length= " + listOfFiles.length);

		for (int i1 = 0; i1 < listOfFiles.length; i1++) {
			if (listOfFiles[i1].isFile()) {
				target_file = listOfFiles[i1].getName();
//				IJ.log("" + target_file);
				if (target_file.startsWith(part1) && target_file.endsWith(part2)) {
					list1.add(target_file);
//					if (target_file.startsWith("codici") && target_file.endsWith(".csv")) {
//						list1.add(target_file);
//					}
				}
			}

		}

		// MyLog.waitHere();
		String[] list2 = ArrayUtils.arrayListToArrayString(list1);
		if (list2.length == 1) {
		} else {
			MyLog.waitHere(
					"ATTENZIONE: esiste piu' di un file col nome che inizia con 'CODICI' ed estensione '.CSV', cio' non deve succedere MAI, MAI, MAIIIII");
		}

		// MyLog.logVector(list2, "list2");
		// MyLog.waitHere();
		String[][] table1 = loadMultipleTable2(list2);
		return table1;
	}

	/**
	 * Estrae il nome del primo file contenente la tabella. Introdotto per poter
	 * vedere che versione di codicixxx.csv viene utilizzata dall'utente
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
		String nome1 = list2[0];
		return nome1;
	}

	/**
	 * Ricerca di eventuali codici doppi, cosa che non deve accadere.
	 * 
	 * @param table
	 * @return
	 */
	public boolean ricercaDoppioni(String[][] table) {

		String code1 = "";
		String code2 = "";
		String coil1 = "";
		String coil2 = "";
		boolean doppio = false;
		for (int i1 = 0; i1 < table.length; i1++) {
			code1 = TableCode.getCode(table, i1);
			coil1 = TableCode.getCoil(table, i1);
			for (int i2 = i1 + 1; i2 < table.length; i2++) {
				code2 = TableCode.getCode(table, i2);
				coil2 = TableCode.getCoil(table, i2);
				if (code1.equals(code2) && coil1.equals(coil2)) {
					doppio = true;
					IJ.log("doppione rilevato tra riga " + i1 + " cod1=" + code1 + " coil1=" + coil1 + " e riga " + i2
							+ "cod2= " + code2 + " coil2= " + coil2);
				}
			}
		}
		return doppio;
	}

	public static String[][] loadTable(String path) {
		boolean absolute = false;
		String[][] tableCode1 = new InputOutput().readFile6LIKE(path, absolute);
		String[][] tableCode = InputOutput.removeColumn(tableCode1, 1);
		MyLog.logMatrix(tableCode, "tableCode");
		MyLog.waitHere();
		return tableCode;
	}

	public static String[][] loadMultipleTable2(String[] pathComplete) {
		boolean absolute = false;
		String path = "";
		String path2 = "";
		String[][] tableCode1 = null;
		String[][] tableCode2 = null;
		String[][] tableCode = null;
		String[][] sumTableCode = null;
		for (int i1 = 0; i1 < pathComplete.length; i1++) {
			path = pathComplete[i1];
			path2 = InputOutput.findResource(path);
			if (path2 == null)
				continue;
			tableCode1 = new InputOutput().readFile6LIKE(path, absolute);
			tableCode2 = InputOutput.substCharInMatrix(tableCode1, "*", ";");
			tableCode = InputOutput.removeColumn(tableCode2, 1);
			sumTableCode = TableUtils.sumMultipleTable(sumTableCode, tableCode);
		}
		// MyLog.logMatrix(sumTableCode, "sumTableCode");
		// MyLog.waitHere();
		return sumTableCode;
	}

	public static String[][] loadMultipleTableChiaro(String[] pathComplete) {
		boolean absolute = false;
		String path = "";
		String path2 = "";
		String[][] tableCode1 = null;
		String[][] tableCode2 = null;
		String[][] tableCode = null;
		String[][] sumTableCode = null;
		for (int i1 = 0; i1 < pathComplete.length; i1++) {
			path = pathComplete[i1];
			path2 = InputOutput.findResource(path);
			if (path2 == null)
				continue;
			MyLog.waitHere("path= " + path + "\npath2= " + path2);
			tableCode1 = new InputOutput().readFile6LIKE(path, absolute);
			tableCode2 = InputOutput.substCharInMatrix(tableCode1, "*", ";");
			tableCode = InputOutput.removeColumn(tableCode2, 1);
			sumTableCode = TableUtils.sumMultipleTable(sumTableCode, tableCode);
		}
		// MyLog.logMatrix(sumTableCode, "sumTableCode");
		// MyLog.waitHere();
		return sumTableCode;
	}

	public static String getCode(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][CODE]);
	}

	public static String getImaPass(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][IMA_PASS]);
	}

	public static String getImaTotal(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][IMA_TOTAL]);
	}

	public static String getImaOrder(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][IMA_ORDER]);
	}

	public static String getImaIncrement(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][IMA_INCREMENT]);
	}

	public static String getImaSpare_1(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][SPARE_1]);
	}

	public static String getImaSpare_2(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][SPARE_2]);
	}

	public static String getImaSpare_3(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][SPARE_3]);
	}

	public static String getCoil(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][COIL]);
	}

	public static String getDirez(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][DIREZ]);
	}

	public static String getProfond(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][PROFOND]);
	}

	public static String getPluginName(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][PLUGIN]);
	}

}
