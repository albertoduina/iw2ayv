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
 * Genera gli overlay graphics per p8rmn_
 * 
 * @author Alberto Duina - SPEDALI CIVILI DI BRESCIA - Servizio di Fisica
 *         Sanitaria
 * 
 */
public class CustomCanvasGeneric extends ImageCanvas {

	private static final long serialVersionUID = 1L;
	public static String VERSION = "CustomCanvasGeneric-v1.00_26feb10_";

	double[] cx1 = null;
	double[] cy1 = null;
	double[] cx2 = null;
	double[] cy2 = null;
	double bx1 = 0;
	double by1 = 0;
	double bw1 = 0;
	int gridElements = 0;

	Color c1 = Color.gray;
	Color c2 = Color.gray;
	boolean set1 = false;
	boolean set2 = false;

	public CustomCanvasGeneric(ImagePlus imp) {
		super(imp);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (set1 && !set2)
			drawNumbers1(g);
		if (set2)
			drawNumbers2(g);
		if (gridElements > 0) {
			drawGrid(g);
		}
	}

	public void setPosition1(double vet1X[], double vet1Y[]) {
		cx1 = vet1X;
		cy1 = vet1Y;
		set1 = true;
	}

	public void setPosition2(double vet2X[], double vet2Y[]) {
		cx2 = vet2X;
		cy2 = vet2Y;
		set2 = true;
	}

	public void setOffset(double bx, double by, double bw) {
		bx1 = bx;
		by1 = by;
		bw1 = bw;
	}

	public void setColor1(Color col1) {
		c1 = col1;
	}

	public void setColor2(Color col2) {
		c2 = col2;
	}

	public void drawNumbers1(Graphics g) {
		g.setColor(c1);
		for (int i1 = 0; i1 < cx1.length; i1++) {
			double x1 = (cx1[i1] - srcRect.x) * magnification;
			double y1 = (cy1[i1] - srcRect.y) * magnification;
			String n1 = "" + (i1 + 1);
			g.drawString(n1, (int) x1, (int) y1);
		}
	}

	

	public void drawNumbers2(Graphics g) {

		g.setColor(c1);
		for (int i1 = 0; i1 < cx1.length; i1++) {
			double x1 = (cx1[i1] * bw1 + bx1 - srcRect.x) * magnification;
			double y1 = (cy1[i1] * bw1 + by1 - srcRect.y) * magnification;
			String n1 = "" + (i1 + 1);
			g.drawString(n1, (int) x1, (int) y1);
		}
		g.setColor(c2);
		for (int i1 = 0; i1 < cx2.length; i1++) {
			double x2 = (cx2[i1] * bw1 + bx1 - srcRect.x) * magnification;
			double y2 = (cy2[i1] * bw1 + by1 - srcRect.y) * magnification;
			String n2 = "" + (cx1.length + i1 + 1);
			g.drawString(n2, (int) x2, (int) y2);
		}
	}

	public void drawGrid(Graphics g) {

		g.setColor(Color.gray);
		double delta = imageWidth / (double) gridElements;
		double x = delta;
		while (x < imageWidth) {
			g.drawLine(screenX((int) x), screenY(0), screenX((int) x),
					screenY(imageHeight));
			x += delta;
		}
		double y = delta;
		while (y < imageHeight) {
			g.drawLine(screenX(0), screenY((int) y), screenX(imageWidth),
					screenY((int) y));
			y += delta;
		}
	} // drawOverlay

	public void setGridElements(int number) {
		gridElements = number;
	}

}
