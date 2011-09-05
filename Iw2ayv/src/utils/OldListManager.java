package utils;


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
 * Fornisce il supporto per leggere/scrivere la tabella dal file iw2ayv.txt
 * 
 * @author Alberto Duina - SPEDALI CIVILI DI BRESCIA - Servizio di Fisica
 *         Sanitaria
 * 
 */
public class OldListManager {

	public static String VERSION = "ListManager-v3.01_28mar07_";

//	public String[][] caricaTabella(String path) {
//
//		
//		//
//		// leggo ora riga per riga il file,
//		// creo un array a due dimensioni che conterrà le informazioni:
//		// RIGA strTabella [n] [0]
//		// FILE strTabella [n] [1] nome del file immagine completo di path
//		// CODICE strTabella [n] [2] codice della sequenza indicante il tipo di
//		// controllo
//		// PASS strTabella [n] [3] numero di immagi da passare al plugin
//		// SERIE strTabella [n] [4] numSerie= ReadPara2(imp1,"0020,0011");
//		// ACQ strTabella [n] [5] numAcq= ReadPara2(imp1,"0020,0012");
//		// IMA strTabella [n] [6] numIma= ReadPara2(imp1,"0020,0013");
//		// TIME strTabella [n] [7] imageTime= ReadPara2(imp1,"0008,0033");
//		// ECHO strTabella [n] [8] echoTime= ReadPara2(imp1,"0018,0081");
//		// FATTO strTabella [n] [9] default 0, indica con 1 se i calcoli sono
//		// già stati eseguiti
//		//
//		int count2 = 0;
//		int riga2 = 0;
//		int riga3 = 0;
//		int tokensExpected = 20;
//		if (contaRighe(path)<=0) return null;
//		
//		String[][] strTabella = new String[contaRighe(path)][10];
//
//		try {
//			LineNumberReader r = new LineNumberReader(new FileReader(path));
//			String rigaCompleta;
//			while ((rigaCompleta = r.readLine()) != null) {
//				riga2 = r.getLineNumber();
//				StringTokenizer parser = new StringTokenizer(rigaCompleta, "#");
//				int nTokens = parser.countTokens();
//				if (nTokens == tokensExpected) {
//					parser.nextToken();
//					strTabella[count2][0] = parser.nextToken();
//					riga3 = Integer.parseInt(strTabella[count2][0]);
//					parser.nextToken();
//					strTabella[count2][1] = parser.nextToken();
//					parser.nextToken();
//					strTabella[count2][2] = parser.nextToken();
//					parser.nextToken();
//					strTabella[count2][3] = parser.nextToken();
//					parser.nextToken();
//					strTabella[count2][4] = parser.nextToken();
//					parser.nextToken();
//					strTabella[count2][5] = parser.nextToken();
//					parser.nextToken();
//					strTabella[count2][6] = parser.nextToken();
//					parser.nextToken();
//					strTabella[count2][7] = parser.nextToken();
//					parser.nextToken();
//					strTabella[count2][8] = parser.nextToken();
//					parser.nextToken();
//					strTabella[count2][9] = parser.nextToken();
//					count2++;
//					if (riga2 != (riga3 + 1)) {
//						IJ.write("1 caricaTabella ERRORE DECODIFICA RIGA   "
//								+ (count2 + 1));
//						return (null);
//					}
//
//				} else {
//					IJ.write("2 caricaTabella ERRORE DECODIFICA RIGA   "
//							+ (count2 + 1));
//					return (null);
//				}
//			}
//
//			r.close();
//
//		} catch (IOException e) {
//			IJ.error("" + e);
//			return (null);
//		}
//		return (strTabella);
//	} // listReader
//
//	// ------------------------------------------------------------------------------------------------------
//
//	public int contaRighe(String path) {
//
//		if (path.length() <= 0)
//			return (-1);
//		// IJ.log("ListManager.contaRighe path=" + path);
//
//		int count = 0;
//		try {
//			BufferedReader r = new BufferedReader(new FileReader(path));
//			while (r.readLine() != null) {
//				count++;
//			}
//			r.close();
//		} catch (Exception e) {
//			IJ.error(e.getMessage());
//			return (-1);
//		}
//		return (count);
//
//	} // contaRighe
//
//	// ------------------------------------------------------------------------------------------------------
//
//	public void dumpaTabella(String[][] strTabella) {
//
//		if (strTabella.length == 0)
//			return;
//		IJ.log("---------------------------");
//		String str = "";
//		for (int j1 = 0; j1 < strTabella.length; j1++) {
//			for (int j2 = 0; j2 < strTabella[0].length; j2++) {
//				str = str + strTabella[j1][j2] + " ";
//			}
//			IJ.log(str);
//			str = "";
//		}
//		IJ.log("---------------------------");
//	} // dumpaTabella
//
//	public void dumpaTabella(String[][] strTabella, String title) {
//
//		if (strTabella.length == 0)
//			return;
//		IJ.log("----- \t" + title + "\t --------");
//		String str = "";
//		for (int j1 = 0; j1 < strTabella.length; j1++) {
//			for (int j2 = 0; j2 < strTabella[0].length; j2++) {
//				str = str + strTabella[j1][j2] + " ";
//			}
//			IJ.log(str);
//			str = "";
//		}
//		IJ.log("---------------------------");
//	} // dumpaTabella
//
//	/**
//	 * 
//	 * @param strTabella1
//	 * @param strTabella2
//	 * @return
//	 */
//	public boolean compareTable(String[][] strTabella1, String[][] strTabella2) {
//
//		if (strTabella1 == null)
//			return false;
//		if (strTabella2 == null)
//			return false;
//		if (strTabella1.length != strTabella2.length)
//			return false;
//
//		for (int j1 = 0; j1 < strTabella1.length; j1++) {
//			for (int j2 = 0; j2 < strTabella1[0].length; j2++) {
//				if (!(strTabella1[j1][j2].equals(strTabella2[j1][j2])))
//					return false;
//			}
//		}
//		// se non abbiamo già dato un return false vuol dire che sono identiche
//		return true;
//	} // compareTable
//
//	public String getPath(String[][] strTabella, int riga) {
//		return (strTabella[riga][1]);
//	}
//
//	public String getCode(String[][] strTabella, int riga) {
//		return strTabella[riga][2];
//	}
//
//	public int getFatto(String[][] strTabella, int riga) {
//		return Integer.parseInt(strTabella[riga][9]);
//	}
//
//	/**
//	 * Scrive su file una tabella La struttura della mia tabella sarà la
//	 * seguente:
//	 * 
//	 * RIGA strTabella [n][0] numero di riga, è il valore n
//	 * 
//	 * FILE strTabella [n][1] nome del file immagine completo di path
//	 * 
//	 * CODICE strTabella [n][2] codice della sequenza indicante il tipo di
//	 * controllo
//	 * 
//	 * PASS strTabella [n][3] numero di immagini da passare al plugin
//	 * 
//	 * SERIE strTabella [n][4] numSerie= ReadPara2(imp1,"0020,0011")
//	 * 
//	 * ACQ strTabella [n][5] numAcq= ReadPara2(imp1,"0020,0012")
//	 * 
//	 * IMA strTabella [n][6] numIma= ReadPara2(imp1,"0020,0013")
//	 * 
//	 * TIME strTabella [n][7] imageTime= ReadPara2(imp1,"0008,0033")
//	 * 
//	 * ECHO strTabella [n][8] echoTime= ReadPara2(imp1,"0018,0081")
//	 * 
//	 * FATTO strTabella [n][9] default 0, indica con 1 se i calcoli sono già
//	 * stati eseguiti
//	 * 
//	 * @param path
//	 *            path del file da scrivere
//	 * @param tabella
//	 *            dati
//	 * 
//	 */
//	public boolean scriviTabella(String path, String[][] strTabella) {
//		int j1 = 0;
//		String rigaCompleta = "";
//		if (strTabella == null)
//			return false;
//		try {
//			BufferedWriter bw = new BufferedWriter(new FileWriter(path));
//			if (bw != null) {
//				while (j1 < strTabella.length) {
//					rigaCompleta = "R#" + strTabella[j1][0] + "#FILE#"
//							+ strTabella[j1][1] + "#COD#" + strTabella[j1][2]
//							+ "#PASS#" + strTabella[j1][3] + "#SER#"
//							+ strTabella[j1][4] + "#ACQ#" + strTabella[j1][5]
//							+ "#IMA#" + strTabella[j1][6] + "#TIME#"
//							+ strTabella[j1][7] + "#ECHO#" + strTabella[j1][8]
//							+ "#DONE#" + strTabella[j1][9] + "#\n";
//					j1++;
//					bw.write(rigaCompleta, 0, rigaCompleta.length());
//				}
//				bw.close();
//			}
//			return true;
//		} catch (IOException e) {
//			IJ.showMessage("Error on Save As! ");			
//			return false;
//		}
//	} // chiude scrivi
//
//	public void putFatto(String[][] strTabella, int riga) {
//		int fatto = Integer.parseInt(strTabella[riga][9]);
//		fatto++;
//		strTabella[riga][9] = "" + fatto;
//		return;
//	}

} // ListManager
