package utils;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;

/***
 * Copied with some modifications from "Autothreshold segmentation" ImageJ
 * plugin by G. Landini at bham. ac. uk
 * 
 * @author alberto
 *
 */
public class MyAutoThreshold {

	public static ImagePlus threshold(ImagePlus imp2, String myMethod,
			boolean noBlack, boolean noWhite, boolean doWhite, boolean doSet,
			boolean doLog) {

		int threshold = -1;
		ImagePlus imp1 = imp2.duplicate();
		ImageProcessor ip1 = imp1.getProcessor();
		int[] data = (ip1.getHistogram());

		if (noBlack)
			data[0] = 0;
		if (noWhite)
			data[data.length - 1] = 0;

		// bracket the histogram to the range that holds data to make it quicker
		int minbin = -1, maxbin = -1;
		for (int i = 0; i < data.length; i++) {
			if (data[i] > 0)
				maxbin = i;
		}
		for (int i = data.length - 1; i >= 0; i--) {
			if (data[i] > 0)
				minbin = i;
		}
		// IJ.log (""+minbin+" "+maxbin);
		int[] data2 = new int[(maxbin - minbin) + 1];
		for (int i = minbin; i <= maxbin; i++) {
			data2[i - minbin] = data[i];
		}

		// Apply the selected algorithm
		if (data2.length < 2) {
			threshold = 0;
		}

		int c = 0;
		int b = imp2.getBitDepth() == 8 ? 255 : 65535;
		if (doWhite) {
			c = b;
			b = 0;
		}

		imp1.setDisplayRange(0, Math.max(b, c)); // otherwise we can never set
													// the threshold
		if (data2.length < 2) {
			threshold = 0;
		} else if (myMethod.equals("Mean")) {
			threshold = Mean(data2);
		} else if (myMethod.equals("Huang")) {
			threshold = Huang(data2);
		} else if (myMethod.equals("Li")) {
			threshold = Li(data2);
		} else if (myMethod.equals("Intermodes")) {
			threshold = Intermodes(data2);
		} else if (myMethod.equals("RenyiEntropy")) {
			threshold = RenyiEntropy(data2);
		} else if (myMethod.equals("Minimum")) {
			threshold = Minimum(data2);			
		} else if (myMethod.equals("Moments")) {
			threshold = Moments(data2);			
		}

		// MyLog.waitHere("threshold= " + threshold);

		threshold += minbin; // add the offset of the histogram

		// show treshold in log window if required
		if (doLog)
			IJ.log("threshold= " + threshold);
		if (threshold > -1) {
			// threshold it
			if (doSet) {
				if (doWhite) {
					ip1.setThreshold(threshold + 1, data.length - 1,
							ImageProcessor.RED_LUT);
					// IJ.setThreshold(threshold+1, data.length - 1);}
				} else {
					ip1.setThreshold(0, threshold, ImageProcessor.RED_LUT);
					// IJ.setThreshold(0,threshold);
				}

			} else {

				for (int y1 = 0; y1 < ip1.getHeight(); y1++) {
					for (int x1 = 0; x1 < ip1.getWidth(); x1++) {
						if (ip1.getPixel(x1, y1) > threshold)
							ip1.putPixel(x1, y1, c);
						else
							ip1.putPixel(x1, y1, b);
					}
				}
			}

		}

		imp1.setDisplayRange(0, 65535);

		imp1.setProcessor(null, imp1.getProcessor().convertToByte(true));

		imp1.updateAndDraw();

		return imp1;
	}

	public static int Mean(int[] data) {
		// C. A. Glasbey,
		// "An analysis of histogram-based thresholding algorithms,"
		// CVGIP: Graphical Models and Image Processing, vol. 55, pp. 532-537,
		// 1993.
		//
		// The threshold is the mean of the greyscale data
		int threshold = -1;
		double tot = 0, sum = 0;
		for (int i = 0; i < data.length; i++) {
			tot += data[i];
			sum += (i * data[i]);
		}
		threshold = (int) Math.floor(sum / tot);
		return threshold;
	}

