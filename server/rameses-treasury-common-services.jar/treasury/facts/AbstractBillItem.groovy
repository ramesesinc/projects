package treasury.facts;

import java.util.*;
import enterprise.facts.*;

public abstract class AbstractBillItem {

	Account account;
	double amount;
	double amtpaid;
	double principal;	//original amount 

	int _sortorder = 0;
	String txntype;
	boolean dynamic;	//if true - then this should not be saved in database. Applicable for surcharge and interest
	String remarks;
	
	Org org;			//specified org for account

	//This is used for sharing
	Account nullParentaccount = new Account();

	//tag is used for additional info 
	String tag;

	public AbstractBillItem( def o ) {

		if(o.account) account = o.account;
		if(o.amount) amount = o.amount;
		if(o.amtpaid) amtpaid = o.amtpaid;
		if(o.principal) principal = o.principal;	//original amount 

		if(o.sortorder) sortorder = o.sortorder;
		if(o.txntype) txntype = o.txntype;
		if(o.dynamic) dynamic = o.dynamic;	//if true - then this should not be saved in database. Applicable for surcharge and interest
		if(o.remarks) remarks = o.remarks;
		
		if(o.org) org = o.org;			//specified org for account
	}

	public AbstractBillItem() {}


	public Account getParentaccount() {
		if( account?.parentaccount == null )
			return nullParentaccount;
		return account.parentaccount;
	}

	Org nullOrg = new Org();
	public Org getOrg() {
		if( account?.org == null )
			return nullOrg;
		return account.org;
	}


	//used for sharing

	public int hashCode() {
		if( account?.objid ) {
			return account.objid.hashCode();
		}
		else if(txntype) {
			return txntype.hashCode();
		}
		else {
			return this.toString().hashCode();
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
		m.tag = tag;
		return m;
	}

	public void setSortorder( int s ) {
		_sortorder = s;
	}

	public int getSortorder() {
		return _sortorder;
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