package utils;

import ij.IJ;
import ij.gui.Plot;
import ij.gui.WaitForUserDialog;

import java.awt.Color;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utils.MyLog;

public class MyPlotTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testBasePlotTest() {

		double[] profile1 = InputOutput
				.readDoubleArrayFromFile((new InputOutput()
						.findResource("002.txt")));
	//	MyLog.logVector(profile1, "profile1");

		String title = "P R O V A";
		Color color = Color.red;

		Plot plot = MyPlot.basePlot(profile1, title, color);
		plot.show();

		// new WaitForUserDialog("premere OK.").show();
		IJ.wait(100);

	}
	
	@Test
	public final void testBasePlot2Test() {

		double[][] profile2 = InputOutput
				.readDoubleMatrixFromFile((new InputOutput()
						.findResource("profile4d.txt")));
		
		
		MyLog.waitHere("profile2.length= "+profile2.length+ " profile2[0].length= "+profile2[0].length);
		
		
		
	double[][] profile1 = TableUtils.rotateTable(profile2);
	double[] vetXpoints = {20.4, 101.8, 30,7};
	
	
	//	MyLog.logVector(profile1, "profile1");

		String title = "P R O V A";
		Color color = Color.red;

		Plot plot = MyPlot.basePlot2(profile1,vetXpoints,  title, color);
		plot.show();

		MyLog.waitHere();
		// new WaitForUserDialog("premere OK.").show();
		IJ.wait(100);

	}

}