	public static int Huang(int[] data) {
		// Implements Huang's fuzzy thresholding method
		// Uses Shannon's entropy function (one can also use Yager's entropy
		// function)
		// Huang L.-K. and Wang M.-J.J. (1995) "Image Thresholding by Minimizing
		// the Measures of Fuzziness" Pattern Recognition, 28(1): 41-51
		// Reimplemented (to handle 16-bit efficiently) by Johannes Schindelin
		// Jan 31, 2011

		// find first and last non-empty bin
		int first, last;
		for (first = 0; first < data.length && data[first] == 0; first++)
			; // do nothing
		for (last = data.length - 1; last > first && data[last] == 0; last--)
			; // do nothing
		if (first == last)
			return 0;

		// calculate the cumulative density and the weighted cumulative density
		double[] S = new double[last + 1], W = new double[last + 1];
		S[0] = data[0];
		for (int i = Math.max(1, first); i <= last; i++) {
			S[i] = S[i - 1] + data[i];
			W[i] = W[i - 1] + i * data[i];
		}

		// precalculate the summands of the entropy given the absolute
		// difference x - mu (integral)
		double C = last - first;
		double[] Smu = new double[last + 1 - first];
		for (int i = 1; i < Smu.length; i++) {
			double mu = 1 / (1 + Math.abs(i) / C);
			Smu[i] = -mu * Math.log(mu) - (1 - mu) * Math.log(1 - mu);
		}

		// calculate the threshold
		int bestThreshold = 0;
		double bestEntropy = Double.MAX_VALUE;
		for (int threshold = first; threshold <= last; threshold++) {
			double entropy = 0;
			int mu = (int) Math.round(W[threshold] / S[threshold]);
			for (int i = first; i <= threshold; i++)
				entropy += Smu[Math.abs(i - mu)] * data[i];
			mu = (int) Math.round((W[last] - W[threshold])
					/ (S[last] - S[threshold]));
			for (int i = threshold + 1; i <= last; i++)
				entropy += Smu[Math.abs(i - mu)] * data[i];

			if (bestEntropy > entropy) {
				bestEntropy = entropy;
				bestThreshold = threshold;
			}
		}

		return bestThreshold;
	}

