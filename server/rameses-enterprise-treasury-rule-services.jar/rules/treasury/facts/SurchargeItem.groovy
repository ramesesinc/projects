package rules.treasury.facts;

import java.util.*;

class SurchargeItem extends BillItem {

	BillItem parent;
	String txntype = "surcharge";

	public int hashCode() {
		return (parent?.account?.objid+"_"+parent?.txntype+"_"+ account?.objid+"_"+txntype).hashCode();			
	}

}