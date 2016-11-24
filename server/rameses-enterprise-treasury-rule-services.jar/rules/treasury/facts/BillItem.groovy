package rules.treasury.facts;

import java.util.*;

class BillItem extends AbstractBillItem {

	String refid;
	
	LinkedHashSet<BillSubItem> items = new LinkedHashSet<BillSubItem>();

	public def getTotals( def txntype ) {
		return items.findAll{ it.txntype == txntype }.sum{it.amount};
	}

	public double getTotal() {
		return amount + items.sum{ it.amount };	
	}

	public def toMap() {
		def m = super.toMap();
		m.refid = refid;
		return m;
	}

}