	public static int Li(int[] data) {
		// Implements Li's Minimum Cross Entropy thresholding method
		// This implementation is based on the iterative version (Ref. 2) of the
		// algorithm.
		// 1) Li C.H. and Lee C.K. (1993) "Minimum Cross Entropy Thresholding"
		// Pattern Recognition, 26(4): 617-625
		// 2) Li C.H. and Tam P.K.S. (1998) "An Iterative Algorithm for Minimum
		// Cross Entropy Thresholding"Pattern Recognition Letters, 18(8):
		// 771-776
		// 3) Sezgin M. and Sankur B. (2004) "Survey over Image Thresholding
		// Techniques and Quantitative Performance Evaluation" Journal of
		// Electronic Imaging, 13(1): 146-165
		// http://citeseer.ist.psu.edu/sezgin04survey.html
		// Ported to ImageJ plugin by G.Landini from E Celebi's fourier_0.8
		// routines
		int threshold;
		int ih;
		int num_pixels;
		int sum_back; /* sum of the background pixels at a given threshold */
		int sum_obj; /* sum of the object pixels at a given threshold */
		int num_back; /* number of background pixels at a given threshold */
		int num_obj; /* number of object pixels at a given threshold */
		double old_thresh;
		double new_thresh;
		double mean_back; /* mean of the background pixels at a given threshold */
		double mean_obj; /* mean of the object pixels at a given threshold */
		double mean; /* mean gray-level in the image */
		double tolerance; /* threshold tolerance */
		double temp;

		tolerance = 0.5;
		num_pixels = 0;
		for (ih = 0; ih < data.length; ih++)
			num_pixels += data[ih];

		/* Calculate the mean gray-level */
		mean = 0.0;
		for (ih = 0; ih < data.length; ih++)
			// 0 + 1?
			mean += ih * data[ih];
		mean /= num_pixels;
		/* Initial estimate */
		new_thresh = mean;

		do {
			old_thresh = new_thresh;
			threshold = (int) (old_thresh + 0.5); /* range */
			/* Calculate the means of background and object pixels */
			/* Background */
			sum_back = 0;
			num_back = 0;
			for (ih = 0; ih <= threshold; ih++) {
				sum_back += ih * data[ih];
				num_back += data[ih];
			}
			mean_back = (num_back == 0 ? 0.0 : (sum_back / (double) num_back));
			/* Object */
			sum_obj = 0;
			num_obj = 0;
			for (ih = threshold + 1; ih < data.length; ih++) {
				sum_obj += ih * data[ih];
				num_obj += data[ih];
			}
			mean_obj = (num_obj == 0 ? 0.0 : (sum_obj / (double) num_obj));

			/* Calculate the new threshold: Equation (7) in Ref. 2 */
			// new_thresh = simple_round ( ( mean_back - mean_obj ) / ( Math.log
			// ( mean_back ) - Math.log ( mean_obj ) ) );
			// simple_round ( double x ) {
			// return ( int ) ( IS_NEG ( x ) ? x - .5 : x + .5 );
			// }
			//
			// #define IS_NEG( x ) ( ( x ) < -DBL_EPSILON )
			// DBL_EPSILON = 2.220446049250313E-16
			temp = (mean_back - mean_obj)
					/ (Math.log(mean_back) - Math.log(mean_obj));

			if (temp < -2.220446049250313E-16)
				new_thresh = (int) (temp - 0.5);
			else
				new_thresh = (int) (temp + 0.5);
			/*
			 * Stop the iterations when the difference between the new and old
			 * threshold values is less than the tolerance
			 */
		} while (Math.abs(new_thresh - old_thresh) > tolerance);
		return threshold;
	}

	public static boolean bimodalTest(double[] y) {
		int len = y.length;
		boolean b = false;
		int modes = 0;

		for (int k = 1; k < len - 1; k++) {
			if (y[k - 1] < y[k] && y[k + 1] < y[k]) {
				modes++;
				if (modes > 2)
					return false;
			}
		}
		if (modes == 2)
			b = true;
		return b;
	}

