package aml.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;

public class AddRedFlag implements RuleActionHandler {

	def result; 

	public void execute( params, drools ) { 
		if ( !result.redflags ) {
			result.redflags = [];
		} 

		result.redflags << params.message.stringValue;
	} 
}