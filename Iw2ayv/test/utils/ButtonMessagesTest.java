package utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utils.ButtonMessages;

public class ButtonMessagesTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testModelessMsg2zero() {
		ButtonMessages.ModelessMsg2(
				"Scegliere modo funzionamento AUTOM o un PASSO alla volta",
				"AUTOM", "PASSO", "PROVA", "ABOUT", "CHIUDI", 0);
	}

	@Test
	public final void testModelessMsg2uno() {
		ButtonMessages.ModelessMsg2(
				"Scegliere modo funzionamento AUTOM o un PASSO alla volta",
				"AUTOM", "PASSO", "PROVA", "ABOUT", "CHIUDI", 1);
	}

	@Test
	public final void testModelessMsg0() {
		ButtonMessages.ModelessMsg("UNO", "DUE");
	}
	
	@Test
	public final void testModelessMsg1() {
		ButtonMessages.ModelessMsg("UNO", "DUE", "TRE");
	}

	@Test
	public final void testModelessMsg2() {
		ButtonMessages.ModelessMsg("UNO", "DUE", "TRE", "QUATTRO");
	}
	@Test
	public final void testModelessMsg3() {
		ButtonMessages.ModelessMsg("UNO", "DUE", "TRE", "QUATTRO", "CINQUE");
	}
	@Test
	public final void testModelessMsg4() {
		ButtonMessages.ModelessMsg("UNO", "DUE", "TRE", "QUATTRO", "CINQUE", "SEI");
	}

}
