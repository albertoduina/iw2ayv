package utils;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import ij.IJ;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;






import utils.AboutBox;




public class AutoDialogTest {

	@Before
	public void setUp() throws Exception {
	}

	
	@After
	public void tearDown() throws Exception {
	}


	@Test
	public final void testAutoDialog() {

		boolean modal= true;
		String[] vetPulsanti= {"uno", "due", "tre","quattro"};
		AutoDialog ad1 = new AutoDialog("domanda", vetPulsanti, "due", modal);
		int risposta = ad1.answer();
		ad1.dispose();
		System.out.println("Hai scelto " + risposta);
	}

	

}

