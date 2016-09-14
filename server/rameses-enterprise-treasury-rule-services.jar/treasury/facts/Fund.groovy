package treasury.facts;

public class Fund {
	
	String objid;
	String code;
	String title;

	public Fund( def m ) {
		this.objid = m.objid;
		this.code = m.code;
		this.title = m.title;
	}

	def toItem() {
		return [
			objid:objid,
			code: code,
			title: title
		];
	}

	public boolean equals(def obj) {
        return hashCode() == obj.hashCode();
    }    

    public int hashCode() {
    	return this.objid.hashCode();
    }

}