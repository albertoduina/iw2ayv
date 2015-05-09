package utils;

import ij.gui.MultiLineLabel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JDialog;

/*
 * Copyright (C) 2015 Luca Rossi, SPEDALI CIVILI DI BRESCIA, Brescia ITALY
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
 * @author Luca Rossi - SPEDALI CIVILI DI BRESCIA - Servizio di Fisica Sanitaria
 * 
 */
public class AutoDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	public static String VERSION = "ModelessDialog-v3.00_29jan07_";

//	static String strUno = "none";
//
//	static String strDue = "none";
//
//	static String strTre = "none";
//
//	static String strQuattro = "none";
//
//	static String strCinque = "none";

	JButton auto;
	MultiLineLabel ml;
	String testo;
	int millDelay = 5000;
	String[] vetPulsanti;

	int countDown;
	boolean autoPlay;
	
	int cliccato = Integer.MAX_VALUE;
	
	ScheduledExecutorService ses1 = Executors.newSingleThreadScheduledExecutor();
	ScheduledExecutorService ses2 = Executors.newSingleThreadScheduledExecutor();

	
	// --------------------------------------------------------------------------------------/

	public AutoDialog(String question, String[] vetPulsanti, String autoStr,
			boolean modal) {
		if(modal)
		{
			setModal(true);
		}
		if (autoStr != null) {
			autoPlay = true;
		}
		
		this.vetPulsanti = vetPulsanti.clone();

		MultiLineLabel mll = new MultiLineLabel(question);
		mll.setFont(new Font("Dialog", Font.PLAIN, 12));
		add(mll, BorderLayout.NORTH);
		
		ml = mll;
		testo = question;
		
		Panel np = new Panel();
		np.setLayout(new GridLayout(1,0));

		for (int i1 = 0; i1 < vetPulsanti.length; i1++) {
			JButton button = new JButton(vetPulsanti[i1]);
			button.addActionListener(this);
			np.add(button);

			if (vetPulsanti[i1].equalsIgnoreCase(autoStr)) {
				auto = button;
			}
		}
		add(np, BorderLayout.SOUTH);
		pack();
		setVisible(true);
	}

	public void setVisible(boolean visible) {
		if(!visible)
		{
			super.setVisible(false);
			return;
		}
		if (autoPlay) {
			countDown = (millDelay - (millDelay % 1000)) / 1000;
			ses1.schedule(new Runnable() {
				public void run() {
					if (isShowing()) {
						auto.doClick(500);
					}
				}
			}, millDelay, TimeUnit.MILLISECONDS);
			ses2.scheduleAtFixedRate(new Runnable() {
				public void run() {
					ml.setText(testo + " (" + countDown-- + ")");
					if (countDown < 0) {
						ml.setText(testo);
						ses2.shutdown();
					}
					// pack();
				}

			}, 0, 1000, TimeUnit.MILLISECONDS);

		}

		super.setVisible(true);
	}

	synchronized public int answer() {
		
		while(cliccato == Integer.MAX_VALUE)
			try
			{
				wait();
			}
			catch(InterruptedException ie)
			{}
		return cliccato;
	}

	synchronized public void actionPerformed(ActionEvent e) {
		cliccato = Arrays.asList(vetPulsanti).indexOf(e.getActionCommand()) + 1;
		notifyAll();
		setVisible(false);
	}
}
