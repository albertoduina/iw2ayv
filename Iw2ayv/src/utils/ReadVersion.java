package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import ij.IJ;

/**
 * Questa classe è stata testata e rappresenta un metodo altrenativo per leggere
 * la ImplementationVersion da MANIFEST del file Jar. E'possibile leggere anche
 * le altre informazioni. In futuro potrebbe venire usata per inserire
 * automaticamente la versione di build (sia di contMensili che di iw2ayv)
 * all'interno del file di result.txt stampato dai plugins.
 * 
 * @author alberto duina
 * 
 */
public class ReadVersion {

	// public static void readVersionInfoInManifestDemo() {
	// Package[] localPackages = Package.getPackages();
	// String[] packageNames = new String[localPackages.length];
	//
	// for (int i1 = 0; i1 < localPackages.length; i1++) {
	// packageNames[i1] = localPackages[i1].toString();
	// }
	//
	// for (int i1 = 0; i1 < localPackages.length; i1++) {
	// if ((!packageNames[i1].contains("Java Platform API"))
	// && (!packageNames[i1].contains("ij"))) {
	// IJ.log(packageNames[i1]);
	// IJ.log(localPackages[i1].getImplementationVersion());
	// MyLog.waitHere();
	// }
	// }
	// }

	public static String readVersionInfoInManifest(String name1) {
		Package[] localPackages = Package.getPackages();
		String[] packageNames = new String[localPackages.length];
		String out = "MISSING";

		for (int i1 = 0; i1 < localPackages.length; i1++) {
			packageNames[i1] = localPackages[i1].toString();
		}

		for (int i1 = 0; i1 < localPackages.length; i1++) {
			if (packageNames[i1].contains(name1)) {
				if (localPackages[i1].getImplementationVersion() != null) {
					out = localPackages[i1].getImplementationVersion();
				}
				return out;
			}
		}
		return out;
	}

	public String readDateInfoInManifest(String name1) {
		
		
		String[] result = InputOutput.readStringArrayFromFile("/META-INF/MANIFEST.MF");
		MyLog.logVector(result, "result");

//		try {
//			InputStream stream = getClass().getResourceAsStream(
//					"/META-INF/MANIFEST.MF");
//			if (stream == null) {
//				System.out.println("Couldn't find manifest.");
//				return null;
//			}
//			
//			
//
//			Manifest manifest = new Manifest(stream);
//			MyLog.waitHere("manifest= "+manifest);
//			Attributes attributes = manifest.getMainAttributes();
//			String impTitle = attributes.getValue("Implementation-Title");
//			MyLog.waitHere(impTitle);
//			String impVersion = attributes.getValue("Implementation-Version");
//			MyLog.waitHere(impVersion);
//			String impBuildDate = attributes.getValue("Built-Date");
//			MyLog.waitHere(impBuildDate);
//			String impBuiltBy = attributes.getValue("Built-By");
//			MyLog.waitHere(impBuiltBy);
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return null;
	}
}
