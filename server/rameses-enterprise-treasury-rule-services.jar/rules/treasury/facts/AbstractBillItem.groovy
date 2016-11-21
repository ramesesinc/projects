package rules.treasury.facts;

import java.util.*;

public abstract class BillItem {

	Account account;
	double amount;
	double amtpaid;
	String txntype;

	public int hashCode() {
		return (account?.objid+"_"+txntype).hashCode();			
	}

	public boolean equals( def o ) {
		return (o.hashCode() == hashCode());	
	}


}