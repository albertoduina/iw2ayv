package utils;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;

import ij.IJ;
import ij.gui.GenericDialog;

/**
 * Implementa un dialogo modale e non modale da 1 a 5 pulsanti
 * 
 * @author Alberto Duina - SPEDALI CIVILI DI BRESCIA - Servizio di Fisica
 *         Sanitaria
 * 
 */
public class MyGenericDialogGrid {

	public boolean showDialog2(int gridWidth, int gridHeight, TextField[] tf2, String[] lab2, double[] value2,
			String title2, int decimals) {
		GenericDialog gd2 = new GenericDialog(title2);
		gd2.addPanel(makePanel2(gd2, gridWidth, gridHeight, tf2, lab2, value2, decimals));
		gd2.showDialog();
		if (gd2.wasCanceled())
			return false;
		getValues2(gridWidth * gridHeight, tf2, value2);
		return true;
	}

	Panel makePanel2(GenericDialog gd2, int gridWidth, int gridHeight, TextField[] tf2, String[] lab2, double[] value2,
			int decimals) {
		Panel panel = new Panel();
		panel.setLayout(new GridLayout(gridHeight, gridWidth));
		int gridSize = gridWidth * gridHeight;
		for (int i1 = 0; i1 < gridSize; i1++) {
			tf2[i1] = new TextField("  " + IJ.d2s(value2[i1], decimals));
			panel.add(new Label(lab2[i1]));
			panel.add(tf2[i1]);
		}
		return panel;
	}

	public void getValues2(int gridSize, TextField[] tf2, double[] value2) {
		for (int i1 = 0; i1 < gridSize; i1++) {
			String s2 = tf2[i1].getText();
			value2[i1] = getValue2(s2);
		}
	}

	public void displayValues2(int gridSize, double[] value2, int decimals) {
		for (int i1 = 0; i1 < gridSize; i1++)
			IJ.log(i1 + " " + IJ.d2s(value2[i1], decimals));
	}

	/***
	 * Serve a suddividere una stringa
	 * 
	 * @param theText
	 * @return
	 */
	public double getValue2(String theText) {
		Double d;
		String str = theText;
		try {
			str = str.replaceAll("\\s+", "");
			d = new Double(str);
		} catch (NumberFormatException e) {
			d = null;
		}
		return d == null ? Double.NaN : d.doubleValue();
	}

	// =======================================================

	public boolean showDialog3(int gridWidth, int gridHeight, TextField[] tf3, String[] lab2, double[] valuePrevious,
			double[] valueDefault, double[] valueReturned, String title2, int decimals) {

		boolean vai = false;
		boolean canceled = false;
		do {
			GenericDialog gd3 = new GenericDialog(title2);
			// MyLog.waitHere("preparazione");

			gd3.enableYesNoCancel("default", "OK");
			gd3.addPanel(makePanel3(gd3, gridWidth, gridHeight, tf3, lab2, valuePrevious, decimals));

			gd3.showDialog();
			if (gd3.wasCanceled()) {
				return false;
			}
			if (gd3.wasOKed()) {
				// MyLog.waitHere("hai premuto default");
				valuePrevious = valueDefault.clone();
				// valueReturned = valueDefault.clone();
				vai = true;
			} else {
				vai = false;
			}
			if (gd3.wasCanceled())
				canceled = true;
			getValues3(gridWidth * gridHeight, tf3, valueReturned);
		} while (vai);
		return !canceled;
	}

	Panel makePanel3(GenericDialog gd3, int gridWidth, int gridHeight, TextField[] tf3, String[] lab3, double[] value3,
			int decimals) {
		Panel panel = new Panel();
		panel.setLayout(new GridLayout(gridHeight, gridWidth));
		int gridSize = gridWidth * gridHeight;
		for (int i1 = 0; i1 < gridSize; i1++) {
			tf3[i1] = new TextField("  " + IJ.d2s(value3[i1], decimals));
			panel.add(new Label(lab3[i1]));
			panel.add(tf3[i1]);
		}
		return panel;
	}

	public void getValues3(int gridSize, TextField[] tf3, double[] outValues) {
		for (int i1 = 0; i1 < gridSize; i1++) {
			String s2 = tf3[i1].getText();
			IJ.log("" + i1 + "  " + s2);
			outValues[i1] = getValue3(s2);
		}
	}

	public void displayValues3(int gridSize, double[] value2, int decimals) {
		for (int i1 = 0; i1 < gridSize; i1++)
			IJ.log(i1 + " " + IJ.d2s(value2[i1], decimals));
	}

	public double getValue3(String theText) {
		Double d;
		String str = theText;
		try {
			str = str.replaceAll("\\s+", "");
			d = new Double(str);
		} catch (NumberFormatException e) {
			d = null;
		}
		return d == null ? Double.NaN : d.doubleValue();
	}

}
