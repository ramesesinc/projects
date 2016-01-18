package bpls.actions;

import com.rameses.rules.common.*;
import bpls.facts.*;

public class AddSysVar implements RuleActionHandler {
	
	def request;
	def comparator;

	public void execute(def params, def drools) {
		def vars = request.vars;
		String name = params.name;
		String agg = params.aggregate;
		String dtype = params.datatype;
		def value = params.value;
		if( agg == "COUNT") dtype = "integer";
		
		def var = vars[name];
		if( var == null ) {
			var = [datatype:dtype, name: name ];
			vars[name] = var;
		}
		def newAmt = 0;
		if( agg !="COUNT") {
			newAmt = (dtype=="integer") ? value.intValue : value.doubleValue;
		}
		
		def oldAmt = (var.value==null) ? 0 : var.value;
		var.value = comparator( agg, oldAmt, newAmt );
 		
	}
}
