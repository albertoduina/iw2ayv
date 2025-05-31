package utils;

import java.util.Comparator;

/**
 * Brescia 30/05/2025
 * 
 * Compara due tabelle per COIL
 * 
 * @author www.codejava.net
 *
 */

public class XTableCoilComparator implements Comparator<XTable>{
			 
		    // @Override
		    public int compare(XTable tab1, XTable tab2) {
		        return tab1.getCoil().compareTo(tab2.getCoil());
		    }


}
