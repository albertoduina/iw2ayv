package utils;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;

public class MyDirectoryUtilsTest {
	@Before
	public void setUp() throws Exception {
		new ImageJ(ImageJ.NORMAL);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testListFiles() {
		String path1 = ".\\TEST_SET_1\\";
		ArrayList<String> arrayList1 = MyDirectoryUtils.listFiles(path1);
		int len = arrayList1.size();
		assertTrue(len == 20);
	}

	@Test
	public final void testListSorted() {
		String path1 = ".\\TEST_SET_1\\";
		String[] list1 = MyDirectoryUtils.listSorted(path1);
		IJ.log("----");
		for (int i1 = 0; i1 < list1.length; i1++) {
			IJ.log("list1 " + i1 + " " + list1[i1]);
		}
		IJ.log("----");
		MyLog.waitHere();

	}

	@Test
	public final void testListSlicePosition() {
		String path1 = ".\\TEST_SET_1\\";
		String[] list1 = MyDirectoryUtils.listSorted(path1);
		String[] list2 = MyDirectoryUtils.listSlicePosition(list1);
		IJ.log("----");
		for (int i1 = 0; i1 < list1.length; i1++) {
			IJ.log("list1 " + i1 + " " + list1[i1] + " slicePos= " + list2[i1]);
		}
		IJ.log("----");
		MyLog.waitHere();
	}

}