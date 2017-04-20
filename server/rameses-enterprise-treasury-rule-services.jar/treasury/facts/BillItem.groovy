package treasury.facts;

import java.util.*;
import com.rameses.util.*;

class BillItem extends AbstractBillItem {

	String refid;
	String ledgertype;

	//pay priority is only used during apply payment and will not be used anywhere else. This is defined by the extending class.
	int paypriority = 0;
	
	LinkedHashSet<BillSubItem> items = new LinkedHashSet<BillSubItem>();

	public def getTotals( def txntype ) {
		return items.findAll{ it.txntype == txntype }.sum{it.amount};
	}

	public double getTotal() {
		if(items.size()>0) {
			return  NumberUtil.round( amount + items.sum{ it.amount } );		
		}
		else {
			return NumberUtil.round( amount );
		}
	};

	public int hashCode() {
		if( refid ) {
			return refid.hashCode();
		} 
		else {
			return super.hashCode();
		}
	}

	public def toMap() {
		def m = super.toMap();
		m.refid = refid;
		m.ledgertype = ledgertype;
		items.each {
			if(it.amount == null) it.amount = 0;
			m.put(it.txntype, NumberUtil.round(it.amount));
		};
		m.total = total;
		return m;
	}

	//call this after apply payment
	void recalc() {;}


}