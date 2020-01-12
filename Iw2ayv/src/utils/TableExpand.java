package utils;

import ij.IJ;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TableExpand {

	public static int OLD_CODE = 0;

	public static int ECHO = 1;

	public static int NEW_CODE = 2;

	public static int IMA_PASS = 3;

	public static int IMA_TOTAL = 4;

	public static int PLUGIN = 5;
	
	

	public String[][] loadTableNew(String part1, String part2) {
		String target_file; // fileThatYouWantToFilter
		List<String> list1 = new ArrayList<String>();
		URL url3 = this.getClass().getClassLoader().getResource("contMensili/Sequenze_.class");
		String myString = url3.toString();
		int start = myString.indexOf("plugins");
		int end = myString.lastIndexOf("!");
		String myPart1 = myString.substring(start, end);
		end = myPart1.lastIndexOf("/");
		String myPart2 = myPart1.substring(0, end + 1);

		// MyLog.waitHere("myString= " + myString + " myPart1= " + myPart1 + " myPart2=
		// " + myPart2);

		File folderToScan = new File(myPart2);

		File[] listOfFiles = folderToScan.listFiles();
		// IJ.log("length= " + listOfFiles.length);

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
		
		
		/// qui DEVO avere un solo file, altrimenti qualcosa non va bene.
		

//		MyLog.waitHere();
		String[] list2 = ArrayUtils.arrayListToArrayString(list1);
		if (list2.length == 1 ) {} else {
			MyLog.waitHere("ATTENZIONE: esiste piu' di un file col nome che inizia con 'EXPAND' ed estensione '.CSV', cio' non deve succedere MAI, MAI, MAIIIII");	
		}
			
		// MyLog.logVector(list2, "list2");
		// MyLog.waitHere();
		String[][] table1 = loadTable(list2[0]);
		return table1;
	}
	
	/**
	 * Estrae il nome del primo file contenente la tabella.
	 * Introdotto per poter vedere che versione di expandxxx.csv viene utilizzata dall'utente

	 * @param part1
	 * @param part2
	 * @return
	 */
	
	public  String nameTable(String part1, String part2) {
		String target_file; // fileThatYouWantToFilter
		List<String> list1 = new ArrayList<String>();
		URL url3 = this.getClass().getClassLoader().getResource("contMensili/Sequenze_.class");
		String myString = url3.toString();
		int start = myString.indexOf("plugins");
		int end = myString.lastIndexOf("!");
		String myPart1 = myString.substring(start, end);
		end = myPart1.lastIndexOf("/");
		String myPart2 = myPart1.substring(0, end + 1);

		// MyLog.waitHere("myString= " + myString + " myPart1= " + myPart1 + " myPart2=
		// " + myPart2);

		File folderToScan = new File(myPart2);

		File[] listOfFiles = folderToScan.listFiles();
		// IJ.log("length= " + listOfFiles.length);

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
		
		String[] list2 = ArrayUtils.arrayListToArrayString(list1);
		String nome1 = list2[0];
		return nome1;
	}

	

//	public static String[][] loadTable(String path) {
//		ArrayList<ArrayList<String>> tableArray = new InputOutput()
//				.readFile3LIKE(path);
//		String[][] tableExpand = new InputOutput()
//				.fromArrayListToStringTable(tableArray);
//		return tableExpand;
//	}

	public static String[][] loadTable(String path) {
		boolean absolute=false;
		String[][] tableExpand = new InputOutput().readFile6LIKE(path, absolute);
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
