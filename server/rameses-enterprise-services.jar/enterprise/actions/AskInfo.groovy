package enterprise.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import enterprise.facts.*;
import enterprise.utils.*;
import com.rameses.osiris3.common.*;

/***
* Parameters:
*  infotype
*  defaultvalue  - 
****/
public class AskInfo implements RuleActionHandler {

	public String getInfoSchemaName() {
		return null;	
	}

	public void execute(def params, def drools) {
		def infotype = params.name;
		if(!infotype) throw new Exception("type is required in any AskInfo action");

		String infoName = params.name.key;
		def value = null;
		if(params.value) {
			value = params.value.eval();
		}	
		
		def ct = RuleExecutionContext.getCurrentContext();

		/********************************************************************************************
		* check first if input already exists in infos or askinfos do not add if its exists already.
  		*********************************************************************************************/
		boolean include = true;
		if( ct.facts.find{ (it instanceof VariableInfo) && it.name == infoName  }!=null ) {
			include = false;
		}
		else if(ct.result.infos) {
			if( ct.result.infos.find{ it.name == infoName } ) include = false;
		}
		else if( ct.result.askinfos ) {
			if( ct.result.askinfos.find{ it.name == infoName } ) include = false;
		}	

		if(include) {
			if( !ct.result.askinfos ) ct.result.askinfos = new LinkedHashSet<VariableInfo>();
			if(! ct.env.infoUtil ) {
				ct.env.infoUtil = new VariableInfoUtil();
			}	
			String sname = getInfoSchemaName();
			if( sname!=null ) {
				 ct.env.infoUtil.schemaName = sname; 
			}
			def vinfo = ct.env.infoUtil.createFact([name: infoName, value: value]);
			ct.result.askinfos.add( vinfo  );
		}
	}

}