package utils;

import java.util.Comparator;


/**
 * Brescia 30/05/2025
 * 
 * Compara due tabelle per ACQ
 * 
 * @author www.codejava.net
 *
 */

public class XTableAcqComparator implements Comparator<XTable>{
			 
		     @Override
		    public int compare(XTable tab1, XTable tab2) {
		        return tab1.getAcq() - tab2.getAcq();
		    }


}