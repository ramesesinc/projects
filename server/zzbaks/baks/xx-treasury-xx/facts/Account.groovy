package treasury.facts;

public class Account {
	
	String objid;
	String code;
	String title;
	Fund fund;

	public Account( def m ) {
		this.objid = m.objid;
		this.code = m.code;
		this.title = m.title;
		if( m.fund ) {
			this.fund = new Fund( m.fund );	
		}
	}

	def toItem() {
		return [
			objid:objid,
			code: code,
			title: title,
			fund: fund?.toItem()
		];
	}

	public boolean equals(def obj) {
        return hashCode() == obj.hashCode();
    }    

    public int hashCode() {
    	return this.objid.hashCode();
    }

}