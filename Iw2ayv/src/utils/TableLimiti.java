package utils;

import java.util.ArrayList;

public class TableLimiti {

	public static int NOME = 0;


	public static String[][] loadTable(String path) {

		ArrayList<ArrayList<String>> tableArray = new InputOutput()
				.readFile3LIKE(path);
		String[][] tableCode = new InputOutput()
				.fromArrayListToStringTable(tableArray);

		return tableCode;
	}

	public static String[][] loadTableCSV(String path) {
		boolean absolute= false;
		String[][] tableCode = new InputOutput().readFile6LIKE(path, absolute);
		
		return tableCode;
	}

	

	
	public static String getNome(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][NOME]);
	}

	public static String getVal1(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][1]);
	}

	public static String getVal2(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][2]);
	}

	public static String getVal3(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][3]);
	}

	public static String getVal4(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][4]);
	}

	public static String getVal5(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][5]);
	}

	public static String getVal6(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][6]);
	}
	public static String getVal7(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][7]);
	}
	public static String getVal8(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][8]);
	}
	public static String getVal9(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][9]);
	}
	public static String getVal10(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][10]);
	}
	public static String getVal11(String[][] tableCode, int riga) {
		if (tableCode == null)
			return null;
		return (tableCode[riga][11]);
	}
	
	
}
