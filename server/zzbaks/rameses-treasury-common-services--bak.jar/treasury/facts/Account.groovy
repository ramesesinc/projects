package treasury.facts;

class Account {
	
	Fund fund;
	String objid;
	String code;
	String title;
	int sortorder;

	public Map toMap() {
		def m = [:];
		m.objid = objid;
		m.code = code;
		m.title = title;
		m.fund = fund?.toMap();
		return m;
	}

	public int hashCode() {
		return objid.hashCode();
	}

	public boolean equals( def o ) {
		return hashCode() == o.hashCode();
	}

}