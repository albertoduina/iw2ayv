package utils;



public class MyTimeUtils {

	
	/**
	 * Trasformazione di AcqTime (h:m:s) in millisecondi (long integer)
	 * 
	 * @param hmsTime
	 * @return
	 */
	public static long milliTime(String hmsTime) {

		// per comodità di calcolo trasformo tutto in millisecondi, tanto un
		// giorno è composto da 86400 secondi ovvero 86,400,000 millisecondi,
		// comodamente rappresentati in un long integer a 32 bit che può
		// contenere fino a +2,147,483,647. Poichè quei Philippini della Philips
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
		long totale = (long) ore * 3600000L + (long) minuti * 60000L
				+ (long) secondi * 1000L + (long) millesimi;
		return totale;
	}

}
