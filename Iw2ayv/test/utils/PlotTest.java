package utils;

import ij.IJ;
import ij.gui.Plot;
import ij.gui.WaitForUserDialog;

import java.awt.Color;

import org.junit.Test;

import utils.MyLog;

public class PlotTest {

	@Test
	public final void testMyPlotTest() {

		double[] profile1 = InputOutput
				.readDoubleArrayFromFile((new InputOutput()
						.findResource("/002.txt")));
		MyLog.logVector(profile1, "profile1");

		String title = "P R O V A";
		Color color = Color.red;

		Plot plot = MyPlot.basePlot(profile1, title, color);
		plot.show();

		// new WaitForUserDialog("premere OK.").show();
		IJ.wait(1000);

	}

}
