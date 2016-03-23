package aml.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;

public class RequireInfo implements RuleActionHandler {

	def result; 

	public void execute( params, drools ) {
		if ( !result.requireinfos ) {
			result.requireinfos = [];
		} 

		result.requireinfos << [id: params.infotype, message: params.message.stringValue]; 
	} 
}