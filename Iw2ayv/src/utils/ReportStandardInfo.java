package utils;

import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import ij.measure.ResultsTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportStandardInfo {

	// private final static String DICOM_SERIES_DESCRIPTION = "0008,103E";
	//
	// private final static String DICOM_ACQUISITION_DATE = "0008,0022";
	//
	// private final static String DICOM_STATION_NAME = "0008,1010";
	//
	// private final static String DICOM_PATIENT_NAME = "0010,0010";
	//
	// private final static String DICOM_COIL = "0051,100F";

	private static String[] simpleHeader = { "none", "none", "none", "none", "none", "none", "none", "<END>" };

	/**
	 * legge un gruppo di informazioni che verranno inserite nella ResultsTable
	 * 
	 * @param tabImmagini
	 *            tabella coi valori di iw2ayv.txt
	 * @param riga
	 *            numero di riga passato da sequenze
	 * @param tabCodici
	 *            tabella coi valori di codici.txt
	 * @param version
	 *            nome del plugin e sua versione
	 * @param called
	 *            true se il plugin � stato chiamato da sequenze
	 * @return gruppo informazioni per la ResultsTable
	 */
	public static String[][] getStandardInfo(String[][] tabImmagini, int riga, String[][] tabCodici, String version,
			boolean called) {
		String[][] out1 = { { "Codice", "none" }, { "Station", "none" }, { "Patient", "none" }, { "AcqDate", "none" },
				{ "ElabDate", "none" }, { "Coil", "none" }, { "Frequeny", "none" }, { "<END>", "<END>" } };

		if (tabImmagini == null) {
			out1[0][1] = "TEST";
			out1[1][1] = "TEST";
			out1[2][1] = "TEST";
			out1[3][1] = "TEST";
			out1[4][1] = "TEST";
			out1[5][1] = "TEST";
			out1[6][1] = "TEST";
			out1[7][1] = "TEST";
			return (out1);
		}

		//
		// TODO
		// posso ottenere i dati dell'immagine, anzich� partendo dalle tabelle e
		// cazzi e mazzi,
		// semplicemente partendo da imp1 e, per il nome del file andando a
		// leggere le fileInfo
		//
		//

		String path1 = TableSequence.getPath(tabImmagini, riga);
		Opener opener = new Opener();
		ImagePlus imp1 = opener.openImage(path1);
		if (imp1 == null)
			IJ.error("Immagine " + path1 + " inesistente");
		if (imp1 == null)
			return null;

		String aux3 = opener.getName(path1);
		String codice1 = TableSequence.getCode(tabImmagini, riga);
		String codice2 = "";
		if (InputOutput.isCode(aux3, tabCodici))
			codice2 = aux3.substring(0, 5).trim();
		else {
			aux3 = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_SERIES_DESCRIPTION);
			codice2 = aux3.substring(0, 5).trim();
		}
		String codice = "";
		if (called)
			codice = codice1;
		else
			codice = codice2;

		String stationName = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_STATION_NAME);
		String patName = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_PATIENT_NAME);

		String frequency = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_IMAGING_FREQUENCY);

		String[] mesi = { "gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic" };
		// data acquisizione
		String strDay = "none";
		String strMonth = "none";
		String strYear = "none";
		// String acqDate2 = ReadDicom.readDicomParameter(imp1,
		// MyConst.DICOM_ACQUISITION_DATE);

		String acqDate2 = readDate(imp1);

		strDay = acqDate2.substring(6).trim();
		strMonth = mesi[ReadDicom.readInt(acqDate2.substring(4, 6).trim()) - 1];
		strYear = acqDate2.substring(0, 4).trim();
		// u1.ModelessMsg ("Acquisizione "+acqDate2+" Day="+strDay+"
		// Month="+strMonth+" Year="+strYear);
		String acqDate = strDay + "-" + strMonth + "-" + strYear;

		// data elaborazione

		strDay = "none";
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.DAY_OF_MONTH) < 10)
			// strDay="0"+ nowl.getDate();
			strDay = "0" + cal.get(Calendar.DAY_OF_MONTH);
		else
			// strDay=""+ now.getDate();
			strDay = "" + cal.get(Calendar.DAY_OF_MONTH);

		strMonth = "none";
		strMonth = mesi[cal.get(Calendar.MONTH)];
		strYear = "" + (cal.get(Calendar.YEAR));
		// u1.ModelessMsg ("Elaborazione Day="+strDay+" Month="+strMonth+"
		// Year="+strYear);

		String elabDate = strDay + "-" + strMonth + "-" + strYear + "_" + version;

		// String coil1 = ReadDicom.readDicomParameter(imp1, DICOM_COIL);
		String coil = ReadDicom.getFirstCoil(imp1);
		if (coil.equals("MISSING")) {
			coil = new UtilAyv().kludge(path1);
		}

		// ModelessMsg("codice="+codice, "continua");
		out1[0][1] = codice;
		out1[1][1] = stationName;
		out1[2][1] = patName;
		out1[3][1] = acqDate;
		out1[4][1] = elabDate;
		out1[5][1] = coil;
		out1[6][1] = frequency;

		return (out1);
	}

	/**
	 * Lettura di AcqDate di una immagine (Siemens + Philips)
	 * 
	 * @param imp1
	 *            ImagePlus immagine
	 * @return acqDate
	 */
	public static String readDate(ImagePlus imp1) {
		String acqTime = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_ACQUISITION_DATE);
		if (acqTime.equals("MISSING"))
			acqTime = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_IMAGE_DATE);
		return acqTime;
	}

	/**
	 * legge un gruppo di informazioni che verranno inserite nella ResultsTable
	 * 
	 * @param strRiga
	 *            tabella coi valori di iw2ayv.txt
	 * @param imp1
	 *            ImagePlus
	 * @param tabCodici
	 *            tabella coi valori di codici.txt
	 * @param version
	 *            nome del plugin e sua versione
	 * @param called
	 *            true se il plugin � stato chiamato da sequenze
	 * @return gruppo informazioni per la ResultsTable
	 */
	public static String[] getSimpleStandardInfo(String path, ImagePlus imp1, String[][] tabCodici, String version,
			boolean called) {

		if (imp1 == null) {
			IJ.log("getSimpleStandardInfo.imp1 == null");
			return null;
		}
		String aux3 = imp1.getTitle();

		// IJ.log("getSimpleStandardInfo.aux3=" + aux3);
		String codice;
		// 2 possibilities: the first 5 letters of filename are the CODE or
		// if (InputOutput.isCode(aux3.substring(0, 5).trim(), tabCodici))
		if (InputOutput.isCode(UtilAyv.getFiveLetters(aux3).trim(), tabCodici))
			// main possibility: the first 5 letters of the filename are a
			// recognized code (in codici.txt)
			codice = UtilAyv.getFiveLetters(aux3).trim();
		else {
			// or: the code is in the dicomSeriesDescription
			aux3 = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_SERIES_DESCRIPTION);

			codice = UtilAyv.getFiveLetters(aux3).trim();
		}

		String stationName = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_STATION_NAME);
		String patName = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_PATIENT_NAME);
		String frequency = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_IMAGING_FREQUENCY);

		String[] mesi = { "gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic" };
		// acquisitionDate
		// String acqDate2 = ReadDicom.readDicomParameter(imp1,
		// MyConst.DICOM_ACQUISITION_DATE);

		String acqDate2 = readDate(imp1);

		String strDay = acqDate2.substring(6).trim();
		String strMonth = mesi[ReadDicom.readInt(acqDate2.substring(4, 6).trim()) - 1];
		String strYear = acqDate2.substring(0, 4).trim();
		String acqDate = strDay + "-" + strMonth + "-" + strYear;

		// elaboration date
		String strDay2 = "";
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.DAY_OF_MONTH) < 10)
			strDay2 = "0" + cal.get(Calendar.DAY_OF_MONTH);
		else
			strDay2 = "" + cal.get(Calendar.DAY_OF_MONTH);

		String strMonth2 = mesi[cal.get(Calendar.MONTH)];
		String strYear2 = "" + (cal.get(Calendar.YEAR));
		String elabDate = strDay2 + "-" + strMonth2 + "-" + strYear2 + "_" + version;
		// String coil = UtilAyv.getFirstCoil(ReadDicom.readDicomParameter(imp1,
		// DICOM_COIL));

		aux3 = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_SERIES_DESCRIPTION);
		String[] reqCoil = getRequestedCoil2(UtilAyv.getFiveLetters(aux3).trim(), tabCodici);

		String coil = ReadDicom.getThisCoil(imp1, reqCoil);
		if (coil == null) {
			coil = "null";
			MyLog.waitHere("coil = null");
		}

		if (coil.equals("MISSING")) {
			coil = new UtilAyv().kludge(path);
		}

		// ModelessMsg("codice="+codice, "continua");
		simpleHeader[0] = codice;
		simpleHeader[1] = stationName;
		simpleHeader[2] = patName;
		simpleHeader[3] = acqDate;
		simpleHeader[4] = elabDate;
		simpleHeader[5] = coil;
		simpleHeader[6] = frequency;
		// MyLog.logVector(simpleHeader, "simpleHeader");

		return (simpleHeader);
	}

	/**
	 * legge un gruppo di informazioni che verranno inserite nella ResultsTable
	 * 
	 * @param strRiga
	 *            tabella coi valori di iw2ayv.txt
	 * @param imp1
	 *            ImagePlus
	 * @param tabCodici
	 *            tabella coi valori di codici.txt
	 * @param version
	 *            nome del plugin e sua versione
	 * @param called
	 *            true se il plugin � stato chiamato da sequenze
	 * @return gruppo informazioni per la ResultsTable
	 */
	public static String[] getSimpleStandardInfo(String path, ImagePlus imp1, String[][] tabCodici,
			String[][] tableSequence, int riga, String version, boolean called) {

		if (imp1 == null) {
			IJ.log("getSimpleStandardInfo.imp1 == null");
			return null;
		}
		String aux3 = imp1.getTitle();

		// String tableCode = TableSequence.getCode(tableSequence, riga);
		String[] reqCoil = ReadDicom.splitCoils(TableSequence.getCoil(tableSequence, riga));

		// IJ.log("getSimpleStandardInfo.aux3=" + aux3);
		String codice;
		// 2 possibilities: the first 5 letters of filename are the CODE or
		// if (InputOutput.isCode(aux3.substring(0, 5).trim(), tabCodici))
		if (InputOutput.isCode(UtilAyv.getFiveLetters(aux3).trim(), tabCodici))
			// main possibility: the first 5 letters of the filename are a
			// recognized code (in codici.txt)
			codice = UtilAyv.getFiveLetters(aux3).trim();
		else {
			// or: the code is in the dicomSeriesDescription
			aux3 = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_SERIES_DESCRIPTION);

			codice = UtilAyv.getFiveLetters(aux3).trim();
		}

		MyLog.waitHere("codice= " + codice);
		String stationName = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_STATION_NAME);
		String patName = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_PATIENT_NAME);
		String frequency = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_IMAGING_FREQUENCY);

		String[] mesi = { "gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic" };
		// acquisitionDate
		// String acqDate2 = ReadDicom.readDicomParameter(imp1,
		// MyConst.DICOM_ACQUISITION_DATE);

		String acqDate2 = readDate(imp1);

		String strDay = acqDate2.substring(6).trim();
		String strMonth = mesi[ReadDicom.readInt(acqDate2.substring(4, 6).trim()) - 1];
		String strYear = acqDate2.substring(0, 4).trim();
		String acqDate = strDay + "-" + strMonth + "-" + strYear;

		// elaboration date
		String strDay2 = "";
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.DAY_OF_MONTH) < 10)
			strDay2 = "0" + cal.get(Calendar.DAY_OF_MONTH);
		else
			strDay2 = "" + cal.get(Calendar.DAY_OF_MONTH);

		String strMonth2 = mesi[cal.get(Calendar.MONTH)];
		String strYear2 = "" + (cal.get(Calendar.YEAR));
		String elabDate = strDay2 + "-" + strMonth2 + "-" + strYear2 + "_" + version;
		// String coil = UtilAyv.getFirstCoil(ReadDicom.readDicomParameter(imp1,
		// DICOM_COIL));

		aux3 = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_SERIES_DESCRIPTION);

		// String reqCoil =
		// getRequestedCoil(UtilAyv.getFiveLetters(aux3).trim(),
		// tabCodici);
		// MyLog.waitHere("reqCoil= "+reqCoil);

		String coil = ReadDicom.getThisCoil(imp1, reqCoil);
		MyLog.waitHere("coil= " + coil);
		if (coil == null)
			coil = "";

		if (coil.equals("MISSING")) {
			coil = new UtilAyv().kludge(path);
		}

		// ModelessMsg("codice="+codice, "continua");
		simpleHeader[0] = codice;
		simpleHeader[1] = stationName;
		simpleHeader[2] = patName;
		simpleHeader[3] = acqDate;
		simpleHeader[4] = elabDate;
		simpleHeader[5] = coil;
		simpleHeader[6] = frequency;
		// MyLog.logVector(simpleHeader, "simpleHeader");

		return (simpleHeader);
	}

	public static String getRequestedCoil1(String searchThisCode, String[][] tabCodici) {
		String out1 = null;
		for (int i1 = 0; i1 < tabCodici.length; i1++) {
			if (TableCode.getCode(tabCodici, i1).equals(searchThisCode)) {
				out1 = TableCode.getCoil(tabCodici, i1);
			}
		}
		return out1;
	}

	public static String[] getRequestedCoil2(String searchThisCode, String[][] tabCodici) {

		List<String> listOut1 = new ArrayList<String>();

		for (int i1 = 0; i1 < tabCodici.length; i1++) {
			if (TableCode.getCode(tabCodici, i1).equals(searchThisCode)) {
				listOut1.add(TableCode.getCoil(tabCodici, i1));
			}
		}
		String[] vetOut1 = ArrayUtils.arrayListToArrayString(listOut1);

		return vetOut1;
	}

	public static String[] getMiniStandardInfo(String path, ImagePlus imp1, boolean called) {

		if (imp1 == null) {
			IJ.log("getSimpleStandardInfo.imp1 == null");
			return null;
		}
		// or: the code is in the dicomSeriesDescription
		String aux3 = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_SERIES_DESCRIPTION);

		String codice = UtilAyv.getFiveLetters(aux3).trim();

		String stationName = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_STATION_NAME);
		String patName = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_PATIENT_NAME);
		String frequency = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_IMAGING_FREQUENCY);

		String[] mesi = { "gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic" };
		String acqDate2 = readDate(imp1);
		String strDay = acqDate2.substring(6).trim();
		String strMonth = mesi[ReadDicom.readInt(acqDate2.substring(4, 6).trim()) - 1];
		String strYear = acqDate2.substring(0, 4).trim();
		String acqDate = strDay + "-" + strMonth + "-" + strYear;
		// elaboration date
		String strDay2 = "";
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.DAY_OF_MONTH) < 10)
			strDay2 = "0" + cal.get(Calendar.DAY_OF_MONTH);
		else
			strDay2 = "" + cal.get(Calendar.DAY_OF_MONTH);

		String strMonth2 = mesi[cal.get(Calendar.MONTH)];
		String strYear2 = "" + (cal.get(Calendar.YEAR));
		String elabDate = strDay2 + "-" + strMonth2 + "-" + strYear2;
		String coil = ReadDicom.getFirstCoil(imp1);
		if (coil.equals("MISSING")) {
			coil = new UtilAyv().kludge(path);
		}

		// ModelessMsg("codice="+codice, "continua");
		simpleHeader[0] = codice;
		simpleHeader[1] = stationName;
		simpleHeader[2] = patName;
		simpleHeader[3] = acqDate;
		simpleHeader[4] = elabDate;
		simpleHeader[5] = coil;
		simpleHeader[6] = frequency;
		return (simpleHeader);
	}

	/**
	 * scrive le informazioni raccolte da getStandardInfo2 o getStandardInfo3
	 * nella ResultsTable
	 * 
	 * @param info1
	 *            informazioni da getStandardInfo2
	 * @return ResultsTable del report
	 */
	@SuppressWarnings("deprecation")
	public static ResultsTable putStandardInfoRT(String[][] info1) {
		// IJ.run("Measure");

		ResultsTable rt = ResultsTable.getResultsTable();
		rt.reset();
		String t1 = "TESTO          ";
		// rt.setHeading(++col, t1);
		rt.setHeading(2, t1);
		rt.setHeading(2, "VALORE");
		for (int i1 = 0; i1 < info1.length; i1++) {
			rt.incrementCounter();
			rt.addLabel(t1, info1[i1][1]);
		}
		rt.incrementCounter();
		return (rt);
	}

	/***
	 * Questa versione aggiornata non utilizza pi� setHeading, ora deprecata
	 * 
	 * @param info1
	 * @return
	 */
	// @SuppressWarnings("deprecation")
	public static ResultsTable putSimpleStandardInfoRT(String[] info1) {

		// ResultsTable rt = ResultsTable.getResultsTable();
		ResultsTable rt = new ResultsTable();
		rt.reset();

		rt.incrementCounter();
		String t1 = "TESTO";

		for (int i1 = 0; i1 < info1.length; i1++) {
			rt.addLabel(t1, info1[i1]);
			rt.incrementCounter();
		}
		return (rt);
	}

	public static ResultsTable putMiniStandardInfoRT(String[] info1) {

		ResultsTable rt = ResultsTable.getResultsTable();
		rt.reset();
		String t1 = "TESTO";
		for (int i1 = 0; i1 < info1.length; i1++) {
			rt.incrementCounter();
			rt.addValue(t1, info1[i1]);
		}
		return (rt);
	}

	public static int getStandardInfoLength() {
		return simpleHeader.length;
	}

}