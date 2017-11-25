package utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ij.ImageJ;
import ij.ImagePlus;

public class MyColorTest {
	@Before
	public void setUp() throws Exception {
		new ImageJ(ImageJ.NORMAL);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testCreateStripRGB() {

		ImagePlus aux1 = ImageUtils.createStripRGB(MyColor.myRed());
		aux1.show();
		ImagePlus aux2 = ImageUtils.createStripRGB(MyColor.myGreen());
		aux2.show();
		ImagePlus aux3 = ImageUtils.createStripRGB(MyColor.myOrange());
		aux3.show();
		ImagePlus aux4 = ImageUtils.createStripRGB(MyColor.myYellow());
		aux4.show();
		ImagePlus aux5 = ImageUtils.createStripRGB(MyColor.myBlue());
		aux5.show();
		ImagePlus aux6 = ImageUtils.createStripRGB(MyColor.myPink());
		aux6.show();

		MyLog.waitHere();

	}

	
	
	
}