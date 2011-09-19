package utils;

import java.awt.Color;
import java.awt.Rectangle;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.gui.OvalRoi;
import ij.gui.Roi;
import ij.gui.WaitForUserDialog;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CustomCanvasGenericTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testDrawNumbers1() {

		String path1 = "./Test4/B003_testP2";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, true);

		CustomCanvasGeneric ccg1 = new CustomCanvasGeneric(imp1);
		double[] cx1 = { 125, 135, 125, 115 };
		double[] cy1 = { 120, 130, 140, 130 };
		ccg1.setPosition1(cx1, cy1);
		ccg1.setColor1(Color.red);
		ImageWindow iw1 = new ImageWindow(imp1, ccg1);
		iw1.maximize();
		// new WaitForUserDialog(
		// "Verificare se appaiono i numeri 1,2,3,4 rossi al centro, poi premere OK.")
		// .show();
		IJ.wait(1000);
	}

	@Test
	public final void testDrawNumbers2() {

		String path1 = "./Test4/B003_testP2";
		ImagePlus imp1 = UtilAyv.openImageMaximized(path1);
		boolean circular = true;
		UtilAyv.presetRoi(imp1, 100, -50, -50, circular);
		Roi roi2 = imp1.getRoi();
		Rectangle rec2 = roi2.getBounds();
		imp1.setRoi(new OvalRoi(0, 0, 0, 0));
		double bx = rec2.getX();
		double by = rec2.getY();
		double bw = rec2.getWidth() / imp1.getWidth();
		CustomCanvasGeneric ccg1 = new CustomCanvasGeneric(imp1);
		double[] cx1 = { 245, 105, 245, 380, 175, 310, 75, 160, 335, 425, 175,
				310, 105, 245, 380, 245 };
		double[] cy1 = { 40, 95, 130, 95, 160, 160, 230, 230, 230, 230, 305,
				305, 370, 330, 370, 430 };
		double[] cx2 = { 250, 270, 250, 230 };
		double[] cy2 = { 240, 260, 280, 260 };
		ccg1.setOffset(bx, by, bw);
		ccg1.setPosition1(cx1, cy1);
		ccg1.setColor1(Color.red);
		ccg1.setPosition2(cx2, cy2);
		ccg1.setColor2(Color.green);
		ImageWindow iw1 = new ImageWindow(imp1, ccg1);
		iw1.maximize();
		// new WaitForUserDialog("Do something, then click OK.").show();
		IJ.wait(1000);
	}

	@Test
	public final void testDrawGrid() {

		int gridNumber = 10;
		String path1 = "./Test4/B003_testP2";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, true);
		CustomCanvasGeneric ccg1 = new CustomCanvasGeneric(imp1);
		ccg1.setGridElements(gridNumber);
		ImageWindow iw1 = new ImageWindow(imp1, ccg1);
		iw1.maximize();
		// new WaitForUserDialog("Do something, then click OK.").show();
		IJ.wait(1000);
	}

	@Test
	public final void testDrawCircles() {

		int elements = 10;
		String path1 = "./Test4/B003_testP2";
		ImagePlus imp1 = UtilAyv.openImageNoDisplay(path1, true);

		// int iX=imp1.getWidth()/2;
		// int iY=imp1.getHeight()/2;

		int iX = 17;
		int iY = 20;
		int iR = 220; // dimensione del fantoccio (spero)

		imp1.setRoi(new OvalRoi(iX, iY, iR, iR));

		CustomCanvasGeneric ccg1 = new CustomCanvasGeneric(imp1);
		ccg1.setCircleElements(elements);
		ccg1.setGridElements(0);
		ImageWindow iw1 = new ImageWindow(imp1, ccg1);
		iw1.maximize();
		// new WaitForUserDialog("Do something, then click OK.").show();
		IJ.wait(1000);
	}

}
