package utils;

import java.util.ArrayList;

public class TableCode {

	public static int CODE = 0;

	public static int IMA_PASS = 1;

	public static int IMA_TOTAL = 2;

	public static int COIL = 3;

	public static int DIREZ = 4;

	public static int PROFOND = 5;

	public static int PLUGIN = 6;

	public static String[][] loadTable(String path) {

		ArrayList<ArrayList<String>> tableArray = new InputOutput()
				.readFile3(path);
		String[][] tableCode = new InputOutput()
				.fromArrayListToStringTable(tableArray);

		return tableCode;
	}

	public static String[][] loadTableCSV(String path) {

		String[][] tableCode = new InputOutput().readFile7(path);
		return tableCode;
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
