package utils;

import utils.Vertex;

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
 * Utility class for the SimplexOptimizer class
 * 
 * 
 * @author Karl Schmidt <karl.schmidt@umassmed.edu> This software is provided
 *         for use free of any costs, Be advised that NO guarantee is made
 *         regarding it's quality, and there is no ongoing support for this
 *         codebase. (c) Karl Schmidt 2003
 * 
 * @author *** modified version *** Alberto Duina - SPEDALI CIVILI DI BRESCIA -
 *         Servizio di Fisica Sanitaria 2006
 * 
 * 
 */
public // package kfschmidt.simplex;
class Vertex implements Comparable<Object> {
	public static String VERSION = "Vertex-v3.00_29jan07_";


	
	public Vertex(int i) {
		mResidual = -1D;
		mParameters = new double[i];
	}

	public void setResidual(double d) {
		mResidual = d;
	}

	public double getResidual() {
		return mResidual;
	}

	public int compareTo(Object obj) {
		if (mResidual == -1D)
			return 1;
		if (((Vertex) obj).getResidual() < mResidual
				&& ((Vertex) obj).getResidual() > 0.0D)
			return 1;
		return ((Vertex) obj).getResidual() <= mResidual ? 0 : -1;
	}

	public double getParameter(int i) {
		return mParameters[i];
	}

	public void setParameter(int i, double d) {
		mParameters[i] = d;
	}

	public int getNumParams() {
		return mParameters.length;
	}

	public void addVertex(Vertex vertex) {
		for (int i = 0; i < mParameters.length; i++)
			mParameters[i] = mParameters[i] + vertex.getParameter(i);

	}

	public void multiplyByScalar(double d) {
		for (int i = 0; i < mParameters.length; i++)
			mParameters[i] = mParameters[i] * d;

	}

	double mResidual;

	double mParameters[];
}
