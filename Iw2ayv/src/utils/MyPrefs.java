package utils;

import ij.IJ;
import ij.Prefs;

public class MyPrefs {
	static boolean ignoreOld= false;
	
	
	public static void myInput() {
		
		String prop2 = Prefs.getPrefsDir();
		MyLog.waitHere("prop2= "+prop2);
		
		
		ignoreOld = Prefs.getBoolean(Prefs.KEY_PREFIX+"ignoreRescaleSlope", false);
		IJ.run("DICOM...", "ignore");
	}
	public static void myOutput() {
	//	Prefs.set(Prefs.KEY_PREFIX+"ignoreRescaleSlope", ignoreOld);		
	}



}