	public static int Intermodes(int[] data) {
		// J. M. S. Prewitt and M. L. Mendelsohn, "The analysis of cell images,"
		// in
		// Annals of the New York Academy of Sciences, vol. 128, pp. 1035-1053,
		// 1966.
		// ported to ImageJ plugin by G.Landini from Antti Niemisto's Matlab
		// code (GPL)
		// Original Matlab code Copyright (C) 2004 Antti Niemisto
		// See http://www.cs.tut.fi/~ant/histthresh/ for an excellent slide
		// presentation
		// and the original Matlab code.
		//
		// Assumes a bimodal histogram. The histogram needs is smoothed (using a
		// running average of size 3, iteratively) until there are only two
		// local maxima.
		// j and k
		// Threshold t is (j+k)/2.
		// Images with histograms having extremely unequal peaks or a broad and
		// ﬂat valley are unsuitable for this method.
		double[] iHisto = new double[data.length];
		int iter = 0;
		int threshold = -1;
		for (int i = 0; i < data.length; i++)
			iHisto[i] = (double) data[i];

		while (!bimodalTest(iHisto)) {
			// smooth with a 3 point running mean filter
			double previous = 0, current = 0, next = iHisto[0];
			for (int i = 0; i < data.length - 1; i++) {
				previous = current;
				current = next;
				next = iHisto[i + 1];
				iHisto[i] = (previous + current + next) / 3;
			}
			iHisto[data.length - 1] = (current + next) / 3;
			iter++;
			if (iter > 10000) {
				threshold = -1;
				IJ.log("Intermodes Threshold not found after 10000 iterations.");
				return threshold;
			}
		}

		// The threshold is the mean between the two peaks.
		int tt = 0;
		for (int i = 1; i < data.length - 1; i++) {
			if (iHisto[i - 1] < iHisto[i] && iHisto[i + 1] < iHisto[i]) {
				tt += i;
				// IJ.log("mode:" +i);
			}
		}
		threshold = (int) Math.floor(tt / 2.0);
		return threshold;
	}

	
	public static int RenyiEntropy(int [] data ) {
		// Kapur J.N., Sahoo P.K., and Wong A.K.C. (1985) "A New Method for
		// Gray-Level Picture Thresholding Using the Entropy of the Histogram"
		// Graphical Models and Image Processing, 29(3): 273-285
		// M. Emre Celebi
		// 06.15.2007
		// Ported to ImageJ plugin by G.Landini from E Celebi's fourier_0.8 routines

		int threshold; 
		int opt_threshold;

		int ih, it;
		int first_bin;
		int last_bin;
		int tmp_var;
		int t_star1, t_star2, t_star3;
		int beta1, beta2, beta3;
		double alpha;/* alpha parameter of the method */
		double term;
		double tot_ent;  /* total entropy */
		double max_ent;  /* max entropy */
		double ent_back; /* entropy of the background pixels at a given threshold */
		double ent_obj;  /* entropy of the object pixels at a given threshold */
		double omega;
		double [] norm_histo = new double[data.length]; /* normalized histogram */
		double [] P1 = new double[data.length]; /* cumulative normalized histogram */
		double [] P2 = new double[data.length];

		int total =0;
		for (ih = 0; ih < data.length; ih++ )
			total+=data[ih];

		for (ih = 0; ih < data.length; ih++ )
			norm_histo[ih] = (double)data[ih]/total;

		P1[0]=norm_histo[0];
		P2[0]=1.0-P1[0];
		for (ih = 1; ih < data.length; ih++ ){
			P1[ih]= P1[ih-1] + norm_histo[ih];
			P2[ih]= 1.0 - P1[ih];
		}

		/* Determine the first non-zero bin */
		first_bin=0;
		for (ih = 0; ih < data.length; ih++ ) {
			if ( !(Math.abs(P1[ih])<2.220446049250313E-16)) {
				first_bin = ih;
				break;
			}
		}

		/* Determine the last non-zero bin */
		last_bin=data.length - 1;
		for (ih = data.length - 1; ih >= first_bin; ih-- ) {
			if ( !(Math.abs(P2[ih])<2.220446049250313E-16)) {
				last_bin = ih;
				break;
			}
		}

		/* Maximum Entropy Thresholding - BEGIN */
		/* ALPHA = 1.0 */
		/* Calculate the total entropy each gray-level
		and find the threshold that maximizes it 
		*/
		threshold =0; // was MIN_INT in original code, but if an empty image is processed it gives an error later on.
		max_ent = 0.0;

		for ( it = first_bin; it <= last_bin; it++ ) {
			/* Entropy of the background pixels */
			ent_back = 0.0;
			for ( ih = 0; ih <= it; ih++ )  {
				if ( data[ih] !=0 ) {
					ent_back -= ( norm_histo[ih] / P1[it] ) * Math.log ( norm_histo[ih] / P1[it] );
				}
			}

			/* Entropy of the object pixels */
			ent_obj = 0.0;
			for ( ih = it + 1; ih < data.length; ih++ ){
				if (data[ih]!=0){
				ent_obj -= ( norm_histo[ih] / P2[it] ) * Math.log ( norm_histo[ih] / P2[it] );
				}
			}

			/* Total entropy */
			tot_ent = ent_back + ent_obj;

			// IJ.log(""+max_ent+"  "+tot_ent);

			if ( max_ent < tot_ent ) {
				max_ent = tot_ent;
				threshold = it;
			}
		}
		t_star2 = threshold;

		/* Maximum Entropy Thresholding - END */
		threshold =0; //was MIN_INT in original code, but if an empty image is processed it gives an error later on.
		max_ent = 0.0;
		alpha = 0.5;
		term = 1.0 / ( 1.0 - alpha );
		for ( it = first_bin; it <= last_bin; it++ ) {
			/* Entropy of the background pixels */
			ent_back = 0.0;
			for ( ih = 0; ih <= it; ih++ )
				ent_back += Math.sqrt ( norm_histo[ih] / P1[it] );

			/* Entropy of the object pixels */
			ent_obj = 0.0;
			for ( ih = it + 1; ih < data.length; ih++ )
				ent_obj += Math.sqrt ( norm_histo[ih] / P2[it] );

			/* Total entropy */
			tot_ent = term * ( ( ent_back * ent_obj ) > 0.0 ? Math.log ( ent_back * ent_obj ) : 0.0);

			if ( tot_ent > max_ent ){
				max_ent = tot_ent;
				threshold = it;
			}
		}

		t_star1 = threshold;

		threshold = 0; //was MIN_INT in original code, but if an empty image is processed it gives an error later on.
		max_ent = 0.0;
		alpha = 2.0;
		term = 1.0 / ( 1.0 - alpha );
		for ( it = first_bin; it <= last_bin; it++ ) {
			/* Entropy of the background pixels */
			ent_back = 0.0;
			for ( ih = 0; ih <= it; ih++ )
				ent_back += ( norm_histo[ih] * norm_histo[ih] ) / ( P1[it] * P1[it] );

			/* Entropy of the object pixels */
			ent_obj = 0.0;
			for ( ih = it + 1; ih < data.length; ih++ )
				ent_obj += ( norm_histo[ih] * norm_histo[ih] ) / ( P2[it] * P2[it] );

			/* Total entropy */
			tot_ent = term *( ( ent_back * ent_obj ) > 0.0 ? Math.log(ent_back * ent_obj ): 0.0 );

			if ( tot_ent > max_ent ){
				max_ent = tot_ent;
				threshold = it;
			}
		}

		t_star3 = threshold;

		/* Sort t_star values */
		if ( t_star2 < t_star1 ){
			tmp_var = t_star1;
			t_star1 = t_star2;
			t_star2 = tmp_var;
		}
		if ( t_star3 < t_star2 ){
			tmp_var = t_star2;
			t_star2 = t_star3;
			t_star3 = tmp_var;
		}
		if ( t_star2 < t_star1 ) {
			tmp_var = t_star1;
			t_star1 = t_star2;
			t_star2 = tmp_var;
		}

		/* Adjust beta values */
		if ( Math.abs ( t_star1 - t_star2 ) <= 5 )  {
			if ( Math.abs ( t_star2 - t_star3 ) <= 5 ) {
				beta1 = 1;
				beta2 = 2;
				beta3 = 1;
			}
			else {
				beta1 = 0;
				beta2 = 1;
				beta3 = 3;
			}
		}
		else {
			if ( Math.abs ( t_star2 - t_star3 ) <= 5 ) {
				beta1 = 3;
				beta2 = 1;
				beta3 = 0;
			}
			else {
				beta1 = 1;
				beta2 = 2;
				beta3 = 1;
			}
		}
		//IJ.log(""+t_star1+" "+t_star2+" "+t_star3);
		/* Determine the optimal threshold value */
		omega = P1[t_star3] - P1[t_star1];
		opt_threshold = (int) (t_star1 * ( P1[t_star1] + 0.25 * omega * beta1 ) + 0.25 * t_star2 * omega * beta2  + t_star3 * ( P2[t_star3] + 0.25 * omega * beta3 ));

		return opt_threshold;
	}

