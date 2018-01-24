package treasury.facts;

/**********************************************************************
* If you need  aspecial sharing, just extend this class
**********************************************************************/

class RevenueShare {
	

	Account refaccount;
	Account payableaccount;
	double amount;
	double share;

	public Map toMap() {
		def m = [:];
		m.refaccount = refaccount.toMap();
		if( payableaccount) {
			m.payableaccount = payableaccount.toMap();
		}	
		m.amount = amount;
		m.share = share;
		return m;
	}

	public int hashCode() {
		if( payableaccount ) {
			return (refaccount.hashCode() + "" + payableaccount.hashCode()).hashCode();
		}
		else {
			return refaccount.hashCode();
		}
	}

	public boolean equals( def o ) {
		return hashCode() == o.hashCode();
	}

}