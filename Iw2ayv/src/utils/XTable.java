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
 * Brescia 30/05/2025
 * 
 * Release sperimentale della tabella contenente i dati di iw2ayv. In questo
 * modo dovrebbe essere possibile effettuare semplicemente il sort con criteri
 * multipli, come quello di Excel
 * 
 * @author Alberto Duina - SPEDALI CIVILI DI BRESCIA - Servizio di Fisica
 *         Sanitaria
 * 
 */

public class XTable {
	private int row;
	private String path;
	private String code;
	private String coil;
	private int imapass;
	private int imaorder;
	private int imaincr;
	private int multipli;
	private int spare2;
	private int spare3;
	private int serie;
	private int acq;
	private int ima;
	private String acqtime;
	private double echo;
	private double pos;
	private String dir;
	private int prof;
	private int done;

	public XTable(int row, String path, String code, String coil, int imapass, int imaorder, int imaincr, int multipli,
			int spare2, int spare3, int serie, int acq, int ima, String acqtime, double echo, double pos, String dir,
			int prof, int done) {
		this.row = row;
		this.path = path;
		this.code = code;
		this.coil = coil;
		this.imapass = imapass;
		this.imaorder = imaorder;
		this.imaincr = imaincr;
		this.multipli = multipli;
		this.spare2 = spare2;
		this.spare3 = spare3;
		this.serie = serie;
		this.acq = acq;
		this.ima = ima;
		this.acqtime = acqtime;
		this.echo = echo;
		this.pos = pos;
		this.dir = dir;
		this.prof = prof;
		this.done = done;
	}

	// getters and setters

	public int getRow() {
		return row;
	}

	public String getPath() {
		return code;
	}

	public String getCode() {
		return code;
	}

	public String getCoil() {
		return coil;
	}

	public int getImapass() {
		return imapass;
	}

	public int getImaorder() {
		return imaorder;
	}

	public int getImaincr() {
		return imaincr;
	}

	public int getMultipli() {
		return multipli;
	}

	public int getAcq() {
		return acq;
	}

	public int getSpare2() {
		return spare2;
	}

	public int getSpare3() {
		return spare3;
	}

	public int getSerie() {
		return serie;
	}

	public int getIma() {
		return ima;
	}

	public String getAcqtime() {
		return acqtime;
	}

	public double getEcho() {
		return echo;
	}

	public double getPos() {
		return pos;
	}

	public String getDir() {
		return dir;
	}

	public int getProf() {
		return prof;
	}

	public int getDone() {
		return done;
	}

	public String toString() {
		return "" + row + "; " + path + "; " + code + "; " + coil + "; " + imapass + "; " + imaorder + "; " + imaincr
				+ "; " + multipli + "; " + spare2 + "; " + spare3 + "; " + serie + "; " + acq + "; " + ima + "; "
				+ acqtime + "; " + echo + "; " + pos + "; " + dir + "; " + prof + "; " + done;
	}

//	public String toString() {
//		return String.format("%d; %s; %s; %d; %d; %d; %d; %d; %d; %d; %d; %d; %s; %.1f; %.1f; %s; %d; %d", row, code,
//				coil, imapass, imaorder, imaincr, multipli, spare2, spare3, serie, acq, ima, acqtime, echo, pos, dir,
//				prof, done);
//	}
//	public String toString() {
//		return String.format("%d;\t%s;\t%s;\t%d;\t%d;\t%d;\t%d;\t%d;\t%d;\t%d;\t%d;\t%d;\t%s;\t%.1f;\t%.1f;\t%s;\t%d;\t%d", row, code,
//				coil, imapass, imaorder, imaincr, multipli, spare2, spare3, serie, acq, ima, acqtime, echo, pos, dir,
//				prof, done);
//	}

}