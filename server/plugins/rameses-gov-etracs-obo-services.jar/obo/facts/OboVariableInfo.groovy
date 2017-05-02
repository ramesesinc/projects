package obo.facts;

public abstract class OboVariableInfo extends enterprise.facts.VariableInfo {

	String excludeFields = "|classificationid|section|classification";
	def amount;

	public def toMap() {
		def m = super.toMap();
		m.amount = amount;
		return m;
	}	
	
}


