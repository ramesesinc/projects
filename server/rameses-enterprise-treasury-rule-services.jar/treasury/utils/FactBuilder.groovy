package treasury.utils;

import treasury.facts.*;
import enterprise.facts.*;

public class FactBuilder {
	
	String infoSchemaName = "variableinfo";
	def facts = [];	
	
	public VariableInfo getInfoFact( def o ) {
		return new VariableInfo( o );
	}	

	public BillItem getBillItemFact( def o ) {
		return new BillItem(o);
	}

	public Requirement getRequirementFact(def o) {
		return new Requirement(o);
	}


}