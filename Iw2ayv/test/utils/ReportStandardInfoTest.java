package utils;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ij.ImageJ;
import ij.ImagePlus;
import ij.io.Opener;

public class ReportStandardInfoTest {

	@Before
	public void setUp() throws Exception {
		new ImageJ(ImageJ.NORMAL);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void getSimpleStandardInfoTest() {
		String path1 = ".\\Test4\\003.dcm";
		String path2 = ".\\Test4\\004.dcm";
		ImagePlus imp1 = new Opener().openImage(path1);
		if (imp1 == null) {
			fail("Manca immagine");
		}

		//
		// Il test con PATH1, con elencata una sola bobina SP4,5 non va a buon fine,
		// viene stampata solo la bobina di ricezione seguita dal carattere "="
		//
		// Il test con PATH2, con elencate tutte le bobine BAL+BAR+BCL+BCR avviene
		// correttamente, viene stampata la bobina di ricezione seguita dal carattere
		// "=" e poi il codice degli elementi
		//

		String[][] tabCodici =

//			{ { "U1CS_", "2", "2", "0", "0", "0", "888", "999", "SP4,5+UFL", "vup", "30", "p11rmn_" },
//					{ "BR2S_", "2", "2", "0", "0", "0", "888", "999", "BAL+BAR+BCL+BCR", "vup", "30", "p11rmn_" } };

				{ { "U1CS_", "2", "2", "0", "0", "0", "888", "999", "SP4,5", "vup", "30", "p11rmn_" },
						{ "BR2S_", "2", "2", "0", "0", "0", "888", "999", "BAL+BAR+BCL+BCR", "vup", "30", "p11rmn_" } };

		String[] vetOut = ReportStandardInfo.getSimpleStandardInfo(path1, imp1, tabCodici, "version1", false);
		MyLog.logVector(vetOut, "vetOut");
		MyLog.waitHere();

	}

}
