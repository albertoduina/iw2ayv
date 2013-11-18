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

public class TableCode {

	public static int CODE = 0;

	public static int IMA_PASS = 1;

	public static int IMA_TOTAL = 2;

	public static int COIL = 3;

	public static int DIREZ = 4;

	public static int PROFOND = 5;

	public static int PLUGIN = 6;

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
//		MyLog.logMatrix(sumTableCode, "sumTableCode");
//		MyLog.waitHere();
		return sumTableCode;
	}

	// public static String[] loadTableUnSaccoBello() {
	// boolean absolute = false;
	//
	// // URL url1 = new InputOutput().getClass().getClassLoader()
	// // .getResource(name);
	//
	// // JarFile file2 = new JarFile(Con)
	//
	// URL url1 = new InputOutput().getClass().getClassLoader()
	// .getResource("ControlliDoNotRemove.txt");
	// String path1 = url1.toString();
	//
	// int pos = path1.indexOf("!");
	// MyLog.waitHere("path1= " + path1+ " pos= "+pos);
	// String path2 = path1.substring(0, pos );
	// MyLog.waitHere("path2= " + path2);
	// URL url2 = (URL) path2;
	//
	// // CodeSource src = new InputOutput().getClass().getClassLoader()
	// // .getResource("Sequenze_.class").getProtectionDomain().getCodeSource();
	// List<String> list = new ArrayList<String>();
	// // if (src != null) {
	// // URL jar = src.getLocation();
	// try {
	// ZipInputStream zip = new ZipInputStream(path2.openStream());
	// ZipEntry ze = null;
	// while ((ze = zip.getNextEntry()) != null) {
	// String entryName = ze.getName();
	// // if (entryName.startsWith("p")
	// // if ( entryName.endsWith(".csv")) {
	// list.add(entryName);
	// // }
	//
	// }
	//
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// String[] vetNames = new String[list.size()];
	// list.toArray(vetNames);
	// IJ.log("" + url1);
	// MyLog.logVector(vetNames, "vetNames");
	// MyLog.waitHere("list.size= " + list.size());
	//
	// return null;
	// }

	// public static String[][] loadTableCSV(String path) {
	//
	// boolean absolute=false;
	// String[][] tableCode1 = new InputOutput().readFile6LIKE(path, absolute);
	// String[][] tableCode = InputOutput.removeColumn(tableCode1, 1);
	//
	// return tableCode;
	// }

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
