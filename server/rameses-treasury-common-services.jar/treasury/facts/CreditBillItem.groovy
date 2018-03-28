package treasury.facts;

import java.util.*;
import com.rameses.util.*;

class CreditBillItem extends AbstractBillItem {

	String reftype;
	String refid;
	int sortorder = 1000;
	String txntype = "credit";	

	public CreditBillItem( def o ) {
		super(o);
		if(o.refid ) refid = o.refid;
		if(o.reftype) reftype = o.reftype; 
	}

	public def toMap() {
		def m = super.toMap();
		m.refid = refid;
		m.reftype = reftype;
		return m;
	}

	/*
	public int hashCode() {
		if( account?.objid ) {
			throw new Exception("account." + account.objid );
			return account.objid.hashCode();
		}
		else if(txntype) {
			return txntype.hashCode();
		}
		else {
			return this.toString().hashCode();
		}
	}
	*/

}