package treasury.facts;

import java.util.*;

public class BillSubItem extends AbstractBillItem {

	BillItem parent;
	boolean dynamic = true;
	int sortorder = 200;

	public int hashCode() {
		return (parent?.account?.objid+"_"+parent?.txntype+"_"+ account?.objid+"_"+txntype).hashCode();			
	}

	public def toMap() {
		def m = super.toMap();
		m.dynamic = true;
		return m;
	}

}