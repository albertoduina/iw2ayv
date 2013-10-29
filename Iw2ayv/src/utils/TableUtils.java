package utils;

import ij.IJ;

public class TableUtils {

	public static void dumpTable(String[][] strTabella) {

		if (strTabella == null) {
			IJ.log("dumpTable.strTabella == Null");
			return;
		}
		if (strTabella.length == 0) {
			IJ.log("dumpTable.strTabella.length() == 0");
			return;
		}
		IJ.log("---------------------------");
		String str = "";
		for (int j1 = 0; j1 < strTabella.length; j1++) {
			for (int j2 = 0; j2 < strTabella[0].length; j2++) {
				str = str + strTabella[j1][j2] + " ";
			}
			IJ.log(str);
			str = "";
		}
		IJ.log("---------------------------");
	}

	public static void dumpTable(int[][] strTabella) {

		if (strTabella == null) {
			IJ.log("dumpTable.strTabella == Null");
			return;
		}
		if (strTabella.length == 0) {
			IJ.log("dumpTable.strTabella.length() == 0");
			return;
		}
		IJ.log("---------------------------");
		String str = "";
		for (int j1 = 0; j1 < strTabella.length; j1++) {
			for (int j2 = 0; j2 < strTabella[0].length; j2++) {
				str = str + strTabella[j1][j2] + " ";
			}
			IJ.log(str);
			str = "";
		}
		IJ.log("---------------------------");
	}

	public static void dumpTable(String[][] strTabella, String title) {

		if (strTabella == null) {
			IJ.log("dumpTable.strTabella " + title + " == Null");
			return;
		}
		if (strTabella.length == 0) {
			IJ.log("dumpTable.strTabella." + title + ".length() == 0");
			return;
		}
		IJ.log("----- \t" + title + " " + strTabella.length + "x"
				+ strTabella[0].length + "\t --------");
		String str = "";
		for (int j1 = 0; j1 < strTabella.length; j1++) {
			for (int j2 = 0; j2 < strTabella[0].length; j2++) {
				str = str + strTabella[j1][j2] + " ";
			}
			IJ.log(str);
			str = "";
		}
		IJ.log("---------------------------");
	}

	public static void dumpTableRow(String[][] strTabella, int row) {

		if (strTabella == null) {
			IJ.log("dumpTable.strTabella  == Null");
			return;
		}
		if (strTabella.length == 0) {
			IJ.log("dumpTable.strTabella.length() == 0");
			return;
		}
		IJ.log("-------------");
		String str = "";
		for (int j1 = 0; j1 < strTabella[0].length; j1++) {
			str = str + strTabella[row][j1] + "#";
		}
		IJ.log(str);
		IJ.log("---------------------------");
	}

	/**
	 * 
	 * @param dblTabella1
	 * @param dblTabella2
	 * @return
	 */
	
	public static boolean compareTable(double[][] dblTabella1,
			double[][] dblTabella2) {

		if (dblTabella1 == null) {
			IJ.error("compareTable table1= null");
			return false;
		}
		if (dblTabella2 == null) {
			IJ.error("compareTable table2= null");
			return false;
		}
		if (dblTabella1.length != dblTabella2.length) {
			IJ.error("compareTable different length 1=" + dblTabella1.length
					+ " 2=" + dblTabella2.length);
			return false;
		}
		if (dblTabella1[0].length != dblTabella2[0].length) {
			IJ.error("compareTable different width 1=" + dblTabella1[0].length
					+ " 2=" + dblTabella2[0].length);
			return false;
		}

		for (int j1 = 0; j1 < dblTabella1.length; j1++) {
			for (int j2 = 0; j2 < dblTabella1[0].length; j2++) {
				if (!(dblTabella1[j1][j2]==(dblTabella2[j1][j2]))) {
					IJ.error("compareTable different elements at row=" + j1
							+ " column=" + j2);
					return false;
				}
			}
		}
		// se non abbiamo già dato un return false vuol dire che sono identiche
		return true;
	} 

