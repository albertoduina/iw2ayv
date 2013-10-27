package utils;



import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



import utils.AboutBox;




public class AboutBoxTest {

	@Before
	public void setUp() throws Exception {
	}

	
	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testAboutBox() {
		
//		new AboutBox().about("Messaggio", this.getClass());	
		new AboutBox().about("Messaggio", MyVersionUtils.getVersion());	
	}

	
	@Test
	public void testAboutBox1() {
		
//		new AboutBox().about("Messaggio", this.getClass());
		String[] vetMsg = {"Prima riga", "Seconda riga", "Terza riga", "Quarta riga", "Quinta riga", "Sesta riga", "Settima riga", "Ottava riga"};
		new AboutBox().about(vetMsg, MyVersionUtils.getVersion());	
	}

	

	@Test
	public void testAboutBox2() {
		
		new AboutBox().about2("Riga unica");	
	}

	@Test
	public void testAboutBox5() {
		
		MyCircleDetector mcd = new MyCircleDetector();
		new AboutBox().about5("Messaggio", mcd.getClass());
	}
	
	@Test
	public void testAboutBox10() {
		
		new AboutBox().about10("Messaggio", this.getClass());	
	}

}

