package rules.treasury.facts;

import java.util.*;

public abstract class AbstractBillItem {

	Account account;
	double amount;
	double amtpaid;
	
	double principal;	//original amount 

	int sortorder = 0;
	String txntype = "fee";
	boolean dynamic;	//if true - then this should not be saved in database. Applicable for surcharge and interest

	public int hashCode() {
		return (account?.objid+"_"+txntype).hashCode();			
	}

	public boolean equals( def o ) {
		return (o.hashCode() == hashCode());	
	}

	public def toMap() {
		def m = [:];
		m.item = account?.toMap();
		m.amount = amount;
		m.amtpaid = amtpaid;
		m.balance = amount - amtpaid;
		m.txntype = txntype;
		m.sortorder = sortorder;
		return m;
	}
}