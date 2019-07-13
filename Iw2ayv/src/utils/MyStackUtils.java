package utils;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.io.DirectoryChooser;
import ij.io.Opener;
import ij.measure.Calibration;
import ij.plugin.ImageCalculator;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;

public class MyStackUtils {

	/**
	 * estrae una singola slice da uno stack. Estrae anche i dati header
	 * 
	 * @param stack stack contenente le slices
	 * @param slice numero della slice da estrarre, deve partire da 1, non e'
	 *              ammesso lo 0
	 * @return ImagePlus della slice estratta
	 */
	public static ImagePlus imageFromStack(ImagePlus stack, int slice) {

		if (stack == null) {
			IJ.log("imageFromStack.stack== null");
			return null;
		}
		// IJ.log("stack bitDepth= "+stack.getBitDepth());
		ImageStack imaStack = stack.getImageStack();
		if (imaStack == null) {
			IJ.log("imageFromStack.imaStack== null");
			return null;
		}
		if (slice == 0) {
			IJ.log("imageFromStack.requested slice 0!");
			return null;

		}
		if (slice > stack.getStackSize()) {
			IJ.log("imageFromStack.requested slice > slices!");
			return null;
		}

		ImageProcessor ipStack = imaStack.getProcessor(slice);

		String titolo = "** " + slice + " **";
		// String titolo = imaStack.getShortSliceLabel(slice);
		String sliceInfo1 = imaStack.getSliceLabel(slice);

		ImagePlus imp = new ImagePlus(titolo, ipStack);
		imp.setProperty("Info", sliceInfo1);
		return imp;
	}

	public static ImagePlus imageFromStack(ImagePlus stack, int slice, boolean info) {

		if (stack == null) {
			IJ.log("imageFromStack.stack== null");
			return null;
		}
		// IJ.log("stack bitDepth= "+stack.getBitDepth());
		ImageStack imaStack = stack.getImageStack();
		if (imaStack == null) {
			IJ.log("imageFromStack.imaStack== null");
			return null;
		}
		if (slice == 0) {
			IJ.log("imageFromStack.requested slice 0!");
			return null;

		}
		if (slice > stack.getStackSize()) {
			IJ.log("imageFromStack.requested slice > slices!");
			return null;
		}

		ImageProcessor ipStack = imaStack.getProcessor(slice);

		String titolo = "** " + slice + " **";
		// String titolo = imaStack.getShortSliceLabel(slice);
		String sliceInfo1 = imaStack.getSliceLabel(slice);

		ImagePlus imp = new ImagePlus(titolo, ipStack);
		if (info) {
			imp.setProperty("Info", sliceInfo1);
		}
		return imp;
	}

