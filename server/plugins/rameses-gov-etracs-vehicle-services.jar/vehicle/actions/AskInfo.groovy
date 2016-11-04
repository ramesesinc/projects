package vehicle.actions;

import com.rameses.rules.common.*;
import vehicle.facts.*;
import com.rameses.util.*;
import java.util.*;
import com.rameses.osiris3.common.*;

public class AskInfo  implements RuleActionHandler {
	
	public void execute(def params, def drools) {
		def info = params.variable;
		def index = params.index;
		if(index==null) index = "0";

		def ct = RuleExecutionContext.getCurrentContext();
		if(!ct.result.infos ) {
			ct.result.infos = [];
		}
		
		def infos = ct.result.infos;

		def m = [ variable: [name: info.key, caption: info.value], sortorder: index.toInteger() ];
		if( !infos.find{ it.variable?.name == m.variable.name }  ) {
			infos << m;	
		}
	}

}