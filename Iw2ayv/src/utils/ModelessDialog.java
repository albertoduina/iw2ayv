package utils;

import ij.gui.GUI;
import ij.gui.MultiLineLabel;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
 * Implementa un dialogo modale e non modale da 1 a 5 pulsanti
 * 
 * @author Alberto Duina - SPEDALI CIVILI DI BRESCIA - Servizio di Fisica
 *         Sanitaria
 * 
 */
public class ModelessDialog extends Dialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	public static String VERSION = "ModelessDialog-v3.00_29jan07_";

	private boolean isAnswered = false, isUno = false, isDue = false,
			isTre = false, isQuattro = false, isCinque = false;

	static String strUno = "none";

	static String strDue = "none";

	static String strTre = "none";

	static String strQuattro = "none";

	static String strCinque = "none";

	int jbuttonAutomatico = 0;

	int timeout = 0;

	// --------------------------------------------------------------------------------------/

	public ModelessDialog(String question, String cinque, String quattro,
			String tre, String due, String uno, boolean modal) {
		super(new Frame(), modal);

		strUno = uno;
		strDue = due;
		strTre = tre;
		strQuattro = quattro;
		strCinque = cinque;

		MultiLineLabel mll = new MultiLineLabel(question);
		mll.setFont(new Font("Dialog", Font.PLAIN, 12));
		add("Center", mll);
		Panel np = new Panel();

		Button button = new Button(strCinque);
		button.addActionListener(this);
		np.add(button);
		button = new Button(strQuattro);
		button.addActionListener(this);
		np.add(button);
		button = new Button(strTre);
		button.addActionListener(this);
		np.add(button);
		add("South", np);
		button = new Button(strDue);
		button.addActionListener(this);
		np.add(button);
		add("South", np);
		button = new Button(strUno);
		button.addActionListener(this);
		np.add(button);
		add("South", np);
		pack();
	}

	public ModelessDialog(String question, String cinque, String quattro,
			String tre, String due, String uno) {
		this(question, cinque, quattro, tre, due, uno, false);
	}

	public ModelessDialog(String question, String quattro, String tre,
			String due, String uno, boolean modal) {
		super(new Frame(), modal);
		strUno = uno;
		strDue = due;
		strTre = tre;
		strQuattro = quattro;

		MultiLineLabel mll = new MultiLineLabel(question);
		mll.setFont(new Font("Dialog", Font.PLAIN, 12));
		add("Center", mll);
		Panel np = new Panel();

		Button button = new Button(strQuattro);
		button.addActionListener(this);
		np.add(button);
		button = new Button(strTre);
		button.addActionListener(this);
		np.add(button);
		add("South", np);
		button = new Button(strDue);
		button.addActionListener(this);
		np.add(button);
		add("South", np);
		button = new Button(strUno);
		button.addActionListener(this);
		np.add(button);
		add("South", np);
		pack();
	}

	public ModelessDialog(String question, String quattro, String tre,
			String due, String uno) {
		this(question, quattro, tre, due, uno, false);
	}

	public ModelessDialog(String question, String tre, String due, String uno,
			boolean modal) {
		super(new Frame(), modal);
		strUno = uno;
		strDue = due;
		strTre = tre;

		MultiLineLabel mll = new MultiLineLabel(question);
		mll.setFont(new Font("Dialog", Font.PLAIN, 12));
		add("Center", mll);
		Panel np = new Panel();

		Button button = new Button(strTre);
		button.addActionListener(this);
		np.add(button);
		button = new Button(strDue);
		button.addActionListener(this);
		np.add(button);
		add("South", np);
		button = new Button(strUno);
		button.addActionListener(this);
		np.add(button);
		add("South", np);
		pack();
	}

	public ModelessDialog(String question, String tre, String due, String uno) {
		this(question, tre, due, uno, false);
	}

	public ModelessDialog(String question, String due, String uno, boolean modal) {
		super(new Frame(), modal);
		strUno = uno;
		strDue = due;

		MultiLineLabel mll = new MultiLineLabel(question);
		mll.setFont(new Font("Dialog", Font.PLAIN, 12));
		add("Center", mll);
		Panel np = new Panel();

		Button button = new Button(strDue);
		button.addActionListener(this);
		np.add(button);
		add("South", np);
		button = new Button(strUno);
		button.addActionListener(this);
		np.add(button);
		add("South", np);
		pack();
	}

	public ModelessDialog(String question, String due, String uno) {
		this(question, due, uno, false);
	}

	public ModelessDialog(String question, String uno, boolean modal) {
		super(new Frame(), modal);
		strUno = uno;

		MultiLineLabel mll = new MultiLineLabel(question);
		mll.setFont(new Font("Dialog", Font.PLAIN, 12));
		add("Center", mll);
		Panel np = new Panel();

		Button button = new Button(strUno);
		button.addActionListener(this);
		np.add(button);
		add("South", np);
		pack();
	}

	public ModelessDialog(String question, String uno) {
		this(question, uno, false);
	}

	public ModelessDialog(String question, boolean modal) {
		super(new Frame(), modal);

		MultiLineLabel mll = new MultiLineLabel(question);
		mll.setFont(new Font("Dialog", Font.PLAIN, 12));
		add("Center", mll);
		pack();
	}

	public ModelessDialog(String question) {
		this(question, false);
		GUI.center(this);
	}

	public int getJbuttonAutomatico() {
		return jbuttonAutomatico;
	}

	public void setJbuttonAutomatico(int value) {
		jbuttonAutomatico = value;
	}

	public void setTimeout(int value) {
		timeout = value;
	}

	synchronized public int answer() {
		
		
		while (!isAnswered)
			try {
				wait();
			} catch (InterruptedException e) { /* error */
			}
		if (isCinque)
			return 5;
		else if (isQuattro)
			return 4;
		else if (isTre)
			return 3;
		else if (isDue)
			return 2;
		else if (isUno)
			return 1;
		else
			return 0;
	}

	synchronized public void actionPerformed(ActionEvent e) {


		isCinque = e.getActionCommand().equals(strCinque);
		isQuattro = e.getActionCommand().equals(strQuattro);
		isTre = e.getActionCommand().equals(strTre);
		isDue = e.getActionCommand().equals(strDue);
		isUno = e.getActionCommand().equals(strUno);
		isAnswered = true;
		notifyAll();
		dispose();
	}
}
