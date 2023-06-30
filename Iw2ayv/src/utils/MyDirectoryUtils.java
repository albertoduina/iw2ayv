package utils;

import java.io.File;
import java.util.ArrayList;

import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import ij.plugin.DICOM;

public class MyDirectoryUtils {

	/**
	 * Lista tutti i file, appartenenti a cartelle e sottocartelle, in maniera
	 * ricorsiva. Sara' la routine successiva a dividere per sottocartelle.
	 * 
	 * @param filePath path della directory di partenza
	 * 
	 * @return ArrayList con i path di tutti i file
	 */
	public static ArrayList<String> listFiles(String filePath) {
		ArrayList<String> list3 = new ArrayList<String>();
		String[] list2 = new File(filePath).list();
		if (list2 == null) {
			MyLog.waitHere("vuota");
			return null;
		}
		for (int i1 = 0; i1 < list2.length; i1++) {
			String path1 = filePath + "\\" + list2[i1];
			IJ.showStatus("listFiles " + i1 + "\\" + list2.length);
			File f1 = new File(path1);
			if (f1.isDirectory()) {
				list3.addAll(listFiles(path1));
			} else {
				IJ.redirectErrorMessages();
				if (isDicomImage(path1) || isTiffImage(path1)) {
					list3.add(path1);
					// IJ.log(path1);
				}
			}
		}
		return list3;
	}

	/***
	 * Testa se e' un file dicom ed e' un immagine visualizzabile
	 * 
	 * @param fileName1 nome immagine
	 * @return true se dicom
	 */
	public static boolean isDicomImage(String fileName1) {
		// IJ.redirectErrorMessages();
		int type = (new Opener()).getFileType(fileName1);
		if (type != Opener.DICOM) {
			return false;
		}
		ImagePlus imp1 = new Opener().openImage(fileName1);
		if (imp1 == null) {
			return false;
		}
		String info = new DICOM().getInfo(fileName1);
		if (info == null || info.length() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/***
	 * Testa se � un file tiff ed � un immagine visualizzabile
	 * 
	 * @param fileName1 nome immagine
	 * @return true se tiff
	 */
	public static boolean isTiffImage(String fileName1) {
		// IJ.redirectErrorMessages();
		int type = (new Opener()).getFileType(fileName1);
		if (type != Opener.TIFF) {
			return false;
		}
		ImagePlus imp1 = new Opener().openImage(fileName1);
		if (imp1 == null) {
			return false;
		}
		return true;
	}

	public static String[] listSorted(String inputDir) {
		ArrayList<String> arrayList1 = MyDirectoryUtils.listFiles(inputDir);
		String[] lista1 = ArrayUtils.arrayListToArrayString(arrayList1);
		String[] lista2 = MyDirectoryUtils.pathSorter(lista1);
		return lista2;
	}

	/**
	 * Legge la slice position dall�'header
	 * 
	 * @param listIn lista immagini
	 * @return vettore posizioni
	 */
	public static String[] listSlicePosition(String[] listIn) {
		String[] slicePosition = new String[listIn.length];
		for (int w1 = 0; w1 < listIn.length; w1++) {
			ImagePlus imp1 = UtilAyv.openImageNoDisplay(listIn[w1], true);
			slicePosition[w1] = ReadDicom.readDicomParameter(imp1, "0020,1041");
		}
		return slicePosition;
	}

	/**
	 * Ordinamento del vettore dei path delle immagini, secondo slicePosition
	 * crescente
	 * 
	 * @param path vettore dei path
	 * @return vettore ordinato
	 */
	public static String[] pathSorter(String[] path) {
		if ((path == null) || (path.length == 0)) {
			// IJ.log("pathSorter: path problems");
			return null;
		}
		Opener opener1 = new Opener();
		// test disponibilit� files
		for (int w1 = 0; w1 < path.length; w1++) {
			ImagePlus imp1 = opener1.openImage(path[w1]);
			if (imp1 == null) {
				IJ.log("pathSorter: image file unavailable?");
				return null;
			}
		}
		String[] slicePosition = listSlicePosition(path);
		String[] pathSortato = bubbleSortPath(path, slicePosition);
		// new UtilAyv().logVector(pathSortato, "pathSortato");
		return pathSortato;
	}

	/**
	 * Inverte l'ordine degli elementi nel vettore di stringhe contenente il path
	 * 
	 * @param path1 path da invertire
	 * @return path invertito
	 */
	public static String[] pathReverser(String[] path1) {
		String[] path2 = new String[path1.length];
		for (int i1 = 0; i1 < path1.length; i1++) {
			path2[path1.length - i1 - 1] = path1[i1];
		}
		return path2;
	}

	/**
	 * Effettua il sort, col metodo BubbleSort, dei path utilizzando come criterio
	 * sliceParameter
	 * 
	 * @param path           vettore dei path
	 * @param sliceParameter vettore parametro del sort
	 * @return vettore sortato
	 */
	public static String[] bubbleSortPath(String[] path, String[] sliceParameter) {

		if (path == null)
			return null;
		if (sliceParameter == null)
			return null;
		if (!(path.length == sliceParameter.length))
			return null;
		if (path.length < 2) {
			return path;
		}
		//
		// bubblesort
		//
		String[] sortedPath = new String[path.length];
		sortedPath = path;

		for (int i1 = 0; i1 < path.length; i1++) {
			for (int i2 = 0; i2 < path.length - 1 - i1; i2++) {
				double position1 = ReadDicom.readDouble(sliceParameter[i2]);
				double position2 = ReadDicom.readDouble(sliceParameter[i2 + 1]);
				if (position1 > position2) {
					String positionSwap = sliceParameter[i2];
					sliceParameter[i2] = sliceParameter[i2 + 1];
					sliceParameter[i2 + 1] = positionSwap;
					String pathSwap = sortedPath[i2];
					sortedPath[i2] = sortedPath[i2 + 1];
					sortedPath[i2 + 1] = pathSwap;
				}
			}
		}
		// bubblesort end
		return sortedPath;
	}

}
