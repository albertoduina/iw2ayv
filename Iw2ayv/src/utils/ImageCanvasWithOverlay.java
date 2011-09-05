package utils;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageCanvas;
import ij.gui.Roi;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

/*
 *  
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *
 * Peter Sebastian Masny
 * ps-contact@masny.dk
 *
 * Parts of this code were adapted from Wayne Rasband, Sergio
 * Caballero Jr., Michael A. Miller, Philippe Thevenaz, 
 * Samuel Moll, and Johannes Schindelin.  Content derived from
 * these authors follow their corresponding copyrights.
 *
 * @version    2.3 7 Feb 2009
 * @author     Peter Sebastian Masny
 * 
 * modified by AlbertoDuina 24 feb 2010
 * 
 */

public class ImageCanvasWithOverlay extends ImageCanvas {

	private static final long serialVersionUID = 1L;

	boolean overlayDisplay = true;

	protected BufferedImage overlay;

	double dimPixel = 1.0;

	int overlayType = 1;

	public ImageCanvasWithOverlay(ImagePlus iPlus) {
		super(iPlus);
		overlay = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_ARGB);
	}

	public void overlayHide() {
		overlayDisplay = false;
	}

	public void overlayShow() {
		overlayDisplay = true;
	}

	public void setDimPixel(double dimPixExternal) {
		dimPixel = dimPixExternal;
	}

	public void overlayType(int type) {
		overlayType = type;
		// type = 0 non impostato
		// type = 1 MTF
		//
	}

	@Override
	public void paint(Graphics g) {

		super.paint(g);

		if (overlayDisplay) {
			switch (overlayType) {
			case 1: {
				g.drawImage(overlay, 0, 0, (int) (srcRect.width),
						(int) (srcRect.height), (int) (srcRect.x),
						(int) (srcRect.y), (int) (srcRect.x + srcRect.width),
						(int) (srcRect.y + srcRect.height), null);
				break;
			}
			case 0:
			default: {
				IJ.log("overlayType.paint.004");
				g.drawImage(overlay, 0, 0,
						(int) (srcRect.width * magnification),
						(int) (srcRect.height * magnification), srcRect.x,
						srcRect.y, srcRect.x + srcRect.width, srcRect.y
								+ srcRect.height, null);
			}
			}

		}
	}

	public Graphics getColoredGraphics(Color color) {
		Graphics graphics = overlay.getGraphics();
		graphics.setColor(color);
		return graphics;
	}

	public void overlaySetPixel(int x, int y, Color c) {
		getColoredGraphics(c).drawRect(x, y, 1, 1);
	}

	public void overlaySetLine(int x1, int y1, int x2, int y2, Color c) {
		getColoredGraphics(c).drawLine(x1, y1, x2, y2);
	}

	public void overlayDrawRoi(Color c) {
		overlayDrawRoi(imp.getRoi(), c);
	}

	public void overlayFillRoi(Color c) {
		overlayFillRoi(imp.getRoi(), c);
	}

	public void overlayDrawRoi(Roi roi, Color c) {
		if (roi == null)
			return;
		Polygon polygon = roi.getPolygon();
		getColoredGraphics(c).drawPolygon(polygon);
	}

	public void overlayFillRoi(Roi roi, Color c) {
		if (roi == null)
			return;
		Polygon polygon = roi.getPolygon();
		getColoredGraphics(c).fillPolygon(polygon);
	}

	public void overlayDrawString(String str, int xPos, int yPos, Color c) {
		getColoredGraphics(c).drawString(str, xPos, yPos);
	}

	public void overlayClear() {
		overlay = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_ARGB);
	}

	public void overlayDrawNumbers(double[] cx, double[] cy, Color c) {
		IJ.log("magnification= " + magnification + " dimPixel= " + dimPixel
				+ " srcRect.x= " + srcRect.x + " srcRect.y= " + srcRect.y);

		for (int i1 = 0; i1 < cx.length; i1++) {
			double x1 = cx[i1] * srcRect.width - srcRect.x;
			double y1 = cy[i1] * srcRect.height - srcRect.y;
			String n1 = "" + (i1 + 1);
			getColoredGraphics(c).drawString(n1, (int) x1, (int) y1);
		}

	}
}
