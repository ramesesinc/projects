package rules.treasury.facts;

public abstract class VariableInfo {
	
	VariableInfoType type;

	public String getDatatype() {
		return  "object";
	}

	public int hashCode() {
		return type.objid.hashCode();
	}

	public boolean equals( def o ) {
		return (hashCode() == o.hashCode());
	}

}