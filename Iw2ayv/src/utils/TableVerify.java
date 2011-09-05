package utils;

public class TableVerify {

	public static final int CODE = 0;

	public static final int IMA_REQUIRED = 1;

	public static final int IMA_FOUND = 2;

	public static final int PATH = 3;

	public static final int SERIE = 4;

	public static final int ACQ = 5;

	public static final int IMA = 6;

	public static final int COIL = 7;

	public static String getCode(String[][] tableVerify, int riga) {
		if (tableVerify == null)
			return null;
		return (tableVerify[riga][CODE]);
	}

	
	public static String getImaRequired(String[][] tableVerify, int riga) {
		if (tableVerify == null)
			return null;
		return (tableVerify[riga][IMA_REQUIRED]);
	}

	public static String getImaFound(String[][] tableVerify, int riga) {
		if (tableVerify == null)
			return null;
		return (tableVerify[riga][IMA_FOUND]);
	}

	public static String getPath(String[][] tableVerify, int riga) {
		if (tableVerify == null)
			return null;
		return (tableVerify[riga][PATH]);
	}

	public static String getSerie(String[][] tableVerify, int riga) {
		if (tableVerify == null)
			return null;
		return (tableVerify[riga][SERIE]);
	}

	public static String getAcq(String[][] tableVerify, int riga) {
		if (tableVerify == null)
			return null;
		return (tableVerify[riga][ACQ]);
	}

	public static String getIma(String[][] tableVerify, int riga) {
		if (tableVerify == null)
			return null;
		return (tableVerify[riga][IMA]);
	}
	public static String getCoil(String[][] tableVerify, int riga) {
		if (tableVerify == null)
			return null;
		return (tableVerify[riga][COIL]);
	}

}
