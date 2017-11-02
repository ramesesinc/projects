package treasury.facts;

import java.util.*;

public abstract class AbstractBillItem {

	Account account;
	double amount;
	double amtpaid;
	double principal;	//original amount 

	int sortorder = 0;
	String txntype;
	boolean dynamic;	//if true - then this should not be saved in database. Applicable for surcharge and interest
	String remarks;

	public int hashCode() {
		if( account?.objid ) {
			return account.objid.hashCode();
		}
		else if(txntype) {
			return txntype.hashCode();
		}
		else {
			return this.hashCode();
		}
	}

	public boolean equals( def o ) {
		return (o.hashCode() == hashCode());	
	}

	public def toMap() {
		if(!principal) principal = amount;

		def m = [:];
		m.item = account?.toMap();
		m.amount = amount;
		m.amtpaid = amtpaid;
		m.principal = principal;
		m.balance = amount - amtpaid;
		m.txntype = txntype;
		m.sortorder = sortorder;
		m.remarks = remarks;
		return m;
	}

	def createClone() {
		return this.class.newInstance();
  	}  

	public final def clone() {
		def p = createClone();
		this.metaClass.properties.each { k ->
			if( !k.name.matches("class|metaClass")) {
				p[(k.name)] = this.getProperty( k.name );
			}
		}
		return p;
	}

	public void copy( def o ) {
		this.metaClass.properties.each { k ->
			if( !k.name.matches("class|metaClass")) {
				//add only if there is a setter
				if( k.setter && o.containsKey(k.name)) {
					this[(k.name)] = o.get( k.name );	
				}
			}
		}
	}

}