package utils;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.gui.Line;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.gui.PointRoi;
import ij.process.ImageStatistics;

public class MyGeometry {

	/**
	 * Trasformazione delle coordinate dei punti in equazione esplicita della
	 * retta
	 * 
	 * @param x0
	 *            coordinata X inizio
	 * @param y0
	 *            coordinata Y inizio
	 * @param x1
	 *            coordinata X fine
	 * @param y1
	 *            coordinata Y fine
	 * @return vettore con parametri equazione
	 */
	public static double[] fromPointsToEquLineExplicit(double x0, double y0, double x1, double y1) {
		// la formula esplicita e' y = mx + b
		// in cui m e' detta anche slope (pendenza) e b intercept (intercetta)
		// non puo' rappresentare rette verticali
		double[] out = new double[2];

		double m = (y1 - y0) / (x1 - x0);

		double b = y0 - m * x0;

		out[0] = m;
		out[1] = b;
		return out;
	}

	/**
	 * Trasformazione delle coordinate dei punti in equazione implicita della
	 * retta
	 * 
	 * @param x0
	 *            coordinata X inizio
	 * @param y0
	 *            coordinata Y inizio
	 * @param x1
	 *            coordinata X fine
	 * @param y1
	 *            coordinata Y fine
	 * @return vettore con parametri equazione
	 */
	public static double[] fromPointsToEquLineImplicit(double x0, double y0, double x1, double y1) {
		// la formula implicita � ax + by + c = 0
		double[] out = new double[3];

		double a = y0 - y1;
		double b = x1 - x0;
		double c = x0 * y1 - x1 * y0;

		out[0] = a;
		out[1] = b;
		out[2] = c;

		return out;
	}

	public static double[] fromPointsToEquCirconferenceImplicit(double cx, double cy, double radius) {
		// la formula implicita � x^2 + y^2 + ax + by + c = 0
		double[] out = new double[3];

		double a = -2 * cx;
		double b = -2 * cy;
		double c = cx * cx + cy * cy - radius * radius;

		out[0] = a;
		out[1] = b;
		out[2] = c;

		return out;
	}

	/**
	 * calcoli per rototraslazione delle coordinate rispetto ad un segmento di
	 * riferimento
	 * 
	 * @param ax
	 *            coord x inizio segmento di riferimento
	 * @param ay
	 *            coord y inizio segmento di riferimento
	 * @param bx
	 *            coord x fine segmento di riferimento
	 * @param by
	 *            coord y fine segmento di riferimento
	 * @param cx
	 *            coordinata x da rototraslare
	 * @param cy
	 *            coordinata y da rototraslare
	 * @param debug1
	 *            true per debug
	 * @return msd[0] coordinata x rototraslata, msd[1] coordinata y
	 *         rototraslata
	 */
	public static double[] coord2D(double ax, double ay, double bx, double by, double cx, double cy, boolean debug1) {
		double x1;
		double y1;
		double msd[];
		double alf1;

		alf1 = Math.atan((ay - by) / (ax - bx));
		if (debug1) {
			IJ.log("-------- coord2D --------");

			IJ.log("angolo= " + Math.toDegrees(alf1) + "  sin= " + Math.sin(alf1));
			IJ.log("angolo= " + Math.toDegrees(alf1) + "  cos= " + Math.cos(alf1));
		}
		if (Math.sin(alf1) < 0) {
			// piu30
			x1 = -cx * Math.sin(alf1) - cy * Math.cos(alf1) + ax;
			y1 = cx * Math.cos(alf1) - cy * Math.sin(alf1) + ay;
		} else {
			// meno30
			x1 = cx * Math.sin(alf1) + cy * Math.cos(alf1) + ax;
			y1 = -cx * Math.cos(alf1) + cy * Math.sin(alf1) + ay;
		}
		msd = new double[2];
		msd[0] = x1;
		msd[1] = y1;
		if (debug1) {
			IJ.log("coord2D output cx= " + cx + "  x1= " + x1 + "  cy= " + cy + "  y1= " + y1);
			IJ.log("--------------------------");

		}
		return msd;
	} // coord2D

