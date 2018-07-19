package enterprise.actions;

import com.rameses.rules.common.*;
import com.rameses.osiris3.*;
import com.rameses.util.*;
import java.util.*;
import enterprise.facts.*;
import com.rameses.osiris3.common.*;

public class AddInfo implements RuleActionHandler {

	
import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import enterprise.facts.*;
import enterprise.utils.*;
import com.rameses.osiris3.common.*;

/***
* Parameters:
*  infotype
*  datatype
*  value  
****/
class AddInfo implements RuleActionHandler {

	public void execute(def params, def drools) {

		def aggtype = params.aggtype;

		if(!params.name) throw new Exception("name is required in any AskSystemInfo action");
		if(!params.aggtype ) throw new Exception("aggtype is required in any AskSystemInfo action");
		if(aggtype!="COUNT" && !params.value) throw new Exception("value is required in any AskSystemInfo action");

		def ct = RuleExecutionContext.getCurrentContext();
		if(! ct.env.infoUtil ) ct.env.infoUtil = new VariableInfoUtil();

		String infoName = params.name.key;

		def value =  0;
		if( params.value ) value = params.value.eval();
		if( aggtype == "COUNT" ) value = 1; 

		/********************************************************************************************
		* check first if input already exists in infos or askinfos do not add if its exists already.
  		*********************************************************************************************/
		boolean include = true;

		def vinfo = ct.facts.find{ (it instanceof VariableInfo) && it.name == infoName  };
		
		if( vinfo == null ) {
			vinfo = ct.env.infoUtil.createFact([name: infoName, value: value]);
			ct.facts << vinfo;
		}
		else {
			def infoUtil = ct.env.infoUtil;
			def oldval =  vinfo.getValue();
			value = infoUtil.getConvertedData( vinfo.datatype, value );
			//place the value
			if( aggtype.matches("COUNT|SUM")) {
				vinfo.setValue(  value + oldval );	
			}
			else if( aggtype == "MIN") {
				if( value < oldval) {
					vinfo.setValue( value );
				}
			}
			else if( aggtype == "MAX" ) {
				if( value > oldval ) {
					vinfo.setValue( value );
				}
			}
			else {
				throw new Exception("Aggregate type " + aggtype + " not supported");
			}
		}
		
	}



}