	public static int Minimum(int [] data ) {
		if (data.length < 2)
			return 0;
		// J. M. S. Prewitt and M. L. Mendelsohn, "The analysis of cell images," in
		// Annals of the New York Academy of Sciences, vol. 128, pp. 1035-1053, 1966.
		// ported to ImageJ plugin by G.Landini from Antti Niemisto's Matlab code (GPL)
		// Original Matlab code Copyright (C) 2004 Antti Niemisto
		// See http://www.cs.tut.fi/~ant/histthresh/ for an excellent slide presentation
		// and the original Matlab code.
		//
		// Assumes a bimodal histogram. The histogram needs is smoothed (using a
		// running average of size 3, iteratively) until there are only two local maxima.
		// Threshold t is such that yt−1 > yt ≤ yt+1.
		// Images with histograms having extremely unequal peaks or a broad and
		// ﬂat valley are unsuitable for this method.
		int iter =0;
		int threshold = -1;
		int max = -1;
		double [] iHisto = new double [data.length];

		for (int i=0; i<data.length; i++){
			iHisto[i]=(double) data[i];
			if (data[i]>0) max =i;
		}
		double [] tHisto = iHisto;

		while (!bimodalTest(iHisto) ) {
			 //smooth with a 3 point running mean filter
			for (int i=1; i<data.length - 1; i++)
				tHisto[i]= (iHisto[i-1] + iHisto[i] +iHisto[i+1])/3;
			tHisto[0] = (iHisto[0]+iHisto[1])/3; //0 outside
			tHisto[data.length - 1] = (iHisto[data.length - 2]+iHisto[data.length - 1])/3; //0 outside
			iHisto = tHisto;
			iter++;
			if (iter>10000) {
				threshold = -1;
				IJ.log("Minimum Threshold not found after 10000 iterations.");
				return threshold;
			}
		}
		// The threshold is the minimum between the two peaks. modified for 16 bits
		for (int i=1; i<max; i++) {
			//IJ.log(" "+i+"  "+iHisto[i]);
			if (iHisto[i-1] > iHisto[i] && iHisto[i+1] >= iHisto[i]){
				threshold = i;
				break;
			}
		}
		return threshold;
	}
	
