package utils;

import java.util.Comparator;

/**
 * Brescia 30/05/2025
 * 
 * Compara due tabelle per ECHO
 * 
 * @author www.codejava.net
 *
 */


public class XTableEchoComparator implements Comparator<XTable>{
			 
		     @Override
		    public int compare(XTable tab1, XTable tab2) {
		        return Double.compare(tab1.getEcho(),tab2.getEcho());
		    }


}