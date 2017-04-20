package obo.facts;

public abstract class OboVariableInfo extends enterprise.facts.VariableInfo {

	double amount;

	public def toMap() {
		def m = super.toMap();
		m.amount = amount;
		return m;
	}	
	
}


