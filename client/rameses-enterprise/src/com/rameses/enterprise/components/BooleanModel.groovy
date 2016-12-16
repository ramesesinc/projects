package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class BooleanYesNoModel extends ComponentBean {

    def trueValue;
    def falseValue;
    
    public Object getVal() {
        Object val = getValue();
        if(val == null ||  val.toString().trim().length() == 0) return null;
        else if( val == trueValue ) return true;
        else if( val == falseValue ) return false;
        else {
            return val.toString().toBoolean();
        }
    }
    
    public void setVal( Object value ) { 
        setValue( resolveValue( value) ); 
    } 
    
    private Object resolveValue( boolean o ) { 
        if( o == true ) { 
            return (trueValue? trueValue: o); 
        } else { 
            return (falseValue? falseValue: o); 
} 
    }
}
