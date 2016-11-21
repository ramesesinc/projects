package rules.treasury.facts;

import java.util.*;

public abstract class AbstractBillItem {

	Account account;
	double amount;
	double amtpaid;
	String txntype;

	boolean dynamic;	//if true - then this should not be saved in database. Applicable for surcharge and interest

	public int hashCode() {
		return (account?.objid+"_"+txntype).hashCode();			
	}

	public boolean equals( def o ) {
		return (o.hashCode() == hashCode());	
	}


}