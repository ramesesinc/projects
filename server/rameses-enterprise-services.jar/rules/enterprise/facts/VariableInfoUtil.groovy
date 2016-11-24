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
		def vinfo = null;
		if(o.datatype == 'decimal') {
			vinfo = new DecimalInfo();
			if(value!=null) value = (""+value).toDouble();
		}
		else if(o.datatype=="integer") {
			vinfo = new IntegerInfo();
			if(value!=null) value = (""+value).toInteger();
		}
		else if(o.datatype=="boolean") {
			vinfo = new BooleanInfo();
			if(value!=null) value = (""+value).toBoolean();
		}
		else {
			vinfo = new StringInfo();
		}
		vinfo.name = o.name;
		vinfo.caption = o.caption;
		vinfo.arrayvalues = o.arrayvalues;
		vinfo.category = o.category;
		vinfo.sortorder = o.sortorder;
		vinfo.value = value;
		return vinfo;
	}


}