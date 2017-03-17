package enterprise.facts;

class Requirement {
	
	RequirementType type;
	boolean completed;

	public int hashCode() {
		return type.objid.hashCode();
	}

	public boolean equals(def o ) {
		return (hashCode()==o.hashCode());
	}

}