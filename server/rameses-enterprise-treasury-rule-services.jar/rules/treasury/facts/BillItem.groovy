package rules.treasury.facts;

import java.util.*;

class BillItem extends AbstractBillItem {

	int sortorder;

	LinkedHashSet<SubBillItem> items = new LinkedHashSet<SubBillItem>();

	public def getTotals( def txntype ) {
		return items.findAll{ it.txntype == txntype }.sum{it.amount};
	}

	public double getTotal() {
		return amount + items.sum{ it.amount };	
	}

}