	public static double[] coord2D2(double[] vetReference, double cx, double cy, boolean debug1) {
		double x1;
		double y1;
		double msd[];
		double alf1;

		alf1 = Math.atan((vetReference[1] - vetReference[3]) / (vetReference[0] - vetReference[2]));
		if (debug1) {
			IJ.log("-------- coord2D2 --------");

			IJ.log("angolo= " + Math.toDegrees(alf1) + "  sin= " + Math.sin(alf1));
			IJ.log("angolo= " + Math.toDegrees(alf1) + "  cos= " + Math.cos(alf1));
		}

		if (Math.sin(alf1) < 0) {
			// piu30
			x1 = -cx * Math.sin(alf1) - cy * Math.cos(alf1) + vetReference[0];
			y1 = cx * Math.cos(alf1) - cy * Math.sin(alf1) + vetReference[1];
		} else {
			// meno30
			x1 = cx * Math.sin(alf1) + cy * Math.cos(alf1) + vetReference[0];
			y1 = -cx * Math.cos(alf1) + cy * Math.sin(alf1) + vetReference[1];
		}
		msd = new double[2];
		msd[0] = x1;
		msd[1] = y1;
		if (debug1) {
			IJ.log("coord2D output cx= " + cx + "  x1= " + x1 + "  cy= " + cy + "  y1= " + y1);
			IJ.log("--------------------------");
		}

		return msd;
	} // coord2D

	/**
	 * Rotazione di un segmento
	 * 
	 * Probabilmente si pu� usare la AffineTransform di Java, mi mancano le
	 * conscenze tecniche e matematiche per capirne la eventuale convenienza e
	 * modalit� di utilizzo.
	 * 
	 * @param points
	 *            coordinate degli estremi del segmento [x1][y1][x2][y2]
	 * @param angdeg
	 *            angolo di rotazione decimale
	 * @return coordinate segmento rotato (o arrotato) [x1][y1][ax][ay]
	 */
	public static double[] rotateSegment(double points[], double angdeg) {

		double angrad = Math.toRadians(angdeg);
		double sin = Math.sin(angrad);
		double cos = Math.cos(angrad);
		double x1 = points[0];
		double y1 = points[1];
		double x2 = points[2];
		double y2 = points[3];

		double aa = x2 - x1;
		double bb = y2 - y1;
		double ax = (aa * cos) - (bb * sin);
		double ay = (aa * sin) + (bb * cos);

		// MyLog.waitHere("x= " + cx + " y= " + cy + " gradi= " + angdeg
		// + " xrot= " + ax + " yrot= " + ay);
		//
		double[] out = new double[4];
		out[0] = x1;
		out[1] = y1;
		out[2] = ax;
		out[3] = ay;
		return out;
	}

	/**
	 * Distanza tra due punti
	 * 
	 * @param ax
	 *            starting point coordinata x
	 * @param ay
	 *            starting point coordinata y
	 * @param bx
	 *            end point coordinata x
	 * @param by
	 *            end point coordinata y
	 * @return
	 */
	public static double pointsDistance(double ax, double ay, double bx, double by) {

		return (new Line(ax, ay, bx, by).getLength());

	}

	public static double pointsDistance(Point a1, Point b1) {

		return (new Line(a1.getX(), a1.getY(), b1.getX(), b1.getY()).getLength());

	}

	/**
	 * Distanza di un punto da un segmento
	 * 
	 * @param x1
	 *            coordinata inizio segmento
	 * @param y1
	 *            coordinata inizio segmento
	 * @param x2
	 *            coordinata fine segmento
	 * @param y2
	 *            coordinata fine segmento
	 * @param xp
	 *            coordinata punto
	 * @param yp
	 *            coordinata punto
	 * @return distanza
	 */
	public static double pointToLineDistance(double x1, double y1, double x2, double y2, double xp, double yp) {

		double normalLength = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		return Math.abs((xp - x1) * (y2 - y1) - (yp - y1) * (x2 - x1)) / normalLength;
	}

