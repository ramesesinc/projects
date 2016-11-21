package rules.treasury.facts;

class Info {
	
	InfoType type;
	Object value;

	public int hashCode() {
		return type.objid.hashCode();
	}

	public boolean equals( def o ) {
		return (hashCode() == o.hashCode());
	}

}