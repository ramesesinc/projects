package treasury.facts;

class RevenueShare {
	
	Account refaccount;
	Account payableaccount;
	double amount;

	public Map toMap() {
		def m = [:];
		m.refaccount = refaccount.toMap();
		m.payableaccount = payableaccount.toMap();
		m.amount = amount;
		return m;
	}

	public int hashCode() {
		return (refaccount.hashCode() + "" + payableaccount.hashCode()).hashCode();
	}

	public boolean equals( def o ) {
		return hashCode() == o.hashCode();
	}

}