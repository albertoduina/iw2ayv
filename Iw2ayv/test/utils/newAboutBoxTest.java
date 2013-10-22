package utils;

import junit.framework.TestCase;

public class newAboutBoxTest extends TestCase {

	public newAboutBoxTest(String name) {
		super(name);
	}

//	protected void setUp() throws Exception {
//		super.setUp();
//	}
//
//	protected void tearDown() throws Exception {
//		super.tearDown();
//	}

	
	public void testAboutBox() {
		
//		new AboutBox().about("Messaggio", this.getClass());	
		new AboutBox().about("Messaggio", MyVersionUtils.getVersion());	
	}

}
