package utils;

import ij.ImagePlus;
import ij.gui.ImageCanvas;

import java.awt.Color;
import java.awt.Graphics;

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
 * Genera gli overlay graphics per p4rmn_
 * 
 * @author Alberto Duina - SPEDALI CIVILI DI BRESCIA - Servizio di Fisica
 *         Sanitaria
 * 
 */
public class CustomCanvasMTF extends ImageCanvas {

	private static final long serialVersionUID = 1L;
	public static String VERSION = "CustomCanvas4-v3.00_29jan07_";
	double dimPixel=0;
	public CustomCanvasMTF(ImagePlus imp) {
		super(imp);
	}

	public void paint(Graphics g) {
		super.paint(g);
		drawOverlay2(g);
	}
	
	
	public void setDimPixel(double dimPixExternal) {
		dimPixel = dimPixExternal;
	}


	public void drawOverlay2(final Graphics g) {

		double[] cx = { 53, 80, 70, 58, 168 };
		double[] cy = { 99, 165, 85, 130, 67 };

	//	double dimPixel = Singleton.getSingleton().getGlobDou1();

		// -----------------------------------
		// posizione delle scritte
		// -----------------------------------

		double cx1 = 0;
		double cy1 = 0;
		double x1 = 0;
		double y1 = 0;
		String n1 = "";
		// double magnification = getMagnification();
		g.setColor(Color.green);

		for (int i1 = 0; i1 < 5; i1++) {
			cx1 = cx[i1];
			cy1 = cy[i1];
			x1 = cx1 * magnification / dimPixel - srcRect.x * magnification;
			y1 = cy1 * magnification / dimPixel - srcRect.y * magnification;

			n1 = "" + (i1 + 1);
			g.drawString(n1, (int) x1, (int) y1);
		}
	}

} // CustomCanvas4

