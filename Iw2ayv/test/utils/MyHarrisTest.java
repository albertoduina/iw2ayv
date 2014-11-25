package utils;

import static org.junit.Assert.assertTrue;
import ij.IJ;
import ij.ImagePlus;
import ij.io.Opener;
import ij.process.ImageProcessor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MyHarrisTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testMyHarris() {

		String path1 = ".\\Test4\\HWSA_.tif";
		ImagePlus imp1 = new Opener().openImage(path1);
		ImageProcessor ip1 = imp1.getProcessor();
		ip1.rotate(10.0);
		UtilAyv.showImageMaximized(imp1);
		MyHarris mh1 = new MyHarris();
		ImagePlus imp2 = mh1.execute(imp1);
		UtilAyv.showImageMaximized(imp2);
		MyLog.waitHere("ruotato 10");
		ip1.rotate(20.0);
		imp1.updateAndDraw();
		imp2 = mh1.execute(imp1);
		imp2.updateAndDraw();
		UtilAyv.showImageMaximized(imp2);
		MyLog.waitHere("ruotato 20");
		imp1.flush();
		imp2.flush();
		
		path1 = "C:\\Dati\\_____P17\\DESENZANO\\NUOVA\\22_DESENZANO_NUOVA_HWSA3";
		imp1 = new Opener().openImage(path1);
		ip1 = imp1.getProcessor();
		ip1.rotate(10.0);
		UtilAyv.showImageMaximized(imp1);
		imp2 = mh1.execute(imp1);
		UtilAyv.showImageMaximized(imp2);
		MyLog.waitHere("ruotato 10");
		ip1.rotate(20.0);
		imp1.updateAndDraw();
		imp2 = mh1.execute(imp1);
		imp2.updateAndDraw();
		UtilAyv.showImageMaximized(imp2);
		MyLog.waitHere("ruotato 20");

		
		
	}

}
