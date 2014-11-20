package utils;

import ij.IJ;
import ij.gui.GenericDialog;

import java.awt.Frame;

public class MyInput {
	
	
	public static int myIntegerInput(String sMsg, String sLabel, int def1, int digits) {
		
		Frame f = new Frame();
		GenericDialog gd1 = new GenericDialog("", IJ.getInstance());
		gd1.addMessage(sMsg);
		gd1.addNumericField(sLabel, def1, digits);
		gd1.showDialog();
		if (gd1.wasCanceled())
			return def1;
		double out1 = gd1.getNextNumber(); 
		int out2 = (int) out1;		
		f.dispose();
		return out2;
	}



}
