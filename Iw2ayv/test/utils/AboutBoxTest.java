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
	public void testAboutBox10() {
		
		new AboutBox().about10("Messaggio", this.getClass());	
	}

}

