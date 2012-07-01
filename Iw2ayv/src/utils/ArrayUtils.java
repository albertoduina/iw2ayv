package utils;

import java.util.List;

/***
 * Array utilities
 * 
 * @author Alberto Duina - SPEDALI CIVILI DI BRESCIA - Servizio di Fisica
 *         Sanitaria
 *
 */

public class ArrayUtils {
	/**
	 * Conversion from arrayList<String> to String[]
	 * 
	 * @param inArrayList
	 *            arrayList input
	 * @return String[] output
	 */
	public static String[] arrayListToArrayString(List<String> inArrayList) {
		Object[] objArr = inArrayList.toArray();
		String[] outStrArr = new String[objArr.length];
		for (int i1 = 0; i1 < objArr.length; i1++) {
			outStrArr[i1] = objArr[i1].toString();
		}
		return outStrArr;
	}

	/**
	 * Conversion from arrayList<Integer> to int[]
	 * 
	 * @param inArrayList
	 *            arrayList input
	 * @return String[] output
	 */
	public static int[] arrayListToArrayInt(List<Integer> inArrayList) {
		int[] outIntArr = new int[inArrayList.size()];
		int i1=0;
		for (Integer n : inArrayList) {
			outIntArr[i1++] = n;
		}
		return outIntArr;
	}




}
