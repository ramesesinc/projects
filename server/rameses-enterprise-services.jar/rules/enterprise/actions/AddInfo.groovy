package rules.enterprise.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import rules.enterprise.facts.*;
import com.rameses.osiris3.common.*;

/***
* Parameters:
*  infotype
*  datatype
*  value  
****/
class AddInfo implements RuleActionHandler {

	public void execute(def params, def drools) {
		def infotype = params.infotype;
		def value = null;
		if(params.value) {
			value = params.value.eval();
		}

		def ct = RuleExecutionContext.getCurrentContext();
		if( !ct.result.infos ) ct.result.infos = new LinkedHashSet<Info>();
		def infos = ct.result.infos;

		Info info = new Info(value: value, new InfoType( objid: infotype.key, title: infotype.value ));
		boolean b = infos.add( info );
		if( b && info.value !=null  ) {
			facts << info;
		}
	}

}