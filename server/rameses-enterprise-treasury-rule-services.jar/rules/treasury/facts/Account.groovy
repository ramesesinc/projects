package rules.treasury.facts;

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

}