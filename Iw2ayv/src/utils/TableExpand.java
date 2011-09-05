package utils;

import java.util.ArrayList;




public class TableExpand {

	public static int OLD_CODE = 0;

	public static int ECHO = 1;

	public static int NEW_CODE = 2;

	public static int IMA_PASS = 3;

	public static int IMA_TOTAL = 4;

	public static int PLUGIN = 5;

	public static String[][] loadTable(String path) {

		ArrayList<ArrayList<String>> tableArray = new InputOutput()
				.readFile3(path);
		String[][] tableExpand = new InputOutput()
				.fromArrayListToStringTable(tableArray);

		
		return tableExpand;
	}

	public static String getOldCode(String[][] tableExpand, int riga) {
		if (tableExpand == null)
			return null;
		return (tableExpand[riga][OLD_CODE]);
	}

	public static String getEcho(String[][] tableExpand, int riga) {
		if (tableExpand == null)
			return null;
		return (tableExpand[riga][ECHO]);
	}

	public static String getNewCode(String[][] tableExpand, int riga) {
		if (tableExpand == null)
			return null;
		return (tableExpand[riga][NEW_CODE]);
	}

	public static String getImaPass(String[][] tableExpand, int riga) {
		if (tableExpand == null)
			return null;
		return (tableExpand[riga][IMA_PASS]);
	}

	public static String getImaTotal(String[][] tableExpand, int riga) {
		if (tableExpand == null)
			return null;
		return (tableExpand[riga][IMA_TOTAL]);
	}

	public static String getPluginName(String[][] tableExpand, int riga) {
		if (tableExpand == null)
			return null;
		return (tableExpand[riga][PLUGIN]);
	}

}
