package utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Brescia 30/05/2025
 * 
 * This is a chained comparator that is used to sort a list by multiple
 * attributes by chaining a sequence of comparators of individual fields
 * together.
 * 
 * @author www.codejava.net
 *
 */

public class XTableChainedComparator implements Comparator<XTable> {

	private List<Comparator<XTable>> listComparators;

	@SafeVarargs
	public XTableChainedComparator(Comparator<XTable>... comparators) {
		this.listComparators = Arrays.asList(comparators);
	}

	@Override
	public int compare(XTable tab1, XTable tab2) {
		for (Comparator<XTable> comparator : listComparators) {
			int result = comparator.compare(tab1, tab2);
			if (result != 0) {
				return result;
			}
		}
		return 0;
	}

}
