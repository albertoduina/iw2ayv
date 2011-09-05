package utils;



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
 * Il singleton rappresenta un tipo particolare di classe, esso garantisce che
 * soltanto un'unica istanza della classe stessa possa essere creata all'interno
 * di un programma.
 * 
 * @author UNKNOWN
 * @author *** modified version *** Alberto Duina - SPEDALI CIVILI DI BRESCIA -
 *         Servizio di Fisica Sanitaria 2006
 * 
 * 
 */
public class Singleton {
	
	
	public static String VERSION = "Singleton-v3.00_29jan07_";

	private Singleton() {
		// no code req'd
	}

	
	public static Singleton getSingleton() {
		if (istanza == null)
			// it's ok, we can call this constructor
			istanza = new Singleton();
		return istanza;
	}	
	
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	// keep global data private
	private int globBx;

	private int globBy;

	private int globBw;

	private double globBmag;

	private double globDou1;

	private double globDou2;

	private double globDou3;

	private double globDou4;

	private double globDou5;

	public int getGlobBx() {
		return globBx;
	}

	public int getGlobBy() {
		return globBy;
	}

	public int getGlobBw() {
		return globBw;
	}

	public double getGlobBmag() {
		return globBmag;
	}

	public double getGlobDou1() {
		return globDou1;
	}

	public double getGlobDou2() {
		return globDou2;
	}

	public double getGlobDou3() {
		return globDou3;
	}

	public double getGlobDou4() {
		return globDou4;
	}

	public double getGlobDou5() {
		return globDou5;
	}

	public void setGlobBx(int value) {
		globBx = value;
	}

	public void setGlobBy(int value) {
		globBy = value;
	}

	public void setGlobBw(int value) {
		globBw = value;
	}

	public void setGlobBmag(double value1) {
		globBmag = value1;
	}

	public void setGlobDou1(double value1) {
		globDou1 = value1;
	}

	public void setGlobDou2(double value1) {
		globDou2 = value1;
	}

	public void setGlobDou3(double value1) {
		globDou3 = value1;
	}

	public void setGlobDou4(double value1) {
		globDou4 = value1;
	}

	public void setGlobDou5(double value1) {
		globDou5 = value1;
	}

	private static Singleton istanza;
}
