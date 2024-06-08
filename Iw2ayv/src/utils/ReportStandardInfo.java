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
	//
	//-----------2024 - 04 - 17--------------
	//
	// private final static String DICOM_RECEIVING_COIL = "0018,1250";
	//
	
	
	

	private static String[] simpleHeader = { "none", "none", "none", "none", "none", "none", "none", "<END>" };

	/**
	 * legge un gruppo di informazioni che verranno inserite nella ResultsTable
	 * 
	 * @param tabImmagini tabella coi valori di iw2ayv.txt
	 * @param riga        numero di riga passato da sequenze
	 * @param tabCodici   tabella coi valori di codici.txt
	 * @param version     nome del plugin e sua versione
	 * @param called      true se il plugin � stato chiamato da sequenze
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
		// posso ottenere i dati dell'immagine, anziche' partendo dalle tabelle e
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
		String coilElement = ReadDicom.getFirstCoil(imp1);
		if (coilElement.equals("MISSING")) {
			coilElement = new UtilAyv().kludge(path1);
		}
		//-------< 2024_04_17 >----------------------
		String receivingCoil = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_RECEIVING_COIL);
		// a questo punto riunisco il nome della bobina di ricezione e quello del cluster separandoli con un ";"
		String coil= receivingCoil+ " - "+  coilElement;
		//-------< 2024_04_17 >----------------------
		

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
	 * @param imp1 ImagePlus immagine
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
	 * @param strRiga   tabella coi valori di iw2ayv.txt
	 * @param imp1      ImagePlus
	 * @param tabCodici tabella coi valori di codici.txt
	 * @param version   nome del plugin e sua versione
	 * @param called    true se il plugin � stato chiamato da sequenze
	 * @return gruppo informazioni per la ResultsTable
	 */
	public static String[] getSimpleStandardInfo(String path, ImagePlus imp1, String[][] tabCodici, String version,
			boolean called) {

		if (imp1 == null) {
			IJ.log("getSimpleStandardInfo.imp1 == null");
			return null;
		}
		String aux3 = imp1.getTitle();

		// MyLog.waitHere("getSimpleStandardInfo.aux3=" + aux3);
		String codice;
		// 2 possibilities: the first 5 letters of filename are the CODE or
		// if (InputOutput.isCode(aux3.substring(0, 5).trim(), tabCodici))
		if (InputOutput.isCode(UtilAyv.getFiveLetters(aux3).trim(), tabCodici)) {
			// main possibility: the first 5 letters of the filename are a
			// recognized code (in codici.txt)
			codice = UtilAyv.getFiveLetters(aux3).trim();
			// MyLog.waitHere("prime 5 lettere riconosciute");
		} else {
			// or: the code is in the dicomSeriesDescription
			aux3 = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_SERIES_DESCRIPTION);
			if (aux3 == null)
				return null;

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
		String coilElement = ReadDicom.getThisCoil(imp1, reqCoil);
//		MyLog.logVector(reqCoil, "reqCoil");
//		IJ.log("coilElement= "+coilElement);
//		MyLog.waitHere();
		if (coilElement == null) {
			coilElement = "null";
//			MyLog.waitHere("coil = null");
		}

		if (coilElement.equals("MISSING")) {
			coilElement = new UtilAyv().kludge(path);
		}
		
		//-------< 2024_04_17 >----------------------
		String receivingCoil = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_RECEIVING_COIL);
		// a questo punto riunisco il nome della bobina di ricezione e quello del cluster separandoli con un ";"
		String coil= receivingCoil+ " - "+  coilElement;
		//-------< 2024_04_17 >----------------------

		
		
		

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
	 * @param strRiga   tabella coi valori di iw2ayv.txt
	 * @param imp1      ImagePlus
	 * @param tabCodici tabella coi valori di codici.txt
	 * @param version   nome del plugin e sua versione
	 * @param called    true se il plugin � stato chiamato da sequenze
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

		String coilElement = ReadDicom.getThisCoil(imp1, reqCoil);
		MyLog.waitHere("coil= " + coilElement);
		if (coilElement == null)
			coilElement = "";

		if (coilElement.equals("MISSING")) {
			coilElement = new UtilAyv().kludge(path);
		}

		
		//-------< 2024_04_17 >----------------------
		String receivingCoil = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_RECEIVING_COIL);
		// a questo punto riunisco il nome della bobina di ricezione e quello del cluster separandoli con un ";"
		String coil= receivingCoil+ " - "+  coilElement;
		//-------< 2024_04_17 >----------------------

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
		
		
		String coilElement = ReadDicom.getFirstCoil(imp1);
		if (coilElement.equals("MISSING")) {
			coilElement = new UtilAyv().kludge(path);
		}
		
		//-------< 2024_04_17 >----------------------
		String receivingCoil = ReadDicom.readDicomParameter(imp1, MyConst.DICOM_RECEIVING_COIL);
		// a questo punto riunisco il nome della bobina di ricezione e quello del cluster separandoli con un ";"
		String coil= receivingCoil+ " - "+  coilElement;
		//-------< 2024_04_17 >----------------------


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
	 * scrive le informazioni raccolte da getStandardInfo2 o getStandardInfo3 nella
	 * ResultsTable
	 * 
	 * @param info1 informazioni da getStandardInfo2
	 * @return ResultsTable del report
	 */
	@SuppressWarnings("deprecation")
	public static ResultsTable putStandardInfoRT(String[][] info1) {
		// IJ.run("Measure");

		ResultsTable rt = ResultsTable.getResultsTable();
		rt.reset();
		String t1 = "TESTO";
		for (int i1 = 0; i1 < info1.length; i1++) {
			rt.incrementCounter();
			rt.addValue(t1, info1[i1][1]);
		}
		rt.incrementCounter();
		return (rt);
	}

	/***
	 * Questa versione aggiornata non utilizza piu' setHeading ed addLabel, ora
	 * deprecate
	 * 
	 * @param info1
	 * @return
	 */
	public static ResultsTable putSimpleStandardInfoRT_new(String[] info1) {

		ResultsTable rt = new ResultsTable();
		rt.reset();

		for (int i1 = 0; i1 < info1.length; i1++) {
			rt.incrementCounter();
			rt.addValue("TESTO", info1[i1]);
		}
		rt.incrementCounter();
		return (rt);
	}

	/***
	 * Questa versione aggiornata non utilizza piu' setHeading, ora deprecata
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
			rt.addValue(t1, info1[i1]);
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

	public static ResultsTable abortResultTable_P2(String[] info11) {

		// P2 (GELS) dovrebbe generare 20 righe totali, le standardinfo occupano le
		// prime 8 righe

		ResultsTable rt11 = ResultsTable.getResultsTable();
		String t11 = "TESTO";
		String s12 = "VALORE";
		String s13 = "roi_x";
		String s14 = "roi_y";
		String s15 = "roi_b";
		String s16 = "roi_h";

		rt11.reset();
		rt11 = ReportStandardInfo.putSimpleStandardInfoRT_new(info11);
		rt11.showRowNumbers(true);

		rt11.addValue(t11, "dummy02");
		rt11.addValue(s12, "xxxx");
		rt11.addValue(s13, "xxxx");
		rt11.addValue(s14, "xxxx");
		rt11.addValue(s15, "xxxx");
		rt11.addValue(s16, "xxxx");

		for (int i2 = 0; i2 < 11; i2++) {
			rt11.incrementCounter();
			rt11.addValue(t11, "dummy02");
			rt11.addValue(s12, "xxxx");
			rt11.addValue(s13, "xxxx");
			rt11.addValue(s14, "xxxx");
			rt11.addValue(s15, "xxxx");
			rt11.addValue(s16, "xxxx");
		}

	//	rt11.show("AbortResultTable_P2");
	//	MyLog.waitHere("AbortResultTable_P2");

		return rt11;
	}

	public static ResultsTable abortResultTable_P3(String[] info11) {

		// P3 (unint manuale) dovrebbe generare 20 righe totali, le standardinfo
		// occupano le
		// prime 8 righe

		ResultsTable rt11 = ResultsTable.getResultsTable();
		String t11 = "TESTO";
		String s12 = "VALORE";
		String s13 = "roi_x";
		String s14 = "roi_y";
		String s15 = "roi_b";
		String s16 = "roi_h";

		rt11.reset();
		rt11 = ReportStandardInfo.putSimpleStandardInfoRT_new(info11);
		rt11.showRowNumbers(true);

		rt11.addValue(t11, "dummy03");
		rt11.addValue(s12, "xxxx");
		rt11.addValue(s13, "xxxx");
		rt11.addValue(s14, "xxxx");
		rt11.addValue(s15, "xxxx");
		rt11.addValue(s16, "xxxx");

		for (int i2 = 0; i2 < 14; i2++) {
			rt11.incrementCounter();
			rt11.addValue(t11, "dummy03");
			rt11.addValue(s12, "xxxx");
			rt11.addValue(s13, "xxxx");
			rt11.addValue(s14, "xxxx");
			rt11.addValue(s15, "xxxx");
			rt11.addValue(s16, "xxxx");
		}
		return rt11;
	}

	public static ResultsTable abortResultTable_P4(String[] info11) {

		// P4 (MTF) dovrebbe generare 16 righe totali, le standardinfo occupano le
		// prime 8 righe

		ResultsTable rt11 = ResultsTable.getResultsTable();
		String t11 = "TESTO";
		String s12 = "VALORE";
		String s13 = "roi_x";
		String s14 = "roi_y";
		String s15 = "roi_b";
		String s16 = "roi_h";

		rt11.reset();
		rt11 = ReportStandardInfo.putSimpleStandardInfoRT_new(info11);
		rt11.showRowNumbers(true);

		rt11.addValue(t11, "dummy04");
		rt11.addValue(s12, "xxxx");
		rt11.addValue(s13, "xxxx");
		rt11.addValue(s14, "xxxx");
		rt11.addValue(s15, "xxxx");
		rt11.addValue(s16, "xxxx");

		for (int i2 = 0; i2 < 7; i2++) {
			rt11.incrementCounter();
			rt11.addValue(t11, "dummy04");
			rt11.addValue(s12, "xxxx");
			rt11.addValue(s13, "xxxx");
			rt11.addValue(s14, "xxxx");
			rt11.addValue(s15, "xxxx");
			rt11.addValue(s16, "xxxx");
		}
		return rt11;
	}

	public static ResultsTable abortResultTable_P5(String[] info11) {

		// P5 (UNINT SUPERFICIALI) dovrebbe generare 16 righe totali, le standardinfo
		// occupano le
		// prime 8 righe

		ResultsTable rt11 = ResultsTable.getResultsTable();
		String t11 = "TESTO";
		String s12 = "VALORE";
		String s13 = "roi_x";
		String s14 = "roi_y";
		String s15 = "roi_b";
		String s16 = "roi_h";

		rt11.reset();
		rt11 = ReportStandardInfo.putSimpleStandardInfoRT_new(info11);
		rt11.showRowNumbers(true);

		rt11.addValue(t11, "dummy05");
		rt11.addValue(s12, "xxxx");
		rt11.addValue(s13, "xxxx");
		rt11.addValue(s14, "xxxx");
		rt11.addValue(s15, "xxxx");
		rt11.addValue(s16, "xxxx");

		for (int i2 = 0; i2 < 17; i2++) {
			rt11.incrementCounter();
			rt11.addValue(t11, "dummy05");
			rt11.addValue(s12, "xxxx");
			rt11.addValue(s13, "xxxx");
			rt11.addValue(s14, "xxxx");
			rt11.addValue(s15, "xxxx");
			rt11.addValue(s16, "xxxx");
		}
		return rt11;
	}

	public static ResultsTable abortResultTable_P6(String[] info11, String[] slicePosition11, int nFrames) {

		// P6 (FWHM) dovrebbe generare 29 righe totali, le standardinfo occupano le
		// prime 8 righe

		int num = 1;
		if (info11[0].equals("BTMA_") || info11[0].equals("HTMA_")) {
			num = 15;
		}

//		MyLog.waitHere("num= " + num);

		ResultsTable rt11 = ResultsTable.getResultsTable();
		String t11 = "TESTO";
		String s12 = "SLICE";
		String s13 = "roi_x";
		String s14 = "roi_y";
		String s15 = "roi_b";
		String s16 = "roi_h";

		rt11.reset();
		rt11 = ReportStandardInfo.putSimpleStandardInfoRT_new(info11);
		rt11.showRowNumbers(true);

		for (int i1 = 0; i1 < num; i1++) {
			rt11.addValue(t11, " SLICE");
			for (int j1 = 0; j1 < num; j1++) {
				rt11.addValue(s12 + j1, "---");
			}
		}
		rt11.addValue(s13, "---");
		rt11.addValue(s14, "---");
		rt11.addValue(s15, "---");
		rt11.addValue(s16, "---");


		for (int i2 = 0; i2 < 20; i2++) {
			rt11.incrementCounter();
			rt11.addValue(t11, "dummy06");
			for (int j1 = 0; j1 < num; j1++) {
				rt11.addValue(s12 + j1, "xxxx");
			}
		}

		return rt11;
	}

	public static ResultsTable abortResultTable_P7(String[] info11) {

		// P7 (WARP) dovrebbe generare 46 righe totali, le standardinfo occupano le
		// prime 8 righe

		ResultsTable rt11 = ResultsTable.getResultsTable();
		TableCode tc1 = new TableCode();
		// String[][] tabCodici = tc1.loadMultipleTable("codici", ".csv");

		String t11 = "TESTO";
		String s12 = "VALORE";
		String s13 = "seg_ax";
		String s14 = "seg_ay";
		String s15 = "seg_bx";
		String s16 = "seg_by";

		rt11.reset();

		rt11 = ReportStandardInfo.putSimpleStandardInfoRT_new(info11);

		rt11.showRowNumbers(true);

		rt11.addValue(t11, "dummy07");
		rt11.addValue(s12, "xxxx");
		rt11.addValue(s13, "xxxx");
		rt11.addValue(s14, "xxxx");
		rt11.addValue(s15, "xxxx");
		rt11.addValue(s16, "xxxx");

		for (int i1 = 0; i1 < 37; i1++) {
			rt11.incrementCounter();
			rt11.addValue(t11, "dummy07");
			rt11.addValue(s12, "xxxx");
			rt11.addValue(s13, "xxxx");
			rt11.addValue(s14, "xxxx");
			rt11.addValue(s15, "xxxx");
			rt11.addValue(s16, "xxxx");
		}

		return rt11;

	}

	public static ResultsTable abortResultTable_P8(String[] info11, String slicePosition11) {

		// P8 (DGP) dovrebbe generare 16 righe totali, le standardinfo occupano le prime
		// 8 righe

		ResultsTable rt11 = ResultsTable.getResultsTable();

		String t1 = "TESTO";
		String s2 = "VALORE";
		String s3 = "seg_ax";
		String s4 = "seg_ay";
		String s5 = "seg_bx";
		String s6 = "seg_by";

		rt11.reset();

		rt11 = ReportStandardInfo.putSimpleStandardInfoRT_new(info11);
		rt11.showRowNumbers(true);

		rt11.addValue(t1, "slicePos");
		rt11.addValue(s2, UtilAyv.convertToDouble(slicePosition11));
		// rt11.addValue(s2, slicePosition11);

		for (int i1 = 0; i1 < 7; i1++) {
			rt11.incrementCounter();
			rt11.addValue(t1, "dummy08");
			rt11.addValue(s2, "xxxx");
			rt11.addValue(s3, "xxxx");
			rt11.addValue(s4, "xxxx");
			rt11.addValue(s5, "xxxx");
			rt11.addValue(s6, "xxxx");
		}
		return rt11;
	}

	public static ResultsTable abortResultTable_P9(String[] info11) {

		// P2 (GELS) dovrebbe generare 20 righe totali, le standardinfo occupano le
		// prime 8 righe

		ResultsTable rt11 = ResultsTable.getResultsTable();
		String t11 = "TESTO";
		String s12 = "VALORE";
		String s13 = "roi_x";
		String s14 = "roi_y";
		String s15 = "roi_b";
		String s16 = "roi_h";

		rt11.reset();
		rt11 = ReportStandardInfo.putSimpleStandardInfoRT_new(info11);
		rt11.showRowNumbers(true);

		rt11.addValue(t11, "dummy09");
		rt11.addValue(s12, "xxxx");
		rt11.addValue(s13, "xxxx");
		rt11.addValue(s14, "xxxx");
		rt11.addValue(s15, "xxxx");
		rt11.addValue(s16, "xxxx");

		for (int i2 = 0; i2 < 6; i2++) {
			rt11.incrementCounter();
			rt11.addValue(t11, "dummy09");
			rt11.addValue(s12, "xxxx");
			rt11.addValue(s13, "xxxx");
			rt11.addValue(s14, "xxxx");
			rt11.addValue(s15, "xxxx");
			rt11.addValue(s16, "xxxx");
		}
		return rt11;
	}

	public static ResultsTable abortResultTable_P10(String[] info11, double slicePosition11) {

		// P10 (UNCOMBINED CIRCOLARE) dovrebbe generare 27 righe totali, le standardinfo
		// occupano le prime 8 righe

		ResultsTable rt11 = ResultsTable.getResultsTable();
		rt11.reset();

		String t11 = "TESTO";
		String s12 = "VALORE";
		String s13 = "roi_x";
		String s14 = "roi_y";
		String s15 = "roi_b";
		String s16 = "roi_h";
		rt11 = ReportStandardInfo.putSimpleStandardInfoRT_new(info11);
		rt11.showRowNumbers(true);

		rt11.addValue(t11, "dummy10");
		rt11.addValue(s12, "xxxx");
		rt11.addValue(s13, "xxxx");
		rt11.addValue(s14, "xxxx");
		rt11.addValue(s15, "xxxx");
		rt11.addValue(s16, "xxxx");

		for (int i1 = 0; i1 < 5; i1++) {
			rt11.incrementCounter();
			rt11.addValue(t11, "dummy10");
			rt11.addValue(s12, "xxxx");
			rt11.addValue(s13, "xxxx");
			rt11.addValue(s14, "xxxx");
			rt11.addValue(s15, "xxxx");
			rt11.addValue(s16, "xxxx");
		}

		rt11.incrementCounter();
		rt11.addValue(t11, "Pos");
		rt11.addValue(s12, slicePosition11);

		for (int i1 = 0; i1 < 12; i1++) {
			rt11.incrementCounter();
			rt11.addValue(t11, "dummy_10");
			rt11.addValue(s12, "xxxx");
			rt11.addValue(s13, "xxxx");
			rt11.addValue(s14, "xxxx");
			rt11.addValue(s15, "xxxx");
			rt11.addValue(s16, "xxxx");
		}
		return rt11;
	}

	public static ResultsTable abortResultTable_P11(String[] info11) {

		// P11 (unif piatta auto) dovrebbe generare 26 righe totali, le standardinfo
		// occupano le
		// prime 8 righe

		ResultsTable rt11 = ResultsTable.getResultsTable();
		String t11 = "TESTO";
		String s12 = "VALORE";
		String s13 = "roi_x";
		String s14 = "roi_y";
		String s15 = "roi_b";
		String s16 = "roi_h";

		rt11.reset();
		rt11 = ReportStandardInfo.putSimpleStandardInfoRT_new(info11);
		rt11.showRowNumbers(true);

		rt11.addValue(t11, "dummy11");
		rt11.addValue(s12, "xxxx");
		rt11.addValue(s13, "xxxx");
		rt11.addValue(s14, "xxxx");
		rt11.addValue(s15, "xxxx");
		rt11.addValue(s16, "xxxx");

		for (int i2 = 0; i2 < 17; i2++) {
			rt11.incrementCounter();
			rt11.addValue(t11, "dummy11");
			rt11.addValue(s12, "xxxx");
			rt11.addValue(s13, "xxxx");
			rt11.addValue(s14, "xxxx");
			rt11.addValue(s15, "xxxx");
			rt11.addValue(s16, "xxxx");
		}
		return rt11;
	}

	public static ResultsTable abortResultTable_P12(String[] info11, double slicePosition11) {

		// P12 (COMBINED) dovrebbe generare 23 righe totali, le standardinfo occupano le
		// prime 8 righe

		ResultsTable rt11 = ResultsTable.getResultsTable();
		rt11.reset();

		String t11 = "TESTO";
		String s12 = "VALORE";
		String s13 = "roi_x";
		String s14 = "roi_y";
		String s15 = "roi_b";
		String s16 = "roi_h";
		rt11 = ReportStandardInfo.putSimpleStandardInfoRT_new(info11);
		rt11.showRowNumbers(true);
		for (int i1 = 0; i1 < 8; i1++) {
			rt11.addValue(t11, "dummy12");
			rt11.addValue(s12, "xxxx");
			rt11.addValue(s13, "xxxx");
			rt11.addValue(s14, "xxxx");
			rt11.addValue(s15, "xxxx");
			rt11.addValue(s16, "xxxx");
			rt11.incrementCounter();
		}
		rt11.incrementCounter();
		rt11.addValue(t11, "Pos");
		rt11.addValue(s12, slicePosition11);
		rt11.addValue(s13, 0);
		rt11.addValue(s14, 0);
		rt11.addValue(s15, 0);
		rt11.addValue(s16, 0);

		for (int i1 = 0; i1 < 5; i1++) {
			rt11.addValue(t11, "dummy12");
			rt11.addValue(s12, "xxxx");
			rt11.addValue(s13, "xxxx");
			rt11.addValue(s14, "xxxx");
			rt11.addValue(s15, "xxxx");
			rt11.addValue(s16, "xxxx");
			rt11.incrementCounter();
		}
		return rt11;
	}

}