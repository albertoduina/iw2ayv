package utils;

import ij.IJ;
import ij.Prefs;
import ij.io.OpenDialog;
import ij.util.Java2;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

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
 * This class displays a dialog box that allows the user can select a directory.
 * 
 * @author Wayne Rasband (wayne@codon.nih.gov), Research Services Branch,
 *         National Institute of Mental Health, Bethesda, Maryland, USA.
 * 
 */


public class DirectoryChooser2 {

	
	public static String VERSION = "DirectoryChooser2-v3.00_29jan07_";

	private String directory;

	/** Display a dialog using the specified title. */
	public DirectoryChooser2(String title, String dir2) {
		if (IJ.isMacOSX() && IJ.isJava14())
			getDirectoryUsingFileDialog(title);
		else if (IJ.isJava2()) {
			if (EventQueue.isDispatchThread())

				getDirectoryUsingJFileChooserOnThisThread(title, dir2);
			else

				getDirectoryUsingJFileChooser(title, dir2);

		} else {
			OpenDialog od = new OpenDialog(title, null);
			directory = od.getDirectory();
		}
	}

	// runs JFileChooser on event dispatch thread to avoid possible thread
	// deadlocks
	void getDirectoryUsingJFileChooser(final String title, final String dir2) {
		Java2.setSystemLookAndFeel();
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new File(dir2));
					chooser.setDialogTitle(title);
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					chooser.setApproveButtonText("Select");
					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						File dir = chooser.getCurrentDirectory();
						File file = chooser.getSelectedFile();
						directory = dir.getPath();
						if (!directory.endsWith(File.separator))
							directory += File.separator;

						String fileName = file.getName();
						if (fileName.indexOf(":\\") != -1)
							directory = fileName;
						else
							directory += fileName + File.separator;
					}
				}
			});
		} catch (Exception e) {
		}
	}

	// Choose a directory using JFileChooser on the current thread
	void getDirectoryUsingJFileChooserOnThisThread(final String title,
			final String dir2) {
		Java2.setSystemLookAndFeel();
		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(dir2));
			chooser.setDialogTitle(title);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setApproveButtonText("Select");
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				File dir = chooser.getCurrentDirectory();
				File file = chooser.getSelectedFile();
				directory = dir.getPath();
				if (!directory.endsWith(File.separator))
					directory += File.separator;
				String fileName = file.getName();
				if (fileName.indexOf(":\\") != -1)
					directory = fileName;
				else
					directory += fileName + File.separator;
			}
		} catch (Exception e) {
		}
	}

	// On Mac OS X, we can select directories using the native file open dialog
	void getDirectoryUsingFileDialog(String title) {
		boolean saveUseJFC = Prefs.useJFileChooser;
		Prefs.useJFileChooser = false;
		System.setProperty("apple.awt.fileDialogForDirectories", "true");
		OpenDialog od = new OpenDialog(title, null);
		directory = od.getDirectory() + od.getFileName() + "/";
		System.setProperty("apple.awt.fileDialogForDirectories", "false");
	
	Prefs.useJFileChooser = saveUseJFC;
	}

	/** Returns the directory selected by the user. */
	public String getDirectory() {
		return directory;
	}

}
