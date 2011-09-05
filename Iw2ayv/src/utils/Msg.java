package utils;

public class Msg {
	
	
	public static void msgTestPassed() {
		ButtonMessages.ModelessMsg(
				"Il test su immagini campione E' STATO SUPERATO", "CONTINUA");
	}

	public static void msgTestFault() {
		ButtonMessages.ModelessMsg("Il test su immagini campione E' FALLITO",
				"CONTINUA");
	}


	public static void msgParamError() {
		ButtonMessages.ModelessMsg(" >> ERRORE PARAMETRI CHIAMATA",
				"CHIUDI");
	}

	public static boolean msgStandalone() {
		ButtonMessages
				.ModelessMsg(
						"Fine programma, in modo STAND-ALONE salvare ADESSO, A MANO la finestra Risultati, dopo di che premere CONTINUA",
						"CONTINUA");
		return true;
	}
	/**
	 * 
	 * @return
	 */
	public static boolean accettaMenu() {
		boolean accetta = false;
		int userSelection3 = ButtonMessages.ModelessMsg(
				"Accettare il risultato delle misure?", "ACCETTA", "RIFAI");
		switch (userSelection3) {
		case 1:
			UtilAyv.cleanUp();
			accetta = false;
			break;
		case 2:
			accetta = true;
			break;
		}
		return accetta;
	}


}