	public static double pointToLineDistance(Point a1, Point a2, Point p1) {

		double normalLength = Math.sqrt((a2.x - a1.x) * (a2.x - a1.x) + (a2.y - a1.y) * (a2.y - a1.y));
		return Math.abs((p1.x - a1.x) * (a2.y - a1.y) - (p1.y - a1.y) * (a2.x - a1.x)) / normalLength;
	}

	/***
	 * rotazione di un punto
	 * 
	 * @param x1
	 *            punto da ruotare coordinata x
	 * @param y1
	 *            punto da ruotare coordinata y
	 * @param angoloDec
	 *            angolo di rotazione
	 * @return coordinate punto dopo rotazione [x2], [y2]
	 */
	public static double[] rotatePoint(double x1, double y1, double angoloDec) {

		double angoloRad = Math.toRadians(angoloDec);
		double sin = Math.sin(angoloRad);
		double cos = Math.cos(angoloRad);
		double x2 = x1 * cos - y1 * sin;
		double y2 = x1 * sin + y1 * cos;
		double[] msd = new double[2];
		msd[0] = x2;
		msd[1] = y2;
		return msd;
	}

	/***
	 * traslazione e rotazione di un punto
	 * 
	 * @param rotx
	 *            centro di rotazione coordinata x
	 * @param roty
	 *            centro di rotazione coordinata y
	 * @param pointx
	 *            punto da ruotare coordinata x
	 * @param pointy
	 *            punto da ruotare coordinata y
	 * @param angoloDec
	 *            angolo di rotazione
	 * @return coordinate punto dopo rotazione [0]x, [1]y
	 */
	public static double[] traslateRotatePoint(double rotx, double roty, double pointx, double pointy,
			double angoloDec) {

		double[] punto1 = MyGeometry.rotatePoint(pointx, pointy, angoloDec);
		double[] punto2 = new double[2];
		punto2[0] = rotx + punto1[0];
		punto2[1] = roty - punto1[1];
		return punto2;
	}

	/**
	 * From www.sunshine2k.de/coding/PointOnLine/PointOnLine.html. Get projected
	 * point P' of P on line e1.
	 * 
	 * @return projected point p.
	 */

	/**
	 * From www.sunshine2k.de/coding/PointOnLine/PointOnLine.html. Get projected
	 * point P' of P on line e1. Faster version.
	 * 
	 * @return projected point p.
	 */
	public static Point getProjectedPointOnLineFast(Point v1, Point v2, Point p1) {
		// get dot product of e1, e2
		Point e1 = new Point(v2.x - v1.x, v2.y - v1.y);
		Point e2 = new Point(p1.x - v1.x, p1.y - v1.y);
		double valDp = dotProduct(e1, e2);
		// get squared length of e1
		double len2 = e1.x * e1.x + e1.y * e1.y;
		Point p2 = new Point((int) (v1.x + (valDp * e1.x) / len2), (int) (v1.y + (valDp * e1.y) / len2));
		return p2;
	}

	/**
	 * From www.sunshine2k.de/coding/PointOnLine/PointOnLine.html. Calculates
	 * the dot product of two 2D vectors / points.
	 * 
	 * @param p1
	 *            first point used as vector
	 * @param p2
	 *            second point used as vector
	 * @return dotProduct of vectors
	 */
	private static double dotProduct(Point p1, Point p2) {
		return (p1.x * p2.x + p1.y * p2.y);
	}

	/***
	 * verifica che un cerchio sia dentro un altro
	 * 
	 * @param x1
	 * @param y1
	 * @param d1
	 * @param x2
	 * @param y2
	 * @param d2
	 * @return
	 */
	public static boolean isCircleInside(int x1, int y1, int d1, int x2, int y2, int d2) {
		double centersDistance = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
		double radiusDifference = d1 / 2 - d2 / 2;
		if (centersDistance <= radiusDifference) {
			return true;
		} else
			return false;
	}

	public static boolean isSphereInside(int x1, int y1, int z1, int r1, int x2, int y2, int z2, int r2) {
		double centersDistance = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2) + Math.pow((z1 - z2), 2));
		double radiusDifference = r1 - r2;
		if (centersDistance <= radiusDifference) {
			return true;
		} else
			return false;
	}

}
