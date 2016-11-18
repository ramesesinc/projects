package rules.treasury.facts;

import java.util.*;

class InterestItem extends BillItem {

	BillItem parent;
	String txntype = "interest";

	public int hashCode() {
		return (parent?.account?.objid+"_"+parent?.txntype+"_"+ account?.objid+"_"+txntype).hashCode();			
	}

}