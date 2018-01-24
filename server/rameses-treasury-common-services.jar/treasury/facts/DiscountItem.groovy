package treasury.facts;

import java.util.*;

class DiscountItem extends BillSubItem {

	String txntype = "DISCOUNT";
	int sortorder = 300;


	public def toMap() {
		def m = super.toMap();
		m.txntype = "discount";
		return m;
	}
}