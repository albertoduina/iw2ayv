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

	
	
/**
 * Calcola il max di un vettore	
 * @param data
 * @return
 */
	public static double vetMax(double[] data) {
		final int n = data.length;
		if (n < 1) {
			return Double.NaN;
		}
		double max = Double.MIN_VALUE;
		for (int i1 = 0; i1 < data.length; i1++) {
			if (data[i1] > max) {
				max = data[i1];
			}
		}
		return max;
	}
	public static float vetMax(float[] data) {
		final int n = data.length;
		if (n < 1) {
			return Float.NaN;
		}
		float max = Float.MIN_VALUE;
		for (int i1 = 0; i1 < data.length; i1++) {
			if (data[i1] > max) {
				max = data[i1];
			}
		}
		return max;
	}
	public static int vetMax(int[] data) {
		final int n = data.length;
		if (n < 1) {
			return Integer.MIN_VALUE;
		}
		int max = Integer.MIN_VALUE;
		for (int i1 = 0; i1 < data.length; i1++) {
			if (data[i1] > max) {
				max = data[i1];
			}
		}
		return max;
	}
	public static short vetMax(short[] data) {
		final int n = data.length;
		if (n < 1) {
			return Short.MIN_VALUE;
		}
		short max = Short.MIN_VALUE;
		for (int i1 = 0; i1 < data.length; i1++) {
			if (data[i1] > max) {
				max = data[i1];
			}
		}
		return max;
	}

	/**
	 * Calcola il min di un vettore	
	 * @param data
	 * @return
	 */
		public static double vetMin(double[] data) {
			final int n = data.length;
			if (n < 1) {
				return Double.NaN;
			}
			double min = Double.MAX_VALUE;
			for (int i1 = 0; i1 < data.length; i1++) {
				if (data[i1] < min) {
					min = data[i1];
				}
			}
			return min;
		}
		public static double vetMin(float[] data) {
			final int n = data.length;
			if (n < 1) {
				return Float.NaN;
			}
			double min = Float.MAX_VALUE;
			for (int i1 = 0; i1 < data.length; i1++) {
				if (data[i1] < min) {
					min = data[i1];
				}
			}
			return min;
		}
		public static int vetMin(int[] data) {
			final int n = data.length;
			if (n < 1) {
				return Integer.MAX_VALUE;
			}
			int min = Integer.MAX_VALUE;
			for (int i1 = 0; i1 < data.length; i1++) {
				if (data[i1] < min) {
					min = data[i1];
				}
			}
			return min;
		}
		public static short vetMin(short[] data) {
			final int n = data.length;
			if (n < 1) {
				return Short.MAX_VALUE;
			}
			short min = Short.MAX_VALUE;
			for (int i1 = 0; i1 < data.length; i1++) {
				if (data[i1] < min) {
					min = data[i1];
				}
			}
			return min;
		}
		
		/**
		 * Calcola la media di un vettore	
		 * @param data
		 * @return
		 */
		
		public static double vetMean(double[] data) {
			final int n = data.length;
			if (n < 1) {
				return Double.NaN;
			}
			double sum = 0;
			for (int i1 = 0; i1 < data.length; i1++) {
				sum += data[i1];
			}
			double mean = sum / data.length;
			return mean;
		}
		public static double vetMean(float[] data) {
			final int n = data.length;
			if (n < 1) {
				return Float.NaN;
			}
			double sum = 0;
			for (int i1 = 0; i1 < data.length; i1++) {
				sum += data[i1];
			}
			double mean = sum / data.length;
			return mean;
		}
		public static double vetMean(int[] data) {
			final int n = data.length;
			if (n < 1) {
				return Double.NaN;
			}
			double sum = 0;
			for (int i1 = 0; i1 < data.length; i1++) {
				sum += (double) data[i1];
			}
			double mean = sum / data.length;
			return mean;
		}
		public static double vetMean(short[] data) {
			final int n = data.length;
			if (n < 1) {
				return Double.NaN;
			}
			double sum = 0;
			for (int i1 = 0; i1 < data.length; i1++) {
				sum += (double) data[i1];
			}
			double mean = sum / data.length;
			return mean;
		}

		/**
		 * Calculates the standard deviation of an array of numbers. see Knuth's The
		 * Art Of Computer Programming Volume II: Seminumerical Algorithms This
		 * algorithm is slower, but more resistant to error propagation.
		 * 
		 * @param data
		 *            Numbers to compute the standard deviation of. Array must
		 *            contain two or more numbers.
		 * @return standard deviation estimate of population ( to get estimate of
		 *         sample, use n instead of n-1 in last line )
		 */
		public static double vetSdKnuth(double[] data) {
			final int n = data.length;
			if (n < 2) {
				return Double.NaN;
			}
			double avg = data[0];
			double sum = 0;
			// yes, i1 below starts from 1
			for (int i1 = 1; i1 < data.length; i1++) {
				double newavg = avg + (data[i1] - avg) / (i1 + 1);
				sum += (data[i1] - avg) * (data[i1] - newavg);
				avg = newavg;
			}
			return Math.sqrt(sum / (n - 1));
		}
		public static double vetSdKnuth(float[] data) {
			final int n = data.length;
			if (n < 2) {
				return Float.NaN;
			}
			double avg = (double) data[0];
			double sum = 0;
			// yes, i1 below starts from 1
			for (int i1 = 1; i1 < data.length; i1++) {
				double newavg = avg + (data[i1] - avg) / (i1 + 1);
				sum += (data[i1] - avg) * (data[i1] - newavg);
				avg = newavg;
			}
			return Math.sqrt(sum / (n - 1));
		}
		public static double vetSdKnuth(int[] data) {
			final int n = data.length;
			if (n < 2) {
				return Double.NaN;
			}
			double avg = (double) data[0];
			double sum = 0;
			// yes, i1 below starts from 1
			for (int i1 = 1; i1 < data.length; i1++) {
				double newavg = avg + (data[i1] - avg) / (i1 + 1);
				sum += (data[i1] - avg) * (data[i1] - newavg);
				avg = newavg;
			}
			return Math.sqrt(sum / (n - 1));
		}
		public static double vetSdKnuth(short[] data) {
			final int n = data.length;
			if (n < 2) {
				return Double.NaN;
			}
			double avg = (double) data[0];
			double sum = 0;
			// yes, i1 below starts from 1
			for (int i1 = 1; i1 < data.length; i1++) {
				double newavg = avg + (data[i1] - avg) / (i1 + 1);
				sum += (data[i1] - avg) * (data[i1] - newavg);
				avg = newavg;
			}
			return Math.sqrt(sum / (n - 1));
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
