package aml.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import com.rameses.osiris3.script.*;

public class CollectException implements RuleActionHandler {

	def result; 

	public void execute( params, drools ) {
		if ( !result.exceptions ) {
			result.exceptions = [];
		}

		result.exceptions << params.message.stringValue; 
	} 
}