	public static int Moments(int [] data ) {
		//  W. Tsai, "Moment-preserving thresholding: a new approach," Computer Vision,
		// Graphics, and Image Processing, vol. 29, pp. 377-393, 1985.
		// Ported to ImageJ plugin by G.Landini from the the open source project FOURIER 0.8
		// by  M. Emre Celebi , Department of Computer Science,  Louisiana State University in Shreveport
		// Shreveport, LA 71115, USA
		//  http://sourceforge.net/projects/fourier-ipal
		//  http://www.lsus.edu/faculty/~ecelebi/fourier.htm
		double total =0;
		double m0=1.0, m1=0.0, m2 =0.0, m3 =0.0, sum =0.0, p0=0.0;
		double cd, c0, c1, z0, z1;	/* auxiliary variables */
		int threshold = -1;

		double [] histo = new  double [data.length];

		for (int i=0; i<data.length; i++)
			total+=data[i];

		for (int i=0; i<data.length; i++)
			histo[i]=(double)(data[i]/total); //normalised histogram

		/* Calculate the first, second, and third order moments */
		for ( int i = 0; i < data.length; i++ ){
			m1 += i * histo[i];
			m2 += i * i * histo[i];
			m3 += i * i * i * histo[i];
		}
		/* 
		First 4 moments of the gray-level image should match the first 4 moments
		of the target binary image. This leads to 4 equalities whose solutions 
		are given in the Appendix of Ref. 1 
		*/
		cd = m0 * m2 - m1 * m1;
		c0 = ( -m2 * m2 + m1 * m3 ) / cd;
		c1 = ( m0 * -m3 + m2 * m1 ) / cd;
		z0 = 0.5 * ( -c1 - Math.sqrt ( c1 * c1 - 4.0 * c0 ) );
		z1 = 0.5 * ( -c1 + Math.sqrt ( c1 * c1 - 4.0 * c0 ) );
		p0 = ( z1 - m1 ) / ( z1 - z0 );  /* Fraction of the object pixels in the target binary image */

		// The threshold is the gray-level closest  
		// to the p0-tile of the normalized histogram 
		sum=0;
		for (int i=0; i<data.length; i++){
			sum+=histo[i];
			if (sum>p0) {
				threshold = i;
				break;
			}
		}
		return threshold;
	}


}
