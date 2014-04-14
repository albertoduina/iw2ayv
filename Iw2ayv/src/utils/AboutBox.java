package utils;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import ij.*;
import ij.gui.*;
import ij.process.*;

/***
 * About box class, generation and display
 * 
 * @author Alberto Duina - SPEDALI CIVILI DI BRESCIA - Servizio di Fisica
 *         Sanitaria
 * 
 */
public class AboutBox {
	static final int SMALL_FONT = 10, MEDIUM_FONT = 14, LARGE_FONT = 18;

	public void about(String arg, String version1) {
		// public void about(String arg, Class<?> myClass) {

		int lines = 7;

		String[] text = new String[lines];
		text[0] = arg;
		text[1] = "Azienda Spedali Civili di Brescia";
		text[2] = "Servizio di Fisica Sanitaria";
		text[3] = "©2007-2014  Alberto Duina";
		text[4] = "albertoduina@virgilio.it";
		// text[5] = "VERSIONE " + myImplementationVersion(myClass);
		// text[6] = "VERSIONE libreria iw2ayv " +
		// myImplementationVersion(this.getClass());
		text[5] = "VERSIONE " + version1;
		text[6] = "VERSIONE libreria iw2ayv " + MyVersionUtils.getVersion();

		// VERSION = className + "_build_"
		// + MyVersion.CURRENT_VERSION
		// + "_iw2ayv_build_"
		// + MyVersionUtils.CURRENT_VERSION;

		int w = 150, h = 150;
		ImageProcessor ip = new ColorProcessor(w, h);
		int[] pixels = (int[]) ip.getPixels();

		for (int y = 0; y < (h / 2); y++) {
			for (int x = 0; x < (w / 2); x++) {
				int offset = (y * h) + x;
				double paint = (double) (x * y) / (double) ((h / 2) * (w / 2))
						* 150.;
				pixels[offset] = gradient1(paint);
			}
		}
		for (int y = 0; y < (h / 2); y++) {
			for (int x = w - 1; x > (w / 2) - 1; x--) {
				int offset = (y * h) + x;
				double paint = (double) ((h - x) * y)
						/ (double) ((h / 2) * (w / 2)) * 150.;
				pixels[offset] = gradient1(paint);
			}
		}

		for (int y = h - 1; y > (h / 2) - 1; y--) {
			for (int x = w - 1; x > (w / 2) - 1; x--) {
				int offset = (y * h) + x;
				double paint = (double) ((h - x) * (h - y))
						/ (double) ((h / 2) * (w / 2)) * 150.;
				pixels[offset] = gradient1(paint);
			}
		}

		for (int y = h - 1; y > (h / 2) - 1; y--) {
			for (int x = 0; x < (w / 2); x++) {
				int offset = (y * h) + x;
				double paint = (double) (x * (h - y))
						/ (double) ((h / 2) * (w / 2)) * 150.;
				pixels[offset] = gradient1(paint);
			}
		}

		ip = ip.resize(ip.getWidth() * 2, ip.getHeight() * 2);
		ip.setFont(new Font("SansSerif", Font.BOLD, LARGE_FONT));
		ip.setAntialiasedText(true);
		int[] widths = new int[lines];
		widths[0] = ip.getStringWidth(text[0]);
		for (int i = 1; i < lines - 1; i++)
			widths[i] = ip.getStringWidth(text[i]);
		int max = 0;
		for (int i = 0; i < lines - 1; i++)
			if (widths[i] > max)
				max = widths[i];
		ip.setColor(new Color(0, 0, 0));
		ip.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, LARGE_FONT));
		ip.setJustification(ImageProcessor.LEFT_JUSTIFY);
		int y = 80;
		ip.drawString(text[0], xDim(text[0], ip, max), y);
		ip.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, LARGE_FONT));
		y += 30;
		ip.drawString(text[1], xDim(text[1], ip, max), y);
		y += 25;
		ip.drawString(text[2], xDim(text[2], ip, max), y);
		y += 25;
		ip.setFont(new Font("SansSerif", Font.ITALIC, MEDIUM_FONT));
		ip.drawString(text[3], xDim(text[3], ip, max), y);
		y += 18;
		ip.drawString(text[4], xDim(text[4], ip, max), y);
		y += 18;
		ip.drawString(text[5], xDim(text[5], ip, max), y);
		y += 18;
		ip.drawString(text[6], xDim(text[6], ip, max), y);
		ImageWindow.centerNextImage();

		new ImagePlus("Controlli Mensili", ip).show();
		IJ.wait(2500);
		close();

	}

	public void about(String[] arg, String version1) {
		// public void about(String arg, Class<?> myClass) {

		int lines = 6 + arg.length;

		String[] text = new String[lines];
		for (int i1 = 0; i1 < arg.length; i1++) {
			text[i1] = arg[i1];
		}
		
		int before = arg.length-1;
		text[before + 1] = "Azienda Spedali Civili di Brescia";
		text[before + 2] = "Servizio di Fisica Sanitaria";
		text[before + 3] = "©2007-2014  Alberto Duina";
		text[before + 4] = "albertoduina@virgilio.it";
		// text[5] = "VERSIONE " + myImplementationVersion(myClass);
		// text[6] = "VERSIONE libreria iw2ayv " +
		// myImplementationVersion(this.getClass());
		text[before + 5] = "VERSIONE " + version1;
		text[before + 6] = "VERSIONE libreria iw2ayv "
				+ MyVersionUtils.getVersion();
		
		// VERSION = className + "_build_"
		// + MyVersion.CURRENT_VERSION
		// + "_iw2ayv_build_"
		// + MyVersionUtils.CURRENT_VERSION;

		int w = 150, h = 150;
		ImageProcessor ip = new ColorProcessor(w, h);
		int[] pixels = (int[]) ip.getPixels();

		for (int y = 0; y < (h / 2); y++) {
			for (int x = 0; x < (w / 2); x++) {
				int offset = (y * h) + x;
				double paint = (double) (x * y) / (double) ((h / 2) * (w / 2))
						* 150.;
				pixels[offset] = gradient1(paint);
			}
		}
		for (int y = 0; y < (h / 2); y++) {
			for (int x = w - 1; x > (w / 2) - 1; x--) {
				int offset = (y * h) + x;
				double paint = (double) ((h - x) * y)
						/ (double) ((h / 2) * (w / 2)) * 150.;
				pixels[offset] = gradient1(paint);
			}
		}

		for (int y = h - 1; y > (h / 2) - 1; y--) {
			for (int x = w - 1; x > (w / 2) - 1; x--) {
				int offset = (y * h) + x;
				double paint = (double) ((h - x) * (h - y))
						/ (double) ((h / 2) * (w / 2)) * 150.;
				pixels[offset] = gradient1(paint);
			}
		}

		for (int y = h - 1; y > (h / 2) - 1; y--) {
			for (int x = 0; x < (w / 2); x++) {
				int offset = (y * h) + x;
				double paint = (double) (x * (h - y))
						/ (double) ((h / 2) * (w / 2)) * 150.;
				pixels[offset] = gradient1(paint);
			}
		}

		ip = ip.resize(ip.getWidth() * 2, ip.getHeight() * 2);
		ip.setFont(new Font("SansSerif", Font.BOLD, LARGE_FONT));
		ip.setAntialiasedText(true);
		int[] widths = new int[lines];
		widths[0] = ip.getStringWidth(text[0]);
		for (int i = 1; i < lines - 1; i++)
			widths[i] = ip.getStringWidth(text[i]);
		int max = 0;
		for (int i = 0; i < lines - 1; i++)
			if (widths[i] > max)
				max = widths[i];
		ip.setColor(new Color(0, 0, 0));
		ip.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, LARGE_FONT));
		ip.setJustification(ImageProcessor.LEFT_JUSTIFY);
		int y = 80;
		ip.drawString(text[0], xDim(text[0], ip, max), y);
		ip.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, LARGE_FONT));
		y += 30;
		ip.drawString(text[1], xDim(text[1], ip, max), y);
		y += 25;
		ip.drawString(text[2], xDim(text[2], ip, max), y);
		y += 25;
		ip.setFont(new Font("SansSerif", Font.ITALIC, MEDIUM_FONT));
		ip.drawString(text[3], xDim(text[3], ip, max), y);
		y += 18;
		ip.drawString(text[4], xDim(text[4], ip, max), y);
		y += 18;
		ip.drawString(text[5], xDim(text[5], ip, max), y);
		y += 18;
		ip.drawString(text[6], xDim(text[6], ip, max), y);
		ImageWindow.centerNextImage();

		new ImagePlus("Controlli Mensili", ip).show();
		IJ.wait(2500);
		close();

	}

	public void about10(String arg, Class<?> myClass) {

		int lines = 7;

		String[] text = new String[lines];
		text[0] = arg;
		text[1] = "Azienda Spedali Civili di Brescia";
		text[2] = "Servizio di Fisica Sanitaria";
		text[3] = "©2007-2014";
		text[4] = "Luca Berta, Alberto Duina";
		text[5] = "albertoduina@virgilio.it";
		text[6] = "VERSIONE " + myImplementationVersion(myClass);

		int w = 150, h = 150;
		ImageProcessor ip = new ColorProcessor(w, h);
		int[] pixels = (int[]) ip.getPixels();

		for (int y = 0; y < (h / 2); y++) {
			for (int x = 0; x < (w / 2); x++) {
				int offset = (y * h) + x;
				double paint = (double) (x * y) / (double) ((h / 2) * (w / 2))
						* 150.;
				pixels[offset] = gradient2(paint);
			}
		}
		for (int y = 0; y < (h / 2); y++) {
			for (int x = w - 1; x > (w / 2) - 1; x--) {
				int offset = (y * h) + x;
				double paint = (double) ((h - x) * y)
						/ (double) ((h / 2) * (w / 2)) * 150.;
				pixels[offset] = gradient2(paint);
			}
		}

		for (int y = h - 1; y > (h / 2) - 1; y--) {
			for (int x = w - 1; x > (w / 2) - 1; x--) {
				int offset = (y * h) + x;
				double paint = (double) ((h - x) * (h - y))
						/ (double) ((h / 2) * (w / 2)) * 150.;
				pixels[offset] = gradient2(paint);
			}
		}

		for (int y = h - 1; y > (h / 2) - 1; y--) {
			for (int x = 0; x < (w / 2); x++) {
				int offset = (y * h) + x;
				double paint = (double) (x * (h - y))
						/ (double) ((h / 2) * (w / 2)) * 150.;
				pixels[offset] = gradient2(paint);
			}
		}

		ip = ip.resize(ip.getWidth() * 2, ip.getHeight() * 2);
		ip.setFont(new Font("SansSerif", Font.BOLD, LARGE_FONT));
		ip.setAntialiasedText(true);
		int[] widths = new int[lines];
		widths[0] = ip.getStringWidth(text[0]);
		for (int i = 1; i < lines - 1; i++)
			widths[i] = ip.getStringWidth(text[i]);
		int max = 0;
		for (int i = 0; i < lines - 1; i++)
			if (widths[i] > max)
				max = widths[i];
		ip.setColor(new Color(0, 0, 0));
		ip.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, LARGE_FONT));
		ip.setJustification(ImageProcessor.LEFT_JUSTIFY);
		int y = 80;
		ip.drawString(text[0], xDim(text[0], ip, max), y);
		ip.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, LARGE_FONT));
		y += 30;
		ip.drawString(text[1], xDim(text[1], ip, max), y);
		y += 25;
		ip.drawString(text[2], xDim(text[2], ip, max), y);
		y += 25;
		ip.setFont(new Font("SansSerif", Font.ITALIC, MEDIUM_FONT));
		ip.drawString(text[3], xDim(text[3], ip, max), y);
		y += 18;
		ip.drawString(text[4], xDim(text[4], ip, max), y);
		y += 18;
		ip.drawString(text[5], xDim(text[5], ip, max), y);
		ImageWindow.centerNextImage();
		y += 18;
		ip.drawString(text[6], xDim(text[6], ip, max), y);
		ImageWindow.centerNextImage();

		new ImagePlus("TITOLO10", ip).show();

		IJ.wait(2000);
		close();

	}

	public int gradient1(double paint) {

		int red = (int) paint + 100;
		int green = 255;
		int blue = 1;
		int color = ((red & 0xff) << 16) | ((green & 0xff) << 8)
				| (blue & 0xff);
		return color;
	}

	public int gradient2(double paint) {

		int green = (int) paint + 100;
		int red = 255;
		int blue = 40;
		int color = ((red & 0xff) << 16) | ((green & 0xff) << 8)
				| (blue & 0xff);
		return color;
	}

	public void about5(String arg, Class<?> myClass) {

		int lines = 6;

		String[] text = new String[lines];
		text[0] = arg;
		text[1] = "Azienda Spedali Civili di Brescia";
		text[2] = "Servizio di Fisica Sanitaria";
		text[3] = "©2007-2014  Alberto Duina";
		text[4] = "albertoduina@virgilio.it";
		text[5] = "VERSIONE " + myImplementationVersion(myClass);

		int w = 150, h = 150;
		ImageProcessor ip = new ColorProcessor(w, h);
		int[] pixels = (int[]) ip.getPixels();
		int red = 0;
		int green = 125;
		int blue = 0;

		for (int y = 0; y < (h / 2); y++) {
			for (int x = 0; x < (w / 2); x++) {
				int offset = (y * h) + x;
				double paint = (double) (x * y) / (double) ((h / 2) * (w / 2))
						* 150.;
				green = (int) paint + 100;
				pixels[offset] = ((red & 0xff) << 16) | ((green & 0xff) << 8)
						| (blue & 0xff);
			}
		}
		for (int y = 0; y < (h / 2); y++) {
			for (int x = w - 1; x > (w / 2) - 1; x--) {
				int offset = (y * h) + x;
				double paint = (double) ((h - x) * y)
						/ (double) ((h / 2) * (w / 2)) * 150.;
				green = (int) paint + 100;
				pixels[offset] = ((red & 0xff) << 16) | ((green & 0xff) << 8)
						| (blue & 0xff);
			}
		}

		for (int y = h - 1; y > (h / 2) - 1; y--) {
			for (int x = w - 1; x > (w / 2) - 1; x--) {
				int offset = (y * h) + x;
				double paint = (double) ((h - x) * (h - y))
						/ (double) ((h / 2) * (w / 2)) * 150.;
				green = (int) paint + 100;
				pixels[offset] = ((red & 0xff) << 16) | ((green & 0xff) << 8)
						| (blue & 0xff);
			}
		}

		for (int y = h - 1; y > (h / 2) - 1; y--) {
			for (int x = 0; x < (w / 2); x++) {
				int offset = (y * h) + x;
				double paint = (double) (x * (h - y))
						/ (double) ((h / 2) * (w / 2)) * 150.;
				green = (int) paint + 100;
				pixels[offset] = ((red & 0xff) << 16) | ((green & 0xff) << 8)
						| (blue & 0xff);
			}
		}

		ip = ip.resize(ip.getWidth() * 2, ip.getHeight() * 2);
		ip.setFont(new Font("SansSerif", Font.BOLD, LARGE_FONT));
		ip.setAntialiasedText(true);
		int[] widths = new int[lines];
		widths[0] = ip.getStringWidth(text[0]);
		for (int i = 1; i < lines - 1; i++)
			widths[i] = ip.getStringWidth(text[i]);
		int max = 0;
		for (int i = 0; i < lines - 1; i++)
			if (widths[i] > max)
				max = widths[i];
		ip.setColor(new Color(0, 0, 0));
		ip.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, LARGE_FONT));
		ip.setJustification(ImageProcessor.LEFT_JUSTIFY);
		int y = 80;
		ip.drawString(text[0], xDim(text[0], ip, max), y);
		ip.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, LARGE_FONT));
		y += 30;
		ip.drawString(text[1], xDim(text[1], ip, max), y);
		y += 25;
		ip.drawString(text[2], xDim(text[2], ip, max), y);
		y += 25;
		ip.setFont(new Font("SansSerif", Font.ITALIC, MEDIUM_FONT));
		ip.drawString(text[3], xDim(text[3], ip, max), y);
		y += 18;
		ip.drawString(text[4], xDim(text[4], ip, max), y);
		y += 18;
		ip.drawString(text[5], xDim(text[5], ip, max), y);
		ImageWindow.centerNextImage();

		new ImagePlus("Controlli Mensili", ip).show();
		IJ.wait(2000);
		close();

	}

	int xDim(String text, ImageProcessor ip, int max) {
		return ip.getWidth() - max + (max - ip.getStringWidth(text)) / 2 - 10;
	}

	public void about2(String arg) {

		int lines = 6;

		String[] text = new String[lines];
		text[0] = arg;
		text[1] = "Azienda Spedali Civili di Brescia";
		text[2] = "Servizio di Fisica Sanitaria";
		text[3] = "©2007-2014  Alberto Duina";
		text[4] = "albertoduina@virgilio.it";
		// text[5] = "VERSIONE " + myImplementationVersion(myClass);

		int w = 150, h = 150;
		ImageProcessor ip = new ColorProcessor(w, h);
		int[] pixels = (int[]) ip.getPixels();
		int red = 0;
		int green = 1;
		int blue = 1;

		for (int y = 0; y < (h / 2); y++) {
			for (int x = 0; x < (w / 2); x++) {
				int offset = (y * h) + x;
				double paint = (double) (x * y) / (double) ((h / 2) * (w / 2))
						* 150.;
				green = 255;
				blue = (int) paint + 100;
				pixels[offset] = ((red & 0xff) << 16) | ((green & 0xff) << 8)
						| (blue & 0xff);
			}
		}
		for (int y = 0; y < (h / 2); y++) {
			for (int x = w - 1; x > (w / 2) - 1; x--) {
				int offset = (y * h) + x;
				double paint = (double) ((h - x) * y)
						/ (double) ((h / 2) * (w / 2)) * 150.;
				green = 255;
				blue = (int) paint + 100;
				pixels[offset] = ((red & 0xff) << 16) | ((green & 0xff) << 8)
						| (blue & 0xff);
			}
		}

		for (int y = h - 1; y > (h / 2) - 1; y--) {
			for (int x = w - 1; x > (w / 2) - 1; x--) {
				int offset = (y * h) + x;
				double paint = (double) ((h - x) * (h - y))
						/ (double) ((h / 2) * (w / 2)) * 150.;
				green = 255;
				red = (int) paint + 100;
				pixels[offset] = ((red & 0xff) << 16) | ((green & 0xff) << 8)
						| (blue & 0xff);
			}
		}

		for (int y = h - 1; y > (h / 2) - 1; y--) {
			for (int x = 0; x < (w / 2); x++) {
				int offset = (y * h) + x;
				double paint = (double) (x * (h - y))
						/ (double) ((h / 2) * (w / 2)) * 150.;
				green = 255;
				blue = (int) paint + 100;
				pixels[offset] = ((red & 0xff) << 16) | ((green & 0xff) << 8)
						| (blue & 0xff);
			}
		}

		ip = ip.resize(ip.getWidth() * 2, ip.getHeight() * 2);
		ip.setFont(new Font("SansSerif", Font.BOLD, LARGE_FONT));
		ip.setAntialiasedText(true);
		int[] widths = new int[lines];
		widths[0] = ip.getStringWidth(text[0]);
		for (int i = 1; i < lines - 1; i++)
			widths[i] = ip.getStringWidth(text[i]);
		int max = 0;
		for (int i = 0; i < lines - 1; i++)
			if (widths[i] > max)
				max = widths[i];
		ip.setColor(new Color(0, 0, 0));
		ip.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, LARGE_FONT));
		ip.setJustification(ImageProcessor.LEFT_JUSTIFY);
		int y = 80;
		ip.drawString(text[0], xDim(text[0], ip, max), y);
		ip.setFont(new Font("SansSerif", Font.ROMAN_BASELINE, LARGE_FONT));
		y += 30;
		ip.drawString(text[1], xDim(text[1], ip, max), y);
		y += 25;
		ip.drawString(text[2], xDim(text[2], ip, max), y);
		y += 25;
		ip.setFont(new Font("SansSerif", Font.ITALIC, MEDIUM_FONT));
		ip.drawString(text[3], xDim(text[3], ip, max), y);
		y += 18;
		ip.drawString(text[4], xDim(text[4], ip, max), y);
		y += 18;
		// ip.drawString(text[5], x(text[5], ip, max), y);
		ImageWindow.centerNextImage();

		new ImagePlus("Controlli Mensili", ip).show();
		IJ.wait(2000);
		close();

	}

	public void close() {
		if (WindowManager.getFrame("Controlli Mensili") != null) {
			IJ.selectWindow("Controlli Mensili");
			IJ.run("Close");
		}
	}

	/**
	 * legge la versione dal file jar
	 * 
	 * @param classPath
	 *            path del file jar
	 * @return implementazione
	 */
	public String myImplementationVersion(Class<?> myClass) {
		String implementation = "unknown";
		String myName = "/" + myClass.getPackage().getName() + "/"
				+ myClass.getSimpleName() + ".class";
		URL url = getClass().getResource(myName);
		String type1 = url.toString().substring(0,
				url.toString().indexOf(":") + 1);
		String manifestPath = "";
		if (type1.equals("file:")) {
			// in questo caso sono in test, restituisco unknown
			return implementation;
		} else {
			manifestPath = url.toString().substring(0,
					url.toString().lastIndexOf("!") + 1)
					+ "/META-INF/MANIFEST.MF";
		}
		try {
			Manifest manifest = new Manifest(new URL(manifestPath).openStream());
			Attributes attr = manifest.getMainAttributes();
			implementation = attr.getValue("Implementation-Version");
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		return implementation;
	}

}
