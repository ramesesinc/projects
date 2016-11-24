package rules.enterprise.facts;

public abstract class VariableInfo {
	
	String name;
	String caption;
	List arrayvalues;
	String category;
	int sortorder;

	public String getDatatype() {
		return  "object";
	}

	public int hashCode() {
		return name.hashCode();
	}

	public boolean equals( def o ) {
		return (hashCode() == o.hashCode());
	}

	public def toMap() {
		def m = [:];
		m.datatype = datatype;
		m.caption = caption;
		m.name = name;
		m.value = value;
		m.arrayvalues = arrayvalues;
		m.sortorder = sortorder;
		return m;
	}

}