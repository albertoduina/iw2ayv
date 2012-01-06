package utils;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.NewImage;
import ij.io.FileSaver;
import ij.io.Opener;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;
import ij.process.ShortProcessor;

public class MyStackUtils {

	/**
	 * estrae una singola slice da uno stack. Estrae anche i dati header
	 * 
	 * @param stack
	 *            stack contenente le slices
	 * @param slice
	 *            numero della slice da estrarre, deve partire da 1, non è
	 *            ammesso lo 0
	 * @return ImagePlus della slice estratta
	 */
	/**
	 * estrae una singola slice da uno stack. Estrae anche i dati header
	 * 
	 * @param stack
	 *            stack contenente le slices
	 * @param slice
	 *            numero della slice da estrarre, deve partire da 1, non è
	 *            ammesso lo 0
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

		String titolo = "** "+slice+" **";
//		String titolo = imaStack.getShortSliceLabel(slice);
		String sliceInfo1 = imaStack.getSliceLabel(slice);
	
		ImagePlus imp = new ImagePlus(titolo, ipStack);
		imp.setProperty("Info", sliceInfo1);
		return imp;
	}

	/**
	 * -- Builds a stack
	 * 
	 * @param path
	 *            path of image files
	 * @return ImagePlus with the stack
	 */
	public static ImagePlus imagesToStack16(String[] path) {
		Opener opener1 = new Opener();
		ImagePlus imp1 = opener1.openImage(path[0]);
		int height = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1,
				MyConst.DICOM_ROWS));
		int width = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1,
				MyConst.DICOM_COLUMNS));
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

	/**
	 * -- Builds a stack
	 * 
	 * @param path
	 *            path of image files
	 * @return ImagePlus with the stack
	 */
	public static ImagePlus imagesToStack32(String[] path) {
		Opener opener1 = new Opener();
		ImagePlus imp1 = opener1.openImage(path[0]);
		int height = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1,
				MyConst.DICOM_ROWS));
		int width = ReadDicom.readInt(ReadDicom.readDicomParameter(imp1,
				MyConst.DICOM_COLUMNS));
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

	public ImagePlus extractZStack(ImagePlus imp1, int numberOfFrames,
			int numberOfTemporalPositions) {

		int numberOfSlices = numberOfFrames / numberOfTemporalPositions;
		ImageStack zStack = new ImageStack(imp1.getWidth(), imp1.getHeight());
		for (int i1 = 0; i1 < numberOfSlices; i1++) {
		}

		return null;
	}

	public ImagePlus extractTimeStack(ImagePlus imp1, int slice) {
		return null;
	}

}
