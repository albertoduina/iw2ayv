package utils;

//set debug 290607

import utils.SimplexOptimizer;
import ij.IJ;

/*
 * Copyright (C) 2007 Alberto Duina, SPEDALI CIVILI DI BRESCIA, Brescia ITALY
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */


/**
 * 
 * Implementation of the Simplex minimization algorithm Nelder Mead (1965)
 * 
 * 
 * @author Karl Schmidt <karl.schmidt@umassmed.edu> This software is provided
 *         for use free of any costs, Be advised that NO guarantee is made
 *         regarding it's quality, and there is no ongoing support for this
 *         codebase. (c) Karl Schmidt 2003
 * 
 * 
 * @author *** modified version *** Alberto Duina - SPEDALI CIVILI DI BRESCIA -
 *         Servizio di Fisica Sanitaria 2006
 * 
 * 
 */

// package kfschmidt.mricalculations;
// import kfschmidt.simplex.SimplexOptimizer;
// import ij.*;
// import ij.gui.*;
// import ij.io.*;
public class SimplexBasedRegressor {

	public static String VERSION = "SimplexBasedRegressor-v3.00_08apr08_";

	SimplexOptimizer mSimplex = new SimplexOptimizer();

	double[] mXPoints;

	double[] mYPoints;

	/**
	 * Perform a T2 relaxation fit S=So*exp(-TE/T2)) returns [t2, So, r^2]
	 * 
	 */
	public double[] regressT2(double[] te_points, double[] signal_points,
			double t2_guess, boolean debug3) {

		// check if this is two value
		
		if (te_points==null) IJ.log("te_points null");
		if (signal_points==null) IJ.log("signal_points null");
		
		
		if (te_points.length == 2) {
			double[] ret = new double[3];
			ret[0] = (te_points[1] - te_points[0])
					/ Math.log((signal_points[0] / signal_points[1]));
			ret[1] = 0d;
			ret[2] = 0d;
			return ret;
		}
		if (debug3) {
			String sta1 = "";
			for (int i1 = 0; i1 < te_points.length; i1++)
				sta1 = sta1 + " " + te_points[i1];
			sta1 = "";
			for (int i1 = 0; i1 < signal_points.length; i1++)
				sta1 = sta1 + " " + signal_points[i1];

		}

		// more than two values, use laborious fit
		try {

			mXPoints = te_points; // t3
			mYPoints = signal_points; // signal
			mSimplex.setObjectAndMethod(this, "calculateResidualT2", 2, 0d);
			mSimplex.setVariableParam(1, t2_guess); // t2
			if (signal_points[0] <= 0)
				signal_points[0] = 0.000000001d; // modifica 250906
			mSimplex.setVariableParam(2, signal_points[0]); // So
			mSimplex.initialize();
			mSimplex.setMaxIterations(200);
			if (debug3)
				ij.IJ.log("debug3= " + debug3);

			mSimplex.setDebug(debug3);
			Object[] final_params = mSimplex.go();
			double residual = mSimplex.getBestResidual();
			double[] ret = new double[3]; // [t2,So,r^2]
			ret[0] = ((Double) final_params[0]).doubleValue(); // t2
			ret[1] = ((Double) final_params[1]).doubleValue(); // So
			ret[2] = residual;
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Perform a b value relaxation fit S=So*exp(-b*D)) returns [D, So, r^2]
	 * 
	 */
	public double[] regressADC(double[] b_vals, double[] signal_points,
			double d_guess) {
		
		if (b_vals==null) IJ.log("b_vals null");
		if (signal_points==null) IJ.log("signal_points null");


		// check if this is two value
		if (b_vals.length == 2) {
			double[] ret = new double[3];
			ret[0] = Math.log((signal_points[0] / signal_points[1]))
					/ (b_vals[1] - b_vals[0]);
			ret[1] = 0d;
			ret[2] = 0d;
			return ret;
		}

		try {
			mXPoints = b_vals; // t3
			mYPoints = signal_points; // signal
			mSimplex.setObjectAndMethod(this, "calculateResidualADC", 2, 0d);
			mSimplex.setVariableParam(1, d_guess); // t2
			if (signal_points[0] <= 0)
				signal_points[0] = 0.000000001d; // modifica 250906
			mSimplex.setVariableParam(2, signal_points[0]); // So
			mSimplex.initialize();
			mSimplex.setMaxIterations(200);
			Object[] final_params = mSimplex.go();
			double residual = mSimplex.getBestResidual();
			double[] ret = new double[3]; // [D,So,r^2]
			ret[0] = ((Double) final_params[0]).doubleValue(); // D
			ret[1] = ((Double) final_params[1]).doubleValue(); // So
			ret[2] = residual;
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Perform a T1 relaxation fit S=So(1-2*exp(-TR/T1)) returns [t1, So, r^2]
	 * 
	 */
	public double[] regressT1InversionRecovery(double[] tr_points,
			double[] signal_points, double t1_guess)

	{
		if (tr_points==null) IJ.log("tr_points null");
		if (signal_points==null) IJ.log("signal_points null");


		try {
			mXPoints = tr_points; // te
			mYPoints = signal_points; // signal
			mSimplex.setObjectAndMethod(this, "calculateResidualT1IR", 2, 0d);
			mSimplex.setVariableParam(1, t1_guess); // t1
			if (signal_points[0] <= 0)
				signal_points[0] = 0.000000001d; // modifica 250906
			mSimplex.setVariableParam(2, signal_points[0]); // So
			mSimplex.initialize();
			mSimplex.setMaxIterations(200);
			Object[] final_params = mSimplex.go();
			double residual = mSimplex.getBestResidual();
			double[] ret = new double[3]; // [t2,So,r^2]
			ret[0] = ((Double) final_params[0]).doubleValue(); // t2
			ret[1] = ((Double) final_params[1]).doubleValue(); // So
			ret[2] = residual;
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Perform a T1 relaxation fit S=So(1-exp(-TR/T1)) returns [t2, So, r^2]
	 * 
	 */
	public double[] regressT1SaturationRecovery(double[] tr_points,
			double[] signal_points, double t1_guess) {
		try {
			mXPoints = tr_points; // t3
			mYPoints = signal_points; // signal
			mSimplex.setObjectAndMethod(this, "calculateResidualT1SR", 2, 0d);
			mSimplex.setVariableParam(1, t1_guess); // t1
			if (signal_points[0] <= 0)
				signal_points[0] = 0.000000001d; // modifica 250906
			mSimplex.setVariableParam(2, signal_points[0]); // So
			mSimplex.initialize();
			mSimplex.setMaxIterations(200);
			Object[] final_params = mSimplex.go();
			double residual = mSimplex.getBestResidual();
			double[] ret = new double[3]; // [t2,So,r^2]
			ret[0] = ((Double) final_params[0]).doubleValue(); // t2
			ret[1] = ((Double) final_params[1]).doubleValue(); // So
			ret[2] = residual;
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * returns the residual for a test t2 value
	 */
	public double calculateResidualT2(double t2, double so) {
		// if (t2<0.00001 || t2>100) return Double.MAX_VALUE;
//		if (t2 < 0.00001 || t2 > 3000)
			if (t2 < 0.00001 || t2 > 300)

			return Double.MAX_VALUE; // inserito il 300 poich� i nostri gel
		// hanno T2 superiori a 100 msec
		double residual = 0d;
		// int iter = 0;
		// X= te, Y=signal
		for (int a = 0; a < mXPoints.length; a++) {
			double testval = so * Math.exp((-1d * mXPoints[a] / t2));
			residual += Math.pow(mYPoints[a] - testval, 2d);
		}
		return residual;
	}

	/**
	 * returns the residual for a test adc value
	 */
	public double calculateResidualADC(double D, double so) {
		if (D < 0.0000001 || D > 100)
			return Double.MAX_VALUE;
		double residual = 0d;
		// int iter = 0;
		// X= te, Y=signal
		for (int a = 0; a < mXPoints.length; a++) {
			double testval = so * Math.exp((-1d * mXPoints[a] * D));
			residual += Math.pow(mYPoints[a] - testval, 2d);
		}
		return residual;
	}

	/**
	 * returns the residual for a test t1 value in inversion recov
	 */
	public double calculateResidualT1SR(double t1, double so) {
		if (t1 < 0.001 || t1 > 1000)
			return Double.MAX_VALUE;
		double residual = 0d;
		// int iter = 0;
		// X= tr, Y=signal
		for (int a = 0; a < mXPoints.length; a++) {
			double testval = so * (1d - Math.exp((-1d * mXPoints[a] / t1)));
			residual += Math.pow(mYPoints[a] - testval, 2d);
		}
		return residual;
	}

	// #############################################################################
	/**
	 * returns the residual for a test t1 value in inversion recovery. Qui
	 * abbiamo corretto la formula per la IR, vedremo in seguito se � possibile 
	 * parametrizzare in qualche modo il TR, che vale 4000d e per ora � scritto
	 * come magicnumber
	 * 
	 * Perform a T1 relaxation fit S=So(1-2*exp(-TI/T1)-exp(-TR/T1)) returns
	 * [t1, So, r^2] *
	 * 
	 */
	public double calculateResidualT1IR(double t1, double so) {
		if (t1 < 0.001 || t1 > 1500)
			return Double.MAX_VALUE;
		double residual = 0d;
		// int iter = 0;
		// X= tr, Y=signal
		for (int a = 0; a < mXPoints.length; a++) {
			double testval =
			// vecchia !!! Math.abs(so * (1d-2d*Math.exp((-1d*mXPoints[a]/t1)))
			// );
			Math.abs(so * (1d - 2d * Math.exp(-1d * mXPoints[a] / t1) + 1d
					* Math.exp(-4000d / t1)));
			residual += Math.pow(mYPoints[a] - testval, 2d);
		}
		return residual;
	}
	// ###############################################################################

}
