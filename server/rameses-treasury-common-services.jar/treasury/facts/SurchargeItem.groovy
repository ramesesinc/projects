package treasury.facts;

import java.util.*;

class SurchargeItem extends BillSubItem {

	int sortorder = 400;

	public def toMap() {
		def m = super.toMap();
		m.txntype = getTxntype();
		return m;
	}

	public String getTxntype() {
		return "surcharge";
	}
}