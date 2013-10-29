package utils;

import org.junit.After;
import org.junit.Before;

import junit.framework.TestCase;

public class newAboutBoxTest extends TestCase {
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


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
