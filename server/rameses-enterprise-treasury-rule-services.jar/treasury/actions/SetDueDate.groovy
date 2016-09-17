package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;

public class SetDueDate implements RuleActionHandler {

	public void execute(def params, def drools) {
		def ct = RuleExecutionContext.getCurrentContext();

		def billdate = params.duedate.getStringValue();
		def type = params.type;

		if( !billdate )
			throw new Exception("Due date is required in SetDueDate rule action");

		if( !type )
			throw new Exception("Type is required in SetDueDate action");

		def dt = new DueDate(billdate);
		ct.facts.add( dt );

		if( !ct.result.duedates ) {
			ct.result.duedates = [:];	
		} 
		ct.result.duedates.put( type, dt.date ); 

	}
	
}