	public static boolean compareTable(float[][] fltTabella1,
			float[][] fltTabella2) {

		if (fltTabella1 == null) {
			IJ.error("compareTable table1= null");
			return false;
		}
		if (fltTabella2 == null) {
			IJ.error("compareTable table2= null");
			return false;
		}
		if (fltTabella1.length != fltTabella2.length) {
			IJ.error("compareTable different length 1=" + fltTabella1.length
					+ " 2=" + fltTabella2.length);
			return false;
		}
		if (fltTabella1[0].length != fltTabella2[0].length) {
			IJ.error("compareTable different width 1=" + fltTabella1[0].length
					+ " 2=" + fltTabella2[0].length);
			return false;
		}

		for (int j1 = 0; j1 < fltTabella1.length; j1++) {
			for (int j2 = 0; j2 < fltTabella1[0].length; j2++) {
				if (!(fltTabella1[j1][j2]==(fltTabella2[j1][j2]))) {
					IJ.error("compareTable different elements at row=" + j1
							+ " column=" + j2);
					return false;
				}
			}
		}
		// se non abbiamo già dato un return false vuol dire che sono identiche
		return true;
	} 

	
	public static boolean compareTable(int[][] intTabella1,
			int[][] intTabella2) {

		if (intTabella1 == null) {
			IJ.error("compareTable table1= null");
			return false;
		}
		if (intTabella2 == null) {
			IJ.error("compareTable table2= null");
			return false;
		}
		if (intTabella1.length != intTabella2.length) {
			IJ.error("compareTable different length 1=" + intTabella1.length
					+ " 2=" + intTabella2.length);
			return false;
		}
		if (intTabella1[0].length != intTabella2[0].length) {
			IJ.error("compareTable different width 1=" + intTabella1[0].length
					+ " 2=" + intTabella2[0].length);
			return false;
		}

		for (int j1 = 0; j1 < intTabella1.length; j1++) {
			for (int j2 = 0; j2 < intTabella1[0].length; j2++) {
				if (!(intTabella1[j1][j2]==(intTabella2[j1][j2]))) {
					IJ.error("compareTable different elements at row=" + j1
							+ " column=" + j2);
					return false;
				}
			}
		}
		// se non abbiamo già dato un return false vuol dire che sono identiche
		return true;
	} 
	
	
	
	public static boolean compareTable(String[][] strTabella1,
			String[][] strTabella2) {

		if (strTabella1 == null) {
			IJ.error("compareTable table1= null");
			return false;
		}
		if (strTabella2 == null) {
			IJ.error("compareTable table2= null");
			return false;
		}
		if (strTabella1.length != strTabella2.length) {
			IJ.error("compareTable different length 1=" + strTabella1.length
					+ " 2=" + strTabella2.length);
			return false;
		}
		if (strTabella1[0].length != strTabella2[0].length) {
			IJ.error("compareTable different width 1=" + strTabella1[0].length
					+ " 2=" + strTabella2[0].length);
			return false;
		}

		for (int j1 = 0; j1 < strTabella1.length; j1++) {
			for (int j2 = 0; j2 < strTabella1[0].length; j2++) {
				if (!(strTabella1[j1][j2].equals(strTabella2[j1][j2]))) {
					IJ.error("compareTable different elements at row=" + j1
							+ " column=" + j2);
					return false;
				}
			}
		}
		// se non abbiamo già dato un return false vuol dire che sono identiche
		return true;
	} 

	/**
	 * Table duplication
	 * 
	 * @param inTable
	 *            Table input
	 * @return Table duplicate
	 */
	public String[][] duplicateTable(String[][] inTable) {
		if (inTable == null)
			return null;
		if (inTable.length == 0)
			return null;
		String[][] outTable = new String[inTable.length][inTable[0].length];
		for (int i1 = 0; i1 < inTable.length; i1++) {
			for (int i2 = 0; i2 < inTable[0].length; i2++) {
				outTable[i1][i2] = inTable[i1][i2];
			}
		}
		return outTable;
	}

	public static double[][] rotateTable(double[][] inTable) {
		int dim1 = inTable.length;
		int dim2 = inTable[0].length;
		int count = 0;
		double aux1 = 0;
		double[][] outTable = new double[dim2][dim1];
		for (int i1 = 0; i1 < dim1; i1++) {
			count = 0;
			for (int i2 = 0; i2 < dim2; i2++) {
				aux1 = inTable[i1][i2];
				outTable[count][i1] = aux1;
				count++;
			}
		}
		return outTable;
	}
	
	public static float[][] rotateTable(float[][] inTable) {
		int dim1 = inTable.length;
		int dim2 = inTable[0].length;
		int count = 0;
		float aux1 = 0;
		float[][] outTable = new float[dim2][dim1];
		for (int i1 = 0; i1 < dim1; i1++) {
			count = 0;
			for (int i2 = 0; i2 < dim2; i2++) {
				aux1 = inTable[i1][i2];
				outTable[count][i1] = aux1;
				count++;
			}
		}
		return outTable;
	}

	
	public static int[][] rotateTable(int[][] inTable) {
		int dim1 = inTable.length;
		int dim2 = inTable[0].length;
		int count = 0;
		int aux1 = 0;
		int[][] outTable = new int[dim2][dim1];
		for (int i1 = 0; i1 < dim1; i1++) {
			count = 0;
			for (int i2 = 0; i2 < dim2; i2++) {
				aux1 = inTable[i1][i2];
				outTable[count][i1] = aux1;
				count++;
			}
		}
		return outTable;
	}

	
	public static String[][] rotateTable(String[][] inTable) {
		int dim1 = inTable.length;
		int dim2 = inTable[0].length;
		int count = 0;
		String aux1 = "";
		String[][] outTable = new String[dim2][dim1];
		for (int i1 = 0; i1 < dim1; i1++) {
			count = 0;
			for (int i2 = 0; i2 < dim2; i2++) {
				aux1 = inTable[i1][i2];
				outTable[count][i1] = aux1;
				count++;
			}
		}
		return outTable;
	}


}
