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
		for (int i1 = 0; i1 < inArray.length - 1; i1++) {
			outArray[i1] = inArray[i1 + 1];
		}
		outArray[inArray.length - 1] = inArray[0];
		return outArray;
	}

	/**
	 * Calcola il max di un vettore
	 * 
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

	public static int posMax(double[] data) {
		int pos = -1;
		final int n = data.length;
		if (n < 1) {
			return -1;
		}
		double max = Double.MIN_VALUE;
		for (int i1 = 0; i1 < data.length; i1++) {
			if (data[i1] > max) {
				max = data[i1];
				pos = i1;
			}
		}
		return pos;
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

	public static int posMax(float[] data) {
		int pos = -1;
		final int n = data.length;
		if (n < 1) {
			return -1;
		}
		float max = Float.MIN_VALUE;
		for (int i1 = 0; i1 < data.length; i1++) {
			if (data[i1] > max) {
				max = data[i1];
				pos = i1;
			}
		}
		return pos;
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

	public static int posMax(int[] data) {
		int pos = -1;
		final int n = data.length;
		if (n < 1) {
			return -1;
		}
		int max = Integer.MIN_VALUE;
		for (int i1 = 0; i1 < data.length; i1++) {
			if (data[i1] > max) {
				max = data[i1];
				pos = i1;
			}
		}
		return pos;
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

	public static int posMax(short[] data) {
		int pos = -1;
		final int n = data.length;
		if (n < 1) {
			return -1;
		}
		short max = Short.MIN_VALUE;
		for (int i1 = 0; i1 < data.length; i1++) {
			if (data[i1] > max) {
				max = data[i1];
				pos = i1;
			}
		}
		return pos;
	}

	/**
	 * Calcola il min di un vettore
	 * 
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

	public static int posMin(double[] data) {
		int pos = -1;
		final int n = data.length;
		if (n < 1) {
			return -1;
		}
		double min = Double.MAX_VALUE;
		for (int i1 = 0; i1 < data.length; i1++) {
			if (data[i1] < min) {
				min = data[i1];
				pos = i1;
			}
		}
		return pos;
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

	public static int posMin(float[] data) {
		int pos = -1;
		final int n = data.length;
		if (n < 1) {
			return -1;
		}
		double min = Float.MAX_VALUE;
		for (int i1 = 0; i1 < data.length; i1++) {
			if (data[i1] < min) {
				min = data[i1];
				pos = i1;
			}
		}
		return pos;
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

	public static int posMin(int[] data) {
		int pos = -1;
		final int n = data.length;
		if (n < 1) {
			return -1;
		}
		int min = Integer.MAX_VALUE;
		for (int i1 = 0; i1 < data.length; i1++) {
			if (data[i1] < min) {
				min = data[i1];
				pos = i1;
			}
		}
		return pos;
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

	public static int pos(short[] data) {
		int pos = -1;
		final int n = data.length;
		if (n < 1) {
			return -1;
		}
		short min = Short.MAX_VALUE;
		for (int i1 = 0; i1 < data.length; i1++) {
			if (data[i1] < min) {
				min = data[i1];
				pos = i1;
			}
		}
		return pos;
	}

	/**
	 * Calcola la media di un vettore
	 * 
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
			sum += data[i1];
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
			sum += data[i1];
		}
		double mean = sum / data.length;
		return mean;
	}

	public static double vetMean(byte[] data) {
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

	public static double vetSd(double[] data) {
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
		return Math.sqrt(sum / (n));
	}

	public static double vetSd(float[] data) {
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
		return Math.sqrt(sum / (n));
	}

	public static double vetSd(int[] data) {
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
		return Math.sqrt(sum / (n));
	}

	public static double vetSd(short[] data) {
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
		return Math.sqrt(sum / (n));
	}

	public static int vetMedian(int[] data) {

		int[] sorted = ArrayUtils.vetSort(data);
		int median = sorted[sorted.length / 2];
		return median;
	}

	public static double vetMedian(double[] data) {

		double[] sorted = ArrayUtils.vetSort(data);
		double median = sorted[sorted.length / 2];
		return median;
	}

	public static int vetQuartile(int[] data, int num) {

		int[] sorted = ArrayUtils.vetSort(data);
		int len = sorted.length / 2;
		int out = 0;
		int[] aux = new int[len];
		if (num == 1) {
			for (int i1 = 0; i1 < len; i1++) {
				aux[i1] = sorted[i1];
			}
			out = vetMedian(aux);

		} else if (num == 3) {
			for (int i1 = 0; i1 < len; i1++) {
				aux[i1] = sorted[len + i1];
			}
			out = vetMedian(aux);

		} else {
			MyLog.waitHere("non previsto");
			return 999999;
		}
		return out;
	}

	public static double[] vetReverse(double[] profile1) {
		double[] vetreverse = new double[profile1.length];
		for (int i1 = 0; i1 < profile1.length; i1++) {
			vetreverse[profile1.length - 1 - i1] = profile1[i1];
		}
		return vetreverse;
	}

	public static double[] vetSort(double[] source) {
		double[] sorted = new double[source.length];
		for (int i1 = 0; i1 < source.length; i1++) {
			sorted[i1] = source[i1];
		}
		// effettuo minsort su key, gli altri campi andranno in parallelo
		double aux1 = 0;
		for (int i1 = 0; i1 < sorted.length; i1++) {
			for (int i2 = i1 + 1; i2 < sorted.length; i2++) {
				if (sorted[i2] < sorted[i1]) {
					aux1 = sorted[i1];
					sorted[i1] = sorted[i2];
					sorted[i2] = aux1;
				}
			}
		}
		return sorted;
	}

	public static int[] vetSort(int[] source) {
		int[] sorted = new int[source.length];
		for (int i1 = 0; i1 < source.length; i1++) {
			sorted[i1] = source[i1];
		}
		// effettuo minsort su key, gli altri campi andranno in parallelo
		int aux1 = 0;
		for (int i1 = 0; i1 < sorted.length; i1++) {
			for (int i2 = i1 + 1; i2 < sorted.length; i2++) {
				if (sorted[i2] < sorted[i1]) {
					aux1 = sorted[i1];
					sorted[i1] = sorted[i2];
					sorted[i2] = aux1;
				}
			}
		}
		return sorted;
	}

	public static double[] vetInvert(double[] profile1) {
		double max = ArrayUtils.vetMax(profile1);
		double[] vetinvert = new double[profile1.length];
		for (int i1 = 0; i1 < profile1.length; i1++) {
			vetinvert[i1] = max - profile1[i1];
		}
		return vetinvert;
	}

	public static double[] vetCopy(double[] source, double[] destination) {
		for (int i1 = 0; i1 < source.length; i1++) {
			destination[i1] = source[i1];
		}
		return destination;
	}
	
	public static boolean compareVectors(double[] vect1, double[] vect2, double precision, String msg) {
		if ((vect1 == null) || (vect2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning vector = null");
			}
			return false;
		}
		if (vect1.length != vect2.length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Vectors with different length");
			}
			return false;
		}
		for (int i1 = 0; i1 < vect1.length; i1++) {
			double diff = vect1[i1] - vect2[i1];
			if (Math.abs(diff) > precision) {
				return false;
			}
		}
		return true;
	}

	public static boolean compareVectors(float[] vect1, float[] vect2, float precision, String msg) {
		if ((vect1 == null) || (vect2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning vector = null");
			}
			return false;
		}
		if (vect1.length != vect2.length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Vectors with different length");
			}
			return false;
		}
		for (int i1 = 0; i1 < vect1.length; i1++) {
			double diff = vect1[i1] - vect2[i1];
			if (Math.abs(diff) > precision) {
				return false;
			}
		}
		return true;
	}

	public static boolean compareVectors(int[] vect1, int[] vect2, String msg) {
		if ((vect1 == null) || (vect2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning vector = null");
			}
			return false;
		}
		if (vect1.length != vect2.length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Vectors with different length");
			}
			return false;
		}
		for (int i1 = 0; i1 < vect1.length; i1++) {
			if (vect1[i1] != vect2[i1]) {
				return false;
			}
		}
		return true;
	}

	public static boolean compareVectors(long[] vect1, long[] vect2, String msg) {
		if ((vect1 == null) || (vect2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning vector = null");
			}
			return false;
		}
		if (vect1.length != vect2.length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Vectors with different length");
			}
			return false;
		}
		for (int i1 = 0; i1 < vect1.length; i1++) {
			if (vect1[i1] != vect2[i1]) {
				return false;
			}
		}
		return true;
	}

	public static boolean compareVectors(String[] vect1, String[] vect2, String msg) {
		if ((vect1 == null) || (vect2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning vector = null");
			}
			return false;
		}
		if (vect1.length != vect2.length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Vectors with different length");
			}
			return false;
		}
		for (int i1 = 0; i1 < vect1.length; i1++) {
			if (!vect1[i1].equals(vect2[i1])) {
				if (msg.length() > 0) {
					IJ.log(msg + " At pos " + i1 + " " + vect1[i1] + " differ " + vect2[i1]);
				}
				return false;
			}
		}
		return true;
	}

	public static boolean compareMatrix(double[][] mat1, double[][] mat2, String msg) {
		if ((mat1 == null) || (mat2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning matrix = null");
				return false;
			}
		}
		if (mat1.length != mat2.length || mat1[0].length != mat2[0].length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Matrices with different dimensions");
				return false;
			}
		}
		for (int i2 = 0; i2 < mat1[0].length; i2++) {
			for (int i1 = 0; i1 < mat1.length; i1++) {
				// IJ.log("compare
				// mat1["+i1+"]["+i2+"]="+mat1[i1][i2]+"\tmat2["+i1+"]["+i2+"]="+mat2[i1][i2]);
				if (mat1[i1][i2] != mat2[i1][i2]) {
					IJ.log("compareMatrix difference in " + i1 + "," + i2);
					return false;
				}
			}
		}
		return true;
	}

	public static boolean compareMatrix(float[][] mat1, float[][] mat2, String msg) {
		if ((mat1 == null) || (mat2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning matrix = null");
				return false;
			}
		}
		if (mat1.length != mat2.length || mat1[0].length != mat2[0].length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Matrices with different dimensions");
				return false;
			}
		}
		for (int i2 = 0; i2 < mat1[0].length; i2++) {
			for (int i1 = 0; i1 < mat1.length; i1++) {
				// IJ.log("compare
				// mat1["+i1+"]["+i2+"]="+mat1[i1][i2]+"\tmat2["+i1+"]["+i2+"]="+mat2[i1][i2]);
				if (mat1[i1][i2] != mat2[i1][i2]) {
					IJ.log("compareMatrix difference in " + i1 + "," + i2);
					return false;
				}
			}
		}
		return true;
	}

	public static boolean compareMatrix(int[][] mat1, int[][] mat2, String msg) {
		if ((mat1 == null) || (mat2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning matrix = null");
				return false;
			}
		}
		if (mat1.length != mat2.length || mat1[0].length != mat2[0].length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Matrices with different dimensions");
				return false;
			}
		}
		for (int i2 = 0; i2 < mat1[0].length; i2++) {
			for (int i1 = 0; i1 < mat1.length; i1++) {
				// IJ.log("compare
				// mat1["+i1+"]["+i2+"]="+mat1[i1][i2]+"\tmat2["+i1+"]["+i2+"]="+mat2[i1][i2]);
				if (mat1[i1][i2] != mat2[i1][i2]) {
					IJ.log("compareMatrix difference in " + i1 + "," + i2);
					return false;
				}
			}
		}
		return true;
	}

	public static boolean compareMatrix(String[][] mat1, String[][] mat2, String msg) {
		if ((mat1 == null) || (mat2 == null)) {
			if (msg.length() > 0) {
				IJ.log(msg + " Warning matrix = null");
				return false;
			}
		}
		if (mat1.length != mat2.length || mat1[0].length != mat2[0].length) {
			if (msg.length() > 0) {
				IJ.log(msg + " Matrices with different dimensions");
				return false;
			}
		}
		for (int i2 = 0; i2 < mat1[0].length; i2++) {
			for (int i1 = 0; i1 < mat1.length; i1++) {
				// IJ.log("compare
				// mat1["+i1+"]["+i2+"]="+mat1[i1][i2]+"\tmat2["+i1+"]["+i2+"]="+mat2[i1][i2]);
				if (!mat1[i1][i2].equals(mat2[i1][i2])) {
					IJ.log("compareMatrix difference in " + i1 + "," + i2);
					return false;
				}
			}
		}
		return true;
	}


}
