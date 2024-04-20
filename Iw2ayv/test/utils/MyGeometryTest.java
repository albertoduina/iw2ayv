package utils;

import static org.junit.Assert.assertTrue;
import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageProcessor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MyGeometryTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testFromPointsToEquLineExplicit() {

		double x1 = 0;
		double y1 = 128;
		double x2 = 255;
		double y2 = 0;
		double[] out1 = null;

		// x1 = 0;
		// y1 = 0;
		// x2 = 0;
		// y2 = 255;

		out1 = MyGeometry.fromPointsToEquLineExplicit(x1, y1, x2, y2);

		// MyLog.logVector(out1, "out1");
		double[] expected = { -0.5019607843137255, 128.0 };
		assertTrue(UtilAyv.compareVectors(out1, expected, 1e-11, ""));

	}

	@Test
	public final void testFromPointsToEquLineImplicit() {

		double x1 = 0;
		double y1 = 0;
		double x2 = 255;
		double y2 = 0;
		double[] out1 = null;

		x1 = 120;
		y1 = 30;
		x2 = 120;
		y2 = 50;

		out1 = MyGeometry.fromPointsToEquLineImplicit(x1, y1, x2, y2);

		// MyLog.logVector(out1, "out1");
		double[] expected = { -20.0, 0.0, 2400.0 };
		assertTrue(UtilAyv.compareVectors(out1, expected, 1e-11, ""));
	}

	@Test
	public final void testFromPointsToEquCirconferenceImplicit() {

		double cx = 150;
		double cy = 186;
		double radius = 120;

		double[] circonference = MyGeometry
				.fromPointsToEquCirconferenceImplicit(cx, cy, radius);
		double[] expected = { -300.0, -372.0, 42696.0 };
		assertTrue(UtilAyv.compareVectors(circonference, expected, 1e-11, ""));
	}



	@Test
	public final void testPointToLineDistance() {

		double x1 = 191;
		double y1 = 323;
		double x2 = 338;
		double y2 = 180;
		double xp = 239;
		double yp = 204;


		double dist= MyGeometry.pointToLineDistance(x1,y1,x2,y2,xp,yp);
		MyLog.waitHere("distanza calcolata= "+dist+" fatto a manina mi d� 50.2 pixels");
		
	}


}
