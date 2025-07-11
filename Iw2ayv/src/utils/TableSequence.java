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
 * 
 * NOTA_BENE TableSequenze viene chiamato in tutti i plugins di ConMensili.
 * Conviene lasciarla sotto forma si String[][] ed effettuare le opportune
 * convesioni durante le comparazioni
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

	public static int IMA_ORDER = 5;

	public static int IMA_INCREMENT = 6;

	public static int MULTIPLI = 7;

	public static int SPARE_2 = 8;

	public static int SPARE_3 = 9;

	public static int SERIE = 10;

	public static int ACQ = 11;

	public static int IMA = 12;

	public static int TIME = 13;

	public static int ECHO = 14;

	public static int POSIZ = 15;

	public static int DIREZ = 16;

	public static int PROFOND = 17;

	public static int DONE = 18;

	public static int columns0 = 19;

	/***
	 * Carica la tabella in memoria leggendo il file su disco
	 * 
	 * @param path path del file iw2ayv.txt
	 * @return tabella con i dati
	 */
	public String[][] loadTable(String path) {

		// IJ.log("TableSequence.loadTable riceve=" + path);
		ArrayList<ArrayList<String>> tableArray = new InputOutput().readFile5LIKE(path, true);

//		MyLog.logArrayListTable2(tableArray, "titolo");
//		MyLog.waitHere();

		String[][] sequenceTable = new InputOutput().fromArrayListToStringTable(tableArray);

//		MyLog.logMatrixTable(sequenceTable, "SeqTableNeonata");
//		MyLog.waitHere();

		return sequenceTable;
	}

	public static boolean checkWidth(String[][] table) {
		IJ.log("table.length= " + table.length);
		IJ.log("table.length[1]= " + table[1].length);

		if (table[0].length == columns0) {
			return true;
		} else {
			MyLog.waitHere("errore larghezza iw2ayvTable = " + table[0].length + " anziche' " + columns0);
			return false;
		}
	}

	/***
	 * Conta le righe del file iw2ayv.txt
	 * 
	 * @param path path del file iw2ayv.txt
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

	public static String getImaGroup(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][IMA_ORDER];
	}

	public static String getImaIncrement(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][IMA_INCREMENT];
	}

	public static String getMultipli(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][MULTIPLI];
	}

	public static String getSpare2(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][SPARE_2];
	}

	public static String getSpare3(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][SPARE_3];
	}

	public static String getSerie(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][SERIE];
	}

	public static String getAcq(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][ACQ];
	}

	public static String getIma(String[][] strTabella, int riga) {
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

	public static String getPosiz(String[][] strTabella, int riga) {
		if (strTabella == null)
			return null;
		return strTabella[riga][POSIZ];
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

	public static String getKey(String[][] strTabella, int riga, int key) {
		if (strTabella == null)
			return null;
		return strTabella[riga][key];
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
	 * @param inTable tabella su cui scrivere
	 * @param strData vettore coi dati
	 * @param column  numero della colonna su cui scrivere
	 * @return tabella
	 */
	public static String[][] writeColumn(String[][] inTable, String[] strData, int column) {
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
	 * @param righe   numero di righe
	 * @param colonne numero di colonne
	 * @return tabella vuota
	 */
	public static String[][] createEmptyTable(int righe, int colonne) {
		String[][] tableVuota = new String[righe][colonne];
		return tableVuota;
	}

	/**
	 * Scrive la tabella su disco nel file iw2ayv.txt
	 * 
	 * @param path       path su cui scrivere
	 * @param strTabella tabella da scrivere
	 * @return true se scritto con successo
	 */
	public boolean writeTable(String path, String[][] strTabella) {
		int j1 = 0;
		String rigaCompleta = "";
		if (strTabella == null) {
			MyLog.waitHere("scriviTabella strTabella null");
			return false;
		}
		if (strTabella[ROW].length != columns0) {
			MyLog.waitHere(
					"scriviTabella Error on strTabella length " + strTabella[ROW].length + " invece di " + columns0);
			return false;
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path));
			if (bw != null) {
				while (j1 < strTabella.length) {
					rigaCompleta = "R#" + strTabella[j1][ROW] + "#FILE#" + strTabella[j1][PATH] + "#COD#"
							+ strTabella[j1][CODE] + "#COIL#" + strTabella[j1][COIL] + "#PASS#"
							+ strTabella[j1][IMA_PASS] + "#ORD#" + strTabella[j1][IMA_ORDER] + "#INCR#"
							+ strTabella[j1][IMA_INCREMENT] + "#SP1#" + strTabella[j1][MULTIPLI] + "#SP2#"
							+ strTabella[j1][SPARE_2] + "#SP3#" + strTabella[j1][SPARE_3] + "#SER#"
							+ strTabella[j1][SERIE] + "#ACQ#" + strTabella[j1][ACQ] + "#IMA#" + strTabella[j1][IMA]
							+ "#TIME#" + strTabella[j1][TIME] + "#ECHO#" + strTabella[j1][ECHO] + "#POSIZ#"
							+ strTabella[j1][POSIZ] + "#DIREZ#" + strTabella[j1][DIREZ] + "#PROF#"
							+ strTabella[j1][PROFOND] + "#DONE#" + strTabella[j1][DONE] + "#\n";
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
