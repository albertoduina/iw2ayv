package utils;

import java.util.Comparator;

/**
 * Brescia 30/05/2025
 * 
 * Compara due tabelle per CODE
 * 
 * @author www.codejava.net
 *
 */

public class XTableCodeComparator implements Comparator<XTable>{
		 
	    // @Override
	    public int compare(XTable tab1, XTable tab2) {
	        return tab1.getCode().compareTo(tab2.getCode());
	    }

}
