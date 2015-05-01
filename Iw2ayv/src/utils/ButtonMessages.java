package utils;

import utils.ModelessDialog;
import ij.IJ;

import java.awt.Frame;

/***
 * Button messages class
 * 
 * @author Alberto Duina - SPEDALI CIVILI DI BRESCIA - Servizio di Fisica
 *         Sanitaria
 *
 */

public class ButtonMessages {

	/**
	 * messaggio non modale, overloaded con numero pulsanti variabile da 5 a 1
	 * 
	 * @param sMsg
	 *            messaggio da mostrare
	 * @param sUno
	 *            stringa per il puls di destra = 1
	 * @param sDue
	 *            stringa puls 2
	 * @param sTre
	 *            stringa puls 3
	 * @param sQuattro
	 *            stringa puls 4
	 * @param sCinque
	 *            stringa puls 5
	 * @return numero pulsante premuto (il dx vale sempre 1)
	 */
	public static int ModelessMsg(String sMsg, String sUno, String sDue,
			String sTre, String sQuattro, String sCinque) {
		int iRetcode;
		Frame f = new Frame();
		ModelessDialog query = new ModelessDialog(sMsg, sUno, sDue, sTre,
				sQuattro, sCinque);
		query.setVisible(true);
		query.setAlwaysOnTop(true);
		iRetcode = query.answer();
		f.dispose();
		return iRetcode;
	}

	public static int ModelessMsg2(String sMsg, String sUno, String sDue,
			String sTre, String sQuattro, String sCinque, int preset) {
		int iRetcode;
		Frame f = new Frame();
		ModelessDialog query = new ModelessDialog(sMsg, sUno, sDue, sTre,
				sQuattro, sCinque);
		query.setVisible(true);
		query.setAlwaysOnTop(true);
		if ((preset >= 0) && (preset <= 5)) {
			IJ.wait(50);
			iRetcode = preset;
		} else {
			iRetcode = query.answer();
		}
		f.dispose();
		return iRetcode;
	}

	public static int ModelessMsg(String sMsg, String sUno, String sDue,
			String sTre, String sQuattro) {
		int iRetcode;
		Frame f = new Frame();
		ModelessDialog query = new ModelessDialog(sMsg, sUno, sDue, sTre,
				sQuattro);
		query.setVisible(true);
		query.setAlwaysOnTop(true);
		iRetcode = query.answer();
		f.dispose();
		return iRetcode;
	}

	public static int ModelessMsg(String sMsg, String sUno, String sDue,
			String sTre) {
		int iRetcode;
		Frame f = new Frame();
		ModelessDialog query = new ModelessDialog(sMsg, sUno, sDue, sTre);
		query.setVisible(true);
		query.setAlwaysOnTop(true);
		iRetcode = query.answer();
		f.dispose();
		return iRetcode;
	}

	public static int ModelessMsg(String sMsg, String sUno, String sDue) {
		int iRetcode;
		Frame f = new Frame();
		ModelessDialog query = new ModelessDialog(sMsg, sUno, sDue);
		query.setVisible(true);
		query.setAlwaysOnTop(true);
		iRetcode = query.answer();
		f.dispose();
		return iRetcode;
	}
	
	
	public static int ModelessMsg(String sMsg, String sUno, String sDue, int preset, int timeout) {
		int iRetcode;
		Frame f = new Frame();
		ModelessDialog query = new ModelessDialog(sMsg, sUno, sDue);
		if (preset > 0) {
			query.setJbuttonAutomatico(2);
			query.setTimeout(100);
		}

		query.setVisible(true);
		query.setAlwaysOnTop(true);
		iRetcode = query.answer();
		f.dispose();
		return iRetcode;
	}


	public static int ModelessMsg(String sMsg, String sUno) {
		int iRetcode;
		Frame f = new Frame();
		ModelessDialog query = new ModelessDialog(sMsg, sUno);
		query.setVisible(true);
		query.setAlwaysOnTop(true);
		iRetcode = query.answer();
		f.dispose();
		return iRetcode;
	}

	public static int ModelessMsg(String sMsg, String sUno, int preset,
			int timeout) {
		int iRetcode;
		Frame f = new Frame();
		ModelessDialog query = new ModelessDialog(sMsg, sUno);
		if (preset > 0) {
			query.setJbuttonAutomatico(2);
			query.setTimeout(100);
		}
		query.setVisible(true);
		query.setAlwaysOnTop(true);
		iRetcode = query.answer();
		f.dispose();
		return iRetcode;
	}
	
//	public static int ModelessMsg(String string, String string2,
//			String string3, int preset, int timeout) {
//		// TODO Auto-generated method stub
//		return 0;
//	}


	// private ModelessDialog ModelessMsg(String sMsg) {
	// ModelessDialog query = new ModelessDialog(sMsg);
	// query.setVisible(true);
	// query.setAlwaysOnTop(true);
	// return query;
	// }
	//
	// private Frame ModelessMsg0(String sMsg) {
	// Frame f = new Frame();
	// ModelessDialog query = new ModelessDialog(sMsg);
	// query.setVisible(true);
	// query.setAlwaysOnTop(true);
	// return f;
	// }

	/**
	 * messaggio modale, overloaded con numero pulsanti variabile da 5 a 1
	 * 
	 * @param sMsg
	 *            messaggio da mostrare
	 * @param sUno
	 *            stringa per il puls di destra = 1
	 * @param sDue
	 *            stringa puls 2
	 * @param sTre
	 *            stringa puls 3
	 * @return numero pulsante premuto (il dx vale sempre 1)
	 */
	public static int ModalMsg(String sMsg, String sUno, String sDue,
			String sTre) {
		int iRetcode;
		Frame f = new Frame();
		ModelessDialog query = new ModelessDialog(sMsg, sUno, sDue, sTre, true);
		query.setVisible(true);
		query.setAlwaysOnTop(true);
		iRetcode = query.answer();
		f.dispose();
		return iRetcode;
	}

	public static int ModalMsg(String sMsg, String sUno, String sDue) {
		int iRetcode;
		Frame f = new Frame();
		ModelessDialog query = new ModelessDialog(sMsg, sUno, sDue, true);
		query.setVisible(true);
		query.setAlwaysOnTop(true);
		iRetcode = query.answer();
		f.dispose();
		return iRetcode;
	}

	public static int ModalMsg(String sMsg, String sUno) {
		int iRetcode;
		Frame f = new Frame();
		ModelessDialog query = new ModelessDialog(sMsg, sUno, true);
		query.setVisible(true);
		query.setAlwaysOnTop(true);
		iRetcode = query.answer();
		f.dispose();
		return iRetcode;
	}


}