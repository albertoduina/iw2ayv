package utils;

import java.util.concurrent.TimeUnit;

import ij.IJ;

public class MyTimeUtils {

	/**
	 * Trasformazione di AcqTime (h:m:s) in millisecondi (long integer)
	 * 
	 * @param hmsTime
	 * @return
	 */
	public static long milliTime(String hmsTime) {

		// per comodit� di calcolo trasformo tutto in millisecondi, tanto un
		// giorno � composto da 86400 secondi ovvero 86,400,000 millisecondi,
		// comodamente rappresentati in un long integer a 32 bit che pu�
		// contenere fino a +2,147,483,647. Poich� quei Philippini della Philips
		// non stampano gli zeri dopo la virgola, faccio una specie di zero
		// padding, per permettere il funzionamento corretto della routine

		String campione = "000000.000000";
		if (hmsTime.length() < 13) {
			String padding = campione.substring(hmsTime.length());
			hmsTime = hmsTime + padding;
		}
		int ore = ReadDicom.readInt(hmsTime.substring(0, 2));
		int minuti = ReadDicom.readInt(hmsTime.substring(2, 4));
		int secondi = ReadDicom.readInt(hmsTime.substring(4, 6));
		int millesimi = ReadDicom.readInt(hmsTime.substring(7, 10));
		long totale = (long) ore * 3600000L + (long) minuti * 60000L + (long) secondi * 1000L + (long) millesimi;
		return totale;
	}

	/***
	 * Calcolo del tempo trascorso utilizzando i nanosecondi
	 **
	 ** ......long time1 = System.nanoTime(); ......long time2 =
	 * System.nanoTime(); ......String tempo1 = MyTimeUtils.stringNanoTime(time2
	 * - time1); ......MyLog.waitHere("Tempo hh:mm:ss.ms " + tempo1);
	 * 
	 * @param nanodiff1
	 * @return
	 */
	public static String stringNanoTime(long nanodiff1) {

		int dmillis;
		int dseconds;
		int dminutes;
		int dhours;
		long kappa = 1000000000L;
		long kappa1 = 1000000L;
		long nanodiff = nanodiff1;
		dhours = (int) (nanodiff / (60L * 60L * kappa));
		nanodiff %= (60L * 60L * kappa);
		dminutes = (int) (nanodiff / (60L * kappa));
		nanodiff %= (60L * kappa);
		dseconds = (int) (nanodiff / (kappa));
		nanodiff %= (kappa);
		dmillis = (int) (nanodiff / kappa1);
		StringBuilder sb1 = new StringBuilder();
		sb1.append(dhours == 0 ? "00" : dhours < 10 ? String.valueOf("0" + dhours) : String.valueOf(dhours));
		sb1.append(":");
		sb1.append(dminutes == 0 ? "00" : dminutes < 10 ? String.valueOf("0" + dminutes) : String.valueOf(dminutes));
		sb1.append(":");
		sb1.append(dseconds == 0 ? "00" : dseconds < 10 ? String.valueOf("0" + dseconds) : String.valueOf(dseconds));
		sb1.append(".");
		sb1.append(String.valueOf(dmillis));
		return sb1.toString();
	}

	/***
	 * Calcolo del tempo trascorso utilizzando i millisecondi
	 **
	 ** .......long time1 = System.currentTimeMillis .......long time2 =
	 * System.currentTimeMillis ......String tempo1 =
	 * MyTimeUtils.stringMilliTime(time2 - time1); ......MyLog.waitHere(
	 * "Tempo hh:mm:ss.ms " + tempo1);
	 * 
	 * @param nanodiff1
	 * @return
	 */
	public static String stringMilliTime(long millidiff1) {

		int dmillis;
		int dseconds;
		int dminutes;
		int dhours;
		long kappa = 1000000L;
		long kappa1 = 1000L;
		long millidiff = millidiff1;
		dhours = (int) (millidiff / (60L * 60L * kappa));
		millidiff %= (60L * 60L * kappa);
		dminutes = (int) (millidiff / (60L * kappa));
		millidiff %= (60L * kappa);
		dseconds = (int) (millidiff / (kappa));
		millidiff %= (kappa);
		dmillis = (int) (millidiff / kappa1);
		StringBuilder sb1 = new StringBuilder();
		sb1.append(dhours == 0 ? "00" : dhours < 10 ? String.valueOf("0" + dhours) : String.valueOf(dhours));
		sb1.append(":");
		sb1.append(dminutes == 0 ? "00" : dminutes < 10 ? String.valueOf("0" + dminutes) : String.valueOf(dminutes));
		sb1.append(":");
		sb1.append(dseconds == 0 ? "00" : dseconds < 10 ? String.valueOf("0" + dseconds) : String.valueOf(dseconds));
		sb1.append(".");
		sb1.append(String.valueOf(dmillis));
		return sb1.toString();
	}

	public static String combinationFormatter(final long nanos) {

		long millis = TimeUnit.NANOSECONDS.toMillis(nanos) - TimeUnit.NANOSECONDS.toSeconds(nanos);
		long seconds = TimeUnit.NANOSECONDS.toSeconds(nanos) - TimeUnit.NANOSECONDS.toMinutes(nanos);
		long minutes = TimeUnit.NANOSECONDS.toMinutes(nanos) - TimeUnit.NANOSECONDS.toHours(nanos);
		long hours = TimeUnit.NANOSECONDS.toHours(nanos);

		StringBuilder sb1 = new StringBuilder();
		sb1.append(hours == 0 ? "00" : hours < 10 ? String.valueOf("0" + hours) : String.valueOf(hours));
		sb1.append(":");
		sb1.append(minutes == 0 ? "00" : minutes < 10 ? String.valueOf("0" + minutes) : String.valueOf(minutes));
		sb1.append(":");
		sb1.append(seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) : String.valueOf(seconds));
		sb1.append(".");
		sb1.append(String.valueOf(millis));
		return sb1.toString();
	}
}
