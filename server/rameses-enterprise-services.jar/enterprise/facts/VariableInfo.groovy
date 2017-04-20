package enterprise.facts;

import java.util.*;

public class VariableInfo {
	
	String objid;
	String name;
	String state;
	String description;
	String caption;
	def arrayvalues;
	String category;
	int sortorder;

	double decimalvalue;
	int intvalue;
	boolean booleanvalue;
	String stringvalue;
	Date datevalue;
	String datatype;

	//just add so that it will match database. if 1=yes 0=no
	int system;

	public int hashCode() {
		return name.hashCode();
	}

	public boolean equals( def o ) {
		return (hashCode() == o.hashCode());
	}

	/********************************************
	* This is from the fact to map data
	********************************************/
	public def toMap() {
		def m = [:];
		m.datatype = datatype;
		m.caption = caption;
		m.category = category;
		m.name = name;
		m.value = null;
		if(m.datatype == 'decimal') m.value  = decimalvalue;
		else if(m.datatype=="integer") m.value = intvalue;
		else if(m.datatype=="boolean") m.value = booleanvalue;
		else if(m.datatype == "date" ) m.value = datevalue;
		else m.value = stringvalue;
		m.arrayvalues = arrayvalues;
		m.sortorder = sortorder;
		return m;
	}

}