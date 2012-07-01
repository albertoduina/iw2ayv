package utils;

import org.junit.Test;

public class ReadVersionTest {

	@Test
	public final void testReadVersionInfo() {
		
		String pippo = ReadVersion.readVersionInfoInManifest("utils");
		MyLog.waitHere(pippo);
	}

}
