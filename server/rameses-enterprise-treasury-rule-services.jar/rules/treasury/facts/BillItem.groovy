package rules.treasury.facts;

import java.util.*;

class BillItem {

	Account account;
	double amount;
	double amtpaid;
	String txntype;

	LinkedHashSet<BillItem> items = new LinkedHashSet<BillItem>();

	public int hashCode() {
		return (account?.objid+"_"+txntype).hashCode();			
	}

	public boolean equals( def o ) {
		return (o.hashCode() == hashCode());	
	}

	public def getTotals( def txntype ) {
		return items.findAll{ it.txntype == txntype }.sum{it.amount};
	}

	public double getTotal() {
		return amount + items.sum{ it.amount };	
	}

}