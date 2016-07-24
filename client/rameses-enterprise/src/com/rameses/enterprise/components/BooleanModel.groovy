package com.rameses.enterprise.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class BooleanYesNoModel extends ComponentBean {

    def trueValue;
    def falseValue;
    
    private def _value; 
    
    public Object getVal() { 
        if( _value == true ) { 
            return (trueValue? trueValue: _value); 
        } else { 
            return (falseValue? falseValue: _value); 
        } 
    }
    
    public void setVal( Object value ) { 
        this._value = value; 
        setValue( getVal() ); 
    } 
}
