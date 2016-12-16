package rules.enterprise.facts;

import com.rameses.rules.common.*;
import com.rameses.osiris3.common.*;
import com.rameses.util.*;

public class VariableInfoUtil {
	
	private def map = [:];
	private def svc;

	public def lookup( def vname ) {
		if(svc==null) {
			svc = EntityManagerUtil.lookup( "variableinfo" );
		}
		if( ! map.containsKey(vname)) {
			def m = svc.find( [name: vname] ).first();	
			if( !m ) throw new Exception("Variable Info not found " + vname + " in VariableInfoLookup");
			map.put(vname, m );
		}
		return map.get(vname);		
	}
 
	public def createFact(def v) {
		def o = lookup(v.name);
		def value = v.value;
		VariableInfo vinfo = new VariableInfo();
		vinfo.name = o.name;
		vinfo.caption = o.caption;
		vinfo.arrayvalues = o.arrayvalues;
		vinfo.category = o.category;
		vinfo.sortorder = o.sortorder;
		vinfo.datatype = o.datatype;		

		if( value!=null ) {
			if(o.datatype == 'decimal') {
				vinfo.decimalvalue = (""+value).toDouble();
			}
			else if(o.datatype=="integer") {
				vinfo.intvalue = (""+value).toInteger();
			}
			else if(o.datatype=="boolean") {
				vinfo.booleanvalue = (""+value).toBoolean();
			}
			else if( o.datatype == "date" ) {
				if(!(value instanceof Date)) {
					def df = new java.text.SimpleDateFormat("yyyy-MM-dd");
					vinfo.datevalue = df.parse( value );
				}	
				else {
					vinfo.datevalue = value;
				}
			}
			else if(o.datatype == "string"){
				vinfo.stringvalue = value.toString();
			}
		}
		return vinfo;
	}


}