package utils;


import java.util.Comparator;

/**
 * Brescia 30/05/2025
 * 
 * Compara due tabelle per POS
 * 
 * @author www.codejava.net
 *
 */


public class XTablePosComparator implements Comparator<XTable>{
			 
		     @Override
		    public int compare(XTable tab1, XTable tab2) {
		        return Double.compare(tab1.getPos(),tab2.getPos());
		    }

}