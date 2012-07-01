package utils;

import static org.junit.Assert.fail;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.OvalRoi;
import ij.gui.Plot;
import ij.gui.Roi;
import ij.gui.WaitForUserDialog;
import ij.io.Opener;
import ij.process.ImageProcessor;

import java.awt.Color;

import org.junit.Test;

import utils.MyLog;

public class MyOvalProfileTest {

	@Test
	public final void testMyOvalProfileTest() {

		// apertura immagine
		// String path1 = ".\\Test4\\01";
		String path1 = ".\\Test4\\aaa.dcm";
		ImagePlus imp1 = new Opener().openImage(path1);
		if (imp1 == null) {
			fail("Manca immagine");
		}

		double rr = 180;
		double xx = imp1.getWidth() / 2 - rr / 2 + 30;
		double yy = imp1.getHeight() / 2 - rr / 2;

		// sovrapposizione ROI circolare
		imp1.setRoi(new OvalRoi(xx, yy, rr, rr));
		imp1.show();

		Roi roi1 = imp1.getRoi();

		double[] profile1 = MyOvalProfile.getOvalProfile(imp1, 300, 720);

		String title = "P R O V A";
		Color color = Color.red;

		Plot plot = MyPlot.basePlot(profile1, title, color);
		plot.show();

		new WaitForUserDialog("premere OK.").show();
		// IJ.wait(1000);

	}

}
