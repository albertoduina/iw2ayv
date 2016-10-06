package utils;

import ij.IJ;

import java.util.ArrayList;
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
		int i1 = 0;
		for (Integer n : inArrayList) {
			outIntArr[i1++] = n;
		}
		return outIntArr;
	}

	
	public static float[] arrayListToArrayFloat(List<Float> inArrayList) {

		float[] outIntArr = new float[inArrayList.size()];
		int i1 = 0;
		for (Float n : inArrayList) {
			outIntArr[i1++] = n;
		}
		return outIntArr;
	}

	
	public static double[] arrayListToArrayDouble(List<Double> inArrayList) {

		double[] outIntArr = new double[inArrayList.size()];
		int i1 = 0;
		for (Double n : inArrayList) {
			outIntArr[i1++] = n;
		}
		return outIntArr;
	}

	
	
	
	public static double[] rotateArrayLeft(double[] inArray) {
		double[] outArray = new double[inArray.length];
		for (int i1 = 0; i1 < inArray.length-1; i1++) {
			outArray[i1] = inArray[i1+1];
		}
		outArray[inArray.length-1]=inArray[0];
		return outArray;
	}

	
	
	
//	public static String[][] bubbleSortMulti(String[][] MultiIn, int compIdx) {
//	       String[][] temp = new String[MultiIn.length][MultiIn[0].length];
//	       boolean finished = false;
//
//	       while (!finished) {
//	         finished = true;
//	         for (int i = 0; i < MultiIn.length - 1; i++) {
//	            if (MultiIn[i][compIdx].compareToIgnoreCase(MultiIn[i + 1][compIdx]) > 0) {
//	              for (int j = 0; j < MultiIn[i].length; j++) {
//	            	  MyLog.waitHere();
//	                 temp[i][j] = MultiIn[i][j];
//	                 MultiIn[i][j] = MultiIn[i + 1][j];
//	                 MultiIn[i + 1][j] = temp[i][j];
//	              }
//	              finished = false;
//	            }
//	          }
//	       }
//	       return MultiIn;
//	}   
//	/**
//	 * Bubble sort on a 2D array
//	 * @param myArray
//	 * @param compareIndex
//	 * @param increase
//	 * @return
//	 */
//	public static String[][] bubbleSortMulti(String[][] myArray,
//			int compareIndex) {
//		String[][] temp = new String[myArray.length][myArray[0].length];
//		boolean finished = false;
//		while (!finished) {
//			finished = true;
//			for (int i1 = 0; i1 < myArray.length - 1; i1++) {
//				if (myArray[i1][compareIndex]
//						.compareToIgnoreCase(myArray[i1 + 1][compareIndex]) > 0) {
//					for (int i2 = 0; i2 < myArray[i1].length; i1++) {
//						temp[i1][i2] = myArray[i1][i2];
//						myArray[i1][i2] = myArray[i1 + 1][i2];
//						myArray[i1 + 1][i2] = temp[i1][i2];
//					}
//					finished = false;
//				}
//			}
//		}
//		return myArray;
//	}

//	public static int[][] bubbleSortMulti(int[][] myArray, int compareIndex) {
//		int[][] temp = new int[myArray.length][myArray[0].length];
//		boolean finished = false;
//		while (!finished) {
//			finished = true;
//			for (int i1 = 0; i1 < myArray[i1].length - 1; i1++) {
//				if (myArray[i1][compareIndex] > (myArray[i1 + 1][compareIndex])) {
//					for (int i2 = 0; i2 < myArray[i1].length; i1++) {
//						temp[i1][i2] = myArray[i1][i2];
//						myArray[i1][i2] = myArray[i1 + 1][i2];
//						myArray[i1 + 1][i2] = temp[i1][i2];
//					}
//					finished = false;
//				}
//			}
//		}
//		return myArray;
//	}
//
//	public static double[][] bubbleSortMulti(double[][] myArray, int compareIndex) {
//		double[][] temp = new double[myArray.length][myArray[0].length];
//		boolean finished = false;
//		while (!finished) {
//			finished = true;
//			for (int i1 = 0; i1 < myArray[i1].length - 1; i1++) {
//				if (myArray[i1][compareIndex] > (myArray[i1 + 1][compareIndex])) {
//					for (int i2 = 0; i2 < myArray[i1].length; i1++) {
//						temp[i1][i2] = myArray[i1][i2];
//						myArray[i1][i2] = myArray[i1 + 1][i2];
//						myArray[i1 + 1][i2] = temp[i1][i2];
//					}
//					finished = false;
//				}
//			}
//		}
//		return myArray;
//	}

}
