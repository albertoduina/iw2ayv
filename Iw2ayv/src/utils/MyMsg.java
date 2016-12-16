package utils;

import java.awt.Color;
import java.awt.Font;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.ImageRoi;
import ij.gui.ImageWindow;
import ij.gui.Overlay;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;

public class MyMsg {
	static final int SMALL_FONT = 10, MEDIUM_FONT = 14, LARGE_FONT = 18;

	public static void timedMessage(String[] text, Color background, Color foreground, int milliseconds) {

		
		int w = 150;
		int h = 30 + text.length * 15;

		
		ImageProcessor ip1 = new ColorProcessor(w, h);
		ip1.setColor(background);
		ip1.fill();
		ip1 = ip1.resize(ip1.getWidth() * 2, ip1.getHeight() * 2);
		ip1.setAntialiasedText(true);
		int lines = text.length;

		int[] widths = new int[lines];
		widths[0] = ip1.getStringWidth(text[0]);
		for (int i1 = 1; i1 < lines - 1; i1++)
			widths[i1] = ip1.getStringWidth(text[i1]);
		int max = 0;
		for (int i1 = 0; i1 < lines - 1; i1++)
			if (widths[i1] > max)
				max = widths[i1];
		ip1.setColor(foreground);
		ip1.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, LARGE_FONT));
		ip1.setJustification(ImageProcessor.LEFT_JUSTIFY);
		int y = 30;
		for (int i1 = 0; i1 < text.length; i1++) {
			ip1.drawString(text[i1], 10, y);
			y += 30;
		}

		ImageWindow.centerNextImage();

		ImagePlus imp1 = new ImagePlus("*", ip1);
		imp1.show();
		IJ.wait(milliseconds);
		closeTimedMessage();
	}

	static void closeTimedMessage() {
		if (WindowManager.getFrame("*") != null) {
			IJ.selectWindow("*");
			IJ.run("Close");
		}
	}

	static int xDim(String text, ImageProcessor ip, int max) {
		return ip.getWidth() - max + (max - ip.getStringWidth(text)) / 2 - 10;
	}

	public static void msgTestPassed() {
		ButtonMessages.ModelessMsg(
				"Il test su immagini campione E' STATO SUPERATO", "CONTINUA");
	}

	public static void msgTestFault() {
		ButtonMessages.ModelessMsg("Il test su immagini campione E' FALLITO",
				"CONTINUA");
	}

	public static void msgParamError() {
		ButtonMessages.ModelessMsg(" >> ERRORE PARAMETRI CHIAMATA", "CHIUDI");
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
