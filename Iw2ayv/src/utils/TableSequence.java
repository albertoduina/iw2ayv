package utils;

import utils.TableUtils;
import ij.IJ;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Copyright (C) 2007 Alberto Duina, SPEDALI CIVILI DI BRESCIA, Brescia ITALY
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

/**
 * E'la tabella contenente i dati di iw2ayv.txt, che vengono utilizzati per
 * automatizzare l'analisi delle immagini
 * 
 * @author Alberto Duina - SPEDALI CIVILI DI BRESCIA - Servizio di Fisica
 *         Sanitaria
 * 
 */

public class TableSequence {

	public static String VERSION = "ListManager-v3.01_28mar07_";

	public static int ROW = 0;

	public static int PATH = 1;

	public static int CODE = 2;

	public static int COIL = 3;

	public static int IMA_PASS = 4;

	public static int SERIE = 5;

	public static int ACQ = 6;

	public static int IMA = 7;

	public static int TIME = 8;

	public static int ECHO = 9;

	public static int DIREZ = 10;

	public static int PROFOND = 11;

	public static int DONE = 12;

	public static int COLUMNS = 13;

	/***
	 * Carica la tabella in memoria leggendo il file su disco
	 * 
	 * @param path
	 *            path del file iw2ayv.txt
	 * @return tabella con i dati
	 */
	public String[][] loadTable(String path) {

		// IJ.log("TableSequence.loadTable riceve=" + path);
		ArrayList<ArrayList<String>> tableArray = new InputOutput()
				.readFile5(path);
		String[][] sequenceTable = new InputOutput()
				.fromArrayListToStringTable(tableArray);

		return sequenceTable;
	}

	/***
	 * Conta le righe del file iw2ayv.txt
	 * 
	 * @param path
	 *            path del file iw2ayv.txt
	 * @return numero di righe
	 */
	public static int countRows(String path) {

		if (path.length() <= 0)
			return (-1);
		// IJ.log("ListManager.contaRighe path=" + path);

		int count = 0;
		try {
			BufferedReader r = new BufferedReader(new FileReader(path));
			while (r.readLine() != null) {
				count++;
			}
			r.close();
		} catch (Exception e) {
			IJ.error(e.getMessage());
			return (-1);
		}
		return (count);

	} // contaRighe

	// ------------------------------------------------------------------------------------------------------

	public static String getRow(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return (strTabella[riga][ROW]);
	}

	public static String getPath(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;

		return (strTabella[riga][PATH]);
	}

	public static String getCode(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][CODE];
	}

	public static String getCoil(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][COIL];
	}

	public static String getImaPass(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][IMA_PASS];
	}

	public static String getNumSerie(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][SERIE];
	}

	public static String getNumAcq(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][ACQ];
	}

	public static String getNumIma(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][IMA];
	}

	public static String getAcqTime(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][TIME];
	}

	public static String getEcho(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][ECHO];
	}

	public static String getDirez(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][DIREZ];
	}

	public static String getProfond(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][PROFOND];
	}

	public static String getDone(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][DONE];
	}

	public static int getLength(String[][] strTabella) {
		if (strTabella == null)
			return 0;
		return strTabella.length;
	}

	public void putDone(String[][] strTabella, int riga) {
		int fatto = Integer.parseInt(strTabella[riga][DONE]);
		fatto++;
		strTabella[riga][DONE] = "" + fatto;
		return;
	}

	/***
	 * Scrive un vettore in una colonna della tabella
	 * 
	 * @param inTable
	 *            tabella su cui scrivere
	 * @param strData
	 *            vettore coi dati
	 * @param column
	 *            numero della colonna su cui scrivere
	 * @return tabella
	 */
	public static String[][] writeColumn(String[][] inTable, String[] strData,
			int column) {
		if (inTable == null)
			return null;
		if (strData == null)
			return null;
		String[][] outTable = new TableUtils().duplicateTable(inTable);
		for (int i1 = 0; i1 < strData.length; i1++) {
			outTable[i1][column] = strData[i1];
		}
		return outTable;
	}

	/***
	 * Crea una tabella vuota
	 * 
	 * @param righe
	 *            numero di righe
	 * @param colonne
	 *            numero di colonne
	 * @return tabella vuota
	 */
	public static String[][] createEmptyTable(int righe, int colonne) {
		String[][] tableVuota = new String[righe][colonne];
		return tableVuota;
	}

	/**
	 * Scrive la tabella su disco nel file iw2ayv.txt
	 * 
	 * @param path
	 *            path su cui scrivere
	 * @param strTabella
	 *            tabella da scrivere
	 * @return true se scritto con successo
	 */
	public boolean writeTable(String path, String[][] strTabella) {
		int j1 = 0;
		String rigaCompleta = "";
		if (strTabella == null) {
			MyLog.waitHere("scriviTabella strTabella null");
			return false;
		}
		if (strTabella[ROW].length != 13) {
			MyLog.waitHere("scriviTabella Error on strTabella length");
			return false;
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path));
			if (bw != null) {
				while (j1 < strTabella.length) {
					rigaCompleta = "R#" + strTabella[j1][ROW] + "#FILE#"
							+ strTabella[j1][PATH] + "#COD#"
							+ strTabella[j1][CODE] + "#COIL#"
							+ strTabella[j1][COIL] + "#PASS#"
							+ strTabella[j1][IMA_PASS] + "#SER#"
							+ strTabella[j1][SERIE] + "#ACQ#"
							+ strTabella[j1][ACQ] + "#IMA#"
							+ strTabella[j1][IMA] + "#TIME#"
							+ strTabella[j1][TIME] + "#ECHO#"
							+ strTabella[j1][ECHO] + "#DIREZ#"
							+ strTabella[j1][DIREZ] + "#PROFOND#"
							+ strTabella[j1][PROFOND] + "#DONE#"
							+ strTabella[j1][DONE] + "#\n";
					j1++;
					bw.write(rigaCompleta, 0, rigaCompleta.length());
				}
				bw.close();
			}
			return true;
		} catch (IOException e) {
			IJ.showMessage("Error on Save As! ");
			return false;
		}
	}

}