	/**
	 * Costruisce uno stack di immagini a 16 bit, a partire dal vettore dei path.
	 * 
	 * @param path path of image files
	 * @return ImagePlus with the stack
	 */
	public static ImagePlus imagesToStack16(String[] path) {
		Opener opener1 = new Opener();
		ImagePlus imp1 = opener1.openImage(path[0]);
		int height = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1, MyConst.DICOM_ROWS));
		int width = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1, MyConst.DICOM_COLUMNS));
		ImageStack newStack = new ImageStack(width, height);
		for (int f1 = 0; f1 < path.length; f1++) {
			imp1 = opener1.openImage(path[f1]);
			if (imp1 == null) {
				IJ.log("stackBuilder2: image file unavailable?");
				return null;
			}
			ImageProcessor ip1 = imp1.getProcessor();
			if (f1 == 0)
				newStack.update(ip1);

			String sliceInfo1 = imp1.getTitle();
			String sliceInfo2 = (String) imp1.getProperty("Info");
			// aggiungo i dati header alle singole immagini dello stack
			if (sliceInfo2 != null)
				sliceInfo1 += "\n" + sliceInfo2;
			newStack.addSlice(sliceInfo2, ip1);
		}

		ImagePlus newImpStack = new ImagePlus("INPUT_STACK", newStack);
		if (path.length == 1) {
			String sliceInfo3 = imp1.getTitle();
			sliceInfo3 += "\n" + (String) imp1.getProperty("Info");
			newImpStack.setProperty("Info", sliceInfo3);
		}
		return newImpStack;
	}

	public static ImagePlus imagesToStack16(String[] path, boolean deb) {
		Opener opener1 = new Opener();
		ImagePlus imp1 = opener1.openImage(path[0]);
		int height = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1, MyConst.DICOM_ROWS));
		int width = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1, MyConst.DICOM_COLUMNS));
		ImageStack newStack = new ImageStack(width, height);
		for (int f1 = 0; f1 < path.length; f1++) {
			if (deb && f1 % 100 == 0)
				MyLog.trace("load ima " + f1 + "/" + path.length + "   MEMORY= " + IJ.currentMemory() / (1024 * 1024),
						deb);
			imp1 = opener1.openImage(path[f1]);
			if (imp1 == null) {
				IJ.log("stackBuilder2: image file unavailable?");
				return null;
			}
			ImageProcessor ip1 = imp1.getProcessor();
			if (f1 == 0)
				newStack.update(ip1);

			String sliceInfo1 = imp1.getTitle();
			String sliceInfo2 = (String) imp1.getProperty("Info");
			// aggiungo i dati header alle singole immagini dello stack
			if (sliceInfo2 != null)
				sliceInfo1 += "\n" + sliceInfo2;
			newStack.addSlice(sliceInfo2, ip1);
		}

		ImagePlus newImpStack = new ImagePlus("INPUT_STACK", newStack);
		if (path.length == 1) {
			String sliceInfo3 = imp1.getTitle();
			sliceInfo3 += "\n" + (String) imp1.getProperty("Info");
			newImpStack.setProperty("Info", sliceInfo3);
		}
		return newImpStack;
	}

	/**
	 * Costruisce uno stack a 32 bit
	 * 
	 * @param path path of image files
	 * @return ImagePlus with the stack
	 */
	public static ImagePlus imagesToStack32(String[] path) {
		Opener opener1 = new Opener();
		ImagePlus imp1 = opener1.openImage(path[0]);
		int height = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1, MyConst.DICOM_ROWS));
		int width = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1, MyConst.DICOM_COLUMNS));
		ImageStack newStack = new ImageStack(width, height);
		for (int f1 = 0; f1 < path.length; f1++) {
			imp1 = opener1.openImage(path[f1]);
			if (imp1 == null) {
				IJ.log("stackBuilder2: image file unavailable?");
				return null;
			}
			ImageConverter ic1 = new ImageConverter(imp1);
			ic1.convertToGray32();
			ImageProcessor ip1 = imp1.getProcessor();
			if (f1 == 0)
				newStack.update(ip1);
			newStack.addSlice("", ip1);
		}
		ImagePlus newImpStack = new ImagePlus("INPUT_STACK_float", newStack);
		return newImpStack;
	}

	/***
	 * Estrazione del valore dei pixel da uno stack a 16 bit e restituzione sotto
	 * forma di matrice a 3 dimensioni a 32 bit [slice][column[[row]
	 * 
	 * @param imp1 stack ImagePlus
	 * @return pixel matrix [slices][height][width]
	 */
	public static double[][][] stack16ToMatrix32(ImagePlus imp1) {
		ImageStack stack1 = imp1.getImageStack();
		int slices1 = imp1.getStackSize();
		int width1 = imp1.getWidth();
		int height1 = imp1.getHeight();
		double[][][] ret = new double[slices1][width1][height1];
		for (int slice = 0; slice < ret.length; slice++) {
			ImageProcessor ip1 = stack1.getProcessor(slice + 1);
			if (imp1.getType() == ImagePlus.GRAY16) {
				short[] sdata = (short[]) ip1.getPixels();
				for (int i1 = 0; i1 < width1; i1++) {
					for (int i2 = 0; i2 < height1; i2++) {
						ret[slice][i1][i2] = sdata[i1 + i2 * width1];
					}
				}
			}
			if (imp1.getType() == ImagePlus.GRAY32) {
				float[] sdata = (float[]) ip1.getPixels();
				for (int i1 = 0; i1 < width1; i1++) {
					for (int i2 = 0; i2 < height1; i2++) {
						ret[slice][i1][i2] = sdata[i1 + i2 * width1];
					}
				}
			}

		}
		return ret;
	}

	public static double[][][] stack16ToMatrix32Calibrated(ImagePlus imp1) {
		ImageStack stack1 = imp1.getImageStack();
		int slices1 = imp1.getStackSize();
		int width1 = imp1.getWidth();
		int height1 = imp1.getHeight();
		Calibration cal1 = imp1.getCalibration();

		double[][][] ret = new double[slices1][width1][height1];
		for (int slice = 0; slice < ret.length; slice++) {
			ImageProcessor ip1 = stack1.getProcessor(slice + 1);
			if (imp1.getType() == ImagePlus.GRAY16) {
				short[] sdata = (short[]) ip1.getPixels();
				for (int i1 = 0; i1 < width1; i1++) {
					for (int i2 = 0; i2 < height1; i2++) {
						ret[slice][i1][i2] = (double) cal1.getRawValue(sdata[i1 + i2 * width1]);
					}
				}
			}
			if (imp1.getType() == ImagePlus.GRAY32) {
				float[] sdata = (float[]) ip1.getPixels();
				for (int i1 = 0; i1 < width1; i1++) {
					for (int i2 = 0; i2 < height1; i2++) {
						ret[slice][i1][i2] = (double) cal1.getRawValue(sdata[i1 + i2 * width1]);
					}
				}
			}

		}
		return ret;
	}

	// public static ImagePlus stackFromFolder(String path) {
	//
	// DirectoryChooser od2 = new DirectoryChooser(
	// "SELEZIONARE LA CARTELLA IMMAGINI");
	// String filePath = od2.getDirectory();
	// if (filePath == null)
	// return null;
	//
	//
	// return null;
	// }

	// public ImagePlus extractZStack(ImagePlus imp1, int numberOfFrames,
	// int numberOfTemporalPositions) {
	//
	// int numberOfSlices = numberOfFrames / numberOfTemporalPositions;
	// ImageStack zStack = new ImageStack(imp1.getWidth(), imp1.getHeight());
	// for (int i1 = 0; i1 < numberOfSlices; i1++) {
	// }
	//
	// return null;
	// }
	//
	// public ImagePlus extractTimeStack(ImagePlus imp1, int slice) {
	// return null;
	// }

	/***
	 * Estrae l'immagine da un mosaico. Attenzione che la prima immagine dovra'
	 * essere la 0
	 * 
	 * @param imp1 Immagine mosaic
	 * @param num2 Numero della immagine da estrarre, si parte da 0 in alto a sx e
	 *             ci si muove prima verso destra
	 * @return Immgine estratta
	 */
	public static ImagePlus imageFromMosaic(ImagePlus imp1, int num) {

		// int step = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1,
		// MyConst.DICOM_PHASE_ENCODING_STEPS)); // 64 "0018,0089"

		String[] matrix2 = ReadDicom.parseString(ReadDicom.readDicomParameter(imp1, "0018,1310"));
		int step = ReadDicom.readInt(matrix2[0]);

		// int width = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1,
		// MyConst.DICOM_COLUMNS)); // 384
		// // "0028,0011"

		int width = imp1.getWidth();

		// int height = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1,
		// MyConst.DICOM_ROWS)); // 384
		// "0028,0010"
		int height = imp1.getHeight();

		int cropWidth = step;
		int cropHeight = step;
		int box = width / step;

		int rigona = (num / box); // 252
		int colonnona = num - rigona * box; // 131 (27)
		ImageProcessor ip2 = imp1.getProcessor();
		int startRiga = rigona * step;
		int startColonna = colonnona * step;

		imp1.setRoi(startColonna, startRiga, cropWidth, cropHeight);
		String title = "SUBMOSAIC " + num;
		ImagePlus imp3 = new ImagePlus(title, ip2.crop());
		return imp3;
	}

	/***
	 * Estrae l'immagine da un mosaico. Attenzione che la prima immagine dovra'
	 * essere la 0
	 * 
	 * @param imp1 Immagine mosaic
	 * @param num2 Numero della immagine da estrarre, si parte da 0 in alto a sx e
	 *             ci si muove prima verso destra
	 * @return Immgine estratta
	 */
	public static ImagePlus imageFromMosaic(ImagePlus imp1, int num, int step) {

		// int step = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1,
		// MyConst.DICOM_PHASE_ENCODING_STEPS)); // 64 "0018,0089"

		// String[] matrix2 =
		// ReadDicom.parseString(ReadDicom.readDicomParameter(imp1,
		// "0018,1310"));
		// int step = ReadDicom.readInt(matrix2[0]);

		// int width = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1,
		// MyConst.DICOM_COLUMNS)); // 384
		// // "0028,0011"

		int width = imp1.getWidth();

		// int height = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1,
		// MyConst.DICOM_ROWS)); // 384
		// "0028,0010"
		int height = imp1.getHeight();

		int cropWidth = step;
		int cropHeight = step;
		int box = width / step;

		int rigona = (num / box); // 252
		int colonnona = num - rigona * box; // 131 (27)
		ImageProcessor ip2 = imp1.getProcessor();
		int startRiga = rigona * step;
		int startColonna = colonnona * step;

		imp1.setRoi(startColonna, startRiga, cropWidth, cropHeight);
		String title = "SUBMOSAIC " + num;
		ImagePlus imp3 = new ImagePlus(title, ip2.crop());
		return imp3;
	}

	/***
	 * Estrae l'immagine da un mosaico. Attenzione che la prima immagine dovr�
	 * essere la 0 (questa routine serve esclusivamente a creare una immagine
	 * farlocca per testare il posizionamento automatico) NON USARE SE NON PER
	 * CREARE IMMAGINI FARLOCCHE
	 * 
	 * @param imp1 Immagine mosaic
	 * @param num2 Numero della immagine da estrarre, si parte da 0 in alto a sx e
	 *             ci si muove prima verso destra
	 * @return Immgine estratta
	 */
	public static ImagePlus imageFromMosaicWithOffset(ImagePlus imp1, int num, int offX, int offY) {

		int step = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1, MyConst.DICOM_PHASE_ENCODING_STEPS)); // 64

		int width = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1, MyConst.DICOM_COLUMNS)); // 384

		int height = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1, MyConst.DICOM_ROWS)); // 384

		int cropWidth = step;
		int cropHeight = step;
		int box = width / step;

		int rigona = (num / box); // 252
		int colonnona = num - rigona * box; // 131 (27)
		ImageProcessor ip2 = imp1.getProcessor();
		int startRiga = rigona * step + offX;
		int startColonna = colonnona * step + offY;

		imp1.setRoi(startColonna, startRiga, cropWidth, cropHeight);
		String title = "SUBMOSAIC " + num;
		ImagePlus imp3 = new ImagePlus(title, ip2.crop());
		return imp3;
	}

	/***
	 * Esegue la comparazione di due stack, ritorna true se sono uguali
	 * 
	 * @param imp1
	 * @param imp2
	 * @return
	 */
	public static boolean compareStacks(ImagePlus imp1, ImagePlus imp2) {
		boolean equality = true;
		int len1 = imp1.getImageStackSize();
		int len2 = imp2.getImageStackSize();

		if (len1 != len2)
			equality = false;
		// volendo posso complicarmi la vita andando a controllare che le
		// immagini siano una a una uguali
		for (int i1 = 0; i1 < len1; i1++) {
			ImagePlus imp3 = imageFromStack(imp1, i1 + 1);
			ImagePlus imp4 = imageFromStack(imp2, i1 + 1);
			boolean ok = UtilAyv.compareImagesByImageProcessors(imp3, imp4);
			if (!ok) {
				IJ.log("compareStacks.la immagine " + (i1 + 1) + " differisce");
				equality = false;
			}
		}
		return equality;
	}

	/***
	 * Restituisce, analogamente a RoiManager le ImageStatistics della Roi per ogni
	 * immagine dello Stack
	 * 
	 * @param stack
	 * @param roi1
	 * @return
	 */
	public static ImageStatistics[] stackStatistics(ImagePlus impStack, Roi roi1) {

		if (impStack == null) {
			IJ.log("imageFromStack.stack== null");
			return null;
		}
		ImageStack imaStack = impStack.getImageStack();
		if (imaStack == null) {
			IJ.log("imageFromStack.imaStack== null");
			return null;
		}
		ImageStatistics stat1[] = new ImageStatistics[imaStack.getSize()];
		impStack.setRoi(roi1);
		for (int i1 = 0; i1 < imaStack.getSize(); i1++) {
			ImagePlus imp1 = imageFromStack(impStack, i1 + 1);
			stat1[i1] = imp1.getStatistics();
		}
		return stat1;
	}

	/***
	 * Estrazione del valore dei pixel da uno stack a 16 bit e restituzione sotto
	 * forma di matrice a 3 dimensioni a 32 bit [slice][column[[row]
	 * 
	 * @param imp1 stack ImagePlus
	 * @return pixel matrix [slices][height][width]
	 */
	public static ImagePlus stackDiff(ImagePlus imp1, ImagePlus imp2) {

		ImageCalculator ic1 = new ImageCalculator();
		ImagePlus imp3 = ic1.run("Difference create 32-bit stack", imp1, imp2);
		return imp3;
	}

}
